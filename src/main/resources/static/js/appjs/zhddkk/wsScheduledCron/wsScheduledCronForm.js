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
        url : "/zhddkk/wsScheduledCron/" + action,
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
                                    cronKey : {
                required : true
            },
                                cronExpression : {
                required : true
            },
                                taskExplain : {
                required : true
            },
                                status : {
                required : true
            },
                    },
        messages : {
                                    cronKey : {
                required : icon + "请输入定时任务完整类名"
            },
                                cronExpression : {
                required : icon + "请输入cron表达式"
            },
                                taskExplain : {
                required : icon + "请输入任务描述"
            },
                                status : {
                required : icon + "请输入状态,1:正常;2:停用"
            },
                    }
    })
}