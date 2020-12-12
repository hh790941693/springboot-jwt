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
        url: "/zhddkk/wsUsers/" + action,
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
            name: {
                required: true
            },
            password: {
                required: true
            },
            registerTime: {
                required: true
            },
            state: {
                required: true
            },
            lastLoginTime: {
                required: true
            },
            lastLogoutTime: {
                required: true
            },
            enable: {
                required: true
            },
            speak: {
                required: true
            },
            question1: {
                required: true
            },
            answer1: {
                required: true
            },
            question2: {
                required: true
            },
            answer2: {
                required: true
            },
            question3: {
                required: true
            },
            answer3: {
                required: true
            },
            createTime: {
                required: true
            }
        },
        messages: {
            name: {
                required: icon + "请输入姓名"
            },
            password: {
                required: icon + "请输入密码"
            },
            registerTime: {
                required: icon + "请输入注册时间"
            },
            state: {
                required: icon + "请输入是否在线 0:离线 1:在线"
            },
            lastLoginTime: {
                required: icon + "请输入上次登录时间"
            },
            lastLogoutTime: {
                required: icon + "请输入上次登出时间"
            },
            enable: {
                required: icon + "请输入是否可用 0:不可用  1:可用"
            },
            speak: {
                required: icon + "请输入是否禁言  0:禁言 1：没有禁言"
            },
            question1: {
                required: icon + "请输入问题1"
            },
            answer1: {
                required: icon + "请输入答案1"
            },
            question2: {
                required: icon + "请输入问题2"
            },
            answer2: {
                required: icon + "请输入答案2"
            },
            question3: {
                required: icon + "请输入问题3"
            },
            answer3: {
                required: icon + "请输入答案3"
            },
            createTime: {
                required: icon + "请输入创建时间"
            }
        }
    })
}