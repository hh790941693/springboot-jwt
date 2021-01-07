var prefix = "/zhddkk/sysMenu";

$(function() {
	load();
});

//加载表格
function load() {
	$('#exampleTable').bootstrapTreeTable({
        id : 'id',
        code :'id',
        parentCode : 'parentId',
        type : "GET", // 请求数据的ajax类型
        url : prefix + '/list', // 请求数据的ajax的url
        ajaxParams : {}, // 请求数据的ajax的data属性
        expandColumn : '1',// 在哪一列上面显示展开按钮
        striped : true, // 是否各行渐变色
        bordered : true, // 是否显示边框
        expandAll : false, // 是否全部展开
		queryParamsType : "",

		//设置为limit则会发送符合RESTFull格式的参数
		queryParams : function(params) {
			return {
				//传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
				pageNumber : params.pageNumber,
				pageSize : params.pageSize
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
                title : '菜单id',
                field : 'id',
                visible : false,
                align : 'center',
                valign : 'middle',
                width : '50px'
			},
						{
				field : 'name',
				title : '菜单名称'
			},
						{
				field : 'parentId',
				title : '父菜单id'
			},
						{
				field : 'url',
				title : 'url'
			},
						{
				field : 'icon',
				title : '图标名称',
                align : 'center',
                valign : 'middle',
			},
						{
				field : 'extColumn1',
				title : '扩展字段1'
			},
						{
				field : 'extColumn2',
				title : '扩展字段2'
			},
						{
				field : 'extColumn3',
				title : '扩展字段3'
			},
						{
				field : 'createTime',
				title : '创建时间'
			},
						{
				field : 'updateTime',
				title : '更新时间'
			},
						{
				title : '操作',
				field : 'id',
				align : 'center',
				formatter : function(value, row, index) {
					var e = '<a class="btn btn-warning btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
							+ row.id
							+ '\')"><i class="fa fa-edit"></i></a> ';
					var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
							+ row.id
							+ '\')"><i class="fa fa-remove"></i></a> ';
					return e+d;
				}
			}
		]
	});
}

//重新加载表格
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

//重置
function cleanForm(){
	reLoad();
}

//添加
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

//编辑
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

//删除一条记录
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
			}
		});
	})
}

//批量删除记录
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