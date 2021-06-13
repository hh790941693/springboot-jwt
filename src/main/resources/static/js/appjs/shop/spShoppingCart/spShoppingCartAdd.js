$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});

function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/shop/spShoppingCart/save",
		data : $('#signupForm').serialize(),
		async : false,
		error : function(request) {
			parent.layer.alert("保存失败");
		},
		success : function(r) {
			if (r.code == 1) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name);//获取窗口索引
				parent.layer.close(index);
			} else {
				parent.layer.alert(r.data);
			}
		}
	});
}

//表单验证
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			}
		},
		messages : {
			name : {
				required : icon + "请输入姓名"
			}
		}
	})
}