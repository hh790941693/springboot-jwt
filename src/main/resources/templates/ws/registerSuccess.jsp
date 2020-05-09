<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>登录失败</title>
</head>
<body>
<h1>
	用户${user}注册成功!
</h1>
<a href="login.page?user=${user}">返回登录页面</a>
</body>
</html>
