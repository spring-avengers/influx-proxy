var pagePrefix = "/retention";
var deletePrefix = "/management/retention";
var synPrefix = "/management/retention/syn";
$(function () {
    load();
});

function load() {
    $('#retentionTable').bootstrapTable({
        type: "GET",
        url: "/management/retention",
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
            field: 'nodeUrl',
            title: '节点链接'
        }, {
            field: 'databaseName',
            title: '数据库名称'
        }, {
            field: 'retentionName',
            title: '策略名称',
        }, {
            field: 'retentionDuration',
            title: '策略时间',
        }, {
            field: 'retentionDefault',
            title: '默认状态',
        }, {
            field: 'synStatus',
            title: '同步状态',
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
    $('#retentionTable').bootstrapTable('refresh');
}

function add() {
    page(pagePrefix + '/add', '添加策略');
}

function edit(id) {
    page(pagePrefix + '/edit/' + id, '编辑策略数据');
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
                        title: "删除成功！",
                        text: "删除成功！",
                        type: "success"
                    }, function (e) {
                        reLoad();
                    });
                } else {
                    swal("删除失败！", "删除失败！", "error");
                }
            }

        });
    });
}

function batchRemove() {
    var rows = $('#retentionTable').bootstrapTable('getSelections');
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
                            title: "删除成功！",
                            text: "删除成功！",
                            type: "success"
                        }, function () {
                            reLoad();
                        });
                    } else {
                        swal("删除失败！", "删除失败！", "error");
                    }
                }
            });
        });
    }
}

function batchSyn() {
    var rows = $('#retentionTable').bootstrapTable('getSelections');
    if (rows.length == 0) {
        swal("请选择要同步的记录？", "请选择要同步的记录？", "warning");
        return;
    }

    swal({
        title: "同步确认？",
        text: "确定要同步选中的记录？",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "同步",
        closeOnConfirm: false,
        showLoaderOnConfirm: true
    }, function () {
        suresyn();
    });

    function suresyn() {
        $.each(rows, function (i, row) {
            $.ajax({
                url: synPrefix + '/' + row['id'],
                type: "POST",
                success: function (data) {
                    if (ajaxIsSuccess(data)) {
                        swal({
                            title: "同步成功！",
                            text: "同步成功！",
                            type: "success"
                        }, function () {
                            reLoad();
                        });
                    } else {
                        swal("同步失败！", "同步失败！", "error");
                    }
                }
            });
        });
    }
}
