var $;
layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'laypage'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = parent.pageOperation === 1 ? "/back/appVersion/add" : "/back/appVersion/modify";

    if (parent.pageOperation === 1 || parent.pageOperation === 2) {
        //日期
        laydate.render({
            elem: '#date'
        });
        form.on('submit(addData)', function (data) {
            if (typeof(data.field.enable) === "undefined" || data.field.enable === 'undefined') {
                data.field.enable = 0;
            }
            var role = [];
            $('input[name="role"]:checked').each(function (index, element) {
                role[index] = $(this).val();
            });
            data.field.role = role;
            data.field.id = parent.pkId;
            if (data.field.isForcedUpdate == null || data.field.isForcedUpdate == '') {
                data.field.isForcedUpdate = "N";//否
            } else {
                data.field.isForcedUpdate = "Y";//是
            }
            var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
            $.ajax({
                type: 'POST',
                url: submitUrl,
                data: JSON.stringify(data.field),
                success: function (data) {
                    if (data.code === 200) {
                        top.layer.close(index);
                        if (parent.pageOperation === 1) {
                            layer.msg('添加成功', {icon: 1});
                            layer.closeAll("iframe");
                            parent.tableIns.reload({
                                page: {
                                    curr: 1 //重新从第 1 页开始
                                }
                            });
                        } else if (parent.pageOperation === 2) {
                            top.layer.msg("修改成功！", {icon: 1});
                            layer.closeAll("iframe");
                            //刷新父页面
                            parent.tableIns.reload({});
                        }
                    } else {
                        top.layer.close(index);
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
            //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            return false;
        });
    }

    if (parent.pageOperation === 0 || parent.pageOperation === 2) {
        // 页面赋值
        $.ajax({
            type: "GET",
            url: "/back/appVersion/query/" + parent.pkId,
            success: function (data) {
                if (data.code === 200) {
                    var rest = data.data;
                    //循环实体
                    for (var i in rest) {
                        //文本框赋值
                        if ($("." + i).attr("type") === "text" || $("." + i).attr("type") === "hidden") {
                            if ($("." + i).attr("name") === "birthDay") {
                                rest[i] = rest[i].substring(0, 10);
                            }
                            $("." + i).val(rest[i]);
                            if (parent.pageOperation === 0) {
                                $("." + i).prop("placeholder", "");
                            }

                        } else if ($("." + i).attr("type") === "checkbox") {//复选框改变状态
                            if (rest[i] == "Y") {
                                $("." + i).attr("checked", 'checked');
                            }
                        } else if ($("." + i).attr("name") === 'sourceMode') {
                            var numbers = $("#sourceMode").find("option"); //获取select下拉框的所有值
                            for (var j = 1; j < numbers.length; j++) {
                                if ($(numbers[j]).val() == rest["sourceMode"]) {
                                    $(numbers[j]).attr("selected", "selected");
                                }
                            }
                            renderForm();
                        }
                    }
                } else {
                    top.layer.msg(data.message, {icon: 2});
                }
            },
            contentType: "application/json"
        });
    }

    if (parent.pageOperation === 0) {
        $(".layui-form input").prop("readonly", true);
        $("#sourceMode").attr("disabled", "disabled");
        $("#isForcedUpdate").attr("disabled", "disabled");
        $(".sex").prop("disabled", "disabled");
        $(".enable").prop("disabled", "disabled");
        $(".role").prop("disabled", "disabled");
        $('.layui-form button').hide();
    }

    function renderForm() {
        layui.use('form', function () {
            var form = layui.form;//高版本建议把括号去掉，有的低版本，需要加()
            form.render();
        });
    }
});

