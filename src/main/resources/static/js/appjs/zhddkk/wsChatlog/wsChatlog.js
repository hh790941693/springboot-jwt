var prefix = "/zhddkk/wsChatlog";

$(function () {
    load();
    initTimePicker();
});

function initTimePicker() {
    layui.laydate.render({
        elem: '#beginTime',
        type: 'datetime'
    });
    layui.laydate.render({
        elem: '#endTime',
        type: 'datetime'
    });
}

function load() {
    $('#exampleTable').bootstrapTable({
        method: 'get',
        url: prefix + "/list",
        //showRefresh : true,
        //showToggle : true,
        //showColumns : true,
        iconSize: 'outline',
        toolbar: '#exampleToolbar',
        striped: true,//设置为true会有隔行变色效果
        dataType: "json",//服务器返回的数据类型
        pagination: true,//设置为true会在底部显示分页条
        singleSelect: false,//设置为true将禁止多选
        //contentType : "application/x-www-form-urlencoded",//发送到服务器的数据编码类型
        pageSize: 6,//如果设置了分页，每页数据条数
        pageNumber: 1,//如果设置了分布，首页页码
        //search : true,// 是否显示搜索框
        showColumns: false,// 是否显示内容下拉框（选择显示的列）
        sidePagination: "server",//设置在哪里进行分页，可选值为"client" 或者 "server"
        queryParamsType: "",
        //设置为limit则会发送符合RESTFull格式的参数
        queryParams: function (params) {
            return {
                //传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                pageNumber: params.pageNumber,
                pageSize: params.pageSize,
                roomName: $('#roomName').val(),
                user: $('#fromUser').val(),
                toUser: $('#toUser').val(),
                beginTime: $('#beginTime').val(),
                endTime: $('#endTime').val()
            };
        },
        // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
        // queryParamsType = 'limit' ,返回参数必须包含
        // limit, offset, search, sort, order 否则, 需要包含:
        // pageSize, pageNumber, searchText, sortName,
        // sortOrder.
        // 返回false将会终止请求
        responseHandler: function (res) {
            return {
                "total": res.data.total,//总数
                "rows": res.data.records//数据
            };
        },
        columns: [
            {
                field: 'id',
                title: '主键'
            },
            {
                field: 'roomName',
                title: '房间名称'
            },
            {
                field: 'user',
                title: '发起人'
            },
            {
                field: 'toUser',
                title: '被聊人'
            },
            {
                field: 'msg',
                title: '消息内容',
                formatter: function (value) {
                    value = eval("'"+value+"'");
                    return $.ws.subStringText(value, 30);
                }
            },
            {
                field: 'remark',
                title: '备注',
                visible: false
            },
            {
                field: 'time',
                title: '创建时间'
            },
            {
                title: '操作',
                field: 'id',
                align: 'center',
                formatter: function (value, row, index) {
                    var e = '<a class="btn btn-warning btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                        + row.id
                        + '\')"><i class="fa fa-edit"></i></a> ';
                    var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                        + row.id
                        + '\')"><i class="fa fa-remove"></i></a> ';
                    return "";
                }
            }
        ]
    });
}

function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}

function cleanForm() {
    $('#roomName').val("");
    $("#fromUser").val("");
    $("#toUser").val("");
    $("#beginTime").val("");
    $("#endTime").val("");
    reLoad();
}

function add() {
    layer.open({
        type: 2,
        title: '增加',
        maxmin: true,
        shadeClose: false,//点击遮罩关闭层
        area: ['1000px', '520px'],
        content: prefix + '/add'
    });
}

function edit(id) {
    layer.open({
        type: 2,
        title: '编辑',
        maxmin: true,
        shadeClose: false,//点击遮罩关闭层
        area: ['1000px', '520px'],
        content: prefix + '/edit/' + id
    });
}

function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'id': id
            },
            success: function (r) {
                if (r.code == 1) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
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
        btn: ['确定', '取消']
    }, function () {
        var ids = new Array();
        $.each(rows, function (i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "ids": ids
            },
            url: prefix + '/batchRemove',
            success: function (r) {
                if (r.code == 1) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {

    });
}