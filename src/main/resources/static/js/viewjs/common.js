function getCurrentTime()
{
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	var h = date.getHours();
	var m = date.getMinutes();
	var s = date.getSeconds();
	
	if (month<10)
	{
		month = "0"+month;
	}
	if (day<10)
	{
		day = "0"+day;
	}	
	if (h<10)
	{
		h = "0"+h;
	}
	if (m<10)
	{
		m = "0"+m;
	}
	if (s<10)
	{
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