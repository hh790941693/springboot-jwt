<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>

<html>
	<head>
		<meta charset="utf-8">
		<title>上传文件</title>
		<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
		<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
		<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
		<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>
		
		<style type="text/css">
			input[type="file"] {
				color: transparent;
			}
		</style>
	</head> 
	<body>
		<div id="addMusci" align="center" style="width:800px;height:100%;margin:5px auto;">
			<form action="uploadFiles.do" method="post" enctype="multipart/form-data" onsubmit="return verify()">
				<input type="text" name="user" value="${user}" hidden="true">
				<input type="text" name="fileType" value="${fileType}" hidden="true">
				文件:<input id="fileInput" type="file" name="files" multiple="multiple" accept="audio/mpeg">
				<input type="submit" class='btn btn-primary' value="添加歌曲">
			</form>
		</div>	
		
	</body>
	<script type="text/javascript">
			var result="${result}";

			function verify(){
				var uploadFiles=$("#fileInput").val();
				if (uploadFiles == ""){
					return false;
				}
				console.log("待上传的文件:"+uploadFiles);
				return true;
			}
			
			$(":submit").click(function(){
				var uploadFiles=$("#fileInput").val();
				//if (uploadFiles != ""){
				//	window.parent.refreashTimeout(4);
				//}
			});					
	</script>
</html>
