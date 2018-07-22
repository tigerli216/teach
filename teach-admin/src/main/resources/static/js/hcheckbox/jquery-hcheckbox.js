;(function($){
	$.fn.hcheckbox=function(options){
		$(':checkbox+label',this).each(function(){
			$(this).addClass('checkbox');
			if($(this).prev().is(':checked')) {
				$(this).addClass("checked");
			}
            if($(this).prev().is(':disabled')) {
            	$(this).css("cursor", "not-allowed");	// 手势
            } else {
            	$(this).on("click", function(event){
            		if(!$(this).prev().is(':checked')){
    				    $(this).addClass("checked");
                        $(this).prev()[0].checked = true;
                    }
                    else{
                        $(this).removeClass('checked');			
                        $(this).prev()[0].checked = false;
                    }
                    event.stopPropagation();
            	})
            }
		}).prev().hide();
	}

    $.fn.hradio = function(options){
        var self = this;
        return $(':radio+label',this).each(function(){
            $(this).addClass('hRadio');
            if($(this).prev().is(":checked")) {
            	$(this).addClass('hRadio_Checked');
            }
            if ($(this).prev().is(':disabled')) {	// 失效
            	if ($(this).parent("a").length == 0) {
            		$(this).css("cursor", "not-allowed");	// 手势
            	}
            } else {
            	$(this).on("click", function(event){
            		var name = $(this).prevAll("input[type=radio]").attr("name");
        			$("body").find("input[type=radio][name='" + name + "']").each(function(){
        				$(this).nextAll("label").removeClass("hRadio_Checked");
        			});
                    if(!$(this).prev().is(':checked')){
        				$(this).addClass("hRadio_Checked");
                        $(this).prev()[0].checked = true;
                    } else {
                    	$(this).prev().removeAttr("checked");
                    }
                       
                    event.stopPropagation();
            	})
            }
        }).prev().hide();
    }

})(jQuery)