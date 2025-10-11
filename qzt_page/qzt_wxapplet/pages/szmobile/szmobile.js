// pages/xgmm/xgmm.js
var app = getApp();
const util = require('../../utils/util.js')
const md5 = require('../../utils/md5.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imput1:'无',
    imput2:''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (options.mobile !='添加手机号'){
        this.setData({
          imput1:options.mobile
        })
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  imput2: function (e) {
    this.setData({
      imput2: e.detail.value
    })
  },
 
  doSubmit:function(e){
    
    var pwd2 = this.data.imput2;
    if(pwd2==''){
      util.alert("请输入11位手机号码");
      return false;
    }
    if (pwd2.length != 11) {
      util.alert("请输入11位手机号码");
      return false;
    }
    var imput1 = this.data.imput1;
    var url = app.globalData.baseUrl + '/webapi/qztUser/updateUserMess';
    var data = {
      oldMobile: imput1=='无'?'':imput1,
      mobile: pwd2,
      conType: 2
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;    
      if (data.code == '200') {
        wx.navigateBack({
          delta: 1
        })
      }
    })

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

  }
})