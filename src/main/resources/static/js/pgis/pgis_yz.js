function initLayer() {
    dojo.declare("PGISLayer", esri.layers.TiledMapServiceLayer, {
        constructor: function () {
            this.spatialReference = new esri.SpatialReference({ wkid: 4326 });
			this.version = "1.0.0";
			this.identifier = "LWYZSL2017";
            this.initialExtent = this.fullExtent = new esri.geometry.Extent(118.41906, 32.0035, 120.53393, 33.1579, this.spatialReference);
			//this.initialExtent = (this.fullExtent = new esri.geometry.Extent(115.64123,33.77795,115.94421,33.94665, this.spatialReference));

            this.tileInfo = new esri.layers.TileInfo({
                "rows": 256,
                "cols": 256,
                //"compressionQuality": 0,
                "origin": { "x": -180, "y": 90 },
                "spatialReference": { "wkid": 4326 },
				"minZoom":0,
				"maxZoom":19, 
                "lods": [{ "level": 0, "resolution": 1.40625, "scale": 591658710.9 },//(0, 1.40625, 591658710.9),

                             { "level": 1, "resolution": 0.703125, "scale": 295829355.45 },//(1, 0.703125, 295829355.45),

                             { "level": 2, "resolution": 0.3515625, "scale": 147914677.73 },//(2, 0.3515625, 147914677.73),

                             { "level": 3, "resolution": 0.17578125, "scale": 73957338.86 },//(3, 0.17578125, 73957338.86),

                             { "level": 4, "resolution": 0.087890625, "scale": 36978669.43 },//(4, 0.087890625, 36978669.43),

                             { "level": 5, "resolution": 0.0439453125, "scale": 18489334.72},//(5, 0.0439453125, 18489334.72),

                             { "level": 6, "resolution": 0.02197265625, "scale": 9244667.36 },//(6, 0.02197265625, 9244667.36),

                             { "level": 7, "resolution": 0.010986328125, "scale": 4622333.68 },//(7, 0.010986328125, 4622333.68),

                             { "level": 8, "resolution": 0.0054931640625, "scale": 2311166.84 },//(8, 0.0054931640625, 2311166.84),

                             { "level": 9, "resolution": 0.00274658203125, "scale": 1155583.42 },//(9, 0.00274658203125, 1155583.42),

                             { "level": 10, "resolution": 0.001373291015625, "scale": 577791.71 },//(10, 0.001373291015625, 577791.71),

                             { "level": 11, "resolution": 0.0006866455078125, "scale": 288895.85 },//(11, 0.0006866455078125, 288895.85),

                             { "level": 12, "resolution": 0.00034332275390625, "scale": 144447.93 },//(12, 0.00034332275390625, 144447.93),

                             { "level": 13, "resolution": 0.000171661376953125, "scale": 72223.96 },//(13, 0.000171661376953125, 72223.96),

                             { "level": 14, "resolution": 0.0000858306884765625, "scale": 36111.98 },//(14, 0.0000858306884765625, 36111.98),

                             { "level": 15, "resolution": 0.00004291534423828125, "scale": 18055.99 },//(15, 0.00004291534423828125, 18055.99),

                             { "level": 16, "resolution": 0.000021457672119140625, "scale": 9028.00 },//(16, 0.000021457672119140625, 9028.00),

                             { "level": 17, "resolution": 0.0000107288360595703125, "scale": 4514.00 },
							 { "level": 18, "resolution": 0.00000536441802978515625, "scale": 2257.00 },
							 { "level": 19, "resolution": 0.000002682209014892578125, "scale": 1128.50 },
							 { "level": 20, "resolution": 0.000001341104507446289062, "scale": 564.25 }
						]
			
            });
			
            this.loaded = true;

            this.onLoad(this);

        },

        getTileUrl: function (level, row, col) {
            var levelMap = "";

			//levelMap = "http://10.36.159.38:9098/PGIS_S_LWTileMap_TDT/Maps/LWYZSL2014/EzMap" + "?Service=getImage&Type=RGB" + "&Col=" + col + "&Row=" + row + "&Zoom=" + level;
			
            var mapName = "LWYZSL2017";
            levelMap = "http://10.36.159.38:9098/PGIS_S_LWTileMap_TDT/Maps/" + mapName + "?SERVICE=WMTS&VERSION=1.0.0&REQUEST=GetTile"
                	 + "&LAYER=" + this.identifier + "&STYLE=default" + "&FORMAT=image/png" + "&TILEMATRIXSET=WholeWorld_CRS_84"
                	 + "&TILEMATRIX=" + level + "&TILEROW=" + row + "&TILECOL=" + col;
            return levelMap;
        }
    });
    
    
    
    dojo.declare("PGISLayer_YX", esri.layers.TiledMapServiceLayer, {
        constructor: function () {
            this.spatialReference = new esri.SpatialReference({ wkid: 4326 });
			this.version = "1.0.0";
			this.identifier = "LWYZYX2014";
            this.initialExtent = this.fullExtent = new esri.geometry.Extent(118.41906, 32.0035, 120.53393, 32.9579, this.spatialReference);
			//this.initialExtent = (this.fullExtent = new esri.geometry.Extent(115.64123,33.77795,115.94421,33.94665, this.spatialReference));

            this.tileInfo = new esri.layers.TileInfo({
                "rows": 256,
                "cols": 256,
                //"compressionQuality": 0,
                "origin": { "x": -180, "y": 90 },
                "spatialReference": { "wkid": 4326 },
				"minZoom":0,
				"maxZoom":19, 
                "lods": [{ "level": 0, "resolution": 1.40625, "scale": 591658710.9 },//(0, 1.40625, 591658710.9),
                             { "level": 1, "resolution": 0.703125, "scale": 295829355.45 },//(1, 0.703125, 295829355.45),

                             { "level": 2, "resolution": 0.3515625, "scale": 147914677.73 },//(2, 0.3515625, 147914677.73),

                             { "level": 3, "resolution": 0.17578125, "scale": 73957338.86 },//(3, 0.17578125, 73957338.86),

                             { "level": 4, "resolution": 0.087890625, "scale": 36978669.43 },//(4, 0.087890625, 36978669.43),

                             { "level": 5, "resolution": 0.0439453125, "scale": 18489334.72},//(5, 0.0439453125, 18489334.72),

                             { "level": 6, "resolution": 0.02197265625, "scale": 9244667.36 },//(6, 0.02197265625, 9244667.36),

                             { "level": 7, "resolution": 0.010986328125, "scale": 4622333.68 },//(7, 0.010986328125, 4622333.68),

                             { "level": 8, "resolution": 0.0054931640625, "scale": 2311166.84 },//(8, 0.0054931640625, 2311166.84),

                             { "level": 9, "resolution": 0.00274658203125, "scale": 1155583.42 },//(9, 0.00274658203125, 1155583.42),

                             { "level": 10, "resolution": 0.001373291015625, "scale": 577791.71 },//(10, 0.001373291015625, 577791.71),

                             { "level": 11, "resolution": 0.0006866455078125, "scale": 288895.85 },//(11, 0.0006866455078125, 288895.85),

                             { "level": 12, "resolution": 0.00034332275390625, "scale": 144447.93 },//(12, 0.00034332275390625, 144447.93),

                             { "level": 13, "resolution": 0.000171661376953125, "scale": 72223.96 },//(13, 0.000171661376953125, 72223.96),

                             { "level": 14, "resolution": 0.0000858306884765625, "scale": 36111.98 },//(14, 0.0000858306884765625, 36111.98),

                             { "level": 15, "resolution": 0.00004291534423828125, "scale": 18055.99 },//(15, 0.00004291534423828125, 18055.99),

                             { "level": 16, "resolution": 0.000021457672119140625, "scale": 9028.00 },//(16, 0.000021457672119140625, 9028.00),

                             { "level": 17, "resolution": 0.0000107288360595703125, "scale": 4514.00 },
							 { "level": 18, "resolution": 0.00000536441802978515625, "scale": 2257.00 },
							 { "level": 19, "resolution": 0.000002682209014892578125, "scale": 1128.50 },
							 { "level": 20, "resolution": 0.000001341104507446289062, "scale": 564.25 }
						]
			
            });
			
            this.loaded = true;

            this.onLoad(this);

        },

        getTileUrl: function (level, row, col) {

            var levelMap = "";
			
            var mapName = "LWYZYX2017";
            
            levelMap = "http://10.36.159.38:9098/PGIS_S_LWTileMap_TDT/Maps/" + mapName + "?SERVICE=WMTS&VERSION=1.0.0&REQUEST=GetTile"

                + "&LAYER=" + this.identifier + "&STYLE=default" + "&FORMAT=image/png" + "&TILEMATRIXSET=WholeWorld_CRS_84"

                + "&TILEMATRIX=" + level + "&TILEROW=" + row + "&TILECOL=" + col;
            return levelMap;
        }
    });
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
	//dojo.connect(btnClearLayer, "onclick", function(){ clearMap(); });
	var btnResetPos = dojo.byId('btnResetPos');
	if (btnResetPos != null && btnResetPos != undefined) {
		dojo.connect(btnResetPos, "onclick", function(){ resetMapPos(); });
	}
	
}

//清除图层(所谓的清除图层只是隐藏图层，全局搜索checkbox)
function clearMap() {
   var checkBoxList = dojo.query("input[type='checkbox']");
   for(var i = 0; i<checkBoxList.length; i++) {
	   hiddenLayer(checkBoxList[i].id)
   }
}

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
	var pgis = new PGISLayer();
	pgis.id = "pgis";
	pgis.visible = true;
	
	var pgis_yx = new PGISLayer_YX();
	pgis_yx.id = "pgis_yx";
	pgis_yx.visible = false;
	
	map.addLayer(pgis);
	map.addLayer(pgis_yx);
}

function resetMapPos() {
	map.centerAndZoom([119.4534057,32.7834561], 16);
}