$(function(){
    validateRule();
});

//保存数据
function save() {
    var action = "save";
    if($("#id").val()){
        action = "update";
    }
    $.ajax({
        cache : true,
        type : "POST",
        url : "/shop/spMerchant/" + action,
        data : $('#form').serialize(),
        async : false,
        error : function() {
            parent.layer.alert("保存失败，请稍后再试");
        },
        success : function(r) {
            if (r.code == 1) {
                parent.layer.msg("保存成功");
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name);//获取窗口索引
                parent.layer.close(index);
            } else {
                parent.layer.alert("保存失败，请稍后再试");
            }

        }
    });
}

//表单提交拦截
$.validator.setDefaults({
    submitHandler : function() {
        save();
    }
});

//表单验证
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#form").validate({
        rules : {
                                    userId : {
                required : true
            },
                                merchantId : {
                required : true
            },
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
                                status : {
                required : true
            },
                                createTime : {
                required : true
            },
                                updateTime : {
                required : true
            },
                    },
        messages : {
                                    userId : {
                required : icon + "请输入商家id"
            },
                                merchantId : {
                required : icon + "请输入店铺id"
            },
                                name : {
                required : icon + "请输入店铺名称"
            },
                                address : {
                required : icon + "请输入店铺地址"
            },
                                image : {
                required : icon + "请输入店铺图片"
            },
                                desc : {
                required : icon + "请输入店铺描述"
            },
                                contact : {
                required : icon + "请输入联系电话"
            },
                                status : {
                required : icon + "请输入店铺状态 0:已关闭 1:营业中 2:暂停营业 3:已打烊"
            },
                                createTime : {
                required : icon + "请输入创建时间"
            },
                                updateTime : {
                required : icon + "请输入更新时间"
            },
                    }
    })
}