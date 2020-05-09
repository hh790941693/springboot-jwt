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
		<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
		
		<style type="text/css">
			*{
				margin:0px;
				padding:0px;
			}
		</style>
		
	</head> 
	<body>
		<div id='mainDiv' style='width:99%;height:100%;'>
			<canvas id="canvas" style="background:#000;">
				你的浏览器不支持canvas标签
			</canvas>
		</div>
	</body>
	<script type="text/javascript">	
		var canvas = document.getElementById("canvas");
		canvas.width = window.innerWidth-10;
		canvas.height = window.innerHeight-10;
		var ctx = canvas.getContext("2d");
		
		var colorArray = new Array();
		colorArray.push("red");
		colorArray.push("white");
		colorArray.push("yellow");
		colorArray.push("blue");
		colorArray.push("grey");
		colorArray.push("green");

		var maxRadius = 40;
		var minRadius = 10;
		
		var gravity = 0.99;
		
		
		var mousePos = {
			x:10,
			y:10
		};
		
		var circleArray = [];

		for (var i=0;i<100;i++){
			var radius = randomNumber(5,15);
			var x = randomNumber(radius+10,canvas.width-10);
			var y = randomNumber(radius+10,canvas.height-radius-10);
			var dx = randomNumber(-3,3);
			var dy = randomNumber(1,3);
			var color = colorArray[randomNumber(0,colorArray.length-1)];

			var c = new Circle(x,y,dx,dy,radius,color);
			circleArray.push(c);
		}
		
		window.addEventListener('mousemove',function(event){
			mousePos.x = event.x;
			mousePos.y = event.y;
		})
		
		window.addEventListener('click',function(event){
			var targetX = event.x;
			var targetY = event.y;
			
			
			var srcR = 20;
			var srcX = canvas.width/2;
			var srcY = canvas.height-srcR-50;
			
			var xDistance = srcX - targetX;
			var yDistance = srcY - targetY;
			
			var dx=-xDistance/30,dy=-yDistance/30;

			//var distance = Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
			//var c = new Circle("zidan",zidanArray.length,srcX,srcY,dx,dy,srcR,"red");
			//zidanArray.push(c);
		})
		
		window.addEventListener('resize',function(event){
			canvas.width = window.innerWidth;
			canvas.height = window.innerHeight;
		})
		
		function getTwoCircleDistance(circle1,circle2){
			var xDistance = circle1.x - circle2.x;
			var yDistance = circle1.y - circle2.y;
			var distance = Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
			
			return distance;
		}
		
		function Circle(x,y,dx,dy,radius,color){
			this.x = x;
			this.y = y;
			this.dx = dx;
			this.dy = dy;
			this.radius = radius;
			this.color = color;
			
			// 画图
			this.draw = function(){
				ctx.beginPath();
				ctx.arc(this.x,this.y,this.radius,0,2*Math.PI,false);
				
				ctx.strokeStyle = this.color;
				ctx.stroke();
				
				ctx.fillStyle = this.color;
				ctx.fill();
			}
			
			// 更新
			this.update = function(){
				
				if (mousePos.x-this.x<50 && mousePos.x - this.x> -50
						  && mousePos.y - this.y< 50
						  && mousePos.y - this.y> -50){
					if (this.x <= mousePos.x){
						this.x +=-30;
					}else{
						this.x +=30;
					}
					if (this.y <= mousePos.y){
						this.y +=-30;
					}else{
						this.y +=30;
					}
					
					if (this.x < this.radius){
						this.x = this.radius;
					}else if (this.x > canvas.width - this.radius){
						this.x = canvas.width - this.radius;
					}
					
					if (this.y < this.radius){
						this.y = this.radius;
					}else if (this.y + this.radius > canvas.height){
						this.y = canvas.height - this.radius;
					}
				}
				
				if (this.x + this.radius > canvas.width || this.x-this.radius < 0){
					this.dx = -this.dx;
				}

				if (this.y+this.radius > canvas.height || this.y - this.radius <0){
					this.dy = -this.dy*gravity;
				}else{
					this.dy +=1;
				}

				this.x += this.dx;
				this.y += this.dy;
				
				this.draw();
			}
		}
		
		function animate(){
			requestAnimationFrame(animate);
			ctx.clearRect(0,0,canvas.width,canvas.height);
			
			for (var i=0;i<circleArray.length;i++){
				circleArray[i].update();
			}
		}
		
		animate();
		//setInterval(animate,10);
		
		function randomNumber(start,end){
			var range = end-start;
			var rand = Math.random();
			var num = start + Math.round(rand*range);
			
			return num;
		}
	</script>
</html>
