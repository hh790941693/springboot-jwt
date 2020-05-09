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
		<title>上传文件结果</title>
		<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
		<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
		<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
		<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>
		
		<style type="text/css">
			#resultDiv{
				width:100%;
				height:100%;
				border:2px solid green;
			}
			
			table tr td{
				text-align:center;
			}
			
			table{
				border:2px solid green;
				width:100%;
				height:auto;
			}
			
			table th{
				background-color:green;
				color:white;
				font-weight:bold;
				text-align:center;
			}
		</style>
	</head> 
	<body>
		<div id="resultDiv" align="center" style="margin:5px auto;">
			<table>
				<tr>
					<th width="43%">文件名</th>
					<th width="15%">上传结果</th>
					<th width="42%">失败原因</th>
				</tr>
			</table>
			<div align="center" style='margin:10px'>
				<button id='backToIndexBtn' class='btn btn-sm btn-primary' onclick='toMusicList()'>返回音乐列表</button>
			</div>
		</div>
		
	</body>
	<script type="text/javascript">
		var result=${result};
		var list = result.list;
		var html = "";
		for (var i=0;i<list.length;i++){
			var oneRecord = list[i];
			var filename = oneRecord.filename;
			var uploadFlag = oneRecord.uploadFlag;
			var uploadResult = oneRecord.uploadResult;
			var failedReason = oneRecord.failedReason;
			if (uploadFlag){
				html += "<tr><td>"+filename+"</td><td><span style='background-color:green;color:white;display: inline-block;width:110px;'>"+uploadResult+"</span></td><td>"+failedReason+"</td></tr>";
			}else{
				html += "<tr><td>"+filename+"</td><td><span style='background-color:red;color:white;display: inline-block;width:110px;'>"+uploadResult+"</span></td><td>"+failedReason+"</td></tr>";
			}
		}
		$("table").append(html);

		function toMusicList(){
			window.location.href="musicPlayer.page";
		}
		
		var time=5;
		var srcText=$("#backToIndexBtn").text();	
		setInterval(function(){
			time=time-1;
			var newText=srcText+"("+time+")";
			$("#backToIndexBtn").text(newText);
			if (time==0){
				toMusicList();
			}
		},1000)
	</script>
</html>