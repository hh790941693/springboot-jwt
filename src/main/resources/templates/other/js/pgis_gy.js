// 高邮PGIS地图
function initLayer() {
	
}




//比例尺
function scaleBar() {
	var scalebar = new esri.dijit.Scalebar({
		map: map,
		scalebarUnit: "metric",
		attachTo: "bottom-right"
	}, dojo.byId("scaleBar"));
}


//地图的基本工具栏操作
function mapBasicToolBar() {
	var btnSL = dojo.byId("btnSL");
	var btnYX = dojo.byId("btnYX");
	dojo.connect(btnSL, "onclick", function(){ mapSwitch(0); });
	dojo.connect(btnYX, "onclick", function(){ mapSwitch(1); });
	
	var btnClearLayer = dojo.byId('btnClearLayer');
	if (btnClearLayer != null && btnClearLayer != undefined) {
		dojo.connect(btnClearLayer, "onclick", function(){ clearMap(); });
	}
	
	var btnResetPos = dojo.byId('btnResetPos');
	if (btnResetPos != null && btnResetPos != undefined) {
		dojo.connect(btnResetPos, "onclick", function(){ resetMapPos(); });
	}
}


function clearMap() {
   map.removeAllLayers();
   mapBasicLayer();
}

//清除图层(所谓的清除图层只是隐藏图层，全局搜索checkbox)
/*function clearMap() {
   var checkBoxList = dojo.query("input[type='checkbox']");
   for(var i = 0; i<checkBoxList.length; i++) {
	   hiddenLayer(checkBoxList[i].id)
   }
}*/

//隐藏图层
function hiddenLayer(id) {
	document.getElementById(id).checked = false;
	var chk = document.getElementById(id); 
	var layerId = id.substr(4); 
	//避免寻找不到图层(如社区网格中的人员初始化是不加载的)
	if (null != map.getLayer(layerId) && map.getLayer(layerId)!='' && map.getLayer(layerId) !='undefined')
		map.getLayer(layerId).setVisibility(false);
}

//地图切换
function mapSwitch(flag) {
	if (flag == 0) {
		map.getLayer("pgis").setVisibility(true);
		map.getLayer("pgis_yx").setVisibility(false);
	}
	
	if (flag == 1) {
		map.getLayer("pgis").setVisibility(false);
		map.getLayer("pgis_yx").setVisibility(true);
	}
}


//加载地图的基础图层(矢量图/影像图)
function mapBasicLayer() {
	var options = {
		displayLevels: [10,11,12,13,14,15,16,17,18,19,20]
	};

	var pgis = new esri.layers.ArcGISTiledMapServiceLayer("http://10.36.153.248:6080/arcgis/rest/services/gysl/MapServer", options);
	pgis.id = "pgis";
	pgis.visible = true;
	
	var pgis_yx = new esri.layers.ArcGISTiledMapServiceLayer("http://10.36.153.248:6080/arcgis/rest/services/gyyx/MapServer", options);
	pgis_yx.id = "pgis_yx";
	pgis_yx.visible = false;
	
	map.addLayer(pgis);
	map.addLayer(pgis_yx);
}

function resetMapPos() {
	map.centerAndZoom([119.4534057,32.7834561], 16);
}