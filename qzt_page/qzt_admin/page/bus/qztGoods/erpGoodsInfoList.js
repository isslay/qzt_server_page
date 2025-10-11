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
    urls = 'back/erpGoodsInfo/queryListPage';
    tableIns = table.render({
        //设置表头
        cols: [[
            //{type: 'checkbox', fixed: 'left'},
            {type: 'numbers', title: '序号', fixed: 'left'},

            {field: 'bzgg', title: '店铺', align: 'center', minWidth: 180, templet: '<div>{{d.bzgg==""?"-":d.bzgg}}</div>'},
            {field: 'goodsName', title: '商品名称', align: 'center', minWidth: 180, templet: '<div>{{d.goodsName==""?"-":d.goodsName}}</div>'},
            {field: 'goodsSpec', title: '规格/型号', align: 'center', minWidth: 180, templet: '<div>{{d.goodsSpec==""?"-":d.goodsSpec}}</div>'},
            {field: 'retailP', title: '零售价', align: 'center', minWidth: 180, templet: '<div>{{d.retailP==""?"-":d.retailP}}</div>'},
            {field: 'orgId', title: '机构ID', align: 'center', minWidth: 180, templet: '<div>{{d.orgId==""?"-":d.orgId}}</div>'},
            {field: 'goodsId', title: '商品ID', align: 'center', minWidth: 180, templet: '<div>{{d.goodsId==""?"-":d.goodsId}}</div>'},


            // {field: 'createTime', title: '录入时间', align: 'center', minWidth: 180, templet: '<div>{{d.createTime==""?"-":d.createTime}}</div>'},
            
            // {field: 'goodsId', title: '商品ID', align: 'center', minWidth: 180, templet: '<div>{{d.goodsId==""?"-":d.goodsId}}</div>'},
            // {field: 'goodsCode', title: '商品编号', align: 'center', minWidth: 180, templet: '<div>{{d.goodsCode==""?"-":d.goodsCode}}</div>'},
            
            // {field: 'logogram', title: '助记码', align: 'center', minWidth: 180, templet: '<div>{{d.logogram==""?"-":d.logogram}}</div>'},
            
            // {field: 'manufacturer', title: '生产厂商', align: 'center', minWidth: 180, templet: '<div>{{d.manufacturer==""?"-":d.manufacturer}}</div>'},
            
            // {field: 'barCode', title: '商品条码', align: 'center', minWidth: 180, templet: '<div>{{d.barCode==""?"-":d.barCode}}</div>'},
            // {field: 'brandName', title: '商品品牌', align: 'center', minWidth: 180, templet: '<div>{{d.brandName==""?"-":d.brandName}}</div>'},
            // {field: 'chemName', title: '化学名称', align: 'center', minWidth: 180, templet: '<div>{{d.chemName==""?"-":d.chemName}}</div>'},
            // {field: 'standCode', title: '药品本位码', align: 'center', minWidth: 180, templet: '<div>{{d.standCode==""?"-":d.standCode}}</div>'},
            // {field: 'unit', title: '基本单位', align: 'center', minWidth: 180, templet: '<div>{{d.unit==""?"-":d.unit}}</div>'},
            // {field: 'generalName', title: '通用名称', align: 'center', minWidth: 180, templet: '<div>{{d.generalName==""?"-":d.generalName}}</div>'},
            // {field: 'lifeType', title: '保质期方式', align: 'center', minWidth: 180, templet: '<div>{{d.lifeType==""?"-":d.lifeType}}</div>'},
            // {field: 'dayUnit', title: '保质期单位', align: 'center', minWidth: 180, templet: '<div>{{d.dayUnit==""?"-":d.dayUnit}}</div>'},
            // {field: 'inEffectDay', title: '保质期', align: 'center', minWidth: 180, templet: '<div>{{d.inEffectDay==""?"-":d.inEffectDay}}</div>'},
            // {field: 'regMark', title: '注册商标', align: 'center', minWidth: 180, templet: '<div>{{d.regMark==""?"-":d.regMark}}</div>'},
            // {field: 'regMarkValidity', title: '注册商标效期', align: 'center', minWidth: 180, templet: '<div>{{d.regMarkValidity==""?"-":d.regMarkValidity}}</div>'},
            // {field: 'place', title: '产地', align: 'center', minWidth: 180, templet: '<div>{{d.place==""?"-":d.place}}</div>'},
            // {field: 'goodsLm', title: '商品分类', align: 'center', minWidth: 180, templet: '<div>{{d.goodsLm==""?"-":d.goodsLm}}</div>'},
            // {field: 'spflA', title: '一级分类', align: 'center', minWidth: 180, templet: '<div>{{d.spflA==""?"-":d.spflA}}</div>'},
            // {field: 'spflB', title: '二级分类', align: 'center', minWidth: 180, templet: '<div>{{d.spflB==""?"-":d.spflB}}</div>'},
            // {field: 'spflC', title: '三级分类', align: 'center', minWidth: 180, templet: '<div>{{d.spflC==""?"-":d.spflC}}</div>'},
            // {field: 'spflD', title: '四级分类', align: 'center', minWidth: 180, templet: '<div>{{d.spflD==""?"-":d.spflD}}</div>'},
            // {field: 'entrustName', title: '受托方', align: 'center', minWidth: 180, templet: '<div>{{d.entrustName==""?"-":d.entrustName}}</div>'},
            // {field: 'entrustAddress', title: '受托方地址', align: 'center', minWidth: 180, templet: '<div>{{d.entrustAddress==""?"-":d.entrustAddress}}</div>'},
            // {field: 'scdz', title: '生产地址', align: 'center', minWidth: 180, templet: '<div>{{d.scdz==""?"-":d.scdz}}</div>'},
            // {field: 'newRepOrigin', title: '新品上报来源', align: 'center', minWidth: 180, templet: '<div>{{d.newRepOrigin==""?"-":d.newRepOrigin}}</div>'},
            // {field: 'sanZz', title: '三支柱分类', align: 'center', minWidth: 180, templet: '<div>{{d.sanZz==""?"-":d.sanZz}}</div>'},
            // {field: 'swflCode', title: '税务分类编码', align: 'center', minWidth: 180, templet: '<div>{{d.swflCode==""?"-":d.swflCode}}</div>'},
            // {field: 'ckbs', title: '参考标识', align: 'center', minWidth: 180, templet: '<div>{{d.ckbs==""?"-":d.ckbs}}</div>'},
            // {field: 'dwbs', title: '定位标识', align: 'center', minWidth: 180, templet: '<div>{{d.dwbs==""?"-":d.dwbs}}</div>'},
            // {field: 'yyfl', title: '运营分类', align: 'center', minWidth: 180, templet: '<div>{{d.yyfl==""?"-":d.yyfl}}</div>'},
            // {field: 'yyflB', title: '运营二级分类', align: 'center', minWidth: 180, templet: '<div>{{d.yyflB==""?"-":d.yyflB}}</div>'},
            // {field: 'yyflC', title: '运营三级分类', align: 'center', minWidth: 180, templet: '<div>{{d.yyflC==""?"-":d.yyflC}}</div>'},
            // {field: 'yyflD', title: '运营四级分类', align: 'center', minWidth: 180, templet: '<div>{{d.yyflD==""?"-":d.yyflD}}</div>'},
            // {field: 'jgfl', title: '结构分类', align: 'center', minWidth: 180, templet: '<div>{{d.jgfl==""?"-":d.jgfl}}</div>'},
            // {field: 'csspfl', title: '财税商品分类', align: 'center', minWidth: 180, templet: '<div>{{d.csspfl==""?"-":d.csspfl}}</div>'},
            // {field: 'regDates', title: '注册日期', align: 'center', minWidth: 180, templet: '<div>{{d.regDates==""?"-":d.regDates}}</div>'},
            // {field: 'kkYbmc', title: '医保对应名称', align: 'center', minWidth: 180, templet: '<div>{{d.kkYbmc==""?"-":d.kkYbmc}}</div>'},
            // {field: 'medInsCode', title: '医保项目代码', align: 'center', minWidth: 180, templet: '<div>{{d.medInsCode==""?"-":d.medInsCode}}</div>'},
            // {field: 'medInsName', title: '医保项目名称', align: 'center', minWidth: 180, templet: '<div>{{d.medInsName==""?"-":d.medInsName}}</div>'},
            // {field: 'yblb', title: '医保类型', align: 'center', minWidth: 180, templet: '<div>{{d.yblb==""?"-":d.yblb}}</div>'},
            // {field: 'purTaxP', title: '含税进价', align: 'center', minWidth: 180, templet: '<div>{{d.purTaxP==""?"-":d.purTaxP}}</div>'},
            // {field: 'inTaxRate', title: '进项税率', align: 'center', minWidth: 180, templet: '<div>{{d.inTaxRate==""?"-":d.inTaxRate}}</div>'},
            // {field: 'purP', title: '标准进价', align: 'center', minWidth: 180, templet: '<div>{{d.purP==""?"-":d.purP}}</div>'},
            // {field: 'biddPrice', title: '中标价', align: 'center', minWidth: 180, templet: '<div>{{d.biddPrice==""?"-":d.biddPrice}}</div>'},
            // {field: 'saleTaxP', title: '含税售价', align: 'center', minWidth: 180, templet: '<div>{{d.saleTaxP==""?"-":d.saleTaxP}}</div>'},
            // {field: 'outTaxRate', title: '销项税率', align: 'center', minWidth: 180, templet: '<div>{{d.outTaxRate==""?"-":d.outTaxRate}}</div>'},
            // {field: 'saleP', title: '标准售价', align: 'center', minWidth: 180, templet: '<div>{{d.saleP==""?"-":d.saleP}}</div>'},
            
            // {field: 'maxRetailP', title: '最高零售价', align: 'center', minWidth: 180, templet: '<div>{{d.maxRetailP==""?"-":d.maxRetailP}}</div>'},
            // {field: 'memPrice', title: '会员价', align: 'center', minWidth: 180, templet: '<div>{{d.memPrice==""?"-":d.memPrice}}</div>'},
            // {field: 'creatorName', title: '录入人', align: 'center', minWidth: 180, templet: '<div>{{d.creatorName==""?"-":d.creatorName}}</div>'},
            // {field: 'lastStfName', title: '修改人', align: 'center', minWidth: 180, templet: '<div>{{d.lastStfName==""?"-":d.lastStfName}}</div>'},
            // {field: 'lastRevTime', title: '修改时间', align: 'center', minWidth: 180, templet: '<div>{{d.lastRevTime==""?"-":d.lastRevTime}}</div>'},
            // {field: 'miDosageUnit', title: '单次用量单位', align: 'center', minWidth: 180, templet: '<div>{{d.miDosageUnit==""?"-":d.miDosageUnit}}</div>'},
            // {field: 'miDosage', title: '单次用量', align: 'center', minWidth: 180, templet: '<div>{{d.miDosage==""?"-":d.miDosage}}</div>'},
            // {field: 'miConversion', title: '医保换算率', align: 'center', minWidth: 180, templet: '<div>{{d.miConversion==""?"-":d.miConversion}}</div>'},
            // {field: 'miMinPackage', title: '最小包装', align: 'center', minWidth: 180, templet: '<div>{{d.miMinPackage==""?"-":d.miMinPackage}}</div>'},
            // {field: 'miDays', title: '天数', align: 'center', minWidth: 180, templet: '<div>{{d.miDays==""?"-":d.miDays}}</div>'},
            // {field: 'miHowCode', title: '医保用法', align: 'center', minWidth: 180, templet: '<div>{{d.miHowCode==""?"-":d.miHowCode}}</div>'},
            // {field: 'miDose', title: '医保剂型', align: 'center', minWidth: 180, templet: '<div>{{d.miDose==""?"-":d.miDose}}</div>'},
            // {field: 'lastPullDate', title: '自动拉取购药记录的最后日期(不包含)', align: 'center', minWidth: 180, templet: '<div>{{d.lastPullDate==""?"-":d.lastPullDate}}</div>'},
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
        elem: '#erpGoodsInfoTable',
        page: {
            elem: 'pageDiv',
            limit: 10,
            limits: [10, 20, 30, 40, 50]
        }
    });
    getDicObj("", tableIns, urls, "searchId");

    //监听工具条
    table.on('tool(erpGoodsInfoFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data;
        pkId = data.goodsCode;
        deptId = data.goodsName;
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        if (layEvent === 'detail') { //查看
            parent.deptTreeCallBack1(data.bzgg, data.goodsName,data.goodsSpec,data.retailP,data.orgId,data.goodsId);
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        } else if (layEvent === 'edit') { //编辑
            pageOperation = 2;
            xadmin.open('编辑', 'erpGoodsInfomodel.html', null, null, null);
        } else if (layEvent === 'del') { //删除
            var pkIds = [data.id];
            layer.confirm('您确定要删除吗？', {icon: 3, title: '确认'}, function () {
                $.ajax({
                    type: 'POST',
                    url: 'back/erpGoodsInfo/delBatchByIds',
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
    table.on('checkbox(erpGoodsInfoFilter)', function (obj) {
        //obj.type == "all"//全选
        //table.cache.erpGoodsInfoTable;//当前数据表格的id
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
    form.on('submit(erpGoodsInfoAdd)', function (obj) {
        pageOperation = 1, pkId = "";
        xadmin.open('添加', 'erpGoodsInfomodel.html', null, null, null);
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

    //批量删除
    form.on('submit(batchDel)', function (obj) {
        var checkStatus = table.checkStatus('erpGoodsInfoFilter');
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
                url: 'back/erpGoodsInfo/delBatchByIds',
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