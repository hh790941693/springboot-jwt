//左侧导航栏点击事件
$("#leftSerNavi ul li").click(function () {
    var curPageid = this.id;
    if (curPageid.indexOf("configurationLi") != -1) {
        $("#configurationDiv").slideDown();
    } else {
        parent.showPage(curPageid);
    }
    if (curPageid.indexOf("wsserverChartMonitor.page") != -1) {
        parent.unreadCount = "";
        $("#unreadCountSpan").text("");
    }
    $("#leftSerNavi ul li").removeClass("mouseleftclick");
    $(this).addClass("mouseleftclick");

    var parentObj = $(this).parent().parent();
    var parentDivId = $(parentObj).attr("id");
    if (curPageid.indexOf("configurationLi") == -1 && parentDivId == "leftSerNavi") {
        $("#configurationDiv").slideUp();
    }
})

//定时滚动左侧导航栏背景图片
$.ws.backgroundImgAnimation("leftNavBackgroundImg", "leftSerNavi", 2, 20);