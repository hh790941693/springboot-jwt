<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
<title>${TYPE_TEXT}</title>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
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
		<div align="right" id="buttongroup">
			<button onclick="addItem()" class='btn btn-sm btn-info'>&nbsp;增&nbsp;&nbsp;&nbsp;加&nbsp;</button>
			<button onclick="modifyItem()" class='btn btn-sm btn-warning'>&nbsp;修&nbsp;&nbsp;&nbsp;改&nbsp;</button>
			<button onclick="deleteItem()" class='btn btn-sm btn-danger'>&nbsp;删&nbsp;&nbsp;&nbsp;除&nbsp;</button>
			<input type="file" id="file" style="display:none">  
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
			<button id="firstPage" class='btn btn-sm btn-warning'  onclick="getCommonByPage(1,parseInt($('#numPerPage').val()))" style="margin-left:10px;">&lt;&lt;</button>
			<button id="prePage" class='btn btn-sm btn-primary' onclick="getCommonByPage(parseInt($('#curPage').text())-1,parseInt($('#numPerPage').val()))" style="margin-left:10px">&lt;</button>
			<button id="nextPage" class='btn btn-sm btn-primary' onclick="getCommonByPage(parseInt($('#curPage').text())+1,parseInt($('#numPerPage').val()))" style="margin-left:10px">&gt;</button>
			<button id="lastPage" class='btn btn-sm btn-warning'  onclick="getCommonByPage(parseInt($('#totalPage').text()),parseInt($('#numPerPage').val()))"style="margin-left:10px">&gt;&gt;</button>
			
			<span style="margin-left:10px">每页显示</span>
			<select id="numPerPage" onchange="getCommonByPage(parseInt($('#curPage').text()),parseInt($(this).val()))">
				<option value="5">5</option>
				<option value="10" selected="selected">10</option>
				<option value="15">15</option>
				<option value="20">20</option>
				<option value="50">50</option>
				<option value="100">100</option>
			</select>
			<span>条</span>
			
			<span style="margin-left:10px">跳转至</span><input id="toPage" onblur="getCommonByPage(parseInt($(this).val()),parseInt($('#numPerPage').val()))" style="width:20px;"></input><span>页</span>
		</div>
	</div>
	
	<script type="text/javascript">
		var TYPE='${type}';
		var TYPE_TEXT='${title}';
	
		getCommonByPage(1,10);
		//console.log("${users}");	
		function getCommonByPage(curPage,numPerPage) {
			layer.load(1);
			var data = {};
			data.curPage=curPage;  //当前页
			data.numPerPage=numPerPage;  //每页显示条数
			data.type=TYPE;
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/getCommonByPage.json',
		        contentType:"application/json", 
		        data:JSON.stringify(data),
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	layer.closeAll('loading');
		        	var totalPage = result.totalPage;
		        	var list = result.list;
		        	var totalCount = result.totalCount;
		        	$("#userTable").html("");
		        	$("#totalPage").text(0);
		        	$("#curPage").text(1);
		        	$("#totalCount").text(0);
		        	var tabinfostr = "<tr><th width='5%'>选择</th><th width='10%'>类型</th><th width='18%'>文字</th><th width='10%'>备注</th></tr>";
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
		        			var type = i.type;
							var name = i.name;
							var remark = i.remark;
							if (remark == "" || remark == undefined)
							{
								remark = "";
							}
			    			tabinfostr += "<tr> <td><input id='" +id + "' type='radio' name='radio'></td> <td>"+type+"</td><td>"+name+"</td> <td>"+remark+"</td>";

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

		function addItemAjax(name)
		{
			var data = {};
			data.name=name
			data.type=TYPE;
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/addCommonItem.do',
		        contentType:"application/json", 
		        data:JSON.stringify(data),
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	if (result=='success'){
		        		layer.alert('添加敏感词'+name+'成功');
		        	}
		        }
			});
		}
		
		function deleteItemAjax(id)
		{
			var data = {};
			data.id=id
			data.type=TYPE;
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/deleteCommonItem.do',
		        contentType:"application/json", 
		        data:JSON.stringify(data),
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	if (result=='success'){
		        		layer.alert('删除敏感词'+name+'成功');
		        	}
		        }
			});
		}
		
		function updateItemAjax(id,name)
		{
			var data = {};
			data.id=id
			data.type=TYPE;
			data.name=name;
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/updateCommonItem.do',
		        contentType:"application/json", 
		        data:JSON.stringify(data),
		        dataType: 'json',
		        async:false,
		        success: function(result) {
		        	if (result=='success'){
		        		layer.alert('更新敏感词'+name+'成功');
		        	}
		        }
			});
		}
		
		function addItem()
		{
			layer.prompt({title: '添加'+TYPE_TEXT, formType: 2}, function(text, index){
				    layer.close(index);
				    layer.load(1);
				    addItemAjax(text);
				    refresh();
				});
		}
		
		function modifyItem()
		{
			var val=$('input:radio[name="radio"]:checked');
			if (val.length == 0)
			{
				layer.alert('请先选择一条!');
				return false;
			}
			var id = val[0].id;

			layer.prompt({title: '编辑'+TYPE_TEXT, formType: 3}, function(text, index){
			    layer.close(index);
			    layer.load(1);
			    updateItemAjax(id,text);
			    refresh();
			});		
		}
		
		function deleteItem()
		{
			var val=$('input:radio[name="radio"]:checked');
			if (val.length == 0)
			{
				layer.alert('请先选择一条!');
				return false;
			}
			var id = val[0].id;
			
			layer.confirm('确认要删除？', {btn: ['确定','取消']}, 
				function(){
					deleteItemAjax(id);
					refresh();
				}, 
				function(){layer.close();}
			);
		}

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
