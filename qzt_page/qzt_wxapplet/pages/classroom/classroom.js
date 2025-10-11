// pages/classroom/classroom.js
var app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    goodsround:[]
  },

 

  /**
   * 生命周期函数--监听页面加载
   */

  onLoad: function (options) {

    //获得参数  shareCode
    var shareCode = options.shareCode;
    if (shareCode != null) {
      wx.setStorageSync('shareCode', shareCode)
    }

    let this_ = this;

    //获取banner
    wx.request({
      url: app.globalData.baseUrl + '/pubapi/qztHealthInfo/getListPage', //url
      method: 'GET', //请求方式
      header: {
        'Content-Type': 'application/json',
      },
      data: {
        vno: app.globalData.vno, //参数
        cno: app.globalData.cno, //参数
        pageNum: 1, //参数
        pageSize: 10000, //参数
      },
      success: function (res) {
        if (res.data.code === '200') {
          var bannerList = [];
          var dates = res.data.data.data;
          for (var i = 0; i < dates.length; i++) {
            bannerList.push({
              "goodsId": dates[i].id,
              "url": dates[i].infoPic,
              "name": dates[i].infoTitle
            });
          }
          this_.setData({
            goodsround: bannerList
          })
        }
      },
      fail: function () {
        app.consoleLog("请求数据失败");
      },
      complete: function () {
        // complete 
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
    if (typeof this.getTabBar === 'function' &&
      this.getTabBar()) {
      this.getTabBar().setData({
        selected: 1
      })
    }
  },
  go: function (e) {
    var goodsId = e.currentTarget.dataset.goodsid; //id;
    wx.navigateTo({
      url: '../classroompage/classroompage?goodsId=' + goodsId,
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