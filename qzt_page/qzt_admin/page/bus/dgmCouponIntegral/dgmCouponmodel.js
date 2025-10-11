var $;
layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'laypage'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = parent.pageOperation === 1 ? "back/dgmCouponIntegral/add" : "back/dgmCouponIntegral/modify";

    var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
    var reg1 = /(^[1-9]\d*|0$)/;

    //日期1
    laydate.render({
        elem: '#couponBegin',
        format: 'yyyy-MM-dd'
    });
    //日期2
    laydate.render({
        elem: '#couponEnd',
        format: 'yyyy-MM-dd'
    });

    if (parent.pageOperation === 1 || parent.pageOperation === 2) {

        //选择
        form.on('select(couponType)', function(data){
            if(data.value ==='3'){
                $("#showTargetId").css('display','block');
                $("#showGoodsId").css('display','block');
                
                $("#beginDate").css('display','none');
                $("#endDate").css('display','none');
            }else if(data.value ==='4'){
                $("#beginDate").css('display','block');
                $("#endDate").css('display','block');

                $("#showTargetId").css('display','none');
                $("#showGoodsId").css('display','none');
            }else{
                $("#showTargetId").css('display','none');
                $("#showGoodsId").css('display','none');
                $("#beginDate").css('display','none');
                $("#endDate").css('display','none');
            }

        });

        //表单验证
        form.verify({
            couponMoney: function (value, item) { //value：表单的值、item：表单的DOM对象
                if ("" == value) {
                    return '请填写优惠券金额';
                } else if (!reg.test(value)) {
                    return '优惠券金额设置无效';
                }
            },
            number: function (value, item) { //value：表单的值、item：表单的DOM对象
                if ("" == value) {
                    return '请填写所需积分';
                } else if (!reg1.test(value)) {
                    return '所需积分无效';
                }
            },
            targetMoney: function (value, item) { //value：表单的值、item：表单的DOM对象
                if ("" == value) {
                    return '请填写订单金额';
                } else if (!reg.test(value)) {
                    return '订单金额设置无效';
                }
            }
        });

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

            var money = data.field.couponMoney*100;
            data.field.couponMoney = money;
            var money1= data.field.targetMoney*100;
            data.field.targetMoney = money1;

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
            url: "back/dgmCouponIntegral/query/" + parent.pkId,
            success: function (data) {
                if (data.code === 200) {
                    var rest = data.data;
                    //循环实体
                    for (var i in rest) {
                        //文本框赋值
                        if ($("." + i).attr("type") === "text" || $("." + i).attr("type") === "hidden") {

                            if ($("." + i).attr("name") === "couponMoney") {
                                $("." + i).val(rest[i]/100);
                            }else if ($("." + i).attr("name") === "targetMoney") {
                                $("." + i).val(rest[i]/100);
                            }else{
                                $("." + i).val(rest[i]);
                            }

                            if ($("." + i).attr("name") === "birthDay") {
                                rest[i] = rest[i].substring(0, 10);
                            }
                            
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
                        } else if ($("." + i).attr("type") === "select") {
                            $("." + i).val(rest[i]);//设置选中的值  TypeID为HTML标签ID
                            layui.form.render("select");//layUI设置
                            if(rest[i]==3){
                                $("#showTargetId").css('display','block');
                                $("#showGoodsId").css('display','block');
                            }else if(rest[i]==4){
                                $("#beginDate").css('display','block');
                                $("#endDate").css('display','block');
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

