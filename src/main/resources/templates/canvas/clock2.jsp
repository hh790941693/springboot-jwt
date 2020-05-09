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
		<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
		
		<style type="text/css">
			#canvas{
				display:block;
				margin:3px auto;
			}
		</style>
	</head> 
	<body>
		<canvas id="canvas" width="600" height="550" style="background:#000">
			你的浏览器不支持canvas标签
		</canvas>
		
		<audio id="player" controls="controls" src="" style="width:100%;display:none;"></audio>
	</body>
	<script type="text/javascript">	
		var ctx = document.getElementById("canvas").getContext("2d");
		var X=300,Y=250,R=200;
		
		var player =  document.getElementById("player");
		
		// 画轨道
		var bufferCanvas = document.createElement("canvas");
		bufferCanvas.width = ctx.width;
		bufferCanvas.height = ctx.height;
		var bufferCtx = bufferCanvas.getContext("2d");
		function drawTrack(){
			if (true){
				bufferCtx.beginPath();
				bufferCtx.arc(X,Y,R,0,2*Math.PI,false);
				bufferCtx.closePath();
				bufferCtx.strokeStyle = "#fff";
				bufferCtx.lineWidth=1;
				bufferCtx.stroke();
				
				bufferCtx.beginPath();
				bufferCtx.arc(X,Y,10,0,2*Math.PI,false);
				bufferCtx.fillStyle="yellow";
				bufferCtx.fill();
			}else{
				ctx.beginPath();
				ctx.arc(X,Y,R,0,2*Math.PI,false);
				ctx.closePath();
				ctx.strokeStyle = "#fff";
				ctx.lineWidth=1;
				ctx.stroke();
				
				ctx.beginPath();
				ctx.arc(X,Y,10,0,2*Math.PI,false);
				ctx.fillStyle="yellow";
				ctx.fill();
			}
		}
		
		// type:指针类型
		// x,y:圆心坐标
		// x1,y1:指针路径
		// radius:指针长度
		// color:指针颜色
		// width:指针宽度
		function drawObject(type, x,y,x1,y1,radius,color,width){
			this.type = type;
			this.x = x;
			this.y = y;
			this.x1 = x1;
			this.y1 = y1;
			this.radius = radius;
			this.color = color;
			this.width = width;
			
			this.radians = 0;
			//this.time = 0;

			this.draw = function(){
				ctx.beginPath();
				ctx.moveTo(this.x,this.y);
				ctx.lineTo(this.x1,this.y1);
				ctx.lineWidth=this.width;
				ctx.strokeStyle=this.color;
				ctx.stroke();
				
				ctx.beginPath();
				ctx.moveTo(this.x1,this.y1);
				
				ctx.strokeStyle=this.color;
				var radians1,radians2;
				var angelBeita = 15*Math.PI/180;
				if (this.radians >=0 && this.radians <=Math.PI){
					radians1 = Math.PI+this.radians-angelBeita;
					radians2 = Math.PI+this.radians+angelBeita;
				}else if (this.radians >=Math.PI && this.radians <=Math.PI*2){
					radians1 = this.radians-Math.PI+angelBeita;
					radians2 = this.radians-Math.PI-angelBeita;
				}
				
				//radians1 = this.radians+Math.PI*2/3;
				ctx.lineTo(this.x1+Math.cos(radians1)*20,this.y1-Math.sin(radians1)*20);
				ctx.stroke();
				
				ctx.moveTo(this.x1,this.y1);
				//radians2 = this.radians+Math.PI*4/3;
				ctx.lineTo(this.x1+Math.cos(radians2)*20,this.y1-Math.sin(radians2)*20);
				ctx.stroke();
			}
			
			this.update = function(){
				var date = new Date();
				var hour = date.getHours();
				hour = parseInt(hour);
				var minutes = date.getMinutes();
				minutes = parseInt(minutes);
				var second = date.getSeconds();
				second = parseInt(second);
				
				if (this.type == "second"){
					if (minutes ==0 && second == 0){
						player.src = rootUrl + "/audio/" +hour+".wav";
						player.play();
					}
				}
				
				var angle = getRadians(this.type);
				this.radians = angle*Math.PI/180;

				this.x1 = x + Math.cos(this.radians) * this.radius;
				this.y1 = y -Math.sin(this.radians) * this.radius;

				this.draw();
			}
		}

		function getRadians(type){
			var date = new Date();
			var hour = date.getHours();
			hour = parseInt(hour);
			var minutes = date.getMinutes();
			minutes = parseInt(minutes);
			var second = date.getSeconds();
			second = parseInt(second);
			
			var hourRadians = 0;
			var minuteRadians = 0;
			var secondRadians = 0;
			
			if (hour > 12){
				hour = hour-12 + minutes / 60 + second / 3600;
			}
			minutes = minutes + second/60;
			
			if (hour >=0 && hour <=3 ){
				hourRadians = Math.abs(hour-3)*30;
			}else if (hour >=9 && hour <=12){
				hourRadians = 90+Math.abs(hour-12)*30;
			}else if (hour >=6 && hour <=9){
				hourRadians = 180+Math.abs(hour-9)*30;
			}else if (hour >=3 && hour <=6){
				hourRadians = 270+Math.abs(hour-6)*30;
			}

			if (minutes >=0 && minutes <=15 ){
				minuteRadians = Math.abs(minutes-15)*6;
			}else if (minutes >=45 && minutes <=59){
				minuteRadians = 90+Math.abs(minutes-60)*6;
			}else if (minutes >=30 && minutes <=45){
				minuteRadians = 180+Math.abs(minutes-45)*6
			}else if (minutes >=15 && minutes <=30){
				minuteRadians = 270+Math.abs(minutes-30)*6;
			}

			if (second >=0 && second <=15 ){
				secondRadians = Math.abs(second-15)*6;
			}else if (second >=45 && second <=59){
				secondRadians = 90+Math.abs(second-60)*6;
			}else if (second >=30 && second <=45){
				secondRadians = 180+Math.abs(second-45)*6;
			}else if (second >=15 && second <=30){
				secondRadians = 270+Math.abs(second-30)*6;
			}
			
			if (type == "hour"){
				return hourRadians;
			}else if (type == "minute"){
				return minuteRadians;
			}else if (type == "second"){
				return secondRadians;
			}
		}
		
		function ClockBord(type,r,color){
			this.type = type;
			this.r = r;
			this.value = 0;

			this.finalAngel = 0;
			this.color = color;
			this.lineWidth=12;
			this.draw = function(){
				ctx.save();
				ctx.beginPath();
				ctx.translate(X,Y);
				ctx.rotate(-Math.PI/2);
			
				ctx.arc(0,0,this.r,0,this.finalAngle,false);
				ctx.strokeStyle=this.color;
				ctx.lineWidth = this.lineWidth;
				ctx.stroke();
				ctx.closePath();
				ctx.restore();
			}
			
			this.update = function(){
				var date = new Date();
				var hour = date.getHours();
				hour = parseInt(hour);
				if (hour > 12){
					hour = hour -12;
				}
				var minute = date.getMinutes();
				minute = parseInt(minute);
				var second = date.getSeconds();
				second = parseInt(second);

				if (this.type=="hour"){
					var tmpvar= hour+minute/60+second/3600;
					this.finalAngle = tmpvar*30 * Math.PI/180;
					
				}else if (this.type=="minute"){
					var tmpvar = minute+second/60;
					this.finalAngle = tmpvar*6 * Math.PI/180;
				}else{
					if (second==0){
						this.finalAngle = Math.PI*2;
					}else{
						var tmpvar = second*6;
						this.finalAngle = tmpvar*Math.PI/180;
					}
				}
				
				this.draw();
			}
		}
		
		function drawTime(){
			var date = new Date();
			var hour = date.getHours();
			var minutes = date.getMinutes();
			var second = date.getSeconds();
			
			if (parseInt(hour) >=0 && parseInt(hour) <10){
				hour = "0"+hour;
			}
			
			if (parseInt(minutes) >=0 && parseInt(minutes) <10){
				minutes = "0"+minutes;
			}
			
			if (parseInt(second) >=0 && parseInt(second) <10){
				second = "0"+second;
			}
			
			ctx.font="20pt Calibri";
			ctx.fillStyle="white";
			ctx.fillText(hour+":"+minutes+":"+second,250,510);
		}
		
		var hourHand = new drawObject("hour",X,Y,X,0,100,"green",5);
		var minutesHand = new drawObject("minute",X,Y,X,0,140,"blue",3);
		var secondHand = new drawObject("second",X,Y,X,0,170,"red",1);
		
		var hourArc = new ClockBord("hour",R-15,"green");
		var minuteArc = new ClockBord("minute",R,"blue");
		var secondArc = new ClockBord("second",R+15,"red");
		function move(){
			//requestAnimationFrame(move);
			ctx.clearRect(0,0,600,600);
			//drawTrack();
			secondHand.update();
			minutesHand.update();
			hourHand.update();
			
			hourArc.update();
			minuteArc.update();
			secondArc.update();
			
			drawTime();
		}
		setInterval(move,1000);
	</script>
</html>
