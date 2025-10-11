var $;
layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'laypage'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = "back/qztApplyBusorder/goodsOrderShipments";

    //快递code
    $.ajax({
        type: "POST",
        url: "webapi/sysDic?dicType=EXPRESSAGE_CODE",
        dataType: 'JSON',
        async: false,
        success: function (data) {
            var codes = data.data.EXPRESSAGE_CODE;
            var html = '<option value="">请选择快递公司</option>';
            for (var i = 0; i < codes.length; i++) {
                var list = codes[i];
                html += '<option value="' + list.code + '">' + list.codeText + '</option>';
            }
            $("#courierCode").append(html)
            form.render();
        }
    });

    form.verify({
        courierCode: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ("" == value) {
                return '请选择快递公司';
            }
        },
        courierNo: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ("" == value) {
                return '请输入快递单号';
            }
        }
    });

    form.on('submit(btn_submit)', function (data) {
        data.field.id = parent.pkId;
        data.field.courierCompany = $("#courierCode").find("option:selected").text();
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        $.ajax({
            type: 'POST',
            url: submitUrl,
            data: JSON.stringify(data.field),
            success: function (data) {
                if (data.code === 200) {
                    top.layer.close(index);
                    top.layer.msg("发货成功！", {icon: 1});
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.tableIns.reload();
                } else {
                    top.layer.close(index);
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });

});

