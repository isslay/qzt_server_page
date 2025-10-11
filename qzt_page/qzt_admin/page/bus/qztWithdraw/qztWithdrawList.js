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

    var userType = getUrlParmas("userType");
    var auditType = getUrlParmas("auditType");//1受理accept、2确认affirm、3完成finish
    var withdrawMoneyType = getUrlParmas("withdrawMoneyType");//提现金额类型（余额01、抵扣券03）
    withdrawMoneyType = withdrawMoneyType == null || withdrawMoneyType == "" ? "01" : withdrawMoneyType;
    $("#withdrawMoneyType").val(withdrawMoneyType);
    $("#auditType").val(auditType);
    //审核权限操作处理
    var toolbarT = '';
    var auditStateCondition = '';
    headBarName = '';
    var approveUrl = '';//审核通过url
    var rejectionUrl = '';//驳回url
    // auditType = "finish";//accept、affirm、finish
    if (auditType == "accept") {
        headBarName = '受理';
        approveUrl = 'back/qztWithdraw/theBatchAcceptance';
        rejectionUrl = 'back/qztWithdraw/batchAcceptanceRejection';
        toolbarT = '{{#if(d.auditState == 0 ){ }}<a class="layui-btn table_btn layui-btn-xs accept" lay-event="acceptance">受理</a>';
        toolbarT += '<a class="layui-btn table_btn layui-btn-xs layui-btn-danger accept" lay-event="reject">驳回</a>{{#} }}';
        auditStateCondition = '<option value="00">待审核</option><option value="11">已驳回</option><option value="">全部</option>';
    } else if (auditType == "affirm") {
        headBarName = '确认';
        approveUrl = 'back/qztWithdraw/batchConfirmation';
        rejectionUrl = 'back/qztWithdraw/batchConfirmationRejection';
        toolbarT = '{{#if(d.auditState == 1 ){ }}<a class="layui-btn table_btn layui-btn-xs affirm"  lay-event="acceptance">确认</a>';
        toolbarT += '<a class="layui-btn table_btn layui-btn-xs layui-btn-danger affirm"  lay-event="reject">驳回</a>{{#} }}';
        auditStateCondition = '<option value="01">待审核</option><option value="13">已驳回</option><option value="">全部</option>';
    } else if (auditType == "finish") {
        headBarName = '完成';
        approveUrl = 'back/qztWithdraw/batchToComplete';
        rejectionUrl = 'back/qztWithdraw/batchRejection';
        toolbarT = '{{#if(d.auditState == 3 ){ }}<a class="layui-btn table_btn layui-btn-xs finish" lay-event="acceptance">完成</a>';
        toolbarT += '<a class="layui-btn table_btn layui-btn-xs layui-btn-danger finish" lay-event="reject">驳回</a>{{#} }}';
        auditStateCondition = '<option value="03">待审核</option><option value="20">已完成</option><option value="15">已驳回</option><option value="">全部</option>';
    }
    $("#auditState").html(auditStateCondition);
    $("#toolBar").html(toolbarT);
    $("#xblockyy").html('<a id="headBarName" class="layui-btn" lay-submit="" lay-filter="theBatchAcceptance">批量' + headBarName + '</a><a class="layui-btn layui-btn-danger" lay-submit="" lay-filter="batchRejected">批量驳回</a>');
    form.render();
    var colsy = [[
        {type: 'checkbox', fixed: 'left'},
        {field: 'createTime', title: '申请时间', align: 'center', minWidth: 180, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
        {field: 'userName', title: '商家名称', align: 'center', minWidth: 160, templet: '<div>{{d.userName==""?"-":d.userName}}</div>'},
        {field: 'account', title: '商家账号', align: 'center', minWidth: 100, templet: '<div>{{d.account==""?"-":d.account}}</div>'},
        /*{field: 'realName', title: '姓名', align: 'center', minWidth: 100, templet: '<div>{{d.realName==""?"-":d.realName}}</div>'},
        {field: 'userTel', title: '电话', align: 'center', minWidth: 160, templet: '<div>{{d.userTel==""?"-":d.userTel}}</div>'},
        {field: 'cardCode', title: '支付宝账号', align: 'center', minWidth: 180, templet: '<div>{{d.cardCode==""?"-":d.cardCode}}</div>'},*/
        {field: 'bankName', title: '银行名称', align: 'center', minWidth: 180, templet: '<div>{{d.bankName==""?"-":d.bankName}}</div>'},
        {field: 'realName', title: '姓名', align: 'center', minWidth: 100, templet: '<div>{{d.realName==""?"-":d.realName}}</div>'},
        {field: 'cardCode', title: '银行账号', align: 'center', minWidth: 180, templet: '<div>{{d.cardCode==""?"-":d.cardCode}}</div>'},
        {field: 'userTel', title: '预留手机号', align: 'center', minWidth: 180, templet: '<div>{{d.userTel==""?"-":d.userTel}}</div>'},
        {field: 'openingBank', title: '支行名称', align: 'center', minWidth: 180, templet: '<div>{{d.openingBank==""?"-":d.openingBank}}</div>'},
        {field: 'withdrawMoney', title: '提现金额', align: 'right', minWidth: 90, templet: '<div>{{d.withdrawMoney===""?"0.00":d.withdrawMoney.toFixed(2)}}</div>'},
        {field: 'serviceCharge', title: '提现手续费', align: 'right', minWidth: 90, templet: '<div>{{d.serviceCharge===""?"0.00":d.serviceCharge.toFixed(2)}}</div>'},
        {field: 'serviceRatio', title: '提现手续费比例(%)', align: 'center', minWidth: 160, templet: '<div>{{d.serviceRatio===""?"0":d.serviceRatio*100}}%</div>'},
        {field: 'arrivalAmount', title: '实际到账金额', align: 'right', minWidth: 108, templet: '<div>{{d.arrivalAmount===""?"0.00":d.arrivalAmount.toFixed(2)}}</div>'},
        {field: 'auditState', title: '审核状态', align: 'center', minWidth: 160, templet: '<div>{{d.auditState==""?"-":d.auditState}}</div>'},
        {field: 'auditUserName', title: '审核人', align: 'center', minWidth: 180, templet: '<div>{{d.auditUserName==""?"-":d.auditUserName}}</div>'},
        {field: 'auditTime', title: '审核时间', align: 'center', minWidth: 180, templet: '<div>{{d.auditTime==""?"-":d.auditTime}}</div>'},
        {field: 'auditRemark', title: '审核描述', align: 'center', minWidth: 260, templet: '<div>{{d.auditRemark==""?"-":d.auditRemark}}</div>'},
        {field: 'opt', title: '操作', fixed: 'right', width: 160, align: 'center', toolbar: '#toolBar'}
    ]];
    $("#userType").val(userType);
    urls = 'back/qztWithdraw/queryShopAgencyListPage';
    var userTypeMc = "";
    switch (userType) {
        case "1":
            userTypeMc = withdrawMoneyType == "01" ? "用户余额提现" : "加油金提现";
            $("#userId").attr("type", "text");
            urls = 'back/qztWithdraw/queryListPage';
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
                    templet: '<div>{{d.withdrawMoney===""?"0.00":d.withdrawMoney.toFixed(2)}}</div>'
                },
                {
                    field: 'serviceCharge',
                    title: '提现手续费',
                    align: 'right',
                    minWidth: 90,
                    templet: '<div>{{d.serviceCharge===""?"0.00":d.serviceCharge.toFixed(2)}}</div>'
                },
                {field: 'serviceRatio', title: '提现手续费比例(%)', align: 'center', minWidth: 160, templet: '<div>{{d.serviceRatio===""?"0":d.serviceRatio*100}}%</div>'},
                {
                    field: 'arrivalAmount',
                    title: '实际到账金额',
                    align: 'right',
                    minWidth: 108,
                    templet: '<div>{{d.arrivalAmount===""?"0.00":d.arrivalAmount.toFixed(2)}}</div>'
                },
                {field: 'auditState', title: '审核状态', align: 'center', minWidth: 160, templet: '<div>{{d.auditState==""?"-":d.auditState}}</div>'},
                {field: 'auditUserName', title: '审核人', align: 'center', minWidth: 180, templet: '<div>{{d.auditUserName==""?"-":d.auditUserName}}</div>'},
                {field: 'auditTime', title: '审核时间', align: 'center', minWidth: 180, templet: '<div>{{d.auditTime==""?"-":d.auditTime}}</div>'},
                {field: 'auditRemark', title: '审核描述', align: 'center', minWidth: 260, templet: '<div>{{d.auditRemark==""?"-":d.auditRemark}}</div>'},
                {field: 'opt', title: '操作', fixed: 'right', width: 160, align: 'center', toolbar: '#toolBar'}
            ]];
            break;
        case "3":
            $("#account").attr("type", "text");
            $("#userName").attr("type", "text");
            userTypeMc = "超值购商家提现";
            break;
        case "5":
            $("#account").attr("type", "text");
            $("#userName").attr("type", "text");
            userTypeMc = "精选商家提现";
            break;
        case "7":
            $("#account").attr("type", "text");
            $("#userName").attr("type", "text");
            userTypeMc = "联盟商家提现";
            break;
        case "9":
            $("#account").attr("type", "text");
            $("#userName").attr("type", "text");
            $("#account").attr("placeholder", "区域代理账号");
            $("#userName").attr("placeholder", "区域代理名称");
            userTypeMc = "区域代理提现";
            break;
    }
    $("cite").html(userTypeMc + "(" + headBarName + ")");


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
            $("[data-field='userType']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["WITHDRAW_USER_TYPE" + $(this).text()]);
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
    getDicObj("REG_SOURCE,WITHDRAW_USER_TYPE,WITHDRAW_AUDIT_STATE", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztWithdrawFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.id;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'qztWithdrawmodel.html', null, null, null);
        } else if (layEvent === 'acceptance') { //受理、确认、完成
            layer.confirm('您确定要' + headBarName + '吗？', {icon: 3, title: '确认'}, function () {
                var indexMsg = layer.msg(headBarName + '中，请稍候', {icon: 16, time: false, shade: 0.8});
                var pkIds = [];
                pkIds.push(pkId);
                approve(pkIds, indexMsg);
            });
        } else if (layEvent === 'reject') { //驳回
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
                if (index != null && index != "") {
                    layer.close(index);
                }
                if (data.code == 200) {
                    layer.msg(headBarName + "成功", {icon: 1, time: 2000});
                    tableIns.reload({});
                } else {
                    /*layer.open({
                        type: 1,
                        title: false,//不显示标题栏
                        closeBtn: false,
                        area: '500px;',
                        shade: 0.2,
                        id: 'LAY_layuipro', //设定一个id，防止重复弹出
                        btn: ['确认'],
                        btnAlign: 'c',
                        moveType: 1,//拖拽模式，0或者1
                        content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 500;">' + data.message + '</div>'
                    });*/
                    layer.msg(data.message, {icon: 2, time: 5000});
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
                    layer.msg(data.message, {icon: 2, time: 5000});
                }
            }
        });
    };


});