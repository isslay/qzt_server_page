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
    urls = 'back/qztApplyTryorder/queryListPage';
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
            {field: 'createTime', title: '申请时间', align: 'center', minWidth: 145, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'applyUserId', title: '用户ID', align: 'center', minWidth: 80, templet: '<div>{{d.applyUserId==""?"-":d.applyUserId}}</div>'},
            {field: 'referrerUserId', title: '推荐人ID', align: 'center', minWidth: 100, templet: '<div>{{d.referrerUserId==""?"-":d.referrerUserId}}</div>'},
            {field: 'storeType', title: '联系人', align: 'center', minWidth: 100, templet: '<div>{{d.storeType==""?"-":d.storeType}}</div>'},
            {field: 'storeName', title: '联系电话', align: 'center', minWidth: 100, templet: '<div>{{d.storeName==""?"-":d.storeName}}</div>'},
            {field: 'diseaseName', title: '疾病名称', align: 'center', minWidth: 160, templet: '<div>{{d.diseaseName==""?"-":d.diseaseName}}</div>'},
            {field: 'busName', title: '服务站名称', align: 'center', minWidth: 180, templet: '<div>{{d.busName==""?"-":d.busName}}</div>'},
            {field: 'orderState', title: '订单状态', align: 'center', minWidth: 95, templet: '<div>{{d.orderState==""?"-":d.orderState}}</div>'},
            {field: 'idCardno', title: '身份证', align: 'center', minWidth: 160, templet: '<div>{{d.idCardno==""?"-":d.idCardno}}</div>'},
            {field: 'opt', title: '操作', fixed: 'right', width: 100, align: 'center', toolbar: '#toolBar'}
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
        done: function (res, page, count) {//字典翻译 需要翻译的字段 与 字典类型对应
            var index = 0;
            $("[data-field='orderState']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["TRY_STATE" + $(this).text()]);
                }
                index++;
            })
        },
        elem: '#qztApplyTryorderTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("TRY_STATE", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztApplyTryorderFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'orderDetails') { //查看
            pageOperation = 0;
            xadmin.open('订单详情', 'qzt_applytryorder_model.html', null, null, 1);
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
        excel("searchId", "back/qztexport/orderExcel?orderType=SY");
    });


});