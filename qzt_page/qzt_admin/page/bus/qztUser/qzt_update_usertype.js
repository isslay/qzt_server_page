var $;
layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'laypage'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = "back/qztUser/updateUserType";
    userId = parent.pkId;
    userType = parent.userType;
    isDemotion = parent.isDemotion;

    //服务站类型
    $.ajax({
        type: "POST",
        url: "webapi/sysDic?dicType=SERVICE_TYPE",
        dataType: 'JSON',
        async: false,
        success: function (data) {
            var codes = data.data.SERVICE_TYPE;
            var html = '';
            for (var i = 0; i < codes.length; i++) {
                var list = codes[i];
                html += '<option value="' + list.code + '">' + list.codeText + '</option>';
            }
            $("#userType").append(html)
            form.render();
        }
    });

    $("#userType").val(userType)
    $(':radio[name="isDemotion"][value="' +isDemotion + '"]').attr("checked", "checked");
    form.render();

    form.verify({
        userType: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ("" == value) {
                return '请选择服务站类型';
            }
        }
    });


    form.on('submit(btn_submit)', function (data) {
        data.field.userId = userId;
        data.field.userTypeText = $("#userType").find("option:selected").text();
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        $.ajax({
            type: 'POST',
            url: submitUrl,
            data: JSON.stringify(data.field),
            success: function (data) {
                if (data.code === 200) {
                    top.layer.close(index);
                    top.layer.msg("修改成功！", {icon: 1});
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

