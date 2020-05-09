<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
<title>用户管理</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/viewjs/common.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style type="text/css">

	#userTable tr td{
		text-align:center;
	}
	
	#userTable{
		border:2px solid green;
	}
	
	#userTable th{
		background-color:green;
		color:white;
		font-weight:bold;
		text-align:center;
	}
	
	ul li{
		list-style:none;
		height:40px;
		font-size:20px;
		margin-left:-20px;
		cursor:pointer;
	}
	ul a{
		text-decoration:none;
	}
	
	#pagenavigation span{
		display: inline-block;
	}

	.btnDisabledClass{
		background-color:grey;
		color:black;
	}
	
	#controlDiv button{
		width:45px;
	}	
</style>
</head>
<body>
	<div style="width:1095px;">
		<div style="float:left;width:20%">
			<a id="exportDataBtn" type="button" class='btn btn-sm btn-primary'>导&nbsp;&nbsp;出</a>
		</div>
		<div style="float:right;width:10%">
			<label id="onlineCount">在线:0</label>
			<button id="refresh" type="button" class='btn btn-sm btn-primary'  onClick="refresh()">刷&nbsp;&nbsp;新</button>
		</div>
		<table id= "userTable" style="width:100%" >
		</table>
	</div>
	
	<div id="pagenavigation" style="width:1095px;height:26px;">
		<div id="totalCountDiv" style="float:left;">
			<span>总共:</span><span id="totalCount"></span><span>条</span>
		</div>
		
		<div id="controlDiv" style="float:right;">
			<span>当前是第</span><span id="curPage">1</span><span>页</span>
			<span style="margin-left:10px">总共</span><span id="totalPage">0</span><span>页</span>
			<button id="firstPage" class='btn btn-sm btn-warning' onclick="getOnlineUsersByPage(1,parseInt($('#numPerPage').val()))" style="margin-left:10px;">&lt;&lt;</button>
			<button id="prePage" class='btn btn-sm btn-primary' onclick="getOnlineUsersByPage(parseInt($('#curPage').text())-1,parseInt($('#numPerPage').val()))" style="margin-left:10px">&lt;</button>
			<button id="nextPage" class='btn btn-sm btn-primary' onclick="getOnlineUsersByPage(parseInt($('#curPage').text())+1,parseInt($('#numPerPage').val()))" style="margin-left:10px">&gt;</button>
			<button id="lastPage" class='btn btn-sm btn-warning'  onclick="getOnlineUsersByPage(parseInt($('#totalPage').text()),parseInt($('#numPerPage').val()))"style="margin-left:10px">&gt;&gt;</button>
			
			<span style="margin-left:10px">每页显示</span>
			<select id="numPerPage" onchange="getOnlineUsersByPage(parseInt($('#curPage').text()),parseInt($(this).val()))">
				<option value="5">5</option>
				<option value="10" selected="selected">10</option>
				<option value="15">15</option>
				<option value="20">20</option>
				<option value="50">50</option>
				<option value="100">100</option>
			</select>
			<span>条</span>
			
			<span style="margin-left:10px">跳转至</span><input id="toPage" onblur="getOnlineUsersByPage(parseInt($(this).val()),parseInt($('#numPerPage').val()))" style="width:20px;"></input><span>页</span>
		</div>
	</div>
	
	<script type="text/javascript">
		getOnlineUsersByPage(1,10);
		function getOnlineUsersByPage(curPage,numPerPage) {
			var data = {};
			data.curPage=curPage;  //当前页
			data.numPerPage=numPerPage;  //每页显示条数
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/getOnlineUsersByPage.json',
		        data:JSON.stringify(data),
		        contentType:"application/json", 
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	var totalPage = result.totalPage;
		        	var list = result.list;
		        	var totalCount = result.totalCount;
		    		var onlineCount = result.parameter1;
		    		var tabinfostr = "<tr><th width='8%'>用户</th><th width='8%'>是否在线</th><th  width='175px;'>注册时间</th><th width='175px;'>最后登录时间</th><th width='24%'>操作</th><th width='30%'>发送消息</th></tr>";
		        	if (list != null && list != "" && list != undefined && list.length>0)
		        	{
		        		$("#totalPage").text(totalPage);
		        		$("#curPage").text(curPage);
		        		$("#totalCount").text(totalCount);
		        		
		        		var curPageInt = parseInt(curPage);
		        		var totalPageInt =  parseInt(totalPage);

		        		if (totalPage == 1)
		        		{
		        			$("#prePage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
		        			$("#firstPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
		        			$("#nextPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
		        			$("#lastPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
		        		}
		        		else
		        		{
			    			if (curPageInt == 1)
			    			{
			        			$("#prePage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
			        			$("#firstPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
			    			}
			    			if (curPageInt > 1)
			    			{
			        			$("#prePage").removeClass("btn-warning").addClass("btn-primary").removeClass("btnDisabledClass").attr("disabled",false);
			        			$("#firstPage").removeClass("btn-primary").addClass("btn-warning").removeClass("btnDisabledClass").attr("disabled",false);
			    			}
			    			if (curPageInt < totalPage)
			    			{
			        			$("#nextPage").removeClass("btn-warning").addClass("btn-primary").removeClass("btnDisabledClass").attr("disabled",false);
			        			$("#lastPage").removeClass("btn-primary").addClass("btn-warning").removeClass("btnDisabledClass").attr("disabled",false);
			    			}
			        		if (curPageInt == totalPageInt)
			        		{
			        			$("#nextPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
			        			$("#lastPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
			        		}
		        		}

		        		list.forEach(function(i,index){
			    			var user = i.name;
			    			if (user == "admin"){
			    				return;
			    			}
			    			var stateTmp = i.state;
			    			var enableTmp = i.enable;
			    			var speak = i.speak;
			    			var registerTime = i.registerTime;
			    			var lastLoginTime= i.lastLoginTime;
			    			var state = "在线";
			    			var onlineUserCss = "'color:blue;font-weight:bold'"
			    			var id="'" + user + "'";
			    			var sendStatusId = "'" + user + "SendStatus'";
			    			
			    			var offlineDisable = "class='btn btn-sm btn-danger'";//下线button状态
			    			var jinyongDisable = "class='btn btn-sm btn-danger'";//禁用button状态
			    			var qiyongDisable = "class='btn btn-sm btn-success'";//启用button状态
			    			var jinyanDisable = "class='btn btn-sm btn-danger'";//禁言button状态
			    			var kaiyanDisable = "class='btn btn-sm btn-success'";//开言button状态
			    			if (stateTmp == "0"){
			    				//离线状态  下线按钮置灰
			    				state = "离线";
			    				onlineUserCss = "'color:grey;'"
			    				offlineDisable = "class='btn btn-sm btnDisabledClass' disabled='disabled'";
			    			}
			    			if (enableTmp == "0"){
			    				//账号禁用状态下   禁用按钮:不可用   启用按钮:可用
			    				jinyongDisable = "class='btn btn-sm btnDisabledClass' disabled='disabled'";
			    			}
			    			else{
			    				//账号可用状态下  禁用按钮:可用false  启用按钮:true
			    				qiyongDisable = "class='btn btn-sm btnDisabledClass' disabled='disabled'";
			    			}
			    			if (speak == "0"){
			    				//非禁言状态下  禁言按钮:true  开言按钮:false
			    				jinyanDisable = "class='btn btn-sm btnDisabledClass' disabled='disabled'";
			    			}
			    			else{
			    				//禁言状态下  禁言按钮:false  开言按钮:true
			    				kaiyanDisable = "class='btn btn-sm btnDisabledClass' disabled='disabled'";
			    			}
			    			tabinfostr += "<tr id="+id+"><td><a href='javascript:void(0);' style='text-decoration: none;font-size:17px;color:black;' onclick='queryPersonInfo(this)'>" + user + "</a></td><td style=" +onlineUserCss+">" + state + "</td>"+"<td><label>"+registerTime+ "</label></td>" +"<td><label>"+lastLoginTime+ "</label></td>" +"<td class='.operate'><button type='button' onclick='offline(this)' "+offlineDisable+">下线</button>"+"<button type='button' onclick='enableUser(this,0)' "+jinyongDisable+">禁用</button>"+"<button type='button' onclick='enableUser(this,1)' "+qiyongDisable+">启用</button><button type='button' onclick='enableSpeak(this,0)' "+jinyanDisable+">禁言</button><button type='button' onclick='enableSpeak(this,1)' "+kaiyanDisable+">开言</button></td>"+"<td><input id=" + sendStatusId + " type='text' style='width:260px;'><button style='margin-left:10px;' type='button' class='btn btn-sm btn-primary' onclick='send(this)'>发送</button></td></tr>";
			    		});
			    		$("#userTable").html(tabinfostr);
			    		$("#onlineCount").html("在线:"+onlineCount);
		        	}else{
	        			$("#prePage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#firstPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#nextPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#lastPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
		        		$("#userTable").html(tabinfostr);
		        	}
		        }
			});
			
			return status;
		}

		//getOnlineUsers();
	
		// {"count":2,"users":"usr1,usr2"}
		function getOnlineUsers() {
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/getOnlineInfo.json',
		        dataType: 'json',
		        success: function(result) {
		        	var jsonObj = result;
		        	// 在线人数
		        	var count = jsonObj.onlineCount;
		        	var usersInfoList = jsonObj.userInfoList;
		        	
		    		var tabinfostr = "<tr><th width='10%'>用户</th><th width='10%'>是否在线</th><th  width='160px;'>注册时间</th><th width='160px;'>最后登录时间</th><th width='15%'>操作</th><th width='35%'>发送消息</th></tr>";
		    		//var userArr = users.split(",");
		    		
		    		usersInfoList.forEach(function(i,index){
		    			var user = i.name;
		    			if (user == "admin"){
		    				return;
		    			}
		    			var stateTmp = i.state;
		    			var enableTmp = i.enable;
		    			var registerTime = i.registerTime;
		    			var lastLoginTime= i.lastLoginTime;
		    			var state = "在线";
		    			if (stateTmp == "0")
		    			{
		    				state = "离线";
		    			}
		    			var id="'" + user + "'";
		    			tabinfostr += "<tr id="+id+"><td>" + user + "</td><td>" + state + "</td>"+"<td><label>"+registerTime+ "</label></td>" +"<td><label>"+lastLoginTime+ "</label></td>" +"<td class='.operate'><button type='button' onclick='offline(this)'>下线</button>"+"<button type='button' onclick='enableUser(this,0)'>禁用</button>"+"<button type='button' onclick='enableUser(this,1)'>启用</button></td>"+"<td><input class='sendTextClass' type='text' style='width:200px;'><button style='margin-left:10px;' type='button' onclick='send(this)'>发送</button><label style='margin-left:5px;width:30px;'></label></td></tr>";

		    		});
		    		$("#userTable").html(tabinfostr);
		    		$("#onlineCount").html("在线:"+count);
		    		usersInfoList.forEach(function(i,index){
		    			var user = i.name;
		    			if (user == "admin"){
		    				continue;
		    			}
		    			var stateTmp = i.state;
		    			var enableTmp = i.enable;
		    			var speak = i.speak;
		    			
	    				var trObj = $("#"+user);
	    				var childrensObj = $(trObj).find("button");
		    			if (stateTmp == "0")
		    			{
		    				var offlineBtn = childrensObj[0];
		    				$(offlineBtn).attr('disabled',"true");
		    			}
		    			if (enableTmp == "0")
		    			{
		    				$(childrensObj[1]).attr('disabled',true);
		    				$(childrensObj[2]).attr('disabled',false);
		    			}
		    			else
		    			{
		    				$(childrensObj[1]).attr('disabled',false);
		    				$(childrensObj[2]).attr('disabled',true);
		    			}
		    			if (speak == "0")
		    			{
		    				$(childrensObj[3]).attr('disabled',true);
		    				$(childrensObj[4]).attr('disabled',false);
		    			}
		    			else
		    			{
		    				$(childrensObj[3]).attr('disabled',false);
		    				$(childrensObj[4]).attr('disabled',true);
		    			}
		    		});
		        }
		    });
		}
		
		function send(obj)
		{
			var parentObj = $(obj).parent().parent();
			var user = parentObj[0].id;
			var inputObj = $(obj).prev();
			var msg = $(inputObj).val();

			var sendStatusId = "#"+user+"SendStatus";
			if (msg == null || msg == "")
			{
		       layer.tips('消息不能为空!', sendStatusId, {
	                 tips: [1, '#0FA6D8'], 
	                 tipsMore: false, 
	                 time:2000  
	             });
				return;
			}

			var tmpMsg = "typeId:1;typeDesc:系统消息;from:admin;to:"+ user+";msg:"+msg;
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/sendText.do',
		        data:{user:user,msg:tmpMsg},
		        dataType: 'json',
		        success: function(result) {
		        	var detail = result;
		        	
		        	var statusTxtObj = $(obj).next();
		        	if (detail == "success")
		        	{
		 		       layer.tips('发送成功!', sendStatusId, {
			                 tips: [1, '#0FA6D8'], 
			                 tipsMore: false, 
			                 time:1000  
			             });
		        	}
		        	else if (detail == "offline")
		        	{
		 		       layer.tips('用户不在线!', sendStatusId, {
			                 tips: [1, '#0FA6D8'], 
			                 tipsMore: false, 
			                 time:1000  
			             });
		        	}
		        	else
		        	{
		 		       layer.tips('发送失败!', sendStatusId, {
			                 tips: [1, '#0FA6D8'], 
			                 tipsMore: false, 
			                 time:1000  
			             });
		        	}
		        	$(inputObj).val("");
		        }
			});
		}
		
		// 让某用户下线
		function offline(obj)
		{
			var parentObj = $(obj).parent().parent();
			var user = parentObj[0].id;
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/offlineUser.do',
		        data:{user:user},
		        dataType: 'json',
		        success: function(result) {
		        	var detail = result;
		        	if (detail == "success")
		        	{
		        		refresh();
		        	}
		        }
			});
		}
		
		// 禁用/启用用户
		function enableUser(obj,enableVar)
		{
			var parentObj = $(obj).parent().parent();
			var user = parentObj[0].id;
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/enableUser.do',
		        data:{user:user,enable:enableVar},
		        dataType: 'json',
		        success: function(result) {
		        	var detail = result;
		        	if (detail == "success")
		        	{
		        		refresh();
		        	}
		        }
			});			
		}
		
		//禁言/开言
		function enableSpeak(obj,enableVar)
		{
			var parentObj = $(obj).parent().parent();
			var user = parentObj[0].id;
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/enableSpeak.do',
		        data:{user:user,speak:enableVar},
		        dataType: 'json',
		        success: function(result) {
		        	var detail = result;
		        	if (detail == "success")
		        	{
		        		refresh();
		        	}
		        }
			});	
		}
		
		//查看个人信息
		function queryPersonInfo(thisObj){
			var user=$(thisObj).text();
			layer.open({
				  type: 2,
				  title: '用户个人信息',
				  shadeClose: true,
				  shade: 0.8,
				  area: ['384px', '370px'],
				  content: 'showPersonalInfo.page?user='+user
				}); 
		}
		
		function refresh(){
			window.location.href = window.location.href;
		};
		
		//导出用户信息
		$('#exportDataBtn').attr('href', rootUrl + "ws/exportUser.do");
		
		window.setInterval(function(){
			refresh();
		},300000);
	</script>
</body>
</html>
