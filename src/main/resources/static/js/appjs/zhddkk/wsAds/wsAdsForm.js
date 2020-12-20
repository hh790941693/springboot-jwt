$(function () {
    validateRule();

    $('#content').editable({
        inlineMode: false,
        alwaysBlank: true,
        height: '300px',
        language: "zh_cn",
        imageUploadURL: $.ws.uploadByFroalaUrl,
        imageUploadParams: {folder: "ads"}
    }).on('editable.afterRemoveImage', function (e, editor, $img) {
        // Set the image source to the image delete params.
        editor.options.imageDeleteParams = {src: $img.attr('src')}
    });
    var markupStr = $('#contentInput').val();
    $('#content')[0].childNodes[1].innerHTML = markupStr;
});

//保存数据
function save() {
    var markupStr = $('#content')[0].childNodes[1].innerHTML;
    $('#contentInput').val(markupStr);

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
        error: function () {
            parent.layer.alert("保存失败，请稍后再试");
        },
        success: function (data) {
            if (data.code === 1) {
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
            }
        },
        messages: {
            title: {
                required: icon + "请输入标题"
            },
            content: {
                required: icon + "请输入内容"
            }
        }
    });
}