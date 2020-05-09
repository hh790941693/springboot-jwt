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
			#canvas{
				display:block;
				margin:3px auto;
			}
		</style>
	</head> 
	<body>
		<canvas id="canvas" style="background:#000">
			你的浏览器不支持canvas标签
		</canvas>
		<div id="controlDiv">
			星球旋转速度:<input id="planetSpeed" type="range" value=1  min=1 max=10 step=1 onchange="changePlanetSpeed()">
			<span id="speedVar">1</span>
			<br/>
			星星闪烁频率:<input id="starFrequency" type="range" value=0.01  min=0.01 max=1 step=0.05 onchange="changeStarFrequency()">
			<span id="freVar">0.05</span>
		</div>
	</body>
	<script type="text/javascript">	
		var canvas = document.getElementById("canvas");
		canvas.width = 570;
		canvas.height = 570;
		var ctx = canvas.getContext("2d");
		
		var CENTER_X = canvas.width/2;
		var CENTER_Y = canvas.height/2;
		var SETP_DIS = 34;
		
		// 星球旋转速度
		var SPEED = 1;
		//星星闪烁频率
		var STAR_FREQUENCY = 0.05;
		
		var STAR_LEVEL = {
			father:0,
			son:1,
			grandson:2
		}
		
		// 画轨道
		function drawTrack(){
			for (var i=0;i<8;i++){
				ctx.beginPath();
				ctx.arc(CENTER_X,CENTER_Y,(i+1)*SETP_DIS,0,2*Math.PI,false);
				ctx.closePath();
				ctx.strokeStyle = "#fff";
				ctx.setLineDash([1,2]);
				ctx.stroke();
			}
		}
		
		function drawLine(x1,y1,x2,y2){
			ctx.moveTo(x1,y1);
			ctx.lineTo(x2,y2);
			ctx.lineWidth=2;
			ctx.strokeStyle='#fff';
			ctx.stroke();
			ctx.lineWidth=1;
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
		function Star(name,parentName,radius,distanceFromCenter,cycle,color,angle){
			this.name = name;
			this.parentName = parentName;
			this.radius = radius;
			this.distanceFromCenter = Math.abs(distanceFromCenter);
			this.cycle = cycle;
			this.color = color;
			this.angle = angle;
			
			// 球的圆心坐标
			this.x = 0;
			this.y = 0;
			
			this.time = 0;
			// 旋转速度
			//this.speed = SPEED;
			
			// 画布中心坐标
			this.centerX = CENTER_X;
			this.centerY = CENTER_Y;
			this.draw = function(){
				ctx.save();
				ctx.translate(this.centerX,this.centerY);  // 移动画布的原点至(500,500)
				ctx.beginPath();
				
				if (this.cycle != 0){
					var angle = this.time*2*Math.PI/this.cycle+this.angle;
					this.x = Math.cos(angle)*this.distanceFromCenter;
					this.y = Math.sin(angle)*this.distanceFromCenter;
				}
				// 画弧或者圆   圆的中心x坐标，圆的中心y坐标，圆的半径，起始角度，结束角度，绘图方向  false表示顺时针绘图
				ctx.arc(this.x,this.y,this.radius,0,2*Math.PI,false);
				ctx.fillStyle = this.color;
				ctx.fill();//填充颜色
				
				var font = "bold 10px Arial";
				
				// 判断是否是儿子   太阳是父亲
				var starLevel = getStarLevel(this.name);
				
				if (starLevel == STAR_LEVEL.son){
					if (hasSon(this.name)){
						ctx.beginPath();
						ctx.arc(this.x,this.y,25,0,2*Math.PI,false);
						ctx.strokeStyle = this.color;
						ctx.stroke();//填充颜色
					}
				}

				if (starLevel == STAR_LEVEL.grandson){
					ctx.font = "bold 6px Arial";	
				}else{
					ctx.font = "bold 10px Arial";
				}
				ctx.fillStyle="white";
				if (this.name == "太阳"){
					ctx.fillText(this.name,this.x-11,this.y+5);
				}else{
					ctx.fillText(this.name,this.x-5,this.y-13);
				}
				ctx.restore();
			}
			
			this.update = function(){
				if (this.parentName !=""){
					for (var i=0;i<starsArr.length;i++){
						if (starsArr[i].name == parentName){
							this.centerX = starsArr[i].x+CENTER_X;
							this.centerY = starsArr[i].y+CENTER_Y;
							break;
						}
					}	
				}
				
				this.time += SPEED;
				this.draw();
			}
		}
		
		function Star2(name,x,y,radius,cycle,sColor,eColor){
			this.name = name;
			this.x = x;
			this.y = y;
			this.radius = radius;
			this.cycle = cycle;
			this.sColor = sColor;
			this.eColor = eColor;
			this.color = null;
			this.time = 0;
			this.centerX = CENTER_X;
			this.centerY = CENTER_Y;
			this.draw = function(){
				ctx.save();
				ctx.translate(CENTER_X,CENTER_Y);  // 移动画布的原点至(500,500)
				ctx.rotate(this.time*2*Math.PI/this.cycle);  // 旋转角度
				ctx.beginPath();

				// 画弧或者圆   圆的中心x坐标，圆的中心y坐标，圆的半径，起始角度，结束角度，绘图方向  false表示顺时针绘图
				ctx.arc(this.x,this.y,this.radius,0,2*Math.PI,false);
				ctx.closePath();
				this.color = ctx.createRadialGradient(this.x,this.y,0,this.x,this.y,this.radius);
				this.color.addColorStop(0,this.sColor);
				this.color.addColorStop(1,this.eColor);
				ctx.fillStyle = this.color;
				ctx.fill();//填充颜色
				
				ctx.font = "bold 10px Arial";
				ctx.fillStyle="white";
				ctx.fillText(this.name,x-5,y-13);
				ctx.restore();
			}
			
			this.update = function(){
				this.time += 1;
				this.draw();
			}
		}
		
		function randomNumber(start,end){
			var range = end-start;
			var rand = Math.random();
			var num = start + Math.round(rand*range);
			
			return num;
		}
		
		function StarFive(r , R , x , y,borderWidth , borderStyle , fillStyle, opcity){
			this.r = r;
			this.R = R;
			this.x = x;
			this.y = y;
			this.borderWidth = borderWidth;
			this.borderStyle = borderStyle;
			
			this.fillStyle = "rgba(255,255,255,0.05)";
			this.opcity = opcity;
			this.plus = true;
			this.time = 0;
	        
	        this.draw = function(){
				ctx.beginPath();
		        for( var i = 0 ; i < 5 ; i ++){
		            ctx.lineTo(Math.cos((18+72*i)/180*Math.PI) * R + x ,- Math.sin((18+72*i)/180*Math.PI) * this.R + this.y);
		            ctx.lineTo(Math.cos((54+72*i)/180*Math.PI) * r + x ,- Math.sin((54+72*i)/180*Math.PI) * this.r + this.y);
		        }
		        ctx.closePath();

		        ctx.lineWidth = this.borderWidth;
		        ctx.strokeStyle = this.borderStyle;
		        ctx.fillStyle = this.fillStyle;

		        ctx.fill();
		        ctx.stroke();
	        }
	        
	        this.update = function(){
	        	this.draw();
	        	this.time++;
	        	
	        	if (this.opcity >0 && this.opcity <1){
	        		if (this.plus){
	        			this.opcity+=STAR_FREQUENCY;
	        		}else{
	        			this.opcity-=STAR_FREQUENCY;
	        		}
	        	}else if (this.opcity >= 1){
	        		this.opcity-=STAR_FREQUENCY;
	        		this.plus = false;
	        	}else{
	        		this.opcity+=STAR_FREQUENCY;
	        		this.plus = true;
	        	}
	        	
	        	//console.log(this.opcity);
	        	this.fillStyle = "rgba(255,255,255," + this.opcity + ")";
	        }
	    }
		
		var starsArr = new Array(
			new Star("太阳",   "",   15,    0,         0,        "rgba(255,255,0,0.65)",     0),
			new Star("水星",  "太阳",  10, SETP_DIS*1,  87.70,     "#A69697", 0),
			new Star("金星",  "太阳",  10, SETP_DIS*2,  224.701,   "#C4BBAC", 0),
			new Star("地球",  "太阳",  10, SETP_DIS*3,  365.2422,  "#78B1E8", 0),
			new Star("火星",  "太阳",  10, SETP_DIS*4,  686.98,    "#CEC9B6", 0),
			new Star("木星",  "太阳",  10, SETP_DIS*5,  4332.589,  "#C0A48E", 0),
			new Star("土星",  "太阳",  10, SETP_DIS*6,  10759.5,   "#F7F9E3", 0),
			new Star("天王星", "太阳",  10, SETP_DIS*7,  30799.095, "#A7E1E5",  0),
			new Star("海王星", "太阳",  10, SETP_DIS*8,  60152,     "#0661B2",  0),
			new Star("月球",  "地球",   5,    25,       29.53,     "#0661B2", 0),
			new Star("土卫1", "土星",   5,    25,         150,     "#AA2102", 0),
			new Star("土卫2", "土星",   5,    25,         150,     "#CC61B2",Math.PI),
			new Star("木卫1", "木星",   4,    25,         100,     "#AA2102", Math.PI/2),
			new Star("木卫2", "木星",   4,    25,         100,     "#CC61B2",Math.PI*3/2)
		)

		// 获取太阳的名称
		function getFatherStar(){
			var fatherStarName;
			for (var i=0;i<starsArr.length;i++){
				if (starsArr[i].name != "" && starsArr[i].parentName == ""){
					fatherStarName = starsArr[i].name;
					break;
				}
			}
			
			return fatherStarName;
		}
		
		function isSonStar(starName){
			var isSon = false;
			var fatherStarName = getFatherStar();
			for (var i=0;i<starsArr.length;i++){
				if (starsArr[i].name == starName && starsArr[i].parentName == fatherStarName){
					isSon = true;
					break;
				}
			}
			
			return isSon;
		}
		
		function isGrandsonStar(starName){
			var isGrandson = false;
			var fatherStarName = getFatherStar();
			for (var i=0;i<starsArr.length;i++){
				if (starsArr[i].name == starName){
					var isSon = isSonStar(starsArr[i].parentName);
					if (isSon){
						isGrandson = true;
					}
					break;
				}
			}
			
			return isGrandson;
		}
		
		function hasSon(starName){
			var res = false;
			fatherStarName = getFatherStar();
			if (fatherStarName == starName){
				res = true;
			}else{
				for (var i=0;i<starsArr.length;i++){
					if (starsArr[i].parentName == starName){
						res = true;
						break;
					}
				}
			}
			return res;
		}
		
		// 获取星球的级别    0:父亲   1:儿子  2:孙子
		function getStarLevel(starName){
			var level;
			var fatherStarName = getFatherStar();
			if (starName == fatherStarName){
				level = STAR_LEVEL.father;
			}else{
				if (isSonStar(starName)){
					level = STAR_LEVEL.son;
				}else if (isGrandsonStar(starName)){
					level = STAR_LEVEL.grandson;
				}
			}
			
			return level;
		}
		
		// 星星数组
		var starFiveArr = new Array();
		for (var i=0;i<40;i++){
			var r = randomNumber(1,3);
			var R = r + randomNumber(1,3);
			var x = randomNumber(10,canvas.width-10);
			var y = randomNumber(10,canvas.height-10);
			var opcity = Math.random();
			var tmpStar = new StarFive(r,R,x,y,1,"white","white",opcity);
			starFiveArr.push(tmpStar);
		}
		
		function move(){
			ctx.clearRect(0,0,1000,900);
			drawTrack();
			//drawLine(CENTER_X,CENTER_Y,CENTER_X,-SETP_DIS*8);

			for (var i=0;i<starsArr.length;i++){
				starsArr[i].update();
			}
			
			for (var i=0;i<starFiveArr.length;i++){
				starFiveArr[i].update();
			}
		}
		
		setInterval(move,100);
		
		function changePlanetSpeed(){
			var curSpeed =  document.getElementById("planetSpeed").value;
			SPEED = parseInt(curSpeed);
			document.getElementById("speedVar").innerHTML=SPEED;
		}
		
		function changeStarFrequency(){
			var curSpeed =  document.getElementById("starFrequency").value;
			STAR_FREQUENCY = parseFloat(curSpeed);
			document.getElementById("freVar").innerHTML=STAR_FREQUENCY;
		}
		
	</script>
</html>
