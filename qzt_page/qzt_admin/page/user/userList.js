layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;
    //页面操作：0：查看，1：添加，2：修改
    pageOperation = 0;
    userId = "";
    deptId = "";
    urls = 'user/listPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'account', title: '账号', minWidth: 150},
            {field: 'userName', title: '名称', minWidth: 150},
            // {field: 'sex', title: '性别', templet: '#sexTpl', width: 70},
            {field: 'phone', title: '手机号', minWidth: 120, templet: '<div>{{d.phone==""?"-":d.phone}}</div>'},
            // {field: 'idCard', title: '身份证', minWidth: 100, templet: '<div>{{d.idCard==""?"-":d.idCard}}</div>'},
            {field: 'deptName', title: '部门', minWidth: 100, templet: '<div>{{d.deptName==""?"-":d.deptName}}</div>'},
            {field: 'position', title: '职位', minWidth: 100, templet: '<div>{{d.position==""?"-":d.position}}</div>'},
            // {field: 'email', title: '邮箱', minWidth: 180, templet: '<div>{{d.email==""?"-":d.email}}</div>'},
            {field: 'enable', title: '状态', templet: '<div>{{d.enable === 1 ? "启用" : "禁用"}}</div>', minWidth: 60},
            {field: 'opt', title: '操作', fixed: 'right', minWidth: 260, align: 'center', toolbar: '#toolBar'}
        ]],
        url: urls,
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
        elem: '#userTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });

    //监听工具条
    table.on('tool(tableFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        userId = data.id;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'userView.html');
        } else if (layEvent === 'del') { //停用/启用
            var enable, s;
            if (data.enable == 1) {
                enable = 0;
                s = "停用"
            } else {
                enable = 1;
                s = "启用"
            }
            layer.confirm('您确定要' + s + '该用户吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: "user/modifyStopUp?id=" + data.id + "&enable=" + enable,
                    success: function (data) {
                        if (data.code == 200) {
                            if (data.data === true) {
                                layer.msg("操作成功", {icon: 1, time: 2000});
                                location.reload()
                            }
                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
            });
        } else if (layEvent === 'edit') { //编辑
            pageOperation = 2;
            xadmin.open('编辑', 'user.html', 800, 400, null);
        } else if (layEvent === 'update') { //密码重置
            var name = data.account;
            layer.confirm('您确定要将' + name + '的密码重置吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'user/updatePassword',
                    data: JSON.stringify(name),
                    success: function (data) {
                        if (data.code == 200) {
                            layer.msg("操作成功", {icon: 1, time: 2000});

                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
            });
        }
    });

    //监听表格复选框选择
    table.on('checkbox(tableFilter)', function (obj) {
    });

    //查询
    $(".search_btn").click(function () {
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                condition: getSearchParams('searchId')
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    //添加会员
    form.on('submit(usersAdd)', function (obj) {
        pageOperation = 1, pkId = "";
        xadmin.open('添加', 'user.html', 800, 400, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量删除
    form.on('submit(batchDel)', function (obj) {
        var checkStatus = table.checkStatus('userTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请选择要删除的用户", {icon: 0, time: 2000});
            return;
        }
        layer.confirm('确定删除选中的信息？', {icon: 3, title: '确认'}, function (index) {
            var indexMsg = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
            var userIds = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                userIds[i] = checkStatus.data[i].id;
            }
            $.ajax({
                type: 'POST',
                url: 'user/delBatchByIds',
                data: JSON.stringify(userIds),
                success: function (data) {
                    if (data.code == 200) {
                        if (data.data === true) {
                            layer.close(indexMsg);
                            layer.msg("删除成功", {icon: 1, time: 2000});
                            tableIns.reload({
                                page: {
                                    curr: 1 //重新从第 1 页开始
                                }
                            });
                        }
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        });
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    })
});