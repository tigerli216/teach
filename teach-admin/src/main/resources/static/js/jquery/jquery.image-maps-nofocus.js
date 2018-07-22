 var curUpload = "";
 $(document).ready(function () {
	 $('.main_page').each(function(){
		 bind_map_event($(this));
	 })
 });
function bind_map_event(obj){
	var box = obj.find('.box');
	var boxW = box.width();
	var boxH = box.height();
	var boxX = box.offset().left;
	var boxY = box.offset().top;
	obj.find('.div1').each(function(){
		var div1 = $(this);
		var div = $(this).parent();
		var div2 = $(this).siblings('.div2');
		div1.mousedown(function(e){
			var disX = e.pageX - div.offset().left;
			var disY = e.pageY - div.offset().top;
			$(document).bind('mousemove',function(e){
				var divW = div.width();
				var divH = div.height();
				var dsW = boxW - divW;
				var dsH = boxH - divH;
				var l = e.pageX - disX - boxX;
				var t = e.pageY - disY - boxY;
				if( l < 0 ) l = 0;
				if( t < 0 ) t = 0;
				if( l > dsW ) l = dsW;
				if( t > dsH ) t = dsH;
				div.css({
					left:l+'px',
					top:t+'px'
				});
			}).mouseup(function(){
				$(document).unbind('mousemove');
			});
		});
		div2.mousedown(function(e){
			var disXr = e.pageX - div.offset().left;
			var disYr = e.pageY - div.offset().top;
			var divWr = div.width();
			var divHr = div.height();
			var disR = boxW - div.offset().left - divWr + boxX;
			var disB = boxH - div.offset().top - divHr + boxY;
			$(document).bind('mousemove',function(e){
				var lr = e.pageX - disXr - div.offset().left;
				var tr = e.pageY - disYr - div.offset().top;
				divWr1 = divWr + lr;
				divHr1 = divHr + tr;
				if( divWr1 < 50 ) divWr1 = 50;
				if( divHr1 < 20 ) divHr1 = 20;
				if( (divWr1 + div.offset().left) > (boxW + boxX) ) divWr1 = boxW + boxX - div.offset().left;
				if( (divHr1 + div.offset().top) > (boxH + boxY) ) divHr1 = boxH + boxY - div.offset().top;
				div.css({
					width:divWr1,
					height:divHr1
				});
			}).mouseup(function(){
				$(document).unbind('mousemove');
//				var dot1L = div.offset().left - boxX;
//				var dot1T = div.offset().top - boxY;
//				var dot2L = dot1L + divWr1;
//				var dot2T = dot1T + divHr1;
//				alert(dot1L+','+dot1T+','+dot2L+','+dot2T);
			})
		})
	});
}
//添加热点
$('.add').live('click',function(){
	var index1 = $(this).parents('.main_page').find('.div').length + 1;
	var suffix = $(this).parents('.addBox').attr('suffix');
	if(!isNotBlank(suffix)){
		suffix = "";
	}
	var appDiv = '<div class="div" rel="'+index1+suffix+'" >'
				+'<div class="div1"></div>'
				+'<div class="div2"></div>'
				+'<div class="del" >X</div>'
				+'<a class="edit order_btn_hot" title="热点'+index1+suffix+'" ></a>'
				+'</div>';
	var appHot = '<div class="inpHot" style="display:none;" rel="'+index1+suffix+'" >'
				+'<input type="text" class="marR10 inpHotName" name="inpHotName" id="inpHotName'+index1+suffix+'" rel="'+index1+suffix+'" />'
				+'<input type="text" name="inpHotUrl" id="inpHotUrl'+index1+suffix+'" class="inpHotUrl" value="http://" />'
				+'</div>';
	$(this).parents('.main_page').find(".box").append(appDiv);
	$(this).parents('.main_page').append(appHot);
	bind_map_event($(this).parents('.main_page'));
})
//模板复制
/*$('.copy').live('click',function(){
	var newimgmap = $(this).parents('.main_page').clone(true).insertAfter('.main_page:last');
	newimgmap.find('.div').remove();
	newimgmap.find('.inpHot').remove();
});*/
//删除热点
$('.del').live('click',function(){
	var main_page = $(this).parents('.main_page');
	var suffix = $(this).parents('.addBox').attr('suffix');
	if(!isNotBlank(suffix)){
		suffix = "";
	}
	var index = main_page.find('.del').index(this);
	main_page.find('.inpHot').eq(index).remove();
	$(this).parents('.div').remove();
	var index2 = 1;
	main_page.find('.inpHot').each(function(){
		$(this).find('.inpHotName').attr('id','inpHotName'+index2+suffix);
		$(this).find('.inpHotUrl').attr('id','inpHotUrl'+index2+suffix);
		$(this).attr('rel',index2+suffix);
		index2++;
	})
	var index3 = 1;
	main_page.find('.div').each(function(){
		$(this).find('.order_btn_hot').attr('title','热点'+index3);
		$(this).attr('rel',index3+suffix);
		index3++;
	})
})
//模板删除	
/*$('.delmb').live('click',function(){
	if($('.main_page').length==1){
		alert("不能全部删除");
		return;
	}		
	$(this).parents('.main_page').remove();
}) */
function openUploadWindow(obj){
	curUpload = $(obj).parents('.box').find('.imgtest');
	$.openDOMWindow({windowSourceID:"#XUELI_ENC_UPLOAD_TEMPLATE", height:156, width:591});
}	
var FGetImg = function(sUrl, fCallback)  
{     
    var img = new Image();  
    img.src = sUrl + '?t=' + Math.random();    //IE下，ajax会缓存，导致onreadystatechange函数没有被触发，所以需要加一个随机数   
    if (navigator.userAgent.indexOf("MSIE")>0)  
    {  
        img.onreadystatechange = function()  
        {  
            if (this.readyState=="loaded" || this.readyState=="complete")  
            {  
                fCallback({width:img.width, height:img.height, url:sUrl});  
            }  
        };  
    }else if (navigator.userAgent.indexOf("Firefox")>0 
    		|| navigator.userAgent.indexOf("Safari")>0 
    		|| navigator.userAgent.indexOf("Camino")>0 
    		|| navigator.userAgent.indexOf("isChrome")>0)
    {  
        img.onload = function()  
        {  
            fCallback({width:img.width, height:img.height, url:sUrl});  
        };  
    }  
    else  
    {  
        fCallback({width:img.width, height:img.height, url:sUrl});  
    }  
}
function saveFile(thobj){
	var jqFormObj = $(thobj).parents("form");
	if(checkPhoto(jqFormObj)){
		jqFormObj.ajaxSubmit({
			dataType: 'json',
			success : function(data) {
				msg = eval(data);
				if(msg.status!="200"){
					alert(msg.message);
					return;
				}
				var ar = msg.param.split("@@");
				var src = ar[0];
				var id  = ar[1];
				curUpload.attr('alt',id);
				var zq_width = curUpload.attr('width');
				FGetImg(src,function(img){
					var now_width = img.width;
					var now_height = img.height;
					var fz = now_width/zq_width;
					var after_height = now_height/fz;
					curUpload.attr('src',src);
					curUpload.addClass("hasUpload");
					var _img_offset = curUpload.offset();
					var _img_conrainer = curUpload.parent();
					var _img_conrainer_offset = _img_conrainer.offset();	
					setTimeout(function(){
						_img_conrainer.css({
							top: _img_offset.top - _img_conrainer_offset.top,
							//left: _img_offset.left - _img_conrainer_offset.left,
							height:after_height,
							border:'1px solid #ddd'
						});		        				
					},2000)								
				
				})
				$.closeDOMWindow(); 
			}             
		});
	}
}

function isRightUploadType(photoPath){
	var photoPattern = photoPath.substring(photoPath.lastIndexOf(".")+1,photoPath.length);
	if(!isFitPattern(photoPattern)){
		alert("上传照片格式不对!");
		return false;
	}
	return true;
}
function isFitPattern(photoPattern){
	var arrPattern = new Array("JPG","PNG","JPEG");
	for(var p=0;p<arrPattern.length;p++){
		if(arrPattern[p]==photoPattern.toUpperCase()){
			return true;
		}else{
			continue;
		}
	}
	return false;
}		
function checkPhoto(jqForm){
	var expr = ":input[type=file]";
	var photoEnc = $(expr,jqForm).val();
	return isRightUploadType(photoEnc);
}