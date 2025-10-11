layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'table', 'laydate'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table;
    //页面操作：0：查看，1：添加，2：修改
    pageOperation = 0;
    pkId = "";
    rejectionType = "";
    headBarName = '';
    //日期1
    laydate.render({
        elem: '#startTime',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });
    //日期2
    laydate.render({
        elem: '#endTime',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    var cardType = getUrlParmas("cardType");
    var approveUrl = '';//审核通过url
    var rejectionUrl = '';//驳回url
    var withdrawMoneyType = getUrlParmas("withdrawMoneyType");//提现金额类型（余额01、抵扣券03）
    withdrawMoneyType = withdrawMoneyType == null || withdrawMoneyType == "" ? "01" : withdrawMoneyType;
    $("#withdrawMoneyType").val(withdrawMoneyType);
    $("#auditType").val("admin");
    $("#cardType").val(cardType);
    form.render();
    var colsy;
    urls = 'back/qztWithdraw/queryListPage';
    if (cardType == "01") {//银行卡
        colsy = [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'createTime', title: '申请时间', align: 'center', minWidth: 180, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'userId', title: '用户ID', align: 'center', minWidth: 160, templet: '<div>{{d.userId==""?"-":d.userId}}</div>'},
            {field: 'realName', title: '姓名', align: 'center', minWidth: 100, templet: '<div>{{d.realName==""?"-":d.realName}}</div>'},
            {field: 'bankName', title: '银行名称', align: 'center', minWidth: 180, templet: '<div>{{d.bankName==""?"-":d.bankName}}</div>'},
            {field: 'cardCode', title: '银行卡号', align: 'center', minWidth: 180, templet: '<div>{{d.cardCode==""?"-":d.cardCode}}</div>'},
            {field: 'userTel', title: '预留手机号', align: 'center', minWidth: 180, templet: '<div>{{d.userTel==""?"-":d.userTel}}</div>'},
            {field: 'openingBank', title: '支行信息', align: 'center', minWidth: 180, templet: '<div>{{d.openingBank==""?"-":d.openingBank}}</div>'},
            {
                field: 'withdrawMoney',
                title: '提现金额',
                align: 'right',
                minWidth: 90,
                templet: '<div>{{d.withdrawMoney===""?"0.00":(d.withdrawMoney/100).toFixed(2)}}</div>'
            },
            {
                field: 'serviceCharge',
                title: '提现手续费',
                align: 'right',
                minWidth: 90,
                templet: '<div>{{d.serviceCharge===""?"0.00":(d.serviceCharge/100).toFixed(2)}}</div>'
            },
            {field: 'serviceRatio', title: '提现手续费比例', align: 'center', minWidth: 130, templet: '<div>{{d.serviceRatio===""?"-":d.serviceRatio}}</div>'},
            {
                field: 'arrivalAmount',
                title: '实际到账金额',
                align: 'right',
                minWidth: 110,
                templet: '<div>{{d.arrivalAmount===""?"0.00":(d.arrivalAmount/100).toFixed(2)}}</div>'
            },
            {field: 'auditState', title: '审核状态', align: 'center', minWidth: 160, templet: '<div>{{d.auditState==""?"-":d.auditState}}</div>'},
            {field: 'auditUserName', title: '审核人', align: 'center', minWidth: 180, templet: '<div>{{d.auditUserName==""?"-":d.auditUserName}}</div>'},
            {field: 'auditTime', title: '审核时间', align: 'center', minWidth: 180, templet: '<div>{{d.auditTime==""?"-":d.auditTime}}</div>'},
            {field: 'auditRemark', title: '驳回理由', align: 'center', minWidth: 260, templet: '<div>{{d.auditRemark==""?"-":d.auditRemark}}</div>'},
            {field: 'opt', title: '操作', fixed: 'right', width: 160, align: 'center', toolbar: '#toolBar'}
        ]];
    } else if (cardType == "02") {//支付宝
        colsy = [[
            {type: 'checkbox', fixed: 'left'},
            {field: 'createTime', title: '申请时间', align: 'center', minWidth: 180, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'userId', title: '用户ID', align: 'center', minWidth: 160, templet: '<div>{{d.userId==""?"-":d.userId}}</div>'},
            {field: 'realName', title: '姓名', align: 'center', minWidth: 100, templet: '<div>{{d.realName==""?"-":d.realName}}</div>'},
            {field: 'userTel', title: '电话', align: 'center', minWidth: 160, templet: '<div>{{d.userTel==""?"-":d.userTel}}</div>'},
            {field: 'cardCode', title: '支付宝账号', align: 'center', minWidth: 180, templet: '<div>{{d.cardCode==""?"-":d.cardCode}}</div>'},
            {
                field: 'withdrawMoney',
                title: '提现金额',
                align: 'right',
                minWidth: 90,
                templet: '<div>{{d.withdrawMoney===""?"0.00":(d.withdrawMoney/100).toFixed(2)}}</div>'
            },
            {
                field: 'serviceCharge',
                title: '提现手续费',
                align: 'right',
                minWidth: 90,
                templet: '<div>{{d.serviceCharge===""?"0.00":(d.serviceCharge/100).toFixed(2)}}</div>'
            },
            {field: 'serviceRatio', title: '提现手续费比例', align: 'center', minWidth: 130, templet: '<div>{{d.serviceRatio===""?"-":d.serviceRatio}}</div>'},
            {
                field: 'arrivalAmount',
                title: '实际到账金额',
                align: 'right',
                minWidth: 110,
                templet: '<div>{{d.arrivalAmount===""?"0.00":(d.arrivalAmount/100).toFixed(2)}}</div>'
            },
            {field: 'auditState', title: '审核状态', align: 'center', minWidth: 160, templet: '<div>{{d.auditState==""?"-":d.auditState}}</div>'},
            {field: 'auditUserName', title: '审核人', align: 'center', minWidth: 180, templet: '<div>{{d.auditUserName==""?"-":d.auditUserName}}</div>'},
            {field: 'auditTime', title: '审核时间', align: 'center', minWidth: 180, templet: '<div>{{d.auditTime==""?"-":d.auditTime}}</div>'},
            {field: 'auditRemark', title: '驳回理由', align: 'center', minWidth: 260, templet: '<div>{{d.auditRemark==""?"-":d.auditRemark}}</div>'},
            {field: 'opt', title: '操作', fixed: 'right', width: 160, align: 'center', toolbar: '#toolBar'}
        ]];
    }
    $("cite").html("用户余额提现");
    $("#userId").attr("type", "text");
    tableIns = table.render({
        //设置表头
        cols: colsy,
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
        done: function (res, page, count) {//字典翻译 需要翻译的字段 与 字典类型对应
            var index = 0;
            $("[data-field='source']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["REG_SOURCE" + $(this).text()]);
                }
                index++;
            })
            $("[data-field='auditState']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["WITHDRAW_AUDIT_STATE" + $(this).text()]);
                }
                index++;
            })
        },
        elem: '#qztWithdrawTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 50, 100]
        }
    });
    getDicObj("REG_SOURCE,WITHDRAW_AUDIT_STATE", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztWithdrawFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'qztWithdrawmodel.html', null, null, null);
        } else if (layEvent === 'acceptance') { //审核
            if (data.auditState == '00') {//受理
                headBarName = '受理';
                approveUrl = 'back/qztWithdraw/theBatchAcceptance';
            } else if (data.auditState == '01') {//确认
                headBarName = '确认';
                approveUrl = 'back/qztWithdraw/batchConfirmation';
            } else if (data.auditState == '03') {//完成
                headBarName = '完成';
                approveUrl = 'back/qztWithdraw/batchToComplete';
            }
            layer.confirm('您确定要' + headBarName + '吗？', {icon: 3, title: '确认'}, function () {
                var indexMsg = layer.msg(headBarName + '中，请稍候', {icon: 16, time: false, shade: 0.8});
                var pkIds = [];
                pkIds.push(pkId);
                approve(pkIds, indexMsg);
            });
        } else if (layEvent === 'reject') { //驳回
            if (data.auditState == '00') {//受理
                rejectionUrl = 'back/qztWithdraw/batchAcceptanceRejection';
            } else if (data.auditState == '01') {//确认
                rejectionUrl = 'back/qztWithdraw/batchConfirmationRejection';
            } else if (data.auditState == '03') {//完成
                rejectionUrl = 'back/qztWithdraw/batchRejection';
            }
            xadmin.open('驳回', 'qztWithdrawmodel.html', 480, 256, null);
        }
    });


    //监听表格复选框选择
    table.on('checkbox(qztWithdrawFilter)', function (obj) {
        //obj.type == "all"//全选
        //table.cache.qztWithdrawTable;//当前数据表格的id
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


    //用户提现记录导出
    $(".excel").click(function () {
        var withdrawIds = "";
        var checkStatus = table.checkStatus('qztWithdrawTable');
        for (var i = 0; i < checkStatus.data.length; i++) {
            if (i == 0) {
                withdrawIds = checkStatus.data[i].id;
            } else {
                withdrawIds += "," + checkStatus.data[i].id;
            }
        }
        $("#withdrawIds").val(withdrawIds);
        excel("searchId", "back/qztexport/withdrawExcel");
    });

    //批量受理
    form.on('submit(theBatchAcceptance)', function (obj) {
        var checkStatus = table.checkStatus('qztWithdrawTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请勾选需要" + headBarName + "的提现申请", {icon: 0, time: 2000});
            return;
        }
        layer.confirm('确定要' + headBarName + '选中的提现申请？', {icon: 3, title: '确认'}, function (index) {
            var indexMsg = layer.msg(headBarName + '中，请稍候', {icon: 16, time: false, shade: 0.8});
            var pkIds = [];
            for (var i = 0; i < checkStatus.data.length; i++) {
                pkIds[i] = checkStatus.data[i].id;
            }
            approve(pkIds, indexMsg);
        });
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量驳回
    form.on('submit(batchRejected)', function (obj) {
        var checkStatus = table.checkStatus('qztWithdrawTable');
        if (checkStatus.data.length === 0) {
            layer.msg("请勾选需要驳回的提现申请", {icon: 0, time: 2000});
            return;
        }
        pkId = checkStatus.data;
        rejectionType = "1";
        xadmin.open('批量驳回', 'qztWithdrawmodel.html', 480, 256, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //监听行单击事件
    table.on('row(qztWithdrawFilter)', function (obj) {
        // console.log(obj.tr) //得到当前行元素对象
        //obj.del(); //删除当前行
        //obj.update(fields) //修改当前行数据
        // console.log(obj.data) //得到当前行数据
    });

    //监听行双击事件
    table.on('rowDouble(qztWithdrawFilter)', function (obj) {
        // console.log(obj.tr) //得到当前行元素对象
        //obj.del(); //删除当前行
        //obj.update(fields) //修改当前行数据
        // console.log(obj.data.userId) //得到当前行数据
    });

    //审核通过
    approve = function (pkIds, index) {
        $.ajax({
            async: false,
            type: 'POST',
            url: approveUrl,
            data: JSON.stringify(pkIds),
            success: function (data) {
                if (data.code == 200) {
                    if (index != null && index != "") {
                        layer.close(index);
                    }
                    layer.msg(headBarName + "成功", {icon: 1, time: 2000});
                    tableIns.reload({});
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    };

    //驳回
    rejection = function (pkIds, auditRemark, index) {
        $.ajax({
            async: false,
            type: 'POST',
            url: rejectionUrl + "?auditRemark=" + auditRemark,
            data: JSON.stringify(pkIds),
            success: function (data) {
                if (data.code == 200) {
                    if (index != null && index != "") {
                        layer.close(index);
                    }
                    layer.msg("驳回成功", {icon: 1, time: 2000});
                    tableIns.reload({});
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    };


});