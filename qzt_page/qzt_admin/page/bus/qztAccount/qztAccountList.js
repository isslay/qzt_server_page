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
    urls = 'back/qztAccount/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            {type: 'numbers', title: '序号', fixed: 'left', rowspan: 2},
            {
                field: 'userId',
                title: '用户ID',
                align: 'center',
                rowspan: 2,
                minWidth: 100,
                templet: '<div><a href="#" title="查看用户详情"  lay-event="qureyUserInfo" class="layui-table-link">{{d.objUser.userId==""?"-":d.objUser.userId}}</a></div>'
            },
            {field: 'mobile', title: '手机号', align: 'center', rowspan: 2, minWidth: 120, templet: '<div>{{d.objUser.mobile==""?"-":d.objUser.mobile}}</div>'},
            {field: 'userType', title: '用户类型', align: 'center', rowspan: 2, minWidth: 180, templet: '<div>{{d.objUser.userType}}</div>'},
            {
                field: 'accountMoney',
                title: '余额',
                align: 'right',
                rowspan: 2,
                sort: true,
                minWidth: 120,
                templet: '<div><a href="#" title="查看余额"  lay-event="qureyMoney" class="layui-table-link">{{d.shareMoney.userAccount}}</a></div>'
            },

            {title: '<b>推广佣金</b>', align: 'center', colspan: 4},
            {title: '<b>分享佣金</b>', align: 'center', colspan: 4},
        ],
            [
                /*券总额*/
                {
                    field: 'allMoney',
                    title: '合计',
                    align: 'right',
                    minWidth: 100,
                    templet: '<div><a href="#" title="推广佣金流水"  lay-event="qureyTMoney" class="layui-table-link">{{d.tMoneyMess.allMoney==undefined?"0.00":d.tMoneyMess.allMoney}}</a></div>'
                },
                {
                    field: 'all_zqg',
                    title: '待激活',
                    align: 'right',
                    minWidth: 100,
                    templet: '<div>{{d.tMoneyMess.haveMoney==undefined?"0.00":d.tMoneyMess.haveMoney}}</div>'
                },
                {
                    field: 'all_xsq',
                    title: '已激活',
                    align: 'right',
                    minWidth: 100,
                    templet: '<div>{{d.tMoneyMess.changeMoney==undefined?"0.00":d.tMoneyMess.changeMoney}}</div>'
                },
                {
                    field: 'all_qqg',
                    title: '已赠送',
                    align: 'right',
                    minWidth: 100,
                    templet: '<div>{{d.tMoneyMess.giveMoney==undefined?"0.00":d.tMoneyMess.giveMoney}}</div>'
                },

                /*已激活*/
                {
                    field: 'giveMoney',
                    title: '合计',
                    align: 'right',
                    minWidth: 100,
                    templet: '<div><a href="#" title="激活佣金流水"  lay-event="qureySMoney" class="layui-table-link">{{d.shareMoney.sMoney==undefined?"0.00":d.shareMoney.sMoney}}</a></div>'
                },
                {
                    field: 'give_xsq',
                    title: '待激活',
                    align: 'right',
                    minWidth: 100,
                    templet: '<div>{{d.sDMoneyMess.haveMoney==undefined?"0.00":d.sDMoneyMess.haveMoney}}</div>'
                },
                {
                    field: 'give_qqg',
                    title: '已激活',
                    align: 'right',
                    minWidth: 100,
                    templet: '<div>{{d.sMoneyMess.changeMoney==undefined?"0.00":d.sMoneyMess.changeMoney}}</div>'
                },
                {
                    field: 'give_zqg',
                    title: '已过期',
                    align: 'right',
                    minWidth: 100,
                    templet: '<div>{{d.sMoneyMess.haveMoney==undefined?"0.00":d.sMoneyMess.haveMoney}}</div>'
                }
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
            $("[data-field='userType']").children().each(function () {
                if (index > 0) {
                    console.log($(this).text());
                    $(this).text(dic["SERVICE_TYPE" + $(this).text()]);
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
    getDicObj("SERVICE_TYPE", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(klygAccountFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = obj.data.obj.userId;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'qureyTMoney') { //宝石花详情
            pageOperation = 0;
            xadmin.open('查看推广佣金详情', 'qztAccountTMoneyList.html', null, null, null);
        } else if (layEvent === 'qureyMoney') { //余额详情
            pageOperation = 0;
            xadmin.open('查看余额详情', 'qztAccountMoneyList.html', null, null, null);
        } else if (layEvent === 'qureySMoney') { //抵扣券详情
            pageOperation = 0;
            xadmin.open('查看分享佣金详情', 'qztAccountShareMoneyList.html', null, null, null);
        } else if (layEvent === 'qureyUserInfo') { //用户详情
            pageOperation = 0;
            xadmin.open('查看用户详情', '../qztUser/qztUsermodel.html', null, null, null);
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

    //添加
    form.on('submit(klygAccountAdd)', function (obj) {
        pageOperation = 1, pkId = "";
        xadmin.open('添加', 'klygAccountmodel.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量删除
    form.on('submit(batchDel)', function (obj) {
        var checkStatus = table.checkStatus('klygAccountFilter');
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
                url: 'back/klygAccount/delBatchByIds',
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

    //监听表格排序
    table.on('sort(klygAccountFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
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
                break
            case 'recommendMoney':
                orderByField = "recommend_money";
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
    var userArr = new Array();

    //获取用户信息
    function queryUserById(userId) {
        var realName = userArr[userId];
        if (realName == null) {
            $.ajax({
                type: 'GET',
                url: "/back/qztUser/getUserMess/" + userId,
                dataType: "JSON",
                async: false,
                success: function (data) {
                    if (data.code == 200) {
                        realName = data.data.realName == "" || data.data.realName == null ? "无姓名" : data.data.realName;
                        userArr[userId.toString()] = realName;
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
        }
        return realName;
    }
});