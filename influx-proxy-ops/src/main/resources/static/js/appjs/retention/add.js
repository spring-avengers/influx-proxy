$(function () {
    var e = "<i class='fa fa-times-circle'></i> ";
    $("#retentionForm").validate({
        rules: {
            nodeUrl: "required",
            databaseName: "required",
            retentionName: "required",
            retentionDuration: "required",
            retentionDefault: "required"
        },
        messages: {
            nodeUrl: e + "请输入链接",
            databaseName: e + "请输入数据库名称",
            retentionName: e + "请输入策略名称",
            retentionDuration: e + "请输入策略时间",
            retentionDefault: e + "请输入默认状态"
        },
        submitHandler: function (form) {
            swal({
                title: "新增策略？",
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
                    url: "/management/retention",
                    data: JSON.stringify($(form).serializeObject()),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {
                        if (ajaxIsSuccess(data)) {
                            swal({
                                title: "新增策略成功！",
                                text: "新增策略成功！",
                                type: "success"
                            }, function (e) {
                                removeIframe();
                            });
                        } else {
                            swal("新增策略失败！", "新增策略失败。", "error");
                        }
                    }
                });
            });
        }
    });
});