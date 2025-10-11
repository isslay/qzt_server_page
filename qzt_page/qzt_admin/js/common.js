/**
 * Created by cgw on 2018/6/6.
 */
var excelPath = 'http://' + window.location.host + '/qzt';


/**
 *  根据搜索容器得到搜索筛选参数
 * @param searchId
 * @returns {{}} 参数集合
 */
function getSearchParams(searchId) {
    var param = {};
    $ = layui.jquery;
    $("#" + searchId).find("input,select").each(function () {
        var id = this.id;
        var value = this.value;
        if (id.length > 0) {
            if (value != null) {
                var str = "param." + id + "='" + value + "'";
                eval("(" + str + ")")
            }
        }
    });
    return param;
}

/**
 *  格式化时间  data.format(时间格式)
 * @param fmt
 * @returns {*}
 */
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

/**
 * 取得固定位数的数值
 *@param{Object}num
 */
function fixedNum(num, len) {
    if (len > 0) {
        //num = new Number(num).toFixed(len+1)+"";
        //num = num.substring(0,num.length-1);
        num = new Number(num + 1).toFixed(len);//四舍五入之前加1
        num = new Number(num - 1).toFixed(len);//四舍五入之后减1，再四舍五入一下
    }
    return num;
}

var dic = new Array();

/**
 *  数据表格字典翻译
 * @param dicType 字典类型
 * @param tableIns  layui表格id
 * @param url   请求地址
 * @param searchId  搜索条件容器
 */
function getDicObj(dicType, tableIns, url, searchId) {
    $ = layui.jquery;
    if (dicType == null) {
        tableIns.reload({
            url: url,
            where: { //设定异步数据接口的额外参数，任意设
                condition: getSearchParams(searchId)
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        });
    } else {
        $.ajax({
            type: "POST",
            url: "webapi/sysDic?dicType=" + dicType,
            dataType: 'json',
            async: false,
            success: function (data) {
                if (data.code == 200) {
                    for (var key in data.data) {
                        var jsonAr = data.data[key];
                        for (var i in jsonAr) {
                            dic[key + jsonAr[i].code] = jsonAr[i].codeText;
                        }
                    }
                }
                tableIns.reload({
                    url: url,
                    where: { //设定异步数据接口的额外参数，任意设
                        condition: getSearchParams(searchId)
                    },
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                });
            }
        });
    }
}

//获取字段集合
function getDicObjByType(dicType) {
    $ = layui.jquery;
    $.ajax({
        type: "post",
        url: "webapi/sysDic?dicType=" + dicType,
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data.code == 200) {
                for (var key in data.data) {
                    var jsonAr = data.data[key];
                    for (var i in jsonAr) {
                        dic[key + jsonAr[i].code] = jsonAr[i].codeText;
                    }
                }
            }
        }
    });
}

function toThousands(num) {
    var num = (num || 0).toString(), result = '';
    var numAr = num.split(".");
    num = numAr[0];
    while (num.length > 3) {
        result = ',' + num.slice(-3) + result;
        num = num.slice(0, num.length - 3);
    }
    if (num) {
        result = num + result;
    }
    if (numAr.length > 1) {
        result = result + "." + numAr[1];
    }
    return result;
}

//单独字典翻译xiaofei
function getsysDic(dicType) {
    $.ajax({
        type: "post",
        url: "webapi/sysDic?dicType=" + dicType,
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data.code == 200) {
                for (var key in data.data) {
                    var jsonAr = data.data[key];
                    for (var i in jsonAr) {
                        dic[key + jsonAr[i].code] = jsonAr[i].codeText;
                    }
                }
            }
        }
    });
}


//导出通用方法 Xiaofei
function excel(searchId, urls) {
    var urlhead = window.location.href.substr(0, 5);
    urlhead = urlhead == "https" ? "https://" : "http://";
    urls = urlhead + window.location.host + "/qzt/" + urls;
    if (searchId != "") {
        $("#" + searchId).find("input,select").each(function () {
            var id = this.id;
            var value = this.value;
            if (id.length > 0) {
                if (value != "" && value != null) {
                    if (urls.indexOf("?") > -1) {
                        urls += "&" + id + "=" + value;
                    } else {
                        urls += "?" + id + "=" + value;
                    }
                }
            }
        });
    }
    window.location.href = urls;
}

/**
 * 获取uel对应的参数
 * @param name key
 * @returns {*}
 */
function getUrlParmas(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return r[2];
    return '';
}