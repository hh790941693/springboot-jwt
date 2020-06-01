var prefix = "/zhddkk/wsFeedback";
$(function() {
	load();
});

//加载表格
function load() {
	$('#exampleTable').bootstrapTable({
		method : 'get',
		url : prefix + "/adminFeedbackList",
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
		pageSize : 4,//如果设置了分页，每页数据条数
		pageNumber : 1,//如果设置了分布，首页页码
		//search : true,// 是否显示搜索框
		showColumns : true,// 是否显示内容下拉框（选择显示的列）
		sidePagination : "server",//设置在哪里进行分页，可选值为"client" 或者 "server"
		queryParamsType : "",
		//设置为limit则会发送符合RESTFull格式的参数
		queryParams : function(params) {
			return {
				//传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
				pageNumber : params.pageNumber,
				pageSize : params.pageSize,
                userName : $('#nameSearchInput').val(),
                status : $('#statusSelect').val(),
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
				field : 'userId',
				title : '用户id',
                visible : false
			},
						{
				field : 'userName',
				title : '用户名称'
			},
						{
				field : 'type',
				title : '反馈类型',
                formatter:function (value) {
                    var res = "";
                    if (value == "1"){
                        res = "<span class='label label-primary'>建议</span>";
                    }else if (value == "2"){
                        res = "<span class='label label-warning'>问题</span>";
                    }

                    return res;
                }
			},
						{
				field : 'title',
				title : '标题'
			},
						{
				field : 'content',
				title : '问题描述'
			},
						{
				field : 'picUrl',
				title : '图片url',
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
				field : 'replyContent',
				title : '答复内容'
			},
						{
				field : 'replyTime',
				title : '答复时间',
                visible : false
			},
						{
				field : 'createTime',
				title : '创建时间',
                visible : false
			},
						{
				field : 'updateTime',
				title : '更新时间',
                visible: false
			},
						{
				field : 'status',
				title : '状态',
                formatter :function (value) {
                    var res = "";
                    if (value == "1"){
                        res = "<span class='label label-success'>待答复</span>";
                    }else if (value == "2"){
                        res = "<span class='label label-primary'>已答复</span>";
                    }

                    return res;
                }
			},
						{
				title : '操作',
				field : 'id',
				align : 'center',
				formatter : function(value, row, index) {
					var d = '<a class="btn btn-danger btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
							+ row.id
							+ '\')"><i class="fa fa-remove"></i></a> ';
					var replyBtn = '<a class="btn btn-primary btn-sm" href="#" title="答复"  mce_href="#" onclick="reply(\''
                        + row.id
                        + '\')">答复</a> ';
					if (row.status == 1){
					    return replyBtn+d;
                    }
					return d;
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
    $('#nameSearchInput').val("");
    $('#statusSelect').val("");
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
		content : prefix + '/add?user='+username
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
				if (r.code==1) {
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

function reply(id){
    layer.prompt({title: '随便写点啥，并确认', formType: 2}, function(text, index){
        layer.close(index);
        $.ajax({
            type : 'POST',
            data : {
                "id" : id,
                "replyContext" : text
            },
            url : prefix + '/replyFeedback',
            success : function(r) {
                if (r.code == 1) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    });
}