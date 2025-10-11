layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;
    //页面操作：0：查看，1：添加，2：修改
    pageOperation = 0;
    pkId = "";
    deptId = "";
    urls = 'back/qztServiceOrder/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'userId', title: '用户ID', align: 'center', minWidth: 50, templet: '<div>{{d.userId==""?"-":d.userId}}</div>'},
            {field: 'busniessName', title: '服务站名称', align: 'center', minWidth: 100, templet: '<div>{{d.busniessName==""?"-":d.busniessName}}</div>'},
            {field: 'orderNo', title: '订单编号', align: 'center', minWidth: 100, templet: '<div>{{d.orderNo==""?"-":d.orderNo}}</div>'},
            {field: 'orderState', title: '订单状态', align: 'center', minWidth: 90, templet: '<div>{{d.orderState==""?"-":d.orderState}}</div>'},
            {field: 'contactsName', title: '联系人', align: 'center', minWidth: 100, templet: '<div>{{d.contactsName==""?"-":d.contactsName}}</div>'},
            {field: 'contactsTel', title: '联系电话', align: 'center', minWidth: 100, templet: '<div>{{d.contactsTel==""?"-":d.contactsTel}}</div>'},
            {field: 'disease', title: '疾病名称', align: 'center', minWidth: 100, templet: '<div>{{d.disease==""?"-":d.disease}}</div>'},
            {field: 'shareMoney', title: '服务金额', align: 'center', minWidth: 100, templet: '<div>{{d.shareMoney==""?"-":d.shareMoney/100}}</div>'},
            {field: 'finishTime', title: '完成时间', align: 'center', minWidth: 100, templet: '<div>{{d.finishTime==""?"-":d.finishTime}}</div>'},
            {field: 'opt', title: '操作', fixed: 'right', width: 100, align: 'center', toolbar: '#toolBar'}
        ]],
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
                    $(this).text(dic["SERVER_ORDER_STATE" + $(this).text()]);
                }
                index++;
            })
        },
        elem: '#qztServiceOrderTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("SERVER_ORDER_STATE", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztServiceOrderFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'qztServiceOrdermodel.html', null, null, null);
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
        excel("searchId", "back/qztexport/orderExcel?orderType=SO");
    });

});