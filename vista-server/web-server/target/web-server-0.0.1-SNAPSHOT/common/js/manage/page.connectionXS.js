(function ($) {
    function init(target) {
        //初始化基础设置
        var options = $(target).data('options');
        var opt = options.options;     

        $.messager.progress("close");
        
        
        var editFun=function (id){
			$.messager.progress("show","加载中...");
			var editDialog=$("<div></div>");
			editDialog.dialog({
				title : '票类编辑',
				width : 360,
				height :355,
				href:opt.appUrl+"m_ticketV/toEdit?key=" + id,
				onLoad : function() {
					$.messager.progress("close");
					if(editDialog.find('#theform').length>0){
						editDialog.find('#theform').form({
							url : opt.appUrl + "m_ticketV/save",
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

        
        
        
    	options.table=$(target).datagrid({ 
			url:opt.appUrl+'m_concession/list?ptype=0',		  
			onLoadSuccess:function(data){
				var dgv=$(this).parent();
				dgv.find(".btn_edit").linkbutton({plain:true,iconCls:'icon-edit',onClick:function(){
					var key=$(this).data("key");
					editFun(key);
				}});
			},
		    columns:[[   
		    	{field:'pid',title:'卖品ID',width:150,sortable:true},
		    	{field:'hopk',title:'HOPK',width:150,sortable:true},
		        {field:'name',title:'卖品名称',width:150,sortable:true},  
		        {field:'price',title:'卖品价格',width:150,sortable:true},
		        {field:'pid',title:'卖品设置',width:150,sortable:true,formatter:function(val){
		        	return "<a class='btn_edit' data-id='" + row.id + "'>编辑</a><a class='btn_del' data-id='" + row.id + "'>删除</a>"
				}}
		    ]]   
		});  
		
		$.messager.progress("close");
		
		opt.reload=function(){
			options.table.datagrid("reload");
		}
    }

    $.fn.connection_XS = function (options) {
        $.fn.loadPage(this, $.fn.connection_XS.defaults, init);
    };
    $.fn.connection_XS.defaults = {};
    
})(jQuery);