$(function(){
    validateRule();
    $.ws.initUpload("merchant", "imageClickBtn", "imageUrlInput", "imageShow");
});

//保存数据
function save() {
    $.ajax({
        cache : true,
        type : "POST",
        url : "/shop/spMerchant/update",
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
                    },
        messages : {
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
                    }
    })
}