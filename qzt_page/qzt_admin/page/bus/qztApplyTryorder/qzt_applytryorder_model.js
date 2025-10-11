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

    var orderId = parent.pkId;

    initialize();

    // 页面加载数据
    function initialize() {
        $.ajax({
            async: false,
            contentType: "application/json",
            type: "GET",
            url: "back/qztApplyTryorder/query/" + orderId,
            success: function (data) {
                if (data.code == 200) {
                    var ret = data.data;
                    var applyTryorderMess = ret.applyTryorderMess;
                    for (var item in applyTryorderMess) {
                        $("#" + item).html(applyTryorderMess[item] == "" ? "—" : applyTryorderMess[item]);
                    }
                    var beforeTrialPic = ret.beforeTrialPic;//试用前图
                    var afterTrialPic = ret.afterTrialPic;//试用后图
                    if (beforeTrialPic.length > 0) {
                        $("#beforeTrialPicf").show();
                        for (var item in beforeTrialPic) {
                            var beforeTrial = beforeTrialPic[item];
                            var html = '<a class="tryout_pic" href="' + beforeTrial.bannerUrl + '" target="_blank"><img src="' + beforeTrial.bannerUrl + '"></a>';
                            $("#beforeTrialPicd").append(html);
                        }
                    }
                    if (afterTrialPic.length > 0) {
                        $("#afterTrialPicf").show();
                        for (var item in afterTrialPic) {
                            var afterTrial = afterTrialPic[item];
                            var html = '<a class="tryout_pic" href="' + afterTrial.bannerUrl + '" target="_blank"><img src="' + afterTrial.bannerUrl + '"></a>';
                            $("#afterTrialPicd").append(html);
                        }
                    }

                    table.render({
                        elem: '#orderLogTable',
                        height: 420,
                        title: '订单日志',
                        data: ret.orderLogs,
                        page: false,
                        limit: 999,//显示的数量
                        cols: [[
                            {type: 'numbers', title: '序号', fixed: 'left'},
                            {field: 'operNode', title: '操作节点', minWidth: 150, align: 'center'},
                            {field: 'createTime', title: '操作时间', minWidth: 180, align: 'center'},
                            {field: 'createBy', title: '操作人ID', minWidth: 130, align: 'center'},
                            {field: 'busRemark', title: '备注', minWidth: 480, align: 'center'}
                        ]]
                    });
                    form.render();
                } else {
                    top.layer.msg(data.message, {icon: 2});
                }
            }
        });
    }

    //监听Tab切换
    element.on('tab(demo)', function (data) {
        layer.tips('切换了 ' + data.index + '：' + this.innerHTML, this, {
            tips: 1
        });
    });

});

