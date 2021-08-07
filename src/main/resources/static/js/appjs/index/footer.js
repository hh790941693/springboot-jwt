//当前版本号
var curVersion = "";
queryVersion();

console.log("footer页面获取session数据:" + sessionUser);

//查询版本号
function queryVersion() {
    $.ajax({
        type: 'GET',
        url: rootUrl + 'ws/queryVersion.json',
        //dataType: 'json',
        async: false,
        success: function (result) {
            curVersion = result;
            $("#versionLabel").text(result);
        }
    });
}

//检查升级
function checkUpdate() {
    $.ajax({
        type: 'GET',
        url: rootUrl + 'ws/checkUpdate.do?version=' + curVersion + "&cmd=check",
        //dataType: 'json',
        async: false,
        success: function (result) {
            //needUpdate:false;from:1.0;to:2.0
            var needUpdate = result.split("needUpdate:")[1].split(";")[0];
            if (needUpdate == "true") {
                var toVersion = result.split("to:")[1];

                layer.confirm('已经发现新版本,是否要升级到' + toVersion + "?", {
                    btn: ['是', '否'] //按钮
                }, function (index) {
                    console.log("开始升级到" + toVersion);
                    toUpdate(curVersion);
                    layer.close(index);
                }, function (index) {
                    layer.close(index);
                });
            } else {
                layer.alert('已经是最新版本!');
            }
        }
    });
}

//开始升级
function toUpdate(curVersion) {
    $.ajax({
        type: 'GET',
        url: rootUrl + 'ws/checkUpdate.do?version=' + curVersion + "&cmd=update",
        //dataType: 'json',
        async: false,
        success: function (result) {
            console.log("升级结果:" + result);
        }
    });
}

//查看session信息
function showSessionInfo() {
    parent.layer.open({
        type: 2,
        title: '会话信息',
        maxmin: false,
        shadeClose: true,//点击遮罩关闭层
        area: ['630px', '510px'],
        anim: 1,
        content: rootUrl + '/index/sessionInfo.page'
    });
}

//联系作者
function contactAuthor() {
    parent.layer.open({
        type: 2,
        title: '联系作者',
        maxmin: false,
        shadeClose: true,//点击遮罩关闭层
        area: ['530px', '300px'],
        anim: 1,
        content: rootUrl + 'index/contactAuthor.page'
    });
}

//捐赠作者
function donate() {
    parent.layer.open({
        type: 2,
        title: '捐赠作者',
        maxmin: false,
        shadeClose: true,//点击遮罩关闭层
        area: ['630px', '350px'],
        anim: 1,
        content: rootUrl + 'index/donate.page'
    });
}

//提出疑问
function feedbackUs() {
    parent.layer.open({
        type: 2,
        title: '提出疑问',
        maxmin: false,
        shadeClose: true,//点击遮罩关闭层
        area: ['530px', '300px'],
        anim: 1,
        content: rootUrl + 'index/feedbackUs.page'
    });
}

//关于我们
function aboutUs() {
    parent.layer.open({
        type: 2,
        title: '关于我们',
        maxmin: false,
        shadeClose: true,//点击遮罩关闭层
        area: ['530px', '300px'],
        anim: 1,
        content: rootUrl + 'index/aboutUs.page'
    });
}