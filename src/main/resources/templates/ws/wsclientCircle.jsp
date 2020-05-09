<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
<head>
	<title>朋友圈</title>
<script type="text/javascript" src="<%=path%>/static/js/rootPath.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path%>/viewjs/common.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/bootstrap/css/bootstrap.css"/>
<script type="text/javascript" src="<%=path%>/static/bootstrap/js/bootstrap.js"></script>

<style type="text/css">
	*{
		margin:0px;
		padding:0px;
	}
	
	span{
		display:inline-block;
		height:100%;
	}
	
	#mainDiv{
	    width: 99%;
	    height: 585px;
	    margin: 2px auto;
	    overflow: scroll;
	    padding:5px;
	    background-color:#aaeca812;
	}
	#naviDiv{
	    width: 130px;
	    height: 30px;
	    position: fixed;
	    bottom: 35px;
	    right: 25px;
	}
	
	.nameDivClass{
		background-color:#b3d7ff;
		height:37px;
	}
	
	.userSpanClass{
		width:49%;
		font-size:20px;
		font-weight:bold;
		color:blue;
		cursor:pointer;
	}
	
	.myselfUserSpanClass{
		width:48%;
		font-size:20px;
		font-weight:bold;
		color:green;
		cursor:pointer;
	}

	.commentNameSpanClass{
		color:blue;
		width:65px;
		margin-left:10px;
		cursor:pointer;
	}
	
	.commentDivClass{
		margin-top:2px;
	}
	.commentDivClass p{
		background-color:#d6f3d9;
		margin-bottom:1px;
	}
	
	.contentDivClass{
		letter-spacing:3px;
		font-size:20px;
		margin-bottom:2px;
	}
	
	.timeSpanClass{
	    width: 120px;
    	font-size:13px;
    	color:grey;
    	margin-left:18px;
	}
	
	.likeNumSpanClass{
		color:green;
	    width: 20%;
    	font-size:15px;
	}
	
	.commentSpanClass{
		font-size:15px;
    	width: 73%;
	}
	
	.commentTimeSpanClass{
		color:grey;
		font-size:13px;
		margin-left:30px;
	}
	
	#showBigImageDiv{
	    width: 400px;
	    height: 350px;
	    display: none;
	    position: fixed;
	    top: 130px;
	    left: 350px;
	    z-index:99;
	    border:1px solid black;
	}
	
	.buttonGroupSpan{
		width:170px;
	}
	
	.btnDisabledClass{
		background-color:grey;
		color:black;
	}	
</style>
</head>
<body>
	<div id="mainDiv">
		<div id='addCircleDiv' align='right' style='margin-bottom: 5px;'>
			<span>当前第</span><span id='curPageSpan'></span><span>页</span>&nbsp;&nbsp;&nbsp;&nbsp;<span>共</span><span id='totalPageSpan'></span><span>页</span>
			<button onclick='toPrePage()' class='btn btn-sm btn-primary' >&lt;&lt;</button>&nbsp;&nbsp;
			<button onclick='toNextPage()' class='btn btn-sm btn-primary' >&gt;&gt;</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<button onclick='addCircle()' class='btn btn-sm btn-primary'>&nbsp;&nbsp;发&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;布&nbsp;&nbsp;</button>
		</div>
		<hr/>
		<div id="circleDiv">			
		</div>
		<div id='showBigImageDiv'>
			<img id='bigImg' width='100%' height='100%'  onclick='hideBigImg();' title='点击关闭图片' style='cursor: pointer;' border='0'>
		</div>
		
		<div id="naviDiv" align="center">
			<button id='preBtn' onclick='toPrePage()' class='btn btn-sm btn-primary' >前一页</button>
			<button id='nextBtn' onclick='toNextPage()' class='btn btn-sm btn-primary' >后一页</button>
		</div>		
	</div>
	
	<script type="text/javascript">
		var curUser=parent.s_user;
		var curPage = 1;
		var numPerPage = 10;
		var totalPage = 0;
		$("#preBtn").hide();
		$("#nextBtn").hide();
		queryCirclList(curPage,numPerPage);
		
		//上一页
		function toPrePage(){
			if (curPage>1){
				curPage = curPage-1;
				queryCirclList(curPage,numPerPage);
			}
		}
		//下一页
		function toNextPage(){
			if (curPage<totalPage){
				curPage = curPage+1;
				queryCirclList(curPage,numPerPage);
			}
		}
		$("#naviDiv").hover(function(){
			$("#preBtn,#nextBtn").show();
		},function(){
			$("#preBtn,#nextBtn").hide();
		})
		
		//查询朋友圈列表
		function queryCirclList(curPagePara,numPerPagePara){
			var data = {};
			data.curPage=curPagePara;  //当前页
			data.numPerPage=numPerPagePara;  //每页显示条数
			$("#curPageSpan").text(curPagePara);
			
			if (curPage <= 1){
				$("#preBtn").removeClass("btn-primary").addClass("btnDisabledClass").attr("disabled",true);
			}else{
				$("#preBtn").addClass("btn-primary").removeClass("btnDisabledClass").attr("disabled",false);
			}			
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/queryCircleByPage.do',
		        data:JSON.stringify(data),
		        contentType:"application/json", 
		        dataType: 'json',
		        async:false,
		        success: function(result) {
					var list = result.list;
					totalPage = result.totalPage;
					$("#totalPageSpan").text(totalPage);
					if (curPage >= totalPage){
						$("#nextBtn").removeClass("btn-primary").addClass("btnDisabledClass").attr("disabled",true);
					}else{
						$("#nextBtn").addClass("btn-primary").removeClass("btnDisabledClass").attr("disabled",false);
					}
					if (list.length>0){
						$("#circleDiv").empty();
						var htmlData = "";
						for (var i=0;i<list.length;i++){
							var circle = list[i];
							var circleId = circle.id;
							var userName = circle.userName;
							var content = circle.content;
							var likeNum = circle.likeNum;
							var createTime = circle.createTimeStr;
							var commentList = circle.commentList;
							var headImg = circle.headImg;
							if (headImg == ""){
								headImg = "imgerror_default.jpg";
							}
							var pic1 = circle.pic1;
							var pic2 = circle.pic2;
							var pic3 = circle.pic3;
							var pic4 = circle.pic4;
							
							var picArr = new Array();
							if (pic1 != ""){
								picArr.push(pic1)
							}
							if (pic2 != ""){
								picArr.push(pic2)
							}
							if (pic3 != ""){
								picArr.push(pic3)
							}
							if (pic4 != ""){
								picArr.push(pic4)
							}
							
							htmlData += "<div id='"+circleId+"'>";
							htmlData += "      <div class='bodyDivClass'>";
							htmlData += "            <div class='nameDivClass'>";
							var selfImgUrl = rootUrl+"img/headimg/"+headImg;
							htmlData += "                 <div style='display:inline-block;height:100%;width:540px;'><img alt='' src='"+selfImgUrl+"' width='30' height='30' onerror='imgerror(this)'>";
							var compareTwotimeResult = compareTwoTime(new Date(createTime).getTime(),new Date().getTime());
							if (curUser == userName || curUser == 'admin'){
								htmlData += "             <span class='myselfUserSpanClass' onclick='queryPersonInfo(this)'>"+userName+"</span></div><span class='likeNumSpanClass'>已有"+likeNum+"人点赞</span><span class='buttonGroupSpan'><button onclick='toComment("+circleId+")' class='btn btn-sm btn-primary'>评论</button>&nbsp;&nbsp;&nbsp;<button onclick='toLike("+circleId+")' class='btn btn-sm btn-info'>点赞</button>&nbsp;&nbsp;&nbsp;<button onclick='deleteCircle("+circleId+")' class='btn btn-sm btn-danger'>删除</button></span><span class='timeSpanClass'>发表于"+compareTwotimeResult+"</span>";
							}else{
								htmlData += "             <span class='userSpanClass' onclick='queryPersonInfo(this)'>"+userName+"</span></div><span class='likeNumSpanClass'>已有"+likeNum+"人点赞</span><span class='buttonGroupSpan'><button onclick='toComment("+circleId+")' class='btn btn-sm btn-primary'>评论</button>&nbsp;&nbsp;&nbsp;<button onclick='toLike("+circleId+")' class='btn btn-sm btn-info'>点赞</button></span><span class='timeSpanClass'>发表于"+compareTwotimeResult+"</span>";
							}
							htmlData += "            </div>";
							htmlData += "            <div class='contentDivClass'>";
							htmlData += "               <span>"+content+"</span>";
							htmlData += "            </div>";
							htmlData += "      </div>";
							if (picArr.length>0){
								htmlData += "  <div class='picDivClass' align='center'>";
								for (var k=0;k<picArr.length;k++){
									var picName = picArr[k];
									var picWebUrl = rootUrl+"img/circleimg/"+picName;
									var imgId = "circleImg"+i;
									htmlData += "    <img id='"+imgId+"' src='"+picWebUrl+"' alt='' width='100px' height='100px' onerror='imgerror(this)' style='margin-left:20px;'>";
								}
								htmlData += "  </div>";
							}

							htmlData += "      <div class='commentDivClass'>";							
							if (commentList.length>0){
								for (var j=0;j<commentList.length;j++){
									var commentObj = commentList[j];
									var commentId = commentObj.id;
									var commentUserName = commentObj.userName;
									var commentText = commentObj.comment;
									var commentCreateTime = commentObj.createTimeStr;
									var commentTimeResult = compareTwoTime(new Date(commentCreateTime).getTime(),new Date().getTime());
									if (curUser == commentUserName || curUser == userName || curUser == 'admin'){
										htmlData += "<p><span class='commentNameSpanClass' onclick='queryPersonInfo(this)'>"+commentUserName+":</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='commentSpanClass'>"+commentText+"</span><a href='javascript:void(0)' onclick='deleteComment("+commentId+")'>删除</a><span class='commentTimeSpanClass'>发表于"+commentTimeResult+"</span></p>";
									}else{
										htmlData += "<p><span class='commentNameSpanClass' onclick='queryPersonInfo(this)'>"+commentUserName+":</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='commentSpanClass'>"+commentText+"</span><span class='commentTimeSpanClass' style='margin-left:55px;'>发表于"+commentTimeResult+"</span></p>";		
									}
								}
							}
							htmlData += "      </div>";
							htmlData += "</div>";
							htmlData += "<hr/>";
						}
						$("#circleDiv").append(htmlData);
					}
		        }
			});	
		}
		//评论
		function toComment(circleId){
			var comment="";
			layer.prompt({
				formType: 2,
			  	value: '',
			  	title: '请输入评论内容'
			}, function(value,index){
				comment = value;
			  	layer.close(index);
			  	
				if (comment != ""){
					$.ajax({
				      	type: 'POST',
				        url: rootUrl+'ws/toComment.do',
				        dataType: 'json',
				        data:{user:curUser,circleId:circleId,comment:comment},
				        async:false,
				        success: function(result) {
				        	refresh();
				        }
					});
				}else{
					alert("评论内容不能为空!");
				}
			});
		}
		//点赞
		function toLike(circleId){
			$.ajax({
		      	type: 'POST',
		        url: rootUrl+'ws/toLike.do',
		        dataType: 'json',
		        data:{user:curUser,circleId:circleId},
		        async:false,
		        success: function(result) {
		        	refresh();
		        }
			});
		}
		//删除朋友圈
		function deleteCircle(circleId){
			layer.confirm('确认要删除？', {btn: ['确定','取消']}, 
					function(){
						$.ajax({
					      	type: 'POST',
					        url: rootUrl+'ws/toDeleteCircle.do',
					        dataType: 'json',
					        data:{id:circleId},
					        async:false,
					        success: function(result) {
					        	refresh();
					        }
						});
					}, 
					function(){layer.close();}
				);
		}
		
		//删除评论
		function deleteComment(commentId){
			console.log("删除评论:"+commentId);
			layer.confirm('确认要删除？', {btn: ['确定','取消']}, 
					function(){
						$.ajax({
					      	type: 'POST',
					        url: rootUrl+'ws/toDeleteComment.do',
					        dataType: 'json',
					        data:{id:commentId},
					        async:false,
					        success: function(result) {
					        	refresh();
					        }
						});
					}, 
					function(){layer.close();}
				);
		}
		
		//新增朋友圈内容
		function addCircle(){
			layer.open({
				  type: 2,
				  title: '添加朋友圈',
				  shadeClose: true,
				  shade: 0.8,
				  area: ['484px', '530px'],
				  content: 'wsclientAddCircle.page?user='+curUser
				}); 
		}
		
		//刷新
		function refresh()
		{
			window.location.href = window.location.href;
		};
		
		//对img绑定放大事件
		$(".picDivClass img").click(function(){
			// 记录点击坐标
			var circleObj = $(this).parent().parent();
			var canvasObj = $(circleObj)[0];
			var offleft = canvasObj.offsetLeft;
			var offtop = canvasObj.offsetTop;
			var clientY = event.clientY;
			var mouseY = clientY-offtop;
			showBigImg(this);
		})
		
		//显示大图
      	function showBigImg(imgObj) {
    	  	$("#bigImg").attr("src",$(imgObj).attr("src"));
    	  	$("#showBigImageDiv").css("display","block");
      	}
		//关闭大图
      	function hideBigImg() {
    	  	$("#showBigImageDiv").css("display","none");
      	}
      	//鼠标移走关闭大图
      	$("#showBigImageDiv").mouseout(hideBigImg);
      	
		function refresh(){
			window.location.href = window.location.href;
		};
		
		function imgerror(imgObj){
			var scrVar = $(imgObj).attr("src");
			$(imgObj).attr("src",rootUrl+"/img/imgerror_default.jpg");
		}
		
		//查看个人信息
		function queryPersonInfo(thisObj){
			var username = $(thisObj).text();
			if (username.indexOf(":")!= -1){
				username = username.replace(":","");
			}
			layer.open({
				  type: 2,
				  title: '用户个人信息',
				  shadeClose: true,
				  shade: 0.8,
				  area: ['384px', '370px'],
				  content: 'showPersonalInfo.page?user='+username
			}); 
		}
	</script>
</body>
</html>
