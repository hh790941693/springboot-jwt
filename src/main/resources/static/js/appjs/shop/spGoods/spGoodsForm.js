$(function(){
    validateRule();
    $.ws.initUpload("goods", "imageClickBtn", "imageUrlInput", "imageShow");
    $.ws.initUpload("goods", "imageClickBtn1", "imageUrlInput1", "imageShow1");
    $.ws.initUpload("goods", "imageClickBtn2", "imageUrlInput2", "imageShow2");
    $.ws.initUpload("goods", "imageClickBtn3", "imageUrlInput3", "imageShow3");
    $.ws.initUpload("goods", "imageClickBtn4", "imageUrlInput4", "imageShow4");

    // froala编辑器初始化
    initH5Editor("description", "descriptionInput", "goodsDetail");
});

/**
 * froala编辑器初始化
 * @param divId     用于页面展示用的div
 * @param inputId   用于传递h5数据到后台的input
 * @param folder    图片上传目录
 */
function initH5Editor(divId, inputId, folder){
    $('#'+divId).editable({
        inlineMode: false,
        alwaysBlank: true,
        height: '300px',
        language: "zh_cn",
        imageUploadURL: $.ws.uploadByFroalaUrl,
        imageUploadParams: {category: folder}
    }).on('editable.afterRemoveImage', function (e, editor, $img) {
        // Set the image source to the image delete params.
        editor.options.imageDeleteParams = {src: $img.attr('src')}
    });
    var markupStr = $('#'+inputId).val();
    $('#'+divId)[0].childNodes[1].innerHTML = markupStr;
}

/**
 * 从编辑器区域获取h5数据,并赋值到input中,方便传递到后台
 * @param divId    用于页面展示用的div
 * @param inputId  用于传递h5数据到后台的input
 */
function setH5EditorData(divId, inputId){
    var markupStr = $('#'+divId)[0].childNodes[1].innerHTML;
    $('#'+inputId).val(markupStr);
    return markupStr;
}

//保存数据
function save() {
    var action = "save";
    if($("#id").val()){
        action = "update";
    }
    // 从编辑器区域获取h5数据,并赋值到input中,方便传递到后台
    var markupStr = setH5EditorData("description", "descriptionInput");
    if (!markupStr || typeof(markupStr) == "undefined") {
        parent.layer.alert("请填写商品详细内容");
        return;
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