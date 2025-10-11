// pages/syddxq/syddxq.js
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgAr: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    let that = this;
    var id = options.orderId;
    that.setData({
      orderId: id
    });
    wx.request({
      //请求地址
      url: app.globalData.baseUrl + '/webapi/qztApplyTryorder/getTryOrderMess',
      data: {
        tokenId: wx.getStorageSync('tokenId'),
        userId: wx.getStorageSync('userMess').userId,
        vno: app.globalData.vno,
        cno: app.globalData.cno,
        orderId: id
      },//发送给后台的数据         
      method: 'POST',
      header: {
        //设置参数内容
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      success: function (res) {
        var data = res.data;
        if (data.code == '200') {
          var orderData = data.orderData;
          that.setData({
            orderData: orderData,
            qztBusPics: data.qztBusPics,
            qztBusPics1: data.qztBusPics1,
            stateMc: data.stateMc
          })
        }
      }
    })
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

  }
})