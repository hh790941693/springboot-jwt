<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>移动警力</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>
</head>
<body>
<h1>
	移动警力 
</h1>

	<div align="right" style="width:400px;">
		<button id="btnstart"  type="button" onClick="start()">启动</button>
		<button id="btnstop"  type="button"  onClick="stop()">停止</button>
	</div>
	
	<textarea id="showProgress" rows="100" cols="100" style="width:400px;height:250px;">
	</textarea>
	
	<script>
		// 定时器
		var timer;
		
		// 车辆坐标列表
		var gpsInfo;
		var i = 0;

		function start()
		{
			if (gpsInfo != null && gpsInfo != undefined)
			{
				if (timer == null || timer == undefined)
				{
					timer = window.setInterval(updateGisData,5000);
					$("#btnstart").attr("disabled",true);
					$("#btnstop").attr("disabled",false);
				}
			}
			else
			{
				getJsonData();
			}
		}
		
		function stop()
		{
			if (timer != null || timer != undefined)
			{
				window.clearInterval(timer);
				$("#btnstart").attr("disabled",false);
				$("#btnstop").attr("disabled",true);
			}
		}
		
		function updateGisData()
		{
			var jsonObj = gpsInfo.aa;
			for (var idx in jsonObj)
			{
				var carcode = jsonObj[idx].carcode;
				var cartype = jsonObj[idx].cartype;
				var gpsList = jsonObj[idx].gpslist;
				
				var gpsArr = gpsList.split(";");
				var gpsLength = gpsArr.length;
				if (i > gpsLength-1)
				{
					i=0;
				}
				var gps = gpsArr[i];
				var msg = "carcode:" + carcode + ",i="+i+",gps:" + gps;
				console.log(msg);
				updateGisCar(carcode, cartype, gps);
				var totalMsg = $("#showProgress").html()+"\n"+msg;
				$("#showProgress").html(totalMsg);
			}
			i++;
		}
		
		// 获取车辆json数据
		function getJsonData()
		{
			$.ajax({
				url : rootUrl + "json/giscarbasic.json",
				type : "GET",
				dataType : "json",
				success : function(result) {
					gpsInfo = result;
				},
				error : function(result) {
					console.log("failed");
				}
			});
		}
		
		function updateGisCar(carCode,carType,gps)
		{
			$.ajax({
				url : rootUrl + "ws/updateGisCar",
				type : "GET",
				data:{carCode:carCode,carType:carType,gps:gps},
				dataType : "json",
				success : function(result) {
					var rt = result;
				},
				error : function(result) {
					console.log("failed");
				}
			});
		}
		
		function clearTextArea()
		{
			$("#showProgress").html("");
		};
		
		setInterval(function(){
			clearTextArea();
		},30000);
	</script>
</body>
</html>
