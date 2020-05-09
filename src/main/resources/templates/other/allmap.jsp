<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%
String basePath = request.getContextPath();
String path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	<title>网格地图</title>
	<style type="text/css">
	</style>
	<c:import url="/base.jsp"/>
	<c:import url="/arcgis_api.jsp"/>
	
	<link rel="stylesheet" type="text/css" href="<%=path%>/css/layer.css"/>
	<script type="text/javascript" src="<%=path%>/extjs/userControl/cboDic.js"></script>
	<script type="text/javascript" src="<%=path%>/extjs/userControl/cboDept.js"></script>
	<script type="text/javascript" src="<%=path%>/extjs/userControl/cboOption.js"></script>
	<script type="text/javascript" src="<%=path%>/extjs/plugs/ux/TreePicker.js"></script>
	<script type="text/javascript" src="<%=path%>/extjs/userControl/treecboJkqylx.js"></script>
	<script type="text/javascript" src="<%=path%>/pgis/layer_pcs.js"></script>
	<script type="text/javascript" src="<%=path%>/pgis/layer_monitor.js"></script>
	
	<script src="<%=path%>/static/hik/BigInt.js" type="text/javascript"></script>
	<script src="<%=path%>/static/hik/RSA_Stripped.js" type="text/javascript"></script>
	<script src="<%=path%>/static/hik/Barrett.js" type="text/javascript"></script>
	<script type="text/javascript">
		//------------------------------------------------------------------------------------------------------------------------------
		var map;
		var _Layer_Monitor; //监控图层
		
		dojo.addOnLoad(function(){
			initMap(); //初始化地图
			mapBasicToolBar(); //地图基本工具
		});
		
		//------------------------------------------------------------------------------------------------------------------------------
		
		//初始化地图
		function initMap() {
			initLayer();
			map = new esri.Map("map", {
				logo: false,
				center: [119.4534057,32.7834561],
				zoom: 16,
				minZoom: 10,
				maxZoom: 20
			});
			
			mapBasicLayer();
			scaleBar();
			map.infoWindow.resize(800, 600);
			
			//辖区范围	
			layer_pcs_area();
			//map.getLayer("layer_pcs_area").setVisibility(true);
			_layer_pcs_area = true;
			
			
			_Layer_Monitor = new esri.layers.GraphicsLayer();
			_Layer_Monitor.id = "layer_monitor";
			map.addLayer(_Layer_Monitor);
		}
		
		
		
		//通过gps坐标参数,定位(格式:x,y)
		function markerSingle(gps) {
			_Layer_Monitor.clear(); //每次调用先清空图层
			
			var x = '', y = '';
			if (gps != null && typeof(gps) != 'undefined' && gps != '') {
				var arrayGps = gps.split(',');
				x = arrayGps[0];
            	y = arrayGps[1];
				if (x == null || x == undefined || x == '' || 
						y == null || y == undefined || y == '') {
					return false;
				}
			} else {
				return false;
			}
			
			var gpsX = parseFloat(x);
			var gpsY = parseFloat(y);
			var gpsXY = new Array(gpsX, gpsY);
			var geometry = new esri.geometry.Point({
				"x": x,
				"y": y,
				"spatialReference": map.spatialReference
			});
			
			var iconPic = rootUrl + "img/tack.gif";
			var iconSize = 25;
			dojo.require("esri.symbols.PictureMarkerSymbol");
			var symbolIcon = new esri.symbols.PictureMarkerSymbol(iconPic, iconSize, iconSize);
			var graphic = new esri.Graphic(geometry, symbolIcon);
			_Layer_device1.add(graphic);
			//map.centerAndZoom(gpsXY, 16);
			map.centerAt(gpsXY);
			
			var x = ForDight(gpsX, 6), y = ForDight(gpsY, 6);
			var gps = x + ',' + y;
			setTxtGPS(gps);
		}
		
		
		
		//------------------------------------------------------------------------------------------------------------------------------
		
		Ext.onReady(function () {
			//查询面板
			//------------------------------------------------------------------------------------------------------
			var cboDevicetype = uc.cboDic({
				name: 'devicetype',
				fieldLabel: '设备类型',
				labelAlign: 'right',
		    	labelWidth: 70,
				dicIndex: 'devicetype',
				value: '1',
				promptText: '-- 请选择 --'
			});
			
			var txtSbbm = Ext.create('Ext.form.field.Text', {
				name: 'sbbm',
				fieldLabel: '设备编码',
				labelAlign: 'right',
		    	labelWidth: 70,
				maxLength: 30
			});
			
			var txtSbmc = Ext.create('Ext.form.field.Text', {
				name: 'sbmc',
				fieldLabel: '设备名称',
				labelAlign: 'right',
		    	labelWidth: 70,
				maxLength: 20
			});
			
			var cboSbcs = uc.cboDic({
				name: 'sbcs',
				fieldLabel: '设备厂商',
				labelAlign: 'right',
		    	labelWidth: 70,
				dicIndex: 'sbcs',
				promptText: '-- 请选择 --'
			});
			
			var txtMlxz = Ext.create('Ext.form.field.Text', {
				name: 'mlxz',
				fieldLabel: '安装地址',
				labelAlign: 'right',
		    	labelWidth: 80,
				maxLength: 20
			});
			
			var txtGldw = Ext.create('Ext.form.field.Text', {
				name: 'gldw',
				fieldLabel: '管理单位',
				labelAlign: 'right',
		    	labelWidth: 80,
				maxLength: 20
			});
			
			var txtYwdw = Ext.create('Ext.form.field.Text', {
				name: 'ywdw',
				fieldLabel: '运维单位',
				labelAlign: 'right',
		    	labelWidth: 80,
				maxLength: 20
			});
			
			var cboLwsx = uc.cboDic({
				name: 'lwsx',
				fieldLabel: '联网属性',
				labelAlign: 'right',
		    	labelWidth: 80,
				dicIndex: 'lwsx',
				promptText: '-- 请选择 --'
			});
			
			var cboGajg = uc.cboDept({
				name: 'gajg',
				fieldLabel: '所属公安机关',
				labelAlign: 'right',
		    	labelWidth: 80,    	
				grade: '3',
				promptText: '-- 请选择 --'
			});
			
			var cboSxjlx = uc.cboDic({
				name: 'sxjlx',
				fieldLabel: '摄像机类型',
				labelAlign: 'right',
		    	labelWidth: 80,
				dicIndex: 'sxjlx',
				promptText: '-- 请选择 --'
			});
			
			var cboSxjgnlx = uc.cboDic({
				name: 'sxjgnlx',
				fieldLabel: '摄像机功能类型',
				labelAlign: 'right',
		    	labelWidth: 80,
				dicIndex: 'sxjgnlx',
				promptText: '-- 请选择 --'
			});
			
			var cboJkqylx = uc.treecboJkqylx({
				name: 'jkqylxzz', //域字段是jkqylxzz子类
				indexId: 'jkqylx',
				fieldLabel: '监控区域类型',
				labelAlign: 'right',
		    	labelWidth: 80
			});
			
			var cboZcfbl = uc.cboDic({
				name: 'zcfbl',
				fieldLabel: '支持分辨率',
				labelAlign: 'right',
		    	labelWidth: 80,
				dicIndex: 'zcfbl',
				promptText: '-- 请选择 --'
			});
			
			var cboSxjbmgs = uc.cboDic({
				name: 'sxjbmgs',
				fieldLabel: '摄像机编码格式',
				labelAlign: 'right',
		    	labelWidth: 80,
				dicIndex: 'sxjbmgs',
				promptText: '-- 请选择 --'
			});
			
			var cboSbsslx = uc.cboDic({
				name: 'sbsslx',
				fieldLabel: '设备所属类型',
				labelAlign: 'right',
		    	labelWidth: 80,
				dicIndex: 'sbsslx',
				promptText: '-- 请选择 --'
			});
			
			var cboSbzt = uc.cboDic({
				name: 'sbzt',
				fieldLabel: '设备状态',
				labelAlign: 'right',
		    	labelWidth: 80,
				dicIndex: 'sbzt',
				promptText: '-- 请选择 --'
			});
			
			var cboJszt = uc.cboDic({
				name: 'jszt',
				fieldLabel: '建设状态',
				labelAlign: 'right',
		    	labelWidth: 80,
				dicIndex: 'jszt',
				promptText: '-- 请选择 --'
			});
			
			var cboJkxhlx = uc.cboDic({
				name: 'jkxhlx',
				fieldLabel: '监控信号类型',
				labelAlign: 'right',
		    	labelWidth: 80,
				dicIndex: 'jkxhlx',
				promptText: '-- 请选择 --'
			});
			
			var cboIsFace = uc.cboOption({
				name: 'isFace',
				fieldLabel: '人脸识别功能',
				labelAlign: 'right',
		    	labelWidth: 80,
		    	autoWidth: true,
				dicIndex: 'bool',
				promptText: '-- 请选择 --'
			});
			
			var dpDateArea = Ext.create('Ext.form.field.Display', {
				fieldLabel: '时段查询',
				labelAlign: 'right',
		    	labelWidth: 70,
				value: '请选择查询的时间类型。'
			});
			
			var cboDateType = uc.cboOption({
				name: 'dateRange',
				fieldLabel: '时间类型',
				labelAlign: 'right',
		    	labelWidth: 70,
		    	autoWidth: true,
				dicIndex: 'dateRange',
				promptText: '-- 请选择 --'
			});
			
			var dateStart = Ext.create('Ext.form.field.Date', {
				name: 'startDate',
				fieldLabel: '超始时间',
				labelAlign: 'right',
		    	labelWidth: 70,
		    	format: 'Y-m-d',
		    	editable: false
			});
			
			var dateEnd = Ext.create('Ext.form.field.Date', {
				name: 'endDate',
				fieldLabel: '截止时间',
				labelAlign: 'right',
		    	labelWidth: 70,
		    	format: 'Y-m-d',
		    	editable: false
			});
			
			var btnQuery = Ext.create('Ext.button.Button',{
				text: '查 询',
				width: '70%',
				height: 26,
				margin: '10 0 0 10',
	            iconCls: 'x-button-query',
	            handler: function() {
	            	query();
	            }
			});
			
			var btnQueryReset = Ext.create('Ext.button.Button',{
				text: '重 置',
				width: '70%',
				height: 26,
				margin: '10 0 0 10',
	            iconCls: 'x-button-redo',
	            handler: function() {
	            	resetQuery();
	            }
			});
			
			var panelQuery = Ext.create('Ext.form.Panel', {
				renderTo: 'panelQuery',
			    title: '查询',
			    collapsible: true,
			    collapseDirection: 'top',
			    headerPosition: 'top',
			    floating: false,
			    width: 1300,
			    height: 180,
			    padding: 0,
			    border: true,
			    layout: 'column',
			    items: [{
			        columnWidth: .2,
			        layout: 'form',
			        border: 0,
			        padding: 5,
			        items: [cboDevicetype, txtSbmc, txtSbbm, cboSbcs]
			    }, {
			        columnWidth: .2,
			        layout: 'form',
			        border: 0,
			        padding: 5,
			        items: [cboSbsslx, cboGajg, txtMlxz, txtGldw, btnQuery]
			    }, {
			        columnWidth: .23,
			        layout: 'form',
			        border: 0,
			        padding: 5,
			    	items: [cboJkqylx, cboSbzt, cboJszt, cboLwsx, btnQueryReset]
			    }, {
			    	id: 'panelWl',
			        columnWidth: .17,
			        layout: 'form',
			        border: 0,
			        padding: 5,
			    	items: [cboSxjlx, cboZcfbl, cboIsFace, cboJkxhlx]
			    }, {
			        columnWidth: .2,
			        layout: 'form',
			        border: 0,
			        padding: 5,
			    	items: [dpDateArea, cboDateType, dateStart, dateEnd]
			    }]
			});
			
			var panelWl = Ext.getCmp('panelWl');
			cboDevicetype.on('select', function(combo, records, eOpts) {
				var type = combo.getValue();
				if (type != '1') {
					panelWl.setVisible(false);
					cboSxjlx.setValue('');
					cboZcfbl.setValue('');
					cboIsFace.setValue('');
					cboJkxhlx.setValue('');
				} else {
					panelWl.setVisible(true);
				}
			});
			
			
			function query() {
				displayDom('loadMask', true);
				var params = panelQuery.getValues();
				_Layer_Monitor.clear();
				layer_monitor(params);
	        }
			
	        function resetQuery() {
	        	panelWl.setVisible(true);
	        	panelQuery.getForm().reset();
	        	_Layer_Monitor.clear();
	        }
		});
	
	</script>
</head>
<body>
	<div id="loadMask" class="centerPos" style="display:none;z-index:100;margin:0 auto;position:absolute;top:360px;width:200px;background-color:#e9f9f9;">
		<div>正在查询加载中，请稍等......</div>
		<div><img src="<%=path%>/img/loading.gif"/></div>
	</div>
	
    <div style="width: 100%; height: 100%; margin: 0;">
        <div id="map" style="overflow: hidden; width: 100%; height: 100%;">
        </div>
    </div>
    <div id="panelQuery" style="position:fixed;top:8px;left:60px;"></div>
    
    <c:import url="/mapBasicToolBar.jsp">
    	<c:param name="type" value="all"/>
    </c:import>
    <iframe width="0" height="0" id="monitorUrl" style="display:none"></iframe>
</body>
</html>