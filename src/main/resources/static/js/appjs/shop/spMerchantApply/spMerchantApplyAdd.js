$().ready(function() {
	validateRule();
    initUpload();
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
		url : "/shop/spMerchantApply/save",
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
			},
            address : {
                required : true
            },
            image : {
                required : true
            },
            desc : {
                required : true
            },
            contact : {
                required : true
            },
		},
		messages : {
			name : {
				required : icon + "请输入店铺名称"
			},
            address : {
                required : icon + "请输入店铺地址"
            },
            image : {
                required : icon + "请上传店铺图片"
            },
            desc : {
                required : icon + "请输入店铺描述"
            },
            contact : {
                required : icon + "请输入联系电话"
            },
		}
	})
}

// 初始化上传模块
function initUpload() {
    layui.upload.render({
        elem: '#imageBtn',
        url: $.ws.uploadFileUrl,
        size: 10240,//单位为KB
        accept: 'images',
        data: {
            "folder": "merchant"
        },
        before: function () {
            layer.load();
        },
        done: function (r) {
            layer.closeAll('loading');
            if (r.code == 1) {
                $("#image").attr("src", r.data);
                $("#merchantImageInput").val(r.data);
            }
        },
        error: function () {
            layer.closeAll('loading');
        }
    });
}