// pages/charge_off/charge_off.js
const util = require('../../utils/util.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    currentIndex: 0,
    dataShow: false,
    goodsList: [],
    theVerificationCode: "",
    orderNo: "",
    orderUserId: ""
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.setData({
      bussId: wx.getStorageSync("userMess").bussId
    });
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },
  //获取核销订单信息
  findChargeOffOrderInfo: function(e) {
    var _this = this;
    var datas = {
      bussId: _this.data.bussId,
      theVerificationCode: _this.data.theVerificationCode
    };
    util.httpPost(util.findChargeOffOrderInfo, datas, "GET", false, false).then(res => {
      let data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        _this.setData({
          orderUserId: ret.userId,
          orderNo: ret.orderNo,
          goodsList: ret.goodsList,
          dataShow: ret.goodsList.length > 0
        });
      }
    });
  },
  //确认核销
  confirmTheSettlement: function(e) {
    if (this.data.orderNo == null || this.data.orderNo == "") {
      util.showModal("请查询有效订单进行核销");
    } else {
      var datas = {
        bussId: this.data.bussId,
        orderUserId: this.data.orderUserId,
        orderNo: this.data.orderNo,
      };
      wx.showModal({
        title: '提示',
        content: '确定要核销该订单吗？',
        success: function(sm) {
          if (sm.confirm) {
            util.httpPost(util.confirmTheSettlement, datas, "POST", false, false).then(res => {
              var data = res.data;
              if (data.code == '200') {
                util.promptJumpNavigateBack("核销成功");
              }
            });
          }
        }
      });
    }
  },
  //核销码输入监听
  theVerificationCode: function(e) {
    this.setData({
      theVerificationCode: e.detail.value
    });
  },
  //查询
  selectTheVerificationCode: function(e) {
    this.setData({
      theVerificationCode: e.detail.value
    });
    this.findChargeOffOrderInfo();
  }
})