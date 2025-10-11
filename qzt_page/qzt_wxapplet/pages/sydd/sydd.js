// pages/sydd/sydd.js
var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    dataShow: true,
  },

  go: function(e) {
    console.log(e);
    wx.navigateTo({
      url: '../syddxq/syddxq?orderId=' + e.currentTarget.dataset.orderid,
    })

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.loadData(false);

  },
  loadData: function(vr) {

    var that = this;
    var url = app.globalData.baseUrl + '/webapi/qztApplyTryorder/getListPage';
    var data = {
      pageNum: 1,
      pageSize: 10,
      type: 1
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        that.setData({
          orderData: data.data.pageData.data,
          current: data.data.pageData.current,
          total: data.data.pageData.total,
          size: data.data.pageData.size
        })
        wx.hideLoading();
        if (vr) {
          wx.hideNavigationBarLoading();
          wx.stopPullDownRefresh();
        }
        console.log(that.data.orderData.length);
        if (that.data.orderData.length == 0) {
          that.setData({
            dataShow: false
          })
        }
      }
    })

  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  qxorder: function(e) {
    let that = this;
    wx.showModal({
      title: '提示',
      content: '您确定取消该试用订单么？',
      confirmText: '确认',
      cancelText: '取消',
      success(res) {
        if (res.confirm) {
          var id = e.target.dataset.orderid;
          wx.request({
            //请求地址
            url: app.globalData.baseUrl + '/webapi/qztApplyTryorder/cancelOrder',
            data: {
              tokenId: wx.getStorageSync('tokenId'),
              userId: wx.getStorageSync('userMess').userId,
              vno: app.globalData.vno,
              cno: app.globalData.cno,
              orderId: id
            }, //发送给后台的数据         
            method: 'POST',
            header: {
              //设置参数内容
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            success: function(res) {
              var data = res.data;
              if (data.code == '200') {
                var orderData = that.data.orderData;
                var nowData = orderData[e.target.dataset.orderno];
                nowData.obj.orderState = '99';
                nowData.stateMc = '已取消';
                orderData[e.target.dataset.orderno] = nowData;
                that.setData({
                  orderData: orderData
                })
              }
            }
          })

        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

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
    wx.showNavigationBarLoading();
    this.loadData(true);
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
    var current = this.data.current;
    var total = this.data.total;
    var size = this.data.size;
    if (current * size < total) {

      var that = this;

      var url = app.globalData.baseUrl + '/webapi/qztApplyTryorder/getListPage';
      var data = {
        tokenId: wx.getStorageSync('tokenId'),
        userId: wx.getStorageSync('userMess').userId,
        pageNum: current + 1,
        pageSize: size,
        vno: app.globalData.vno,
        cno: app.globalData.cno,
        type: 1
      }
      util.httpPost(url, data).then(res => {
        var data = res.data;
        console.log(data.applyType)
        if (data.code == '200') {
          that.setData({
            orderData: that.data.orderData.concat(data.data.pageData.data),
            current: data.data.pageData.current,
            total: data.data.pageData.total,
            size: data.data.pageData.size
          })
          wx.hideLoading();
        }
      })
    }
  }
  
})