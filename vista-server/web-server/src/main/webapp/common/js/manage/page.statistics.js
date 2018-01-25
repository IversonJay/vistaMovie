(function ($) {
    function init(target) {
        //初始化基础设置
        var options = $(target).data('options');
        var opt = options.options;     

        $.messager.progress("close");

        
    	options.table=$(target).datagrid({ 
			url:opt.appUrl+'m_statistics/getPager',
		    columns:[[   
		        {field:'date',title:'日期',width:150,sortable:true},  
		        {field:'count',title:'登陆用户数',width:150,sortable:true}
		    ]]   
		});  
		
		$.messager.progress("close");
		
		opt.reload=function(){
			options.table.datagrid("reload");
		}
    }

    $.fn.page_statistics = function (options) {
        $.fn.loadPage(this, $.fn.page_statistics.defaults, init);
    };
    $.fn.page_statistics.defaults = {};
    
})(jQuery);