// pages/classroompage/classroompage.js
var app = getApp();
let wxparse = require("../../utils/wxParse/wxParse.js");
var shareTitle, sharePic,id;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    goodsId:"",
    remark:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    //获得参数  shareCode
    var shareCode = options.shareCode;
    //
    //shareCode = "10018";
    if (shareCode != null) {
      wx.setStorageSync('shareCode', shareCode)
    }

    id = options.goodsId;

    let this_ = this;

    //获取详情
    wx.request({
      url: app.globalData.baseUrl + '/pubapi/qztHealthInfo/selectById/' + options.goodsId, //url
      method: 'GET', //请求方式
      header: {
        'Content-Type': 'application/json',
      },
      success: function (res) {
        console.log(res);

        if (res.data.data == "") {
          wx.showToast({
            title: "查无此商品信息",
            icon: 'none'
          })
          setTimeout(function () {
            //要延时执行的代码
            wx.switchTab({
              url: '../index/index'
            })
          }, 1500) //延迟时间 这里是1秒
        }

        if (res.data.code === '200') {

          var datesGood = res.data.data;

          shareTitle = datesGood.infoTitle; 
          sharePic = datesGood.infoPic

          wx.setNavigationBarTitle({
            title: datesGood.infoTitle
          });

          this_.setData({            
            remark: datesGood.infoRemark,
            goodsId: datesGood.linkGoodsId
          })
          wxparse.wxParse('remark', 'html', datesGood.infoRemark, this_, 5);
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

  },
  go: function (e) {
    var goodsId = e.currentTarget.dataset.goodsid; //id;
    wx.navigateTo({
      url: "../details/details?goodsId=" + goodsId
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
      title: shareTitle,
      path: '/pages/classroompage/classroompage?goodsId=' + id + "&shareCode=" + wx.getStorageSync('userMess').userId,
      imageUrl: sharePic,  //用户分享出去的自定义图片大小为5:4,
      success: function (res) {
        // 转发成功
        console.log(path)
        wx.showToast({
          title: "分享成功",
          icon: 'success',
          duration: 2000
        })
      },
      fail: function (res) {
        // 分享失败
      },
    }
  }
})