$(function () {
    var e = "<i class='fa fa-times-circle'></i> ";
    $("#nodeForm").validate({
        rules: {
            name: "required",
            online: "required",
            url: "required",
            writeTimeout: "required",
            queryTimeout: "required"
        },
        messages: {
            name: e + "请输入名称",
            online: e + "请输入状态",
            url: e + "请输入链接",
            writeTimeout: e + "请输入超时时间",
            queryTimeout: e + "请输入超时时间"
        },
        submitHandler: function (form) {
            swal({
                title: "新增节点？",
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
                    url: "/management/nodes",
                    data: JSON.stringify($(form).serializeObject()),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (data) {
                        if (ajaxIsSuccess(data)) {
                            swal({
                                title: "新增节点成功！",
                                text: "新增节点成功！",
                                type: "success"
                            }, function (e) {
                                removeIframe();
                            });
                        } else {
                            swal("新增节点失败！", "新增节点失败。", "error");
                        }
                    }
                });
            });
        }
    });
});