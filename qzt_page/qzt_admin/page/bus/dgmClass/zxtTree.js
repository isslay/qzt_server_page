layui.config({
    base: "../../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'tree'], function () {
    var base = layui.base,
        form = layui.form,
        tree = layui.tree,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    // var deptId = window.parent.document.getElementById("deptId").value;
    var treeUrl = "/back/dgmZxtClass/classList";
    // 新增、编辑跳转则加载部门树
    $.ajax({
        type: "GET",
        url: treeUrl,
        async: false,
        success: function (data) {
            if (data.code == "200") {
                // layui.tree({
                //     elem: '#demo', //传入元素选择器
                //     nodes: data.data,
                //     click: function (node) {
                //         /*var ifrc = window.parent.frames[0];
                //          var winc = ifrc.window || ifrc.contentWindow;
                //          winc.document.getElementById("parentId").value = node.id;
                //          winc.document.getElementById("parentName").value = node.name;*/
                //         parent.deptTreeCallBack(node.id, node.name);
                //         var index = parent.layer.getFrameIndex(window.name);
                //         parent.layer.close(index);
                //     }
                // });

                //渲染
                inst1 = tree.render({
                    data: data.data,
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
                        parent.deptTreeCallBack(ret.id, ret.title);
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(index);
                    }
                });
            } else {
                layer.msg("分类加载失败！", {icon: 2});
            }
        }
    });
})