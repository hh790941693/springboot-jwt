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
	#adsTable tr td{
		text-align:center;
	}
	
	#adsTable{
		border:2px solid green;
	}
	
	#adsTable th{
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
	<div>
		<div align="right">
			<button id='publishAdBtn' class='btn btn-sm btn-primary'>发布广告</button>
		</div>
		<table id= "adsTable" style="width:100%" >
		</table>
	</div>

	<div id="pagenavigation" style="width:1095px;height:26px;">
		<div id="totalCountDiv" style="float:left;">
			<span>总共:</span><span id="totalCount"></span><span>条</span>
		</div>
		
		<div id="controlDiv" style="float:right;">
			<span>当前是第</span><span id="curPage">1</span><span>页</span>
			<span style="margin-left:10px">总共</span><span id="totalPage">0</span><span>页</span>
			<button id="firstPage" class='btn btn-sm btn-warning'  onclick="getAdsListByPage(1,parseInt($('#numPerPage').val()))" style="margin-left:10px;">&lt;&lt;</button>
			<button id="prePage" class='btn btn-sm btn-primary' onclick="getAdsListByPage(parseInt($('#curPage').text())-1,parseInt($('#numPerPage').val()))" style="margin-left:10px">&lt;</button>
			<button id="nextPage" class='btn btn-sm btn-primary' onclick="getAdsListByPage(parseInt($('#curPage').text())+1,parseInt($('#numPerPage').val()))" style="margin-left:10px">&gt;</button>
			<button id="lastPage" class='btn btn-sm btn-warning'  onclick="getAdsListByPage(parseInt($('#totalPage').text()),parseInt($('#numPerPage').val()))"style="margin-left:10px">&gt;&gt;</button>
			
			<span style="margin-left:10px">每页显示</span>
			<select id="numPerPage" onchange="getAdsListByPage(parseInt($('#curPage').text()),parseInt($(this).val()))">
				<option value="5">5</option>
				<option value="10" selected="selected">10</option>
				<option value="15">15</option>
				<option value="20">20</option>
				<option value="50">50</option>
				<option value="100">100</option>
			</select>
			<span>条</span>
			
			<span style="margin-left:10px">跳转至</span><input id="toPage" onblur="getAdsListByPage(parseInt($(this).val()),parseInt($('#numPerPage').val()))" style="width:20px;"></input><span>页</span>
		</div>
	</div>

	<div id="adDiv" style="margin:10px auto;width:451px;height:auto;padding-top: 10px;border:2px solid green;display:none">
		<form action="addAd.do" method="post" style="width:450px;" onsubmit="return publishAdsValidate()">
			标题:<input id="adTitle" name="adTitle" type="text" style="width:82%"><br/><br/>
			内容:<textarea id="adContent" rows="10" cols="55" name="adContent"></textarea><br/>
			<input type="submit" value="发布" style="margin-left:200px;">
		</form>
	</div>

	<script type="text/javascript">
		
		var layerIndex=undefined;
		
		$("#publishAdBtn").click(function(){
			layerIndex = layer.open({
				  type: 2,
				  title: '发布广告',
				  shadeClose: true,
				  shade: 0.6,
				  area: ['490px', '370px'],
				  content: 'addAd.page'
			});	
		});
		
		//供子iframe调用
		function closeLayerAndReload(){
			if (layerIndex != undefined){
				layer.close(layerIndex);
			}
		}
		
		function publishAdsValidate(){
			var title = $("#adTitle").val();
			if (null == title || title == ""){
				alert("标题不能为空!");
				return false;
			}
			return true;
		}
		
		getAdsListByPage(1,10);
		function getAdsListByPage(curPage,numPerPage) {
			$.ajax({
		      	type: 'GET',
		        url: rootUrl+'ws/getAdsListByPage.json',
		        data:{curPage:curPage,numPerPage:numPerPage},
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	var totalPage = result.totalPage;
		        	var list = result.list;
		        	var totalCount = result.totalCount;
		        	var tabinfostr = "<tr><th width='7%'>序号</th><th width='50px'>标题</th><th  width='165px;'>内容</th><th width='175px;'>接收人列表</th><th width='16%'>创建时间</th></tr>";
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
			    			var title = i.title;
			    			if (title.length > 15){
			    				title = title.substring(0,15);
			    			}
			    			var content = i.content;
			    			if (content.length > 20){
			    				content = content.substring(0,20);
			    			}
			    			var receiveList = i.receiveList;
			    			if (receiveList.length > 11){
			    				receiveList = receiveList.substring(0,11);
			    			}
			    			var createTime= i.createTimeStr;
			    			tabinfostr += "<tr id="+id+"><td>" + id + "</td><td>" + title + "</td>"+"<td><label>"+content+ "</label></td>" +"<td><label>"+receiveList+ "</label></td>" + "<td>"+createTime+"</td>"+"</tr>";
			    		});
		        	}else{
	        			$("#prePage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#firstPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#nextPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
	        			$("#lastPage").removeClass("btn-primary btn-warning").addClass("btnDisabledClass").attr("disabled",true);
		        	}
		        	$("#adsTable").html(tabinfostr);
		        }
			})
		}
	</script>
</body>
</html>
