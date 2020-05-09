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
		<title>上传文件</title>
		<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
		<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>
		
		<style type="text/css">
			.selectRowClass{
				background-color:grey;
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

			#btnPre{
				background-image:url(<%=path%>/img/musicIcon2.jpg);
				background-repeat:no repeat;
				//background-attachment:fixed;
				//background-position:center;
				background-position: -280px -23px;
				width:56px;
				height:23px;
				background-size:336px 46px;

			}
		
		</style>
	</head> 
	<body>
		<div id="addMusci" align="right" style="width:800px;height:30px;margin:10px auto;">
			<form action="upload" method="post" enctype="multipart/form-data">
					文件:<input type="file" name="files" multiple="multiple">
					<input type="submit" value="添加歌曲">
			</form>
		</div>
		<div id="playerDiv" style="width:800px;height:auto;border:3px solid grey;margin:-10px auto;">
			<hr>
			<div id="buttons" style="height:62px;">
				<div align="center" style="background-color:yellow">
					当前正在播放:<label id="songName"></label>
				</div>
				<hr>
				<div>
					<div style="float:left;">
						<audio id="player" controls="controls" src=""></audio>
					</div>

					<div style="float:right">
						<button id="btnPre" onclick="preMusic()">上一首</button>
						<button id="btnPlay" onclick="play()">播放</button>
						<button id="btnNext" onclick="nextMusic()">下一首</button>
					</div>
				</div>
			</div>
			
			<div id="musicList">
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
		function playMusic(songname){
			$("#player").attr("src",rootUrl + "/upload/" + songname);
			var player = $("#player")[0];
			$("#songName").html(songname);
			player.play();
		}
	
		function play(){
			var inputArr = $("table td input"); //转换成js对象
			var songname = null;
			for (var i=0;i<inputArr.length;i++){
				var isChecked = inputArr[i].checked;
				console.log(isChecked);
				if (isChecked == true){
					// js对象转换成jquery对象
					var inputObj = $(inputArr[i]);
					var tdObj = inputObj.parent().next()[0];
					var songname=tdObj.innerHTML;
					break;
				}
			}
			
			var player = $("#player")[0];
			$("#songName").html(songname);
			$("#player").attr("src",rootUrl + "/upload/" + songname);
			if (player.paused){
				player.play();
			}else{
				player.pause();
			}
		}
		
		function nextMusic(){
			// 判断当前所处的td是否是最后一个,如果是的话,就播放第一首歌曲
			$("table tr").removeClass("selectRowClass");
			
			var inputArr = $("table td input"); //所有td对象
			var songname = null;
			for (var i=0;i<inputArr.length;i++){
				var isChecked = inputArr[i].checked;
				if (isChecked == true){
					// js对象转换成jquery对象
					var inputObj = $(inputArr[i]);
					// 看其是否有next及兄弟节点
					var nextTrObj = inputObj.parent().parent().next();
					if (nextTrObj.length == 0){
						//var firstObj = inputArr[0];
						var firstInpuObj = $("table input:first");
						$(firstInpuObj).attr("checked",true);
						var firstTdObj = $(firstInpuObj).parent().next()[0];
						var songname=firstTdObj.innerHTML;
						
						var firstTrObj = $(firstInpuObj).parent().parent();
						$(firstTrObj).addClass("selectRowClass");
					}else{
						//var nextTrJsObj = nextTrObj[0]; //js对象
						// 获取第二个td的值
						var secondChildren=nextTrObj.children()[1];
						var firstChildren = $(secondChildren).prev().children()[0];
						firstChildren.checked = true;
						var songname = secondChildren.innerHTML;
						$(nextTrObj).addClass("selectRowClass");
					}
					break;
				}
			}
			playMusic(songname);
		}
		
		function preMusic(){
			// 判断当前所处的td是否是不是第一个,如果是的话,就播放最后一首歌曲
			$("table tr").removeClass("selectRowClass");
			var inputArr = $("table td input"); //所有td对象
			var songname = null;
			for (var i=0;i<inputArr.length;i++){
				var isChecked = inputArr[i].checked;
				if (isChecked == true){
					// js对象转换成jquery对象
					var inputObj = $(inputArr[i]);
					// 看其是否有next及兄弟节点
					var prevTrObj = inputObj.parent().parent().prev();
					var className = prevTrObj[0].className;
					if (className == "thClass"){
						// 播放最后一首歌
						//var firstObj = inputArr[0];
						var lastInpuObj = $("table input:last");
						$(lastInpuObj).attr("checked",true);
						var lastTdObj = $(lastInpuObj).parent().next()[0];
						var songname=lastTdObj.innerHTML;
						
						var lastTrObj = $(lastInpuObj).parent().parent();
						$(lastTrObj).addClass("selectRowClass");
					}else{
						//var nextTrJsObj = nextTrObj[0]; //js对象
						// 获取前一个td的值
						var secondChildren=prevTrObj.children()[1];
						var firstChildren = $(secondChildren).prev().children()[0];
						firstChildren.checked = true;
						var songname = secondChildren.innerHTML;
						
						$(prevTrObj).addClass("selectRowClass");
					}
					break;
				}
			}
			playMusic(songname);
		}
		
		showMusic();
		function showMusic(){
			var fileType = ".mp3";
			$.ajax({
				type:"POST",
				url:rootUrl + "file/showFiles.do",
				data:{fileType:fileType},
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
							$("table tr").removeClass("selectRowClass");
							$(this).addClass("selectRowClass");
							//inputObj.checked = true;
				
							var tdObj = $(this).children().get(0);
							var inputObj = $(tdObj).children().get(0);
							inputObj.checked = true;
							var musicnameObj = $(this).children().get(1);
							
							var songname = musicnameObj.innerHTML;
							playMusic(songname);
						})
					}
				},
				failed:function(result){
					console.log("showFiles.do  call failed!");	
				}
			})
		}
		
		Media = document.getElementById("player");
		var eventTester = function(e){
	        Media.addEventListener(e,function(){
	            console.log((new Date()).getTime(),e);
	            if (e == "ended"){
	            	nextMusic();
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
