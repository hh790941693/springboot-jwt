// ajax请求session过期时处理
function  redirectHandle(xhr) {
    var url = xhr.getResponseHeader("redirectUrl");
    var enable = xhr.getResponseHeader("enableRedirect");

    if((enable == "true") && (url != "")){
        var win = window;
        while(win != win.top){
            win = win.top;
        }
        win.location.href = url;
    }
}

$(function () {
    $(document).ajaxComplete(function (event, xhr, settings) {
        redirectHandle(xhr);
    })
})