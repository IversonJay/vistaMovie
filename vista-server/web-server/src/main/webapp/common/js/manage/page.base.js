(function ($) {
    //初始化一些基本属性，用于整个系统
    $.fn.baseOptions = {
        appUrl: "",
        easyuiroot: "",
        fileServerUrls: {},
        oldFileServerUrl: "http://121.42.25.253/file/api/[file_id]/downloadFile",
        cmntId: "",
        reload: function () {
        }
    };

    $.fn.loadPage = function (target, defaultOpt, init, opt) {
        var pageopt = $(target).data('options');
        if (pageopt == null) {
            $(target).data('options', {options: $.extend({}, defaultOpt, opt, $.fn.baseOptions)});
            $.messager.progress("show", "加载中...");
            init(target)
        } else {
            pageopt.options.reload();
        }
    }


    Util.showMessage = function (title, message) {
        $.messager.show({
            title: title,
            msg: message,
            timeout: 5000,
            showType: 'fade'
        });
    };
    //给日期选择添加清空按钮
    $.extend($.fn.datebox.defaults, {
        buttons: [{
            text: function (_a4c) {
                return '今天';
            }, handler: function (_a4d) {
                var now = new Date();
                $(_a4d).datebox("setValue", now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate());
                $(_a4d).datebox("hidePanel");
            }
        }, {
            text: function (_a4e) {
                return "清空";
            }, handler: function (_a4f) {
                $(_a4f).datebox("setValue", "");
                $(_a4f).datebox("hidePanel");
            }
        }]
    });

    //面板
    $.extend($.fn.panel.defaults, {
        cache: true,
        extractor: function (json) {
            var r = Util.checkJsonData(json);
            if (r == undefined || r == "" || r == null) {
                $.messager.alert('警告', '系统错误!');
                return;
            }
            if (r.state > 0) {
                return r.result;
            } else {
                $.messager.alert('警告', r.msg);
            }
            $(this).window("destroy");
            return "";
        }
    });

    //弹出窗口拦截
    $.extend($.fn.dialog.defaults, {
        cache: true,
        shadow: false,
        destroyInClose: true,
        onClose: function () {
            if ($(this).dialog("options").destroyInClose) {
                $(this).dialog("destroy");
            }
        },
        modal: true,
        extractor: function (json) {
            var r = Util.checkJsonData(json);
            if (r == undefined || r == "" || r == null) {
                $.messager.alert('警告', '系统错误!');
                return;
            }
            if (r.state > 0) {
                return r.result;
            } else {
                $.messager.alert('警告', r.msg);
            }
            $(this).window("destroy");
            return "";
        },
        onLoadError: function () {
            $(this).dialog("close");
        }
    });


    $.extend($.fn.datagrid.defaults, {
        fit: true,
        border: false,
        fitColumns: true,
        pagination: true,
        selectOnCheck: false,
        checkOnSelect: false,

        singleSelect: true,
        scrollbarSize: 0,
        loadFilter: function (r) {
            if (r == undefined || r == "" || r == null) {
                $.messager.alert('警告', '系统错误!');
                return;
            }
            if (r.state > 0) {
                return r.result;
            } else {
                $.messager.alert('警告', '系统错误!');
            }
            var r = {rows: [], totle: 0}
            return r;
        },
        onLoadError: function () {
            $.messager.alert('警告', '系统错误!');
            $.messager.progress("close");
        }
    });

    //自定义进度条
    $.wow_post = function (path, param, callback) {
        $.post(path, param, function (json) {
            $.wow_analyseReturn(json, callback);
        });
    };
    $.wow_analyseReturn = function (r, callback) {
        if (r == undefined || r == "" || r == null) {
            $.messager.alert('警告', '系统错误!');
            return;
        }
        var data = Util.checkJsonData(r);
        if (data.state > 0) {
            callback(true, data.result);
        } else {
            $.messager.alert('警告', data.msg);
            callback(false);
        }
    };

    var progress = {
        load: null,
        num: 0,
        resize: function () {
            if (progress.load != null) {
                var _swidth = window.innerWidth;
                var _sheight = window.innerHeight;
                progress.load.width(_swidth);
                progress.load.height(_sheight);
                var d = progress.load.find("#dialog");
                d.css("left", (_swidth - d[0].scrollWidth - 20) / 2 + "px");
                d.css("top", (_sheight - d[0].scrollHeight - 20) / 2 + "px");
            }
        },
        hide: function () {
            if (progress.num > 0) {
                progress.num = progress.num - 1;
            } else {
                progress.num = 0;
            }
            if (progress.num == 0) {
                if (progress.load != undefined) {
                    progress.load.remove();
                    progress.load = null;
                }
            }
        },
        show: function (message, isTransparent) {
            progress.num++;
            var _swidth = window.innerWidth;
            var _sheight = window.innerHeight;
            if (message == undefined) {
                message = '数据加载中...';
            }
            if (progress.load == null) {
                var bc = "204,204,204,0.4";
                if (!isTransparent) {
                    bc = "256,256,256,1";
                }
                progress.load = $("<div style='background:rgba(" + bc + ");width:400px;height:400px;position:fixed;top:0px;left:0px;z-index:99999'></div>");
                progress.load.append("<div id='dialog' style='font-size: 12px;border:2px solid #95B8E7;color:#000;background:#fff;padding:10px;position:absolute;width:auto;height:20px;line-height:20px;'><img align='absmiddle' style='margin-right:10px' src='" + $.fn.baseOptions.appUrl + $.fn.baseOptions.easyuiroot + "/themes/default/images/loading.gif'/>" + message + "</div>");
                $("body").append(progress.load);

                progress.resize();
            }
        }
    }
    $(window).resize(function () {
        progress.resize();
    });

    $.messager.progress = function (p, m, isTransparent) {
        if (p == "show") {
            if (isTransparent == undefined) {
                isTransparent = true;
            }
            progress.show(m, isTransparent);
        } else if (p == "resize") {
            progress.resize();
        } else {
            progress.hide();
        }
    }
})(jQuery);
function getFileServerUrls() {
    $.wow_post($.fn.baseOptions.appUrl + "m_common/getFileServer", {}, function (s, data) {
        if (s) {
            for (var i = 0; i < data.length; i++) {
                $.fn.baseOptions.fileServerUrls[data[i].id] = data[i].url;
            }
        }
    });
}