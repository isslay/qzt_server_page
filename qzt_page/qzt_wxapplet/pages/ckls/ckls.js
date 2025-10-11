// pages/ckls/ckls.js
// pages/sydd/sydd.js
var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    pageNum: 0,
    pageSize: 15,
    orderData:[],
    isLoad:true,
    dataShow:true,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var conType = options.conType;
    this.setData({
      conType: conType,
    })
    this.loadData();
  },
  loadData:function(){
    ///
    var that = this;
    if(!that.data.isLoad){
      return false;
    }
    var url = app.globalData.baseUrl + '/webapi/qztAccountLog/getListPage';
    var data = {
      pageNum: that.data.pageNum + 1,
      pageSize: that.data.pageSize
    }
    if(this.data.conType==1){
      app.globalData.baseUrl + '/webapi/qztAccountLog/getListPage';
    }
    if (this.data.conType ==2) {
      url = app.globalData.baseUrl + '/webapi/qztAccountRelog/getListPage';
      data.conType = 0;
    }
    if (this.data.conType == 3) {
      url = app.globalData.baseUrl + '/webapi/qztAccountRelog/getListPage';
      data.conType = 1;
    }
    
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var listData = data.data.pageData.data;
        if (data.data.pageData.current==1){
          listData = that.data.orderData.concat(listData)
        }
        that.setData({
          orderData: listData,
          pageNum: data.data.pageData.current,
          total: data.data.pageData.total,
          pageSize: data.data.pageData.size
        })  
        var isLoad = true;     
        if (data.data.pageData.current * data.data.pageData.size >= data.data.pageData.total) {
          isLoad = false;
        }        
        that.setData({
          isLoad: isLoad
        })
        if (that.data.orderData.length==0){
          that.setData({
            dataShow:false,
          })
        }
        wx.hideNavigationBarLoading();
        wx.stopPullDownRefresh();        
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
    this.setData({
      isLoad: true,
      pageNum: 0,
      orderData: []
    })
    this.loadData()
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    
    this.loadData()
  }
})