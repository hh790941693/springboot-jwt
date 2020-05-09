<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
<meta charset="UTF-8">
<title>设置个人信息</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>

<style>
	#mainDiv{
		width:370px;
		height:280px;
		margin:2px auto;
		border:2px solid green;
	}
	
	#formDiv{
		width:360px;
		height:240px;
	}
	
	select{
		width:212px;
		margin-left:5px;
	}
</style>
</head>
<body>
	<div id="mainDiv" align="center">
		<div id="formDiv" align="left" style='position:relative;'>
			<label for="realName">真实姓名:</label><input id="realName" name="realName" type="text" style="width:208px;margin-left:10px;"></input>
			<br/>
			
			<div>
				<label id="headImgLabel" for="headImgFile">头&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;像:</label>
			</div>
			<div style='padding-left:80px;margin-top:-15px;'>
				<img id="headImg" alt="" title="点击放大图片" width="60px" height="60px" onclick="showdiv()" onerror="imgerror(this)">
			</div>
			
			<div id="showBigImageDiv" style="width:200px;height:200px;display:none;position:absolute;top:8px;left:80px;">
				<br><img id="bigImg" width="99%" height="99%"  onclick="hidediv();" title="点击关闭图片" style="cursor: pointer;" border="0">
			</div>

			<label for="sign">个性签名:</label><input id="sign" name="sign" type="text" style="width:208px;margin-left:10px;"></input>
			<br/>				
			
			<label for="age">年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;龄:</label><input id="age" name="age" type="text" style="width:208px;margin-left:10px;"></input>
			<br/>
			
			<label for="sexSelect">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别:</label>
			<select id="sexSelect">
				<option value="1" selected="selected">男</option>
				<option value="2">女</option>
				<option value="3">未知</option>
			</select>
			<br/>				
			
			<label for="tel">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话:</label><input id="tel" name="tel" type="text" style="width:208px;margin-left:10px;"></input>
			<br/>
			
			<label for="address">住&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址:</label><input id="address" name="address" type="text" style="width:208px;margin-left:10px;"></input>
			<br/>				

			<label for="professionSelect">职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业:</label>
			<select id="professionSelect">
				<option value="1">IT</option>
				<option value="2">建筑</option>
				<option value="3">金融</option>
				<option value="4">个体商户</option>
				<option value="5">旅游</option>
				<option value="6" selected="selected">其他</option>
			</select>
			<br/>
			
			<label for="hobbySelect">爱&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;好:</label>
			<select id="hobbySelect">
				<option value="1">篮球</option>
				<option value="2">足球</option>
				<option value="3">爬山</option>
				<option value="4">旅游</option>
				<option value="5">网游</option>
				<option value="6" selected="selected">其他</option>
			</select>
			<br/>
			
		</div>
	</div>
	<div id="tipDiv" style="display:none;color:red;" align="center">该用户尚未设置个人信息</div>
		
	<script>
		var userInfo = ${userInfo};
		$("input,select").attr("disabled","disabled");
		if (userInfo != null){
       		$("#realName").val(userInfo.realName);
       		$("#sign").val(userInfo.sign);
       		$("#age").val(userInfo.age);
       		$("#sexSelect").val(userInfo.sex);
       		$("#tel").val(userInfo.tel);
       		$("#address").val(userInfo.address);
       		$("#professionSelect").val(userInfo.profession);
       		$("#hobbySelect").val(userInfo.hobby);
       		
       		var imgFilename = userInfo.img;
       		var imgUrl = rootUrl+"img/headimg/"+imgFilename;
       		$("#headImg").attr("src",imgUrl);
		}else{
			$("#tipDiv").css("display","block");
		}
		
	    function showdiv() {
	    	 $("#bigImg").attr("src",$("#headImg").attr("src"));
	    	 $("#showBigImageDiv").css("display","block");
	    }
	    function hidediv(i) {
	    	 $("#showBigImageDiv").css("display","none");
	    }	
	      
		// 加载图片失败
		function imgerror(imgObj){
			var scrVar = $(imgObj).attr("src");
			$(imgObj).attr("src",rootUrl+"/img/imgerror_default.jpg");
		}		      
	</script>
</body>
</html>