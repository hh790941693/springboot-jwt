protocol = window.location.protocol;
host = window.location.host;

//公共api
$.ws = {

    //上传文件接口地址
    uploadUrl: protocol + "//" + host + "/upload/app",

    uploadByFroalaUrl: protocol + "//" + host + "/upload/app/uploadByFroala",

    //图片显示异常时,显示该图片
    errorImageName: "imgerror_default.jpg",

    // 图片显示异常时的默认图片url
    errorImgUrl: rootUrl + "img/imgerror_default.jpg",

    //显示图片
    gShowImg: function (imgUrl) {
        $.fancybox.open('<img src = "' + imgUrl + '" />');
    },

    i18nInit: function (lang, country) {
        i18nInit(lang, country);
    },

    i18n: function (labelKey) {
        return i18n(labelKey);
    },

    //背景图片动画
    backgroundImgAnimation: function (imgId, divId, direction, frequency) {
        var imgWidth = $("#" + imgId).width();
        var imgHeight = $("#" + imgId).height();
        var imgUrl = $("#" + imgId).attr("src");

        var divWidth = $("#" + divId).width();
        var divHeight = $("#" + divId).height();

        if (frequency == null || frequency == "" || frequency == undefined) {
            frequency = 5;
        }

        var divBackSizeX = 0;
        var divBackSizeY = 0;
        if (imgWidth <= divWidth) {
            divBackSizeX = divWidth;
        } else {
            divBackSizeX = imgWidth;
        }

        if (imgHeight <= divHeight) {
            divBackSizeY = divHeight;
        } else {
            divBackSizeY = imgHeight;
        }
        $("#" + divId).css("background-size", divBackSizeX + "px " + divBackSizeY + "px");
        $("#" + divId).css("background-image", "url('" + imgUrl + "')");

        var maxPosX = Math.abs(imgWidth - divWidth);
        var maxPosY = Math.abs(imgHeight - divHeight);
        if (direction == 1) {
            //上下滚动
            var posY = 0;  //max -690
            var downFlag = true;
            setInterval(function () {
                if (imgHeight == 0 || imgWidth == 0 || divWidth == 0 || divHeight == 0) {
                    imgWidth = $("#" + imgId).width();
                    imgHeight = $("#" + imgId).height();

                    divWidth = $("#" + divId).width();
                    divHeight = $("#" + divId).height();

                    if (imgWidth <= divWidth) {
                        divBackSizeX = divWidth;
                    } else {
                        divBackSizeX = imgWidth;
                    }

                    if (imgHeight <= divHeight) {
                        divBackSizeY = divHeight;
                    } else {
                        divBackSizeY = imgHeight;
                    }
                    $("#" + divId).css("background-size", divBackSizeX + "px " + divBackSizeY + "px");
                    maxPosX = Math.abs(imgWidth - divWidth);
                    maxPosY = Math.abs(imgHeight - divHeight);
                }
                if (downFlag) {
                    posY -= 1;
                }
                if (!downFlag) {
                    posY += 1;
                }
                if (posY <= -maxPosY) {
                    downFlag = false;
                }
                if (posY >= 0) {
                    downFlag = true;
                }
                $("#" + divId).css("background-position", "0px " + posY + "px");
            }, frequency);
        } else if (direction == 2) {
            //左右滚动
            var posX = 0;
            var leftFlag = true;
            setInterval(function () {
                if (imgHeight == 0 || imgWidth == 0 || divWidth == 0 || divHeight == 0) {
                    imgWidth = $("#" + imgId).width();
                    imgHeight = $("#" + imgId).height();

                    divWidth = $("#" + divId).width();
                    divHeight = $("#" + divId).height();

                    if (imgWidth <= divWidth) {
                        divBackSizeX = divWidth;
                    } else {
                        divBackSizeX = imgWidth;
                    }

                    if (imgHeight <= divHeight) {
                        divBackSizeY = divHeight;
                    } else {
                        divBackSizeY = imgHeight;
                    }
                    $("#" + divId).css("background-size", divBackSizeX + "px " + divBackSizeY + "px");
                    maxPosX = Math.abs(imgWidth - divWidth);
                    maxPosY = Math.abs(imgHeight - divHeight);
                }
                if (leftFlag) {
                    posX -= 1;
                }
                if (!leftFlag) {
                    posX += 1;
                }
                if (posX <= -maxPosX) {
                    leftFlag = false;
                }
                if (posX >= 0) {
                    leftFlag = true;
                }
                $("#" + divId).css("background-position", posX + "px 0px");
            }, frequency);
        } else if (direction == 3) {
            //圆圈运动
            var posX = 0;
            var posY = 0;
            // 1 右  2下 3 左  4上
            var direct = 1;
            setInterval(function () {
                if (imgHeight == 0 || imgWidth == 0 || divWidth == 0 || divHeight == 0) {
                    imgWidth = $("#" + imgId).width();
                    imgHeight = $("#" + imgId).height();

                    divWidth = $("#" + divId).width();
                    divHeight = $("#" + divId).height();

                    if (imgWidth <= divWidth) {
                        divBackSizeX = divWidth;
                    } else {
                        divBackSizeX = imgWidth;
                    }

                    if (imgHeight <= divHeight) {
                        divBackSizeY = divHeight;
                    } else {
                        divBackSizeY = imgHeight;
                    }
                    $("#" + divId).css("background-size", divBackSizeX + "px " + divBackSizeY + "px");
                    maxPosX = Math.abs(imgWidth - divWidth);
                    maxPosY = Math.abs(imgHeight - divHeight);
                }
                if (direct == 1) {
                    posX -= 1;
                } else if (direct == 2) {
                    posY -= 1;
                } else if (direct == 3) {
                    posX += 1;
                } else if (direct == 4) {
                    posY += 1;
                }
                if (posX <= -maxPosX && direct == 1) {
                    direct = 2;
                }
                if (posY <= -maxPosY && direct == 2) {
                    direct = 3;
                }
                if (posX >= 0 && direct == 3) {
                    direct = 4;
                }
                if (posY >= 0 && direct == 4) {
                    direct = 1;
                }
                $("#" + divId).css("background-position", posX + "px " + posY + "px");
            }, frequency);
        }
    },

    //拖动窗口  downDiv:子div  moveDiv:父div
    dragPanelMove: function (downDiv, moveDiv) {
        var position = $(moveDiv).css("position");
        var cursor = $(downDiv).css("cursor");
        if (position != "fixed") {
            $(moveDiv).css("position", "fixed");
        }
        if (cursor != "move") {
            $(downDiv).css("cursor", "move");
        }
        $(downDiv).mousedown(function (e) {
            var isMove = true;
            var div_x = e.pageX - $(moveDiv).offset().left;
            var div_y = e.pageY - $(moveDiv).offset().top;
            $(document).mousemove(function (e) {
                if (isMove) {
                    var obj = $(moveDiv)
                    var left = e.pageX - div_x;
                    var top = e.pageY - div_y;
                    if (left < 0) {
                        left = 0;
                    }
                    if (top < 0) {
                        top = 0;
                    }
                    //console.log("left:"+left+",top:"+top);
                    obj.css({"left": left, "top": top});
                }
            }).mouseup(
                function () {
                    isMove = false;
                });
        });
    },
    subStringText: function (content, maxLength) {
        var res = "";
        if (content.length <= maxLength) {
            res = content;
        } else {
            res = content.substring(0, maxLength);
            res += "&nbsp;&nbsp;<a style='color:blue;cursor:pointer;' onclick=\"layer.alert('" + content + "')\">详情</a>";
        }
        return res;
    }
}

function getCurrentTime() {
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var h = date.getHours();
    var m = date.getMinutes();
    var s = date.getSeconds();

    var weekendArr = new Array('星期日','星期一','星期二','星期三','星期四','星期五','星期六');
    var week = weekendArr[date.getDay()];

    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    if (h < 10) {
        h = "0" + h;
    }
    if (m < 10) {
        m = "0" + m;
    }
    if (s < 10) {
        s = "0" + s;
    }

    return year + "-" + month + "-" + day + " " + h + ":" + m + ":" + s;
}

function getCurrentTimeOfHHMMSS() {
    var date = new Date();
    var h = date.getHours();
    var m = date.getMinutes();
    var s = date.getSeconds();

    if (h < 10) {
        h = "0" + h;
    }
    if (m < 10) {
        m = "0" + m;
    }
    if (s < 10) {
        s = "0" + s;
    }

    return h + ":" + m + ":" + s;
}

function randomNumber(start, end) {
    var w = end - start - 1;
    var randomNum = Math.round(Math.random() * w + start + 1);
    return randomNum;
}

//比较两个时间戳
function compareTwoTime(time1, time2) {
    var interval = Math.round(Math.abs(time1 - time2) / 1000);
    if (interval < 60) {
        return interval + "秒前";
    } else if (interval >= 60 && interval < 3600) {
        var tmp = Math.round(interval / 60);
        return tmp + "分前";
    } else if (interval >= 3600 && interval < 86400) {
        var tmp = Math.round(interval / 3600);
        return tmp + "小时前";
    } else {
        var tmp = Math.round(interval / 86400);
        return tmp + "天前";
    }
}

//初始化i18n插件
function i18nInit(lang, country) {
    try {
        $.i18n.properties({
            path: '/i18n/',
            name: 'messages',
            language: lang + '_' + country,
            mode: "both"
        });
    } catch (e) {
        console.log("load i18n properties error");
    }
}

//根据key获取国际化的值
function i18n(labelKey) {
    return $.i18n.prop(labelKey);
}

function countListSize(obj) {
    console.log(obj);
    return Object.keys(obj).length;
}