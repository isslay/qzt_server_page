// pages/lqyhq/lqyhq.js
var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    showRs:true,
    userName:'',
    price:'',
    message:''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var couponId = options.couponId;   
    if (couponId==null){
      return false;
    }
    this.setData({
      couponId: couponId
    })
    var shareCode = options.shareCode;    
    if (shareCode != null) {
      wx.setStorageSync('shareCode', shareCode)
    }    
    let that = this;
    var url = app.globalData.baseUrl + '/webapi/qztUserCoupon/activeCoupon';
    var data = {
      conId: couponId
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        that.setData({
          showRs:false,
          userName: data.userName,
          price:data.price
        })

      }
    })
  },
  reLoad:function(){
    let that = this;
    var url = app.globalData.baseUrl + '/webapi/qztUserCoupon/activeCoupon';
    var data = {
      conId: that.data.couponId
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        that.setData({
          showRs: false,
          userName: data.userName,
          price: data.price
        })

      }else{
        
        that.setData({
          message: '对不起，优惠券领取失败！'          
        })

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
  goIndex:function(){
    wx.switchTab({
      url: '../index/index'
    })
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