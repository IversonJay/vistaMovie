(function ($) {
    var reloadD = function (appurl) {

        $.messager.confirm('警告', '确认进行同步？同步可能需要比较长的时间。。', function (r) {
            if (r) {
                $.messager.progress("show", "同步可能需要比较长的时间，请耐心等待。。。", true);
                $.wow_post(appurl + 'm_system/reloadD', {}, function (s, data) {
                    if (s) {
                        Util.showMessage('提示信息', '处理成功！');
                    } else {
                        $.messager.alert('警告', '处理失败!');
                    }
                    $.messager.progress("close");
                });
            }
        });
    }

    var reloadC = function (appurl) {
        $.messager.confirm('警告', '确认进行同步？同步可能需要比较长的时间。。', function (r) {
            if (r) {
                $.messager.progress("show", "同步可能需要比较长的时间，请耐心等待。。。", true);
                $.wow_post(appurl + 'm_system/reloadC', {}, function (s, data) {
                    if (s) {
                        Util.showMessage('提示信息', '处理成功！');
                    } else {
                        $.messager.alert('警告', '处理失败!');
                    }
                    $.messager.progress("close");
                });
            }
        });

    }
    var reloadM = function (appurl) {
        $.messager.confirm('警告', '确认进行同步？同步可能需要比较长的时间。。', function (r) {
            if (r) {
                $.messager.progress("show", "同步可能需要比较长的时间，请耐心等待。。。", true);
                $.wow_post(appurl + 'm_system/reloadM', {}, function (s, data) {
                    if (s) {
                        Util.showMessage('提示信息', '处理成功！');
                    } else {
                        $.messager.alert('警告', '处理失败!');
                    }
                    $.messager.progress("close");
                });
            }
        });
    }
    
    
    var reloadT = function (appurl) {
        $.messager.confirm('警告', '确认进行同步？同步可能需要比较长的时间。。', function (r) {
            if (r) {
                $.messager.progress("show", "同步可能需要比较长的时间，请耐心等待。。。", true);
                $.wow_post(appurl + 'm_system/reloadT', {}, function (s, data) {
                    if (s) {
                        Util.showMessage('提示信息', '处理成功！');
                    } else {
                        $.messager.alert('警告', '处理失败!');
                    }
                    $.messager.progress("close");
                });
            }
        });
    }
    
    //添加页
    var addTab = function (target, name) {
        //初始化基础设置
        var options = $(target).data('options');
        var opt = options.options;
        var tab = options.tab;
        if (tab.tabs("exists", name)) {
            tab.tabs("select", name);
            return tab.tabs("getTab", name).find(".wow_panel");
        } else {
            var content = $("<div class='wow_panel'/>")
            tab.tabs("add", {
                title: name,
                content: content[0],
                closable: true
            });
            return content;
        }
    }
    

    //初始化方法
    function init(target) {
        //初始化基础设置
        var options = $(target).data('options');
        var opt = options.options;
        //获取存储的主题
        var nowTheme = $.cookie('theme');
        if (nowTheme == undefined) {
            nowTheme = "metro-blue";
        }
        var themeLink = $("<link>").attr({
            rel: "stylesheet",
            type: "text/css",
            href: opt.appUrl + opt.easyuiroot + "/themes/" + nowTheme + "/easyui.css"
        }).appendTo("head");
        //初始化北面
        var north = $("<div region='north'><div style='float:left;padding:0px 20px;'><h1>系统后台管理</h1></div><div id='menu' style='position:absolute; right:10px; bottom:0;padding:5px;'></div></div>");
        $(target).append(north);

        //初始化西面
        var west = $("<div region='west' title='导航栏' style='width:190px;overflow:hidden;overflow:auto;'></div>");
        $(target).append(west);

        //初始化中间
        var center = $("<div region='center' title='欢迎回来：" + opt.username + "'><div class='easyui-tabs' fit='true' border='false'></div></div>");
        $(target).append(center);

        //===================上方导航初始化start===================

        //菜单初始化
        var personSet_menu = $("<div>" +
            "<div id='changePwd'>修改密码</div>" +
            "<div data-options=\"iconCls:'icon-theme'\">" +
            "<span>修改主题</span>" +
            "<div>" +
            "<div id='theme' data-themename='default'>default</div>" +
            "<div id='theme' data-themename='bootstrap' >bootstrap</div>" +
            "<div id='theme' data-themename='black'>black</div>" +
            "<div id='theme' data-themename='gray'>gray</div>" +
            "<div id='theme' data-themename='metro'>metro</div>" +
            "<div class='menu-sep'></div>  " +
            "<div id='theme' data-themename='metro-blue'>Metro Blue</div>" +
            "<div id='theme' data-themename='metro-green'>Metro Green</div>" +
            "<div id='theme' data-themename='metro-gray'>Metro Gray</div>" +
            "<div id='theme' data-themename='metro-orange');'>Metro Orange</div>" +
            "<div id='theme' data-themename='metro-red'>Metro Red</div>" +
            "</div>" +
            "</div>" +
                //"<div data-options=\"iconCls:'icon-redo'\">锁定系统</div>" +
            "<div id='quitLogin' data-options=\"iconCls:'icon-redo'\">退出登录</div> " +
            "</div>");

        personSet_menu.find("#quitLogin").bind('click', function () {
            window.location.href = opt.appUrl + "m_manageUser/quitLogin";
        });
        personSet_menu.find("#changePwd").bind('click', function () {
            var changePwdDialog = $("<div></div>");
            changePwdDialog.dialog({
                title: '修改密码',
                width: 360,
                height: 300,
                href: opt.appUrl + "m_manageUser/toChangePwd",
                onLoad: function () {
                    $.messager.progress("close");
                    if (changePwdDialog.find('#theform').length > 0) {
                        changePwdDialog.find('#theform').form({
                            url: opt.appUrl + "m_manageUser/changePwd",
                            success: function (data) {
                                log(data)
                                if ($.wow_analyseReturn(data, function (r) {
                                        if (r) {
                                            changePwdDialog.dialog("close");
                                            Util.showMessage('提示信息', '提交成功！');
                                        }
                                    }));
                                $.messager.progress("close");
                            },
                            onSubmit: function () {
                                if (changePwdDialog.find('#theform').form('validate')) {
                                    if (changePwdDialog.find('#theform #password1').textbox("getValue") == changePwdDialog.find('#theform #password2').textbox("getValue")) {
                                        $.messager.progress("show", "提交中...");
                                        return true;
                                    } else {
                                        $.messager.alert('提示信息', '两次输入密码不一致！');
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            }
                        });
                    } else {
                        changePwdDialog.dialog("close");
                    }
                },
                buttons: [{
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        changePwdDialog.find('#theform').submit();
                    }
                }, {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        changePwdDialog.dialog("close");
                    }
                }]
            });
        });


        personSet_menu.find("div[data-themename='" + nowTheme + "']").attr('data-options', "{iconCls : 'icon-ok'}");
        //修改主题
        personSet_menu.menu({
            onClick: function (item) {
                if (item.id == "theme") {
                    var theme = $(item.target).data("themename");
                    $.messager.progress("show", "加载中...");

                    var href = opt.appUrl + opt.easyuiroot + "/themes/" + theme + "/easyui.css";
                    themeLink.attr('href', href);
                    $.cookie('theme', theme);
                    var menus = $(item.target).parent().children("div.menu-item");
                    for (var i = 0; i < menus.length; i++) {
                        personSet_menu.menu('setIcon', {
                            target: menus[i],
                            iconCls: 'emptyIcon'
                        });
                    }
                    personSet_menu.menu('setIcon', {
                        target: item.target,
                        iconCls: 'icon-ok'
                    });

                    $.messager.progress("close");
                }
            }
        });

        var btn_personSet = $("<div/>");
        btn_personSet.menubutton({iconCls: 'icon-control', text: "个人设置", plain: true});
        btn_personSet.bind('click', function () {
            personSet_menu.menu("show", {alignTo: btn_personSet})
        });
        north.find("#menu").append(btn_personSet);

        //===================上方导航初始化end===================


        //===================导航栏初始化start===================

        var jsondata = [{
            "text": "APP管理",
            "iconCls": "icon-save",
            "children": [{
                "text": "滚动管理",
                "url": "page_roll"
            }, {
                "text": "广告管理",
                "url": "page_ads"
            },/* {
                "text": "抽奖配置",
                "url": "page_rewardControl"
            }, */{
                "text": "统计管理",
                "url": "page_statistics"
            }]
        }, {
            "text": "系统管理",
            "children": [{
                "text": "系统用户",
                "url": "page_manager"
            }, {
                "text": "城市列表",
                "url": "page_city"
            }, {
                "text": "系统设置",
                "url": "page_systemValue"
            }]
        }, {
            "text": "缓存管理",
            "children": [{
                "text": "重置缓存数据库",
                "url": "reloadD"
            }, {
                "text": "同步电影信息",
                "url": "reloadC"
            }, {
                "text": "同步票类信息",
                "url": "reloadT"
            }, {
            	"text": "同步电影类型信息",
            	"url": "reloadM"
            }]
        }, {
            "text": "影片管理",
            "children": [{
                "text": "正在热映影片排序",
                "url": "movie_Sort"
            },{
                "text": "即将上映影片排序",
                "url": "movie_SortR"
            }]
        }, {
            "text": "票类管理",
            "children": [{
                "text": "尊享卡俱乐部",
                "url": "ticket_ZX"
            },{
                "text": "海上明珠俱乐部",
                "url": "ticket_HS"
            }]
        }, {
            "text": "卖品管理",
            "children": [{
                "text": "卖品管理",
                "url": "connection_CZ"
            }]
        }];

        var tree = '<ul id="tt"></ul>';

        west.append(tree);
        $('#tt').tree({
            data: jsondata,
            onClick: function (node) {
                if (node.children == undefined || node.children.length == 0) {
                    if (node.url.indexOf("page_") == 0) {
                        eval("$(addTab(target, '" + node.text + "'))." + node.url + "()");
                    } else if (node.url.indexOf("movie_") == 0) {
                        eval("$(addTab(target, '" + node.text + "'))." + node.url + "()");
                    } else if (node.url.indexOf("ticket_") == 0) {
                        eval("$(addTab(target, '" + node.text + "'))." + node.url + "()");
                    } else if (node.url.indexOf("connection_") == 0) {
                        eval("$(addTab(target, '" + node.text + "'))." + node.url + "()");
                    }else {
                        eval(node.url + "('" + opt.appUrl + "')");
                    }
                }
            }
        });

        //===================导航栏初始化end===================

        options.tab = center.find(".easyui-tabs").tabs({tabPosition: "bottom"});
        //options.tab.tabs("add", {
        //    title: "系统监控"
        //});
        setTimeout(function () {
            $(target).layout();
            $.messager.progress("close");
        }, 100);
    }

    $.fn.page_main = function (cmd, options) {
        if (typeof cmd == 'string') {
            return $.fn.page_main.methods[cmd](this, options);
        } else {
            $.messager.progress("show", "正在处理，请稍待。。。", false);
            $(this).data('options', {options: $.extend({}, $.fn.page_main.defaults, cmd, $.fn.baseOptions)});
            init(this);
            return $(this);
        }
    };
    $.fn.page_main.methods = {
        addTab: function (target, name) {
            return addTab(target, name);
        }
    };
    $.fn.page_main.defaults = {
        username: ""
    };
    
//    
//    $.fn.movieSort = function (options) {
//        $.fn.loadPage(this, $.fn.movieSort.defaults, init);
//    };
//    $.fn.movieSort.defaults = {};
    
})(jQuery);