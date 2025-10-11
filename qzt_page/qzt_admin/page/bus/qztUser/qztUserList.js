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
    deptId = "";
    isDemotion = "";
    userType = "";
    laydate.render({
        elem: '#regStartTime',
        format: 'yyyy-MM-dd'
    });
    //日期2
    laydate.render({
        elem: '#regEndTime',
        format: 'yyyy-MM-dd'
    });
    urls = 'back/qztUser/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            //{type: 'checkbox', fixed: 'left'},
            {type: 'numbers', title: '序号', fixed: 'left'},
            {field: 'userId', title: '用户ID', align: 'center', minWidth: 180, templet: '<div>{{d.userId==""?"-":d.userId}}</div>'},
            {field: 'createTime', title: '创建时间', align: 'center', minWidth: 180, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            {field: 'mobile', title: '手机号', align: 'center', minWidth: 180, templet: '<div>{{d.mobile==""?"-":d.mobile}}</div>'},
            // {field: 'wxNickName', title: '微信名称', align: 'center', minWidth: 180, templet: '<div>{{d.wxNickName==""?"-":d.wxNickName}}</div>'},
            {field: 'realName', title: '真实姓名', align: 'center', minWidth: 180, templet: '<div>{{d.realName==""?"-":d.realName}}</div>'},
            {field: 'state', title: '拉黑状态', align: 'center', width: 160, templet: '#switchTpl', event: 'editColumn'},
            // {field: 'userType', title: '用户身份', align: 'center', minWidth: 180, templet: '<div>{{d.userType}}</div>'},
            {field: 'referrerFirst', title: '推荐人ID', align: 'center', minWidth: 100, templet: '<div>{{d.referrerFirst==""?"-":d.referrerFirst}}</div>'},
            {field: 'referrerSecond', title: '推荐人', align: 'center', minWidth: 100, templet: '<div>{{d.referrerSecond==""?"-":d.referrerSecond}}</div>'},
            {field: 'phShortName', title: '推荐人部门', align: 'center', minWidth: 100, templet: '<div>{{d.phShortName==""?"-":d.phShortName}}</div>'},
            {field: 'wxOpenId', title: '微信openId', align: 'center', minWidth: 260, templet: '<div>{{d.wxOpenId==""?"-":d.wxOpenId}}</div>'},
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
        done: function (res, page, count) {//字典翻译 需要翻译的字段 与 字典类型对应
            var index = 0;
            $("[data-field='userType']").children().each(function () {
                if (index > 0) {
                    $(this).text(dic["SERVICE_TYPE" + $(this).text()]);
                }
                index++;
            })
        },
        elem: '#qztUserTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("SERVICE_TYPE", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(qztUserFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.userId;
        deptId = data.deptId;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            pageOperation = 0;
            xadmin.open('查看', 'qztUsermodel.html', null, null, null);
        } else if (layEvent === 'edit') { //修改身份
            isDemotion = data.isDemotion;
            userType = data.userType;
            xadmin.open('修改身份', 'qzt_update_usertype.html', 560, 260, null);
        } else if (layEvent === 'addSuperior') { //添加推荐人
            layer.prompt({title: '请输入推荐人ID', formType: 0, maxlength: 5}, function (value, index) {
                if (!/^[0-9]+$/.test(value)) {
                    layer.msg("请输入正确的推荐人ID");
                } else if (data.userId == value) {
                    layer.msg("推荐人ID不可为当前用户");
                } else {
                    $.ajax({
                        type: 'POST',
                        url: "back/qztUser/addSuperior",
                        data: JSON.stringify({
                            userId: data.userId,
                            referrerSecond: value
                        }),
                        success: function (data) {
                            if (data.code == "200") {
                                layer.msg("添加推荐人成功！", {icon: 1});
                                tableIns.reload();
                                layer.close(index);
                            } else {
                                layer.msg(data.message, {icon: 2});
                            }
                        }
                    });
                }
            });
        }
    });

    //监听表格复选框选择
    table.on('checkbox(qztUserFilter)', function (obj) {
        //obj.type == "all"//全选
        //table.cache.qztUserTable;//当前数据表格的id
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
    form.on('submit(qztUserAdd)', function (obj) {
        pageOperation = 1, pkId = "";
        xadmin.open('添加', 'qztUsermodel.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    form.on('switch(state)', function (obj) {
        console.log(this.value);
        $.ajax({
            type: "POST",
            url: "back/qztUser/blacklist?userId=" + Number(this.value) + "&state=" + (this.checked ? '0' : '2'),
            //data: JSON.stringify({"userId":Number(this.value),"state":(this.checked ? '0' : '2')}),
            success: function (data) {
                if (data.code == 200) {
                    setTimeout(function () {
                        layer.msg("修改状态成功！", {icon: 1, time: 2000});
                    }, 500);
                } else {
                    layer.msg(data.message, {icon: 2, time: 2000});
                    tableIns.reload({
                        page: {
                            curr: 1 //重新从第 1 页开始
                        }
                    });
                }
            },
        });
    });

    //导出
    $(".excel").click(function () {
        excel("searchId", "back/qztexport/orderExcel?orderType=USER");
    });

});