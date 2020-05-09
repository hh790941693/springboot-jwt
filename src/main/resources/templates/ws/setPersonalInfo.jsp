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
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style>
	#mainDiv{
		width:360px;
		height:auto;
		border:2px solid green;
		margin:40px auto;
	}
	
	#formDiv{
		width:354px;
		height:100%;
	}
	
	select{
		width:212px;
		margin-left:5px;
	}
	
	#formDiv div{
		margin-top:10px;
		margin-left:5px;
	}
</style>
</head>
<body>
		<div id="mainDiv" align="center">
			<div id="formDiv" align="left">
				<form method="post" action="setPersonInfo.do" enctype="multipart/form-data" onsubmit="return beforeSumbit()" target="hidden_frame">
					<input id="userNameHidden" name="userName" hidden="hidden"></input>
					<div>
						<label id="realNameLabel" for="realName">真实姓名:</label><input id="realName" name="realName" type="text" style="width:208px;margin-left:10px;"></input>
					</div>
					
					<div>
						<label id="headImgLabel" for="headImgFile">头&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;像:</label><input id="headImgFile" name="headImgFile" type="file" accept="image/png,image/jpeg,image/gif" style="width:208px;margin-left:10px;"></input>
					</div>
					<div style='padding-left:80px;'>
						<img id="headImg" alt="" width="60px" height="60px" onerror="imgerror(this)">
					</div>
					
					<div>
						<label for="sign">个性签名:</label><input id="sign" name="sign" type="text" style="width:208px;margin-left:10px;"></input>
					</div>
			
					<div>
						<label for="age">年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;龄:</label><input id="age" name="age" type="text" style="width:208px;margin-left:10px;"></input>
					</div>
					
					<div>
						<label for="sexSelect">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别:</label>
						<select id="sexSelect" name="sex" onchange="setHiddenText(this,'professionTextHidden')">
							<option value="1" selected="selected">男</option>
							<option value="2">女</option>
							<option value="3">未知</option>
						</select>
						<input id="sexTextHidden" name="sexText" hidden="hidden"></input>
					</div>
					
					<div>
						<label for="tel">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话:</label><input id="tel" name="tel" type="text" style="width:208px;margin-left:10px;"></input>
					</div>
	
					<div>
						<label for="address">住&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址:</label><input id="address" name="address" type="text" style="width:208px;margin-left:10px;"></input>
					</div>
					
					<div>
						<label for="professionSelect">职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业:</label>
						<select id="professionSelect" name="profession" onchange="setHiddenText(this,'professionTextHidden')">
							<option value="1">IT</option>
							<option value="2">建筑</option>
							<option value="3">金融</option>
							<option value="4">个体商户</option>
							<option value="5">旅游</option>
							<option value="6" selected="selected">其他</option>
						</select>
						<input id="professionTextHidden" name="professionText" hidden="hidden"></input>
					</div>
	
					<div>
						<label for="hobbySelect">爱&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;好:</label>
						<select id="hobbySelect" name="hobby" onchange="setHiddenText(this,'hobbyTextHidden')">
							<option value="1">篮球</option>
							<option value="2">足球</option>
							<option value="3">爬山</option>
							<option value="4">旅游</option>
							<option value="5">网游</option>
							<option value="6" selected="selected">其他</option>
						</select>
						<input id="hobbyTextHidden" name="hobbyText" hidden="hidden"></input>
					</div>
					<div align="center" style='margin-bottom: 5px;'>
						<button type='submit' class='btn btn-sm btn-success' id="updatePersonInfoBtn">提&nbsp;&nbsp;&nbsp;&nbsp;交</button>
					</div>
			    </form>
			</div>
			<iframe name="hidden_frame" id="hidden_frame" style='display: none'></iframe>
		</div>
		
		<script>
			var user = parent.s_user;
			
			function setHiddenText(selectObj,hiddenId){
				var checkText=$(selectObj).find("option:selected").text();
				$("#"+hiddenId).val(checkText);
			}
			
			//查询个人信息
			queryPersonInfo(user);

			$("#headImgFile").change(function(){
				$("#headImg").attr("src",URL.createObjectURL($(this)[0].files[0]));
			})
			
			function beforeSumbit(){
				$("#userNameHidden").val(user);
				
				var sexText=$("#sexSelect").find("option:selected").text();
        		$("#sexTextHidden").val(sexText);

        		var professionText=$("#professionSelect").find("option:selected").text();
        		$("#professionTextHidden").val(professionText);
        		
        		var hobbyText=$("#hobbySelect").find("option:selected").text();
        		$("#hobbyTextHidden").val(hobbyText);
				return true;
			}

			// 查询个人信息
			function queryPersonInfo(user){
				$.ajax({
			      	type: 'POST',
			        url: rootUrl+'ws/queryPersonInfo.json',
			        data:{user:user},
			        dataType: 'json',
			        success: function(result) {
			        	if (JSON.stringify(result) == '{}'){
			        		console.log("空对象")
			        	}else{
			        		$("#realName").val(result.realName);
			        		$("#sign").val(result.sign);
			        		$("#age").val(result.age);
			        		$("#sexSelect").val(result.sex);
			        		$("#sexTextHidden").val(result.sexText);
			        		$("#tel").val(result.tel);
			        		$("#address").val(result.address);
			        		$("#professionSelect").val(result.profession);
			        		$("#professionTextHidden").val(result.professionText);
			        		$("#hobbySelect").val(result.hobby);
			        		$("#hobbyTextHidden").val(result.hobbyText);
			        		
			        		var imgFilename = result.img;
			        		var imgUrl = rootUrl+"img/headimg/"+imgFilename;
			        		$("#headImg").attr("src",imgUrl);
			        	}
			        }
				})				
			}
			
			$("#formDiv div").css("background-color","white");

			$("#formDiv div").hover(function(){
				$(this).css("background-color","green");
			},function(){
				$(this).css("background-color","white");
			})
			
			function refresh(){
				window.location.href = window.location.href;
			};
			
			// 加载图片失败
			function imgerror(imgObj){
				var scrVar = $(imgObj).attr("src");
				$(imgObj).attr("src",rootUrl+"/img/imgerror_default.jpg");
			}
			
			$("#hidden_frame").load(function(){
			    var text=$(this).contents().find("body").text();
				if (text.indexOf("success") != -1){
					var timeout=3;
					layer.msg('更新个人信息成功');
					var user=$("#user").val();
					var index = parent.layer.getFrameIndex(window.name);
					setTimeout(function(){
						parent.layer.close(index);
						refresh();
					},timeout*1000)
					
				}else if (text.indexOf("failed") != -1){
					layer.msg('更新个人信息失败');
				}
			});				
		</script>
</body>
</html>