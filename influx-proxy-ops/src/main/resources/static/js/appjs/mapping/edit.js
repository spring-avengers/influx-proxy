$(function () {
    var apiPrefix = "/management/keyMappings/update";
    var e = "<i class='fa fa-times-circle'></i>";
    $("#mappingEditForm").validate({
        rules: {
            id:"required",
            backendNodeNames: "required",
            databaseRegex: "required",
            measurementRegex: "required"
        },
        messages: {
            id: e + "请输入编号",
            backendNodeNames: e + "请输入名称",
            databaseRegex: e + "请输入数据库规则",
            measurementRegex: e + "请输入测量规则"
        },
        submitHandler: function (form) {
            swal({
                title: "修改映射？",
                text: "是否确定修改映射信息？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "保存",
                closeOnConfirm: false,
                showLoaderOnConfirm: true
            }, function () {
                $.ajax({
                    cache: true,
                    type: "PUT",
                    url: apiPrefix,
                    data: JSON.stringify($(form).serializeObject()),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {
                        if (ajaxIsSuccess(data)) {
                            swal({
                                title: "修改映射成功！",
                                text: "修改映射成功！",
                                type: "success"
                            }, function (e) {
                                removeIframe();
                            });
                        } else {
                            swal("修改映射失败！", "修改映射失败。", "error");
                        }
                    }
                });
            });
        }
    });
});