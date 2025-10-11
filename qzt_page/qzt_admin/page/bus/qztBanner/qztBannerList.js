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
    urls = 'back/qztBanner/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            //{type: 'checkbox', fixed: 'left'},
            // {type: 'numbers', title: '序号', fixed: 'left'},
            // {field: 'id', title: '', align: 'center', minWidth: 180, templet: '<div>{{d.id==""?"-":d.id}}</div>'},
            // {field: 'createTime', title: '创建时间', align: 'center', minWidth: 180, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            // {field: 'createBy', title: '创建人', align: 'center', minWidth: 180, templet: '<div>{{d.createBy==""?"-":d.createBy}}</div>'},
            // {field: 'updateTime', title: '修改时间', align: 'center', minWidth: 180, templet: '<div>{{d.updateTime==""?"-":d.updateTime}}</div>'},
            // {field: 'updateBy', title: '修改人', align: 'center', minWidth: 180, templet: '<div>{{d.updateBy==""?"-":d.updateBy}}</div>'},
            {
                field: 'bannerType', title: '类型', align: 'center', minWidth: 180, templet: function (d) {
                if (d.bannerType == "1") {
                    return "首页轮播";
                } else if (d.bannerType == "2") {
                    return "首页说明";
                } else if (d.bannerType == "3") {
                    return "商城页";
                } else {
                    return "";
                }
            }
            },//（1：首页，2：联盟商家页，3：商城页）
            // {
            //     field: 'bannerSource',
            //     title: '资源',
            //     align: 'center',
            //     minWidth: 180,
            //     templet: '<div>{{d.bannerSource==""?"-":d.bannerSource}}</div>'
            // },//WX,IOS,AN
            {
                field: 'bannerNum',
                title: '顺序',
                align: 'center',
                edit: 'text',
                minWidth: 180,
                templet: '<div>{{d.bannerNum==""?"-":d.bannerNum}}</div>'
            },
            {
                field: 'picUrl',
                title: '图片',
                align: 'center',
                minWidth: 180,
                templet: '<div><img src="{{d.picUrl}}"></div>'
            },
            {field: 'linkUrl', title: '商品编号', align: 'center', minWidth: 180, templet: '<div>{{d.linkUrl==""?"-":d.linkUrl}}</div>'},
            // {field: 'remark', title: '备注', align: 'center', minWidth: 180, templet: '<div>{{d.remark==""?"-":d.remark}}</div>'},
            {
                field: 'state', title: '状态', align: 'center', minWidth: 180, templet: function (d) {
                if (d.state == "01") {
                    return "上架";
                } else {
                    return "下架";
                }
            }
            },//（01：上架；02 下架）
            {field: 'opt', title: '操作', fixed: 'right', width: 260, align: 'center', toolbar: '#toolBar'}
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
        elem: '#qztBannerTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztBannerFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'qztBannermodel.html', null, null, null);
        } else if (layEvent === 'edit') { //编辑
            pageOperation = 2;
            xadmin.open('编辑', 'qztBannermodel.html', null, null, null);
        } else if (layEvent === 'del') { //删除
            var pkIds = [data.id];
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'back/qztBanner/delBatchByIds',
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
        } else if (layEvent === 'up') { //上架
            $.ajax({
                type: 'POST',
                url: "/back/qztBanner/up/" + data.id,
                success: function (data) {
                    // console.log(data);
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
                url: "/back/qztBanner/down/" + data.id,
                success: function (data) {
                    // console.log(data);
                    if (data.code == 200) {
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
        }
    });

    //监听表格复选框选择
    table.on('checkbox(qztBannerFilter)', function (obj) {
        //obj.type == "all"//全选
        //table.cache.qztBannerTable;//当前数据表格的id
        //obj.checked//true  false
    });

    //监听单元格编辑
    table.on('edit(qztBannerFilter)', function (obj) {
        var value = obj.value //得到修改后的值
            , data = obj.data //得到所在行所有键值
            , field = obj.field; //得到字段
        // layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
        var modData = {};
        if (value == "") {
            value = 1;  
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
    form.on('submit(qztBannerAdd)', function (obj) {
        pageOperation = 1, pkId = "";
        xadmin.open('添加', 'qztBannermodel.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量删除
    form.on('submit(batchDel)', function (obj) {
        var checkStatus = table.checkStatus('qztBannerFilter');
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
                url: 'back/qztBanner/delBatchByIds',
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
            url: "back/qztBanner/modify",
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