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
<script type="text/javascript" src="<%=path%>/viewjs/common.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style type="text/css">
	#headerDiv{
		width:1320px;
		height:55px;
		font-weight:bold;
		margin:1px auto;
	}

	#btnDiv button{
		margin-top: 15px;
	}
	
	#curTimeSpan{
		margin-left:20px;
		font-size:18px;
	}
</style>
</head>
<body>
	<jsp:include  page="sessionKeeper.jsp"/>

	<div id="headerDiv" align="center">
		<div style="float:left;">
			<img id='myImg' alt=""  width="48" height="48">
			你好:<label id="curUser" style="color:blue;font-size:30px;"></label>
			<span id="curTimeSpan">0000-00-00 00:00:00</span>
		</div>
		<div style="float:left;margin-left:300px;font-size:30px;">
			<label id="titleLabel" style="color:black;"></label>
		</div>		
		<div id="btnDiv" style="float:right">
			<button id="showQRCodeBtn" class='btn btn-sm btn-primary'>查看二维码</button>		
			<button id="exitBtn" class='btn btn-sm btn-danger' style="margin-left:10px;" onclick="toIndex()">退&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;出</button>
		</div>
	</div>	
	
	<script type="text/javascript">
		if (s_user == "admin"){
			if (s_img == ""){
				$("#myImg").attr("src",rootUrl+"img/headimg/admin.jpg");
			}else{
				$("#myImg").attr("src",rootUrl+"img/headimg/"+s_img);
			}
		}else{
			$("#myImg").attr("src",rootUrl+"img/headimg/"+s_img);
		}
		
		var curTimeTemp = getCurrentTime();
		$("#curTimeSpan").text(curTimeTemp);
		
		setInterval(function(){
			var curTime = getCurrentTime();
			$("#curTimeSpan").text(curTime);
		},1000)
		
		function toIndex(){
			window.location.href = rootUrl+"ws/login.page?user="+parent.s_user;
			//$.ajax({
		    //  	type: 'POST',
		    //    url: rootUrl+'ws/logout.do',
		    //    dataType: 'json',
		    //    data:{user:parent.s_user},
		    //    async:false,
		    //    success: function(result) {
			//		console.log(result);
			//		window.location.href = rootUrl+"ws/login.page";
		    //    }
			//});
		}
		
		$("#showQRCodeBtn").click(function(){
			var imgSrc = $("#qrcodeImg").attr("src");
			if (imgSrc == "" || imgSrc == undefined){
				$.ajax({
			      	type: 'POST',
			        url: rootUrl+'ws/showQRCode.do',
			        dataType: 'json',
			        async:false,
			        success: function(result) {
			        	$("#qrDiv").css("display","block");
			        	$("#qrcodeImg").css("display","block");
			        	$("#qrcodeImg").attr("src",result);
			        }
				});
			}else{
				$("#qrcodeImg").attr("src","");
				$("#qrDiv").css("display","none");
				$("#qrcodeImg").css("display","none");
			}
		})		
	</script>
</body>
</html>
