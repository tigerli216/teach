var _v="?_="+(new Date()).getTime();

var QueryMain = {
		printBody:function(){
			window.print();
		},
		loadModal:function(){ 
			jQuery("<div id='msgDiv' align='center'  style='width:0px;heigth:0px;text-align:center;line-heigth:center;z-index:9999999;_z-index:9999999;margin-left:-110px;_margin-left:-40px;position:absolute;left:35%;top:40%;font:12px/1.6em Verdana, Geneva, Arial, Helvetica, sans-serif'><img src='http://m1.img.10010.com/images/loading.gif' align='absmiddle'></div>").modal({
				escClose:true,  
				close:true,
				zIndex:"9999999"
			});
		},
		closeModal:function(){
			//jQuery.modal.close();
			$('#simplemodal-overlay').hide();
			$('#simplemodal-container').hide(); 

		},
		loadData: function(argUrl,paramdata){
			radomT="?v="+(new Date()).getTime();
			var url=argUrl+radomT;

			jQuery.ajax({ url:url, type:"POST", async:true, dataType:"script",data:paramdata,
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				beforeSend:function(){
					QueryMain.loadModal();
				}
			});
			//20秒后自动隐藏loading图片
			setTimeout("QueryMain.closeModal()",25000);
		},
		showLoginModal:function(){
			$('#l_').show();
			$('.alpha').show();
			$('.rejectionClose').click(function(){$('.alpha').hide();$('#l_').hide();window.location="http://wcf.10010.com/wcf/";});
		},
		//处理数据，加载jstemplate模板
		processTemplateData: function(show,templateId,data){
			jQuery(show).setTemplateElement(templateId).processTemplate(data); 
			QueryMain.closeModal();
		},
		//处理分页显示
		processPageTag:function(totalSize,currentPage,pageSize){
			var totalPage=(totalSize%pageSize==0?totalSize/pageSize:Math.round(totalSize/pageSize));
			if(currentPage>totalPage){currentPage=totalPage;}
			var last=currentPage-1;
			var next=currentPage+1;	
			if(last<1){last=1;}
			if(next>totalPage){next=totalPage;}
			var start = pageSize*(currentPage-1);
			var end = currentPage*pageSize;
			var pageInfo={'totalSize':totalSize,'totalPage':totalPage,'currentPage':currentPage,'pageSize':pageSize,'last':last,'next':next,'start':start,'end':end};
			return pageInfo;
		},
		//处理数据，分页显示数据
		processPageQuery:function(show,templateId,data,currentPage,pageSize){
			var start = pageSize*(currentPage-1);
			var end = currentPage*pageSize;
			var subjson=QueryMain.sliceData(data,start,end);
			QueryMain.processTemplateData(show,templateId,subjson);
		},
		goPage:function(show,templateId,totalSize,pageinput,pageSize){
			var gopage=jQuery(pageinput).val();
			QueryMain.processPageTag(show,templateId,totalSize,gopage,pageSize);
		},
		exceptionBack:function(data){
			alert(data.msg);
		},
		getHTML:function(id){
			html=jQuery("#"+id).html();
			return html;
		},
		//获取cookie
		getCookie:function(objName){//获取指定名称的cookie的值
			var arrStr = document.cookie.split(";");
			var o="";
			for(var i = 0;i < arrStr.length;i++){
				var temp = arrStr[i].split("=");
				if(jQuery.trim(temp[0]) == jQuery.trim(objName)) {
					o=unescape(jQuery.trim(temp[1]));
					break;
				}
			}
			return o;
		}
};

/**
 * 定义简单Map
 * @returns {___map_0}
 */
function getMap() {// 初始化map_,给map_对象增加方法，使map_像Map
	var map_ = new Object();
	map_.put = function(key, value) {
		map_[key + '_'] = value;
	};
	map_.get = function(key) {
		return map_[key + '_'];
	};
	map_.remove = function(key) {
		delete map_[key + '_'];
	};
	map_.keyset = function() {
		var ret = "";
		for (var p in map_) {
			if (typeof p == 'string' && p.substring(p.length - 1) == "_") {
				ret += ",";
				ret += p.substring(0, p.length - 1);
			}
		}
		if (ret == "") {
			return ret.split(",");
		} else {
			return ret.substring(1).split(",");
		}
	};
	return map_;
}

/**
 * @功能:获取当前url的域名
 */
function getUrlDomainName(){
	var host = "null";
	if(typeof url == "undefined"
		|| null == url)
		url = window.location.href;
	var regex = /.*\:\/\/([^\/]*).*/;
	var match = url.match(regex);
	if(typeof match != "undefined"
		&& null != match){
		host = match[1];
	}
	return "http://"+host+"/";
}

/**
 * @功能:跳转url
 * @param url
 */
function getToUrl(url){
	var suffix = "systssuffix="+new Date().getTime();

	if(url.endWith(".html") || url.endWith(".htm") || url.endWith(".do")){
		url = url + "?" + suffix;
	}else{
		url = url + "&" + suffix;
	}

	if(!url.startWith('http')){
		url = getUrlDomainName() + url;
	}

	window.location.assign(encodeURI(url));
}

//清空div中input组件的值
function emptyDivInputValue(id) {
	$("#" + id + " input,textarea").each(function() {
		$(this).val("");
	});
	$("#" + id + " select").each(function() {
		if ($(this).attr("multiple") == "multiple"){
			$(this).val("");
		} else {
			$(this).find("option:first").attr("selected", true);
			$(this).find("option:not(:first)").attr("selected", false);
		}
	});
}

/**
 * @功能:给input标签赋值
 * @param obj是对象
 * @param initParams是input标签id的数组，可也为空
 * @param divId，可也为空
 * @parm type 类型（如为1则对html内容初始化）
 * 
 */
function initParam(obj, initParams, divId, type){
	if(divId != null && initParams != null){
		if (type == 1) {
			for(var i = 0; i < initParams.length; i++){
				$('#' + divId + ' #' + initParams[i]).html(obj[initParams[i]]);
			}
		} else {
			for(var i = 0; i < initParams.length; i++){
				$('#' + divId + ' #' + initParams[i]).val(obj[initParams[i]]);
			}
		}
	}else{
		if(divId == null && initParams != null){
			for(var i = 0; i < initParams.length; i++){
				$('#' + initParams[i]).val(obj[initParams[i]]);
			}
		}else if(divId != null && initParams == null){
			for(var o in obj){
				$('#' + divId + ' [name=' + o + "]").val(obj[o]);
				$('#' + divId + ' #' + o).val(obj[o]);
				if (type == 1) {
					$('#' + divId + ' #' + o).html(obj[o]);
				}
			}
		}else{
			for(var o in obj){
				$('#' + o).val(obj[o]);
			}
		}
	}
}

/**
 * 判断是否为IE10以下版本
 */
function isIE10Down(){
	// 判断是否是IE10以下版本
	var browser=navigator.appName 
	var b_version=navigator.appVersion 
	if(browser=="Microsoft Internet Explorer") {
		var version=b_version.split(";"); 
		var trim_Version=version[1].replace(/[ ]/g,""); 
		if(trim_Version=="MSIE6.0" || trim_Version=="MSIE7.0" || trim_Version=="MSIE8.0" || trim_Version=="MSIE9.0"){
			return true;
		}
	}
	return false;
}

/**
 * @功能:计算字符串长度(英文占1个字符，中文汉字占2个字符)
 */
String.prototype.gblen = function() {  
	var len = 0;  
	for (var i=0; i<this.length; i++) {  
		if (this.charCodeAt(i)>127 || this.charCodeAt(i)==94) {  
			len += 2;  
		} else {  
			len ++;  
		}  
	}  
	return len;  
}
