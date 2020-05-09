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
	}

	#leftNavi ul li{
		list-style: none;
	    height: 55px;
	    width: 150px;
	    font-size: 20px;
	    font-weight: bold;
	    margin-left: -20px;
	    padding: 12px;
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
	
</style>
</head>
<body>
	<jsp:include  page="sessionKeeper.html"/>
	<!-- 头 -->
	<jsp:include page="header.html"/>
	
	<div id="qrDiv" style="display:none">
		<img id="qrcodeImg"></img>
	</div>
	<div style="width:1328px;height:610px;margin:1px auto;">
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
			<div style='margin-top:75px;'>
				<button id='preMusicBtn' class='btn btn-sm btn-primary'>上一首</button>
				<button id='playMusicBtn' class='btn btn-sm btn-success'>播放</button>
				<button id='nextMusicBtn' class='btn btn-sm btn-primary'>下一首</button>
			</div>
		</div>

		<div id="diffPage" align="center" style="width:1136px;height:100%;float:left;border:2px solid green;margin-left:2px;">
			<iframe id="iframePage" width="1130px" height="605px;"></iframe>
		</div>
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
	<jsp:include page="statusBar.html"/>

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
		
		//-------------------------------音乐播放器全局变量----------------------------------------------------
		var tableObj;
		var inputObjArr;
		var trObjArr;
		var playerObj;
		var songNameObj;
		var palyBtnObj;
		var musicContentWindowObj;
		function initMusicPlayer(){
			console.log("init music player!")
			tableObj = getIframeElement("musicTable");
			trObjArr = $(tableObj).find("tr");          
			inputObjArr = $(tableObj).find("input");    //输入框控件
			playerObj = getIframeElement("player");     //播放
			songNameObj = getIframeElement("songName"); //歌曲名称label
			palyBtnObj = getIframeElement("btnPlay"); //播放按钮
			musicContentWindowObj = document.getElementById("iframePage").contentWindow;
			
			console.log("window:"+musicContentWindowObj + "  "+musicContentWindowObj.hasOwnProperty("play"));
			bindMusicEvent();
			bindMusicController();
		}
		
		bindMusicController();
		
		function bindMusicController(){
			$("#preMusicBtn").click(function(){
				musicContentWindowObj.preMusic();
			});

			$("#playMusicBtn").click(function(){
				musicContentWindowObj.playMusic();
			});
			
			$("#nextMusicBtn").click(function(){
				musicContentWindowObj.nextMusic();
			});
		}
		
		function bindMusicEvent(){
			// 绑定事件
			Media = getIframeElement("player")[0];
			musicContentWindowObj = document.getElementById("iframePage").contentWindow;
			playMode = 1
			var eventTester = function(e){
		        Media.addEventListener(e,function(){
		            if (e == "ended"){
		            	if (playMode == 0){
		            		// 单曲循环
		            		musicContentWindowObj.playMusic();
		            	}else if (playMode == 1){
		            		//顺序播放
		            		musicContentWindowObj.nextMusic();
		            	}else if (playMode == 2){
		            		//随机播放
		            		musicContentWindowObj.randomMusic();
		            	}
		            }
		            else if (e == "error"){
		            	if (playMode== 1 || playMode == 2){
		            		musicContentWindowObj.nextMusic();
		            	}
		            }
		        });
		    }

		    eventTester("loadstart");    //客户端开始请求数据
		    eventTester("progress");    //客户端正在请求数据
		    eventTester("suspend");        //延迟下载
		    eventTester("abort");        //客户端主动终止下载（不是因为错误引起），
		    eventTester("error");        //请求数据时遇到错误
		    eventTester("stalled");        //网速失速
		    eventTester("play");        //play()和autoplay开始播放时触发
		    eventTester("pause");        //pause()触发
		    eventTester("loadedmetadata");    //成功获取资源长度
		    eventTester("loadeddata");    //
		    eventTester("waiting");        //等待数据，并非错误
		    eventTester("playing");        //开始回放
		    eventTester("canplay");        //可以播放，但中途可能因为加载而暂停
		    eventTester("canplaythrough"); //可以播放，歌曲全部加载完毕
		    eventTester("seeking");        //寻找中
		    eventTester("seeked");        //寻找完毕
		    eventTester("timeupdate");    //播放时间改变
		    eventTester("ended");        //播放结束
		    eventTester("ratechange");    //播放速率改变
		    eventTester("durationchange");    //资源长度改变
		    eventTester("volumechange");    //音量改变
		}
		
		//获取iframe中的元素
		function getIframeElement(eleName){
			var obj = $("#iframePage").contents().find("#"+eleName);
			return obj;
		}
	</script>
</body>
</html>
