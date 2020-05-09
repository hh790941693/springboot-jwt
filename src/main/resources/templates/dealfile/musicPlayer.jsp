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
		<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
		<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
		<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
		<script type="text/javascript" src="<%=path%>/static/viewjs/common.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
		<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>
		
		<style type="text/css">
			.selectRowClass{
				background-color:grey;
				color:white;
			}
			.thClass{
				background-color:green;
			}
			
			.mouseonClass{
				background-color:#ffc107;
			}
			
			#musicTable th{
				color:white;
				text-align:center;
			}
			
			#musicTable td{
				text-align:center;
			}

			#addMusic{
				width:100%;
				height:40px;
				margin:5px auto;
				border-bottom:1px solid grey;
			}
			
			#playerDiv{
				width: 100%;
			    height: 550px;
			    margin: 0px auto;
			    overflow-y: scroll;
			}
		</style>
	</head> 
	<body>
		<div id="addMusic" align="center">
			<form action="uploadFiles.do" method="post" enctype="multipart/form-data" onsubmit="return verify()">
				<input type="text" name="user" value="${user}" hidden="true">
				<input type="text" name="fileType" value="mp3" hidden="true">
				文件:<input id="fileInput" type="file" name="files" multiple="multiple" accept="audio/mpeg">
				<input type="submit" class='btn btn-primary' value="上传歌曲">
			</form>
		</div>		
		<div id="playerDiv">
			<div id="buttons" style="height:110px;">
				<div align="center" style="background-color:yellow;height:35px;border-bottom:2px solid green;padding-top:7px;">
					当前正在播放:<label id="songName"></label>
				</div>
				<div style="height:75px;">
					<div style="float:left;width:300px;margin-top:5px">
						<audio id="player" controls="controls" src="" style="width:100%;"></audio>
					</div>

					<div style="float:right;border:1px solid bulue;margin-top:12px;width:740px;">
						<div style="float:left;margin-left:10px;height:40px;">
							<button id="btnPre" style='height:40px;' class='btn btn-primary' onclick="preMusic()">上一首</button>
							<button id="btnPlay" style='height:40px;width:74px;' class='btn btn-success' onclick="play()">播放</button>
							<button id="btnNext" style='height:40px;' class='btn btn-primary' onclick="nextMusic()">下一首</button>
							<button id="btnShowList" style='height:40px;' class='btn btn-warning' onclick="showList(this)">关闭列表</button>
							<button id="btnRefresh" style='height:40px;' class='btn btn-info' onclick="refreash()">刷&nbsp;&nbsp;&nbsp;&nbsp;新</button>
						</div>
						<div id="playModeDiv" style="float:left;margin-top:10px;margin-left:15px;">
							<input type="radio" name="playmode" value="-1" checked="checked">无
							<input type="radio" name="playmode" value="0">单曲循环
							<input type="radio" name="playmode" value="1" >顺序播放
							<input type="radio" name="playmode" value="2">随机播放
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
						<th style="width:90px;">发行时间</th>
						<th style="width:40px;">操作</th>
						<th style="display:none;">url</th>
					</tr>
				</table>
			</div>
		</div>
	<jsp:include page="../ws/sessionKeeper.html"/>
	</body>

	<script type="text/javascript">
		// 全局变量
		var playMode = -1; //播放模式   默认为顺序播放
		//当前用户
		var loginUser = s_user;
		//播放器对象
		var player = $("#player")[0];

		// 播放模式监听
		$("#playModeDiv input[name='playmode']").each(function(index, val){
			$(val).click(function(){
				playMode = $(val).val();
			})
		})
		
		//上传文件校验
		function verify(){
			var uploadFiles=$("#fileInput").val();
			if (uploadFiles == ""){
				return false;
			}
			console.log("待上传的文件:"+uploadFiles);
			setInterval(function(){
				layer.closeAll();
				layer.msg('上传中', {
				  icon: 16,
				  shade: 0.2
				});
			},1000);
			return true;
		}

		function showList(obj){
			if ($(obj).text() == "关闭列表"){
				$("#musicList").slideUp(500);
				$(obj).text("显示列表");
			}else{
				$("#musicList").slideDown(500);
				$(obj).text("关闭列表");
			}
		}

		function playByIndex(trObj){
			var indexId = $(trObj).attr("id");
			if (indexId == undefined){
				return;
			}
			var url = $(trObj).children("td:last-child").text();
			var songnameObj = $(trObj).children().get(1);
			var songname = $(songnameObj).text();
			var firstTd = $(trObj).children().get(0);
			var inputObj = $(firstTd).children().get(0);
			
			$("table input").removeAttr("checked");
			$(inputObj).prop("checked",true);
			//$(inputObj).attr("checked",true);
			$("table tr").removeClass("selectRowClass");
			$(trObj).addClass("selectRowClass");
			//console.log("start to play:" + url);
			
			$("#player").attr("src",url);
			//var player = $("#player")[0];
			$("#songName").html(songname);
             
			player.play();
		}
		
		// 点击播放按钮
		function play(){
			console.log("start to playing......");
			var inputArr = $("table td input");
			var trObj =  $("table tr").eq(1);
			for (var i=0;i<inputArr.length;i++){
				var isChecked = inputArr[i].checked;
				if (isChecked == true){
					trObj = $(inputArr[i]).parent().parent();
					break;	
				}
			}
			var url = $(trObj).children("td:last-child").text();
			var songnameObj = $(trObj).children().get(1);
			var songname = $(songnameObj).text();
			var firstTd = $(trObj).children().get(0);
			var inputObj = $(firstTd).children().get(0);
			
			$("table input").removeAttr("checked");
			$(inputObj).prop("checked",true);
			//$(inputObj).attr("checked",true);
			$("table tr").removeClass("selectRowClass");
			$(trObj).addClass("selectRowClass");
			//console.log("start to play:" + url);
			
			$("#player").attr("src",url);
			//var player = $("#player")[0];
			$("#songName").html(songname);	
			var btnText = $("#btnPlay").text();
			if (btnText == "播放"){
				btnText = "暂停";
				player.play();
			}else{
				btnText = "播放";
				player.pause();
			}
			$("#btnPlay").text(btnText);
		}

		// 下一首
		function nextMusic(){
			var inputArr = $("table td input");
			var selectTrObj =  $("table tr").eq(1);
			
			for (var i=0;i<inputArr.length;i++){
				var isChecked = inputArr[i].checked;
				if (isChecked == true){
					var trObj = $(inputArr[i]).parent().parent();
					var nextTrObj = $(trObj).next();
					if (nextTrObj.length != 0){
						selectTrObj = nextTrObj;
					}
					break;	
				}
			}
			playByIndex(selectTrObj);
		}
		
		// 上一首
		function preMusic(){
			var inputArr = $("table td input");
			var selectTrObj =  $("table").find("tr").last();

			for (var i=0;i<inputArr.length;i++){
				var isChecked = inputArr[i].checked;
				if (isChecked == true){
					var trObj = $(inputArr[i]).parent().parent();
					var prevTrObj = $(trObj).prev();
					
					if (i!=0){
						selectTrObj = prevTrObj;
					}
					break;	
				}
			}
			playByIndex(selectTrObj);
		}

		// 随机播放 
		function randomMusic(){
			var inputArr = $("table td input"); //所有td对象
			var musicNum = inputArr.length;
			var randomNum = randomNumber(0,musicNum-1);
			
			var inputObj=$(inputArr[randomNum]);
			var trObj = $(inputObj).parent().parent();
			playByIndex(trObj);
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
						var id=dataList[i].id;
						var fileName=dataList[i].filename;
						var author = dataList[i].author;
						var fileSize = dataList[i].fileSize;
						fileSize = (parseInt(fileSize)/1024.0/1024.0).toFixed(0);
						var updateTime = dataList[i].updateTime;
						var month = updateTime.date+1
						var modifyTime = updateTime.month+"-"+month+" " + updateTime.hours+":"+updateTime.minutes+":"+updateTime.seconds;
						var trackLength = dataList[i].trackLength;
						var url = dataList[i].url;
						url = url.replace(url.substring(url.indexOf("//")+2,url.lastIndexOf(":")),parent.s_webserverip);
						if (i==0){
							var trStr = "<tr id='"+id+"'><td><input type='radio' name='selectMusic' checked='checked'/></td><td>"+fileName+"</td><td>"+trackLength+"</td><td>"+fileSize+" M</td><td>"+modifyTime+"</td><td><button class='delBtn btn btn-sm btn-danger'>删除</button></td><td style='display:none'>"+url+"</td></tr>";
						}else{
							var trStr = "<tr id='"+id+"'><td><input type='radio' name='selectMusic'/></td><td>"+fileName+"</td><td>"+trackLength+"</td><td>"+fileSize+" M</td><td>"+modifyTime+"</td><td><button class='delBtn btn btn-sm btn-danger'>删除</button></td><td style='display:none'>"+url+"</td></tr>";
						}
						$("#musicTable").append(trStr);
					}

					// 绑定双击事件
					var trList = $("table tr");
					for (var i=0;i<trList.length;i++){
						var className = $(trList[i]).attr("class");
						if (className == "thClass"){
							continue;
						}
						//console.log(rootUrl); 
						$(trList[i]).dblclick(function(){
							playByIndex(this);
						})
					}
					
					// 绑定删除按钮事件
					$("#musicTable .delBtn").click(function(){
						var trObj = $(this).parent().parent();
						var trId = $(trObj).attr("id");
						layer.confirm("确定删除吗?", {btn: ['确定', '取消'], title: "提示"}, 
						  function (){delItem(trId)}
						 ,function (){layer.close()}
						);
					});
					
					$("#musicTable tr").hover(function(){
						if (!$(this).hasClass("thClass") && !$(this).hasClass("selectRowClass")){
							$(this).addClass("mouseonClass");
						}
					},function(){
						$(this).removeClass("mouseonClass");
					})
					
					parent.refrshMusic();
				},
				failed:function(result){
					console.log("showFiles.do call failed!");	
				}
			})
		}
		
		// 删除音乐
		function delItem(id){
			$.ajax({
				type:"POST",
				url:rootUrl + "file/delFile.do",
				data:{id:id},
				dataType:'json',
				success:function(result){
					refreash();
				}
			});
		}
		
		// 刷新音乐列表
		function refreash(){
			window.location.href = window.location.href;
		}
		
		// 供iframe子页面调用
		function refreashTimeout(sec){
			setTimeout(refreash,sec*1000);
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
