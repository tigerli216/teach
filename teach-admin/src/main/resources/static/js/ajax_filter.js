(function($){
    // 备份jquery的ajax方法  
    var _ajax=$.ajax;
    
    window.ajax_url_result = {};	// 避免ajax重复提交
    window.ajax_url = null;
    // 重写jquery的ajax方法  
    $.ajax=function(opt){
    	ajax_url = opt.url;
    	if (window.ajax_url_result[ajax_url] != undefined) {
    		return;
    	}
    	window.ajax_url_result[ajax_url] = 1;
    	
        //备份opt中error和success方法  
        var fn = {
            error:function(XMLHttpRequest, textStatus, errorThrown){},
            success:function(data, textStatus){},
            complete:function(XMLHttpRequest,textStatus){}
        }
        if(opt.error){
            fn.error=opt.error;
        }
        if(opt.success){
            fn.success=opt.success;
        }
        if(opt.complete){
            fn.complete=opt.complete;
        }
          
        // 扩展增强处理
        var _opt = $.extend(opt,{
            error:function(XMLHttpRequest, textStatus, errorThrown){
                // 错误方法增强处理 
                fn.error(XMLHttpRequest, textStatus, errorThrown);
            },
            success:function(data, textStatus){
            	delete window.ajax_url_result[ajax_url];	// 因为success比complete先执行
                // 成功回调方法增强处理
            	if (data) {
            		// session过期或无访问权限  不做处理
            		if (data == "sessionTimeout" || data == "noAccessPermissions") {
            		} else if (data.result == false && data.code=="500") {
            			alert(data.msg);
            		} else {
                		fn.success(data, textStatus);
                	}
            	}
            },
            complete:function(XMLHttpRequest,textStatus) {
            	// 通过XMLHttpRequest取得responseText，sessionstatus
                var responseText = XMLHttpRequest.responseText;
                if(responseText){
              	  try{
                  	  if(responseText){
                  	  	  if (responseText == "sessionTimeout") {
	                  		  // session过期
	                  		  alert("您好，您的登陆信息已失效，请重新登陆！");
	                  		  var url = location.href;
	                  		  var urlArr = url.split("/");
	                  		  var newUrl = urlArr[0] + "//" + urlArr[2] + "/" + urlArr[3] + "/login";
	                  		  top.location.href = newUrl;
                  	  	  } else if (responseText == "noAccessPermissions") {
                  	  	  	  // 无访问权限
	                  		  alert("您好，您没有该功能的访问权限！");
                  	  	  } else {
                        	  fn.complete(XMLHttpRequest, textStatus);
                      	  }
                  	  }
              	  }catch(e){}
                } else {
                	fn.complete(XMLHttpRequest, textStatus);
                }
            }
        });
       return _ajax(_opt);
    };
    
})(jQuery);

$(document).ready(function() {
	$(document).ajaxComplete(function(event, jqXHR, options){
 		delete window.ajax_url_result[options.url];
 	});
});


// --------------------  jquery监听resize的小插件 -------------
(function($, h, c) {
	var a = $([]), e = $.resize = $.extend($.resize, {}), i, k = "setTimeout", j = "resize", d = j
			+ "-special-event", b = "delay", f = "throttleWindow";
	e[b] = 250;
	e[f] = true;
	$.event.special[j] = {
		setup : function() {
			if (!e[f] && this[k]) {
				return false
			}
			var l = $(this);
			a = a.add(l);
			$.data(this, d, {
				w : l.width(),
				h : l.height()
			});
			if (a.length === 1) {
				g()
			}
		},
		teardown : function() {
			if (!e[f] && this[k]) {
				return false
			}
			var l = $(this);
			a = a.not(l);
			l.removeData(d);
			if (!a.length) {
				clearTimeout(i)
			}
		},
		add : function(l) {
			if (!e[f] && this[k]) {
				return false
			}
			var n;
			function m(s, o, p) {
				var q = $(this), r = $.data(this, d);
				r.w = o !== c ? o : q.width();
				r.h = p !== c ? p : q.height();
				n.apply(this, arguments)
			}
			if ($.isFunction(l)) {
				n = l;
				return m
			} else {
				n = l.handler;
				l.handler = m
			}
		}
	};
	function g() {
		i = h[k](function() {
			a.each(function() {
				var n = $(this), m = n.width(), l = n.height(), o = $.data(
						this, d);
				if (m !== o.w || l !== o.h) {
					n.trigger(j, [ o.w = m, o.h = l ])
				}
			});
			g()
		}, e[b])
	}
})(jQuery, this);