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
        url : "/shop/spOrder/" + action,
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
                                    orderNo : {
                required : true
            },
                                parentOrderNo : {
                required : true
            },
                                totalPrice : {
                required : true
            },
                                payPrice : {
                required : true
            },
                                orderUserId : {
                required : true
            },
                                payUserId : {
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
                                    orderNo : {
                required : icon + "请输入订单号"
            },
                                parentOrderNo : {
                required : icon + "请输入父订单号"
            },
                                totalPrice : {
                required : icon + "请输入总价"
            },
                                payPrice : {
                required : icon + "请输入支付价格"
            },
                                orderUserId : {
                required : icon + "请输入下单用户id"
            },
                                payUserId : {
                required : icon + "请输入支付用户id"
            },
                                status : {
                required : icon + "请输入状态 1：待支付 2:已支付 3:待发货 4:已发货 5:已确认收货"
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