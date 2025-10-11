// pages/ywc/ywc.js
var util = require('../../utils/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    orderNo: "",
    consigneeName: "",
    consigneeTel: "",
    consigneeAddress: "",
    stoGoodsList: [],
    totalPrice: "",
    orderState: "",
    orderStateMc: "",
    userTel: "",
    payTypeMc: "",
    createTime: "",
    payTime: "",
    finshTime: "",
    cashCouponMoney: "",
    balanceMoney: "",
    threeSidesMoney: "",
    isServe: "N",
    pickupWay: "",
    theVerificationCode: "",
    theVerificationCodeq: "",
    theVerificationCodeh: "",
    payState: "",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let bussId = options.bussId;
    bussId = bussId == null || bussId == undefined || bussId == "undefined" ? "" : bussId;
    this.setData({
      orderNo: options.orderNo,
      bussId: bussId
    });
    this.selectByGOrderNo();
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },
  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },
  //获取订单详情
  selectByGOrderNo: function (e) {
    let _this = this;
    let bussId = _this.data.bussId;
    let datas = {
      orderNo: _this.data.orderNo,
      bussId: bussId
    };
    util.httpPost(util.selectByGOrderNo, datas, "GET", false, false, null, "1.0.1").then(res => {
      let data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        let addressMess = ret.addressMess;
        let orderMess = ret.orderMess;
        let payMess = ret.payMess;
        let theVerificationCode = orderMess.theVerificationCode;

        let orderStateMc = orderMess.orderStateMc;
        if (bussId != null && bussId != "") {
          if (orderMess.orderState == "03") {
            orderStateMc = "待到店";
          } else if (orderMess.orderState == "05") {
            orderStateMc = "待确认";
          } else if (orderMess.orderState == "09") {
            orderStateMc = "已核销";
          }
        }
        _this.setData({
          consigneeName: addressMess.consigneeName,
          consigneeTel: addressMess.consigneeTel,
          consigneeAddress: addressMess.consigneeAddress,
          stoGoodsList: ret.stoGoodsList,
          totalPrice: orderMess.totalPrice,
          orderState: orderMess.orderState,
          orderStateMc: orderStateMc,
          userTel: orderMess.userTel,
          payTypeMc: orderMess.payTypeMc,
          createTime: orderMess.createTime,
          payTime: orderMess.payTime,
          finshTime: orderMess.finshTime,
          cashCouponMoney: payMess.cashCouponMoney,
          balanceMoney: payMess.balanceMoney,
          threeSidesMoney: payMess.threeSidesMoney,
          isServe: orderMess.isServe,
          pickupWay: orderMess.pickupWay,
          theVerificationCode: theVerificationCode,
          payState: orderMess.payState,
        });
      }
    });
  },
  //取消订单
  cancellationOrder: function (e) {
    var orderNo = e.currentTarget.dataset.orderno;
    wx.showModal({
      title: '提示',
      content: '确定要取消该订单吗？',
      success: function (sm) {
        if (sm.confirm) {
          var datas = {
            orderNo: orderNo
          };
          util.httpPost(util.cancellationOfOrder, datas, "POST", false, false).then(res => {
            var data = res.data;
            if (data.code == '200') {
              util.promptJumpNavigateBack("已取消");
            }
          });
        }
      }
    });
  },
  //去支付
  toPayFor: function (e) {
    wx.redirectTo({
      url: '../goods_order_pay/goods_order_pay?orderNo=' + this.data.orderNo,
    });
  },
  //确认收货
  confirmReceipt: function (e) {
    var datas = {
      orderNo: this.data.orderNo
    };
    util.httpPost(util.confirmReceipt, datas, "POST", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        util.promptJumpNavigateBack("收货成功");
      }
    });
  },
  //查看物流
  checkTheLogistics: function (e) {
    var orderNo = this.data.orderNo;
    wx.navigateTo({
      url: '../logistics_details/logistics_details?orderNo=' + orderNo
    });
  },
  //再来一单
  recurOrder: function (e) {
    wx.navigateTo({
      url: "../details/details?goodsId=" + this.data.goodsId
    });
  },
  //申请服务
  applyForTheService: function (e) {
    wx.navigateTo({
      url: '../sqfwtwo/sqfwtwo?orderNo=' + this.data.orderNo,
    });
  },
  //联系客服/联系客户
  calling: function (res) {
    let tel = res.currentTarget.dataset.tel;
    wx.makePhoneCall({
      phoneNumber: tel,
      success: function () {
        console.log("拨打电话成功！")
      },
      fail: function () {
        console.log("拨打电话失败！")
      }
    })
  },
  //复制核销码
  copyVerificationCode() {
    wx.setClipboardData({
      data: this.data.theVerificationCode,
      success: function (res) {
        wx.getClipboardData({
          success: function (res) {
            wx.showToast({
              title: '复制成功'
            })
          }
        })
      }
    })
  },

});