function page(url, title) {
    var nav = $(window.parent.document).find('.J_menuTabs .page-tabs-content ');
    $(window.parent.document).find('.J_menuTabs .page-tabs-content ').find(".J_menuTab.active").removeClass("active");
    $(window.parent.document).find('.J_mainContent').find("iframe").css("display", "none");
    var iframe = '<iframe class="J_iframe" name="iframe10000" width="100%" height="100%" src="' + url + '" frameborder="0" data-id="' + url
        + '" seamless="" style="display: inline;"></iframe>';
    $(window.parent.document).find('.J_menuTabs .page-tabs-content ').append(
        ' <a href="javascript:;" class="J_menuTab active" data-id="' + url + '">' + title + ' <i class="fa fa-times-circle"></i></a>');
    $(window.parent.document).find('.J_mainContent').append(iframe);
}

/*关闭iframe*/
function removeIframe() {
    var topWindow = $(window.parent.document),
        iFrame = topWindow.find('.J_mainContent .J_iframe'),
        tab = topWindow.find(".J_menuTabs .page-tabs-content .J_menuTab"),
        i = topWindow.find(".J_menuTabs .page-tabs-content .J_menuTab .active").index();
    tab.eq(i - 1).addClass("active");
    tab.eq(i).remove();
    iFrame.eq(i - 1).show();
    iFrame.eq(i - 1)[0].contentWindow.reLoad();
    iFrame.eq(i).remove();
}

function removeIframeWithSwal() {
    swal({
        title: "关闭确定？",
        text: "请检查本页是否未保存，是否确定关闭？",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "关闭",
        closeOnConfirm: false
    }, function () {
        removeIframe();
    });
}

function ajaxIsSuccess(data) {
    return data.success;
}

$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};