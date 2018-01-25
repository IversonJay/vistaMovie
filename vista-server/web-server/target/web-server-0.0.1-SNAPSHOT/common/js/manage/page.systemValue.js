(function ($) {
    function init(target) {


        var editFun = function (tkey, value) {
            var title = "";
            var url = "";
            var height = 200;
            var width = 460;
            if (tkey == 1000) {
                title = "服务器地址";
                url = opt.appUrl + "m_systemValue/toEdit_input";
                height = 200;
            } else if (tkey == 1001) {
                title = "服务器密钥";
                url = opt.appUrl + "m_systemValue/toEdit_input";
                height = 200;
            } else if (tkey == 1002) {
                title = "电影封面地址,实际使用时,${movieID}会被替换成电影ID";
                url = opt.appUrl + "m_systemValue/toEdit_input";
                height = 200;
            } else if (tkey == 1003) {
                title = "电影海报地址,实际使用时,${movieID}会被替换成电影ID";
                url = opt.appUrl + "m_systemValue/toEdit_input";
                height = 200;
            } else if (tkey == 1004) {
                title = "卖品地址,实际使用时,${movieID}会被替换成电影ID";
                url = opt.appUrl + "m_systemValue/toEdit_input";
                height = 200;
            } else if (tkey == 1006) {
                title = "提前停售时间(分钟)";
                url = opt.appUrl + "m_systemValue/toEdit_num";
                height = 150;
            } else if (tkey == 1007) {
                title = "大转盘总概率";
                url = opt.appUrl + "m_systemValue/toEdit_num";
                height = 150;
            } else if (tkey == 1008) {
                title = "用户条款";
                url = opt.appUrl + "m_systemValue/toEdit_html";
                height = 620;
                width = 730;
            } else if (tkey == 1009) {
                title = "大转盘活动规则";
                url = opt.appUrl + "m_systemValue/toEdit_html";
                height = 620;
                width = 730;
            } else if (tkey == 1010) {
                title = "关于我们内容";
                url = opt.appUrl + "m_systemValue/toEdit_html";
                height = 620;
                width = 730;
            } else if (tkey == 1020) {
                title = "CLIENT_CLASS";
                url = opt.appUrl + "m_systemValue/toEdit_input";
                height = 620;
                width = 730;
            } else if (tkey == 1021) {
                title = "CLIENT_ID";
                url = opt.appUrl + "m_systemValue/toEdit_input";
                height = 620;
                width = 730;
            } else if (tkey == 1022) {
                title = "CLIENT_NAME";
                url = opt.appUrl + "m_systemValue/toEdit_input";
                height = 620;
                width = 730;
            } else if (tkey == 1023) {
                title = "海上明珠影业俱乐部";
                url = opt.appUrl + "m_systemValue/toEdit_num";
                height = 620;
                width = 730;
            } else if (tkey == 1024) {
                title = "尊享卡会员俱乐部";
                url = opt.appUrl + "m_systemValue/toEdit_num";
                height = 620;
                width = 730;
            }else {
                return;
            }
            var editDialog = $("<div></div>");
            editDialog.dialog({
                title: title,
                width: width,
                height: height,
                href: url,
                onLoad: function () {
                    if (editDialog.find('#theform').length > 0) {
                        editDialog.find('#theform').form({
                            url: opt.appUrl + "m_systemValue/save",
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
                        if (url == opt.appUrl + "m_systemValue/toEdit_html") {
                            editDialog.dialog("options").editor = KindEditor.create(editDialog.find('#value'), {
                                width: editDialog.find('.tabs-panels').width() - 60,
                                height: 500,
                                minHeight: 200,
                                resizeType: 0,
                                uploadJson: opt.appUrl + "m_goods/uploadimg",               //指定上传文件的服务器端程序
                                // fileManagerJson:,          // 指定浏览远程图片的服务器端程序
                                allowImageRemote: true,
                                allowFileManager: false,
                                afterBlur: function () {
                                    this.sync();
                                },
                                items: [
                                    'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
                                    'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                                    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                                    'superscript', 'clearhtml', 'quickformat', 'selectall', '/',
                                    'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                                    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                                    'anchor', 'link', 'unlink', '|', 'about'
                                ]
                            });

                            editDialog.dialog("options").editor.html(value);
                        }


                        editDialog.find('#theform').form('load', {key: tkey, value: value});
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

        //初始化基础设置
        var options = $(target).data('options');
        var opt = options.options;
        options.table = $(target).datagrid({
            pagination: false,
            url: opt.appUrl + 'm_systemValue/getAll',
            onLoadSuccess: function (data) {
                var dgv = $(this).parent();
                dgv.find(".btn_edit").linkbutton({
                    plain: true, iconCls: 'icon-edit', onClick: function () {
                        var tkey = $(this).data("key");
                        var value = $(this).data("value");
                        editFun(tkey, value);
                    }
                });
            },
            columns: [[
                {
                    field: 'key', title: '项目', width: 150, sortable: true, formatter: function (val, row) {
                    if (val == 1000) {
                        return "服务器地址";
                    } else if (val == 1001) {
                        return "服务器密钥";
                    } else if (val == 1002) {
                        return "电影封面地址,实际使用时,${movieID}会被替换成电影ID";
                    } else if (val == 1003) {
                        return "电影海报地址,实际使用时,${movieID}会被替换成电影ID";
                    } else if (val == 1004) {
                        return "卖品地址,实际使用时,${movieID}会被替换成电影ID";
                    } else if (val == 1006) {
                        return "提前停售时间";
                    } else if (val == 1007) {
                        return "大转盘总概率";
                    } else if (val == 1008) {
                        return "用户条款";
                    } else if (val == 1009) {
                        return "大转盘活动规则";
                    } else if (val == 1010) {
                        return "关于我们内容";
                    } else if (val == 9999) {
                        return "当前会员卡流水号";
                    } else if (val == 1020) {
                        return "CLIENT_CLASS";
                    } else if (val == 1021) {
                        return "CLIENT_ID";
                    } else if (val == 1022) {
                        return "CLIENT_NAME";
                    } else if (val == 1023) {
                        return "海上明珠影业俱乐部";
                    } else if (val == 1024) {
                        return "尊享卡会员俱乐部";
                    }  else {
                        return val;
                    }
                }
                },
                {field: 'value', title: '内容', width: 150, sortable: true},
                {
                    field: 'ids', title: '操作', width: 150, sortable: true, formatter: function (val, row) {
                    if (row.key != 9999) {
                        return "<a class='btn_edit' data-key='" + row.key + "' data-value='" + row.value + "'>编辑</a>";
                    } else {
                        return "";
                    }
                }
                }
            ]]
        });

        $.messager.progress("close");

        opt.reload = function () {
            options.table.datagrid("reload");
        }
    }

    $.fn.page_systemValue = function (options) {
        $.fn.loadPage(this, $.fn.page_systemValue.defaults, init);
    };
    $.fn.page_systemValue.defaults = {};
})(jQuery);