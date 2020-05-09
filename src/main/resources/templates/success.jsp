<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>${title}</title>
</head>
<body>
<h1>
	成功!
</h1>
<c:out value="${result}" escapeXml="true"/>
</body>
</html>
