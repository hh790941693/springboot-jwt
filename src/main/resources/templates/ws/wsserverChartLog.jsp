<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
<title>用户管理</title>
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
			<div style="width:95%;float:left;">
				用户:&nbsp;&nbsp;<select id="user" onchange="getChatLogByPage(parseInt($('#curPage').text()),parseInt($('#numPerPage').val()))">
					<option value="-1">--请选择--</option>
				</select>
				&nbsp;&nbsp;&nbsp;
				发给:&nbsp;&nbsp;<select id="toUser" onchange="getChatLogByPage(parseInt($('#curPage').text()),parseInt($('#numPerPage').val()))">
					<option value="-1">--请选择--</option>
				</select>
				
				&nbsp;&nbsp;开始时间:&nbsp;&nbsp;<input id="beginTime" style="width:135px;" type="text"></input>
				&nbsp;&nbsp;结束时间:&nbsp;&nbsp;<input id="endTime" style="width:135px;" type="text"></input>
				
				&nbsp;&nbsp;关键词:<input id="keywordInput" style='width:120px;' placeholder="请输入关键词" onblur="getChatLogByPage(parseInt($('#curPage').text()),parseInt($('#numPerPage').val()))">
			</div>
			<div style="width:5%;float:left;">
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
		//console.log("${users}");
		var users = ${users};

		for (var k=0;k<users.length;k++)
		{
			var userName = users[k];
			if (userName != "admin")
			{
    			$("#user").append("<option value=" + userName + ">" + userName+ "</option>");
    			$("#toUser").append("<option value=" + userName + ">" + userName+ "</option>");
			}
		}		
		function getChatLogByPage(curPage,numPerPage) {
			var data = {};
			data.curPage=curPage;  //当前页
			data.numPerPage=numPerPage;  //每页显示条数
			var beginTime=$("#beginTime").val();
			if (beginTime == "")
			{
				beginTime=null;
			}
			var endTime=$("#endTime").val();
			if (endTime == "")
			{
				endTime=null;
			}
			data.beginTime=beginTime;
			data.endTime=endTime;
			
			if ($("#user").val() != "-1"){
				data.user=$("#user").val();
			}
			if ($("#toUser").val() != "-1"){
				data.toUser=$("#toUser").val();
			}
			var keyword=$("#keywordInput").val();
			if (keyword == ""){
				keyword = null;
			}
			data.keyword=keyword;

			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/getChatLogByPage.json',
		        contentType:"application/json", 
		        //data:{curPage:curPage,numPerPage:numPerPage},
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
		        	var tabinfostr = "<tr><th width='5%'>选择</th><th width='18%'>时间</th><th width='10%'>用户</th><th  width='10%'>发给</th><th width='35%'>信息</th><th width='10%'>备注</th></tr>";
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
							var time = i.time;
							var user = i.user;
							var toUser = i.toUser;
							var fromUser = i.fromUser;
							var msg = i.msg;
							var remark = i.remark;
							if (remark == "" || remark == undefined)
							{
								remark = "";
							}
			    			tabinfostr += "<tr> <td><input type='radio' name='radio'></td> <td>"+time+"</td><td>"+user+"</td> <td>"+toUser+"</td><td>"+msg+"</td><td>"+remark+"</td> </tr>";
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

        $("#beginTime").datetimepicker({
        	format:'Y-m-d H:i:s',
            onClose: function(selectedDate) {
            	getChatLogByPage(parseInt($('#curPage').text()),parseInt($('#numPerPage').val()));
            }
        })
        
        $("#endTime").datetimepicker({
        	format:'Y-m-d H:i:s',
            onClose: function(selectedDate) {
            	getChatLogByPage(parseInt($('#curPage').text()),parseInt($('#numPerPage').val()));
            }
        });
		
		function refresh()
		{
			window.location.href = window.location.href;
		};
		
		window.setInterval(function(){
			refresh();
		},300000);
	</script>
</body>
</html>
