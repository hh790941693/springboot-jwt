// ajax请求session过期时处理
function redirectHandle(xhr) {
    var redirectUrl = xhr.getResponseHeader("redirectUrl");
    var errorCode = xhr.getResponseHeader("errorCode");
    var errorMsg = xhr.getResponseHeader("errorMsg");

    if(errorCode == "-255"){
        var win = window;
        while(win != win.top){
            win = win.top;
        }
        win.location.href = redirectUrl+"?errorMsg="+errorMsg;
    }
}

$(function () {
    $(document).ajaxComplete(function (event, xhr, settings) {
        redirectHandle(xhr);
    });

    //全局的ajax访问，处理ajax清求时session超时
    $.ajaxSetup({
        contentType : "application/x-www-form-urlencoded;charset=utf-8",
        complete : function(XMLHttpRequest, textStatus) {
            var errorMsg = XMLHttpRequest.getResponseHeader("errorMsg");
            var errorCode = XMLHttpRequest.getResponseHeader("errorCode");
            if (errorCode == "-255") {
                // 如果超时就处理 ，指定要跳转的页面(比如登陆页)
                window.location.replace("/index?errorMsg="+errorMsg);
            }
        },
        dataFilter : function (data,type) {
            if (data.indexOf("code") != -1) {
                var dataJson = JSON.parse(data);
                if (dataJson.code == "-255") {
                    var win = window;
                    while (win != win.top) {
                        win = win.top;
                    }
                    win.location.href = dataJson.redirectUrl + "?errorMsg=" + dataJson.msg;
                } else {
                    return data;
                }
            } else {
                return data;
            }
        }
    });
})