layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', "treecheck", "tree", "util"], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        util = layui.util,
        treel = layui.tree,
        treeUrl = "menu/funcTree",
        treeData = "",
        treeCheckBoxAllDisable = false,
        submitUrl = "role/add";

    // 获取父页面的pageOperation，判断是查看、添加、修改
    if (parent.pageOperation === 1) { // 添加角色
    } else if (parent.pageOperation === 2) { // 修改角色
        treeUrl = "menu/roleFuncTree";
        treeData = JSON.stringify(parent.checkedRoleId);
        submitUrl = "role/modify";
    } else { // 查看角色
        treeUrl = "menu/roleFuncTree";
        treeData = JSON.stringify(parent.checkedRoleId);
        treeCheckBoxAllDisable = true;

        $(".layui-form input").prop("disabled", true);
        $('.layui-form button').hide();
    }

 /*   funcTree();
    function funcTree() {
        $.ajax({
            async: true,//请求方式 默认true异步，false同步
            type: "POST",
            url: "menu/funcTree",
            dataType: "JSON",
            contentType: "application/json",
            success: function (data) {
                if (data.code == "200") {
                    var ret = data.data;
                } else {
                    top.layer.msg(data.message, {icon: 2});
                }
            },
            error: function (data) {

            }
        });
    }*/

    // 初始化功能权限tree
     var tree = layui.treecheck({
         elem: 'funcAuthTree', // 放xtree的容器，id，不要带#号（必填）
         form: form, // layui form对象 （必填）
         url: treeUrl, // 服务端地址（必填）
         data: treeData, // 参数
         isopen: true, // 初次加载时全部展开，默认true
         color: "#000", // 图标颜色
         icon: { // 图标样式 （必填，不填会出点问题）
             open: "&#xe7a0;", // 节点打开的图标
             close: "&#xe622;", // 节点关闭的图标
             end: "&#xe621;" // 末尾节点的图标
         },
         allDisable: treeCheckBoxAllDisable // 全部checkbox是否不可用
     });


    /*var datas = [{
        title: '一级1'
        , id: 1
        , field: 'name1'
        , checked: true
        , spread: true
        , children: [{
            title: '二级1-1 可允许跳转'
            , id: 3
            , field: 'name11'
            , href: 'https://www.layui.com/'
            , children: [{
                title: '三级1-1-3'
                , id: 23
                , field: ''
                , children: [{
                    title: '四级1-1-3-1'
                    , id: 24
                    , field: ''
                    , children: [{
                        title: '五级1-1-3-1-1'
                        , id: 30
                        , field: ''
                    }, {
                        title: '五级1-1-3-1-2'
                        , id: 31
                        , field: ''
                    }]
                }]
            }, {
                title: '三级1-1-1'
                , id: 7
                , field: ''
                , children: [{
                    title: '四级1-1-1-1 可允许跳转'
                    , id: 15
                    , field: ''
                    , href: 'https://www.layui.com/doc/'
                }]
            }, {
                title: '三级1-1-2'
                , id: 8
                , field: ''
                , children: [{
                    title: '四级1-1-2-1'
                    , id: 32
                    , field: ''
                }]
            }]
        }, {
            title: '二级1-2'
            , id: 4
            , spread: true
            , children: [{
                title: '三级1-2-1'
                , id: 9
                , field: ''
                , disabled: true
            }, {
                title: '三级1-2-2'
                , id: 10
                , field: ''
            }]
        }, {
            title: '二级1-3'
            , id: 20
            , field: ''
            , children: [{
                title: '三级1-3-1'
                , id: 21
                , field: ''
            }, {
                title: '三级1-3-2'
                , id: 22
                , field: ''
            }]
        }]
    }, {
        title: '一级2'
        , id: 2
        , field: ''
        , spread: true
        , children: [{
            title: '二级2-1'
            , id: 5
            , field: ''
            , spread: true
            , children: [{
                title: '三级2-1-1'
                , id: 11
                , field: ''
            }, {
                title: '三级2-1-2'
                , id: 12
                , field: ''
            }]
        }, {
            title: '二级2-2'
            , id: 6
            , field: ''
            , children: [{
                title: '三级2-2-1'
                , id: 13
                , field: ''
            }, {
                title: '三级2-2-2'
                , id: 14
                , field: ''
                , disabled: true
            }]
        }]
    }, {
        title: '一级3'
        , id: 16
        , field: ''
        , children: [{
            title: '二级3-1'
            , id: 17
            , field: ''
            , fixed: true
            , children: [{
                title: '三级3-1-1'
                , id: 18
                , field: ''
            }, {
                title: '三级3-1-2'
                , id: 19
                , field: ''
            }]
        }, {
            title: '二级3-2'
            , id: 27
            , field: ''
            , children: [{
                title: '三级3-2-1'
                , id: 28
                , field: ''
            }, {
                title: '三级3-2-2'
                , id: 29
                , field: ''
            }]
        }]
    }];*/
    //渲染
    /*var inst1 = treel.render({
        elem: '#test1', //绑定元素
        data: datas,
        // edit: ['add', 'update', 'del'],//是否开启节点的操作图标。默认 false。
        showCheckbox: true,//是否显示复选框，默认 false
        // accordion: true,//是否开启手风琴模式，默认 false
        onlyIconControl: true,//是否仅允许节点左侧图标控制展开收缩。默认 false
        text: {//自定义各类默认文本，目前支持以下设定：
            defaultNodeName: '未命名', //节点默认名称
            none: '无数据' //数据为空时的提示文本
        }
    });*/

    if (parent.pageOperation === 2 || parent.pageOperation === 0) {
        $("#id").val(parent.checkedRoleId);
        // 查询角色
        $.ajax({
            type: 'POST',
            url: 'role/query',
            data: JSON.stringify(parent.checkedRoleId),
            success: function (data) {
                if (data.code === 200) {
                    if (data.data !== null) {
                        $("#roleName").val(data.data.roleName);
                        $("#deptId").val(data.data.deptId);
                        $("#deptName").val(data.data.deptName);
                        $("#remark").val(data.data.remark);
                    }
                } else {
                    layer.msg(data.message, {icon: 2});
                }
            }
        });
    }

    // 监听submit
    form.on('submit(addFilter)', function (data) {
        var loadingIndex = base.loading(layer);
        var menuIdArray = tree.GetAllCheckedValue();
        data.field["menuIdList"] = menuIdArray;
        $.ajax({
            type: 'POST',
            url: submitUrl,
            data: JSON.stringify(data.field),
            success: function (data) {
                layer.close(loadingIndex);
                if (data.code === 200) {
                    if (parent.pageOperation === 1) {
                        // 重置表单
                        // $("#roleForm")[0].reset();
                        top.layer.msg('角色添加成功', {icon: 1});
                    } else {
                        top.layer.msg('角色修改成功', {icon: 1});
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

    // 监听部门文本框单击事件
    $("#deptName").click(function () {
        // 不直接使用layer.open而使用layui.layer.open，是因为layer.open实际是调用父窗口的layer对象
        layui.layer.open({
            type: 2,
            title: '选择部门',
            shadeClose: true,
            shade: 0.5,
            area: ['320px', '70%'],
            content: '/page/dept/deptTree.html' //iframe的url
        });
    });

    // 选择部门树页面选中后回调函数
    deptTreeCallBack = function (deptId, deptName) {
        $("#deptId").val(deptId);
        $("#deptName").val(deptName);
    }
});

