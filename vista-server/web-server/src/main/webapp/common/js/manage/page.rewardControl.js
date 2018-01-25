(function ($) {
    function init(target) {
        //初始化基础设置
        var options = $(target).data('options');
        var opt = options.options;

        var addFun = function () {
            $.messager.progress("show", "加载中...");
            var editDialog = $("<div></div>");
            editDialog.dialog({
                title: '信息编辑',
                width: 360,
                height: 370,
                href: opt.appUrl + "m_rewardControl/toEdit",
                onLoad: function () {
                    $.messager.progress("close");
                    if (editDialog.find('#theform').length > 0) {
                        editDialog.find('#theform').form({
                            url: opt.appUrl + "m_rewardControl/create",
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

        var editFun = function (rid, rtype, rchance, remark) {
            $.messager.progress("show", "加载中...");
            var editDialog = $("<div></div>");
            editDialog.dialog({
                title: '信息编辑',
                width: 360,
                height: 370,
                href: opt.appUrl + "m_rewardControl/toEdit",
                onLoad: function () {
                    $.messager.progress("close");
                    if (editDialog.find('#theform').length > 0) {
                        editDialog.find('#theform').form({
                            url: opt.appUrl + "m_rewardControl/update",
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
                        editDialog.find('#theform').form('load', {
                            rid: rid,
                            rtype: rtype,
                            rchance: rchance,
                            remark: remark
                        });
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

        var delFun = function (rid, rtype) {
            $.messager.confirm('警告', '确认删除这条记录?', function (r) {
                if (r) {
                    $.messager.progress("show", "加载中...");
                    $.wow_post(opt.appUrl + "m_rewardControl/del", {rid: rid, type: rtype}, function (s, data) {
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
            url: opt.appUrl + 'm_rewardControl/getPager',
            toolbar: [
                {
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        addFun();
                    }
                }
            ],
            onLoadSuccess: function (data) {
                var dgv = $(this).parent();
                dgv.find(".btn_edit").linkbutton({
                    plain: true, iconCls: 'icon-edit', onClick: function () {
                        var rid = $(this).data("rid");
                        var rtype = $(this).data("rtype");
                        var rchance = $(this).data("rchance");
                        var remark = $(this).data("remark");
                        editFun(rid,rtype,rchance,remark);
                    }
                });
                dgv.find(".btn_del").linkbutton({
                    plain: true, iconCls: 'icon-cancel', onClick: function () {
                        var rid = $(this).data("rid");
                        var rtype = $(this).data("rtype");
                        delFun(rid, rtype);
                    }
                });
            },
            columns: [[
                {field: 'rid', title: '奖品ID', width: 150, sortable: true},
                {
                    field: 'rtype', title: '类型', width: 150, sortable: true, formatter: function (val, row) {
                    if (val == 1) {
                        return "大转盘";
                    } else {
                        return "错误";
                    }
                }
                },
                {field: 'rchance', title: '中奖概率', width: 150, sortable: true},
                {field: 'remark', title: '备注', width: 150, sortable: true},
                {
                    field: 'ids', title: '操作', width: 150, sortable: true, formatter: function (val, row) {
                    return "<a class='btn_edit' data-rid='" + row.rid + "' data-rtype='" + row.rtype + "'   data-rchance='" + row.rchance + "'   data-remark='" + row.remark + "'         >编辑</a><a class='btn_del' data-rid='" + row.rid + "' data-rtype='" + row.rtype + "'>删除</a>";
                }
                }
            ]]
        });

        $.messager.progress("close");

        opt.reload = function () {
            options.table.datagrid("reload");
        }
    }

    $.fn.page_rewardControl = function (options) {
        $.fn.loadPage(this, $.fn.page_rewardControl.defaults, init);
    };
    $.fn.page_rewardControl.defaults = {};
})(jQuery);