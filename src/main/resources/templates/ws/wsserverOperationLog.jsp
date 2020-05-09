<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
<title>操作日志管理</title>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/timepicker/jquery.datetimepicker.js"></script>
<link href="<%=path%>/static/timepicker/jquery.datetimepicker.css" rel="stylesheet" />
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/viewjs/common.js"></script>
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
	<div  style="width:1095px;">
		<div style="width:100%;">
			<div style="width:80%;float:left;">
				操作用户:&nbsp;&nbsp;<select id="userSelect" onchange="getChatLogByPage(parseInt($('#curPage').text()),parseInt($('#numPerPage').val()))">
					<option value="">--请选择--</option>
				</select>
				&nbsp;&nbsp;&nbsp;
				模块:&nbsp;&nbsp;<select id="moduleSelect" onchange="getChatLogByPage(parseInt($('#curPage').text()),parseInt($('#numPerPage').val()))">
					<option value="">--请选择--</option>
				</select>
			</div>
			<div style="width:12%;float:right;">
				<a id="exportDataBtn" type="button" class='btn btn-sm btn-primary'>导&nbsp;&nbsp;出</a>&nbsp;&nbsp;
				<button id="refresh" type="button" class='btn btn-sm btn-primary'  onClick="refresh()">刷&nbsp;&nbsp;新</button>
			</div>
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
			<button id="firstPage" class='btn btn-sm btn-warning' onclick="getChatLogByPage(1,parseInt($('#numPerPage').val()))" style="margin-left:10px;">&lt;&lt;</button>
			<button id="prePage" class='btn btn-sm btn-primary' onclick="getChatLogByPage(parseInt($('#curPage').text())-1,parseInt($('#numPerPage').val()))" style="margin-left:10px">&lt;</button>
			<button id="nextPage" class='btn btn-sm btn-primary' onclick="getChatLogByPage(parseInt($('#curPage').text())+1,parseInt($('#numPerPage').val()))" style="margin-left:10px">&gt;</button>
			<button id="lastPage" class='btn btn-sm btn-warning'  onclick="getChatLogByPage(parseInt($('#totalPage').text()),parseInt($('#numPerPage').val()))"style="margin-left:10px">&gt;&gt;</button>
			
			<span style="margin-left:10px">每页显示</span>
			<select id="numPerPage" onchange="getChatLogByPage(parseInt($('#curPage').text()),parseInt($(this).val()))">
				<option value="5">5</option>
				<option value="10" selected="selected">10</option>
				<option value="15">15</option>
				<option value="20">20</option>
				<option value="50">50</option>
				<option value="100">100</option>
			</select>
			<span>条</span>
			
			<span style="margin-left:10px">跳转至</span><input id="toPage" onblur="getChatLogByPage(parseInt($(this).val()),parseInt($('#numPerPage').val()))" style="width:20px;"></input><span>页</span>
		</div>
	</div>
	
	<script type="text/javascript">
		getChatLogByPage(1,10);
		var users = ${users};
		var modules = ${modules};

		//用户列表
		for (var k=0;k<users.length;k++){
			var userName = users[k];
    		$("#userSelect").append("<option value=" + userName + ">" + userName+ "</option>");
		}
		//模块列表
		for (var k=0;k<modules.length;k++){
			var moduleName = modules[k];
    		$("#moduleSelect").append("<option value=" + moduleName + ">" + moduleName+ "</option>");
		}		
		
		function getChatLogByPage(curPage,numPerPage) {
			var data = {};
			data.curPage=curPage;  //当前页
			data.numPerPage=numPerPage;  //每页显示条数

			if ($("#userSelect").val() != "" && $("#userName").val() != 'undefined'){
				data.userName=$("#userSelect").val();
			}
			if ($("#moduleSelect").val() != "" && $("#moduleName").val() != 'undefined'){
				data.operModule=$("#moduleSelect").val();
			}

			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/getOperationLogByPage.json',
		        contentType:"application/json",
		        data:JSON.stringify(data),
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	var totalPage = result.totalPage;
		        	var list = result.list;
		        	var totalCount = result.totalCount;
		        	$("#userTable").html("");
		        	$("#totalPage").text(0);
		        	$("#curPage").text(1);
		        	$("#totalCount").text(0);
		        	var tabinfostr = "<tr><th width='5%'>选择</th><th width='15%'>操作用户</th><th width='13%'>模块</th><th  width='10%'>操作类型</th><th width='30%'>操作描述</th><th width='10%'>耗时</th><th width='auto'>操作时间</th></tr>";
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
							var userName = i.userName;
							if (userName == undefined){
								userName = "未知";
							}
							var operType = i.operType;
							var operModule = i.operModule;
							var operDescribe = i.operDescribe;
							var costTime = i.costTime+" ms";
							var accessTimeText = i.accessTimeText;
							
							var msg = i.msg;
							var remark = i.remark;
							if (remark == "" || remark == undefined)
							{
								remark = "";
							}
			    			tabinfostr += "<tr> <td><input type='radio' name='radio'></td> <td>"+userName+"</td><td>"+operModule+"</td> <td>"+operType+"</td><td>"+operDescribe+"</td><td>"+costTime+"</td><td>"+accessTimeText+"</td></tr>";
			    		});
		        	}else{
	        			$("#prePage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#firstPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#nextPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#lastPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
		        	}
		        	$("#userTable").html(tabinfostr);
		        }
			});
			
			return status;
		}

		function refresh(){
			window.location.href = window.location.href;
		};
		
		//导出操作日志信息
		$('#exportDataBtn').click(function(){
			var userName="";
			var operModule="";
			if ($("#userSelect").val() != "" && $("#userName").val() != 'undefined'){
				userName=$("#userSelect").val();
			}
			if ($("#moduleSelect").val() != "" && $("#moduleName").val() != 'undefined'){
				operModule=$("#moduleSelect").val();
			}
			$(this).attr('href', rootUrl + "ws/exportOperateLog.do?userName="+userName+"&operModule="+operModule);
		})
		
		window.setInterval(function(){
			refresh();
		},300000);
	</script>
</body>
</html>
