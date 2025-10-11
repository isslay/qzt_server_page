// pages/myyhq/myyhq.js
var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    showShare: true,
    currentIndex: 0,
    tAccountMoney: 0,
    showModalStatus: false,
    dataShow: [true, true, true, true],
    pageData: [{
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'state': '0'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'state': '1'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'state': '2'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'state': '3'
    }],
    orderData: [
      [],
      [],
      [],
      []
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
  titleClick: function(e) {
    let currentPageIndex = e.currentTarget.dataset.idx;
    this.setData({
      //拿到当前索引并动态改变
      currentIndex: e.currentTarget.dataset.idx
    });
    this.refresh1();
  },
  showModal: function() {
    this.setData({
      showShare: true
    })
    let that = this;
    var url = app.globalData.baseUrl + '/webapi/qztAccount/queryUserAccountMess';
    var data = {

    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        this.setData({
          tAccountMoney: data.accountData.tMoneyMess.haveMoney
        })
        // 显示遮罩层
        var animation = wx.createAnimation({
          duration: 200,
          timingFunction: "ease-in-out",
          delay: 0
        })
        this.animation = animation
        animation.translateY(500).step()
        this.setData({
          animationData: animation.export(),
          showModalStatus: true
        })
        setTimeout(function() {
          animation.translateY(0).step()
          this.setData({
            animationData: animation.export()
          })
        }.bind(this), 10)
      }
    })

  },
  hideModal: function() {
    this.refresh();
    this.setData({
      showModalStatus: false,
    })
  },
  createCoupon: function(re) {
    let that = this;
    var url = app.globalData.baseUrl + '/webapi/qztUserCoupon/createCoupone';
    var data = {

    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        if (data.rs == '0001') {
          util.alert("对不起，您当前推广佣金不足");
        } else if (data.rs == '0002') {
          util.alert("对不起，优惠券生成失败请联系客服");
        } else {
          util.alert("恭喜您优惠券生成成功！点击分享即可");
          that.setData({
            shareUrl: data.shareUrl,
            sharePicUrl: data.sharePicUrl,
            shareMess: data.shareMess,
            showShare: false
          })
        }

      }
    })

  },
  onShareAppMessage: function(res) {
    //开始生成优惠券    
    let that = this;
    var shareUrl = that.data.shareUrl + "&shareCode=" + wx.getStorageSync('userMess').userId; //获取产品id
    console.log(shareUrl)
    var sharePicUrl = that.data.sharePicUrl; //获取产品标题
    var shareMess = that.data.shareMess; //产品图片
    if (res.from === 'button') {
      if(res.target.dataset.couid==null){
        return {
          title: shareMess,
          path: shareUrl,
          imageUrl: sharePicUrl //不设置则默认为当前页面的截图
        }
      }else{    
        console.log(that.data.shareData.shareUrl + res.target.dataset.couid + "&shareCode=" + wx.getStorageSync('userMess').userId)    
        return {
          title: that.data.shareData.shareMess,
          path: that.data.shareData.shareUrl + res.target.dataset.couid + "&shareCode=" + wx.getStorageSync('userMess').userId,
          imageUrl: that.data.shareData.sharePicUrl //不设置则默认为当前页面的截图
        }
      }
      // 来自页面内转发按钮      
    }else{
      return {
        title: app.globalData.shareTitle,
        path: app.globalData.shareUrl + "?shareCode=" + wx.getStorageSync('userMess').userId,
        imageUrl: app.globalData.sharePicUrl //不设置则默认为当前页面的截图
      }
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //加载分享默认信息
    let that = this;
    var url = app.globalData.baseUrl + '/webapi/qztUserCoupon/shareBaseMess';
    var data = {

    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        that.setData({
          shareData: data.backData         
        })
      }
    })
    this.loadData();
  },
  loadData: function() {
    let that = this;
    if (!that.data.pageData[that.data.currentIndex].isLoad) {
      return false;
    }
    var url = app.globalData.baseUrl + '/webapi/qztUserCoupon/getListPage';
    //console.log(url);
    var data = that.data.pageData[that.data.currentIndex];
    data.giveUserId = wx.getStorageSync('userMess').userId;
    data.pageNum = data.pageNum + 1;
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var newOrderData = that.data.orderData;
        if (data.data.pageData.current==1){
          newOrderData[that.data.currentIndex] = data.data.pageData.data;
        }else{
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
        }
        that.setData({
          dataShow: dataShowAr
        })
        console.log(that.data.pageData);
        wx.hideNavigationBarLoading();
        wx.stopPullDownRefresh();
      }
    })
  },

  scrollLower: function() {
    this.loadData();
  },
  refresh: function() {
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
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

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

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  }
})