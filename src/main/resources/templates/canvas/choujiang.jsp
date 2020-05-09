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
			}
		</style>
		
	</head> 
	<body>
		<div id="mainDiv" style="width:870px;height:630px;margin:5px auto;overflow:hidden;border:2px solid green">
                <canvas id="canvas" style="background-color:black">
                </canvas>
        </div>
	</body>
	
	<script type="text/javascript">	
        var canvas = document.getElementById("canvas");
        canvas.width = 860;
        canvas.height = 620;
        var ctx = canvas.getContext("2d");
        
        var BLOCK_WIDTH = 100;
        var BLOCK_HEIGHT = 100;
        var BLOCK_GAP = 20;
        
        var BLOCK_ARR = new Array();
        var COLOR_ARR = new Array(
			"rgba(47,138,251,0.7)",
			"rgba(31,254,177,0.3)",
			"rgba(136,160,40,0.7)",
			"rgba(214,36,198,1)",
			"rgba(167,9,177,1)",
			"rgba(210,245,44,0.3)",
			"rgba(191,93,233,0.5)",
			"rgba(10,100,200,1)",
			"rgba(148,203,102,1)",
			"rgba(135,249,104,1)",
			"rgba(55,115,28,1)",
			"rgba(127,145,130,0.7)",
			"rgba(254,98,87,1)",
			"rgba(96,129,229,0.9)",
			"rgba(85,32,204,1)",
			"rgba(177,144,202,1)",
			"rgba(121,217,23,0.3)",
			"rgba(223,201,172,1)",
			"rgba(235,114,238,1)",
			"rgba(164,173,250,1)"
        )
        
        var IMAGE = new Image();
		IMAGE.id="mypic";
        IMAGE.src = rootUrl+"/img/start.png";
        var RUN = false;

        function initBoard(){
            var index = 1;
            BLOCK_ARR = new Array();
            for (var x=BLOCK_GAP;x<canvas.width;x+=BLOCK_WIDTH+BLOCK_GAP){
                BLOCK_ARR.push(new Block(index,x,BLOCK_GAP,BLOCK_WIDTH,BLOCK_HEIGHT,COLOR_ARR[index-1]));
                index++;
            }
            
            for (var y=BLOCK_HEIGHT+BLOCK_GAP*2;y<canvas.height;y+=BLOCK_HEIGHT+BLOCK_GAP){
                 BLOCK_ARR.push(new Block(index,canvas.width-BLOCK_WIDTH-BLOCK_GAP,y,BLOCK_WIDTH,BLOCK_HEIGHT,COLOR_ARR[index-1]));
                 index++;
            }
            
            for (var x=canvas.width-2*BLOCK_WIDTH-2*BLOCK_GAP;x>0;x-=BLOCK_WIDTH+BLOCK_GAP){
                BLOCK_ARR.push(new Block(index,x,canvas.height-BLOCK_HEIGHT-BLOCK_GAP,BLOCK_WIDTH,BLOCK_HEIGHT,COLOR_ARR[index-1]));
                index++;
            }
            
            
            for (y=canvas.height-2*BLOCK_HEIGHT-2*BLOCK_GAP;y>=BLOCK_HEIGHT+2*BLOCK_GAP;y-=BLOCK_HEIGHT+BLOCK_GAP){
                BLOCK_ARR.push(new Block(index,BLOCK_GAP,y,BLOCK_WIDTH,BLOCK_HEIGHT,COLOR_ARR[index-1]));
                index++;            
            }
        }
        
        function Block(id,x,y,w,h,color){
            this.id = id;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.color = color;
            this.tempId = this.id;
            
            this.draw = function(){
                ctx.beginPath();
                ctx.fillStyle = this.color;
                ctx.fillRect(this.x,this.y,this.w,this.h);
                ctx.fill();
                
                ctx.strokeStyle = "white";
                ctx.font = "normal normal 30px 宋体";
                ctx.strokeText(this.id,this.x+this.w/2-10,this.y+this.h/2+10);
                ctx.closePath();
                
                ctx.beginPath();
                ctx.save();
                ctx.strokeStyle = "red";
                ctx.lineWidth=5;
                if (this.tempId == 1){
                    ctx.rect(this.x-10,this.y-10,20+this.w,20+this.h);
                }
                ctx.stroke();
                ctx.restore();
                ctx.closePath();
            }

            this.update = function(){
				if (this.tempId == 1){
					this.w =110;
					this.h =110;
				}else{
					this.w =100;
					this.h =100;					
				}
			
                this.draw();
            }
        }
        
		function getBlockById(tempId){
            for (var i=0;i<BLOCK_ARR.length;i+=1){
				if (tempId == BLOCK_ARR[i].tempId){
					return BLOCK_ARR[i];
				}
			}	
		}
		
        function drawButton(){
            ctx.beginPath();
            ctx.drawImage(IMAGE,canvas.width/2-70,canvas.height/2-50,140,100);
        }
        
        canvas.onclick = function(event){ 
            var canvasObj = document.getElementById("canvas");
            var offleft = canvasObj.offsetLeft;
            var offtop = canvasObj.offsetTop;
            
            var clientX = event.clientX;
            var clientY = event.clientY;
            
            var mouseX = clientX-offleft;
            var mouseY = clientY-offtop;
            // 获取鼠标点击时的坐标位置
            if (mouseX >= canvas.width/2-70 && mouseX <= canvas.width/2-70+140
               && mouseY >= canvas.height/2-50 && mouseY <= canvas.height/2-50+100){
					//$(this).css("cursor","pointer");
                    if (!RUN){
                        RUN = true;
                    }
            }
        }
        
        // 开始转盘
        function startRun(){
             for (var i=0;i<BLOCK_ARR.length;i+=1){
                var block_cur;
                var block_before;
                var block_after;
                if (i == 0){
                    block_cur = BLOCK_ARR[i].tempId;
                    block_before = BLOCK_ARR[BLOCK_ARR.length-1].tempId
                    block_after = BLOCK_ARR[i+1].tempId;
                    
                    BLOCK_ARR[i].tempId = block_before;
                    BLOCK_ARR[i+1].tempId = block_cur;
                    block_cur = block_after;
                }else if (i == BLOCK_ARR.length-1){
                    block_after = BLOCK_ARR[0].tempId;
                    BLOCK_ARR[0].tempId = block_cur;
                    block_cur = block_after;
                }else{ 
                    block_after = BLOCK_ARR[i+1].tempId;
                    
                    BLOCK_ARR[i+1].tempId = block_cur;
                    block_cur = block_after;
                }    
            }
        }
        
        // 保留
        function changeBlockColor(){
            for (var i=0;i<BLOCK_ARR.length;i+=1){
                var block_cur;
                var block_before;
                var block_after;
                if (i == 0){
                    block_cur = BLOCK_ARR[i].color;
                    block_before = BLOCK_ARR[BLOCK_ARR.length-1].color
                    block_after = BLOCK_ARR[i+1].color;
                    
                    BLOCK_ARR[i].color = block_before;
                    BLOCK_ARR[i+1].color = block_cur;
                    block_cur = block_after;
                }else if (i == BLOCK_ARR.length-1){
                    //block_before = BLOCK_ARR[i-1].color;
                    block_after = BLOCK_ARR[0].color;
                    
                    //BLOCK_ARR[i].color = block_before;
                    BLOCK_ARR[0].color = block_cur;
                    block_cur = block_after;
                }else{ 
                    block_after = BLOCK_ARR[i+1].color;
                    
                    BLOCK_ARR[i+1].color = block_cur;
                    block_cur = block_after;
                }    
            }
        }
        
		initBoard();
        var G_CYCLE = 0;
        var interval = 1;

        function animate(){
            ctx.clearRect(0,0,canvas.width,canvas.height);
            requestAnimationFrame(animate);

            drawButton();
            
            for (var i=0;i<BLOCK_ARR.length;i++){
                BLOCK_ARR[i].update();
            }

            G_CYCLE++;
            
            if (RUN && G_CYCLE % interval == 0){
                startRun();
            }
            
            if (RUN && G_CYCLE % 60 == 0){
                interval+=randomNumber(1,5);
                if (interval >= 25){
                    RUN = true;
					interval = 1;
					
					var blockObj = getBlockById(1);
					console.log(blockObj.id);
                }
            }
        }
        
        animate();
  
	</script>
	
</html>