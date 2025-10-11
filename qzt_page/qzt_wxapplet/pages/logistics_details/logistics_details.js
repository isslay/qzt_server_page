// pages/logistics_details/logistics_details.js
const util = require('../../utils/util.js');
Page({
  /**
   * 页面的初始数据
   */
  data: {
    orderNo: "",
    courierCompany: "",
    courierNo: "",
    trackinfo: []
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let orderNo = options.orderNo;
    this.setData({
      orderNo: orderNo
    });
    this.getLogisticsInformation(orderNo);
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
  //获取物流信息
  getLogisticsInformation: function (orderNo) {
    var _this = this;
    var datas = {
      orderNo: orderNo
    };
    util.httpPost(util.getLogisticsInformation, datas, "GET", false, true).then(res => {
      let data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        _this.setData({
          trackinfo: ret.trackinfo,
          courierCompany: ret.courierCompany,
          courierNo: ret.courierNo
        });
      } else {
        util.promptJumpNavigateBack(data.message);
      }
    });
  },
})