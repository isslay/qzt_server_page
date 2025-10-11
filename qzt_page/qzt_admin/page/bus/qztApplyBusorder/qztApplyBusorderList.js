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
    pkId = "";
    deptId = "";
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
    urls = 'back/qztApplyBusorder/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers', title: '序号', fixed: 'left'},
            {
                field: 'orderNo',
                title: '订单编号',
                align: 'center',
                minWidth: 150,
                templet: '<div><a href="#" title="订单详情"  lay-event="orderDetails" class="layui-table-link">{{d.orderNo}}</a></div>}'
            },
            {field: 'createTime', title: '申请时间', align: 'center', minWidth: 160, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'applyUserId', title: '申请用户ID', align: 'center', minWidth: 126, templet: '<div>{{d.applyUserId==""?"-":d.applyUserId}}</div>'},
            {field: 'contactName', title: '申请人姓名', align: 'center', minWidth: 160, templet: '<div>{{d.contactName==""?"-":d.contactName}}</div>'},
            {field: 'contactTel', title: '联系电话', align: 'center', minWidth: 126, templet: '<div>{{d.contactTel==""?"-":d.contactTel}}</div>'},
            {field: 'stateMark', title: '详细地址', align: 'center', minWidth: 180, templet: '<div>{{d.stateMark==""?"-":d.stateMark}}</div>'},
            {field: 'referrerUserId', title: '推荐人ID', align: 'center', minWidth: 126, templet: '<div>{{d.referrerUserId==""?"-":d.referrerUserId}}</div>'},
            {field: 'orderStateMc', title: '订单状态', align: 'center', minWidth: 145, templet: '<div>{{d.orderStateMc==""?"-":d.orderStateMc}}</div>'},
            /*{
                field: 'threeSidesMoney',
                title: '支付金额',
                align: 'center',
                minWidth: 126,
                templet: '<div>{{d.threeSidesMoney==""?"0.00":(d.threeSidesMoney/100).toFixed(2)}}</div>'
            },
            {field: 'payTypeMc', title: '支付方式', align: 'center', minWidth: 145, templet: '<div>{{d.payTypeMc==""?"-":d.payTypeMc}}</div>'},
            {field: 'payStateMc', title: '支付状态', align: 'center', minWidth: 145, templet: '<div>{{d.payStateMc==""?"-":d.payStateMc}}</div>'},
            {field: 'payTime', title: '支付时间', align: 'center', minWidth: 145, templet: '<div>{{d.payTime==""?"-":d.payTime}}</div>'},*/
            {field: 'consigneeName', title: '收货人名称', align: 'center', minWidth: 180, templet: '<div>{{d.consigneeName==""?"-":d.consigneeName}}</div>'},
            {field: 'consigneeTel', title: '收货人电话', align: 'center', minWidth: 180, templet: '<div>{{d.consigneeTel==""?"-":d.consigneeTel}}</div>'},
            {field: 'consigneeAddress', title: '收货地址', align: 'center', minWidth: 260, templet: '<div>{{d.consigneeAddress==""?"-":d.consigneeAddress}}</div>'},
            {field: 'courierCompany', title: '快递公司名称', align: 'center', minWidth: 180, templet: '<div>{{d.courierCompany==""?"-":d.courierCompany}}</div>'},
            {field: 'courierNo', title: '快递单号', align: 'center', minWidth: 180, templet: '<div>{{d.courierNo==""?"-":d.courierNo}}</div>'},
            {field: 'courierRemarks', title: '快递备注', align: 'center', minWidth: 180, templet: '<div>{{d.courierRemarks==""?"-":d.courierRemarks}}</div>'},
            {field: 'shipmentsTime', title: '发货时间', align: 'center', minWidth: 145, templet: '<div>{{d.shipmentsTime==""?"-":d.shipmentsTime}}</div>'},
            {field: 'signforTime', title: '签收时间', align: 'center', minWidth: 145, templet: '<div>{{d.signforTime==""?"-":d.signforTime}}</div>'},
            {field: 'opt', title: '操作', fixed: 'right', width: 260, align: 'center', toolbar: '#toolBar'}
        ]],
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
        elem: '#qztApplyBusorderTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztApplyBusorderFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'orderDetails') { //订单详情
            xadmin.open('订单详情', 'qzt_applybusorder_model.html', null, null, 1);
        } else if (layEvent === 'edit') { //发货
            pageOperation = 2;
            xadmin.open('发货', 'qzt_applybusorder_shipments.html', 600, 400, null);
        } else if (layEvent === 'dispatching') { //自提
            layer.confirm('您确定要自提吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'back/qztApplyBusorder/offlineDistribution/' + data.id,
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
        } else if (layEvent === 'auditPass') { //审核通过
            layer.confirm('您确定要通过吗？', {icon: 3, title: '确认'}, function () {
                audit(data.id, "1");
            });
        } else if (layEvent === 'auditReject') { //驳回
            layer.confirm('您确定要驳回吗？', {icon: 3, title: '确认'}, function () {
                audit(data.id, "2");
            });
        }
    });

    /**
     * 审核
     * @param type 1通过、2驳回
     */
    function audit(id, type) {
        $.ajax({
            type: 'POST',
            url: 'back/qztApplyBusorder/audit?id=' + id + "&type=" + type,
            success: function (data) {
                if (data.code == "200") {
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
    }

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
        excel("searchId", "back/qztexport/orderExcel?orderType=SA");
    });

});