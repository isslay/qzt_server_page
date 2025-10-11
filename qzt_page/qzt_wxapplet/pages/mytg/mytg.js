// pages/mytg/mytg.js
var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentIndex: 0,
    showTitle:true,
    dataShow:[true,true],
    imgcss: ['listimg', 'listimg', 'listimg','listimg'],
    loadUrl: ['/webapi/qztUser/getListPage', '/webapi/qztUser/getServiceListPage'],
    pageData: [{ 'pageNum': 0, 'pageSize': 10, 'isLoad': true}, { 'pageNum': 0, 'pageSize': 10, 'isLoad': true}],
    orderData: [[],[]]
  },
  //swiper切换时会调用
  pagechange: function (e) {
    if ("touch" === e.detail.source) {
      let currentPageIndex = this.data.currentIndex;
      currentPageIndex = e.detail.current;
      this.setData({
        currentIndex: currentPageIndex
      });
      this.refresh1();
    }
  },
  //用户点击tab时调用
  titleClick: function (e) {
    let currentPageIndex = e.currentTarget.dataset.idx;
      this.setData({
        //拿到当前索引并动态改变
        currentIndex: e.currentTarget.dataset.idx
      });
    this.refresh1();
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {    
    let that = this;
    var url = app.globalData.baseUrl + '/webapi/qztUser/queryUserAccount';
    var data = {
      colType:1
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var userType = data.userBaseData.userType;
        var cssAr = that.data.imgcss;
        if (userType > 0) {
          cssAr[userType / 10 - 1] = "listimg dqimg";
          that.setData({
            userTypeMc: data.userTypeMc,
            parseMessage: data.parseMessage,
            showTitle: false,
            imgcss: cssAr
          })
        }
        wx.hideNavigationBarLoading();
      }
    })
    this.loadData();
  },

  loadData:function(){
    let that = this;
    if (!that.data.pageData[that.data.currentIndex].isLoad){
      return false;
    }
    var url = app.globalData.baseUrl +that.data.loadUrl[that.data.currentIndex];
    //console.log(url);
    var data = that.data.pageData[that.data.currentIndex];
    data.pageNum = data.pageNum+1;
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var newOrderData = that.data.orderData;
        if (data.data.pageData.current==1){
          newOrderData[that.data.currentIndex] = data.data.pageData.data;
        }else{
          newOrderData[that.data.currentIndex] = newOrderData[that.data.currentIndex].concat(data.data.pageData.data);
        }
       
        var pageJson = { 'pageNum': data.data.pageData.current, 'pageSize': data.data.pageData.size, 'isLoad': true }
        if (data.data.pageData.current * data.data.pageData.size >= data.data.pageData.total) {
          pageJson.isLoad = false;
        }
        var allPageData = that.data.pageData;
        allPageData[that.data.currentIndex]=pageJson;
        that.setData({
          orderData: newOrderData,
          pageData: allPageData
        })
        var dataShowAr = that.data.dataShow;
        if (newOrderData[that.data.currentIndex].length==0){
          dataShowAr[that.data.currentIndex] = false;
        }
        that.setData({
          dataShow: dataShowAr,
         
        })
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

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
   
  },
  scrollLower :function(){
    this.loadData();
  },
  refresh:function(){
    let that = this;
    wx.startPullDownRefresh();
    wx.showNavigationBarLoading();
    var allPageData = that.data.pageData;
    var orderData = that.data.orderData;
    allPageData[that.data.currentIndex] = {'pageNum': 0, 'pageSize': 10, 'isLoad': true };
    orderData[that.data.currentIndex]=[];
    that.setData({
      orderData: orderData,
      pageData: allPageData
    })
    this.loadData();
  },
  refresh1: function () {
    let that = this;
    wx.showNavigationBarLoading();
    var allPageData = that.data.pageData;
    var orderData = that.data.orderData;
    allPageData[that.data.currentIndex] = { 'pageNum': 0, 'pageSize': 10, 'isLoad': true };
    orderData[that.data.currentIndex] = [];
    that.setData({
      orderData: orderData,
      pageData: allPageData
    })
    this.loadData();
  },
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    return {
      title: app.globalData.shareTitle,
      path: app.globalData.shareUrl + "?shareCode=" + wx.getStorageSync('userMess').userId,
      imageUrl: app.globalData.sharePicUrl //不设置则默认为当前页面的截图
    }
  }
})