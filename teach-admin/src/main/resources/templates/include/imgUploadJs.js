
<!-- 文件上传 -->
<script type="text/javascript" src="${ctx.contextPath}/js/ace/ace.min.js"></script>
<script type="text/javascript" src="${ctx.contextPath}/js/ace/ace-elements.min.js"></script>

<script type="text/javascript">

	//弹窗模块的显示隐藏切换（show控制是否显示）
	function modalClose(id, show) {
		var isShow = true;	// 默认是显示
		
		var modal = $("#" + id);
		if (show == false) {	// 传入show则根据该字段判断显示隐藏
			isShow = false;
		} else if (show == true) {
			isShow = true;
		} else {
			if (!modal.is(":hidden")) {
				isShow = false;
			}
		}
		if (isShow) {
			modal.modal("show");
		} else {
			modal.modal("hide");
		}
	}
	
	// 提示内容初始化
	function tipInit(content, method) {
		var obj = $("#modal_common_tip");
		if (obj.length == 0) {
			var html = '<div id="modal_common_tip" class="modal">\
				<div class="modal-dialog" style="width: 20%; margin: 80px auto;">\
				<div class="modal-content">\
					<div class="modal-body step-content" id="modal-step-contents1">\
						<h4 class="blue bigger" id="tip_content_h">'+ content +'</h4>\
					</div>\
					<div class="modal-footer wizard-actions">\
						<button id="tip_method_btn" class="btn btn-sm btn-primary"\
							data-dismiss="modal" type="button"';
			if (!isEmpty(method)) {
				html += 'onClick="javascript:'+ method +';"';
			}
			html += '>\
						<i class="ace-icon fa fa-check bigger-110"></i> 确定\
						</button>\
						<button class="btn btn-danger btn-sm pull-left" data-dismiss="modal">\
							<i class="ace-icon fa fa-times"></i> 取消\
						</button>\
					</div>\
				</div>\
			</div>';
			$("body").append(html);
		}
		obj.find("#tip_content_h").html(content);
		obj.find("#tip_method_btn").attr("onClick", "javascript:" + method);
		modalClose("modal_common_tip", true);
	}
	
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
	
	// 图片新增div初始化
	function imgAddInit(divObj, name, inputId) {
		$(divObj).empty().append('<input type="file" name="' + name + '" id="'+ inputId +'" accept="image/*">');
		initImgFileDropzone(divObj);
	}
	
	// 图片编辑时div初始化
	function imgEditInit(divObj, imgUrl, name, inputId) {
		if (isEmpty(imgUrl)) {
			imgAddInit(divObj, name, inputId);
		} else {
			var adminUrl = "";
			var titlePage = adminUrl + imgUrl;
			
			var html = '<div id="img_edit_div">\
							<div id="img_edit_file" style="display:none;">\
								<div>\
									<input type="file" name="' + name + '" id="'+ inputId +'" accept="image/*">\
								</div>\
								<div class="center">\
									<button class="btn btn-grey btn-xs" type="button" onClick="javascript:img_edit_div_tab(this);">\
										<i class="ace-icon fa fa-times"></i>\
									</button>\
								</div>\
							</div>\
							<div id="img_edit_pic">\
								<span class="profile-picture">\
									<img src="' + titlePage + '" style="width:100%; max-width:200px; max-height:200px;cursor:pointer;"\
										onClick="javascript:img_edit_div_tab(this);" onerror="this.src=\'/images/error/error.jpg\'">\
								</span>\
							</div>\
						</dvi>';
	
			$(divObj).empty().append(html);
			initImgFileDropzone(divObj);
		}
	}
	
	// 图片编辑时div切换
	function img_edit_div_tab(obj) {
		var div = $(obj).parents("#img_edit_div");
		var fileDiv = div.find("#img_edit_file");
		var imgDiv = div.find("#img_edit_pic");
		if (imgDiv.is(":hidden")) {
			imgDiv.show();
			fileDiv.hide();
			
			fileDiv.find("input[type=file]").ace_file_input('reset_input');
		} else {
			imgDiv.hide();
			fileDiv.show();
		}
	}

	// 初始化图片上传框
 	function initImgFileDropzone(divObj) {
 		$(divObj).find("input[type=file]").ace_file_input({
			style:'well',
			btn_choose:'请点击添加图片',
			btn_change:null,
			no_icon : 'ace-icon fa fa-picture-o',
			droppable:false,
			thumbnail:'large',
			allowExt: ['jpg', 'jpeg', 'png', 'gif'],
			allowMime: ['image/jpg', 'image/jpeg', 'image/png', 'image/gif'],
			before_remove : function() {
				return true;
			},
			preview_error : function(filename, error_code) {
				
			}
		}).on('change', function(){
			
		});
 	};
 	
 	</script>