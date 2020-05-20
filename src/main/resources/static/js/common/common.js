protocol = window.location.protocol;
host = window.location.host;

//上传文件接口地址
uploadUrl = protocol+"//"+host+"/upload/app";

//显示图片
$.ws = {
    gShowImg : function(imgUrl) {
        $.fancybox.open('<img src = "' + imgUrl + '" />');
    },

    backgroundImgAnimation : function(imgId, divId, direction) {
        var imgWidth = $("#" + imgId).width();
        var imgHeight = $("#" + imgId).height();
        var imgUrl = $("#" + imgId).attr("src");

        var divWidth = $("#" + divId).width();
        var divHeight = $("#" + divId).height();

        var divBackSizeX, divBackSizeY = 0;
        if (imgWidth < divWidth) {
            divBackSizeX = divWidth;
        } else {
            divBackSizeX = imgWidth;
        }

        if (imgHeight < divHeight) {
            divBackSizeY = divHeight;
        } else {
            divBackSizeY = imgHeight;
        }
        $("#" + divId).css("background-size", divBackSizeX + "px " + divBackSizeY + "px");
        $("#" + divId).css("background-image", "url('" + imgUrl + "')");

        var maxPosX = imgWidth - divWidth;
        var maxPosY = imgHeight - divHeight;
        if (direction == 1) {
            //上下滚动
            var posY = 0;  //max -690
            var downFlag = true;
            setInterval(function () {
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
            }, 10);
        } else if (direction == 2) {
            //左右滚动
            var posX = 0;
            var leftFlag = true;
            setInterval(function () {
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
            }, 10);
        } else if (direction == 3) {
            //圆圈运动
            var posX = 0;
            var posY = 0;
            // 1 右  2下 3 左  4上
            var direct = 1;
            setInterval(function () {
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
            }, 5);
        }
    }
}

function getCurrentTime() {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	var h = date.getHours();
	var m = date.getMinutes();
	var s = date.getSeconds();
	
	if (month<10){
		month = "0"+month;
	}
	if (day<10) {
		day = "0"+day;
	}	
	if (h<10) {
		h = "0"+h;
	}
	if (m<10) {
		m = "0"+m;
	}
	if (s<10) {
		s = "0" + s;
	}
	
	return year+"-"+month+"-"+day + " "+h+":"+m+":"+s;
}

function randomNumber(start,end){
	var w = end-start-1;
	var randomNum = Math.round(Math.random()*w+start+1);
	return randomNum;
}

//比较两个时间戳
function compareTwoTime(time1,time2){
	var interval = Math.round(Math.abs(time1-time2)/1000);
	if (interval<60){
		return interval+"秒前";
	}else if (interval>=60 && interval<3600){
		var tmp = Math.round(interval/60);
        return tmp+"分前";
	}else if (interval>=3600 && interval<86400){
		var tmp = Math.round(interval/3600);
		return tmp+"小时前";
	}else{
		var tmp = Math.round(interval/86400);
		return tmp+"天前";
	}
}