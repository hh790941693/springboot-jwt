var prefix = "/zhddkk/wsChatroom";

var app = new Vue({
	el: '#roomListDiv',
	data: {
		chatRoomList : [],
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
				layer.prompt({title: '请输入房间密码', formType: 3}, function(pass, index){
					layer.close(index);
					if (pass != roomInfo.password) {
						layer.alert('密码不正确', {
							icon: 2,
						});
						return;
					}
					window.open("/zhddkk/wsChatroom/wsSimpleChatRoom.page?roomId="+roomInfo.roomId+"&roomPass="+roomInfo.password,"_blank");
				});
			} else {
				window.open("/zhddkk/wsChatroom/wsSimpleChatRoom.page?roomId="+roomInfo.roomId+"&roomPass=NO-PASSWORD","_blank");
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
		area : [ '600px', '420px' ],
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

$("#keywordLike li").click(function() {
	var kv=$(this).text();
	$("#roomNameSearchInput").val(kv);
	app.queryChatRoomList();
})