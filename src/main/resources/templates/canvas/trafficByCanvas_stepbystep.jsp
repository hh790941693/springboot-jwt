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
		</style>
		
	</head> 
	<body>
		<div id="mainDiv" style="width:1250px;height:600px;margin:1px auto">
			<canvas id="canvas" style="width:780px;height:600px;background:#fff;float:left;">
				你的浏览器不支持canvas标签
			</canvas>
			<div id="infoDiv" style="auto;height:100px;float:left;font-size:18px;">
				<span>车辆总数:</span><span id="totalCarNum" style="color:red">0</span>
				<br/>
				<span>行人总数:</span><span id="totalManNum" style="color:red">0</span>
				<br/>
				
				汽车行驶速度:<input id="carSpeed" type="range" value=10  min=5 max=70 step=3 onchange="changeCarSpeed()">
				<span id="carSpeedShow">10</span>
				<br/>
				
				车辆刷新频率:<input id="carRefreashFrequency" type="range" value=300  min=18 max=2000 step=20 onchange="changeCarRefreashFrequency()">
				<span id="carFrequencyShow">300</span>
				<br/>
				
				行人刷新频率:<input id="manRefreashFrequency" type="range" value=300  min=18 max=2000 step=20 onchange="changeManRefreashFrequency()">
				<span id="manFrequencyShow">300</span>
				<br/>
				
				最大车辆数:<input id="maxCarNumInput" type="range" value=10  min=10 max=50 step=3 onchange="changeMaxCarNum()">
				<span id="maxCarNumShow">10</span>
				<br/>
				
				最大行人数:<input id="maxManNumInput" type="range" value=10  min=10 max=50 step=3 onchange="changeMaxManNum()">
				<span id="maxManNumShow">10</span>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var canvas = document.getElementById("canvas");
		canvas.width  = 780;
		canvas.height = 600;
		var ctx = canvas.getContext("2d");
		
		// 每一个网格的宽高 单位:像素
		var DIV_WIDTH  = 30;
		var DIV_HEIGHT = 30;
		
		// 红灯时间
		var RED_TIME    = 10;
		var YELLOW_TIME = 3;
		var GREEN_TIME  = RED_TIME-YELLOW_TIME;
		
		// 行、列
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
		var MAX_CAR_NUM = 10;
		var MAX_MAN_NUM = 10;
		
		// 汽车、行人行驶速度
		var CAR_SPEED = 10;  // 即10*17=170ms
		var MAN_SPEED = 30;
		
		// 道路方向:左转、直行、右转
		var LEFT=1, CENTER=2, RIGHT=3;
		// 道路方向:北-南、南-北、西-东、东-西
		var NORTH_TO_SOUTH=1,SOUTH_TO_NORTH=2,WEST_TO_EAST=3,EAST_TO_WEST=4;
		
		// 汽车等候区坐标
		var CAR_WAIT_ZONE = {
			NORTH_TO_SOUTH: getRangePos(START_ROW-3,START_ROW-3,START_COL,START_COL+2),
			SOUTH_TO_NORTH: getRangePos(END_ROW+3,END_ROW+3,END_COL-2,END_COL),
			WEST_TO_EAST:   getRangePos(END_ROW-2,END_ROW,START_COL-3,START_COL-3),
			EAST_TO_WEST:   getRangePos(START_ROW,START_ROW+2,END_COL+3,END_COL+3)
		}
		
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
		// ------------------------------------------信号灯读秒器---------------------------------------------
		var westTimer = new Text(0,(START_ROW-1)*DIV_HEIGHT-5,0,"red");
		var eastTimer = new Text(canvas.width-DIV_WIDTH,(END_ROW+1)*DIV_HEIGHT-5,0,"red");
		var northTimer = new Text((END_COL)*DIV_WIDTH,DIV_HEIGHT-5,0,"green");
		var southTimer = new Text((START_COL-2)*DIV_WIDTH,canvas.height-5,0,"green");
		
		// -------------------------------------------东南西北风向---------------------------------------------
		var westDirectionText = new Text(0,(START_ROW+3)*DIV_HEIGHT-5,"西","blue");
		var eastDirectionText = new Text(canvas.width-DIV_WIDTH,(END_ROW-3)*DIV_HEIGHT-5,"东","blue");
		var northDirectionText = new Text((END_COL-4)*DIV_WIDTH,DIV_HEIGHT-5,"北","blue");
		var southDirectionText = new Text((START_COL+2)*DIV_WIDTH,canvas.height-5,"南","blue");
		
		
		//-------------------------------------------------红绿灯---------------------------------------------------
		// 北边
		var north_left_lamp =     new Circle("nl", (END_COL-3)*DIV_WIDTH+DIV_HEIGHT/2, DIV_HEIGHT/2,DIV_HEIGHT/2-2, "red");
		var north_straight_lamp = new Circle("ns", (END_COL-2)*DIV_WIDTH+DIV_HEIGHT/2, DIV_HEIGHT/2,DIV_HEIGHT/2-2, "green");
		var north_right_lamp =    new Circle("nr", (END_COL-1)*DIV_WIDTH+DIV_HEIGHT/2, DIV_HEIGHT/2,DIV_HEIGHT/2-2, "green");
		
		// 南边
		var south_right_lamp =    new Circle("sr", (START_COL-1)*DIV_WIDTH+DIV_HEIGHT/2, canvas.height-DIV_HEIGHT/2,DIV_HEIGHT/2-2, "green");
		var south_straight_lamp = new Circle("ss", START_COL*DIV_WIDTH+DIV_HEIGHT/2,     canvas.height-DIV_HEIGHT/2,DIV_HEIGHT/2-2, "green");
		var south_left_lamp =     new Circle("sl", (START_COL+1)*DIV_WIDTH+DIV_HEIGHT/2, canvas.height-DIV_HEIGHT/2,DIV_HEIGHT/2-2, "red");
		
		// 西边
		var west_right_lamp =     new Circle("wr" ,DIV_WIDTH/2, (START_ROW-1)*DIV_HEIGHT+DIV_HEIGHT/2,     DIV_HEIGHT/2-2,"red");
		var west_straight_lamp =  new Circle("ws" ,DIV_WIDTH/2, (START_ROW)*DIV_HEIGHT+DIV_HEIGHT/2, DIV_HEIGHT/2-2,"red");
		var west_left_lamp =      new Circle("wl" ,DIV_WIDTH/2, (START_ROW+1)*DIV_HEIGHT+DIV_HEIGHT/2, DIV_HEIGHT/2-2,"green");
		
		// 东边
		var east_left_lamp =      new Circle("el" ,canvas.width-DIV_WIDTH/2, (END_ROW-3)*DIV_HEIGHT+DIV_HEIGHT/2, DIV_HEIGHT/2-2,"green");
		var east_straight_lamp =  new Circle("es" ,canvas.width-DIV_WIDTH/2, (END_ROW-2)*DIV_HEIGHT+DIV_HEIGHT/2, DIV_HEIGHT/2-2,"red");
		var east_right_lamp =     new Circle("er" ,canvas.width-DIV_WIDTH/2, (END_ROW-1)*DIV_HEIGHT+DIV_HEIGHT/2,     DIV_HEIGHT/2-2,"red");

		// 汽车数组
		var CAR_ARR = new Array();
		// 行人数组
		var MAN_ARR = new Array();
		
		// 汽车资源图片
		var carImage = new Image();
		carImage.src = "<%=path%>/img/car.jpg";

		// 初始化面板网格
		function initPanel(){
			ctx.beginPath();
			for (var y=0;y<=canvas.height;y+=DIV_WIDTH){
				ctx.moveTo(0,y);
				ctx.lineTo(canvas.width,y);
				ctx.strokeStyle="grey";
				ctx.stroke();
			}
			
			for (var x=0;x<=canvas.width;x+=DIV_HEIGHT){
				ctx.moveTo(x,0);
				ctx.lineTo(x,canvas.width);
				ctx.strokeStyle="grey";
				ctx.stroke();
			}
			ctx.closePath();
		}
		
		// 初始化道路路基颜色以及行人等候区道路颜色
		function initRoadBackground(){
			// 道路路基颜色
			ctx.beginPath();
			ctx.fillStyle="#666665";
			// 西东
			ctx.fillRect(DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT,canvas.width-2*DIV_WIDTH,6*DIV_HEIGHT);
			// 南北
			ctx.fillRect((START_COL-1)*DIV_WIDTH,DIV_HEIGHT,6*DIV_WIDTH,canvas.height-2*DIV_HEIGHT);
			ctx.closePath();
			
			// 行人等候区
			ctx.beginPath();
			ctx.fillStyle="yellow";
			// 南
			ctx.fillRect(END_COL*DIV_WIDTH,(END_ROW+2)*DIV_HEIGHT,2*DIV_WIDTH,(MAX_ROW-END_ROW-3)*DIV_HEIGHT);
			// 北
			ctx.fillRect((START_COL-3)*DIV_WIDTH,DIV_HEIGHT,2*DIV_WIDTH,(START_ROW-4)*DIV_HEIGHT);
			// 西
			ctx.fillRect(DIV_WIDTH,(END_ROW)*DIV_HEIGHT,(START_COL-4)*DIV_WIDTH,2*DIV_HEIGHT);
			// 东
			ctx.fillRect((END_COL+2)*DIV_WIDTH,(START_ROW-3)*DIV_HEIGHT,(MAX_COL-END_COL-3)*DIV_WIDTH,2*DIV_HEIGHT);
			ctx.closePath();
		}
		
		// 初始化道路路线以及斑马线等
		function initRoadSperateLine(){
		
			ctx.beginPath();
			// 水平
			ctx.moveTo(0, (START_ROW+2)*DIV_HEIGHT);
			ctx.lineTo(canvas.width, (START_ROW+2)*DIV_HEIGHT);
			ctx.strokeStyle="white";
			
			// 垂直
			ctx.moveTo((START_COL+2)*DIV_WIDTH,0);
			ctx.lineTo((START_COL+2)*DIV_WIDTH,canvas.height);
			
			// 中间
			ctx.rect((START_COL-1)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT,6*DIV_WIDTH,6*DIV_HEIGHT);
			
			// 斑马线
			for (var y=(START_ROW-1)*DIV_HEIGHT;y<=(START_ROW-1)*DIV_HEIGHT+6*DIV_HEIGHT;y+=DIV_HEIGHT){
				//西边
				ctx.moveTo((START_COL-3)*DIV_WIDTH,y);
				ctx.lineTo((START_COL-1)*DIV_WIDTH,y);
				
				// 东边
				ctx.moveTo(END_COL*DIV_WIDTH,y);
				ctx.lineTo((END_COL+2)*DIV_WIDTH,y);
			}
			
			for (var x=(START_COL-1)*DIV_WIDTH;x<=(START_COL-1)*DIV_WIDTH+6*DIV_WIDTH;x+=DIV_WIDTH){
				// 北边
				ctx.moveTo(x,(START_ROW-3)*DIV_HEIGHT);
				ctx.lineTo(x,(START_ROW-1)*DIV_HEIGHT);	

				// 南边
				ctx.moveTo(x,(END_ROW)*DIV_HEIGHT);
				ctx.lineTo(x,(END_ROW+2)*DIV_HEIGHT);					
			}
			
			// 车辆候车线
			// 西
			ctx.moveTo((START_COL-3)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT);
			ctx.lineTo((START_COL-3)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT+6*DIV_HEIGHT);
			
			// 东
			ctx.moveTo((END_COL+2)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT);
			ctx.lineTo((END_COL+2)*DIV_WIDTH,(START_ROW-1)*DIV_HEIGHT+6*DIV_HEIGHT);
			
			// 北
			ctx.moveTo((START_COL-1)*DIV_WIDTH,(START_ROW-3)*DIV_HEIGHT);
			ctx.lineTo((START_COL-1)*DIV_WIDTH+6*DIV_WIDTH,(START_ROW-3)*DIV_HEIGHT);			

			// 南
			ctx.moveTo((START_COL-1)*DIV_WIDTH,(END_ROW+2)*DIV_HEIGHT);
			ctx.lineTo((START_COL-1)*DIV_WIDTH+6*DIV_WIDTH,(END_ROW+2)*DIV_HEIGHT);
			
			ctx.stroke();
			ctx.closePath();
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
			var runPointArr;
			// 汽车斑马线候车位置
			var carWaitArr;
			// 红绿灯对象
			var lampObj;
			if (direction == SOUTH_TO_NORTH){
				carWaitArr = CAR_WAIT_ZONE.SOUTH_TO_NORTH;
				if (roadType == LEFT){					
					initPoint = CAR_INIT_GPS.SOUTH_TO_NORTH.LEFT;
					runPointArr = CAR_RUN_GPS.SOUTH_TO_NORTH.LEFT;
					lampObj = south_left_lamp;
				} else if (roadType == CENTER){
					initPoint = CAR_INIT_GPS.SOUTH_TO_NORTH.CENTER;
					runPointArr = CAR_RUN_GPS.SOUTH_TO_NORTH.CENTER;
					lampObj = south_straight_lamp;
				} else if (roadType == RIGHT){
					initPoint = CAR_INIT_GPS.SOUTH_TO_NORTH.RIGHT;
					runPointArr = CAR_RUN_GPS.SOUTH_TO_NORTH.RIGHT;
					lampObj = south_right_lamp;
				}
			} else if (direction == NORTH_TO_SOUTH){
				carWaitArr = CAR_WAIT_ZONE.NORTH_TO_SOUTH;
				if (roadType == LEFT){
					initPoint = CAR_INIT_GPS.NORTH_TO_SOUTH.LEFT;	
					runPointArr = CAR_RUN_GPS.NORTH_TO_SOUTH.LEFT;
					lampObj = north_left_lamp;
				} else if (roadType == CENTER){
					initPoint = CAR_INIT_GPS.NORTH_TO_SOUTH.CENTER;	
					runPointArr = CAR_RUN_GPS.NORTH_TO_SOUTH.CENTER;	
					lampObj = south_straight_lamp;					
				} else if (roadType == RIGHT){
					initPoint = CAR_INIT_GPS.NORTH_TO_SOUTH.RIGHT;
					runPointArr = CAR_RUN_GPS.NORTH_TO_SOUTH.RIGHT;	
                    lampObj = south_right_lamp;					
				}					
			} else if (direction == WEST_TO_EAST){
				carWaitArr = CAR_WAIT_ZONE.WEST_TO_EAST;
				if (roadType == LEFT){
					initPoint = CAR_INIT_GPS.WEST_TO_EAST.LEFT;	
					runPointArr = CAR_RUN_GPS.WEST_TO_EAST.LEFT;
                    lampObj = west_left_lamp; 					
				} else if (roadType == CENTER){
					initPoint = CAR_INIT_GPS.WEST_TO_EAST.CENTER;
					runPointArr = CAR_RUN_GPS.WEST_TO_EAST.CENTER;
                    lampObj = west_straight_lamp;					
				} else if (roadType == RIGHT){
					initPoint = CAR_INIT_GPS.WEST_TO_EAST.RIGHT;
					runPointArr = CAR_RUN_GPS.WEST_TO_EAST.RIGHT;	
                    lampObj = west_right_lamp;					
				}					
			} else if (direction == EAST_TO_WEST){
				carWaitArr = CAR_WAIT_ZONE.EAST_TO_WEST;
				if (roadType == LEFT){
					initPoint = CAR_INIT_GPS.EAST_TO_WEST.LEFT;	
					runPointArr = CAR_RUN_GPS.EAST_TO_WEST.LEFT;		
                    lampObj = east_left_lamp;					
				} else if (roadType == CENTER){
					initPoint = CAR_INIT_GPS.EAST_TO_WEST.CENTER;
					runPointArr = CAR_RUN_GPS.EAST_TO_WEST.CENTER;
                    lampObj = east_straight_lamp;					
				} else if (roadType == RIGHT){
					initPoint = CAR_INIT_GPS.EAST_TO_WEST.RIGHT;
					runPointArr = CAR_RUN_GPS.EAST_TO_WEST.RIGHT;
                    lampObj = east_right_lamp;					
				}	
			}
			var car = new Car(initPoint, runPointArr, carWaitArr, direction, roadType, lampObj);
			
			CAR_ARR.push(car);
		}
		
		// 信号灯类
		function Circle(id,x,y,r,color){
			this.id = id;
			this.x = x;
			this.y = y;
			this.r = r;
			this.color = color;
			this.time = 0;
			this.showTime = 0;
			
			this.draw = function(){
				ctx.beginPath();
				ctx.arc(x,y,r,0,2*Math.PI,false);
				ctx.fillStyle=this.color;
				ctx.fill();
				ctx.closePath();
			}
			
			this.update = function(){
				this.time = g_second%(RED_TIME*2)
				if (this.id=="wr" || this.id=="ws"|| this.id=="er" || this.id=="es" || this.id=="nl" || this.id=="sl"){
					if (this.time >=0 && this.time<RED_TIME){
						this.color="red";
						this.showTime = RED_TIME - this.time;
					}else if (this.time>=RED_TIME && this.time<RED_TIME+GREEN_TIME){
						this.color="green";
						this.showTime = RED_TIME+GREEN_TIME - this.time;
					}else{
						this.color="yellow";
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
						this.color="green";
						this.showTime = GREEN_TIME - this.time;
					}else if (this.time>=GREEN_TIME && this.time<GREEN_TIME+YELLOW_TIME){
						this.color="yellow";
						this.showTime = GREEN_TIME+YELLOW_TIME - this.time;
					}else{
						this.color="red";
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
		function Car(initPoint, runPointArr, carWaitArr, direction, roadType, lampObj){
			this.initPoint = initPoint;
			this.x = initPoint.x;
			this.y = initPoint.y;
			this.runPointArr = runPointArr;
			this.carWaitArr = carWaitArr;
			this.direction = direction;
			this.roadType = roadType;
			this.lampObj = lampObj;

			this.runTime = 0;
			
			this.nextStep = new Point();
			this.draw = function(){
				ctx.save();
				//ctx.translate(this.x,this.y);
				//if (direction == SOUTH_TO_NORTH){
				//	ctx.rotate(3*Math.PI/2);
				//} else if (direction == NORTH_TO_SOUTH){
				//	ctx.rotate(Math.PI/2);
				//} else if (direction == WEST_TO_EAST){
				//	
				//} else if (direction == EAST_TO_WEST){
				//	ctx.rotate(Math.PI);
				//}

				ctx.drawImage(carImage,this.x,this.y,DIV_WIDTH,DIV_HEIGHT);
				//ctx.drawImage(carImage,0,0,DIV_WIDTH,DIV_HEIGHT);
				ctx.restore();
			}
			
			this.update = function(){
				// 如果当前车辆运行时间超过20秒  则清除之  避免各车辆都在等待对方让行
				if (this.runTime >= 1000){
					console.log("有车辆运行时间超过了20秒"+this);
					removeArr(CAR_ARR, this);
				}
			
				if (g_cycle % CAR_SPEED == 0){
				
					// 判断是否撞墙了
					if (this.checkWallCollision()){
						removeArr(CAR_ARR, this);
						return;
					}
					
					// 判断当前位置是否在斑马线的候车位置,如果是的话 判断前方的红绿灯
					if (isPonitInArr(carWaitArr, new Point(this.x,this.y))){
						// 判断红绿灯
						if (this.lampObj.color == "red"){
							return;
						}
					}
					
					this.nextStep = this.getNextStep();
					if (this.nextStep != undefined){
						
						// 判断下一步是否有其他车辆,如果有,则当前车辆暂停
						if (this.checkCarCollision(this.nextStep)){
							return;
						}
						this.x = this.nextStep.x;
						this.y = this.nextStep.y;
					}
				}
				this.draw();
				this.runTime++;
			}

			// 检查当前车辆的下个位置是否有其他车辆  如果有的话  则当前车辆等待
			this.checkCarCollision = function(point){
				for (var i=0;i<CAR_ARR.length;i++){
					if (this === CAR_ARR[i]){
						continue;
					}
					if (CAR_ARR[i].x == point.x && CAR_ARR[i].y == point.y){
						return true;
					}
				}
				
				return false;
			}
			
			// 检查当前车辆是否已经出界 若是 则移除之
			this.checkWallCollision = function(){
				var res = false;
				
				if (this.x<0 || this.x > canvas.width
					|| this.y<0 || this.y > canvas.height){
					return true;
				}
				
				if ((direction == EAST_TO_WEST && roadType == CENTER)
				    || (direction == SOUTH_TO_NORTH && roadType == LEFT)
					|| (direction == NORTH_TO_SOUTH && roadType == RIGHT)){
					if (this.x <= DIV_WIDTH){
						return true;
					}
				}
				
				if ((direction == WEST_TO_EAST && roadType == CENTER)
				    || (direction == SOUTH_TO_NORTH && roadType == RIGHT)
					|| (direction == NORTH_TO_SOUTH && roadType == LEFT)){
					if (this.x >= (MAX_COL-2)*DIV_WIDTH){
						return true;
					}
				}
				
				if ((direction == SOUTH_TO_NORTH && roadType == CENTER)
				    || (direction == WEST_TO_EAST && roadType == LEFT)
					|| (direction == EAST_TO_WEST && roadType == RIGHT)){
					if (this.y <= DIV_HEIGHT){
						return true;
					}
				}
				
				if ((direction == NORTH_TO_SOUTH && roadType == CENTER)
				    || (direction == WEST_TO_EAST && roadType == RIGHT)
					|| (direction == EAST_TO_WEST && roadType == LEFT)){
					if (this.y >= (MAX_ROW-2)*DIV_HEIGHT){
						return true;
					}
				}

				return res;
			}
			
			// 获取车辆的下一个位置
			this.getNextStep = function(){
				for (var i in runPointArr){
					var p = runPointArr[i];
					var xDistance = this.x - p.x;
					var yDistance = this.y - p.y;
					var towPointDistance = Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
					
					if (direction == SOUTH_TO_NORTH){
						if (roadType == LEFT){					
							if (towPointDistance == DIV_WIDTH && p.x<=this.x && p.y<= this.y){
								return p;
							}
						} else if (roadType == CENTER){
							if (towPointDistance == DIV_WIDTH && p.y<=this.y){
								return p;
							}						
						} else if (roadType == RIGHT){
							if (towPointDistance == DIV_WIDTH && p.x>=this.x && p.y<= this.y){
								return p;
							}
						}
					} else if (direction == NORTH_TO_SOUTH){
						if (roadType == LEFT){
							if (towPointDistance == DIV_WIDTH && p.x>=this.x && p.y>= this.y){
								return p;
							}						
						} else if (roadType == CENTER){
							if (towPointDistance == DIV_WIDTH && p.y>=this.y){
								return p;
							}						
						} else if (roadType == RIGHT){
							if (towPointDistance == DIV_WIDTH && p.x<=this.x && p.y>= this.y){
								return p;
							}						
						}					
					} else if (direction == WEST_TO_EAST){
						if (roadType == LEFT){
							if (towPointDistance == DIV_WIDTH && p.x>=this.x && p.y<= this.y){
								return p;
							}						
						} else if (roadType == CENTER){
							if (towPointDistance == DIV_WIDTH && p.x>=this.x){
								return p;
							}						
						} else if (roadType == RIGHT){
							if (towPointDistance == DIV_WIDTH && p.x>=this.x && p.y >= this.y){
								return p;
							}						
						}					
					} else if (direction == EAST_TO_WEST){
						if (roadType == LEFT){
							if (towPointDistance == DIV_WIDTH && p.x<=this.x && p.y>= this.y){
								return p;
							}						
						} else if (roadType == CENTER){
							if (towPointDistance == DIV_WIDTH && p.x<=this.x){
								return p;
							}							
						} else if (roadType == RIGHT){
							if (towPointDistance == DIV_WIDTH && p.x<=this.x && p.y<= this.y){
								return p;
							}						
						}	
					}
				}
			}
			
			this.up = function(){
				this.y -= 1;
			}
			
			this.down = function(){
				this.y += 1;
			}
			
			this.left = function(){
				this.x -= 1;
			}
			
			this.right = function(){
				this.x += 1;
			}
		}
		
		// 文本类,包括读秒器和东西南北方向
		function Text(x,y,text,color){
			this.x = x;
			this.y = y;
			this.text = text;
			this.color = color;
			
			this.draw = function(){
				ctx.beginPath();
				ctx.strokeStyle=this.color;
				ctx.font = "30px 黑体";
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
		
		function TestCar(x,y){
			this.x = x;
			this.y = y;
			this.draw = function(){
				ctx.save();
				ctx.drawImage(carImage,this.x,this.y,DIV_WIDTH,DIV_HEIGHT);
				ctx.restore();
			}
			this.update = function(){
				this.x +=3;
				this.draw();
			}
		}
		
		function test(){
			for (var x in CAR_RUN_GPS.EAST_TO_WEST.LEFT){
				ctx.beginPath()
				ctx.fillRect(CAR_RUN_GPS.EAST_TO_WEST.LEFT[x].x,CAR_RUN_GPS.EAST_TO_WEST.LEFT[x].y,DIV_WIDTH,DIV_HEIGHT);
				ctx.closePath();
			}
		}
		
		// 动画刷新的次数
		var g_cycle=0;
		// 时钟:秒
		var g_second=0;
		var testCar = new TestCar(20,240);
		function ainimate(){
			ctx.clearRect(0,0,canvas.width,canvas.height);
			requestAnimationFrame(ainimate);
			
			initRoadBackground();
			initPanel();
			initRoadSperateLine();

			drawLight();
			//test();
			//testCar.update();
			
			for (var cIndex=0;cIndex<CAR_ARR.length;cIndex++){
				CAR_ARR[cIndex].update();
			}
			
			g_cycle++;
			if (g_cycle %60 == 0){
				g_second++;
			}
			
			if (g_cycle%(parseInt(CAR_GENERATE_INTERVAL/17)) == 0){
				createCar();
			}
		}
		
		ainimate();

		setInterval(function(){
			$("#totalCarNum").text(CAR_ARR.length);
			$("#totalManNum").text(MAN_ARR.length);
		},1000);
		
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

		function randomNumber(start,end){
			var range = end-start;
			var rand = Math.random();
			var num = start + Math.round(rand*range);
			
			return num;
		}
		
		function randomRgb(){
			var red = randomNumber(0,255);
			var green = randomNumber(0,255);
			var blue = randomNumber(0,255);
			
			return "rgb(" + red + "," + green + "," + blue + ")";
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
	</script>
</html>
