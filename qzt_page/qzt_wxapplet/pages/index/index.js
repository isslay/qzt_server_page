// pages/index/index.js
var app = getApp();
var current, size, total;
var common = require('../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    background: ['../../img/sli1.png', '../../img/sli2.png', '../../img/sli1.png'],
    indicatorDots: false, //是否显示小点
    vertical: false, //横竖播放
    autoplay: true, //是否自动播放
    circular: true, //循环播放
    interval: 2000, //轮播时间
    duration: 500, //轮播速度
    previousMargin: 20, //前距离
    nextMargin: 20, //后距离
    imageIndex: 0,
    goodsround: []
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    //获得参数  shareCode
    var shareCode = options.shareCode;
    if (options.scene) {
      let scene = decodeURIComponent(options.scene);
      shareCode = scene;
      //wx.setStorageSync('shareCode', shareCode)
    }
    if (shareCode != null) {
      wx.setStorageSync('shareCode', shareCode)
    }
    
    
    let this_ = this;

    //获取banner
    wx.request({
      url: app.globalData.baseUrl + '/pubapi/qztBanner/getListPage', //url
      method: 'GET', //请求方式
      header: {
        'Content-Type': 'application/json',
      },
      data: {
        vno: app.globalData.vno, //参数
        cno: app.globalData.cno, //参数
        type: '1', //参数
        source: 'WX', //参数
        pageNum: 1, //参数
        pageSize: 100, //参数
      },
      success: function(res) {
        if (res.data.code === '200') {
          var bannerList = [];
          var dates = res.data.data.data;
          for (var i = 0; i < dates.length; i++) {
            bannerList.push({
              "goodsId": dates[i].linkUrl,
              "url": dates[i].picUrl
            });
          }
          this_.setData({
            background: bannerList,
            imageIndex: 0
          })
        }
      },
      fail: function() {
        app.consoleLog("请求数据失败");
      },
      complete: function() {
        // complete 
      }
    })

    this.loadData(false);
  },

  loadData: function(vr) {
    wx.showLoading({
      title: '加载中',
    })
    var this_ = this;
    //获取商品列表
    wx.request({
      url: app.globalData.baseUrl + '/pubapi/qztGoods/getListPage', //url
      method: 'GET', //请求方式
      header: {
        'Content-Type': 'application/json',
      },
      data: {
        vno: app.globalData.vno, //参数
        cno: app.globalData.cno, //参数
        pageNum: 1, //参数
        pageSize: 10, //参数
      },
      success: function(res) {
        // console.log(res);
        if (res.data.code === '200') {
          var goods = [];
          var dates = res.data.data.data;
          for (var i = 0; i < dates.length; i++) {
            goods.push({
              "goodsId": dates[i].id,
              "url": dates[i].thumbnail,
              "name": dates[i].goodsName,
              "money": dates[i].goodsPrice / 100
            });
          }
          this_.setData({
            goodsround: goods,
            current: res.data.data.current,
            total: res.data.data.total,
            size: res.data.data.size
          })
          wx.hideLoading();
          if (vr) {
            wx.hideNavigationBarLoading();
            wx.stopPullDownRefresh();
          }
        }
      },
      fail: function() {
        app.consoleLog("请求数据失败");
      },
      complete: function() {
        // complete 
      }
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
    if (typeof this.getTabBar === 'function' &&
      this.getTabBar()) {
      this.getTabBar().setData({
        selected: 0
      })
    }
  },
  go: function() {
    wx.navigateTo({
      url: '../mfsy/mfsy',
    })
  },

  onGotUserInfo: function(e) {
    console.log("nickname=" + e.detail.userInfo.nickName);
  },
  //商品详情
  particulars: function(e) {

    var goodsId = e.currentTarget.dataset.goodsid; //商品id;

    wx.navigateTo({
      url: "../details/details?goodsId=" + goodsId
    })
  },

  //轮播详情
  background: function(e) {

    var goodsId = e.currentTarget.dataset.goodsid; //商品id;
    // console.log(goodsId);
    if (goodsId != "") {
      wx.navigateTo({
        url: "../details/details?goodsId=" + goodsId
      })
    }

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
    var total = this.data.total; //总
    var size = this.data.size;
    if (current * size < total) {
      wx.showLoading({
        title: '加载中',
      })
      var this_ = this;
      //获取商品列表
      wx.request({
        url: app.globalData.baseUrl + '/pubapi/qztGoods/getListPage', //url
        method: 'GET', //请求方式
        header: {
          'Content-Type': 'application/json',
        },
        data: {
          vno: app.globalData.vno, //参数
          cno: app.globalData.cno, //参数
          pageNum: current + 1,
          pageSize: size,
        },
        success: function(res) {
          // console.log("=============");
          if (res.data.code === '200') {
            var goods = [];
            var dates = res.data.data.data;
            for (var i = 0; i < dates.length; i++) {
              goods.push({
                "goodsId": dates[i].id,
                "url": dates[i].thumbnail,
                "name": dates[i].goodsName,
                "money": dates[i].goodsPrice / 100
              });
            }
            this_.setData({
              goodsround: this_.data.goodsround.concat(goods),
              current: res.data.data.current,
              total: res.data.data.total,
              size: res.data.data.size
            })
            wx.hideLoading();
          }
        },
        fail: function() {
          app.consoleLog("请求数据失败");
        },
        complete: function() {
          // complete 
        }
      })
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {
    return {
      title: app.globalData.shareTitle,
      path: app.globalData.shareUrl + "?shareCode=" + wx.getStorageSync('userMess').userId,
      imageUrl: app.globalData.sharePicUrl //不设置则默认为当前页面的截图
    }
  }
})