layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', "treecheck"], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        submitUrl = "";

    var dicId = parent.dicId;//父级id
    var dicType = parent.dicType;//父级字典类型
    var dicName = parent.dicName;//父级字典名称
    $("#dicType").val(dicType);
    $("#dicName").val(dicName);
    $("#parentId").val(dicId);
    $("#dicType").prop("disabled", true);
    $("#dicName").prop("disabled", true);

    // 获取父页面的pageOperation，判断是查看、添加、修改
    if (parent.pageOperation === 1) { // 添加
        submitUrl = "back/sysDic/addSublist";
    } else if (parent.pageOperation === 2) { // 修改
        submitUrl = "back/sysDic/modifySublist";
    }

    if (parent.pageOperation === 2) {
        $("#id").val(parent.checkedDicId);
        // 查询字典
        $.ajax({
            type: 'GET',
            url: 'back/sysDic/querySublist/' + parent.checkedDicId,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        $("#code").val(data.data.code);
                        $("#codeText").val(data.data.codeText);
                        $("#sortNo").val(data.data.sortNo);
                        $("#code").val(data.data.code);
                        $("#remark").val(data.data.remark);
                        $(':radio[name="enable"][value="' + data.data.enable + '"]').attr("checked", "checked");
                        form.render('radio');
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    }

    //表单验证
    form.verify({
        code: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ("" == value) {
                return '请输入关键字';
            }
        },
        codeText: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ("" == value) {
                return '请输入关键字名称';
            }
        },
        sortNo: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ("" == value) {
                return '请输入排序';
            }
        }
    });

    // 监听submit
    form.on('submit(addFilter)', function (data) {
        var loadingIndex = base.loading(layer);
        $.ajax({
            type: 'POST',
            url: submitUrl,
            data: JSON.stringify(data.field),
            success: function (data) {
                layer.close(loadingIndex);
                if (data.code === 200) {
                    if (parent.pageOperation === 1) {
                        // 重置表单
                        // $("#dicForm")[0].reset();
                        top.layer.msg('字典添加成功', {icon: 1});
                    } else {
                        top.layer.msg('字典修改成功', {icon: 1});
                    }
                    layer.closeAll("iframe");
                    // 刷新父页面
                    parent.location.reload();
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });

        // 阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    });
});

