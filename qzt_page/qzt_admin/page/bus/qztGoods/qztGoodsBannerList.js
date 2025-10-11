layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        table = layui.table;

    $("#busId").val(parent.goodsId);
    busId="";

    //页面操作：0：查看，1：添加，2：修改
    pageOperation = 0;
    pkId = "";
    deptId = "";
    urls = 'back/qztGoodsBanner/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            //{type: 'checkbox', fixed: 'left'},
            {type: 'numbers', title: '序号', fixed: 'left'},
            // {field: 'id', title: '', align: 'center', minWidth: 180, templet: '<div>{{d.id==""?"-":d.id}}</div>'},
            // {field: 'createTime', title: '创建时间', align: 'center', minWidth: 180, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            // {field: 'createBy', title: '创建人', align: 'center', minWidth: 180, templet: '<div>{{d.createBy==""?"-":d.createBy}}</div>'},
            // {field: 'updateTime', title: '修改时间', align: 'center', minWidth: 180, templet: '<div>{{d.updateTime==""?"-":d.updateTime}}</div>'},
            // {field: 'updateBy', title: '修改人', align: 'center', minWidth: 180, templet: '<div>{{d.updateBy==""?"-":d.updateBy}}</div>'},
            // {field: 'busId', title: '业务ID', align: 'center', minWidth: 180, templet: '<div>{{d.busId==""?"-":d.busId}}</div>'},
            // {field: 'bannerName', title: '名称', align: 'center', minWidth: 180, templet: '<div>{{d.bannerName==""?"-":d.bannerName}}</div>'},
            {field: 'bannerUrl', title: '图片', align: 'center', minWidth: 180, templet: '<div><img src="{{d.bannerUrl}}"></div>'},
            {field: 'orderNum', title: '排序', align: 'center', minWidth: 180, templet: '<div>{{d.orderNum==""?"-":d.orderNum}}</div>'},
            // {field: 'state', title: '状态', align: 'center', minWidth: 180, templet: '<div>{{d.state==""?"-":d.state}}</div>'},
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
        elem: '#qztGoodsBannerTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztGoodsBannerFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'qztGoodsBannermodel.html', null, null, null);
        } else if (layEvent === 'edit') { //编辑
            pageOperation = 2;
            xadmin.open('编辑', 'qztGoodsBannermodel.html', null, null, null);
        } else if (layEvent === 'del') { //删除
            var pkIds = [data.id];
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'back/qztGoodsBanner/delBatchByIds',
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
        }
    });

    //监听表格复选框选择
    table.on('checkbox(qztGoodsBannerFilter)', function (obj) {
        //obj.type == "all"//全选
        //table.cache.qztGoodsBannerTable;//当前数据表格的id
        //obj.checked//true  false
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
    form.on('submit(qztGoodsBannerAdd)', function (obj) {
        pageOperation = 1, pkId = "",busId=parent.goodsId;
        xadmin.open('添加', 'qztGoodsBannermodel.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量删除
    form.on('submit(batchDel)', function (obj) {
        var checkStatus = table.checkStatus('qztGoodsBannerFilter');
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
                url: 'back/qztGoodsBanner/delBatchByIds',
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

});