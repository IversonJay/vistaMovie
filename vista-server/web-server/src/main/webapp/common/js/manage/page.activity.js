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
                width: 670,
                height: 750,
                href: opt.appUrl + "m_activity/toEdit",
                onLoad: function () {
                    $.messager.progress("close");
                    if (editDialog.find('#theform').length > 0) {
                        editDialog.find('#theform').form({
                            url: opt.appUrl + "m_activity/save",
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

                        editDialog.dialog("options").editor = KindEditor.create(editDialog.find('#content'), {
                            width: "606px",
                            height: "390px;",
                            minWidth: "400px",
                            resizeType: 0,
                            uploadJson: opt.appUrl + "m_goods/uploadimg",               //指定上传文件的服务器端程序
                            // fileManagerJson:,          // 指定浏览远程图片的服务器端程序
                            allowImageRemote: true,
                            allowFileManager: false,
                            afterBlur: function () {
                                this.sync();
                            },
                            items: [
                                'undo', 'redo', '|', 'preview', 'cut', 'copy', 'paste',
                                'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                                'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'clearhtml', 'quickformat', 'selectall', '/',
                                'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                                'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
                                'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'pagebreak', 'anchor'
                            ]
                        });
                        if (id == undefined) {
                        } else {
                            // 加载数据,加载完毕打开窗口
                            $.wow_post(opt.appUrl + 'm_activity/find', {id: id}, function (s, data) {
                                if (s) {
                                    editDialog.find('#theform').form('load', data);

                                    editDialog.dialog("options").editor.html(data.content);
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
                        editDialog.dialog("options").editor.remove();
                        editDialog.find('#theform').submit();
                    }
                }, {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        editDialog.dialog("options").editor.remove();
                        editDialog.dialog("close");
                    }
                }]
            });
        }

        var delFun = function (id) {
            $.messager.confirm('警告', '确认删除这条信息?', function (r) {
                if (r) {
                    $.messager.progress("show", "加载中...");
                    $.wow_post(opt.appUrl + "m_activity/del", {id: id}, function (s, data) {
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
            url: opt.appUrl + 'm_activity/getPager',
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
            },
            columns: [[
                {field: 'aname', title: '活动名称', width: 150, sortable: true},
                {
                    field: 'state', title: '活动状态', width: 150, sortable: true, formatter: function (val, row) {
                    if(val==-1){
                        return '关闭';
                    }else{
                        return "打开";
                    }
                }
                },
                {
                    field: 'stime', title: '开始时间', width: 150, sortable: true, formatter: function (val, row) {
                    return new Date(parseInt(val)).toLocaleString();
                }
                },
                {
                    field: 'etime', title: '结束时间', width: 150, sortable: true, formatter: function (val, row) {
                    return new Date(parseInt(val)).toLocaleString();
                }
                },
                {
                    field: 'ids', title: '操作', width: 150, sortable: true, formatter: function (val, row) {
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

    $.fn.page_activity = function (options) {
        $.fn.loadPage(this, $.fn.page_activity.defaults, init);
    };
    $.fn.page_activity.defaults = {};
})(jQuery);