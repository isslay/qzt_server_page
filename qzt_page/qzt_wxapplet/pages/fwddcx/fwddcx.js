// pages/myyhq/myyhq.js
var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentIndex: 0,
    dataShow: [false, false, false, false, false],
    pageData: [{
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'state': '01'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'state': '03'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'state': '05'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'state': '07'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'state': '90'
    }],
    orderData: [
      [],
      [],
      [],
      [], []
    ]
  },
  //swiper切换时会调用
  pagechange: function(e) {
    if ("touch" === e.detail.source) {
      let currentPageIndex = this.data.currentIndex
      currentPageIndex = e.detail.current
      this.setData({
        currentIndex: currentPageIndex
      })
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
  go: function(e) {
    // console.log(e.currentTarget.dataset.id);
    wx.navigateTo({
      url: '../fwddxq/fwddxq?id=' + e.currentTarget.dataset.id,
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.loadData();
  },
  loadData: function () {
    let that = this;
    if (!that.data.pageData[that.data.currentIndex].isLoad) {
      return false;
    }
    var url = app.globalData.baseUrl + '/pubapi/qztServiceOrder/getBusListPage';
    //console.log(url);
    var data = that.data.pageData[that.data.currentIndex];
    data.pageNum = data.pageNum + 1;
    util.httpPost(url, data, 'GET').then(res => {
      var data = res.data;
      if (data.code == '200') {
        var newOrderData = that.data.orderData;
        if (data.data.pageData.current == 1) {
          newOrderData[that.data.currentIndex] = data.data.pageData.data;
        } else {
          newOrderData[that.data.currentIndex] = newOrderData[that.data.currentIndex].concat(data.data.pageData.data);
        }
        var allPageData = that.data.pageData;
        var pageJson = allPageData[that.data.currentIndex];
        pageJson.isLoad = true;
        if (data.data.pageData.current * data.data.pageData.size >= data.data.pageData.total) {
          pageJson.isLoad = false;
        }
        pageJson.pageNum = data.data.pageData.current;
        allPageData[that.data.currentIndex] = pageJson;
        that.setData({
          orderData: newOrderData,
          pageData: allPageData
        })
        var dataShowAr = that.data.dataShow;
        if (newOrderData[that.data.currentIndex].length == 0) {
          dataShowAr[that.data.currentIndex] = false;
        } else {
          dataShowAr[that.data.currentIndex] = true;
        }
        that.setData({
          dataShow: dataShowAr
        })
        console.log(that.data.orderData);
        wx.hideNavigationBarLoading();
        wx.stopPullDownRefresh();
      }
    })
  },
  scrollLower: function () {
    this.loadData();
  },
  refresh: function () {
    let that = this;
    wx.startPullDownRefresh();
    wx.showNavigationBarLoading();
    var allPageData = that.data.pageData;
    var nowPageDate = allPageData[that.data.currentIndex];
    nowPageDate.pageNum = 0;
    nowPageDate.isLoad = true;
    var orderData = that.data.orderData;
    orderData[that.data.currentIndex] = [];
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
    var nowPageDate = allPageData[that.data.currentIndex];
    nowPageDate.pageNum = 0;
    nowPageDate.isLoad = true;
    var orderData = that.data.orderData;
    orderData[that.data.currentIndex] = [];
    that.setData({
      orderData: orderData,
      pageData: allPageData
    })
    this.loadData();
  },
  qxorder: function (e) {
    var that=this;
    wx.showModal({
      title: '提示',
      content: '您确定开始服务【' + e.target.dataset.name + '】订单么？',
      confirmText: '确认',
      cancelText: '取消',
      success(res) {
        if (res.confirm) {
          var url = app.globalData.baseUrl + '/webapi/qztServiceOrder/update';
          var data={
            sId: e.target.dataset.id,
            type: "0"
          }
          util.httpPost(url, data).then(res => {
            var data = res.data;
            if (data.code == '200') {
              wx.showToast({
                title: "操作成功",
                icon: 'none'
              })
              var orderData = that.data.orderData;
              var nowOrderData = orderData[that.data.currentIndex];
              nowOrderData.splice(e.target.dataset.orderno, 1);
              that.setData({
                orderData: orderData
              })
            }
          });
         
        }
      }
    });
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

  /**
   * 用户点击右上角分享
   */
  // onShareAppMessage: function() {

  // }
})