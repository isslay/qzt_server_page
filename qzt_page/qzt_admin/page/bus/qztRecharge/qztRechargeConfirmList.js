layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table;
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
    urls = 'back/qztRecharge/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'checkbox', fixed: 'left'},
            //{type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'createTime', title: '申请时间', align: 'center', minWidth: 180, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'userId', title: '充值用户ID', align: 'center', minWidth: 100, templet: '<div>{{d.userId==""?"-":d.userId}}</div>'},
            {field: 'userTel', title: '手机号', align: 'center', minWidth: 120, templet: '<div>{{d.userTel==""?"-":d.userTel}}</div>'},
            {field: 'topUpMoney', title: '充值金额', align: 'right', minWidth: 120, templet: '<div>{{parseFloat(d.topUpMoney/100).toFixed(2)}}</div>'},
            {field: 'auditState', title: '审核状态', align: 'center', minWidth: 120, templet: '<div>{{d.auditState}}</div>'},
            {field: 'auditUserName', title: '审核人', align: 'center', minWidth: 120, templet: '<div>{{d.auditUserName==""?"-":d.auditUserName}}</div>'},
            {field: 'auditTime', title: '审核时间', align: 'center', minWidth: 180, templet: '<div>{{d.auditTime==""?"-":d.auditTime}}</div>'},
            {field: 'auditRemark', title: '备注', align: 'center', minWidth: 120, templet: '<div>{{d.auditRemark==""?"-":d.auditRemark}}</div>'},
            // {field: 'createBy', title: '创建人', align: 'center', minWidth: 180, templet: '<div>{{d.createBy==""?"-":d.createBy}}</div>'},
            {field: 'opt', title: '操作', fixed: 'right', width: 150, align: 'center', toolbar: '#toolBar'}
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
            $("[data-field='auditState']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["RECHARGE_STATE" + $(this).text()]);
                }
                index++;
            })


        },
        elem: '#klygAccountTable',
        autoSort: false,
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("RECHARGE_STATE", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(klygAccountFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        //deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent == 'QR') {
            var pkIds = [];
            pkIds.push(data.id);

            layer.confirm('您确定要提交受理吗？', {icon: 3, title: '确认'}, function () {
                approve(pkIds, '10');
            });
        }
        if (layEvent == 'reject') {
                pkId = data.id;
                rejectionType = "0";
                xadmin.open('充值驳回', 'qztRechargemodel.html', 480, 256, null);
        }

    });

    //监听表格复选框选择
    table.on('checkbox(klygAccountFilter)', function (obj) {
        //obj.type == "all"//全选
        //table.cache.klygAccountTable;//当前数据表格的id
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

    //查看充值记录
    form.on('submit(rechargeLog)', function (obj) {
        pageOperation = 1, pkId = "";
        xadmin.open('充值', 'klygAccountLogList.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量受理
    form.on('submit(theBatchAcceptance)', function (obj) {
        var checkStatus = table.checkStatus('klygAccountTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请勾选需要审核的申请记录", {icon: 0, time: 2000});
            return;
        }
        layer.confirm('确定要批量提交选中的充值申请？', {icon: 3, title: '确认'}, function (index) {
            var indexMsg = layer.msg('提交中，请稍候', {icon: 16, time: false, shade: 0.8});
            var pkIds = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                pkIds[i] = checkStatus.data[i].id;
            }
            approve(pkIds, '10');
        });
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量驳回
    form.on('submit(batchRejected)', function (obj) {
        var checkStatus = table.checkStatus('klygAccountTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请勾选需要驳回的充值申请", {icon: 0, time: 2000});
            return;
        }
        pkId = checkStatus.data;
        rejectionType = "1";
        xadmin.open('充值驳回', 'qztRechargemodel.html', 480, 256, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });
    //审核通过
    approve = function (pkIds, state) {
        $.ajax({
            async: false,
            type: 'POST',
            url: 'back/qztRecharge/updateRechargeState?auditState='+state,
            data: JSON.stringify(pkIds),
            success: function (data) {
                if (data.code == 200) {
                    layer.msg("受理成功", {icon: 1, time: 2000});
                    tableIns.reload({});
                } else {
                    layer.msg(data.message, {icon: 2, time: 5000});
                }
            }
        });
    };

    //驳回
    rejection = function (pkIds, auditRemark) {
        $.ajax({
            async: false,
            type: 'POST',
            url: 'back/qztRecharge/updateRechargeState?auditState=10&auditRemark=' + auditRemark,
            data: JSON.stringify(pkIds),
            success: function (data) {
                if (data.code == 200) {
                    layer.msg("驳回成功", {icon: 1, time: 2000});
                    tableIns.reload({});
                } else {
                    layer.msg(data.message, {icon: 2, time: 5000});
                }
            }
        });
    };



    //监听表格排序
    table.on('sort(klygAccountFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var orderByField = "";
        var ascType = obj.type;

        switch (obj.field) {
            case 'accountCoin':
                orderByField = "account_coin";
                break;
            case 'accountMoney':
                orderByField = "account_money";
                break;
            case 'deductionMoney':
                orderByField = "deduction_money";
                break;
            case 'usedCoin':
                orderByField = "used_coin";
                break;
            case 'usedMoney':
                orderByField = "used_money";
                break;
            case 'frozenCoin':
                orderByField = "frozen_coin";
                break;
            case 'frozenMoney':
                orderByField = "frozen_money";
                break;
        }
        $("#orderByField").val(orderByField);
        if (ascType != null && ascType != "") {
            $("#ascType").val(ascType);
        }
        tableIns.reload({
            initSort: obj, //记录初始排序，如果不设的话，将无法标记表头的排序状态。 layui 2.1.1 新增参数
            where: { //设定异步数据接口的额外参数，任意设
                condition: getSearchParams("searchId")
            },
            page: {
                curr: 1
            },
        });
    });
    //导出
    $(".excel").click(function () {
        excel("searchId", "back/qztexport/rechargeExcel");
    });


});