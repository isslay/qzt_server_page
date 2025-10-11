layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;

    // 页面操作：0：查看，1：添加，2：修改
    pageOperation = 0;
    checkedRoleId = "";

    // 渲染表格
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'roleName', title: '角色名称', align: 'center', minWidth: 120, templet: '<div>{{d.roleName==""?"-":d.roleName}}</div>'},
            {field: 'deptName', title: '所属部门', align: 'center', minWidth: 120, templet: '<div>{{d.deptName==""?"-":d.deptName}}</div>'},
            {field: 'createTime', title: '创建时间', align: 'center', minWidth: 180, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'remark', title: '备注', align: 'center', minWidth: 120, templet: '<div>{{d.remark==""?"-":d.remark}}</div>'},
            {field: 'opt', title: '操作', fixed: 'right', width: 260, align: 'center', toolbar: '#toolBar'}
        ]],
        url: 'role/listPage',
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
        elem: '#roleTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });

    //监听工具条
    table.on('tool(tableFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            checkedRoleId = data.id;
            xadmin.open('查看', 'role.html', 1030, 700, null);
        } else if (layEvent === 'del') { //删除
            checkedRoleId = data.id;
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'role/delBatchByIds',
                    data: JSON.stringify([data.id]),
                    success: function (data) {
                        if (data.code === 200) {
                            if (data.data === true) {
                                obj.del();
                                layer.msg("删除成功", {icon: 1, time: 2000});
                            }
                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
            });
        } else if (layEvent === 'edit') { //编辑
            pageOperation = 2;
            checkedRoleId = data.id;
            xadmin.open('编辑', 'role.html', 1030, 700, null);
        }
    });

    //查询
    $(".search_btn").click(function () {
        var searchKey = $(".search_input").val();
        tableIns.reload({
            where: { //设定异步数据接口的额外参数，任意设
                condition: {
                    role_name: searchKey
                }
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    //添加角色
    form.on('submit(roleAdd)', function (obj) {
        pageOperation = 1;
        xadmin.open('添加', 'role.html', 1030, 700, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量删除
    form.on('submit(batchDel)', function (obj) {
        var checkStatus = table.checkStatus('roleTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请选择要删除的角色", {icon: 0, time: 2000});
            return;
        }
        layer.confirm('确定删除选中的信息？', {icon: 3, title: '确认'}, function (index) {
            var loadingIndex = base.loading(layer);
            var roleIds = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                roleIds[i] = checkStatus.data[i].id;
            }
            $.ajax({
                type: 'POST',
                url: 'role/delBatchByIds',
                data: JSON.stringify(roleIds),
                success: function (data) {
                    if (data.code === 200) {
                        if (data.data === true) {
                            layer.close(loadingIndex);
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