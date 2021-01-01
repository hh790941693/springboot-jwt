// 首页tab标题
var indexTabTitle = "首页";
$(function(){
    // 初始化左侧导航栏菜单
	initLeftMenu();
	// 绑定双击关闭事件、右键菜单事件
	tabClose();
	// 右键菜单事件
	tabCloseEven();

	// 左侧导航栏展开与关闭
	$(".accordion-header").click(function(){
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
        menujson.title = n.menuname;
        menujson.iconCls = n.icon;
        var contentHtml = "<ul>";
        $.each(n.menus, function(j, o) {
            contentHtml += '<li><div closable="'+o.closable+'"><a target="mainFrame" href="' + o.url + '" ><span class="icon '+o.icon+'" ></span>' + o.menuname + '</a></div></li> ';
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
		addTab(tabTitle, url, closableValue);
		$('.easyui-accordion li div').removeClass("selected");
		$(this).parent().addClass("selected");
	}).hover(function(){
		$(this).parent().addClass("hover");
	},function(){
		$(this).parent().removeClass("hover");
	});

	$(".easyui-accordion").accordion();
}

function addTab(subtitle,url, closable){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title: subtitle,
			content: createFrame(url),
			closable: closable == "true" ? true : false,
			width: $('#mainPanle').width()-10,
			height: $('#mainPanle').height()-26
		});
	}else{
		$('#tabs').tabs('select',subtitle);
	}
	tabClose();
}

function createFrame(url) {
	var s = '<iframe name="mainFrame" scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose() {
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children("span").text();
		if (subtitle != indexTabTitle) {
            $('#tabs').tabs('close', subtitle);
        }
	});

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
//绑定右键菜单事件
function tabCloseEven() {
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
