var $;
layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laypage'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;

    //表单验证
    form.verify({
        auditRemark: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ("" == value) {
                return '请输入驳回理由';
            }
        }
    });

    form.on('submit(btn_submit)', function (data) {
        var index = top.layer.msg('驳回中，请稍候', {icon: 16, time: false, shade: 0.8});
        var pkIds = [];
        if (parent.rejectionType == "1") {//为1则批量驳回
            for (var i = 0; i < parent.pkId.length; i++) {
                pkIds[i] = parent.pkId[i].id;
            }
        } else {
            pkIds.push(parent.pkId);
        }
        parent.rejection(pkIds, data.field.auditRemark);
        layer.closeAll("iframe");
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });
});

