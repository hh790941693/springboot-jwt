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
            
			span{
				display:inline-block;
				margin-left:5px;
				font-size:15px;
				width:80px;
			}
		</style>
		
	</head> 
	<body>
		<div id="mainDiv" style="width:985px;height:504px;margin:5px auto;overflow:hidden;border:2px solid green;">
                <canvas id="canvas" style="background-color:black;float:left;margin:2px;">
                </canvas>
                
                <div style="width:auto;height:606px;float:left;margin-left:5px;">
                    <div id="controllDiv" style="width:170px;height:375px;border:2px solid green;">
                        <button id="startBtn">开始</button>
                        <button id="endBtn" disabled>结束</button><br/>
                        
                        <button id="changePicBtn">切换图片</button>
                        <button id="resetBtn">重置</button><br/>
                        <button id="autoPlayBtn">自动拼图</button><span id="progressSpan">0%</span><br/>
                        <hr/>
                        
                        <span>选择模式:</span>
                        <select id="selectMode">
                            <option value="1" selected="selected">简单</option>
                            <option value="2">中等</option>
                            <option value="3">困难</option>
                            <option value="4">变态</option>
                            <option value="5">疯狂</option>
                            <option value="99">无敌</option>
                        </select>
                        <span>提示:</span><input id="helpInput" style="width:20px;" type="checkbox">
                        
                        <hr/>
                        <span>已走步数:</span><span id="steps">0</span><br/>
                        
                        <hr/>
                        <span>开始时间:</span><span id="startTimeSpan">--:--:--</span><br/>
                        <span>结束时间:</span><span id="endTimeSpan">--:--:--</span><br/>
                        
                        <span>当前时间:</span><span id="curTimeSpan">--:--:--</span>
                    </div>
                    
                    <div id="helpDiv" style="width:170px;height:110px;display:none;border:2px solid green;margin-top:8px;">
                        <canvas id="helpCanvas" style="background-color:black;"></canvas>
                    </div>
                </div>
        </div>
	</body>
	
	<script type="text/javascript">
        // 主画布
        var canvas = document.getElementById("canvas");
        canvas.width = 800;
        canvas.height = 500;
        var ctx = canvas.getContext("2d");
        
        // 缓冲画布
        var bufferCanvas = document.createElement("canvas");
        bufferCanvas.width = canvas.width;
        bufferCanvas.height = canvas.height;
        var bufferCtx = bufferCanvas.getContext("2d");
        
        // 游戏难度
        var GM_EASY = 1,GM_MEDIUM = 2,GM_DIFFICULT = 3,GM_CRAZY = 4,GM_AMAZING = 5,GM_UNSTOPABLE = 99;
        // 初始化游戏难度
        var GAME_MODE = GM_EASY;
        
        // 游戏难度配置
        var GAME_CFG = {
            "1":{
                "row":3,
                "col":3,
                "interval":10
            },
            "2":{
                "row":4,
                "col":4,
                "interval":7
            },
            "3":{
                "row":5,
                "col":5,
                "interval":5
            },
            "4":{
                "row":6,
                "col":6,
                "interval":3
            },
            "5":{
                "row":7,
                "col":7,
                "interval":2
            },
            "99":{
                "row":20,
                "col":20,
                "interval":1
            },
        }
        
        // 最大行、列数
        var MAX_ROW = GAME_CFG[GAME_MODE].row;
        var MAX_COL = GAME_CFG[GAME_MODE].col;

        // 每个block的大小
        var BLOCK_WIDTH  = canvas.width/MAX_ROW;
        var BLOCK_HEIGHT = canvas.height/MAX_COL;
        
        // 帮助用的画布
        var helpCanvas = document.getElementById("helpCanvas");
        helpCanvas.width = 170;
        helpCanvas.height = 106;
        var helpCtx = helpCanvas.getContext("2d");
        
        var BLOCK_ARR = new Array();

        // 上次选中的块
        var LAST_CLICK_BLOCK = undefined;
        // 当前选中的块
        var CUR_CLICK_BLOCK = undefined;

        // 游戏是否已开始
        var IS_START = false;
        // 是否显示帮助
        var SHOW_HELP = false;
        
        var IS_AUTO_PLAY = false;
        
        // 已走步数
        var WALK_STEP = 0;
        
        // 全局定时器
        var TIMER = undefined;
        
        var picRoot = rootUrl+"/img/pintu/";
        var IMAGE = new Image();
        IMAGE.src = picRoot+"pic1.jpg";
        IMAGE.onload = imgOnload;
        
        function imgOnload(){
            var w =  IMAGE.width;
            var h = IMAGE.height;
            
            var pw = IMAGE.width/MAX_COL;
            var ph = IMAGE.height/MAX_ROW;
            
            var index = 1;
            BLOCK_ARR = new Array();
 
            for (r=1;r<=MAX_ROW;r++){
                for (c=1;c<=MAX_COL;c++){
                    
                    var x = (c-1)*BLOCK_WIDTH;
                    var y = (r-1)*BLOCK_HEIGHT;
                    
                    var dx = (c-1)*pw;
                    var dy = (r-1)*ph;
 
                    bufferCtx.drawImage(IMAGE,dx,dy,pw,ph,x,y,BLOCK_WIDTH,BLOCK_HEIGHT);
                
                    BLOCK_ARR.push(new Block(index,x,y,BLOCK_WIDTH,BLOCK_HEIGHT,x,y,BLOCK_WIDTH,BLOCK_HEIGHT));
                    index++; 
                }
            }
            
            // 洗牌
            for (var i=0;i<MAX_COL*MAX_ROW+100;i++){
                var index1 = randomNumber(1,MAX_ROW*MAX_COL);
                var index2 = randomNumber(1,MAX_ROW*MAX_COL);
                
                exchangeBlock(getBlockById(index1),getBlockById(index2));
            }
        }
        
        function Block(id,x,y,w,h,x0,y0,w0,h0){
            this.id = id;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.x0 = x0;
            this.y0 = y0;
            this.w0 = w0;
            this.h0 = h0;
            this.tempId = this.id;
            
            this.getXY = function(){
                 // 根据id找出x,y坐标
                var col,row;
                if (this.id%MAX_COL == 0){
                    row = this.id/MAX_COL;
                    col = MAX_COL;
                }else{
                    row = parseInt(this.id/MAX_COL)+1;
                    col = this.id%MAX_COL;
                }
                
                this.x = (col-1)*BLOCK_WIDTH;
                this.y = (row-1)*BLOCK_HEIGHT;               
            }
            
            this.draw = function(){
                ctx.beginPath();
                ctx.drawImage(bufferCanvas,this.x0,this.y0,this.w0,this.h0,this.x,this.y,this.w,this.h);
                if (SHOW_HELP){
                    ctx.strokeStyle="yellow";
                    ctx.lineWidth=1;
                    if (GAME_MODE == GM_EASY){
                        ctx.font = "normal normal 40px 宋体";
                        ctx.strokeText(this.id,this.x+90,this.y+90);
                    }else if  (GAME_MODE == GM_MEDIUM){
                        ctx.font = "normal normal 35px 宋体";
                        ctx.strokeText(this.id,this.x+60,this.y+90);                    
                    }else if (GAME_MODE == GM_DIFFICULT){
                        ctx.font = "normal normal 30px 宋体";
                        ctx.strokeText(this.id,this.x+40,this.y+60);                       
                    }else if (GAME_MODE == GM_CRAZY){
                        ctx.font = "normal normal 25px 宋体";
                        ctx.strokeText(this.id,this.x+30,this.y+50);                       
                    }else if (GAME_MODE == GM_AMAZING){
                        ctx.font = "normal normal 20px 宋体";
                        ctx.strokeText(this.id,this.x+25,this.y+40);                       
                    }else if (GAME_MODE == GM_UNSTOPABLE){
                        ctx.font = "normal normal 6px 宋体";
                        ctx.strokeText(this.id,this.x+5,this.y+20);                       
                    }
                    ctx.stroke();
                }
                ctx.closePath();
                
                if (IS_START && typeof(CUR_CLICK_BLOCK) != "undefined" && this === CUR_CLICK_BLOCK){
                    ctx.beginPath();
                    ctx.strokeStyle="yellow";
                    var lineWidth = 2;
                    ctx.lineWidth=lineWidth;
                    //ctx.rect(this.x-lineWidth,this.y-lineWidth,this.w+lineWidth*2,this.h+lineWidth*2);
                    ctx.rect(this.x,this.y,this.w-lineWidth,this.h-lineWidth);
                    ctx.stroke();
                    ctx.closePath();
                }
            }

            this.update = function(){
                this.draw();
            }
        }
        
        // 交换block
        function exchangeBlock(firstBlock,secondBlock){
            var tempBlockId = firstBlock.id;
            var tempX0 = firstBlock.x0;
            var tempY0 = firstBlock.y0;

            firstBlock.id = secondBlock.id;
            secondBlock.id = tempBlockId;
            
            firstBlock.x0 = secondBlock.x0;
            firstBlock.y0 = secondBlock.y0;
            
            secondBlock.x0 = tempX0;
            secondBlock.y0 = tempY0;
        }

        // 检查是否拼图完成
        function checkHasFinish(){
            var index = 1;
            for (var i=0;i<BLOCK_ARR.length;i++){
                if (index != BLOCK_ARR[i].id){
                    return false;
                }
                index++;
            }
            return true;
        }
        
        // 绑定画布的点击事件
        canvas.onclick = function(event){
            if (!IS_START){
                return;
            }
            var canvasObj = document.getElementById("canvas");
            var offleft = canvasObj.offsetLeft;
            var offtop = canvasObj.offsetTop;
            
            var clientX = event.clientX;
            var clientY = event.clientY;
            
            var mouseX = clientX-offleft;
            var mouseY = clientY-offtop;
            // 获取鼠标点击时的坐标位置
            for (var i=0;i<BLOCK_ARR.length;i++){
                if (isPointInRectangle(mouseX,mouseY,BLOCK_ARR[i])){
                    if (typeof(LAST_CLICK_BLOCK) == "undefined"){
                        LAST_CLICK_BLOCK = BLOCK_ARR[i];
                    }else{
                        LAST_CLICK_BLOCK = CUR_CLICK_BLOCK;
                    }
                    CUR_CLICK_BLOCK = BLOCK_ARR[i];
                    
                    if (LAST_CLICK_BLOCK.id != CUR_CLICK_BLOCK.id){
                        // 判断两点之间的距离
                        var distance = getTwoPointDistance(LAST_CLICK_BLOCK.x,LAST_CLICK_BLOCK.y,CUR_CLICK_BLOCK.x,CUR_CLICK_BLOCK.y);
                        // block的对角线距离
                        var duijiaoxianDistance = Math.sqrt(Math.pow(BLOCK_WIDTH,2) + Math.pow(BLOCK_HEIGHT,2));
                        if (distance < duijiaoxianDistance){
                            exchangeBlock(LAST_CLICK_BLOCK,CUR_CLICK_BLOCK);
                             IS_START = true;
                             WALK_STEP++;

                             setStep(WALK_STEP);
                        }
                    }
                    break;
                }
            }
        }

        // 获取当前时间
        function getCurTime(){
            var date = new Date();
            
            var h = date.getHours();
            if (h>=0 && h<10){
                h = "0"+h;
            }
            var m = date.getMinutes();
            if (m>=0 && m<10){
                m = "0"+m;
            }
            var s = date.getSeconds();
            if (s>=0 && s<10){
                s = "0"+s;
            }
            
            return h+":"+m+":"+s;
        }
        
        
        // 根据id获取指定的block
        function getBlockById(id){
            var block;
            for (var i=0;i<BLOCK_ARR.length;i++){
                if (BLOCK_ARR[i].id == id){
                    block = BLOCK_ARR[i];
                    break;
                }
            }

            return block;
        }
        
        // 根据id获取其在数组中的索引号
        function getBlockIndexById(id){
            var index;
            for (var i=0;i<BLOCK_ARR.length;i++){
                if (BLOCK_ARR[i].id == id){
                    index = i;
                    break;
                }
            }

            return index;       
        }

        // 根据数组中的索引号 获取其所在的行号和列号
        function getRowNumByIndex(indexNo){
            var row,col;
            row = parseInt(indexNo/MAX_COL)+1;
            col = indexNo%MAX_COL+1;
            return {"r":row,"c":col};
        }
        
        // 自动拼图
        function autoPlay(){
            if (BLOCK_ARR.length == 0){
                return;
            }
            
            // 本次要洗的牌  每次只排序一次
            var startId = 1;
            for (var i=1;i<=MAX_COL*MAX_ROW+1;i++){
                if (i == MAX_COL*MAX_ROW+1){
                    IS_AUTO_PLAY = false;
                    console.log("自动拼图完成");
                    $("#progressSpan").text("100%");
                    
                    // 重复自动拼图
                    setTimeout(function(){
                        $("#changePicBtn").click();
                        $("#resetBtn").click();
                        IS_AUTO_PLAY = true;                    
                    },2000);

                    return;
                }
            
                 if (BLOCK_ARR[i-1].id == i){
                    //console.log("牌号:"+i+"已经摆好!");
                    continue;
                }
                
                startId = i;
                break;
            } 

            var progress = parseFloat(startId*100/(MAX_COL*MAX_ROW)).toFixed(2);
            $("#progressSpan").text(progress+"%");
            
            for (var j=0;j<=0;j++){
                if (BLOCK_ARR[startId-1].id == startId){
                    //console.log("牌号:"+i+"已经摆好!");
                    break;
                }
            
                // 获取id为i的block的索引号
                var blockIndex = getBlockIndexById(startId);
                // 当前block所处的行号  从1开始
                var curRowNum = getRowNumByIndex(blockIndex).r;
                // 目标行号
                var destRowNum = getRowNumByIndex(startId-1).r;
                
                 // 当前block所处的列号  从1开始
                var curColNum = getRowNumByIndex(blockIndex).c;
                // 目标列号
                var destColNum = getRowNumByIndex(startId-1).c;

                if ((blockIndex+1) % MAX_COL == 0 && parseInt(blockIndex/MAX_COL) == 0){
                    // 牌在最右边 且 牌在最上边  则与左边的牌交换
                     exchangeBlock(BLOCK_ARR[blockIndex],BLOCK_ARR[blockIndex-1]);
                }else if ((blockIndex+1) % MAX_COL == 0){
                    // 牌在最右边
                    if (curRowNum < destRowNum){
                        exchangeBlock(BLOCK_ARR[blockIndex],BLOCK_ARR[blockIndex+MAX_COL]);
                    }else if (curRowNum > destRowNum){
                        exchangeBlock(BLOCK_ARR[blockIndex],BLOCK_ARR[blockIndex-MAX_COL]);
                    }else{
                        exchangeBlock(BLOCK_ARR[blockIndex],BLOCK_ARR[blockIndex-1]);
                    }
                    
                }else if (parseInt(blockIndex/MAX_COL) == 0){
                    // 牌在最上边     与左边的牌交换
                    if (blockIndex != 0){
                        exchangeBlock(BLOCK_ARR[blockIndex],BLOCK_ARR[blockIndex-1]);
                    }
                }else{
                    // 牌在其他地方
                    if (curRowNum == destRowNum){
                        if (curColNum < destColNum){
                            exchangeBlock(BLOCK_ARR[blockIndex],BLOCK_ARR[blockIndex+1]);
                        }else if (curColNum > destColNum){
                            exchangeBlock(BLOCK_ARR[blockIndex],BLOCK_ARR[blockIndex-1]);
                        }
                    }else{
                        if (curColNum < destColNum){
                            exchangeBlock(BLOCK_ARR[blockIndex],BLOCK_ARR[blockIndex+1]);
                        }else if (curColNum > destColNum){
                            exchangeBlock(BLOCK_ARR[blockIndex],BLOCK_ARR[blockIndex-MAX_COL]);
                        }else{
                            exchangeBlock(BLOCK_ARR[blockIndex],BLOCK_ARR[blockIndex-MAX_COL]);
                        }
                        
                    } 
                }
            }
        }
        
        var G_CYCLE = 0;
        // 主函数
        function animate(){
            ctx.clearRect(0,0,canvas.width,canvas.height);

            requestAnimationFrame(animate);

            for (var i=0;i<BLOCK_ARR.length;i++){
                BLOCK_ARR[i].update();
            }

            // 检查是否拼全了
            if (IS_START && checkHasFinish()){
                console.log("恭喜你");
                alert("恭喜你!!");
                IS_START = false;
                
                $("#startBtn").attr("disabled",false);
                $("#changePicBtn").attr("disabled",false);
                $("#resetBtn").attr("disabled",false);
                
                var endtime = getCurTime();
                $("#endTimeSpan").text(endtime);
                clearInterval(TIMER);
                TIMER = undefined;
            }
            
            // 自动拼图
            if (IS_AUTO_PLAY && G_CYCLE % GAME_CFG[GAME_MODE].interval == 0){
                autoPlay();
            }

            G_CYCLE++;
        }
        
        animate();
        
        // 设置步数
        function setStep(step){
            $("#steps").text(step);
            if (step == 0){
                WALK_STEP = 0;
            }
        }
        
        // 启动游戏
        $("#startBtn").click(function(){
            if (!IS_START){
                IS_START = true;
                
                $("#startBtn").attr("disabled",true);
                $("#endBtn").attr("disabled",false);
                $("#changePicBtn").attr("disabled",true);
                $("#resetBtn").attr("disabled",true);
                
                var startTime = getCurTime();
                $("#startTimeSpan").text(startTime);
                
                if (typeof(TIMER) == "undefined"){
                    TIMER = setInterval(function(){
                        var curTime = getCurTime();
                        $("#curTimeSpan").text(curTime);
                    },1000);
                }
            }
        })
        
        // 结束游戏
        $("#endBtn").click(function(){
            if (IS_START){
                IS_START = false;
                
                $("#startBtn").attr("disabled",false);
                $("#endBtn").attr("disabled",false);
                $("#changePicBtn").attr("disabled",false);
                $("#resetBtn").attr("disabled",false);
                
                if (typeof(TIMER) != "undefined"){
                    clearInterval(TIMER);
                    TIMER = undefined;
                }
            }
        })
        
        // 切换图片
        $("#changePicBtn").click(function(){
            var fileAbsname = IMAGE.src.toString();
            var filename = fileAbsname.substring(fileAbsname.lastIndexOf("/")+1);
            
            var fileIndex = parseInt(filename.replace("pic","").replace(".jpg",""));
            var nextFileIndex = fileIndex+1;
            if (nextFileIndex > 7){
                nextFileIndex = 1;
            }

            var newFilename = "pic" + nextFileIndex +".jpg";
            IMAGE.src=picRoot+newFilename;
            
            setStep(0);
            $("#startTimeSpan").text("--:--:--");
            $("#endTimeSpan").text("--:--:--");
            $("#curTimeSpan").text("--:--:--");
        });
        
        // 重置图片
        $("#resetBtn").click(function(){
            imgOnload();
            setStep(0);
            
            $("#startTimeSpan").text("--:--:--");
            $("#endTimeSpan").text("--:--:--");
            $("#curTimeSpan").text("--:--:--");
        });
        
        // 自动拼图
        $("#autoPlayBtn").click(function(){
            if (IS_AUTO_PLAY){
                IS_AUTO_PLAY = false;
            }else{
                IS_AUTO_PLAY = true;
            }
        });
        
        // 选择游戏难度
        $("#selectMode").change(function(){
            var val = $("#selectMode").val();
            GAME_MODE = val;

            MAX_ROW = GAME_CFG[GAME_MODE].row;
            MAX_COL = GAME_CFG[GAME_MODE].col;
            
            BLOCK_WIDTH  = canvas.width/MAX_ROW;
            BLOCK_HEIGHT = canvas.height/MAX_COL;
            
            imgOnload();
        })
        
        // 显示帮助
        $("#helpInput").click(function(){
			var isChecked = $(this).attr("checked");
			if (isChecked){
				SHOW_HELP = true;
                helpCtx.drawImage(IMAGE,0,0,helpCanvas.width,helpCanvas.height);
			}else{
                //helpCtx.clearRect(0,0,helpCanvas.width,helpCanvas.height);
				SHOW_HELP = false;
			}
             $("#helpDiv").slideToggle();
        });
	</script>
</html>