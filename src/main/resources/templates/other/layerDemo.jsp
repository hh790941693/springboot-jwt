<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%
	String basePath = request.getContextPath();
	String path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>死亡注销户口申请</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/frameTab.css" />
<script src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/viewjs/laydate/laydate.js"></script>
<script src="<%=path%>/static/js/jquery.validate.min.js"></script>
<style type="text/css">

 	#id_title{
 		width:800px;
 		height: 50px;
 		margin-top:0px;
 		margin-left:540px;
 		font-size:40px;
 	}

	#id_main{
 		width: 1000px;
 		height: 840px;
 		margin-top:20px;
 		margin-left:200px;
 		background-color:#295C94;
 		border:1px solid #0a3160;
 	}

 	#id_form{
 		width:950px;
 		height:690px;
 	}
 	
 	#id_table{
 		width:900px;
 		height:700px;
 		text-align:center;
 		margin:30px auto;
 	}
 	
 	td{
 		text-align:left;
 		height: 50px;
 	}
 	
 	.sexInput input{
 		width:30%;
 		height: 20px;
 		display:inline-block;
 	}
 	#btn_submit,#btn_reset{
 		width:200px;
 		height:56px;
 		display:inline-block;
 		margin:7px 0px 5px 180px;
 		cursor:pointer;
 		background-color:#92bbc1a8;
 	}

 	#id_footer{
 		width:1000px;
 		height:100px;
 		margin-top:15px;
 	}
 	hr{
 		width:99%;
 		height:1px;
 		border: 1px solid #3582c1;
 	}
 	
 	input{
 		background-color:#22548b;
 		width:100%;
 		color:white;
 		border:1px solid #3582c1;
 	}
 	.error{
 		color:red;
 	}
 	select option{
 		color:black;
 	}

</style>
</head>
<body>
	<div id="id_title">死亡注销户口申请</div>
	<div id="id_main">
		<form id="id_form" action="Swzxhksq.action" method="post">
			<table id="id_table" align="center">
				<tr>
					<td style="width: 30%;">申请人姓名:</td>
					<td style="width: 40%;"> <input type="text" name="sqr_name" /></td>
					<td style="width: 30%;"><label  for="sqr_name" class="error" style='display:none;'></label></td>
				</tr>

				<tr>
					<td>与被申请人关系 :</td>
					<td><input type="text" name="relation"> </td>
				</tr>
				<tr>
					<td>被申请人姓名:</td>
					<td><input type="text" name="s_name"></td>
				</tr>
				<tr>
					<td>被申请人性别:</td>
					<td class="sexInput"><input type="radio" name="s_sex" value="1" checked="checked">男 <input type="radio" name="s_sex" value="2">女 </td>
				</tr>
				
				<tr>
					<td>被申请人出生日期 :</td>
					<td><input id="s_birthday" type="text" name="s_birthday"></td>
					<td style="width: 30%;"><label  for="s_birthday" class="error" style='display:none;'></label></td>
				</tr>
				<tr>
					<td>被申请人户籍地:</td>
					<td><input type="text" name="s_address"></td>
				</tr>
				<tr>
					<td>死亡原因:</td>
					<td><input type="text" name="swyy"></td>
				</tr>
				<tr>
					<td>死亡时间:</td>
					<td><input id="swsj" type="text" name="swsj"></td>
					<td style="width: 30%;"><label  for="swsj" class="error" style='display:none;'></label></td>
				</tr>

				<tr>
					<td>备注 :</td>
					<td><input type="text" name="remark"> </td>
				</tr>

			</table>
		</form>
		<div id="id_footer">
			<hr/>
			<div style="margin-top:10px;">
				<img id="btn_submit" alt="" src="<%=path%>/img/btn_tijiao.png"/>
				<img id="btn_reset" alt="" src="<%=path%>/img/btn_chongzhi.png"/>
			</div>
		</div> 
	</div>

	<script>

		laydate.render({
			  elem: '#s_birthday'
			  ,type: 'datetime'
			  ,theme: '#393D49'
			  ,fixed:false
			  ,format:'yyyy/MM/dd HH:mm:ss'
			});
		
		laydate.render({
			  elem: '#swsj'
			  ,type: 'datetime'
			  ,theme: '#393D49'
			  ,fixed:false
			  ,format:'yyyy/MM/dd HH:mm:ss'
			});
		
		// 输入框聚焦和失焦背景颜色设置
		$("input").focus(function(){
			this.style.backgroundColor="#295C94"; //#F5F8F2 淡黄色   
		}).blur(function(){
			this.style.backgroundColor="#22548b";
		});

		$("#btn_submit").click(function(){
			$("#id_form").submit();
			return true;
		})
		
		$("#btn_reset").click(function(){
			$("input").val("");
		}); 

		//输入校验
		$().ready(function() {
		$("#id_form").validate({  
             rules: {  
            	 sqr_name: {  
                     required:true,
                 },
                 s_birthday:{
                	 required:true,
                 },
                 swsj:{
                	 required:true,
                 },
             },  
             messages: {  
            	 sqr_name: {  
                     required:'申请人姓名不能为空!',
                 },
                 s_birthday: {  
                     required:'出生日期不能为空!',
                 },	
                 swsj:{
                	 required:'死亡时间不能为空!'
                 }
             }  
         });
		});
		
	</script>
</body>
</html>