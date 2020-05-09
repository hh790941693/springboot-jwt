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

<style type="text/css">
	#statusDiv{
		width:1320px;
		height:30px;
		font-size:15px;
		font-weight:bold;
		margin:1px auto;;
	}
	
	#statusTips{
	    width: 100%;
	    display: inline-block;
	    margin-top: -10px;
	}
</style>
</head>
<body>
	<div id="statusDiv" align="right">
		<span id="statusTips"></span>
	</div>
	
	<script type="text/javascript">
		function showTips(msg,color){
			if (color ==null || color== undefined){
				color="black";
			}
			$("#statusTips").css("color",color);
			$("#statusTips").text(msg);
		}
	</script>
</body>
</html>
