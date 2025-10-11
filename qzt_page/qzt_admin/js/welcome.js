//欢迎页面js
layui.config({
    base: "../js/"
}).use(['base', 'form', 'element', 'layer', 'jquery'], function () {
    var base = layui.base,
        form = layui.form,
        layer = layui.layer,
        element = layui.element,
        $ = layui.jquery;

    //渲染用户名和头像
    var currentUser = layui.data("JWW_UMP").CUURENT_USER;
    $("#userinfo").html('欢迎：<span class="x-red">' + currentUser.userName + '</span>！当前时间: ' + new Date().format("yyyy-MM-dd hh:mm:ss"));
});