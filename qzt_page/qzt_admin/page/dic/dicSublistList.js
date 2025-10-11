layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;


    checkedDicId = parent.checkedDicId;//父级id
    dicType = parent.dicType;//父级字典类型
    dicName = parent.dicName;//父级字典名称
    dicId = parent.dicId;//父级字典id
    $("#parentId").val(checkedDicId);
    var urls = "back/sysDic/queryListSublistPage";
    // 渲染表格
    var tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'dicType', title: '字典类型', align: 'center'},
            {field: 'code', title: '关键字', align: 'center'},
            {field: 'codeText', title: '关键字名称', align: 'center'},
            {field: 'sortNo', title: '排序', align: 'center'},
            {field: 'enable', title: '启用状态', align: 'center', templet: '#switchTpl', event: 'editColumn'},
            {field: 'remark', title: '备注', align: 'center'},
            {field: 'createTime', title: '创建时间', align: 'center'},
            {field: 'opt', title: '操作', fixed: 'right', width: 200, align: 'center', toolbar: '#toolBar'}
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
        where: { //设定异步数据接口的额外参数，任意设
            condition: getSearchParams('searchId')
        },
        elem: '#dicTable',
        page: true,
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
        if (layEvent === 'del') { //删除
            checkedDicId = data.id;
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'back/sysDic/delectSublistById/' + data.id,
                    success: function (data) {
                        if (data.code === 200) {
                            obj.del();
                            layer.msg("删除成功", {icon: 1, time: 2000});
                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
            });
        } else if (layEvent === 'edit') { //修改字典关键字
            pageOperation = 2;
            checkedDicId = data.id;
            xadmin.open('编辑', 'dicSublist.html', null, null, null);
        }
    });


    //查询
    $(".search_btn").click(function () {
        tableIns.reload({
            url: urls,
            where: { //设定异步数据接口的额外参数，任意设
                condition: getSearchParams('searchId')
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    });

    //添加字典关键字
    form.on('submit(dicSubAdd)', function (obj) {
        pageOperation = 1;
        xadmin.open('添加', 'dicSublist.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //监听启用状态操作
    form.on('switch(enable)', function (obj) {
        //layer.msg('value: ' + this.value + ',name: ' + this.name + ',开关是否选中：' + (this.checked ? 'true' : 'false'));
        var index = layer.msg('修改中，请稍候', {icon: 16, time: false, shade: 0.8});
        $.ajax({
            type: "POST",
            url: "back/sysDic/enableSublistById?id=" + this.value + "&enable=" + (this.checked ? '0' : '1'),
            success: function (data) {
                if (data.code === 200) {
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg("修改成功！", {icon: 1});

                    }, 500);
                } else {
                    top.layer.close(index);
                    layer.msg(data.message, {icon: 2});
                    tableIns.reload({
                        page: {
                            curr: 1 //重新从第 1 页开始
                        }
                    });
                }
            },
        });
    });

});