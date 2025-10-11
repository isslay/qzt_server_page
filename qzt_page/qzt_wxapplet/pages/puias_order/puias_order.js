// pages/puias/puias.js
const util = require('../../utils/util.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    currentIndex: 0,
    dataShow: [true, true, true],
    pageData: [{
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'orderStateShop': '02',
      'bussId': ""
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'orderStateShop': '05',
      'bussId': ""
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'orderStateShop': '09',
      'bussId': ""
    }],
    orderData: [
      [],
      [],
      []
    ],
    setInter: null //定时器实例
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let currentIndex = options.currentIndex;
    if (currentIndex != "" && currentIndex != undefined) {
      this.setData({
        currentIndex: currentIndex
      });
    }
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
    let allPageData = this.data.pageData;
    let nowPageDate = allPageData[this.data.currentIndex];
    nowPageDate.pageNum = 0;
    nowPageDate.isLoad = true;
    let orderData = this.data.orderData;
    orderData[this.data.currentIndex] = [];
    this.setData({
      orderData: orderData,
      pageData: allPageData
    });
    this.loadData();
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
    clearInterval(this.data.setInter);
  },
  /**
   * 页面上拉触底事件的处理函数 翻页使用
   */
  scrollLower: function () {
    this.loadData();
  },
  //swiper切换时会调用
  pagechange: function (e) {
    if ("touch" === e.detail.source) {
      let currentPageIndex = this.data.currentIndex
      currentPageIndex = e.detail.current
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
  //获取订单数据
  loadData: function (e) {
    let _this = this;
    if (!_this.data.pageData[_this.data.currentIndex].isLoad) {
      return false;
    }
    var data = _this.data.pageData[_this.data.currentIndex];
    data.bussId = wx.getStorageSync("userMess").bussId;
    data.pageNum = data.pageNum + 1;
    util.httpPost(util.getGorderListPage, data, "GET", false, false, null, "1.0.1").then(res => {
      var data = res.data;
      if (data.code == '200') {
        var ret = data.data;
        var dataShowAr = [true, true, true];
        if (ret.total == 0) {
          dataShowAr[_this.data.currentIndex] = false;
          _this.setData({
            dataShow: dataShowAr
          });
        } else {
          var newOrderData = _this.data.orderData;
          if (ret.current == 1) {
            newOrderData[_this.data.currentIndex] = ret.data;
          } else {
            newOrderData[_this.data.currentIndex] = newOrderData[_this.data.currentIndex].concat(ret.data);
          }
          var allPageData = _this.data.pageData;
          var pageJson = allPageData[_this.data.currentIndex];
          pageJson.isLoad = true;
          if (ret.current * ret.size >= ret.total) {
            pageJson.isLoad = false;
          }
          pageJson.pageNum = ret.current;
          allPageData[_this.data.currentIndex] = pageJson;
          _this.setData({
            orderData: newOrderData,
            pageData: allPageData,
            dataShow: dataShowAr
          });
          wx.hideNavigationBarLoading();
          wx.stopPullDownRefresh();
        }
      }
      wx.hideNavigationBarLoading();
      wx.stopPullDownRefresh();
    });
  },
  refresh: function (e) {
    let _this = this;
    wx.startPullDownRefresh();
    wx.showNavigationBarLoading();
    var allPageData = _this.data.pageData;
    var nowPageDate = allPageData[_this.data.currentIndex];
    nowPageDate.pageNum = 0;
    nowPageDate.isLoad = true;
    var orderData = _this.data.orderData;
    orderData[_this.data.currentIndex] = [];
    var dataShow = _this.data.dataShow;
    dataShow[_this.data.currentIndex] = true;
    _this.setData({
      orderData: orderData,
      pageData: allPageData,
      dataShow: dataShow
    });
    this.loadData();
  },
  refresh1: function (e) {
    let _this = this;
    wx.showNavigationBarLoading();
    var allPageData = _this.data.pageData;
    allPageData[_this.data.currentIndex].pageNum = 0;
    allPageData[_this.data.currentIndex].isLoad = true;
    var orderData = _this.data.orderData;
    orderData[_this.data.currentIndex] = [];
    _this.setData({
      orderData: orderData,
      pageData: allPageData
    });
    this.loadData();
  },
  //核销
  clearing: function (e) {
    wx.navigateTo({
      url: "../charge_off/charge_off",
    });
  },
  //订单详情
  orderDetails: function (res) {
    let orderNo = res.currentTarget.dataset.orderno;
    let bussId = wx.getStorageSync("userMess").bussId;
    wx.navigateTo({
      url: '../ddxq/ddxq?orderNo=' + orderNo + "&bussId=" + bussId
    });
  }

})