<!-- 文件上传 -->
<script type="text/javascript" src="${ctx.contextPath}/js/ace/ace.min.js"></script>
<script type="text/javascript" src="${ctx.contextPath}/js/ace/ace-elements.min.js"></script>

<script type="text/javascript">

// 表单file文件校验（动态更改name属性）
function form_fileValid(id) {
	$("#" + id + " input[type=file]").each(function(){
		if (isEmpty($(this).val())){
			$(this).removeAttr("name");
		} else {
			if (isEmpty($(this).attr("name"))) {
				$(this).attr("name", $(this).attr("id"));
			}
		}
	})
}

// 新增div初始化
function fileAddInit(divObj, name, inputId, accept) {
	if (accept == undefined) {
		accept = ".doc,.docx,.rtf,.xls,.xlsx,.ppt,.pptx,.pdf,.wps";
	}
	$(divObj).empty().append('<input type="file" name="' + name + '" id="'+ inputId +'" accept='+ accept +'>');
	initFileUpload(divObj, accept);
}

// 初始化文件上传框
function initFileUpload(divObj, accept) {
	var acceptArr = accept.split(",");
	var allowExt = [];
	for (var i=0; i<acceptArr.length; i++) {
		allowExt.push(acceptArr[i].replace(",", ""));
	}
	$(divObj).find("input[type=file]").ace_file_input({
		style:'well',
		btn_choose:"选择文件",
	    btn_change:null,
		droppable:false,
		thumbnail:false,//| true | large
		allowExt: allowExt,
		before_remove : function() {
			return true;
		},
		preview_error : function(filename, error_code) {
			
		}
	}).on('change', function(){
		
	});
};

//文件编辑时div初始化
function fileEditInit(divObj, fileUrl, name, inputId, accept) {
	if (accept == undefined) {
		accept = ".doc,.docx,.rtf,.xls,.xlsx,.ppt,.pptx,.pdf,.wps";
	}
	if (isEmpty(fileUrl)) {
		fileAddInit(divObj, name, inputId, accept);
	} else {
		var html = '<div id="file_edit_div">\
						<div id="file_edit_file" style="display:none;">\
							<div>\
								<input type="file" name="' + name + '" id="'+ inputId +'" accept="'+ accept +'">\
							</div>\
							<div class="modal-footer" style="padding: 5px">\
								<button type="button" class="btn btn-primary" onClick="javascript:file_edit_div_tab(this);">返回</button>\
							</div>\
						</div>\
						<div id="file_edit_pic">\
							<span class="profile-picture">\
								<button type="button" class="btn btn-primary" onClick="javascript:file_edit_div_tab(this);">重新上传</button>\
								<a type="button" class="btn btn-primary" href="'+fileUrl+'" target="_blank">查看</a>\
							</span>\
						</div>\
					</dvi>';

		$(divObj).empty().append(html);
		initFileUpload(divObj, accept);
	}
}

//文档文件编辑时div初始化
function wordFileEditInit(divObj, fileUrl, name, inputId, accept) {
	if (accept == undefined) {
		accept = ".doc,.docx,.rtf,.xls,.xlsx,.ppt,.pptx,.pdf,.wps";
	}
	if (isEmpty(fileUrl)) {
		fileAddInit(divObj, name, inputId);
	} else {
		var html = '<div id="file_edit_div">\
						<div id="file_edit_file" style="display:none;">\
							<div>\
								<input type="file" name="' + name + '" id="'+ inputId +'" accept=".doc,.docx,.rtf,.xls,.xlsx,.ppt,.pptx,.pdf,.wps">\
							</div>\
							<div class="modal-footer" style="padding: 5px">\
								<button type="button" class="btn btn-primary" onClick="javascript:file_edit_div_tab(this);">返回</button>\
							</div>\
						</div>\
						<div id="file_edit_pic">\
							<span class="profile-picture">\
								<button type="button" class="btn btn-primary" onClick="javascript:file_edit_div_tab(this);">重新上传</button>\
								<a type="button" class="btn btn-primary" href="https://ow365.cn/?i=13958&ssl=1&furl='+fileUrl+'" target="_blank">查看</a>\
							</span>\
						</div>\
					</dvi>';

		$(divObj).empty().append(html);
		initFileUpload(divObj, accept);
	}
}

// 图片编辑时div切换
function file_edit_div_tab(obj) {
	var div = $(obj).parents("#file_edit_div");
	var fileDiv = div.find("#file_edit_file");
	var filePic = div.find("#file_edit_pic");
	if (filePic.is(":hidden")) {
		filePic.show();
		fileDiv.hide();
		
		fileDiv.find("input[type=file]").ace_file_input('reset_input');
	} else {
		filePic.hide();
		fileDiv.show();
	}
}
	
// 预览文件
function openUrl(url){
	window.open('http://ow365.cn/?i=13958&ssl=1&furl='+url)
}

</script>

