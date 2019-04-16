var pagePrefix = "/node";
var deletePrefix = "/management/nodes";
$(function () {
    load();
});

function load() {
    $('#nodeTable').bootstrapTable({
        type: "GET",
        url: "/management/nodes",
        striped: true,
        dataType: "json",
        pagination: true,
        singleSelect: false,
        sidePagination: "server",
        showRefresh: true,
        iconSize: "outline",
        icons: {
            refresh: "glyphicon-repeat"
        },
        toolbar: "#deptToolBar",
        pageNumber: 1,
        pageSize: 10,
        pageList: [5, 10, 20],
        queryParams: function (params) {
            return {
                limit: params.limit,
                offset: params.offset
            };
        },
        contentType: "application/x-www-form-urlencoded",
        columns: [{
            checkbox: true
        }, {
            field: 'id',
            title: '节点编号'
        }, {
            field: 'nodeName',
            title: '节点名称'
        }, {
            field: 'url',
            title: '节点链接'
        }, {
            field: 'online',
            title: '节点状态',
        }, {
            field: 'queryTimeout',
            title: '查询超时',
        }, {
            field: 'writeTimeout',
            title: '写入超时',
        }, {
            title: '操作',
            field: 'id',
            width: '70px',
            align: 'center',
            formatter: function (value, row) {
                return '<a href="javascript:void(0)"  title="编辑" onclick="edit(\'' + row.id + '\')">' + '<i class="glyphicon glyphicon-edit"></i></a> ' ;
            }
        }]
    });
}

function reLoad() {
    $('#nodeTable').bootstrapTable('refresh');
}

function add() {
    page(pagePrefix + '/add', '添加节点');
}

function edit(id) {
    page(pagePrefix + '/edit/' + id, '编辑节点数据');
}

function remove(id) {
    swal({
        title: "确定要删除选中的记录？",
        text: "确定要删除选中的记录，删除后不可逆？",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "删除",
        closeOnConfirm: false,
        showLoaderOnConfirm: true
    }, function () {
        $.ajax({
            url: deletePrefix + "/" + id,
            type: "DELETE",
            success: function (data) {
                if (ajaxIsSuccess(data)) {
                    swal({
                        title: "删除节点成功！",
                        text: "删除节点成功！",
                        type: "success"
                    }, function (e) {
                        reLoad();
                    });
                } else {
                    swal("删除节点失败！", "删除节点失败！", "error");
                }
            }

        });
    });
}

function batchRemove() {
    var rows = $('#nodeTable').bootstrapTable('getSelections');
    if (rows.length == 0) {
        swal("请选择要删除的记录？", "请选择要删除的记录？", "warning");
        return;
    }

    swal({
        title: "删除确认？",
        text: "确定要删除选中的记录？",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "删除",
        closeOnConfirm: false,
        showLoaderOnConfirm: true
    }, function () {
        sureremove();
    });

    function sureremove() {
        $.each(rows, function (i, row) {
            $.ajax({
                url: deletePrefix + '/' + row['id'],
                type: "DELETE",
                success: function (data) {
                    if (ajaxIsSuccess(data)) {
                        swal({
                            title: "删除节点成功！",
                            text: "删除节点成功！",
                            type: "success"
                        }, function () {
                            reLoad();
                        });
                    } else {
                        swal("删除节点失败！", "删除节点失败！", "error");
                    }
                }
            });
        });
    }
}
