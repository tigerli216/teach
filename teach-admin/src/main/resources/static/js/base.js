
// 判断字符或对象是否为空
function isEmpty(obj) {
	if (obj == undefined || trim(obj) == "" || obj == null || obj.length == 0) {
		return true;
	}
	return false;
}
//去空格（前后）
function trim(str) {
	if (str != undefined) {
		if (typeof(str) == "string") {
			return str.replace(/(^\s*)|(\s*$)/g, "");
		}
	}
	return str;
}
// 时间比较（大于当前时间则返回true）
function dateCompare(str) {
	if (isBlank(str)) {
		return false;
	}
	var now = new Date();
	var date = new Date(str.replace(/\-/g, "\/"));
	if (date > now) {
		return true;
	}
	return false;
}
//验证url
function checkUrlRight(value){
	if(value==""||value==null||value==undefined){
	   return true;
	}
	if (/^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test($.trim(value))){
	    return true;
	}else {
		return false;
	}
}

/**
 * 增加提示信息
 */
function addHint(className,content,addClass,removeClass){
	$('.'+className).removeClass('hide');
	
	if(removeClass != null && removeClass != "undefined"){
		$('.'+className+'-content').removeClass(removeClass);
	}
	
	if(addClass != null && addClass != "undefined"){
		$('.'+className+'-content').addClass(addClass);
	}
	$('.'+className+'-content').html(content);
}
/**
 * 取消提示信息
 */
function cancelHint(className,addClass,removeClass){
	$('.'+className).addClass('hide');
	
	if(removeClass != null && removeClass != "undefined"){
		$('.'+className+'-content').removeClass(removeClass);
	}
	
	if(addClass != null && addClass != "undefined"){
		$('.'+className+'-content').addClass(addClass);
	}
	$('.'+className+'-content').html("");
}

// 检查浏览器类型
function _wifi_checkbrowser() {
    var browse = "other";
    var ua = navigator.userAgent.toLowerCase();
    var ios = /.*(iphone|ios|ipod|ipad).*/;
    var android = /.*(android|adr).*/;
    var firefox = /.*(firefox).*/;
    var opera = /.*(opera).*/;
    
    if (ios.test(ua)) {	//IOS版
        browse = "ios";
    } else if (android.test(ua)) {	// Android版
        browse = "android";
    } else if (firefox.test(ua)) {	// firefox
    	browse = "firefox";
    } else if (opera.test(ua)) {	// opera
    	browse = "opera";
    } else if (ua.indexOf('msie') >= 0 && navigator.userAgent.indexOf('opera') < 0) {	// IE
    	browse = "ie";
    }
    return browse;
}

/**
 * 获得cookies信息
 * @param cookieName
 */
function getCookie(cookieName){
	var cookieValue = $.cookie(cookieName);
	if(!isEmpty(cookieValue)){
		// 替换双引号
		return cookieValue.replace(/\"/g,"");
	}
	return "";
}

/**
 * 设置cookies信息
 * @param cookieName
 * @param cookieValue
 */
function setCookie(cookieName,cookieValue){
	$.cookie(cookieName,cookieValue);
}

//是否有特殊字符
function checkSpecialCharacter(value){
	if(value==""||value==null||value==undefined){
	   return true;
	}
	var txt = new RegExp("[\\*,\\&,\\\\,\\/,\\?,\\|,\\:,\\<,\\>,\"]");
	if (txt.test($.trim(value))) {
	    return true;
	}else {
		return false;
	}
}

// --- 解决IE6、7、8、9不支持placeholder的问题
var JPlaceHolder = {
    //检测
    _check : function(){
        return 'placeholder' in document.createElement('input');
    },
    //初始化
    init : function(){
        if(!this._check()){
            this.fix();
        }
    },
    //修复
    fix : function(){
        jQuery(':input[placeholder]').each(function(index, element) {
            var self = $(this), txt = self.attr('placeholder');
            self.wrap($('<div></div>').css({position:'relative', zoom:'1', border:'none', background:'none', padding:'none', margin:'none'}));
            var pos = self.position(), h = self.outerHeight(true), paddingleft = self.css('padding-left');
            var holder = $('<span></span>').text(txt).css({position:'absolute', left:pos.left, top:pos.top, height:h, lienHeight:h, paddingLeft:paddingleft, color:'#aaa'}).appendTo(self.parent());
            if (!isBlank($(this).val())) {
            	holder.hide();
            }
            self.focusin(function(e) {
                holder.hide();
            }).focusout(function(e) {
                if(!self.val()){
                    holder.show();
                }
            });
            holder.click(function(e) {
                holder.hide();
                self.focus();
            });
        });
    }
};
//执行
jQuery(function(){
    // JPlaceHolder.init();    
});

//以下为修改jQuery Validation插件兼容Bootstrap的方法，没有直接写在插件中是为了便于插件升级
$.validator.setDefaults({
    highlight: function (element) {
        $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
    },
    success: function (element) {
        element.closest('.form-group').removeClass('has-error').addClass('has-success');
    },
    errorElement: "span",
    errorPlacement: function (error, element) {
        if (element.is(":radio") || element.is(":checkbox")) {
            error.appendTo(element.parent().parent().parent());
        } else {
            error.appendTo(element.parent());
        }
    },
    errorClass: "help-block m-b-none",
    validClass: "help-block m-b-none"
});

