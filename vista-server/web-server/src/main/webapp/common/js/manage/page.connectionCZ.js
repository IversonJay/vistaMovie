(function ($) {
    function init(target) {
        //初始化基础设置
        var options = $(target).data('options');
        var opt = options.options;     

        $.messager.progress("close");
        
        
        var addFun=function (){
        	var cid = $('#clist option:selected').val();
            $.messager.progress("show", "加载中...");
            var addDialog = $("<div></div>");
            addDialog.dialog({
                title: '卖品添加',
                width: 375,
                height: 470,
                href: opt.appUrl + "m_concession/add?cid=" + cid,
                onLoad: function () {
                    $.messager.progress("close");
                    if (addDialog.find('#theform').length > 0) {
                    	addDialog.find('#theform').form({
                            url: opt.appUrl + "m_concession/save",
                            success: function (data) {
                                if ($.wow_analyseReturn(data, function (r) {
                                        if (r) {
                                            options.table.datagrid("reload");
                                            addDialog.dialog("close");
                                            Util.showMessage('提示信息', '提交成功！');
                                        } else {
                                            $.messager.alert('提示信息', '提交失败！');
                                        }

                                    }));
                                $.messager.progress("close");
                            },
                            onSubmit: function () {
                                if (addDialog.find('#theform').form('validate')) {
                                    $.messager.progress("show", "提交中...");
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                    } else {
                    	addDialog.dialog("close");
                    }
                },
                buttons: [{
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                    	addDialog.find('#theform').submit();
                    }
                }, {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                    	addDialog.dialog("close");
                    }
                }]
            });
        }
        
        
        var editFun=function (id, cid){
			$.messager.progress("show","加载中...");
			var editDialog=$("<div></div>");
			editDialog.dialog({
				title : '卖品编辑',
				width : 360,
				height :355,
				href:opt.appUrl + "m_concession/edit?cid=" + cid + "&id=" + id,
				onLoad : function() {
					$.messager.progress("close");
					if(editDialog.find('#theform').length>0){
						editDialog.find('#theform').form({
							url : opt.appUrl + "m_concession/saveE",
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
        
        
        var delFun = function (id) {
            $.messager.confirm('警告', '确认删除这条信息?', function (r) {
                if (r) {
                    $.messager.progress("show", "加载中...");
                    $.wow_post(opt.appUrl + "m_concession/del", {id: id}, function (s, data) {
                        if (s) {
                            options.table.datagrid("reload");
                            Util.showMessage('提示信息', '删除成功！');
                        } else {
                            $.messager.alert('提示信息', '删除失败！');
                        }
                        $.messager.progress("close");
                    });
                }
                ;
            });
        }
        
        
        options.table=$(target).datagrid({ 
			url:opt.appUrl+'m_concession/list',	
			toolbar: [
                {
                    text: '<span id="cc"></span>',
                },
                {
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                    	var cid = $('#clist option:selected').val();
                    	addFun(cid);
                    }
                }
            ],
			onLoadSuccess:function(data){
				var dgv=$(this).parent();
				dgv.find(".btn_edit").linkbutton({
                    plain: true, iconCls: 'icon-edit', onClick: function () {
                        var id = $(this).data("id");
                        editFun(id, $('#clist option:selected').val());
                    }
                });
                dgv.find(".btn_del").linkbutton({
                    plain: true, iconCls: 'icon-cancel', onClick: function () {
                        var id = $(this).data("id");
                        delFun(id);
                    }
                });
				
				
				
				var htmlStr = '';
				$.ajax({
					type:'get',
					url: opt.appUrl+"m_concession/cinemaList",
					datatype:'json',
					async:false,
					success : function(data) {
						for (var i = 0; i < data.result.length; i++) {
							htmlStr += '<option value="' + data.result[i].cid + '">' + data.result[i].cname + '</option>'							
						}
					}
				});
				if ($('#clist').val() == undefined) {
					$('#cc').append('影院 ：<select id="clist" class="easyui-combobox"  style="width:200px; height:20px" data-options="required:true">' + htmlStr + '</select>');					
				}
				
				$('#clist').on('change', function() {
					var cid = $('#clist option:selected').val();
					options.table.datagrid("load", {cid : cid});
				})
				
			},
		    columns:[[   
		    	{field:'pid',title:'卖品ID',width:150,sortable:true},
		        {field:'name',title:'卖品名称',width:150,sortable:true},  
		        {field:'price',title:'卖品价格(元)',width:150,sortable:true,formatter:function(val){
		        	return val/100;
				}},
		        {field:'ptype',title:'卖品类型',width:150,sortable:true,formatter:function(val){
		        	if (val == 0) {
		        		return "小食卖品";
		        	} else if (val == 1) {
		        		return "充值卖品";
		        	}
				}},
		        {field:'id',title:'卖品设置',width:150,sortable:true,formatter:function(val, row){
		        	return "<a class='btn_edit' data-id='" + row.id + "'>编辑</a><a class='btn_del' data-id='" + row.id + "'>删除</a>"
				}}
		    ]]   
		});   
		
		$.messager.progress("close");
		
		opt.reload=function(){
			options.table.datagrid("reload");
		}
    }

    $.fn.connection_CZ = function (options) {
        $.fn.loadPage(this, $.fn.connection_CZ.defaults, init);
    };
    $.fn.connection_CZ.defaults = {};
    
})(jQuery);