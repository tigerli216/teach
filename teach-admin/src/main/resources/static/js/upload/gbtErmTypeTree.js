var curObjId;

var settingType = {
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onClick : onClickErmType
	}
};

function showErmTypeTree(inputSelectObjId, xurl) {
	//alert(inputSelectObjId);
	curObjId = inputSelectObjId;
	var xobj = $("#" + inputSelectObjId);
	var xojbOffset = $("#" + inputSelectObjId).offset();
	//alert(xobj);
	// $("#menuContent").remove();
	$("body").append("<div id='menuContent' class='menuContent'></div>");
	$("#menuContent").attr(
			"style",
			"display: none; position: absolute; z-index: 1000; background: #f0f6e4;width:"
					+ xobj.width() + "px");
	$("#menuContent").append("<ul id='typeTree'></ul>");
	$("#typeTree").attr("class", "ztree");
	$("#typeTree")
			.attr("sytle", "margin-top: 0; width: " + xobj.width() + "px;");

	htmlobj = $.ajax({
		url : xurl + "&t=" + Math.random(),
		async : false
	});
	var str = htmlobj.responseText;
	var zNodes = eval('(' + str + ')');
	$.fn.zTree.init($("#typeTree"), settingType, zNodes);
	$("#menuContent").css({
		left : xojbOffset.left + "px",
		top : xojbOffset.top + xobj.outerHeight() + "px"
	}).slideDown("fast");
	$("body").bind("click", onBodyDownType);
}

function onClickErmType(event, treeId, treeNode, clickFlag) {
	//alert(treeNode);
	var zTree = $.fn.zTree.getZTreeObj("typeTree"), v = "";
	var tnode = treeNode;
	v += treeNode.name;
	while (true) {
		tnode = tnode.getParentNode();
		if (tnode == undefined || tnode == null || tnode.id == tnode.pId
				|| tnode.pId == 0) {
			break;
		}
		v = tnode.name + ">>" + v;
	}
	//alert(v);
	var selectid=document.getElementById(curObjId);
    selectid.options.length=0;
    selectid[0]=new Option(v,treeNode.id);
}

function onBodyDownType(event) {
	if (!(event.target.id.indexOf(curObjId) >= 0 || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
		$("#menuContent").fadeOut("fast");
	}
}
