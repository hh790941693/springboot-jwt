<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>

<style>
	#mainDiv td{
		width:30px;
		height:30px;
	}
</style>
</head>
<body>
	<div id="mainDiv" style="width:560px;height:530px;margin:55px auto;">
		<div id="activeDiv" style="width:390px;height:510px;float:left;">
			<table id="activeBord" style="width:380px;height:500px;" cellpadding="0" cellspacing="0" border="1">
				
			</table>
		</div>
		
		<div id="nextDiv" style="width:160px;height:260px;float:right;">
			<table id="previewBord" style="width:150px;height:90px;" cellpadding="0" cellspacing="0" border="1">
				
			</table>
			
			<br/>
			<div id="buttonDiv">
				<button id="startBtn" onclick="start()">开始</button>&nbsp;&nbsp;&nbsp;&nbsp;
				<button id="pauseBtn" onclick="pause()">暂停</button><br/><br/>
				分数:<span id="score">0</span><br/>
				级别:<span id="level">1</span>
				<br/>
				<input id="keybordController" value="手机用户点击我进行控制" style="width:150px;">
				
				<div id="shuoming" style="font-size:15px;">
					<p>变形: w,l,方向键上</p>
					<p>向左: a,方向键左</p>
					<p>向右: d,方向键右</p>
					<p>向下: s,方向键下</p>
				</div>
			</div>
		</div>
	</div>
</body>

	<script type="text/javascript">
		var ROW = 16;
		var COL = 12;
		
		var activeTable = null;
		var previewTable = null;
		
		var activeBlock = null;
		var previewBlock = null;
		var nextBlock = null;
		
		var score = 0;
		var level = 1;
		
		var board = null;
		var timer = null;
		
		var activeBlockColor = "red";
		var staticBlockColor = "blue";
		var nextBlockColor = "green";

		// 初始化内存面板
		board = new Array(ROW);
		for (var i=0;i<ROW;i++){
			board[i] = new Array(COL);
		}
		for (var r=0;r<ROW;r++){
			for (var c=0;c<COL;c++){
				board[r][c] = 0;
			}
		}
		
		// 分数和等级对应关系
		var scoreArr = new Array(5);
		scoreArr[0] = {min:0,max:300,speed:1300,level:1};
		scoreArr[1] = {min:301,max:750,speed:1000,level:2};
		scoreArr[2] = {min:751,max:1300,speed:800,level:3};
		scoreArr[3] = {min:1301,max:2050,speed:500,level:4};
		scoreArr[4] = {min:2051,max:3300,speed:300,level:5};
		scoreArr[5] = {min:3301,max:6900,speed:200,level:6};
		scoreArr[6] = {min:6901,max:99999,speed:100,level:7};
		
		var deleteLineScoreArr = new Array(4);
		deleteLineScoreArr[0] = 10;
		deleteLineScoreArr[1] = 25;
		deleteLineScoreArr[2] = 40;
		deleteLineScoreArr[3] = 90;
		
		initBord();
		
		// 开始游戏
		function start(){
			// 绑定键盘事件
			$(document).keydown(function(event){
				var keyCode = event.keyCode;
				console.log(keyCode);
				if (keyCode == 75){
					start();//k
				}else if (keyCode == 90){
					pause();//z
				}else if (keyCode == 83 || keyCode == 40){
					moveDown();//s
				}else if (keyCode == 65 || keyCode == 37){
					moveLeft();//a
				}else if (keyCode == 68 || keyCode == 39){
					moveRight();//d
				}else if (keyCode == 87 || keyCode == 38 || keyCode == 76){
					rotate(); //变形 w l
				}
			});
			clearInputText();
			
			activeTable = document.getElementById("activeBord");
			previewTable = document.getElementById("previewBord");
			
			activeBlock = generateBlock();
			nextBlock = generateBlock();
			paintActiveBlock();
			paintNextBlock();
			
			timer = setInterval(function(){
				moveDown();
			},1000);
		}
		
		function pause(){
			if (timer != null){
				clearInterval(timer);
			}
		}
		
		function moveDown(){
			
			// 检查底边界
			if (checkBottomBorder()){
				// 碰底
				// 停止向下运动
				clearInterval(timer);
				
				// 更新board
				updateBoard();
				
				// 消行
				var lines = deleteLine();
				
				if (lines != 0){
					// 统计分数
					score = score + deleteLineScoreArr[lines-1];
					// 更新分数
					$("#score").text(score);
					
					// 擦除活动面板
					//earseActiveBord();
					
					// 绘制活动面板
					//paintActiveBord();
				}
				earseActiveBord();
				paintActiveBord();
				
				earseNextBlock();
				
	            //产生一个新图形并判断是否可以放在最初的位置.     
	            if(!validateBlock(nextBlock)){    
	                alert("游戏结束!");    
	                return;     
	            };
				
				activeBlock = nextBlock;
				nextBlock = generateBlock();
				paintActiveBlock();
				paintNextBlock();
				
				var levelInfo = getLevelInfo(score);
				$("#level").text(levelInfo.level);
				
				timer = setInterval(function(){
					moveDown();
				},levelInfo.speed);
				
			}else{
				// 没有碰底
				// 擦除当前活动图形方块
				earseActiveBlock();
				// 更新当前活动方块坐标
				for (var i=0;i<activeBlock.length;i++){
					activeBlock[i].x = activeBlock[i].x+1;
				}
				// 重新绘制活动方块
				paintActiveBlock();
			}
		}
		
		function moveLeft(){
			if (checkLeftBorder()){
				return;
			}
			// 擦除当前活动图形方块
			earseActiveBlock();
			// 更新当前活动方块坐标
			for (var i=0;i<activeBlock.length;i++){
				activeBlock[i].y = activeBlock[i].y-1;
			}
			// 重新绘制活动方块
			paintActiveBlock();
		}
		
		function moveRight(){
			if (checkRightBorder()){
				return;
			}
			
			// 擦除当前活动图形方块
			earseActiveBlock();
			// 更新当前活动方块坐标
			for (var i=0;i<activeBlock.length;i++){
				activeBlock[i].y = activeBlock[i].y+1;
			}
			// 重新绘制活动方块
			paintActiveBlock();
		}
		
		function rotate(){
			var tmpBlock = copyBlock(activeBlock);

			var cx = Math.round((tmpBlock[0].x+tmpBlock[1].x+tmpBlock[2].x+tmpBlock[3].x)/4);
			var cy = Math.round((tmpBlock[0].y+tmpBlock[1].y+tmpBlock[2].y+tmpBlock[3].y)/4);
			
			for (var i=0;i<tmpBlock.length;i++){
				tmpBlock[i].x = cx+cy-activeBlock[i].y;
				tmpBlock[i].y = cy-cx+activeBlock[i].x;
			}
			
			for (var i=0;i<tmpBlock.length;i++){
				if (!isCellValid(tmpBlock[i].x,tmpBlock[i].y)){
					return;
				}
			}
			
			// 如果合法
			earseActiveBlock();
			activeBlock = copyBlock(tmpBlock);
			paintActiveBlock();
			
		}
		
		// 检查底边界,如果底边界撞墙返回false,如果底边界碰到board也返回false
		// true:碰底   false:没有碰底
		function checkBottomBorder(){
			for (var i=0;i<activeBlock.length;i++){
				if (activeBlock[i].x == ROW-1){
					return true;
				}
				
				if (!isCellValid(activeBlock[i].x+1,activeBlock[i].y)){
					return true;
				}
			}
			
			return false;
		}
		
		// 检查左边界
		function checkLeftBorder(){
			for (var i=0;i<activeBlock.length;i++){
				if (activeBlock[i].y == 0){
					return true;
				}
				
				if (!isCellValid(activeBlock[i].x,activeBlock[i].y-1)){
					return true;
				}
			}
			
			return false;
		}
		
		// 检查右边界
		function checkRightBorder(){
			for (var i=0;i<activeBlock.length;i++){
				if (activeBlock[i].y == COL-1){
					return true;
				}
				
				if (!isCellValid(activeBlock[i].x,activeBlock[i].y+1)){
					return true;
				}
			}
			
			return false;
		}
		
        function validateBlock(block){    
        	if(!block){      
                return false;    
            }    
          	for(var i=0; i<4; i++){     
          		if(!isCellValid(block[i].x, block[i].y)){    
                	return false;     
            	}     
          	}
          
          return true;    
        } 
		
		// 检查坐标(x,y)是否在board中存在,存在说明这个方法不能继续移动,或者不能变形
		// true:合法   false:不合法
		function isCellValid(x, y){
			if (x<0 || x>ROW-1 || y<0 || y>COL-1){
				return false;
			}
			
			if (board[x][y] == 1){
				return false;
			}
			
			return true;
		}
		
		function copyBlock(old){
			var newBlock = new Array(4);
			for (var i=0;i<4;i++){
				newBlock[i] = {x:0,y:0};
			}
			
			for (var i=0;i<4;i++){
				newBlock[i].x = old[i].x;
				newBlock[i].y = old[i].y;
			}
			
			return newBlock;
		}
		
		function earseActiveBord(){
			for (var r=0;r<ROW;r++){
				for (c=0;c<COL;c++){
					activeTable.rows[r].cells[c].style.backgroundColor = "white";
				}
			}
		}
		
		function paintActiveBord(){
			for (var r=0;r<board.length;r++){
				for (var c=0;c<board[r].length;c++){
					if (board[r][c] == 1){
						activeTable.rows[r].cells[c].style.backgroundColor = staticBlockColor;
					}
				}
			}
		}
		
		function updateBoard(){
			for (var i=0;i<activeBlock.length;i++){
				board[activeBlock[i].x][activeBlock[i].y] = 1;
			}
		}
		
		// 消行
		function deleteLine(){
			var lines = 0;
			for (var r=0;r<ROW;r++){
				var c=0;
				for (;c<COL;c++){
					if (board[r][c] == 0){
						break;
					}
				}
				if (c == COL){
					lines++;
					if (r!=0){
						for (var k=r-1;k>=0;k--){
							board[k+1] = board[k];
						}
					}
					board[0] = generateBlankLine();
				}
			}
			
			return lines;
		}
		
		function generateBlankLine(){
			var blankLine = new Array();
			for (var i=0;i<COL;i++){
				blankLine[i] = 0;
			}
			
			return blankLine;
		}
		
		// 绘制活动方块
		function paintActiveBlock(){
			for (var i=0;i<activeBlock.length;i++){
				activeTable.rows[activeBlock[i].x].cells[activeBlock[i].y].style.backgroundColor = activeBlockColor;
			}
		}
		
		// 绘制下一块方块
		function paintNextBlock(){
			for (var i=0;i<nextBlock.length;i++){
				previewTable.rows[nextBlock[i].x].cells[nextBlock[i].y-4].style.backgroundColor = nextBlockColor;
			}
		}
		
		
		// 擦除活动方块
		function earseActiveBlock(){
			for (var i=0;i<activeBlock.length;i++){
				activeTable.rows[activeBlock[i].x].cells[activeBlock[i].y].style.backgroundColor = "white";
			}
		}
		
		// 擦除下一块方块
		function earseNextBlock(){
			for (var i=0;i<nextBlock.length;i++){
				previewTable.rows[nextBlock[i].x].cells[nextBlock[i].y-4].style.backgroundColor = "white";
			}
		}
		
		// 获取级别和速度
		function getLevelInfo(score){
			for (var i=0;i<scoreArr.length;i++){
				if (score>=scoreArr[i].min && score<=scoreArr[i].max){
					return scoreArr[i];
				}
			}
		}
		
		// 随机生成一块方块
		function generateBlock(){
			var block = new Array(4);
			var	typeIndex = randomNumber(0,6);
			console.log(typeIndex);
			
			switch(typeIndex){
				case 0:{
					//   ■ ■ 
					//   ■ ■  
					block[0] = {x:0,y:5};
					block[1] = {x:1,y:5};
					block[2] = {x:0,y:6};
					block[3] = {x:1,y:6};
					break;
				}
				case 1:{
					//   ■ ■ ■ ■
					block[0] = {x:0,y:4};
					block[1] = {x:0,y:5};
					block[2] = {x:0,y:6};
					block[3] = {x:0,y:7};					
					break;
				}
				case 2:{
					//    ■ 
					//  ■ ■
					//  ■
					block[0] = {x:0,y:6};
					block[1] = {x:1,y:5};
					block[2] = {x:1,y:6};
					block[3] = {x:2,y:5};					
					break;
				}
				case 3:{ 
					//    ■
					//    ■ ■
					//      ■
					block[0] = {x:0,y:5};
					block[1] = {x:1,y:5};
					block[2] = {x:1,y:6};
					block[3] = {x:2,y:6};					
					break;
				}
				case 4:{
					//    ■ 
					//    ■ ■ ■ 
					block[0] = {x:0,y:5};
					block[1] = {x:1,y:5};
					block[2] = {x:1,y:6};
					block[3] = {x:1,y:7};					
					break;
				}
				case 5:{
					//    ■    
					//    ■
					//    ■ ■
					block[0] = {x:0,y:5};
					block[1] = {x:1,y:5};
					block[2] = {x:2,y:5};
					block[3] = {x:2,y:6};					
					break;
				}
				case 6:{
					//       ■  
					//     ■ ■ ■    
					block[0] = {x:0,y:5};
					block[1] = {x:1,y:4};
					block[2] = {x:1,y:5};
					block[3] = {x:1,y:6};					
					break;
				}				
			}
			
			return block;
		}
		
		
		function initBord(){
			var strActiveHtml="";
			for (var r=0;r<ROW;r++){
				strActiveHtml += "<tr>"
				for (var c=0;c<COL;c++){
					strActiveHtml += "<td></td>";
				}
				strActiveHtml += "</tr>";
			}
			
			$("#activeBord").html(strActiveHtml);
			
			var strPreviewHtml = "";
			for (var r=0;r<3;r++){
				strPreviewHtml += "<tr>"
				for (var c=0;c<5;c++){
					strPreviewHtml += "<td></td>";
				}
				strPreviewHtml += "</tr>";
			}
			
			$("#previewBord").html(strPreviewHtml);
		}
		
		// 定时清理输入框内容
		function clearInputText(){
			clearInputTimer = setInterval(function(){
				$("#keybordController").val("");
			},5000);
		}
		
	//----------------------------公共函数区------------------------------------
	// 随机获取指定范围内的数字  闭区间
	function randomNumber(start,end){
		var range = end-start;
		var rand = Math.random();
		var num = start + Math.round(rand*range);
		
		return parseInt(num);
	}
	</script>
</html>