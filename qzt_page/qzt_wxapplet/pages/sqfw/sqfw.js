// pages/sqfw/sqfw.js
var common = require('../../utils/util.js');
var app = getApp();
var busId;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    name:"",
    array: [{ 'code': '', 'codeText': '' }, { 'code': '', 'codeText': '' }, { 'code': '', 'codeText': '' }],
    array1: [{ 'code1': '', 'codeText1': '' }, { 'code1': '', 'codeText1': '' }, { 'code1': '', 'codeText1': '' }],
    index: 0,
    index1: 0,
    contactName: "",
    contactTel: "",
    context: "",
    stateMark: "",
    referrerUserId: "",
    proCityArea: "" //地区选择结果name
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let this_ = this;
    busId = options.id;
    wx.request({
      //请求地址--疾病
      url: app.globalData.baseUrl + '/pubapi/app/selectDic',
      data: {
        dicType: "APPLY_TYPE"
      },//发送给后台的数据         
      method: 'POST',
      header: {
        //设置参数内容
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      success: function (res) {
        var data = res.data.data.APPLY_TYPE;
        var dataList = [];
        for (var i = 0; i < data.length; i++) {
          dataList.push({
            "code": data[i].code,
            "codeText": data[i].codeText
          });
        }
        this_.setData({
          array: dataList,
          name: options.name
        })
      },
    })

    wx.request({
      //请求地址--订单
      url: app.globalData.baseUrl + '/webapi/qztGorder/getCanServiceOrderList',
      data: {
        tokenId: wx.getStorageSync('tokenId'),
        userId: wx.getStorageSync('userMess').userId,
        vno: app.globalData.vno,
        cno: app.globalData.cno,
        businessId: options.id
      },//发送给后台的数据         
      method: 'GET',
      header: {
        'Content-Type': 'application/json',
      },
      success: function (res) {
        var data = res.data.data;
        var dataList = [];
        for (var i = 0; i < data.length; i++) {
          dataList.push({
            "code1": data[i].orderNo,
            "codeText1": data[i].orderNo + " " + data[i].goodsName + " X" + data[i].buyNum
          });
        }
        this_.setData({
          array1: dataList
        })
      },
    })
  },
  bindPickerChange: function(e) {
    this.setData({
      applyType: this.data.array[e.detail.value].code,
      applyTypeName: this.data.array[e.detail.value].codeText
    })
  },
  bindPickerChange1: function(e) {
    this.setData({
      applyType1: this.data.array1[e.detail.value].code1,
      applyTypeName1: this.data.array1[e.detail.value].codeText1
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

  },

  //提交申请
  subApplyfor: function(e) {
    var _this = this;
    if (_this.data.contactName == null || _this.data.contactName == "") {
      common.showModal('请输入申请人姓名');
    } else if (!/^1(3|4|5|6|7|8|9)\d{9}$/.test(_this.data.contactTel)) {
      common.showModal('请输入正确的联系电话');
    } else if (_this.data.applyTypeName == null || _this.data.applyTypeName == "") {
      common.showModal('请选择疾病类型');
    } else if (_this.data.applyTypeName1 == null || _this.data.applyTypeName1 == "") {
      common.showModal('请选择订单');
    } else {
      var datas = {
        tokenId: wx.getStorageSync('tokenId'),
        userId: wx.getStorageSync('userMess').userId,
        vno: app.globalData.vno,
        cno: app.globalData.cno,

        name: this.data.contactName,
        tel: this.data.contactTel,
        oId: this.data.applyType1,
        dis: this.data.applyTypeName,
        bId: busId
      };
      common.httpPost(app.globalData.baseUrl + '/webapi/qztServiceOrder/add', datas, "POST", true, true).then(res => {
        var data = res.data;
        if (data.code == '200') {
          common.alert(data.message);          
          setTimeout(function () {
            //要延时执行的代码
            wx.switchTab({
              url: '../servicestation/servicestation'
            })
          }, 1000) //延迟时间 这里是1秒
        } else if (data.code == "6022") {
          wx.showModal({
            title: '提示',
            content: data.message,
            success: function(sm) {
              if (sm.confirm) {
                _this.setData({
                  referrerUserId: ""
                });
                _this.subApplyfor();
              }
            }
          });
        }
      });
    }
  },
  contactName: function (e) { //申请人姓名
    this.setData({
      contactName: e.detail.value
    });
  },
  contactTel: function (e) { //联系电话
    this.setData({
      contactTel: e.detail.value
    });
  },
  
  /**
   * 用户点击右上角分享
   */
  // onShareAppMessage: function () {

  // }
})