<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>WebSocket服务端</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style type="text/css">
	#leftNavi ul{
		padding-left:30px;
	}
	
	#leftNavi>ul{
		height:490px;
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
    	margin-left: 5px;	
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
	<!-- 头 -->
	<jsp:include page="header.html"/>
	
	<div id="qrDiv" style="display:none">
		<img id="qrcodeImg"></img>
	</div>
	<div id='mainDiv' style="width:1328px;height:610px;margin:1px auto;position:relative;">
		<div id="leftNavi" style="width:180px;height:100%;float:left;border:2px solid green">
			<ul>
				<li id="wsserverUser.page" class="mouseleftclick">用户管理</li>
				<li id="wsserverChartLog.page">聊天记录管理</li>
				<li id="wsserverOperationLog.page">操作日志管理</li>
				<li id="wsserverOperationLogByBootstrap.page">操作日志管理2</li>
				<li id="wsserverChartLogMonitor.page">聊天监控<span id="unreadCountSpan"></span></li>
				<li id="configurationLi">配置</li>
				<li id="../file/musicPlayer.page?user=admin">音乐播放</li>
				<li id="wsclientCircle.page?user=admin">朋友圈</li>
				<li id="ad.page">广告发布</li>
				<li id="../game/gameIndex.page">游戏娱乐</li>
				<li id="../canvas/canvasIndex.page">2D画图</li>
			</ul>
			
			<div id='configurationDiv' style='display:none;position:absolute;left:-165px;top:100px;border:2px solid green;'>
				<ul>
					<li id="wsserverCommon.page?type=mgc">敏感词配置</li>
					<li id="wsserverCommon.page?type=zh" >脏话配置</li>
					<li id="wsserverCommon.page?type=cyy">常用语配置</li>
					<li id="wsserverCommon.page?type=zcwt">注册问题配置</li>
				</ul>
			</div>
		</div>
		
		<div id="diffPage" align="center" style="width:1136px;height:100%;float:left;border:2px solid green;margin-left:2px;">
			<iframe id="iframePage" width="1130px" height="605px"></iframe>
		</div>
	</div>
	
	<div id='musicIframeDiv'>
		<div id='musicIframeHeader'>
			<span style='display:inline-block;width:65%;'>&nbsp;&nbsp;按住我拖动&nbsp;&nbsp;</span>
			<button id='resetMusicIframeDivPosBtn' style='display:inline-block;width:30%;'>复位</button>
		</div>
		<iframe id="musicIframePage" width="100%" height="100%" src="../file/musicPlayerSimple.page"></iframe>
	</div>
	
	<jsp:include  page="sessionKeeper.html"/>
	<!-- 尾 -->
	<jsp:include page="footer.jsp"/>
	<jsp:include page="statusBar.jsp"/>

	<script type="text/javascript">
		var cur_user=s_user;
		$("#curUser").text(cur_user);
		$("#titleLabel").text("websocket服务端");

		var curPageid = "wsserverUser.page";
		showPage(curPageid);
		function showPage(pageId){
			if (pageId.indexOf("gameIndex.page") != -1){
				window.location.href=pageId;
			}else if (pageId.indexOf("canvasIndex.page") != -1){
				window.location.href=pageId;
			}else if (pageId.indexOf("configurationLi") != -1){
				$("#configurationDiv").slideDown();
			}else{
				$("#iframePage").attr("src",pageId);
			}
		}
		
		$("#leftNavi ul li").click(function (){
			curPageid=this.id;
			showPage(curPageid);
			$("#leftNavi ul li").removeClass("mouseleftclick");
			$(this).addClass("mouseleftclick");
			
			var parentObj=$(this).parent().parent();
			var parentDivId=$(parentObj).attr("id");
			if (curPageid.indexOf("configurationLi") == -1 && parentDivId == "leftNavi"){
				$("#configurationDiv").slideUp();
			}
		})
		
		$("#leftNavi ul li").hover(function(){
			$(this).removeClass("mousemoveout");
			$(this).removeClass("mousemoveon");
			$(this).addClass("mousemoveon");
		},function(){
			$(this).removeClass("mousemoveout");
			$(this).removeClass("mousemoveon");
			$(this).addClass("mousemoveout");
		})
		
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
				showStr="["+curTime+"] ["+typeDesc+"] ["+msgFrom+"-->"+msgTo+"] " +msg;
			}			
			else if (typeId == 3)
			{
				//离线消息
				defaultCss = "color:grey;font-size:10px;line-height:10px;";
				showStr="["+curTime+"] ["+typeDesc+"] ["+msgFrom+"-->"+msgTo+"] " +msg;
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
			
			if (curPageid != "wsserverChartLogMonitor.page"){
				var htmlData = "<p style='" + defaultCss + "'>"+showStr +"</p>";
				chatRecord = chatRecord + htmlData;
				unreadCount = unreadCount+1;
				$("#unreadCountSpan").text(unreadCount);
			}else{
				var htmlData = "<p style='" + defaultCss + ";display:inline-block;'>"+showStr +"</p>";
				htmlData += "<button onclick='enableSpeak("+msgFrom+",0)' style='display:inline-block;'>禁言</button>";
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
		
		//获取iframe中的元素
		function getIframeElement(eleName){
			var obj = $("#iframePage").contents().find("#"+eleName);
			return obj;
		}
		
		function stop(){
			if (webSocket != null)
			{
				webSocket.close();
				webSocket = null;
			}
		}

		function refrshMusic(){
			document.getElementById("musicIframePage").contentWindow.showMusic();
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
