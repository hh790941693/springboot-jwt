<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
<title>我的申请记录</title>
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
			<span>总共:</span><span id="totalCount">0</span><span>条</span>
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
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/getMyApplyList.json',
		        data:{curUser:curUser,userLike:userLike,curPage:curPage,numPerPage:numPerPage},
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	var totalPage = result.totalPage;
		        	var list = result.list;
		        	var totalCount = result.totalCount;
		    		var onlineCount = result.parameter1;
		    		var tabinfostr = "<tr><th width='24%'>好友名称</th><th  width='38%'>申请时间</th><th width='38%'>状态</th></tr>";
	        		$("#totalPage").text(totalPage);
	        		$("#curPage").text(curPage);
	        		$("#totalCount").text(totalCount);
	        		
	        		var curPageInt = parseInt(curPage);
	        		var totalPageInt =  parseInt(totalPage);
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
			    			var fromUser = i.fromName;
			    			var toUser = i.toName;
							var createTimeStr = i.createTimeStr;
							var processStatus = i.processStatus;
			    			
			    			// 1:申请中 2:被拒绝 3:申请成功
			    			if (processStatus == 1){
			    				tabinfostr += "<tr id="+id+"><td>" + toUser + "</td><td>" + createTimeStr + "</td>"+"<td><span style='width:80px;background-color:blue;color:white;display:inline-block;'>申请中</span></td></tr>";
			    			}else if (processStatus == 2){
			    				tabinfostr += "<tr id="+id+"><td>" + toUser + "</td><td>" + createTimeStr + "</td>"+"<td><span style='width:80px;background-color:red;color:white;display:inline-block;'>已拒绝</span></td><td class='.operate'></td></tr>";
			    			}else if (processStatus == 3){
			    				tabinfostr += "<tr id="+id+"><td>" + toUser + "</td><td>" + createTimeStr + "</td>"+"<td><span style='width:80px;background-color:green;color:white;display:inline-block;'>已同意</span></td><td class='.operate'></td></tr>";
			    			}
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

		function refresh(){
			window.location.href = window.location.href;
		};
		
		window.setInterval(function(){
			refresh();
		},300000);
		
		//window.setInterval(function(){
		//	getOnlineUsers();
		//},60000);
	</script>
</body>
</html>
