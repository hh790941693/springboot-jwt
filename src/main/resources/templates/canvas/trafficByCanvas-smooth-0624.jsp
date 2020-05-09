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
			body{
				margin:0;
			}

			canvas{
			}
			
			input{
				width:50px
			}
			.toggleBtn{
				margin-left:285px;
				width:25px;
			}
		</style>
		
	</head> 
	<body>
		<div id="mainDiv" style="width:1105px;height:auto;margin:5px auto;overflow:hidden;">
			<canvas id="canvas" style="width:780px;height:600px;background:#fff;float:left;">
				你的浏览器不支持canvas标签
			</canvas>

			<div style="width:312px;height:auto;float:left;font-size:16px;margin:1px 1px 1px 5px;border:2px solid green;border:1px solid green">
				<button id="ctlShow">全部收起</button>
				<div id="infoDiv" style="background-color:#1d3d79;color:white;padding-left:2px;">
					<button id="totalNumBtn" class="toggleBtn" style="">-</button>
					<hr>
					<div id="totalNumDiv">
						<span>车辆总数&nbsp;&nbsp;:</span><span id="totalCarNum" style="color:red;font-weight:bold;font-size:17px;">0</span>
						<br/>
						<span>行人总数&nbsp;&nbsp;:</span><span id="totalManNum" style="color:red;font-weight:bold;font-size:17px;">0</span>
						<br/>
						<span>FPS&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</span><span id="fpsVal" style="color:red;font-weight:bold;font-size:17px;">0</span>
					</div>

					<button id="runBtn" class="toggleBtn" style="">+</button>
					<hr>
					<div id="runDiv" style="display:none">
						汽车速度随机&nbsp;:<input id="speedSwitch" style="width:15px;" type="checkbox" checked="checked">
						<br/>
						
						显示网格&nbsp;&nbsp;&nbsp;:<input id="gridSwitch" style="width:15px;" type="checkbox" checked="checked">
						<br/>

						<div id="carSpeeddiv" style="display:none">
							汽车行驶速度:<input id="carSpeed" type="range" value=3  min=1 max=10 step=1 onchange="changeCarSpeed()">
							<span id="carSpeedShow">3</span>
						</div>
						
						行人行驶速度&nbsp;:<input id="manSpeed" type="range" value=1  min=1 max=10 step=1 onchange="changeManSpeed()">
						<span id="manSpeedShow">1</span>
						<br/>
						
						车辆刷新频率&nbsp;:<input id="carRefreashFrequency" type="range" value=300  min=18 max=2000 step=20 onchange="changeCarRefreashFrequency()">
						<span id="carFrequencyShow">300</span>
						<br/>
						
						行人刷新频率&nbsp;:<input id="manRefreashFrequency" type="range" value=300  min=18 max=2000 step=20 onchange="changeManRefreashFrequency()">
						<span id="manFrequencyShow">300</span>
						<br/>
						
						最大车辆数&nbsp;&nbsp;:<input id="maxCarNumInput" type="range" value=5  min=5 max=50 step=3 onchange="changeMaxCarNum()">
						<span id="maxCarNumShow">5</span>
						<br/>
						
						最大行人数&nbsp;&nbsp;:<input id="maxManNumInput" type="range" value=5  min=5 max=50 step=3 onchange="changeMaxManNum()">
						<span id="maxManNumShow">5</span>
					</div>
					
					<button id="objBtn" class="toggleBtn" style="">-</button>
					<hr>
					<div id="objDiv" style="width:240px;">
						类别&nbsp;&nbsp;&nbsp;&nbsp;:<span id="objType">无</span>
						<br/>
						坐标&nbsp;&nbsp;&nbsp;&nbsp;:<span id="objGps">(0,0)</span>
						<br/>
						行列&nbsp;&nbsp;&nbsp;&nbsp;:<span id="objRowCol">0,0</span>
						<br/>
						血量&nbsp;&nbsp;&nbsp;&nbsp;:<span id="objBlood">0</span>
						<br/>
						血量比&nbsp;&nbsp;&nbsp;:<span id="objBloodPercent">0%</span>
						<br/>
						速度&nbsp;&nbsp;&nbsp;&nbsp;:<span id="objSpeed">0</span>
						<br/>
						背景颜色&nbsp;&nbsp;:<select id="bgColor" onchange="changeBgColor()">
							<option value="white" selected="selected">白色</option>
							<option value="black">黑色</option>
						</select>
					</div>
					
					<button id="mapBtn" class="toggleBtn" style="">-</button>
					<hr>
					<div id="mapDiv" style="width:312px;height:290px;">
						小地图模式:<input name="smallMapMode" type="radio" value="1" checked="checked">复杂<input name="smallMapMode" type="radio" value="2">简单
						<canvas id="smallMapCanvas" style="background:black;"></canvas>
						<div align="right">
							<button id="beSmallButton" style="width:25px;">-</button>   <button id="beBigButton" style="width:25px;">+</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	</body>
	<script type="text/javascript">	
		// 大地图画布
		var canvas = document.getElementById("canvas");
		canvas.width  = 780;
		canvas.height = 600;
		var ctx = canvas.getContext("2d");
		
		// 右下角小地图画布
		var smallMapCanvas = document.getElementById("smallMapCanvas");
		smallMapCanvas.width  = 312;
		smallMapCanvas.height = 240;
		var smallMapCtx = smallMapCanvas.getContext("2d");

		// 大地图每一个网格的宽高 单位:像素
		var DIV_WIDTH  = 30;
		var DIV_HEIGHT = 30;
		
		// 汽车的各个方向的图形缓冲画布
		var bufferCarCanvas = document.createElement("canvas");
		bufferCarCanvas.width = DIV_WIDTH*4;
		bufferCarCanvas.height = DIV_HEIGHT;
		var bufferCtx = bufferCarCanvas.getContext("2d");
		
		// 道路静态图形画布
		var bufferBaseCanvas = document.createElement("canvas");
		bufferBaseCanvas.width = canvas.width;
		bufferBaseCanvas.height = canvas.height;
		var bufferBaseCtx = bufferBaseCanvas.getContext("2d");
		
		// 红黄绿灯时间
		var RED_TIME    = 10;
		var YELLOW_TIME = 3;
		var GREEN_TIME  = RED_TIME-YELLOW_TIME;
		
		// 最大行、最大列
		var MAX_COL = parseInt(canvas.width/DIV_WIDTH);
		var MAX_ROW = parseInt(canvas.height/DIV_HEIGHT);
		
		// 道路的起始结束行
		var START_ROW = (MAX_ROW-6)/2+1; //8
		var END_ROW   = START_ROW+6-1; //13
		
		// 道路的起始结束列
		var START_COL = (MAX_COL-6)/2+1; //11
		var END_COL   = START_COL+6-1;   //16
		
		// 汽车和行人生成间隔  单位:毫秒
		var CAR_GENERATE_INTERVAL = 300;
		var MAN_GENERATE_INTERVAL = 300;
		
		// 最大车辆数、最大行人数
		var MAX_CAR_NUM = 5;
		var MAX_MAN_NUM = 5;
		
		// 汽车、行人行驶速度  单位:像素
		var CAR_SPEED = 3;  
		var MAN_SPEED = 3;
		
		// 汽车速度是否随机
		var BOOL_RANDOM_SPEED = true;
		// 是否显示地面网格
		var BOOL_SHOW_ROAD_GRID = true;
		// 小地图显示模式 1:复杂 2:简单
		var SMALL_MAP_MODE = 1;
		// 小地图放大级别 0-7 级别越大 地图越大
		var SMALL_MAP_LEVEL = 0;
		
		// 道路方向:左转、直行、右转
		var LEFT=1, CENTER=2, RIGHT=3;
		// 道路方向:北-南、南-北、西-东、东-西
		var NORTH_TO_SOUTH=1,SOUTH_TO_NORTH=2,WEST_TO_EAST=3,EAST_TO_WEST=4;
		
		// 左右上下方向
		var D_LEFT=1,D_RIGHT=2,D_UP=3,D_DOWN=4;
		
		// 颜色
		var GREEN_COLOR = "#00FF00";
		var RED_COLOR = "#FF0000";
		var YELLOW_COLOR = "#FFFF00"
		
		// 每秒钟画面刷新次数
		var FPS = 0;
		
		// 鼠标位于大地图的坐标位置(相对于大地图左上角的位置)
		var MOUSE_CANVAS_X = 0;
		var MOUSE_CANVAS_Y = 0;
		
		// 小地图拖拽事件,是否被单击
		var MOUSE_DOWN = false;
		// 在小地图上单击时的坐标
		var MOUSE_DOWN_X = 0;
		var MOUSE_DOWN_Y = 0;
		// 在小地图上移动时相对于左键被按下时的偏移距离
		var MOUSE_MOVE_DX = 0;
		var MOUSE_MOVE_DY = 0;
		
		// 汽车等候区坐标
		var CAR_WAIT_ZONE = {
			NORTH_TO_SOUTH: getRangePos(START_ROW-3,START_ROW-3,START_COL,START_COL+2),
			SOUTH_TO_NORTH: getRangePos(END_ROW+3,END_ROW+3,END_COL-2,END_COL),
			WEST_TO_EAST:   getRangePos(END_ROW-2,END_ROW,START_COL-3,START_COL-3),
			EAST_TO_WEST:   getRangePos(START_ROW,START_ROW+2,END_COL+3,END_COL+3)
		}
		
		// 行人等候区坐标
		var MAN_WAIT_ZONE = {
			NORTH_TO_SOUTH: getRangePos(START_ROW-3,START_ROW-3,START_COL-2,START_COL-1),
			SOUTH_TO_NORTH: getRangePos(END_ROW+3,END_ROW+3,END_COL+1,END_COL+2),
			WEST_TO_EAST:   getRangePos(END_ROW+1,END_ROW+2,START_COL-3,START_COL-3),
			EAST_TO_WEST:   getRangePos(START_ROW-2,START_ROW-1,END_COL+3,END_COL+3)		
		}
		
		// 汽车初始化位置
		var CAR_INIT_GPS = {
			NORTH_TO_SOUTH:{
				LEFT:new Point((START_COL+1)*DIV_WIDTH, DIV_HEIGHT),
				CENTER:new Point(START_COL*DIV_WIDTH,DIV_HEIGHT),
				RIGHT:new Point((START_COL-1)*DIV_WIDTH,DIV_HEIGHT)
			},
			SOUTH_TO_NORTH:{
				LEFT:new Point((START_COL+2)*DIV_WIDTH,(MAX_ROW-2)*DIV_HEIGHT),
				CENTER:new Point((START_COL+3)*DIV_WIDTH,(MAX_ROW-2)*DIV_HEIGHT),
				RIGHT:new Point((START_COL+4)*DIV_WIDTH,(MAX_ROW-2)*DIV_HEIGHT)
			},
			WEST_TO_EAST:{
				LEFT:new Point(DIV_WIDTH,(START_ROW+2)*DIV_HEIGHT),
				CENTER:new Point(DIV_WIDTH,(START_ROW+3)*DIV_HEIGHT),
				RIGHT:new Point(DIV_WIDTH,(START_ROW+4)*DIV_HEIGHT)
			},
			EAST_TO_WEST:{
				LEFT:new Point((MAX_COL-2)*DIV_WIDTH,(START_ROW+1)*DIV_HEIGHT),
				CENTER:new Point((MAX_COL-2)*DIV_WIDTH,(START_ROW)*DIV_HEIGHT),
				RIGHT:new Point((MAX_COL-2)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT)
			}
		}
		
		// 行人初始化位置
		var MAN_INIT_GPS = {
			NORTH_TO_SOUTH: getRangePos(2,2,START_COL-2,START_COL-1),
			SOUTH_TO_NORTH: getRangePos(MAX_ROW-1,MAX_ROW-1,END_COL+1,END_COL+2),
			WEST_TO_EAST:   getRangePos(END_ROW+1,END_ROW+2,2,2),
			EAST_TO_WEST:   getRangePos(START_ROW-2,START_ROW-1,MAX_COL-1,MAX_COL-1)
		}

		//汽车运行轨迹坐标
		var CAR_RUN_GPS = {
			NORTH_TO_SOUTH:{
				LEFT:addTwoRangePos(getRangePos(2,END_ROW-2,START_COL+2,START_COL+2),getRangePos(END_ROW-2,END_ROW-2,START_COL+2,MAX_COL-1)),
				CENTER:getRangePos(2,MAX_ROW-1,START_COL+1,START_COL+1),
				RIGHT:addTwoRangePos(getRangePos(2,START_ROW,START_COL,START_COL),getRangePos(START_ROW,START_ROW,2,START_COL)),
			},
			SOUTH_TO_NORTH:{
				LEFT:addTwoRangePos(getRangePos(START_ROW+2,START_ROW+2,2,START_COL+3), getRangePos(START_ROW+2,MAX_ROW-1,START_COL+3,START_COL+3)),
				CENTER:getRangePos(2,MAX_ROW-1,END_COL-1,END_COL-1),
				RIGHT:addTwoRangePos(getRangePos(END_ROW,END_ROW,END_COL,MAX_COL-1),getRangePos(END_ROW,MAX_ROW-1,END_COL,END_COL))			
			},
			WEST_TO_EAST:{
				LEFT:addTwoRangePos(getRangePos(END_ROW-2,END_ROW-2,2,END_COL-2),getRangePos(2,END_ROW-2,END_COL-2,END_COL-2)),
				CENTER:getRangePos(END_ROW-1,END_ROW-1,2,MAX_COL-1),
				RIGHT:addTwoRangePos(getRangePos(END_ROW,END_ROW,2,START_COL),getRangePos(END_ROW,MAX_ROW-1,START_COL,START_COL))			
			},
			EAST_TO_WEST:{
				LEFT:addTwoRangePos(getRangePos(START_ROW+2,START_ROW+2,START_COL+2,MAX_COL-1),getRangePos(START_ROW+2,MAX_ROW-1,START_COL+2,START_COL+2)),
				CENTER:getRangePos(START_ROW+1,START_ROW+1,2,MAX_COL-1),
				RIGHT:addTwoRangePos(getRangePos(2,START_ROW,END_COL,END_COL),getRangePos(START_ROW,START_ROW,END_COL,MAX_COL-1))			
			}
		}

		//------------------------------------------以下变量虽是全局变量,但是是变化的--------------------------------------------------------------
		// ------------------------------------------信号灯计时器---------------------------------------------
		var westTimer = new Text(8,(START_ROW-1)*DIV_HEIGHT-5,0,RED_COLOR,25);
		var eastTimer = new Text(canvas.width-DIV_WIDTH+8,(END_ROW+1)*DIV_HEIGHT-5,0,RED_COLOR,25);
		var northTimer = new Text((END_COL)*DIV_WIDTH+8,DIV_HEIGHT-5,0,GREEN_COLOR,25);
		var southTimer = new Text((START_COL-2)*DIV_WIDTH+8,canvas.height-5,0,GREEN_COLOR,25);
		
		// -------------------------------------------东南西北风向---------------------------------------------
		var westDirectionText = new Text(2,(START_ROW+3)*DIV_HEIGHT-5,"西","blue",25);
		var eastDirectionText = new Text(canvas.width-DIV_WIDTH+2,(END_ROW-3)*DIV_HEIGHT-5,"东","blue",25);
		var northDirectionText = new Text((END_COL-4)*DIV_WIDTH+2,DIV_HEIGHT-5,"北","blue",25);
		var southDirectionText = new Text((START_COL+2)*DIV_WIDTH+2,canvas.height-5,"南","blue",25);
		
		
		//-------------------------------------------------红绿灯---------------------------------------------------
		// 北边
		var north_left_lamp =     new Circle(ctx, "nl", (END_COL-3)*DIV_WIDTH+DIV_HEIGHT/2, DIV_HEIGHT/2,DIV_HEIGHT/2-2, RED_COLOR);
		var north_straight_lamp = new Circle(ctx, "ns", (END_COL-2)*DIV_WIDTH+DIV_HEIGHT/2, DIV_HEIGHT/2,DIV_HEIGHT/2-2, GREEN_COLOR);
		var north_right_lamp =    new Circle(ctx, "nr", (END_COL-1)*DIV_WIDTH+DIV_HEIGHT/2, DIV_HEIGHT/2,DIV_HEIGHT/2-2, GREEN_COLOR);
		
		// 南边
		var south_right_lamp =    new Circle(ctx, "sr", (START_COL-1)*DIV_WIDTH+DIV_HEIGHT/2, canvas.height-DIV_HEIGHT/2,DIV_HEIGHT/2-2, GREEN_COLOR);
		var south_straight_lamp = new Circle(ctx, "ss", START_COL*DIV_WIDTH+DIV_HEIGHT/2,     canvas.height-DIV_HEIGHT/2,DIV_HEIGHT/2-2, GREEN_COLOR);
		var south_left_lamp =     new Circle(ctx, "sl", (START_COL+1)*DIV_WIDTH+DIV_HEIGHT/2, canvas.height-DIV_HEIGHT/2,DIV_HEIGHT/2-2, RED_COLOR);
		
		// 西边
		var west_right_lamp =     new Circle(ctx, "wr" ,DIV_WIDTH/2, (START_ROW-1)*DIV_HEIGHT+DIV_HEIGHT/2,     DIV_HEIGHT/2-2, RED_COLOR);
		var west_straight_lamp =  new Circle(ctx, "ws" ,DIV_WIDTH/2, (START_ROW)*DIV_HEIGHT+DIV_HEIGHT/2, DIV_HEIGHT/2-2,  RED_COLOR);
		var west_left_lamp =      new Circle(ctx, "wl" ,DIV_WIDTH/2, (START_ROW+1)*DIV_HEIGHT+DIV_HEIGHT/2, DIV_HEIGHT/2-2, GREEN_COLOR);
		
		// 东边
		var east_left_lamp =      new Circle(ctx, "el" ,canvas.width-DIV_WIDTH/2, (END_ROW-3)*DIV_HEIGHT+DIV_HEIGHT/2, DIV_HEIGHT/2-2, GREEN_COLOR);
		var east_straight_lamp =  new Circle(ctx, "es" ,canvas.width-DIV_WIDTH/2, (END_ROW-2)*DIV_HEIGHT+DIV_HEIGHT/2, DIV_HEIGHT/2-2, RED_COLOR);
		var east_right_lamp =     new Circle(ctx, "er" ,canvas.width-DIV_WIDTH/2, (END_ROW-1)*DIV_HEIGHT+DIV_HEIGHT/2, DIV_HEIGHT/2-2, RED_COLOR);

		// 汽车数组
		var CAR_ARR = new Array();
		// 行人数组
		var MAN_ARR = new Array();
		
		// 汽车资源图片
		var carImage = new Image();
		carImage.src = "<%=path%>/img/car.jpg";
		// 行人资源图片
		var manImage = new Image();
		manImage.src = "<%=path%>/img/man.png";

		// 初始化面板网格
		function initPanel(content){
			content.beginPath();
			var startRow = 0,endRow = MAX_ROW;
			var startCol = 0,endCol = MAX_COL;
			if (!BOOL_SHOW_ROAD_GRID){
				startRow = START_ROW;
				endRow = END_ROW;
				startCol = START_COL;
				endCol = END_COL;
			}
			
			for (var y=startRow*DIV_HEIGHT;y<=endRow*DIV_HEIGHT;y+=DIV_HEIGHT){
				content.moveTo(0,y);
				content.lineTo(canvas.width,y);
				content.strokeStyle="grey";
				content.stroke();
			}
			for (var x=startCol*DIV_WIDTH;x<=endCol*DIV_WIDTH;x+=DIV_WIDTH){
				content.moveTo(x,0);
				content.lineTo(x,canvas.height);
				content.strokeStyle="grey";
				content.stroke();
			}
			content.closePath();
		}
		
		// 初始化道路路基颜色以及行人等候区道路颜色
		function initRoadBackground(content){
			// 道路路基颜色
			content.beginPath();
			content.fillStyle="#666665";
			// 西东
			content.fillRect(DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT,canvas.width-2*DIV_WIDTH,6*DIV_HEIGHT);
			// 南北
			content.fillRect((START_COL-1)*DIV_WIDTH,DIV_HEIGHT,6*DIV_WIDTH,canvas.height-2*DIV_HEIGHT);
			content.closePath();
			
			// 行人等候区
			content.beginPath();
			content.fillStyle="#BBFF66";
			// 南
			content.fillRect(END_COL*DIV_WIDTH,(END_ROW+2)*DIV_HEIGHT,2*DIV_WIDTH,(MAX_ROW-END_ROW-3)*DIV_HEIGHT);
			// 北
			content.fillRect((START_COL-3)*DIV_WIDTH,DIV_HEIGHT,2*DIV_WIDTH,(START_ROW-4)*DIV_HEIGHT);
			// 西
			content.fillRect(DIV_WIDTH,(END_ROW)*DIV_HEIGHT,(START_COL-4)*DIV_WIDTH,2*DIV_HEIGHT);
			// 东
			content.fillRect((END_COL+2)*DIV_WIDTH,(START_ROW-3)*DIV_HEIGHT,(MAX_COL-END_COL-3)*DIV_WIDTH,2*DIV_HEIGHT);
			content.closePath();
		}
		
		// 初始化道路路线以及斑马线等
		function initRoadSperateLine(content){
			content.beginPath();
			// 水平
			content.moveTo(DIV_WIDTH, (START_ROW+2)*DIV_HEIGHT-3);
			content.lineTo(canvas.width-DIV_WIDTH, (START_ROW+2)*DIV_HEIGHT-3);
			
			content.moveTo(DIV_WIDTH, (START_ROW+2)*DIV_HEIGHT+3);
			content.lineTo(canvas.width-DIV_WIDTH, (START_ROW+2)*DIV_HEIGHT+3);
			content.strokeStyle="#FFCC22";

			// 垂直
			content.moveTo((START_COL+2)*DIV_WIDTH-3,DIV_HEIGHT);
			content.lineTo((START_COL+2)*DIV_WIDTH-3,canvas.height-DIV_HEIGHT);
			
			content.moveTo((START_COL+2)*DIV_WIDTH+3,DIV_HEIGHT);
			content.lineTo((START_COL+2)*DIV_WIDTH+3,canvas.height-DIV_HEIGHT);
			
			content.stroke();
			content.closePath();
			
			
			content.beginPath();
			// 中间
			content.strokeStyle="#FFFFFF";
			content.rect((START_COL-1)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT,6*DIV_WIDTH,6*DIV_HEIGHT);
			
			// 斑马线
			for (var y=(START_ROW-1)*DIV_HEIGHT;y<=(START_ROW-1)*DIV_HEIGHT+6*DIV_HEIGHT;y+=DIV_HEIGHT/2){
				//西边
				content.moveTo((START_COL-3)*DIV_WIDTH,y);
				content.lineTo((START_COL-1)*DIV_WIDTH,y);
				
				// 东边
				content.moveTo(END_COL*DIV_WIDTH,y);
				content.lineTo((END_COL+2)*DIV_WIDTH,y);
			}
			
			for (var x=(START_COL-1)*DIV_WIDTH;x<=(START_COL-1)*DIV_WIDTH+6*DIV_WIDTH;x+=DIV_WIDTH/2){
				// 北边
				content.moveTo(x,(START_ROW-3)*DIV_HEIGHT);
				content.lineTo(x,(START_ROW-1)*DIV_HEIGHT);	

				// 南边
				content.moveTo(x,(END_ROW)*DIV_HEIGHT);
				content.lineTo(x,(END_ROW+2)*DIV_HEIGHT);					
			}
			
			// 车辆候车线
			// 西
			content.moveTo((START_COL-3)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT);
			content.lineTo((START_COL-3)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT+6*DIV_HEIGHT);
			
			// 东
			content.moveTo((END_COL+2)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT);
			content.lineTo((END_COL+2)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT+6*DIV_HEIGHT);
			
			// 北
			content.moveTo((START_COL-1)*DIV_WIDTH,(START_ROW-3)*DIV_HEIGHT);
			content.lineTo((START_COL-1)*DIV_WIDTH+6*DIV_WIDTH,(START_ROW-3)*DIV_HEIGHT);			

			// 南
			content.moveTo((START_COL-1)*DIV_WIDTH,(END_ROW+2)*DIV_HEIGHT);
			content.lineTo((START_COL-1)*DIV_WIDTH+6*DIV_WIDTH,(END_ROW+2)*DIV_HEIGHT);
			
			content.stroke();
			content.closePath();
		}

		// 定时更新信号灯颜色以及读秒器
		function drawLight(){
			north_left_lamp.update();
			north_straight_lamp.update();
			north_right_lamp.update();
			northDirectionText.update();
			
			south_left_lamp.update();
			south_straight_lamp.update();
			south_right_lamp.update();
			southDirectionText.update();
			
			west_left_lamp.update();
			west_straight_lamp.update();
			west_right_lamp.update();
			westDirectionText.update();
			
			east_left_lamp.update();
			east_straight_lamp.update();
			east_right_lamp.update();
			eastDirectionText.update();
		}
		
		// 构建汽车各个方向的图形到缓冲画布中
		carImage.onload = function(){
			var bufferWidth = DIV_WIDTH*2;
			var bufferHeight = DIV_HEIGHT*2;
			
			for (var angle=0;angle<=3*Math.PI/2;angle+=Math.PI/2){
				var tempCanvas = document.createElement("canvas");
				tempCanvas.width = bufferWidth;
				tempCanvas.height = bufferHeight;
				var tempCtx = tempCanvas.getContext("2d");
				tempCtx.translate(bufferWidth/2,bufferHeight/2);
				tempCtx.rotate(angle);
				tempCtx.drawImage(carImage,0,0,bufferWidth/2,bufferHeight/2);
				
				if (angle == 0){
					// 右
					bufferCtx.drawImage(tempCanvas,30,30,30,30,0,0,DIV_WIDTH,DIV_HEIGHT);
				} else if (angle == Math.PI/2){
					// 下
					bufferCtx.drawImage(tempCanvas,0,30,30,30,30,0,DIV_WIDTH,DIV_HEIGHT);				
				} else if (angle == Math.PI){
					// 左
					bufferCtx.drawImage(tempCanvas,0,0,30,30,60,0,DIV_WIDTH,DIV_HEIGHT);				
				} else if (angle == 3*Math.PI/2){
					// 上
					bufferCtx.drawImage(tempCanvas,30,0,30,30,90,0,DIV_WIDTH,DIV_HEIGHT);					
				}
			}
		}
		
		// 构建道路的静态图形到缓冲画布中
		manImage.onload = function(){
			initRoadBackground(bufferBaseCtx);
			initPanel(bufferBaseCtx);
			initRoadSperateLine(bufferBaseCtx);
		}
		
		// 小地图拖拽事件
		$("#smallMapCanvas").mousedown(function(event){
			// 记录点击坐标
			var canvasObj = document.getElementById("smallMapCanvas");
			var offleft = canvasObj.offsetLeft;
			var offtop = canvasObj.offsetTop;
			
			var clientX = event.clientX;
			var clientY = event.clientY;
			
			var mouseX = clientX-offleft;
			var mouseY = clientY-offtop;
			MOUSE_DOWN_X = mouseX;
			MOUSE_DOWN_Y = mouseY;

			//console.log("单击已按下:"+mouseX+","+mouseY);
			MOUSE_DOWN = true;
		})
		
		$("#smallMapCanvas").mousemove(function(event){
			if (MOUSE_DOWN){
				var canvasObj = document.getElementById("smallMapCanvas");
				var offleft = canvasObj.offsetLeft;
				var offtop = canvasObj.offsetTop;
				
				var clientX = event.clientX;
				var clientY = event.clientY;
				
				var mouseX = clientX-offleft;
				var mouseY = clientY-offtop;
				
				if (mouseX < MOUSE_DOWN_X){
					MOUSE_MOVE_DX = MOUSE_DOWN_X - mouseX;  // 正数
				}else{
					MOUSE_MOVE_DX = MOUSE_DOWN_X - mouseX; //负数
				}
				
				if (mouseY < MOUSE_DOWN_Y){
					MOUSE_MOVE_DY =  MOUSE_DOWN_Y - mouseY;    //正数
				}else{
					MOUSE_MOVE_DY =  MOUSE_DOWN_Y - mouseY;    //负数
				}
			}
		})
		
		$("#smallMapCanvas").mouseup(function(event){
			// 记录点击坐标
			var canvasObj = document.getElementById("smallMapCanvas");
			var offleft = canvasObj.offsetLeft;
			var offtop = canvasObj.offsetTop;
			
			var clientX = event.clientX;
			var clientY = event.clientY;
			
			var mouseX = clientX-offleft;
			var mouseY = clientY-offtop;
			//console.log("单击被释放:"+mouseX+","+mouseY);
			MOUSE_DOWN = false;
		})
		
		// 大地图鼠标移动事件
		$("#canvas").mousemove(function(event){
			var canvasObj = document.getElementById("canvas");
			var mousePos = windowToCanvas(event);

			if (mousePos.x>=0 && mousePos.x<=canvas.width && mousePos.y>=0 && mousePos.y<=canvas.height){
				MOUSE_CANVAS_X = mousePos.x;
				MOUSE_CANVAS_Y = mousePos.y
			}
		});

		// 鼠标移动时显示对应物体的坐标、血量等信息
		function drawTitleInfo(mouseX, mouseY){
			var isFindRecord = false;
			var rowNum = parseInt(mouseY/DIV_WIDTH)+1;
			var colNum = parseInt(mouseX/DIV_HEIGHT)+1;
			
			for (var i=0;i<MAN_ARR.length;i++){
				if (mouseX >= MAN_ARR[i].x && mouseY>= MAN_ARR[i].y && getTwoPointDistance(mouseX, mouseY, MAN_ARR[i].x, MAN_ARR[i].y) <= DIV_WIDTH){
					$("#objType").text("行人");
					$("#objGps").text("("+MAN_ARR[i].x+","+MAN_ARR[i].y+")");
					$("#objBlood").text(MAN_ARR[i].curBlood+"/"+MAN_ARR[i].totalBlood);
					$("#objBloodPercent").text(parseInt(MAN_ARR[i].curBlood*100/MAN_ARR[i].totalBlood)+"%");
					$("#objSpeed").text(MAN_ARR[i].dx);
					isFindRecord = true;
					break;
				}
			}
			
			for (var i=0;i<CAR_ARR.length;i++){
				if (mouseX >= CAR_ARR[i].x && mouseY>= CAR_ARR[i].y && getTwoPointDistance(mouseX, mouseY, CAR_ARR[i].x, CAR_ARR[i].y) <= DIV_WIDTH){
					$("#objType").text("汽车");
					$("#objGps").text("("+CAR_ARR[i].x+","+CAR_ARR[i].y+")");
					$("#objBlood").text(CAR_ARR[i].curBlood+"/"+CAR_ARR[i].totalBlood);
					$("#objBloodPercent").text(parseInt(CAR_ARR[i].curBlood*100/CAR_ARR[i].totalBlood)+"%");
					$("#objSpeed").text(CAR_ARR[i].dx);
					isFindRecord = true;
					break;
				}
			}

			if (!isFindRecord){
				$("#objType").text("无");
				$("#objGps").text("("+mouseX+","+mouseY+")");
				$("#objRowCol").text(rowNum+","+colNum);
				$("#objBlood").text("0/0");
				$("#objBloodPercent").text("0%");
				$("#objSpeed").text(0);
			}
		}
		
		// 生成一辆车
		function createCar(){
			if (CAR_ARR.length >= MAX_CAR_NUM){
				return;
			}
			var direction = randomNumber(1,4);
			var roadType = randomNumber(1,3);

			// 初始坐标
			var initPoint;
			// 运动轨迹
			//var runPointArr;
			// 汽车斑马线候车位置
			var carWaitArr;
			// 汽车初始行驶方向
			var runDirect;
			// 红绿灯对象
			var lampObj;
			var timerObj;
			if (direction == SOUTH_TO_NORTH){
				carWaitArr = CAR_WAIT_ZONE.SOUTH_TO_NORTH;
				runDirect = D_UP;
				timerObj = northTimer;
				if (roadType == LEFT){					
					initPoint = CAR_INIT_GPS.SOUTH_TO_NORTH.LEFT;
					//runPointArr = CAR_RUN_GPS.SOUTH_TO_NORTH.LEFT;
					lampObj = north_left_lamp;
				} else if (roadType == CENTER){
					initPoint = CAR_INIT_GPS.SOUTH_TO_NORTH.CENTER;
					//runPointArr = CAR_RUN_GPS.SOUTH_TO_NORTH.CENTER;
					lampObj = north_straight_lamp;
				} else if (roadType == RIGHT){
					initPoint = CAR_INIT_GPS.SOUTH_TO_NORTH.RIGHT;
					//runPointArr = CAR_RUN_GPS.SOUTH_TO_NORTH.RIGHT;
					lampObj = north_right_lamp;
				}
			} else if (direction == NORTH_TO_SOUTH){
				carWaitArr = CAR_WAIT_ZONE.NORTH_TO_SOUTH;
				runDirect = D_DOWN;
				timerObj = southTimer;
				if (roadType == LEFT){
					initPoint = CAR_INIT_GPS.NORTH_TO_SOUTH.LEFT;	
					//runPointArr = CAR_RUN_GPS.NORTH_TO_SOUTH.LEFT;
					lampObj = south_left_lamp;
				} else if (roadType == CENTER){
					initPoint = CAR_INIT_GPS.NORTH_TO_SOUTH.CENTER;	
					//runPointArr = CAR_RUN_GPS.NORTH_TO_SOUTH.CENTER;	
					lampObj = south_straight_lamp;					
				} else if (roadType == RIGHT){
					initPoint = CAR_INIT_GPS.NORTH_TO_SOUTH.RIGHT;
					//runPointArr = CAR_RUN_GPS.NORTH_TO_SOUTH.RIGHT;	
                    lampObj = south_right_lamp;					
				}					
			} else if (direction == WEST_TO_EAST){
				carWaitArr = CAR_WAIT_ZONE.WEST_TO_EAST;
				runDirect = D_RIGHT;
				timerObj = eastTimer;
				if (roadType == LEFT){
					initPoint = CAR_INIT_GPS.WEST_TO_EAST.LEFT;	
					//runPointArr = CAR_RUN_GPS.WEST_TO_EAST.LEFT;
                    lampObj = east_left_lamp; 					
				} else if (roadType == CENTER){
					initPoint = CAR_INIT_GPS.WEST_TO_EAST.CENTER;
					//runPointArr = CAR_RUN_GPS.WEST_TO_EAST.CENTER;
                    lampObj = east_straight_lamp;					
				} else if (roadType == RIGHT){
					initPoint = CAR_INIT_GPS.WEST_TO_EAST.RIGHT;
					//runPointArr = CAR_RUN_GPS.WEST_TO_EAST.RIGHT;	
                    lampObj = east_right_lamp;					
				}					
			} else if (direction == EAST_TO_WEST){
				carWaitArr = CAR_WAIT_ZONE.EAST_TO_WEST;
				runDirect = D_LEFT;
				timerObj = westTimer;
				if (roadType == LEFT){
					initPoint = CAR_INIT_GPS.EAST_TO_WEST.LEFT;	
					//runPointArr = CAR_RUN_GPS.EAST_TO_WEST.LEFT;		
                    lampObj = west_left_lamp;					
				} else if (roadType == CENTER){
					initPoint = CAR_INIT_GPS.EAST_TO_WEST.CENTER;
					//runPointArr = CAR_RUN_GPS.EAST_TO_WEST.CENTER;
                    lampObj = west_straight_lamp;					
				} else if (roadType == RIGHT){
					initPoint = CAR_INIT_GPS.EAST_TO_WEST.RIGHT;
					//runPointArr = CAR_RUN_GPS.EAST_TO_WEST.RIGHT;
                    lampObj = west_right_lamp;					
				}	
			}
			
			CAR_ARR.push(new Car(initPoint, runDirect, carWaitArr, direction, roadType, lampObj, timerObj));
		}
		
		// 生成一个行人
		function createMan(){
			if (MAN_ARR.length >= MAX_MAN_NUM){
				return;
			}
			var direction = randomNumber(1,4);
			var randomPos = randomNumber(0,1);
			
			// 初始坐标
			var initPoint;
			// 汽车斑马线候车位置
			var carWaitArr;
			var runDirect;
			// 红绿灯对象
			var lampObj;
			var timerObj;
			if (direction == SOUTH_TO_NORTH){
				manWaitArr = MAN_WAIT_ZONE.SOUTH_TO_NORTH;
				runDirect = D_UP;
				lampObj = north_straight_lamp;
				timerObj = northTimer;
				initPoint = MAN_INIT_GPS.SOUTH_TO_NORTH[randomPos];
			} else if (direction == NORTH_TO_SOUTH){
				runDirect = D_DOWN;
				manWaitArr = MAN_WAIT_ZONE.NORTH_TO_SOUTH;
				lampObj = south_straight_lamp;		
				timerObj = southTimer;
				initPoint = MAN_INIT_GPS.NORTH_TO_SOUTH[randomPos];	
			} else if (direction == WEST_TO_EAST){
				runDirect = D_RIGHT;
				manWaitArr = MAN_WAIT_ZONE.WEST_TO_EAST;
				lampObj = east_straight_lamp;	
				timerObj = eastTimer;
				initPoint = MAN_INIT_GPS.WEST_TO_EAST[randomPos];	
			} else if (direction == EAST_TO_WEST){
				manWaitArr = MAN_WAIT_ZONE.EAST_TO_WEST;
				runDirect = D_LEFT;
				lampObj = west_straight_lamp;	
				timerObj = westTimer;
				initPoint = MAN_INIT_GPS.EAST_TO_WEST[randomPos];
			}
			
			MAN_ARR.push(new Man(initPoint, runDirect, manWaitArr, direction, lampObj, timerObj));
		}
				
		// 信号灯类
		function Circle(content,id,x,y,r,color){
			this.content = content; // 画图句柄
			this.id = id;
			this.x = x;
			this.y = y;
			this.r = r;
			this.color = color;
			this.time = 0;
			this.showTime = 0;
			
			this.draw = function(){
				this.content.beginPath();
				this.content.arc(x,y,r,0,2*Math.PI,false);
				this.content.fillStyle=this.color;
				this.content.fill();
				this.content.closePath();
			}
			
			this.update = function(){
				this.time = g_second%(RED_TIME*2)
				if (this.id=="wr" || this.id=="ws"|| this.id=="er" || this.id=="es" || this.id=="nl" || this.id=="sl"){
					if (this.time >=0 && this.time<RED_TIME){
						this.color=RED_COLOR;
						this.showTime = RED_TIME - this.time;
					}else if (this.time>=RED_TIME && this.time<RED_TIME+GREEN_TIME){
						this.color=GREEN_COLOR;
						this.showTime = RED_TIME+GREEN_TIME - this.time;
					}else{
						this.color=YELLOW_COLOR;
						this.showTime = RED_TIME+GREEN_TIME+YELLOW_TIME - this.time;
					}

					westTimer.text = this.showTime;
					westTimer.color = this.color;
					westTimer.update();
					eastTimer.text = this.showTime;
					eastTimer.color = this.color;
					eastTimer.update();
				}
				
				if (this.id == "wl" || this.id == "el" || this.id=="ns" || this.id=="nr" || this.id=="ss" || this.id=="sr"){
					if (this.time >=0 && this.time <GREEN_TIME){
						this.color=GREEN_COLOR;
						this.showTime = GREEN_TIME - this.time;
					}else if (this.time>=GREEN_TIME && this.time<GREEN_TIME+YELLOW_TIME){
						this.color=YELLOW_COLOR;
						this.showTime = GREEN_TIME+YELLOW_TIME - this.time;
					}else{
						this.color=RED_COLOR;
						this.showTime = GREEN_TIME+YELLOW_TIME+RED_TIME - this.time;
					}		
					northTimer.text = this.showTime;
					northTimer.color = this.color;
					northTimer.update();
					
					southTimer.text = this.showTime;
					southTimer.color = this.color;
					southTimer.update();					
				}
				this.draw();
			}
		}
		
		// 汽车类
		function Car(initPoint, runDirect, carWaitArr, direction, roadType, lampObj, timerObj){
			this.initPoint = initPoint; // 初始坐标
			this.runDirect = runDirect  // 汽车初始行驶方向
			this.x = initPoint.x;       // 实时坐标
			this.y = initPoint.y;
			this.carWaitArr = carWaitArr;   // 斑马线候车位置
			this.direction = direction;     // 道路方向
			this.roadType = roadType;       // 道路类型
			this.lampObj = lampObj;         // 当前道路红绿灯对象
			this.timerObj = timerObj;       // 红绿灯计时器

			this.dx = CAR_SPEED;  // 水平运行速度   单位:像素
			this.dy = CAR_SPEED;  // 垂直运行速度
			
			if (BOOL_RANDOM_SPEED){
				var speed = randomNumber(1,5);	
				this.dx = speed;
				this.dy = speed;
			}
			
			this.totalBlood = randomNumber(1200,5000);  // 总血量  单位:动画循环次数 即 1200/17 ms
			this.curBlood = this.totalBlood;            // 当前血量

			this.draw = function(){
				ctx.save();
				if (this.runDirect == D_RIGHT){
					ctx.drawImage(bufferCarCanvas,0,0,DIV_WIDTH,DIV_HEIGHT,this.x,this.y,DIV_WIDTH-1,DIV_HEIGHT-1);
				} else if (this.runDirect == D_DOWN){
					ctx.drawImage(bufferCarCanvas,DIV_WIDTH,0,DIV_WIDTH,DIV_HEIGHT,this.x,this.y,DIV_WIDTH-1,DIV_HEIGHT-1);
				} else if (this.runDirect == D_LEFT){
					ctx.drawImage(bufferCarCanvas,DIV_WIDTH*2,0,DIV_WIDTH,DIV_HEIGHT,this.x,this.y,DIV_WIDTH-1,DIV_HEIGHT-1);
				} else if (this.runDirect == D_UP){
					ctx.drawImage(bufferCarCanvas,DIV_WIDTH*3,0,DIV_WIDTH,DIV_HEIGHT,this.x,this.y,DIV_WIDTH-1,DIV_HEIGHT-1);
				}
				ctx.restore();
				
				ctx.beginPath();
				var bloodRatio = this.curBlood/this.totalBlood;
				ctx.strokeStyle=RED_COLOR;
				ctx.fillStyle=GREEN_COLOR;
				if (bloodRatio>=0.5 && bloodRatio<=0.75){
					ctx.fillStyle=YELLOW_COLOR;
				}else if (bloodRatio<0.5){
					ctx.fillStyle=RED_COLOR;
				}
				ctx.rect(this.x,this.y-11,30,5);
				ctx.stroke();
				ctx.fillRect(this.x,this.y-11,bloodRatio*30,5);
				ctx.closePath();
			}
			
			this.update = function(){
				this.draw(runDirect);
				this.curBlood--;

				// 如果当前车辆运行时间超过20秒  则清除之  避免各车辆都在等待对方让行
				if (this.curBlood<=0){
					//console.log("有车辆运行时间超过了20秒,坐标为:("+this.x+","+this.y+")");
					removeArr(CAR_ARR, this);
				}			
				
				// 判断是否撞墙了
				if (this.checkWallCollision()){
					removeArr(CAR_ARR, this);
					return;
				}
				
				// 判断当前位置是否在斑马线的候车位置,如果是的话 判断前方的红绿灯
				if (isPonitInArr(carWaitArr, new Point(this.x,this.y))){
					// 判断红绿灯
					if (this.lampObj.color == RED_COLOR || (this.lampObj.color == YELLOW_COLOR && parseInt(this.timerObj.text) <=2) ){
						return;
					}
				}

				// 运动方向判断
				if (direction == SOUTH_TO_NORTH){
					if (roadType == LEFT){
						if (this.y > (START_ROW+1)*DIV_HEIGHT && this.y <= (MAX_ROW-2)*DIV_HEIGHT){
							this.runDirect = D_UP;
						} else{
							this.runDirect = D_LEFT;
						}
					} else if (roadType == CENTER){
						this.runDirect = D_UP;	
					} else if (roadType == RIGHT){
						if (this.y > (END_ROW-1)*DIV_HEIGHT && this.y <= (MAX_ROW-2)*DIV_HEIGHT){
							this.runDirect = D_UP;
						} else{
							this.runDirect = D_RIGHT;
						}
					}						
				} else if (direction == NORTH_TO_SOUTH){
					if (roadType == LEFT){
						if (this.y >= DIV_HEIGHT && this.y < (START_ROW+2)*DIV_HEIGHT){
							this.runDirect = D_DOWN;
						} else{
							this.runDirect = D_RIGHT;
						}
					} else if (roadType == CENTER){
						this.runDirect = D_DOWN;
					} else if (roadType == RIGHT){
						if (this.y >= DIV_HEIGHT && this.y < (START_ROW-1)*DIV_HEIGHT){
							this.runDirect = D_DOWN;
						} else{
							this.runDirect = D_LEFT;	
						}
					}					
				} else if (direction == WEST_TO_EAST){
					if (roadType == LEFT){
						if (this.x >= DIV_WIDTH && this.x < (END_COL-3)*DIV_WIDTH){
							this.runDirect = D_RIGHT;
						} else{
							this.runDirect = D_UP;
						}
					} else if (roadType == CENTER){
						this.runDirect = D_RIGHT;
					} else if (roadType == RIGHT){
						if (this.x >= DIV_WIDTH && this.x < (START_COL-1)*DIV_WIDTH){
							this.runDirect = D_RIGHT;
						} else{
							this.runDirect = D_DOWN;
						}
					}					
				} else if (direction == EAST_TO_WEST){
					if (roadType == LEFT){
						if (this.x > (START_COL+1)*DIV_WIDTH && this.x <= (MAX_COL-2)*DIV_WIDTH){
							this.runDirect = D_LEFT;
						} else{
							this.runDirect = D_DOWN;
						}
					} else if (roadType == CENTER){
						this.runDirect = D_LEFT;
					} else if (roadType == RIGHT){
						if (this.x > (END_COL-1)*DIV_WIDTH && this.x <= (MAX_COL-2)*DIV_WIDTH){
							this.runDirect = D_LEFT;
						} else{
							this.runDirect = D_UP;
						}
					}	
				}

				if (this.checkCarCollision()){
					return;
				}
				
				if (this.checkManCollision(runDirect)){
					return;
				}
				
				if (this.runDirect == D_LEFT){
					this.left();
				} else if (this.runDirect == D_RIGHT){
					this.right();
				} else if (this.runDirect == D_UP){
					this.up();
				} else if (this.runDirect == D_DOWN){
					this.down();
				} 
			}

			// 检查当前车辆的下个位置是否有其他车辆  如果有的话  则当前车辆等待
			this.checkCarCollision = function(){
				var nextX=0,nextY=0;
				if (this.runDirect == D_LEFT){
					nextX = this.x-this.dx;
					nextY = this.y;
				} else if (this.runDirect == D_RIGHT){
					nextX = this.x+this.dx;
					nextY = this.y;
				} else if (this.runDirect == D_UP){
					nextX = this.x;
					nextY = this.y-this.dy;
				} else if (this.runDirect == D_DOWN){
					nextX = this.x;
					nextY = this.y+this.dy;
				} 

				for (var i=0;i<CAR_ARR.length;i++){
					if (this === CAR_ARR[i]){
						continue;
					}

					var xDistance = CAR_ARR[i].x - nextX;
					var yDistance = CAR_ARR[i].y - nextY;
					var towPointDistance = Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
					
					if (runDirect == D_LEFT){
						if (towPointDistance <= DIV_WIDTH && CAR_ARR[i].x < nextX){
							return true;
						}
					} else if (runDirect == D_RIGHT){
						if (towPointDistance <= DIV_WIDTH && CAR_ARR[i].x > nextX){
							return true;
						}
					} else if (runDirect == D_UP){
						if (towPointDistance <= DIV_WIDTH && CAR_ARR[i].y < nextY){
							return true;
						}
					} else if (runDirect == D_DOWN){
						if (towPointDistance <= DIV_WIDTH && CAR_ARR[i].y > nextY){
							return true;
						}
					}
				}
				
				return false;
			}
			
			this.checkManCollision = function(){
				var nextX=0,nextY=0;
				if (this.runDirect == D_LEFT){
					nextX = this.x-this.dx;
					nextY = this.y;
				} else if (this.runDirect == D_RIGHT){
					nextX = this.x+this.dx;
					nextY = this.y;
				} else if (this.runDirect == D_UP){
					nextX = this.x;
					nextY = this.y-this.dy;
				} else if (this.runDirect == D_DOWN){
					nextX = this.x;
					nextY = this.y+this.dy;
				} 

				for (var i=0;i<MAN_ARR.length;i++){
					if (this === MAN_ARR[i]){
						continue;
					}

					var xDistance = MAN_ARR[i].x - nextX;
					var yDistance = MAN_ARR[i].y - nextY;
					var towPointDistance = Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
					
					if (this.runDirect == D_LEFT){
						if (towPointDistance <= DIV_WIDTH && MAN_ARR[i].x < nextX){
							MAN_ARR[i].curBlood -= 4;
							this.curBlood-=2;
							return true;
						}
					} else if (this.runDirect == D_RIGHT){
						if (towPointDistance <= DIV_WIDTH && MAN_ARR[i].x > nextX){
							MAN_ARR[i].curBlood -= 4;
							this.curBlood-=2;
							return true;
						}
					} else if (this.runDirect == D_UP){
						if (towPointDistance <= DIV_WIDTH && MAN_ARR[i].y < nextY){
							MAN_ARR[i].curBlood -= 4;
							this.curBlood-=2;
							return true;
						}
					} else if (this.runDirect == D_DOWN){
						if (towPointDistance <= DIV_WIDTH && MAN_ARR[i].y > nextY){
							MAN_ARR[i].curBlood -= 4;
							this.curBlood-=2;
							return true;
						}
					}
				}
				
				return false;
			}
			
			// 检查当前车辆是否已经出界 若是 则移除之
			this.checkWallCollision = function(){
				if (this.x<DIV_WIDTH || this.x > (MAX_COL-2)*DIV_WIDTH
					|| this.y<DIV_HEIGHT || this.y > (MAX_ROW-2)*DIV_HEIGHT){
					return true;
				}

				return false;
			}
			
			// 向上移动
			this.up = function(){
				this.y -= this.dy;
			}
			
			// 向下移动
			this.down = function(){
				this.y += this.dy;
			}
			
			// 向左移动
			this.left = function(){
				this.x -= this.dx;
			}
			
			// 向右移动
			this.right = function(){
				this.x += this.dx;
			}
		}
	
		// 行人类
		function Man(initPoint, runDirect, manWaitArr, direction, lampObj, timerObj){
			this.initPoint = initPoint; // 初始坐标
			this.runDirect = runDirect;
			this.x = initPoint.x;       // 实时坐标
			this.y = initPoint.y;
			this.manWaitArr = manWaitArr;   // 斑马线候车位置
			this.direction = direction;     // 道路方向
			this.lampObj = lampObj;         // 当前道路红绿灯对象
			this.timerObj = timerObj;       // 红绿灯计时器

			var speed = randomNumber(1,5);	
			this.dx = speed;       // 水平运行速度   单位:像素
			this.dy = speed;       // 垂直运行速度
			this.picIndex = 0;         // 图片索引
			this.walkSpeed = 11-speed;
			
			this.totalBlood = randomNumber(1200,5000);  // 总血量  单位:动画循环次数 即 1200/17 ms
			this.curBlood = this.totalBlood;            // 当前血量

			this.draw = function(){
				ctx.save();
				this.picIndex = this.picIndex%4;
				if (this.runDirect == D_RIGHT){
					ctx.drawImage(manImage,40*this.picIndex,130,40,65,this.x-10,this.y-10,40,40);
				} else if (this.runDirect == D_DOWN){
					ctx.drawImage(manImage,40*this.picIndex,0,40,65,this.x-10,this.y-10,40,40);
				} else if (this.runDirect == D_LEFT){
					ctx.drawImage(manImage,40*this.picIndex,65,40,65,this.x-10,this.y-10,40,40);
				} else if (this.runDirect == D_UP){
					ctx.drawImage(manImage,40*this.picIndex,195,40,65,this.x-10,this.y-10,40,40);
				}
				ctx.restore();
				if (g_cycle%this.walkSpeed==0){
					this.picIndex++;
				}
				
				ctx.beginPath();
				var bloodRatio = this.curBlood/this.totalBlood;
				ctx.strokeStyle=RED_COLOR;
				ctx.fillStyle=GREEN_COLOR;
				if (bloodRatio>=0.5 && bloodRatio<=0.75){
					ctx.fillStyle=YELLOW_COLOR;
				}else if (bloodRatio<0.5){
					ctx.fillStyle=RED_COLOR;
				}
				ctx.rect(this.x-3,this.y-11,30,5);
				ctx.stroke();
				ctx.fillRect(this.x-3,this.y-11,bloodRatio*30,5);
				ctx.closePath();
			}
			
			this.update = function(){
				this.curBlood--;
				this.draw();

				// 如果当前行人运行时间超过20秒  则清除之
				if (this.curBlood <= 0){
					//console.log("有行人运行时间超过了20秒,坐标为:("+this.x+","+this.y+")");
					removeArr(MAN_ARR, this);
				}			
				
				// 判断是否撞墙了
				if (this.checkWallCollision()){
					removeArr(MAN_ARR, this);
					return;
				}
				
				// 判断行人是否在毒气圈中,如果是的话 让其持续掉血
				if (GAS_ARR.length >=1 ){
					if (isPointInCircle(GAS_ARR[0],new Point(this.x+DIV_WIDTH/2,this.y+DIV_HEIGHT/2))){
						this.curBlood -= 2;
					}
				}
				
				// 判断当前行人位置是否在斑马线的候车位置,如果是的话 判断前方的红绿灯
				if (isPonitInArr(manWaitArr, new Point(this.x,this.y))){
					// 判断红绿灯
					if (this.lampObj.color == RED_COLOR || (this.lampObj.color == YELLOW_COLOR && parseInt(this.timerObj.text) <=2) ){
						this.picIndex = 0;
						return;
					}
				}

				// 运动方向判断
				if (direction == SOUTH_TO_NORTH){
					this.initPoint = D_UP;							
				} else if (direction == NORTH_TO_SOUTH){
					this.initPoint = D_DOWN;					
				} else if (direction == WEST_TO_EAST){
					this.initPoint = D_RIGHT;					
				} else if (direction == EAST_TO_WEST){
					this.initPoint = D_LEFT;
				}

				if (this.checkManCollision()){
					this.picIndex = 0;
					return;
				}
				
				if (this.initPoint == D_LEFT){
					this.left();
				} else if (this.initPoint == D_RIGHT){
					this.right();
				} else if (this.initPoint == D_UP){
					this.up();
				} else if (this.initPoint == D_DOWN){
					this.down();
				} 
			}

			// 检查当前车辆的下个位置是否有其他车辆  如果有的话  则当前车辆等待
			this.checkManCollision = function(){
				var nextX=0,nextY=0;
				if (this.initPoint == D_LEFT){
					nextX = this.x-this.dx;
					nextY = this.y;
				} else if (this.initPoint == D_RIGHT){
					nextX = this.x+this.dx;
					nextY = this.y;
				} else if (this.initPoint == D_UP){
					nextX = this.x;
					nextY = this.y-this.dy;
				} else if (this.initPoint == D_DOWN){
					nextX = this.x;
					nextY = this.y+this.dy;
				} 

				for (var i=0;i<MAN_ARR.length;i++){
					if (this === MAN_ARR[i]){
						continue;
					}

					var xDistance = MAN_ARR[i].x - nextX;
					var yDistance = MAN_ARR[i].y - nextY;
					var towPointDistance = Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
					
					if (this.initPoint == D_LEFT){
						if (towPointDistance <= DIV_WIDTH && MAN_ARR[i].x < nextX){
							return true;
						}
					} else if (this.initPoint == D_RIGHT){
						if (towPointDistance <= DIV_WIDTH && MAN_ARR[i].x > nextX){
							return true;
						}
					} else if (this.initPoint == D_UP){
						if (towPointDistance <= DIV_WIDTH && MAN_ARR[i].y < nextY){
							return true;
						}
					} else if (this.initPoint == D_DOWN){
						if (towPointDistance <= DIV_WIDTH && MAN_ARR[i].y > nextY){
							return true;
						}
					}
				}

				return false;
			}
			
			// 检查当前车辆是否已经出界 若是 则移除之
			this.checkWallCollision = function(){
				if (this.x<DIV_WIDTH || this.x > (MAX_COL-2)*DIV_WIDTH
					|| this.y<DIV_HEIGHT || this.y > (MAX_ROW-2)*DIV_HEIGHT){
					return true;
				}

				return false;
			}
			
			// 向上移动
			this.up = function(){
				this.y -= this.dy;
			}
			
			// 向下移动
			this.down = function(){
				this.y += this.dy;
			}
			
			// 向左移动
			this.left = function(){
				this.x -= this.dx;
			}
			
			// 向右移动
			this.right = function(){
				this.x += this.dx;
			}
		}
	
		// 文本类,包括读秒器和东西南北方向
		function Text(x,y,text,color,fontSize){
			this.x = x;
			this.y = y;
			this.text = text;
			this.color = color;
			this.fontSize = fontSize;
			
			this.draw = function(){
				ctx.beginPath();
				ctx.strokeStyle=this.color;
				ctx.font = "normal normal "+fontSize+"px 宋体";
				ctx.strokeText(this.text,this.x,this.y);
				ctx.closePath();
			}
			
			this.update = function(){
				this.draw();
			}
		}
		
		function Point(x,y){
			this.x = x;
			this.y = y;
			// 起始结束行、起始结束列都从1开始
			this.convert = function(){
				var col = this.x/DIV_WIDTH+1;
				var row = this.y/DIV_HEIGHT+1;
				
				return (row-1)*MAX_COL+col;
			}
		}

		// 毒气圈
		var GAS_ARR = new Array();
		function createGasCircle(){
			var r = randomNumber(30,90);
			var x = randomNumber(r+DIV_WIDTH,canvas.width-r-DIV_WIDTH);
			var y = randomNumber(r+DIV_HEIGHT,canvas.height-r-DIV_HEIGHT);
			var circle = new Circle(ctx, "gas",x,y,r,"rgba(255,0,0,0.5)");
			GAS_ARR.push(circle);
		}

		// 动画刷新的次数
		var g_cycle=0;
		// 时钟:秒
		var g_second=0;
		// 汽车、行人数组
		var obj_arr = new Array(CAR_ARR, MAN_ARR);
		// 信号灯数组
		var lamp_arr = new Array(north_left_lamp,north_right_lamp,north_straight_lamp,
							 	 south_left_lamp,south_right_lamp,south_straight_lamp,
								 west_left_lamp,west_right_lamp,west_straight_lamp,
								 east_left_lamp,east_right_lamp,east_straight_lamp);
		// 信号灯计时器数组
		var timer_arr = new Array(northTimer,southTimer,westTimer,eastTimer);
		
		// 主函数
		function ainimate(){
			// 大小地图清屏
			ctx.clearRect(0,0,canvas.width,canvas.height);
			smallMapCtx.clearRect(0,0,smallMapCanvas.width,smallMapCanvas.height);
			
			// 主角 每秒刷新60次
			requestAnimationFrame(ainimate);
			
			// 绘制道路静态图形
			ctx.drawImage(bufferBaseCanvas,0,0,canvas.width,canvas.height);
			if (SMALL_MAP_MODE == 2){
				smallMapCtx.drawImage(bufferBaseCanvas,0,0,smallMapCanvas.width,smallMapCanvas.height);
			}

			// 绘制红绿灯以及计时器
			drawLight();

			// 绘制毒气圈
			for (var i=0;i<GAS_ARR.length;i++){
				GAS_ARR[i].draw();
			}
			
			// 绘制汽车、行人
			for (var i=0;i<obj_arr.length;i++){
				for (var j=0;j<obj_arr[i].length;j++){
					obj_arr[i][j].update();
				}
			}
			
			// 刷新右侧的血量、速度等信息
			drawTitleInfo(MOUSE_CANVAS_X,MOUSE_CANVAS_Y);
			
			// 时钟,便于红绿灯计时器的倒计时用
			g_cycle++;
			if (g_cycle %60 == 0){
				g_second++;
			}
			
			// 每隔20秒随机绘制生成一辆汽车
			if (g_cycle%(parseInt(CAR_GENERATE_INTERVAL/17)) == 0){
				createCar();
			}
			
			// 每隔20秒随机绘制生成一个行人
			if (g_cycle%(parseInt(MAN_GENERATE_INTERVAL/17)) == 0){
				createMan();
			}

			// 每隔10秒随机生成一个毒气圈
			if (g_cycle % 600 == 0){
				createGasCircle();
				if (GAS_ARR.length >= 2){
					GAS_ARR.splice(0,1);
				}
			}
			
			if (g_cycle >= Number.MAX_VALUE-20){
				g_cycle = 0;
			}

			// 绘制小地图
			showSmallMap(SMALL_MAP_MODE,SMALL_MAP_LEVEL,MOUSE_MOVE_DX,MOUSE_MOVE_DY);
		}
		
		ainimate();

		// 绘制小地图
		function showSmallMap(mode,level,dx,dy){
			if (level == 0){
				dx = 0;
				dy = 0;
			}
			var x = level*39-dx;
			var y = level*30-dy;
			var wh = canvas.width-2*x-dx;
			var ht = canvas.height-2*y-dy;
			
			if (mode == 1){		
				//smallMapCtx.drawImage(canvas,x,y,wh,ht,0,0,smallMapCanvas.width,smallMapCanvas.height);	
				smallMapCtx.drawImage(canvas,x,y,canvas.width,canvas.height,0,0,smallMapCanvas.width+level*39,smallMapCanvas.height+level*30);	
			}else{
				for (var i=0;i<obj_arr.length;i++){
					for (var j=0;j<obj_arr[i].length;j++){
						var objTemp = obj_arr[i][j];
						
						var tempX = smallMapCanvas.width*objTemp.x/canvas.width;
						var tempY = smallMapCanvas.height*objTemp.y/canvas.height;
						
						smallMapCtx.beginPath();
						smallMapCtx.arc(tempX+6,tempY+6,6,0,2*Math.PI,false);
						var alphaColor = parseInt(objTemp.curBlood*100/objTemp.totalBlood)/100;
						if (i==0){
							smallMapCtx.fillStyle="rgba(0,255,255," + alphaColor + ")";
						}else{
							smallMapCtx.fillStyle="rgba(0,0,255," + alphaColor + ")";
						}

						smallMapCtx.fill();
						smallMapCtx.closePath();
						
						smallMapCtx.beginPath();
						smallMapCtx.strokeStyle="white";
						if (objTemp.runDirect == D_RIGHT){
							smallMapCtx.moveTo(tempX,tempY+6);
							smallMapCtx.lineTo(tempX+12,tempY+6);
							
							smallMapCtx.moveTo(tempX+6,tempY);
							smallMapCtx.lineTo(tempX+12,tempY+6);
							
							smallMapCtx.moveTo(tempX+6,tempY+12);
							smallMapCtx.lineTo(tempX+12,tempY+6);
						} else if (objTemp.runDirect == D_LEFT){
							smallMapCtx.moveTo(tempX,tempY+6);
							smallMapCtx.lineTo(tempX+12,tempY+6);
							
							smallMapCtx.moveTo(tempX+6,tempY);
							smallMapCtx.lineTo(tempX,tempY+6);
							
							smallMapCtx.moveTo(tempX+6,tempY+12);
							smallMapCtx.lineTo(tempX,tempY+6);					
						} else if (objTemp.runDirect == D_UP){
							smallMapCtx.moveTo(tempX+6,tempY+12);
							smallMapCtx.lineTo(tempX+6,tempY);
							
							smallMapCtx.moveTo(tempX,tempY+6);
							smallMapCtx.lineTo(tempX+6,tempY);
							
							smallMapCtx.moveTo(tempX+12,tempY+6);
							smallMapCtx.lineTo(tempX+6,tempY);			
						}else if (objTemp.runDirect == D_DOWN){
							smallMapCtx.moveTo(tempX+6,tempY+12);
							smallMapCtx.lineTo(tempX+6,tempY);
							
							smallMapCtx.moveTo(tempX,tempY+6);
							smallMapCtx.lineTo(tempX+6,tempY+12);
							
							smallMapCtx.moveTo(tempX+12,tempY+6);
							smallMapCtx.lineTo(tempX+6,tempY+12);			
						}
						smallMapCtx.stroke();
						smallMapCtx.closePath();					
					}
				}			

				// 绘制小地图红绿灯
				for (var i=0;i<lamp_arr.length;i++){
					smallMapCtx.beginPath();
					smallMapCtx.fillStyle=lamp_arr[i].color;
					
					var tempX = smallMapCanvas.width*lamp_arr[i].x/canvas.width;
					var tempY = smallMapCanvas.height*lamp_arr[i].y/canvas.height;
					
					smallMapCtx.arc(tempX,tempY,5,0,2*Math.PI,false);
					smallMapCtx.fill();
					smallMapCtx.closePath();
				}
				
				// 绘制小地图信号灯计时器
				for (var i=0;i<timer_arr.length;i++){
					smallMapCtx.beginPath();
					smallMapCtx.strokeStyle=timer_arr[i].color;
					
					var tempX = smallMapCanvas.width*timer_arr[i].x/canvas.width;
					var tempY = smallMapCanvas.height*timer_arr[i].y/canvas.height;
					
					smallMapCtx.strokeText(timer_arr[i].text,tempX,tempY);
					smallMapCtx.closePath();
				}			
			}
		}
		
		// 每秒钟画面刷新次数,定时刷新车辆总数和行人总数	
		var last_g_cycle = g_cycle;
		setInterval(function(){
			var cur_g_cycle = g_cycle;
			FPS = cur_g_cycle-last_g_cycle;
			$("#fpsVal").text(FPS);
			$("#totalCarNum").text(CAR_ARR.length);
			$("#totalManNum").text(MAN_ARR.length);
			last_g_cycle = cur_g_cycle;
		},1000);
		
		// 缩小小地图
		$("#beSmallButton").click(function(){
			if (SMALL_MAP_LEVEL >0){
				SMALL_MAP_LEVEL--;
			}
		})
		
		// 放大小地图
		$("#beBigButton").click(function(){
			if (SMALL_MAP_LEVEL<7){
				SMALL_MAP_LEVEL++;
			}
		})
		
		// 汽车行驶速度控制
		$("#speedSwitch").click(function(){
			var carSwitch = $(this).attr("checked");
			if (carSwitch){
				BOOL_RANDOM_SPEED = true;
				$("#carSpeeddiv").slideUp();
			}else{
				BOOL_RANDOM_SPEED = false;
				$("#carSpeeddiv").slideDown();
			}
		})
		
		// 小地图模式控制
		$(":radio").click(function(){
			var val = $(this).val();
			if (val == "1"){
				SMALL_MAP_MODE = 1;
			}else{
				SMALL_MAP_MODE = 2;
			}
			SMALL_MAP_LEVEL = 0;
			MOUSE_MOVE_DX = 0;
			MOUSE_MOVE_DX = 0;
		});
		
		// 控制右侧几个div的显示和隐藏
		$("#mainDiv button[class=toggleBtn]").click(function(){
			var id = $(this).attr("id");
			var divId = id.replace("Btn","")+"Div";
			
			var tempSwitch = $(this).text();
			if (tempSwitch == "-"){
				tempSwitch = "+";
			}else{
				tempSwitch = "-";
			}
			$(this).text(tempSwitch);
			
			$("#"+divId).slideToggle(500);
		})

		// 显示/隐藏路面的网格线
		$("#gridSwitch").click(function(){
			var carSwitch = $(this).attr("checked");
			if (carSwitch){
				BOOL_SHOW_ROAD_GRID = true;
			}else{
				BOOL_SHOW_ROAD_GRID = false;
			}
			
			// 重新绘制大地图的基础结构
			bufferBaseCtx.clearRect(0,0,bufferBaseCanvas.width,bufferBaseCanvas.height);
			initRoadBackground(bufferBaseCtx);
			initPanel(bufferBaseCtx);
			initRoadSperateLine(bufferBaseCtx);

		})

		// 显示/隐藏右侧整个div面板
		$("#ctlShow").click(function(){
			var text = $(this).text();
			if (text == "全部收起"){
				text = "全部展开";
				$("#infoDiv").slideUp(500);
			} else{
				var divs = $("#mainDiv div[id*=Div]");
				for (var i=0;i<divs.length;i++){
					$(divs[i]).slideDown(500);
				}
				text = "全部收起";
			}
			$(this).text(text);
		})
		
		// 从一个点数组中移除一个点
		function removeArr(arr,point){
			for (var i=0;i<arr.length;i++){
				if (arr[i] === point){
					arr.splice(i,1);
					break;
				}
			}
		}
		
		function addTwoRangePos(arr1,arr2){
			var resultArr = new Array();
			for (var x in arr1){
				resultArr.push(arr1[x]);
			}
			for (var y in arr2){
				resultArr.push(arr2[y]);
			}
			
			return resultArr;
		}
		
		// 根据传入的起始结束行,起始结束列,计算出该范围内的点数组
		function getRangePos(startRow,endRow,startCol,endCol){
			var pArr = new Array();
			for (var r=startRow;r<=endRow;r++){
				for (var c=startCol;c<=endCol;c++){
					var p = convertGridToPoint(r,c);
					pArr.push(p);
				}
			}
			
			return pArr;
		}
		
		// 根据行列转换成对应的坐标
		function convertGridToPoint(row,col){
			var p = new Point();
			
			var x = (col-1)*DIV_HEIGHT;
			var y = (row-1)*DIV_WIDTH;
			p.x = x;
			p.y = y;
			
			return p;
		}
		
		// 计算两点之间距离
		function getTwoPointDistance(x1,y1,x2,y2){
			var xDistance = x1 - x2;
			var yDistance = y1 - y2;
			var towPointDistance = Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
			
			return towPointDistance
		}
		
		// 判断一个点是否在一个点数组中
		function isPonitInArr(arr,point){
			var res = false;
			for (var i=0;i<arr.length;i++){
				if (arr[i].x == point.x && arr[i].y == point.y){
					res = true;
					break;
				}
			}
			
			return res;
		}
		
		// 判断一个点是否在一个圆中
		function isPointInCircle(circle, point){
			var towPointDistance = getTwoPointDistance(circle.x, circle.y, point.x, point.y);
			if (towPointDistance <= circle.r){
				return true;
			}
			
			return false;
		}

		// 随机生成一个在指定范围内的数字
		function randomNumber(start,end){
			var range = end-start;
			var rand = Math.random();
			var num = start + Math.round(rand*range);
			
			return num;
		}
		
		// 随机生成颜色
		function randomRgb(){
			var red = randomNumber(0,255);
			var green = randomNumber(0,255);
			var blue = randomNumber(0,255);
			
			return "rgb(" + red + "," + green + "," + blue + ")";
		}
		
		function windowToCanvas(event){
			var box = canvas.getBoundingClientRect();

			var tempX = parseInt(event.clientX-box.left);
			var tempY = parseInt(event.clientY-box.top);
			return {x:tempX,
					y:tempY};
		}
		
		function changeMaxCarNum(){
			var curNum =  document.getElementById("maxCarNumInput").value;
			MAX_CAR_NUM = parseInt(curNum);
			document.getElementById("maxCarNumShow").innerHTML=MAX_CAR_NUM;			
		}
		
		function changeMaxManNum(){
			var curNum =  document.getElementById("maxManNumInput").value;
			MAX_MAN_NUM = parseInt(curNum);
			document.getElementById("maxManNumShow").innerHTML=MAX_MAN_NUM;			
		}
		
		function changeCarRefreashFrequency(){
			var curNum =  document.getElementById("carRefreashFrequency").value;
			CAR_GENERATE_INTERVAL = parseInt(curNum);
			document.getElementById("carFrequencyShow").innerHTML=CAR_GENERATE_INTERVAL;			
		}
		
		function changeManRefreashFrequency(){
			var curNum =  document.getElementById("manRefreashFrequency").value;
			MAN_GENERATE_INTERVAL = parseInt(curNum);
			document.getElementById("manFrequencyShow").innerHTML=MAN_GENERATE_INTERVAL;			
		}
		
		function changeCarSpeed(){
			var curNum =  document.getElementById("carSpeed").value;
			CAR_SPEED = parseInt(curNum);
			document.getElementById("carSpeedShow").innerHTML=CAR_SPEED;		
		}

		function changeManSpeed(){
			var curNum =  document.getElementById("manSpeed").value;
			MAN_SPEED = parseInt(curNum);
			document.getElementById("manSpeedShow").innerHTML=MAN_SPEED;		
		}
		
		function changeBgColor(){
			var color = $("#bgColor").val();
			$("#canvas").css("background-color",color);
			//$("#smallMapCanvas").css("background-color",color);
		}
	</script>
</html>
