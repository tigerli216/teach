// JavaScript Document
// JavaScript Document
/*li绝对定位，控制索引号为i的显示，其前者i-1隐藏即可实现轮换效果，不再需要给目标图片添加替补图片。*/

	//调用方式： objLi.imgSlide({},objSpan,objBtn,objBtnR)
	         	//var banner_type=3; //1为透明度轮换；2为向上滚动；3：向下滚动；4：向左滚动；5：向右滚动。banner_type需要单独在插件内修改
			  //objLi为滚动单位；objSpan为指示显示的iconspan;objBtn为两个左右按钮；objBtnR为右侧按钮。
			  
(function($){
	$.fn.imgSlide=function(settings){
		settings=$.extend({
			banner_type:4,
			speed:500,
			timeout:3000,
			spanBg:'spanBg', //指示icon的背景类名
			spanBgNow:'spanBgNow',
			liOpacity:30
		},settings);
		//获取相关元素。
		$('.spanWrp span',this).addClass('spanScroll');
		var $li=$('li',this);  //在调用对象内部的li元素
		var objSpan=$('.spanScroll',this);
		var objBtn=$('.scrollBtn',this);
		var objBtnR=$('.scrollBtnR',this);
		
		var baseW=$li.first().width();
		var baseH=$li.first().height();
		var i=1;   //下一个要显示的序列号。
		var t=0; 
		$li.eq(0).css({zIndex:2,opacity:1,filter:'alpha(opacity=100)'});
		objSpan.eq(0).addClass(settings.spanBgNow).siblings().addClass(settings.spanBg);
	
		function scroller(){
			settings.banner_type=4;
			var numLi=$li.length;
			//alert(numLi); //5
			//alert()
			banner(objSpan.eq(i),i==0?numLi-1:i-1);
			if(i>=numLi-1){    
				i=0;    
				}else{i++;}		
			}
		
		var autoPlay=function(){
			t=setInterval(scroller,settings.timeout);   
		}
		var stopPlay=function(){
			clearTimeout(t);	
		}
		
	function banner(obj,prev){
		//console.log(!_this.siblings().is(':animated'))
		//if(!_this.is(':animated')){
		if(settings.banner_type==1){  
		$li.eq(prev).animate({opacity:0,filter:'alpha(opacity=0)'},settings.speed);
		$li.eq(obj.index()).animate({opacity:1,filter:'alpha(opacity=100)'},settings.speed);
		}else if(settings.banner_type==2){  
		$li.css({opacity:1,filter:'alpha(opacity=100)'});	
		$li.eq(prev).animate({top:baseH},settings.speed).css({zIndex:1});
		$li.eq(obj.index()).css({top:-baseH}).animate({top:0},settings.speed).css({zIndex:2});
			}else if(settings.banner_type==3){  
		$li.css({opacity:1,filter:'alpha(opacity=100)'});	
		$li.eq(prev).animate({top:-baseH},1000).css({zIndex:1});
		$li.eq(obj.index()).css({top:baseH}).animate({top:0},settings.speed).css({zIndex:2});
			}else if(settings.banner_type==4){  
		$li.css({opacity:1,filter:'alpha(opacity=100)'});	
		$li.eq(prev).animate({left:-baseW},settings.speed).css({zIndex:1});
		$li.eq(obj.index()).css({left:baseW}).animate({left:0},settings.speed).css({zIndex:2});
			}else if(settings.banner_type==5){  
		$li.css({opacity:1,filter:'alpha(opacity=100)'});	
		$li.eq(prev).animate({left:baseW},settings.speed).css({zIndex:1});
		$li.eq(obj.index()).css({left:-baseW}).animate({left:0},settings.speed).css({zIndex:2});
			}
		objSpan.addClass(settings.spanBg).removeClass(settings.spanBgNow);
		obj.addClass(settings.spanBgNow).removeClass(settings.spanBg);
		//}
		}
	
	
	function handThum(){
		$li.each(function(){
			$(this).on('swipeleft',function(e){
				e.preventDefault();
			  e.stopPropagation();
				stopPlay();
						//alert(i);
				if(!$li.is(':animated')){   //当快速点击时，先让上次点击事件的动画执行完，此次点击动画不执行。当上次动画执行完之后点击才有新动画执行。
					//if($(this).is(objBtnR)){
						//console.log( settings.banner_type)  //4
						 settings.banner_type=4;
						banner(objSpan.eq(i),i==0?objSpan.length-1:i-1);
						i++;
						if(i>=$li.length){
							i=0;
						}
				}
				autoPlay();
			})
			$(this).on('swiperight',function(e){
				e.preventDefault();
				  e.stopPropagation();

				stopPlay();
						//alert(i);
				if(!$li.is(':animated')){   //当快速点击时，先让上次点击事件的动画执行完，此次点击动画不执行。当上次动画执行完之后点击才有新动画执行。
						settings.banner_type=5;	
							banner(objSpan.eq(i-2),i-1); 
							i--;
							if(i<0){
								i=objSpan.length-1;
								}
				}
				autoPlay();
				})
			})
		}
	
		autoPlay();
		handThum();
	
	
//	}
	return this;
	}
})(jQuery);

















