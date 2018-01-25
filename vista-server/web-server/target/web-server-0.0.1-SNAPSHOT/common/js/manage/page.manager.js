(function($){
	function init(target){
		//初始化基础设置
		var options=$(target).data('options');
		var opt=options.options;
		
		var editFun=function (id){
			$.messager.progress("show","加载中...");
			var editDialog=$("<div></div>");
			editDialog.dialog({
				title : '用户信息编辑',
				width : 360,
				height :355,
				href:opt.appUrl+"m_manager/toEdit",
				onLoad : function() {
					$.messager.progress("close");
					if(editDialog.find('#theform').length>0){
						editDialog.find('#theform').form({
							url : opt.appUrl + "m_manager/save",
							success : function(data) {
								if($.wow_analyseReturn(data, function(r){
									if(r){
										options.table.datagrid("reload");
										editDialog.dialog("close");
										Util.showMessage('提示信息', '提交成功！');
									}else{
										$.messager.alert('提示信息', '提交失败！');
									}
									
								}));
								$.messager.progress("close");
							},
							onSubmit: function () {
                                if (editDialog.find('#theform').form('validate')) {
                                    $.messager.progress("show", "提交中...");
                                    return true;
                                } else {
                                    return false;
                                }
                            }
						});
						if(id==undefined){
							editDialog.find("#theform #username").textbox("readonly",false);
							editDialog.find("#theform #password").textbox("enableValidation");
						}else{
							editDialog.find("#theform #username").textbox("readonly",true);
							editDialog.find("#theform #password").textbox("disableValidation");
							
							// 加载数据,加载完毕打开窗口
							$.wow_post(opt.appUrl + 'm_manager/find', {id : id}, function(s,data) {
								if(s){
									editDialog.find('#theform').form('load',data);
								}else{
									$.messager.alert('警告','表单数据加载错误!'); 
									editDialog.dialog("close");
								}
								$.messager.progress("close");
							});
						}
					}else{
						editDialog.dialog("close");
					}
				},
				buttons : [ {
					text : '提交',
					iconCls : 'icon-ok',
					handler : function() {
						editDialog.find('#theform').submit();
					}
				},{
					text : '关闭',
					iconCls : 'icon-cancel',
					handler : function() {
						editDialog.dialog("close");
					}
				}]
			});
		}
		
		var delFun=function (id){
			$.messager.confirm('警告', '确认删除这条信息?', function(r) {
				if (r) {
					$.messager.progress("show","加载中...");
					$.wow_post(opt.appUrl + "m_manager/del", {id : id}, function(s,data) {
						if(s){
							options.table.datagrid("reload");
							Util.showMessage('提示信息', '删除成功！');
						}else{
							$.messager.alert('提示信息', '删除失败！');
						}
						$.messager.progress("close");
					});
				};
			});
		}
		
		
		options.table=$(target).datagrid({ 
			url:opt.appUrl+'m_manager/getPager',
		    toolbar:[
            	{
               		text:'添加用户',
               	    iconCls:'icon-add',
               	    handler:function(){
               	    	editFun();
               	    }
               	}
            ],
			onLoadSuccess:function(data){ 
				var dgv=$(this).parent();
				dgv.find(".btn_edit").linkbutton({plain:true,iconCls:'icon-edit',onClick:function(){
					var id=$(this).data("id");
					editFun(id);
				}});
				dgv.find(".btn_del").linkbutton({plain:true,iconCls:'icon-cancel',onClick:function(){
					var id=$(this).data("id");
					delFun(id);
				}});
			},
		    columns:[[   
		        {field:'ck',checkbox:true},
		        {field:'username',title:'登录名',width:150,sortable:true},   
		        {field:'role',title:'角色',width:150,sortable:true,formatter:function(val,row){
		        	if(val==1){
		        		return "系统管理员";
		        	}else{
		        		return "普通用户";
		        	};
				}},
		        {field:'state',title:'锁定',width:150,sortable:true,formatter:function(val,row){
		        	if(val==-1){
		        		return "锁定";
		        	}else{
		        		return "未锁定";
		        	};
				}},
		        {field:'lastLoginTime',title:'最后登录时间',width:150,sortable:true},
		        {field:'lastLoginIp',title:'最后登录IP',width:150,sortable:true},   
		        {field:'id',title:'操作',width:150,sortable:true,formatter:function(val,row){
		        	if(val>0){
		        		var html="<a class='btn_edit' data-id='"+val+"'>编辑</a><a class='btn_del' data-id='"+val+"'>删除</a>"
			        	return html;
		        	}else{
		        		return "";
		        	}
				}}
		    ]]   
		});  
		
		$.messager.progress("close");
		
		opt.reload=function(){
			options.table.datagrid("reload");
		}
	}
	$.fn.page_manager = function(options){
		$.fn.loadPage(this,$.fn.page_manager.defaults,init);
	};
	$.fn.page_manager.defaults = {
		
	};
})(jQuery);