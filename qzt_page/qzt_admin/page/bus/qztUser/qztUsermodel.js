layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table,
        element = layui.element;

    var userId = parent.pkId;
    initialize();

    // 页面加载数据
    function initialize() {
        $.ajax({
            type: "GET",
            url: "/back/qztUser/query/" + userId,
            success: function (data) {
                if (data.code == 200) {
                    var ret = data.data;
                    $("#wxHeadImage").attr('src', ret.wxHeadImage);
                    for (var item in ret) {
                        $("#" + item).html(ret[item]);
                    }
                    $("#state").html(ret.state == 0 ? "正常" : "拉黑"); //0正常，1删除，2拉黑
                    form.render();
                } else {
                    top.layer.msg(data.message, {icon: 2});
                }
            },
            contentType: "application/json"
        });
    }


});
