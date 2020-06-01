var prefix = "/zhddkk/wsFriends";
var user=$("#user").val();

$(function() {
	load();
});

function load() {
	$('#exampleTable').bootstrapTable({
		method : 'get',
		url : prefix + "/list",
		//showRefresh : true,
		//showToggle : true,
		//showColumns : true,
		iconSize : 'outline',
		toolbar : '#exampleToolbar',
		striped : true,//设置为true会有隔行变色效果
		dataType : "json",//服务器返回的数据类型
		pagination : true,//设置为true会在底部显示分页条
		singleSelect : false,//设置为true将禁止多选
		//contentType : "application/x-www-form-urlencoded",//发送到服务器的数据编码类型
		pageSize : 6,//如果设置了分页，每页数据条数
		pageNumber : 1,//如果设置了分布，首页页码
		//search : true,// 是否显示搜索框
		showColumns : false,// 是否显示内容下拉框（选择显示的列）
		sidePagination : "server",//设置在哪里进行分页，可选值为"client" 或者 "server"
		queryParamsType : "",
		//设置为limit则会发送符合RESTFull格式的参数
		queryParams : function(params) {
			return {
				//传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
				pageNumber : params.pageNumber,
				pageSize : params.pageSize,
				uname : user,
				fname : $('#nameSearchInput').val()
				//name:$('#searchName').val(),
				//username:$('#searchName').val()
			};
		},
		// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
		// queryParamsType = 'limit' ,返回参数必须包含
		// limit, offset, search, sort, order 否则, 需要包含:
		// pageSize, pageNumber, searchText, sortName,
		// sortOrder.
		// 返回false将会终止请求
		responseHandler : function(res){
			return {
				"total" : res.data.total,//总数
				"rows" : res.data.records//数据
			 };
		},
		columns : [
						{
				field : 'id',
				title : '主键'
			},
						{
				field : 'uid',
				title : '用户id',
				visible : false
			},
						{
				field : 'uname',
				title : '用户姓名',
				visible : false
			},
						{
				field : 'fid',
				title : '好友id',
				visible : false
			},
						{
				field : 'fname',
				title : '好友姓名'
			},
			{
				field : 'state',
				title : '是否在线',
				formatter : function (value, row) {
					var res = "";
					if (value == "0"){
						res = '<span class="label label-default">离线</span>';
					}else{
						res = '<span class="label label-primary">在线</span>';
					}
					return res;
				}
			},
			{
				field : 'headImage',
				title : '头像',
				formatter : function (value) {
					var result = "";
					if (value != "" && value != null) {
						result += "<div style='text-align: center;'>";
						result += "<img  onclick=\"$.ws.gShowImg('" + value + "')\"  style='height:50px;width:60px;margin-right:10px;background:transparent;CURSOR:pointer;' src='" + value + "'/>"
						result += "</div>";
					}
					return result;
				}
			},
						{
				field : 'createTime',
				title : '添加时间'
			},
						{
				field : 'remark',
				title : '备注',
				visible : false
			},
						{
				title : '操作',
				field : 'id',
				align : 'center',
				formatter : function(value, row, index) {
					var d = '<a class="btn btn-warning btn-sm" href="#" title="删除"  mce_href="#" onclick="remove(\''
							+ row.id
							+ '\')"><i class="fa fa-remove"></i></a> ';
					return d;
				}
			}
		]
	});
}

function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

function cleanForm(){
	$("#nameSearchInput").val("");
	reLoad();
}

function add() {
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false,//点击遮罩关闭层
		area : [ '1000px', '520px' ],
		content : prefix + '/add'
	});
}

function edit(id) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false,//点击遮罩关闭层
		area : [ '1000px', '520px' ],
		content : prefix + '/edit/' + id
	});
}

function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code == 1) {
					layer.msg(r.msg);
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			},
			fail : function (r) {
				console.log("failed to remove");
			},
			error : function (r) {
				console.log("error to remove");
			}
		});
	})
}

function batchRemove() {
    //返回所有选择的行，当没有选择的记录时，返回一个空数组
	var rows = $('#exampleTable').bootstrapTable('getSelections');
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	}, function() {
		var ids = new Array();
		$.each(rows, function(i, row) {
			ids[i] = row['id'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 1) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {

	});
}