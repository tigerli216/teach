
/**
 * 分页2012-12-7
*/
var pagination =
{
	getPaginationHtml: function(url,currentPage,count,divid)
	{  
		
	   if(currentPage<1){
	       currentPage=1;
	   }
	   if(currentPage>count){
		   currentPage=count;
	   }
		if (url == null || url == "" || url.length == 0)
			return "";
		if (count <= 0){
			return "";
		}
		var s = "";
		s+= '<img src="${cdnx}/images/pg_first_01.gif" onclick=\"'+ url.replace("{0}",1)+ '\" style="cursor:pointer;" title="首页"/>';
		if (currentPage > 1){
			s += "<a class=\"rprev\" href=\""+ url.replace("{0}",currentPage - 1)+ "\" >上一页</a>";
		}else{
			s +="<span class=\"prev\">上一页</span>";
		}
		s+=" <span class=\"currentp\">"+currentPage+"</span>/<span class=\"count\">"+count+"</span> ";
		if (currentPage < count){
			s += "<a class=\"rnext\" href=\""+ url.replace("{0}",currentPage + 1)+ "\" >下一页</a>";
		}else{
			s+="<span class=\"nex\">下一页</span>";
		}
		if(count>1){
			var v=currentPage+1;
			if(v>count){
				v=count;
			}
			v="";
			s+=" <span >去第 </span><input id=\"goPage\" name=\"goPage\" size=\"1\" onclick=\"document.getElementById('gopagediv').style.display = '';\" onkeydown=\"return true;\" value=\""+v+"\"/><span id=\"gopagediv\" style=\"display:none\"><a class=\"gopageok\"  href=\"#\" onclick=\" var reg = /^\\d+$/;var obj=document.getElementById('goPage');if(obj.value.match(reg)==null || parseInt(obj.value)<1 || parseInt(obj.value)>"+count+"){ obj.focus();return false;} var nurl='"+url+"'.replace('{0}',obj.value); this.href=nurl;  return true;\">确定</a></span> <span >页</span>";
		}
		if(divid!=null && divid!=''){
			 set_innerHTML(divid,s);
		}else{
			set_innerHTML("pagination",s);
		}
	}
	
}

		
