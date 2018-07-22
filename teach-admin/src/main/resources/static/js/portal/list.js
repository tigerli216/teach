var _kuan=0;
function resizeWidth(){ 
    var wid=$(window).outerWidth()-40; 
    var num = Math.floor(wid/290);
    var jianju = wid - num*290;
    var mar = Math.floor(jianju/(num-1));
    if(mar>=20){
        mar=20;
    }
    if(num<4){
        num=4;
    }
    $('.hzBox').css({'width':num*(290+mar)-mar,'margin':'0 auto'}); 
    $('.list_check').css({'width':num*(290+mar)-mar,'margin':'0 auto'});
    $('.lbBox').css({'width':num*(290+mar)-mar-40,'margin':'0 auto'}); 
    $('.hzBox ul').css({'width':num*(291+mar),'margin-left':-mar});
    $('.hzBox ul li').css({'margin-left':mar}); 
    _kuan=$('#proj_list').width()*0.42-12; 
    $('.proj_title').css('width',_kuan);
}

$(function(){
    loadMack({off:'on',Limg:1,text:'加载中...',set:1}); 
    $('#search_txt').focus(function(){
        $(this).css("color",'#454545');
    }).blur(function(){
        $(this).css("color",'#999');
    });
    resizeWidth();
    $(window).resize(function(){
        resizeWidth();
    });
    $('#listHz li').on('click',function(){
		
    }); 
    $('#listHz li').on('mouseenter',function(){
        $(this).find('.old_ceng').stop(true,true).fadeIn(200);
    }); 
    $('#listHz li').on('mouseleave',function(){
        $(this).find('.old_ceng').stop(true,true).fadeOut(200);
    }); 
    
    $('#proj_list .tr .first').on('click',function(){

    }); 
    $('.list_tab a').click(function(){
        $(this).addClass('active').siblings('a').removeClass('active');
        $('.listBox').eq($(this).index()).show().siblings('.listBox').hide(); 
        resizeWidth();
    }); 
    
    $('body').click(function(){
        $(".loginBox").fadeOut("slow");
        $('.username').find('.loginMeun').hide();
        $('.username').find('.arrowupt').removeClass('atop');
    });    
});

var page = 1;
var type=1; 
var on=true;
var txt='';
var sort=1;  
var p_s_id = '';
 
function getList(url,page,obj,p_type,search_txt,sort){ 
    var url=url;
    var timestamp = new Date().getTime()
	url+='?type='+p_type; 
    if(search_txt!=''){
        url+='&searchField='+search_txt; 
    }
    var pageTotal = $('#zon_page').val();
    if(pageTotal==0){
        pageTotal=10;
    }
    if($(obj).hasClass('ul_list')){ 	          
            $.ajax({
                type: 'get',
                url: url,
                data: {
                    page: page,
                    sort:sort,
                    t:timestamp
                }, 
                success: function (data) { 
                    $('.loadCon,.loadMack').remove();
                    var list=data.list,len=list.length,html_hz='',html_lb='',_class='',_icon='',state='';
                    if(page > data.pageCount) {
                        page--;
                        return;
                    }
                    $('#zon_page').val(data.pageCount);
                    for(var i=0;i<len;i++){
                        if(list[i].type==1){
                            _class='assess';
                        }else if(list[i].type==2){
                            _class='vote'; 
                        }else if(list[i].type==3){
                            _class='question'; 
                        }else if(list[i].type==4){
                            _class='answer'; 
                        }  
                        html_hz +='<li class="hz '+_class+'" oid="'+list[i].id+'"><a href="javascript:;" class="bt" title="'+list[i].title+'">'+list[i].title+'</a><span class="edit_time">'+list[i].updateTime+'</span></li>';
                    } 
                    var $item = $(html_hz).hide();
                    $('#listHz').append($item);
                    $item.fadeIn(150);                     
                    resizeWidth(); 
                    if($('.hzBox').is(':visible')){ 
                        setTimeout("load()", 300); 
                    }                        
                    on=true;
                },
                error: function () {  
                    loadMack({off:'on',Limg:1,text:'网络繁忙，请稍后再试！',set:1});
                } 
            }); 
    }else if($(obj).hasClass('table_list')){ 
            $.ajax({
                type: 'get',
                url: url,
                data: {
                    page: page,
                    time_order:sort,
                    t:timestamp
                }, 
                success: function (data) {    
                    var list=data.list,len=list.length,html_hz='',html_lb='',_class='',_icon='',state='',leixing='',_sel='';
                    $('#zon_page').val(data.pageCount); 
                    for(var i=0;i<len;i++){ 
                        if(list[i].type==1){
                            leixing='评价';
                        }else if(list[i].type==2){
                            leixing='投票'; 
                        }else if(list[i].type==3){
                            leixing='问卷'; 
                        }else if(list[i].type==4){
                            leixing='试题'; 
                        } 
                        html_lb+='<tr oldId="'+list[i].old_id+'" status="'+list[i].status+'" class="tr '+_class+'"><td class="first" width="42%" oid="'+list[i].id+'"><span class="proj_title" style="width:'+_kuan+'px" title="'+list[i].title+'">'+list[i].title+'</span></td><td class="tl tdcj">'+list[i].createTime+'</td><td class="tl">'+list[i].updateTime+'</td><td class="last" type="'+leixing+'"><a onclick="project_design_confirm(this);return false;" pid="'+list[i].id+'" fid="'+list[i].project_func_id+'" class="editor btn"></a><a href="http://www.wenjuan.com/collect/urllink/'+list[i].id+'" class="collect_a"></a><a href="http://www.wenjuan.com/static/js/report/basic_chart/'+list[i].id+'?pid='+list[i].id+'" class="analysis"></a><a href="http://www.wenjuan.com/s/'+list[i].short_id+'?test=1" target="_blank" class="a">预览</a><a href="javascript:;" pid="'+list[i].id+'" fid="'+list[i].project_func_id+'" class="a copysurvey2" data-title="'+list[i].title+'">复制</a><a href="javascript:;" id="cleanup_'+list[i].id+'" pid="'+list[i].id+'" class="color a">清空</a><a href="javascript:;" pid="'+list[i].id+'" fid="'+list[i].project_func_id+'" class="a deletebtn">删除</a></td></tr>';
                    } 
                    var _type=$('#state_sel').find("option:selected").val();
                    if(_type=="全部"){
                        _type="";
                    }
                    if(html_lb==''){
                        if(search_txt=="" || search_txt=="输入标题搜索"){
//                         $('#proj_list').html('<tr style="border-bottom:none;"><td colspan="6"><p style="text-align:center; padding:150px 0px; font-size:16px;">暂无项目，点击<a href="../../new.htm" style="color:#53a4f4;">&nbsp;新建</a></p></td></tr>'); 
                        }else{
                           $('#proj_list').html('<tr style="border-bottom:none;"><td colspan="6"><p style="text-align:center; padding:150px 0px; font-size:16px;">对不起，没有找到你想要的结果</p></td></tr>');  
                       } 
                    }else{
                        $('#proj_list').html('<tr class="thead_td"><td width="42%" class="first">'+_type+'标题</td><td class="tl tdcj">创建时间</td><td class="tl">编辑时间</td><td class="last">操作</td></tr>'+html_lb); 
                    }
                    $('#proj_list .tr').each(function(){
                        if($(this).find('.cq_sdsj').text()==0){
                            $(this).find('.last .color').addClass('color_gray').removeClass('color');
                        }
                        if($(this).attr('status')==2){
                            $(this).find('.editor').attr("onclick",'');
                            $(this).find('.editor,.collect_a').addClass('end').attr("href",'javascript:;')
                        } 
                    });
                    $(".pagenum").createPage({
                        pageCount:data.totalPage,
                        current:page,
                        backFn:function(p){
                            loadMack({off:'on',Limg:1,text:'加载中...',set:1});
                           if(on){
                            on=false;
                            page=p;
                            loaded(p);
                           }
                        } 
                    }); 
                    if(data.totalPage<=1){
                        $('.pagenum').hide();
                    } 
                    on=true;   
                    $('.loadCon,.loadMack').remove();
                },
                error: function () {
                    loadMack({off:'on',Limg:1,text:'网络繁忙，请稍后再试！',set:1});
                } 
            });
    } 
}  

function loaded(page){
    var type=$("#state_sel").find("option:selected").attr("p_type");

    if($('#search_txt').val()!='' && $('#search_txt').val()!='输入标题搜索'){
        txt= encodeURIComponent($('#search_txt').val());
    }else{
        txt='';
    }
    sort=$('#sort_sel').find("option:selected").attr("sort");
    if($('.hzBox').is(':visible')){
        getList('/ocp/portal/getPaging/',page,'#listHz',type,txt,sort);
    }
    if($('.lbBox').is(':visible')){
        getList('/ocp/portal/getPaging/',page,'#proj_list',type,txt,sort); 
    }
}

function install(){ 
    loadMack({off:'on',Limg:1,text:'加载中...',set:0});
    page=1;
    loaded(1);
}

function load(){
    var top=0;
    if($('#listHz li').length>0){
        top=$('#listHz li:last').offset().top
    }else{
        top=$('#listHz').offset().top
    }
    if(top<$(window).height()){
        page++;
        loaded(page)
    } 
}

$(function(){    
    $(document).click(function(){
        $('.dropdown .dropselectbox ul').hide();
    });
    $('.dropdown').on('click',function(e){  
         e.stopPropagation();
    }); 
    $(".list_sel select").select(); 
    $('#search_btn').click(function(){ 
        install();
    });
    $('#search_txt').keyup(function(e){
        if(e.keyCode==13){ 
            install();
        }  
    }); 
    //select
    $('#state_sel,#sort_sel').change(function(){  
    	$('#listHz').html('');
        install();
    });
    //tab
    $('.list_tab a').click(function(){ 
    	$('#listHz').html('');
        install(); 
    });   
    $(window).scroll(function(){
        var type=$("#state_sel").find("option:selected").attr("p_type"); 
        if($('.hzBox').is(':visible')){
            var top=$(window).scrollTop()+$(window).height();
            var lastTop=0;
            if($('#listHz li').length>0){
                lastTop=$('#listHz li:last').offset().top;
            }else{
                lastTop=$('#listHz').offset().top;
            } 
            if( lastTop <= top ){
                if(on){
                    on=false;
                    page++;
                    loaded(page);
                }
            }
        } 
    }); 
    
    $('.list_tab a').get(0).click();
});
