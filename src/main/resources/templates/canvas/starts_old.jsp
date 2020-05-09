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
		</style>
	</head> 
	<body>
		<canvas id="canvas" style="background:#000">
			你的浏览器不支持canvas标签
		</canvas>
	</body>
	<script type="text/javascript">	
		//var canvas = document.getElementById("canvas").
		//canvas.width=570;
		//canvas.height=570;
		//var ctx = canvas.getContext("2d");
		
		var canvas = document.getElementById("canvas");
		canvas.width = 570;
		canvas.height = 570;
		var ctx = canvas.getContext("2d");
		
		var CENTER_X = canvas.width/2;
		var CENTER_Y = canvas.height/2;
		var SETP_DIS = 34;
		
		// 画轨道
		function drawTrack(){
			for (var i=0;i<8;i++){
				ctx.beginPath();
				ctx.arc(CENTER_X,CENTER_Y,(i+1)*SETP_DIS,0,2*Math.PI,false);
				ctx.closePath();
				ctx.strokeStyle = "#fff";
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
		*  x      起始横坐标
		*  y      起始纵坐标
		*  radius 半径
		*  cycle  周期
		*
		*
		*/
		function star(name,x,y,radius,cycle,sColor,eColor){
			this.name = name;
			this.x = x;
			this.y = y;
			this.radius = radius;
			this.cycle = cycle;
			this.sColor = sColor;
			this.eColor = eColor;
			this.color = null;
			this.time = 0;
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
				this.time += 1;
			}
		}
		
		function Sun(){
			star.call(this,"太阳",0,0,15,0,"#FFFF00","#FF9900");
		}
		
		// 水
		function Mercury(){
			star.call(this,"水星",0,-SETP_DIS,10,87.70,"#A69697","#5C3E40");
		}
		
		// 金
		function Venus(){
			star.call(this,"金星",0,-SETP_DIS*2,10,224.701,"#C4BBAC","#1F1315");
		}
		
		//地
		function Earth(){
			star.call(this,"地球",0,-SETP_DIS*3,10,365.2422,"#78B1E8","#050C12");
		}		
		
		// 火
		function Mars(){
			star.call(this,"火星",0,-SETP_DIS*4,10,686.98,"#CEC9B6","#76422D");
		}
		
		// 木
		function Jupiter(){
			star.call(this,"木星",0,-SETP_DIS*5,10,4332.589,"#C0A48E","#322222");
		}
		
		// 土
		function Saturn(){
			star.call(this,"土星",0,-SETP_DIS*6,10,10759.5,"#F7F9E3","#5C4533");
		}
		
		// 天王
		function Uranus(){
			star.call(this,"天王星",0,-SETP_DIS*7,10,30799.095,"#A7E1E5","#19243A");
		}
		
		// 海王
		function Neptune(){
			star.call(this,"海王星",0,-SETP_DIS*8,10,60152,"#0661B2","#1E3B73");
		}
		
		var sun = new Sun();
		var mercury = new Mercury();
		var venus = new Venus();
		var earth = new Earth();
		var mars = new Mars();
		var jupiter = new Jupiter();
		var saturn = new Saturn();
		var uranus = new Uranus();
		var neptune = new Neptune();
		
		
		function move(){
			ctx.clearRect(0,0,1000,900);
			drawTrack();
			drawLine(CENTER_X,CENTER_Y,CENTER_X,-SETP_DIS*8);
			sun.draw();
			mercury.draw();
			venus.draw();
			earth.draw();
			mars.draw();
			jupiter.draw();
			saturn.draw();
			uranus.draw();
			neptune.draw();
		}
		
		setInterval(move,100);
	</script>
</html>
