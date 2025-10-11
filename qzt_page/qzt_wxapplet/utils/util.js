var newapp = getApp();
const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

function reLogin() {
  wx.showLoading({
    title: '重新登录中..',
  })
  wx.login({
    success: res => {
      // 发送 res.code 到后台换取 openId, sessionKey, unionId
      wx.request({
        //请求地址
        url: newapp.globalData.baseUrl + '/pubapi/weChat/weJsChatLogin',
        data: {
          code: res.code,
          shareCode: wx.getStorageSync('shareCode')
        }, //发送给后台的数据         
        method: 'POST',
        header: {
          //设置参数内容
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        success: function (res) {
          var data = res.data;
          if (data.code == '200') {
            wx.hideLoading();
            data = data.data;
            wx.setStorageSync('tokenId', data.tokenId);
            wx.setStorageSync('userMess', data.userData);
            //刷新当前页面的数据
            let pages = getCurrentPages();
            let atPresent = pages[pages.length - 1]; //当前页实例
            console.log("当前页地址：", atPresent.route);
            if (atPresent.route == "pages/fillin_goods_order/fillin_goods_order") {
              atPresent.onLoad();
            } else {
              atPresent.onShow();
            }
            // atPresent.onShow();

          }
        },
        fail: function (err) {}, //请求失败
        complete: function () {} //请求完成后执行的函数
      })

    }
  })
}
/**
 * url:请求地址
 * data:请求数据
 * method:默认为post
 * userMess:默认包含了tokenid和userid 是否需要自传用户信息 
 * isReturnData:是否返回data数据
 * isJsonData:请求数据编码类型
 * vnos:接口版本
 */
function httpPost(urls, data, method, userMess, isReturnData, isJsonData, vnos) {
  // console.log(wx.getStorageSync('userMess'));
  let contentType = "application/x-www-form-urlencoded";
  let vno = !vnos ? newapp.globalData.vno : vnos;
  if (isJsonData == "Y") {
    contentType = "application/json;charset=UTF-8";
    if (urls.indexOf("?") == -1) {
      urls += "?vno=" + vno;
    } else {
      urls += "&vno=" + vno;
    }
    urls += "&cno=" + newapp.globalData.cno;
  } else {
    data.vno = vno;
    data.cno = newapp.globalData.cno;
  }
  if (method == null) {
    method = "POST";
  }
  if (userMess == null || !userMess) {
    if (wx.getStorageSync('tokenId') == '') {
      return new Promise(function (resolve, reject) {

      });
    }
    if (isJsonData == "Y") {
      urls += "&tokenId=" + wx.getStorageSync('tokenId');
      urls += "&userId=" + wx.getStorageSync('userMess').userId;
    } else {
      data.tokenId = wx.getStorageSync('tokenId');
      data.userId = wx.getStorageSync('userMess').userId;
    }
  }

  return new Promise(function (resolve, reject) {
    wx.showLoading({
      title: '加载中',
    });

    wx.request({
      url: urls,
      data: data,
      method: method,
      header: {
        'Content-Type': contentType
      },
      success: function (res) {
        wx.hideLoading();
        if (res.data.code == '0002') {
          reLogin();
        } else if (res.data.code == '200' || isReturnData) {
          resolve(res);
        } else {
          wx.showToast({
            title: res.data.message,
            icon: 'none',
            duration: 2500
            // mask: true
          });
        }

      },
      fail: () => {
        reject("系统异常，请重试！")
      }
    })
  })

}

/**
 * 通用提示框：灰
 */
function alert(title) {
  wx.showToast({
    title: title,
    icon: 'none',
    duration: 2500
    // mask: true
  });
}

/**
 * 白框提示
 */
function showModal(content) {
  wx.showModal({
    title: '提示',
    content: content,
    showCancel: false
  })
}


/**
 * 白框提示 返回上一页
 */
function promptJumpNavigateBack(content) {
  wx.showModal({
    title: '提示',
    content: content,
    showCancel: false,
    success: function () {
      wx.navigateBack({
        delta: 1
      });
    }
  })
}

/**
 * 白框提示 跳转指定页
 * 
 * content:提示内容
 * urls:跳转的页面
 * type:跳转方式 返回上一页01、关闭当前页跳转03、正常跳转05
 */
function promptJumpAssign(content, urls, type) {
  wx.showModal({
    title: '提示',
    content: content,
    showCancel: false,
    success: function () {
      if ("01" == type) {
        wx.navigateBack({
          delta: 1
        });
      } else if ("03" == type) {
        wx.redirectTo({
          url: urls
        });
      } else if ("05" == type) {
        wx.navigateTo({
          url: urls,
        });
      } else {

      }
    }
  })
}

/**
 * S转 HH:mm:ss
 */
function secondTransformation(second) {
  let countdownTimeText = "";
  let timingTime = parseInt(second);
  if (timingTime > 0) {
    let days = Math.floor(timingTime / (60 * 60 * 24)); //天数
    let modulo = timingTime % (60 * 60 * 24); //取模（余数）
    let hours = Math.floor(modulo / (60 * 60)); //小时数
    modulo = modulo % (60 * 60);
    let minutes = Math.floor(modulo / 60); //分钟
    let seconds = modulo % 60; //秒
    hours = hours.toString();
    minutes = minutes.toString();
    seconds = seconds.toString();
    if (days > 0) {
      countdownTimeText = (hours.length == 1 ? "0" + hours : hours) + ":" + (minutes.length == 1 ? "0" + minutes : minutes) + ":" + (seconds.length == 1 ? "0" + seconds : seconds);
    } else {
      countdownTimeText = (hours.length == 1 ? "0" + hours : hours) + ":" + (minutes.length == 1 ? "0" + minutes : minutes) + ":" + (seconds.length == 1 ? "0" + seconds : seconds);
    }
  }
  return countdownTimeText;
}

module.exports = {
  formatTime: formatTime,
  httpPost: httpPost,
  reLogin: reLogin,
  alert: alert,
  showModal: showModal,
  promptJumpNavigateBack: promptJumpNavigateBack,
  promptJumpAssign: promptJumpAssign,
  secondTransformation: secondTransformation,

  getAddressList: getApp().globalData.baseUrl + "webapi/qztUserAddresss/getAddressList", //查询收货地址列表
  selectAddressById: getApp().globalData.baseUrl + "webapi/qztUserAddresss/selectAddressById", //查询收货地址详情
  selectDefaultaccAddress: getApp().globalData.baseUrl + "webapi/qztUserAddresss/selectDefaultaccAddress", //获取默认地址
  addAddress: getApp().globalData.baseUrl + "webapi/qztUserAddresss/addAddress", //新增收货地址
  updateAddress: getApp().globalData.baseUrl + "webapi/qztUserAddresss/updateAddress", //修改用户收货地址
  delectAddress: getApp().globalData.baseUrl + "webapi/qztUserAddresss/delectAddress", //删除收货地址
  defaultArea: getApp().globalData.baseUrl + "webapi/qztUserAddresss/defaultArea", //设置默认地址
  applySub0rder: getApp().globalData.baseUrl + "webapi/qztApplyBusorder/applySub0rder", //服务站申请提交订单
  payApplay0rder: getApp().globalData.baseUrl + "webapi/qztApplyBusorder/payApplay0rder", //服务站申请订单支付
  getPayApplay0rderInfo: getApp().globalData.baseUrl + "webapi/qztApplyBusorder/getPayApplay0rderInfo", //获取服务站申请订单支付信息
  getApplay0rderInfo: getApp().globalData.baseUrl + "webapi/qztApplyBusorder/getApplay0rderInfo", //获取申请服务站订单信息
  pay: getApp().globalData.baseUrl + "pubapi/weChat/pay", //微信支付
  getBusinessListPage: getApp().globalData.baseUrl + "pubapi/qztBusiness/getListPage", //分页查询服务站列表
  selectBusinessById: getApp().globalData.baseUrl + "pubapi/qztBusiness/selectById", //查询服务站详情
  queryFillinGorderInfo: getApp().globalData.baseUrl + "webapi/qztGoods/queryFillinGorderInfo", //获取商品填写订单页信息
  subGorder: getApp().globalData.baseUrl + "webapi/qztGorder/subGorder", //提交商品订单
  getPay0rderInfo: getApp().globalData.baseUrl + "webapi/qztGorder/getPay0rderInfo", //获取支付页信息
  pay0rder: getApp().globalData.baseUrl + "webapi/qztGorder/pay0rder", //商品订单支付
  cancellationOfOrder: getApp().globalData.baseUrl + "webapi/qztGorder/cancellationOfOrder", //取消商品订单
  confirmReceipt: getApp().globalData.baseUrl + "webapi/qztGorder/confirmReceipt", //确认收货
  findChargeOffOrderInfo: getApp().globalData.baseUrl + "webapi/qztGorder/findChargeOffOrderInfo", //核销码查询核销订单
  confirmTheSettlement: getApp().globalData.baseUrl + "webapi/qztGorder/confirmTheSettlement", //商家确认核销
  getLogisticsInformation: getApp().globalData.baseUrl + "webapi/qztGorder/getLogisticsInformation", //获取物流信息
  getGorderListPage: getApp().globalData.baseUrl + "webapi/qztGorder/getListPage", //分页查询商品订单
  selectByGOrderNo: getApp().globalData.baseUrl + "webapi/qztGorder/selectByOrderNo", //查询商品订单详情
  getShroffAccountList: getApp().globalData.baseUrl + "webapi/qztUserBank/getList", //查询我的收款账号列表
  getShroffAccountById: getApp().globalData.baseUrl + "webapi/qztUserBank/selectById", //根据id查询收款账号详情
  addShroffAccount: getApp().globalData.baseUrl + "webapi/qztUserBank/addUserBank", //新增收款账号
  defaultShroffAccount: getApp().globalData.baseUrl + "webapi/qztUserBank/defaultUserBank", //设置默认收款账号
  modifyShroffAccount: getApp().globalData.baseUrl + "webapi/qztUserBank/modify", //修改收款账号
  delShroffAccount: getApp().globalData.baseUrl + "webapi/qztUserBank/delById", //删除收款账号
  getBankList: getApp().globalData.baseUrl + "webapi/qztUserBank/getBankList", //获取银行列表
  withdrawApplyfor: getApp().globalData.baseUrl + "webapi/qztWithdraw/withdrawApplyfor", //提现申请
  getWithdrawListPage: getApp().globalData.baseUrl + "webapi/qztWithdraw/getListPage", //提现记录分页查询
  getWithdrawPageInfo: getApp().globalData.baseUrl + "webapi/qztWithdraw/getWithdrawPageInfo", //提现页相关信息

  addGoodsShoppingCart: getApp().globalData.baseUrl + "webapi/qztStoGoods/addGoods", //添加商品到购物车
  delGoodsShoppingCart: getApp().globalData.baseUrl + "webapi/qztStoGoods/delGoods", //删除购物车商品
  getStoGoodsList: getApp().globalData.baseUrl + "webapi/qztStoGoods/getStoGoodsList", //查询购物车商品
  updateStoGoodsList: getApp().globalData.baseUrl + "webapi/qztStoGoods/updateStoGoodsList", //更新购物车商品信息
  getSettlementPageInfo: getApp().globalData.baseUrl + "webapi/qztStoGoods/getSettlementPageInfo", //获取商品填写订单页-结算信息
  recurOrder: getApp().globalData.baseUrl + "webapi/qztGorder/recurOrder", //再来一单添加购物车
}