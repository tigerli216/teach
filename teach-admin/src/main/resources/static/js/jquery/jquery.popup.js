// JavaScript Document
	
;(function($){
	$.fn.popup=function(settings){
		settings=$.extend({
			mask:'maskPop',  //遮罩层的样式类
			popMain:'popMain', //弹出层的样式类
			closePop:'closePop' //关闭按钮的样式类
		},settings);
		//$('body,html').height(window.innerHeight).css({overflow:'hidden'});
		this.siblings(settings.maskObj).add(this).show();
		this.addClass(settings.popMain);
		this.siblings(settings.maskObj).addClass(settings.mask);
		this.css({marginTop:-settings.marginTop+'px'});
		this.find(settings.closeObj).addClass(settings.closePop);
		this.find(settings.closeObj).click(function(){
		//$('body,html').height('auto');
			$(this).parent().css({display:'none'});
			$(this).parent().siblings(settings.maskObj).css({display:'none'});
			return false;
		})
		return this;
	}	
		
})(jQuery);

//调用：$('#popMain')弹出层的id值；height:'520'弹出层的高度;marginTop:'260'弹出层高度的一半；maskObj:'#mask' 遮罩层的id值；closeObj:'#closePop'关闭按钮的id值
//	$(function(){
//		$('#popMain').popup({height:520,marginTop:260,maskObj:'#mask',closeObj:'closeBtn'}); 
//	})
	
