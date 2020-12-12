$(function () {
    validateRule();
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
        url: "/zhddkk/wsChatlog/" + action,
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
            time: {
                required: true
            },
            user: {
                required: true
            },
            toUser: {
                required: true
            },
            msg: {
                required: true
            },
            remark: {
                required: true
            }
        },
        messages: {
            time: {
                required: icon + "请输入时间"
            },
            user: {
                required: icon + "请输入发起人"
            },
            toUser: {
                required: icon + "请输入被聊人"
            },
            msg: {
                required: icon + "请输入消息内容"
            },
            remark: {
                required: icon + "请输入备注"
            }
        }
    })
}