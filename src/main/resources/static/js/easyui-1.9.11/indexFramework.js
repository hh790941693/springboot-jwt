$(function(){
    // 初始化左侧导航栏菜单
	initLeftMenu();

	// 左侧导航栏展开与关闭
	$(".accordion-header").click(function(){
		if ($(this).hasClass("accordion-header-selected")) {
			// 开启
			$(this).children("div[class*='panel-icon']").removeClass().addClass("panel-icon icon-menu-folder-open");
		} else {
			// 关闭
			$(this).children("div[class*='panel-icon']").removeClass().addClass("panel-icon icon-menu-folder-close");
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
            contentHtml += '<li><div closable="'+o.extColumn1+'"><a target="mainFrame" href="' + o.url + '" ><span class="'+o.icon+'" ></span>' + o.name + '</a></div></li> ';
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
		closableValue = closableValue == "true"?true:false;
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

	$(".easyui-accordion").accordion({
		border:true, // 定义是否显示边框。
		selected:0,  // 初始化选中的面板（panel）索引。该属性自版本 1.3.5 起可用。
		fit:true     //  设置为 true，就使折叠面板（Accordion）容器的尺寸适应它的父容器。
	});
}

function addTab(subtitle,url, closable, iconClass){
	if ($('#tabs').tabs('exists',subtitle)) {
		$('#tabs').tabs('select',subtitle);
	} else{
		$('#tabs').tabs('add',{
			title: subtitle,
			content: createFrame(url),
			iconCls: iconClass,
			closable: closable,
			width: $('#mainPanle').width()-10,
			height: $('#mainPanle').height()-26
		});
	}
	if (closable) {
        tabSelectEvent();
        tabMenuEvent();
        tabMenuCloseEvent();
    }
}

function createFrame(url) {
	var s = '<iframe name="mainFrame" scrolling="auto" frameborder="0"  src="'+url+'" style="border:0;width:100%;height:99.5%;"></iframe>';
	return s;
}

// tab选中事件
function tabSelectEvent(){
    $(".tabs-inner").click(function(){
        var subtitle = $(this).children("span").text();
        $('#tabs').tabs('select', subtitle);

        // var tab = $('#tabs').tabs('getSelected');  // 获取选择的面板
        // $('#tabs').tabs('update', {
        //     tab: tab,
        //     options: {
        //         title: subtitle
        //     }
        // });
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
			//alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i,n){
			var text=$('a:eq(0) span',$(n)).text();
			var clazz=$('a:eq(0) span',$(n)).attr('class');
			if (clazz.indexOf("tabs-closable") != -1) {
				$('#tabs').tabs('close', text);
			}
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length == 0){
			//alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i,n){
			var text = $('a:eq(0) span',$(n)).text();
			var clazz=$('a:eq(0) span',$(n)).attr('class');
			if (clazz.indexOf("tabs-closable") != -1) {
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
