<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>第一个Gis地图</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery-1.4.4.min.js"></script>
<link rel="stylesheet" type="text/css" href="http://localhost/arcgis_js_api/library/3.14/3.14/esri/css/esri.css" />
<link rel="stylesheet" type="text/css" href="http://localhost/arcgis_js_api/library/3.14/3.14/dijit/themes/claro/claro.css" />
<link rel="stylesheet" type="text/css" href="http://localhost/arcgis_js_api/library/3.14/3.14/dijit/themes/soria/soria.css" />
<script type="text/javascript" src="http://localhost/arcgis_js_api/library/3.14/3.14/init.js"></script>

<script type="text/javascript" src="<%=path%>/static/js/require.js"></script>
<script type="text/javascript" src="<%=path%>/pgis/pgis_gy.js"></script>

<style type="text/css">
	html,body,#mapDiv{
		padding:0;
		margin:0;
		height:100%;
	}
</style>
</head>
<body>
		<div id="mapDiv" style="width:900px;height:600px;border:1px solid #000">
		</div>
		<button id="btn1" name="mybtn" class="clzzz">点击我</button>
	<script type="text/javascript">
		dojo.require("esri.map");
		dojo.require("esri.layers.ArcGISTiledMapServiceLayer");
		var map;
		function initMap(){
			initLayer();
			map = new esri.Map("mapDiv", {
				logo: false,
				center: [119.4534057,32.7834561],
				zoom: 16,
				minZoom: 10,
				maxZoom: 20
			});
			
			mapBasicLayer();
			scaleBar();
			map.infoWindow.resize(800, 600);
		}
		var btnid = dojo.byId("btn1");
		dojo.connect(btnid,"onclick","click");
		function click(){
			alert("you have clicked!");
		}
		dojo.addOnLoad(function(){
			initMap();
		});
	</script>
</body>
</html>
