<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>游戏首页</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>

<style type="text/css">
	ul li{
		list-style:none;
		height:40px;
		width:130px;
		font-size:20px;
		font-weight:bold;
		margin-left:-20px;
		padding-top:15px;
		cursor:pointer;
	}
	ul a{
		text-decoration:none;
	}
	
	.mousemoveon{
		background-color:grey;
		color:white;
	}
	.mousemoveout{
		background-color:white;
		color:black;
	}
	.mouseleftclick{
		background-color:blue;
		color:white;
	}
</style>
</head>
<body>
	<div style="width:1054px;height:640px;margin:2px auto;">
		<div id="leftNavi" style="width:160px;height:100%;float:left;border:2px solid green">
			<ul>
				<li id="../game/snake.page" onclick="$('#iframePage').attr('src',this.id)">贪吃蛇</li>
				<li id="../game/plane.page" onclick="$('#iframePage').attr('src',this.id)">飞机大战</li>
				<li id="../game/ball.page" onclick="$('#iframePage').attr('src',this.id)" class="mouseleftclick">桌球</li>
				<li id="../game/trafficSignal.page" onclick="$('#iframePage').attr('src',this.id)">交通信号灯</li>
				<li id="../game/russia.page" onclick="$('#iframePage').attr('src',this.id)">俄罗斯方块</li>
				<li id="../game/myrussia.page" onclick="$('#iframePage').attr('src',this.id)">俄罗斯方块2</li>
			</ul> 
		</div>
		
		<div id="diffPage" align="center" style="width:880px;height:100%;float:right;border:2px solid green;margin-left:5px;">
			<iframe id="iframePage" width="100%" height="100%" ></iframe>
			<div>
				<button id="showNewWindowBtn">全屏显示</button>
				<button id="backBtn" style='display:none'>返&nbsp;&nbsp;&nbsp;&nbsp;回</button>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		var curPage = "../game/ball.page";
		$('#iframePage').attr('src',curPage);
		$("ul li").click(function (){
			curPage = this.id;
			$('#iframePage').attr('src',curPage);
			$("ul li").removeClass("mouseleftclick");
			$(this).addClass("mouseleftclick");
		})
		
		$('#showNewWindowBtn').click(function(){
			window.open(curPage);
		})
		
		$('#backBtn').click(function(){
			window.location.href="../ws/wsserverIndex.page?user=admin";
		})
		
		$("ul li").hover(function(){
			$(this).removeClass("mousemoveout");
			$(this).removeClass("mousemoveon");
			$(this).addClass("mousemoveon");
		},
		function(){
			//$(this).css({"background-color":"white","color":"black"});
			$(this).removeClass("mousemoveout");
			$(this).removeClass("mousemoveon");
			$(this).addClass("mousemoveout");
		})
	</script>
</body>
</html>
