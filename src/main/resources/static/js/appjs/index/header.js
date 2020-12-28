var headerTitle = $.ws.i18n("web.title");
if (sessionUser == "admin") {
    if (sessionSelfimg == "") {
        $("#myImg").attr("src", rootUrl + "img/headimg/admin.jpg");
    } else {
        $("#myImg").attr("src", sessionSelfimg);
    }
} else {
    if (sessionSelfimg == "" || sessionSelfimg == null) {
        var errorImgUrl = $.ws.errorImgUrl;
        $("#myImg").attr("src", errorImgUrl);
    }
}
$("#curUser").text(sessionUser);

//当前时间
var curTimeTemp = getCurrentTime();
$("#curTimeSpan").text(curTimeTemp);

//定时更新时间
setInterval(function () {
    var curTime = getCurrentTime();
    $("#curTimeSpan").text(curTime);
}, 1000);

// 窗口大小监听事件
$("#percentSelect").change(function () {
    parent.manualResizWindow($(this).val());
});

//定时获取天气
grapWeatherInfo();
// setInterval(function () {
//     grapWeatherInfo();
// }, 600000);
function grapWeatherInfo() {
    $.ajax({
        type: 'GET',
        url: rootUrl + 'index/grapWetherInfo',
        async: false,
        success: function (result) {
            if (result.code == 1) {
                $("#weatherInfo").text(result.data.remark);
                $("#lastUpdateTime").text(result.data.lastUpdateTime);
                $("#location").text(result.data.location);
            }
        }
    });
}

//定时刷新在线人数和积分数量
getOnlineInfo();
//setInterval(getOnlineInfo, 15000);
function getOnlineInfo() {
    $.ajax({
        type: 'GET',
        url: 'ws/getOnlineInfo.json',
        data: {"user": sessionUser},
        //dataType: 'json',
        success: function (result) {
            if (result.code != 1) {
                return;
            }
            var jsonObj = result.data;
            var currentUserOnlineInfo = jsonObj.currentOnlineUserInfo;
            var coinNum = currentUserOnlineInfo.coinNum;
            var onlineNum = jsonObj.onlineUserList.length;
            var headImage = currentUserOnlineInfo.headImage;

            $("#coinNum").text(coinNum);
            $("#onlineNum").text(onlineNum);
            if (headImage == null || headImage == "") {
                $("#myImg").attr("src", rootUrl + "../img/" + $.ws.errorImageName);
            } else {
                $("#myImg").attr("src", headImage);
            }
        }
    });
}

//定时切换header标题
setInterval(function () {
    var titleStatus = $("#headerTitleDiv").css("display");
    if (titleStatus == "none") {
        $("#titleLabel").text(headerTitle);
        $("#headerTitleDiv").css("display", "block");
        $("#weatherDiv").css("display", "none");
    } else {
        $("#headerTitleDiv").css("display", "none");
        $("#weatherDiv").css("display", "block");
    }
}, 8000);

//定时滚动header背景图片
$.ws.backgroundImgAnimation("headBackgroundImg", "headerDiv", 1, 20);

//头部滚动广告
var scrollAdDiv = document.getElementById("scrollAdDiv");
var adContentDiv = document.getElementById("adContentDiv");
var adNoContentDiv = document.getElementById("adNoContentDiv");
scrollAdDiv.scrollTop = adContentDiv.offsetHeight;
function Marquee() {
    adNoContentDiv.innerHTML = adContentDiv.innerHTML;
    if (scrollAdDiv.scrollTop >= adContentDiv.offsetHeight) {
        scrollAdDiv.scrollTop -= adNoContentDiv.offsetHeight;
    } else {
        scrollAdDiv.scrollTop += 1;
    }
}
setInterval(Marquee, 100);

//获取最新广告
//queryRecentAdsList();
function queryRecentAdsList() {
    $.ajax({
        type: 'GET',
        url: '/zhddkk/wsAds/queryRecentAdsList?count=4',
        async: false,
        success: function (result) {
            if (result.code == 1) {
                var data = result.data;
                var totalAds = "";
                $.each(data, function (i, val) {
                    totalAds += "<span style='font-weight: bold;'>" + val.content + "</span>" + "&nbsp;&nbsp;&nbsp;" + val.createTime + "<br/>";
                });
                $("#adContentDiv").html(totalAds);
            }
        }
    });
}

//定时获取最新广告
//setInterval(queryRecentAdsList, 60000);