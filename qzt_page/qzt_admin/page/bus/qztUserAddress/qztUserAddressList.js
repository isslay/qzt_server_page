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
    urls = 'back/qztUserAddress/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            //{type: 'checkbox', fixed: 'left'},
            {type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'id', title: '', align: 'center', minWidth: 180, templet: '<div>{{d.id==""?"-":d.id}}</div>'},
            {field: 'createTime', title: '创建时间', align: 'center', minWidth: 180, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'createBy', title: '创建人', align: 'center', minWidth: 180, templet: '<div>{{d.createBy==""?"-":d.createBy}}</div>'},
            {field: 'updateTime', title: '更新时间', align: 'center', minWidth: 180, templet: '<div>{{d.updateTime==""?"-":d.updateTime}}</div>'},
            {field: 'updateBy', title: '更新人', align: 'center', minWidth: 180, templet: '<div>{{d.updateBy==""?"-":d.updateBy}}</div>'},
            {field: 'userId', title: '用户ID', align: 'center', minWidth: 180, templet: '<div>{{d.userId==""?"-":d.userId}}</div>'},
            {field: 'province', title: '省', align: 'center', minWidth: 180, templet: '<div>{{d.province==""?"-":d.province}}</div>'},
            {field: 'city', title: '市', align: 'center', minWidth: 180, templet: '<div>{{d.city==""?"-":d.city}}</div>'},
            {field: 'area', title: '区', align: 'center', minWidth: 180, templet: '<div>{{d.area==""?"-":d.area}}</div>'},
            {field: 'zipCode', title: '邮编', align: 'center', minWidth: 180, templet: '<div>{{d.zipCode==""?"-":d.zipCode}}</div>'},
            {field: 'detailAddress', title: '详细地址', align: 'center', minWidth: 180, templet: '<div>{{d.detailAddress==""?"-":d.detailAddress}}</div>'},
            {field: 'recipientName', title: '姓名', align: 'center', minWidth: 180, templet: '<div>{{d.recipientName==""?"-":d.recipientName}}</div>'},
            {field: 'phone', title: '联系电话', align: 'center', minWidth: 180, templet: '<div>{{d.phone==""?"-":d.phone}}</div>'},
            {field: 'isDefault', title: '是否默认地址（是Y/ 否N）', align: 'center', minWidth: 180, templet: '<div>{{d.isDefault==""?"-":d.isDefault}}</div>'},
            {field: 'isDel', title: '是否删除（是Y/ 否N）', align: 'center', minWidth: 180, templet: '<div>{{d.isDel==""?"-":d.isDel}}</div>'},
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
        elem: '#qztUserAddressTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztUserAddressFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'qztUserAddressmodel.html', null, null, null);
        } else if (layEvent === 'edit') { //编辑
            pageOperation = 2;
            xadmin.open('编辑', 'qztUserAddressmodel.html', null, null, null);
        } else if (layEvent === 'del') { //删除
            var pkIds = [data.id];
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'back/qztUserAddress/delBatchByIds',
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
    table.on('checkbox(qztUserAddressFilter)', function (obj) {
        //obj.type == "all"//全选
        //table.cache.qztUserAddressTable;//当前数据表格的id
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
    form.on('submit(qztUserAddressAdd)', function (obj) {
        pageOperation = 1, pkId = "";
        xadmin.open('添加', 'qztUserAddressmodel.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量删除
    form.on('submit(batchDel)', function (obj) {
        var checkStatus = table.checkStatus('qztUserAddressFilter');
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
                url: 'back/qztUserAddress/delBatchByIds',
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