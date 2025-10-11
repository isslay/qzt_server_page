var $;
layui.config({
    base: "../../../js/"
}).use(['base', 'form', 'layer', 'jquery', 'laydate', 'laypage'], function () {
    var base = layui.base,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        submitUrl = parent.pageOperation === 1 ? "back/qztGoodsBanner/add" : "back/qztGoodsBanner/modify";

    if (parent.pageOperation === 1 || parent.pageOperation === 2) {
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
            data.field.busId = parent.busId;
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
            url: "back/qztGoodsBanner/query/" + parent.pkId,
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
        });
    }

    if (parent.pageOperation === 0) {
        $(".layui-form input").prop("readonly", true);
        $(".sex").prop("disabled", "disabled");
        $(".enable").prop("disabled", "disabled");
        $(".role").prop("disabled", "disabled");
        $('.layui-form button').hide();
    }

    var uploader1 = Qiniu.uploader({
        runtimes: 'html5,flash,html4', // 上传模式，依次退化
        browse_button: 'pickfiles1', // 上传选择的点选按钮，必需
        uptoken_func: function () { // 在需要获取uptoken时，该方法会被调用
            var res;
            $.ajax({
                cache: false,
                type: "GET",
                url: "back/qiniuUploading/gettoken",
                dataType: "text",
                async: false,
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                },
                success: function (data) {
                    res = data;
                }
            });
            return res;
        },
        get_new_uptoken: false, // 设置上传文件的时候是否每次都重新获取新的uptoken
        unique_names: true, // 默认false，key为文件名。若开启该选项，JS-SDK会为每个文件自动生成key（文件名）
        domain: 'dgdyf', // bucket域名，下载资源时用到，必需
        container: 'container1', // 上传区域DOM ID，默认是browser_button的父元素
        max_file_size: '8mb', // 最大文件体积限制
        max_retries: 1, // 上传失败最大重试次数
        dragdrop: false, // 开启可拖曳上传
        chunk_size: '4mb', // 分块上传时，每块的体积
        auto_start: true, // 选择文件后自动上传，若关闭需要自己绑定事件触发上传
        init: {
            'FilesAdded': function (up, files) {
                plupload.each(files, function (file) {
                    // 文件添加进队列后，处理相关的事情
                });
            },
            'BeforeUpload': function (up, file) {
                // 每个文件上传前，处理相关的事情
            },
            'UploadProgress': function (up, file) {
                // 每个文件上传时，处理相关的事情
            },
            'FileUploaded': function (up, file, info) {
                // 每个文件上传成功后，处理相关的事情
                // 其中info.response是文件上传成功后，服务端返回的json，形式如：
                var res = JSON.parse(info.response);
                var s = res.key;
                var pic1 = "http://dgdqn.drugmirror.com/" + s;
                $("#bannerUrl1").attr("src", pic1);
                $("#bannerUrl").val(pic1);
            },
            'Error': function (up, err, errTip) {
                //上传出错时，处理相关的事情
            },
            'UploadComplete': function () {
                //队列文件处理完毕后，处理相关的事情
            },
            'Key': function (up, file) {
                // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
                // 该配置必须要在unique_names: false，save_key: false时才生效
                var key = "";
                return key
            }
        }
    });
});

