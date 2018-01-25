(function ($) {
    function init(target) {
        //初始化基础设置
        var options = $(target).data('options');
        var opt = options.options;     

        $.messager.progress("close");

        
        var mergeFun = function() {
        	var row = options.table.datagrid('getChecked');
        	if (row == null || row == "" || row.length == 1) {
        		$.messager.alert('警告', '合并影片必须两部及以上。。');
        		return;
        	}
        	var mids = "";
        	var mnames = "";
        	for(var i = 0;i < row.length; i++ ) {
        		if (mids != "") {
        			mids = mids + "," + row[i].mid;
        			mnames = mnames + "】<br>【" + row[i].mname;
        		} else {
        			mids = row[i].mid;
        			mnames =  row[i].mname;
        		}
        	}
        	
        	//alert('MID ID:' + mids);
        	
        	$.messager.confirm('警告', '确认将<br>【' + mnames + "】进行合并？", function (r) {
                if (r) {
                    $.wow_post(opt.appUrl + 'm_movie/mergeMovie?mids=' + mids, {}, function (s, data) {
                        if (s) {
                            Util.showMessage('提示信息', '处理成功！<br> 请确保同一电影在【滚动设置】中的目标ID与当前【影片标识】相同！！！');
                            options.table.datagrid("reload");
                        } else {
                            $.messager.alert('警告', '处理失败!');
                        }
                        $.messager.progress("close");
                    });
                }
            });
        	
        }
        
        
        var recoverFun = function() {
        	var row = options.table.datagrid('getChecked');
        	if (row == null || row == "" || row.length > 1) {
        		$.messager.alert('警告', '单次只能恢复一条影片记录。。。');
        		return;
        	}
        	var mid = row[0].mid;
        	var mname = row[0].mname;
        	
        	$.messager.confirm('警告', '确认将【' + mname + "】进行恢复？", function (r) {
                if (r) {
                    $.wow_post(opt.appUrl + 'm_movie/recoverMovie?mid=' + mid, {}, function (s, data) {
                        if (s) {
                            Util.showMessage('提示信息', '处理成功！<br> 请确保同一电影在【滚动设置】中的目标ID与当前【影片标识】相同！！！');
                            options.table.datagrid("reload");
                        } else {
                            $.messager.alert('警告', '处理失败!');
                        }
                        $.messager.progress("close");
                    });
                }
            });
        }
        
        
        var editFun = function (mid) {
            var title = "";
            var url = "";
            var height = 200;
            var width = 460;
            
            title = "设置序号";
            url = opt.appUrl + "m_movie/toEdit_input";
            
            var editDialog = $("<div></div>");
            editDialog.dialog({
                title: title,
                width: width,
                height: height,
                href: url,
                onLoad: function () {
                    if (editDialog.find('#theform').length > 0) {
                        editDialog.find('#theform').form({
                            url: opt.appUrl + "m_movie/editSequence",
                            success: function (data) {
                                if ($.wow_analyseReturn(data, function (r) {
                                        if (r) {
                                            options.table.datagrid("reload");
                                            editDialog.dialog("close");
                                            Util.showMessage('提示信息', '提交成功！');
                                        } else {
                                            $.messager.alert('提示信息', '提交失败！');
                                        }

                                    }));
                                $.messager.progress("close");
                            },
                            onSubmit: function () {
                                return true;
                            }
                        });

                        editDialog.find('#theform').form('load', {mid: mid, idx: idx});
                    } else {
                        editDialog.dialog("close");
                    }
                },
                buttons: [{
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        editDialog.find('#theform').submit();
                    }
                }, {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        editDialog.dialog("close");
                    }
                }]
            });
        }

        
        
        
    	options.table=$(target).datagrid({ 
			url:opt.appUrl+'m_movie/getPager?type=1',
		    toolbar:[
            	{
               		text:'合并影片',
               	    iconCls:'icon-undo',
               	    handler:function(){
               	    	mergeFun();
               	    }
               	},
               	{
               		text:'恢复影片',
               	    iconCls:'icon-redo',
               	    handler:function(){
               	    	recoverFun();
               	    }
               	}
            	
            ],
			onLoadSuccess:function(data){ 
				var dgv=$(this).parent();
				dgv.find(".btn_edit").linkbutton({plain:true,iconCls:'icon-edit',onClick:function(){
					var mid=$(this).data("mid");
					editFun(mid);
				}});
			},
		    columns:[[   
		        {field:'ck',checkbox:true},
		        {field:'mname',title:'影片名',width:150,sortable:true},  
		        /*{field:'mid',title:'mid',width:150,sortable:true}, */
		        {field:'merge',title:'影片标识',width:150,sortable:true},
		        {field:'idx',title:'序号',width:150,sortable:true},
		        {field:'mid',title:'序号设置',width:150,sortable:true,formatter:function(val){
		        	return "<a class='btn_edit' data-mid='"+val+"'>编辑</a>";
				}}
		    ]]   
		});  
		
		$.messager.progress("close");
		
		opt.reload=function(){
			options.table.datagrid("reload");
		}
    }

    $.fn.movie_Sort = function (options) {
        $.fn.loadPage(this, $.fn.movie_Sort.defaults, init);
    };
    $.fn.movie_Sort.defaults = {};
    
})(jQuery);