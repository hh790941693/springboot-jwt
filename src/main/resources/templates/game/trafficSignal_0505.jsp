<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>

<html>
	<head>
		<meta charset="utf-8">
		<title></title>
		<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>
		
		<style type="text/css">
			.wallClass1,.wallClass2,.wallClass3,.wallClass4{
			}
			
			
			.signal11,.signal12,.signal13,.signal21,.signal22,.signal23,.signal31,.signal32,.signal33,.signal41,.signal42,.signal43{
				border-radius:50%;
			}
			.signalTimer1,.signalTimer2,.signalTimer3,.signalTimer4{
			}
			
			
			.redClass{
				background-color:rgb(255,0,0);
			}
			
			.yellowClass{
				background-color:rgb(255,255,0);
			}
			
			.greenClass{
				background-color:rgb(0,255,0);
			}
			
			.roadBaseClass{
				background-color:#666665;
			}
			
			.bmxClass1,.bmxClass3{
				background-color:#666665;
			}
			
			.bmxClass4,.bmxClass2{
				transform:rotate(90deg);
			}
			
			.carWaitClass4,.carWaitClass2,.carWaitClass1,.carWaitClass3{
				//background-color:red;
			}
			
			.manWaitClass4,.manWaitClass2,.manWaitClass1,.manWaitClass3{
				background-color:yellow;
			}
			
			.roadDirectionClass1,.roadDirectionClass3,.roadDirectionClass4,.roadDirectionClass2{
			}
			
			.carClass1,.carClass2,.carClass3,.carClass4,.carClass5,.carClass6,.carClass7,.carClass8,.carClass9,.carClass10{
				//background-color:blue;
				background-image: url("<%=path%>/img/car.jpg");
				background-repeat: no-repeat;
				background-size:30px 30px;
				//transform:rotate(270deg);
			}
			
			.carClass11,.carClass12,.carClass13,.carClass14,.carClass15,.carClass16,.carClass17,.carClass18,.carClass19,.carClass20{
				//background-color:blue;
				background-image: url("<%=path%>/img/car.jpg");
				background-repeat: no-repeat;
				background-size:30px 30px;
				//transform:rotate(270deg);
			}
			
			.manClass1,.manClass2,.manClass3,.manClass4,.manClass5,.manClass6,.manClass7,.manClass8,.manClass9,.manClass10{
			
			}
			.manClass11,.manClass12,.manClass13,.manClass14,.manClass15,.manClass16,.manClass17,.manClass18,.manClass19,.manClass20{
			
			}
		</style>
	</head> 
	<body>
		<div id="gameMainDiv" style="width:1210px;height:610px;border:2px solid green;margin:5px auto;padding:10px;">
			<div id="borderDiv" style="width:70%;height:100%;float:left">
				
			</div>
		</div>
		
	</body>
	<script type="text/javascript">	
	
		// 初始化网格
		var ROW_MAX = 19;
		var COL_MAX = 26;
		
		// 每个div的宽高度
		var DIV_WIDTH = 30
		var DIV_HEIGHT = 30;
		
		// 车道朝向位置
		var NORTH=1,EAST=2,SOUTH=3,WEST=4;
		// 车道方向位置    直行,左转,右转
		var LEFT=1,STRAIGHT=2,RIGHT=3;
		
		var SIGNAL_CLASS_PREFIX = "signal";
		var SIGNAL_TIMER_PREFIX = "signalTimer";

		var RED = 1;
		var GREEN = 2;
		var YELLOW = 3;
		
		var NS_RED_TIME = 10; //北,南方红灯时间
		var WE_RED_TIME = 10; //西,东方红灯时间
		
		var SOUTH_TO_NORTH=1,EAST_TO_WEST=2,NORTH_TO_SOUTH=3,WEST_TO_EAST=4;
		
		// 面板中是否显示div的id
		var SHOW_DIVID = false;

		//--------------------------------暂时不用------------------------------------------------
		// 墙的标识
		var TOP_WALL=01,RIGHT_WALL=02,BOTTOM_WALL=03,LEFT_WALL=04;
		
		//道路标识
		var BASIC_ROAD=05;
		
		// 信号灯标识
		var WEST_STRAIGHT_LIGHT=10,  WEST_LEFT_LIGHT=11,  WEST_RIGHT_LIGHT=12,  WEST_LEFT_SHOW=13;
		var EAST_STRAIGHT_LIGHT=20,  EAST_LEFT_LIGHT=21,  EAST_RIGHT_LIGHT=22,  EAST_LEFT_SHOW=23;
		var NORTH_STRAIGHT_LIGHT=30, NORTH_LEFT_LIGHT=31, NORTH_RIGHT_LIGHT=32, NORTH_LEFT_SHOW=33;
		var SOUTH_STRAIGHT_LIGHT=40, SOUTH_LEFT_LIGHT=41, SOUTH_RIGHT_LIGHT=42, SOUTH_LEFT_SHOW=43;

		// 斑马线标识
		var WEST_ZEBRA=50,EAST_ZEBRA=51,NORTH_ZEBRA=52,SOUTH_ZEBRA=53;
		// 汽车等待区域标识
		var WEST_CAR_WAIT=54,EAST_CAR_WAIT=55,NORTH_CAR_WAIT=56,SOUTH_CAR_WAIT=57;
		// 行人等待区域标识
		var WEST_MAN_WAIT=66,EAST_MAN_WAIT=67,NORTH_MAN_WAIT=68,SOUTH_MAN_WAIT=69;
		
		
		// 直行,左转,右转路线
		var WEST_STRAIGHT_MAP=60,WEST_LEFT_MAP=61,WEST_RIGHT_MAP=62;
		var WEST_STRAIGHT_MAP=70,WEST_LEFT_MAP=71,WEST_RIGHT_MAP=72;
		var WEST_STRAIGHT_MAP=80,WEST_LEFT_MAP=81,WEST_RIGHT_MAP=82;
		var WEST_STRAIGHT_MAP=90,WEST_LEFT_MAP=91,WEST_RIGHT_MAP=92;
		//--------------------------------暂时不用------------------------------------------------
		
// 面板				  1		2		3	  4	     5      6      7      8      9      10     11     12     13     14     15      16    17    18      19     20     21     22     23     24    25      26 
//1	var arrTmp = [[010000,010000,010000,010000,010000,010000,010000,010000,010000,010000,010000,010000,010000,013100,013000,013200,013300,010000,010000,010000,010000,010000,010000,010000,010000,010000],
//2               [040000,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,020000],
//3               [040000,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,020000],
//4               [040000,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,020000],
//5               [040000,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,020000],
//6               [041300,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,020000],
//7               [041200,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,020000],
//8               [041000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,020000],
//9               [041100,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,020000],
//10              [040000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,022100],
//11              [040000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,022000],
//12              [040000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,050000,022200],
//13              [040000,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,022300],
//14              [040000,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,020000],
//15              [040000,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,020000],
//16              [040000,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,020000],
//17              [040000,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,020000],
//18              [040000,000000,000000,000000,000000,000000,000000,000000,000000,000000,050000,050000,050000,050000,050000,050000,000000,000000,000000,000000,000000,000000,000000,000000,000000,020000],
//19              [030000,030000,030000,030000,030000,030000,030000,030000,030000,034300,034200,034000,034100,030000,030000,030000,030000,030000,030000,030000,030000,030000,030000,030000,030000,030000]
//			];
		
		//-------------------------------------------------------------------
		
		// 绑定键盘事件
		$(document).keydown(function(event){
			var keyCode = event.keyCode;
			//console.log(keyCode);
			if (keyCode == 75){
				start();//k
			}else if (keyCode == 90){
				pause();//z
			}else if (keyCode == 67){
				restart();//c
			}else if (keyCode == 71){
				kaigua();//g
			}else if (keyCode == 87 || keyCode == 38){
				up();//w
			}else if (keyCode == 83 || keyCode == 40){
				down();//s
			}else if (keyCode == 65 || keyCode == 37){
				left();//a
			}else if (keyCode == 68 || keyCode == 39){
				right();//d
			}
		});
		
		// 初始化路面,红绿灯
		initPanel();
		
		// 判断一个div里是否包含某个class
		function hasClassLike(gps, className){
			var res = false;
			var classes = $("#borderDiv div[class*='"+className+"']");
			for (i=0;i<classes.length;i++){
				var divId = classes[i].id;
				if (parseInt(divId) == parseInt(gps)){
					res = true;
					break;
				}
			}
			
			return res;
		}
		
		function mainInterval(){
			var mainTimer = setInterval(function(){
				var direct = randomNumber(1,4);
				var roadType = randomNumber(1,3);
				
				// 南北方向道路所在列
				var startCol = parseInt((COL_MAX-6)/2)+1;
				var endCol = startCol+6-1;
				
				// 西东方向道路所在列
				var startRow = parseInt((ROW_MAX-6)/2)+1;
				var endRow = startRow+6-1;

				var carWaitArr = null;
				if (direct == SOUTH_TO_NORTH){
					// 南-北
					if (roadType == LEFT){
						carWaitArr = getPolyRange(endRow+3,ROW_MAX-1,endCol-2,endCol-2);
					}else if (roadType == STRAIGHT){
						carWaitArr = getPolyRange(endRow+3,ROW_MAX-1,endCol-1,endCol-1);
					}else if (roadType == RIGHT){
						carWaitArr = getPolyRange(endRow+3,ROW_MAX-1,endCol,endCol);
					}
					
					var firstDivId = carWaitArr[carWaitArr.length-1];
					if (hasClassLike(firstDivId,"carClass")){
						return;
					}else{
						carTimerInterval(direct, roadType, firstDivId,null,true);
					}
				}else if (direct == NORTH_TO_SOUTH){
					// 北-南
					if (roadType == LEFT){
						carWaitArr = getPolyRange(2,startRow-3,startCol+2,startCol+2);
					}else if (roadType == STRAIGHT){
						carWaitArr = getPolyRange(2,startRow-3,startCol+1,startCol+1);
					}else if (roadType == RIGHT){
						carWaitArr = getPolyRange(2,startRow-3,startCol,startCol);
					}
					
					var firstDivId = carWaitArr[0];
					if (hasClassLike(firstDivId,"carClass")){
						return;
					}else{
						carTimerInterval(direct, roadType, firstDivId,null,true);
					}
				}else if (direct == WEST_TO_EAST){
					// 西-东
					if (roadType == LEFT){
						carWaitArr = getPolyRange(endRow-2,endRow-2,2,startCol-3);
					}else if (roadType == STRAIGHT){
						carWaitArr = getPolyRange(endRow-1,endRow-1,2,startCol-3);
					}else if (roadType == RIGHT){
						carWaitArr = getPolyRange(endRow,endRow,2,startCol-3);
					}
					
					var firstDivId = carWaitArr[0];
					if (hasClassLike(firstDivId,"carClass")){
						return;
					}else{
						carTimerInterval(direct, roadType, firstDivId,null,true);
					}
				}else if (direct == EAST_TO_WEST){
					// 东-西
					if (roadType == LEFT){
						carWaitArr = getPolyRange(startRow+2,startRow+2,endCol+3,COL_MAX-1);
					}else if (roadType == STRAIGHT){
						carWaitArr = getPolyRange(startRow+1,startRow+1,endCol+3,COL_MAX-1);
					}else if (roadType == RIGHT){
						carWaitArr = getPolyRange(startRow,startRow,endCol+3,COL_MAX-1);
					}
					
					var firstDivId = carWaitArr[carWaitArr.length-1];
					if (hasClassLike(firstDivId,"carClass")){
						return;
					}else{
						carTimerInterval(direct, roadType, firstDivId,null,true);
					}
				}
				
			},3000);
		}
		
		// 新增一辆汽车   direciton道路东南西北朝向      roadType:直行,左转,右转   gps:     carId:汽车编号    isFirstRun是否是第一次运行
		function carTimerInterval(direciton,roadType,gps,carId,isFirstRun){
			
			function up(gpsTmp){
				return gpsTmp-COL_MAX;
			}
			
			function down(gpsTmp){
				return gpsTmp+COL_MAX;
			}
			
			function left(gpsTmp){
				return gpsTmp-1;
			}
			
			function right(gpsTmp){
				return gpsTmp+1;
			}
			
			var curGps = gps;
			var nextGps = null;
			var preGps = null;
			
			var totalCarNum = $("#borderDiv div[class*='carClass']");
			
			if (totalCarNum.length > 11){
				return;
			}
			
			if (carId == null){
				carId = totalCarNum.length+1;
			}

			//var nextGps = null;
			var carTimer = setInterval(function(){
				if (direciton == SOUTH_TO_NORTH){
					if (isFirstRun){
						if (hasClassLike(curGps,"carClass")){
							console.log(curGps+" 当前位置有车辆,本次定时任务不做什么");
							return;
						}
					}

					nextGps = up(curGps);
					preGps = down(curGps);
										
					//console.log("--->"+nextGps);
					var jiaodu = "rotate(270deg)";
					// 南-北
					if (roadType == LEFT){
						if ($("#"+curGps).hasClass("roadDirectionClass"+EAST_TO_WEST) && $("#"+curGps).hasClass("roadDirectionClass"+SOUTH_TO_NORTH)){
							nextGps = left(curGps);
							preGps = down(curGps);
							jiaodu = "rotate(180deg)";
						}else if ($("#"+curGps).hasClass("roadDirectionClass"+EAST_TO_WEST)){
							nextGps = left(curGps);
							preGps = right(curGps);
							jiaodu = "rotate(180deg)";
						}
					}else if (roadType == STRAIGHT){
						//noting to do
						var a=1;
					}else if (roadType == RIGHT){
						if ($("#"+curGps).hasClass("roadDirectionClass"+WEST_TO_EAST) && $("#"+curGps).hasClass("roadDirectionClass"+SOUTH_TO_NORTH)){
							nextGps = right(curGps);
							preGps = down(curGps);
							jiaodu = "rotate(90deg)";
						}else if ($("#"+curGps).hasClass("roadDirectionClass"+WEST_TO_EAST)){
							nextGps = right(curGps);
							preGps = left(curGps);
							jiaodu = "rotate(90deg)";
						}
					}
					
					$("#"+preGps).removeClass("carClass"+carId);
					$("#"+curGps).addClass("carClass"+carId);
					//$("#"+curGps).css("transform",jiaodu);
					
					// 判断下一步是否撞墙了
					if ($("#"+nextGps).hasClass("wallClass"+NORTH) || $("#"+nextGps).hasClass("wallClass"+SOUTH)
							                                       || $("#"+nextGps).hasClass("wallClass"+EAST) 
							                                       || $("#"+nextGps).hasClass("wallClass"+WEST) ){
						$("#"+curGps).removeClass("carClass"+carId);
						clearInterval(carTimer);
						return;
					}
				
					// 如果当前位置是候车区  下一个位置是斑马线
					if ($("#"+nextGps).hasClass("bmxClass"+WEST_TO_EAST) && $("#"+curGps).hasClass("carWaitClass"+SOUTH_TO_NORTH) ){
						// 判断红绿灯
						var lightClassName = SIGNAL_CLASS_PREFIX + NORTH + roadType;
						var rgbColor = $("."+lightClassName).css("background-color");
						var color = convertRgb(rgbColor);
						if (color == RED){
							console.log(curGps+":汽车等候区,"+nextGps+":有斑马线,当前是红灯");
							clearInterval(carTimer);
							carTimerInterval(direciton,roadType,curGps,carId,false);
							return;
						}
					}
					
					// 如果当前位置是斑马线  下一个位置也是斑马线
					if ($("#"+curGps).hasClass("bmxClass"+WEST_TO_EAST) && $("#"+nextGps).hasClass("bmxClass"+WEST_TO_EAST) ){
						// noting to do;
						//console.log(curGps+":有斑马线,"+nextGps+":也有斑马线");
					}
					
					// 如果当前位置是斑马线  下一个位置不是斑马线
					if ($("#"+curGps).hasClass("bmxClass"+WEST_TO_EAST) && !$("#"+nextGps).hasClass("bmxClass"+WEST_TO_EAST) ){
						// noting to do;
						//console.log(curGps+":有斑马线,"+nextGps+":没有斑马线");
					}
					
					// 判断下一步是否有人
					if ($("#"+nextGps).hasClass("manClass")){
						return;
					}
					
					if (hasClassLike(nextGps,"carClass")){
						// 判断前面是否有车
						carTimerInterval(direciton,roadType,curGps,carId,false);
						clearInterval(carTimer);
						return;
					}
										
					curGps = nextGps;
				}else if (direciton == NORTH_TO_SOUTH){
					if (isFirstRun){
						if (hasClassLike(curGps,"carClass")){
							console.log(curGps+" 当前位置有车辆,本次定时任务不做什么");
							return;
						}
					}

					nextGps = down(curGps);
					preGps = up(curGps);
									
					var jiaodu = "rotate(270deg)";
					//console.log("--->"+nextGps);
					// 南-北
					if (roadType == LEFT){
						if ($("#"+curGps).hasClass("roadDirectionClass"+NORTH_TO_SOUTH) && $("#"+curGps).hasClass("roadDirectionClass"+WEST_TO_EAST)){
							nextGps = right(curGps);
							preGps = up(curGps);
						}else if ($("#"+curGps).hasClass("roadDirectionClass"+WEST_TO_EAST)){
							nextGps = right(curGps);
							preGps = left(curGps);
						}
					}else if (roadType == STRAIGHT){
						//noting to do
						var a=1;
					}else if (roadType == RIGHT){
						if ($("#"+curGps).hasClass("roadDirectionClass"+NORTH_TO_SOUTH) && $("#"+curGps).hasClass("roadDirectionClass"+EAST_TO_WEST)){
							nextGps = left(curGps);
							preGps = up(curGps);
						}else if ($("#"+curGps).hasClass("roadDirectionClass"+EAST_TO_WEST)){
							nextGps = left(curGps);
							preGps = right(curGps);
						}
					}
					
					$("#"+preGps).removeClass("carClass"+carId);
					$("#"+curGps).addClass("carClass"+carId);
					//$("#"+curGps).css("transform",jiaodu);
					
					// 判断下一步是否撞墙了
					if ($("#"+nextGps).hasClass("wallClass"+NORTH) || $("#"+nextGps).hasClass("wallClass"+SOUTH)
							                                       || $("#"+nextGps).hasClass("wallClass"+EAST) 
							                                       || $("#"+nextGps).hasClass("wallClass"+WEST) ){
						$("#"+curGps).removeClass("carClass"+carId);
						clearInterval(carTimer);
						return;
					}
				
					// 如果当前位置是候车区  下一个位置是斑马线
					if ($("#"+nextGps).hasClass("bmxClass"+EAST_TO_WEST) && $("#"+curGps).hasClass("carWaitClass"+NORTH_TO_SOUTH) ){
						// 判断红绿灯
						//console.log(curGps+":汽车等候区,"+nextGps+":有斑马线");
						var lightClassName = SIGNAL_CLASS_PREFIX + SOUTH + roadType;
						var rgbColor = $("."+lightClassName).css("background-color");
						var color = convertRgb(rgbColor);
						if (color == RED){
							console.log(curGps+":汽车等候区,"+nextGps+":有斑马线,当前是红灯");
							clearInterval(carTimer);
							carTimerInterval(direciton,roadType,curGps,carId,false);
							return;
						}
					}
					
					// 如果当前位置是斑马线  下一个位置也是斑马线
					if ($("#"+curGps).hasClass("bmxClass"+EAST_TO_WEST) && $("#"+nextGps).hasClass("bmxClass"+EAST_TO_WEST) ){
						// noting to do;
						//console.log(curGps+":有斑马线,"+nextGps+":也有斑马线");
					}
					
					// 如果当前位置是斑马线  下一个位置不是斑马线
					if ($("#"+curGps).hasClass("bmxClass"+EAST_TO_WEST) && !$("#"+nextGps).hasClass("bmxClass"+EAST_TO_WEST) ){
						// noting to do;
						//console.log(curGps+":有斑马线,"+nextGps+":没有斑马线");
					}
					
					// 判断下一步是否有人
					if ($("#"+nextGps).hasClass("manClass")){
						return;
					}
					
					if (hasClassLike(nextGps,"carClass")){
						// 判断前面是否有车
						carTimerInterval(direciton,roadType,curGps,carId,false);
						clearInterval(carTimer);
						return;
					}
										
					curGps = nextGps;
				}else if (direciton == WEST_TO_EAST){
					if (isFirstRun){
						if (hasClassLike(curGps,"carClass")){
							console.log(curGps+" 当前位置有车辆,本次定时任务不做什么");
							return;
						}
					}

					nextGps = right(curGps);
					preGps = left(curGps);
										
					//console.log("--->"+nextGps);
					// 西-东
					if (roadType == LEFT){
						if ($("#"+curGps).hasClass("roadDirectionClass"+WEST_TO_EAST) && $("#"+curGps).hasClass("roadDirectionClass"+SOUTH_TO_NORTH)){
							nextGps = up(curGps);
							preGps = left(curGps);
						}else if ($("#"+curGps).hasClass("roadDirectionClass"+SOUTH_TO_NORTH)){
							nextGps = up(curGps);
							preGps = down(curGps);
						}
					}else if (roadType == STRAIGHT){
						//noting to do
						var a=1;
					}else if (roadType == RIGHT){
						if ($("#"+curGps).hasClass("roadDirectionClass"+WEST_TO_EAST) && $("#"+curGps).hasClass("roadDirectionClass"+NORTH_TO_SOUTH)){
							nextGps = down(curGps);
							preGps = left(curGps);
						}else if ($("#"+curGps).hasClass("roadDirectionClass"+NORTH_TO_SOUTH)){
							nextGps = down(curGps);
							preGps = up(curGps);
						}
					}
					
					$("#"+preGps).removeClass("carClass"+carId);
					$("#"+curGps).addClass("carClass"+carId);
					
					// 判断下一步是否撞墙了
					if ($("#"+nextGps).hasClass("wallClass"+NORTH) || $("#"+nextGps).hasClass("wallClass"+SOUTH)
							                                       || $("#"+nextGps).hasClass("wallClass"+EAST) 
							                                       || $("#"+nextGps).hasClass("wallClass"+WEST) ){
						$("#"+curGps).removeClass("carClass"+carId);
						clearInterval(carTimer);
						return;
					}
				
					// 如果当前位置是候车区  下一个位置是斑马线
					if ($("#"+nextGps).hasClass("bmxClass"+NORTH_TO_SOUTH) && $("#"+curGps).hasClass("carWaitClass"+WEST_TO_EAST) ){
						// 判断红绿灯
						//console.log(curGps+":汽车等候区,"+nextGps+":有斑马线");
						var lightClassName = SIGNAL_CLASS_PREFIX + EAST + roadType;
						var rgbColor = $("."+lightClassName).css("background-color");
						var color = convertRgb(rgbColor);
						if (color == RED){
							console.log(curGps+":汽车等候区,"+nextGps+":有斑马线,当前是红灯");
							clearInterval(carTimer);
							carTimerInterval(direciton,roadType,curGps,carId,false);
							return;
						}
					}
					
					// 如果当前位置是斑马线  下一个位置也是斑马线
					if ($("#"+curGps).hasClass("bmxClass"+NORTH_TO_SOUTH) && $("#"+nextGps).hasClass("bmxClass"+NORTH_TO_SOUTH) ){
						// noting to do;
						//console.log(curGps+":有斑马线,"+nextGps+":也有斑马线");
					}
					
					// 如果当前位置是斑马线  下一个位置不是斑马线
					if ($("#"+curGps).hasClass("bmxClass"+NORTH_TO_SOUTH) && !$("#"+nextGps).hasClass("bmxClass"+NORTH_TO_SOUTH) ){
						// noting to do;
						//console.log(curGps+":有斑马线,"+nextGps+":没有斑马线");
					}
					
					// 判断下一步是否有人
					if ($("#"+nextGps).hasClass("manClass")){
						return;
					}
					
					if (hasClassLike(nextGps,"carClass")){
						// 判断前面是否有车
						carTimerInterval(direciton,roadType,curGps,carId,false);
						clearInterval(carTimer);
						return;
					}
										
					curGps = nextGps;
				}else if (direciton == EAST_TO_WEST){
					if (isFirstRun){
						if (hasClassLike(curGps,"carClass")){
							console.log(curGps+" 当前位置有车辆,本次定时任务不做什么");
							return;
						}
					}

					nextGps = left(curGps);
					preGps = right(curGps);
										
					//console.log("--->"+nextGps);
					// 东-西
					if (roadType == LEFT){
						if ($("#"+curGps).hasClass("roadDirectionClass"+EAST_TO_WEST) && $("#"+curGps).hasClass("roadDirectionClass"+NORTH_TO_SOUTH)){
							nextGps = down(curGps);
							preGps = right(curGps);
						}else if ($("#"+curGps).hasClass("roadDirectionClass"+NORTH_TO_SOUTH)){
							nextGps = down(curGps);
							preGps = up(curGps);
						}
					}else if (roadType == STRAIGHT){
						//noting to do
						var a=1;
					}else if (roadType == RIGHT){
						if ($("#"+curGps).hasClass("roadDirectionClass"+EAST_TO_WEST) && $("#"+curGps).hasClass("roadDirectionClass"+SOUTH_TO_NORTH)){
							nextGps = up(curGps);
							preGps = right(curGps);
						}else if ($("#"+curGps).hasClass("roadDirectionClass"+SOUTH_TO_NORTH)){
							nextGps = up(curGps);
							preGps = down(curGps);
						}
					}
					
					$("#"+preGps).removeClass("carClass"+carId);
					$("#"+curGps).addClass("carClass"+carId);
					
					// 判断下一步是否撞墙了
					if ($("#"+nextGps).hasClass("wallClass"+NORTH) || $("#"+nextGps).hasClass("wallClass"+SOUTH)
							                                       || $("#"+nextGps).hasClass("wallClass"+EAST) 
							                                       || $("#"+nextGps).hasClass("wallClass"+WEST) ){
						$("#"+curGps).removeClass("carClass"+carId);
						clearInterval(carTimer);
						return;
					}
				
					// 如果当前位置是候车区  下一个位置是斑马线
					if ($("#"+nextGps).hasClass("bmxClass"+SOUTH_TO_NORTH) && $("#"+curGps).hasClass("carWaitClass"+EAST_TO_WEST) ){
						// 判断红绿灯
						//console.log(curGps+":汽车等候区,"+nextGps+":有斑马线");
						var lightClassName = SIGNAL_CLASS_PREFIX + WEST + roadType;
						var rgbColor = $("."+lightClassName).css("background-color");
						var color = convertRgb(rgbColor);
						if (color == RED){
							console.log(curGps+":汽车等候区,"+nextGps+":有斑马线,当前是红灯");
							clearInterval(carTimer);
							carTimerInterval(direciton,roadType,curGps,carId,false);
							return;
						}
					}
					
					// 如果当前位置是斑马线  下一个位置也是斑马线
					if ($("#"+curGps).hasClass("bmxClass"+SOUTH_TO_NORTH) && $("#"+nextGps).hasClass("bmxClass"+SOUTH_TO_NORTH) ){
						// noting to do;
						//console.log(curGps+":有斑马线,"+nextGps+":也有斑马线");
					}
					
					// 如果当前位置是斑马线  下一个位置不是斑马线
					if ($("#"+curGps).hasClass("bmxClass"+SOUTH_TO_NORTH) && !$("#"+nextGps).hasClass("bmxClass"+SOUTH_TO_NORTH) ){
						// noting to do;
						//console.log(curGps+":有斑马线,"+nextGps+":没有斑马线");
					}
					
					// 判断下一步是否有人
					if ($("#"+nextGps).hasClass("manClass")){
						return;
					}
					
					if (hasClassLike(nextGps,"carClass")){
						// 判断前面是否有车
						carTimerInterval(direciton,roadType,curGps,carId,false);
						clearInterval(carTimer);
						return;
					}
										
					curGps = nextGps;
				}
			},300);
		}
		
		function initPanel()
		{
			for (var k=1;k<=(ROW_MAX*COL_MAX);k++)
			{
				var divId = k.toString();
				var divCon = "<div id='" +divId+ "' style='float:left;width:" + DIV_WIDTH + "px;height:" + DIV_HEIGHT + "px;border:1px solid grey;'></div>";
				$("#borderDiv").append(divCon);
				
				if (SHOW_DIVID){
					$("#"+divId).text(divId);
				}
				
				var rowNo = 0;
				if (k % COL_MAX == 0 ){
					rowNo = parseInt(k/COL_MAX);
				}else{
					rowNo = parseInt(k/COL_MAX)+1;
				}
				var colNo = 0;
				if (k%COL_MAX==0){
					colNo = COL_MAX;
				}else{
					colNo = k%COL_MAX;
				}
				//$("#"+divId).text(rowNo+","+colNo);
				//$("#"+divId).css("font-size","10px");
				
				// 如果是第一行
				if (k>=1 && k<=COL_MAX){
					// 第一行
					$("#"+divId).addClass("wallClass" + NORTH);
				}else if (k>=COL_MAX*ROW_MAX-COL_MAX+1 && k<=COL_MAX*ROW_MAX){
					// 最后一行
					$("#"+divId).addClass("wallClass" + SOUTH);
				}
				if (k%COL_MAX==1){
					// 第一列
					$("#"+divId).addClass("wallClass" + WEST);
				}else if (k%COL_MAX==0){
					// 最后一列
					$("#"+divId).addClass("wallClass" + EAST);
				}
			}

			// 初始化道路、信号灯位置
			initRoad();
			
			initRoadDirection();
			
			// 初始化红绿灯位置
			initLight();
			
			// 初始化斑马线
			initBmx();
			
			//初始化汽车等待区域
			initCarWaitingZone();
			
			// 初始化行人等待区域
			initManWatingZone();
			
			// 启动化红绿灯---第一种方案
			//setWestStraightRed();
			//setWestLeftRed();
			//setWestRightGreen();
			
			//setEastStraightRed();
			//setEastLeftRed();
			//setEastRightGreen();
			
			
			//setNorthStraightGreen();
			//setNorthLeftGreen();
			//setNorthRightRed();
			
			//setSouthStraightGreen();
			//setSouthLeftGreen();
			//setSouthRightRed();
			
			//---第二种方案
			//setWestStraightRed();
			//setWestLeftRed();
			//setWestRightGreen();
			
			//setEastStraightRed();
			//setEastLeftRed();
			//setEastRightGreen();
			
			
			//setNorthStraightGreen();
			//setNorthLeftRed();
			//setNorthRightGreen();
			
			//setSouthStraightGreen();
			//setSouthLeftRed();
			//setSouthRightGreen();
			
			//---第三种方案
			setWestStraightRed();
			setWestLeftGreen();
			setWestRightRed();
			
			setEastStraightRed();
			setEastLeftGreen();
			setEastRightRed();
			
			
			setNorthStraightGreen();
			setNorthLeftRed();
			setNorthRightGreen();
			
			setSouthStraightGreen();
			setSouthLeftRed();
			setSouthRightGreen();
			
			
			mainInterval();
		}

		function initRoad(){
			// 南北方向道路所在列
			var startCol = parseInt((COL_MAX-6)/2)+1;
			var endCol = startCol+6-1;
			
			// 西东方向道路所在列
			var startRow = parseInt((ROW_MAX-6)/2)+1;
			var endRow = startRow+6-1;
			
			var xdRoadArr = getPolyRange(startRow,endRow,2,COL_MAX-1);
			
			var nbRoadArr = getPolyRange(2,ROW_MAX-1,startCol,endCol);
			
			for (var a=0;a<=xdRoadArr.length;a++){
				$("#"+xdRoadArr[a]).addClass("roadBaseClass");
			}
			
			for (var a=0;a<=nbRoadArr.length;a++){
				$("#"+nbRoadArr[a]).addClass("roadBaseClass");
			}
			
			// 东西南北中间的分割线
			var xdRoadLineArr = getPolyRange(startRow+2,startRow+2,2,COL_MAX-1);
			for (var a=0;a<=xdRoadLineArr.length;a++){
				$("#"+xdRoadLineArr[a]).css("border-color","grey grey white grey");
			}			
			
			var nbRoadLineArr = getPolyRange(2,ROW_MAX-1,startCol+2,startCol+2);
			for (var a=0;a<=nbRoadLineArr.length;a++){
				$("#"+nbRoadLineArr[a]).css("border-color","grey white grey grey");
			}
			
			var centerRoadLineArr = getPolyRange(startRow,endRow,startCol,endCol);
			for (var a=0;a<=centerRoadLineArr.length;a++){
				var divId = centerRoadLineArr[a];
				if (divId%COL_MAX==startCol){
					$("#"+divId).css("border-color","grey grey grey white");
				}
				
				if (divId%COL_MAX==endCol){
					$("#"+divId).css("border-color","grey white grey grey");
				}
				if (divId>(startRow-1)*COL_MAX && divId<startRow*COL_MAX){
					$("#"+divId).css("border-color","white grey grey grey");
				}
				if (divId>(endRow-1)*COL_MAX && divId<endRow*COL_MAX){
					$("#"+divId).css("border-color","grey grey white grey");
				}
				
				if (a==0){
					$("#"+divId).css("border-color","white grey grey white");
				}else if (a==2){
					$("#"+divId).css("border-color","white white grey grey");
				}else if (a==5){
					$("#"+divId).css("border-color","white white grey grey");
				}else if (a==12){
					$("#"+divId).css("border-color","grey grey white white");
				}else if (a==14){
					$("#"+divId).css("border-color","grey white white grey");
				}else if (a==17){
					$("#"+divId).css("border-color","grey white white grey");
				}else if (a==30){
					$("#"+divId).css("border-color","grey grey white white");
				}else if (a==32){
					$("#"+divId).css("border-color","grey white white grey");
				}else if (a==35){
					$("#"+divId).css("border-color","grey white white grey");
				}
			}
		}
		
		function initRoadDirection(){
			// 南北方向道路所在列
			var startCol = parseInt((COL_MAX-6)/2)+1;
			var endCol = startCol+6-1;
			
			// 西东方向道路所在列
			var startRow = parseInt((ROW_MAX-6)/2)+1;
			var endRow = startRow+6-1;
			
			var x2dRoadDirectionArr = getPolyRange(endRow-2,endRow,2,COL_MAX-1);
			
			var d2xRoadDirectionArr = getPolyRange(startRow,startRow+2,2,COL_MAX-1);
			
			var n2bRoadDirectionArr = getPolyRange(2,ROW_MAX-1,endCol-2,endCol);
			
			var b2nRoadDirectionArr = getPolyRange(2,ROW_MAX-1,startCol,startCol+2);

			
			for (var a=0;a<=x2dRoadDirectionArr.length;a++){
				$("#"+x2dRoadDirectionArr[a]).addClass("roadDirectionClass4");
			}
			
			for (var a=0;a<=d2xRoadDirectionArr.length;a++){
				$("#"+d2xRoadDirectionArr[a]).addClass("roadDirectionClass2");
			}
			
			for (var a=0;a<=n2bRoadDirectionArr.length;a++){
				$("#"+n2bRoadDirectionArr[a]).addClass("roadDirectionClass1");
			}
			
			for (var a=0;a<=b2nRoadDirectionArr.length;a++){
				$("#"+b2nRoadDirectionArr[a]).addClass("roadDirectionClass3");
			}
		}
		
		function initLight(){
			// 南北方向道路所在列
			var startCol = parseInt((COL_MAX-6)/2)+1;
			var endCol = startCol+6-1;
			
			// 西东方向道路所在列
			var startRow = parseInt((ROW_MAX-6)/2)+1;
			var endRow = startRow+6-1;

			for (var i=1;i<=ROW_MAX*COL_MAX;i++){
				if (i%COL_MAX >= startCol && i%COL_MAX <= endCol){
					if (i>=1 && i<=COL_MAX){
						// 第一行
						if (i%COL_MAX>=startCol+2 && i%COL_MAX<= endCol){
							// 北方红绿灯
							if (i%COL_MAX==startCol+2){
								$("#"+i).text("北");
								$("#"+i).css({"color":"blue","font-size":"25px","text-align":"center"});
							}
							else if (i%COL_MAX==startCol+3){
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+NORTH+LEFT);
							}else if (i%COL_MAX==startCol+4){
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+NORTH+STRAIGHT);
							}else{
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+NORTH+RIGHT);
								
								$("#"+(i+1)).addClass(SIGNAL_TIMER_PREFIX+NORTH);
							}
						}
					}else if (i >= COL_MAX*(ROW_MAX-1)+1 && i<= COL_MAX*ROW_MAX){
						//最后一行
						if (i%COL_MAX>=startCol && i%COL_MAX<= startCol+3){
							// 南方红绿灯
							if (i%COL_MAX==startCol){
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+SOUTH+RIGHT);
								
								$("#"+(i-1)).addClass(SIGNAL_TIMER_PREFIX+SOUTH);
							}else if (i%COL_MAX==startCol+1){
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+SOUTH+STRAIGHT);
							}else if (i%COL_MAX==startCol+2){
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+SOUTH+LEFT);
							}else{
								$("#"+i).text("南");
								$("#"+i).css({"color":"blue","font-size":"25px","text-align":"center"});
							}
						}
					}
				}
				
				if (i>=COL_MAX*(startRow-1)+1 && i<=COL_MAX*endRow){
					if (i%COL_MAX==1){
						// 西边红绿灯
						var curRow = parseInt(i/COL_MAX)+1;
						if (curRow >= startRow && curRow<= startRow+3){
							if (curRow == startRow){
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+WEST+RIGHT);
								
								$("#"+(i-COL_MAX)).addClass(SIGNAL_TIMER_PREFIX+WEST);
							}else if (curRow == startRow+1){
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+WEST+STRAIGHT);
							}else if (curRow == startRow+2){
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+WEST+LEFT);
							}else{
								$("#"+i).text("西");
								$("#"+i).css({"color":"blue","font-size":"25px","text-align":"center"});
							}
						}
						
					}else if (i%COL_MAX==0){
						// 东边红绿灯
						var curRow = parseInt(i/COL_MAX);
						
						if (curRow >= startRow+2 && curRow<= endRow){
							if (curRow == startRow+2){
								$("#"+i).text("东");
								$("#"+i).css({"color":"blue","font-size":"25px","text-align":"center"});
							}else if (curRow == startRow+3){
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+EAST+LEFT);
							}else if (curRow == startRow+4){
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+EAST+STRAIGHT);
							}else{
								$("#"+i).addClass(SIGNAL_CLASS_PREFIX+EAST+RIGHT);
								
								$("#"+(i+COL_MAX)).addClass(SIGNAL_TIMER_PREFIX+EAST);
							}
						}
					}
				}
			}			
		}
		
		function initBmx(){
			// 南北方向道路所在列
			var startCol = parseInt((COL_MAX-6)/2)+1;
			var endCol = startCol+6-1;
			
			// 西东方向道路所在列
			var startRow = parseInt((ROW_MAX-6)/2)+1;
			var endRow = startRow+6-1;
			
			// 北-南  
			var b2nArr = getPolyRange(startRow,endRow,startCol-2,startCol-1);
			
			// 南-北
			var n2bArr = getPolyRange(startRow,endRow,endCol+1,endCol+2);
			
			// 东-西
			var d2xArr = getPolyRange(startRow-2,startRow-1,startCol,endCol);
			
			// 西-东
			var x2dArr = getPolyRange(endRow+1,endRow+2,startCol,endCol);
			
			
			for (var i=0;i<b2nArr.length;i++){
				$("#"+b2nArr[i]).addClass("bmxClass3");
				$("#"+b2nArr[i]).css("border-color","white grey white grey");
			}
			
			for (var i=0;i<n2bArr.length;i++){
				$("#"+n2bArr[i]).addClass("bmxClass1");
				
				$("#"+n2bArr[i]).css("border-color","white grey white grey");
			}
			
			for (var i=0;i<d2xArr.length;i++){
				$("#"+d2xArr[i]).addClass("bmxClass2");
				
				$("#"+d2xArr[i]).css("border-color","white grey white grey");
			}
			
			for (var i=0;i<x2dArr.length;i++){
				$("#"+x2dArr[i]).addClass("bmxClass4");
				
				$("#"+x2dArr[i]).css("border-color","white grey white grey");
			}
		}
		
		function initCarWaitingZone(){
			// 南北方向道路所在列
			var startCol = parseInt((COL_MAX-6)/2)+1;
			var endCol = startCol+6-1;
			
			// 西东方向道路所在列
			var startRow = parseInt((ROW_MAX-6)/2)+1;
			var endRow = startRow+6-1;
			
			var xCarWaitArr = getPolyRange(endRow-2,endRow,2,8);
			
			var dCarWaitArr = getPolyRange(startRow,startRow+2,COL_MAX-7,COL_MAX-1);
			
			var bCarWaitArr = getPolyRange(2,4,startCol,startCol+2);
			
			var nCarWaitArr = getPolyRange(ROW_MAX-4,ROW_MAX-1,endCol-2,endCol);
			
			for (var i=0;i<xCarWaitArr.length;i++){
				$("#"+xCarWaitArr[i]).addClass("carWaitClass" + WEST_TO_EAST);
			}
			
			for (var i=0;i<dCarWaitArr.length;i++){
				$("#"+dCarWaitArr[i]).addClass("carWaitClass" + EAST_TO_WEST);
			}
			
			for (var i=0;i<bCarWaitArr.length;i++){
				$("#"+bCarWaitArr[i]).addClass("carWaitClass" + NORTH_TO_SOUTH);
			}
			
			for (var i=0;i<nCarWaitArr.length;i++){
				$("#"+nCarWaitArr[i]).addClass("carWaitClass" + SOUTH_TO_NORTH);
			}
		}
		
		function initManWatingZone(){
			// 南北方向道路所在列
			var startCol = parseInt((COL_MAX-6)/2)+1;
			var endCol = startCol+6-1;
			
			// 西东方向道路所在列
			var startRow = parseInt((ROW_MAX-6)/2)+1;
			var endRow = startRow+6-1;

			var n2bManWaitArr = getPolyRange(endRow+3,ROW_MAX-1,endCol+1,endCol+2);
			
			var d2xManWaitArr = getPolyRange(startRow-2,startRow-1,endCol+3,COL_MAX-1);
			
			var b2nManWaitArr = getPolyRange(2,startRow-3,startCol-2,startCol-1);
			
			var x2dManWaitArr = getPolyRange(endRow+1,endRow+2,2,startCol-3);
			
			for (var i=0;i<n2bManWaitArr.length;i++){
				$("#"+n2bManWaitArr[i]).addClass("manWaitClass1");
			}
			
			for (var i=0;i<d2xManWaitArr.length;i++){
				$("#"+d2xManWaitArr[i]).addClass("manWaitClass2");
			}
			
			for (var i=0;i<b2nManWaitArr.length;i++){
				$("#"+b2nManWaitArr[i]).addClass("manWaitClass3");
			}
			
			for (var i=0;i<x2dManWaitArr.length;i++){
				$("#"+x2dManWaitArr[i]).addClass("manWaitClass4");
			}
		}
				
		// 获取制定起始行,起始列的divId集合
		function getPolyRange(startRow,endRow,startCol,endCol){
			var arr = new Array();
			
			for (var r=1;r<=ROW_MAX;r++){
				for (var c=1;c<=COL_MAX;c++){
					var divId=(r-1)*COL_MAX+c;
					
					if (r>=startRow && r<=endRow && c>=startCol && c<=endCol){
						arr.push(divId);
					}
				}
			}
			
			return arr;
		}
		
		// 随机获取指定范围内的数字  闭区间
		function randomNumber(start,end){
			var range = end-start;
			var rand = Math.random();
			var num = start + Math.round(rand*range);
			
			return num;
		}
		
		// 转换颜色  参数格式为 rgb(255,0,0)
		function convertRgb(rgbColor){
			var strTmp = rgbColor.replace("rgb(","");
			strTmp = strTmp.replace(")","");
			strTmp = strTmp.replace(" ","");
			
			var colorArr = strTmp.split(",");
			if (colorArr[0] == "255" && colorArr[1] == "255"){
				return YELLOW;
			}else if (colorArr[0] == "255"){
				return RED;
			}else if (colorArr[1] == "255"){
				return GREEN;
			}
		}
		
		// 设置信号灯读秒器
		function setInputShowTimer(direction,colorType,initTime){
			var color="";
			if (colorType == RED){
				color = "rgb(255,0,0)";
			}else if (colorType == GREEN){
				color = "green";
			}else{
				color = "yellow";
			}

			$("#borderDiv ."+SIGNAL_TIMER_PREFIX+direction).text(initTime);
			$("#borderDiv ."+SIGNAL_TIMER_PREFIX+direction).css({"color":color,"font-size":"25px","text-align":"center"});
			var num = initTime-1;
			var inputTimer = setInterval(function(){
				if (num==1){
					if (color=="green"){
						$("#borderDiv ."+SIGNAL_TIMER_PREFIX+direction).css("background-color","white");
					}
					clearInterval(inputTimer);
				}
				
				if (num==4 && color == "green"){
					var count = 1;
					var tmpTimer = setInterval(function(){
						if (count >= 6){
							clearInterval(tmpTimer);
						}
						if (count %2 == 0){
							$("#borderDiv ."+SIGNAL_TIMER_PREFIX+direction).css("background-color","white");
						}else{
							$("#borderDiv ."+SIGNAL_TIMER_PREFIX+direction).css("background-color","black");
						}
						count++;
					},500);
				}

				$("#borderDiv ."+SIGNAL_TIMER_PREFIX+direction).text(num);
				$("#borderDiv ."+SIGNAL_TIMER_PREFIX+direction).css({"color":color,"font-size":"25px","text-align":"center"});
				
				num--;
			},1000);
		}

		// 设置红绿灯的样式   direction:东南西北   roadType:直行,左转,右转     colorType:红绿蓝
		function setSignalCssStyle(direction, roadType, colorType){
			var className = null;
			if (colorType == RED){
				className = "redClass";
			}else if (colorType == GREEN){
				className = "greenClass";
			}else{
				className = "yellowClass";
			}
			
			$("#borderDiv ."+SIGNAL_CLASS_PREFIX+direction+roadType).removeClass("redClass").removeClass("greenClass").removeClass("yellowClass");
			$("#borderDiv ."+SIGNAL_CLASS_PREFIX+direction+roadType).addClass(className);
		}
		
		// -----------------------------------西边信号灯---------------------------------
		
		// 西侧-直行信号灯
		function setWestStraightRed(){
			setSignalCssStyle(WEST, STRAIGHT, RED);
			
			setTimeout(setWestStraightGreen, WE_RED_TIME*1000);
			setInputShowTimer(WEST, RED, WE_RED_TIME);
		}
		function setWestStraightGreen(){
			setSignalCssStyle(WEST, STRAIGHT, GREEN);
			
			setTimeout(setWestStraightYellow, (WE_RED_TIME-3)*1000);
			setInputShowTimer(WEST, GREEN, WE_RED_TIME-3);
		}
		function setWestStraightYellow(){
			setSignalCssStyle(WEST, STRAIGHT, YELLOW);
			
			setTimeout(setWestStraightRed, 3*1000);
			setInputShowTimer(WEST, YELLOW, 3);
		}
		
		// 西侧-左转
		function setWestLeftRed(){
			setSignalCssStyle(WEST, LEFT, RED);
			
			setTimeout(setWestLeftGreen, NS_RED_TIME*1000);
		}
		function setWestLeftGreen(){
			setSignalCssStyle(WEST, LEFT, GREEN);
			
			setTimeout(setWestLeftYellow, (NS_RED_TIME-3)*1000);
		}
		function setWestLeftYellow(){
			setSignalCssStyle(WEST, LEFT, YELLOW);
			
			setTimeout(setWestLeftRed, 3*1000);
		}
		
		// 西侧-右转
		function setWestRightRed(){
			setSignalCssStyle(WEST, RIGHT, RED);
			
			setTimeout(setWestRightGreen, NS_RED_TIME*1000);
		}
		function setWestRightGreen(){
			setSignalCssStyle(WEST, RIGHT, GREEN);
			
			setTimeout(setWestRightYellow, (NS_RED_TIME-3)*1000);
		}
		function setWestRightYellow(){
			setSignalCssStyle(WEST, RIGHT, YELLOW);
			
			setTimeout(setWestRightRed, 3*1000);
		}
		
		
		// ----------------------------东边信号灯------------------------------------
		// 东侧-直行信号灯
		function setEastStraightRed(){
			setSignalCssStyle(EAST, STRAIGHT, RED);
			
			setTimeout(setEastStraightGreen, WE_RED_TIME*1000);
			setInputShowTimer(EAST, RED, WE_RED_TIME);
		}
		function setEastStraightGreen(){
			setSignalCssStyle(EAST, STRAIGHT, GREEN);
			
			setTimeout(setEastStraightYellow, (WE_RED_TIME-3)*1000);
			setInputShowTimer(EAST, GREEN, WE_RED_TIME-3);
		}
		function setEastStraightYellow(){
			setSignalCssStyle(EAST, STRAIGHT, YELLOW);
			
			setTimeout(setEastStraightRed, 3*1000);
			setInputShowTimer(EAST, YELLOW, 3);
		}
		
		// 东侧-左转
		function setEastLeftRed(){
			setSignalCssStyle(EAST, LEFT, RED);
			
			setTimeout(setEastLeftGreen, NS_RED_TIME*1000);
		}
		function setEastLeftGreen(){
			setSignalCssStyle(EAST, LEFT, GREEN);
			
			setTimeout(setEastLeftYellow, (NS_RED_TIME-3)*1000);
		}
		function setEastLeftYellow(){
			setSignalCssStyle(EAST, LEFT, YELLOW);
			
			setTimeout(setEastLeftRed, 3*1000);
		}
		
		// 东侧-右转
		function setEastRightRed(){
			setSignalCssStyle(EAST, RIGHT, RED);
			
			setTimeout(setEastRightGreen, NS_RED_TIME*1000);
		}
		function setEastRightGreen(){
			setSignalCssStyle(EAST, RIGHT, GREEN);
			
			setTimeout(setEastRightYellow, (NS_RED_TIME-3)*1000);
		}
		function setEastRightYellow(){
			setSignalCssStyle(EAST, RIGHT, YELLOW);
			
			setTimeout(setEastRightRed, 3*1000);
		}
		
		
		//-----------------------------------北边信号灯--------------------------------------
		// 北侧-直行
		function setNorthStraightRed(){
			setSignalCssStyle(NORTH, STRAIGHT, RED);
			
			setTimeout(setNorthStraightGreen, NS_RED_TIME*1000);
			setInputShowTimer(NORTH, RED, NS_RED_TIME);
		}
		function setNorthStraightGreen(){
			setSignalCssStyle(NORTH, STRAIGHT, GREEN);
			
			setTimeout(setNorthStraightYellow, (NS_RED_TIME-3)*1000);
			setInputShowTimer(NORTH, GREEN, NS_RED_TIME-3);
		}
		function setNorthStraightYellow(){
			setSignalCssStyle(NORTH, STRAIGHT, YELLOW);
			
			setTimeout(setNorthStraightRed, 3*1000);
			setInputShowTimer(NORTH, YELLOW, 3);
		}
		
		// 北侧-左转
		function setNorthLeftRed(){
			setSignalCssStyle(NORTH, LEFT, RED);
			
			setTimeout(setNorthLeftGreen, WE_RED_TIME*1000);
		}
		function setNorthLeftGreen(){
			setSignalCssStyle(NORTH, LEFT, GREEN);
			
			setTimeout(setNorthLeftYellow, (WE_RED_TIME-3)*1000);
		}
		function setNorthLeftYellow(){
			setSignalCssStyle(NORTH, LEFT, YELLOW);
			
			setTimeout(setNorthLeftRed, 3*1000);
		}
		
		// 北侧-右转
		function setNorthRightRed(){
			setSignalCssStyle(NORTH, RIGHT, RED);
			
			setTimeout(setNorthRightGreen, WE_RED_TIME*1000);
		}
		function setNorthRightGreen(){
			setSignalCssStyle(NORTH, RIGHT, GREEN);
			
			setTimeout(setNorthRightYellow, (WE_RED_TIME-3)*1000);
		}
		function setNorthRightYellow(){
			setSignalCssStyle(NORTH, RIGHT, YELLOW);
			
			setTimeout(setNorthRightRed, 3*1000);
		}
		
		//---------------------------------南边信号灯-------------------------------------
		// 南侧-直行
		function setSouthStraightRed(){
			setSignalCssStyle(SOUTH, STRAIGHT, RED);
			
			setTimeout(setSouthStraightGreen, NS_RED_TIME*1000);
			setInputShowTimer(SOUTH, RED, NS_RED_TIME);
		}
		function setSouthStraightGreen(){
			setSignalCssStyle(SOUTH, STRAIGHT, GREEN);
			
			setTimeout(setSouthStraightYellow, (NS_RED_TIME-3)*1000);
			setInputShowTimer(SOUTH, GREEN, NS_RED_TIME-3);
		}
		function setSouthStraightYellow(){
			setSignalCssStyle(SOUTH, STRAIGHT, YELLOW);
			
			setTimeout(setSouthStraightRed, 3*1000);
			setInputShowTimer(SOUTH, YELLOW, 3);
		}
		
		// 南侧-左转
		function setSouthLeftRed(){
			setSignalCssStyle(SOUTH, LEFT, RED);
			
			setTimeout(setSouthLeftGreen, WE_RED_TIME*1000);
		}
		function setSouthLeftGreen(){
			setSignalCssStyle(SOUTH, LEFT, GREEN);
			
			setTimeout(setSouthLeftYellow, (WE_RED_TIME-3)*1000);
		}
		function setSouthLeftYellow(){
			setSignalCssStyle(SOUTH, LEFT, YELLOW);
			
			setTimeout(setSouthLeftRed, 3*1000);
		}
		
		// 南侧-右转
		function setSouthRightRed(){
			setSignalCssStyle(SOUTH, RIGHT, RED);
			
			setTimeout(setSouthRightGreen, WE_RED_TIME*1000);
		}
		function setSouthRightGreen(){
			setSignalCssStyle(SOUTH, RIGHT, GREEN);
			
			setTimeout(setSouthRightYellow, (WE_RED_TIME-3)*1000);
		}
		function setSouthRightYellow(){
			setSignalCssStyle(SOUTH, RIGHT, YELLOW);
			
			setTimeout(setSouthRightRed, 3*1000);
		}
	</script>
</html>
