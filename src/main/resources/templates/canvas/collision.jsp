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
		</style>
		
	</head> 
	<body>
		<canvas id="canvas" style="background:#000">
			你的浏览器不支持canvas标签
		</canvas>
	</body>
	<script type="text/javascript">	
	
		var canvas = document.getElementById("canvas");
		canvas.width = window.innerWidth;
		canvas.height = window.innerHeight;
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
		
		
		var mousePos = {
			x:10,
			y:10
		};
		
		var circleArray = [];
		for (var i=0;i<100;i++){
			var radius = randomNumber(5,15);
			var x = randomNumber(radius,canvas.width-radius);
			var y = randomNumber(radius,canvas.height-radius);
			var dx = randomNumber(1,3);
			var dy = randomNumber(1,3);
			var color = colorArray[randomNumber(0,colorArray.length-1)];
			
			var isCollision = false;
			for (var j=0;j<circleArray.length;j++){
				var distance = getTwoCircleDistance(x,y,circleArray[j].x,circleArray[j].y);
				if (distance <= radius + circleArray[j].radius){
					isCollision = true;
					break;
				}
			}
			
			if (!isCollision){
				var c = new Circle(x,y,dx,dy,radius,color);
				circleArray.push(c);
			}else{
				i--;
			}
		}
		
		function getTwoCircleDistance(x1,y1,x2,y2){
			var xDistance = x1 - x2;
			var yDistance = y1 - y2;
			return Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
		}
		
		window.addEventListener('mousemove',function(event){
			mousePos.x = event.x;
			mousePos.y = event.y;
			//console.log(mousePos.x + "," + mousePos.y);
		})
		
		window.addEventListener('resize',function(event){
			canvas.width = window.innerWidth;
			canvas.height = window.innerHeight;
		})
		
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
				ctx.fillStyle = this.color;
				ctx.fill();
			}
			
			// 更新
			this.update = function(){
				if (this.x + this.radius > canvas.width || this.x-this.radius < 0){
					this.dx = -this.dx;
				}
				
				if (this.y+this.radius > canvas.height || this.y - this.radius <0){
					this.dy = -this.dy;
				}
				
				// 遍历其他球
				for (var i=0;i<circleArray.length;i++){
					
					//if (this.x == circleArray[i].x && this.y == circleArray[i].y){
					//	continue;
					//}
					if (this == circleArray[i]){
						continue;
					}

					var distance = getTwoCircleDistance(this.x,this.y,circleArray[i].x,circleArray[i].y);
					if (distance <= this.radius + circleArray[i].radius){
						this.dx = -this.dx;
						this.dy = -this.dy;
					}
				}
				
				this.x += this.dx;
				this.y += this.dy;
				
				//if (mousePos.x-this.x<50 && mousePos.x - this.x> -50
				//						  && mousePos.y - this.y< 50
				//						  && mousePos.y - this.y> -50){
				//	if (this.radius <maxRadius){
				//		this.radius +=1;
				//	}
				//}else if (this.radius >minRadius){
				//	this.radius -= 1;
				//}
				
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
		
		
		function randomNumber(start,end){
			var range = end-start;
			var rand = Math.random();
			var num = start + Math.round(rand*range);
			
			return num;
		}
	</script>
</html>
