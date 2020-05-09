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
		<title>音乐播放器</title>
		<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
		<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="<%=path%>/static/viewjs/common.js"></script>
		
		<style type="text/css">
			.selectRowClass{
				background-color:grey;
				color:white;
			}
			.thClass{
				background-color:green;
			}
			
			#musicTable th{
				color:white;
			}
			
			#musicTable td{
				text-align:center;
			}		
		</style>
	</head> 
	<body>
		<div id="uploadDiv" align="center" style="width:1070px;height:40px;margin:5px auto;border:2px solid green">
			<iframe id="iframeUploadPage" width="100%" height="100%" src="upload.page?user=${user}"></iframe>
		</div>
		
		<div id="playerDiv" style="width:1070px;height:auto;border:3px solid green;margin:20px auto;">
			<div id="buttons" style="height:80px;">
				<div align="center" style="background-color:yellow;height:35px;border-bottom:2px solid green;padding-top:7px;">
					当前正在播放:<label id="songName"></label>
				</div>
				<div style="height:32px;">
					<div style="float:left;width:420px;">
						<audio id="player" controls="controls" src="" style="width:100%;"></audio>
					</div>

					<div style="float:right;border:1px solid bulue;margin-top:12px;">
						<div id="playModeDiv" style="float:left">
							<input type="radio" name="playmode" value="-1" checked="checked">无
							<input type="radio" name="playmode" value="0">单曲循环
							<input type="radio" name="playmode" value="1" >顺序播放
							<input type="radio" name="playmode" value="2">随机播放
						</div>
						<div style="float:right;margin-left:30px;">
							<button id="btnPre" onclick="preMusic()">上一首</button>
							<button id="btnPlay" onclick="play()">播放</button>
							<button id="btnNext" onclick="nextMusic()">下一首</button>
							<button id="btnShowList" onclick="showList(this)">关闭列表</button>
							<button id="btnRefresh" onclick="refreash()">刷新</button>
						</div>
					</div>
				</div>
			</div>
			
			<div id="musicList" style="display:block">
				<table id="musicTable" style="width:100%;">
					<tr class="thClass">
						<th style="width:60px;">选择</th>
						<th style="width:200px;">歌曲名称</th>
						<th style="width:60px;">时长</th>
						<th style="width:60px;">大小</th>
						<th style="width:130px;">发行时间</th>
					</tr>
				</table>
			</div>
		</div>
		
	</body>
	<script type="text/javascript">	
	
		// 全局变量
		var playMode = -1; //播放模式   默认为顺序播放
		
		var loginUser = "${user}";
		
		// 播放模式监听
		$("#playModeDiv input[name='playmode']").each(function( index, val ){
			$(val).click(function(){
				playMode = $(val).val();
			})
		})

		function showList(obj){
			if ($(obj).text() == "关闭列表"){
				//$("#musicList").css("display","none");
				$("#musicList").slideUp(500);
				$(obj).text("显示列表");
			}else{
				//$("#musicList").css("display","block");
				$("#musicList").slideDown(500);
				$(obj).text("关闭列表");
			}
		}
		
		function playByIndex(indexNo){
			$("table tr").removeClass("selectRowClass");
			var inputArr = $("table td input"); //所有input对象
			
			var songname = null;
			for (var i=0;i<inputArr.length;i++){
				if (i == indexNo){
					var inputObj = $(inputArr[i]);
					inputObj.attr("checked",true);
					
					var trObj = inputObj.parent().parent();
					trObj.addClass("selectRowClass");
					
					var tdMusicObj = trObj.children()[1];
					var songname = tdMusicObj.innerHTML;
					
					break;
				}
			}
			
			$("#player").attr("src",rootUrl + "/upload/" + loginUser + "/" + songname);
			var player = $("#player")[0];
			$("#songName").html(songname);
			player.play();
		}
		
		// 点击播放按钮
		function play(){
			var inputArr = $("table td input");
			var indexNo = 0;
			for (var i=0;i<inputArr.length;i++){
				var isChecked = inputArr[i].checked;
				if (isChecked == true){
					indexNo = i;
					break;	
				}
			}
			playByIndex(indexNo);
		}

		// 下一首
		function nextMusic(){
			var inputArr = $("table td input");
			var indexNo = 0;
			for (var i=0;i<inputArr.length;i++){
				var isChecked = inputArr[i].checked;
				if (isChecked == true){
					if (i==inputArr.length-1){
						indexNo = 0;
					}else{
						indexNo = i+1;
					}
						
					break;	
				}
			}
			playByIndex(indexNo);
		}
		
		// 上一首
		function preMusic(){
			var inputArr = $("table td input");
			var indexNo = 0;
			for (var i=0;i<inputArr.length;i++){
				var isChecked = inputArr[i].checked;
				if (isChecked == true){
					if (i==0){
						indexNo = inputArr.length-1;
					}else{
						indexNo = i-1;
					}
						
					break;	
				}
			}
			playByIndex(indexNo);
		}

		// 随机播放 
		function randomMusic(){
			var inputArr = $("table td input"); //所有td对象
			var musicNum = inputArr.length;
			var randomNum = randomNumber(0,musicNum-1);
			playByIndex(randomNum);
		}
		
		showMusic();
		// 显示歌曲列表
		function showMusic(){
			var fileType = ".mp3";
			var user = "${user}";
			$.ajax({
				type:"POST",
				url:rootUrl + "file/showFiles.do",
				data:{user:user, fileType:fileType},
				dataType:'json',
				success:function(result){
					var dataList = result.dataList;
					for (var i=0;i<dataList.length;i++){
						var fileName=dataList[i].fileName;
						var author = dataList[i].author;
						var fileSize = dataList[i].fileSize;
						fileSize = (parseInt(fileSize)/1024/1024).toFixed(0);
						var modifyTime = dataList[i].modifyTime;
						var trackLength = dataList[i].trackLength;
						
						var trStr = "<tr><td><input type='radio' name='selectMusic' checked='checked'/></td><td>"+fileName+"</td><td>"+trackLength+"</td><td>"+fileSize+" M</td><td>"+modifyTime+"</td</tr>";
						$("#musicTable").append(trStr);
					}
					
					// 绑定双击事件
					var trList = $("table tr");
					for (var i=0;i<trList.length;i++){
						var className = $(trList[i]).attr("class");
						if (className == "thClass"){
							continue;
						}
						$(trList[i]).dblclick(function(){
							var tdObj = $(this).children().get(0);
							var inputObj = $(tdObj).children().get(0);
							
							var inputArr = $("table td input");
							for (var i=0;i<inputArr.length;i++){
								if (inputObj == inputArr[i]){
									playByIndex(i);
									break;
								}
							}
						})
					}
				},
				failed:function(result){
					console.log("showFiles.do  call failed!");	
				}
			})
		}
		
		// 刷新音乐列表
		function refreash(){
			window.location.href = window.location.href;
		}
		
		// 绑定事件
		Media = document.getElementById("player");
		var eventTester = function(e){
	        Media.addEventListener(e,function(){
	            if (e == "ended"){
	            	if (playMode == 0){
	            		// 单曲循环
	            		play();
	            	}else if (playMode == 1){
	            		//顺序播放
	            		nextMusic();
	            	}else if (playMode == 2){
	            		//随机播放
	            		randomMusic();
	            	}
	            }
	            else if (e == "error"){
	            	if (playMode== 1 || playMode == 2){
	            		nextMusic();
	            	}
	            }
	        });
	    }

	    eventTester("loadstart");    //客户端开始请求数据
	    eventTester("progress");    //客户端正在请求数据
	    eventTester("suspend");        //延迟下载
	    eventTester("abort");        //客户端主动终止下载（不是因为错误引起），
	    eventTester("error");        //请求数据时遇到错误
	    eventTester("stalled");        //网速失速
	    eventTester("play");        //play()和autoplay开始播放时触发
	    eventTester("pause");        //pause()触发
	    eventTester("loadedmetadata");    //成功获取资源长度
	    eventTester("loadeddata");    //
	    eventTester("waiting");        //等待数据，并非错误
	    eventTester("playing");        //开始回放
	    eventTester("canplay");        //可以播放，但中途可能因为加载而暂停
	    eventTester("canplaythrough"); //可以播放，歌曲全部加载完毕
	    eventTester("seeking");        //寻找中
	    eventTester("seeked");        //寻找完毕
	    eventTester("timeupdate");    //播放时间改变
	    eventTester("ended");        //播放结束
	    eventTester("ratechange");    //播放速率改变
	    eventTester("durationchange");    //资源长度改变
	    eventTester("volumechange");    //音量改变
	</script>
</html>
