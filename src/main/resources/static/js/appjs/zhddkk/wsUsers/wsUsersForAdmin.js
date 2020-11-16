var prefix = "/zhddkk/wsUsers";
$(function() {
	load();
	$('#exportDataBtn').attr('href', prefix + "/exportUser.do");
});

function load() {
	$('#exampleTable').bootstrapTable({
		method : 'get',
		url : prefix + "/wsUsersListForAdmin",
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
		showColumns : false,// 是否显示内容下拉框（选择显示的列）
		sidePagination : "server",//设置在哪里进行分页，可选值为"client" 或者 "server"
		queryParamsType : "",
		//设置为limit则会发送符合RESTFull格式的参数
		queryParams : function(params) {
			return {
				//传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
				pageNumber : params.pageNumber,
				pageSize : params.pageSize,
				name:$('#nameSearchInput').val(),
				state:$('#stateSelect').val(),
				enable:$('#enableSelect').val(),
				speak:$('#speakSelect').val()
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
				field : 'name',
				title : '姓名',
                formatter : function (value) {
                    var res = "<a href='javascript:void(0);' style='text-decoration: none;font-size:17px;color:blue;' onclick='queryPersonInfo(this)'>" + value + "</a>";
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
				field : 'state',
				title : '是否在线',
				align: 'center',
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
				field : 'registerTime',
				title : '注册时间'
			},
						{
				field : 'lastLoginTime',
				title : '上次登录时间',
			},
						{
				field : 'lastLogoutTime',
				title : '上次登出时间',
				visible :false
			},
						{
				field : 'enable',
				title : '是否可用',
				formatter : function(value){
					var res = "";
					if (value == "0"){
						res = '<span class="label label-danger">不可用</span>';
					}else{
						res = '<span class="label label-primary">可用</span>';
					}
					return res;
				},
				visible : false
			},
						{
				field : 'speak',
				title : '是否禁言',
				formatter : function(value){
					var res = "";
					if (value == "0"){
						res = '<span class="label label-danger">禁言中</span>';
					}else{
						res = '<span class="label label-primary">没有禁言</span>';
					}
					return res;
				},
				visible : false
			},
						{
				field : 'createTime',
				title : '创建时间',
				visible : false
			},
			{
				field : 'sendMsg',
				title : '聊天内容',
				formatter : function (value, row, index) {
					var inputId = "msgId"+row.id;
					return "<input type='text' name='sendMsg' id='"+inputId+"'>";
				}
			},
						{
				title : '操作',
				field : 'id',
				align : 'center',
				formatter : function(value, row, index) {
					var btns = "";

                    //是否在线 0:离线 1:在线
                    if (row.state == "1"){
                        var offLineBtn = '<a class="btn btn-danger btn-sm" href="#" title="下线"  mce_href="#" onclick="offlineUser(\''
                            + row.id
                            + '\')">下线</a> ';
                        btns += offLineBtn;
                    }else{
                        var offLineDisableBtn = '<a disabled="disabled" readonly="readonly" class="btn btn-default btn-sm" href="#" title="下线"  mce_href="#" onclick="javascript:void(0);">下线</a> ';
                        btns += offLineDisableBtn;
                    }

					//是是否禁言  0:禁言 1：没有禁言
					if (row.speak == "0"){
						var enableSpeakBtn = '<a class="btn btn-info btn-sm" href="#" title="开言"  mce_href="#" onclick="operSpeakUser(\''
							+ row.id
							+ '\',\''
							+ '1'
							+ '\')">开言</a> ';
						btns += enableSpeakBtn;
					}else{
						var disableSpeakBtn = '<a class="btn btn-warning btn-sm" href="#" title="禁言"  mce_href="#" onclick="operSpeakUser(\''
							+ row.id
							+ '\',\''
							+ '0'
							+ '\')">禁言</a> ';
						btns += disableSpeakBtn;
					}

					//是否可用 0:不可用  1:可用
					if (row.enable == "0"){
						var enableBtn = '<a class="btn btn-primary btn-sm" href="#" title="启用"  mce_href="#" onclick="operEnableUser(\''
							+ row.id
							+ '\',\''
							+ '1'
							+ '\')">启用</a> ';
						btns += enableBtn;
					}else{
						var disableBtn = '<a class="btn btn-danger btn-sm" href="#" title="禁用"  mce_href="#" onclick="operEnableUser(\''
							+ row.id
							+ '\',\''
							+ '0'
							+ '\')">禁用</a> ';
						btns += disableBtn;
					}

					var sendBtn = '<a class="btn btn-success btn-sm" href="#" title="发送"  mce_href="#" onclick="sendMsg(\''
						+ row.id
						+ '\',\''
						+ row.name
						+ '\')">发送</a> ';
                    btns += sendBtn;
					return btns;
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
	$("#stateSelect").val("");
	$("#enableSelect").val("");
	$("#speakSelect").val("");
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

function addAsFriends(id){
	$.ajax({
		url : prefix+"/addAsFriends",
		type : "post",
		data : {
			'curUser' : user,
			'toUserId' : id
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
}

function offlineUser(id){
	$.ajax({
		type: 'POST',
		url: prefix+'/offlineUser.do',
		data:{'id':id},
		success: function(result) {
			if (result.code == 1){
				layer.msg(result.msg);
				reLoad();
			}else{
				layer.msg(result.msg);
			}
		}
	});
}

function operEnableUser(id, status){
	$.ajax({
		type: 'POST',
		url: prefix+'/operEnableUser.do',
		data:{'id':id,'status':status},
		success: function(result) {
			if (result.code == 1){
				layer.msg(result.msg);
				reLoad();
			}else{
				layer.msg(result.msg);
			}
		}
	});
}

function operSpeakUser(id, status){
	$.ajax({
		type: 'POST',
		url: prefix+'/operSpeakUser.do',
		data:{'id':id,'status':status},
		success: function(result) {
			if (result.code == 1){
				layer.msg(result.msg);
				reLoad();
			}else{
				layer.msg(result.msg);
			}
		}
	});
}

function sendMsg(id, user){
	console.log("发送消息 id:"+id);
	var inputId = "msgId"+id;
	var msg = $("#"+inputId).val();

	if (msg == null || msg == ""){
		layer.tips('消息不能为空!', "#"+inputId, {
			tips: [1, '#0FA6D8'],
			tipsMore: false,
			time:2000
		});
		return;
	}

	var toServerMsgObj = {};
	toServerMsgObj.typeId = 2;
	toServerMsgObj.typeDesc = "在线消息";
	toServerMsgObj.from = "admin";
	toServerMsgObj.to = user;
	toServerMsgObj.msg = msg;
	var jsonObj = JSON.stringify(toServerMsgObj);
	parent.webSocket.send(jsonObj);
	layer.tips('发送成功!', "#"+inputId, {
		tips: [1, '#0FA6D8'],
		tipsMore: false,
		time:1000
	});
	$("#"+inputId).val("");
}

//查看个人信息
function queryPersonInfo(thisObj){
    var user=$(thisObj).text();
    layer.open({
        type: 2,
        title: '用户个人信息',
        shadeClose: true,
        shade: 0.2,
        area: ['390px', '420px'],
        content: prefix+'/showPersonalInfo.page?user='+user
    });
}