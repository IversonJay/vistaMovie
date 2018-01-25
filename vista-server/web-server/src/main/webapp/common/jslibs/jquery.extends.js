jQuery.prototype.serializeObject = function () {
    var obj = new Object();
    $.each(this.serializeArray(), function (index, param) {
        if (!(param.name in obj)) {
            obj[param.name] = param.value;
        }
    });
    return obj;
};

jQuery.prototype.formLoadData = function (data) {
    $(this)[0].reset();
    for (var p in data) {
        $(this).find("#" + p).val(data[p]);
    }
};
