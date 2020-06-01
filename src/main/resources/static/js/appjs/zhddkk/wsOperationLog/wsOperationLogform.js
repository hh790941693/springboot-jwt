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
        url : "/zhddkk/wsOperationLog/" + action,
        data : $('#form').serialize(),
        async : false,
        error : function() {
            parent.layer.alert("保存失败，请稍后再试");
        },
        success : function(data) {
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
                                operType : {
                required : true
            },
                                operModule : {
                required : true
            },
                                operSubmodule : {
                required : true
            },
                                operDescribe : {
                required : true
            },
                                operRemark : {
                required : true
            },
                                requestUrl : {
                required : true
            },
                                costTime : {
                required : true
            },
                                className : {
                required : true
            },
                                methodName : {
                required : true
            },
                                parameters : {
                required : true
            },
                                operResult : {
                required : true
            },
                                errorMsg : {
                required : true
            },
                                accessTime : {
                required : true
            },
                                createTime : {
                required : true
            },
                    },
        messages : {
                                    userId : {
                required : icon + "请输入操作人ID"
            },
                                userName : {
                required : icon + "请输入操作人姓名"
            },
                                operType : {
                required : icon + "请输入操作类型(增|删|改)"
            },
                                operModule : {
                required : icon + "请输入操作模块"
            },
                                operSubmodule : {
                required : icon + "请输入操作子模块"
            },
                                operDescribe : {
                required : icon + "请输入操作描述"
            },
                                operRemark : {
                required : icon + "请输入操作描述"
            },
                                requestUrl : {
                required : icon + "请输入请求路径"
            },
                                costTime : {
                required : icon + "请输入耗时(毫秒)"
            },
                                className : {
                required : icon + "请输入类名"
            },
                                methodName : {
                required : icon + "请输入方法名"
            },
                                parameters : {
                required : icon + "请输入参数列表"
            },
                                operResult : {
                required : icon + "请输入操作结果"
            },
                                errorMsg : {
                required : icon + "请输入错误信息"
            },
                                accessTime : {
                required : icon + "请输入访问时间"
            },
                                createTime : {
                required : icon + "请输入创建时间"
            },
                    }
    })
}