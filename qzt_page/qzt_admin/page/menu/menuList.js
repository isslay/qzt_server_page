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
    menuId = "";

    //列表加载
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'menuName', title: '菜单名称', edit: 'text', align: 'center', minWidth: 120},
            {field: 'parentName', title: '上级菜单', align: 'center', minWidth: 120, templet: '<div>{{d.parentName==""?"一级菜单":d.parentName}}</div>'},
            {field: 'menuType', title: '类型', align: 'center', minWidth: 90, templet: '<div>{{d.menuType === 0 ? "目录" : d.menuType === 1 ? "菜单" : "按钮"}}</div>'},
            {
                field: 'iconcls',
                title: '菜单图标样式',
                align: 'center',
                minWidth: 180,
                templet: '<div><i class="icon iconfont">{{d.iconcls==""?"&#xe6b4;":d.iconcls}}</i></div>'
            },
            {field: 'sortNo', title: '排序', align: 'center', minWidth: 70, edit: 'text', templet: '<div>{{d.sortNo===""?"-":d.sortNo}}</div>'},
            {field: 'request', title: '请求地址', minWidth: 400, align: 'center', edit: 'text', templet: '<div>{{d.request==""?"-":d.request}}</div>'},
            {field: 'permission', title: '权限标识', align: 'center', edit: 'text', templet: '<div>{{d.permission==""?"-":d.permission}}</div>'},
            {field: 'opt', title: '操作', fixed: 'right', minWidth: 260, align: 'center', toolbar: '#toolBar'}
        ]],
        contentType: 'application/json',//加此参数传参方式为 json
        url: 'menu/selectListPage',
        method: 'POST',
        response: {
            statusCode: 200 //成功的状态码，默认：0
        },
        request: {
            pageName: 'current', //页码的参数名称，默认：page
            limitName: 'size' //每页数据量的参数名，默认：limit
        },
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        },
        elem: '#menuTable'
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

    //监听状态操作
    form.on('checkbox(enableCbx)', function (obj) {
        // layer.tips(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);
    });

    //监听单元格编辑
    table.on('edit(menuTable)', function (obj) {
        var value = obj.value //得到修改后的值
            , data = obj.data //得到所在行所有键值
            , field = obj.field; //得到字段
        // layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
        var modData = {};
        modData["id"] = data.id;
        modData[field] = value;
        modData["parentId"] = data.parentId;
        modMenuData(modData);
    });

    //监听工具条
    table.on('tool(menuTable)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        menuId = data.id;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'menu.html', null, null, 1);
        } else if (layEvent === 'del') { //删除
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'DELETE',
                    url: 'menu/delete',
                    data: menuId,
                    success: function (data) {
                        if (data.code === 200) {
                            if (data.data === true) {
                                obj.del();
                                layer.msg("删除成功", {icon: 1, time: 2000});
                            } else {
                                layer.msg(data.message, {icon: 2});
                            }
                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
            });
        } else if (layEvent === 'edit') { //编辑
            pageOperation = 2;
            xadmin.open('编辑', 'menu.html', null, null, 1);
        }
    });

    //监听表格复选框选择
    table.on('checkbox(menuTable)', function (obj) {
    });

    //添加菜单
    form.on('submit(menuAdd)', function (obj) {
        pageOperation = 1, menuId = "";
        xadmin.open('添加', 'menu.html', null, null, 1);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量删除
    form.on('submit(batchDel)', function (obj) {
        var checkStatus = table.checkStatus('menuTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请选择要删除的菜单", {icon: 0, time: 2000});
            return;
        }
        layer.confirm('确定删除选中的信息？', {icon: 3, title: '确认'}, function (index) {
            var indexMsg = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
            var menuIds = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                menuIds[i] = checkStatus.data[i].id;
            }
            $.ajax({
                type: 'POST',
                url: 'menu/deleteBatchIds',
                data: JSON.stringify(menuIds),
                success: function (data) {
                    if (data.code === 200) {
                        //layer.close(indexMsg);
                        layer.msg("操作成功，成功删除记录数" + data.data, {icon: 1, time: 2000});
                        tableIns.reload();
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        });
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    })

    function modMenuData(modData) {
        var index = layer.msg('修改中，请稍候', {icon: 16, time: false, shade: 0.8});
        $.ajax({
            type: "POST",
            url: "menu/modify",
            data: JSON.stringify(modData),
            success: function (data) {
                if (data.code === 200) {
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg("修改成功！", {icon: 1});
                    }, 500);
                } else {
                    top.layer.close(index);
                    layer.msg(data.message, {icon: 2});
                }
            },
            contentType: "application/json"
        });
    }

});