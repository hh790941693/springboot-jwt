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
        url : "/zhddkk/wsChatroom/" + action,
        data : $('#form').serialize(),
        async : false,
        error : function() {
            parent.layer.alert("保存失败，请稍后再试");
        },
        success : function(r) {
            if (r.code == 1) {
                parent.layer.msg("保存成功");
                var index = parent.layer.getFrameIndex(window.name);//获取窗口索引
                parent.layer.close(index);
                parent.app.queryChatRoomList();
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
        },
        messages : {
            name : {
                required : icon + "请输入房间名称"
            },
        }
    })
}