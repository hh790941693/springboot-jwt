// 获取扑克中的value值
function getFeatures(pokers){
    var f = [];
    for (var i=0;i<pokers.length;i++){
        f.push(pokers[i].value);
    }
    
    return f.join("");
}

function getFeaturesExt(pokers){
    var f = [];
    for (var i=0;i<pokers.length;i++){
        f.push(pokers[i].value);
    }
    
    return f.join(" ");
}

// 对扑克就行排序(正序)
function orderPokers(pokers){
    if (pokers && pokers.length>1){
        pokers.sort(function(a,b){
            return a.value - b.value;
        });
    }  
}

// 排序
function orderPokersExt(pokers){
    if (pokers && pokers.length>1){
        pokers.sort(function(a,b){
            return a[0].value - b[0].value;
        });
    }
}

// 将数组中指定索引的元素移到数组的最后面去
function moveOneToLast(arr,index){
    var tempArr = arr.splice(index,1);
    arr.splice(arr.length,1,tempArr[0]);
}

// 将数组中指定的多个索引的元素依次移到到数组后面去
function moveMoreToLast(arr,indexArr){
    var tempArr = new Array();
    for (var i=arr.length-1;i>=0;i--){
        if (indexArr.isInArray(i)){
             tempArr.push(arr.splice(i,1)[0]);
        }
    }
    
    for (var i=0;i<tempArr.length;i++){
         arr.splice(arr.length,1,tempArr[i]);
    }
}


// 获取某一张牌出现的次数
function getOnePokerSize(pokers, value){
    var size = 0;
    for (var i=0;i<pokers.length;i++){
        if (pokers[i].value == value){
            size++;
        }
    }

    return size;
}

// 获取某一张牌的数组对象
function getMorePokerObjArr(pokers, value){
    var objArr = new Array();
    for (var i=0;i<pokers.length;i++){
        if (pokers[i].value == value){
            objArr.push(pokers[i]);
        }
    }

    return objArr;
}

// 获取单个
function getDan(pokers,isGetZha){
    if (typeof(isGetZha) == "undefined"){
        isGetZha = true;
    }
    var tempArr = new Array();
    for (var i=0;i<pokers.length;i++){
        if (i == pokers.length-1){
            tempArr.push(new Array(pokers[i]));
            break;
        }else if (i == 0){
            if (pokers[i].value != pokers[i+1].value){
                tempArr.push(new Array(pokers[i]));
            }
        }else{
            if (pokers[i-1].value != pokers[i].value && pokers[i].value != pokers[i+1].value){
                tempArr.push(new Array(pokers[i]));
            }
        }
    }
    
   if (isGetZha){
        // 判断是否有四炸
        var siZhaArr = getSi(pokers);
        if (siZhaArr.length != 0){
            tempArr = tempArr.concat(siZhaArr);
        }
        
        // 判断是否有王炸
        var wangZhaArr = getZha(pokers);
        if (wangZhaArr.length > 0){
            tempArr = tempArr.concat(wangZhaArr);
        }
        
        var sanArr = getSan(pokers);
        if (sanArr.length > 0){
            var tempArrNew = new Array();
            var tempFinalArr = new Array();
            for (var i=0;i<sanArr.length;i++){
                tempArr.push(new Array(sanArr[i][0]));
            }
        }
   }
   
    var duiArr =  getDui(pokers,isGetZha);
    if (duiArr.length > 0){
        var tempArrNew = new Array();
        var tempFinalArr = new Array();
        for (var i=0;i<duiArr.length;i++){
            tempArr.push(new Array(duiArr[i][0]));
        }
    }

    orderPokersExt(tempArr);
    return tempArr;
}

// 获取对子
function getDui(pokers,isGetZha){
    if (typeof(isGetZha) == "undefined"){
        isGetZha = true;
    }
    var tempArr = new Array();
    if (pokers.length >= 2){
        for (var i=0;i<pokers.length;i++){
            if (i==pokers.length-2){
                if (pokers[i].value == pokers[i+1].value){
                    tempArr.push(new Array(pokers[i],pokers[i+1]));
                }
                break;
            }else if (i==0){
                if (pokers[i].value== pokers[i+1].value && pokers[i+1].value != pokers[i+2].value){
                    tempArr.push(new Array(pokers[i],pokers[i+1]));
                }
            }else{
                if (pokers[i].value == pokers[i+1].value && pokers[i].value != pokers[i-1].value && pokers[i+1].value != pokers[i+2].value){
                    tempArr.push(new Array(pokers[i],pokers[i+1]));
                }
            }
        }
    }
    
    if (isGetZha){
        // 判断是否有四炸
        var siZhaArr = getSi(pokers);
        if (siZhaArr.length != 0){
            tempArr = tempArr.concat(siZhaArr);
        }
        
        // 判断是否有王炸
        var wangZhaArr = getZha(pokers);
        if (wangZhaArr.length > 0){
            tempArr = tempArr.concat(wangZhaArr);
        }
    
        // 判断是否有三个
        var sanArr = getSan(pokers,false);
        if (sanArr.length > 0){
            for (var i=0;i<sanArr.length;i++){
                var tempArrNew = new Array();
                
                tempArrNew.push(sanArr[i][0]);
                tempArrNew.push(sanArr[i][1]);
                tempArr.push(tempArrNew);
            }
        }
    }

    orderPokersExt(tempArr);
    return tempArr;
}

// 获取三个子
function getSan(pokers,isGetZha){
    if (typeof(isGetZha) == "undefined"){
        isGetZha = true;
    }
    var tempArr = new Array();
    if (pokers.length >= 3){
        for (var i=0;i<pokers.length;i++){
            if (i == pokers.length-3){
                if (pokers[i].value == pokers[i+1].value && pokers[i+1].value == pokers[i+2].value){
                    tempArr.push(new Array(pokers[i],pokers[i+1],pokers[i+2]));
                }
                break;
            }else if (i==0){
                if (pokers[i].value == pokers[i+1].value && pokers[i+1].value == pokers[i+2].value && pokers[i+2].value != pokers[i+3].value){
                    tempArr.push(new Array(pokers[i],pokers[i+1],pokers[i+2]));
                }
            }else{
                if (pokers[i].value != pokers[i-1].value && pokers[i].value == pokers[i+1].value && pokers[i+1].value == pokers[i+2].value && pokers[i+2].value != pokers[i+3].value){
                    tempArr.push(new Array(pokers[i],pokers[i+1],pokers[i+2]));
                }                            
            }
        }
    }
    
    if (isGetZha){
        // 判断是否有四炸
        var siZhaArr = getSi(pokers);
        if (siZhaArr.length != 0){
            tempArr = tempArr.concat(siZhaArr);
        }
        
        // 判断是否有王炸
        var wangZhaArr = getZha(pokers);
        if (wangZhaArr.length > 0){
            tempArr = tempArr.concat(wangZhaArr);
        }
    }
    
    
    orderPokersExt(tempArr);
    return tempArr;
}

// 获取四个
function getSi(pokers){
    var tempArr = new Array();
    if (pokers.length >=4){
        for (var i=0;i<pokers.length;i++){
            if (i == pokers.length-4){
                if (pokers[i].value == pokers[i+1].value && pokers[i+1].value == pokers[i+2].value &&  pokers[i+2].value == pokers[i+3].value){
                    tempArr.push(new Array(pokers[i],pokers[i+1],pokers[i+2],pokers[i+3]));
                }
                break;
            }else if (i==0){
                if (pokers[i].value == pokers[i+1].value && pokers[i+1].value == pokers[i+2].value &&  pokers[i+2].value == pokers[i+3].value && pokers[i+3].value != pokers[i+4].value){
                    tempArr.push(new Array(pokers[i],pokers[i+1],pokers[i+2],pokers[i+3]));
                }
            }else{
                if (pokers[i].value != pokers[i-1].value && pokers[i].value == pokers[i+1].value && pokers[i+1].value == pokers[i+2].value && pokers[i+2].value == pokers[i+3].value && pokers[i+3].value != pokers[i+4].value){
                    tempArr.push(new Array(pokers[i],pokers[i+1],pokers[i+2],pokers[i+3]));
                }                            
            }
        }
    }

    orderPokersExt(tempArr);
    return tempArr;
}

// 获取顺子
function getShunzi(pokers,length){
    var tempArr = new Array();
    if (typeof(length) == "undefined"){
        length = 5;
    }
    if (pokers.length >= length && length>=5 && length<=12){
        for (var i=3;i<i+length;i++){
            if (i == pokers.length-length){
                break;
            }
        
            for (j=i;j<i+length;j++){
                if (getOnePokerSize(pokers,j) >0 && getOnePokerSize(pokers,j)<4 ){
                
                }else{
                    break;
                }
                
                if (j == i+length-1){
                    var arr = new Array();
                    for (j=i;j<i+length;j++){
                        arr.push(getMorePokerObjArr(pokers,j)[0]);
                    }
                    tempArr.push(arr);
                }
            }
        }
    }
    
    // 判断是否有四炸
    var siZhaArr = getSi(pokers);
    if (siZhaArr.length != 0){
        tempArr = tempArr.concat(siZhaArr);
    }
    
    // 判断是否有王炸
    var wangZhaArr = getZha(pokers);
    if (wangZhaArr.length > 0){
        tempArr = tempArr.concat(wangZhaArr);
    } 
    
    orderPokersExt(tempArr);
    return tempArr;
}

// 获取连对
function getLiandui(pokers,length){
    var tempArr = new Array();
    if (typeof(length) == "undefined"){
        length = 6;
    }
    if (pokers.length >= length && length % 2 == 0 && length >=6){
        for (var i=3;i<i+length/2;i++){
            if (i == pokers.length-length){
                break;
            }
        
            for (j=i;j<i+length/2;j++){
                if (getOnePokerSize(pokers,j) >=2 && getOnePokerSize(pokers,j)<4 ){
                
                }else{
                    break;
                }
                
                if (j == i+length/2-1){
                    var arr = new Array();
                    for (j=i;j<i+length/2;j++){
                        arr.push(getMorePokerObjArr(pokers,j)[0]);
                        arr.push(getMorePokerObjArr(pokers,j)[1]);
                    }
                    tempArr.push(arr);
                }
            }
        }
    }
    
    // 判断是否有四炸
    var siZhaArr = getSi(pokers);
    if (siZhaArr.length != 0){
        tempArr = tempArr.concat(siZhaArr);
    }
    
    // 判断是否有王炸
    var wangZhaArr = getZha(pokers);
    if (wangZhaArr.length > 0){
        tempArr = tempArr.concat(wangZhaArr);
    } 
    
    orderPokersExt(tempArr);
    return tempArr;
}

// 获取三带1或者三带2
function getSan1Or2(pokers,length){
    var resultArr = new Array();
    
    if (typeof(length) == "undefined"){
        length = 4;
    }
    
    // 先获取三个,再获取一个;如果三个没有,或者1个没有,则直接获取炸弹
    var daiArr = new Array();
    if (length == 5){
        daiArr = getDui(pokers,false);
    }else if (length == 4){
        daiArr = getDan(pokers,false);
    }
    var sanArr = getSan(pokers,false);
    
    if (daiArr.length > 0 || sanArr.length > 0){
        // 交叉组合
        for (var i=0;i<sanArr.length;i++){
            for (var j=0;j<daiArr.length;j++){
                var tempArr = sanArr[i].concat(daiArr[j]);
                resultArr.push(tempArr);
            }   
        }
    }
    
    // 判断是否有四炸
    var siZhaArr = getSi(pokers);
    if (siZhaArr.length != 0){
        resultArr = resultArr.concat(siZhaArr);
    }
    
    // 判断是否有王炸
    var wangZhaArr = getZha(pokers);
    if (wangZhaArr.length > 0){
        resultArr = resultArr.concat(wangZhaArr);
    }
    
    orderPokersExt(resultArr);
    return resultArr;
}

function getSid2(pokers,length){
    var resultArr = new Array();
    
    if (typeof(length) == "undefined"){
        length = 6;
    }
    
    var daiArr = getDui(pokers,false); // 可以拆三个
    // 判断是否有三个
    var sanArr = getSan(pokers,false);
    if (sanArr.length > 0){
        for (var i=0;i<sanArr.length;i++){
            var tempArrNew = new Array();
            
            tempArrNew.push(sanArr[i][0]);
            tempArrNew.push(sanArr[i][1]);
            daiArr.push(tempArrNew);
        }
    }
    
    var siArr = getSi(pokers);
    if (siArr.length > 0 && daiArr.length > 0){
        var tempDaiArr;
        if (length == 6){
            // 获取一个对子
            for (var i=0;i<siArr.length;i++){
                for (var j=0;j<daiArr.length;j++){
                    var tempArr = siArr[i].concat(daiArr[j]);
                    resultArr.push(tempArr);
                }
            }
        }else if (length == 8 && daiArr.length >= 2){
            // 获取两个对子
            var soloTempArr = new Array();
            for (var i=0;i<daiArr.length;i++){
                for (var j=i+1;j<daiArr.length;j++){
                    var tempArr = daiArr[i].concat(daiArr[j]);
                    soloTempArr.push(tempArr);
                }
            }
            
            for (var i=0;i<siArr.length;i++){
                for (var j=0;j<soloTempArr.length;j++){
                    var tempArr = siArr[i].concat(soloTempArr[j]);
                    resultArr.push(tempArr);
                }
            }
        }
    }
    
    // 判断是否有四炸
    if (siArr.length != 0){
        resultArr = resultArr.concat(siArr);
    }
    
    // 判断是否有王炸
    var wangZhaArr = getZha(pokers);
    if (wangZhaArr.length > 0){
        resultArr = resultArr.concat(wangZhaArr);
    }
    
    orderPokersExt(resultArr);
    return resultArr;
}

// 判断是否含有王炸
function isHasZha(pokers){
    return (getOnePokerSize(pokers,16) == 1 && getOnePokerSize(pokers,17) == 1);
}

// 获取王炸2张牌的对象数组
function getZha(pokers){
    var tempArr = new Array();
    var finalTempArr = new Array();

    if (isHasZha(pokers)){
        tempArr.push(getMorePokerObjArr(pokers,16)[0]);
        tempArr.push(getMorePokerObjArr(pokers,17)[0]);
        finalTempArr.push(tempArr);
    }
    
    return finalTempArr;
}

// 获取炸弹
function getZhadan(pokers){
    var resultArr = new Array();
    
    // 判断是否有四炸
    var siArr = getSi(pokers);
    if (siArr.length != 0){
        resultArr = resultArr.concat(siArr);
    }
    
    // 判断是否有王炸
    var wangZhaArr = getZha(pokers);
    if (wangZhaArr.length > 0){
        resultArr = resultArr.concat(wangZhaArr);
    }
    
    orderPokersExt(resultArr);
    return resultArr;    
}


// 单牌
function Dan(){}
Dan.prototype = {
    name:'单个',
    exp  : /^((1[0-7])|[3-9])$/,
    valid: function(pokers){
            if (pokers.length == 1 && this.exp.test(getFeatures(pokers))){
                return true;
            }
            return false;
    },
    compare: function(selfPokers,otherPokers){
        if (this.valid(selfPokers) && this.valid(otherPokers)){
            
            if (selfPokers[0].value > otherPokers[0].value){
                return true;
            }
        }
        return false;
    },
    // selfPokers 手上的牌  otherPokers:上家打的牌
    matchExt:function(selfPokers,otherPokers){
        var finalPokerArr = new Array();
        var validPokerArr = getDan(selfPokers);
        for (var i=0;i<validPokerArr.length;i++){
            if (validPokerArr[i].length == 1){
                if (this.compare(validPokerArr[i],otherPokers)){
                    finalPokerArr.push(validPokerArr[i]);
                }
            }else{
                finalPokerArr.push(validPokerArr[i]);
            }
        }
        
        return finalPokerArr;
    }
}

// 对
function Dui(){}
Dui.prototype = {
    name:'对子',
    exp:/^(3{2}|4{2}|5{2}|6{2}|7{2}|8{2}|9{2}|(10){2}|(11){2}|(12){2}|(13){2}|(14){2}|(15){2})$/,
    valid:function(pokers){
        if (pokers.length == 2 && this.exp.test(getFeatures(pokers))){
            return true;
        }
        return false;
    },
    compare:function(selfPokers,otherPokers){
        if (this.valid(selfPokers) && this.valid(otherPokers)){
            
            if (selfPokers[0].value > otherPokers[0].value){
                return true;
            }
        }
        return false;
    },
    // selfPokers 手上的牌  otherPokers:上家打的牌
    matchExt:function(selfPokers,otherPokers){
        var finalPokerArr = new Array();
        var validPokerArr = getDui(selfPokers);
        for (var i=0;i<validPokerArr.length;i++){
            if (validPokerArr[i].length == 2 && !isHasZha(validPokerArr[i])){
                if (this.compare(validPokerArr[i],otherPokers)){
                    finalPokerArr.push(validPokerArr[i]);
                }
            }else{
                finalPokerArr.push(validPokerArr[i]);
            }
        }
        
        return finalPokerArr;
    }
}

// 顺子
function Shunzi(){}
Shunzi.prototype = {
    name:'顺子',
    exp:/^([3-9]|1[0-4]){5,12}$/,
    order:function(pokers){
        orderPokers(pokers);
    },
    valid:function(pokers){
        if (pokers.length<5){
            return false;
        }
        this.order(pokers);
        if (!this.exp.test(getFeatures(pokers))){
            return false;
        }
        
        for (var i=0;i<pokers.length;i++){
            if (i==0 && pokers[i].value>=11){
                return false;
            }
            
            if (i>0){
                if (pokers[i].value-pokers[i-1].value != 1){
                    return false;
                }
            }
        }
        
        return true;
    },
    compare:function(selfPokers,otherPokers){
        if (this.valid(selfPokers) && this.valid(otherPokers)){
            if (selfPokers.length == otherPokers.length && selfPokers[0].value > otherPokers[0].value){
                return true;
            }
        }
        
        return false;
    },
    // selfPokers 手上的牌  otherPokers:上家打的牌
    matchExt:function(selfPokers,otherPokers){
        var finalPokerArr = new Array();
        var validPokerArr = getShunzi(selfPokers,otherPokers.length);
        for (var i=0;i<validPokerArr.length;i++){
            if (validPokerArr[i].length >= 5){
                if (this.compare(validPokerArr[i],otherPokers)){
                    finalPokerArr.push(validPokerArr[i]);
                }
            }else{
                finalPokerArr.push(validPokerArr[i]);
            }
        }
        
        return finalPokerArr;
    }
}

// 连对
function LianDui(){}
LianDui.prototype = {
    name:'连对',
    exp:/^(3{2}|4{2}|5{2}|6{2}|7{2}|8{2}|9{2}|(10){2}|(11){2}|(12){2}|(13){2}|(14){2}){3,9}$/,
    order:function(pokers){
        orderPokers(pokers);
    },
    valid:function(pokers){
        if (pokers.length % 2 == 0 && pokers.length >=6){
            this.order(pokers);
            if (!this.exp.test(getFeatures(pokers))){
                return false;
            }
            
            for (var i=0;i<pokers.length;i++){
                if (i==0 && pokers[i] >=13){
                    return false;
                }else if (i>0 && i%2 == 0){
                    if (pokers[i].value - pokers[i-1].value != 1){
                        return false;
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    },
    compare:function(selfPokers,otherPokers){
        if (this.valid(selfPokers) && this.valid(otherPokers)){
            if (selfPokers.length == otherPokers.length && selfPokers[0].value > otherPokers[0].value){
                return true;
            }
        }
        
        return false;
    },
    // selfPokers 手上的牌  otherPokers:上家打的牌
    matchExt:function(selfPokers,otherPokers){
        var finalPokerArr = new Array();
        var validPokerArr = getLiandui(selfPokers,otherPokers.length);
        for (var i=0;i<validPokerArr.length;i++){
            if (validPokerArr[i].length >= 6){
                if (this.compare(validPokerArr[i],otherPokers)){
                    finalPokerArr.push(validPokerArr[i]);
                }
            }else{
                finalPokerArr.push(validPokerArr[i]);
            }
        }
        
        return finalPokerArr;
    }
}

// 三张
function San(){}
San.prototype = {
    name:'三个',
    exp:/^(3{3}|4{3}|5{3}|6{3}|7{3}|8{3}|9{3}|(10){3}|(11){3}|(12){3}|(13){3}|(14){3}|(15){3})$/,
    valid:function(pokers){
        if (pokers.length == 3 && this.exp.test(getFeatures(pokers))){
            return true;
        }else{
            return false;
        }
        
    },
    compare:function(selfPokers,otherPokers){
        if (this.valid(selfPokers) && this.valid(otherPokers)){
            if (selfPokers[0].value > otherPokers[0].value){
                return true;
            }
        }
        
        return false;
    },
    // selfPokers 手上的牌  otherPokers:上家打的牌
    matchExt:function(selfPokers,otherPokers){
        var finalPokerArr = new Array();
        var validPokerArr = getSan(selfPokers);
        for (var i=0;i<validPokerArr.length;i++){
            if (validPokerArr[i].length == 3){
                if (this.compare(validPokerArr[i],otherPokers)){
                    finalPokerArr.push(validPokerArr[i]);
                }
            }else{
                finalPokerArr.push(validPokerArr[i]);
            }
        }
        
        return finalPokerArr;
    }
}

// 三带1
function Sand1(){}
Sand1.prototype = {
    name:'三对1',
    exp:/^((3{3}|4{3}|5{3}|6{3}|7{3}|8{3}|9{3}|(10){3}|(11){3}|(12){3}|(13){3}|(14){3}|(15){3})([1-9]|(1[0-5])))$/,
    order:function(pokers){
        orderPokers(pokers);
        if (pokers[0].value != pokers[1].value){
            moveOneToLast(pokers,0);
        }
    },
    valid:function(pokers){
        if (pokers.length == 4){
            this.order(pokers);
            if (this.exp.test(getFeatures(pokers))){
                // 排除炸弹
                if (pokers[2].value == pokers[3].value){
                    return false;
                }
                
                return true;
            }
        }
        
        return false;
    },
    compare:function(selfPokers,otherPokers){
        if (this.valid(selfPokers) && this.valid(otherPokers)){
            if (selfPokers[0].value > otherPokers[0].value){
                return true;
            }
        }
        
        return false;
    },
    // selfPokers 手上的牌  otherPokers:上家打的牌
    matchExt:function(selfPokers,otherPokers){
        var finalPokerArr = new Array();
        var validPokerArr = getSan1Or2(selfPokers,otherPokers.length);
        for (var i=0;i<validPokerArr.length;i++){
            if (validPokerArr[i].length == 4 && getSi(validPokerArr[i]) == 0){
                if (this.compare(validPokerArr[i],otherPokers)){
                    finalPokerArr.push(validPokerArr[i]);
                }
            }else{
                finalPokerArr.push(validPokerArr[i]);
            }
        }
        
        return finalPokerArr;
    }
}

// 三带2
function Sand2(){}
Sand2.prototype = {
    name:'三对2',
    exp:/^((3{3}|4{3}|5{3}|6{3}|7{3}|8{3}|9{3}|(10){3}|(11){3}|(12){3}|(13){3}|(14){3}|(15){3})(3{2}|4{2}|5{2}|6{2}|7{2}|8{2}|9{2}|(10){2}|(11){2}|(12){2}|(13){2}|(14){2}|(15){2}))$/,
    order:function(pokers){
        orderPokers(pokers);
        if (pokers[1].value != pokers[2].value){
            moveMoreToLast(pokers,new Array(0,1));
        }
    },
    valid:function(pokers){
        if (pokers.length == 5){
            this.order(pokers);
            if (this.exp.test(getFeatures(pokers))){
                return true;
            }
        }
        
        return false;
    },
    compare:function(selfPokers,otherPokers){
        if (this.valid(selfPokers) && this.valid(otherPokers)){
            if (selfPokers[0].value > otherPokers[0].value){
                return true;
            }
        }
        
        return false;
    },
    // selfPokers 手上的牌  otherPokers:上家打的牌
    matchExt:function(selfPokers,otherPokers){
        var finalPokerArr = new Array();
        var validPokerArr = getSan1Or2(selfPokers,otherPokers.length);
        for (var i=0;i<validPokerArr.length;i++){
            if (validPokerArr[i].length == 5){
                if (this.compare(validPokerArr[i],otherPokers)){
                    finalPokerArr.push(validPokerArr[i]);
                }
            }else{
                finalPokerArr.push(validPokerArr[i]);
            }
        }
        
        return finalPokerArr;
    }
}

//4带2
function Sid2(){}
Sid2.prototype={
    name:function(pokers){
        if (typeof(pokers) != "undefined"){
            if (pokers.length == 6){
                return "四对2(个子)";
            }else if (pokers.length == 8){
                return "四对2(对子)";
            }
        }
    },
    exp1: /^((3{4}|4{4}|5{4}|6{4}|7{4}|8{4}|9{4}|(10){4}|(11){4}|(12){4}|(13){4}|(14){4}|(15){4})(3{2}|4{2}|5{2}|6{2}|7{2}|8{2}|9{2}|(10){2}|(11){2}|(12){2}|(13){2}|(14){2}|(15){2}){2})$/, 
    exp2: /^((3{4}|4{4}|5{4}|6{4}|7{4}|8{4}|9{4}|(10){4}|(11){4}|(12){4}|(13){4}|(14){4}|(15){4})([1-9]|1[0-5]){2})$/,
    order: function(pokers){
        orderPokers(pokers);
        if (pokers.length == 6){
            if ( pokers[0].value != pokers[1].value && pokers[1].value != pokers[2].value){
                moveMoreToLast(pokers,new Array(0,1));
            }else if (pokers[0].value == pokers[1].value && pokers[1].value != pokers[2].value){
                moveMoreToLast(pokers,new Array(0,1));
            }else if  (pokers[0].value != pokers[1].value){
                moveOneToLast(pokers,0);
            }
        }else if (pokers.length == 8){
            if (pokers[1].value != pokers[3].value && pokers[3].value != pokers[4].value){
                moveMoreToLast(pokers,new Array(0,1,2,3));
            }else if (pokers[1].value != pokers[3].value){
                moveMoreToLast(pokers,new Array(0,1));
            }
        }
    },
   valid: function(pokers){
        if (pokers.length == 6 || pokers.length == 8){
            this.order(pokers);
            
            if (pokers.length == 6){
                if (this.exp2.test(getFeatures(pokers))){
                    return true;
                }
            }else if (pokers.length == 8){
                if (this.exp1.test(getFeatures(pokers))){
                    return true;
                }                
            }
        }

        return false;
    },
    compare:function(selfPokers,otherPokers){
        if (this.valid(selfPokers) && this.valid(otherPokers)){
            if (selfPokers.length == otherPokers.length && selfPokers[0].value > otherPokers[0].value){
                return true;
            }
        }
        
        return false;
    },
    // selfPokers 手上的牌  otherPokers:上家打的牌
    matchExt:function(selfPokers,otherPokers){
        var finalPokerArr = new Array();
        var validPokerArr = getSid2(selfPokers,otherPokers.length);
        for (var i=0;i<validPokerArr.length;i++){
            if (validPokerArr[i].length >= 6){
                if (this.compare(validPokerArr[i],otherPokers)){
                    finalPokerArr.push(validPokerArr[i]);
                }
            }else{
                finalPokerArr.push(validPokerArr[i]);
            }
        }
        
        return finalPokerArr;
    }
}

// 飞机带翅膀
function Feiji(){}
Feiji.prototype={
    exp:'',
    valid:function(){
        
    },
    compare:function(selfPokers,otherPokers){
        if (this.valid(selfPokers) && this.valid(otherPokers)){
            if (selfPokers.length == otherPokers.length && selfPokers[0].value > otherPokers[0].value){
                return true;
            }
        }
        
        return false;
    }
}

// 炸弹
function Zha(){}
Zha.prototype={
    exp:/^((3{4}|4{4}|5{4}|6{4}|7{4}|8{4}|9{4}|(10){4}|(11){4}|(12){4}|(13){4}|(14){4}|(15){4})|(16|17){2})$/,
    order:function(pokers){
       orderPokers(pokers);
    },
    isWZ:function(pokers){
        this.order(pokers);
        return /^1617$/.test(getFeatures(pokers));
    },
    name:function(pokers){
        if (typeof(pokers) != "undefined"){
            if (pokers.length == 2){
                return "王炸";
            }
            return "4炸";
        }    
    },
    isZha:function(pokers){
        return this.exp.test(getFeatures(pokers));
    },
    valid:function(pokers){
        if (this.isZha(pokers)){
            return true;
        }
        return false;
    },
    compare:function(selfPokers,otherPokers){
        if (this.valid(selfPokers) && this.valid(otherPokers)){
            if (selfPokers.length == otherPokers.length){
                if (selfPokers[0].value > otherPokers[0].value){
                    return true;
                }
                return false;
            }else{
                if (this.isWZ(selfPokers)){
                    return true;
                }
            }
        }
        
        return false;
    },
    // selfPokers 手上的牌  otherPokers:上家打的牌
    matchExt:function(selfPokers,otherPokers){
        var finalPokerArr = new Array();
        var validPokerArr = getZhadan(selfPokers);
        for (var i=0;i<validPokerArr.length;i++){
            if (validPokerArr[i].length == 4){
                if (this.compare(validPokerArr[i],otherPokers)){
                    finalPokerArr.push(validPokerArr[i]);
                }
            }else{
                finalPokerArr.push(validPokerArr[i]);
            }
        }
        
        return finalPokerArr;
    }
}