<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>注册</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style type="text/css">
	a{
		font-size:13px;
	}
	span{
		display: inline-block;
		width:90px;
	}
	select{
		width:175px;
	}
	
	input{
		width:175px;
	}
	label{
		margin-bottom:0px;
	}
</style>
</head>
<body>
	<div style="width:470px;height:auto;margin:10px auto;border:2px solid green;background-color:#53D2FB">
		<form style="margin:10px 5px;" id="registerForm" action="wsregister.do" method="post" target="hidden_frame">
			<div>
				<span>用户名:</span><input id="user" name="user" type="text"></input>
			</div>
			<div>
				<span>密码:</span><input id="pass" name="pass" type="password" ></input>
				<br/>
				<span>确认密码:</span><input id="confirmPass" name="confirmPass" type="password"></input>
				<br/>
				<span>问题1:</span><select id="select1"  name="select1">
				</select>
				<br/>
				<span>你的答案</span><input id="answer1" name="answer1"></input>
				<br/>
				<span>问题2:</span><select id="select2"  name="select2">
				</select>
				<br/>
				<span>你的答案</span><input id="answer2" name="answer2"></input>
				<br/>
				<span>问题3:</span><select id="select3" name="select3">
				</select>
				<br/>
				<span>你的答案</span><input id="answer3" name="answer3"></input>
				
			</div>
			<div align="left" style="margin-top:10px;margin-left:130px;">
				<button id="btnregist" class='btn btn-sm btn-success'  type="submit" style="width:80px;" >注册</button>
			</div>
		</form>
	</div>
	<iframe name="hidden_frame" id="hidden_frame" style='display: none'></iframe>

	<script type="text/javascript">
		var dicJson=${dicJson};
		var questionArr = dicJson.zcwt;
		
		$("#select1").empty();
		$("#select2").empty();
		$("#select3").empty();
		for (var i=0;i<questionArr.length;i++)
		{
			var qusTxt = questionArr[i].name;
			$("#select1").append("<option value=" + qusTxt + ">" + qusTxt+ "</option>");
			$("#select2").append("<option value=" + qusTxt + ">" + qusTxt+ "</option>");
			$("#select3").append("<option value=" + qusTxt + ">" + qusTxt+ "</option>");
			
			$("#select1").get(0).selectedIndex=0;
			$("#select2").get(0).selectedIndex=1;
			$("#select3").get(0).selectedIndex=2;
		}

		$("#user").blur(function(){
			  var user = $("#user").val();
			  if (user != '' && user != undefined)
			  {
				  checkUserRegisterStatus(user);
			  }
			});
		
		function checkUserRegisterStatus(user)
		{
			if (user == "admin"){
				layer.msg('admin为系统所用,请使用其他名称!');
				$("#btnregist").attr("disabled",true);
				$("#user").val("");
				return
			}
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/checkUserRegisterStatus.json',
		        data:{user:user},
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	var isExist = result.result;
		        	if (isExist == "true")
		        	{
		        		$("#btnregist").attr("disabled",true);
		        		layer.msg('该用户已经注册过了,请直接登录!');
		        		$("#user").val("");
		        	}
		        	else
		        	{
		        		$("#btnregist").attr("disabled",false);
		        	}
		        }
			});
		}
		
		$("#hidden_frame").load(function(){
		    var text=$(this).contents().find("body").text();
			if (text.indexOf("success") != -1){
				var timeout=3;
				layer.msg('注册成功,'+timeout+"秒后返回登录页面");
				var user=$("#user").val();
				var index = parent.layer.getFrameIndex(window.name);
				//console.log("windowname:"+window.name);
				//console.log("index:"+index);
				console.log("user:"+user)
				setTimeout(function(){
					parent.layer.close(index);
					window.location.href = "login.page";
				},timeout*1000)
				
			}else if (text.indexOf("failed") != -1){
				layer.msg('注册失败');
			}
		});		
		
		
		$("#registerForm").validate({
			    rules: {
					      user: 
					      {
					        required: true,
					        minlength: 2,
					        maxlength:6
					      },
					      pass: 
					      {
					        required: true,
					        minlength: 2,
					        maxlength:6
					      },
					      confirmPass:{
					    	  required: true,
					    	  minlength: 2,
					    	  maxlength:6,
					    	  equalTo:"#pass"
					      },
					      select1:{
					    	  required: true
					      },
					      answer1:{
					    	  required: true,
					    	  minlength: 2
					      },
					      select2:{
					    	  required: true
					      },
					      answer2:{
					    	  required: true,
					    	  minlength: 2
					      },
					      select3:{
					    	  required: true,
					      },
					      answer3:{
					    	  required: true,
					    	  minlength: 2
					      }
			   		 },
			    messages:{
					       user: 
					       {
					        required: "请输入用户名",
					        minlength: "用户名至少包含2个字符",
					        maxlength:"用户名最多包含6位字符"
					       },
					       pass: 
					       {
					        required: "请输入密码",
					        minlength: "密码至少包含2个字符",
					        maxlength:"密码最多包含6位字符"
					      },
					      confirmPass:{
						        required: "请输入密码",
						        minlength: "密码至少包含2个字符",
						        maxlength:"密码最多包含6位字符",
						        equalTo:"密码前后不一致"
					      },
					      answer1:{
					    	  required:"请输入答案",
					    	  minlength:"答案至少2个字符"
					      },
					      answer2:{
					    	  required:"请输入答案",
					    	  minlength:"答案至少2个字符"
					      },
					      answer3:{
					    	  required:"请输入答案",
					    	  minlength:"答案至少2个字符"
					      }
			  	 	}
		    });
	</script>
</body>
</html>
