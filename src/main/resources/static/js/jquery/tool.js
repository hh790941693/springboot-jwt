

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