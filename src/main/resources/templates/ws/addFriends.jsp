<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
<title>添加好友</title>
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
	<div align="right" style="width:1095px;">
		<label id="onlineCount">在线:0</label>
		<button id="refresh" type="button" class='btn btn-sm btn-primary'  onClick="refresh()">刷&nbsp;&nbsp;&nbsp;新</button>
		
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
			<button id="firstPage" class='btn btn-sm btn-warning'  onclick="getOnlineUsersByPage(1,parseInt($('#numPerPage').val()))" style="margin-left:10px;">&lt;&lt;</button>
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
		var curUser=parent.s_user;
		var userLike="";
		getOnlineUsersByPage(1,10);
		function getOnlineUsersByPage(curPage,numPerPage) {
			var data = {};
			data.curPage=curPage;  //当前页
			data.numPerPage=numPerPage;  //每页显示条数
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/showAllUser.json',
		        data:JSON.stringify(data),
		        dataType: 'json',
		        contentType:"application/json", 
		        data:JSON.stringify(data),
		        async:false,
		        success: function(result) {
		        	var totalPage = result.totalPage;
		        	var list = result.list;
		        	var totalCount = result.totalCount;
		    		var onlineCount = result.parameter1;
		    		var tabinfostr = "<tr><th width='8%'>用户</th><th width='8%'>是否在线</th><th  width='175px;'>注册时间</th><th width='175px;'>最后登录时间</th><th width='24%'>操作</th></tr>";
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
		        			var id = i.id;
			    			var user = i.name;
			    			if (user == curUser){
			    				return true;
			    			}
			    			var stateTmp = i.state;
			    			var enableTmp = i.enable;
			    			var registerTime = i.registerTime;
			    			var lastLoginTime= i.lastLoginTime;
			    			var state = "在线";
			    			var isFriend = i.isFriend;
			    			var onlineUserCss = "'color:blue;'"
			    			if (stateTmp == "0")
			    			{
			    				onlineUserCss = "'color:grey;'"
			    				state = "离线";
			    			}
			    			
			    			// 0:不是  1:申请中 2:被拒绝 3:申请成功
			    			var addBtnDisabled = "";
			    			var addBtnText = "添加好友";
			    			var btnClass = "btn btn-sm";
			    			if (isFriend == 0)
			    			{
			    				addBtnDisabled = "";
			    				addBtnText = "添加好友";
			    				btnClass += " btn-primary";
			    			}else if (isFriend == 1){
			    				addBtnDisabled = "disabled=disabled";
			    				addBtnText = "申请中";
			    				btnClass += " btn-info";
			    			}else if (isFriend == 2){
			    				addBtnDisabled = "";
			    				addBtnText = "重新申请";
			    				btnClass += " btn-primary";
			    			}else if (isFriend == 3){
			    				addBtnDisabled = "disabled=disabled";
			    				addBtnText = "已是好友";
			    				btnClass += " btn-success";
			    			}
			    			
			    			tabinfostr += "<tr id="+id+"><td><a href='javascript:void(0);' style='text-decoration: none;font-size:17px;color:black;' onclick='queryPersonInfo(this)'>" + user + "</a></td><td style=" +onlineUserCss+">" + state + "</td>"+"<td><label>"+registerTime+ "</label></td>" +"<td><label>"+lastLoginTime+ "</label></td>" +"<td class='.operate'><button type='button' style='width:80px;' class='"+btnClass+ "' " + addBtnDisabled+" onclick='addFriend(this)'>"+addBtnText+"</button></td>"+"</tr>";
			    		});
		        	}else{
	        			$("#prePage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#firstPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#nextPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#lastPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
		        	}
		    		$("#userTable").html(tabinfostr);
		    		$("#onlineCount").html("在线:"+onlineCount);
		        }
			});
			return status;
		}
		
		// 申请添加好友
		function addFriend(thisObj)
		{
			var recordId=$(thisObj).parent().parent().attr("id");
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/addFriend.do',
		        data:{fromUserName:curUser,toUserId:recordId},
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
		
		function refresh()
		{
			window.location.href = window.location.href;
		};
		
		window.setInterval(function(){
			refresh();
		},300000);
		
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
		
		//window.setInterval(function(){
		//	getOnlineUsers();
		//},60000);
	</script>
</body>
</html>
