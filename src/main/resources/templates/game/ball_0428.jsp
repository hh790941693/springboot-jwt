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
		<script type="text/javascript" src="<%=path%>/static/viewjs/ball.js"></script>
		
		<style type="text/css">

			.ballClass{
				background-color:red;
			}
			.woodClass{
				background-color:green;
			}
			
			.enemyWall{
				background-color:blue;
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
		<div id="gameMainDiv" style="width:760px;height:590px;border:2px solid green;margin:5px auto;padding:10px;">
			<div id="borderDiv" style="width:465px;height:590px;border:1px solid grey;float:left">
				
			</div>
			<div id="controlDiv" style="width:250px;height:590px;border:2px solid grey;float:right">
				<div id="buttonDiv">
					<div id="modeDiv" style="padding-left:30px">
						<input type="radio" value="1" name="mode" checked="checked">简单
						<input type="radio" value="2" name="mode">中等
						<input type="radio" value="3" name="mode">困难
					</div>
					<br/>
					<div id="mapDiv" style="padding-left:15px;">
						地图:<select id="selectMap" style="width:150px;">
							<option value="1" selected="selected">随机</option>
							<option value="2">实体型</option>
							<option value="3">回字型</option>
							<option value="4">钻石型</option>
							<option value="5">竖状型</option>
							<option value="6">横状型</option>
							<option value="7">Y字型</option>
							<option value="8">爱心型</option>
							<option value="9">五角星</option>
							<option value="10">空心型</option>
						</select>
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
					<div style="padding-left:10px">
						<button id="btnShoot" onclick="shoot()">发射(space)</button>
						
						<button id="btnKaigua" onclick="kaigua()">开挂(g)</button>
					</div>
					
					<br/>
					<div style="padding-left:15px">
						<button id="btnStart" onclick="start()">开始(k)</button>
						<button id="btnStop" onclick="pause()">暂停(z)</button>
						<button id="btnRestart" onclick="restart()">重新开始(c)</button>
					</div>
					
					<br/>
					<div style="padding-left:5px">
							手机用户点击我进行控制<input id="inputText" type="text">
					</div>
				</div>
			</div>
		</div>
		
	</body>
	<script type="text/javascript">	
		// 初始化网格
		var ROW_MAX = 18;
		var COL_MAX = 11;
		
		var TOP = 1;
		var RIGHT = 2;
		var BOTTOM = 3;
		var LEFT = 4;
		
		// 球的运动方向  初始化时为右上
		var X_DIRECTION = RIGHT;
		var Y_DIRECTION = TOP;
		
		// 木板的运动方向
		var wood_direction = RIGHT;

		// 是否开始
		var isStart = null;
		// 是否暂停
		var isPause = null;  // null为  false  true
		
		// 障碍物定时器
		var enemyWallTimer = null;
		// 球运动定时器
		var ballRunTimer = null;
		// 挂机定时器
		var kaiguaTimer = null;
		// 定时生成障碍物的定时器
		var generateEnemyWallTimer = null;
		// 定时清理输入框雷人
		var clearInputTimer = null;
		
		// 球运动速度
		var LEVEL_ONE = 1;
		var LEVEL_TWO = 2;
		var LEVEL_THREE = 3;
		
		// 游戏模式
		var gameMode = LEVEL_ONE;
		$("#modeDiv input").each(function (index,obj){
			$(obj).click(function(){
				var modeStr  = $(this).val();
				gameMode = parseInt(modeStr);
			})
		});
		
		//var mapId = 1;
		$("#selectMap").change(function(){
			initEnemyWall(8)
		});
		
		// 绑定键盘事件
		$(document).keydown(function(event){
			var keyCode = event.keyCode;
			//console.log(keyCode);
			if (keyCode == 75){
				start();//k
			}else if (keyCode == 90){
				pause();//z
			}else if (keyCode == 67){
				restart();//c
			}else if (keyCode == 71){
				kaigua();//g
			}else if (keyCode == 87 || keyCode == 38){
				up();//w
			}else if (keyCode == 83 || keyCode == 40){
				down();//s
			}else if (keyCode == 65 || keyCode == 37){
				left();//a
			}else if (keyCode == 68 || keyCode == 39){
				right();//d
			}
		});
		
		// 初始化面板  包括球,木板,障碍物
		initPanel();
		
		function initPanel()
		{
			for (var k=1;k<=(ROW_MAX*COL_MAX);k++)
			{
				var divId = k.toString();
				var divCon = "<div id='" +divId+ "' style='float:left;width:40px;height:30px;border:1px solid grey;'></div>";
				$("#borderDiv").append(divCon);
				//$("#"+divId).text(divId);
				
				// 如果是第一行
				if (k>=1 && k<=COL_MAX){
					// 第一行
					$("#"+divId).addClass("topWallClass");
				}else if (k>=COL_MAX*ROW_MAX-COL_MAX+1 && k<=COL_MAX*ROW_MAX){
					// 最后一行
					$("#"+divId).addClass("bottomWallClass");
				}
				
				if (k%COL_MAX==1){
					// 第一列
					$("#"+divId).addClass("leftWallClass");
				}else if (k%COL_MAX==0){
					// 最后一列
					$("#"+divId).addClass("rightWallClass");
				}
			}
			// 初始化球和木板的位置 参数为球的位置
			repaitBall(182); //球的位置
			repaitWood(193); //木板中间位置
			initEnemyWall(8); //初始化障碍物
		}

		// 初始化障碍物
		function initEnemyWall(totalRowNo){
			var mapId = $("#selectMap").val();
			$("#borderDiv div").removeClass("enemyWall");
			if (parseInt(mapId) == 1){
				for (var i=1;i<=totalRowNo;i++){
					//随机生成改行的障碍物
					var enemyPerRow = randomNumber(COL_MAX/2+3,COL_MAX);
					for (var j=1;j<=enemyPerRow;j++){
						var divIdTmp = randomNumber(1,COL_MAX);
						//console.log(divIdTmp);
						var divIdIndex = (i-1)*COL_MAX + divIdTmp;
						if ($("#"+divIdIndex).hasClass("enemyWall")){
							j--;
						}else{
							$("#"+divIdIndex).addClass("enemyWall");
						}
					}
				}
			}else{
				var mapArr = showBallMap(parseInt(mapId));
				for (var r=0;r<mapArr.length;r++){
					var tmpRowArr = mapArr[r];
					for (c=0;c<tmpRowArr.length;c++){
						if (mapArr[r][c] == 1){
							var divIdIndex = ((r+1)-1)*tmpRowArr.length+(c+1);
							$("#"+divIdIndex).addClass("enemyWall");
						}
					}
				}
			}
		}
		
		function generateEnemyWall(){
			if (generateEnemyWallTimer == null){
				var maxWallRowNo = 8;
				generateEnemyWallTimer = setInterval(function(){
					var row8start = (maxWallRowNo-1)*COL_MAX+1;
					var row8end = COL_MAX*maxWallRowNo;
					
					var isHasEmemy = false;
					for (i=row8start;i<=row8end;i++){
						if ($("#"+i).hasClass("enemyWall")){
							isHasEmemy = true;
							break;
						}
					}
					
					if (!isHasEmemy){
						for (j=COL_MAX*maxWallRowNo;j>=1;j--){
							if ($("#"+j).hasClass("enemyWall")){
								var nextGps = j+COL_MAX;
								$("#"+nextGps).addClass("enemyWall");
								$("#"+j).removeClass("enemyWall");
							}
							
						}
						
						//随机生成炸弹数
						var enemyRowNo = randomNumber(COL_MAX/2+1,COL_MAX);
						while (enemyRowNo>0){
							var divIdIndex = randomNumber(0,COL_MAX);
							if ($("#"+divIdIndex).hasClass("enemyWall")){
								continue;	
							}else{
								$("#"+divIdIndex).addClass("enemyWall");
								enemyRowNo--;
							}
						}
					}
				},1000);
			}
		}
		
		// 重新绘制木板
		function repaitWood(gps){
			var woodBodyIndex1 = gps;
			var woodBodyIndex2 = woodBodyIndex1+1;
			var woodBodyIndex3 = woodBodyIndex1-1;
			
			$("#borderDiv div").removeClass("woodClass");
			$("#"+woodBodyIndex1).addClass("woodClass");
			$("#"+woodBodyIndex2).addClass("woodClass");
			$("#"+woodBodyIndex3).addClass("woodClass");
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
	
		// 获取指定行的第一列的序号
		function getRowStartNo(row){
			return COL_MAX*(row-1)+1;
		}
		
		// 获取指定行的最后一列序号
		function getRowEndNo(row){
			return row*COL_MAX;
		}
		
		// 获取指定列的第一行的序号
		function getColumnStartNo(col){
			return col % COL_MAX;
		}
		
		// 获取指定列的最后一行的序号
		function getColumnEndNo(col){
			return COL_MAX*(ROW_MAX-1)+col;
		}

		// 获取当前球的位置
		function getBallPosition(){
			//var headDiv = $("#borderDiv div[class='snakeHeadClass']");
			var divObjs = $("#borderDiv div");
			for (var i=0;i<divObjs.length;i++){
				if ($(divObjs[i]).hasClass("ballClass")){
					var headId = $(divObjs[i]).attr("id");
					return parseInt(headId);
				}
			}
		}

		// 重新绘制球
		function repaitBall(headGps){
			$("#borderDiv div").removeClass("ballClass");
			$("#"+headGps).addClass("ballClass");
		}
		
		// 获取木头指定方位的位置    
		function getWoodPosition(posType){
			var pos = null;
			if (posType == LEFT){
				for (var i=1;i<=ROW_MAX*COL_MAX;i++){
					if ($("#"+i).hasClass("woodClass")){
						pos = i;
						break;
					}
				}
			}else if (posType == RIGHT){
				for (var i=ROW_MAX*COL_MAX;i>=1;i--){
					if ($("#"+i).hasClass("woodClass")){
						pos = i;
						break;
					}
				}
			}
			
			return pos;
		}
		
		// 判断木板是否撞墙了 
		function isCloseWall(gps){
			var gpsInt = parseInt(gps);
			if (direction == TOP){
				if (gpsInt>=1 && gpsInt<=COL_MAX){
					return true;
				}
			}else if (direction == RIGHT){
				if (gpsInt %COL_MAX == 0){
					return true;
				}
			}else if (direction == BOTTOM){
				var startNo = getRowStartNo(ROW_MAX);
				var endNo = getRowEndNo(ROW_MAX);
				if (gpsInt>=startNo && gpsInt<=endNo){
					return true;
				}
			}else{
				if (gpsInt % COL_MAX == 1){
					return true;
				}
			}
			
			return false;
		}
		
		// 向左移动一步
		function left(){
			
			direction = LEFT;
			// 如果暂停了 不能移动
			if (isPause){
				return;
			}
			
			// 获取木头最左侧的id
			var woodLeftId = getWoodPosition(LEFT);
		
			if (isCloseWall(woodLeftId)){
				console.log("撞到了左边墙");
				return;
			}
			
			repaitWood(woodLeftId);
			if (isStart == null){
				// 如果游戏启动了,则只动木板class
				var ballPos = getBallPosition();
				repaitBall(ballPos-1);
			}
		}
		
		// 向右移动一步
		function right(){
			direction = RIGHT;
			// 如果暂停了 不能移动
			if (isPause){
				return;
			}
			
			// 获取木头最左侧的id
			var woodRightId = getWoodPosition(RIGHT);
		
			if (isCloseWall(woodRightId)){
				console.log("撞到了右边墙");
				return;
			}
			
			repaitWood(woodRightId);
			if (isStart == null){
				// 如果游戏启动了,则只动木板class
				var ballPos = getBallPosition();
				repaitBall(ballPos+1);
			}
		}
		
		// 向上移动一步
		function up(){
			direction = TOP;
			// 如果暂停了 不能移动
			if (isPause){
				return;
			}
			
			// 获取木头最左侧的id
			var woodRightId = getWoodPosition(RIGHT);
		
			if (isCloseWall(woodRightId)){
				console.log("撞到了上边墙");
				return;
			}
			
			repaitWood(woodRightId-1-COL_MAX);
			if (isStart == null){
				// 如果没有启动,则只动木板class
				var ballPos = getBallPosition();
				repaitBall(ballPos-COL_MAX);
			}
		}
		
		// 向下移动一步
		function down(){
			direction = BOTTOM;
			// 如果暂停了 不能移动
			if (isPause){
				return;
			}
			
			// 获取木头最左侧的id
			var woodRightId = getWoodPosition(RIGHT);
		
			if (isCloseWall(woodRightId)){
				console.log("撞到了下边墙");
				return;
			}
			
			repaitWood(woodRightId-1+COL_MAX);
			if (isStart == null){
				// 如果没有启动,则只动木板class
				var ballPos = getBallPosition();
				repaitBall(ballPos+COL_MAX);
			}
		}
		
		// 随机获取指定范围内的数字  闭区间
		function randomNumber(start,end){
			var range = end-start;
			var rand = Math.random();
			var num = start + Math.round(rand*range);
			
			return num;
		}

		
		// 消灭指定的墙,参数为墙的位置,多个参数以逗号分隔
		function clearEnemyWall(){
			for (var i=0;i<arguments.length;i++){
				var enemyDivId = arguments[i];
				$("#"+enemyDivId).removeClass("enemyWall");
			}
		}
		
		// 判断给定的位置是否都有墙,如果都有则返回true, 参数为墙的位置,多个参数以逗号分隔
		function isHasEnemyWall(){
			var res = true;
			for (var i=0;i<arguments.length;i++){
				var enemyDivId = arguments[i];
				if (!$("#"+enemyDivId).hasClass("enemyWall")){
					res = false;
					break;
				}
			}
			
			return res;
		}
		
		// 返回球下一步的位置
		// ballGps:当前球的位置   xDirection:x轴方向    yDirection:y轴方向
		function ballDirection(ballGps, xDirection, yDirection){
			if (xDirection == LEFT && yDirection == TOP){
				// 朝左上运动
				X_DIRECTION = LEFT;
				Y_DIRECTION = TOP;
				
				nextGps=parseInt(ballGps)-1-COL_MAX;
			}else if (xDirection == RIGHT && yDirection == TOP){
				// 朝右上运动
				X_DIRECTION = RIGHT;
				Y_DIRECTION = TOP;
				
				nextGps=parseInt(ballGps)+1-COL_MAX;
			}else if (xDirection == LEFT && yDirection == BOTTOM){
				// 朝左下运动
				X_DIRECTION = LEFT;
				Y_DIRECTION = BOTTOM;
				
				nextGps=parseInt(ballGps)-1+COL_MAX;
			}else if (xDirection == RIGHT && yDirection == BOTTOM){
				// 朝右下运动
				X_DIRECTION = RIGHT;
				Y_DIRECTION = BOTTOM;
				
				nextGps=parseInt(ballGps)+1+COL_MAX;
			}
			
			return nextGps;
		}

		// 球运动定时器
		function start(){
			isStart = true;
			isPause = false;
			
			if (null != ballRunTimer){
				return;
			}
			
			clearInputText();
			
			generateEnemyWall();
			
			var speed=300;
			if (gameMode == LEVEL_ONE){
				speed = 300;
			}else if (gameMode == LEVEL_TWO){
				speed = 150;
			}else if (gameMode == LEVEL_THREE){
				speed = 50;
			}
			ballRunTimer = setInterval(function(){
				//当前球的位置
				var ballGps = getBallPosition();
				
				// 当前球的上下左右位置
				var ballLeftGps = ballGps -1;
				var ballRightGps = ballGps + 1;
				var ballTopGps = ballGps - COL_MAX;
				var ballBottomGps = ballGps + COL_MAX;
				
				// 当前球的右上角,左上角,左下角,右下角位置
				var ballRTGps = ballGps + 1 - COL_MAX;
				var ballLTGps = ballGps - 1 - COL_MAX;
				var ballLBGps = ballGps - 1 + COL_MAX;
				var ballRBGps = ballGps + 1 + COL_MAX;
				
				// 当前木板的最左侧和最右侧位置
				var leftWoodGps = getWoodPosition(LEFT);
				var rightWoodGps = getWoodPosition(RIGHT);
				
				//球的下一步位置
				var nextGps = null;
				
				if (X_DIRECTION == RIGHT && Y_DIRECTION == TOP){
					// 右上
					if ($("#"+ballGps).hasClass("rightWallClass") && $("#"+ballGps).hasClass("topWallClass")){
						// 碰到了右上角落位置,下一步位置为 左下
						nextGps = ballDirection(ballGps, LEFT, BOTTOM);
						console.log("碰到了右上角落");
					}else{
						if ($("#"+ballGps).hasClass("topWallClass")){
							// 幢上了上边墙,下一步位置为右下
							nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							
							// 判断是否撞到了木头  有三种情况  最左侧木头2种情况   其他木头一种情况
							if (leftWoodGps== ballGps+1+COL_MAX){
								// 如果当前球在左侧木头的左上角位置    左上运动
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (leftWoodGps== ballGps+1){
								// 如果当前球在左侧木头的左边     左下运动
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (rightWoodGps == ballGps+COL_MAX){
								// 如果当前球在右侧木头的上边    右上运动
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if ($("#"+nextGps).hasClass("woodClass")){
								// 如果球在木头的其他位置上边   右下运动
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}
							
							// --------------------消灭障碍物  判断右,右上角--------------------
							if (isHasEnemyWall(ballRightGps,ballRBGps)){
								clearEnemyWall(ballRightGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (isHasEnemyWall(ballRightGps)){
								clearEnemyWall(ballRightGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (isHasEnemyWall(ballRBGps)){
								clearEnemyWall(ballRBGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}

						}else if ($("#"+ballGps).hasClass("rightWallClass")){
							// 幢上了右边墙,下一步位置为左上
							nextGps = ballDirection(ballGps, LEFT, TOP);
							
							// 如果下一步有木头,则下一步位置为左下
							if ($("#"+nextGps).hasClass("woodClass")){
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}
							
							// --------------------消灭障碍物  判断上,左上角--------------------
							if (isHasEnemyWall(ballTopGps,ballLTGps)){
								clearEnemyWall(ballTopGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (isHasEnemyWall(ballTopGps)){
								clearEnemyWall(ballTopGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (isHasEnemyWall(ballLTGps)){
								clearEnemyWall(ballLTGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}
						}else{
							// 如果上边和右边墙都没撞到,则方向暂时保持不变
							nextGps = ballDirection(ballGps, RIGHT, TOP);

							// 判断是否撞到了木头  有三种情况  最左侧木头2种情况   其他木头一种情况
							if (leftWoodGps== ballGps+1-COL_MAX){
								// 如果当前球在左侧木头的左下角位置    左下运动
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (leftWoodGps== ballGps+1){
								// 如果当前球在左侧木头的左边     左上运动
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (rightWoodGps== ballGps-COL_MAX){
								// 如果当前球在右侧木头的下边    右下运动
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if ($("#"+nextGps).hasClass("woodClass")){
								// 如果球在木头的其他位置下边   右下运动
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}
							
							// --------------------消灭障碍物  判断上,右上角,右--------------------
							if (isHasEnemyWall(ballTopGps,ballRTGps,ballRightGps)){
								clearEnemyWall(ballTopGps,ballRightGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (isHasEnemyWall(ballTopGps,ballRTGps)){
								clearEnemyWall(ballTopGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if (isHasEnemyWall(ballTopGps,ballRightGps)){
								clearEnemyWall(ballTopGps,ballRightGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (isHasEnemyWall(ballRTGps,ballRightGps)){
								clearEnemyWall(ballRightGps);
								
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (isHasEnemyWall(ballTopGps)){
								clearEnemyWall(ballTopGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if (isHasEnemyWall(ballRTGps)){
								clearEnemyWall(ballRTGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (isHasEnemyWall(ballRightGps)){
								clearEnemyWall(ballRightGps);
								
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}
						}
					}
				}else if (X_DIRECTION == RIGHT && Y_DIRECTION==BOTTOM){
					// 右下
					if ($("#"+ballGps).hasClass("bottomWallClass") && $("#"+ballGps).hasClass("rightWallClass")){
						console.log("碰到了右下角落,游戏结束");
						nextGps = ballDirection(ballGps, LEFT, TOP);
					}else{
						if ($("#"+ballGps).hasClass("rightWallClass")){
							// 如果碰到右边墙, 下一步位置为 左下
							nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							
							//如果下一步有木头   下一步位置为左上
							if ($("#"+nextGps).hasClass("woodClass")){
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}
							
							// --------------------消灭障碍物  判断下,左下角--------------------
							if (isHasEnemyWall(ballBottomGps,ballLBGps)){
								clearEnemyWall(ballBottomGps);
								
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (isHasEnemyWall(ballBottomGps)){
								clearEnemyWall(ballBottomGps);
								
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (isHasEnemyWall(ballLBGps)){
								clearEnemyWall(ballLBGps);
								
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}
							
							
						}else if ($("#"+ballGps).hasClass("bottomWallClass")){
							console.log("撞到了底部墙,游戏结束");
							nextGps = ballDirection(ballGps, RIGHT, TOP);
						}else{
							nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							
							// 判断是否撞到了木头  有三种情况  最左侧木头2种情况   其他木头一种情况
							if (leftWoodGps== ballGps+1+COL_MAX){
								// 如果当前球在左侧木头的左上角位置    左上运动
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (leftWoodGps== ballGps+1){
								// 如果当前球在左侧木头的左边     左下运动
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (rightWoodGps == ballGps+COL_MAX){
								// 如果当前球在右侧木头的上边    右上运动
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if ($("#"+nextGps).hasClass("woodClass")){
								// 如果球在木头的其他位置上边   右下运动
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}
							
							
							// --------------------消灭障碍物  判断下,右下角,右--------------------
							if (isHasEnemyWall(ballBottomGps,ballRBGps,ballRightGps)){
								clearEnemyWall(ballBottomGps,ballRightGps);
								
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (isHasEnemyWall(ballBottomGps,ballRBGps)){
								clearEnemyWall(ballBottomGps);
								
								nextGps = ballDirection(ballGps, RIGHT, TOP);
								
							}else if (isHasEnemyWall(ballBottomGps,ballRightGps)){
								clearEnemyWall(ballBottomGps,ballRightGps);
								
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (isHasEnemyWall(ballRBGps,ballRightGps)){
								clearEnemyWall(ballRightGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (isHasEnemyWall(ballBottomGps)){
								clearEnemyWall(ballBottomGps);
								
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if (isHasEnemyWall(ballRBGps)){
								clearEnemyWall(ballRBGps);
								
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (isHasEnemyWall(ballRightGps)){
								clearEnemyWall(ballRightGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}
						}
					}
				}else if (X_DIRECTION == LEFT && Y_DIRECTION==TOP){
					// 左上
					if ($("#"+ballGps).hasClass("topWallClass") && $("#"+ballGps).hasClass("leftWallClass")){
						console.log("碰到了左上角落");
						nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
					}
					else{
						if ($("#"+ballGps).hasClass("leftWallClass")){
							nextGps = ballDirection(ballGps, RIGHT, TOP);
							
							if ($("#"+nextGps).hasClass("woodClass")){
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}
							
							// --------------------消灭障碍物  判断上和右上角--------------------
							if (isHasEnemyWall(ballTopGps,ballRTGps)){
								// 如果上,右上角都有敌人  则消灭上   下一步位置 右下
								clearEnemyWall(ballTopGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if (isHasEnemyWall(ballRTGps)){
								// 如果右上角有敌人  则消灭上右上角   下一步位置 右下
								clearEnemyWall(ballRTGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if (isHasEnemyWall(ballTopGps)){
								// 如果上有敌人  则消灭上   下一步位置 右下
								clearEnemyWall(ballTopGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}
							
						}else if ($("#"+ballGps).hasClass("topWallClass")){
							nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							
							// 判断是否撞到了木头  有三种情况  最右侧木头2总情况 其他木头一种情况	
							if (leftWoodGps== ballGps+COL_MAX){
								// 如果当前球在左侧木头的上边    左上运动
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (rightWoodGps== ballGps-1){
								// 如果当前球在右侧木头的右边   右下运动
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if (rightWoodGps == ballGps+COL_MAX-1){
								// 如果当前球在右侧木头右上角    右上运动
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if ($("#"+nextGps).hasClass("woodClass")){
								// 如果球在木头的其他位置上边   左上运动
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}
							
							// --------------------消灭障碍物  判断左和左下角-------------------- 
							if (isHasEnemyWall(ballLeftGps,ballLBGps)){
								// 如果左,左下角都有敌人  则消灭左   下一步位置 右下
								clearEnemyWall(ballLeftGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if (isHasEnemyWall(ballLeftGps)){
								// 如果左有敌人  则消灭上左   下一步位置 右下
								clearEnemyWall(ballLeftGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if (isHasEnemyWall(ballLBGps)){
								// 如果左下角有敌人  则消灭左下角   下一步位置 右下
								clearEnemyWall(ballLBGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}
						}else{
							nextGps=ballDirection(ballGps, LEFT, TOP);
							
							// 判断是否撞到了木头  有三种情况  最右侧木头2总情况 其他木头一种情况	
							if (leftWoodGps== ballGps-COL_MAX){
								// 如果当前球在左侧木头的下边    左下运动
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (rightWoodGps== ballGps-1){
								// 如果当前球在右侧木头的右边   右上运动
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if (rightWoodGps == ballGps-COL_MAX-1){
								// 如果当前球在右侧木头右下角    右下运动
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if ($("#"+nextGps).hasClass("woodClass")){
								// 如果球在木头的其他位置下边   右下运动
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}
							
							// --------------------消灭障碍物  判断上,左,左上角--------------------
							if (isHasEnemyWall(ballTopGps,ballLeftGps,ballLTGps)){
								// 如果上,左,左上角都有敌人  则消灭上和左   下一步位置 右下
								clearEnemyWall(ballTopGps,ballLeftGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if (isHasEnemyWall(ballTopGps,ballLeftGps)){
								// 如果上,左有敌人  则消灭上和左   下一步位置 右下
								clearEnemyWall(ballTopGps,ballLeftGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
								
							}else if (isHasEnemyWall(ballLeftGps,ballLTGps)){
								// 如果左,左上角有敌人  则消灭左   下一步位置 右上
								clearEnemyWall(ballLeftGps);
								
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if (isHasEnemyWall(ballTopGps,ballLTGps)){
								// 如果上,左上角有敌人  则消灭上   下一步位置 左下
								clearEnemyWall(ballTopGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (isHasEnemyWall(ballTopGps)){
								// 如果上有敌人  则消灭上   下一步位置 左下
								clearEnemyWall(ballTopGps);
								
								nextGps = ballDirection(ballGps, LEFT, BOTTOM);
							}else if (isHasEnemyWall(ballLeftGps)){
								// 如果左有敌人  则消灭左   下一步位置 右上
								clearEnemyWall(ballLeftGps);
								
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if (isHasEnemyWall(ballLTGps)){
								// 如果左上角有敌人  则消灭左上角   下一步位置 右下
								clearEnemyWall(ballLTGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}
						}
					}
				}else if (X_DIRECTION == LEFT && Y_DIRECTION==BOTTOM){
					//左下
					if ($("#"+ballGps).hasClass("bottomWallClass") && $("#"+ballGps).hasClass("leftWallClass")){
						console.log("碰到了左下   游戏结束");
						nextGps=ballDirection(ballGps, RIGHT, TOP);
					}else{
						if ($("#"+ballGps).hasClass("leftWallClass")){
							nextGps=ballDirection(ballGps, RIGHT, BOTTOM);
							
							if ($("#"+nextGps).hasClass("woodClass")){
								nextGps=ballDirection(ballGps, RIGHT, TOP);
							}
							
							
							// --------------------消灭障碍物  判断下,右下角--------------------
							if (isHasEnemyWall(ballBottomGps,ballRBGps)){
								clearEnemyWall(ballBottomGps);
								
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if (isHasEnemyWall(ballBottomGps)){
								clearEnemyWall(ballBottomGps);
								
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if (isHasEnemyWall(ballRBGps)){
								clearEnemyWall(ballRBGps);
								
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}
							
						}else if ($("#"+ballGps).hasClass("bottomWallClass")){
							console.log("游戏结束");
							
							nextGps=ballDirection(ballGps, LEFT, TOP);
						}else{
							nextGps=ballDirection(ballGps, LEFT, BOTTOM);
							
							// 判断是否撞到了木头  有三种情况  最右侧木头2总情况 其他木头一种情况	
							if (leftWoodGps== ballGps+COL_MAX){
								// 如果当前球在左侧木头的上边    左上运动
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (rightWoodGps== ballGps-1){
								// 如果当前球在右侧木头的右边   右下运动
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if (rightWoodGps == ballGps+COL_MAX-1){
								// 如果当前球在右侧木头右上角    右上运动
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if ($("#"+nextGps).hasClass("woodClass")){
								// 如果球在木头的其他位置上边   左上运动
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}
							
							// --------------------消灭障碍物  判断左,左下角,下--------------------
							if (isHasEnemyWall(ballLeftGps,ballLBGps,ballBottomGps)){
								clearEnemyWall(ballLeftGps,ballBottomGps);
								
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if (isHasEnemyWall(ballLeftGps,ballLBGps)){
								clearEnemyWall(ballLeftGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
								
							}else if (isHasEnemyWall(ballLeftGps,ballBottomGps)){
								clearEnemyWall(ballLeftGps,ballBottomGps);
								
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if (isHasEnemyWall(ballLBGps,ballBottomGps)){
								clearEnemyWall(ballBottomGps);
								
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}else if (isHasEnemyWall(ballLeftGps)){
								clearEnemyWall(ballLeftGps);
								
								nextGps = ballDirection(ballGps, RIGHT, BOTTOM);
							}else if (isHasEnemyWall(ballLBGps)){
								clearEnemyWall(ballLBGps);
								
								nextGps = ballDirection(ballGps, RIGHT, TOP);
							}else if (isHasEnemyWall(ballBottomGps)){
								clearEnemyWall(ballBottomGps);
								
								nextGps = ballDirection(ballGps, LEFT, TOP);
							}
						}
					}
				}
				// 绘制球	
				repaitBall(nextGps);
				
			},speed);
		}
		
		// 开挂
		function kaigua(){
			start();
			
			// 最后一行第一列和最后一列的序号
			var startNo = getRowStartNo(ROW_MAX);
			var endNo = getRowEndNo(ROW_MAX);
			
			kaiguaTimer = setInterval(function(){
				var ballCurPos = getBallPosition();
				if ($("#"+ballCurPos).hasClass("bottomWallClass")){
					console.log("开挂失败");
					//alert("开挂失败");
					// 游戏结束
					return;
				}
				if (Y_DIRECTION==BOTTOM){
					if (X_DIRECTION == LEFT){
						if (!$("#"+ballCurPos).hasClass("leftWallClass")){
							var ballNext = ballCurPos-1+COL_MAX;
							while(true){
								if ($("#"+ballNext).hasClass("leftWallClass") && !$("#"+ballNext).hasClass("bottomWallClass")){
									break;
								}
								
								if ($("#"+ballNext).hasClass("leftWallClass") && $("#"+ballNext).hasClass("bottomWallClass")){
									var a = 1;
									var b = 1;
								}
								
								if (ballNext>=startNo && ballNext<endNo){
									repaitWood(ballNext+1);
									break;
								}
								ballNext = ballNext-1+COL_MAX;
							}
						}
					}else if (X_DIRECTION == RIGHT){
						if (!$("#"+ballCurPos).hasClass("rightWallClass")){
							var ballNext = ballCurPos+1+COL_MAX;
							while(true){
								if ($("#"+ballNext).hasClass("rightWallClass") && !$("#"+ballNext).hasClass("bottomWallClass")){
									break;
								}

								if (ballNext>startNo && ballNext<=endNo){
									repaitWood(ballNext-1);
									break;
								}
								
								if (ballNext<1 || ballNext>ROW_MAX*COL_MAX){
									alert("开挂失败");
									return;
								}
								ballNext = ballNext+1+COL_MAX;
							}
						}
					}
				}
				
			},70);
		}
		
		// 游戏暂停
		function pause(){
			isPause = true;
			isStart = false;
			if (enemyWallTimer != null){
				clearInterval(enemyWallTimer);
				enemyWallTimer=null;
			}
			
			if (ballRunTimer != null){
				clearInterval(ballRunTimer);
				ballRunTimer=null;
			}
			
			if (kaiguaTimer != null){
				clearInterval(kaiguaTimer);
				kaiguaTimer=null;
			}
			
			if (clearInputTimer != null){
				clearInterval(clearInputTimer);
				clearInputTimer=null;
			}
			
			if (generateEnemyWallTimer != null){
				clearInterval(generateEnemyWallTimer);
				generateEnemyWallTimer=null;
			}
		}
		
		// 重新开始
		function restart(){
			window.location.href = window.location.href;
		}
		
		// 定时清理输入框内容
		function clearInputText(){
			clearInputTimer = setInterval(function(){
				$("#inputText").val("");
			},5000);
		}
		
		
		function test(){
		  //如果左上
	 		//撞左边墙 判断上中
			       //如果 只有上  吃上  返回右下
				   //只有中  吃中  返回右下
				   //上中都有 吃上 返回右下
			//撞上边墙  判断左中
					//只有左  吃左  右下
					//只有中  吃中  右下
	                //左中    吃左   右下				  
					 
			//没有撞墙  判断上中左
	                //只有上  吃上  左下
					//只有中  吃中  右下
	                //只有左  吃左  右上
	                //上中   吃上     左下				  
					//  左中   吃左     右上 
					// 上中左  吃上左   右下
			
		}
	</script>
</html>
