var prefix = "/zhddkk/wsFriendsApply";
var user=$('#user').val();
$(function() {
    load();
});

function load() {
    $('#exampleTable').bootstrapTable({
        method : 'get',
        url : prefix + "/friendApplyList",
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
        pageSize : 10,//如果设置了分页，每页数据条数
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
                toName : user
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
                field : 'fromId',
                title : '申请人id',
                visible : false
            },
            {
                field : 'fromName',
                title : '申请人姓名'
            },
            {
                field : 'toId',
                title : '好友id',
                visible : false
            },
            {
                field : 'toName',
                title : '好友姓名',
                visible : false
            },
            {
                field : 'processStatus',
                title : '申请状态',
                formatter : function (value) {
                    var res = "";
                    if (value == 1){
                        res = '<span class="label label-primary">申请中</span>';
                    }else if (value == 2){
                        res = '<span class="label label-danger">已拒绝</span>';
                    }else if (value == 3){
                        res = '<span class="label label-info">已同意</span>';
                    }
                    return res;
                }
            },
            {
                field : 'createTime',
                title : '创建时间'
            },
            {
                title : '操作',
                field : 'id',
                align : 'center',
                formatter : function(value, row, index) {
                    if (row.processStatus == 1){
                        var agreeFriendBtn = '<a class="btn btn-info btn-sm '+s_remove_h+'" href="#" title="同意"  mce_href="#" onclick="operateFriendApply(\''
                            + row.id
                            + '\',\''
                            + '3'
                            + '\')">同意</a> ';
                        var deagreeFriendBtn = '<a class="btn btn-danger btn-sm '+s_remove_h+'" href="#" title="拒绝"  mce_href="#" onclick="operateFriendApply(\''
                            + row.id
                            + '\',\''
                            + '2'
                            + '\')">拒绝</a> ';

                        return agreeFriendBtn+deagreeFriendBtn;
                    }
                    return "";
                }
            }
        ]
    });
}

function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
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
                if (r.code==0) {
                    layer.msg(r.msg);
                    reLoad();
                }else{
                    layer.msg(r.msg);
                }
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
                if (r.code == 0) {
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


// 同意/拒绝好友请求
function operateFriendApply(id, status)
{
    layer.confirm("确认要操作吗", {
        btn : [ '确定', '取消' ]
    }, function() {
        $.ajax({
            type: 'POST',
            url: prefix + "/operateFriendApply",
            data: {'id': id, 'status': status},
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                }
            }
        });
    });
}