$(function () {
    var e = "<i class='fa fa-times-circle'></i> ";
    $("#mappingForm").validate({
        rules: {
            id: "required",
            backendNodeNames: "required",
            databaseRegex: "required",
            measurementRegex: "required"
        },
        messages: {
            id: "required",
            backendNodeNames: e + "请输入名称",
            databaseRegex: e + "请输入数据库规则",
            measurementRegex: e + "请输入测量规则"
        },
        submitHandler: function (form) {
            swal({
                title: "新增映射？",
                text: "是否确定保存？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "保存",
                closeOnConfirm: false,
                showLoaderOnConfirm: true
            }, function () {
                $.ajax({
                    cache: true,
                    type: "POST",
                    url: "/management/keyMappings",
                    data: JSON.stringify($(form).serializeObject()),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {
                        if (ajaxIsSuccess(data)) {
                            swal({
                                title: "新增映射成功！",
                                text: "新增映射成功！",
                                type: "success"
                            }, function (e) {
                                removeIframe();
                            });
                        } else {
                            swal("新增映射失败！", "新增映射失败。", "error");
                        }
                    }
                });
            });
        }
    });
});