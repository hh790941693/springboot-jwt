<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
<title>发布广告</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/viewjs/common.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style type="text/css">

</style>
</head>
<body>
	<div id="adDiv" style="margin:5px auto;width:451px;height:auto;padding:10px;border:2px solid green;">
		标题:<input id="adTitle" name="adTitle" type="text" autocomplete="off" style="width:82%"><br/><br/>
		内容:<textarea id="adContent" rows="7" cols="55" name="adContent" style="width:82%"></textarea><br/><br/>
		<button value="发布" class='btn btn-sm btn-success' onclick="addAd()" style="margin-left:200px;">发&nbsp;&nbsp;&nbsp;&nbsp;布</button>
	</div>

	<script type="text/javascript">
		function publishAdsValidate(){
			var title = $("#adTitle").val();
			if (null == title || title == ""){
				alert("标题不能为空!");
				return false;
			}
			return true;
		}
		
		function addAd(){
			adTitle=$("#adTitle").val();
			if (null == adTitle || adTitle == ""){
				alert("标题不能为空!");
				return;
			}			
			adContent=$("#adContent").val();
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/addAd.do',
		        data:{adTitle:adTitle,adContent:adContent},
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	console.log(result);
		        	layer.close(layer.index);
		        	if (result == "success"){
		        		layer.alert("添加成功");
		        	}else{
		        		layer.alert("添加失败");
		        	}
	        		setTimeout(function(){
	        			//调用父layer弹出层所在页面的方法
	        			parent.closeLayerAndReload();
	        			window.parent.location.reload();
	        		},1500);
		        }
			});
		}
	</script>
</body>
</html>
