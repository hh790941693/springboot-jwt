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
<script type="text/javascript" src="<%=path%>/static/viewjs/common.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style type="text/css">
	body { background: purple url(<%=path%>/img/pic2.jpg); background-repeat:no-repeat;background-size:cover;
	};
	
	.mouseleftclick{
		background-color:blue;
		color:white;
	}
	
	#mainDiv{
		width:604px;
		margin:20px auto;
	}
	
	#header{
		width:604px;
		height:40px;
		padding-top:5px;
		padding-left:5px;
		background-color:#365e71;
	}
	
	#msgFromServer{
		width:604px;
		height:350px;
		overflow:auto;
		border:2px solid green;
		background-color:white;
		padding:3px;
	}
	
	#footer{
		width:100%;
	    border-left:2px solid green;
	    border-right:2px solid green;
	    border-bottom:2px solid green;
	}
	
	#adDiv{
		width:350px;
		height:250px;
		position:fixed;
		bottom:10px;
		right:10px;
		border:3px solid green;
		background-color:white;
		over-flow:hidden;
		display:none;
	}
	
	#msgFromServer p{
		margin-bottom:7px;
	}
</style>
</head>
<body>
	<div id="mainDiv">
		<div id="header">
			<div align="left" style="width:20%;height:100%;float:left">
				<span style="color:yellow;font-weight:bold;height:100%;display:inline-block;" id="loginUser"></span>
			</div>
			
			<div align="left" style="width:33%;height:100%;color:white;float:left">
				<span id="onlineCount" style='height:100%;display:inline-block;'>在线0名</span>
				<button id="refreashConlineCount" class='btn btn-sm btn-primary' style='height:100%;display:inline-block;' onclick="getOnlineInfo()">刷新</button>
			</div>

			<div align="right" style="width:45%;height:100%;float:right;margin-right:3px;">
				<button class='btn btn-sm btn-primary' onclick="changeBackgroundImage()">更换背景</button>&nbsp;&nbsp;
				<button class='btn btn-sm btn-danger' onclick="clearScreen()">清&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;屏</button>&nbsp;&nbsp;
				<button id="btnReconnect" class='btn btn-sm btn-primary' type="button" onclick="loginServer()">重新连接</button>
			</div>
		</div>
	
		<div id="msgFromServer" align="left">
		</div>
		
		<div id="footer" align="left">
			<div style="height:25px;">
				<select id="selectUser" style="width:50%;height:100%;float:left;"></select>
				<select id="selectCyy" style="width:50%;height:100%;float:right;background-color:green;color:white;" 
					onchange="document.getElementById('msgToServer').value=this.options[this.selectedIndex].value">
				</select>
			</div>
			<div style="height:33px;">
				<input id="msgToServer"  style="width:80%;float:left;height:100%;border:2px solid grey" placeholder="请输入聊天内容"></input>
				<button id="send" type="button" class='btn btn-sm btn-primary' style="width:20%;height:100%;float:right;" onClick="send()">&nbsp;&nbsp;发&nbsp;&nbsp;&nbsp;&nbsp;送&nbsp;&nbsp;</button>
			</div>
		</div>
	</div>

	<%--广告页面 --%>
	<div id="adDiv">
		<div id="adBtn" style="style:1px solid red;background-color:#5082b9;padding-left:290px">
			<button id="closeAd" class='btn btn-sm btn-primary' onclick="closeAd()">关&nbsp;&nbsp;闭</button>
		</div>
		<div id="adTitle" style="background-color:2px solid black;height:30px;text-align:center;font-size:20px;">
		</div>
		
		<div id="adContent" style="background-color:2px solid red;height:auto;text-indent:2em;">
		</div>
	</div>

	<script type="text/javascript">
		//var webSocket = parent.webSocket;
		var user = parent.user;
		var pass = parent.pass;
		var webserverip = parent.webserverip;
		var webserverport = parent.webserverport;
		//当前用户的状态信息
		var currentUserOnlineInfo = undefined;
		if (null != parent.webSocket){
			$("#loginUser").html(user);
		}
		
		function loginServer(){
			parent.loginServer();
		}
		
		$("#msgFromServer").val("");
		$("#msgFromServer").append(parent.chatRecord);
		parent.unreadCount=0;
		$("#unreadCountSpan", window.parent.document).text("");
		
		// 发送消息给服务器
		function send()
		{
			if (currentUserOnlineInfo != undefined){
	        	// 是否被禁用
	        	var enableStatus = currentUserOnlineInfo.enable;
	        	if (enableStatus == "0"){
	        		//被禁用了
	        		console.log("你的账号已被禁用了");
	        		return;
	        	}
				
				var speakStatus = currentUserOnlineInfo.speak;
				if (speakStatus == "0"){
					//被禁言了
					console.log("你的已被禁言了");
					return;
				}
			}
			
			var toUser = $("#selectUser").val();
			if (parent.webSocket != null)
			{
				if (parent.user == toUser)
				{
					document.getElementById("msgFromServer").innerHTML +="<p style='color:red;font-size:10px;line-height:10px;'>[系统信息] 自己不能给自己发消息!</p>";
					return false;
				}
				
				var msg = document.getElementById("msgToServer").value;
				if (msg == null || msg == "")
				{
					var msg = "<p style='color:red;font-size:10px;line-height:10px;'>[系统信息] 不允许发送空消息!</p>";
					var textArea = $("#msgFromServer").append(msg);
					parent.chatRecord = parent.chatRecord + msg;
					textArea.scrollTop(textArea[0].scrollHeight - textArea.height());
					return;	
				}
				
				var htmlData = "<p style='color:blue;font-size:10px;line-height:10px;'>["+getCurrentTime() + "] [在线消息]  [我 -->" + toUser +"] "+ msg +"</p>";
				if (toUser == null || toUser == "")
				{
					var msg = "<p style='color:red;font-size:10px;line-height:10px;'>[系统信息] 你暂时没有好友,快去<a href='javascript:void(0)' onclick='toAddUser()'>添加用户</a>吧!</p>";
					var textArea = $("#msgFromServer").append(msg);
					parent.chatRecord = parent.chatRecord + msg;
					textArea.scrollTop(textArea[0].scrollHeight - textArea.height());
				}
				else
				{
					var textArea = $("#msgFromServer").append(htmlData);
					parent.chatRecord = parent.chatRecord + htmlData;
					textArea.scrollTop(textArea[0].scrollHeight - textArea.height());
					sendMsgToServer(parent.user, toUser, msg);
					$("#msgToServer").val("");
				}
			}
			else
			{
				var msg = "<p style='color:red;font-size:10px;line-height:10px;'>[系统信息] 服务器已断开,请<a href='javascript:void(0)' onclick='loginServer()'>重新连接</a>!</p>";
				var textArea = $("#msgFromServer").append(msg);
				parent.chatRecord = parent.chatRecord + msg;
				textArea.scrollTop(textArea[0].scrollHeight - textArea.height());
			}
		}
		
		function sendMsgToServer(from,to,msg)
		{
			if (parent.connectStatus == "1" && parent.webSocket != null){
				var toSerMsg = "typeId:2;typeDesc:在线消息;from:" + from+";to:"+ to+";msg:"+msg;
				parent.webSocket.send(toSerMsg);
			}else{
				var msg = "<p style='color:red;font-size:10px;line-height:10px;'>[系统信息] 连接失败,请<a href='javascript:void(0)' onclick='loginServer()'>重新连接</a>!</p>";
				var textArea = $("#msgFromServer").append(msg);
				parent.chatRecord = parent.chatRecord + msg;
				textArea.scrollTop(textArea[0].scrollHeight - textArea.height());
			}
		}
		
		function logout(){
			window.parent.toIndex();
		}

		// 获取在线人数信息
		getOnlineInfo();
		// {"count":2,"users":"usr1,usr2"}
		function getOnlineInfo() {
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/getOnlineInfo.json',
		        data:{user:parent.user},
		        dataType: 'json',
		        success: function(result) {
		        	var jsonObj = result;
		        	currentUserOnlineInfo = jsonObj.currentOnlineUserInfo;
		        	// 是否被禁用
		        	var enableStatus = currentUserOnlineInfo.enable;
		        	if (enableStatus == "0"){
						layer.tips('该账户已被禁用,10秒后强制退出!', '#msgFromServer', {
							tips: [2, 'red']
							});
		        		setTimeout(logout,10000);
		        		return;
		        	}
		        	
		        	// 是否被禁言
		        	var speakStatus = currentUserOnlineInfo.speak;
		        	if (speakStatus == "0"){
		        		$("#send").attr("disabled",true);
		        		$("#send").html("你已被禁言");
		        	}
		        	else
		        	{
		        		$("#send").attr("disabled",false);
		        		$("#send").html("发送");
		        	}
		        	
		        	// 聊天常用语
		        	//var cyy = jsonObj.dicMap.cyy;
		        	var cyy = jsonObj.commonMap.cyy;
		        	$("#selectCyy").empty();
		        	for (var x=0;x<cyy.length;x++)
		        	{
		        		$("#selectCyy").append("<option value=" + cyy[x].name + ">" + cyy[x].name+ "</option>")
		        	}
		        	
		        	// 在线人数
		        	var count = jsonObj.onlineCount;
		        	// 好友列表
		        	var usersList = jsonObj.friendsList;
		        	
		        	$("#onlineCount").html("在线"+count+"名");
		        	var selectId = $("#selectUser");
		        	var selectUser = selectId.val();
		        	selectId.empty();
		        	if (usersList != "")
		        	{
		        		if (selectUser == null || selectUser == undefined)
		        		{
		        			for (var i=0;i<usersList.length;i++)
		        			{
		        				if (usersList[i].name == user)
		        				{
		        					continue;
		        				}
		        				selectId.append("<option value=" + usersList[i].name + ">" + usersList[i].name+ "</option>");
		        			}
		        		}
		        		else
		        		{
		        			selectId.append("<option value=" + selectUser + ">" + selectUser+ "</option>")
		        			for (var i=0;i<usersList.length;i++)
		        			{
		        				var nameTmp = usersList[i].name;
		        				var enableTmp = usersList[i].enable;
		        				if (nameTmp == selectUser || nameTmp == user || enableTmp=="0")
		        				{
		        					continue;
		        				}
		        				var state = usersList[i].state;
		        				if (state == "0")
		        				{
		        					selectId.append("<option style='color:grey;' value=" + nameTmp + ">" + nameTmp+ "</option>");
		        				}
		        				else
		        				{
		        					selectId.append("<option style='color:#FF0000;' value=" + nameTmp + ">" + nameTmp+ "</option>");
		        				}
		        			}
		        		}
		        	}
		        }
		    });
		}

		//setInterval(function(){
		//	getOnlineInfo();
		//},10000);
		
		var imgIndex=1;
		function changeBackgroundImage(){
			if (imgIndex>5)
			{
				imgIndex=1;	
			}
			$("body").css("background-image","url(<%=path%>/img/pic"+imgIndex+".jpg)");
			imgIndex++;
		}
		setInterval(function(){
			changeBackgroundImage();
		},60000);
		
		$(document).keyup(function(event){
			  if(event.keyCode ==13){
			   	send();
			  }
			});

		function closeAd(){
			$("#adDiv").slideUp(500);
		}
		
		function toAddUser(){
			var cur_pageid = "addFriends.page";
			parent.cur_pageid = cur_pageid;
			parent.showPage(cur_pageid);
			$("#leftNavi ul li",window.parent.document).removeClass("mouseleftclick");
			$("#"+cur_pageid,window.parent.document).addClass("mouseleftclick");
		}
		
		//清屏
		function clearScreen(){
			parent.chatRecord = "";
			$("#msgFromServer").text("");
		}		
	</script>
</body>
</html>
