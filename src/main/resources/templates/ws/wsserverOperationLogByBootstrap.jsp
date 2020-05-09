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

<script type="text/javascript" src="<%=path%>/static/bootstrap/js/popper.js"></script>

<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.min.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.min.js"></script>

<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstraptable/bootstrap-table.min.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstraptable/bootstrap-table.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/bootstraptable/locale/bootstrap-table-zh-CN.min.js"></script>

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
        <div id="toolbar" class="btn-group">
            <button id="btn_add" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
            </button>
            <button id="btn_edit" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改
            </button>
            <button id="btn_delete" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
            </button>
        </div>	
	
		 <table id="tb_departments"></table>
	</div>
	
	<script type="text/javascript">
		
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

		$('#tb_departments').bootstrapTable({
            url: rootUrl+'ws/getOperationLogByPageByBootstrap.json',         //请求后台的URL（*）
            method: 'post',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: function(params){
            	var curPage = Math.floor(params.offset/params.limit)+1;
                var temp = {
                	numPerPage: params.limit,     //每页显示条数
               		curPage:    curPage,          //当前页码
               		search:     params.search,     //搜索框中的关键词
               		sort:       params.sort,       //要排序的字段名称  如果没值则为undefined
               		order:      params.order      //排序方式 asc  desc
                }
                return temp;
            },                    //传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'userName',
                title: '操作用户',
                sortable : true
            },{
                field: 'operModule',
                title: '操作模块'
            }, {
                field: 'operType',
                title: '操作类型',
                sortable : true
            },  {
                field: 'operDescribe',
                title: '操作描述',
                sortable : true
            },{
                field: 'costTime',
                title: '耗时',
                sortable : true
            },{
                field: 'accessTimeText',
                title: '操作时间',
                sortable : true
            },{
                title: "操作",
                align: 'center',
                valign: 'middle',
                width: 160, // 定义列的宽度，单位为像素px
                formatter: function (value, row, index) {
                    return '<button class="btn btn-primary btn-sm" onclick="delRow(\'' + row.id + '\')">删除</button>';
                }
            }]
        });
		

		function refresh(){
			window.location.href = window.location.href;
		};
		
		function delRow(rowId){
			console.log("delete one row:"+rowId);
		}
		
		$("#btn_add").click(function(params){
			var a = $('#tb_departments').bootstrapTable('getData');
			console.log("add item");
		})
		
		$("#btn_delete").click(function(params){
			var a = $('#tb_departments').bootstrapTable('getData');
			console.log("delete item");
		})
		
		window.setInterval(function(){
			refresh();
		},300000);
	</script>
</body>
</html>
