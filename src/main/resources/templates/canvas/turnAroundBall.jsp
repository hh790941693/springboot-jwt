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
				border:1px solid blue;
			}

			#mainDiv{
				border:1px solid red;
				width:804px;
				height:604px;
			}
			#mainDiv{
				margin:3px auto;
			}
		</style>
		
	</head> 
	<body>
		<div id="mainDiv">
			<canvas id="canvas" style="background:#fff">
				你的浏览器不支持canvas标签
			</canvas>
		</div>
	</body>
	<script type="text/javascript">	
	
		var canvas = document.getElementById("canvas");
		//canvas.width = window.innerWidth;
		//canvas.height = window.innerHeight;
		canvas.width = 800;
		canvas.height = 600;
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
			x:canvas.width/2,
			y:canvas.height/2
		};
		
		var circleArray = [];

		window.addEventListener('mousemove',function(event){
			mousePos.x = event.x;
			mousePos.y = event.y;
		})
		
		window.addEventListener('touchstart',function(event){
			event.preventDefault();
			//console.log(event);
			//alert(event);
			
			mousePos.x = event.originalEvent.changedTouches[0].pageX;
			mousePos.y = event.originalEvent.changedTouches[0].pageY;
		});
		
		window.addEventListener('resize',function(event){
			//canvas.width = window.innerWidth;
			//canvas.height = window.innerHeight;
		})
		
		window.addEventListener('click',function(event){
			ctx.clearRect(0,0,canvas.width,canvas.height);
			mousePos.x = event.x;
			mousePos.y = event.y;
			init();
		})
		
		function init(){
			circleArray = [];
			for (var i=0;i<50;i++){
				//var radius = randomNumber(5,25);
				//var x = randomNumber(radius,canvas.width-radius);
				//var y = randomNumber(radius,canvas.height-radius);
				//var x = canvas.width/2;
				//var y = canvas.height/2;
				var x = mousePos.x;
				var y = mousePos.y;
				var radius = randomNumber(1,3);
				var dx = randomNumber(1,3);
				var dy = randomNumber(1,3);
				//var color = colorArray[randomNumber(0,colorArray.length-1)];
				var color = randomRgb();

				var c = new Circle(x,y,dx,dy,radius,color);
				circleArray.push(c);
			}
		}
		
		function Circle(x,y,dx,dy,radius,color){
			this.x = x;
			this.y = y;
			this.dx = dx; // 暂时不用
			this.dy = dy; // 暂时不用
			this.radians = Math.random()*Math.PI*2; //弧度
			this.velocity = 0.05;
			this.radius = radius;
			this.color = color;
			
			this.distanceFromCenter = randomNumber(50,200);
			
			// 画图
			this.draw = function(lastPoint){
				ctx.beginPath();
				//ctx.arc(this.x,this.y,this.radius,0,2*Math.PI,false);
				//ctx.fillStyle = this.color;
				//ctx.fill();
				ctx.strokeStyle=this.color;
				ctx.lineWidth = this.radius;
				ctx.moveTo(lastPoint.x,lastPoint.y);
				ctx.lineTo(this.x,this.y);
				ctx.stroke();
				ctx.closePath();
			}
			
			// 更新
			this.update = function(){
				var lastPoint = {x:this.x,y:this.y};
				this.radians += this.velocity;
				this.x = mousePos.x + Math.cos(this.radians) * this.distanceFromCenter;
				this.y = mousePos.y + Math.sin(this.radians) * this.distanceFromCenter;

				this.draw(lastPoint);
			}
		}
		
		function animate(){
			requestAnimationFrame(animate);
			//ctx.clearRect(0,0,canvas.width,canvas.height);
			ctx.fillStyle = "rgba(255,255,255,0.05)";
			ctx.fillRect(0,0,canvas.width,canvas.height);
			for (var i=0;i<circleArray.length;i++){
				circleArray[i].update();
			}
		}
		
		init();
		animate();
		
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
	</script>
</html>
