var $;
layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'laypage'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = parent.pageOperation === 1 ? "back/qztOrderCommen/add" : "back/qztOrderCommen/modify";

    if (parent.pageOperation === 1 || parent.pageOperation === 2) {
        //日期
        laydate.render({
            elem: '#date'
        });

        form.on('submit(btn_submit)', function (data) {
            if (typeof(data.field.enable) === "undefined" || data.field.enable === 'undefined') {
                data.field.enable = 0;
            }
            var role = [];
            $('input[name="role"]:checked').each(function (index, element) {
                role[index] = $(this).val();
            });
            data.field.role = role;
            data.field.id = parent.pkId;
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
                            parent.tableIns.reload({page: {curr: 1}});//加载第一页数据
                        } else if (parent.pageOperation === 2) {
                            top.layer.msg("修改成功！", {icon: 1});
                            layer.closeAll("iframe");
                            //刷新父页面
                            parent.tableIns.reload();
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
            url: "back/qztOrderCommen/query/" + parent.pkId,
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
                            //复选框改变状态
                        } else if ($("." + i).attr("type") === "checkbox") {
                            if ($("." + i).attr("name") === "enable" && rest[i] === 0) {
                                $("." + i).removeAttr("checked");
                                form.render('checkbox');
                            }
                        } else if ($("." + i).attr("type") === "radio") {
                            if ($("." + i).attr("name") === "sex") {
                                $("input[name='sex'][value='" + rest[i] + "']").attr("checked", true);
                                form.render('radio');
                            }
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
        $(".sex").prop("disabled", "disabled");
        $(".enable").prop("disabled", "disabled");
        $(".role").prop("disabled", "disabled");
        $('.layui-form button').hide();
    }
});

