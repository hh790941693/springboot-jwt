<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>


<html>
	<head>
		<meta charset="utf-8">
		<title>拼图游戏</title>
	<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
	<script type="text/javascript" src="<%=path%>/static/js/newtool.js"></script>
	<script type="text/javascript" src="<%=path%>/static/js/pukeTool.js"></script>
	<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>
	
		<style type="text/css">
			body{
				margin:0;
                padding:0;
			}
            
			span{
				display:inline-block;
				margin-left:5px;
				font-size:15px;
				width:80px;
			}
			button{
				background-color:green;
				border-radius:15px 15px;
			}
			
		</style>
		
	</head> 
	<body>
		<div id="mainDiv" style="width:1160px;height:756px;margin:5px auto;overflow:hidden;border:2px solid green;">
            <div id="topDiv" style="width:850px;height:26px;margin-left:26px;">
                <div id="player1_controller" style="padding-left:40%;display:none;float:left">
                    <button id="player1_help_btn">提示</button>
                    <button id="player1_reset_btn">归位</button>
                    <button id="player1_cancel_btn">不要</button>
                    <button id="player1_chupai_btn">出牌</button>
                </div>
            </div>
            
            <div id="centerDiv" style="width:1160px;height:700px;">
                <div id="leftDiv" style="width:30px;height:700px;float:left;">
                    <div id="player3_controller" style="padding-top:250px;display:none">
                        <button id="player3_help_btn">提示</button>
                        <button id="player3_reset_btn">归位</button>
                        <button id="player3_cancel_btn">不要</button>
                        <button id="player3_chupai_btn">出牌</button>
                    </div>
                </div>
                
                <canvas id="canvas" style="background-color:black;float:left;">
                </canvas>
                
                <div id="rightDiv" style="width:30px;height:700px;float:left;">
                    <div id="player2_controller" style="padding-top:250px;display:none">
                        <button id="player2_help_btn">提示</button>
                        <button id="player2_reset_btn">归位</button>
                        <button id="player2_cancel_btn">不要</button>
                        <button id="player2_chupai_btn">出牌</button>
                    </div>
                </div>
                
                <div style="width:240px;float:right;height:100%;margin-right:2px;">
                    <textarea id="msgText" style="width:100%;height:100%">
                    </textarea>
                </div>
            </div>
            
            <div id="bottomDiv" style="width:850px;height:26px;margin-left:26px;margin-top:2px;">
                <div id="game_controller" style="padding-left:43%;">
                    <button id="startBtn">开始</button>
                    <button id="restartBtn">重新开始</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="player1_tuoguan_ckbox" type="checkbox">玩家1托管&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="player2_tuoguan_ckbox" type="checkbox">玩家2托管&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="player3_tuoguan_ckbox" type="checkbox">玩家3托管
                </div>
            </div>
        </div>
	</body>
	
	<script type="text/javascript">
		var canvas = document.getElementById("canvas");
		canvas.width = 850;
		canvas.height = 700;
		var ctx = canvas.getContext("2d");

        // 完整的一副牌
		var POKER_INIT_ARR = new Array();
		
		// 玩家对象
		var PLAYER1,PLAYER2,PLAYER3;

        // 玩家手上的牌
		var PLAYER1_POKER_ARR = new Array();
		var PLAYER2_POKER_ARR = new Array();
		var PLAYER3_POKER_ARR = new Array();
        
        // 玩家托管
        var PLAYER1_AUTO_PLAY = false;
        var PLAYER2_AUTO_PLAY = false;
        var PLAYER3_AUTO_PLAY = false;
        
        // 游戏是否结束
        var IS_GAME_OVER = false;
        
        // 玩家拿到牌等待时间 单位:秒
        var WAIT_TIME = 15;

		// 玩家扑克位置   x,y:手上扑克的显示位置     xt,yt:打出去的牌的显示位置
		var PLAYER_CFG = {
			"1":{x:110,y:40,xt:180,yt:120},
			"2":{x:canvas.width-90,y:65,xt:canvas.width-166,yt:135},
			"3":{x:40,y:65,xt:116,yt:135}
		}
		
		// 扑克对应的数值
		var POKER_MAP = {
			"3":3,
			"4":4,
			"5":5,
			"6":6,
			"7":7,
			"8":8,
			"9":9,
			"10":10,
			"J":11,
			"Q":12,
			"K":13,
			"A":14,
			"2":15,
			"小王":16,
			"大王":17              
		}
		
		// 扑克花色类型
		var POKER_TYPE = new Array("红桃","方块","梅花","黑桃");
		var POKER_CHAR = new Array("3","4","5","6","7","8","9","10","J","Q","K","A","2");
		var POKER_KING = new Array("小王","大王");
		
		// 地主扑克标志
		var LORD_PORK = undefined;
		
        // 资源文件
		var IMAGE = new Image();
		IMAGE.src = rootUrl+"/img/puke.gif";
		
		// 游戏是否开始
		var IS_START = false;
		
		// 上一个玩家打出去的牌;
		var CUR_PLAYED_POKER_ARR = new Array();
        
        // 提示按钮点击次数统计  方便显示不同组合的牌
        var HELP_BTN_CLICK_COUNT = 0;
		
		// 扑克规则路由 key:扑克长度  value:对应的规则数组
		var POKER_RULE_MAP = {
			1:[Dan],
			2:[Dui,Zha],
			3:[San],
			4:[Zha,Sand1],
			5:[Shunzi,Sand2],
			6:[Shunzi,LianDui,Sid2],
			7:[Shunzi],
			8:[Shunzi,LianDui,Sid2],
			9:[Shunzi],
			10:[Shunzi,LianDui],
			11:[Shunzi],
			12:[Shunzi,LianDui],
			14:[LianDui],
			16:[LianDui],
			18:[LianDui],
		};

		// 扑克类
		function Poker(id,x,y,w,h,type,key,value,isSelected,showMode){
			this.id = id;
			this.x = x||0;
			this.y = y||0;
			this.w = w||0;
			this.h = h||0;
            this.type = type;                            // 花色 比如:红桃  黑桃  方块  梅花
            this.key = key;                               // 牌的值  比如:3  10  K  A
			this.value = value;                         // 牌对应后台要计算的值
			this.isSelected = isSelected||false;  // 是否被选中
			this.showMode = showMode||3;       // 显示模式   1:牌正面  2:牌反面  3:空牌
            
            this.show = type+key                    // 牌的读法  比如:红桃Q
		}
        
		// 玩家类
		function Player(id,name,pokerArr,isLord,canDeliver){
			this.id = id;                                     // 玩家编号
            this.name = name;                           // 玩家名称
			this.pokerArr = pokerArr;                   // 手上的牌
			this.isLord = isLord||false;                // 是否是地主
			this.canDeliver = canDeliver||false;    // 是否能出牌
			
			// 点击出牌按钮
			this.chupai = function(){
				var tempPorkerArr = new Array();
				for (var i=this.pokerArr.length-1;i>=0;i--){
					if (this.pokerArr[i].isSelected){
						tempPorkerArr.push(this.pokerArr[i]);
					}
				}
				
				// 如果玩家没有选择一张牌的话  则直接提示
				if (tempPorkerArr.length == 0){
					console.log("请玩家选择要打的牌!");
					return false;
				}
				
				// 检查牌是否合法
				if (!this.checkValid(tempPorkerArr)){
					console.log("出牌不合法");
					this.resetPoker();
					return false;
				}
				
				// 出牌规则
				var ruler = this.getPokerRuler(tempPorkerArr);
				
				//  检查牌是否能打得过
				if (CUR_PLAYED_POKER_ARR.length > 0 && this.id != CUR_PLAYED_POKER_ARR[0].id && !this.checkOtherNMnoPoker()){
					// 如果上家打的是王炸 则打不过
					if (Zha.prototype.isWZ(CUR_PLAYED_POKER_ARR)){
						console.log("上家打的是王炸,所以打不过");
						this.resetPoker();
						return false;
					}
				
					if (Zha.prototype.isWZ(tempPorkerArr)){
						// 如果当前牌是王炸  则打得过
					}else if (Zha.prototype.isZha(tempPorkerArr) && !Zha.prototype.isZha(CUR_PLAYED_POKER_ARR)){
						// 如果当前牌是4炸  玩家牌不是4炸 则打得过
					}else if (!Zha.prototype.isZha(tempPorkerArr) && Zha.prototype.isZha(CUR_PLAYED_POKER_ARR)){
						// 如果当前牌不是4炸  玩家牌是4炸 则打不过
						console.log("上家打的是4炸,所以打不过");
						this.resetPoker();
						return false;
					}else{
						if (!ruler.prototype.compare(tempPorkerArr,CUR_PLAYED_POKER_ARR)){
							console.log("该牌打不过");
							this.resetPoker();
							return false;
						}
					}
				}
				
				// 从玩家手中移除掉打出去的牌
				for (var i=tempPorkerArr.length-1;i>=0;i--){
					this.pokerArr.removeArrElement(tempPorkerArr[i]);
				}
                // 记录当前玩家打出去的牌 方便下一个玩家打牌时进行比较大小
				CUR_PLAYED_POKER_ARR = tempPorkerArr;
				
                // 玩家打出去的牌的叫法
				var tempPokerName = ruler.prototype.name;
				if (typeof(ruler.prototype.name) == "function"){
					tempPokerName = ruler.prototype.name(tempPorkerArr);
				}
                
                // 记录日志
                writeChupaiLog(this,tempPokerName);

				// 刷新剩余牌的位置 让其居中
				this.refreshPos();
                HELP_BTN_CLICK_COUNT = 0;
                START_CYCLE = G_CYCLE;
				return true;
			}
			
            // 点击提示按钮
            this.help = function(){
                this.resetPoker();

                if (CUR_PLAYED_POKER_ARR.length > 0 && this.id != CUR_PLAYED_POKER_ARR[0].id && !this.checkOtherNMnoPoker()){
                    // 获取上家牌的出牌规则
                    var ruler = this.getPokerRuler(CUR_PLAYED_POKER_ARR);
                    var temparr = ruler.prototype.matchExt(this.pokerArr,CUR_PLAYED_POKER_ARR);
                    if (HELP_BTN_CLICK_COUNT > temparr.length-1){
                        HELP_BTN_CLICK_COUNT = 0;
                    }
                    
                    if (temparr.length > 0){
                        var showArr = temparr[HELP_BTN_CLICK_COUNT];
                        for (var i=0;i<showArr.length;i++){
                            for (var j=0;j<this.pokerArr.length;j++){
                                if (showArr[i] == this.pokerArr[j]){
                                    this.pokerArr[j].isSelected = true;
                                }
                            }
                        }
                    }else{
                        // 要不起
                        if (this.id == "1"){
                             $("#player1_cancel_btn").click();
                        }else if (this.id == "2"){
                             $("#player2_cancel_btn").click();
                        }else if (this.id == "3"){
                            $("#player3_cancel_btn").click();
                        }
                    }
                    HELP_BTN_CLICK_COUNT++;
                }else{
                    var functionArr = new Array(getDan,getDui,getSan,getSi,getShunzi,getLiandui,getSan1Or2,getSid2,getZhadan);
                    var temparr = new Array();
                    for (var i=0;i<functionArr.length;i++){
                        var temparr = functionArr[i](this.pokerArr);
                        if (temparr.length > 0){
                            break;
                        }
                    }
                    
                    if (temparr.length > 0){
                        if (HELP_BTN_CLICK_COUNT > temparr.length-1){
                            HELP_BTN_CLICK_COUNT = 0;
                        }

                        var showArr = temparr[HELP_BTN_CLICK_COUNT];
                        for (var i=0;i<showArr.length;i++){
                            for (var j=0;j<this.pokerArr.length;j++){
                                if (showArr[i] == this.pokerArr[j]){
                                    this.pokerArr[j].isSelected = true;
                                }
                            }
                        }
                        
                        HELP_BTN_CLICK_COUNT++;
                    }
                }
            }
            
			// 重新刷新手上的牌显示位置,使其居中显示
			this.refreshPos = function(){
				var startX = PLAYER_CFG[this.id].x;
				var startY = PLAYER_CFG[this.id].y;
				
				if (this.id == "1"){
					startX = startX+parseInt((18-this.pokerArr.length)/2)*35;
				} else if (this.id == "2"){
					startY = startY + parseInt((18-this.pokerArr.length)/2)*35;
				} else if (this.id == "3"){
					startY = startY + parseInt((18-this.pokerArr.length)/2)*35;
				}
				
				for (var i=0;i<this.pokerArr.length;i++){
					this.pokerArr[i].x = startX;
					this.pokerArr[i].y = startY;
					
					if (this.id == "1"){
						startX += 35;
					}else if (this.id == "2"){
						startY += 35;
					}else if (this.id == "3"){
						startY += 35;
					}
				}
			}
			
			// 点击归位按钮
			this.resetPoker = function(){
				for (var i=0;i<this.pokerArr.length;i++){
					this.pokerArr[i].isSelected = false;
				}
			}
			
			// 判断玩家手上的扑克是否没有了
			this.hasNoPoker = function(){
				return this.pokerArr.length == 0;
			}
			
			// 检查要打的牌是否合法
			this.checkValid = function(tempPorkerArr){
                var isValid = false;
				if (tempPorkerArr.length > 0){
					var ruleArr = POKER_RULE_MAP[tempPorkerArr.length];
					if (typeof(ruleArr) == "undefined"){
						isValid = false;
					}else{
						for (var r=0;r<ruleArr.length;r++){
							if (ruleArr[r].prototype.valid(tempPorkerArr)){
								isValid = true;
								break;
							}
						}
					}
				}
				
				return isValid;
			}
			
			// 获取玩家打的牌的规则
			this.getPokerRuler = function(tempPorkerArr){
				var ruler = undefined;
				if (tempPorkerArr.length > 0){
				   var ruleArr = POKER_RULE_MAP[tempPorkerArr.length];
					if (typeof(ruleArr) != "undefined"){
						for (var r=0;r<ruleArr.length;r++){
							if (ruleArr[r].prototype.valid(tempPorkerArr)){
								ruler = ruleArr[r];
								break;
							}
						}
					}
				}
				return ruler;
			}
            
            // 检查另一名非地主玩家是否牌空了
            this.checkOtherNMnoPoker = function(){
                if (!this.isLord){
                    if (this.id == "1"){
                        if (!PLAYER2.isLord && PLAYER2.hasNoPoker()){
                            return true;
                        }
                        
                        if (!PLAYER3.isLord && PLAYER3.hasNoPoker()){
                            return true;
                        }                        
                    }else if (this.id == "2"){
                        if (!PLAYER1.isLord && PLAYER1.hasNoPoker()){
                            return true;
                        }
                        
                        if (!PLAYER3.isLord && PLAYER3.hasNoPoker()){
                            return true;
                        }                     
                    }else if (this.id == "3"){
                        if (!PLAYER1.isLord && PLAYER1.hasNoPoker()){
                            return true;
                        }
                        
                        if (!PLAYER2.isLord && PLAYER2.hasNoPoker()){
                            return true;
                        }                     
                    }
                }
                
                return false;
            }
		}
		
		// 初始化扑克
		function initPoker(){
            // 3-15
			for (var i=0;i<POKER_TYPE.length;i++){
				for (var j=0;j<POKER_CHAR.length;j++){
					var poker = new Poker(-1,0,0,0,0,POKER_TYPE[i],POKER_CHAR[j],POKER_MAP[POKER_CHAR[j]]);
					POKER_INIT_ARR.push(poker);
				}
			}
			
            // 16,17 大王小王
			for (var i=0;i<POKER_KING.length;i++){
				var poker = new Poker(-1,0,0,0,0,"",POKER_KING[i],POKER_MAP[POKER_KING[i]]);
				POKER_INIT_ARR.push(poker);
			}
		}
		
		// 洗牌
		function washPoker(){
			for (var i=0;i<800;i++){
				var f = randomNumber(0,POKER_INIT_ARR.length-1);
				var t= randomNumber(0,POKER_INIT_ARR.length-1);
				
				var temp = POKER_INIT_ARR[f];
				POKER_INIT_ARR.splice(f,1,POKER_INIT_ARR[t]);
				POKER_INIT_ARR.splice(t,1,temp);
			}
		}
		
		// 随机选择一张牌 谁摸到此牌 谁就是地主
		function selectLordPoker(){
			var random = randomNumber(0,POKER_INIT_ARR.length-1);
			// 如果抽到大小王 则重新抽一张牌
            while (POKER_INIT_ARR[random].value == POKER_MAP["小王"] || POKER_INIT_ARR[random].value == POKER_MAP["大王"]){
                random = randomNumber(0,POKER_INIT_ARR.length-1);
            }
            LORD_PORK = POKER_INIT_ARR[random];
		}
		
		// 发牌
		function deliveryPoker(){
			for (var i=0;i<POKER_INIT_ARR.length;i++){
				if (i % 3 == 0){
					POKER_INIT_ARR[i].id = "1";
					PLAYER1_POKER_ARR.push(POKER_INIT_ARR[i]);
				} else if (i % 3 == 1){
					POKER_INIT_ARR[i].id = "2";
					PLAYER2_POKER_ARR.push(POKER_INIT_ARR[i]);
				} else if (i % 3 == 2){
					POKER_INIT_ARR[i].id = "3";
					PLAYER3_POKER_ARR.push(POKER_INIT_ARR[i]);
				}
			}
			
            // 排序
			PLAYER1_POKER_ARR.sort(function(a,b){
				return a.value - b.value;
			})
			
			PLAYER2_POKER_ARR.sort(function(a,b){
				return b.value - a.value;
			})
			
			PLAYER3_POKER_ARR.sort(function(a,b){
				return a.value - b.value;
			})
		}

		// 检查哪个玩家是地主,并且屏蔽非地主玩家的操作按钮
		function updateLord(){
			var tempPlayer;
			if (PLAYER1_POKER_ARR.isInArray(LORD_PORK)){
				tempPlayer = PLAYER1;
			} else if (PLAYER2_POKER_ARR.isInArray(LORD_PORK)){
				tempPlayer = PLAYER2;
			} else if (PLAYER3_POKER_ARR.isInArray(LORD_PORK)){
				tempPlayer = PLAYER3;
			}
			tempPlayer.isLord = true;
			tempPlayer.canDeliver = true;
			
			if (tempPlayer.id == 1){
				$("#player1_controller").css("display","block");
				$("#player2_controller").css("display","none");
				$("#player3_controller").css("display","none");
			}else if (tempPlayer.id == 2){
				$("#player1_controller").css("display","none");
				$("#player2_controller").css("display","block");
				$("#player3_controller").css("display","none");                    
			}else if (tempPlayer.id == 3){
				$("#player1_controller").css("display","none");
				$("#player2_controller").css("display","none");
				$("#player3_controller").css("display","block");                      
			}
		}

		// 初始化玩家已拿到手的牌的显示位置
		function initUsedPoker(){
			for (var i=0;i<PLAYER1_POKER_ARR.length;i++){
				var x =PLAYER_CFG["1"].x+i*35,y=PLAYER_CFG["1"].y;
				
				PLAYER1_POKER_ARR[i].x = x;
				PLAYER1_POKER_ARR[i].y = y;
				PLAYER1_POKER_ARR[i].w = 35;
				PLAYER1_POKER_ARR[i].h = 50;
			}

			for (var i=0;i<PLAYER2_POKER_ARR.length;i++){
				var x=PLAYER_CFG["2"].x,y=PLAYER_CFG["2"].y+i*35;
				
				PLAYER2_POKER_ARR[i].x = x;
				PLAYER2_POKER_ARR[i].y = y;
				PLAYER2_POKER_ARR[i].w = 50;
				PLAYER2_POKER_ARR[i].h = 35;
			}

			for (var i=0;i<PLAYER3_POKER_ARR.length;i++){
				var x=PLAYER_CFG["3"].x,y=PLAYER_CFG["3"].y+i*35; 
				PLAYER3_POKER_ARR[i].x = x;
				PLAYER3_POKER_ARR[i].y = y;
				PLAYER3_POKER_ARR[i].w = 50;
				PLAYER3_POKER_ARR[i].h = 35;
			}
		}
        
		// 初始化玩家
        function initPlayer(){
			PLAYER1 = new Player("1","玩家1",PLAYER1_POKER_ARR,false,false);
			PLAYER2 = new Player("2","玩家2",PLAYER2_POKER_ARR,false,false);
			PLAYER3 = new Player("3","玩家3",PLAYER3_POKER_ARR,false,false);        
        }
        
		// 显示玩家手上的扑克
		function showPlayerPoker(){
            var poker_use_arr = new Array();
            poker_use_arr = poker_use_arr.concat(PLAYER1_POKER_ARR).concat(PLAYER2_POKER_ARR).concat(PLAYER3_POKER_ARR);

			for (var i=0;i<poker_use_arr.length;i++){    
				var pokerPos = getPokerSrcPos(poker_use_arr[i]);
				var x0 =pokerPos.x0,y0=pokerPos.y0,w0=pokerPos.w0,h0=pokerPos.h0;
				var pokerId = poker_use_arr[i].id;
				var isSelected = poker_use_arr[i].isSelected;
				var showMode = poker_use_arr[i].showMode;
			   
				if (showMode == 2){
					x0=0,y0=181*4,w0=125,h0=181;
				}                   
				else if (showMode == 3){
					x0=7*125,y0=181*4,w0=125,h0=181;
				}
				
				if (pokerId == "1"){
					if (isSelected){
						ctx.drawImage(IMAGE,x0,y0,w0,h0,poker_use_arr[i].x,poker_use_arr[i].y+20,poker_use_arr[i].w,poker_use_arr[i].h);
					}else{
						ctx.drawImage(IMAGE,x0,y0,w0,h0,poker_use_arr[i].x,poker_use_arr[i].y,poker_use_arr[i].w,poker_use_arr[i].h);
					}
				} else if (pokerId == "3"){
					ctx.beginPath();
					ctx.save();
					ctx.translate(poker_use_arr[i].x,poker_use_arr[i].y);
					ctx.rotate(Math.PI/2);
					 if (isSelected){
						ctx.drawImage(IMAGE,x0,y0,w0,h0,0,-20,35,-50);
					}else{
						ctx.drawImage(IMAGE,x0,y0,w0,h0,0,0,35,-50);
					}
					ctx.restore();
					ctx.closePath();
				} else if (pokerId == "2"){
					ctx.beginPath();
					ctx.save();
					ctx.translate(poker_use_arr[i].x,poker_use_arr[i].y);
					ctx.rotate(3*Math.PI/2);
					if (isSelected){
						ctx.drawImage(IMAGE,x0,y0,w0,h0,0,-20,-35,50);
					}else{
						ctx.drawImage(IMAGE,x0,y0,w0,h0,0,0,-35,50);
					}
					ctx.restore();
					ctx.closePath();
				}
				
				ctx.beginPath();
				ctx.strokeStyle="white";
				ctx.rect(poker_use_arr[i].x,poker_use_arr[i].y,poker_use_arr[i].w,poker_use_arr[i].h);
				ctx.stroke();
				ctx.closePath();
			}
			
			if (!IS_FAPAI){
				// 显示底牌
				ctx.beginPath();
				ctx.strokeStyle = "white"
				ctx.font = "normal normal 20px 宋体";
				ctx.strokeText("底牌为:",canvas.width/2-60,canvas.height-30);
				var pokerPos = getPokerSrcPos(LORD_PORK);
				ctx.drawImage(IMAGE,pokerPos.x0,pokerPos.y0,pokerPos.w0,pokerPos.h0,canvas.width/2-60+90,canvas.height-60,35,50);
				ctx.closePath();
			}
		}
		
		// 显示玩家已打出去的牌
		function showPlayerUsedPoker(){
			var transparent = 0.75;
			if (CUR_PLAYED_POKER_ARR.length > 0 && CUR_PLAYED_POKER_ARR[0].id == "1"){
				ctx.beginPath();
				ctx.strokeStyle="white";
				for (var i=0;i<CUR_PLAYED_POKER_ARR.length;i++){
					ctx.rect(PLAYER_CFG["1"].xt+i*35,PLAYER_CFG["1"].yt,35,50);
				}
				ctx.stroke();
				ctx.closePath();
				
				for (var i=CUR_PLAYED_POKER_ARR.length-1;i>=0;i--){
					var pokerObj = CUR_PLAYED_POKER_ARR[i];
					var x = PLAYER_CFG["1"].xt+(CUR_PLAYED_POKER_ARR.length-1-i)*35;
					var y = PLAYER_CFG["1"].yt;
					pokerObj.x = x;
					pokerObj.y = y;
				}
				
				ctx.beginPath();
				ctx.save();
				ctx.globalAlpha = transparent;
				for (var i=0;i<CUR_PLAYED_POKER_ARR.length;i++){
					var pokerObj = CUR_PLAYED_POKER_ARR[i];
					var pokerPos = getPokerSrcPos(pokerObj);
					ctx.drawImage(IMAGE,pokerPos.x0,pokerPos.y0,pokerPos.w0,pokerPos.h0,pokerObj.x,pokerObj.y,pokerObj.w,pokerObj.h);
				}
				ctx.restore();
				ctx.closePath();
			}
			
			if (CUR_PLAYED_POKER_ARR.length > 0 && CUR_PLAYED_POKER_ARR[0].id == "2"){
				ctx.beginPath();
				ctx.strokeStyle="white";
				for (var i=0;i<CUR_PLAYED_POKER_ARR.length;i++){
					ctx.rect(PLAYER_CFG["2"].xt,PLAYER_CFG["2"].yt+i*35,50,35);
				}
				ctx.stroke();
				ctx.closePath();
					
				for (var i=CUR_PLAYED_POKER_ARR.length-1;i>=0;i--){
					var pokerObj = CUR_PLAYED_POKER_ARR[i];
					var x = PLAYER_CFG["2"].xt;
					var y = PLAYER_CFG["2"].yt+(CUR_PLAYED_POKER_ARR.length-1-i)*35;
					pokerObj.x = x;
					pokerObj.y = y;
				}
				
				for (var i=0;i<CUR_PLAYED_POKER_ARR.length;i++){
					var pokerObj = CUR_PLAYED_POKER_ARR[i];
					var pokerPos = getPokerSrcPos(pokerObj);
					
					ctx.beginPath();
					ctx.save();
					ctx.translate(pokerObj.x,pokerObj.y);
					ctx.rotate(3*Math.PI/2);
					ctx.globalAlpha = transparent;
					ctx.drawImage(IMAGE,pokerPos.x0,pokerPos.y0,pokerPos.w0,pokerPos.h0,0,0,-35,50);
					ctx.restore();
					ctx.closePath();
				}
			}

			if (CUR_PLAYED_POKER_ARR.length > 0 && CUR_PLAYED_POKER_ARR[0].id == "3"){
				ctx.beginPath();
				ctx.strokeStyle="white";
				for (var i=0;i<CUR_PLAYED_POKER_ARR.length;i++){
					ctx.rect(PLAYER_CFG["3"].xt,PLAYER_CFG["3"].yt+i*35,50,35);
				}     
				ctx.stroke();
				ctx.closePath();
					
				for (var i=CUR_PLAYED_POKER_ARR.length-1;i>=0;i--){
					var pokerObj = CUR_PLAYED_POKER_ARR[i];

					var x = PLAYER_CFG["3"].xt;
					var y = PLAYER_CFG["3"].yt+(CUR_PLAYED_POKER_ARR.length-1-i)*35;
					pokerObj.x = x;
					pokerObj.y = y;
				}
				
				for (var i=0;i<CUR_PLAYED_POKER_ARR.length;i++){
					var pokerObj = CUR_PLAYED_POKER_ARR[i];
					var pokerPos = getPokerSrcPos(pokerObj);
					
					ctx.beginPath();
					ctx.save();
					ctx.translate(pokerObj.x,pokerObj.y);
					ctx.rotate(Math.PI/2);
					ctx.globalAlpha = transparent;
					ctx.drawImage(IMAGE,pokerPos.x0,pokerPos.y0,pokerPos.w0,pokerPos.h0,0,0,35,-50);
					ctx.restore();
					ctx.closePath();
				}
			}
		}
		
		// 显示当前该哪个玩家出牌
		function showNextPlayer(){
			ctx.beginPath();
			ctx.save();
			ctx.lineWidth = 5;
			ctx.strokeStyle="yellow";
			var x = canvas.width/2,y=canvas.height/2;
			var destX,destY;
			ctx.moveTo(x,y);

			if (PLAYER1.canDeliver){
				destX=x;
				destY=y-200;
				
				ctx.moveTo(x,y);
				ctx.lineTo(destX,destY);
				
				ctx.moveTo(destX,destY);
				ctx.lineTo(destX-20,destY+50);
				
				ctx.moveTo(destX,destY);
				ctx.lineTo(destX+20,destY+50);
			} else if (PLAYER2.canDeliver){
				destX=x+200;
				destY=y;
				
				ctx.moveTo(x,y);
				ctx.lineTo(destX,destY);
				
				ctx.moveTo(destX,destY);
				ctx.lineTo(destX-50,destY-20);
				
				ctx.moveTo(destX,destY);
				ctx.lineTo(destX-50,destY+20);
			} else if (PLAYER3.canDeliver){
				destX=x-200;
				destY=y;
				
				ctx.moveTo(x,y);
				ctx.lineTo(destX,destY);
				
				ctx.moveTo(destX,destY);
				ctx.lineTo(destX+50,destY-20);
				
				ctx.moveTo(destX,destY);
				ctx.lineTo(destX+50,destY+20);
			}

			ctx.stroke();
			ctx.restore();
			ctx.closePath();
            
            
			ctx.beginPath();
			ctx.save();
			ctx.lineWidth = 1;
            ctx.font = "normal normal 30px 宋体";
			ctx.strokeStyle="white";
            var remain_time = WAIT_TIME - parseInt((G_CYCLE-START_CYCLE)/60);
			if (PLAYER1.canDeliver){
                ctx.strokeText(remain_time,canvas.width/2-80,30);
			} else if (PLAYER2.canDeliver){
                ctx.strokeText(remain_time,canvas.width-35,canvas.height/2-80);
			} else if (PLAYER3.canDeliver){
                ctx.strokeText(remain_time,5,canvas.height/2+80);
			}

			ctx.stroke();
			ctx.restore();
			ctx.closePath();
		}
		
		// 更新玩家按钮状态
		function updateBtnStates(){
			// 更新不要按钮
			if (CUR_PLAYED_POKER_ARR.length == 0){
				if (PLAYER1.canDeliver){
					$("#player1_cancel_btn").attr("disabled",true);
				}else if (PLAYER2.canDeliver){
					$("#player2_cancel_btn").attr("disabled",true);
				}else if (PLAYER3.canDeliver){
					$("#player3_cancel_btn").attr("disabled",true);
				}
			}else{
				if (CUR_PLAYED_POKER_ARR[0].id == "1"){
					$("#player1_cancel_btn").attr("disabled",true);
					$("#player2_cancel_btn").attr("disabled",false);
					$("#player3_cancel_btn").attr("disabled",false);				
				}else if (CUR_PLAYED_POKER_ARR[0].id == "2"){
					$("#player1_cancel_btn").attr("disabled",false);
					$("#player2_cancel_btn").attr("disabled",true);
					$("#player3_cancel_btn").attr("disabled",false);				
				}else if (CUR_PLAYED_POKER_ARR[0].id == "3"){
					$("#player1_cancel_btn").attr("disabled",false);
					$("#player2_cancel_btn").attr("disabled",false);
					$("#player3_cancel_btn").attr("disabled",true);				
				}
			}
		}

		// 主函数
		function mainFunc(){
			// 初始化扑克
			initPoker();
			
			// 洗牌
			washPoker();
			
			// 随机抽一张牌  谁摸到此牌谁就是地主
			selectLordPoker();
			
			// 发牌
			deliveryPoker();
			
			// 初始化玩家已拿到手的牌
			initUsedPoker();
            
            // 初始化玩家
            initPlayer();
		}

        // 初始化所有变量&扑克初始化&玩家初始化
        function init(){
            POKER_INIT_ARR = new Array();
            
            // 玩家对象
            PLAYER1 = undefined;
            PLAYER2 = undefined
            PLAYER3 = undefined;

            PLAYER1_POKER_ARR = new Array();
            PLAYER2_POKER_ARR = new Array();
            PLAYER3_POKER_ARR = new Array();   

            // 地主扑克标志
            LORD_PORK = undefined;
            
            // 游戏是否开始
            IS_START = false;
            
            // 上一个玩家打出去的牌;
            CUR_PLAYED_POKER_ARR = new Array();
            
            // 提示按钮点击次数统计  方便显示不同组合的牌
            HELP_BTN_CLICK_COUNT = 0;

            // 游戏是否结束
            IS_GAME_OVER = false;
            
            $("#msgText").text("");
            
            mainFunc();
        }
        
		// 获取牌在图片中的源位置
		function getPokerSrcPos(pokerObj){
			var x0,y0;
			var w0=125,h0=181;
			if (pokerObj.type == "红桃"){
				y0 = 0*h0;
			} else if (pokerObj.type == "方块"){
				y0 = 1*h0;
			}else if (pokerObj.type == "梅花"){
				y0 = 2*h0;
			}else if (pokerObj.type == "黑桃"){
				y0 = 3*h0;
			}
			
			if (pokerObj.value == POKER_MAP["2"] ){
				x0 = 0*w0;
			} else if (pokerObj.value == POKER_MAP["大王"] ){
				x0 = 1*w0;
				y0 = 4*h0;
			}else if (pokerObj.value == POKER_MAP["小王"] ){
				x0 = 1*w0;
				y0 = 4*h0;                
			} else{
				x0 = (pokerObj.value-2)*w0;
			}
			
			return {"x0":x0,"y0":y0,"w0":w0,"h0":h0};
		}
		
		// 更新id获取玩家对象
		function getPlayerById(id){
			if (id == 1){
				return PLAYER1;
			} else if (id == 2){
				return PLAYER2;
			} else if (id == 3){
				return PLAYER3;
			}
		}
		
        // 记录出牌日志
        function writeChupaiLog(obj,tempPokerName){
            var log;
            if (obj.isLord){
                log = "地主 :" + tempPokerName;
            }else{
                log = "玩家"+obj.id+":" + tempPokerName;
            }
            for (var i=0;i<CUR_PLAYED_POKER_ARR.length;i++){
                log += " " + CUR_PLAYED_POKER_ARR[i].key;
            }
            var srcLog = $("#msgText").text();
            log = srcLog + "\n"+log;
            $("#msgText").text(log);
            
            var srcollHeight = $("#msgText").attr("scrollHeight");
            $("#msgText").attr("scrollTop",srcollHeight);
        }
        
        // 记录其他日志
        function writeOtherLog(msg){
            var srcLog = $("#msgText").text();
            var log = srcLog + "\n"+msg;
            $("#msgText").text(log);
            
            var srcollHeight = $("#msgText").attr("scrollHeight");
            $("#msgText").attr("scrollTop",srcollHeight);
        }
        
		// 初始化面板
		function initPanel(){
			ctx.beginPath();
			ctx.strokeStyle = "yellow";
			ctx.font = "normal normal 30px 宋体";
			if (PLAYER1 && PLAYER1.isLord){
				ctx.strokeText("玩家1(地主)",canvas.width/2-10,30);
			}else{
				ctx.strokeText("玩家1",canvas.width/2-10,30);
			}

			ctx.stroke();
			ctx.closePath();
			
			ctx.beginPath();
			ctx.save();
			ctx.strokeStyle = "green";
			ctx.font = "normal normal 30px 宋体";
			ctx.translate(10,canvas.height/2+20);
			ctx.rotate(3*Math.PI/2);
			if (PLAYER3 && PLAYER3.isLord){
				ctx.strokeText("玩家3(地主)",0,20);
			}else{
				ctx.strokeText("玩家3",0,20);
			}
			ctx.stroke();
			ctx.restore();
			ctx.closePath();
			
			ctx.beginPath();
			ctx.save();
			ctx.strokeStyle = "red";
			ctx.font = "normal normal 30px 宋体";
			ctx.translate(canvas.width-10,canvas.height/2);
			ctx.rotate(Math.PI/2);
			if (PLAYER2 && PLAYER2.isLord){
				ctx.strokeText("玩家2(地主)",-50,20);
			}else{
				ctx.strokeText("玩家2",-50,20);
			}
			ctx.stroke();
			ctx.restore();
			ctx.closePath();
		}
		
		// 画布点击事件  主要是选择扑克事件
		canvas.addEventListener('click',function(event){
			if (IS_FAPAI || !IS_START){
				return;
			}
			var canvasObj = document.getElementById("canvas");
			var box = canvas.getBoundingClientRect();

			var tempX = parseInt(event.clientX-box.left);
			var tempY = parseInt(event.clientY-box.top);

			if (tempX>=0 && tempX<=canvas.width && tempY>=0 && tempY<=canvas.height){
				for (var i=0;i<POKER_INIT_ARR.length;i++){
					if (isPointInRectangle(tempX,tempY,POKER_INIT_ARR[i])){
						var playerId = POKER_INIT_ARR[i].id;
						if (getPlayerById(playerId).canDeliver){
							if (POKER_INIT_ARR[i].isSelected){
								POKER_INIT_ARR[i].isSelected = false;
							}else{
								POKER_INIT_ARR[i].isSelected = true;
							}
						}
					}
				}
			}
		});
		
        // 检查游戏是否结束
        function checkIsGameOver(){
            if (PLAYER1.isLord && PLAYER1.pokerArr.length == 0){
                IS_GAME_OVER = true;
                writeOtherLog("游戏结束,地主(玩家1)胜利");
            }else if (PLAYER2.isLord && PLAYER2.pokerArr.length == 0){
                IS_GAME_OVER = true;
                writeOtherLog("游戏结束,地主(玩家2)胜利");
            }else if (PLAYER3.isLord && PLAYER3.pokerArr.length == 0){
                IS_GAME_OVER = true;
                console.log("游戏结束,地主(玩家3)胜利");
            }
            
            if (!PLAYER1.isLord && !PLAYER2.isLord && PLAYER1.pokerArr.length == 0 && PLAYER2.pokerArr.length == 0){
                IS_GAME_OVER = true;
                writeOtherLog("游戏结束,农民(玩家1和玩家2)胜利");            
            }else if (!PLAYER1.isLord && !PLAYER3.isLord && PLAYER1.pokerArr.length == 0 && PLAYER3.pokerArr.length == 0){
                IS_GAME_OVER = true;
                writeOtherLog("游戏结束,农民(玩家1和玩家3)胜利");            
            }else if (!PLAYER2.isLord && !PLAYER3.isLord && PLAYER2.pokerArr.length == 0 && PLAYER3.pokerArr.length == 0){
                IS_GAME_OVER = true;
                writeOtherLog("游戏结束,农民(玩家2和玩家3)胜利");            
            }
            
            if (IS_GAME_OVER){
                setTimeout(function(){
                    $("#restartBtn").click();                
                },3000)
            }
        }
        
		// 发牌动画
		function fapaiAnimate(){
			var pokerIndex;
			if (INDEX %3 == 0){
				pokerIndex = INDEX/3-1;
			}else{
				pokerIndex = parseInt(INDEX/3);
			}

			if (pokerIndex > 17){
				setTimeout(function(){
					 // 显示扑克
					for (var i=0;i<POKER_INIT_ARR.length;i++){
                        if (POKER_INIT_ARR[i].id == "1"){
                            POKER_INIT_ARR[i].showMode = 1;
                        }else if (POKER_INIT_ARR[i].id == "2"){
                            POKER_INIT_ARR[i].showMode = 1;
                        }else if (POKER_INIT_ARR[i].id == "3"){
                            POKER_INIT_ARR[i].showMode = 1;
                        }
					}
					IS_FAPAI = false;
					INDEX = 1;
					
					// 更新地主信息
					updateLord();
                    
                    START_CYCLE = G_CYCLE;
				},1000);
				return;
			}
			if (INDEX % 3 == 1){
				PLAYER1_POKER_ARR[pokerIndex].showMode = 2;
			}else if (INDEX % 3 == 2){
				PLAYER2_POKER_ARR[pokerIndex].showMode = 2;
			}else if (INDEX % 3 == 0){
				PLAYER3_POKER_ARR[pokerIndex].showMode = 2;
			}
		}
		
        // 是否正在发牌
		var IS_FAPAI = false;
		var INDEX = 1;
		var G_CYCLE=1;
        // 上一个玩家出牌的时间点
        var START_CYCLE = 0;
		function animate(){
			ctx.clearRect(0,0,canvas.width,canvas.height);
			requestAnimationFrame(animate);

			if (IS_START){
				initPanel();

				if (IS_FAPAI){
					ctx.drawImage(IMAGE,0,181*4,125,181,canvas.width/2-17,canvas.height/2-25,35,50);
					ctx.strokeText("发牌中",canvas.width/2-40,canvas.height/2+70);
					if (G_CYCLE % 2 == 0){
						fapaiAnimate();
						INDEX++;
					}
				}
				
				// 显示扑克
				showPlayerPoker();
				// 显示玩家已打出去的牌
				showPlayerUsedPoker();
				if (!IS_FAPAI){
                    // 显示下一个出牌的玩家
					showNextPlayer();
                    // 显示对应玩家的按钮
					updateBtnStates();
                    
                    // 托管控制&倒计时控制
                    if (!IS_GAME_OVER){
                        if (PLAYER1.canDeliver){
                            if (PLAYER1_AUTO_PLAY){
                                if ((G_CYCLE - START_CYCLE) % 65 ==0){
                                    $("#player1_help_btn").click();
                                    
                                    setTimeout(function(){
                                        $("#player1_chupai_btn").click();
                                    },1000);
                                }
                            }else{
                                if (G_CYCLE - START_CYCLE >= WAIT_TIME*60){
                                    if (G_CYCLE % 50 == 0){
                                        $("#player1_help_btn").click();
                                        
                                        setTimeout(function(){
                                            $("#player1_chupai_btn").click();
                                        },500);
                                    }
                                }
                            }
                        }
                        
                        if (PLAYER2.canDeliver){
                            if (PLAYER2_AUTO_PLAY){
                                if ((G_CYCLE - START_CYCLE) % 65 ==0){
                                    $("#player2_help_btn").click();
                                    
                                    setTimeout(function(){
                                        $("#player2_chupai_btn").click();
                                    },1000);
                                }
                            }else{
                                if (G_CYCLE - START_CYCLE >= WAIT_TIME*60){
                                    if (G_CYCLE % 50 == 0){
                                        $("#player2_help_btn").click();
                                        
                                        setTimeout(function(){
                                            $("#player2_chupai_btn").click();
                                        },500);
                                    }
                                }                            
                            }
                        }
                        
                        if (PLAYER3.canDeliver){
                            if (PLAYER3_AUTO_PLAY){
                                if ((G_CYCLE - START_CYCLE) % 65 ==0){
                                    $("#player3_help_btn").click();
                                    setTimeout(function(){
                                        $("#player3_chupai_btn").click();
                                    },1000);
                                }
                            }else{
                                if (G_CYCLE - START_CYCLE >= WAIT_TIME*60){
                                    if (G_CYCLE % 50 == 0){
                                        $("#player3_help_btn").click();
                                        setTimeout(function(){
                                            $("#player3_chupai_btn").click();
                                        },500);
                                    }
                                } 
                            }
                        }
                    }
				}
                
                // 检查游戏是否结束
                checkIsGameOver();
			}
			G_CYCLE++;
		}

		init();
		animate();
		
        // 开始按钮
		$("#startBtn").click(function(){
			if (!IS_START){
				IS_START = true;
				IS_FAPAI = true;
			}
		})
        
        // 重新开始
		$("#restartBtn").click(function(){
            if (!IS_FAPAI){
                init();
                IS_START = true;
                IS_FAPAI = true;
            }
		})

		// 玩家1出牌
		$("#player1_chupai_btn").click(function(){
			if (!PLAYER1.canDeliver){
				return;
			}
			
			if (PLAYER1.chupai()){
				$("#player1_controller").css("display","none");
				PLAYER1.canDeliver = false;
				
				if (PLAYER2.hasNoPoker()){
					$("#player2_controller").css("display","none");
					$("#player3_controller").css("display","block");
					
					PLAYER2.canDeliver = false;
					PLAYER3.canDeliver = true;
				}else{
					$("#player2_controller").css("display","block");
					$("#player3_controller").css("display","none");
					
					PLAYER2.canDeliver = true;
					PLAYER3.canDeliver = false;
				}
			}
		});
		
		// 玩家2出牌
		$("#player2_chupai_btn").click(function(){
			if (!PLAYER2.canDeliver){
				return;
			}
			
			if (PLAYER2.chupai()){
				$("#player2_controller").css("display","none");
				PLAYER2.canDeliver = false;
				
				if (PLAYER3.hasNoPoker()){
					$("#player1_controller").css("display","block");
					$("#player3_controller").css("display","none");
					
					PLAYER1.canDeliver = true;
					PLAYER3.canDeliver = false;
				}else{
					$("#player1_controller").css("display","none");
					$("#player3_controller").css("display","block");
					
					PLAYER1.canDeliver = false;
					PLAYER3.canDeliver = true;
				}
			}
		});

		// 玩家3出牌
		$("#player3_chupai_btn").click(function(){
			if (!PLAYER3.canDeliver){
				return;
			}

			if (PLAYER3.chupai()){
				$("#player3_controller").css("display","none");
				PLAYER3.canDeliver = false;
				
				if (PLAYER1.hasNoPoker()){
					$("#player1_controller").css("display","none");
					$("#player2_controller").css("display","block");
					
					PLAYER1.canDeliver = false;
					PLAYER2.canDeliver = true;
				}else{
					$("#player1_controller").css("display","block");
					$("#player2_controller").css("display","none");
					
					PLAYER1.canDeliver = true;
					PLAYER2.canDeliver = false;
				}
			}
		});
		
		// 玩家1不要牌
		$("#player1_cancel_btn").click(function(){
			$("#player1_controller").css("display","none");
			PLAYER1.canDeliver = false;
            $("#player1_reset_btn").click();
            HELP_BTN_CLICK_COUNT = 0;
            var log;
            if (PLAYER1.isLord){
                log = "地主 :不出牌";
            }else{
                log = "玩家1:不出牌";
            }
            writeOtherLog(log);
			
			if (PLAYER2.hasNoPoker()){
				$("#player2_controller").css("display","none");
				$("#player3_controller").css("display","block");
				
				PLAYER2.canDeliver = false;
				PLAYER3.canDeliver = true;
			}else{
				$("#player2_controller").css("display","block");
				$("#player3_controller").css("display","none");
				
				PLAYER2.canDeliver = true;
				PLAYER3.canDeliver = false;
			}
		});
		
		//玩家2不要牌
		$("#player2_cancel_btn").click(function(){
			$("#player2_controller").css("display","none");
			PLAYER2.canDeliver = false;
            $("#player2_reset_btn").click();
            HELP_BTN_CLICK_COUNT = 0;
            var log;
            var srcLog = $("#msgText").text();
            if (PLAYER2.isLord){
                log = "地主 :不出牌";
            }else{
                log = "玩家2:不出牌";
            }
            writeOtherLog(log);
			
			if (PLAYER3.hasNoPoker()){
				$("#player3_controller").css("display","none");
				$("#player1_controller").css("display","block");
				
				PLAYER3.canDeliver = false;
				PLAYER1.canDeliver = true;
			}else{
				$("#player3_controller").css("display","block");
				$("#player1_controller").css("display","none");
				
				PLAYER3.canDeliver = true;
				PLAYER1.canDeliver = false;
			}
		});
        
		//玩家3不要牌
		$("#player3_cancel_btn").click(function(){
			$("#player3_controller").css("display","none");
			PLAYER3.canDeliver = false;
            $("#player3_reset_btn").click();
            HELP_BTN_CLICK_COUNT = 0;
            
            var log;
            var srcLog = $("#msgText").text();
            if (PLAYER3.isLord){
                log = "地主 :不出牌";
            }else{
                log = "玩家3:不出牌";
            }
            writeOtherLog(log);
			
			if (PLAYER1.hasNoPoker()){
				$("#player1_controller").css("display","none");
				$("#player2_controller").css("display","block");
				
				PLAYER1.canDeliver = false;
				PLAYER2.canDeliver = true;
			}else{
				$("#player1_controller").css("display","block");
				$("#player2_controller").css("display","none");
				
				PLAYER1.canDeliver = true;
				PLAYER2.canDeliver = false;
			}		
		});
		
		// 玩家1的牌归位
		$("#player1_reset_btn").click(function(){
			PLAYER1.resetPoker();
		})
		// 玩家2的牌归位
		$("#player2_reset_btn").click(function(){
			PLAYER2.resetPoker();
		})
		// 玩家3的牌归位
		$("#player3_reset_btn").click(function(){
			PLAYER3.resetPoker();
		})
        
		// 玩家1提示
		$("#player1_help_btn").click(function(){
			PLAYER1.help();
		})
		// 玩家2提示
		$("#player2_help_btn").click(function(){
			PLAYER2.help();
		})
		// 玩家3提示
		$("#player3_help_btn").click(function(){
			PLAYER3.help();
		})
        
        // 玩家1托管
        $("#player1_tuoguan_ckbox").click(function(){
			var isChecked = $(this).attr("checked");
			if (isChecked){
				PLAYER1_AUTO_PLAY = true;
			}else{
				PLAYER1_AUTO_PLAY = false;
			}
        });
        
        // 玩家2托管
        $("#player2_tuoguan_ckbox").click(function(){
			var isChecked = $(this).attr("checked");
			if (isChecked){
				PLAYER2_AUTO_PLAY = true;
			}else{
				PLAYER2_AUTO_PLAY = false;
			}
        });
        
        // 玩家3托管
        $("#player3_tuoguan_ckbox").click(function(){
			var isChecked = $(this).attr("checked");
			if (isChecked){
				PLAYER3_AUTO_PLAY = true;
			}else{
				PLAYER3_AUTO_PLAY = false;
			}
        });
	</script>
</html>