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
    var pickupWay = parent.pickupWay;

    initialize();

    // 页面加载数据
    function initialize() {
        $.ajax({
            async: false,
            contentType: "application/json",
            type: "GET",
            url: "back/qztGorder/query/" + orderId,
            success: function (data) {
                if (data.code == 200) {
                    var ret = data.data;
                    var stoGoodsList = ret.stoGoodsList;
                    var orderMess = ret.orderMess;
                    var stohtml = '';
                    for (var item in stoGoodsList) {
                        var stoGoods = stoGoodsList[item];
                        var num = parseInt(item) + 1;
                        stohtml += '<tbody><tr><td class="kv-label" rowspan="3">商品图片' + num + '</td>';
                        stohtml += '<td class="kv-content" rowspan="3"><img src="' + stoGoods.goodsPic + '" height="100px"/></td>';
                        stohtml += '<td class="kv-label">商品名称' + "" + '</td><td class="kv-content" colspan="3"><span>' + stoGoods.goodsName + '</span></td></tr>';
                        stohtml += '<tr><td class="kv-label">商品描述' + "" + '</td><td class="kv-content" colspan="3"><span>' + stoGoods.goodsRemark + '</span></td></tr>';
                        stohtml += '<tr><td class="kv-label">购买数量' + "" + '</td><td class="kv-content" colspan="3"><span>x ' + stoGoods.buyNum + '</span></td></tr></tbody>';
                    }
                    $("#goodsArr").html(stohtml);
                    if (pickupWay == "0") {
                        $("#consigneeNameHtml").html("服务站");
                        $("#consigneeTelHtml").html("服务站电话");
                        $("#consigneeAddressHtml").html("服务站地址");
                        if (orderMess.orderState == "09") {
                            $(".replenishInfo").show();
                        }
                    } else {
                        $(".courierInfo").show();
                    }

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


                    // table.render({
                    //     elem: '#orderShareTable',
                    //     height: 420,
                    //     title: '分润流水',
                    //     data: ret.shareLogs,
                    //     page: false,
                    //     limit: 999,//显示的数量
                    //     cols: [[
                    //         {type: 'numbers', title: '序号', fixed: 'left'},
                    //         {field: 'userId', title: '用户ID', minWidth: 150, align: 'center'},
                    //         {field: 'giveNum', title: '分润金额', minWidth: 150, align: 'center'},
                    //         {field: 'changeNum', title: '实到金额', minWidth: 150, align: 'center'},
                    //         {field: 'createDTime', title: '分润日期', minWidth: 150, align: 'center'},
                    //         {field: 'updateDTime', title: '变更日期', minWidth: 150, align: 'center'},
                    //         {field: 'remark', title: '描述', minWidth: 150, align: 'center'}
                    //     ]]
                    // });


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

