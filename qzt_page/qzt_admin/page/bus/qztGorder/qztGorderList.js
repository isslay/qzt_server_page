layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table;
    //页面操作：0：查看，1：添加，2：修改
    pageOperation = 0;
    replenishCneName = "", replenishCneTel = "", replenishCneAddress = "";
    pkId = "";
    //日期1
    laydate.render({
        elem: '#startTime',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });
    //日期2
    laydate.render({
        elem: '#endTime',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });
    var titles = "商品订单-发货";
    var colss = [[
        {type: 'numbers', title: '序号', fixed: 'left'},
        {
            field: 'orderNo',
            title: '订单编号',
            align: 'center',
            minWidth: 150,
            templet: '<div><a href="#" title="订单详情"  lay-event="orderDetails" class="layui-table-link">{{d.orderNo}}</a></div>}'
        },
        {field: 'createTime', title: '下单时间', align: 'center', minWidth: 145, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
        {field: 'userId', title: '用户ID', align: 'center', minWidth: 80, templet: '<div>{{d.userId==""?"-":d.userId}}</div>'},
        {field: 'referrerSecond', title: '推荐人', align: 'center', minWidth: 80, templet: '<div>{{d.referrerSecond==""?"-":d.referrerSecond}}</div>'},
        {field: 'referrerName', title: '推荐人部门', align: 'center', minWidth: 100, templet: '<div>{{d.referrerName==""?"-":d.referrerName}}</div>'},
        // {field: 'goodsName', title: '商品名称', align: 'center', minWidth: 260, templet: '<div>{{d.goodsName==""?"-":d.goodsName}}</div>'},
        // {field: 'buyNum', title: '购买数量', align: 'center', minWidth: 95, templet: '<div>{{d.buyNum==""?"-":d.buyNum}}</div>'},
        {field: 'orderStateMc', title: '订单状态', align: 'center', minWidth: 95, templet: '<div>{{d.orderStateMc==""?"-":d.orderStateMc}}</div>'},
        // {field: 'remarks', title: '用户备注/留言', align: 'center', minWidth: 180, templet: '<div>{{d.remarks==""?"-":d.remarks}}</div>'},
        // {
        //     field: 'balanceMoney',
        //     title: '余额支付金额',
        //     align: 'center',
        //     minWidth: 126,
        //     templet: '<div>{{d.balanceMoney==""?"0.00":(d.balanceMoney/100).toFixed(2)}}</div>'
        // },
        // {
        //     field: 'cashCouponMoney',
        //     title: '抵扣券支付金额',
        //     align: 'center',
        //     minWidth: 126,
        //     templet: '<div>{{d.cashCouponMoney==""?"0.00":(d.cashCouponMoney/100).toFixed(2)}}</div>'
        // },
        {
            field: 'totalPrice',
            title: '订单金额',
            align: 'center',
            minWidth: 126,
            templet: '<div>{{d.totalPrice==""?"0.00":(d.totalPrice/100).toFixed(2)}}</div>'
        },
        {
            field: 'costPrice',
            title: '成本总额',
            align: 'center',
            minWidth: 126,
            templet: '<div>{{d.costPrice==""?"0.00":(d.costPrice/100).toFixed(2)}}</div>'
        },
        {
            field: 'deductMax',
            title: '抵扣金额',
            align: 'center',
            minWidth: 126,
            templet: '<div>{{d.deductMax==""?"0.00":(d.deductMax/100).toFixed(2)}}</div>'
        },
        {
            field: 'actuaPayment',
            title: '实付金额',
            align: 'center',
            minWidth: 126,
            templet: '<div>{{d.actuaPayment==""?"0.00":(d.actuaPayment/100).toFixed(2)}}</div>'
        },
        // {
        //     field: 'totalPrice',
        //     title: '订单金额',
        //     align: 'center',
        //     minWidth: 126,
        //     templet: '<div>{{d.totalPrice==""?"0.00":(d.totalPrice/100).toFixed(2)}}</div>'
        // },
        // {
        //     field: 'threeSidesMoney',
        //     title: '三方支付金额',
        //     align: 'center',
        //     minWidth: 126,
        //     templet: '<div>{{d.threeSidesMoney==""?"0.00":(d.threeSidesMoney/100).toFixed(2)}}</div>'
        // },
        // {field: 'payTypeMc', title: '支付方式', align: 'center', minWidth: 145, templet: '<div>{{d.payTypeMc==""?"-":d.payTypeMc}}</div>'},
        {field: 'payStateMc', title: '支付状态', align: 'center', minWidth: 145, templet: '<div>{{d.payStateMc==""?"-":d.payStateMc}}</div>'},
        {field: 'payTime', title: '支付时间', align: 'center', minWidth: 145, templet: '<div>{{d.payTime==""?"-":d.payTime}}</div>'},
        {field: 'consigneeName', title: '收货人名称', align: 'center', minWidth: 180, templet: '<div>{{d.consigneeName==""?"-":d.consigneeName}}</div>'},
        {field: 'consigneeTel', title: '收货人电话', align: 'center', minWidth: 180, templet: '<div>{{d.consigneeTel==""?"-":d.consigneeTel}}</div>'},
        {field: 'consigneeAddress', title: '收货地址', align: 'center', minWidth: 260, templet: '<div>{{d.consigneeAddress==""?"-":d.consigneeAddress}}</div>'},
        // {field: 'courierCompany', title: '快递公司名称', align: 'center', minWidth: 180, templet: '<div>{{d.courierCompany==""?"-":d.courierCompany}}</div>'},
        {field: 'courierNo', title: '快递单号', align: 'center', minWidth: 180, templet: '<div>{{d.courierNo==""?"-":d.courierNo}}</div>'},
        {field: 'courierRemarks', title: '快递备注', align: 'center', minWidth: 180, templet: '<div>{{d.courierRemarks==""?"-":d.courierRemarks}}</div>'},
        {field: 'shipmentsTime', title: '发货时间', align: 'center', minWidth: 145, templet: '<div>{{d.shipmentsTime==""?"-":d.shipmentsTime}}</div>'},
        {field: 'signforTime', title: '签收时间', align: 'center', minWidth: 145, templet: '<div>{{d.signforTime==""?"-":d.signforTime}}</div>'},
        {field: 'opt', title: '操作', fixed: 'right', width: 226, align: 'center', toolbar: '#toolBar'}
    ]];
    pickupWay = getUrlParmas("pickupWay");
    if (pickupWay == "0") {//0为自提订单核销
        $("#ssUserid").show();
        $("#replenishStatusdiv").show();
        titles = "商品订单-核销";
        $("#orderState").html('<option value="">订单状态(全部)</option><option value="01">待付款</option><option value="02">待核销</option><option value="09">已核销</option><option value="11">已取消</option><option value="96">异常订单</option>');
        form.render();
        colss = [[
            {type: 'numbers', title: '序号', fixed: 'left'},
            {
                field: 'orderNo',
                title: '订单编号',
                align: 'center',
                minWidth: 150,
                templet: '<div><a href="#" title="订单详情"  lay-event="orderDetails" class="layui-table-link">{{d.orderNo}}</a></div>}'
            },
            {field: 'createTime', title: '下单时间', align: 'center', minWidth: 145, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'userId', title: '用户ID', align: 'center', minWidth: 80, templet: '<div>{{d.userId==""?"-":d.userId}}</div>'},
            // {field: 'goodsName', title: '商品名称', align: 'center', minWidth: 260, templet: '<div>{{d.goodsName==""?"-":d.goodsName}}</div>'},
            // {field: 'buyNum', title: '购买数量', align: 'center', minWidth: 95, templet: '<div>{{d.buyNum==""?"-":d.buyNum}}</div>'},
            {field: 'orderStateMc', title: '订单状态', align: 'center', minWidth: 95, templet: '<div>{{d.orderStateMc==""?"-":d.orderStateMc}}</div>'},
            {
                field: 'balanceMoney',
                title: '余额支付金额',
                align: 'center',
                minWidth: 126,
                templet: '<div>{{d.balanceMoney==""?"0.00":(d.balanceMoney/100).toFixed(2)}}</div>'
            },
            {
                field: 'cashCouponMoney',
                title: '抵扣券支付金额',
                align: 'center',
                minWidth: 126,
                templet: '<div>{{d.cashCouponMoney==""?"0.00":(d.cashCouponMoney/100).toFixed(2)}}</div>'
            },
            {
                field: 'threeSidesMoney',
                title: '三方支付金额',
                align: 'center',
                minWidth: 126,
                templet: '<div>{{d.threeSidesMoney==""?"0.00":(d.threeSidesMoney/100).toFixed(2)}}</div>'
            },
            {field: 'payTypeMc', title: '支付方式', align: 'center', minWidth: 145, templet: '<div>{{d.payTypeMc==""?"-":d.payTypeMc}}</div>'},
            {field: 'payStateMc', title: '支付状态', align: 'center', minWidth: 145, templet: '<div>{{d.payStateMc==""?"-":d.payStateMc}}</div>'},
            {field: 'payTime', title: '支付时间', align: 'center', minWidth: 145, templet: '<div>{{d.payTime==""?"-":d.payTime}}</div>'},
            {
                field: 'theVerificationCode',
                title: '核销码',
                align: 'center',
                minWidth: 145,
                templet: '<div>{{d.theVerificationCode==""?"-":d.theVerificationCode}}</div>'
            },
            {field: 'consigneeName', title: '服务站名称', align: 'center', minWidth: 300, templet: '<div>{{d.consigneeName==""?"-":d.consigneeName}}</div>'},
            {field: 'consigneeTel', title: '服务站电话', align: 'center', minWidth: 180, templet: '<div>{{d.consigneeTel==""?"-":d.consigneeTel}}</div>'},
            {field: 'consigneeAddress', title: '服务站地址', align: 'center', minWidth: 300, templet: '<div>{{d.consigneeAddress==""?"-":d.consigneeAddress}}</div>'},
            {field: 'finshTime', title: '核销时间', align: 'center', minWidth: 145, templet: '<div>{{d.finshTime==""?"-":d.finshTime}}</div>'},
            {field: 'replenishStatusMc', title: '补货状态', align: 'center', minWidth: 145, templet: '<div>{{d.replenishStatusMc==""?"-":d.replenishStatusMc}}</div>'},
            {field: 'opt', title: '操作', fixed: 'right', width: 226, align: 'center', toolbar: '#toolBar'}
        ]];
    }
    $("#pickupWay").val(pickupWay);
    $(".titles").html(titles);
    console.log(pickupWay)
    urls = 'back/qztGorder/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: colss,
        // skin: 'line', //行边框风格
        // even: true,//开启隔行背景
        // size: 'sm', //小尺寸的表格
        contentType: 'application/json',//加此参数传参方式为 json
        method: 'POST',
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message' //状态信息的字段名称，默认：msg
        },
        done: function (res, page, count) {//字典翻译 需要翻译的字段 与 字典类型对应
            var index = 0;
            // $("[data-field='payType']").children().each(function () {
            //     if (index > 0) {
            //         $(this).text(dic["PAY_TYPE" + $(this).text()]);
            //     }
            //     index++;
            // })
            // $("[data-field='payState']").children().each(function () {
            //     if (index > 0) {
            //         $(this).text(dic["PAY_STATE" + $(this).text()]);
            //     }
            //     index++;
            // })
        },
        elem: '#qztGorderTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("", tableIns, urls, "searchId");
    //监听工具条
    table.on('tool(qztGorderFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'orderDetails') { //订单详情
            xadmin.open('订单详情', 'qzt_order_model.html', null, null, 1);
        } else if (layEvent === 'edit') { //发货
            pageOperation = 2;
            replenishCneName = data.replenishCneName, replenishCneTel = data.replenishCneTel, replenishCneAddress = data.replenishCneAddress;
            xadmin.open(pickupWay == "1" ? '发货' : "补货", 'qzt_order_shipments.html', pickupWay == "1" ? 600 : 650, pickupWay == "1" ? 400 : 500, null);
        } else if (layEvent === 'dispatching') { //自提
            layer.confirm('您确定要自提吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'back/qztGorder/offlineDistribution/' + data.id,
                    success: function (data) {
                        if (data.code == 200) {
                            layer.msg("操作成功", {icon: 1, time: 2000});
                            tableIns.reload({
                                where: { //设定异步数据接口的额外参数，任意设
                                    condition: getSearchParams("searchId")
                                }
                            });
                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
            });
        }
    });

    //查询
    $(".search_btn").click(function () {
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                condition: getSearchParams("searchId")
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });


    //导出
    $(".excel").click(function () {
        excel("searchId", "back/qztexport/orderExcel?orderType=GS");
    });


});