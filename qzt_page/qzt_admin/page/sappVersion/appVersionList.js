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
    urls = '/back/appVersion/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'sourceMode', title: 'APP来源', align: 'center', minWidth: 120},
            {field: 'versionNo', title: '版本号', align: 'center', minWidth: 120},
            {field: 'downloadUrl', title: '下载地址', align: 'center', minWidth: 400},
            {field: 'isForcedUpdate', title: '是否强制更新', align: 'center', minWidth: 120},
            {field: 'remark', title: '备注', align: 'center', minWidth: 200, templet: '<div>{{d.remark==""?"-":d.remark}}</div>'},
            {field: 'opt', title: '操作', fixed: 'right', width: 160, align: 'center', toolbar: '#toolBar'}
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
        done: function (res, page, count) {
            var index = 0;
            $("[data-field='isForcedUpdate']").children().each(function (index) {
                if (index > 0 & $(this).text() != "") {
                    $(this).text(dic["YN" + $(this).text()]);
                }
            })
            $("[data-field='sourceMode']").children().each(function (index) {
                if (index > 0 & $(this).text() != "") {
                    $(this).text(dic["REG_SOURCE" + $(this).text()]);
                }
            })
        },
        elem: '#appVersionTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });

    getDicObj("YN,REG_SOURCE", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(tableFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        var layEvent = obj.event;//获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') {//查看
            pageOperation = 0;
            xadmin.open('查看', 'appVersionmodel.html', 800, 400, null);
        } else if (layEvent === 'edit') { //编辑
            pageOperation = 2;
            xadmin.open('编辑', 'appVersionmodel.html', 800, 400, null);
        }
    });

    //查询
    $(".search_btn").click(function () {
        tableIns.reload({
            where: {condition: getSearchParams("searchId")},
            page: {curr: 1}
        });
    });

    //添加
    form.on('submit(appVersionAdd)', function (obj) {
        pageOperation = 1;
        pkId = "";
        xadmin.open('添加', 'appVersionmodel.html', 800, 400, null);
        return false;//阻止表单跳转
    });

});