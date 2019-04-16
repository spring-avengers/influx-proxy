$(function () {
    var apiPrefix = "/management/retention/update";
    var e = "<i class='fa fa-times-circle'></i>";
    $("#retentionEditForm").validate({
        rules: {
            id:"required",
            retentionDuration: "required",
            isDefault: "required"
        },
        messages: {
            id: e + "请输入编号",
            retentionDuration: e + "请输入策略时间",
            isDefault: e + "请输入策略状态"
        },
        submitHandler: function (form) {
            swal({
                title: "修改策略？",
                text: "是否确定修改策略信息？",
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
                                title: "修改策略成功！",
                                text: "修改策略成功！",
                                type: "success"
                            }, function (e) {
                                removeIframe();
                            });
                        } else {
                            swal("修改策略失败！", "修改策略失败。", "error");
                        }
                    }
                });
            });
        }
    });
    $('#isDefault').val($('#fieldDefault').val());
});