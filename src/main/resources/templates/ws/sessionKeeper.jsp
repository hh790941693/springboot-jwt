<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>session值</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>

<style type="text/css">

</style>
</head>
<body>
	<script type="text/javascript">
		var s_user='${requestScope.user}';
		var s_pass='${requestScope.pass}';
		var s_webserverip='${requestScope.webserverip}';
		var s_webserverport='${requestScope.webserverport}';
		var s_img='${requestScope.selfimg}';
		var s_userAgent='${requestScope.user_agent}';
	</script>
</body>
</html>
