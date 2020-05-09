<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
<title>聊天记录监控</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/viewjs/common.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style type="text/css">
	body { background: purple url(<%=path%>/img/pic3.jpg); background-repeat:no-repeat;background-size:cover;}

	#mainDiv{
		width:100%;
		height:91%;
		position:relative;
	}
	
	#btnDiv{
		position:absolute;
		right:155px;
		top:-33px;
	}
	
	#msgFromServer{
		background-color:#b3d7ff85;
		width:800px;
		height:500px;
		overflow:auto;
		border:2px solid green;
		margin:45px auto;
		padding:3px;
	}
	
	#msgFromServer p{
		margin-bottom:7px;
	}
	
</style>
</head>
<body>
	<div id="mainDiv">
		<div id='btnDiv' align='right'>
			<button class='btn btn-sm btn-primary' onclick="changeBackgroundImage()">更换背景</button>&nbsp;&nbsp;
			<button class='btn btn-sm btn-danger' onclick="clearScreen()">清&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;屏</button>&nbsp;&nbsp;
			<button class='btn btn-sm btn-primary' onclick="loginServer()">重新连接</button>
		</div>	
		<div id="msgFromServer">
		</div>
	</div>
	
	<script type="text/javascript">
		$("#msgFromServer").html("");
		$("#msgFromServer").append(parent.chatRecord);
		parent.unreadCount=0;
		$("#unreadCountSpan", window.parent.document).text("");
		
		//重新连接
		function loginServer(){
			parent.loginServer();
		}
		
		//清屏
		function clearScreen(){
			parent.chatRecord = "";
			$("#msgFromServer").text("");
		}
		
		//禁言/开言
		function enableSpeak(userName,enableVar){
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/enableSpeak.do',
		        data:{user:userName,speak:enableVar},
		        dataType: 'json',
		        success: function(result) {
		        	var detail = result;
		        	if (detail == "success"){
		        		layer.tips('禁言成功!', "body", {
			                 tips: [1, '#0FA6D8'], 
			                 tipsMore: false, 
			                 time:1000  
			             });
		        	}
		        }
			});	
		}
		
		//聊天背景图切换
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
	</script>
</body>
</html>
