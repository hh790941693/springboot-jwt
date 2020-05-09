<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>WebSocket客户端</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style type="text/css">
	#leftNavi ul{
		padding-left:30px;
		height:495px;
	}

	#leftNavi ul li{
	    list-style: none;
	    height: 40px;
	    width: 150px;
	    font-size: 20px;
	    font-weight: bold;
	    margin-left: -20px;
	    padding: 4px;
	    cursor: pointer;
	}
	#leftNavi ul a{
		text-decoration:none;
	}
	
	.mousemoveon{
		background-color:grey;
		color:white;
	}
	.mousemoveout{
		background-color:white;
		color:black;
	}
	.mouseleftclick{
		background-color:blue;
		color:white;
	}
	#qrDiv{
		position: fixed;
		right: 5px;
	    top: 40px;
	    width: 180px;
	    height: 180px;
	}
	#qrcodeImg{
		width: 160px;
	    height: 160px;
	}
	
	#unreadCountSpan{
    	color: red;
   	 	font-weight: bold;
    	margin-left: 10px;
    	font-size: 18px;
    	margin-left: 45px;	
	}
	
    #musicIframeDiv {
    	position: absolute;
    	width:175px;
    	height:100px;
   	 	background: black;
    	color：white;
    }
    #musicIframeHeader {
    	background: #ddd;
    	cursor: move;
    	font-size:10px;
    }	
</style>
</head>
<body>
	<jsp:include  page="sessionKeeper.jsp"/>
	<!-- 头 -->
	<jsp:include page="header.jsp"/>
	
	<div id="qrDiv" style="display:none">
		<img id="qrcodeImg"></img>
	</div>
	<div id='mainDiv' style="width:1328px;height:610px;margin:1px auto;">
		<div id="leftNavi" style="width:180px;height:100%;float:left;border:2px solid green">
			<ul>
				<li id="wsclientChat.page" class="mouseleftclick">聊天<span id="unreadCountSpan"></span></li>
				<li id="../file/musicPlayer.page">音乐播放器</li>
				<li id="../canvas/canvasIndex.page">游戏娱乐</li>
				<li id="addFriends.page">添加好友</li>
				<li id="friendsList.page">我的好友</li>
				<li id="myApply.page">我的申请记录</li>
				<li id="friendsApply.page">好友申请记录</li>
				<li id="wsclientCircle.page">朋友圈</li>
				<li id="setPersonalInfo.page">设置个人信息</li>
			</ul>
		</div>

		<div id="diffPage" align="center" style="width:1136px;height:100%;float:left;border:2px solid green;margin-left:2px;">
			<iframe id="iframePage" width="1130px" height="605px;"></iframe>
		</div>
	</div>
	
	<div id='musicIframeDiv'>
		<div id='musicIframeHeader'>
			<span style='display:inline-block;width:65%;'>&nbsp;&nbsp;按住我拖动&nbsp;&nbsp;</span>
			<button id='resetMusicIframeDivPosBtn' style='display:inline-block;width:30%;'>复位</button>
		</div>
		<iframe id="musicIframePage" width="100%" height="100%" src="../file/musicPlayerSimple.page"></iframe>
	</div>	
	
	<%--广告页面 --%>
	<div id="adDiv" style="position:fixed;bottom:10px;right:10px;width:350px;height:250px;border:3px solid green;background-color:white;over-flow:hidden;display:none">
		<div id="adBtn" style="style:1px solid red;background-color:#5082b9;padding-left:290px">
			<button id="closeAd" class='btn btn-sm btn-primary' onclick="closeAd()">关&nbsp;&nbsp;闭</button>
		</div>
		<div id="adTitle" style="background-color:2px solid black;height:30px;text-align:center;font-size:20px;">
		</div>
		
		<div id="adContent" style="background-color:2px solid red;height:auto;text-indent:2em;">
		</div>
	</div>
	
	<jsp:include page="footer.jsp"/>
	<jsp:include page="statusBar.jsp"/>

	<script type="text/javascript">
		$("#curUser").text(s_user);
		$("#titleLabel").text("websocket客户端");
		
		// 显示聊天窗口首页
		var cur_pageid = "wsclientChat.page";
		showPage(cur_pageid);
		function showPage(pageId)
		{
			var url = pageId + "?user="+s_user;
			$("#iframePage").attr("src",url);
		}
		
		$("#leftNavi ul li").click(function (){
			cur_pageid = this.id;
			showPage(cur_pageid);
			$("#leftNavi ul li").removeClass("mouseleftclick");
			$(this).addClass("mouseleftclick");
		})
		
		$("#leftNavi ul li").hover(function(){
			$(this).removeClass("mousemoveout");
			$(this).removeClass("mousemoveon");
			$(this).addClass("mousemoveon");
		},function(){
			//$(this).css({"background-color":"white","color":"black"});
			$(this).removeClass("mousemoveout");
			$(this).removeClass("mousemoveon");
			$(this).addClass("mousemoveout");
		});
		
		
		// ---------------------------------以下是供聊天用的全局变量-----------------------
		var webSocket = null;
		var user = s_user;
		var pass = s_pass;
		var webserverip = s_webserverip;
		var webserverport = s_webserverport;
		var userAgent = s_userAgent;
		
		// 连接状态
		var connectStatus = "0"
		var checkCount = 1;
		var checkMaxCount = 20;
		var checkTimer;
		
		//所有聊天记录
		var chatRecord="";
		//未读消息条数
		var unreadCount=0;
		function checkConnection()
		{
			if (connectStatus == "0")
			{
				if (checkCount >= checkMaxCount)
				{
					if (checkTimer != null && checkTimer != "" && checkTimer != undefined)
					{
						clearInterval(checkTimer);
					}
				}
				var msgFromServerObj = getIframeElement("msgFromServer");
				var msg = "<p style='color:red;font-size:10px;line-height:10px;'>[系统信息] 正在连接服务器" +webserverip+":"+webserverport + " [ "+checkCount +" ]"+ "次 请稍等...</p>";
				$(msgFromServerObj).append(msg);
				chatRecord = chatRecord + msg;
				showTips("正在连接服务器" +webserverip+":"+webserverport + " [ "+checkCount +" ]"+ "次 请稍等...","blue");
				checkCount++;
			}
			else
			{
				if (checkTimer != null && checkTimer != "" && checkTimer != undefined)
				{
					clearInterval(checkTimer);
				}
			}
		}
		checkTimer = setInterval(function(){
			checkConnection();
		},1000);
		

		checkConnection();
		loginServer();
		
		function loginServer()
		{
			if (webSocket == null)
			{
				webSocket = new WebSocket("ws://"+webserverip+":"+webserverport+"/zhddkk/zhddWebSocket/" + user+"/"+pass+"/"+userAgent);
				webSocket.onerror = function(event){
					onError(event);
				};
				webSocket.onclose = function(event){
					OnClose(event);
				};
				webSocket.onopen = function(event){
					onOpen(event);
				};
				webSocket.onmessage = function(event){
					onMessage(event);
				};
			}
		}
		
		function onOpen(event){
			connectStatus = "1";
			var msgFromServerObj = getIframeElement("msgFromServer");
			var msg = "<p style='color:red;font-size:10px;line-height:10px;'>[系统信息] 已连上服务器!</p>";
			$(msgFromServerObj).append(msg);
			showTips("已连上服务器!","green");
			chatRecord = chatRecord + msg;
		}
		
		function onError(event){
			var msgFromServerObj = getIframeElement("msgFromServer");
			var msg = "<p style='color:red;font-size:10px;line-height:10px;'>[系统信息] 连接服务器异常!</p>";
			$(msgFromServerObj).append(msg);
			showTips("连接服务器异常!","red");
			chatRecord = chatRecord + msg;
			connectStatus = "0";
			webSocket = null;
		}
		
		function OnClose(event){
			var msgFromServerObj = getIframeElement("msgFromServer");
			var msg = "<p style='color:red;font-size:10px;line-height:10px;'>[系统信息] 客户端已断开!</p>";
			$(msgFromServerObj).append(msg);
			showTips("客户端已断开!","red");
			chatRecord = chatRecord + msg;
			connectStatus = "0";
			stop();
			//loginServer();
		}
		
		function onMessage(event){
			var data = event.data;
			var curTime = data.split("curTime:")[1].split(";")[0];
			var msgFrom = data.split("from:")[1].split(";")[0];
			var msgTo = data.split("to:")[1].split(";")[0];
			var typeId = data.split("typeId:")[1].split(";")[0];
			var typeDesc = data.split("typeDesc:")[1].split(";")[0];
			var msg = data.split("msg:")[1];
			
			var showStr="["+curTime+"] ["+typeDesc+"] ["+msgFrom+"-->"+msgTo+"] "+msg;
			var defaultCss = "color:red;font-size:10px;line-height:10px;"
			
			if (typeId == 1)
			{
				//系统消息
				defaultCss = "color:red;font-size:10px;line-height:10px;";
				showStr="["+curTime+"] ["+typeDesc+"] " +msg;
			}
			else if (typeId == 2)
			{
				//在线消息
				defaultCss = "color:green;font-size:10px;line-height:10px;";
				showStr="["+curTime+"] ["+typeDesc+"] ["+msgFrom+"-->我] " +msg;
			}			
			else if (typeId == 3)
			{
				//离线消息
				defaultCss = "color:grey;font-size:10px;line-height:10px;";
				showStr="["+curTime+"] ["+typeDesc+"] [我-->"+msgTo+"] " +msg;
			}
			else if (typeId == 4)
			{
				//广告消息
				defaultCss = "color:blue;font-size:10px;line-height:10px;";
				var title = msg.split("title:")[1].split(";")[0];
				var content = msg.split("content:")[1];
				showStr="["+curTime+"] ["+typeDesc+"] ["+msgFrom+"-->"+msgTo+"] "+"广告标题:"+title+" 广告内容:"+content;
			}			
			else if (msgTo==s_user)
			{
				defaultCss = "color:green;font-size:10px;line-height:10px;";
			}
			else if (msgFrom==s_user)
			{
				defaultCss = "color:blue;font-size:10px;line-height:10px;";
			}
			
			if (cur_pageid != "wsclientChat.page"){
				//$("#adTitle").html("来自"+msgFrom+"的消息");
				//$("#adContent").html(msg);
				//$("#adDiv").slideDown(500);
				var htmlData = "<p style='" + defaultCss + "'>"+showStr +"</p>";
				chatRecord = chatRecord + htmlData;
				unreadCount = unreadCount+1;
				$("#unreadCountSpan").text(unreadCount);
			}else{
				var htmlData = "<p style='" + defaultCss + "'>"+showStr +"</p>";
				var msgFromServerObj = getIframeElement("msgFromServer");
				chatRecord = chatRecord + htmlData;
				var textArea = $(msgFromServerObj).append(htmlData);
				textArea.scrollTop(textArea[0].scrollHeight - textArea.height());
			}
			
			if (typeId == 4){
				var title = msg.split("title:")[1].split(";")[0];
				var content = msg.split("content:")[1];
				
				$("#adTitle").html(title);
				$("#adContent").html(content);
				$("#adDiv").slideDown(500);
			}
		}

		function stop()
		{
			if (webSocket != null)
			{
				webSocket.close();
				webSocket = null;
			}
		}
		
		function closeAd(){
			$("#adDiv").slideUp(500);
		}
		
		function refrshMusic(){
			document.getElementById("musicIframePage").contentWindow.showMusic();
		}
		
		//获取iframe中的元素
		function getIframeElement(eleName){
			var obj = $("#iframePage").contents().find("#"+eleName);
			return obj;
		}
		
		$("#resetMusicIframeDivPosBtn").click(adjustMusicWindowPosition);
		
		function adjustMusicWindowPosition(){
			var headDivHeight = $("#headerDiv").height();
			var mainDivHeight = $("#mainDiv").height();
			var mainDivLeft = $("#mainDiv").offset().left;
			var musicIframeDivHeight=$("#musicIframeDiv").height();
			$("#musicIframeDiv").css("top",headDivHeight+mainDivHeight-musicIframeDivHeight-25);
			$("#musicIframeDiv").css("left",mainDivLeft+3);
		}
		
		adjustMusicWindowPosition();
		//音乐播放器可拖动代码
        dragPanelMove("#musicIframeHeader","#musicIframeDiv");
        function dragPanelMove(downDiv,moveDiv){
            $(downDiv).mousedown(function (e) {
                  var isMove = true;
                  var div_x = e.pageX - $(moveDiv).offset().left;
                  var div_y = e.pageY - $(moveDiv).offset().top;
                  $(document).mousemove(function (e) {
                      if (isMove) {
                          var obj = $(moveDiv);
                          obj.css({"left":e.pageX - div_x, "top":e.pageY - div_y});
                      }
                  }).mouseup(
                      function () {
                      isMove = false;
                  });
            });
        }

        //窗口大小变化事件
        $(window).resize(function () {
        	adjustMusicWindowPosition();
        });        
	</script>
</body>
</html>
