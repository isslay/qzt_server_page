// pages/withdrawal_record/withdrawal_record.js
const util = require('../../utils/util.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    currentIndex: 0,
    dataShow: [true, true, true, true],
    pageData: [{
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'auditState': '00'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'auditState': '01'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'auditState': '10'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'auditState': '20'
    }],
    orderData: [
      [],
      [],
      [],
      []
    ]
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

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
    this.loadData();
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
    var allPageData = this.data.pageData;
    var nowPageDate = allPageData[this.data.currentIndex];
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
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
    this.loadData();
  },
  //swiper切换时会调用
  pagechange: function(e) {
    if ("touch" === e.detail.source) {
      let currentPageIndex = this.data.currentIndex
      currentPageIndex = e.detail.current
      this.setData({
        currentIndex: currentPageIndex
      });
      if (this.data.orderData[currentPageIndex].length == 0) {
        this.loadData();
      }
    }
  },
  //用户点击tab时调用
  titleClick: function(e) {
    let currentPageIndex = e.currentTarget.dataset.idx;
    this.setData({
      //拿到当前索引并动态改变
      currentIndex: e.currentTarget.dataset.idx
    });
    if (this.data.orderData[currentPageIndex].length == 0) {
      this.loadData();
    }
  },
  //获取提现记录数据
  loadData: function(e) {
    let _this = this;
    if (!_this.data.pageData[_this.data.currentIndex].isLoad) {
      return false;
    }
    var data = _this.data.pageData[_this.data.currentIndex];
    data.pageNum = data.pageNum + 1;
    util.httpPost(util.getWithdrawListPage, data, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var ret = data.data;
        var dataShowAr = _this.data.dataShow;
        if (ret.total == 0) {
          dataShowAr[_this.data.currentIndex] = false;
          _this.setData({
            dataShow: dataShowAr
          })
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
          if (ret.current * ret.data.size >= ret.total) {
            pageJson.isLoad = false;
          }
          pageJson.pageNum = ret.current;
          allPageData[_this.data.currentIndex] = pageJson;
          _this.setData({
            orderData: newOrderData,
            pageData: allPageData,
            dataShow: dataShowAr
          });
        }
      }
      wx.hideNavigationBarLoading();
      wx.stopPullDownRefresh();
    });


  }

})