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
            url: "back/qztApplyBusorder/query/" + orderId,
            success: function (data) {
                if (data.code == 200) {
                    var ret = data.data;
                    var goodsMess = ret.goodsMess;
                    var orderMess = ret.orderMess;
                    $("#goodsName").html(goodsMess.goodsName);
                    $("#goodsPic").attr("src", goodsMess.goodsPic);

                    for (var item in orderMess) {
                        $("#" + item).html(orderMess[item]);
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

                    table.render({
                        elem: '#orderPayBackTable',
                        height: 420,
                        title: '三方支付回调日志',
                        data: ret.payBackLogs,
                        page: false,
                        limit: 999,//显示的数量
                        cols: [[
                            {type: 'numbers', title: '序号', fixed: 'left'},
                            {field: 'payNo', title: '支付单号', minWidth: 100, align: 'center'},
                            {field: 'payMoney', title: '支付金额', minWidth: 180, align: 'center'},
                            {field: 'backSource', title: '回调来源', minWidth: 100, align: 'center'},
                            {field: 'backPayNo', title: '第三方支付单号', minWidth: 150, align: 'center'}
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

