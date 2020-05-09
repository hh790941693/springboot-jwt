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
		<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
		<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="<%=path%>/static/js/tool.js"></script>
		
		<style type="text/css">
			body{
				margin:0;
			}

			canvas{
			}
			
			input{
				width:50px
			}
			.toggleBtn{
				margin-left:285px;
				width:25px;
			}
			.spanClass{
				display:inline-block;
				margin-left:5px;
				font-size:12px;
				width:60px;
			}
			.wheelInputClass{
				width:40px;
			}
		</style>
		
	</head> 
	<body>
		<div id="mainDiv" style="width:1000px;height:auto;margin:5px auto;overflow:hidden;">
			<canvas id="canvas" style="width:750px;height:600px;background:black;float:left;border:2px solid green;">
				你的浏览器不支持canvas标签
			</canvas>
			<div id="rightDiv" style="width:165px;height:auto;border:2px solid green;float:left;margin-left:3px;">				
				<div id="smallCanvasDiv" style="width:auto;height:600px;">
					<canvas id="smallCanvas" style="background-color:black">
						你的浏览器不支持canvas标签
					</canvas>
				</div>
				<div>
					<input id="mobileControllerInput" value="手机用户点此处进行控制" style="width:160px;">
				</div>
				<div id="controllerDiv" style="width:auto;height:80px;display:none;padding-left:5px;">
					<button id="startBtn" onclick="startGame()">开始</button>
					<button id="pauseBtn" disabled onclick="pauseGame()">暂停</button>
					<button id="showHideBtn">收起</button><br/>

					<button id="selectPlayerBtn">玩家1</button>
					<button id="restartBtn" onclick="restartGame()">重启</button>
					<button id="helpBtn">帮助</button>
					<button id="fingerBtn" style="width:150px;">金手指</button>
				</div>
				
				<div id="helpInfoDiv" style="width:auto;height:400px;display:none;margin-top:10px;border-top:2px solid green;font-size:15px;">
					<span style="color:red;font-weight:bold">玩家1:</span><br/>
							&nbsp;&nbsp;&nbsp;w:上  &nbsp;s:下<br/>
							&nbsp;&nbsp;&nbsp;a:左  &nbsp;d:右<br/>
							&nbsp;&nbsp;&nbsp;空格键:&nbsp;射击<br/>
							&nbsp;&nbsp;&nbsp;k:&nbsp;火箭弹<br/>
					<br/>
					<span style="color:red;font-weight:bold">玩家2:</span><br/>
							&nbsp;&nbsp;&nbsp;↑:上  &nbsp;↓:下<br/>
							&nbsp;&nbsp;←:左  &nbsp;→:右<br/>
							&nbsp;&nbsp;&nbsp;回车键:&nbsp;射击<br/>
							&nbsp;&nbsp;&nbsp;数字0:&nbsp;火箭弹<br/>
							
					<br/>
					<span style="color:red;font-weight:bold">其他:</span><br/>
						&nbsp;&nbsp;&nbsp;鼠标滚轮:&nbsp;选择关卡<br/>
						&nbsp;&nbsp;&nbsp;↑或↓:&nbsp;选择游戏模式<br/>
						&nbsp;&nbsp;&nbsp;p:&nbsp;暂停游戏<br/>
				</div>
				<div id="fingerDiv" style="width:auto;height:345px;display:none">
					<span class="spanClass">选择关卡:</span><input id="chooseMissionInput" class="wheelInputClass" type="range" value=1 min=1 step=1 onchange="chooseMission()">
					<span id="curMission" >1</span>
					<span class="spanClass">生命条数:</span><input id="playerTotalLifeInput" class="wheelInputClass" type="range" value=3 min=1 max=999 step=1 onchange="changePlayerTotalLife()">
					<span id="playerTotalLife" >3</span>
					<br/>
					<span class="spanClass">移动速度:</span><input id="playerMoveSpeedInput" class="wheelInputClass" type="range" value=5 min=3 max=15 step=1 onchange="changePlayerMoveSpeed()">
					<span id="playerMoveSpeed">5</span>
					<br/>
					<span class="spanClass">射击速度:</span><input id="playerShootSpeedInput" class="wheelInputClass" type="range" value=8 min=3 max=20 step=1 onchange="changePlayerShootSpeed()">
					<span id="playerShootSpeed">8</span>
					<br/>
					<span class="spanClass">子弹伤害:</span><input id="playerBombDamageInput" class="wheelInputClass" type="range" value=10 min=5 max=9999 step=10 onchange="changePlayerBombDamage()">
					<span id="playerBombDamage">10</span>
					<br/>
					<span class="spanClass">初始血量:</span><input id="playerTotalBloodInput" class="wheelInputClass" type="range" value=10 min=5 max=10000 step=10 onchange="changePlayerTotalBlood()">
					<span id="playerTotalBlood" >10</span>
					<span class="spanClass">炮弹数量:</span><input id="playerTotalTrackBombInput" class="wheelInputClass" type="range" value=3 min=1 max=90000 step=10 onchange="changePlayerTotalTrackBomb()">
					<span id="playerTotalTrackBomb" >3</span>
					<hr>
					<span class="spanClass">鸟巢加固:</span><input id="homeStrongerInput" style="width:15px;" type="checkbox"><span style="width:40px;font-size:12px;">自杀1:</span><input id="selfKillInput1" style="width:15px;" type="checkbox">
					<br/>
					<span class="spanClass">冻结敌人:</span><input id="freezeEnermyInput" style="width:15px;" type="checkbox"><span style="width:40px;font-size:12px;">自杀2:</span><input id="selfKillInput2" style="width:15px;" type="checkbox">
					<br/>
					<span class="spanClass">生成道具:</span><input id="createBonusInput" style="width:15px;" type="checkbox"><span style="width:40px;font-size:12px;">自动攻击:</span><input id="autoSendTrackBombInput" style="width:15px;" type="checkbox">
					<hr>
					
					<span style="width:120px;font-size:12px;margin-left:5px;">敌人总数:</span><input id="totalEnermyNumInput" class="wheelInputClass" type="range" value=21 min=1 max=1000 step=1 onchange="changeTotalEnermyNum()">
					<span id="totalEnermyNum" >21</span>
					<br/>
					<span style="width:120px;font-size:12px;margin-left:5px;">活动敌人数:</span><input id="maxLiveEnermyInput" style="width:40px;" type="range" value=6 min=1 max=100 step=1 onchange="changeMaxLiveEnermyNum()">
					<span id="maxLiveEnermyNum">6</span>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript">	
		// 游戏主画布
		var canvas = document.getElementById("canvas");
		canvas.width  = 750;
		canvas.height = 600;
		var ctx = canvas.getContext("2d");

		// 大地图每一个网格的宽高 单位:像素
		var DIV_WIDTH  = 30;
		var DIV_HEIGHT = 30;
		
		// 资源文件
		var SPRITE_IMAGE = new Image();
		SPRITE_IMAGE.src="<%=path%>/img/tank.PNG";
		
		// 右侧画布
		var smallCanvas = document.getElementById("smallCanvas");
		smallCanvas.width  = 160;
		smallCanvas.height = 600;
		var smallCtx = smallCanvas.getContext("2d");
		
		// 小地图每一个网格的宽高 单位:像素
		var SMALL_DIV_WIDTH = 30;
		var SMALL_DIV_HEIGHT = 30; 
		
		// 缓冲画布1--绘制暂停/游戏结束画面,游戏主页面使用
		var bufferCanvas1 = document.createElement("canvas");
		bufferCanvas1.width  = canvas.width;
		bufferCanvas1.height = canvas.height;
		var bufferCtx1 = bufferCanvas1.getContext("2d");
		
		// 缓冲画布2--绘制暂停/游戏结束画面,游戏小页面使用
		var bufferCanvas2 = document.createElement("canvas");
		bufferCanvas2.width  = smallCanvas.width;
		bufferCanvas2.height = smallCanvas.height;
		var bufferCtx2 = bufferCanvas2.getContext("2d");

		// 游戏是否开始
		var IS_GAME_START = false;
		// 游戏是否暂停
		var IS_GAME_PAUSE = false;
		// 游戏是否结束
		var IS_GAME_OVER = false;

		// 是否是第一次运行
		var IS_FIRST_RUN = false;
		
		// 是否显示网格
		var IS_SHOW_GRID = false;
		
		// 是否自动发射炮弹
		var IS_AUTO_SEND_TRACK_BOMB = false;

		// 最大行、最大列
		var MAX_COL = parseInt(canvas.width/DIV_WIDTH);
		var MAX_ROW = parseInt(canvas.height/DIV_HEIGHT);
		
		// fps帧数
		var FPS = 0;
		
		// 当前关卡
		var MISSION = 1;

		// 道路方向:左转、直行、右转
		var LEFT="LEFT", CENTER="CENTER", RIGHT="RIGHT";

		// 左右上下方向
		var D_LEFT="D_LEFT",D_RIGHT="D_RIGHT",D_UP="D_UP",D_DOWN="D_DOWN";
		
		// 游戏模式
		var SINGLE_PLAYER = 1,MULTI_PLAYER = 2;
		
		// 玩家模式 1:单人模式  2:双人模式
		var GAME_MODE = SINGLE_PLAYER;

		//---------------------------------------------敌方坦克-----------------------------------------------------------
		// 敌方坦克和子弹id前缀
		var ENERMY_TANK_ID_PREFIX = "ek";
		// 敌人数组
		var ENERMY_ARR = new Array();
		// 敌人坦克出生位置
		var ENERMY_BORN_POS = {
			LEFT:  {x:0,y:0,"isBorn":false},
			CENTER:{x:canvas.width/2,y:0,"isBorn":false},
			RIGHT: {x:canvas.width-DIV_WIDTH,y:0,"isBorn":false}
		}
		// 画面中同时出现敌方坦克数
		var MAX_ENERY_LIVE_NUM = 6;
		// 敌人坦克总数
		var TOTAL_ENERY_NUM = 21;
		// 当前已使用坦克数
		var ENERY_USED_NUM = 0;
		// 已死亡坦克数
		var ENERY_DEAD_NUM = 0;
		// 敌方坦克上下左右对应到资源文件的坐标位置
		var ENERMY_PICTURE = {
			// 一行一列
			"type1":{
				"speed":3,
				"totalBlood":10,
				"isBonus":false,
				D_RIGHT:{x:78, y:228, w:15, h:13},
				D_DOWN: {x:95, y:228, w:13, h:15},
				D_UP:   {x:110,y:228, w:13, h:15},
				D_LEFT: {x:125,y:228, w:15, h:13}
			},
			// 一行二列
			"type2":{
				"speed":5,
				"totalBlood":10,
				"isBonus":false,
				D_DOWN: {x:145, y:227, w:13, h:15},
				D_RIGHT:{x:160, y:227, w:15, h:13},
				D_UP:   {x:177, y:228, w:13, h:15},
				D_LEFT: {x:192, y:228, w:15, h:13}
			},
			// 二行一列
			"type3":{
				"speed":3,
				"totalBlood":10,
				"isBonus":true,
				D_RIGHT:{x:78,  y:261, w:15, h:15},
				D_DOWN: {x:94,  y:261, w:15, h:15},
				D_UP:   {x:110, y:261, w:15, h:15},
				D_LEFT: {x:126, y:261, w:15, h:15}
			},
			// 二行二列
			"type4":{
				"speed":5,
				"totalBlood":10,
				"isBonus":true,
				D_DOWN:  {x:144, y:261, w:15, h:15},
				D_RIGHT: {x:160, y:261, w:15, h:15},
				D_UP:    {x:176, y:261, w:15, h:15},
				D_LEFT:  {x:192, y:261, w:15, h:15}
			},
			// 三行二列
			"type5":{
				"speed":2,
				"totalBlood":50,
				"isBonus":false,
				D_DOWN:  {x:143, y:300, w:15, h:15},
				D_RIGHT: {x:159, y:300, w:15, h:13},
				D_UP:    {x:175, y:300, w:15, h:15},
				D_LEFT:  {x:191, y:300, w:15, h:13}
			},
			// 四行二列
			"type6":{
				"speed":2,
				"totalBlood":50,
				"isBonus":true,
				D_DOWN:  {x:143, y:334, w:15, h:15},
				D_RIGHT: {x:159, y:334, w:15, h:13},
				D_UP:    {x:175, y:334, w:15, h:15},
				D_LEFT:  {x:191, y:334, w:15, h:13}
			}
		}

		//------------------------------------------------玩家-------------------------------------------------------
		// 我方坦克和子弹id前缀
		var PLAYER1_ID_PREIFIX = "player1";
		var PLAYER2_ID_PREIFIX = "player2";
		//右侧地图显示的玩家信息
		var CUR_PLAYER = PLAYER1_ID_PREIFIX;
		// 玩家坦克数组
		var PLAYER_TANK_ARR = new Array();
		// 玩家1生命
		var PLAYER1_TOTAL_LIFE = 3;
		// 玩家2生命
		var PLAYER2_TOTAL_LIFE = 3;
		
		// 作弊器
		var PLAYER_DATA = {
			"player1":{
				"totalLife":3,      // 玩家总生命
				"moveSpeed":5,      // 移动速度
				"bombSpeed":8,      // 射击速度
				"bombDamage":10,    // 子弹伤害
				"totalBlood":10,    // 初始血量
				"totalLife":PLAYER1_TOTAL_LIFE,      //初始生命条数
				"totalTrackBomb":3, // 总追踪炮弹数
				"bornPos":{         // 出生位置 
						 "x":canvas.width/2-4*DIV_WIDTH,
						 "y":canvas.height-DIV_HEIGHT
				},
				"imgPos":{          // 我方坦克上下左右对应到资源文件的坐标位置
					D_RIGHT:{x:2, y:227,w:15,h:15},
					D_DOWN: {x:19,y:227,w:15,h:15},
					D_UP:   {x:36,y:227,w:15,h:15},
					D_LEFT: {x:53,y:227,w:15,h:15}
				}
			},
			"player2":{
				"totalLife":3,      // 玩家总生命
				"moveSpeed":5,
				"bombSpeed":8,
				"bombDamage":10,
				"totalBlood":10,
				"totalLife":PLAYER2_TOTAL_LIFE,
				"totalTrackBomb":3, // 总追踪炮弹数
				"bornPos":{
						"x":canvas.width/2+3*DIV_WIDTH,
						"y":canvas.height-DIV_HEIGHT
				},
				"imgPos":{
					D_RIGHT:{x:2, y:298,w:15,h:15},
					D_DOWN: {x:19,y:298,w:15,h:15},
					D_UP:   {x:36,y:298,w:15,h:15},
					D_LEFT: {x:53,y:298,w:15,h:15}
				}				
			},
		}
		
		// 玩家是否在移动
		var IS_PLAYER1_MOVE = false;
		var IS_PLAYER2_MOVE = false;

		//--------------------------------------------------子弹----------------------------------------------------
		// 子弹数组
		var BOMB_ARR = new Array();
		// 子弹大小
		var BOMB_WIDTH = DIV_WIDTH/5;
		var BOMB_HEIGHT = DIV_HEIGHT/5;
		
		// 追踪炮弹
		var TRACK_BOMB_ARR =  new Array();
		var TRACK_BOMB_WIDTH = DIV_WIDTH/3;
		var TRACK_BOMB_HEIGHT = DIV_HEIGHT;
		
		// 爆炸物数组
		var EXPLODE_ARR = new Array();
		
		// 奖励物数组
		var BONUS_ARR = new Array();
		
		// 提示语数组
		var TIPS_ARR = new Array();
		TIPS_OBJ = {
			"kill":{           //当击杀一名敌方坦克时
				"text":"干得漂亮",
				"level":2
			},
			"dead":{           // 当玩家死亡时
				"text":"别灰心,再接再厉",
				"level":5
			},
			"bomb":{           // 捡到炸弹
				"text":"真棒,敌军全军覆没",
				"level":6,
				"fontSize":18
			},
			"fiveStar":{        // 吃到五角星
				"text":"百发百中",
				"level":2		
			},
			"fiveStar2":{        // 吃到2颗五角星
				"text":"所向披靡",
				"level":2			
			},	
			"shovel":{          // 吃到铁锹
				"text":"坚如磐石",
				"level":2		
			},	
			"life":{            // 吃到生命
				"text":"寿比南山",
				"level":2		
			},
			"freeze":{          // 吃到定时器
				"text":"为所欲为",
				"level":2	
			},	
			"unbeatable":{      // 吃到无敌
				"text":"刀枪不入",
				"level":2		
			},		
			"attack":{          // 鸟巢被攻击
				"text":"鸟巢正在被攻击",
				"level":9,
				"color":"red",
				"fontSize":18				
			},	
			"gameover":{        // 鸟巢被炸毁
				"text":"失败是成功之母",
				"level":10		
			},	
			"notice":{         // 附近有敌人出现
				"text":"附近有敌人出现",
				"level":8,
				"color":"red",
				"fontSize":20
			},			
		}

		// 障碍物数组
		var STICK_ARR = new Array();
		//-------------------------------------------------障碍物------------------------------------------------
		var BLOCK_SOIL = "block_soil",BLOCK_GOLD = "block_gold",BLOCK_RIVER="block_river",BLOCK_ICE = "block_ice";
		var BLOCK_GRASS = "block_grass",BLOCK_HOME = "block_home",BLOCK_EXPLODE1 = "block_explode1",BLOCK_EXPLODE2="block_explode2",BLOCK_GAMEOVER = "block_gameover";
		var BONUS_FIVE_STAR = "bonus_5star",BONUS_BOMB = "bonus_bomb", BONUS_SHOVEL = "bonus_shovel", BONUS_LIFE = "bonus_life", BONUS_FREEZE = "bonus_freeze", BONUS_UNBEATABLE = "bonus_unbeatable";

		// 障碍物对象
		var BLOCK_OBJ = {
			"block_soil":{
                "drawObj":     new Sprite(BLOCK_SOIL,118,414,8,8,30,15),        // 土块
                "totalBlood":  10,
                "canBombCross":false,
                "canTankCross":false,
                "canDie":      true
            },
			"block_gold":{
                "drawObj":     new Sprite(BLOCK_GOLD,110,414,8,8,15,15),        // 铜板
                "totalBlood":  8888,
                "canBombCross":false,
                "canTankCross":false,
                "canDie":      true
            },
			"block_river":{
                "drawObj":     new Sprite(BLOCK_RIVER,109,423,8,8,15,15,0.65),   // 河流
                "totalBlood":  50,
                "canBombCross":true,
                "canTankCross":false,
                "canDie":      false
            },
			"block_grass":{
                "drawObj":     new Sprite(BLOCK_GRASS,136,423,8,8,15,15,0.65),   // 草丛
                "totalBlood":  8888,
                "canBombCross":true,
                "canTankCross":true,
                "canDie":      true
            },
			"block_ice":{
                "drawObj":     new Sprite(BLOCK_ICE,146,424,6,6,15,15,0.4),      // 冰块(减速效果)
                "totalBlood":  50,
                "canBombCross":true,
                "canTankCross":true,
                "canDie":      false
            },
			"block_home":{
                "drawObj":     new Sprite(BLOCK_HOME,186,371,16,16,30,30),       // 鸟巢
                "totalBlood":  10,
                "canBombCross":false,
                "canTankCross":false,
                "canDie":      true
            },
			"block_explode1":{
                "drawObj":     new Sprite(BLOCK_EXPLODE1,191,392,10,10,26,26),   // 爆炸1
                "totalBlood":  50,
                "canBombCross":true,
                "canTankCross":true,
                "canDie":      false
            },
			"block_explode2":{
                "drawObj":     new Sprite(BLOCK_EXPLODE2,205,390,15,15,30,30),   // 爆炸2
                "totalBlood":  50,
                "canBombCross":true,
                "canTankCross":true,
                "canDie":      false
            },
			"block_gameover":{
                "drawObj":     new Sprite(BLOCK_GAMEOVER,2,370,65,40,30,30),     // game over
                "totalBlood":  50,
                "canBombCross":true,
                "canTankCross":true,
                "canDie":      false
            } 
		}

		// 奖励品对象
		var BONUS_OBJ = {
			"bonus_5star":     new Sprite(BONUS_FIVE_STAR, 118,370,15,15,30,30),       // 五角星
			"bonus_bomb":      new Sprite(BONUS_BOMB,      135,370,16,16,30,30),       // 炸弹
			"bonus_shovel":    new Sprite(BONUS_SHOVEL,    136,387,16,16,30,30),       // 铁铲
			"bonus_life":      new Sprite(BONUS_LIFE,      118,388,15,15,30,30),       // 生命
			"bonus_freeze":    new Sprite(BONUS_FREEZE,    100,371,15,15,30,30),       // 定住
			"bonus_unbeatable":new Sprite(BONUS_UNBEATABLE,100,388,15,15,30,30)		   // 无敌
		}
	
		// 记录游戏开始瞬间或者过关瞬间的G_CYCLE值  目的是加载启动动画
		var STARTBTN_PRESSED_CYCLE=0;

		// 鸟巢四周砖块的位置数组
		var HOME_AROUND_ARR = new Array([MAX_ROW-1,MAX_ROW,(MAX_COL-1)/2,(MAX_COL-1)/2],[MAX_ROW-1,MAX_ROW-1,(MAX_COL+1)/2,(MAX_COL+1)/2],[MAX_ROW-1,MAX_ROW,(MAX_COL+3)/2,(MAX_COL+3)/2]);
		
		// 各关卡地图配置
		var MISSION_CFG = {
			"1":{
				"block_soil":[
					[3,7,2,5],
					[3,7,7,12],
					[3,7,13,19],
					[3,7,21,24]
				],
				"block_gold":[
					[16,16,1,3],
					[16,16,5,MAX_COL-4],
					[16,16,MAX_COL-2,MAX_COL],
					[14,14,3,5],
					[14,14,21,23],
					[15,15,1,1],
					[15,15,MAX_COL,MAX_COL],
					[15,15,5,5],
					[15,15,21,21],
					[17,17,2,2],
					[17,17,6,6],
					[17,17,20,20],
					[17,17,24,24],
					[18,18,4,4],
					[18,18,22,22],
					[14,14,7,7],
					[14,14,9,9],
					[14,14,11,11],
					[14,14,13,13],
					[14,14,15,15],
					[14,14,17,17],
					[14,14,19,19],
					[13,13,8,8],
					[13,13,10,10],
					[13,13,12,12],
					[13,13,14,14],
					[13,13,16,16],
					[13,13,18,18],
					[10,10,1,2],
					[10,10,5,7],
					[10,10,19,21],
					[10,10,MAX_COL-1,MAX_COL]
				],
				"block_grass":[
					[12,12,3,6],
					[12,12,20,23]
				],
				"block_river":[
					[8,8,2,5],
					[8,8,7,MAX_COL-6],
					[8,8,MAX_COL-4,MAX_COL-1]
				],
				"block_ice":[
					[11,11,11,16]
				]
			},
			"2":{
				"block_soil":[
					[6,8,1,MAX_COL]
				],
				"block_home":[
					[MAX_ROW,MAX_ROW,(MAX_COL+1)/2,(MAX_COL+1)/2]
				],
				"block_ice":[
					[15,15,5,13],
					[2,3,3,MAX_COL-3]
				],
				"block_river":[
					[12,12,1,6],
					[12,12,MAX_COL-6,MAX_COL]
				],
				"block_grass":[
					[10,13,9,16]
				],
				"block_gold":[
					[10,10,4,6],
					[10,10,MAX_COL-5,MAX_COL-3]
				]
			},
			"3":{
				"block_soil":[
					[8,12,1,MAX_COL]
				],
				"block_grass":[
					[3,6,5,MAX_COL-5]
				]				
			},
			"4":{
			},
			"default":{
				"block_soil":[
					[2,8,2,3],
					[2,8,5,6],
					[2,8,8,9],
					[2,8,11,12],
					[2,8,15,16],
					[2,8,18,19],
					[2,8,21,22],
					[2,8,24,24],
					
					[14,19,2,3],
					[14,19,5,6],
					[14,19,8,9],
					[14,17,11,12],
					[14,17,15,16],
					[14,19,18,19],
					[14,19,21,22],
					[14,19,24,24],
					
					
					[10,10,1,1],
					[10,10,MAX_COL,MAX_COL],
					
					[11,12,4,7],
					[10,12,10,16],
					[11,12,19,22]
				],
				"block_gold":[
					[6,6,13,14],
					[15,15,13,14],
					[11,11,1,1],
					[11,11,MAX_COL,MAX_COL],
				],
				"block_grass":[
				],
				"block_river":[
				]			
			}
		}

		// 游戏首页键盘事件
		function gameStartKeyListener(event){
			var key_state = (event.type == "keydown") ? true:false;
			var keyCode = event.keyCode;
			if (key_state){
				// 如果是初次进入游戏情况下  按方向上下键选择模式  如果点击其他键则直接开始游戏
				if (!IS_GAME_START && !IS_GAME_PAUSE && !IS_GAME_OVER){
					if (keyCode==38 || keyCode==40){
						// 键盘方向键
						if (GAME_MODE == SINGLE_PLAYER){
							GAME_MODE = MULTI_PLAYER;
						}else{
							GAME_MODE = SINGLE_PLAYER;
						}
					}else{
						// 开始游戏
						startGame();
					}
				}
				// 如果游戏运行中 点击p键暂停游戏  
				else if (IS_GAME_START && !IS_GAME_PAUSE){
					if (keyCode == 80){
						pauseGame();
					}
				}
				// 如果游戏处于暂停状态时 点击任意键开始游戏
				else if (!IS_GAME_START && IS_GAME_PAUSE){
					// 开始游戏
					startGame();		
				}
			}
		}

		// 玩家1键盘监听事件
		function player1KeyListener(event){
			var key_state = (event.type == "keydown") ? true:false;
			var keyCode = event.keyCode;
			if (keyCode == 65 || keyCode == 68 || keyCode == 87 || keyCode == 83 || keyCode == 32 || keyCode == 75){
				if (key_state){
					var direct = undefined;
					if (keyCode==65){
						direct = D_LEFT;
					} else if (keyCode==87){
						direct = D_UP;
					} else if (keyCode==68){
						direct = D_RIGHT;
					} else if (keyCode==83){
						direct = D_DOWN;
					} else if (keyCode == 32){
						if (isPlayerExist(PLAYER1_ID_PREIFIX)){
							getPlayerTankInfo(PLAYER1_ID_PREIFIX).shoot();
						}
					} else if (keyCode == 75){
						// k
						if (isPlayerExist(PLAYER1_ID_PREIFIX)){
							getPlayerTankInfo(PLAYER1_ID_PREIFIX).sendTrackBomb();
						}
					}
					if (isPlayerExist(PLAYER1_ID_PREIFIX) && direct != undefined){
						getPlayerTankInfo(PLAYER1_ID_PREIFIX).changeDirection(direct);
					}
					if (keyCode != 32 && keyCode != 75){
						IS_PLAYER1_MOVE = true;
					}
				}else{
					if (keyCode != 32 && keyCode != 75){
						IS_PLAYER1_MOVE = false;
					}
				}
			}
		}

		// 玩家2键盘事件
		function player2KeyListener(event){
			var key_state = (event.type == "keydown") ? true:false;
			var keyCode = event.keyCode;
			if (keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 || keyCode == 13 || keyCode == 96){
				if (key_state){
					var direct = undefined;
					if (keyCode==37){
						direct = D_LEFT;
					} else if (keyCode==38){
						direct = D_UP;
					} else if (keyCode==39){
						direct = D_RIGHT;
					} else if (keyCode==40){
						direct = D_DOWN;
					} else if (keyCode == 13){
						if (isPlayerExist(PLAYER2_ID_PREIFIX)){
							getPlayerTankInfo(PLAYER2_ID_PREIFIX).shoot();
						}
					} else if (keyCode == 96){
						// 数字0键
						if (isPlayerExist(PLAYER2_ID_PREIFIX)){
							getPlayerTankInfo(PLAYER2_ID_PREIFIX).sendTrackBomb();
						}
					}
					if (isPlayerExist(PLAYER2_ID_PREIFIX) && direct != undefined){
						getPlayerTankInfo(PLAYER2_ID_PREIFIX).changeDirection(direct);
					}
					if (keyCode != 13 && keyCode != 96){
						IS_PLAYER2_MOVE = true;
					}					
				}else{
					if (keyCode != 13 && keyCode != 96){
						IS_PLAYER2_MOVE = false;
					}
				}
			}
		}

		// 鼠标滚轮事件
		function mouseWheelListener(event){
			var delta = event.wheelDelta;
			var maxMission = MISSION_CFG.getObjLength();
			if (!IS_GAME_START && !IS_GAME_PAUSE && !IS_GAME_OVER){
				if (delta == 120){
					MISSION--;
					if (MISSION < 1){
						MISSION = maxMission;
					}
				} else if (delta == -120){
					MISSION++;
					if (MISSION > maxMission){
						MISSION = 1;
					}
				}
			}
		}

		// 显示面板网格
		function showPanelGrid(content){
			var startRow = 0,endRow = MAX_ROW;
			var startCol = 0,endCol = MAX_COL;

			content.beginPath();
			content.font = "normal normal 20px 宋体";
			for (var y=startRow*DIV_HEIGHT;y<=endRow*DIV_HEIGHT;y+=DIV_HEIGHT){
				content.moveTo(0,y);
				content.lineTo(canvas.width,y);
				content.strokeStyle="rgba(255,255,255,0.7)";
				content.strokeText(y/DIV_HEIGHT+1,5,y+22);
				content.stroke();
			}
			for (var x=startCol*DIV_WIDTH;x<=endCol*DIV_WIDTH;x+=DIV_WIDTH){
				content.moveTo(x,0);
				content.lineTo(x,canvas.height);
				content.strokeStyle="rgba(255,255,255,0.7)";
				content.strokeText(x/DIV_WIDTH+1,x+5,22);
				content.stroke();
			}
			content.closePath();
		}

		// 敌方坦克
		// id:唯一标识   type:类型  x:x轴坐标  y:y轴坐标  w:宽度  h:高度  direct:运动方向  totalBlood:总血量   speed:移动速度   extendObj:扩展属性
		function EnermyTank(id,type,x,y,w,h,direct,totalBlood,speed,extendObj){
			this.id = id;
			this.type = type;
			this.x = x;
			this.y = y;
			this.direct = direct;
			this.totalBlood = totalBlood; //总血量
			// 移动速度
			this.dx = speed;
			this.dy = speed;
			// 扩展参数对象
			this.extendObj = extendObj;
			
			// 坦克大小
			this.w = w;
			this.h = h;
			// 同时最多发几颗子弹
			this.totalBomb = 3;
			// 每隔几秒发射子弹
			this.shootInteval = 2.5;

			// 当前血量
			this.curBlood = this.totalBlood;
			
			// 敌方子弹速度和伤害
			this.bombSpeed = 6;
			this.bombDamage = 10;
			// 坦克是否被限制移动   true:不能移动  false:可以移动
			this.isFreeze = false;
			// 错开各坦克射击顺序
			this.initShootCycle = G_CYCLE;
			// 当前坦克是否被追踪炮弹锁定
			this.beLocked = false;
			this.beLockedTime = 0;
			
			// 是否在冰块上面
			this.isOnIce = false;
			this.tempSpeed = speed;
			
			this.draw = function(){
				var pictureInfo = ENERMY_PICTURE.getObjValueByKey(this.type);
				var posObj = pictureInfo.getObjValueByKey(this.direct);
				ctx.drawImage(SPRITE_IMAGE,posObj.x,posObj.y,posObj.w,posObj.h,this.x,this.y,this.w,this.h);
				
				// 绘制坦克被追踪炮弹锁定时的样式
				if (this.beLocked && this.beLockedTime %3 == 0){
					ctx.beginPath();
					ctx.strokeStyle="rgb(255,255,0)";
					var lineWidth = 3;
					ctx.lineWidth=lineWidth;
					ctx.arc(this.x+this.w/2,this.y+this.h/2,this.w/2+4,0,2*Math.PI,false);
					
					ctx.moveTo(this.x+this.w/2,this.y-2);
					ctx.lineTo(this.x+this.w/2,this.y-2+10);
		
					ctx.moveTo(this.x+this.w/2,this.y+this.h+2);
					ctx.lineTo(this.x+this.w/2,this.y+this.h+2-10);
					
					ctx.moveTo(this.x-2,this.y+this.h/2-2);
					ctx.lineTo(this.x-2+10,this.y+this.h/2-2);
		
					ctx.moveTo(this.x+this.w+2,this.y+this.h/2-2);
					ctx.lineTo(this.x+this.w+2-10,this.y+this.h/2-2);
		
					ctx.stroke();
					ctx.closePath();
				}
				if (this.beLocked && G_CYCLE %10 == 0){
					this.beLockedTime++;
				}
			}
			
			this.update = function(){
				this.draw();
				
				//检测血量
				if (this.curBlood<=0){
					ENERMY_ARR.removeArrElement(this);
					EXPLODE_ARR.push(new Explode(this.x,this.y));
					ENERY_DEAD_NUM++;
					if (ENERY_DEAD_NUM < TOTAL_ENERY_NUM){
						showSmallCanvasTips(TIPS_OBJ["kill"]);
					}
				}

				// 每隔指定时间自动开炮
				if ((G_CYCLE-this.initShootCycle) % (this.shootInteval*60) == 0){
					this.shoot();
				}

				if (this.x<0 || this.x>canvas.width-DIV_WIDTH || this.y<0 || this.y>canvas.height-DIV_HEIGHT){
					if (this.x<0){
						this.x = 0;
						this.direct = randomSpecifyNumber(D_RIGHT,D_UP,D_DOWN);				
						if (this.y < 0){
							// 左上角
							this.y = 0;
							this.direct = randomSpecifyNumber(D_RIGHT,D_DOWN);		
						}else if (y > canvas.height-DIV_HEIGHT){
							// 左下角
							this.y = canvas.height-DIV_HEIGHT;
							this.direct = randomSpecifyNumber(D_UP,D_RIGHT);
						}
					} else if (this.x>canvas.width-DIV_WIDTH){
						this.x = canvas.width-DIV_WIDTH;
						this.direct = randomSpecifyNumber(D_LEFT,D_UP,D_DOWN);
						if (this.y < 0){
							// 右上角
							this.y = 0;
							this.direct = randomSpecifyNumber(D_LEFT,D_DOWN);
						}else if (y > canvas.height-DIV_HEIGHT){
							// 右下角
							this.y = canvas.height-DIV_HEIGHT;
							this.direct = randomSpecifyNumber(D_UP,D_LEFT);
						}
					}else if (this.y<0){
						this.y = 0;
						this.direct = randomSpecifyNumber(D_LEFT,D_RIGHT,D_DOWN);
						if (this.x < 0){
							// 左上角
							this.x = 0;
							this.direct = randomSpecifyNumber(D_RIGHT,D_DOWN);		
						}else if (x > canvas.width-DIV_WIDTH){
							// 右上角
							this.x = canvas.width-DIV_WIDTH;
							this.direct = randomSpecifyNumber(D_LEFT,D_DOWN);
						}
					}else if (this.y>canvas.height-DIV_HEIGHT){
						this.y = canvas.height-DIV_HEIGHT;
						this.direct = randomSpecifyNumber(D_LEFT,D_RIGHT,D_UP);
						if (this.x < 0){
							// 左下角
							this.x = 0;
							this.direct = randomSpecifyNumber(D_RIGHT,D_DOWN);	
						}else if (x > canvas.width-DIV_WIDTH){
							// 右下角
							this.x = canvas.width-DIV_WIDTH;
							this.direct = randomSpecifyNumber(D_UP,D_RIGHT);
						}
					}
				}

				// 检查敌方坦克是否撞墙
				if (this.checkWallCollision()){
					if (this.direct == D_LEFT){
						this.left();
					} else if (this.direct == D_RIGHT){
						this.right();
					}else if (this.direct == D_UP){
						this.up();
					}else if (this.direct == D_DOWN){
						this.down();
					}
					return;
				}
				
				if (this.direct == D_LEFT){
					this.left();
				} else if (this.direct == D_RIGHT){
					this.right();
				}else if (this.direct == D_UP){
					this.up();
				}else if (this.direct == D_DOWN){
					this.down();
				}
			}
			
			// 开炮
			this.shoot = function(){
				if (this.isFreeze){
					return;
				}
				
				var curBomb = 0;
				for (var i=0;i<BOMB_ARR.length;i++){
					if(BOMB_ARR[i].id == this.id){
						curBomb++;
					}
				}
				
				if (curBomb >= this.totalBomb){
					return;
				}
			
				if (this.direct == D_LEFT){
					BOMB_ARR.push(new Bomb(this.id,this.x,this.y+DIV_HEIGHT/2-BOMB_HEIGHT/2,BOMB_WIDTH,BOMB_HEIGHT,this.direct,this.bombSpeed,this.bombDamage));
				}else if (this.direct == D_RIGHT){
					BOMB_ARR.push(new Bomb(this.id,this.x+DIV_WIDTH,this.y+DIV_HEIGHT/2-BOMB_HEIGHT/2,BOMB_WIDTH,BOMB_HEIGHT,this.direct,this.bombSpeed,this.bombDamage));
				}else if (this.direct == D_UP){
					BOMB_ARR.push(new Bomb(this.id,this.x+DIV_WIDTH/2-BOMB_WIDTH/2,this.y,BOMB_WIDTH,BOMB_HEIGHT,this.direct,this.bombSpeed,this.bombDamage));
				}else if (this.direct == D_DOWN){
					BOMB_ARR.push(new Bomb(this.id,this.x+DIV_WIDTH/2-BOMB_WIDTH/2,this.y+DIV_HEIGHT,BOMB_WIDTH,BOMB_HEIGHT,this.direct,this.bombSpeed,this.bombDamage));
				}
			}
			
			// 坦克不能移动且不能发子弹
			this.freeze = function(){
				this.isFreeze = true;
			}
			
			// 恢复坦克移动且正常发子弹
			this.unfreeze = function(){
				this.isFreeze = false;
			}
			
			// 检查敌方坦克是否撞墙
			this.checkWallCollision = function(){
				var res = false;
				for (var i=0;i<STICK_ARR.length;i++){
					if (STICK_ARR[i] === this){
						continue;
					}
					var collResult = isTwoRectangleHit(this,STICK_ARR[i]);
					if (collResult){
						res = collResult;
						if (!STICK_ARR[i].canTankCross){
							if (this.direct == D_RIGHT){
								this.direct = randomSpecifyNumber(D_LEFT,D_UP,D_DOWN);
								if (this.x == 0 && this.y == 0){
									this.direct = randomSpecifyNumber(D_DOWN);
								}else if (this.x == 0){
									this.direct = randomSpecifyNumber(D_UP,D_DOWN);
								} else if (this.y == 0){
									this.direct = randomSpecifyNumber(D_LEFT,D_DOWN);
								}
								
								if (this.x == 0 && this.y == canvas.height-DIV_HEIGHT){
									this.direct = randomSpecifyNumber(D_UP);
								}else if (this.y == canvas.height-DIV_HEIGHT){
									this.direct = randomSpecifyNumber(D_LEFT,D_UP);
								}
								this.left();
							} else if (this.direct == D_LEFT){
								this.direct = randomSpecifyNumber(D_RIGHT,D_UP,D_DOWN);
								if (this.x == canvas.width-DIV_WIDTH && this.y == 0){
									this.direct = randomSpecifyNumber(D_DOWN);
								}else if (this.x == canvas.width-DIV_WIDTH){
									this.direct = randomSpecifyNumber(D_UP,D_DOWN);
								} else if (this.y == 0){
									this.direct = randomSpecifyNumber(D_RIGHT,D_DOWN);
								}
								
								if (this.x == canvas.width-DIV_WIDTH && this.y == canvas.height-DIV_HEIGHT){
									this.direct = randomSpecifyNumber(D_UP);
								}else if (this.y == canvas.height-DIV_HEIGHT){
									this.direct = randomSpecifyNumber(D_RIGHT,D_UP);
								}
								this.right();
							} else if (this.direct == D_DOWN){
								this.direct = randomSpecifyNumber(D_LEFT,D_RIGHT,D_UP);
								if (this.y==0 && this.x==0){
									this.direct = randomSpecifyNumber(D_RIGHT);
								}else if (this.y==0){
									this.direct = randomSpecifyNumber(D_LEFT,D_RIGHT);
								}else if (this.x ==0){
									this.direct = randomSpecifyNumber(D_RIGHT,D_UP);
								}

								if (this.y ==0 && this.x == canvas.width-DIV_WIDTH)	{
									this.direct = randomSpecifyNumber(D_LEFT);
								}else if (this.x == canvas.width-DIV_WIDTH){
									this.direct = randomSpecifyNumber(D_LEFT,D_UP);
								}
								this.up();
							} else if (this.direct == D_UP){
								this.direct = randomSpecifyNumber(D_LEFT,D_RIGHT,D_DOWN);
								if (this.y==canvas.height-DIV_HEIGHT && this.x==0){
									this.direct = randomSpecifyNumber(D_RIGHT);
								}else if (this.y==canvas.height-DIV_HEIGHT){
									this.direct = randomSpecifyNumber(D_LEFT,D_RIGHT);
								}else if (this.x ==0){
									this.direct = randomSpecifyNumber(D_DOWN,D_RIGHT);
								}

								if (this.y ==canvas.height-DIV_HEIGHT && this.x == canvas.width-DIV_WIDTH)	{
									this.direct = randomSpecifyNumber(D_LEFT);
								}else if (this.x == canvas.width-DIV_WIDTH)	{
									this.direct = randomSpecifyNumber(D_LEFT,D_DOWN);
								}
								this.down();
							}
						}
						
						// 如果坦克在冰块上面  则减速
						if (STICK_ARR[i].type == BLOCK_ICE){
							this.dx = this.tempSpeed*0.6;
							this.dy = this.tempSpeed*0.6;
							this.isOnIce = true;
						}
						
						
						break;
					}else{
						this.isOnIce = false;
						this.dx = this.tempSpeed;
						this.dy = this.tempSpeed;
					}
				}
				return res;
			}

			// 检查敌方坦克是否与我方坦克相撞
			this.checkEnermyCollision = function(){
				var res = false;
				for (var i=0;i<PLAYER_TANK_ARR.length;i++){
					var collResult = isTwoRectangleHit(this,PLAYER_TANK_ARR[i]);
					res = collResult;
					if (collResult){
						if (this.direct == D_RIGHT){
							this.direct = randomSpecifyNumber(D_LEFT,D_UP,D_DOWN);
							if (this.x == 0 && this.y == 0){
								this.direct = randomSpecifyNumber(D_DOWN);
							}else if (this.x == 0){
								this.direct = randomSpecifyNumber(D_UP,D_DOWN);
							} else if (this.y == 0){
								this.direct = randomSpecifyNumber(D_LEFT,D_DOWN);
							}
							
							if (this.x == 0 && this.y == canvas.height-DIV_HEIGHT){
								this.direct = randomSpecifyNumber(D_UP);
							}else if (this.y == canvas.height-DIV_HEIGHT){
								this.direct = randomSpecifyNumber(D_LEFT,D_UP);
							}
							this.left();
						} else if (this.direct == D_LEFT){
							this.direct = randomSpecifyNumber(D_RIGHT,D_UP,D_DOWN);
							if (this.x == canvas.width-DIV_WIDTH && this.y == 0){
								this.direct = randomSpecifyNumber(D_DOWN);
							}else if (this.x == canvas.width-DIV_WIDTH){
								this.direct = randomSpecifyNumber(D_UP,D_DOWN);
							} else if (this.y == 0){
								this.direct = randomSpecifyNumber(D_RIGHT,D_DOWN);
							}
							
							if (this.x == canvas.width-DIV_WIDTH && this.y == canvas.height-DIV_HEIGHT){
								this.direct = randomSpecifyNumber(D_UP);
							}else if (this.y == canvas.height-DIV_HEIGHT){
								this.direct = randomSpecifyNumber(D_RIGHT,D_UP);
							}
							this.right();
						} else if (this.direct == D_DOWN){
							this.direct = randomSpecifyNumber(D_LEFT,D_RIGHT,D_UP);
							if (this.y==0 && this.x==0){
								this.direct = randomSpecifyNumber(D_RIGHT);
							}else if (this.y==0){
								this.direct = randomSpecifyNumber(D_LEFT,D_RIGHT);
							}else if (this.x ==0){
								this.direct = randomSpecifyNumber(D_RIGHT,D_UP);
							}

							if (this.y ==0 && this.x == canvas.width-DIV_WIDTH)	{
								this.direct = randomSpecifyNumber(D_LEFT);
							}else if (this.x == canvas.width-DIV_WIDTH){
								this.direct = randomSpecifyNumber(D_LEFT,D_UP);
							}
							this.up();
						} else if (this.direct == D_UP){
							this.direct = randomSpecifyNumber(D_LEFT,D_RIGHT,D_DOWN);
							if (this.y==canvas.height-DIV_HEIGHT && this.x==0){
								this.direct = randomSpecifyNumber(D_RIGHT);
							}else if (this.y==canvas.height-DIV_HEIGHT){
								this.direct = randomSpecifyNumber(D_LEFT,D_RIGHT);
							}else if (this.x ==0){
								this.direct = randomSpecifyNumber(D_DOWN,D_RIGHT);
							}

							if (this.y ==canvas.height-DIV_HEIGHT && this.x == canvas.width-DIV_WIDTH)	{
								this.direct = randomSpecifyNumber(D_LEFT);
							}else if (this.x == canvas.width-DIV_WIDTH)	{
								this.direct = randomSpecifyNumber(D_LEFT,D_DOWN);
							}
							this.down();
						}
						break;
					}
				}
				return res;
			}
			
			// 向上移动
			this.up = function(){
				if (!this.isFreeze){
					this.y -= this.dy;
				}
			}
			
			// 向下移动
			this.down = function(){
				if (!this.isFreeze){
					this.y += this.dy;
				}
			}
			
			// 向左移动
			this.left = function(){
				if (!this.isFreeze){
					this.x -= this.dx;
				}
			}
			
			// 向右移动
			this.right = function(){
				if (!this.isFreeze){
					this.x += this.dx;
				}
			}
		}

		// 我方坦克
		// id:唯一标识  x:x轴坐标  y:y轴坐标  w:宽度  h:高度  direct:运动方向  totalBlood:总血量   speed:移动速度  bombSpeed:子弹速度  bombDamage:子弹伤害   totalTrackBomb:总炮弹数    extendObj:扩展属性
		function PlayerTank(id,x,y,w,h,direct,totalBlood,speed,bombSpeed,bombDamage,totalTrackBomb,extendObj){
			this.id = id;
			this.x = x;
			this.y = y;
			this.direct = direct;
			// 总血量
			this.totalBlood = totalBlood;
			// 移动速度
			this.dx = speed;
			this.dy = speed;
			
			// 我方子弹速度和伤害
			this.bombSpeed = bombSpeed;
			this.bombDamage = bombDamage;
			
			// 追踪炮弹数
			this.totalTrackBomb = totalTrackBomb;
			
			// 扩展参数对象
			this.extendObj = extendObj;
			
			// 当前血量
			this.curBlood = this.totalBlood;
			
			// 坦克大小
			this.w = w;
			this.h = h;

			// 同时最多发几颗子弹
			this.totalBomb = 3;
			// 每隔几秒发射子弹
			this.shootInteval = 0.3;
			
			// 发射追踪炮弹间隔  单位:秒
			this.sendTrackBombInteval = 1;

			// 上一次发射子弹的时间  -1表示没有发射过
			this.lastShootCycle = -1;
			// 上一次发射追踪炮弹的时间  -1表示没有发射过
			this.lastSendTrackBombCycle = -1;
			
			// 吃五角星个数
			this.eatFiveStarNum = 0;
			// 上一次吃了铁锹的时间  -1表示没有吃过
			this.lastShovelCycle = -1;			
			// 上次吃了无敌的时间   -1表示没有发射过
			this.lastUnbeatableCycle = -1;
			// 上次吃了定时的时间   -1表示没有发射过
			this.lastFreezeCycle = -1;
			
			// 是否在冰块上面
			this.isOnIce = false;
			this.tempSpeed = speed;
			
			this.draw = function(){
				var srcPicInfo = PLAYER_DATA[this.id]["imgPos"][this.direct];
				ctx.drawImage(SPRITE_IMAGE,srcPicInfo.x,srcPicInfo.y,srcPicInfo.w,srcPicInfo.h,this.x,this.y,this.w,this.h);
			}
			
			this.update = function(){
				// 如果当前血量为0 则宣布死亡
				if (this.curBlood<=0){
					this.minusLife(1);
					PLAYER_TANK_ARR.removeArrElement(this);
					EXPLODE_ARR.push(new Explode(this.x,this.y));
					showSmallCanvasTips(TIPS_OBJ["dead"]);
				}

				if (this.y<0){
					this.y = 0;
				}
				if (this.y>canvas.height-DIV_HEIGHT){
					this.y = canvas.height-DIV_HEIGHT;
				}
				if (this.x<0){
					this.x = 0;
				}
				if (this.x>canvas.width-DIV_WIDTH){
					this.x = canvas.width-DIV_WIDTH;
				}
				
				// 吃到奖励物品处理
				this.checkBonusCollision();

				// 吃到铁锹20秒后恢复老窝形状
				if (this.lastShovelCycle != -1){
					if (G_CYCLE - this.lastShovelCycle >= 1200){
						homeBeWeaker();
						this.lastShovelCycle = -1;
					}
				}
				
				// 吃到无敌10秒后恢复
				if (this.lastUnbeatableCycle != -1){
					if (G_CYCLE - this.lastUnbeatableCycle >= 600){
						this.lastUnbeatableCycle = -1;
						this.curBlood = this.totalBlood;
					}
				}
				
				// 解冻地方坦克
				if (this.lastFreezeCycle != -1){
					if (G_CYCLE - this.lastFreezeCycle >= 600){
						this.lastFreezeCycle = -1;
						for (var j=0;j<ENERMY_ARR.length;j++){
							ENERMY_ARR[j].unfreeze();
						}
					}
				}
				
				// 自动发射追踪炮弹
				if (IS_AUTO_SEND_TRACK_BOMB){
					this.sendTrackBomb();
				}

				this.draw();
			}
			
			// 发射子弹
			this.shoot = function(){
				var curBomb = 0;
				for (var i=0;i<BOMB_ARR.length;i++){
					if(BOMB_ARR[i].id == this.id){
						curBomb++;
					}
				}
				
				// 检查发射子弹个数
				if (curBomb >= this.totalBomb){
					return;
				}
				
				// 检查发射子弹间隔
				if (this.lastShootCycle == -1){
					this.lastShootCycle = G_CYCLE;
					this.shootControll();
				}else{
					if ((G_CYCLE-this.lastShootCycle) / 60 >= this.shootInteval){
						this.lastShootCycle = G_CYCLE;
						this.shootControll();			
					}
				}
			}
			
			// 发射追踪炮弹
			this.sendTrackBomb = function(){
				if (this.totalTrackBomb<=0){
					return;
				}

				// 检查发射追踪炮弹间隔
				if (this.lastSendTrackBombCycle == -1){
					this.lastSendTrackBombCycle = G_CYCLE;
				}else{
					if ((G_CYCLE-this.lastSendTrackBombCycle) / 60 >= this.sendTrackBombInteval){
						this.lastSendTrackBombCycle = G_CYCLE;		
					}else{
						return;
					}
				}
				
				var x,y,w,h;
				var moveSpeed = 2;
				var damage = 60;
				var extendObj = {};
				extendObj.cycle = G_CYCLE;
				if (this.direct == D_LEFT){
					x =  this.x-TRACK_BOMB_HEIGHT;
					y =  this.y+DIV_HEIGHT/2-TRACK_BOMB_WIDTH/2;
					w =  TRACK_BOMB_HEIGHT;
					h =  TRACK_BOMB_WIDTH;
				}else if (this.direct == D_RIGHT){
					x =  this.x+DIV_WIDTH;
					y =  this.y+DIV_HEIGHT/2-TRACK_BOMB_WIDTH/2;
					w =  TRACK_BOMB_HEIGHT;
					h =  TRACK_BOMB_WIDTH;
				}else if (this.direct == D_UP){
					x =  this.x+DIV_WIDTH/2-TRACK_BOMB_WIDTH/2;
					y =  this.y-TRACK_BOMB_HEIGHT;
					w =  TRACK_BOMB_WIDTH;
					h =  TRACK_BOMB_HEIGHT;
				}else if (this.direct == D_DOWN){
					x =  this.x+DIV_WIDTH/2-TRACK_BOMB_WIDTH/2;
					y =  this.y+DIV_HEIGHT;
					w =  TRACK_BOMB_WIDTH;
					h =  TRACK_BOMB_HEIGHT;
				}
				
				TRACK_BOMB_ARR.push(new TrackBomb(this.id,x,y,w,h,this.direct,moveSpeed,damage,extendObj));
				this.totalTrackBomb--;
			}
			
			this.shootControll = function(){
				if (this.direct == D_LEFT){
					BOMB_ARR.push(new Bomb(this.id,this.x,this.y+DIV_HEIGHT/2-BOMB_HEIGHT/2,BOMB_WIDTH,BOMB_HEIGHT,this.direct,this.bombSpeed,this.bombDamage));
				}else if (this.direct == D_RIGHT){
					BOMB_ARR.push(new Bomb(this.id,this.x+DIV_WIDTH,this.y+DIV_HEIGHT/2-BOMB_HEIGHT/2,BOMB_WIDTH,BOMB_HEIGHT,this.direct,this.bombSpeed,this.bombDamage));
				}else if (this.direct == D_UP){
					BOMB_ARR.push(new Bomb(this.id,this.x+DIV_WIDTH/2-BOMB_WIDTH/2,this.y,BOMB_WIDTH,BOMB_HEIGHT,this.direct,this.bombSpeed,this.bombDamage));
				}else if (this.direct == D_DOWN){
					BOMB_ARR.push(new Bomb(this.id,this.x+DIV_WIDTH/2-BOMB_WIDTH/2,this.y+DIV_HEIGHT,BOMB_WIDTH,BOMB_HEIGHT,this.direct,this.bombSpeed,this.bombDamage));
				}
			}
			
			// 检查是否撞到障碍物
			this.checkWallCollision = function(){
				var res = false;
				for (var i=0;i<STICK_ARR.length;i++){
					var collResult = isTwoRectangleHit(this,STICK_ARR[i]);
					if (collResult){
						res = collResult;
						if (!STICK_ARR[i].canTankCross){
							if (this.direct == D_RIGHT){
								this.x -= this.dx;
							} else if (this.direct == D_LEFT){
								this.x += this.dx;
							} else if (this.direct == D_DOWN){
								this.y -= this.dy;
							} else if (this.direct == D_UP){
								this.y += this.dy;
							}
						}
						
						// 如果坦克在冰块上面  则减速
						if (STICK_ARR[i].type == BLOCK_ICE){
							this.dx = this.tempSpeed*0.8;
							this.dy = this.tempSpeed*0.8;
							this.isOnIce = true;
						}
						break;
					}else{
						this.isOnIce = false;
						this.dx = this.tempSpeed;
						this.dy = this.tempSpeed;
					}
				}
				return res;
			}

			// 检查是否撞到敌方坦克
			this.checkEnermyCollision = function(){
				var res = false;
				for (var i=0;i<ENERMY_ARR.length;i++){
					var collResult = isTwoRectangleHit(this,ENERMY_ARR[i]);
					res = collResult;
					if (collResult){
						if (this.direct == D_RIGHT){
							this.x -= this.dx;
						} else if (this.direct == D_LEFT){
							this.x += this.dx;
						} else if (this.direct == D_DOWN){
							this.y -= this.dy;
						} else if (this.direct == D_UP){
							this.y += this.dy;
						} 
						break;		
					}
				}
				return res;
			}
			
			// 检查是否撞到奖励物品
			this.checkBonusCollision = function(){
				for (var i=0;i<BONUS_ARR.length;i++){
					var collResult = isTwoRectangleHit(this,BONUS_ARR[i]);
					if (collResult){
						var bonusObj = BONUS_ARR[i];
						BONUS_ARR.splice(i,1);
						if (bonusObj.type == BONUS_BOMB){
							// 如果吃到炸弹,则让敌军坦克全部毁灭
							showSmallCanvasTips(TIPS_OBJ["bomb"]);
							for (var k=ENERMY_ARR.length-1;k>=0;k--){
								EXPLODE_ARR.push(new Explode(ENERMY_ARR[k].x,ENERMY_ARR[k].y));
								ENERMY_ARR.splice(k,1);
								ENERY_DEAD_NUM++;
							}
						}else if (bonusObj.type == BONUS_FIVE_STAR){
							this.eatFiveStarNum++;
							// 如果吃到一颗五角星 则子弹速度提升
							if (this.eatFiveStarNum <=2){
								this.bombSpeed += 3;
								showSmallCanvasTips(TIPS_OBJ["fiveStar"]);
							}

							// 如果累计吃到2颗五角星  则可以射穿钢板
							if (this.eatFiveStarNum >=2){
								this.bombDamage += 9999;
								showSmallCanvasTips(TIPS_OBJ["fiveStar2"]);
							}
						}else if (bonusObj.type == BONUS_SHOVEL){
							// 如果吃到铁锹  则让老窝变得更坚固
							homeBeStronger();
							this.lastShovelCycle = G_CYCLE;
							showSmallCanvasTips(TIPS_OBJ["shovel"]);
						}else if (bonusObj.type == BONUS_LIFE){
							// 如果吃到生命奖励物品,则玩家生命+1
							this.addLife(1);
							showSmallCanvasTips(TIPS_OBJ["life"]);
						}else if (bonusObj.type == BONUS_FREEZE){
							// 如果吃到定时器,则敌人坦克都定住10秒钟
							for (var j=0;j<ENERMY_ARR.length;j++){
								ENERMY_ARR[j].freeze();
							}
							this.lastFreezeCycle = G_CYCLE;
							showSmallCanvasTips(TIPS_OBJ["freeze"]);
						}else if (bonusObj.type == BONUS_UNBEATABLE){
							// 玩家无敌10秒钟
							this.curBlood = 999999;
							this.lastUnbeatableCycle = G_CYCLE;
							showSmallCanvasTips(TIPS_OBJ["unbeatable"]);
						}
						break;
					}
				}					
			}
			
			// 改变方向
			this.changeDirection = function(direct){
				this.direct = direct;
				if (this.direct == D_UP){
					if (this.y<0){
						this.y = 0;
						return;
					}
					this.y -= this.dy;					
				}else if (this.direct == D_DOWN){
					if (this.y>canvas.height-DIV_HEIGHT){
						this.y = canvas.height-DIV_HEIGHT;
						return;
					}
					this.y += this.dy;				
				}else if (this.direct == D_LEFT){
					if (this.x<0){
						this.x = 0;
						return;
					}
					this.x -= this.dx;				
				}else if (this.direct == D_RIGHT){
					if (this.x>canvas.width-DIV_WIDTH){
						this.x = canvas.width-DIV_WIDTH;
						return;
					}
					this.x += this.dx;				
				}
				
				if (this.checkWallCollision()){
					return;
				}
				
				if (this.checkEnermyCollision()){
					return;
				}
			}
			
			// 增加生命
			this.addLife = function(num){
				if (this.id == PLAYER1_ID_PREIFIX){
					PLAYER1_TOTAL_LIFE += num;
				}else{
					PLAYER2_TOTAL_LIFE += num;
				}
			}
			
			// 减少生命
			this.minusLife = function(num){
				if (this.id == PLAYER1_ID_PREIFIX){
					PLAYER1_TOTAL_LIFE--;
				}else{
					PLAYER2_TOTAL_LIFE--;
				}				
			}
			
			// 向上移动
			this.up = function(){
				this.direct = D_UP;
				if (this.y<0){
					this.y = 0;
					return;
				}

				this.y -= this.dy;
				
				if (this.checkWallCollision()){
					return;
				}
				
				if (this.checkEnermyCollision()){
					return;
				}
			}
			
			// 向下移动
			this.down = function(){
				this.direct = D_DOWN;
				if (this.y>canvas.height-DIV_HEIGHT){
					this.y = canvas.height-DIV_HEIGHT;
					return;
				}
				
				this.y += this.dy;
				if (this.checkWallCollision()){
					return;
				}
				
				if (this.checkEnermyCollision()){
					return;
				}
			}
			
			// 向左移动
			this.left = function(){
				this.direct = D_LEFT;
				if (this.x<0){
					this.x = 0;
					return;
				}
	
				this.x -= this.dx;
				if (this.checkWallCollision()){
					return;
				}
				
				if (this.checkEnermyCollision()){
					return;
				}
			}
			
			// 向右移动
			this.right = function(){
				this.direct = D_RIGHT;
				if (this.x>canvas.width-DIV_WIDTH){
					this.x = canvas.width-DIV_WIDTH;
					return;
				}

				this.x += this.dx;
				if (this.checkWallCollision()){
					return;
				}
				
				if (this.checkEnermyCollision()){
					return;
				}
			}
		}	

		// 子弹类
		// id:子弹唯一标识  x:x轴坐标  y:y轴坐标  w:宽度  h:高度  direct:运动方向  speed:移动速度   damage:子弹伤害 extendObj:扩展属性
		function Bomb(id,x,y,w,h,direct,speed,damage,extendObj){
			this.id=id;
			this.x=x;
			this.y=y;
			this.direct=direct;

			// 子弹速度
			this.dx = speed;
			this.dy = speed;
			// 子弹大小
			this.w = w;
			this.h = h;
			
			// 子弹伤害
			this.damage = damage;
			// 扩展参数对象
			this.extendObj = extendObj;
			
			this.draw = function(){
				ctx.beginPath();
				ctx.fillStyle="white";
				ctx.fillRect(this.x,this.y,this.w,this.h);
				ctx.closePath();
			}
			
			this.update = function(){
				this.draw();
				
				if (this.direct == D_LEFT){
					this.left();
				}else if (this.direct == D_RIGHT){
					this.right();
				}else if (this.direct == D_UP){
					this.up();
				}else if (this.direct == D_DOWN){
					this.down();
				}

				// 如果撞墙了 则清除之
				if (this.checkOutBoundary()){
					return;
				}
				
				// 如果撞到了砖块 则清除子弹和砖块
				if (this.checkShootWall()){
					return;
				}
				
				if (this.checkShootEnermyTank()){
					return;
				}
				
				if (this.checkShootMyTank()){
					return;
				}
			}
			
			// 如果子弹出界,移除之
			this.checkOutBoundary = function(){
				// 如果撞墙了 则清除之
				if (this.x<0 || this.x>canvas.width-this.w || this.y<0 || this.y>canvas.height-this.h){
					BOMB_ARR.removeArrElement(this);
				}
				return false;
			}
			
			// 如果子弹射中砖块,则移除砖块和子弹
			this.checkShootWall = function(){
				var res = false;
				for (var i=0;i<STICK_ARR.length;i++){
					var collResult = isTwoRectangleHit(this,STICK_ARR[i]);
					if (collResult){
						if (!STICK_ARR[i].canBombCross){
							res = collResult;
							BOMB_ARR.removeArrElement(this);
						}else{
							// 如果能穿过 nothing to do
						}
						
						// 检查鸟窝是否被攻击
						if (STICK_ARR[i].type == BLOCK_SOIL){
							var soilBlocks = BLOCK_OBJ[BLOCK_SOIL]["drawObj"].getBoundaryAsCanvasByArr(new Array([19,20,12,12],[19,19,13,13],[19,20,14,14]));
							for (var j=0;j<soilBlocks.length;j++){
								if (soilBlocks[j].x == STICK_ARR[i].x && soilBlocks[j].y == STICK_ARR[i].y){
									showSmallCanvasTips(TIPS_OBJ["attack"]);
									break;
								}
							}							
						}
						
						if (STICK_ARR[i].canDie){
							STICK_ARR[i].curBlood -= this.damage;
						}
						break;
					}
				}

				return res;
			}
			
			// 检查子弹是否射中敌人坦克
			this.checkShootEnermyTank = function(){
				var res = false;
				if (this.id.indexOf(PLAYER1_ID_PREIFIX) != -1 || this.id.indexOf(PLAYER2_ID_PREIFIX) != -1){
					for (var i=0;i<ENERMY_ARR.length;i++){
						var collResult = isTwoRectangleHit(this,ENERMY_ARR[i]);
						res = collResult;
						if (collResult){
							ENERMY_ARR[i].curBlood -= this.damage;
							BOMB_ARR.removeArrElement(this);
							break;
						}
					}
				}

				return res;			
			}
			
			// 检查子弹是否射中我方坦克
			this.checkShootMyTank = function(){
				var res = false;
				if (this.id.indexOf(ENERMY_TANK_ID_PREFIX) != -1){
					for (var i=0;i<PLAYER_TANK_ARR.length;i++){
						var collResult = isTwoRectangleHit(this,PLAYER_TANK_ARR[i]);
						res = collResult;
						if (collResult){
							PLAYER_TANK_ARR[i].curBlood -= this.damage;
							BOMB_ARR.removeArrElement(this);
							break;
						}
					}
				}

				return res;				
			}
			
			// 向上移动
			this.up = function(){
				this.y -= this.dy;
			}
			
			// 向下移动
			this.down = function(){
				this.y += this.dy;
			}
			
			// 向左移动
			this.left = function(){
				this.x -= this.dx;
			}
			
			// 向右移动
			this.right = function(){
				this.x += this.dx;
			}
		}
		
		// 追踪炮弹类
		// id:炮弹唯一标识  x:x轴坐标  y:y轴坐标   w:宽度   h:高度  direct:运动方向  speed:移动速度   damage:炮弹伤害 extendObj:扩展属性
		function TrackBomb(id,x,y,w,h,direct,speed,damage,extendObj){
			this.id=id;
			this.x=x;
			this.y=y;
			this.direct=direct;

			// 炮弹速度
			this.dx = speed;
			this.dy = speed;
			// 炮弹大小
			this.w = w;
			this.h = h;
			
			// 炮弹伤害
			this.damage = damage;
			// 扩展参数对象
			this.extendObj = extendObj;
			// 被锁定的坦克对象
			this.lockedEnermyObj = undefined;

			this.draw=function(){
				ctx.beginPath();

				// 与被锁定对象坦克的距离
				var xDistance = 0;
				var yDistance = 0;
				// 与被锁定对象坦克的夹角
				var jiaodu = -1;

				if (typeof(this.lockedEnermyObj) != "undefined"){
					xDistance = Math.abs(this.x - (this.lockedEnermyObj.x+this.lockedEnermyObj.w/2));
					yDistance = Math.abs(this.y - (this.lockedEnermyObj.y+this.lockedEnermyObj.h/2));
					var towPointDistance = Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));
					jiaodu = Math.acos(xDistance/towPointDistance);
					
					if (this.x < this.lockedEnermyObj.x && this.y > this.lockedEnermyObj.y){
						// 敌人在右上角  do nothing
					} else if (this.x > this.lockedEnermyObj.x && this.y > this.lockedEnermyObj.y){
						// 敌人在左上角
						jiaodu = Math.PI - jiaodu;
					} else if (this.x > this.lockedEnermyObj.x && this.y < this.lockedEnermyObj.y){
						// 敌人在左下角
						jiaodu = Math.PI + jiaodu;
					} else if (this.x < this.lockedEnermyObj.x && this.y < this.lockedEnermyObj.y){
						// 敌人在右下角
						jiaodu = 2*Math.PI - jiaodu;
					} else if (this.x == this.lockedEnermyObj.x){
						if (this.y > this.lockedEnermyObj.y){
							jiaodu = Math.PI/2;
						} else if (this.y < this.lockedEnermyObj.y){
							jiaodu = 3*Math.PI/2;
						}
					} else if (this.y == this.lockedEnermyObj.y){
						if (this.x > this.lockedEnermyObj.x){
							jiaodu = Math.PI;
						} else if (this.x < this.lockedEnermyObj.x){
							jiaodu = 0;
						}					
					}
				}

				ctx.save();
				ctx.translate(this.x,this.y);
				if (this.direct == D_UP){
					if (jiaodu >0 && jiaodu < Math.PI/2){
						ctx.rotate(Math.PI/2-jiaodu);
					} else if (jiaodu > Math.PI/2 && jiaodu < Math.PI){
						ctx.rotate(5*Math.PI/2-jiaodu);
					} else if (jiaodu > Math.PI && jiaodu < 3*Math.PI/2){
						ctx.rotate(5*Math.PI/2-jiaodu);
					} else if (jiaodu > 3*Math.PI/2 && jiaodu < 2*Math.PI){
						ctx.rotate(5*Math.PI/2-jiaodu);
					}
					
					if (jiaodu == 0){
						ctx.rotate(Math.PI/2);
					} else if (jiaodu == Math.PI){
						ctx.rotate(3*Math.PI/2);
					}else if  (jiaodu == 3*Math.PI/2){
						ctx.rotate(Math.PI);
					}
				} else if (this.direct == D_RIGHT){
					if (jiaodu >0 && jiaodu < Math.PI/2){
						ctx.rotate(2*Math.PI-jiaodu);
					} else if (jiaodu > Math.PI/2 && jiaodu < Math.PI){
						ctx.rotate(2*Math.PI-jiaodu);
					} else if (jiaodu > Math.PI && jiaodu < 3*Math.PI/2){
						ctx.rotate(2*Math.PI-jiaodu);
					} else if (jiaodu > 3*Math.PI/2 && jiaodu < 2*Math.PI){
						ctx.rotate(2*Math.PI-jiaodu);
					}
					
					if (jiaodu == Math.PI/2){
						ctx.rotate(3*Math.PI/2);
					} else if (jiaodu == Math.PI){
						ctx.rotate(Math.PI);
					}else if  (jiaodu == 3*Math.PI/2){
						ctx.rotate(Math.PI/2);
					}
				} else if (this.direct == D_DOWN){
					if (jiaodu >0 && jiaodu < Math.PI/2){
						ctx.rotate(3*Math.PI/2-jiaodu);
					} else if (jiaodu > Math.PI/2 && jiaodu < Math.PI){
						ctx.rotate(3*Math.PI/2-jiaodu);
					} else if (jiaodu > Math.PI && jiaodu < 3*Math.PI/2){
						ctx.rotate(3*Math.PI/2-jiaodu);
					} else if (jiaodu > 3*Math.PI/2 && jiaodu < 2*Math.PI){
						ctx.rotate(7*Math.PI/2-jiaodu);
					}
					
					if (jiaodu == 0){
						ctx.rotate(3*Math.PI/2);
					} else if (jiaodu == Math.PI/2){
						ctx.rotate(Math.PI);
					}else if  (jiaodu == Math.PI){
						ctx.rotate(Math.PI/2);
					}					
				} else if (this.direct == D_LEFT){
					if (jiaodu >0 && jiaodu < Math.PI/2){
						ctx.rotate(Math.PI-jiaodu);
					} else if (jiaodu > Math.PI/2 && jiaodu < Math.PI){
						ctx.rotate(Math.PI-jiaodu);
					} else if (jiaodu > Math.PI && jiaodu < 3*Math.PI/2){
						ctx.rotate(3*Math.PI-jiaodu);
					} else if (jiaodu > 3*Math.PI/2 && jiaodu < 2*Math.PI){
						ctx.rotate(3*Math.PI-jiaodu);
					}
					
					if (jiaodu == 0){
						ctx.rotate(Math.PI);
					} else if (jiaodu == Math.PI/2){
						ctx.rotate(Math.PI/2);
					}else if  (jiaodu == 3*Math.PI/2){
						ctx.rotate(3*Math.PI/2);
					}
				}

				var fillColor1 = "red";
				var fillColor2 = "blue";
				
				if (extendObj.cycle % 3 == 0){
					fillColor1 = "blue";
					fillColor2 = "red";
				}
				
				if (G_CYCLE % 10 == 0){
					extendObj.cycle++;
				}
				
				if (this.direct == D_UP){
					ctx.beginPath();
					ctx.fillStyle=fillColor1;
					ctx.fillRect(0,0,this.w,10);
					ctx.closePath();
					
					ctx.beginPath();
					ctx.fillStyle=fillColor2;
					ctx.fillRect(0,10,this.w,this.h-10);
					ctx.closePath();					
				}else if 	(this.direct == D_RIGHT){
					ctx.beginPath();
					ctx.fillStyle=fillColor1;
					ctx.fillRect(0,0,this.w-10,this.h);
					ctx.closePath();
					
					ctx.beginPath();
					ctx.fillStyle=fillColor2;
					ctx.fillRect(this.w-10,0,10,this.h);
					ctx.closePath();					
				}else if 	(this.direct == D_DOWN){
					ctx.beginPath();
					ctx.fillStyle=fillColor2;
					ctx.fillRect(0,0,this.w,this.h-10);
					ctx.closePath();			
		
					ctx.beginPath();
					ctx.fillStyle=fillColor1;
					ctx.fillRect(0,this.h-10,this.w,10);
					ctx.closePath();
				}else if 	(this.direct == D_LEFT){
					ctx.beginPath();
					ctx.fillStyle=fillColor1;
					ctx.fillRect(0,0,10,this.h);
					ctx.closePath();			
		
					ctx.beginPath();
					ctx.fillStyle=fillColor2;
					ctx.fillRect(10,0,this.w-10,this.h);
					ctx.closePath();
				}
				ctx.restore();
				ctx.closePath();
			}
			
			this.update = function(){
				this.draw();
				if (typeof(this.lockedEnermyObj) == "undefined"){
					// 找出最近的坦克
					var minDistance = 9999999;
					for (var i=0;i<ENERMY_ARR.length;i++){
						if (!ENERMY_ARR[i].beLocked){
							var tempDistance = getTwoPointDistance(this.x,this.y,ENERMY_ARR[i].x,ENERMY_ARR[i].y);
							if (tempDistance <= minDistance){
								minDistance = tempDistance;
								this.lockedEnermyObj = ENERMY_ARR[i];
							}
						}
					}
				}
				
				if (typeof(this.lockedEnermyObj) != "undefined"){
					this.lockedEnermyObj.beLocked = true;
					if (this.x < this.lockedEnermyObj.x+this.lockedEnermyObj.w/2){
						this.x += this.dx;
					}else if (this.x > this.lockedEnermyObj.x+this.lockedEnermyObj.w/2){
						this.x -= this.dx;
					}
					
					if (this.y < this.lockedEnermyObj.y+this.lockedEnermyObj.h/2){
						this.y += this.dy;
					}else if (this.y > this.lockedEnermyObj.y+this.lockedEnermyObj.h/2){
						this.y -= this.dy;
					}
					
					if (isTwoRectangleHit(this,this.lockedEnermyObj)){
						this.lockedEnermyObj.curBlood -= this.damage;
						this.lockedEnermyObj.beLocked = false;
						TRACK_BOMB_ARR.removeArrElement(this);
					}
				}else{
					if (this.direct == D_UP){
						this.y -= this.dy;
					} else if (this.direct == D_DOWN){
						this.y += this.dy;
					} else if (this.direct == D_LEFT){
						this.x -= this.dx;
					} else if (this.direct == D_RIGHT){
						this.x += this.dx;
					}
				}

				if (this.x <=0 || this.x >= canvas.width || this.y<=0 || this.y >= canvas.height){
					if (typeof(this.lockedEnermyObj) != "undefined"){
						this.lockedEnermyObj.beLocked = false;
						TRACK_BOMB_ARR.removeArrElement(this);
					}
				}
			}
		}			

		// 障碍物类   包括土块  金块  草丛  河流等
		// type:类型   x:x轴坐标   y:y轴坐标   w:宽度   h:高度    totalBlood:总血量   canBombCross:子弹是否能穿越该障碍物    canTankCross:坦克是否能穿越该障碍物   canDie:该障碍物是否会掉血最终死亡   extendObj:扩展属性
		function Stick(type,x,y,w,h,totalBlood,canBombCross,canTankCross,canDie,extendObj){
			// 类型
			this.type = type;
			// 当前坐标
			this.x = x;
			this.y = y;
			// 大小
			this.w = w;
			this.h = h;
			// 总血量
			this.totalBlood = totalBlood;
			// 子弹是否能穿过
			this.canBombCross = canBombCross;
			// 坦克是否能穿过
			this.canTankCross = canTankCross;
			// 被子弹射击时是否被清除  //如果为false时,则被射中后 不需要进行碰撞检测了
			this.canDie = canDie;
			// 扩展参数对象
			this.extendObj = extendObj;

			// 当前血量
			this.curBlood = this.totalBlood;
			
			this.draw = function(){
				ctx.beginPath();
				BLOCK_OBJ[this.type]["drawObj"].render(ctx,this.x,this.y);
				ctx.closePath();			
			}
			
			this.update = function(){
				if (this.canDie){
					if (this.curBlood <= 0){
						STICK_ARR.removeArrElement(this);
						if (this.type == BLOCK_HOME){
							console.log("你的老巢已被敌军炸毁,游戏结束");
							
							// 保存游戏结束那一刹那的截图
							saveBigScreenShot(canvas);
							saveSmallScreenShot(smallCanvas);
							
							IS_GAME_OVER = true;
							IS_GAME_START = false;
							IS_GAME_PAUSE = false;
							$("#pauseBtn").attr("disabled",true);
							showSmallCanvasTips(TIPS_OBJ["gameover"]);
						}
					}
				}
				this.draw();
			}
		}

		// 点类
		// x:x轴坐标   y:y轴坐标
		function Point(x,y){
			this.x = x;
			this.y = y;
		}
		
		// 爆炸类
		function Explode(x,y){
			this.x = x;
			this.y = y;
			this.picIndex = 0;
			this.curCycle = G_CYCLE;
			
			this.draw = function(){
				this.picIndex = this.picIndex%2;
				if (this.picIndex == 0){
					BLOCK_OBJ[BLOCK_EXPLODE1]["drawObj"].render(ctx,this.x+3,this.y+3);
				}else{
					BLOCK_OBJ[BLOCK_EXPLODE2]["drawObj"].render(ctx,this.x,this.y);
				}
			}
			
			this.update = function(){
				this.draw();
				if (G_CYCLE%20==0){
					this.picIndex++;
				}
				if (G_CYCLE - this.curCycle >= 30){
					EXPLODE_ARR.removeArrElement(this);
				}
			}
		}

		// 奖励物
		function Bonus(type,x,y,w,h){
            this.type = type;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.curCycle = G_CYCLE;
			
			// 用来描述奖励物品的停留时间
			this.totalBlood = 901;
			this.curBlood = this.totalBlood;
			
			this.draw = function(){
				BONUS_OBJ[this.type].render(ctx,x,y);
				
				// 绘制奖励物品快消失的计时器
				var remainTime = parseInt(this.curBlood/60);
				ctx.beginPath();
				ctx.strokeStyle="yellow";
				ctx.lineWidth = 1;
				ctx.font = "normal normal 10px 宋体";
				if (remainTime>=10){
					ctx.strokeText(remainTime,x+w-14,y+h-4);
				}else{
					ctx.strokeText(remainTime,x+w-10,y+h-4);
				}
				ctx.stroke();
				ctx.closePath();
			}
			
			this.update = function(){
				this.draw();
				if (this.curBlood <= 0){
					BONUS_ARR.removeArrElement(this);
					return;
				}
				this.curBlood--;
			}
		}
	
		// 动画精灵
		function Sprite(type, x, y, w, h, w0, h0, transparent){
			this.type = type;
			//原图该sprite的起始坐标
			this.x = x;
			this.y = y;
			
			// 原图该sprite的大小
			this.w = w;
			this.h = h;
			
			// 裁剪后的大小
			this.w0 = w0;
			this.h0 = h0;

			// 透明度(0-1) 0:完全透明  1:完全不透明
			this.transparent = transparent||1;

            // 绘图1  x0,y0 要绘制到画布哪个位置
			this.render = function(content,x0,y0){
				ctx.save();
				content.globalAlpha = this.transparent;
				content.drawImage(SPRITE_IMAGE,this.x,this.y,this.w,this.h,x0,y0,this.w0,this.h0);
				content.restore();
			}
			
            // 绘图2  x0,y0 要绘制到画布哪个位置  newW0,newH0:绘制的大小
			this.renderExt = function (content,x0,y0,newW0,newH0){
				ctx.save();
				content.globalAlpha = this.transparent;
				content.drawImage(SPRITE_IMAGE,this.x,this.y,this.w,this.h,x0,y0,newW0,newH0);
				content.restore();				
			}

			this.getBoundary = function(startRow, endRow, startCol, endCol){
				var points = new Array();
				for (var r=startRow;r<endRow;r++){
					for (c=startCol;c<endCol;c++){
						var x = (c-1)*this.w0;
						var y = (r-1)*this.h0;
						points.push(new Point(x,y));
					}
				}
				return points;
			}
			
			this.getBoundaryByArr = function(arr){
				var points = new Array();
				for (var i=0;i<arr.length;i++){
					var tempPoints = this.getBoundary(arr[i][0],arr[i][1],arr[i][2],arr[i][3]);
					for (j=0;j<tempPoints.length;j++){
						points.push(tempPoints[j]);
					}
				}
				
				return points;
			}
			
			this.getBoundaryAsCanvas = function(startRow, endRow, startCol, endCol){
				var points = new Array();
				var startX = (startCol-1)*DIV_WIDTH;
				var startY = (startRow-1)*DIV_HEIGHT;
				var width = (endCol-startCol+1)*DIV_WIDTH;
				var height = (endRow-startRow+1)*DIV_HEIGHT;
				
				for (var x=startX;x<startX+width;x+=this.w0){
					for (var y=startY;y<startY+height;y+=this.h0){
						points.push(new Point(x,y));
					}
				}
				
				return points;
			}

			this.getBoundaryAsCanvasByArr = function(arr){
				var points = new Array();
				for (var i=0;i<arr.length;i++){
					var tempPoints = this.getBoundaryAsCanvas(arr[i][0],arr[i][1],arr[i][2],arr[i][3]);
					for (j=0;j<tempPoints.length;j++){
						points.push(tempPoints[j]);
					}
				}
				
				return points;				
			}
		}

		// 文本类
		function Text(content,x,y,text,dx,dy,color,fontSize){
			this.content = content;
			this.x = x;
			this.y = y;
			this.text = text;
			this.color = color;
			this.fontSize = fontSize;
			this.dx = dx;
			this.dy = dy;
			this.time = 0;
			
			this.draw = function(){
				this.content.beginPath();
				this.content.strokeStyle=this.color;
				this.content.font = "normal normal "+fontSize+"px 宋体";
				this.content.strokeText(this.text,this.x,this.y);
				this.content.closePath();
			}
			
			// 每隔几秒闪烁一次  interval:间隔   单位:秒
			this.blink = function(interval){
				if (this.time % (interval*2) == 0 ){
					this.draw();
				}
				
				if (G_CYCLE %30 == 0){
					this.time++;
				}
			}
			
			// 不移动
			this.noMove = function(){
				this.draw();
			}
			
			// 从下往上移动
			this.moveToUp = function(){
				this.x += this.dx;
				this.y += this.dy;
				if (this.y <= canvas.height/2){
					this.dy = 0;
				}
				this.draw();
			}
			
			// 从上往下移动
			this.moveToDown = function(){
				this.x += this.dx;
				this.y += this.dy;
				if (this.y >= canvas.height/2){
					this.dy = 0;
				}
				this.draw();
			}
		}
		
		// 提示语类
		function Tips(content,x,y,text,level,totalBlood,color,fontSize){
			this.content = content;
			this.x = x;
			this.y = y;
			this.text = text;
			this.level = level;            // 显示级别  1-10 数字越大 级别越大 级别大的优先显示
			this.totalBlood = totalBlood;  // 停留时间 单位:帧数  一秒钟约60帧
			this.color = color||"red";
			this.fontSize = fontSize||25;
			
			this.curBlood = this.totalBlood;
			
			this.draw = function(){
				this.content.beginPath();
				this.content.strokeStyle=this.color;
				this.content.font = "normal normal "+fontSize+"px 宋体";
				this.content.strokeText(this.text,this.x,this.y);
				this.content.closePath();
			}
			
			this.update = function(){
				this.draw();
				
				this.curBlood--;
			}
			
			this.isDie = function(){
				if (this.curBlood<=0){
					return true;
				}else{
					return false;
				}
			}
		}
		
		// 读取本地json文件
		function readJsonByAjax(jsonFileName){
			$.ajax({
				url:jsonFileName,
				type:"GET",
				dataType:"json",
				success:function(data){
					return data;
				}
			})
		}
		
		// 加载关卡
		function loadMission(missionNum){
			STICK_ARR = new Array();

			// 绘图对象
			var drawSprite;
			var missionObj = MISSION_CFG[missionNum];
			if (typeof(missionObj) == "undefined"){
				missionNum = "default";
				missionObj = MISSION_CFG[missionNum];
			}

			if (!missionObj.hasOwnProperty("block_home")){
				// 鸟巢的位置
				missionObj["block_home"] = new Array([MAX_ROW,MAX_ROW,(MAX_COL+1)/2,(MAX_COL+1)/2]);
			}

			// 鸟巢四周砖块位置
			if (!missionObj.hasOwnProperty("block_soil")){
				missionObj["block_soil"] = HOME_AROUND_ARR;
			}else{
                for (var i=0;i<HOME_AROUND_ARR.length;i++){
                    if (!missionObj["block_soil"].isInArray(HOME_AROUND_ARR[i])){
                        missionObj["block_soil"] = missionObj["block_soil"].concat(HOME_AROUND_ARR);
                        break;
                    }
                }
			}
			
			var missionKeysArr = missionObj.getObjKeysAsArr();
			for (var i=0;i<missionKeysArr.length;i++){
				var blockName = missionKeysArr[i];
				drawSprite = BLOCK_OBJ[blockName]["drawObj"];
				if (typeof(drawSprite) == "undefined"){
					continue;
				}
				var blockArr = drawSprite.getBoundaryAsCanvasByArr(missionObj[blockName]);
				for (var j=0;j<blockArr.length;j++){
					var stick = new Stick(drawSprite.type, blockArr[j].x, blockArr[j].y, drawSprite.w0, drawSprite.h0, BLOCK_OBJ[blockName].totalBlood, BLOCK_OBJ[blockName].canBombCross, BLOCK_OBJ[blockName].canTankCross, BLOCK_OBJ[blockName].canDie);
					STICK_ARR.push(stick);
				}
			}
		}

		// 生成敌方坦克
		function createEnermy(){
			if (ENERY_USED_NUM >= TOTAL_ENERY_NUM){
				return;
			}
			if (ENERMY_ARR.length >=MAX_ENERY_LIVE_NUM){
				return;
			}
			
			if (MAX_ENERY_LIVE_NUM >= TOTAL_ENERY_NUM){
				return;
			}

			var posIndex = randomNumber(0,ENERMY_BORN_POS.getObjLength()-1);
			var posObj = ENERMY_BORN_POS.getObjValueByIndex(posIndex);
			var x = posObj.x;
			var y = posObj.y;

			// 检查当前位置是否有坦克 避免发生鬼畜现象
			var isCollision = false;
			for (var i=0;i<ENERMY_ARR.length;i++){
				if (isTwoRectangleHitExt(x,y,DIV_WIDTH,DIV_HEIGHT,ENERMY_ARR[i])){
					isCollision = true;
					break;
				}
			}
			
			if (isCollision){
				return;
			}
			
			var direct = D_DOWN;
			if (x == 0){
				direct = randomSpecifyNumber(D_DOWN,D_RIGHT);
			}else if (x == canvas.width-DIV_WIDTH){
				direct = randomSpecifyNumber(D_DOWN,D_LEFT);
			} else{
				direct = randomSpecifyNumber(D_DOWN,D_LEFT,D_RIGHT);
			}
			
			// 坦克类型数组
			var enermyTankTypeArr = ENERMY_PICTURE.getObjKeysAsArr();

			var eneryPicureIndex = randomNumber(0,ENERMY_PICTURE.getObjLength()-1);
			// 坦克类型
			var enermyTankType = enermyTankTypeArr[eneryPicureIndex];

			var eneryPicureObj = ENERMY_PICTURE.getObjValueByIndex(eneryPicureIndex);
			var extendObj = {};
			ENERMY_ARR.push(new EnermyTank(ENERMY_TANK_ID_PREFIX+(ENERY_USED_NUM+1), enermyTankType, x, y, DIV_WIDTH,DIV_HEIGHT,direct, eneryPicureObj.totalBlood, eneryPicureObj.speed,extendObj));
			ENERY_USED_NUM++;
		}

		// 绘制玩家生命耗尽后的gameover图片
		function drawPlayerDiePic(){
			if (PLAYER1_TOTAL_LIFE <= 0){
				// 如果玩家1生命耗尽  绘制图片
				var playerData = PLAYER_DATA.getObjValueByKey(PLAYER1_ID_PREIFIX);
				var playerPos = playerData["bornPos"];
				BLOCK_OBJ[BLOCK_GAMEOVER]["drawObj"].renderExt(ctx, playerPos.x, playerPos.y-DIV_HEIGHT, 2*DIV_WIDTH, 2*DIV_HEIGHT-10);				
			}
			
			if (PLAYER2_TOTAL_LIFE <= 0){
				// 如果玩家2生命耗尽  绘制图片
				var playerData = PLAYER_DATA.getObjValueByKey(PLAYER2_ID_PREIFIX);
				var playerPos = playerData["bornPos"];
				BLOCK_OBJ[BLOCK_GAMEOVER]["drawObj"].renderExt(ctx, playerPos.x-DIV_WIDTH, playerPos.y-DIV_HEIGHT, 2*DIV_WIDTH, 2*DIV_HEIGHT-10);
			}
		}
		
		// 生成我方坦克
		function createPlayerTank(){
			var totalBlood = 10;
			var speed = 6;
			
			if (GAME_MODE == SINGLE_PLAYER){
				if (PLAYER1_TOTAL_LIFE <= 0){
					console.log("玩家1生命耗尽,游戏结束!");
					
					// 保存游戏最后一次的截图
					saveBigScreenShot(canvas);
					saveSmallScreenShot(smallCanvas);
					
					IS_GAME_OVER = true;
					IS_GAME_START = false;
					IS_GAME_PAUSE = false;
					$("#pauseBtn").attr("disabled",true);
					return;
				}
			
				//单人模式
				if (PLAYER_TANK_ARR.length == 1){
					return;
				}
				
				if (PLAYER_TANK_ARR.length == 0){
					var playerData = PLAYER_DATA.getObjValueByKey(PLAYER1_ID_PREIFIX);
					var playerPos = playerData["bornPos"];
					PLAYER_TANK_ARR.push(new PlayerTank(PLAYER1_ID_PREIFIX, playerPos.x, playerPos.y, DIV_WIDTH,DIV_HEIGHT,D_UP, playerData.totalBlood, playerData.moveSpeed, playerData.bombSpeed, playerData.bombDamage,playerData.totalTrackBomb));
				}				
			} else{
				var playerData1 = PLAYER_DATA.getObjValueByKey(PLAYER1_ID_PREIFIX);
				var playerPos1 = playerData1["bornPos"];
				var playerData2 = PLAYER_DATA.getObjValueByKey(PLAYER2_ID_PREIFIX);
				var playerPos2 = playerData2["bornPos"];
			
				if (PLAYER1_TOTAL_LIFE <=0 && PLAYER2_TOTAL_LIFE<=0){
					console.log("玩家1和玩家2生命耗尽,游戏结束!");

					// 保存游戏最后一次的截图
					saveBigScreenShot(canvas);
					saveSmallScreenShot(smallCanvas);
					
					IS_GAME_OVER = true;
					IS_GAME_START = false;
					IS_GAME_PAUSE = false;
					$("#pauseBtn").attr("disabled",true);
					return;
				}
			
				// 双人模式
				if (PLAYER_TANK_ARR.length == 2){
					return;
				}
				
				if (PLAYER_TANK_ARR.length == 0){
					if (PLAYER1_TOTAL_LIFE > 0){
						PLAYER_TANK_ARR.push(new PlayerTank(PLAYER1_ID_PREIFIX, playerPos1.x, playerPos1.y, DIV_WIDTH,DIV_HEIGHT,D_UP, playerData1.totalBlood, playerData1.moveSpeed, playerData1.bombSpeed, playerData1.bombDamage, playerData1.totalTrackBomb));
					}
					if (PLAYER2_TOTAL_LIFE > 0){
						PLAYER_TANK_ARR.push(new PlayerTank(PLAYER2_ID_PREIFIX, playerPos2.x, playerPos2.y, DIV_WIDTH,DIV_HEIGHT,D_UP, playerData2.totalBlood, playerData2.moveSpeed, playerData2.bombSpeed, playerData2.bombDamage, playerData2.totalTrackBomb));
					}
				}else{
					for (var i=0;i<PLAYER_TANK_ARR.length;i++){
						if (PLAYER_TANK_ARR[i].id == PLAYER1_ID_PREIFIX){
							if (PLAYER2_TOTAL_LIFE > 0){
								PLAYER_TANK_ARR.push(new PlayerTank(PLAYER2_ID_PREIFIX, playerPos2.x, playerPos2.y, DIV_WIDTH,DIV_HEIGHT,D_UP, playerData2.totalBlood, playerData2.moveSpeed, playerData2.bombSpeed, playerData2.bombDamage, playerData2.totalTrackBomb));
								break;
							}
						}else if (PLAYER_TANK_ARR[i].id == PLAYER2_ID_PREIFIX){
							if (PLAYER1_TOTAL_LIFE > 0){
								PLAYER_TANK_ARR.push(new PlayerTank(PLAYER1_ID_PREIFIX, playerPos1.x, playerPos1.y, DIV_WIDTH,DIV_HEIGHT,D_UP, playerData1.totalBlood, playerData1.moveSpeed, playerData1.bombSpeed, playerData1.bombDamage, playerData1.totalTrackBomb));
								break;
							}
						}
					}
				}				
			}
		}
		
		// 生成奖励物品
		function createBonus(){
			if (BONUS_ARR.length >=1){
				return;
			}

            var bonusTypeArr = BONUS_OBJ.getObjKeysAsArr();
			var objIndex = randomNumber(0,BONUS_OBJ.getObjLength()-1);
            var bonusType = bonusTypeArr[objIndex];

			var isFindRightPos = false;
			while (true){
				var x = randomNumber(DIV_WIDTH*2,canvas.width-2*DIV_WIDTH);
				var y = randomNumber(DIV_HEIGHT*2,canvas.height-2*DIV_HEIGHT);
				
				for (var i=0;i<ENERMY_ARR.length;i++){
					if (!isTwoRectangleHitExt(x,y,DIV_WIDTH,DIV_HEIGHT,ENERMY_ARR[i])){
						isFindRightPos = true;
						break;
					}
				}
				
				if (!isFindRightPos){
					for (var j=0;j<PLAYER_TANK_ARR.length;j++){
						if (!isTwoRectangleHitExt(x,y,DIV_WIDTH,DIV_HEIGHT,PLAYER_TANK_ARR[j])){
							isFindRightPos = true;
							break;
						}
					}					
				}
				if (isFindRightPos){
					break;
				}
			}

			if (isFindRightPos){
				BONUS_ARR.push(new Bonus(bonusType,x,y,DIV_WIDTH,DIV_HEIGHT));
			}
		}
		
		// 检查我方子弹跟敌方子弹是否碰撞,如果是则移除它们
		function checkBombMutualCollision(){
			for (var i=0;i<BOMB_ARR.length;i++){
				for (var j=i+1;j<BOMB_ARR.length;j++){
					if ((BOMB_ARR[i].id.indexOf(PLAYER1_ID_PREIFIX) != -1 && BOMB_ARR[j].id.indexOf(ENERMY_TANK_ID_PREFIX) != -1)
					||(BOMB_ARR[j].id.indexOf(PLAYER1_ID_PREIFIX) != -1 && BOMB_ARR[i].id.indexOf(ENERMY_TANK_ID_PREFIX) != -1)){
						if (isTwoRectangleHit(BOMB_ARR[i],BOMB_ARR[j])){
							var bombObj1 = BOMB_ARR[i];
							var bombObj2 = BOMB_ARR[j];
							
							BOMB_ARR.removeArrElement(bombObj1);
							BOMB_ARR.removeArrElement(bombObj2);
						}
					}
				}
			}		
		}
		
		// 检查敌人坦克之间的碰撞
		function checkEnermyMutualCollision(){
			for (var i=0;i<ENERMY_ARR.length;i++){
				for (var j=i+1;j<ENERMY_ARR.length;j++){
					var result = isTwoRectangleHit(ENERMY_ARR[i],ENERMY_ARR[j]);
					if (result){
						var iX = ENERMY_ARR[i].x, iY = ENERMY_ARR[i].y, iDirect = ENERMY_ARR[i].direct, iID = ENERMY_ARR[i].id;
						var jX = ENERMY_ARR[j].x, jY = ENERMY_ARR[j].y, jDirect = ENERMY_ARR[j].direct, jID = ENERMY_ARR[j].id;
						if (iDirect == D_RIGHT){
							iDirect = randomSpecifyNumber(D_LEFT,D_UP,D_DOWN);
							if (iX == 0 && iY == 0){
								iDirect = randomSpecifyNumber(D_DOWN);
							}else if (iX == 0){
								iDirect = randomSpecifyNumber(D_UP,D_DOWN);
							} else if (iY == 0){
								iDirect = randomSpecifyNumber(D_LEFT,D_DOWN);
							}
							
							if (iX == 0 && iY == canvas.height-DIV_HEIGHT){
								iDirect = randomSpecifyNumber(D_UP);
							}else if (iY == canvas.height-DIV_HEIGHT){
								iDirect = randomSpecifyNumber(D_LEFT,D_UP);
							}
							ENERMY_ARR[i].left();
						} else if (iDirect == D_LEFT){
							iDirect = randomSpecifyNumber(D_RIGHT,D_UP,D_DOWN);
							if (iX == canvas.width-DIV_WIDTH && iY == 0){
								iDirect = randomSpecifyNumber(D_DOWN);
							}else if (iX == canvas.width-DIV_WIDTH){
								iDirect = randomSpecifyNumber(D_UP,D_DOWN);
							} else if (iY == 0){
								iDirect = randomSpecifyNumber(D_RIGHT,D_DOWN);
							}
							
							if (iX == canvas.width-DIV_WIDTH && iY == canvas.height-DIV_HEIGHT){
								iDirect = randomSpecifyNumber(D_UP);
							}else if (iY == canvas.height-DIV_HEIGHT){
								iDirect = randomSpecifyNumber(D_RIGHT,D_UP);
							}
							ENERMY_ARR[i].right();
						} else if (iDirect == D_DOWN){
							iDirect = randomSpecifyNumber(D_LEFT,D_RIGHT,D_UP);
							if (iY==0 && iX==0){
								iDirect = randomSpecifyNumber(D_RIGHT);
							}else if (iY==0){
								iDirect = randomSpecifyNumber(D_LEFT,D_RIGHT);
							}else if (iX ==0){
								iDirect = randomSpecifyNumber(D_RIGHT,D_UP);
							}

							if (iY ==0 && iX == canvas.width-DIV_WIDTH)	{
								iDirect = randomSpecifyNumber(D_LEFT);
							}else if (iX == canvas.width-DIV_WIDTH){
								iDirect = randomSpecifyNumber(D_LEFT,D_UP);
							}
							ENERMY_ARR[i].up();
						} else if (iDirect == D_UP){
							iDirect = randomSpecifyNumber(D_LEFT,D_RIGHT,D_DOWN);
							if (iY==canvas.height-DIV_HEIGHT && iX==0){
								iDirect = randomSpecifyNumber(D_RIGHT);
							}else if (iY==canvas.height-DIV_HEIGHT){
								iDirect = randomSpecifyNumber(D_LEFT,D_RIGHT);
							}else if (iX ==0){
								iDirect = randomSpecifyNumber(D_DOWN,D_RIGHT);
							}

							if (iY ==canvas.height-DIV_HEIGHT && iX == canvas.width-DIV_WIDTH){
								iDirect = randomSpecifyNumber(D_LEFT);
							}else if (iX == canvas.width-DIV_WIDTH)	{
								iDirect = randomSpecifyNumber(D_LEFT,D_DOWN);
							}
							ENERMY_ARR[i].down();
						}
						

                        if (jDirect == D_RIGHT){
                            jDirect = randomSpecifyNumber(D_LEFT,D_UP,D_DOWN);
                            if (jX == 0 && jY == 0){
                                jDirect = randomSpecifyNumber(D_DOWN);
                            }else if (jX == 0){
                                jDirect = randomSpecifyNumber(D_UP,D_DOWN);
                            } else if (jY == 0){
                                jDirect = randomSpecifyNumber(D_LEFT,D_DOWN);
                            }
                            
                            if (jX == 0 && jY == canvas.height-DIV_HEIGHT){
                                jDirect = randomSpecifyNumber(D_UP);
                            }else if (jY == canvas.height-DIV_HEIGHT){
                                jDirect = randomSpecifyNumber(D_LEFT,D_UP);
                            }
                            ENERMY_ARR[j].left();
                        } else if (jDirect == D_LEFT){
                            jDirect = randomSpecifyNumber(D_RIGHT,D_UP,D_DOWN);
                            if (jX == canvas.width-DIV_WIDTH && jY == 0){
                                jDirect = randomSpecifyNumber(D_DOWN);
                            }else if (jX == canvas.width-DIV_WIDTH){
                                jDirect = randomSpecifyNumber(D_UP,D_DOWN);
                            } else if (jY == 0){
                                jDirect = randomSpecifyNumber(D_RIGHT,D_DOWN);
                            }
                            
                            if (jX == canvas.width-DIV_WIDTH && jY == canvas.height-DIV_HEIGHT){
                                jDirect = randomSpecifyNumber(D_UP);
                            }else if (jY == canvas.height-DIV_HEIGHT){
                                jDirect = randomSpecifyNumber(D_RIGHT,D_UP);
                            }
                            ENERMY_ARR[j].right();
                        } else if (jDirect == D_DOWN){
                            jDirect = randomSpecifyNumber(D_LEFT,D_RIGHT,D_UP);
                            if (jY==0 && jX==0){
                                jDirect = randomSpecifyNumber(D_RIGHT);
                            }else if (jY==0){
                                jDirect = randomSpecifyNumber(D_LEFT,D_RIGHT);
                            }else if (jX ==0){
                                jDirect = randomSpecifyNumber(D_RIGHT,D_UP);
                            }

                            if (jY ==0 && jX == canvas.width-DIV_WIDTH)	{
                                jDirect = randomSpecifyNumber(D_LEFT);
                            }else if (jX == canvas.width-DIV_WIDTH){
                                jDirect = randomSpecifyNumber(D_LEFT,D_UP);
                            }
                            ENERMY_ARR[j].up();
                        } else if (jDirect == D_UP){
                            jDirect = randomSpecifyNumber(D_LEFT,D_RIGHT,D_DOWN);
                            if (jY==canvas.height-DIV_HEIGHT && jX==0){
                                jDirect = randomSpecifyNumber(D_RIGHT);
                            }else if (jY==canvas.height-DIV_HEIGHT){
                                jDirect = randomSpecifyNumber(D_LEFT,D_RIGHT);
                            }else if (jX ==0){
                                jDirect = randomSpecifyNumber(D_DOWN,D_RIGHT);
                            }

                            if (jY ==canvas.height-DIV_HEIGHT && jX == canvas.width-DIV_WIDTH){
                                jDirect = randomSpecifyNumber(D_LEFT);
                            }else if (jX == canvas.width-DIV_WIDTH)	{
                                jDirect = randomSpecifyNumber(D_LEFT,D_DOWN);
                            }
                            ENERMY_ARR[j].down();
                        }

						ENERMY_ARR[i].direct = iDirect;
						ENERMY_ARR[j].direct = jDirect;
					}
				}
			}		
		}

		// 检查玩家与玩家是否碰撞
		function checkPlayerMutualCollision(){
			if (PLAYER_TANK_ARR.length > 1){
				for (var i=0;i<PLAYER_TANK_ARR.length;i++){
					for (var j=i+1;j<PLAYER_TANK_ARR.length;j++){
						var isCollision = isTwoRectangleHit(PLAYER_TANK_ARR[i],PLAYER_TANK_ARR[j]);
						if (isCollision){
							var player1;
							var player2;
							if (PLAYER_TANK_ARR[i].id == PLAYER1_ID_PREIFIX){
								player1 = PLAYER_TANK_ARR[i];
								player2 = PLAYER_TANK_ARR[j];
							}else{
								player1 = PLAYER_TANK_ARR[j];
								player2 = PLAYER_TANK_ARR[i];							
							}
						
							var iDirect = player1.direct;
							var jDirect = player2.direct;
							if (IS_PLAYER1_MOVE){
								if (iDirect == D_RIGHT){
									player1.x -= player1.dx;
								} else if (iDirect == D_LEFT){
									player1.x += player1.dx;
								} else if (iDirect == D_DOWN){
									player1.y -= player1.dy;
								} else if (iDirect == D_UP){
									player1.y += player1.dy;
								}
							}
							
							if (IS_PLAYER2_MOVE){
								if (jDirect == D_RIGHT){
									player2.x -= player2.dx;
								} else if (jDirect == D_LEFT){
									player2.x += player2.dx;
								} else if (jDirect == D_DOWN){
									player2.y -= player2.dy;
								} else if (jDirect == D_UP){
									player2.y += player2.dy;
								}
							}
						}
					}
				}
			}
		}
		
		// 检查敌人坦克与我方坦克的碰撞
		function checkEnermyToPlayerCollison(){
			for (var j=0;j<ENERMY_ARR.length;j++){
				for (var i=0;i<PLAYER_TANK_ARR.length;i++){
					var collResult = isTwoRectangleHit(ENERMY_ARR[j], PLAYER_TANK_ARR[i]);
					if (collResult){

						var enermy = ENERMY_ARR[j];
						// 坦克处理
						var nexDirect;
						if (enermy.direct == D_RIGHT){
							nexDirect = randomSpecifyNumber(D_LEFT,D_UP,D_DOWN);
							if (enermy.x == 0 && enermy.y == 0){
								nexDirect = randomSpecifyNumber(D_DOWN);
							}else if (enermy.x == 0){
								nexDirect = randomSpecifyNumber(D_UP,D_DOWN);
							} else if (enermy.y == 0){
								nexDirect = randomSpecifyNumber(D_LEFT,D_DOWN);
							}
							
							if (enermy.x == 0 && enermy.y == canvas.height-DIV_HEIGHT){
								nexDirect = randomSpecifyNumber(D_UP);
							}else if (enermy.y == canvas.height-DIV_HEIGHT){
								nexDirect = randomSpecifyNumber(D_LEFT,D_UP);
							}
							enermy.left();
						} else if (enermy.direct == D_LEFT){
							nexDirect = randomSpecifyNumber(D_RIGHT,D_UP,D_DOWN);
							if (enermy.x == canvas.width-DIV_WIDTH && enermy.y == 0){
								nexDirect = randomSpecifyNumber(D_DOWN);
							}else if (enermy.x == canvas.width-DIV_WIDTH){
								nexDirect = randomSpecifyNumber(D_UP,D_DOWN);
							} else if (enermy.y == 0){
								nexDirect = randomSpecifyNumber(D_RIGHT,D_DOWN);
							}
							
							if (enermy.x == canvas.width-DIV_WIDTH && enermy.y == canvas.height-DIV_HEIGHT){
								nexDirect = randomSpecifyNumber(D_UP);
							}else if (enermy.y == canvas.height-DIV_HEIGHT){
								nexDirect = randomSpecifyNumber(D_RIGHT,D_UP);
							}
							enermy.right();
						} else if (enermy.direct == D_DOWN){
							nexDirect = randomSpecifyNumber(D_LEFT,D_RIGHT,D_UP);
							if (enermy.y==0 && enermy.x==0){
								nexDirect = randomSpecifyNumber(D_RIGHT);
							}else if (enermy.y==0){
								nexDirect = randomSpecifyNumber(D_LEFT,D_RIGHT);
							}else if (enermy.x ==0){
								nexDirect = randomSpecifyNumber(D_RIGHT,D_UP);
							}

							if (enermy.y ==0 && enermy.x == canvas.width-DIV_WIDTH)	{
								nexDirect = randomSpecifyNumber(D_LEFT);
							}else if (enermy.x == canvas.width-DIV_WIDTH){
								nexDirect = randomSpecifyNumber(D_LEFT,D_UP);
							}
							enermy.up();
						} else if (enermy.direct == D_UP){
							nexDirect = randomSpecifyNumber(D_LEFT,D_RIGHT,D_DOWN);
							if (enermy.y==canvas.height-DIV_HEIGHT && enermy.x==0){
								nexDirect = randomSpecifyNumber(D_RIGHT);
							}else if (enermy.y==canvas.height-DIV_HEIGHT){
								nexDirect = randomSpecifyNumber(D_LEFT,D_RIGHT);
							}else if (enermy.x ==0){
								nexDirect = randomSpecifyNumber(D_DOWN,D_RIGHT);
							}

							if (enermy.y ==canvas.height-DIV_HEIGHT && enermy.x == canvas.width-DIV_WIDTH)	{
								nexDirect = randomSpecifyNumber(D_LEFT);
							}else if (enermy.x == canvas.width-DIV_WIDTH)	{
								nexDirect = randomSpecifyNumber(D_LEFT,D_DOWN);
							}
							enermy.down();
						}
						
						enermy.direct = nexDirect;
						if (nexDirect == D_LEFT){
							enermy.left();
						} else if (nexDirect == D_RIGHT){
							enermy.right();
						}else if (nexDirect == D_UP){
							enermy.up();
						}else if (nexDirect == D_DOWN){
							enermy.down();
						}
						
						break;		
					}
				}
			}
		}
		
		// 鸟巢加固
		function homeBeStronger(){
			var soilObj = BLOCK_OBJ[BLOCK_SOIL]["drawObj"];
			var soilBlocks = soilObj.getBoundaryAsCanvasByArr(HOME_AROUND_ARR);
			for (var i=0;i<soilBlocks.length;i++){
				for (var j=STICK_ARR.length-1;j>=0;j--){
					if (STICK_ARR[j].type == BLOCK_SOIL){
						if (soilBlocks[i].x == STICK_ARR[j].x && soilBlocks[i].y == STICK_ARR[j].y){
							STICK_ARR.splice(j,1);
						}
					}
				}
			}
			
			var goldObj = BLOCK_OBJ[BLOCK_GOLD]["drawObj"];
			var goldBlocks = goldObj.getBoundaryAsCanvasByArr(HOME_AROUND_ARR);
			for (var i=0;i<goldBlocks.length;i++){
				for (var j=STICK_ARR.length-1;j>=0;j--){
					if (STICK_ARR[j].type == BLOCK_GOLD){
						if (goldBlocks[i].x == STICK_ARR[j].x && goldBlocks[i].y == STICK_ARR[j].y){
							STICK_ARR.splice(j,1);
						}
					}
				}
			}
			
			for (var i=0;i<goldBlocks.length;i++){
				var stick = new Stick(goldObj.type,goldBlocks[i].x,goldBlocks[i].y,goldObj.w0,goldObj.h0,BLOCK_OBJ[BLOCK_GOLD].totalBlood, BLOCK_OBJ[BLOCK_GOLD].canBombCross, BLOCK_OBJ[BLOCK_GOLD].canTankCross, goldObj.canDie);
				STICK_ARR.push(stick);
			}			
		}
		
		// 鸟巢破烂
		function homeBeWeaker(){
			var goldObj = BLOCK_OBJ[BLOCK_GOLD]["drawObj"];
			var goldBlocks = goldObj.getBoundaryAsCanvasByArr(HOME_AROUND_ARR);
			for (var i=0;i<goldBlocks.length;i++){
				for (var j=STICK_ARR.length-1;j>=0;j--){
					if (STICK_ARR[j].type == BLOCK_GOLD){
						if (goldBlocks[i].x == STICK_ARR[j].x && goldBlocks[i].y == STICK_ARR[j].y){
							STICK_ARR.splice(j,1);
						}
					}
				}
			}

			var soilObj = BLOCK_OBJ[BLOCK_SOIL]["drawObj"];
			var soilBlocks = soilObj.getBoundaryAsCanvasByArr(HOME_AROUND_ARR);
			for (var i=0;i<soilBlocks.length;i++){
				var stick = new Stick(soilObj.type,soilBlocks[i].x,soilBlocks[i].y,soilObj.w0,soilObj.h0,BLOCK_OBJ[BLOCK_SOIL].totalBlood, BLOCK_OBJ[BLOCK_SOIL].canBombCross, BLOCK_OBJ[BLOCK_SOIL].canTankCross, BLOCK_OBJ[BLOCK_SOIL].canDie);
				STICK_ARR.push(stick);
			}			
		}
		
		// 绘制右侧canvas画面
		function drawRightCanvas(){
			// FPS
			new Text(smallCtx,130, 15, FPS, 0, 0, "rgb(0,255,0)", 16).noMove();
			
			// 剩余坦克数
			var remainEnermyTankNum = TOTAL_ENERY_NUM-ENERY_DEAD_NUM;
			var i = 0;
			for (var y=SMALL_DIV_HEIGHT-15;y<SMALL_DIV_HEIGHT+SMALL_DIV_HEIGHT*7;y+=SMALL_DIV_HEIGHT+4){
				for (var x=SMALL_DIV_WIDTH-2;x<SMALL_DIV_WIDTH+SMALL_DIV_WIDTH*3;x+=SMALL_DIV_WIDTH+4){
					if (i < remainEnermyTankNum){
						//smallCtx.drawImage(SPRITE_IMAGE,110,227,15,15,x,y,SMALL_DIV_WIDTH,SMALL_DIV_HEIGHT);
						smallCtx.drawImage(SPRITE_IMAGE,232,237,9,8,x,y,SMALL_DIV_WIDTH,SMALL_DIV_HEIGHT);
					}
					i++;
				}
			}
			
			// 玩家生命
			smallCtx.drawImage(SPRITE_IMAGE,232,348,18,9,10,263,30,20);	
			new Text(smallCtx,44,280,PLAYER1_TOTAL_LIFE,0,0,"rgb(0,255,0)",18).noMove();
			
			smallCtx.drawImage(SPRITE_IMAGE,232,372,18,9,85,263,30,20);
			new Text(smallCtx,117,280,PLAYER2_TOTAL_LIFE,0,0,"rgb(0,255,0)",18).noMove();
			
			// 关卡数
			new Text(smallCtx,40,320,"第"+MISSION+"关",0,0,"rgb(255,255,0)",23).noMove();
			
			if (PLAYER2_TOTAL_LIFE <= 0){
				CUR_PLAYER = PLAYER1_ID_PREIFIX;
			}
			
			if (PLAYER1_TOTAL_LIFE <= 0){
				CUR_PLAYER = PLAYER2_ID_PREIFIX;
			}
			
			var player = getPlayerTankInfo(CUR_PLAYER);
			
			var startX = 10;
			var startY = 350;
			var dy = 20;
			var fontColor = "rgb(0,255,255)";
			var fontSize = 16;
			if (typeof(player) != "undefined"){
				new Text(smallCtx,startX, startY, "玩家坦克:"                         , 0, 0, fontColor, fontSize).noMove();
				// 我军坦克当前运动方向
				var srcPicInfo = PLAYER_DATA[CUR_PLAYER]["imgPos"][player.direct];
				smallCtx.drawImage(SPRITE_IMAGE,srcPicInfo.x,srcPicInfo.y,srcPicInfo.w,srcPicInfo.h,85,startY-12,15,15);
				
				new Text(smallCtx,startX, startY+dy*1, "当前血量:"+player.curBlood, 0, 0, fontColor, fontSize).noMove();
				new Text(smallCtx,startX, startY+dy*2, "移动速度:"+player.dx,         0, 0, fontColor, fontSize).noMove();
				new Text(smallCtx,startX, startY+dy*3, "射击速度:"+player.bombSpeed,  0, 0, fontColor, fontSize).noMove();
				new Text(smallCtx,startX, startY+dy*4, "子弹伤害:"+player.bombDamage, 0, 0, fontColor, fontSize).noMove();
				new Text(smallCtx,startX, startY+dy*5, "剩余炮弹:"+player.totalTrackBomb, 0, 0, fontColor, fontSize).noMove();
				
				// 鸟巢加成效果
				new Text(smallCtx,startX, startY+dy*6, "效果加成: ",                   0, 0, fontColor, fontSize).noMove();
				var fiveStarNum = player.eatFiveStarNum;
				if (fiveStarNum>=1){
					BONUS_OBJ[BONUS_FIVE_STAR].renderExt(smallCtx,85,startY+dy*6-13,15,15);
				}
				
				var lastUnbeatableCycle = player.lastUnbeatableCycle;
				if ( lastUnbeatableCycle != -1){
					BONUS_OBJ[BONUS_UNBEATABLE].renderExt(smallCtx,101,startY+dy*6-13,15,15);
				}
				
				var lastFreezeCycle = player.lastFreezeCycle;
				if ( lastFreezeCycle != -1){
					BONUS_OBJ[BONUS_FREEZE].renderExt(smallCtx,117,startY+dy*6-13,15,15);
				}
				
				var eatShovel = player.lastShovelCycle;
				if (eatShovel != -1){
					BONUS_OBJ[BONUS_SHOVEL].renderExt(smallCtx,133,startY+dy*6-13,15,15);
				}

				// 获取最近,最远敌人的距离
				var nearestEnermy=999999,farthestEnermy=0;
				for (var i=0;i<ENERMY_ARR.length;i++){
					var tempDis = getTwoPointDistance(ENERMY_ARR[i].x,ENERMY_ARR[i].y,player.x,player.y);
					if (tempDis >= farthestEnermy){
						farthestEnermy = tempDis;
					}
					
					if (tempDis <= nearestEnermy){
						nearestEnermy = tempDis;
					}
				}
				new Text(smallCtx,startX, startY+dy*7, "最近敌人:"+parseInt(nearestEnermy), 0, 0, fontColor, fontSize).noMove();
				new Text(smallCtx,startX, startY+dy*8, "最远敌人:"+parseInt(farthestEnermy),0, 0, fontColor, fontSize).noMove();

				// 游戏状态
				new Text(smallCtx,startX, startY+dy*9, "游戏状态:",     0, 0, fontColor, fontSize).noMove();
				smallCtx.beginPath();
				var fillColor;
				if (IS_GAME_START){
					fillColor = "rgb(0,255,0)";
				} else if (IS_GAME_PAUSE){
					fillColor = "rgb(255,255,0)";
				} else if (IS_GAME_OVER){
					fillColor = "rgb(255,0,0)";
				}
				smallCtx.fillStyle = fillColor;
				smallCtx.fillRect(85,startY+dy*9-14,14,14);
				smallCtx.fill();
				smallCtx.closePath();
				
				// 敌人入侵预警
				var homePoints = BLOCK_OBJ[BLOCK_HOME]["drawObj"].getBoundaryAsCanvas(MAX_ROW,MAX_ROW,MAX_COL/2,MAX_COL/2);
				for (var i=0;i<ENERMY_ARR.length;i++){
					var rangeDistance = getTwoPointDistance(homePoints[0].x,homePoints[0].y,ENERMY_ARR[i].x,ENERMY_ARR[i].y);
					if (rangeDistance <= 210){
						showSmallCanvasTips(TIPS_OBJ["notice"]);
						break;
					}
				}
			}
			
			// 更新提示语
			for (var j=0;j<TIPS_ARR.length;j++){
				if (TIPS_ARR[j].isDie()){
					TIPS_ARR.splice(j,1);
				}
			}
			
			var minLevel = -1;
			var index = -1;
			for (var j=0;j<TIPS_ARR.length;j++){
				if (!TIPS_ARR[j].isDie()){
					if (TIPS_ARR[j].level >= minLevel){
						minLevel = TIPS_ARR[j].level;
						index = j;
					}
				}
			}

			if (index != -1){
				TIPS_ARR[index].update();			
			}else{
				var musicArr = new Array("小嘛小二郎","背着书包上学堂","不怕太阳晒","也不怕风雨狂","只怕先生骂我懒啊","没有学问那无颜见爹娘");
				var music = musicArr[G_CYCLE%musicArr.length];
				var musicObj = {
					"text":music,
					"level":1,
					"color":"rgb(0,255,0)",
					"fontSize":15
				}
				showSmallCanvasTips(musicObj);
			}
		}
		
		// 绘制游戏首页画面
		function drawGameIndexPage(){
			// 游戏首页图片
			ctx.drawImage(SPRITE_IMAGE,0,0,256,220,0,0,canvas.width,canvas.height);
			var srcPicInfo = PLAYER_DATA[PLAYER1_ID_PREIFIX]["imgPos"][D_RIGHT];
			if (GAME_MODE == SINGLE_PLAYER){
				ctx.drawImage(SPRITE_IMAGE,srcPicInfo.x,srcPicInfo.y,srcPicInfo.w,srcPicInfo.h,200,343,30,30);
			}else{
				ctx.drawImage(SPRITE_IMAGE,srcPicInfo.x,srcPicInfo.y,srcPicInfo.w,srcPicInfo.h,200,385,30,30);
			}
			// 显示关卡数
			ctx.beginPath();
			ctx.font = "normal normal 23px 宋体";
			ctx.strokeStyle = "yellow";
			ctx.strokeText("关卡"+MISSION,325,335);
			ctx.closePath();
			
			var startX = smallCanvas.width/2,startY = 0,dx = 60;
			var posArr = new Array([27,40,30,30],[59,40,30,30],[93,40,30,30],[125,40,30,30],[156,40,30,30],[186,40,34,30],[61,80,34,30],[95,80,34,30],[127,80,34,30],[159,80,34,30]);
			for (var i=0;i<posArr.length;i++){
				var x;
				if (i%2 == 0){
					x = START_X+SPEED_X1;
					if (x > smallCanvas.width-30){
						SPEED_X1 = -SPEED_X1;
					}
				}else{
					x = START_X-SPEED_X2;
					if (x < 0){
						SPEED_X2 = -SPEED_X2;
					}
				}
				var y = START_Y + i*INCREMENT;
				smallCtx.drawImage(SPRITE_IMAGE,posArr[i][0],posArr[i][1],posArr[i][2],posArr[i][3],x,y,30,30);
			}
		}
		
		// 绘制游戏开始动画
		function drawMissionLoading(){
			ctx.beginPath();
			ctx.fillStyle="rgba(255,255,255,0.9)";
			ctx.clearRect(0,0,canvas.width,canvas.height);
			ctx.fillRect(0,0,canvas.width,canvas.height);
			ctx.fill();
			
			ctx.strokeStyle = "black";
			ctx.font = "normal normal 30px 宋体";
			ctx.strokeText("mission " + MISSION,canvas.width/2-50,canvas.height/2);
			ctx.stroke();
			ctx.closePath();
		}
		
		// 击中敌方坦克,吃到奖励物品,被敌军击毁等时交互的提示语
		function showSmallCanvasTips(obj){
			var text = obj.text;
			var level = obj.level ||2;
			var color = obj.color||"yellow";
			var size = obj.fontSize||19;
			
			var canPush = true;

			for (var i=0;i<TIPS_ARR.length;i++){
				if (TIPS_ARR[i].text == text){
					canPush = false;
                    break;
				}
			}
			
			if (canPush){
				TIPS_ARR.push(new Tips(smallCtx, 3, 570, text, level, 180, color, size));
			}
		}
		
		// 保存当前游戏截图到缓存画布
		function saveBigScreenShot(canvasName){
			// 保存当前游戏截图到缓存画布
			bufferCtx1.clearRect(0,0,bufferCanvas1.width,bufferCanvas1.height);
			bufferCtx1.drawImage(canvasName,0,0,bufferCanvas1.width,bufferCanvas1.height);
		}
		
		// 保存当前游戏截图到缓存画布
		function saveSmallScreenShot(canvasName){
			// 保存当前游戏截图到缓存画布
			bufferCtx2.clearRect(0,0,bufferCanvas2.width,bufferCanvas2.height);
			bufferCtx2.drawImage(canvasName,0,0,bufferCanvas2.width,bufferCanvas2.height);
		}
		
		// 根据玩家ID查找玩家对象
		function getPlayerTankInfo(playerId){
			var res = undefined;
			for (var i=0;i<PLAYER_TANK_ARR.length;i++){
				if (PLAYER_TANK_ARR[i].id == playerId){
					res = PLAYER_TANK_ARR[i];
					break;
				}
			}
			
			return res;
		}
		
		// 根据玩家ID判断玩家是否存在
		function isPlayerExist(playerId){
			var res = false;
			for (var i=0;i<PLAYER_TANK_ARR.length;i++){
				if (PLAYER_TANK_ARR[i].id == playerId){
					res = true;
					break;
				}
			}
			return res;	
		}
		
		// 点击开始按钮
		function startGame(){
			// 如果之前从没有点击过暂停按钮  则加载关卡
			if (!IS_FIRST_RUN){
				STARTBTN_PRESSED_CYCLE = G_CYCLE;
			}
		
			IS_GAME_START = true;
			IS_GAME_PAUSE = false;
			$("#startBtn").attr("disabled",true);
			$("#pauseBtn").attr("disabled",false);
		}
		
		// 点击暂停按钮
		function pauseGame(){
			// 点击暂停时保存游戏最后一次的截图
			saveBigScreenShot(canvas);
			saveSmallScreenShot(smallCanvas);

			IS_GAME_START = false;
			IS_GAME_PAUSE = true;
			$("#startBtn").attr("disabled",false);
			$("#pauseBtn").attr("disabled",true);
		}
		
		// 点击重新开始按钮
		function restartGame(){
			window.location.href = window.location.href;
		}
		
		// 动画累计刷新的次数
		var G_CYCLE=0;
		// 时钟:秒
		var G_SECOND=0;
		// 游戏开始文字对象
		var gameStartText = new Text(ctx,canvas.width/2-70,0,"Welcome",0,2,"yellow",30);
		// 游戏暂停文字对象
		var gamePauseText = new Text(ctx,canvas.width/2-70,canvas.height/2-15,"GAME PAUSE",0,0,"yellow",30);
		// 游戏结束文字对象
		var gameOverText = new Text(ctx,canvas.width/2-70,canvas.height-30,"GAME OVER",0,-2,"yellow",30);
		// 游戏提示语文字对象
		var gameTipsText = new Text(ctx,canvas.width/2-158,canvas.height/2+60,"Press any key to start game",0,0,"white",23);
		var gameTipsTextExt = new Text(ctx,canvas.width/2-145,canvas.height/2+195,"Press any key to start game",0,0,"yellow",25);		
		
		// 玩家1键盘事件
		window.addEventListener("keydown",player1KeyListener);
		window.addEventListener("keyup",player1KeyListener);
		// 玩家2键盘事件
		window.addEventListener("keydown",player2KeyListener);
		window.addEventListener("keyup",player2KeyListener);
		// 游戏首页键盘事件
		window.addEventListener("keydown",gameStartKeyListener);
		// 鼠标滚轮事件
		window.addEventListener("mousewheel",mouseWheelListener);

		// 给游戏首页用的变量
		var START_X = smallCanvas.width/2-15;
		var START_Y = 0;
		var INCREMENT = 60;
		var SPEED_X1 = 0;
		var SPEED_X2 = 0;
		
		// 主函数
		function ainimate(){
			// 游戏地图清屏
			ctx.clearRect(0,0,canvas.width,canvas.height);
			smallCtx.clearRect(0,0,smallCanvas.width,smallCanvas.height);
			
			// 主角 每秒刷新60次
			requestAnimationFrame(ainimate);

			// 时钟,备用
			G_CYCLE++;
			if (G_CYCLE %60 == 0){
				G_SECOND++;
			}
		
			// 如果第一次进入画面  则提示用户按任意键进入游戏
			if (!IS_GAME_START && !IS_GAME_PAUSE && !IS_GAME_OVER){
				// 绘制游戏首页文字及动画
				drawGameIndexPage();
				gameTipsTextExt.blink(1);
				SPEED_X1++;
				SPEED_X2++;
			}

			// 点击启动按钮时触发
			if (IS_GAME_START){
				if (!IS_FIRST_RUN){
					if (G_CYCLE - STARTBTN_PRESSED_CYCLE <= 80){
						// 游戏开始动画
						drawMissionLoading();
						return;
					}else{
						// 首次开始游戏时,加载关卡
						loadMission(MISSION);
						IS_FIRST_RUN = true;
					}
				}
			
				// 过关后 加载下一关
				if (ENERY_DEAD_NUM == TOTAL_ENERY_NUM){
					ENERY_DEAD_NUM = 0;
					ENERY_USED_NUM = 0;
					PLAYER_TANK_ARR = new Array();
                    ENERMY_ARR = new Array();
					TRACK_BOMB_ARR = new Array();
					BOMB_ARR = new Array();
                    STICK_ARR = new Array();
					MISSION++;
					IS_FIRST_RUN = false;
					STARTBTN_PRESSED_CYCLE = G_CYCLE;
					return;
				}

				// 每隔0.5秒钟去检查是否需要生成敌方坦克
				if (G_CYCLE %30 == 0){
					createEnermy();
				}

				// 每隔2秒钟去检查是否需要生成我方坦克
				if (G_CYCLE %120 == 0){			
					createPlayerTank();
				}
				
				// 每隔30秒生成一个奖励物品
				if (G_CYCLE %1800 == 0){
					createBonus();
				}
				
				// 绘制敌方坦克
				for (var i=0;i<ENERMY_ARR.length;i++){
					ENERMY_ARR[i].update();
				}
				
				// 绘制我方坦克
				for (var i=0;i<PLAYER_TANK_ARR.length;i++){
					PLAYER_TANK_ARR[i].update();
				}
				
				// 绘制玩家生命耗尽后的gameover图片
				drawPlayerDiePic();

				// 绘制子弹
				for (var i=0;i<BOMB_ARR.length;i++){
					BOMB_ARR[i].update();
				}
				
				// 绘制爆炸物
				for (var i=0;i<EXPLODE_ARR.length;i++){
					EXPLODE_ARR[i].update();
				}
							
				// 绘制静态资源  比如土块、金块、丛林等
				for (var i=0;i<STICK_ARR.length;i++){
					STICK_ARR[i].update();
				}
				
				// 绘制追踪炮弹
				for (var i=0;i<TRACK_BOMB_ARR.length;i++){
					TRACK_BOMB_ARR[i].update();
				}
				
				// 绘制奖励物
				for (var i=0;i<BONUS_ARR.length;i++){
					BONUS_ARR[i].update();
				}
				
				//检查我方子弹跟敌方子弹是否碰撞,如果是则移除它们
				checkBombMutualCollision();
				
				// 检查敌人坦克之间的碰撞
				checkEnermyMutualCollision();
				
				// 检查玩家与玩家之间的碰撞
				checkPlayerMutualCollision();
				
				// 检查玩家与敌人坦克的碰撞
				checkEnermyToPlayerCollison();
				
				// 绘制右侧剩余敌方坦克数的画面
				drawRightCanvas();
				
				// 显示页面网格
				if (IS_SHOW_GRID){
					showPanelGrid(ctx);
				}
			}else if (IS_GAME_PAUSE){
				// 绘制暂停那一刹那的游戏画面			
				ctx.drawImage(bufferCanvas1,0,0,canvas.width,canvas.height);
				smallCtx.drawImage(bufferCanvas2,0,0,smallCanvas.width,smallCanvas.height);
				// 游戏暂停时提示文字
				gamePauseText.noMove();
				gameTipsText.blink(1);
			}else if (IS_GAME_OVER){
				// 绘制结束那一刹那的游戏画面
				ctx.drawImage(bufferCanvas1,0,0,canvas.width,canvas.height);
				smallCtx.drawImage(bufferCanvas2,0,0,smallCanvas.width,smallCanvas.height);
				// 游戏结束提示文字
				gameOverText.moveToUp();
				setTimeout(restartGame,5000);
			}
		}
		
		// 程序入口
		ainimate();
		
		// 每秒钟画面刷新次数	
		var last_g_cycle = G_CYCLE;
		setInterval(function(){
			var cur_g_cycle = G_CYCLE;
			FPS = cur_g_cycle-last_g_cycle;
			last_g_cycle = cur_g_cycle;
		},1000);

		// 鼠标移动至右侧地图后显示控制按钮
		$("#rightDiv").hover(function(){
			var text=$("#showHideBtn").text();
			if (text == "收起"){
				$("#controllerDiv").slideDown(50);
			}
			$(this).css("cursor","pointer");
		},function(){
			var text=$("#showHideBtn").text();
			if (text == "收起"){
				$("#controllerDiv").slideUp(50);
				$("#fingerDiv").slideUp(50);
			}
		})

		// 右侧画布显示/隐藏控制
		$("#showHideBtn").click(function(){
			var text = $(this).text();
			if (text == "收起"){
				text = "展开";
				$("#smallCanvasDiv").animate({height:"50px"},500,function(){
					$("#smallCanvasDiv").hide();
				});
			}else{
				text = "收起";
				$("#helpInfoDiv").animate({height:"0px"},50,function(){
					$("#helpInfoDiv").hide();
					$("#fingerDiv").hide();
					$("#smallCanvasDiv").show();
					$("#smallCanvasDiv").animate({height:"600px"},500);
				})
			}
			$(this).text(text);
		});
		
		// 帮助按钮
		$("#helpBtn").click(function(){
			$("#helpInfoDiv").css("height","400px");
			var text=$("#showHideBtn").text();
			if (text == "收起"){
				$("#smallCanvasDiv").animate({height:"50px"},100,function(){
					$("#smallCanvasDiv").hide();
					$("#helpInfoDiv").slideDown();
					$("#showHideBtn").text("展开");
				});
			}else{
				$("#helpInfoDiv").slideToggle();
			}
			
			$("#fingerDiv").hide();
		});
		
		// 金手指--作弊器
		$("#fingerBtn").click(function(){
			$("#helpInfoDiv").slideUp();
			var text=$("#showHideBtn").text();
			if (text == "收起"){
				$("#smallCanvasDiv").animate({height:"50px"},100,function(){
					$("#smallCanvasDiv").hide();
					$("#helpInfoDiv").slideUp();
					$("#showHideBtn").text("展开");
				});
			}
			$("#fingerDiv").slideToggle();
		});
        
		// 选择显示指定玩家信息
		$("#selectPlayerBtn").click(function(){
			var text = $(this).text();
			if (text == "玩家1"){
				if (isPlayerExist(PLAYER2_ID_PREIFIX)){
					text = "玩家2";
					CUR_PLAYER = PLAYER2_ID_PREIFIX;
				}
			}else{
				if (isPlayerExist(PLAYER1_ID_PREIFIX)){
					text = "玩家1";
					CUR_PLAYER = PLAYER1_ID_PREIFIX;
				}
			}
			$(this).text(text);
		});
		
		// 鸟巢加固
		$("#homeStrongerInput").click(function(){
			var isChecked = $(this).attr("checked");
			if (isChecked){
				homeBeStronger();
			}else{
				homeBeWeaker();
			}
		});
		
		// 冻结敌人
		$("#freezeEnermyInput").click(function(){
			var isChecked = $(this).attr("checked");
			if (isChecked){
				for (var i=0;i<ENERMY_ARR.length;i++){
					ENERMY_ARR[i].freeze();
				}
			}else{
				for (var i=0;i<ENERMY_ARR.length;i++){
					ENERMY_ARR[i].unfreeze();
				}
			}
		});
		
		// 生成奖励道具
		$("#createBonusInput").click(function(){
			var isChecked = $(this).attr("checked");
			if (isChecked){
				createBonus();
			}
		});	
		
		// 玩家1自杀
		$("#selfKillInput1").click(function(){
			var isChecked = $(this).attr("checked");
			if (isChecked){
				for (var i=0;i<PLAYER_TANK_ARR.length;i++){
					if (PLAYER_TANK_ARR[i].id == PLAYER1_ID_PREIFIX){
						PLAYER_TANK_ARR[i].curBlood = 0;
						break;
					}
				}
			}
		});	
		
		// 玩家2自杀
		$("#selfKillInput2").click(function(){
			var isChecked = $(this).attr("checked");
			if (isChecked){
				for (var i=0;i<PLAYER_TANK_ARR.length;i++){
					if (PLAYER_TANK_ARR[i].id == PLAYER2_ID_PREIFIX){
						PLAYER_TANK_ARR[i].curBlood = 0;
						break;
					}
				}
			}
		});
		
		// 自动发射追踪炮弹
		$("#autoSendTrackBombInput").click(function(){
			var isChecked = $(this).attr("checked");
			if (isChecked){
				IS_AUTO_SEND_TRACK_BOMB = true;
			}else{
				IS_AUTO_SEND_TRACK_BOMB = false;
			}
		});	

		// 手机用户控制
		$("#mobileControllerInput").focus(function(){
			$("#mobileControllerInput").val("");
			$("#controllerDiv").slideDown(100);
		}).blur(function(){
			$("#mobileControllerInput").val("手机用户点此处进行控制");
			//$("#controllerDiv").slideUp(100);
		})
		
		// 设置当前关卡
		$("#chooseMissionInput").attr("max",MISSION_CFG.getObjLength());
		function chooseMission(){
			var curNum = document.getElementById("chooseMissionInput").value;
			curNum = parseInt(curNum);
			MISSION = curNum;
			document.getElementById("curMission").innerHTML=curNum;
		}
		
		// 修改玩家总生命
		function changePlayerTotalLife(){
			var curNum = document.getElementById("playerTotalLifeInput").value;
			curNum = parseInt(curNum);
			document.getElementById("playerTotalLife").innerHTML=curNum;
			PLAYER_DATA.getObjValueByKey(PLAYER1_ID_PREIFIX).totalLife = curNum;
			PLAYER_DATA.getObjValueByKey(PLAYER2_ID_PREIFIX).totalLife = curNum;
			PLAYER1_TOTAL_LIFE = curNum;
			PLAYER2_TOTAL_LIFE = curNum;
		}
	
		// 修改玩家运动速度
		function changePlayerMoveSpeed(){
			var curNum = document.getElementById("playerMoveSpeedInput").value;
			curNum = parseInt(curNum);
			document.getElementById("playerMoveSpeed").innerHTML=curNum;

			PLAYER_DATA.getObjValueByKey(PLAYER1_ID_PREIFIX).moveSpeed = curNum;
			PLAYER_DATA.getObjValueByKey(PLAYER2_ID_PREIFIX).moveSpeed = curNum;
		}

		// 修改玩家射击速度
		function changePlayerShootSpeed(){
			var curNum = document.getElementById("playerShootSpeedInput").value;
			curNum = parseInt(curNum);
			document.getElementById("playerShootSpeed").innerHTML=curNum;

			PLAYER_DATA.getObjValueByKey(PLAYER1_ID_PREIFIX).bombSpeed = curNum;
			PLAYER_DATA.getObjValueByKey(PLAYER2_ID_PREIFIX).bombSpeed = curNum;			
		}

		// 修改玩家射击伤害
		function changePlayerBombDamage(){
			var curNum = document.getElementById("playerBombDamageInput").value;
			curNum = parseInt(curNum);
			document.getElementById("playerBombDamage").innerHTML=curNum;	
			
			PLAYER_DATA.getObjValueByKey(PLAYER1_ID_PREIFIX).bombDamage = curNum;
			PLAYER_DATA.getObjValueByKey(PLAYER2_ID_PREIFIX).bombDamage = curNum;
		}

		// 修改玩家总血量
		function changePlayerTotalBlood(){
			var curNum = document.getElementById("playerTotalBloodInput").value;
			curNum = parseInt(curNum);
			document.getElementById("playerTotalBlood").innerHTML=curNum;

			PLAYER_DATA.getObjValueByKey(PLAYER1_ID_PREIFIX).totalBlood = curNum;
			PLAYER_DATA.getObjValueByKey(PLAYER2_ID_PREIFIX).totalBlood = curNum;			
		}

		// 修改玩家总炮弹数
		function changePlayerTotalTrackBomb(){
			var curNum = document.getElementById("playerTotalTrackBombInput").value;
			curNum = parseInt(curNum);
			document.getElementById("playerTotalTrackBomb").innerHTML=curNum;

			PLAYER_DATA.getObjValueByKey(PLAYER1_ID_PREIFIX).totalTrackBomb = curNum;
			PLAYER_DATA.getObjValueByKey(PLAYER2_ID_PREIFIX).totalTrackBomb = curNum;			
		}

		// 修改敌人坦克总数
		function changeTotalEnermyNum(){
			var curNum = document.getElementById("totalEnermyNumInput").value;
			curNum = parseInt(curNum);
			document.getElementById("totalEnermyNum").innerHTML=curNum;
			TOTAL_ENERY_NUM = curNum;
		}

		// 修改画面中出现的最大坦克数
		function changeMaxLiveEnermyNum(){
			var curNum = document.getElementById("maxLiveEnermyInput").value;
			curNum = parseInt(curNum);
			document.getElementById("maxLiveEnermyNum").innerHTML=curNum;
			MAX_ENERY_LIVE_NUM = curNum;
		}
	</script>
</html>