// pages/yxck/yxck.js
var common = require('../../utils/util.js');
Page({
  /**
   * 页面的初始数据
   */
  data: {
    contactName: "",
    contactTel: "",
    address: "",
    orderState: "",
    orderStateMc: "",
    createTime: "",
    shipmentsTime: "",
    finshTime: "",
    consigneeName: "",
    consigneeTel: "",
    consigneeAddress: "",
    payType: "",
    orderType: "",
    threeSidesMoney: "",
    orderNo: "",
    courierCompany: "",
    courierNo: ""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    this.getApplay0rderInfo();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },
  //获取申请服务站订单信息
  getApplay0rderInfo: function(e) {
    var _this = this;
    var datas = {};
    common.httpPost(common.getApplay0rderInfo, datas, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var ret = data.data;
        let applyMess = ret.applyMess;
        let borderMess = ret.borderMess;
        let addressMess = ret.addressMess;
        let payMess = ret.payMess;
        if (payMess.isContinuePay == "N") {
          wx.showModal({
            title: '提示',
            content: "意向申请信息不全，请补全",
            showCancel: false,
            success: function() {
              wx.switchTab({
                url: "../cooperation/cooperation"
              });
            }
          });
        } else {
          _this.setData({
            contactName: applyMess.contactName,
            contactTel: applyMess.contactTel,
            address: applyMess.address,
            orderState: borderMess.orderState,
            orderStateMc: borderMess.orderStateMc,
            createTime: borderMess.createTime,
            shipmentsTime: borderMess.shipmentsTime,
            finshTime: borderMess.finshTime,
            consigneeName: addressMess.consigneeName,
            consigneeTel: addressMess.consigneeTel,
            consigneeAddress: addressMess.consigneeAddress,
            payType: payMess.payType,
            orderType: payMess.orderType,
            threeSidesMoney: payMess.threeSidesMoney,
            orderNo: borderMess.orderNo,
            courierCompany: addressMess.courierCompany,
            courierNo: addressMess.courierNo
          });
        }
      }
    });
  },
  //立即支付
  continuePay: function(e) {
    wx.redirectTo({
      url: "../pay_page/pay_page?orderType=" + this.data.orderType + "&orderNo=" + this.data.orderNo + "&payMoney=" + this.data.threeSidesMoney
    });
  }

})