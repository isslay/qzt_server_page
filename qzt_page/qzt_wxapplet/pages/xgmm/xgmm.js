// pages/xgmm/xgmm.js
var app = getApp();
const util = require('../../utils/util.js')
const md5 = require('../../utils/md5.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imput1:'',
    imput2:'',
    imput3:''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  imput1:function(e){
    this.setData({
      imput1: e.detail.value
    })
  },
  imput2: function (e) {
    this.setData({
      imput2: e.detail.value
    })
  },
  imput3: function (e) {
    this.setData({
      imput3: e.detail.value
    })
  },
  doSubmit:function(e){
    console.log("xxxxxxxx");
      var pwd1 = this.data.imput1;
      var pwd2 = this.data.imput2;
      var pwd3 = this.data.imput3;
      if(pwd1=='' || pwd2=='' || pwd2==''){
        util.alert("请输入6位密码");
        return false;
      }
      if (pwd2.length != 6) {
        util.alert("请输入6位支付密码");
        return false;
      }
      if(pwd2!=pwd3){
        util.alert("您输入的新密码和确认密码不一致");
        return false;
      }
    var url = app.globalData.baseUrl + '/webapi/qztUser/updateUserMess';
    var data = {
      oldPayPwd:md5.hexMD5(pwd1),
      payPwd:md5.hexMD5(pwd2),
      conType: 5
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