$().ready(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        update();
    }
});
function update() {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/common/generator/update",
        data: $('#signupForm').serialize(),
        async: false,
        error: function (request) {
            parent.layer.alert("网络连接超时");
        },
        success: function (data) {
            if (data.code == 0) {
                parent.layer.msg(data.msg);

            } else {
                parent.layer.msg(data.msg);
            }

        }
    });

}
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            author: {
                required: true
            },
            email: {
                required: true
            },
            package: {
                required: true
            }

        },
        messages: {

            author: {
                required: icon + "请输入作者"
            },
            email: {
                required: icon + "请输入email"
            },
            package: {
                required: icon + "请输入包名"
            }
        }
    })
}
