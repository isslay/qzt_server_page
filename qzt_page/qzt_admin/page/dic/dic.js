layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', "treecheck"], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        submitUrl = "";

    // 获取父页面的pageOperation，判断是查看、添加、修改
    if (parent.pageOperation === 1) { // 添加
        submitUrl = "back/sysDic/add";
    } else if (parent.pageOperation === 2) { // 修改
        submitUrl = "back/sysDic/modify";
    }

    if (parent.pageOperation === 2) {//编辑
        $("#id").val(parent.checkedDicId);
        // 查询字典
        $.ajax({
            type: 'GET',
            url: 'back/sysDic/queryz/' + parent.checkedDicId,
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        $("#dicType").val(data.data.dicType);
                        $("#dicName").val(data.data.dicName);
                        $(':radio[name="enable"][value="' + data.data.enable + '"]').attr("checked", "checked");
                        form.render('radio');
                        $("#remark").val(data.data.remark);
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    }

    //表单验证
    form.verify({
        dicType: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ("" == value) {
                return '请输入字典类型';
            }
        },
        dicName: function (value, item) { //value：表单的值、item：表单的DOM对象
            if ("" == value) {
                return '请输入字典名称';
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

