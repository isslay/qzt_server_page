layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;

    //列表加载
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'userName', title: '用户名', minWidth: 120, align: 'center'},
            {field: 'operation', title: '用户操作', minWidth: 90, align: 'center'},
            {field: 'operationType', title: '操作类型', templet: '#operationTypeTpl', minWidth: 90, align: 'center'},
            {field: 'method', title: '请求方法', align: 'center', minWidth: 400},
            {field: 'params', title: '请求参数', align: 'center', minWidth: 400},
            {field: 'result', title: '结果', align: 'center', templet: '<div>{{d.result === 1 ? "成功" : "失败"}}</div>', minWidth: 60},
            {field: 'time', title: '执行时长', minWidth: 90, align: 'center'},
            {field: 'ip', title: 'IP地址', minWidth: 120, align: 'center'},
            {field: 'createTime', title: '创建时间', minWidth: 180, align: 'center'}
        ]],
        url: 'log/queryListPage',
        contentType: 'application/json',//加此参数传参方式为 json
        method: 'POST',
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        response: {
            statusCode: 200, //成功的状态码，默认：0
            msgName: 'message', //状态信息的字段名称，默认：msg
        },
        elem: '#logTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });


    //查询
    $(".search_btn").click(function () {
        var searchKey = $(".search_input").val();
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                condition: {
                    user_name: searchKey,
                    operation_: searchKey
                }
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

});