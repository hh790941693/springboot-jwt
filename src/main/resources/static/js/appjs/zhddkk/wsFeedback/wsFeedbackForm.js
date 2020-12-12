var user = $('#user').val();
$(function () {
    validateRule();
    initUpload();
});

//保存数据
function save() {
    var action = "save";
    if ($("#id").val()) {
        action = "update";
    }
    $.ajax({
        cache: true,
        type: "POST",
        url: "/zhddkk/wsFeedback/" + action,
        data: $('#form').serialize(),
        async: false,
        error: function () {
            parent.layer.alert("保存失败，请稍后再试");
        },
        success: function (data) {
            if (data.code == 1) {
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

function initUpload() {
    //上传文件url
    layui.upload.render({
        elem: '#imageBtn',
        url: $.ws.uploadUrl,
        size: 10240,//单位为KB
        accept: 'images',
        data: {
            "folder": "feedback",
            "user": user
        },
        before: function () {
            layer.load();
        },
        done: function (r) {
            layer.closeAll('loading');
            if (r.code === "1") {
                $("#picUrl").val(r.data);
                $("#imageImg").attr("src", r.data);
                $("#imageDiv").show();
            }
        },
        error: function () {
            layer.closeAll('loading');
        }
    });
}

//表单验证
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#form").validate({
        rules: {
            userId: {
                required: true
            },
            userName: {
                required: true
            },
            type: {
                required: true
            },
            title: {
                required: true
            },
            content: {
                required: true
            },
            picUrl: {
                required: true
            }
        },
        messages: {
            userId: {
                required: icon + "请输入用户id"
            },
            userName: {
                required: icon + "请输入用户名称"
            },
            type: {
                required: icon + "请输入反馈类型 1:建议 2:问题"
            },
            title: {
                required: icon + "请输入标题"
            },
            content: {
                required: icon + "请输入问题描述"
            },
            picUrl: {
                required: icon + "请输入图片url"
            }
        }
    })
}