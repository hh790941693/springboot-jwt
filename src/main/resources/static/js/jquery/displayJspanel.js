var _jsPanelX;
var _jsPanelX_footerToolbar = [{
    item:     "<button type='button' style='cursor:pointer;'></button>",
    event:    jsPanel.evtStart,
    btnclass: "btn-sm",
    btntext:  "<span style='color:black;'>&nbsp;&nbsp;关 闭&nbsp;&nbsp;</span>",
    callback: function( event ){
        event.stopPropagation();
        event.data.close();
    }
}];



function openJSPanel(url, width, height, id, name, color) {
	 if (_jsPanelX != null && _jsPanelX != undefined) {
		_jsPanelX.close();
	 }
	 
	 var panelWidth = 1600, panelHeight = 800;
	 
	 if (width != undefined && width != null && width != '') {
		 panelWidth = width;
	 }
	 
	 if (height != undefined && height != null && height != '') {
		 panelHeight = height;
	 }
	 
	 var themeColor = 'rgb(10,70,90)';
	 if (color == 'blue')
		 themeColor = 'rgb(2,117,203)';
		
	 var strParam = "";
	 if (id != undefined && id != null && id != '') {
		 strParam = "?id=" + id;
	 }
	 
	 _jsPanelX = $.jsPanel({
		 theme: themeColor,
		 position: "center",
		 headerControls: { controls: 'closeonly' },
	     contentSize: { width: panelWidth, height: panelHeight },
	     headerTitle: name,
	     footerToolbar: _jsPanelX_footerToolbar,
	     contentIframe: { 
	    	 src: rootUrl + url + strParam,
	    	 style:  {'border': '5px solid rgb(4,100,140)'}
	     },
	     callback: function () {
	        this.content.css("padding", "0px");
	     }
	});
}

