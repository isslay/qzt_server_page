layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table;

        $("#userId").val(parent.pkId);

    //日期1
    laydate.render({
        elem: '#regStartTime',
        format: 'yyyy-MM-dd'
    });
    //日期2
    laydate.render({
        elem: '#regEndTime',
        format: 'yyyy-MM-dd'
    });

    //页面操作：0：查看，1：添加，2：修改
    pageOperation = 0;
    pkId = "";
    deptId = "";
    urls = 'back/qztAccountLog/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            //{type: 'checkbox', fixed: 'left'},
            {type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'userId', title: '用户Id', align: 'center', minWidth: 70, templet: '<div>{{d.userId==""?"-":d.userId}}</div>'},
            {field: 'changeType', title: '变动类别', align: 'center', minWidth: 80, templet: '<div>{{d.changeType==""?"-":d.changeType}}</div>'},
            {field: 'changeSource', title: '变动来源', align: 'center', minWidth: 80, templet: '<div>{{d.changeSource==""?"-":d.changeSource}}</div>'},
            {field: 'moneyBalance', title: '当前余额', align: 'right', minWidth: 70, templet: '<div>{{d.moneyBalance==""?"-":parseFloat(d.moneyBalance/100).toFixed(2)}}</div>'},
            {field: 'changeNum', title: '变动金额', align: 'right', minWidth: 70, templet: '<div>{{d.changeNum==""?"-":parseFloat(d.changeNum/100).toFixed(2)}}</div>'},
            {field: 'moneyBalanceEnd', title: '变动后余额', align: 'right', minWidth: 70, templet: '<div>{{d.moneyBalanceEnd==""?"-":parseFloat(d.moneyBalanceEnd/100).toFixed(2)}}</div>'},
            {field: 'busId', title: '业务订单ID', align: 'center', minWidth: 200, templet: '<div>{{d.busId==""?"-":d.busId}}</div>'},
            {field: 'remark', title: '描述', align: 'center', minWidth: 240, templet: '<div>{{d.remark==""?"-":d.remark}}</div>'},
            {field: 'createdTime', title: '创建时间', align: 'center', minWidth: 150, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'createBy', title: '操作人', align: 'center', minWidth: 70, templet: '<div>{{d.createBy==""?"-":d.createBy}}</div>'},
            {field: 'logState', title: '消费级别', align: 'center', minWidth: 70, templet: '<div>{{(d.logState=="")?"0":d.logState}}</div>'}

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
            var that = this.elem.next();
            for(var i=0;i<res.data.length;i++){
                if(res.data[i]["moneyLogState"]=="1"){
                    that.find(".layui-table-box tbody tr[data-index='" + i + "']").css("background-color", "#7CCD7C");
                    //多选记录
                    /*res.data[i]["LAY_CHECKED"]='true';
                    var index= res.data[i]['LAY_TABLE_INDEX'];
                    $('.layui-table tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
                    $('.layui-table tr[data-index=' + index + '] input[type="checkbox"]').next().addClass('layui-form-checked');*/
                }
            }
            $("[data-field='changeSource']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["ACCOUNT_CHANGE_SOURCE" + $(this).text()]);
                }
                index++;
            })
            $("[data-field='changeType']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["ACCOUNT_CHANGE_TYPE" + $(this).text()]);
                }
                index++;
            })
            $("[data-field='logState']").children().each(function () {
                if (index > 0) {
                    console.log($(this).text());
                    $(this).text(dic["SERVICE_TYPE" + $(this).text()]);
                }
                index++;
            })
        },
        elem: '#klygAccountLogTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("ACCOUNT_CHANGE_SOURCE,ACCOUNT_CHANGE_TYPE,SERVICE_TYPE", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(klygAccountLogFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'klygAccountLogmodel.html', null, null, null);
        } else if (layEvent === 'edit') { //编辑
            pageOperation = 2;
            xadmin.open('编辑', 'klygAccountLogmodel.html', null, null, null);
        } else if (layEvent === 'del') { //删除
            var pkIds = [data.id];
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'back/klygAccountLog/delBatchByIds',
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
    table.on('checkbox(klygAccountLogFilter)', function (obj) {
        //obj.type == "all"//全选
        //table.cache.klygAccountLogTable;//当前数据表格的id
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
    form.on('submit(klygAccountLogAdd)', function (obj) {
        pageOperation = 1, pkId = "";
        xadmin.open('添加', 'klygAccountLogmodel.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量删除
    form.on('submit(batchDel)', function (obj) {
        var checkStatus = table.checkStatus('klygAccountLogFilter');
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
                url: 'back/klygAccountLog/delBatchByIds',
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
    })
});