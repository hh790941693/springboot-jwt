

//----------------------------------------对象操作--------------------------------------------------
// 根据键名返回对象中的value
Object.prototype.getObjValueByKey = function(key){
	for (var o in this){
		if (key == o){
			return this[o];
		}
	}
}

// 以数组形式返回对象中所有的key
Object.prototype.getObjKeysAsArr = function(){

	var arr = new Array();
	for (var x in this){
		if (this.hasOwnProperty(x)){
			arr.push(x);
		}
	}

	return arr;
}


// 根据索引值返回对象中的value index从0开始
Object.prototype.getObjValueByIndex = function(index){

	if (typeof(index) != "number"){
		return undefined;
	}

	var i=0;
	for (var o in this){
		if (i == index){
			return this[o];
		}
		i++;
	}
}

// 获取对象的长度
Object.prototype.getObjLength = function(){
	var len = 0;
	for (var x in this){
		if (this.hasOwnProperty(x)){
			len++;
		}
	}

	return len;
}
//-------------------------------------------------------------------------------------------------


//--------------------------------------------数组操作---------------------------------------------
//移除数组元素
Array.prototype.removeArrElement = function(obj){
	for (var i=0;i<this.length;i++){
		if(obj === this[i]){
			this.splice(i,1);
			return;
		}
	}
}

Array.prototype.getKeyIndex = function(obj){
    for (var i=0;i<this.length;i++){
        if (obj === this[i]){
            return i;
        }
    }
}

// 判断数组中是否包含某元素
Array.prototype.isInArray = function(ele){
    for (var i=0;i<this.length;i++){
        if (this[i] == ele){
            return true;
        }
    }
    
    return false;
}

// 获取数组元素最小值  仅限于数字型的数组
Array.prototype.getMinValue = function(){
	var temp = Number.MAX_VALUE;
	for (var i=0;i<this.length;i++){
		if (this[i] <= temp){
			temp = this[i];
		}
	}

	return temp;
}

// 获取数组元素最大值 仅限于数字型的数组
Array.prototype.getMaxValue = function(){
	var temp = Number.MIN_VALUE;
	for (var i=0;i<this.length;i++){
		if (this[i] >= temp){
			temp = this[i];
		}
	}

	return temp;
}
//-------------------------------------------------------------------------------------------------

//------------------------------------------JSON字符串和json对象互转-----------------------------
// 字符串转成对象
function jsonStr2jsonObj(jsonStr){
    return JSON.parse(jsonStr);
}

// 对象转成字符串
function jsonObj2jsonStr(jsonObj){
    return JSON.stringify(jsonObj);
}

//-------------------------------------------随机数操作--------------------------------------------
// 随机生成整数,包含左右边界
function randomNumber(start,end){
	var range = end-start;
	var rand = Math.random();
	var num = start + Math.round(rand*range);

	return num;
}

// 随机生成浮点数
function randomFloat(start,end,scale){
	var intNum = randomNumber(start, end);
	var floatNum = 0.0;
	if (intNum < end){
		floatNum = parseFloat(Math.random().toFixed(scale));
	}

	return intNum + floatNum;
}

// 从指定的参数列表中随机返回一个参数
function randomSpecifyNumber(){
	var i = randomNumber(0,arguments.length-1);
	return arguments[i];
}
//-------------------------------------------------------------------------------------------------

//-----------------------------------------颜色操作------------------------------------------------

// 随机生成颜色
function randomRgb(){
	var red = randomNumber(0,255);
	var green = randomNumber(0,255);
	var blue = randomNumber(0,255);
	
	return "rgb(" + red + "," + green + "," + blue + ")";
}

// 随机生成颜色
function randomRgba(){
	var red = randomNumber(0,255);
	var green = randomNumber(0,255);
	var blue = randomNumber(0,255);
	var alpha = randomFloat(0,1,1);
	
	return "rgba(" + red + "," + green + "," + blue + "," + alpha + ")";
}
//--------------------------------------------------------------------------------------------------

//-----------------------------------------点操作---------------------------------------------------
// 获取两点间的距离
function getTwoPointDistance(x1,y1,x2,y2){
	var xDistance = x1 - x2;
	var yDistance = y1 - y2;
	var towPointDistance = Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance,2));

	return towPointDistance
}

// 判断一个点是否在矩形中
function isPointInRectangle(x,y,rect){
	if (x >= rect.x && x <= rect.x+rect.w && y >= rect.y && y <= rect.y+rect.h){
		return true;
	}
	
	return false;
}

// 判断一个点是否在圆中
function isPointInCircle(x,y,circle){
	var distance = getTwoPointDistance(x,y,circle.x,circle.y);
	if (distance <= circle.r){
		return true;
	}
	
	return false;

}
//--------------------------------------------------------------------------------------------------

//-----------------------------------------圆操作---------------------------------------------------
// 判断2个圆是否碰撞
function isTwoCircleHit(circle1,circle2){
	var distance = getTwoPointDistance(circle1.x,circle1.y,circle2.x,circle2.y);
	if (distance < circle1.r + circle2.r){
		return true;
	}
	
	return false;	
}
//--------------------------------------------------------------------------------------------------

//----------------------------------------矩形操作--------------------------------------------------
// 判断2个矩形是否碰撞
function isTwoRectangleHit(rect1,rect2){
	if (rect1.x+rect1.w>rect2.x && rect1.x<rect2.x+rect2.w
		&& rect1.y+rect1.h>rect2.y && rect1.y<rect2.y+rect2.h){
			return true;
		}
	return false;	
}

// 检查2个矩形是否发生重叠(即碰撞)
function isTwoRectangleHitExt(x1,y1,w1,h1,rect2){
	var result = false;
	if (x1+w1>rect2.x && x1<rect2.x+rect2.w
		&& y1+h1>rect2.y && y1<rect2.y+rect2.h){
			result = true;
		}
	return result;
}
//--------------------------------------------------------------------------------------------------


//------------------------------------------------------canvas画布用----------------------------------------------------
// 判断一个数组中是否包含另外一个子数组
// mainArr:[[1,2,3],[4,5,6]]   paraArr:[[1,2,3]]
function isHasArr(mainArr,paraArr){
    var isInArr = false;
    for (var i=0;i<mainArr.length;i++){
        var cnt = 0;
        for (j=0;j<mainArr[i].length;j++){
            if (mainArr[i][j] == paraArr[0][j]){
                cnt++;
            }else{
                cnt--;
                break;
            }
        }
        if (cnt == paraArr[0].length){
            isInArr = true;
            break;
        }
    }
    
    return isInArr;
}

// 获取一个数组中包含的子数组的索引值  undefined表示没有找到
// mainArr:[[1,2,3],[4,5,6]]   paraArr:[[1,2,3]]
function getExistArrIndx(mainArr,paraArr){
    var index = undefined;
    for (var i=0;i<mainArr.length;i++){
        var cnt = 0;
        for (j=0;j<mainArr[i].length;j++){
            if (mainArr[i][j] == paraArr[0][j]){
                cnt++;
            }else{
                cnt--;
                break;
            }
        }
        if (cnt == paraArr[0].length){
            index = i;
            break;
        }
    }

    return index;
}

// 获取画布中任意一点所在的行和列(行和列都从1开始)
//  canvasWidth,canvasHeight画布大小
//  blockWidth,blockHeight画布每个网格大小,宽高必须能被画布整除
//  x,y 画布上任意一点的位置
function getRowAndCol(canvasWidth,canvasHeight,blockWidth,blockHeight,x,y){
    var maxRow = canvasHeight/blockHeight;
    var maxCol = canvasWidth/blockWidth;
    
    var row = parseInt(y/blockHeight)+1;
    var col = parseInt(x/blockWidth)+1;
    
    return {row:row,col:col};
}


// 获取指定起始行、起始列范围内点的集合
// startRow, endRow, startCol, endCol 起始行、结束行、起始列、结束列
// blockWidth,blockHeight 画布中最小单位网格的大小
// w0,h0 被裁减的宽高
function getBoundary(startRow, endRow, startCol, endCol,blockWidth,blockHeight,w0,h0){
    var points = new Array();
    var startX = (startCol-1)*blockWidth;
    var startY = (startRow-1)*blockHeight;
    var width = (endCol-startCol+1)*blockWidth;
    var height = (endRow-startRow+1)*blockHeight;
    
    for (var x=startX;x<startX+width;x+=w0){
        for (var y=startY;y<startY+height;y+=h0){
            points.push(new Point(x,y));
        }
    }
    
    return points;
}

// 获取指定起始行、起始列范围内点的集合
// arr:[[1,2,1,2],[3,6,1,5]]
// blockWidth,blockHeight 画布中最小单位网格的大小
// w0,h0 被裁减的宽高
function getBoundaryByArr(arr,blockWidth,blockHeight,w0,h0){
    var points = new Array();
    for (var i=0;i<arr.length;i++){
        var tempPoints = getBoundary(arr[i][0],arr[i][1],arr[i][2],arr[i][3],blockWidth,blockHeight,w0,h0);
        for (j=0;j<tempPoints.length;j++){
            points.push(tempPoints[j]);
        }
    }
    
    return points;				
}
//-----------------------------------------------------------------------------------------------------

//-------------------------------------------------公共类---------------------------------------------
function Point(x,y){
    this.x = x;
    this.y = y;
}