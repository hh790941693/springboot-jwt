$(function () {
    validateRule();
    initUpload();

    // froala编辑器初始化
    initH5Editor("content", "contentInput", "ads");
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
        imageUploadParams: {folder: folder}
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
    var markupStr = setH5EditorData();
    var backImg = $("#backImgInput").val();

    if (!markupStr || typeof(markupStr) == "undefined") {
        parent.layer.alert("请填写内容");
        return;
    }

    if (!backImg || typeof(backImg) == "undefined") {
        parent.layer.alert("请上传背景图片");
        return;
    }

    var action = "save";
    if ($("#id").val()) {
        action = "update";
    }
    $.ajax({
        cache: true,
        type: "POST",
        url: "/zhddkk/wsAds/" + action,
        data: $('#form').serialize(),
        async: false,
        error: function (result) {
            parent.layer.alert("保存失败:"+result.msg);
        },
        success: function (data) {
            if (data.code === 1) {
                parent.layer.msg("保存成功");
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name);//获取窗口索引
                parent.layer.close(index);
            } else {
                parent.layer.alert("保存失败:"+data.msg);
            }
        }
    });
}

//表单提交拦截
$.validator.setDefaults({
    submitHandler: function () {
        save();
    }
});

//表单验证
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#form").validate({
        rules: {
            title: {
                required: true
            },
            content: {
                required: true
            },
            backImg: {
                required: true
            }
        },
        messages: {
            title: {
                required: icon + "请输入标题"
            },
            content: {
                required: icon + "请输入内容"
            },
            backImg: {
                required: icon + "请上传背景图片"
            }
        }
    });
}

// 初始化上传模块
function initUpload() {
    layui.upload.render({
        elem: '#imageBtn',
        url: $.ws.uploadFileUrl,
        size: 10240,//单位为KB
        accept: 'images',
        data: {
            "folder": "adsBackImg"
        },
        before: function () {
            layer.load();
        },
        done: function (r) {
            layer.closeAll('loading');
            if (r.code == 1) {
                $("#backImg").attr("src", r.data);
                $("#backImgInput").val(r.data);
            }
        },
        error: function () {
            layer.closeAll('loading');
        }
    });
}

// 预览图片
function showImg(imgObj) {
    $.ws.gShowImg($(imgObj).attr("src"));
}