function log(l) {
    console.log(l);
}

var Util = function () {
    return {
        checkJsonData: function (json) {
            if (json == undefined || json == "") {
                return undefined;
            }
            if (json instanceof Object) {
                return json;
            } else {
                return JSON.parse(json);
            }
        },
        downfile: function (app, id) {
            window.open(app + "/api/" + id + "/downloadFile");
        },
        getEvent: function () {
            return window.event || arguments.callee.caller.arguments[0];
        },
        killnull: function (v, c) {
            if (v == undefined || v == null || v == "") {
                if (c) {
                    return c;
                }
                return "";
            }
            return v;
        },
        getStrSize: function (str) {
            var realLength = 0, len = str.length, charCode = -1;
            for (var i = 0; i < len; i++) {
                charCode = str.charCodeAt(i);
                if (charCode >= 0 && charCode <= 128)
                    realLength += 1;
                else
                    realLength += 2;
            }
            return realLength;
        },
        cutStr: function (str, len) {
            var str_length = 0;
            var str_len = 0;
            str_cut = new String();
            str_len = str.length;
            for (var i = 0; i < str_len; i++) {
                a = str.charAt(i);
                str_length++;
                if (escape(a).length > 4) {
                    str_length++;
                }
                str_cut = str_cut.concat(a);
                if (str_length >= len) {
                    return str_cut;
                }
            }
            if (str_length < len) {
                return str;
            }
        }
    };
}();

(function ($) {
    $.fn.fillData = function (data) {
        var container = this;
        if (data != null) {
            for (var key in data) {
                var name = "[name=" + key + "]";
                var domArray = container.find(name);
                for (var j = 0; j < domArray.length; j++) {
                    var $dom = $(domArray[j]);
                    if ($dom.is(":radio") || $dom.is(":checkbox")) {
                        if ($dom.is(":radio") && $dom.val() == data[key]) {
                            $dom.attr("checked", true);
                        }
                        if ($dom.is(":checkbox") && data[key] == "0") {
                            $dom.attr("checked", true);
                        }
                    } else {
                        $dom.val(data[key]);
                    }
                    if ($dom.is("select")) {
                        $dom.change();
                    }
                }
            }
        }
    };
    $.fn.serializeObject = function () {
        var obj = new Object();
        $.each(this.serializeArray(), function (index, param) {
            if (!(param.name in obj)) {
                obj[param.name] = param.value;
            }
        });
        return obj;
    };
})(jQuery);

/**

 * js原有方法扩展,弥补js的不足-----------------------------------------------------------------start

 */

// 数组的删除方法


Array.prototype.remove = function (dx) {
    if (isNaN(dx) || dx > this.length) {
        return false;
    }
    for (var i = 0, n = 0; i < this.length; i++) {
        if (this[i] != this[dx]) {
            this[n++] = this[i];
        }
    }
    this.length -= 1;
};
// 数组删除对象方法


Array.prototype.removeObj = function (dx) {
    for (var i = 0, n = 0; i < this.length; i++) {
        if (this[i] != dx) {
            this[n++] = this[i];
        }
    }
    this.length -= 1;
};
// 获取一个对象的index


Array.prototype.indexOf = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val)
            return i;
    }
    return -1;
};
// 日期格式化方法


Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

/**

 * js原有方法扩展,弥补js的不足-----------------------------------------------------------------end

 */