var menuIds;
$(function(){
    getMenuTreeData();
    validateRule();
});

function loadMenuTree(menuTree) {
    $('#menuTree').jstree({
        "plugins" : [ "wholerow", "checkbox" ],
        'core' : {
            'data' : menuTree
        },
        "checkbox" : {
            //"keep_selected_style" : false,
            //"undetermined" : true
            //"three_state" : false,
            //"cascade" : ' up'
        }
    });
    $('#menuTree').jstree('open_all');
}
function getAllSelectNodes() {
    var ref = $('#menuTree').jstree(true); // 获得整个树
    menuIds = ref.get_selected(); // 获得所有选中节点的，返回值为数组
    $("#menuTree").find(".jstree-undetermined").each(function(i, element) {
        menuIds.push($(element).closest('.jstree-node').attr("id"));
    });
    console.log(menuIds);
}
function getMenuTreeData() {
    var id = $('#id').val();
    var url = "/zhddkk/sysMenu/tree";
    if (!!id){
        url =  "/zhddkk/sysMenu/tree/" + id;
    }
    $.ajax({
        type : "GET",
        url : url,
        success : function(data) {
            loadMenuTree(data);
        }
    });
}

//保存数据
function save() {
    var action = "save";
    if($("#id").val()){
        action = "update";
    }
    $('#menuIds').val(menuIds);
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
        getAllSelectNodes();
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