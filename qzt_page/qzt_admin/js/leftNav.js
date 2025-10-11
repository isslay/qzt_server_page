function navBar(strData) {
    var data;
    if (typeof (strData) === "string" && strData != null && strData != "") {
        //alert(strData)
        console.log(strData)
        data = JSON.parse(strData); // 部分用户解析出来的是字符串，转换一下
    } else {
        data = strData;
    }
    var ulHtml = '';
    for (var i = 0; i < data.length; i++) {
        ulHtml += '<li>';
        var menuStair = data[i];
        ulHtml += spliceMenu(menuStair);
        var childrenSecondLevel = menuStair.children;
        if (childrenSecondLevel !== undefined && childrenSecondLevel.length > 0) {//无下级目录不显示
            ulHtml += '<ul class="sub-menu">';
            for (var j = 0; j < childrenSecondLevel.length; j++) {
                var menuSecondLevel = childrenSecondLevel[j];
                ulHtml += '<li>';
                ulHtml += spliceMenu(menuSecondLevel);
                var childrenThree = menuSecondLevel.children;
                if (childrenThree !== undefined && childrenThree.length > 0) {//无下级目录不显示
                    ulHtml += '<ul class="sub-menu">';
                    for (var k = 0; k < childrenThree.length; k++) {
                        ulHtml += '<li>';
                        ulHtml += spliceMenu(childrenThree[k]);
                        ulHtml += '</li>';
                    }
                    ulHtml += "</ul>";
                }
                ulHtml += '</li>';
            }
            ulHtml += "</ul>";
        }
        ulHtml += '</li>';

        /*if (data[i].spread) {
            ulHtml += '<li class="layui-nav-item layui-nav-itemed">';
        } else {
            ulHtml += '<li class="layui-nav-item">';
        }*/
        /*if (data[i].children !== undefined && data[i].children.length > 0) {
            ulHtml += '<a href="javascript:;">';
            if (data[i].icon != null && data[i].icon != "") {
                ulHtml += '<i class="iconfont left-nav-li">' + data[i].icon + '</i>';
            } else {
                ulHtml += '<i class="iconfont left-nav-li">&#xe6fa;</i>';
            }
            ulHtml += '<cite>' + data[i].name + '</cite>';
            ulHtml += '<i class="iconfont nav_right">&#xe697;</i></a>';
            ulHtml += '<ul class="sub-menu">';
            for (var j = 0; j < data[i].children.length; j++) {
                ulHtml += '<li>';
                ulHtml += '<a onclick="xadmin.add_tab(\'' + data[i].children[j].name + '\',\'' + data[i].children[j].href + '\')">';
                ulHtml += '<i class="iconfont">&#xe6a7;</i>';
                ulHtml += '<cite>' + data[i].children[j].name + '</cite></a></li>';
            }
            ulHtml += "</ul>";
        } else {*/
        /*if (data[i].target === "_blank") {
            ulHtml += '<a href="javascript:;" data-url="' + data[i].href + '" target="' + data[i].target + '">';
        } else {
            ulHtml += '<a href="javascript:;" data-url="' + data[i].href + '">';
        }
        if (data[i].icon !== undefined && data[i].icon !== '') {
            if (data[i].icon.indexOf("icon-") !== -1) {
                ulHtml += '<i class="iconfont ' + data[i].icon + '" data-icon="' + data[i].icon + '"></i>';
            } else {
                ulHtml += '<i class="layui-icon" data-icon="' + data[i].icon + '">' + data[i].icon + '</i>';
            }
        }
        ulHtml += '<cite>' + data[i].name + '</cite></a>';*/
        // }
        // ulHtml += '</li>';
    }
    return ulHtml;
}

/**
 * 拼接菜单
 * @param menuInfo
 * @returns {string}
 */
function spliceMenu(menuInfo) {
    var html = '';
    if (menuInfo.type == 0) {//目录
        html += '<a href="javascript:;">';
        html += '<i class="iconfont left-nav-li">' + menuInfo.icon + '</i>';
        html += '<cite>' + menuInfo.name + '</cite>';
        html += '<i class="iconfont nav_right">&#xe697;</i>';
    } else if (menuInfo.type == 1) {//菜单
        html += '<a onclick="xadmin.add_tab(\'' + menuInfo.name + '\',\'' + menuInfo.href + '\')">';
        html += '<i class="iconfont left-nav-li">&#xe6a7;</i>';
        html += '<cite>' + menuInfo.name + '</cite>';
    }
    html += '</a>';
    return html;
}
