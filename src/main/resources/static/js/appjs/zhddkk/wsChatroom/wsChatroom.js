var prefix = "/zhddkk/wsChatroom";

var app = new Vue({
	el: '#roomListDiv',
	data: {
		chatRoomList : []
	},
	methods: {
		// 房间列表
		queryChatRoomList : function(){
			var that = this;
			layer.load(1, {
				shade: [0.1,'#fff']
			});

			$.ajax({
				type: "GET",
				url: "/zhddkk/wsChatroom/queryChatRoomList",
				data : {
					'name' : $("#roomNameSearchInput").val(),
					'roomId' : $("#roomIdSearchInput").val(),
				},
				success: function (result) {
					if (result.code == 1) {
						app.chatRoomList = result.data;
					}
					layer.closeAll('loading');
				}
			});
		},
		enterRoom: function (index) {
			var roomInfo = app.chatRoomList[index];
			if (!!roomInfo.password) {
				layer.prompt({title: '请输入房间密码', formType: 1}, function(pass, index){
					layer.close(index);
					if (pass != roomInfo.password) {
						layer.alert('密码不正确', {
							icon: 2,
						});
						return;
					}
					layer.msg("进入房间成功");
					//window.location.href = "/ws/wsSimpleChatRoom.page?roomId="+roomInfo.roomId+"&roomPass="+roomInfo.password;
					window.open("/ws/wsSimpleChatRoom.page?roomId="+roomInfo.roomId+"&roomPass="+roomInfo.password,"_blank");
				});
			} else {
				layer.msg("进入房间成功");
				//window.location.href = "/ws/wsSimpleChatRoom.page?roomId="+roomInfo.roomId+"&roomPass=";
				window.open("/ws/wsSimpleChatRoom.page?roomId="+roomInfo.roomId+"&roomPass="+roomInfo.password,"_blank");
			}
		},
		mouseEnter: function (index) {
			$("#roomListDiv").children("div").eq(index).addClass("selected");
		},
		mouseLeave: function (index) {
			$("#roomListDiv").children("div").eq(index).removeClass("selected");
		},
	},
	created : function() {
		this.queryChatRoomList();
	}
});


function reLoad() {
	app.queryChatRoomList();
}

function cleanForm(){
	$("#roomNameSearchInput").val("");
	$("#roomIdSearchInput").val("");
	app.queryChatRoomList();
}

//添加
function add() {
	layer.open({
		type : 2,
		title : '创建房间',
		maxmin : true,
		shadeClose : false,//点击遮罩关闭层
		area : [ '600px', '420px' ],
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