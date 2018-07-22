var isMoreChange = false;// 是否多选

/**
 * 设置单选树的样式
 */
function setOneChangeStyle(treeId){
	(function($ , undefined) {
		var $options = {
				'treeId' : treeId,
				'open-icon' : 'ace-icon tree-minus',
				'close-icon' : 'ace-icon tree-plus',
				'selected-icon' : 'ace-icon fa fa-check',
				'unselected-icon' : 'ace-icon fa fa-times',
				'selectable' : true,
				multiSelect : true,
				loadingHTML : '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>'
		}
	
		$.fn.ace_tree = function(options) {
			$options = $.extend({}, $options, options)
			this.each(function() {
				var $this = $(this);
				// html属性菜单的样式
				var treeHtml = '<div class="tree-folder" style="display:none;">'+
									'<div class="tree-folder-header">'+
										'<i class="'+ ace.vars['icon'] + $options['close-icon']+'"></i>'+
										'<div class="tree-folder-name"></div>'+
										'<div class="tree-id" style="display:none;"></div>'+
									'</div>'+
									'<div class="tree-folder-content" style="display:none"></div>'+
									'<div class="tree-loader" style="display:none"></div>'+
								'</div>'+
								'<div class="tree-item" style="display:none;">'+
									($options['unselected-icon'] == null ? '' : '<i class="'+ ace.vars['icon'] + $options['unselected-icon']+'"></i>')+
									'<div class="tree-item-name"></div>'+
									'<div class="tree-id" style="display:none;"></div>'+
								'</div>';
				$this.html(treeHtml);
				$this.addClass($options['selectable'] == true ? 'tree-selectable' : 'tree-unselectable');
				
				$this.tree($options);
			});
	
			return this;
		}
	})(window.jQuery);
	
	/*
	 * Fuel UX Tree https://github.com/ExactTarget/fuelux
	 * 
	 * Copyright (c) 2012 ExactTarget Licensed under the MIT license.
	 */

	// define(['require','jquery'],function(require) {
	(function($ , undefined) {// ACE

		// var $ = require('jquery');
		var old = $.fn.tree;

		// TREE CONSTRUCTOR AND PROTOTYPE

		var Tree = function (element, options) {
			
			this.$element = $(element);
			this.options = $.extend({}, $.fn.tree.defaults, options);

			this.$element.off('click', '.tree-item');
			this.$element.off('click', '.tree-folder-header');
					
			this.$element.on('click', '.tree-item', $.proxy( function(ev) { this.selectItem(ev.currentTarget); } ,this));
			this.$element.on('click', '.tree-folder-header', $.proxy( function(ev) { this.selectFolder(ev.currentTarget); }, this));

			this.render();
		};

		// TREE PLUGIN DEFINITION

		$.fn.tree = function (option) {
			
			var args = Array.prototype.slice.call( arguments, 1 );
			var methodReturn;

			var $set = this.each(function () {
				var $this   = $( this );
				var data    = $this.data( 'tree' );
				var options = typeof option === 'object' && option;

				if( !data ) {
					$this.data('tree', (data = new Tree( this, options ) ) );
				}
				
				if( typeof option === 'string' ){
					methodReturn = data[ option ].apply( data, args );
				}
			});

			return ( methodReturn === undefined ) ? $set : methodReturn;
		};

		$.fn.tree.defaults = {
			multiSelect: false,
			loadingHTML: '<div>Loading...</div>',
			cacheItems: true
		};

		$.fn.tree.Constructor = Tree;

		$.fn.tree.noConflict = function () {
			$.fn.tree = old;
			return this;
		};
		
		Tree.prototype = {
			constructor: Tree,

			render: function () {
				this.populate(this.$element);
			},

			populate: function ($el) {
				var self = this;
				var $parent = $el.parent();
				var loader = $parent.find('.tree-loader:eq(0)');
				loader.show();
				this.options.dataSource.data($el.data(), function (items) {
					loader.hide();

					$.each( items.data, function(index, value) {
						var $entity;

						if(value.type === "folder") {
							$entity = self.$element.find('.tree-folder:eq(0)').clone().show();
							$entity.find('.tree-folder-name').html(value.name);
							$entity.find('.tree-id').html(value.id);
							$entity.find('.tree-loader').html(self.options.loadingHTML);
							// $entity.find('.tree-folder-header').data(value);
							// ACE
							var header = $entity.find('.tree-folder-header');
							header.data(value);

							if('icon-class' in value)
								header.find('i').addClass(value['icon-class']);
							
							if('additionalParameters' in value
								&& 'item-selected' in value.additionalParameters 
									&& value.additionalParameters['item-selected'] == true) {
									setTimeout(function(){header.trigger('click')}, 0);
							}
						} else if (value.type === "item") {
							
							$entity = self.$element.find('.tree-item:eq(0)').clone().show();
							$entity.find('.tree-item-name').html(value.name);
							$entity.find('.tree-id').html(value.id);
							$entity.data(value);
							
							// ACE
							if('additionalParameters' in value
								&& 'item-selected' in value.additionalParameters 
									&& value.additionalParameters['item-selected'] == true) {
									$entity.addClass ('tree-selected');
									$entity.find('i').removeClass(self.options['unselected-icon']).addClass(self.options['selected-icon']);
									// $entity.closest('.tree-folder-content').show();
							}
						}

						var dataAttributes = value.dataAttributes || [];
						$.each(dataAttributes, function(key, value) {
							switch (key) {
							case 'class':
							case 'classes':
							case 'className':
								$entity.addClass(value);
								break;

							// id, style, data-*
							default:
								$entity.attr(key, value);
								break;
							}
						});

						if($el.hasClass('tree-folder-header')) {
							$parent.find('.tree-folder-content:eq(0)').append($entity);
						} else {
							$el.append($entity);
						}
					});

					// return newly populated folder
					self.$element.trigger('loaded', $parent);
				});
				
				// 加载全部子元素
				if($el.hasClass('tree-selectable')){
					var childFolder = $el.find('.tree-folder').eq(1).find('.tree-folder-header');
					if(childFolder){
						self.populate(childFolder);
					}
				}
				
				if($el.hasClass('tree-folder-header')){
					 $el.parent().find('.tree-folder-content').find('.tree-folder').each(function(){
						self.populate($(this).find('.tree-folder-header'));
					});
				}
				
			},
			lockTree: function() {	// 锁定树
				this.$element.off('click', '.tree-item');
				this.$element.off('click', '.tree-folder-header');
			},
			selectItem: function (el) {
				// 之前选中的元素
				var oldSelectedTreeId = -1;
				// 当前选中的元素
				var nowSelectedTreeId = 0;
				
				if(this.options['selectable'] == false) return;// ACE
				var $el = $(el);
				// 全部选中的元素
				var $all = this.$element.find('.tree-selected');
				var data = [];

				if($all.length){
					oldSelectedTreeId = $all.find('.tree-id').html();
					nowSelectedTreeId = $el.data().id;
					// 如果选中的为同一个菜单则不做处理
					if(oldSelectedTreeId == nowSelectedTreeId){
						return;
					}
				}
				
				
				// 取消选中元素效果
				$all.removeClass('tree-selected').find('i').removeClass('icon-ok').addClass('tree-dot');
				// 全部取消选中
				this.$element.find('.tree-selected-folder').removeClass("tree-selected-folder").removeAttr('style');

				var eventType = 'selected';
				if($el.hasClass('tree-selected')) {
					eventType = 'unselected';
					$el.removeClass('tree-selected');
					$el.find('i').removeClass('icon-ok').addClass('tree-dot');
				} else {
					$el.addClass ('tree-selected');
					$el.find('i').removeClass('tree-dot').addClass('icon-ok');
				}
				
				if(oldSelectedTreeId != nowSelectedTreeId) {
					// 触发选中事件
					this.$element.trigger('selected', [$el.data(),eventType]);
				}
			},

			selectFolder: function (el) {
				// 之前选中的元素
				var oldSelectedTreeId = -1;
				// 当前选中的元素
				var nowSelectedTreeId = 0;
				
				// 全部选中的元素
				var $all = this.$element.find('.tree-selected-folder');
				var $el = $(el);
				var $parent = $el.parent();
				var $treeFolderContent = $parent.find('.tree-folder-content');
				var $treeFolderContentFirstChild = $treeFolderContent.eq(0);
				
				var target = '.' + $.trim(this.options['close-icon'].replace(/\s/g, '.'))// ACE
				
				if($all.length){
					oldSelectedTreeId = $all.find('.tree-id').html();
					nowSelectedTreeId = $el.data().id;
				}
				
				// 全部取消选中
				$all.removeClass("tree-selected-folder").removeAttr('style');
				this.$element.find('.tree-selected').removeClass('tree-selected')
				.find('i').removeClass('icon-ok').addClass('tree-dot');
				
				// 当前添加选中
				$el.addClass("tree-selected-folder").css('background-color','#f0f7fc');
				
				var eventType, classToTarget, classToAdd;
				if ($el.find(target).length) {// ACE
					eventType = 'selected';
					classToTarget = this.options['close-icon'];// ACE
					classToAdd = this.options['open-icon'];// ACE

					$treeFolderContentFirstChild.show();
					if (!$treeFolderContent.children().length) {
						this.populate($el);
					}
				} else {
					target = '.' + $.trim(this.options['open-icon'].replace(/\s/g, '.'))// ACE
					
					eventType = 'unselected';
					classToTarget = this.options['open-icon'];
					classToAdd = this.options['close-icon'];

					$treeFolderContentFirstChild.hide();
					if (!this.options.cacheItems) {
						$treeFolderContentFirstChild.empty();
					}
				}
				
				// ACE
				$parent.find(target).eq(0)
					.removeClass(classToTarget)
					.addClass(classToAdd);

				if($el.data() && oldSelectedTreeId != nowSelectedTreeId) {
					// 触发选中事件
					this.$element.trigger('selected', [$el.data(),eventType]);
				}
				
			},

			selectedItems: function () {
				var $sel = this.$element.find('.tree-selected');
				var data = [];

				$.each($sel, function (index, value) {
					data.push($(value).data());
				});
				return data;
			},

			// collapses open folders
			collapse: function () {
				var cacheItems = this.options.cacheItems;

				// find open folders
				this.$element.find('.icon-folder-open').each(function () {
					// update icon class
					var $this = $(this)
						.removeClass('icon-folder-close icon-folder-open')
						.addClass('icon-folder-close');

					// "close" or empty folder contents
					var $parent = $this.parent().parent();
					var $folder = $parent.children('.tree-folder-content');

					$folder.hide();
					if (!cacheItems) {
						$folder.empty();
					}
				});
			},
			clearItemSelect:function() {
				// 全部选中的元素
				var $all = this.$element.find('.tree-selected');

				// 取消选中元素效果
				$all.removeClass('tree-selected').find('i').removeClass('icon-ok').addClass('tree-dot');
			},
			/**
			 * 根据树的id获取选中的元素(不包括文件夹)
			 * 
			 * @returns
			 */
			getSelectTreeItem: function(){
				var selectEdata = new Array();
				// 查找选中元素
				var selectItem = this.$element.find('.tree-selected');
				
				if(selectItem.hasClass('tree-selected')){
					selectEdata[0] = selectItem.data();
				}else{
					// 查找文件夹
					var selectFolder = this.$element.find('.tree-selected-folder');
					selectEdata[0] = selectFolder.data();
				}
				return selectEdata;
			},
			/**
			 * 伸展和收缩树中的元素
			 * 
			 * @param el
			 *            收缩的元素(如果为null则收缩全部元素)
			 */
			stretch:function(el){
				var $this = this;
				if(el){
					$this.changeTreeStretch(el);
				}else{
					this.$element.find(".tree-folder-header:gt(0)").each(function (){
						$this.changeTreeStretch(this);
					});
				}
			},
			/**
			 * 选中第一个元素
			 */
			selectFirstItem:function(){
				var firstItem = this.$element.find('.tree-folder-header:gt(0)');
				this.changeTreeStretch(firstItem);
				this.selectTreeItem(firstItem.find('.tree-id').html());
			},
			/**
			 * 伸缩第一个元素
			 */
			stretchFirstItem:function(){
				var firstItem = this.$element.find('.tree-folder-header:gt(0)');
				this.changeTreeStretch(firstItem);
				this.selectTreeItem(firstItem.find('.tree-id').html());
			},
			changeTreeStretch: function(el,spreadOrShrink){
				
				var $el = $(el);
				var $parent = $el.parent();
				var $treeFolderContent = $parent.find('.tree-folder-content');
				var $treeFolderContentFirstChild = $treeFolderContent.eq(0);
				
				var target = '.' + $.trim(this.options['close-icon'].replace(/\s/g, '.'))// ACE
				
				var classToTarget, classToAdd;
				if (spreadOrShrink == "spread" || ((spreadOrShrink == null || spreadOrShrink == "" || spreadOrShrink == undefined) && $parent.find(target).length)) {// ACE
					classToTarget = this.options['close-icon'];// ACE
					classToAdd = this.options['open-icon'];// ACE
					$treeFolderContentFirstChild.show();
				} else {
					target = '.' + $.trim(this.options['open-icon'].replace(/\s/g, '.'))// ACE
					
					classToTarget = this.options['open-icon'];
					classToAdd = this.options['close-icon'];
					$treeFolderContentFirstChild.hide();
				}
				
				// ACE
				$parent.find(target).eq(0)
					.removeClass(classToTarget)
					.addClass(classToAdd);
			},
			/**
			 * 展开所有元素
			 */
			spreadAllItem:function(el){
				var $this = this;
				if(el){
					$this.changeTreeStretch(el,"spread");
				}else{
					this.$element.find(".tree-folder-header").each(function (){
						$this.changeTreeStretch(this,"spread");
					});
				}
			},
			/**
			 * 收缩所有元素
			 */
			shrinkAllItem:function(el){
				var $this = this;
				if(el){
					$this.changeTreeStretch(el,"shrink");
				}else{
					this.$element.find(".tree-folder-header").each(function (){
						$this.changeTreeStretch(this,"shrink");
					});
				}
			},
			/**
			 * 
			 * @param selectItems
			 *            选中的元素
			 * @param isClearOtherItem是否清空其他元素的选中
			 */
			selectTreeItem: function(selectItem){
				var self = this;
				if(selectItem != null){
					this.$element.find('.tree-id').each(function(){
						var $this   = $(this);
						if($this.html() == selectItem){
							// 选中
							var $el = $this.parent();
							if($el.hasClass('tree-item')){
								// 全部选中的元素
								var $all = self.$element.find('.tree-selected');

								// 取消选中元素效果
								$all.removeClass('tree-selected').find('i').removeClass('icon-ok').addClass('tree-dot');
								// 全部取消选中
								self.$element.find('.tree-selected-folder').removeClass("tree-selected-folder").removeAttr('style');

								// 选中当前元素
								$el.addClass ('tree-selected');
								$el.find('i').removeClass('tree-dot').addClass('icon-ok');

								// 触发选中事件
								self.$element.trigger('selected', [parent.data,"selected"]);
							}else{
								// 全部选中的元素
								var $all = self.$element.find('.tree-selected-folder');
								
								// 全部取消选中
								$all.removeClass("tree-selected-folder").removeAttr('style');
								self.$element.find('.tree-selected').removeClass('tree-selected')
								.find('i').removeClass('icon-ok').addClass('tree-dot');
								
								// 当前添加选中
								$el.addClass("tree-selected-folder").css('background-color','#f0f7fc');
								
								// 触发选中事件
								self.$element.trigger('selected', [$el.data(),"selected"]);
							}
						}
					});
				}
			}
		};
	// });
	})(window.jQuery);// ACE
}

/**
 * 设置多选树的样式
 */
function setMoreChangeStyle(treeId,treeType){
	(function($ , undefined) {
		// treeType 类型分为两大类 allSelect-parentSelect(全部选中父菜单选中),oneSelect-parentSelect(一个选中父菜单选中)
		if(treeType == null || treeType == undefined){
			treeType = "allSelect-parentSelect";
		}
		
		var $options = {
				'treeId' : treeId,
				'treeType' : treeType,
				'open-icon' : 'ace-icon tree-minus',
				'close-icon' : 'ace-icon tree-plus',
				'selected-icon' : 'ace-icon fa fa-check',
				'unselected-icon' : 'ace-icon fa fa-times',
				'selectable' : true,
				multiSelect : true,
				loadingHTML : '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>'
		}
		
		$.fn.ace_tree = function(options) {
			$options = $.extend({}, $options, options)
			this.each(function() {
				var $this = $(this);
				// html属性菜单的样式(分多选和单选两种样式)
				var treeHtml = '<div class="tree-folder" style="display:none;">'+
									'<div class="tree-folder-header" >'+
										'<i class="stretch '+ ace.vars['icon'] + $options['close-icon']+'"></i>'+
										'<label class="position-relative" style="padding-left: 2px">'+
											'<input type="checkbox" class="ace tree-item-checkbox folder" />'+
											'<span class="lbl"></span>'+
										'</label>'+
										'<div class="tree-folder-name"></div>'+
										'<div class="tree-id" style="display:none;"></div>'+
									'</div>'+
									'<div class="tree-folder-content" style="display:none"></div>'+
									'<div class="tree-loader" style="display:none"></div>'+
								'</div>'+
								'<div class="tree-item" style="display:none;">'+
									'<label class="position-relative">'+
										'<input type="checkbox" class="ace tree-item-checkbox item" />'+
										'<span class="lbl"></span>'+
									'</label>'+
									'<div class="tree-item-name"></div>'+
									'<div class="tree-id" style="display:none;"></div>'+
								'</div>';
				$this.html(treeHtml);
				$this.addClass($options['selectable'] == true ? 'tree-selectable' : 'tree-unselectable');
				
				$this.tree($options);
			});
	
			return this;
		}
	})(window.jQuery);
	
	
	/*
	 * Fuel UX Tree https://github.com/ExactTarget/fuelux
	 * 
	 * Copyright (c) 2012 ExactTarget Licensed under the MIT license.
	 */

	// define(['require','jquery'],function(require) {
	(function($ , undefined) {// ACE

		// var $ = require('jquery');
		var old = $.fn.tree;

		// TREE CONSTRUCTOR AND PROTOTYPE

		var Tree = function (element, options) {
			
			this.$element = $(element);
			this.options = $.extend({}, $.fn.tree.defaults, options);
			
			var $this = $(this);
			
			// 先取消绑定事件
			this.$element.off('click', '.tree-item-checkbox');
			this.$element.off('click', '.stretch');
			
			// 绑定事件
			this.$element.on('click', '.tree-item-checkbox', $.proxy( function(ev) { this.selectItem(ev.currentTarget); } ,this));
			this.$element.on('click', '.stretch', $.proxy( function(ev) { this.stretch(ev.currentTarget); }, this));

			this.render();
		};

		Tree.prototype = {
			constructor: Tree,

			render: function () {
				this.populate(this.$element);
			},

			populate: function ($el) {
				
				var self = this;
				var $parent = $el.parent();
				
				this.options.dataSource.data($el.data(), function (items) {
					$.each( items.data, function(index, value) {
						var $entity;

						if(value.type === "folder") {
							$entity = self.$element.find('.tree-folder:eq(0)').clone().show();
							$entity.find('.tree-folder-name').html(value.name);
							$entity.find('.tree-id').html(value.id);
							$entity.find('.tree-loader').html(self.options.loadingHTML);
							// $entity.find('.tree-folder-header').data(value);
							// ACE
							var header = $entity.find('.tree-folder-header');
							header.data(value);

							if('icon-class' in value){
								header.find('i').addClass(value['icon-class']);
							}
							
						} else if (value.type === "item") {
							$entity = self.$element.find('.tree-item:eq(0)').clone().show();
							$entity.find('.tree-item-name').html(value.name);
							$entity.find('.tree-id').html(value.id);
							$entity.data(value);
							
							// ACE
							if('additionalParameters' in value
								&& 'item-selected' in value.additionalParameters 
									&& value.additionalParameters['item-selected'] == true) {
									$entity.addClass ('tree-selected');
									$entity.find('i').removeClass(self.options['unselected-icon']).addClass(self.options['selected-icon']);
									$entity.closest('.tree-folder-content').show();
							}
						}

						var dataAttributes = value.dataAttributes || [];
						$.each(dataAttributes, function(key, value) {
							switch (key) {
							case 'class':
							case 'classes':
							case 'className':
								$entity.addClass(value);
								break;

							// id, style, data-*
							default:
								$entity.attr(key, value);
								break;
							}
						});

						if($el.hasClass('tree-folder-header')) {
							$parent.find('.tree-folder-content:eq(0)').append($entity);
						} else {
							$el.append($entity);
						}
					});

					// 加载元素
					self.$element.trigger('loaded', $parent);
					
					// 加载全部子元素
					if($el.hasClass('tree-selectable')){
						var childFolder = $el.find('.tree-folder').eq(1).find('.tree-folder-header');
						if(childFolder){
							self.populate(childFolder);
						}
					}
					
					if($el.hasClass('tree-folder-header')){
						 $el.parent().find('.tree-folder-content').find('.tree-folder').each(function(){
							self.populate($(this).find('.tree-folder-header'));
						});
					}
					
				});
			},
			// 选中复选框
			selectItem: function (el) {
				var $el = $(el);
				var $showEl = $el.parent().parent().parent();
				
				// 如果勾选folder(下级菜单统一勾选)
				if($el.hasClass('folder')){
					// 选中文件夹中所有的子元素
					$showEl.find('.tree-item-checkbox').prop('checked',$el.prop("checked"));
				}
				
				// 处理文件夹的选中状态
				this.procFolderSelected();
			},

			selectFolder: function (el) {
				
			},
			selectedItems: function () {
				var $sel = this.$element.find('.tree-selected');
				var data = [];

				$.each($sel, function (index, value) {
					data.push($(value).data());
				});
				return data;
			},
			// collapses open folders
			collapse: function () {
				var cacheItems = this.options.cacheItems;

				// find open folders
				this.$element.find('.icon-folder-open').each(function () {
					// update icon class
					var $this = $(this)
						.removeClass('icon-folder-close icon-folder-open')
						.addClass('icon-folder-close');

					// "close" or empty folder contents
					var $parent = $this.parent().parent();
					var $folder = $parent.children('.tree-folder-content');

					$folder.hide();
					if (!cacheItems) {
						$folder.empty();
					}
				});
			},
			// 清空选中菜单
			clearItemSelect: function(){
				this.$element.find('.tree-item-checkbox').prop('checked',false);
			},
			/**
			 * 
			 * @param selectItems
			 *            选中的元素(ID数组)
			 * @param isClearOtherItem是否清空其他元素的选中
			 */
			selectTreeItem: function(selectItems,isClearOtherItem){
				if(isClearOtherItem){
					this.$element.find('.tree-item-checkbox').prop('checked',false);
				}

				if(selectItems != null){
					// 选中需要选中的元素
					for(var i=0;i<selectItems.length;i++){
						this.$element.find('.tree-id').each(function(){
							var $this   = $( this );
							if($this.html() == selectItems[i]){
								// 选中
								$this.parent().find('.tree-item-checkbox').prop('checked',true);
							}
						});
					}
					this.procFolderSelected();
				}
			},
			/**
			 * 处理文件夹选中状态(当其子元素中包含未选中的元素时取消,当子元素全部选中时选中)
			 */
			procFolderSelected: function(){
				var treeType = this.options.treeType;
				
				var $this = this.$element;
				// 循环所有的tree-folder 处理其选中状态
				var treeFolder = $this.find('.tree-folder:gt(0)').each(function(){
					var $this = $(this);
					
					if(treeType == "allSelect-parentSelect"){
						// 未选中的元素
						var uncheckedItemLen = $this.find('.tree-item-checkbox.item:not(:checked)').length;
						if(uncheckedItemLen > 0){
							$this.find('.tree-folder-header').find('.tree-item-checkbox').prop('checked',false);
						}else{
							// 添加选中状态
							$this.find('.tree-folder-header').find('.tree-item-checkbox').prop('checked',true);
						}
					}else if(treeType == "oneSelect-parentSelect"){
						// 未选中的元素
						var checkedItemLen = $this.find('.tree-item-checkbox.item:checked').length;
						if(checkedItemLen > 0){
							// 添加选中状态
							$this.find('.tree-folder-header').find('.tree-item-checkbox').prop('checked',true);
						}else{
							$this.find('.tree-folder-header').find('.tree-item-checkbox').prop('checked',false);
						}
					}
					
				});
			},
			/**
			 * 伸展和收缩树中的元素
			 * 
			 * @param el
			 *            收缩的元素(如果为null则收缩全部元素)
			 */
			stretch:function(el){
				var $this = this;
				if(el){
					$this.changeTreeStretch(el);
				}else{
					this.$element.find(".stretch").each(function (){
						$this.changeTreeStretch(this);
					});
				}
			},
			/**
			 * 伸缩第一个元素
			 */
			stretchFirstItem:function(){
				this.changeTreeStretch(this.$element.find('.stretch:gt(0)'));
			},
			changeTreeStretch: function(el,spreadOrShrink){
				var $el = $(el);
				var $parent = $el.parent();
				var $treeFolderContent = $parent.parent().find('.tree-folder-content');
				var $treeFolderContentFirstChild = $treeFolderContent.eq(0);
				
				var target = '.' + $.trim(this.options['close-icon'].replace(/\s/g, '.'))// ACE
				
				var classToTarget, classToAdd;
				if (spreadOrShrink == "spread" || ((spreadOrShrink == null || spreadOrShrink == "" || spreadOrShrink == undefined) && $parent.find(target).length)) {// ACE
					classToTarget = this.options['close-icon'];// ACE
					classToAdd = this.options['open-icon'];// ACE
					$treeFolderContentFirstChild.show();
				} else {
					target = '.' + $.trim(this.options['open-icon'].replace(/\s/g, '.'))// ACE
					classToTarget = this.options['open-icon'];
					classToAdd = this.options['close-icon'];
					$treeFolderContentFirstChild.hide();
				}
				
				// ACE
				$parent.parent().find(target).eq(0)
					.removeClass(classToTarget)
					.addClass(classToAdd);
			},
			/**
			 * 展开所有元素
			 */
			spreadAllItem:function(el){
				var $this = this;
				if(el){
					$this.changeTreeStretch(el,"spread");
				}else{
					this.$element.find(".stretch").each(function (){
						$this.changeTreeStretch(this,"spread");
					});
				}
			},
			/**
			 * 收缩所有元素
			 */
			shrinkAllItem:function(el){
				var $this = this;
				if(el){
					$this.changeTreeStretch(el,"shrink");
				}else{
					this.$element.find(".stretch").each(function (){
						$this.changeTreeStretch(this,"shrink");
					});
				}
			},
			/**
			 * 根据树的id获取选中的元素(不包括文件夹)
			 * isFolder 是否过滤文件夹
			 * @returns
			 */
			getSelectTreeItem: function(isFolder){
				if (isFolder == undefined) {
					isFolder = true;	// 默认过滤文件夹
				}
				var selectEdata = new Array();
				var i = 0;
				// 查找选中的复选框
				var checkedCheckBoxs = this.$element.find(".tree-item-checkbox:checked").each(function(){ 
					var folderHeader = $(this).parent().parent();
					var data = folderHeader.data();
					// 过滤文件夹
					if (folderHeader.hasClass('tree-folder-header')) {	// 文件夹
						if (!isFolder) {
							selectEdata[i] = data;
							i++;
						}
					} else {	// 普通目录
						selectEdata[i] = data;
						i++;
					}
				});
				return selectEdata;
			},

			/**
			 * 根据树的id获取选中的所有元素
			 * 
			 * @returns
			 */
			getSelectTreeAllItem: function(){
				var selectEdata = new Array();
				var i = 0;
				// 查找选中的复选框
				var checkedCheckBoxs = this.$element.find(".tree-item-checkbox:checked").each(function(){ 
					var data =  $(this).parent().parent().data();
					selectEdata[i] = data;
					i++;
				});
				return selectEdata;
			}
		};


		// TREE PLUGIN DEFINITION

		$.fn.tree = function (option) {
			var args = Array.prototype.slice.call( arguments, 1 );
			var methodReturn;

			var $set = this.each(function () {
				var $this   = $( this );
				var data    = $this.data( 'tree' );
				var options = typeof option === 'object' && option;

				if( !data ) $this.data('tree', (data = new Tree( this, options ) ) );
				if( typeof option === 'string' ) methodReturn = data[ option ].apply( data, args );
			});

			return ( methodReturn === undefined ) ? $set : methodReturn;
		};

		$.fn.tree.defaults = {
			multiSelect: false,
			loadingHTML: '<div>Loading...</div>',
			cacheItems: true,
			
		};

		$.fn.tree.Constructor = Tree;

		$.fn.tree.noConflict = function () {
			$.fn.tree = old;
			return this;
		};
	// });
	})(window.jQuery);// ACE
}

//-------------------------------------------- 单选树 -----------------------
function setRadioChangeStyle(treeId,treeType){
	(function($ , undefined) {
		// treeType 类型分为两大类 allSelect-parentSelect(全部选中父菜单选中),oneSelect-parentSelect(一个选中父菜单选中)
		if(treeType == null || treeType == undefined){
			treeType = "allSelect-parentSelect";
		}
		
		var $options = {
				'treeId' : treeId,
				'treeType' : treeType,
				'open-icon' : 'ace-icon tree-minus',
				'close-icon' : 'ace-icon tree-plus',
				'selected-icon' : 'ace-icon fa fa-check',
				'unselected-icon' : 'ace-icon fa fa-times',
				'selectable' : true,
				multiSelect : true,
				loadingHTML : '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>'
		}
		
		$.fn.ace_tree = function(options) {
			$options = $.extend({}, $options, options)
			this.each(function() {
				var $this = $(this);
				// html属性菜单的样式(分多选和单选两种样式)
				var treeHtml = '<div class="tree-folder" style="display:none;">'+
									'<div class="tree-folder-header" >'+
										'<i class="stretch '+ ace.vars['icon'] + $options['close-icon']+'"></i>'+
										'<label class="position-relative" style="padding-left: 2px">'+
											'<input type="radio" class="ace tree-item-radio folder" name="tree_item_raio"/>'+
											'<span class="lbl"></span>'+
										'</label>'+
										'<div class="tree-folder-name"></div>'+
										'<div class="tree-id" style="display:none;"></div>'+
									'</div>'+
									'<div class="tree-folder-content" style="display:none"></div>'+
									'<div class="tree-loader" style="display:none"></div>'+
								'</div>'+
								'<div class="tree-item" style="display:none;">'+
									'<label class="position-relative">'+
										'<input type="radio" class="ace tree-item-radio item" name="tree_item_raio"/>'+
										'<span class="lbl"></span>'+
									'</label>'+
									'<div class="tree-item-name"></div>'+
									'<div class="tree-id" style="display:none;"></div>'+
								'</div>';
				$this.html(treeHtml);
				$this.addClass($options['selectable'] == true ? 'tree-selectable' : 'tree-unselectable');
				
				$this.tree($options);
			});
	
			return this;
		}
	})(window.jQuery);
	
	
	/*
	 * Fuel UX Tree https://github.com/ExactTarget/fuelux
	 * 
	 * Copyright (c) 2012 ExactTarget Licensed under the MIT license.
	 */

	// define(['require','jquery'],function(require) {
	(function($ , undefined) {// ACE

		// var $ = require('jquery');
		var old = $.fn.tree;

		// TREE CONSTRUCTOR AND PROTOTYPE

		var Tree = function (element, options) {
			
			this.$element = $(element);
			this.options = $.extend({}, $.fn.tree.defaults, options);
			
			var $this = $(this);
			
			// 先取消绑定事件
			this.$element.off('click', '.tree-item-checkbox');
			this.$element.off('click', '.stretch');
			
			// 绑定事件
			this.$element.on('click', '.stretch', $.proxy( function(ev) { this.stretch(ev.currentTarget); }, this));
			this.$element.on('click', '.tree-folder-name,.tree-item-name,.tree-item-radio', $.proxy( function(ev) { this.selectName(ev.currentTarget); }, this));
			

			this.render();
		};

		Tree.prototype = {
			constructor: Tree,

			render: function () {
				this.populate(this.$element);
			},

			populate: function ($el) {
				
				var self = this;
				var $parent = $el.parent();
				
				this.options.dataSource.data($el.data(), function (items) {
					$.each( items.data, function(index, value) {
						var $entity;

						if(value.type === "folder") {
							$entity = self.$element.find('.tree-folder:eq(0)').clone().show();
							$entity.find('.tree-folder-name').html(value.name);
							$entity.find('.tree-id').html(value.id);
							$entity.find('.tree-loader').html(self.options.loadingHTML);
							// $entity.find('.tree-folder-header').data(value);
							// ACE
							var header = $entity.find('.tree-folder-header');
							header.data(value);

							if('icon-class' in value){
								header.find('i').addClass(value['icon-class']);
							}
							
						} else if (value.type === "item") {
							$entity = self.$element.find('.tree-item:eq(0)').clone().show();
							$entity.find('.tree-item-name').html(value.name);
							$entity.find('.tree-id').html(value.id);
							$entity.data(value);
							
							// ACE
							if('additionalParameters' in value
								&& 'item-selected' in value.additionalParameters 
									&& value.additionalParameters['item-selected'] == true) {
									$entity.addClass ('tree-selected');
									$entity.find('i').removeClass(self.options['unselected-icon']).addClass(self.options['selected-icon']);
									$entity.closest('.tree-folder-content').show();
							}
						}

						var dataAttributes = value.dataAttributes || [];
						$.each(dataAttributes, function(key, value) {
							switch (key) {
							case 'class':
							case 'classes':
							case 'className':
								$entity.addClass(value);
								break;

							// id, style, data-*
							default:
								$entity.attr(key, value);
								break;
							}
						});

						if($el.hasClass('tree-folder-header')) {
							$parent.find('.tree-folder-content:eq(0)').append($entity);
						} else {
							$el.append($entity);
						}
					});

					// 加载元素
					self.$element.trigger('loaded', $parent);
					
					// 加载全部子元素
					if($el.hasClass('tree-selectable')){
						var childFolder = $el.find('.tree-folder').eq(1).find('.tree-folder-header');
						if(childFolder){
							self.populate(childFolder);
						}
					}
					
					if($el.hasClass('tree-folder-header')){
						 $el.parent().find('.tree-folder-content').find('.tree-folder').each(function(){
							self.populate($(this).find('.tree-folder-header'));
						});
					}
					
				});
			},
			// 选中Radio
			selectName: function(el) {
				var oldSelectedTreeId = -1;
				var oldSelectedObj = this.$element.find('.tree-item-radio:checked');
				if (oldSelectedObj.length > 0) {
					oldSelectedTreeId = oldSelectedObj.parent().parent().data().id;
				}
				
				var tree_radio = $(el).siblings("label").find("input[type='radio']");
				
				tree_radio.prop("checked", true);
				
				var selectedData = this.$element.find('.tree-item-radio:checked').parent().parent().data();
				if (last_selected_tree_data != selectedData) {
					// 触发选中事件
					this.$element.trigger('selected', selectedData);
				}
				last_selected_tree_data = selectedData;
				
			},
			selectedItems: function () {
				
			},
			// collapses open folders
			collapse: function () {
				var cacheItems = this.options.cacheItems;

				// find open folders
				this.$element.find('.icon-folder-open').each(function () {
					// update icon class
					var $this = $(this)
						.removeClass('icon-folder-close icon-folder-open')
						.addClass('icon-folder-close');

					// "close" or empty folder contents
					var $parent = $this.parent().parent();
					var $folder = $parent.children('.tree-folder-content');

					$folder.hide();
					if (!cacheItems) {
						$folder.empty();
					}
				});
			},
			// 清空选中菜单
			clearItemSelect: function(){
				this.$element.find('.tree-item-radio').prop('checked',false);
			},
			/**
			 * 
			 * @param selectItems
			 *            选中的元素(ID数组)
			 * @param isClearOtherItem是否清空其他元素的选中
			 */
			selectTreeItem: function(selectItems){
				if(selectItems != null){
					// 选中需要选中的元素
					this.$element.find('.tree-id').each(function(){
						var $this   = $( this );
						if($this.html() == selectItems){
							// 选中
							$this.parent().find('.tree-item-radio').prop('checked',true);
						}
					});
				}
			},
			/**
			 * 伸展和收缩树中的元素
			 * 
			 * @param el
			 *            收缩的元素(如果为null则收缩全部元素)
			 */
			stretch:function(el){
				var $this = this;
				if(el){
					$this.changeTreeStretch(el);
				}else{
					this.$element.find(".stretch").each(function (){
						$this.changeTreeStretch(this);
					});
				}
			},
			/**
			 * 伸缩第一个元素
			 */
			stretchFirstItem:function(){
				this.changeTreeStretch(this.$element.find('.stretch:gt(0)'));
			},
			changeTreeStretch: function(el,spreadOrShrink){
				var $el = $(el);
				var $parent = $el.parent();
				var $treeFolderContent = $parent.parent().find('.tree-folder-content');
				var $treeFolderContentFirstChild = $treeFolderContent.eq(0);
				
				var target = '.' + $.trim(this.options['close-icon'].replace(/\s/g, '.'))// ACE
				
				var classToTarget, classToAdd;
				if (spreadOrShrink == "spread" || ((spreadOrShrink == null || spreadOrShrink == "" || spreadOrShrink == undefined) && $parent.find(target).length)) {// ACE
					classToTarget = this.options['close-icon'];// ACE
					classToAdd = this.options['open-icon'];// ACE
					$treeFolderContentFirstChild.show();
				} else {
					target = '.' + $.trim(this.options['open-icon'].replace(/\s/g, '.'))// ACE
					classToTarget = this.options['open-icon'];
					classToAdd = this.options['close-icon'];
					$treeFolderContentFirstChild.hide();
				}
				
				// ACE
				$parent.parent().find(target).eq(0)
					.removeClass(classToTarget)
					.addClass(classToAdd);
			},
			/**
			 * 展开所有元素
			 */
			spreadAllItem:function(el){
				var $this = this;
				if(el){
					$this.changeTreeStretch(el,"spread");
				}else{
					this.$element.find(".stretch").each(function (){
						$this.changeTreeStretch(this,"spread");
					});
				}
			},
			/**
			 * 收缩所有元素
			 */
			shrinkAllItem:function(el){
				var $this = this;
				if(el){
					$this.changeTreeStretch(el,"shrink");
				}else{
					this.$element.find(".stretch").each(function (){
						$this.changeTreeStretch(this,"shrink");
					});
				}
			},
			/**
			 * 根据树的id获取选中的元素
			 * isFolder 是否过滤文件夹
			 * @returns
			 */
			getSelectTreeItem: function(){
				var selectEdata = new Array();
				var i = 0;
				// 查找选中的复选框
				this.$element.find(".tree-item-radio:checked").each(function(){
					var data = $(this).parent().parent().data();
					if (data != undefined && data != null && data.id != undefined) {
						selectEdata[i] = data;
					}
					i++;
				});
				return selectEdata;
			},

			/**
			 * 根据树的id获取选中的所有元素
			 * 
			 * @returns
			 */
			getSelectTreeAllItem: function(){
				var selectEdata = new Array();
				var i = 0;
				// 查找选中的复选框
				var checkedCheckBoxs = this.$element.find(".tree-item-radio:checked").each(function(){ 
					var data =  $(this).parent().parent().data();
					selectEdata[i] = data;
					i++;
				});
				return selectEdata;
			}
		};


		// TREE PLUGIN DEFINITION

		$.fn.tree = function (option) {
			var args = Array.prototype.slice.call( arguments, 1 );
			var methodReturn;

			var $set = this.each(function () {
				var $this   = $( this );
				var data    = $this.data( 'tree' );
				var options = typeof option === 'object' && option;

				if( !data ) $this.data('tree', (data = new Tree( this, options ) ) );
				if( typeof option === 'string' ) methodReturn = data[ option ].apply( data, args );
			});

			return ( methodReturn === undefined ) ? $set : methodReturn;
		};

		$.fn.tree.defaults = {
			multiSelect: false,
			loadingHTML: '<div>Loading...</div>',
			cacheItems: true,
			
		};

		$.fn.tree.Constructor = Tree;

		$.fn.tree.noConflict = function () {
			$.fn.tree = old;
			return this;
		};
	// });
	})(window.jQuery);// ACE
}

/**
 * 数的对象
 * 
 * @param options
 * @returns
 */
var DataSourceTree = function(options) {
	this._data 	= options.data;
	this._delay = options.delay;
}

DataSourceTree.prototype.data = function(options, callback) {
	var self = this;
	var $data = null;

	if(options == null || (!("name" in options) && !("type" in options))){
		$data = this._data;// the root tree
		callback({ data: $data });
		return;
	}else if("type" in options && options.type == "folder") {
		if("additionalParameters" in options && "children" in options.additionalParameters){
			$data = options.additionalParameters.children;
		}else{
			$data = {}// no data
		}
	}
	callback({ data: $data });
};

/**
 * 树形数据转换
 * 
 * @param treeObj
 */
function treeObjToTreeData(treeId,treeObj) {

	var treeDataStr = "{\"" + treeObj.treeId + "\" : {\"id\":\""+treeObj.treeId+"\",\"name\" : \"" + treeObj.treeName
			+ "\",\"type\" : \"" + converTreeType(treeObj.treeType) + "\",\"gridId\":\""+treeId+"\"";

	if (treeObj.childsTree != null && treeObj.childsTree.length > 0) {
		treeDataStr += ",";
		// 处理子属性
		treeDataStr += treeListToChildStr(treeId,treeObj.childsTree);
	}
	treeDataStr += "}}";
	return $.parseJSON(treeDataStr);
}

/**
 * 处理子菜单
 * 
 * @param treeList
 * @returns
 */
function treeListToChildStr(treeId,treeList) {
	if (treeList == null) {
		return null;
	}

	// 拼接成格式: "{children : {100000 : {name : '物业一公司',type:'folder'},200000 :
	// {name :'物业二公司',type : 'item'}}}";

	var childStr = "\"additionalParameters\":{\"children\" : [";

	for ( var i = 0; i < treeList.length; i++) {
		var tree = treeList[i];
		var treeStr = "{\"id\":\""+tree.treeId+"\",\"name\":\"" + tree.treeName + "\",\"type\":";

		// 转换tree类型
		treeStr += "\"" + converTreeType(tree.treeType) + "\",\"gridId\":\""+treeId+"\"";

		// 递归处理子属性
		if (tree.childsTree != null && tree.childsTree.length > 0) {
			treeStr += ",";
			treeStr += treeListToChildStr(treeId,tree.childsTree);
		}
		treeStr += "}";

		if (i != treeList.length - 1) {
			treeStr += ",";
		}

		childStr += treeStr;
	}
	childStr += "]}";
	return childStr;
}

/**
 * 数类型转成tree识别的字段
 * 
 * @param treeType
 * @returns {String}
 */
function converTreeType(treeType) {
	if (treeType == 0) {
		return "folder";
	} else {
		return "item";
	}
}

/**
 * 加载树
 * 
 * @param treeId
 *            属性控件的ID
 * @param objData
 *            树中的数据
 * @returns
 */
function loadTree(treeId,objData) {
	var $tree = $('#'+treeId);
	if($tree.hasClass('more-change-tree')){
		loadMoreChangeTree(treeId,objData,"allSelect-parentSelect");
	}else if($tree.hasClass('more-change-tree-one')){
		loadMoreChangeTree(treeId,objData,"oneSelect-parentSelect");
	}else if($tree.hasClass('radio-change-tree-one')){
		loadRadioChangeTree(treeId,objData,"oneSelect-parentSelect");
	}else{
		loadOneChangeTree(treeId,objData);
	}
	return $tree;
}

/**
 * 加载单选树
 * 
 * @param treeId
 *            属性控件的ID
 * @param objData
 *            树中的数据
 */
function loadOneChangeTree(treeId,objData){
	// 设置单选树的样式及事件
	setOneChangeStyle(treeId);
	// 生成树
	createTree(treeId, objData);
}

/**
 * 加载多选树
 * 
 * @param treeId
 *            属性控件的ID
 * @param objData
 *            树中的数据
 */
function loadMoreChangeTree(treeId,objData,treeType) {
	// 设置多选树的样式及事件
	setMoreChangeStyle(treeId,treeType);
	// 生成树
	createTree(treeId, objData);
}

//加载单选树
function loadRadioChangeTree(treeId,objData,treeType) {
	// 设置多选树的样式及事件
	setRadioChangeStyle(treeId,treeType);
	// 生成树
	createTree(treeId, objData);
}

/**
 * 创建树
 * 
 * @param treeId
 * @param objData
 */
function createTree(treeId,objData){
	// 数据转换成树数据
	var tree_data = treeObjToTreeData(treeId,objData);
	
	var treeDataSource = new DataSourceTree({
		data : tree_data
	});

	var tree = $('#'+treeId);
	
	if(tree.data('tree') != null){
		tree.children().remove();
		tree.data('tree',null);
	}
	
	
	// 添加树
	tree.ace_tree({
		"treeId" : treeId,
		dataSource : treeDataSource
	});

	tree.off('selected');
	
	// data为填入树中的Tree对象,eventType为选中类型
	tree.on('selected', function(e, data, eventType) {
		selectedTree(data);
	});
}

/**
 * 选中树
 * 
 * @param data
 */
function selectedTree(data){
}
var last_selected_tree_data=null;	// 上次选中的树数据

//div滚动效果
$('.scrollable-horizontal').each(function() {
	var $this = $(this);
	$(this).ace_scroll({
		horizontal : true,
		size : $this.data('size') || 500,
		mouseWheelLock : true
	});
});

$(window).on('resize.scroll_reset', function() {
	$('.scrollable-horizontal').ace_scroll('reset');
});



