$(function(){
    validateRule();
    $.ws.initUpload("goods", "imageClickBtn", "imageUrlInput", "imageShow");
    $.ws.initUpload("goods", "imageClickBtn1", "imageUrlInput1", "imageShow1");
    $.ws.initUpload("goods", "imageClickBtn2", "imageUrlInput2", "imageShow2");
    $.ws.initUpload("goods", "imageClickBtn3", "imageUrlInput3", "imageShow3");
    $.ws.initUpload("goods", "imageClickBtn4", "imageUrlInput4", "imageShow4");
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
        url : "/shop/spGoods/" + action,
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
                                name : {
                required : true
            },
                                brief : {
                required : true
            },
                                place : {
                required : true
            },
                                goodsTypeId : {
                required : true
            },
                                stockNum : {
                required : true
            },
                                originalPrice : {
                required : true
            },
                                salePrice : {
                required : true
            },
                                unitName : {
                required : true
            },
                                backImage : {
                required : true
            },
                    },
        messages : {
                                name : {
                required : icon + "请输入商品名称"
            },
                                brief : {
                required : icon + "请输入商品简介"
            },
                                place : {
                required : icon + "请输入商品生产地"
            },
                                goodsTypeId : {
                required : icon + "请输入商品类型id"
            },
                                stockNum : {
                required : icon + "请输入库存数"
            },
                                originalPrice : {
                required : icon + "请输入原价"
            },
                                salePrice : {
                required : icon + "请输入售价"
            },
                                unitName : {
                required : icon + "请输入商品单位"
            },
                                backImage : {
                required : icon + "请上传封面图片"
            },
                    }
    })
}