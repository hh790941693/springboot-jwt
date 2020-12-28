//左侧导航栏点击事件
$("#leftCliNavi ul li").click(function () {
    var curPageid = this.id;
    parent.showPage(curPageid);
    if (curPageid.indexOf("wsclientChat.page") != -1) {
        parent.unreadCount = "";
        $("#unreadCountSpan").text("");
    }
    $("#leftCliNavi ul li").removeClass("mouseleftclick");
    $(this).addClass("mouseleftclick");
})

//定时滚动左侧导航栏背景图片
$.ws.backgroundImgAnimation("leftCliNavBackgroundImg", "leftCliNavi", 2, 20);