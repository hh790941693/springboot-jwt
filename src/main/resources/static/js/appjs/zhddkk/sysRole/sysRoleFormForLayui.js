var menuIdsData;
$(function(){
    validateRule();
    getMenuTreeData();

    layui.tree.render({
        elem: '#menuTree'
        , data: menuIdsData
        , showCheckbox: true  //是否显示复选框
        , id: 'layuiTreeId'
        , isJump: false //是否允许点击节点时弹出新窗口跳转
        , click: function (obj) {
            var data = obj.data;  //获取当前点击的节点数据
            layer.msg('状态：' + obj.state + '<br>节点数据：' + JSON.stringify(data));
        }
    });
});

function getMenuTreeData() {
    var id = $('#id').val();
    var url = "/zhddkk/sysMenu/layuiTree";
    if (!!id){
        url =  "/zhddkk/sysMenu/layuiTree/" + id;
    }
    $.ajax({
        type : "GET",
        url : url,
        async: false,
        success : function(data) {
            menuIdsData = data.children;
            console.log(data);
        }
    });
}

function getAllSelectNodes() {
    var resultIdArr = new Array();
    var checkedData =  layui.tree.getChecked('layuiTreeId'); //获取选中节点的数据
    $.each(checkedData, function(i, topEle){
        resultIdArr.push(topEle.id);
        $.each(topEle.children, function (j, chilEle){
            resultIdArr.push(chilEle.id);
        });
    });
    console.log(resultIdArr);
    return resultIdArr;
}

//保存数据
function save() {
    var action = "save";
    if($("#id").val()){
        action = "update";
    }
    var selectedRoleIdArr = getAllSelectNodes();
    $('#menuIds').val(selectedRoleIdArr);
    $.ajax({
        cache : true,
        type : "POST",
        url : "/zhddkk/sysRole/" + action,
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
                parent.layer.alert("保存失败，请稍后再试。原因:"+r.data);
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
                                createTime : {
                required : true
            },
                                updateTime : {
                required : true
            },
                    },
        messages : {
                                    name : {
                required : icon + "请输入角色名"
            },
                                createTime : {
                required : icon + "请输入创建时间"
            },
                                updateTime : {
                required : icon + "请输入更新时间"
            },
                    }
    })
}