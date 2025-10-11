var $;
layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'laypage'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = parent.pageOperation === 1 ? "user/add" : "user/modify";

    if (parent.pageOperation === 1 || parent.pageOperation === 2) {
        //日期
        laydate.render({
            elem: '#date'
        });

        form.on('submit(addUser)', function (data) {
            var role = [];
            $('input[name="role"]:checked').each(function (index, element) {
                role[index] = $(this).val();
            });
            data.field.role = role;

            var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
            $.ajax({
                type: 'POST',
                url: submitUrl,
                data: JSON.stringify(data.field),
                success: function (data) {
                    if (data.code === 200) {
                        top.layer.close(index);
                        if (parent.pageOperation === 1) {
                            layer.msg('用户添加成功', {icon: 1});
                            layer.closeAll("iframe");
                            //刷新父页面
                            parent.tableIns.reload({page: {curr: 1}});
                        } else if (parent.pageOperation === 2) {
                            setTimeout(function () {
                                top.layer.msg("用户修改成功！", {icon: 1});
                                layer.closeAll("iframe");
                                //刷新父页面
                                parent.tableIns.reload({});
                            }, 500);
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
        $(".deptName").click(function () {
            layui.layer.open({
                type: 2,
                title: '选择部门',
                shadeClose: true,
                shade: 0.5,
                area: ['320px', '70%'],
                content: '/page/dept/deptTree.html' //iframe的url
            });
        });

    }

    if (parent.pageOperation === 0 || parent.pageOperation === 2) {
        // 页面赋值
        $.ajax({
            type: "GET",
            url: "user/query/" + parent.userId,
            contentType: "application/json",
            success: function (data) {
                if (data.code === 200) {
                    var rest = data.data;
                    for (var i in rest) {//循环实体
                        if ($("." + i).attr("type") === "text" || $("." + i).attr("type") === "hidden") {  //文本框赋值
                            if ($("." + i).attr("name") === "birthDay") {
                                rest[i] = rest[i].substring(0, 10);
                            }
                            $("." + i).val(rest[i]);
                            if (parent.pageOperation === 0) {
                                $("." + i).prop("placeholder", "");
                            }
                        } else if ($("." + i).attr("type") === "checkbox") {//复选框改变状态
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
            }
        });
        //加载部门角色
        if (parent.deptId !== "") {
            loadDeptRoles(parent.deptId);
            $.ajax({
                type: "GET",
                url: "user/queryUserRoles/" + parent.userId,
                success: function (data) {
                    if (data.code == 200) {
                        var ret = data.data;
                        $.each(ret, function (idx, obj) {
                            $("input[name='role'][value='" + obj.roleId + "']").attr("checked", true);
                            form.render('checkbox');
                        });
                    } else {
                        top.layer.msg(data.message, {icon: 2});
                    }
                },
                contentType: "application/json"
            });
        }
    }

    if (parent.pageOperation === 0) {
        $(".layui-form input").prop("readonly", true);
        $(".sex").prop("disabled", "disabled");
        $(".enable").prop("disabled", "disabled");
        $(".role").prop("disabled", "disabled");
        $('.layui-form button').hide();
    }

    // 选择部门树页面选中后回调函数
    deptTreeCallBack = function (deptId, deptName) {
        $("#deptId").val(deptId);
        $("#deptName").val(deptName);
        loadDeptRoles(deptId);
    };

    function loadDeptRoles(deptId) {
        // 查询角色
        $.ajax({
            type: 'GET',
            url: 'role/queryRoles/' + deptId,
            async: false,
            success: function (data) {
                if (data.code == 200) {
                    if (data.data !== null) {
                        var html = '<div class="layui-inline"><label class="layui-form-label">角色</label>';
                        html += '<div class="layui-input-inline">';
                        $.each(data.data, function (index, obj) {
                            html += '<input type="checkbox" class="role" name="role" value="' + obj.id + '" lay-skin="primary" title="' + obj.roleName + '">';
                        });
                        html += '</div></div>';
                        $("#roleDiv").html(html);
                        form.render('checkbox');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    }

});

