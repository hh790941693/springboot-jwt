// 首页tab标题
var indexTabTitle = "首页";
$(function(){
    // 初始化左侧导航栏菜单
	initLeftMenu();

	// 左侧导航栏展开与关闭
	$(".accordion-header").click(function(){
        $(".accordion-header div[class*='panel-icon']").removeClass().addClass("panel-icon").addClass("icon-menu-folder-close");
		if ($(this).hasClass("accordion-header-selected")) {
			// 开启
			$(this).children("div[class*='panel-icon']").removeClass().addClass("panel-icon icon-menu-folder-open");
		} else {
			// 关闭
			$(this).children("div[class*='panel-icon']").removeClass().addClass("panel-icon").addClass("icon-menu-folder-close");
		}
	})
})

//初始化左侧
function initLeftMenu() {
    $(".easyui-accordion").empty();

    var menujson = {};
    $.each(_menus.menus, function(i, n) {
        menujson = {};
        menujson.title = n.name;
        menujson.iconCls = "icon-menu-folder-close";
        var contentHtml = "<ul>";
        $.each(n.childrenList, function(j, o) {
        	var urlNew = o.url + "?userId=" + sessionUserId
				               + "&user=" + sessionUser
				               + "&roleId=" + sessionRoleId;
            contentHtml += '<li><div closable="'+o.extColumn1+'"><a target="mainFrame" href="' + urlNew + '" ><span class="'+o.icon+'" ></span>' + o.name + '</a></div></li> ';
        });
        contentHtml += '</ul>';
        menujson.content = contentHtml;
        $('.easyui-accordion').accordion('add', menujson);
    })

	$('.easyui-accordion li a').click(function(){
		var tabTitle = $(this).text();
		var url = $(this).attr("href");
		var parentDiv = $(this).parent("div:first");
        var closableValue = $(parentDiv).attr("closable");
        var firstSpanChild = $(this).children("span:first");
        var iconClass = $(firstSpanChild).attr("class");
		addTab(tabTitle, url, closableValue, iconClass);
		$('.easyui-accordion li div').removeClass("selected");
		$(this).parent().addClass("selected");
	}).hover(function(){
		$(this).parent().addClass("hover");
	},function(){
		$(this).parent().removeClass("hover");
	});

	$(".easyui-accordion").accordion();
}

function addTab(subtitle,url, closable, iconClass){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title: subtitle,
			content: createFrame(url),
            iconCls: iconClass,
			closable: closable == "true" ? true : false,
			width: $('#mainPanle').width()-10,
			height: $('#mainPanle').height()-26
		});
	}else{
		$('#tabs').tabs('select',subtitle);
	}
	if (closable == "true") {
        tabDoubleClickCloseEvent();
        tabSelectEvent();
        tabMenuEvent();
        tabMenuCloseEvent();
    }
}

function createFrame(url) {
	var s = '<iframe name="mainFrame" scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:99.5%;"></iframe>';
	return s;
}

// tab选中事件
function tabSelectEvent(){
    $(".tabs-inner").click(function(){
        var subtitle = $(this).children("span").text();
        $('#tabs').tabs('select', subtitle);

        var tab = $('#tabs').tabs('getSelected');  // 获取选择的面板
        $('#tabs').tabs('update', {
            tab: tab,
            options: {
                title: subtitle
            }
        });
    });
}

function tabMenuEvent(){
    $(".tabs-inner").bind('contextmenu',function(e){
        $('#mm').menu('show', {
            left: e.pageX,
            top: e.pageY,
        });

        var subtitle =$(this).children("span").text();
        var classes = $(this).children("span[class*='tabs-title']").attr("class");
        var closeable = false;
        if (classes.indexOf("tabs-closable") != -1){
            closeable = true;
        }
        $('#mm').data("currtab", subtitle);
        $('#mm').data("closeable", closeable);

        return false;
    });
}

function tabDoubleClickCloseEvent() {
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children("span").text();
        var classes = $(this).children("span[class*='tabs-title']").attr("class");
        if (classes.indexOf("tabs-closable") != -1){
            $('#tabs').tabs('close', subtitle);
        }
	});
}

//绑定右键菜单事件
function tabMenuCloseEvent() {
	//关闭当前
	$('#mm-tabclose').click(function(){
		var currtabTitle = $('#mm').data("currtab");
		var closeable = $('#mm').data("closeable");
        //var closeTab = $('#tabs').tabs('getTab', currtabTitle);
		if (closeable) {
            $('#tabs').tabs('close', currtabTitle);
        }
	});
	//全部关闭
	$('#mm-tabcloseall').click(function(){
		$('.tabs-inner span').each(function(i,n){
			var text = $(n).text();
			if (text != "") {
			    var classes = $(n).attr("class");
                if (classes.indexOf("tabs-closable") != -1) {
                    $('#tabs').tabs('close', text);
                }
            }
		});	
	});
	//关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function(){
        var currtabTitle = $('#mm').data("currtab");
		$('.tabs-inner span').each(function(i,n){
			var text = $(n).text();
			if (text != "") {
                var classes = $(n).attr("class");
                if (text != currtabTitle && classes.indexOf("tabs-closable") != -1) {
                    $('#tabs').tabs('close', text);
                }
            }
		});	
	});
	//关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function(){
		var nextall = $('.tabs-selected').nextAll();
		if(nextall.length == 0){
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length == 0){
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i,n){
			var text = $('a:eq(0) span',$(n)).text();
            if(text != indexTabTitle) {
                $('#tabs').tabs('close', text);
            }
		});
		return false;
	});

	//退出
	$("#mm-exit").click(function(){
		$('#mm').menu('hide');
	})
}

//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}
