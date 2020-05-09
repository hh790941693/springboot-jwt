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
			.snakeBodyClass{
				background-color:green;
			}
			.snakeHeadClass{
				background-color:red;
			}
            .foodClass{
				background-color:yellow;
			}
			.topWallClass{
			}
			.rightWallClass{
			}
			.bottomWallClass{
			}
			.leftWallClass{
			}
		</style>
	</head> 
	<body>
		<div id="gameMainDiv" style="width:700px;height:620px;border:2px solid green;margin:5px auto;">
			<div id="borderDiv" style="width:420px;height:610px;border:1px solid grey;float:left">
				
			</div>
			<div id="controlDiv" style="width:250px;height:610px;border:2px solid grey;float:right">
				<div id="buttonDiv">
					<div id="modeDiv" style="padding-left:30px">
						<input type="radio" value="1" name="mode" checked="checked">简单
						<input type="radio" value="2" name="mode">中等
						<input type="radio" value="3" name="mode">困难
					</div>
					<br/>
					<div style="height:23px;padding-left:86px">
						<button id="btnUp" onclick="up()">上(w)</button>
					</div>
					<div style="height:23px;padding-left:50px">
						<button id="btnLeft" onclick="left()">左(a)</button>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button id="btnRight" onclick="right()">右(d)</button>
					</div>
					<div style="padding-left:86px">
						<button id="btnDown" onclick="down()">下(s)</button>
					</div>
					
					<br/>
					<div style="padding-left:15px">
						<button id="btnStart" onclick="start()">开始(k)</button>
						<button id="btnStop" onclick="pause()">暂停(z)</button>
						<button id="btnRestart" onclick="restart()">重新开始(c)</button>
					</div>
					
					<div style="padding-left:5px">
							手机用户点击我进行控制<input type="text">
					</div>
				</div>
			</div>
		</div>
		
	</body>
	<script type="text/javascript">	
		// 初始化网格
		var rowMax = 19;
		var colMax = 10;
		var direction = 2; //初始运动方法  1:top  2:right  3:bottom 4:left
		var runTimer = null;
		
		// 初始化运动轨迹
		var moveGpsArr = new Array();
		var snakeGpsArr = new Array();
		// 蛇的长度
		var snakeLength = 0;
		
		var isStart = false;
		var isPause = true;
		
		// 游戏模式
		var gameMode = 1;
		$("#modeDiv input").each(function (index,obj){
			$(obj).click(function(){
				gameMode = $(this).val();
			})
		});
		
		// 绑定键盘事件
		$(document).keyup(function(event){
			var keyCode = event.keyCode;
			if (keyCode == 75){
				start();
			}else if (keyCode == 90){
				pause();
			}else if (keyCode == 67){
				restart();
			}
			else if (keyCode == 87 || keyCode == 38){
				up();
			}else if (keyCode == 83 || keyCode == 40){
				down();
			}else if (keyCode == 65 || keyCode == 37){
				left();
			}else if (keyCode == 68 || keyCode == 39){
				right();
			}
		});

		// 初始化面板,以及蛇的位置和方向
		initPanel();
		
		function initPanel(){
			moveGpsArr.push("12");
			moveGpsArr.push("13");
			moveGpsArr.push("14");
			snakeGpsArr.push("12");
			snakeGpsArr.push("13");
			snakeGpsArr.push("14");
			
			snakeLength = moveGpsArr.length;
			
			for (var r=0;r<rowMax;r++){
				for (var c=0;c<colMax;c++){
					var divId = r.toString()+c.toString();
					var divCon = "<div id='" +divId+ "' style='float:left;width:40px;height:30px;border:1px solid grey;'></div>";
					$("#borderDiv").append(divCon);
					
					//$("#"+divId).text(divId);
					if (r==0){
						$("#"+divId).addClass("topWallClass");
					}else if (r==rowMax-1){
						$("#"+divId).addClass("bottomWallClass");
					}
					
					if (c==0){
						$("#"+divId).addClass("leftWallClass");
					}else if (c==colMax-1){
						$("#"+divId).addClass("rightWallClass");
					}
	
					if (isInArray(moveGpsArr,divId)){
						if (direction == 1){
							// 向上
							if (isInArrayFirst(snakeGpsArr,divId)){
								$("#"+divId).addClass("snakeHeadClass");
							}else{
								$("#"+divId).addClass("snakeBodyClass");
						}
						}else if (direction == 2){
							// 向右
							if (isInArrayLast(snakeGpsArr,divId)){
								$("#"+divId).addClass("snakeHeadClass");
							}else{
								$("#"+divId).addClass("snakeBodyClass");
							}
						}else if (direction == 3){
							// 向下
							if (isInArrayLast(snakeGpsArr,divId)){
								$("#"+divId).addClass("snakeHeadClass");
							}else{
								$("#"+divId).addClass("snakeBodyClass");
							}
						}else{
							// 向左
							if (isInArrayFirst(snakeGpsArr,divId)){
								$("#"+divId).addClass("snakeHeadClass");
							}else{
								$("#"+divId).addClass("snakeBodyClass");
							}
						}
					}
				}
			}
			
			// 初始化食物
			initFood();
		}

		function initFood(){
			// 初始化食物
			var foodGps = null;
			while (true){
				var randomNo = randomNumber(0,rowMax*colMax-1);
				foodGps = randomNo.toString();
				if (randomNo < 10){
					foodGps = "0"+foodGps;
				}
				if (!$("#"+foodGps).hasClass("snakeHeadClass") && !$("#"+foodGps).hasClass("snakeBodyClass")){
					$("#"+foodGps).addClass("foodClass");
					break;
				}
			}
		}
		
		function isInArray(arr,val){
			for (var i=0;i<arr.length;i++){
				if (val == arr[i]){
					return true;
				}
			}
			
			return false;
		}
		
		function isInArrayFirst(arr,val){
			for (var i=0;i<arr.length;i++){
				if (i==0 && val == arr[i]){
					return true;
				}
			}
			
			return false;
		}
		
		function isInArrayLast(arr,val){
			for (var i=0;i<arr.length;i++){
				if (i==arr.length-1 && val == arr[i]){
					return true;
				}
			}
			
			return false;
		}
	
		function showColor(gps,type){
			if (type == '' || type == null || type == undefined || type=="body"){
				$("#" + gps).addClass("snakeBodyClass");
			}else{
				$("#" + gps).addClass("snakeHeadClass");
			}
		}
	
		// 获取蛇头位置
		function getSnakeHeadPosition(){
			var divArr = $("#borderDiv div");
			var headGps = null;
			for (var i = 0; i<divArr.length;i++){
				if ($(divArr[i]).hasClass("snakeHeadClass")){
					headGps = i;
					break;
				}
			}
			
			var headGpsStr = "";
			if (headGps != null){
				headGpsStr = headGps.toString();
				if (headGps<10){
					headGpsStr = "0"+headGpsStr;
					
				}
				console.log("position:"+headGpsStr);
			}else{
				alert("蛇头不见了");
				pause();
			}
			return headGpsStr;
		}
		
		// 判断是否撞墙了  1:top  2:right  3:bottom 4:left
		function isCloseWall(gps){
			if (direction == 1){
				if ($("#"+gps).hasClass("topWallClass")){
					return true;	
				}
			}else if (direction == 2){
				if ($("#"+gps).hasClass("rightWallClass")){
					return true;	
				}
			}else if (direction == 3){
				if ($("#"+gps).hasClass("bottomWallClass")){
					return true;	
				}
			}else{
				if ($("#"+gps).hasClass("leftWallClass")){
					return true;	
				}
			}
			
			return false;
		}
		
		//初始运动方法  1:top  2:right  3:bottom 4:left
		function left(){
			if (!isStart || isPause){
				return;
			}
			
			if (direction == 2){
				return;
			}
			direction = 4;
			
			// 判断当前蛇头是不是在最左边的墙
			var headGps = getSnakeHeadPosition();
			if (isCloseWall(headGps)){
				console.log("撞到了左边墙");
				if (null != runTimer){
					clearInterval(runTimer);
				}
				alert("撞墙啦");
				return;
			}
			
			var nextHeadGps = (parseInt(headGps)-1).toString();
			if (parseInt(nextHeadGps) < 10){
				nextHeadGps = "0"+nextHeadGps;
			}
			moveGpsArr.push(nextHeadGps);
			
			if ($("#"+nextHeadGps).hasClass("foodClass")){
				snakeLength++;
				$("#"+nextHeadGps).removeClass("foodClass");
				initFood();
			}else if ($("#"+nextHeadGps).hasClass("snakeBodyClass")){
				console.log("撞到了自己");
				if (null != runTimer){
					clearInterval(runTimer);
				}
				alert("撞到了自己,游戏结束");
				return
			}
			
			$("#borderDiv div").removeClass("snakeHeadClass").removeClass("snakeBodyClass");

			for (var j=moveGpsArr.length-1;j>=0;j--){
				if (j==moveGpsArr.length-1){
					// 蛇头
					showColor(moveGpsArr[j],"head");
				}
				else if (j>=moveGpsArr.length-snakeLength){
					showColor(moveGpsArr[j],"body");
				}else{
					$("#"+moveGpsArr[j]).removeClass("snakeHeadClass").removeClass("snakeBodyClass");
				}
			}
		}
		
		function right(){
			if (!isStart || isPause){
				return;
			}
			
			if (direction == 4){
				return;
			}
			direction = 2;
			
			// 判断当前蛇头是不是在最右边的墙
			var headGps = getSnakeHeadPosition();
			if (isCloseWall(headGps)){
				console.log("撞到了右边墙");
				if (null != runTimer){
					clearInterval(runTimer);
				}
				alert("撞墙啦");
				return;
			}

			var nextHeadGps = (parseInt(headGps)+1).toString();
			if (parseInt(nextHeadGps) < 10){
				nextHeadGps = "0"+nextHeadGps;
			}
			moveGpsArr.push(nextHeadGps);
			
			if ($("#"+nextHeadGps).hasClass("foodClass")){
				snakeLength++;
				$("#"+nextHeadGps).removeClass("foodClass");
				initFood();
			}else if ($("#"+nextHeadGps).hasClass("snakeBodyClass")){
				console.log("撞到了自己");
				if (null != runTimer){
					clearInterval(runTimer);
				}
				alert("撞到了自己,游戏结束");
				return
			}
			
			$("#borderDiv div").removeClass("snakeHeadClass").removeClass("snakeBodyClass");

			for (var j=moveGpsArr.length-1;j>=0;j--){
				if (j==moveGpsArr.length-1){
					// 蛇头
					showColor(moveGpsArr[j],"head");
				}
				else if (j>=moveGpsArr.length-snakeLength){
					showColor(moveGpsArr[j],"body");
				}else{
					$("#"+moveGpsArr[j]).removeClass("snakeHeadClass").removeClass("snakeBodyClass");
				}
			}
		}
		
		//初始运动方法  1:top  2:right  3:bottom 4:left
		function up(){
			if (!isStart || isPause){
				return;
			}
			
			if (direction == 3){
				return;
			}
			direction = 1;
			
			// 判断当前蛇头是不是在最上边的墙
			var headGps = getSnakeHeadPosition();

			if (isCloseWall(headGps)){
				console.log("撞到了上边墙");
				if (null != runTimer){
					clearInterval(runTimer);
				}
				alert("撞墙啦");
				return;
			}

			
			var nextHeadGps = (parseInt(headGps)-colMax).toString();
			if (parseInt(nextHeadGps) < 10){
				nextHeadGps = "0"+nextHeadGps;
			}
			moveGpsArr.push(nextHeadGps);
			
			if ($("#"+nextHeadGps).hasClass("foodClass")){
				snakeLength++;
				$("#"+nextHeadGps).removeClass("foodClass");
				initFood();
			}else if ($("#"+nextHeadGps).hasClass("snakeBodyClass")){
				console.log("撞到了自己");
				if (null != runTimer){
					clearInterval(runTimer);
				}
				alert("撞到了自己,游戏结束");
				return
			}
			
			$("#borderDiv div").removeClass("snakeHeadClass").removeClass("snakeBodyClass");
			
			for (var j=moveGpsArr.length-1;j>=0;j--){
				if (j==moveGpsArr.length-1){
					// 蛇头
					showColor(moveGpsArr[j],"head");
				}
				else if (j>=moveGpsArr.length-snakeLength){
					showColor(moveGpsArr[j],"body");
				}else{
					$("#"+moveGpsArr[j]).removeClass("snakeHeadClass").removeClass("snakeBodyClass");
				}
			}
		}
		
		//初始运动方法  1:top  2:right  3:bottom 4:left
		function down(){
			if (!isStart || isPause){
				return;
			}
			
			if (direction == 1){
				return;
			}
			direction = 3;
			
			// 判断当前蛇头是不是在最下边的墙
			var headGps = getSnakeHeadPosition();
			if (isCloseWall(headGps)){
				console.log("撞到了下边墙");
				if (null != runTimer){
					clearInterval(runTimer);
				}
				alert("撞墙啦");
				return;
			}

			var nextHeadGps = (parseInt(headGps)+colMax).toString();
			if (parseInt(nextHeadGps) < 10){
				nextHeadGps = "0"+nextHeadGps;
			}
			moveGpsArr.push(nextHeadGps);
			
			if ($("#"+nextHeadGps).hasClass("foodClass")){
				snakeLength++;
				$("#"+nextHeadGps).removeClass("foodClass");
				initFood();
			}else if ($("#"+nextHeadGps).hasClass("snakeBodyClass")){
				console.log("撞到了自己");
				if (null != runTimer){
					clearInterval(runTimer);
				}
				alert("撞到了自己,游戏结束");
				return
			}
			
			$("#borderDiv div").removeClass("snakeHeadClass").removeClass("snakeBodyClass");
			

			for (var j=moveGpsArr.length-1;j>=0;j--){
				if (j==moveGpsArr.length-1){
					// 蛇头
					showColor(moveGpsArr[j],"head");
				}
				else if (j>=moveGpsArr.length-snakeLength){
					showColor(moveGpsArr[j],"body");
				}else{
					$("#"+moveGpsArr[j]).removeClass("snakeHeadClass").removeClass("snakeBodyClass");
				}
			}
		}
		
		function randomNumber(start,end){
			var w = end-start-1;
			return Math.round(Math.random()*w+start+1);
		}
		
		function start(){
			if (runTimer == null){
				isStart = true;
				isPause = false;
				var speed=1000;
				// 游戏模式
				if (gameMode==1){
					speed = 1000;
				}else if (gameMode==2){
					speed = 500;
				}else if (gameMode==3){
					speed = 300;
				}
				runTimer = setInterval(function(){
					if (direction == 1){
						up();
					}else if (direction == 2){
						right();
					}else if (direction == 3){
						down();
					}else{
						left();
					}
				},speed);
			}
		}
		
		// 游戏暂停
		function pause(){
			if (null != runTimer){
				clearInterval(runTimer);
				runTimer = null;
				isPause = true;
			}
		}
		
		// 重新开始
		function restart(){
			window.location.href = window.location.href;
		}
	</script>
</html>
