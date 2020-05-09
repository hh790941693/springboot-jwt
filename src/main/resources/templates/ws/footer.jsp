<!DOCTYPE html>
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
	#footDiv{
		width:1320px;
		height:10px;
		color:black;
		font-size:15px;
		font-weight:bold;
		margin:14px auto 0px auto;
	}

	#copyrightDiv ul li{
		float:left;
		list-style:none;
		margin-top:-15px;
	}
</style>
</head>
<body>
	<div id="footDiv" align="center">
		<div style="float:left;margin-top:-15px;">
			<label id="curUser">邮箱:547495788@qq.com</label>
		</div>
		<div style="float:left;margin-left: 350px;margin-top:-15px;">
			<label id="cliName">所有版权信息归作者所有</label>
			<label id="versionLabel" style='margin-left:15px;color:blue;font-weight:bold;'></label>
			<a href='javascript:void(0)' onclick='checkUpdate()' style='margin-left:15px;' >检查更新</a>
		</div>
		<div id="copyrightDiv" style="float:right;">
			<ul>
				<li>联系作者 | </li>
				<li>捐赠作者 | </li>
				<li>提出疑问 | </li>
				<li>关于我们</li>
			</ul>
		</div>
	</div>
	
	<script type="text/javascript">
		var curVersion = "";
		queryVersion();
		function queryVersion(){
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/queryVersion.json',
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	curVersion = result;
					$("#versionLabel").text(result);
		        }
			});	
		}
		
		function checkUpdate(){
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/checkUpdate.do?version='+curVersion+"&cmd=check",
		        dataType: 'json',
		        async:false,
		        success: function(result) {
					console.log("checkUpdate result:"+result);
					//needUpdate:false;from:1.0;to:2.0
					var needUpdate = result.split("needUpdate:")[1].split(";")[0];
					if (needUpdate == "true"){
						var toVersion = result.split("to:")[1];
						
						layer.confirm('已经发现新版本,是否要升级到'+toVersion+"?", {
							  btn: ['是','否'] //按钮
							}, function(index){
							  console.log("开始升级到"+toVersion);
							  toUpdate(curVersion);
							  layer.close(index);
							}, function(index){
								layer.close(index);
							});
					}else{
						layer.alert('已经是最新版本!');
					}
		        }
			});
		}
		
		function toUpdate(curVersion){
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/checkUpdate.do?version='+curVersion+"&cmd=update",
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	console.log("升级结果:"+result)
		        }
			});
		}
	</script>
</body>
</html>
