var $, tab, skyconsWeather;
layui.config({
    base: "js/"
}).use(['base', 'form', 'element', 'layer', 'jquery'], function () {
    var base = layui.base,
        form = layui.form,
        layer = layui.layer,
        element = layui.element;
    $ = layui.jquery;

    $(".copyright").html('Copyright ©2017-' + new Date().format("yyyy") + ' 吉林省大格新特药连锁有限公司 v1.0 All Rights Reserved');

    //渲染用户名和头像
    var currentUser = layui.data("JWW_UMP").CUURENT_USER;
    if (typeof (currentUser) === "undefined") {
        window.location.href = "/page/login/login.html";
    }
    $("#uaccount").html(currentUser.account + '<span class="layui-nav-more"></span>');//当前用户账号
    $("span[name='userName']").text(currentUser.userName);
    var userId = currentUser.id;
    getMenu("menu/tree/" + userId);// 获取菜单json地址
    //获取二级菜单数据
    function getMenu(url) {
        $.ajax({
            cache: false,
            type: "GET",
            url: url,
            dataType: "JSON",
            async: false,
            success: function (data) {
                data = data.data;
                $("#nav").append(navBar(data));
                //显示左侧菜单
                if ($("#nav").html() === '') {
                    // $("#nav").html(navBar(data)).height($(window).height() - 245);
                    // $("#nav").html(navBar(data));
                    element.init();  //初始化页面元素
                    /* $(window).resize(function () {
                         $(".navBar").height($(window).height() - 245);
                     })*/
                }
            }
        });
    }


    $.ajax({
        type: "GET",
        url: "index/permissions",
        success: function (data) {
            if (data.code === 200) {
                // 设置用户权限到浏览器本地sessionStorage中
                window.sessionStorage.setItem("JWW_UMP_USER_PERMISSIONS", data.data);
            }
        },
        contentType: "application/json"
    });

    //退出
    $(".signOut").click(function () {
        layer.confirm('确定退出系统吗？', {icon: 3, title: '确认'}, function (index) {
            var loadingIndex = base.loading(layer);
            $.ajax({
                type: 'POST',
                url: 'logout',
                success: function (data) {
                    layer.close(loadingIndex);
                    if (data.code === 200) {
                        layui.data('JWW_UMP', {
                            key: 'CUURENT_USER', remove: true
                        });
                        window.sessionStorage.removeItem("menu");
                        menu = [];
                        window.sessionStorage.removeItem("curmenu");
                        window.top.location.href = "/page/login/login.html";
                    } else {
                        layer.msg(data.message, {icon: 2});
                    }
                }
            });
            layer.close(index);
        });
    });
});
