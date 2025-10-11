// pages/pay_page/pay_page.js
var util = require('../../utils/util.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    payMoney: "0.00",
    wxOpenId: "",
    orderNo: "",
    orderType: "",
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let openId = wx.getStorageSync('userMess').wxOpenId;
    // util.showModal("openId：" + openId);
    this.setData({
      payMoney: options.payMoney,
      orderNo: options.orderNo,
      orderType: options.orderType,
      wxOpenId: openId
    });
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {},
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {},
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {},
  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {},
  //立即支付
  pay: function(e) {
    var _this = this;
    var datas = {
      orderNo: _this.data.orderNo,
      orderType: _this.data.orderType,
      openid: _this.data.wxOpenId
    };
    util.httpPost(util.pay, datas, "POST", true, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var ret = data.data;
        wx.requestPayment({
          timeStamp: ret.timeStamp,
          nonceStr: ret.nonceStr,
          package: ret.package,
          signType: ret.signType,
          paySign: ret.paySign,
          success(res) { //接口调用成功的回调函数
            if (_this.data.orderType == "SA") {
              wx.switchTab({
                url: "../my/my"
              })
            } else if (_this.data.orderType == "GS") {
              wx.redirectTo({
                url: "../ddxq/ddxq?orderNo=" + _this.data.orderNo
              });
            }
          },
          fail(res) { //接口调用结束的回调函数（调用成功、失败都会执行）
            wx.showModal({
              title: '支付提示',
              content: '您确认放弃本次支付吗',
              showCancel: false,
              success: res => {
                wx.redirectTo({
                  url: "../order/order?currentIndex=0"
                });
              }
            })
          },
          complete: function(res) { //接口调用结束的回调函数（调用成功、失败都会执行）
            console.log(res);
          }
        });
      }
    });
  },

})