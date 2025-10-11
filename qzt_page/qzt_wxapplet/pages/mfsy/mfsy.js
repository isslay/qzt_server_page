// pages/myyhq/myyhq.js
var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    array: [],
    array1: [],
    index: 0,
    index1: 0,
    show1:false,
    show2:false,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var shareCode = options.bussIdCode;
    if (shareCode == null || shareCode==''){
      shareCode = wx.getStorageSync('shareCode');
    }   
    if (shareCode == wx.getStorageSync('userMess').userId){
      shareCode='';
    }
    let that=this;
    var url = app.globalData.baseUrl + '/webapi/qztApplyTryorder/applyOrderIndex';
    var data = {
      shareCode: shareCode
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      console.log(data.applyType)
      if (data.code == '200') {
        that.setData({
          array: data.applyType
        });
        var bussinessData = data.bussinessData;
        if (bussinessData.length == 1) {
          console.log(bussinessData[0].busName);
          that.setData({
            show1: false,
            show2: true,
            serviceName: bussinessData[0].bus_name,
            serviceId: bussinessData[0].id_,
            serviceUserId: bussinessData[0].user_id
          })
        } else {
          that.setData({
            show1: true,
            show2: false,
            array1: bussinessData
          })
        }
      }
    });



  },
  bindPickerChange: function (e) {
    this.setData({
      applyType: this.data.array[e.detail.value].code,
      applyTypeName: this.data.array[e.detail.value].codeText
    })
  },
  bindPickerChange1: function (e) {
    console.log(e);
    this.setData({
      serviceName: this.data.array1[e.detail.value].bus_name,     
      serviceId: this.data.array1[e.detail.value].id_,
      serviceUserId: this.data.array1[e.detail.value].user_id
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
      
  },
  userNameInput:function(e){
    this.setData({
      storeName: e.detail.value
    })
  },
  userTelInput:function(e){
    this.setData({
      storeTel: e.detail.value
    })
  },
  applyCheck:function(){
    var mess;
    if (this.data.storeName==null || this.data.storeName==''){
      mess = '请输入申请人姓名';
    }else  if (this.data.storeTel == null || this.data.storeTel == '') {
      mess = '请输入申请人联系电话';
    } else if (this.data.applyType == null || this.data.applyType == '') {
      mess = '请选择疾病类型';
    } else if (this.data.serviceUserId == null || this.data.serviceUserId == '') {
      mess = '请选择服务站';
    }
    if(mess!=null){
      wx.showToast({
        title: mess,
        icon: 'none'
      })
      return false;
    }
    var url = app.globalData.baseUrl + '/webapi/qztApplyTryorder/applyOrderCheck';
    var data = {
      storeName: this.data.storeName,
      storeTel: this.data.storeTel,
      diseaseType: this.data.applyType,
      diseaseName: this.data.applyTypeName,
      busId: this.data.serviceUserId,
      busName: this.data.serviceName,
      referrerUserId: wx.getStorageSync('shareCode')
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      console.log(data.applyType)
      if (data.code == '200') {
        wx.showModal({
          title: '提示',
          content: '恭喜您已成功申请试用！请等待服务站与您联系',
          confirmText: '继续逛逛',
          cancelText: '查看订单',
          success(res) {
            if (res.confirm) {
              wx.navigateBack({
                delta: 1
              })
            } else if (res.cancel) {
              wx.redirectTo({
                url: '../sydd/sydd',
              })
            }
          }
        })
      }
    });
 
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
  onShareAppMessage: function () {
    return {
      title: app.globalData.shareTitle,
      path: app.globalData.shareUrl + "?shareCode=" + wx.getStorageSync('userMess').userId,
      imageUrl: app.globalData.sharePicUrl //不设置则默认为当前页面的截图
    }
  }
})