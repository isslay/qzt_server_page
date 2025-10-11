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
    urls = 'back/qztPayBacklog/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            //{type: 'checkbox', fixed: 'left'},
            {type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'createTime', title: '回调时间', align: 'center', minWidth: 160, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'backSource', title: '回调来源', align: 'center', minWidth: 180, templet: '<div>{{d.backSource==""?"-":d.backSource}}</div>'},
            {field: 'busType', title: '业务类型', align: 'center', minWidth: 150, templet: '<div>{{d.busType==""?"-":d.busType}}</div>'},
            {field: 'orderNo', title: '订单编号', align: 'center', minWidth: 150, templet: '<div>{{d.orderNo==""?"-":d.orderNo}}</div>'},
            {field: 'payNo', title: '支付单号', align: 'center', minWidth: 150, templet: '<div>{{d.payNo==""?"-":d.payNo}}</div>'},
            {field: 'backPayNo', title: '第三方支付单号', align: 'center', minWidth: 226, templet: '<div>{{d.backPayNo==""?"-":d.backPayNo}}</div>'},
            {field: 'payMoney', title: '支付金额', align: 'center', minWidth: 100, templet: '<div>{{d.payMoney==""?"-":d.payMoney}}</div>'},
            {field: 'message', title: '报文', align: 'center', minWidth: 260, templet: '<div>{{d.message==""?"-":d.message}}</div>'},
            // {field: 'opt', title: '操作', fixed: 'right', width: 260, align: 'center', toolbar: '#toolBar'}
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
                    $(this).text(dic["PAY_TYPE" + $(this).text()]);
                }
                index++;
            })
            $("[data-field='backSource']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["PAY_TYPE" + $(this).text()]);
                }
                index++;
            })
        },
        elem: '#qztPayBacklogTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("PAY_TYPE", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztPayBacklogFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'qztPayBacklogmodel.html', null, null, null);
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

    //添加
    form.on('submit(qztPayBacklogAdd)', function (obj) {
        pageOperation = 1, pkId = "";
        xadmin.open('添加', 'qztPayBacklogmodel.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });


});