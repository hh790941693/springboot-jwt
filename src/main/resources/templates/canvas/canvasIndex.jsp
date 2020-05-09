<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>2D画图</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>

<style type="text/css">
	ul li{
		list-style: none;
	    height: 25px;
	    width: 100px;
	    font-size: 20px;
	    font-weight: bold;
	    margin-left: -20px;
	    padding-top: 5px;
	    padding-bottom: 5px;
	    cursor: pointer;
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
	<div style="width:1060px;height:650px;margin:2px auto;">
		<div id="leftNavi" style="width:145px;height:100%;float:left;border:2px solid green;overflow-y: scroll">
			<ul>
				<li id="../canvas/starts.page" class='mouseleftclick'>太阳系</li>
				<li id="../canvas/collision.page" >碰撞球</li>
				<li id="../canvas/gravityBall.page" >重力球</li>
				<li id="../canvas/randomBall.page" >随机运动</li>
				<li id="../canvas/clock.page" >时钟</li>
				<li id="../canvas/clock2.page" >时钟2</li>
				<li id="../canvas/turnAroundBall.page" >星系云</li>
				<li id="../canvas/hidePhotoBall.page" >探照灯</li>
				<li id="../canvas/baidong.page" >游乐园</li>
				<li id="../canvas/flappyBird.page" >鸟</li>
				<li id="../canvas/trafficByCanvas_stepbystep.page" >交通1</li>
				<li id="../canvas/trafficByCanvas-smooth-0624.page" >交通2</li>
				<li id="../canvas/tank_p1p2.page" >坦克大战1</li>
				<li id="../canvas/tank_construct.page" >坦克大战2</li>
				<li id="../canvas/snake.page">贪吃蛇(多人对战)</li>
				<li id="../canvas/drawboard.page">画图(多人在线)</li>
				<li id="../canvas/pintu.page">拼图</li>
				<li id="../canvas/wuziqi.page">五子棋</li>
				<li id="../canvas/xiangqi_v6.0.page">象棋</li>
				<li id="../canvas/puke.page">扑克</li>
				<li id="../canvas/choujiang.page">转盘</li>
				<li id="../canvas/test.page">测试</li>
				<li id="../canvas/shootBall.page">射击球</li>
				<li id="../canvas/gamemove.page">人物移动</li>
			</ul>
		</div>
		
		<div id="diffPage" align="center" style="width:896px;height:100%;float:right;border:2px solid green">
			<iframe id="iframePage" width="890px" height="95%" ></iframe>
			<div>
				<button id="showNewWindowBtn">全屏显示</button>
				<button id="backBtn" style='display:none'>返&nbsp;&nbsp;&nbsp;&nbsp;回</button>
			</div>			
		</div>
	</div>
	
	<script type="text/javascript">
	
		var curPage = "../canvas/starts.page";
		$('#iframePage').attr('src',curPage);
		$("ul li").click(function (){
			
			curPage = this.id;
			$('#iframePage').attr('src',curPage);
			$("ul li").removeClass("mouseleftclick");
			$(this).addClass("mouseleftclick");
		});
		
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
