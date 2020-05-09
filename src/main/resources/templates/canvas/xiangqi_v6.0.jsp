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
	<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>
	
		<style type="text/css">
			body{
				margin:0;
                padding:0;
			}

			button{
                color:white;
				background-color:green;
				border-radius:10px 10px;
			}
		</style>
		
	</head> 
	<body>
		<div id="mainDiv" style="width:985px;height:765px;margin:5px auto;overflow:hidden;border:2px solid green;">
            <canvas id="canvas" style="background-color:grey;margin:5px;float:left">
            </canvas>
            
            <div id="rightDiv" style="float:right;width:290px;height:100%;">
                <button id="startBtn">开&nbsp;&nbsp;&nbsp;始</button>
                <button id="restartBtn">重新开始</button>
				<button id="refreshBtn">刷新</button><br/>
	                棋盘大小:<input id="qpSizeInput" type="range" value=75  min=55 max=75 step=5 onchange="changeQpSize()">
					<span id="qpSize">75</span><br/>
	                自动下棋<input id="autoPlay" style="width:15px;" type="checkbox">&nbsp;
	                显示日志<input id="showLog" style="width:15px;" type="checkbox" checked="checked">
                <canvas id="rightCanvas" style="margin-top:5px;background-color:#124C71">
                </canvas>
                <div id="textAreaDiv" style="width:287px;height:460px;margin:2px 2px 2px 0px;">
                    <textarea id="msgText" style="width:100%;height:100%;color:blue;font-size:10px;">
                    </textarea>
                </div>
            </div>
        </div>
	</body>
	
	<script type="text/javascript">
        // 最大行,最大列
        var MAX_ROW = 10;
        var MAX_COL = 9;

        // 棋盘边界宽度
        var BORDER = 40;
        // 每个方格的宽高(宽=高)
        var DIV_SIZE = 75;
    
        // 主画布
		var canvas = document.getElementById("canvas");
		canvas.width = BORDER*2+(MAX_COL-1)*DIV_SIZE;
		canvas.height = BORDER*2+(MAX_ROW-1)*DIV_SIZE;
		var ctx = canvas.getContext("2d");
        
        // 右侧画布
		var rightCanvas = document.getElementById("rightCanvas");
		rightCanvas.width = 288;
		rightCanvas.height = 220;
		var rightCtx = rightCanvas.getContext("2d");
        
        // 缓冲画布
        var bufferCanvas = document.createElement("canvas");
		bufferCanvas.width = canvas.width;
		bufferCanvas.height = canvas.height;
		var bufferCxt = bufferCanvas.getContext("2d");

        // 象棋半径
        var CHESS_R = DIV_SIZE/2-10;

        // 象棋数组
        var CHESS_ARR = new Array();
        
        // 棋子移动速度
        var SPEED = 5;

        // 游戏是否开始
        var  IS_START = false;
        // 游戏是否结束
        var IS_GAME_OVER = false;
        
        // 是否自动下棋
        var IS_AUTO_PLAY = false;
        
        // 是否显示日志
        var IS_SHOW_LOG = true;
        
        // 当前鼠标位置
        var CUR_POS = "(0,0)";
        
        // 玩家ID
        var PLAYER1_ID = "1";
        var PLAYER2_ID = "2";
        
        // 比赛统计
        var MATCH_STATISTICS = {
            "totalMatch":0,      // 总局数
            "dogfall":0,           // 平局数
            "1":{
                "win":0,           // 胜利局数
                "winRatio":0     // 胜率
            },
            "2":{
                "win":0,
                "winRatio":0
            }
        }
        
        // 玩家配置
        var PLAYER_CFG = {
            "1":{                           // id
                "name":"黑方",          // 玩家名称
                "color":"black",         // 棋子背景颜色
                "step":0,                 // 当前已走步数
                "remainChess":16,    // 剩余棋子数
                "score":0                 // 得分
            },
            "2":{
                "name":"红方",
                "color":"red",
                "step":0,
                "remainChess":16,
                "score":0
            }
        }
		
		// 网格大小与棋子半径,字体大小的对应字典
		var SCREEN_CFG = {
			"55":{                          // 方格大小
				"r":18,                      // 棋子半径
				"fs":20,                    // 棋子字体大小
				"offset":{                 // 象棋文字显示偏移量
					"x":-10,
					"y":8
				},
				"moveover":{           // 鼠标移动上去时,文字显示偏移量
					"x":-10,
					"y":8
				}
			},
			"60":{
				"r":20,
				"fs":22,
				"offset":{
					"x":-11,
					"y":9
				},
				"moveover":{
					"x":-11,
					"y":8
				}
			},
			"65":{
				"r":22,
				"fs":24,
				"offset":{
					"x":-12,
					"y":9
				},
				"moveover":{
					"x":-12,
					"y":8
				}
			},
			"70":{
				"r":25,
				"fs":25,
				"offset":{
					"x":-12,
					"y":10
				},
				"moveover":{
					"x":-13,
					"y":8
				}
			},			
			"75":{
				"r":27,
				"fs":30,
				"offset":{
					"x":-14,
					"y":10
				},
				"moveover":{
					"x":-15,
					"y":10
				}
			}
		}

        // 象棋名称数组
        var CHESS_NAME_ARR = new Array("車","馬","象","士","将","士","象","馬","車");
        
        // 每个棋子对应的分数
        var CHESS_SCORE_MAP = {
            "将":10,
            "帅":10,
            "車":8,
            "炮":6,
            "馬":4,
            "象":3,
            "相":3,
            "士":2,
            "兵":1,
            "卒":1
        }
        
        // 当前出棋方
        var CUR_TURN = undefined;
        
        // 当前选中的象棋
        var CUR_SELECTED_CHESS = undefined;
        
        // 可以下棋的位置
        var DEST_CHESS = undefined;
        // 吃棋对象
        var SRC_CHESS = undefined;
        // 被吃棋对象
        var BE_KILLED_CHESS = undefined;
        // 被吃掉时的时间
        var BE_KILLED_TIME = 0;
        // 胜利方的玩家ID
        var WINNER = undefined;
        
        // 游戏开始和结束时间
        var START_TIME = 0;
        var END_TIME = 0;
		
        // 每秒钟图片刷新次数
		var FPS = 0;
        
        // 全局时钟变量
		var G_CYCLE=1;
        
        // 资源文件
		var QP_IMAGE = new Image();
		QP_IMAGE.src = rootUrl+"/img/qp.jpg";
        
        // 棋盘上各个点的坐标数组
        var QP_POINT_ARR = new Array();
		
        // 初始化 
        function init(){
            // 五子棋数组
            CHESS_ARR = new Array();
            
            PLAYER_CFG = {
                "1":{
                    "name":"黑方",
                    "color":"black",
                    "step":0,
                    "remainChess":16,
                    "score":0
                },
                "2":{
                    "name":"红方",
                    "color":"red",
                    "step":0,
                    "remainChess":16,
                    "score":0
                }
            }
            
            // 游戏是否开始
            IS_START = false;
            // 游戏是否结束
            IS_GAME_OVER = false;
 
            // 当前鼠标位置
            CUR_POS = "(0,0)";
 
            // 当前出棋方
            CUR_TURN = randomSpecifyNumber(PLAYER1_ID,PLAYER2_ID);
            
            // 当前选中的象棋
            CUR_SELECTED_CHESS = undefined;
            
            // 可以下棋的位置
            DEST_CHESS = undefined;
            
            SRC_CHESS = undefined;
            BE_KILLED_CHESS = undefined;
            BE_KILLED_TIME = 0;
            WINNER = undefined;
            
            START_TIME = 0;
            END_TIME = 0;
			
			FPS = 0;

            // 棋盘上各个点的坐标数组
            QP_POINT_ARR = new Array();
            
            // 初始化棋盘坐标数组
            initQpPointArray();
            
            // 初始化象棋位置
            initChessPos();            
        }

        // 象棋类
        function Chess(id,name,x,y,r,fontColor,bgColor,transparent,isSelected,isOver,extendObj){
            this.id = id;                                             //所属玩家ID
            this.name = name;                                   //名称
            this.x = x;                                               //横坐标
            this.y = y;                                               //纵坐标
            this.r = r;                                                //半径
            this.fontColor = fontColor;                         //字体颜色
            this.bgColor = bgColor;                             //背景颜色
            this.transparent = transparent||1;              //背景颜色透明度
            this.isSelected = isSelected||false;            //是否被选中
            this.isOver = false;                                  //鼠标是否经过
            this.extendObj = extendObj||{};                // 扩展字段
			
			this.lastX = this.x;                                   //上一次的坐标
			this.lastY = this.y;
			this.dx = 0;                                             //要移动的速度
			this.dy = 0;
			this.destX = this.x;                                  //要移动的目的坐标
			this.destY = this.y;
			this.clearObj = undefined;                         //待清除的敌军棋子
            
            this.fontSize = SCREEN_CFG[DIV_SIZE].fs;  // 初始化字体大小
            this.changeMode = -1;                              // 字体大小变化量
            
            this.isRunning = false;                             //当前棋子是否在运动中
            this.runMsg = undefined;                          // 运动日志

            // 绘图
            this.draw = function(){
                ctx.beginPath();
                ctx.save();
                ctx.globalAlpha = this.transparent;
                ctx.fillStyle = this.bgColor;
                ctx.strokeStyle =PLAYER_CFG[this.id].color;
                ctx.arc(this.x,this.y,this.r,0,2*Math.PI,false);

                // 绘制被选中时的标记
                if (this.isSelected){
					ctx.lineWidth = 3;
					ctx.moveTo(this.x-this.r-8,this.y-this.r-8);
					ctx.lineTo(this.x-this.r+10,this.y-this.r-8);
				
					ctx.moveTo(this.x-this.r-8,this.y-this.r-8);
					ctx.lineTo(this.x-this.r-8,this.y-this.r+10);
					
					ctx.moveTo(this.x+this.r+8,this.y-this.r-8);
					ctx.lineTo(this.x+this.r-10,this.y-this.r-8);
				
					ctx.moveTo(this.x+this.r+8,this.y-this.r-8);
					ctx.lineTo(this.x+this.r+8,this.y-this.r+10);
					
					ctx.moveTo(this.x-this.r-8,this.y+this.r+8);
					ctx.lineTo(this.x-this.r+10,this.y+this.r+8);
				
					ctx.moveTo(this.x-this.r-8,this.y+this.r+8);
					ctx.lineTo(this.x-this.r-8,this.y+this.r-10);
					
					ctx.moveTo(this.x+this.r+8,this.y+this.r+8);
					ctx.lineTo(this.x+this.r-10,this.y+this.r+8);
				
					ctx.moveTo(this.x+this.r+8,this.y+this.r+8);
					ctx.lineTo(this.x+this.r+8,this.y+this.r-10);
                }else{
                    if (this.isOver){
                        //ctx.lineWidth = 4;
                    }
                }
                ctx.fill();
				ctx.stroke();
                ctx.restore();
                ctx.closePath();

                // 绘制象棋名称
                ctx.beginPath();
                ctx.strokeStyle=this.fontColor;
                if (this.isSelected){
                    this.fontSize = SCREEN_CFG[DIV_SIZE].fs;
                    ctx.font = "normal normal "+this.fontSize+"px 宋体";
                    ctx.strokeText(this.name,this.x-14,this.y+10);
                }else{
                    if (this.isOver){
                        if (this.fontSize <=20){
                            this.fontSize=20;
                            this.changeMode=1;
                        }
						var maxFontSize = SCREEN_CFG[DIV_SIZE].fs+5;
                        if (this.fontSize >= maxFontSize){
                            this.fontSize=maxFontSize;
                            this.changeMode=-1;
                        }
                        
                        this.fontSize += this.changeMode;
                        ctx.font = "normal normal "+this.fontSize+"px 宋体";
                        
						var moveoverFont = SCREEN_CFG[DIV_SIZE]["moveover"];
						var tempx = this.x+moveoverFont.x;
                        var tempy = this.y+moveoverFont.y;
                        ctx.strokeText(this.name,tempx,tempy);
                    }else{
						this.fontSize = SCREEN_CFG[DIV_SIZE].fs;
                        ctx.font = "normal normal "+this.fontSize+"px 宋体";
						var offset = SCREEN_CFG[DIV_SIZE]["offset"];
                        ctx.strokeText(this.name,this.x+offset.x,this.y+offset.y);                    
                    }
                }
                ctx.stroke();
                ctx.closePath();
                
                // 绘制象棋边界
                ctx.beginPath();
                ctx.strokeStyle = "white";
                ctx.lineWidth=1;
                ctx.arc(this.x,this.y,this.r,0,2*Math.PI,false);
                ctx.arc(this.x,this.y,this.r+1,0,2*Math.PI,false);
                ctx.stroke();
                ctx.closePath();
                
                // 绘制其可以走的路线或者可以吃的棋子
                if (this.isSelected){
                    var fillColor = this.bgColor;
                    var strokeColor = "black";
                    var lineWidth = 3;

                    for (var i=0;i<this.extendObj.canGoPosArr.length;i++){
                        ctx.beginPath();
                        ctx.save();
                        ctx.fillStyle = fillColor;
                        ctx.strokeStyle = strokeColor;
                        ctx.lineWidth = lineWidth;
                        var temp = this.extendObj.canGoPosArr[i];
                        ctx.arc(temp.x+1,temp.y+1,10,0,2*Math.PI,false);
                        ctx.stroke();
                        ctx.fill();
                        ctx.restore();
                        ctx.closePath();
                    }

                    for (var i=0;i<this.extendObj.canEatPosArr.length;i++){
                        ctx.beginPath();
                        ctx.save();
                        ctx.fillStyle = fillColor;
                        ctx.strokeStyle = strokeColor;
                        ctx.lineWidth = lineWidth;
                        var temp = this.extendObj.canEatPosArr[i]["destChess"];
                        ctx.arc(temp.x+1,temp.y+1,10,0,2*Math.PI,false);
                        ctx.stroke();
                        ctx.fill();
                        ctx.restore();
                        ctx.closePath();
                    }
                    
                    for (var i=0;i<this.extendObj.beEatPosArr.length;i++){
                        ctx.beginPath();
                        ctx.save();
                        ctx.fillStyle = "yellow";
                        ctx.strokeStyle = strokeColor;
                        ctx.lineWidth = lineWidth;
                        var temp = this.extendObj.beEatPosArr[i]["destChess"];
                        ctx.arc(temp.x+1,temp.y+1,10,0,2*Math.PI,false);
                        ctx.stroke();
                        ctx.fill();
                        ctx.restore();
                        ctx.closePath();
                    }
                }
            }
			
			// 走棋
			this.runChess = function(destX,destY){
				this.destX = destX;
				this.destY = destY;
                
                this.isRunning = true;
                PLAYER_CFG[this.id].step++;
				
				var xDistance = Math.abs(this.lastX - this.destX);
				var yDistance = Math.abs(this.lastY - this.destY);
				if (this.x != this.destX && this.y != this.destY){
                    this.dx = Math.abs(xDistance/DIV_SIZE*SPEED);
					if (this.x > this.destX){
						this.dx = -this.dx;
					}
					
                    this.dy = Math.abs(yDistance/DIV_SIZE*SPEED);
					if (this.y > this.destY){
						this.dy = -this.dy;
					}
				}else if (this.x != this.destX){
                    this.dx = Math.abs(xDistance/DIV_SIZE*SPEED);
					if (this.x > this.destX){
						this.dx = -this.dx ;
					}
				}else if (this.y != this.destY){
                    this.dy = Math.abs(yDistance/DIV_SIZE*SPEED);
					if (this.y > this.destY){
						this.dy = -this.dy;
					}
				}
                
                // 记录移动日志
                var srcInfo   = getRowAndColByXY(this.x,this.y);
                var destInfo = getRowAndColByXY(destX,destY);
                var srcRow   = srcInfo.row;
                var srcCol    = srcInfo.col;
                var destRow = destInfo.row;
                var destCol  = destInfo.col;
                
                this.runMsg = PLAYER_CFG[this.id].name+"将[" + this.name + "]从[" + srcRow+","+srcCol+"]移到["+destRow+","+destCol+"]";
                if (srcRow == destRow){
                     this.runMsg = PLAYER_CFG[this.id].name+"将[" + this.name + "]从第[" + srcCol+"]列移到第["+destCol+"]列";
                }else if (srcCol == destCol){
                     this.runMsg = PLAYER_CFG[this.id].name+"将[" + this.name + "]从第[" + srcRow+"]行移到第["+destRow+"]行";
                }
			}

			// 吃棋
			this.eatChess = function(clearObj,destX,destY){
				this.clearObj = clearObj;
				this.runChess(destX,destY);
			}

            // 更新
            this.update = function(){
                this.x += this.dx;
                this.y += this.dy;
				if (this.x == this.destX){
					this.dx = 0;
					this.lastX = this.destX;
				}
				if (this.y == this.destY){
					this.dy = 0;
					this.lastY = this.destY;
				}
                
                if (this.isRunning){
                    if (this.dx == 0 && this.dy == 0){
                        // 走棋日志
                        writeKillLog(this.runMsg);

                        // 吃棋
                        if (this.clearObj != undefined){
                            // 记录吃棋日志
                            var msg = PLAYER_CFG[this.id].name + "的[" + this.name + "]吃掉了" + PLAYER_CFG[this.clearObj.id].name + "的[" + this.clearObj.name+"]";
                            writeKillLog(msg);

                            // 右侧画布显示吃棋信息
                            SRC_CHESS = this;
                            BE_KILLED_CHESS = this.clearObj;
                            BE_KILLED_TIME = G_CYCLE;
                            
                            PLAYER_CFG[this.clearObj.id].remainChess--;
                            PLAYER_CFG[this.id].score += CHESS_SCORE_MAP[this.clearObj.name];
                            CHESS_ARR.removeArrElement(this.clearObj);
                            this.clearObj = undefined;
                        }
                        this.isRunning = false;

                        // 更新各个棋子可走的路线
                        initChessRoad(CHESS_ARR);
                    }
                }
                this.draw();
            }
        }
        
        // 绑定画布的左键点击事件(左键选择棋子)
        $("#canvas").mousedown(function(event){
            if (!IS_START || IS_GAME_OVER){
                return;
            }
            
            var key_state = (event.type == "mousedown" && event.button==0) ? true:false;
            if (!key_state){
                return;
            }
            
            var mousePos = windowToCanvas(event);
            var mouseX = mousePos.x;
            var mouseY = mousePos.y;
        
            for (var i=0;i<CHESS_ARR.length;i++){
                if (CHESS_ARR[i].id != CUR_TURN){
                    continue
                }
                var distance = getTwoPointDistance(mouseX,mouseY,CHESS_ARR[i].x,CHESS_ARR[i].y);
                if (distance <= CHESS_R){
                    CHESS_ARR[i].isSelected = true;
                    CHESS_ARR[i].isOver = true;
                    CUR_SELECTED_CHESS = CHESS_ARR[i];
                    
                    var chessInfo = getRowAndColByXY(CUR_SELECTED_CHESS.x,CUR_SELECTED_CHESS.y);
                    console.log("已选择棋子:" + PLAYER_CFG[CUR_SELECTED_CHESS.id].name + " " + CUR_SELECTED_CHESS.name + "  位于 "+chessInfo.row+"行 "+chessInfo.col+"列");
                }
                else{
                    CHESS_ARR[i].isSelected = false;
                    CHESS_ARR[i].isOver = false;
                    CUR_SELECTED_CHESS = undefined;
                    DEST_CHESS = undefined;
                }
            }
        });
        
        // 禁用鼠标右击默认菜单
		window.oncontextmenu = function(e){
			e.preventDefault();
		}
        
        // 绑定画布的右键点击事件(右键下棋)
        $("#canvas").mousedown(function(event){
            if (!IS_START || IS_GAME_OVER){
                return;
            }
        
            var key_state = (event.type == "mousedown" && event.button==2) ? true:false;
            if (!key_state){
                return;
            }
            var mousePos = windowToCanvas(event);
            var mouseX = mousePos.x;
            var mouseY = mousePos.y;

            // 绘制落地点位置
            var nearestX = undefined,nearestY = undefined,isRun = false;;
            if (CUR_SELECTED_CHESS != undefined && CUR_SELECTED_CHESS.isSelected){
                for (var j=0;j<QP_POINT_ARR.length;j++){
                    var x = QP_POINT_ARR[j].x;
                    var y = QP_POINT_ARR[j].y;
                    var distance = getTwoPointDistance(mouseX,mouseY,x,y);                        
                    if (distance <= CHESS_R){
                        nearestX = x;
                        nearestY = y;
                        break;
                    }
                }

                if (nearestX == undefined || nearestY == undefined){
                    return;
                }

                //var srcChess = getChessByIdXY(CHESS_ARR,CUR_TURN,CUR_SELECTED_CHESS.x,CUR_SELECTED_CHESS.y);
                var srcChess = getChessByXY(CHESS_ARR,CUR_SELECTED_CHESS.x,CUR_SELECTED_CHESS.y);
                var destChess = getChessByXY(CHESS_ARR,nearestX,nearestY);
                
                // 获取该棋子能走的路线
                var canGoPosArr = srcChess.extendObj.canGoPosArr;
                var canEatPosArr = srcChess.extendObj.canEatPosArr;
                if (destChess == undefined){
                    // 走棋
                    for (var i=0;i<canGoPosArr.length;i++){
                        if (canGoPosArr[i].x == nearestX && canGoPosArr[i].y == nearestY){
                            srcChess.isSelected = false;
                            srcChess.isOver = false;
                            srcChess.runChess(nearestX,nearestY);
                            isRun = true;
                            CUR_SELECTED_CHESS = undefined;
                            break;
                        }
                    }
                }else{
                    if (destChess.id != srcChess.id){
                        // 吃棋
                        for (var i=0;i<canEatPosArr.length;i++){
                            if (canEatPosArr[i]["destChess"] === destChess){
                                srcChess.isSelected = false;
                                srcChess.isOver = false;
                                srcChess.eatChess(destChess,nearestX,nearestY);
                                isRun = true;
                                CUR_SELECTED_CHESS = undefined;
                                DEST_CHESS = undefined;
                                break;
                            }
                        }
                    }
                }

                if (isRun){
                    if (CUR_TURN == PLAYER1_ID){
                        CUR_TURN = PLAYER2_ID;
                    }else{
                        CUR_TURN = PLAYER1_ID;
                    }
                }
            }
        });

        // 鼠标移动事件
		$("#canvas").mousemove(function(event){
            if (!IS_START || IS_GAME_OVER){
                return;
            }
            var mousePos = windowToCanvas(event);
            var mouseX = mousePos.x;
            var mouseY = mousePos.y;

            CUR_POS = "("+mouseX+","+mouseY+")";
            if (CUR_SELECTED_CHESS == undefined || (CUR_SELECTED_CHESS !=undefined && !CUR_SELECTED_CHESS.isSelected)){
                for (var i=0;i<CHESS_ARR.length;i++){
                    if (CHESS_ARR[i].id != CUR_TURN){
                        continue;
                    }
                    var distance = getTwoPointDistance(mouseX,mouseY,CHESS_ARR[i].x,CHESS_ARR[i].y);
                    if (distance <= CHESS_R){
                        CHESS_ARR[i].isOver = true;
                        CUR_SELECTED_CHESS = CHESS_ARR[i];
                        break;
                    }else{
                        CHESS_ARR[i].isOver = false;
                    }
                }
            }
            
            // 绘制落地点位置
            var nearestX = undefined,nearestY = undefined;
            if (CUR_SELECTED_CHESS != undefined && CUR_SELECTED_CHESS.isSelected){
                for (var j=0;j<QP_POINT_ARR.length;j++){
                    var x = QP_POINT_ARR[j].x;
                    var y = QP_POINT_ARR[j].y;
 
                    var distance = getTwoPointDistance(mouseX,mouseY,x,y);                        
                    if (distance <= CHESS_R){
                        nearestX = x;
                        nearestY = y;
                        break;
                    }
                }
                
                if (nearestX == undefined || nearestY == undefined){
                    return;
                }

                //var srcChess = getChessByIdXY(CHESS_ARR,CUR_TURN,CUR_SELECTED_CHESS.x,CUR_SELECTED_CHESS.y);
                var srcChess = getChessByXY(CHESS_ARR,CUR_SELECTED_CHESS.x,CUR_SELECTED_CHESS.y);
                var destChess = getChessByXY(CHESS_ARR,nearestX,nearestY);

                // 获取该棋子能走的路线
                var canGoPosArr = srcChess.extendObj.canGoPosArr;
                var canEatPosArr = srcChess.extendObj.canEatPosArr;
                if (destChess == undefined){
                    // 走棋
                    for (var i=0;i<canGoPosArr.length;i++){
                        if (canGoPosArr[i].x == nearestX && canGoPosArr[i].y == nearestY){
                            DEST_CHESS = new Chess(CUR_TURN,CUR_SELECTED_CHESS.name,nearestX,nearestY,CHESS_R,CUR_SELECTED_CHESS.fontColor,CUR_SELECTED_CHESS.bgColor,0.5,false);
                            break;
                        }
                    }
                }else{
                    if (destChess.id != srcChess.id){
                        // 吃棋
                        for (var i=0;i<canEatPosArr.length;i++){
                            if (canEatPosArr[i]["destChess"] === destChess){
                                DEST_CHESS = new Chess(CUR_TURN,CUR_SELECTED_CHESS.name,nearestX,nearestY,CHESS_R,CUR_SELECTED_CHESS.fontColor,CUR_SELECTED_CHESS.bgColor,0.5,false);
                                break;
                            }
                        }
                    }
                }
            }
		})
        
        // 检查指定的棋是否可以合法(走棋和吃棋)
        // chessArr  当前棋盘中棋子数组
        // srcChess  待走的棋子对象   
        // nearestX,nearestY  目标位置
        // 返回 boolean
        function isValidPosition(chessArr,srcChess,nearestX,nearestY){
            var tempDistance = getTwoPointDistance(nearestX,nearestY,srcChess.x,srcChess.y);
            var destChess = getChessByXY(chessArr,nearestX,nearestY);
            
            var canRun = false;
            if (srcChess.name == "兵" || srcChess.name == "卒"){
                var canGo = false;
                if (tempDistance == DIV_SIZE){
                    if (srcChess.id == PLAYER1_ID){
                        // 全地图只能往下走,在下半部分,可以左右走
                        if (nearestY == canvas.height-BORDER){
                            // 只能左右走
                            canGo = true;
                        }else{
                            if (nearestY >= canvas.height-BORDER-4*DIV_SIZE && nearestY<=canvas.height-BORDER-DIV_SIZE){
                                if (srcChess.y <= nearestY){
                                    canGo = true;
                                }
                            }else{
                                if (srcChess.y < nearestY){
                                    canGo = true;
                                }                            
                            }
                        }  
                    }else{
                         // 全地图只能往上走,在上半部分,可以左右走
                        if (nearestY == BORDER){
                            // 只能左右走
                            canGo = true;
                        }else{
                            if (nearestY >= BORDER+DIV_SIZE && nearestY<=BORDER+4*DIV_SIZE){
                                if (srcChess.y >= nearestY){
                                    canGo = true;
                                }
                            }else{
                                if (srcChess.y > nearestY){
                                    canGo = true;
                                }                            
                            }
                        }
                    }
                }
                canRun = canGo;             
            }else if (srcChess.name == "馬"){
                var canGo = false;
                if (tempDistance > 2*DIV_SIZE && tempDistance < Math.sqrt(6)*DIV_SIZE){
                    if (Math.abs(srcChess.x-nearestX) == DIV_SIZE){
                        if (srcChess.y<nearestY){
                            if (!checkChessExist(chessArr,srcChess.x,srcChess.y+DIV_SIZE)){
                                canGo = true;
                            }
                        }else{
                            if (!checkChessExist(chessArr,srcChess.x,srcChess.y-DIV_SIZE)){
                                canGo = true;
                            }
                        }
                    }else{
                        if (srcChess.x<nearestX){
                            if (!checkChessExist(chessArr,srcChess.x+DIV_SIZE,srcChess.y)){
                                canGo = true;
                            }
                        }else{
                            if (!checkChessExist(chessArr,srcChess.x-DIV_SIZE,srcChess.y)){
                                canGo = true;
                            }
                        }                        
                    }
                }
                canRun = canGo;
            }else if (srcChess.name == "象" || srcChess.name == "相"){
                var canGo = false;
                if (tempDistance > Math.sqrt(7)*DIV_SIZE && tempDistance < 3*DIV_SIZE){
                    if (srcChess.id == PLAYER1_ID){
                        if (nearestX >= BORDER && nearestX <= canvas.width-BORDER 
                            && nearestY >= BORDER && nearestY <= BORDER+DIV_SIZE*4){
                            canGo = true;
                        }
                    }else{
                        if (nearestX >= BORDER && nearestX <= canvas.width-BORDER 
                            && nearestY >= canvas.height-BORDER-DIV_SIZE*4 && nearestY <= canvas.height-BORDER){
                            canGo = true;
                        }
                    }
                }
                if (canGo){
                    if (srcChess.x < nearestX && srcChess.y < nearestY){
                        // 跳至右下角
                        if (!checkChessExist(chessArr,srcChess.x+DIV_SIZE,srcChess.y+DIV_SIZE)){
                            canGo = true;
                        }else{
                            canGo = false;
                        }
                    }else if (srcChess.x < nearestX && srcChess.y > nearestY){
                        // 跳至右上角
                        if (!checkChessExist(chessArr,srcChess.x+DIV_SIZE,srcChess.y-DIV_SIZE)){
                            canGo = true;
                        }else{
                            canGo = false;
                        }
                    }else if (srcChess.x > nearestX && srcChess.y < nearestY){
                        // 跳至左下角
                        if (!checkChessExist(chessArr,srcChess.x-DIV_SIZE,srcChess.y+DIV_SIZE)){
                            canGo = true;
                        }else{
                            canGo = false;
                        }
                    }else if (srcChess.x > nearestX && srcChess.y > nearestY){
                        // 跳至左上角
                        if (!checkChessExist(chessArr,srcChess.x-DIV_SIZE,srcChess.y-DIV_SIZE)){
                            canGo = true;
                        }else{
                            canGo = false;
                        }
                    }
                }
                canRun = canGo;
            }else if (srcChess.name == "士"){
                if (tempDistance > DIV_SIZE && tempDistance<= Math.sqrt(3)*DIV_SIZE){
                    if (srcChess.id == PLAYER2_ID){
                        if (nearestX >= BORDER+DIV_SIZE*3 && nearestX <= canvas.width-BORDER-3*DIV_SIZE 
                            && nearestY >= canvas.height-BORDER-DIV_SIZE*2 && nearestY <= canvas.height-BORDER){
                            canRun = true;
                        }
                    }else{
                        if (nearestX >= BORDER+DIV_SIZE*3 && nearestX <= canvas.width-BORDER-3*DIV_SIZE 
                            && nearestY >= BORDER && nearestY <= BORDER+2*DIV_SIZE){
                            canRun = true;
                        }                   
                    }
                }
            }else if (srcChess.name == "将" || srcChess.name == "帅"){
                if (tempDistance == DIV_SIZE){
                    if (srcChess.id == PLAYER2_ID){
                        if (nearestX >= BORDER+DIV_SIZE*3 && nearestX <= canvas.width-BORDER-3*DIV_SIZE 
                            && nearestY >= canvas.height-BORDER-DIV_SIZE*2 && nearestY <= canvas.height-BORDER){
                            canRun = true;
                        }
                    }else{
                        if (nearestX >= BORDER+DIV_SIZE*3 && nearestX <= canvas.width-BORDER-3*DIV_SIZE 
                            && nearestY >= BORDER && nearestY <= BORDER+2*DIV_SIZE){
                            canRun = true;
                        }
                    }
                }
            }else if (srcChess.name == "車"){
                if ((nearestX == srcChess.x && nearestY != srcChess.y) || (nearestY == srcChess.y && nearestX != srcChess.x)){
                    var canGo = true;
                    var totalChess = getChessNumByTwoPos(chessArr,nearestX,nearestY,srcChess.x,srcChess.y);
                    // 判断源棋和目标棋中间是否有其他棋子,如果有的话则不能下棋
                    if (totalChess > 0){
                        canGo = false;
                    }else{
                        if (checkChessExist(chessArr,nearestX,nearestY)){
                            // 如果有棋子 先判断是否是自己的棋子,若是则返回;如果不是的话,则检查中间是否有其他棋子
                            if (destChess.id == srcChess.id){
                                canGo = false;
                            }
                        }
                    }
                    canRun = canGo;
                }
            }else if (srcChess.name == "炮"){
                if ((nearestX == srcChess.x && nearestY != srcChess.y) || (nearestY == srcChess.y && nearestX != srcChess.x)){
                    var canGo = true;
                    var totalChess = getChessNumByTwoPos(chessArr,nearestX,nearestY,srcChess.x,srcChess.y);
                    if (!checkChessExist(chessArr,nearestX,nearestY)){
                        // 判断源棋和目标棋中间是否没有棋子
                        if (totalChess > 0){
                            canGo = false;
                        }
                    }else{
                        // 判断源棋和目标棋中间是否只有一个棋子
                        if (totalChess != 1){
                            canGo = false;
                        }
                    }
                    canRun = canGo;
                }
            }

            // 玩家1的帅位置
            var player1Boss = getChessByIdName(chessArr,PLAYER1_ID,"帅");
            // 玩家2的将位置
            var player2Boss = getChessByIdName(chessArr,PLAYER2_ID,"将");
            
            if (player1Boss != undefined && player2Boss != undefined){
                if (srcChess.name == "帅" || srcChess.name == "将"){
                    var playerBoss;
                    if (srcChess.id == PLAYER1_ID){
                        // 玩家2的将位置
                        playerBoss = player2Boss;
                    }else{
                        // 玩家1的帅位置
                        playerBoss = player1Boss;
                    }

                    // 帅和将之间的棋子个数    如果将和帅不在同一列 则棋子个数为-1
                    var betweenChessOfBoss = getChessNumByTwoPos(chessArr,nearestX,nearestY,playerBoss.x,playerBoss.y);
                    if (nearestX == playerBoss.x && betweenChessOfBoss == 0){
                        canRun = false;
                    }
                }else{
                    // 帅和将之间的棋子个数    如果将和帅不在同一列 则棋子个数为-1
                    var betweenChessOfBoss = getChessNumByTwoPos(chessArr,player1Boss.x,player1Boss.y,player2Boss.x,player2Boss.y);
                    // 如果将和帅在同一列  且中间只有一个棋子
                    if (betweenChessOfBoss == 1 && player1Boss.x == srcChess.x && player1Boss.x != nearestX){
                        canRun = false;
                    }
                }
            }

            return canRun;
        }
        
        // 检查指定位置是否可以存在棋子(无论是哪方的棋子)
        // chessArr  当前棋盘中棋子数组
        // x,y          棋子坐标
        // 返回 boolean
        function checkChessExist(chessArr,x,y){
            var isExist = false;
            for (var i=0;i<chessArr.length;i++){
                if (chessArr[i].x < BORDER || chessArr[i].x > canvas.width-BORDER || chessArr[i].y<BORDER || chessArr[i].y>canvas.height-BORDER){
                    break;
                }

                if (chessArr[i].x == x && chessArr[i].y == y){
                    isExist = true;
                    break;
                }
            }

            return isExist;
        }

        // 根据给定的位置获取棋子对象
        // chessArr  当前棋盘中棋子数组
        // x,y          棋子坐标
        // 返回 Chess对象
        function getChessByXY(chessArr,x,y){
            for (var i=0;i<chessArr.length;i++){
                if (chessArr[i].x == x && chessArr[i].y == y){
                    return chessArr[i];
                }
            }
        }

        // 根据给定的象棋名称获取指定玩家的棋子
        // chessArr  当前棋盘中棋子数组
        // id           玩家id
        // name      象棋名称
        // 返回 Chess对象        
        function getChessByIdName(chessArr,id,name){
            for (var i=0;i<chessArr.length;i++){
                if (chessArr[i].id == id && chessArr[i].name == name){
                    return chessArr[i];
                }
            }
        }
        
        // 获取两点间的象棋个数 不包括起始结束点  -1表示两点不在水平或者垂直线上
        // chessArr  当前棋盘中棋子数组
        // x1,y1      起始点坐标
        // x2,y2      结束点坐标
        // 返回 int 棋子个数       
        function getChessNumByTwoPos(chessArr,x1,y1,x2,y2){
            var betweenChessOfBoss = -1;
            if (x1 == x2 && y1 != y2){
                betweenChessOfBoss = 0;
                var startY,endY;
                if (y1 > y2){
                    startY = y2,endY=y1;
                }else{
                    startY = y1,endY=y2;
                }
                
                for (var yy=startY+DIV_SIZE;yy<endY;yy+=DIV_SIZE){
                    if (checkChessExist(chessArr,x1,yy)){
                        betweenChessOfBoss++;
                    }
                }
            }else if (x1 != x2 && y1 == y2){
                betweenChessOfBoss = 0;
                var startX,endX;
                if (x1 > x2){
                    startX = x2,endX=x1;
                }else{
                    startX = x1,endX=x2;
                }
                
                for (var xx=startX+DIV_SIZE;xx<endX;xx+=DIV_SIZE){
                    if (checkChessExist(chessArr,xx,y1)){
                        betweenChessOfBoss++;
                    }
                }
            }
            
            return betweenChessOfBoss;
        }
        
        // 检查输赢
        function checkWinner(){
            var p1JiangJun = undefined;
            var p2JiangJun = undefined;
            for (var i=0;i<CHESS_ARR.length;i++){
                if (CHESS_ARR[i].name == "将" || CHESS_ARR[i].name == "帅"){
                    if (CHESS_ARR[i].id == PLAYER1_ID){
                        p1JiangJun = CHESS_ARR[i];
                    }else{
                        p2JiangJun = CHESS_ARR[i];
                    }
                }
            }
            
            if (p1JiangJun != undefined && p2JiangJun != undefined){
                // 检查是否平局
                var isPingju = true;
                for (var i=0;i<CHESS_ARR.length;i++){
                    if (CHESS_ARR[i].name != "士" && CHESS_ARR[i].name != "将" 
						&& CHESS_ARR[i].name != "帅" && CHESS_ARR[i].name != "相"
						&& CHESS_ARR[i].name != "象"){
                        isPingju = false;
                    }
                }
                
                if (isPingju){
                    IS_GAME_OVER = true;
                    IS_START = false;
                    CUR_TURN = undefined;
                    END_TIME = getTime();
                    writeKillLog("平局,游戏结束!");
                    writeStatisticsLog();
                    MATCH_STATISTICS["dogfall"]++;
					MATCH_STATISTICS["totalMatch"]++;
					if (IS_AUTO_PLAY){
						setTimeout(function(){
							$("#restartBtn").click();
						},3000);
					}
                }
                
                // 如果走棋步数超过500  则认为平局
                if (PLAYER_CFG[PLAYER1_ID].step > 500 || PLAYER_CFG[PLAYER2_ID].step > 500){
                    IS_GAME_OVER = true;
                    IS_START = false;
                    CUR_TURN = undefined;
                    END_TIME = getTime();
                    writeKillLog("平局,游戏结束!");
                    writeStatisticsLog();
                    MATCH_STATISTICS["dogfall"]++;
                    MATCH_STATISTICS["totalMatch"]++;
                    if (IS_AUTO_PLAY){
                        setTimeout(function(){
                            $("#restartBtn").click();
                        },3000);
                    }
                }
                
                return;
            }

            if (p2JiangJun != undefined){
                WINNER = PLAYER2_ID;
            }else{
                WINNER = PLAYER1_ID;
            }
            IS_GAME_OVER = true;
            IS_START = false;
            CUR_TURN = undefined;
            END_TIME = getTime();
            writeKillLog(PLAYER_CFG[WINNER].name + "赢了,游戏结束!");
            writeStatisticsLog(); 
            MATCH_STATISTICS[WINNER]["win"]++;
            MATCH_STATISTICS["totalMatch"]++;
            
            var win1Ratio =  MATCH_STATISTICS[PLAYER1_ID]["win"]*100/(MATCH_STATISTICS[PLAYER1_ID]["win"]+MATCH_STATISTICS[PLAYER2_ID]["win"]);
            win1Ratio = parseFloat(win1Ratio.toFixed(1));
            var win2Ratio = MATCH_STATISTICS[PLAYER2_ID]["win"]*100/(MATCH_STATISTICS[PLAYER1_ID]["win"]+MATCH_STATISTICS[PLAYER2_ID]["win"]);
            win2Ratio = parseFloat(win2Ratio.toFixed(1));
            
            MATCH_STATISTICS[PLAYER1_ID]["winRatio"] = win1Ratio;
            MATCH_STATISTICS[PLAYER2_ID]["winRatio"] = win2Ratio;

			if (IS_AUTO_PLAY){
				setTimeout(function(){
					$("#restartBtn").click();
				},3000);
			}
        }
        
        // 绘制吃棋动画
        function writeCanvasLog(){
            if (SRC_CHESS != undefined && BE_KILLED_CHESS != undefined){
                rightCtx.beginPath();
                rightCtx.save();
                rightCtx.fillStyle = SRC_CHESS.bgColor;
                rightCtx.arc(45,150,25,0,2*Math.PI,false);
                rightCtx.fill();
                rightCtx.closePath();
                
                rightCtx.beginPath();
                rightCtx.strokeStyle=SRC_CHESS.fontColor;
                var fontSize = 30;
                rightCtx.font = "normal normal "+fontSize+"px 宋体";
                rightCtx.strokeText(SRC_CHESS.name,30,160);
                rightCtx.stroke();
                rightCtx.closePath();
                
                
                rightCtx.beginPath();
                rightCtx.strokeStyle="white";
                rightCtx.font = "normal normal 25px 宋体";
                rightCtx.strokeText("Killed",83,158);
                rightCtx.stroke();
                rightCtx.closePath();
                
                rightCtx.beginPath();
                rightCtx.save();
                rightCtx.fillStyle = BE_KILLED_CHESS.bgColor;
                rightCtx.arc(195,150,25,0,2*Math.PI,false);
                rightCtx.fill();
                rightCtx.closePath();
                
                rightCtx.beginPath();
                rightCtx.strokeStyle=BE_KILLED_CHESS.fontColor;
                rightCtx.font = "normal normal "+fontSize+"px 宋体";
                rightCtx.strokeText(BE_KILLED_CHESS.name,180,160);
                rightCtx.stroke();
                rightCtx.closePath();
                
                rightCtx.beginPath();
                rightCtx.strokeStyle="rgb(0,255,0)";
                rightCtx.font = "normal normal 35px 宋体";
                rightCtx.strokeText("+"+CHESS_SCORE_MAP[BE_KILLED_CHESS.name],233,162);
                rightCtx.stroke();
                rightCtx.closePath();
                
            }
        }
        
        // 记录吃棋日志
        function writeKillLog(msg){
            if (!IS_SHOW_LOG){
                return;
            }
            var srcLog = $("#msgText").text();
            var log = srcLog + "\n"+ "["+getTime()+ "] " + msg;
            $("#msgText").text(log);
            var srcollHeight = $("#msgText").attr("scrollHeight");
            $("#msgText").attr("scrollTop",srcollHeight);
            //console.log(msg);
        }
        
        // 记录游戏结束时的统计日志
        function writeStatisticsLog(){
            if (!IS_SHOW_LOG){
                return;
            }
            writeKillLog("------------------------");
            writeKillLog("开始时间:" + START_TIME);
            writeKillLog("结束时间:" + END_TIME);
            writeKillLog("");
            writeKillLog(PLAYER_CFG[PLAYER1_ID].name+"最终得分:" + PLAYER_CFG[PLAYER1_ID].score);
            writeKillLog(PLAYER_CFG[PLAYER2_ID].name+"最终得分:" + PLAYER_CFG[PLAYER2_ID].score);
            writeKillLog("");
            writeKillLog(PLAYER_CFG[PLAYER1_ID].name+"已走步数:" + PLAYER_CFG[PLAYER1_ID].step);
            writeKillLog(PLAYER_CFG[PLAYER2_ID].name+"已走步数:" + PLAYER_CFG[PLAYER2_ID].step);
            writeKillLog("");
            writeKillLog(PLAYER_CFG[PLAYER1_ID].name+"剩余棋数:" + PLAYER_CFG[PLAYER1_ID].remainChess);
            writeKillLog(PLAYER_CFG[PLAYER2_ID].name+"剩余棋数:" + PLAYER_CFG[PLAYER2_ID].remainChess);
            writeKillLog("------------------------");
        }

        // 获取当前时间
        function getTime(){
            var date = new Date();
            var h = date.getHours();
            var m = date.getMinutes();
            var s = date.getSeconds();
            if (h<10){
                h = "0"+h;
            }
            if (m<10){
                m = "0"+m;
            }
            if (s<10){
                s = "0"+s;
            }
            
            return h+":"+m+":"+s;
        }
        
        // 深拷贝数组
        function deepcopyArr(srcArr){
            var resultArr = new Array();
            for (var i=0;i<srcArr.length;i++){
                var tempObj = Object.assign(new Chess(),srcArr[i]);
                resultArr.push(tempObj);
            }
            
            return resultArr;
        }

        // 获取坐标对应的行和列
        function getRowAndColByXY(x,y){
            var tempX = x - BORDER;
            var tempY = y - BORDER;
            var col = tempX/DIV_SIZE+1;
            var row = tempY / DIV_SIZE+1;
            if (col < 10){
                col = "0"+col;
            }
            if (row < 10){
                row = "0"+row;
            }
            return {"row":row,"col":col};
        }
        
        // 根据行列数返回对应的xy坐标 注意: r和c都是从1开始
        function getXYByRowAndCol(r,c){
            var x = BORDER + c*DIV_SIZE;
            var y = BORDER + r*DIV_SIZE;
            
            return {"x":x,"y":y};
        }
        
        // 自动下棋
        function autoPlay(){
            var playerChessArr = new Array();          // 当前玩家所有棋子
            var canGoPosArr = new Array();             // 当前玩家能走的棋子
            var canEatPosArr = new Array();            // 当前玩家能吃的棋子(存储敌方的棋子)
            var beEatPosArr = new Array();             // 当前玩家被吃的棋子
            
            var bossName = "帅";
            if (CUR_TURN == PLAYER2_ID){
                bossName = "将";
            }

            // 获取当前玩家的棋子
            for (var i=0;i<CHESS_ARR.length;i++){
                if (CHESS_ARR[i].id == CUR_TURN){
                    playerChessArr.push(CHESS_ARR[i]);
                    canGoPosArr  = canGoPosArr.concat(CHESS_ARR[i].extendObj.canGoPosArr);
                    canEatPosArr = canEatPosArr.concat(CHESS_ARR[i].extendObj.canEatPosArr);
                    beEatPosArr  = beEatPosArr.concat(CHESS_ARR[i].extendObj.beEatPosArr);
                }
            }
            
            if (playerChessArr.length == 0){
                return;
            }

            if (canGoPosArr.length == 0 && canEatPosArr.length == 0 && beEatPosArr.length == 0){
                return;
            }
            
            // 优先级    将军被吃>被吃的棋子>能吃的棋子>能走的棋子>走棋后将军是否会被吃
            var srcChess = undefined,destChess = undefined;
            
            
            //  第一步:处理我方将被吃掉的棋子
            if (beEatPosArr.length > 0){
                var canEatBossPosArr = new Array();   // 能吃我方将军的棋子数组
                var canEatOtherPosArr = new Array();  // 能吃我方除将军外的棋子数组
                for (var i=0;i<beEatPosArr.length;i++){
                    if (beEatPosArr[i].srcChess.name == bossName){
                        canEatBossPosArr.push(beEatPosArr[i]);
                    }else{
                        canEatOtherPosArr.push(beEatPosArr[i]);
                    }
                }
                
                // 处理我方将军将被吃的逻辑
                if (canEatBossPosArr.length > 0){
                    // 如果对方只有一个棋子能吃我方的将军
                    if (canEatBossPosArr.length == 1){
                        // 先检查我方是否有棋子能够吃掉将吃我方将的棋子,如果有吃掉它;如果没有则将军移动;如果将军无法移动,则随便移动一个我方的棋子
                        for (var i=0;i<canEatPosArr.length;i++){
                            if (canEatPosArr[i]["destChess"].x == canEatBossPosArr[0]["destChess"].x && canEatPosArr[i]["destChess"].y == canEatBossPosArr[0]["destChess"].y){
                                srcChess = canEatPosArr[i].srcChess;
                                destChess = canEatBossPosArr[0]["destChess"];
                                break;
                            }
                        }
                    }
                    
                    // 如果对方有多个棋子能吃我方将军的话
                    if (srcChess == undefined || destChess == undefined){
                        var bossChess = getChessByIdName(CHESS_ARR,CUR_TURN,bossName);
                        var tempCanGoPosArr = bossChess.extendObj.canGoPosArr;       //将军能走的路线
                        var tempCanEatPosArr = bossChess.extendObj.canEatPosArr;     //将军能吃的棋子
                        
                        // 获取能吃掉的棋子  且不会被敌方棋子吃到
                        for (var i=0;i<canEatBossPosArr.length;i++){
                            for (var j=0;j<tempCanEatPosArr.length;j++){
                               if (canEatBossPosArr[i]["destChess"].x == tempCanEatPosArr[j]["destChess"].x && canEatBossPosArr[i]["destChess"].y == tempCanEatPosArr[j]["destChess"].y){
                                    srcChess = tempCanEatPosArr[j].srcChess;
                                    destChess = tempCanEatPosArr[j].destChess;
                                    i = canEatBossPosArr.length;
                                    break;
                                }else{                                    
                                    if (canEatBossPosArr[i]["destChess"].x != tempCanEatPosArr[j]["destChess"].x && canEatBossPosArr[i]["destChess"].y != tempCanEatPosArr[j]["destChess"].y){
                                        srcChess = tempCanEatPosArr[j].srcChess;
                                        destChess = tempCanEatPosArr[j].destChess;
                                        i = canEatBossPosArr.length;
                                        break;
                                    }
                                }
                            }
                        }

                        if (srcChess == undefined || destChess == undefined){
                            if (tempCanGoPosArr.length > 0){
                                for (var i=0;i<canEatBossPosArr.length;i++){
                                    for (var j=0;j<tempCanGoPosArr.length;j++){
                                        if (canEatBossPosArr[i]["destChess"].x != tempCanGoPosArr[j].x && canEatBossPosArr[i]["destChess"].y != tempCanGoPosArr[j].y){
                                            srcChess = tempCanGoPosArr[j].srcChess;
                                            destChess = tempCanGoPosArr[j];
                                            i = canEatBossPosArr.length;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                // // 处理我方除将军将外被吃的逻辑
                if (srcChess == undefined || destChess == undefined){
                    if (canEatOtherPosArr.length > 0){
                        canEatOtherPosArr.sort(function(a,b){
                            return CHESS_SCORE_MAP[b.srcChess.name] - CHESS_SCORE_MAP[a.srcChess.name];  // 由大到小
                        });

                        for (var i=0;i<canEatOtherPosArr.length;i++){
                            srcChess = canEatOtherPosArr[i].srcChess;
                            // 该棋子能走的路线
                            var tempCanGoPosArr = srcChess.extendObj.canGoPosArr;
                            // 该棋子能吃的棋子
                            var tempCanEatPosArr = srcChess.extendObj.canEatPosArr;
                            if (tempCanEatPosArr.length == 0 && tempCanGoPosArr.length == 0){
                                continue;
                            }

                            // 吃棋
                            for (var j=tempCanEatPosArr.length-1;j>=0;j--){
                                var tempSrcChess = tempCanEatPosArr[j]["srcChess"];
                                var tempDestChess = tempCanEatPosArr[j]["destChess"];
                                var srcX = tempSrcChess.x,srcY = tempSrcChess.y;
                                var destX = tempDestChess.x,destY = tempDestChess.y;
                                
                                var tempChessArr = deepcopyArr(CHESS_ARR);
                                
                                // remove 掉 tempChessArr中的destX,destY棋子
                                // 将tempSrcChess的x和y移到destX,destY
                                // 最后遍历tempChessArr 看将军是否受到威胁 如果受到威胁 则剔除该吃棋方案
                                tempChessArr.removeArrElement(tempDestChess);
                                var sc = getChessByXY(tempChessArr,srcX,srcY);
								if (sc == undefined){
									continue;
								}
                                sc.x = destX,sc.y = destY;

                                initChessRoad(tempChessArr);
                                
                                var bossObj = getChessByIdName(tempChessArr,CUR_TURN,bossName);
                                if (bossObj.extendObj.beEatPosArr.length > 0){
                                    // 这个棋不能走
                                    tempCanEatPosArr.splice(j,1);
                                    continue;
                                }

                                if (sc.extendObj.beEatPosArr.length > 0){
                                    // 这个棋也不能走
                                    tempCanEatPosArr.splice(j,1);
                                    continue;
                                }
                            }
                            
                            if (tempCanEatPosArr.length > 0){
                                destChess = tempCanEatPosArr[randomNumber(0,tempCanEatPosArr.length-1)].destChess;
                                break;
                            }
                            
                            // 走棋
                            if (destChess == undefined){
                                for (var m=tempCanGoPosArr.length-1;m>=0;m--){
                                    var tempSrcChess = tempCanGoPosArr[m].srcChess;
                                    var tempDestChess = tempCanGoPosArr[m];
                                    var srcX = tempSrcChess.x,srcY = tempSrcChess.y;
                                    var destX = tempDestChess.x,destY = tempDestChess.y;

                                    var tempChessArr = deepcopyArr(CHESS_ARR);
                                    var sc = getChessByXY(tempChessArr,srcX,srcY);
									if (sc == undefined){
										continue;
									}
                                    sc.x = destX,sc.y = destY;

                                    initChessRoad(tempChessArr);
                                    
                                    // 检查将军是否被吃
                                    var bossObj = getChessByIdName(tempChessArr,CUR_TURN,bossName);
                                    if (bossObj.extendObj.beEatPosArr.length > 0){
                                        // 这个棋不能走
                                        tempCanGoPosArr.splice(m,1);
                                        continue;
                                    }

                                    if (sc.extendObj.beEatPosArr.length > 0){
                                        // 这个棋也不能走
                                        tempCanGoPosArr.splice(m,1);
                                        continue;
                                    }
                                }
                                if (tempCanGoPosArr.length > 0){
                                    destChess = tempCanGoPosArr[randomNumber(0,tempCanGoPosArr.length-1)];
                                    break;
                                }
                            }

                            if (srcChess == undefined || destChess == undefined){
                                continue;
                            }
                        }
                    }
                }
            }
            
            // 第二步:吃棋  将军被吃>其他棋子被吃>没有棋子被吃>能吃的棋子
            if (srcChess == undefined || destChess == undefined){
                for (var i=canEatPosArr.length-1;i>=0;i--){
                    var tempSrcChess = canEatPosArr[i].srcChess;
                    var tempDestChess = canEatPosArr[i].destChess;
                    var srcX = tempSrcChess.x,srcY = tempSrcChess.y;
                    var destX = tempDestChess.x,destY = tempDestChess.y;
                    
                    var tempChessArr = deepcopyArr(CHESS_ARR);
                    
                    // remove 掉 tempChessArr中的destX,destY棋子
                    // 将tempSrcChess的x和y移到destX,destY
                    // 最后遍历tempChessArr 看将军是否受到威胁 如果受到威胁 则剔除该吃棋方案
                    tempChessArr.removeArrElement(tempDestChess);
                    var sc = getChessByXY(tempChessArr,srcX,srcY);
                    if (sc == undefined){
                        continue;
                    }
                    sc.x = destX,sc.y = destY;
                    
                    initChessRoad(tempChessArr);
                    
                    var bossObj = getChessByIdName(tempChessArr,CUR_TURN,bossName);
                    if (bossObj.extendObj.beEatPosArr.length > 0){
                        // 这个棋不能走
                        canEatPosArr.splice(i,1);
                        continue;
                    }

                    if (sc.extendObj.beEatPosArr.length > 0){
                        // 这个棋也不能走
                        canEatPosArr.splice(i,1);
                        continue;
                    }
                }
            
                // 找出能吃的最大棋
                if (canEatPosArr.length > 0){
					if (canEatPosArr.length == 1){
						srcChess = canEatPosArr[0]["srcChess"];
						destChess = canEatPosArr[0]["destChess"];
					}else{
						canEatPosArr.sort(function(a,b){
							return CHESS_SCORE_MAP[b.destChess.name] - CHESS_SCORE_MAP[a.destChess.name];  // 由大到小
						});

						if (canEatPosArr[0]["destChess"] === canEatPosArr[1]["destChess"]){
							canEatPosArr.sort(function(a,b){
								return CHESS_SCORE_MAP[a.srcChess.name] - CHESS_SCORE_MAP[b.srcChess.name];  // 由小到大
							});
						}

						for (var i=0;i<canEatPosArr.length;i++){
							var tempSrcChess = canEatPosArr[i]["srcChess"];
							if (tempSrcChess.extendObj.canEatPosArr.length == 0 && tempSrcChess.extendObj.canGoPosArr.length == 0){
								continue;
							}
							srcChess = tempSrcChess;
							destChess = canEatPosArr[i]["destChess"];
							break;
						}
					}
                }
            }
            
            // 第三步:走棋   将军被杀>其他棋子被杀>没有棋子被杀>随机走一个棋子
            if (srcChess == undefined || destChess == undefined){
                var finalCanGoArr = new Array();  // 没有任何棋子被吃的走法

                for (var i=canGoPosArr.length-1;i>=0;i--){
                    var tempSrcChess = canGoPosArr[i].srcChess;
                    var tempDestChess = canGoPosArr[i];
                    var srcX = tempSrcChess.x,srcY = tempSrcChess.y;
                    var destX = tempDestChess.x,destY = tempDestChess.y;

                    var tempChessArr = deepcopyArr(CHESS_ARR);
                    var sc = getChessByXY(tempChessArr,srcX,srcY);
                    if (sc == undefined){
                        continue;
                    }
                    sc.x = destX,sc.y = destY;

                    initChessRoad(tempChessArr);

                    if (sc.extendObj.beEatPosArr.length > 0){
                        // 这个棋也不能走
                        continue;
                    }
                    
                    finalCanGoArr.push(canGoPosArr[i]);
                }

                if (finalCanGoArr.length > 0){
                    var randomNum = randomNumber(0,finalCanGoArr.length-1);
                    destChess = finalCanGoArr[randomNum];
                    srcChess = finalCanGoArr[randomNum].srcChess;                
                }
            }

            if (srcChess == undefined || destChess == undefined){
                return;
            }

            var nearestX = destChess.x;
            var nearestY = destChess.y;

            var isRun = false;
            if (!checkChessExist(CHESS_ARR,nearestX,nearestY)){
                srcChess.isSelected = false;
                srcChess.isOver = false;
                srcChess.runChess(nearestX,nearestY);
                isRun = true;
                CUR_SELECTED_CHESS = undefined;
            }else{
                var destChess = getChessByXY(CHESS_ARR,nearestX,nearestY);
                if (destChess.id != CUR_TURN){
                    srcChess.isSelected = false;
                    srcChess.isOver = false;
                    srcChess.eatChess(destChess,nearestX,nearestY);
                    isRun = true;
                    CUR_SELECTED_CHESS = undefined;
                    DEST_CHESS = undefined;
                }
            }
            
            if (isRun){
                if (CUR_TURN == PLAYER1_ID){
                    CUR_TURN = PLAYER2_ID;
                }else{
                    CUR_TURN = PLAYER1_ID;
                }
            }
        }
        
        // 获取当前canvas位于浏览器的坐标
		function windowToCanvas(event){
			var box = canvas.getBoundingClientRect();

			var tempX = parseInt(event.clientX-box.left);
			var tempY = parseInt(event.clientY-box.top);
			return {x:tempX,y:tempY};
		}
        
        // 绘制右侧画布
        function drawRightCanvas(){
            rightCtx.beginPath();
            rightCtx.strokeStyle="#A75EE";
            rightCtx.font = "normal normal 20px 宋体";
            rightCtx.strokeText("当前出棋:",20,30);
            rightCtx.strokeText("当前坐标:",20,60);
            rightCtx.strokeText(CUR_POS,135,60);
            if (CUR_TURN != undefined){
                rightCtx.fillStyle = PLAYER_CFG[CUR_TURN].color;
                rightCtx.fillRect(135,13,70,20);
                rightCtx.fill();
            }
            rightCtx.stroke();
            rightCtx.closePath(); 
           
            rightCtx.beginPath();
            rightCtx.strokeStyle="yellow";
            rightCtx.font = "normal normal 15px 宋体";
            rightCtx.strokeText("得分 步数 平局 胜利 胜率 总局",62,80);
            rightCtx.stroke();
            rightCtx.closePath();            
           
            rightCtx.beginPath();
            rightCtx.strokeStyle="white";
            rightCtx.font = "normal normal 15px 宋体";
            //rightCtx.strokeText(PLAYER_CFG[PLAYER1_ID].name+":",20,100);
            rightCtx.strokeText(PLAYER_CFG[PLAYER1_ID].score,70,100);
            rightCtx.strokeText(PLAYER_CFG[PLAYER1_ID].step,105,100);
            rightCtx.strokeText(MATCH_STATISTICS["dogfall"],145,100);
            rightCtx.strokeText(MATCH_STATISTICS[PLAYER1_ID]["win"],178,100);
            rightCtx.strokeText(MATCH_STATISTICS[PLAYER1_ID]["winRatio"],212,100);
            rightCtx.strokeText(MATCH_STATISTICS["totalMatch"],254,100);
            rightCtx.strokeText(PLAYER_CFG[PLAYER2_ID].name+":",20,120);
            rightCtx.strokeText(PLAYER_CFG[PLAYER2_ID].score,70,120);
            rightCtx.strokeText(PLAYER_CFG[PLAYER2_ID].step,105,120);
            rightCtx.strokeText(MATCH_STATISTICS["dogfall"],145,120);
            rightCtx.strokeText(MATCH_STATISTICS[PLAYER2_ID]["win"],178,120);
            rightCtx.strokeText(MATCH_STATISTICS[PLAYER2_ID]["winRatio"],212,120);
            rightCtx.strokeText(MATCH_STATISTICS["totalMatch"],254,120);
            rightCtx.stroke();
            rightCtx.closePath();
            
            rightCtx.beginPath();
            rightCtx.save();
            rightCtx.fillStyle=PLAYER_CFG[PLAYER1_ID].color;
            rightCtx.fillRect(20,85,40,17);
            rightCtx.fill();
            rightCtx.restore();
            rightCtx.closePath();
            
            rightCtx.beginPath();
            rightCtx.save();
            rightCtx.fillStyle=PLAYER_CFG[PLAYER2_ID].color;
            rightCtx.fillRect(20,105,40,17);
            rightCtx.fill();
            rightCtx.restore();
            rightCtx.closePath();

			rightCtx.beginPath();
			rightCtx.strokeStyle="rgb(0,255,0)";
            rightCtx.font = "normal normal 30px 宋体";
			rightCtx.strokeText(FPS,255,32);
			rightCtx.closePath();

            if (WINNER != undefined){
                rightCtx.beginPath();
                rightCtx.save();
                rightCtx.fillStyle = PLAYER_CFG[WINNER].color;
                rightCtx.fillRect(70,185,70,20);
                rightCtx.fill();
                rightCtx.restore();
                rightCtx.closePath();
                
                rightCtx.beginPath();
                rightCtx.strokeStyle="yellow";
                rightCtx.font = "normal normal 35px 宋体";
                rightCtx.strokeText("胜利",150,210);
                rightCtx.stroke();
                rightCtx.closePath();
            }
            
            // 绘制吃棋动画
            if (G_CYCLE - BE_KILLED_TIME <= 120){
                writeCanvasLog();
            }
        }     

        // 初始化棋盘
        function initPanel(){
            bufferCxt.clearRect(0,0,bufferCanvas.width,bufferCanvas.height);
            var color = "red";
            var showGrid = false;
            for (var r=0;r<MAX_ROW;r++){
                var startX = BORDER;
                var startY = BORDER+r*DIV_SIZE;
                if (showGrid){
                    bufferCxt.beginPath();
                    bufferCxt.strokeStyle = color;
                    bufferCxt.moveTo(startX,startY);
                    bufferCxt.lineTo(bufferCanvas.width-BORDER,startY);
                    bufferCxt.stroke();
                    bufferCxt.closePath();
                }
                
                bufferCxt.beginPath();
                bufferCxt.font = "normal normal 7px 宋体";
                bufferCxt.strokeStyle = "red";
                var text = startY;
                bufferCxt.strokeText(text,0,startY+5);
                bufferCxt.stroke();
                bufferCxt.closePath();
            }
            
            for (var c=0;c<MAX_COL;c++){
                var startX = BORDER+c*DIV_SIZE;
                var startY = BORDER;
                if (showGrid){
                    bufferCxt.beginPath();
                    bufferCxt.strokeStyle = color;
      
                    if (c == 0 || c == MAX_COL-1){
                        bufferCxt.moveTo(startX,startY);
                        bufferCxt.lineTo(startX,bufferCanvas.height-BORDER);
                    }else{
                        bufferCxt.moveTo(startX,startY);
                        bufferCxt.lineTo(startX,BORDER+DIV_SIZE*4);
                        bufferCxt.moveTo(startX,BORDER+DIV_SIZE*5);
                        bufferCxt.lineTo(startX,bufferCanvas.height-BORDER);
                    }
                    
                    bufferCxt.stroke();
                    bufferCxt.closePath();
                }
                
                bufferCxt.beginPath();
                bufferCxt.font = "normal normal 7px 宋体";
                bufferCxt.strokeStyle = "blue";
                var text = startX;
                bufferCxt.strokeText(text,startX-8,10);
                bufferCxt.stroke();
                bufferCxt.closePath();
            }
            
            // draw line
            if (showGrid){
                bufferCxt.beginPath();
                bufferCxt.moveTo(BORDER+DIV_SIZE*3,BORDER);
                bufferCxt.lineTo(BORDER+DIV_SIZE*5,BORDER+DIV_SIZE*2);
                
                bufferCxt.moveTo(BORDER+DIV_SIZE*3,BORDER+DIV_SIZE*2);
                bufferCxt.lineTo(BORDER+DIV_SIZE*5,BORDER);
                
                bufferCxt.moveTo(BORDER+DIV_SIZE*3,canvas.height-BORDER);
                bufferCxt.lineTo(BORDER+DIV_SIZE*5,canvas.height-BORDER-DIV_SIZE*2);
                
                bufferCxt.moveTo(BORDER+DIV_SIZE*3,canvas.height-BORDER-DIV_SIZE*2);
                bufferCxt.lineTo(BORDER+DIV_SIZE*5,canvas.height-BORDER);  
      
                bufferCxt.strokeStyle = color;
                bufferCxt.stroke();
                bufferCxt.closePath();
            }

            // write text
            //bufferCxt.beginPath();
            //bufferCxt.strokeStyle="red";
            //bufferCxt.font = "normal normal 40px 宋体";
            //bufferCxt.strokeText("楚 河",BORDER+DIV_SIZE-10,BORDER+DIV_SIZE*4+50);
            //bufferCxt.strokeText("汉 界",canvas.width-BORDER-DIV_SIZE*2-10,BORDER+DIV_SIZE*4+50);
            //bufferCxt.stroke();
            //bufferCxt.closePath();
        }
  
        // 初始化棋盘坐标数组
        function initQpPointArray(){
            for (var r=0;r<MAX_ROW;r++){
                for (var c=0;c<MAX_COL;c++){
                    var x = BORDER + c*DIV_SIZE;
                    var y = BORDER + r*DIV_SIZE;
                    QP_POINT_ARR.push({"x":x,"y":y});
                }
            }
        }
  
        // 初始化棋的位置
        function initChessPos(){
            var bgColor;
            var fontColor = "white";
            var transparent = 1;
            for (var r=0;r<MAX_ROW;r++){
                if (r==2 || r == MAX_ROW-3){
                    var playerId,c1,c2;
                    if (r == 2){
                        playerId = PLAYER1_ID;
                        bgColor = PLAYER_CFG[playerId].color;
                        c1 = new Chess(playerId,"炮",BORDER+DIV_SIZE,BORDER+DIV_SIZE*2,CHESS_R,fontColor,bgColor,transparent,false);
                        c2 = new Chess(playerId,"炮",canvas.width-BORDER-DIV_SIZE,BORDER+DIV_SIZE*2,CHESS_R,fontColor,bgColor,transparent,false);
                    }else{
                        playerId = PLAYER2_ID;
                        bgColor = PLAYER_CFG[playerId].color;
                        c1 = new Chess(playerId,"炮",BORDER+DIV_SIZE,canvas.height-BORDER-DIV_SIZE*2,CHESS_R,fontColor,bgColor,transparent,false);
                        c2 = new Chess(playerId,"炮",canvas.width-BORDER-DIV_SIZE,canvas.height-BORDER-DIV_SIZE*2,CHESS_R,fontColor,bgColor,transparent,false);
                    }

                    CHESS_ARR.push(c1);
                    CHESS_ARR.push(c2);
                }
            
                for (var c=0;c<MAX_COL;c++){
                    var startX = BORDER+c*DIV_SIZE;
                    var startY = BORDER+r*DIV_SIZE;
                    
                    if (r == 0 || r == MAX_ROW-1){
                        var playerId,name = CHESS_NAME_ARR[c];
                        if (r == 0){
                            playerId = PLAYER1_ID;
                            if (c == 2 || c == 6){
                                name = "相";
                            }else if (c == 4){
                                name = "帅";
                            }
                        }else{
                            playerId = PLAYER2_ID;
                        }
                        bgColor = PLAYER_CFG[playerId].color;
                        var chess = new Chess(playerId,name,startX,startY,CHESS_R,fontColor,bgColor,transparent,false);
                        CHESS_ARR.push(chess);
                    }

                    if (r == 3 || r == 6){
                        var playerId,name;
                        if (r == 3){
                            playerId = PLAYER1_ID;
                            name = "兵";
                        }else{
                            playerId = PLAYER2_ID;
                            name = "卒";
                        }
                        bgColor = PLAYER_CFG[playerId].color;
                        
                        if (c%2 == 0){
                            var chess = new Chess(playerId,name,startX,startY,CHESS_R,fontColor,bgColor,transparent,false);
                            CHESS_ARR.push(chess);                            
                        }
                    }
                }
            }
            
            // 初始化各棋子能走的位置
            initChessRoad(CHESS_ARR);
        }
        
        // 初始化各棋子能走的位置
        function initChessRoad(chessArr){
            // 初始化各棋子能走的位置
            var totalCanEatPosArr = new Array();
			for (var i=0;i<chessArr.length;i++){
                var canGoPosArr = new Array();    // 能走的棋子
                var canEatPosArr = new Array();   // 能吃的棋子
                
                for (var j=0;j<QP_POINT_ARR.length;j++){
                    var x = QP_POINT_ARR[j].x;
                    var y = QP_POINT_ARR[j].y;
                    if (x == chessArr[i].x && y == chessArr[i].y){
                        continue;
                    }

                    if (isValidPosition(chessArr,chessArr[i],x,y)){
                        if (!checkChessExist(chessArr,x,y)){
                            canGoPosArr.push({"x":x,"y":y,"srcChess":chessArr[i]});
                        }else{
                            var dChess = getChessByXY(chessArr,x,y);
                            if (dChess.id != chessArr[i].id){
                                canEatPosArr.push({"srcChess":chessArr[i],"destChess":dChess});
                            }
                        }
                    }                
                }

                chessArr[i]["extendObj"]["canGoPosArr"] = canGoPosArr;
                chessArr[i]["extendObj"]["canEatPosArr"]= canEatPosArr;
                totalCanEatPosArr = totalCanEatPosArr.concat(canEatPosArr);
			}
            
            for (var i=0;i<chessArr.length;i++){
                var beEatPosArr = new Array();    // 被哪些棋子吃
                for (var j=0;j<totalCanEatPosArr.length;j++){
                    var destChessObj = totalCanEatPosArr[j]["destChess"];
                    if (chessArr[i].id == destChessObj.id){
                        if (chessArr[i].x == destChessObj.x && chessArr[i].y == destChessObj.y){
                            beEatPosArr.push({"srcChess":chessArr[i],"destChess":totalCanEatPosArr[j]["srcChess"]});
                        }
                    }
                }
                
                chessArr[i]["extendObj"]["beEatPosArr"]= beEatPosArr;
            }
        }

        // 主函数
		function animate(){
			ctx.clearRect(0,0,canvas.width,canvas.height);
            rightCtx.clearRect(0,0,rightCanvas.width,rightCanvas.height);
			requestAnimationFrame(animate);

            // 绘制棋盘
			ctx.drawImage(QP_IMAGE,-10,-10,canvas.width+25,canvas.height+23);
            ctx.drawImage(bufferCanvas,0,0,canvas.width,canvas.height);
            
            // 绘制玩家已下的棋子
            for (var i=0;i<CHESS_ARR.length;i++){
                CHESS_ARR[i].update();
            }

            if (DEST_CHESS != undefined){
                DEST_CHESS.update();
            }

            // 绘制右侧画布
            drawRightCanvas();

            // 检查输赢
            if (IS_START && !IS_GAME_OVER){
                if (G_CYCLE % 10 == 0){
                    checkWinner();
                }
            }
            
            // 自动下棋
            if (IS_START && !IS_GAME_OVER && IS_AUTO_PLAY){
                if (G_CYCLE % 40 == 0){
                    autoPlay()
                }
            }
            
			G_CYCLE++;
            if (G_CYCLE >= Number.MAX_VALUE){
                G_CYCLE = 1;
            }
		}
        
        // 初始化棋盘
        initPanel();
        
        // 主函数调用入口
		animate();

        // FPS
		var last_g_cycle = G_CYCLE;
		setInterval(function(){
			var cur_g_cycle = G_CYCLE;
			FPS = cur_g_cycle-last_g_cycle;
			last_g_cycle = cur_g_cycle;
		},1000);

        //---------------------------------------------------------jquery code--------------------------------------------------------------
        // 开始按钮事件
        $("#startBtn").click(function(){
            if (!IS_START){
                init();
                
                START_TIME = getTime();
                IS_START = true;
                $("#msgText").text("");
                writeKillLog("比赛开始!");
                
                $("#qpSizeInput").attr("disabled","true");
            }
        })
        
        // 重新开始按钮事件
        $("#restartBtn").click(function(){
            init();
            
            START_TIME = getTime();
            IS_START = true;
            $("#msgText").text("");
            writeKillLog("比赛重新开始!");
            
            $("#qpSizeInput").attr("disabled","true");
        })
        
		// 刷新按钮事件
		$("#refreshBtn").click(function(){
			window.location.href = window.location.href;
		});
		
        // 修改棋盘大小
		function changeQpSize(){
			var curNum =  document.getElementById("qpSizeInput").value;
			DIV_SIZE = parseInt(curNum);
			document.getElementById("qpSize").innerHTML=DIV_SIZE;
            canvas.width = BORDER*2+(MAX_COL-1)*DIV_SIZE;
            canvas.height = BORDER*2+(MAX_ROW-1)*DIV_SIZE;
            ctx = canvas.getContext("2d");

            var rightDivWidth = $("#rightDiv").css("width");
            rightDivWidth = parseInt(rightDivWidth.replace("px",""));

			$("#mainDiv").css("width",canvas.width+rightDivWidth+10);
			$("#mainDiv").css("height",canvas.height+10);
			$("#textAreaDiv").css("height",canvas.height-292);
			
			CHESS_R = SCREEN_CFG[DIV_SIZE].r;
		}
        
		// 自动下棋
		$("#autoPlay").click(function(){
			var apSwitch = $(this).attr("checked");
			if (apSwitch){
				IS_AUTO_PLAY = true;
			}else{
				IS_AUTO_PLAY = false;
			}
		});
        
		// 是否显示日志
		$("#showLog").click(function(){
			var apSwitch = $(this).attr("checked");
			if (apSwitch){
				IS_SHOW_LOG = true;
			}else{
				IS_SHOW_LOG = false;
			}
		});
	</script>
</html>