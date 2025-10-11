var $;
layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'laypage'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = parent.pageOperation === 1 ? "back/klygAccount/add" : "back/klygAccount/modify";
    if (parent.pageOperation === 1 || parent.pageOperation === 2) {
        //日期
        laydate.render({
            elem: '#date'
        });

        form.on('submit(btn_submit)', function (data) {

            var userId = $(".userId").val();
            var addMoney = $(".addMoney").val();
            if(userId == ""){
                layer.msg("请先完成校验", {icon: 2});
                return false;
            }

            if(parseFloat(addMoney) < 0){
                layer.msg("金额不能为负数", {icon: 2});
                return false;
            }

            layer.confirm("确认给 [ "+ userId +" ] 充值吗?",{
                btn: ['确定','取消'] //按钮
            },function(){
                layer.closeAll('dialog');  //点击确定关闭提示框
                if (typeof(data.field.enable) === "undefined" || data.field.enable === 'undefined') {
                    data.field.enable = 0;
                }
                var role = [];
                $('input[name="role"]:checked').each(function (index, element) {
                    role[index] = $(this).val();
                });
                data.field.auditState='00';
                data.field.topUpMoney = data.field.topUpMoney*100;
                var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
                $.ajax({
                    type: 'POST',
                    url: 'back/qztRecharge/add',
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
            });
            //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            return false;
        });
    }

    if (parent.pageOperation === 0 || parent.pageOperation === 2) {
        // 页面赋值
        /*$.ajax({
            type: "GET",
            url: "back/klygAccount/query/" + parent.pkId,
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
        });*/
    }

    $(".btn_verify").click(function () {

            //var indexMsg = layer.msg('校验中，请稍候', {icon: 16, time: false, shade: 0.8});
            var pkIds;
            var userId = $(".uId").val();
            var mobile = $(".mobile").val();
            if(userId == "" && mobile == ""){
                layer.msg("请填写需要检验的信息", {icon: 2});
                return false;
            }
            if(mobile != ""){
                pkIds = mobile;
            }else{
                pkIds = userId;
            }
            $.ajax({
                type: 'Get',
                url: 'back/qztUser/query/'+pkIds,
                success: function (data) {
                    if (data.code == 200) {
                        //layer.close(indexMsg);
                        $(".userId").val(data.data.userId);
                        $(".uId").val(data.data.userId);
                        $(".mobile").val(data.data.mobile);
                    } else {
                        layer.msg("暂无此用户", {icon: 2});
                    }
                }
            });
        //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        return false;
    })

    if (parent.pageOperation === 0) {
        $(".layui-form input").prop("readonly", true);
        $(".sex").prop("disabled", "disabled");
        $(".enable").prop("disabled", "disabled");
        $(".role").prop("disabled", "disabled");
        $('.layui-form button').hide();
    }

    $(".addMoney").on('blur',function(obj){
        console.log(this.value);
        if( /^d*(?:.d{0,2})?$/.test(this.value)){
            layer.msg("请输入小数或整数", {icon: 2});
        }
    });
});

