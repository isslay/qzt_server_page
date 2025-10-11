// pages/myqb/myqb.js
var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    accountMoney:'0.00',
    sMoney: '0.00',
    tMoney: '0.00',
    tChangeMoneyMess: '0.00',
    tHaveMoneyMess: '0.00',
    sPassMoneyMess: '0.00',
    sHaveMoneyMess: '0.00',
    sChangeMoneyMess: '0.00'

  },
  sqxt: function() {
    wx.navigateTo({
      url: '../txsq/txsq',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.loadData();
  },
  loadData:function(){

    let that = this;
    var url = app.globalData.baseUrl + '/webapi/qztAccount/queryUserAccountMess';
    var data = {
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {        
        that.setData({         
          accountMoney: data.accountData.accountMoney,
          sMoney: data.accountData.shareMoney.sMoney,
          tMoney: data.accountData.shareMoney.tMoney,
          tChangeMoneyMess: data.accountData.tMoneyMess.changeMoney,
          tHaveMoneyMess: data.accountData.tMoneyMess.haveMoney,
          tGiveMoneyMess: data.accountData.tMoneyMess.giveMoney,
          sPassMoneyMess: data.accountData.sMoneyMess.haveMoney,
          sHaveMoneyMess: data.accountData.sDMoneyMess.allMoney,
          sChangeMoneyMess: data.accountData.sMoneyMess.changeMoney,       
        })     

       wx.stopPullDownRefresh();
        wx.hideNavigationBarLoading();
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
    
  },
  myAccoutLog:function(){
    wx.navigateTo({

      url: '../ckls/ckls?conType=1',

    })
  },
  tAccountLog: function () {
    wx.navigateTo({

      url: '../ckls/ckls?conType=2',

    })
  },
  sAccountLog: function () {
    wx.navigateTo({

      url: '../ckls/ckls?conType=3',

    })
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
    this.loadData();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  }
})