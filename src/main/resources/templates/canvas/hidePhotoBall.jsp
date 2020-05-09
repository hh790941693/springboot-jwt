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
			body{
				margin:0;
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
	</body>
	<script type="text/javascript">	
	
		var canvas = document.getElementById("canvas");
		canvas.width = 800;
		canvas.height = 560;
		var ctx = canvas.getContext("2d");

		var img = new Image();
		var imgIndex = randomNumber(1,4);
		img.src = rootUrl + "/img/" +"tzd" +imgIndex +".jpg";
		img.onload = function(){
			ctx.drawImage(img,0,0);
		}

		function moveBall(){
			ctx.clearRect(0,0,canvas.width,canvas.height);
			ctx.save();
			
			ball.update();
		}
		
		function drawBall(x,y,radius,dx,dy){
			this.x = y;
			this.y = x;
			this.radius = radius;
			this.dx = dx;
			this.dy = dy;
			
			this.draw = function(){
				//ctx.save();
				//ctx.beginPath();
				//ctx.arc(this.x,this.y,this.radius,0,2*Math.PI,false);
				//ctx.stroke();
				
		        //ctx.fillStyle = '#000';
		        //ctx.fill(); 
				
				//ctx.clip();
				//ctx.closePath();
				
				//ctx.restore();
		        ctx.beginPath();
		        ctx.fillStyle = '#000';
		       	ctx.fillRect(0,0,canvas.width,canvas.height);
		        //渲染探照灯
		        ctx.beginPath();
		        ctx.arc(this.x,this.y,this.radius,0,2*Math.PI,false);
		        ctx.fillStyle = '#000';
		        ctx.fill(); 
		        ctx.clip();      
		        //由于使用了clip()，画布背景图片会出现在clip()区域内
		        ctx.drawImage(img,0,0);
		        
				// 必须在调用restore之前画图
		        ctx.restore();
			}
			
			this.update = function(){
				if (this.x-this.radius <=0 || this.x + this.radius >= canvas.width){
					this.dx=-this.dx;
					if (this.x-this.radius <=0)
					{
						this.x=this.radius;
					}else{
						this.x=canvas.width-this.radius;
					}
				}
				
				if (this.y - this.radius <=0 || this.y + this.radius >= canvas.height){
					this.dy = -this.dy;
					
					if (this.y - this.radius <=0){
						this.y = this.radius;
					}else{
						this.y = canvas.height - this.radius;
					}
				}
				this.x +=this.dx;
				this.y +=this.dy;
				this.draw();
			}
		}

		var maxRadius = 40;
		var minRadius = 10;
		
		var mousePos = {
			x:10,
			y:10
		};

		window.addEventListener('mousemove',function(event){
			mousePos.x = event.x;
			mousePos.y = event.y;
		})
		
		var ball = new drawBall(300,300,150,6,6);
		function animate(){
			requestAnimationFrame(animate);
			ctx.clearRect(0,0,canvas.width,canvas.height);
			ctx.save();
			
			ball.update();
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
