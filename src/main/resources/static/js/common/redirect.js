// ajax请求session过期时处理
function redirectHandle(xhr) {
    var redirectUrl = xhr.getResponseHeader("redirectUrl");
    var errorCode = xhr.getResponseHeader("errorCode");

    if(errorCode == "-255" || errorCode == "-256"){
        var win = window;
        while(win != win.top){
            win = win.top;
        }
        win.location.href = redirectUrl;
    } else if (errorCode == "-257") {
        // 请求频繁
        layer.msg(dataJson.msg);
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
            var errorCode = XMLHttpRequest.getResponseHeader("errorCode");
            if (errorCode == "-255" || errorCode == "-256") {
                // 如果超时就处理 ，指定要跳转的页面(比如登陆页)
                window.location.replace("/exception.page?redirectName=sessionTimeout");
            } else if (errorCode == "-257") {
                // 请求频繁
                layer.msg(dataJson.msg);
            }
        },
        dataFilter : function (data,type) {
            if (data != undefined && data.indexOf("\"code\":") != -1) {
                var dataJson = JSON.parse(data);
                if (dataJson.code == "-255" || dataJson.code == "-256") {
                    var win = window;
                    while (win != win.top) {
                        win = win.top;
                    }
                    win.location.href = dataJson.redirectUrl;
                } else if (dataJson.code == "-257") {
                    // 请求频繁
                    layer.msg(dataJson.msg);
                } else {
                    return data;
                }
            } else {
                return data;
            }
        }
    });
})