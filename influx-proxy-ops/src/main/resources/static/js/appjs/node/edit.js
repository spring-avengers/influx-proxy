$(function () {
    var apiPrefix = "/management/nodes/update";
    var e = "<i class='fa fa-times-circle'></i>";
    $("#nodeEditForm").validate({
        rules: {
            id:"required",
            name: "required",
            online: "required",
            url: "required",
            writeTimeout: "required",
            queryTimeout: "required"
        },
        messages: {
            id: e + "请输入编号",
            name: e + "请输入名称",
            online: e + "请输入状态",
            url: e + "请输入链接",
            writeTimeout: e + "请输入超时时间",
            queryTimeout: e + "请输入超时时间"
        },
        submitHandler: function (form) {
            swal({
                title: "修改节点？",
                text: "是否确定修改节点信息？",
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
                                title: "修改节点成功！",
                                text: "修改节点成功！",
                                type: "success"
                            }, function (e) {
                                removeIframe();
                            });
                        } else {
                            swal("修改节点失败！", "修改节点失败。", "error");
                        }
                    }
                });
            });
        }
    });

    $('#online').val($('#fieldOnline').val());
});