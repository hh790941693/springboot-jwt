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
        url : "/zhddkk/wsUserFile/" + action,
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
            userId : {
                required : true
            },
            userName : {
                required : true
            },
            fileId : {
                required : true
            },
            fileName : {
                required : true
            },
            fileUrl : {
                required : true
            },
            createTime : {
                required : true
            },
            updateTime : {
                required : true
            },
            status : {
                required : true
            },
        },
        messages : {
            userId : {
                required : icon + "请输入用户id"
            },
            userName : {
                required : icon + "请输入用户名"
            },
            fileId : {
                required : icon + "请输入文件id"
            },
            fileName : {
                required : icon + "请输入"
            },
            fileUrl : {
                required : icon + "请输入"
            },
            createTime : {
                required : icon + "请输入创建时间"
            },
            updateTime : {
                required : icon + "请输入更新时间"
            },
            status : {
                required : icon + "请输入状态"
            },
        }
    })
}