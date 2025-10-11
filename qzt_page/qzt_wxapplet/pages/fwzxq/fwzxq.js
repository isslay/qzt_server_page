// pages/fwzxq/fwzxq.js
var common = require('../../utils/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    background: [],
    indicatorDots: true, //是否显示小点
    vertical: false, //横竖播放
    autoplay: true, //是否自动播放
    circular: true, //循环播放
    interval: 2000, //轮播时间
    duration: 500, //轮播速度
    previousMargin: 0, //前距离
    nextMargin: 0, //后距离
    id: null,
    userId:null,
    busName: "",
    busTel: "",
    busAddress: "",
    startEndTime: "",
    busDetail: ""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.setData({
      id: options.id
    });
  },
  go: function() {
    wx.navigateTo({
      url: '../mfsy/mfsy?bussIdCode=' + this.data.userId,
    })
  },
  go1: function() {
    wx.navigateTo({
      url: '../sqfw/sqfw?id=' + this.data.id + '&name=' + this.data.busName,
    })
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
    this.selectBusinessById();
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
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  },
  //获取服务站详情
  selectBusinessById: function(e) {
    var _this = this;
    var datas = {
      id: _this.data.id
    };
    common.httpPost(common.selectBusinessById, datas, "GET", true).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var ret = data.data;
        _this.setData({
          background: ret.busPicList,
          busName: ret.busName,
          busTel: ret.busTel,
          busAddress: ret.busAddress,
          startEndTime: ret.startEndTime,
          busDetail: ret.busDetail,
          userId: ret.userId
        });
      }
    });
  },



})