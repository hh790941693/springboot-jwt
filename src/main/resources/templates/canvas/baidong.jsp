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
			#controlDiv{
				position:fixed;
				top:5px;
				right:5px;
				font-size:18px;
			}
		</style>
	</head> 
	<body>
		<canvas id="canvas" style="background:#000">
			你的浏览器不支持canvas标签
		</canvas>
		<div id="controlDiv" style="display:none">
			星球旋转速度:<input id="planetSpeed" type="range" value=0.3  min=0.1 max=10 step=0.1 onchange="changePlanetSpeed()">
			<span id="speedVar">1</span>
			<br/>
			星星闪烁频率:<input id="starFrequency" type="range" value=0.01  min=0.01 max=1 step=0.05 onchange="changeStarFrequency()">
			<span id="freVar">0.05</span>
		</div>
	</body>
	<script type="text/javascript">	
		var canvas = document.getElementById("canvas");
		//canvas.width = 1200;
		//canvas.height = 900;
		canvas.width = 840;
		canvas.height = 580;
		var ctx = canvas.getContext("2d");
		
		var CENTER_X = canvas.width/2;
		var CENTER_Y = canvas.height/2;
		var SETP_DIS = 50;
		
		var gravity = 0.95;
		
		// 画椭圆
		function ellipse(x,y,a,b){
			
			var step = (a>b)?1/a:1/b;
			ctx.beginPath();
			ctx.moveTo(x+a,y);
			for (var i=0;i<2*Math.PI;i+=step){
				ctx.lineTo(x+a*Math.cos(i),y+b*Math.sin(i));
			}
			ctx.strokeStyle = "white";
			ctx.closePath();
			ctx.stroke();
		}
		
		// 水星轨道半径
		var MERCURY_DISTANCE = 40;
		var PATHWAY_RADIUS = {
				shui:1,
				jin:1.868,
				di:2.583,
				huo:3.936,
				mu:13.44-8.5,
				tu:24.642-18,
				tw:49.543-41,
				hw:77.777-68
		}

		// 星球旋转速度
		var SPEED = 0.3;
		//星星闪烁频率
		var STAR_FREQUENCY = 0.05;
		
		var STAR_LEVEL = {
			father:0,
			son:1,
			grandson:2
		}
		
		function drawLine(x1,y1,x2,y2){
			ctx.beginPath();
			ctx.moveTo(x1,y1);
			ctx.lineTo(x2,y2);
			ctx.lineWidth=2;
			ctx.strokeStyle='white';
			ctx.stroke();
			ctx.lineWidth=1;
			ctx.closePath();
		}
		
		function Point(x,y){
			this.x = x;
			this.y = y;
		}
		
		function drawSanjiaoxing(x,y,r){
			// 获取三个顶点的坐标
			
			var A1 = new Point(x,y-r);
			var A2 = new Point(x+0.5*r*Math.sqrt(3),y+0.5*r);
			var A3 = new Point(x-0.5*r*Math.sqrt(3),y+0.5*r);
			
			ctx.beginPath();
			//ctx.moveTo(x,y);
			ctx.strokeStyle = "white";
			ctx.moveTo(A1.x,A1.y);
			//ctx.lineTo(A1.x,A1.y);
			ctx.lineTo(A2.x,A2.y);
			ctx.lineTo(A3.x,A3.y);
			ctx.lineTo(A1.x,A1.y);
			ctx.stroke();
			
			ctx.fillStyle="white";
			ctx.fillText("A1",A1.x,A1.y);
			ctx.fillText("A2",A2.x,A2.y);
			ctx.fillText("A3",A3.x,A3.y);
			ctx.closePath();
			
			drawLine(CENTER_X,A1.y,CENTER_X,CENTER_Y);
			
		}

		// x,y 直线末端实时坐标
		// radius 长度
		// color 颜色
		// initAngle 初始角度
		// cx,cy 圆心坐标
		
		function LineObject(id,x,y,radius,color,initAngle,cx,cy){
			this.id = id;
			this.x = 0;
			this.y = 0;
			this.radius = radius;
			this.color = color;
			this.initAngle = initAngle;
			this.cx=cx;
			this.cy=cy;
			
			this.angleOpcity = 2; 
			this.angle = 0;
			this.randians = 0;
			
			this.draw = function(){
				ctx.beginPath();
				//ctx.translate(CENTER_X,CENTER_Y);
				ctx.strokeStyle = color;
				ctx.moveTo(this.cx,this.cy);
				ctx.lineTo(this.x,this.y);
				ctx.stroke();
				ctx.closePath();
			}
			
			this.update = function(){
				this.angle += this.angleOpcity;
				
				this.randians = initAngle + Math.PI*this.angle/180;
				//this.randians = Math.PI;
				
				this.x= CENTER_X + Math.cos(this.randians)*radius;
				this.y= CENTER_Y + Math.sin(this.randians)*radius;
				
				this.draw();
			}
			
		}
		
		/**
		*  name         球体名称
		*  parentName   该球体围绕哪个球体旋转的母球体名称
		*  radius       球半径
		*  distanceFromCenter  该球距离母球中心的距离
		*  cycle        旋转一圈所需要的距离  比如:地球转一圈需要365.2442
		*  color        球体颜色
		*  angle        球体初始角度   时钟点钟方向为0度  逆时针旋转时  角度增大
		*/
		function Star(id,x,y,radius,color){
			this.id=id;
			this.x = x;
			this.y = y;
			this.radius = radius;
			this.color = color;
			
			this.draw = function(){
				ctx.save();
				ctx.beginPath();

				// 画弧或者圆   圆的中心x坐标，圆的中心y坐标，圆的半径，起始角度，结束角度，绘图方向  false表示顺时针绘图
				ctx.arc(this.x,this.y,this.radius,0,2*Math.PI,false);
				//ctx.fillStyle = this.color;
				//ctx.fill();//填充颜色
				
				ctx.strokeStyle = this.color;
				ctx.lineWidth=3;
				ctx.stroke();
				ctx.restore();
			}
			
			this.update = function(){
				// 获取直线的圆心坐标
				for (var i=0;i<lineArr.length;i++){
					if (this.id == lineArr[i].id){
						this.x = lineArr[i].x+this.radius*Math.cos(lineArr[i].randians);
						this.y = lineArr[i].y+this.radius*Math.sin(lineArr[i].randians);
						break;
					}
				}
				this.draw();
			}
		}

		function Ball(x,y,r,color){
			this.x = x;
			this.y = y;
			this.r = r;
			this.color=color;
			this.down = true;
			
			this.vx = 0;
			this.vy = randomNumber(1,3);
			this.lift = -0.5;
			
			this.draw = function(){
				ctx.save();
				ctx.beginPath();

				// 画弧或者圆   圆的中心x坐标，圆的中心y坐标，圆的半径，起始角度，结束角度，绘图方向  false表示顺时针绘图
				ctx.arc(this.x,this.y,this.r,0,2*Math.PI,false);
				ctx.fillStyle = this.color;
				ctx.fill();//填充颜色
				ctx.restore();
			}
			
			this.update = function(){
				if (this.y > canvas.height-this.r){
					this.vy = -this.vy*gravity;
					this.y = canvas.height-this.r;
					this.down = false;
				}else if (this.y < this.r){
					this.vy = -this.vy*gravity;
					this.y = this.r;
					this.down = true;
				}else{
					this.vy +=0.1;
				}
				
				if (true){
					for (var i=0;i<starArr.length;i++){
						var distance = getTwoCircleDistance(this.x,this.y,starArr[i].x,starArr[i].y);
						if (distance < starArr[i].radius + this.r){
							this.vy = -this.vy+this.lift;
						}
					}
				}

				if (false){
					// 判断与两个打球的距离
					var isInCircle = false;
					var arrIndex = 0;
					var tmpDistance=0;
					for (var i=0;i<starArr.length;i++){
						var distance = getTwoCircleDistance(this.x,this.y,starArr[i].x,starArr[i].y);
						if (distance < starArr[i].radius + this.r){
							isInCircle = true;
							arrIndex = i;
							tmpDistance = distance;
							if (this.down){
								this.vy = 0.1;
							}else{
								this.vy = -0.1;
							}
						}else{
							continue;
						}
						
						if (isInCircle){
							if (this.down && this.y>starArr[arrIndex].y && tmpDistance >= starArr[arrIndex].radius - this.r){
								this.vy = -this.vy;
								this.down = false;
							}
							
							if (!this.down && this.y<starArr[arrIndex].y && tmpDistance >= starArr[arrIndex].radius - this.r){
								this.down = true;
								this.vy = -this.vy;
							}
						}
					}
				}
				this.x += this.vx;
				this.y += this.vy;
				this.draw();
			}
			
			this.hits = function(){
				if (this.y + this.r > canvas.height){
					return true;
				}
				
				return false;
				
			}
		}
		
		function randomNumber(start,end){
			var range = end-start;
			var rand = Math.random();
			var num = start + Math.round(rand*range);
			
			return num;
		}
		

		
		var lineArr = new Array();
		lineArr.push(new LineObject("1",0,0,150,"white",0,CENTER_X,CENTER_Y));
		lineArr.push(new LineObject("2",0,0,150,"white",Math.PI,CENTER_X,CENTER_Y));
		
		var starArr = new Array();
		starArr.push(new Star("1",0,0,60,"yellow"));
		starArr.push(new Star("2",0,0,60,"blue"));
		
		var ballArr = new Array();
		function generateBall(){
			var posx = randomNumber(50,canvas.width-50);
			var posy = randomNumber(50,150);
			ballArr.push(new Ball(posx,posy,15,randomRgb()));
		}

		var time = 0;
		function move(){
			requestAnimationFrame(move);
			ctx.clearRect(0,0,canvas.width,canvas.height);
			
			drawSanjiaoxing(CENTER_X,canvas.height-60,100);
			for (var i=0;i<lineArr.length;i++){
				lineArr[i].update();
			}
			
			for (var i=0;i<starArr.length;i++){
				starArr[i].update();
			}
			
			if (time % 10==0){
				generateBall();
			}
			
			for (var i=ballArr.length-1;i>0;i--){
				ballArr[i].update();
				
				if (ballArr[i].hits()){
					ballArr.splice(i,1);
				}
			}
			time++;
		}
		move();

		function randomRgb(){
			var red = randomNumber(0,255);
			var green = randomNumber(0,255);
			var blue = randomNumber(0,255);
			
			return "rgb(" + red + "," + green + "," + blue + ")";
		}
		
		function getTwoCircleDistance(x1,y1,x2,y2){
			var xDistance = x1 - x2;
			var yDistance = y1 - y2;
			return Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
		}
		
	</script>
</html>
