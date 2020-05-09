<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>添加朋友圈</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path%>/viewjs/common.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style type="text/css">
	#mainDiv{
		width:98%;
		height:auto;
		border:2px solid green;
		margin:2px auto;
		padding:5px;
	}
	
	#form span{
		width:100px;
		display:inline-block;
	}
	
	.inputImgClass{
	
	}
	
	.imgClass{
	
	}
	
	.imgDiv{
		padding-left:110px;
		margin-top:-22px;
	}
	
	hr{
		margin:6px;
	}
	
</style>
</head>
<body>
	<div id="mainDiv">
		<form id="form" method="post" action="addCircle.do" enctype="multipart/form-data" onsubmit="return beforeSumbit()" target="hidden_frame">
			<div>
				<input id="userNameHidden" name="user" hidden="hidden"></input>
			</div>
			<div>
				<span>内&nbsp;&nbsp;&nbsp;&nbsp;容:</span><textarea id="contentTa" name="content" rows="3" cols="40" placeholder="请输入内容"></textarea>
			</div>
			<hr/>
			<div>
				<span>图&nbsp;&nbsp;&nbsp;&nbsp;片1:</span>
			</div>
			<div class='imgDiv'>
				<img id="circleImg1" class="imgClass" alt="" width="60px" height="60px">
				<input id="circleImgFile1" name="circleImgFile1" class="inputImgClass" type="file" accept="image/png,image/jpeg,image/gif" style="width:208px;margin-left:10px;"></input>
			</div>
			<hr/>
			<div>
				<span>图&nbsp;&nbsp;&nbsp;&nbsp;片2:</span>
			</div>
			<div class='imgDiv'>
				<img id="circleImg2" class="imgClass" alt="" width="60px" height="60px">
				<input id="circleImgFile2" name="circleImgFile2" class="inputImgClass" type="file" accept="image/png,image/jpeg,image/gif" style="width:208px;margin-left:10px;"></input>
			</div>
			<hr/>
			<div>
				<span>图&nbsp;&nbsp;&nbsp;&nbsp;片3:</span>
			</div>
			<div class='imgDiv'>
				<img id="circleImg3" class="imgClass" alt="" width="60px" height="60px">
				<input id="circleImgFile3" name="circleImgFile3" class="inputImgClass" type="file" accept="image/png,image/jpeg,image/gif" style="width:208px;margin-left:10px;"></input>
			</div>
			<hr/>
			<div>
				<span>图&nbsp;&nbsp;&nbsp;&nbsp;片4:</span>
			</div>
			<div class='imgDiv'>
				<img id="circleImg4" class="imgClass" alt="" width="60px" height="60px">
				<input id="circleImgFile4" name="circleImgFile4" class="inputImgClass" type="file" accept="image/png,image/jpeg,image/gif" style="width:208px;margin-left:10px;"></input>
			</div>
			<hr/>
			<div align="center" style='margin-top:20px;'>
				<button type="submit" class='btn btn-sm btn-success'>添&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;加</button>
			</div>
		</form>
		<iframe name="hidden_frame" id="hidden_frame" style='display: none'></iframe>
	</div>
	
	<script type="text/javascript">
		var curUser="${user}";
		
		$(".inputImgClass").change(function(){
			//获取对应的img对象
			var imgObj = $(this).prev();
			$(imgObj).attr("src",URL.createObjectURL($(this)[0].files[0]));
		})
		
		function beforeSumbit(){
			$("#userNameHidden").val(curUser);
			var content = $("#contentTa").val();
			if (content == ""){
				return false;
			}
			return true;
		}

		$("#hidden_frame").load(function(){
		    var text=$(this).contents().find("body").text();
			if (text.indexOf("success") != -1){
				layer.msg('新增朋友圈成功');
				var timeout = 0.7;
				var user=$("#user").val();
				var index = parent.layer.getFrameIndex(window.name);
				setTimeout(function(){
					parent.layer.close(index);
					parent.refresh();
				},timeout*1000)
				
			}else if (text.indexOf("failed") != -1){
				layer.msg('新增朋友圈失败');
			}
		});		
	</script>
</body>
</html>
