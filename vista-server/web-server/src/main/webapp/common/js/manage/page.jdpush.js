
(function ($) {
    function init(target) {
        //初始化基础设置
        var options = $(target).data('options');
        var opt = options.options;

        var editFun = function (id) {
            $.messager.progress("show", "加载中...");
            var editDialog = $("<div></div>");
            editDialog.dialog({
                title: '信息编辑',
                width: 375,
                height: 470,
                href: opt.appUrl + "m_roll/toEdit",
                onLoad: function () {
                    $.messager.progress("close");
                    if (editDialog.find('#theform').length > 0) {
                        editDialog.find('#theform').form({
                            url: opt.appUrl + "m_roll/save",
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
                                if (editDialog.find('#theform').form('validate')) {
                                    $.messager.progress("show", "提交中...");
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                        if (id == undefined) {
                            editDialog.find("#theform #coverFileTemp").filebox("enableValidation");
                        } else {
                            editDialog.find("#theform #coverFileTemp").filebox("disableValidation");
                            // 加载数据,加载完毕打开窗口
                            $.wow_post(opt.appUrl + 'm_roll/find', {id: id}, function (s, data) {
                                if (s) {
                                    editDialog.find('#theform').form('load', data);
                                } else {
                                    $.messager.alert('警告', '表单数据加载错误!');
                                    editDialog.dialog("close");
                                }
                                $.messager.progress("close");
                            });
                        }
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

        var delFun = function (id) {
            $.messager.confirm('警告', '确认删除这条信息?', function (r) {
                if (r) {
                    $.messager.progress("show", "加载中...");
                    $.wow_post(opt.appUrl + "m_roll/del", {id: id}, function (s, data) {
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


        options.table = $(target).datagrid({
            url: opt.appUrl + 'm_roll/getPager',
            toolbar: [
                {
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        editFun();
                    }
                }
            ],
            onLoadSuccess: function (data) {
                var dgv = $(this).parent();
                dgv.find(".btn_edit").linkbutton({
                    plain: true, iconCls: 'icon-edit', onClick: function () {
                        var id = $(this).data("id");
                        editFun(id);
                    }
                });
                dgv.find(".btn_del").linkbutton({
                    plain: true, iconCls: 'icon-cancel', onClick: function () {
                        var id = $(this).data("id");
                        delFun(id);
                    }
                });
                dgv.find(".btn_view").linkbutton({
                    plain: true, iconCls: 'icon-book', onClick: function () {
                        var path = $(this).data("id");
                    }
                });
            },
            columns: [[
                {field: 'title', title: '滚动标题', width: 100, sortable: true},
                {field: 'coverPath', title: '展示图片', width: 200, sortable: true},
                {
                    field: 'type', title: '位置', width: 100, sortable: true, formatter: function (val, row) {
                    if (val == 1) {
                        return "热映影片";
                    } else if (val == 2) {
                        return "即将上映";
                    } else if (val == 3) {
                        return "活动";
                    } else if (val == 4) {
                        return "影院";
                    } else {
                        return "错误"
                    }
                }
                },
                {field: 'targetId', title: '目标ID', width: 100, sortable: true},
               /* {field: 'targetUrl', title: '目标URL', width: 150, sortable: true},*/
                {field:'image', title:'图片', width:100, align:'center',
                    formatter:function(value,row,index){return '<img src="http://www.vistacinemas.cn:9898'+row.coverPath+'" width="250" height="130" />';}
                },
                {
                    field: 'ids', title: '操作', width: 100, sortable: true, formatter: function (val, row) {
                    return "<a class='btn_edit' data-id='" + row.id + "'>编辑</a><a class='btn_del' data-id='" + row.id + "'>删除</a>";
                }
                }
            ]]
        });

        $.messager.progress("close");

        opt.reload = function () {
            options.table.datagrid("reload");
        }
    }

    $.fn.page_jdpush = function (options) {
        $.fn.loadPage(this, $.fn.page_jdpush.defaults, init);
    };
    $.fn.page_jdpush.defaults = {};
})(jQuery);