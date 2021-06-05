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
        url : "/shop/spOrderDetail/" + action,
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
                                goodsId : {
                required : true
            },
                                goodsCount : {
                required : true
            },
                                goodsPrice : {
                required : true
            },
                                merchantId : {
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
                                goodsId : {
                required : icon + "请输入商品id"
            },
                                goodsCount : {
                required : icon + "请输入商品数量"
            },
                                goodsPrice : {
                required : icon + "请输入商品售价"
            },
                                merchantId : {
                required : icon + "请输入商品所属的商家id"
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