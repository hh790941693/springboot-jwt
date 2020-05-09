<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>登录失败</title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
	<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>	
	<style type="text/css">
		#mainDiv{
		    width: 470px;
		    height: auto;
		    margin: 100px auto;
		    border: 2px solid green;
		}
		
		table{
			width:100%;
			margin-top:2px;
		}
		
		table th{
			background-color:green;
			font-weight:bold;
			color:white;
			text-align:center;
		}
		
		table td{
			border:2px solid green;
		}
	</style>
</head>
<body>

<div id="mainDiv">
	<table>
		<tr><th style="width:40%">用户名</th><th>登录失败原因</th></tr>
		<tr style="width:60%">
			<td>${user}</td>
			<td>${detail}</td>
		</tr>
	</table>
	
	<div align="center" style='font-size:20px;margin-top:10px;color:red;'>
		<span id='timeSpan' style='color:blue;'>5</span><span>秒后返回登录页面</span>
	</div>
	
	<div align="center" style="margin:10px auto;">
		<button onclick="toIndex()" class='btn btn-sm btn-primary'>返回登录</button>
	</div>
	
	<jsp:include  page="sessionKeeper.html"/>
	
	<script type="text/javascript">
		var i=5;
		var timer = setInterval(function(){
			$("#timeSpan").text(i);
			if (i==0){
				clearInterval(timer);
				toIndex();
			}
			i=i-1;
		},1000);	
	
		function toIndex(){
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/logout.do',
		        dataType: 'json',
		        data:{user:parent.s_user},
		        async:false,
		        success: function(result) {
					console.log(result);
					window.location.href = rootUrl+"ws/login.page";
		        }
			});
		}
	</script>
</div>
</body>
</html>
