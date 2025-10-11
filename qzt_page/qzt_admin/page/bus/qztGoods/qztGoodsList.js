layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;
    var reg1 = /(^[1-9]\d*|0$)/;
    //页面操作：0：查看，1：添加，2：修改
    pageOperation = 0;
    pkId = "";
    deptId = "";
    urls = 'back/qztGoods/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            //{type: 'checkbox', fixed: 'left'},
            {field: 'id', title: '商品编号', align: 'center', minWidth: 180, templet: '<div>{{d.id==""?"-":d.id}}</div>'},
            // {type: 'numbers', title: '序号', fixed: 'left'},
            {
                field: 'goodsName',
                title: '商品名称',
                align: 'center',
                minWidth: 250,
                templet: '<div>{{d.goodsName==""?"-":d.goodsName}}</div>'
            },
            {
                field: 'typeOrder',
                title: '排序',
                align: 'center',
                edit: 'text',
                minWidth: 10,
                templet: '<div>{{d.typeOrder==""?"-":d.typeOrder}}</div>'
            },
            {
                field: 'goodsNum',
                title: '库存',
                align: 'center',
                edit: 'text',
                minWidth: 50,
                templet: '<div>{{d.goodsNum==""?"-":d.goodsNum}}</div>'
            },
            {
                field: 'goodsPrice',
                title: '商品单价',
                align: 'center',
                minWidth: 100,
                templet: '<div>{{d.goodsPrice==""?"-":d.goodsPrice/100}}</div>'
            },
            {
                field: 'goodsCostPrice',
                title: '成本单价',
                align: 'center',
                minWidth: 100,
                templet: '<div>{{d.goodsCostPrice==""?"-":d.goodsCostPrice/100}}</div>'
            },
            {
                field: 'upTime',
                title: '上架时间',
                align: 'center',
                minWidth: 180,
                templet: '<div>{{d.upTime==""?"-":d.upTime}}</div>'
            },
            {
                field: 'state', title: '商品状态', align: 'center', minWidth: 80, templet: function (d) {
                if (d.state == "0") {
                    return "待上架";
                } else if (d.state == "1") {
                    return "上架";
                } else {
                    return "下架";
                }
            }
            },//（0待上架，1上架，2下架）
            {field: 'opt', title: '操作', fixed: 'right', width: 400, align: 'center', toolbar: '#toolBar'}
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
        /*done: function (res, page, count) {//字典翻译 需要翻译的字段 与 字典类型对应
            var index = 0;
            $("[data-field='orderState']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["GOODS_ORDER_TYPE" + $(this).text()]);
                }
                index++;
            })
            $("[data-field='queuState']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["QUEU_STATE" + $(this).text()]);
                }
                index++;
            })
        },*/
        elem: '#qztGoodsTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztGoodsFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'qztGoodsmodel.html', null, null, null);
        } else if (layEvent === 'edit') { //编辑
            pageOperation = 2;
            xadmin.open('编辑', 'qztGoodsmodel.html', null, null, null);
        } else if (layEvent === 'del') { //删除
            var pkIds = [data.id];
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'back/qztGoods/delBatchByIds',
                    data: JSON.stringify(pkIds),
                    success: function (data) {
                        if (data.code == 200) {
                            obj.del();
                            layer.msg("删除成功", {icon: 1, time: 2000});
                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
            });
        } else if (layEvent === 'banner') { //轮播
            goodsId = data.id;
            xadmin.open(data.goodsName, 'qztGoodsBannerList.html', null, null, 1);
        } else if (layEvent === 'up') { //上架
            $.ajax({
                type: 'POST',
                url: "/back/qztGoods/up/" + data.id,
                success: function (data) {
                    console.log(data);
                    if (data.code == 200) {
                        layer.msg("操作成功", {icon: 1, time: 2000});
                        tableIns.reload({
                            page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        } else if (layEvent === 'down') { //下架
            $.ajax({
                type: 'POST',
                url: "/back/qztGoods/down/" + data.id,
                success: function (data) {
                    console.log(data);
                    if (data.code == 200) {
                        layer.msg("操作成功", {icon: 1, time: 2000});
                        tableIns.reload({
                            page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        } else if (layEvent === 'remove') { //删除
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: "/back/qztGoods/remove/" + data.id,
                    success: function (data) {
                        console.log(data);
                        if (data.code == 200) {
                            layer.msg("操作成功", {icon: 1, time: 2000});
                            tableIns.reload({
                                page: {
                                    curr: 1 //重新从第 1 页开始
                                }
                            });
                        } else {
                            layer.msg(data.message, {icon: 2});
                        }
                    }
                });
            });
        }
    });

    //监听表格复选框选择
    table.on('checkbox(qztGoodsFilter)', function (obj) {
        //obj.type == "all"//全选
        //table.cache.qztGoodsTable;//当前数据表格的id
        //obj.checked//true  false
    });

    //监听单元格编辑
    table.on('edit(qztGoodsFilter)', function (obj) {
        var value = obj.value //得到修改后的值
            , data = obj.data //得到所在行所有键值
            , field = obj.field; //得到字段
        // layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
        var modData = {};
        if (field == "typeOrder" && value == "") {
            value = 100000;
        }
        if (field == "goodsNum" && (value == "" || !reg1.test(value))) {
            value = 0;
        }
        modData["id"] = data.id;
        modData[field] = value;
        modMenuData(modData);
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
    form.on('submit(qztGoodsAdd)', function (obj) {
        pageOperation = 1, pkId = "";
        xadmin.open('添加', 'qztGoodsmodel.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量删除
    form.on('submit(batchDel)', function (obj) {
        var checkStatus = table.checkStatus('qztGoodsFilter');
        if (checkStatus.data.length === 0) {
            layer.msg("请选择要删除的数据", {icon: 0, time: 2000});
            return;
        }
        layer.confirm('确定删除选中的信息？', {icon: 3, title: '确认'}, function (index) {
            var indexMsg = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
            var pkIds = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                pkIds[i] = checkStatus.data[i].id;
            }
            $.ajax({
                type: 'POST',
                url: 'back/qztGoods/delBatchByIds',
                data: JSON.stringify(pkIds),
                success: function (data) {
                    if (data.code == 200) {
                        layer.close(indexMsg);
                        layer.msg("删除成功", {icon: 1, time: 2000});
                        tableIns.reload({
                            page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        });
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    function modMenuData(modData) {
        var index = layer.msg('修改中，请稍候', {icon: 16, time: false, shade: 0.8});
        $.ajax({
            type: "POST",
            url: "back/qztGoods/modify",
            data: JSON.stringify(modData),
            success: function (data) {
                if (data.code === 200) {
                    setTimeout(function () {
                        tableIns.reload({});
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