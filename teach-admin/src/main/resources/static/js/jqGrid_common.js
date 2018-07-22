/**
 * 加载JqGrid
 * 
 * @returns
 */
function loadJqGrid(gridParam) {
	// 基本参数
	var baseGridParam = {
		mtype : "POST",
		datatype : "json",
		multiselect : false,
		multiboxonly : false,
		rowNum : 10,
		rowList : [ 5, 10, 20, 50 ],
		jqGridLoadComplete:function(){}
	};

	$.extend(baseGridParam, gridParam);

	createJqGrid(baseGridParam);
}

var tableObjArr = [];
var tableIdArr = [];
/**
 * 创建JqGrid
 * 
 * @param gridParam
 */
function createJqGrid(gridParam) {
	var scripts = [null,null];

	var subgrid_data = [];
	var grid_selector = "#" + gridParam.gridId;
	var pager_selector = "#" + gridParam.pagerId;
	
	ace.load_ajax_scripts(scripts, function() {
		// table的宽度
		var tableWidth = $(grid_selector).parent();
		
		if (!(tableWidth in tableObjArr)) {
			tableObjArr.push(tableWidth);
			tableIdArr.push(gridParam.gridId);
		}
		
		// 设置jGrid的宽度
		$(window).on('resize.jqGrid', function() {
			for (var i=0; i<tableObjArr.length; i++) {
				var width = 0;
				if (tableObjArr[i] != undefined && tableObjArr[i].length > 0) {
					width = tableObjArr[i].width();
				}
				if(width > 0){
					$("#" + tableIdArr[i]).setGridWidth(width);
				}
			}
		});
		//处理对话框弹出时jqGrid宽度不随对话框宽度自动调整
		$(window).on('show.bs.modal', function(e) {
			$(e.target).css("display", "block");
			for (var i=0; i<tableObjArr.length; i++) {
				var width = 0;
				if (tableObjArr[i] != undefined && tableObjArr[i].length > 0) {
					width = tableObjArr[i].width();
				}
				if(width > 0){
					$("#" + tableIdArr[i]).setGridWidth(width);
				}
			}
			$(e.target).css("display", "none");
		});

		jQuery(grid_selector).jqGrid({
			mtype : gridParam.mtype,
			url : gridParam.url,
			datatype : gridParam.datatype,
			height : "auto",
			colNames : gridParam.colNames,
			colModel : gridParam.colModel,
			viewrecords : true,
			altRows : true,
			forceFit : true,
			rowNum : gridParam.rowNum,
			rowList : gridParam.rowList,
			pager : pager_selector,
			viewrecords : true,
			autowidth : true,
			multiselect : gridParam.multiselect,
			multiboxonly : gridParam.multiboxonly,
			onSelectRow : gridParam.onSelectRow,
			ondblClickRow : gridParam.ondblClickRow,
			beforeSelectRow : gridParam.beforeSelectRow,
			postData : gridParam.postData,
			onPaging : gridParam.onPaging,
			/**
			 * 加载异常处理
			 */
			loadError:function(xhr,st,err){
				// 异常处理
				errorProc(xhr.status);
			},
			loadComplete : function() {
				var table = this;
				setTimeout(function() {
					updatePagerIcons(table);
				}, 0);

				// 表头字体居中
				$('th div').css('text-align', 'center');
				if (gridParam.hidenCheckAll){
					$('#cb_'+ gridParam.gridId).hide();
				}
				// jqGrid加载完成后执行
				jqGridLoadComplete();
				
				// 执行完后调用
				gridParam.jqGridLoadComplete();
				
				// trigger window resize to make the grid get the correct size
				$(window).triggerHandler('resize.jqGrid');
				
				// 调用jquery.resize插件（ajax_filter.js）
				$("#gbox_" + gridParam.gridId).parent().on("resize", function(){
				});
			}
			
		});
	});

}

//jqGrid加载完成后执行
function jqGridLoadComplete(){
	
}

/**
 * 设置上一页下一页等显示图标
 * 
 * @param table
 */
function updatePagerIcons(table) {
	var replacement = {
		'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
		'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
		'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
		'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
	};
	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon')
			.each(function() {
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

				if ($class in replacement)
					icon.attr('class', 'ui-icon ' + replacement[$class]);
			})
}
