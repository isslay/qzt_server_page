layui.config({
    base: "../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'tree'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        tree = layui.tree,
        $ = layui.jquery;

    var menuType = parent.menuType;

    //渲染
    inst1 = tree.render({
        data: parent.treeData,
        elem: '#demo',  //绑定元素
        showCheckbox: false,//是否显示复选框
        edit: false,//是否开启节点的操作图标['add', 'update', 'del']
        accordion: true,//是否开启手风琴模式，默认 false
        onlyIconControl: true,//是否仅允许节点左侧图标控制展开收缩。默认 false
        click: function (obj) {
            /*console.log(obj.data); //得到当前点击的节点数据
            console.log(obj.state); //得到当前节点的展开状态：open、close、normal
            console.log(obj.elem); //得到当前节点元素
            console.log(obj.data.children); //当前节点下是否有子节点*/
            var ret = obj.data;
            if (menuType == 2 && ret.type != 1) {//按钮只能选择菜单
                top.layer.msg("请选择菜单！", {icon: 2});
                return;
            } else if (menuType == 1 && (ret.type != 0 || ret.type != 0)) {
                top.layer.msg("请选择目录！", {icon: 2});
                return;
            } else {
                parent.menuTreeCallBack(ret.id, ret.title);
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            }
        }
    });

    /*data: [{

        title: '江西', //一级菜单
        children: [{
            title: '南昌', //节点标题
            id: '1', //节点唯一索引值
            parentId: "0",
            field: 'field', //节点字段名	String	一般对应表字段名
            disabled: false, //节点是否为禁用状态。默认 false
            checked: false, //节点是否初始为选中状态（如果开启复选框的话），默认 false
            spread: false, //节点是否初始展开，默认 false
            href: '', //点击节点弹出新窗口对应的 url。需开启 isJump 参数
            children: [{
                title: '南昌-2', //节点标题
                id: '1', //节点唯一索引值
                field: 'field', //节点字段名	String	一般对应表字段名
                disabled: false, //节点是否为禁用状态。默认 false
                checked: false, //节点是否初始为选中状态（如果开启复选框的话），默认 false
                spread: false, //节点是否初始展开，默认 false
                href: '', //点击节点弹出新窗口对应的 url。需开启 isJump 参数
                children: [], //子节点。支持设定选项同父节点
            }], //子节点。支持设定选项同父节点
        }]
    },*/
});
