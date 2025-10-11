// pages/sqfw/sqfw.js
var common = require('../../utils/util.js');
var app = getApp();
var busId, lat, lon;

Page({

  /**
   * 页面的初始数据
   */
  data: {
    no: "",
    array: [{
      'code': '',
      'codeText': ''
    }, {
      'code': '',
      'codeText': ''
    }, {
      'code': '',
      'codeText': ''
    }],
    array1: [{
      'code1': '',
      'codeText1': ''
    }, {
      'code1': '',
      'codeText1': ''
    }, {
      'code1': '',
      'codeText1': ''
    }],
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
    busId = options.orderNo;
    this_.setData({
      no: busId
    })

    wx.request({
      //请求地址--疾病
      url: app.globalData.baseUrl + '/pubapi/app/selectDic',
      data: {
        dicType: "APPLY_TYPE"
      }, //发送给后台的数据         
      method: 'POST',
      header: {
        //设置参数内容
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      success: function(res) {
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
      //请求地址--服务站
      url: app.globalData.baseUrl + '/webapi/qztServiceOrder/check',
      data: {
        tokenId: wx.getStorageSync('tokenId'),
        userId: wx.getStorageSync('userMess').userId,
        vno: app.globalData.vno,
        cno: app.globalData.cno,
        oId: options.orderNo
      }, //发送给后台的数据         
      method: 'GET',
      header: {
        'Content-Type': 'application/json',
      },
      success: function(res) {
        // console.log(res);
        var data = res.data;
        // console.log(data.code);
        if (data.code == 200) { //固定服务站
          var dataList = [];
          dataList.push({
            "code1": data.orderNo,
            "codeText1": data.busName
          });
          this_.setData({
            array1: dataList
          })
        } else if (data.code == 201) { //选择服务站
          // console.log("=======");
          wx.getLocation({
            type: 'wgs84',
            success: (res) => {
              lat = res.latitude;
              lon = res.longitude;
              // console.log(lat + "===" + lon);
              // var speed = res.speed
              // var accuracy = res.accuracy              
            }
          })
          this_.getBusinessListPage();
        } else {
          wx.showToast({
            title: "订单操作失败",
            icon: 'none'
          })
          setTimeout(function() {
            //要延时执行的代码
            wx.switchTab({
              url: '../order/order'
            })
          }, 1000) //延迟时间 这里是1秒
        }
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
  //获取服务站列表
  getBusinessListPage: function(e) {
    var this_ = this;
    var datas = {
      pageNum: 1, //_this.data.current,
      pageSize: 100, //_this.data.size,
      busLong: lon,
      busLat: lat,
      busName: "",
      areaCode: "",
      sorts: "01"
    };
    common.httpPost(common.getBusinessListPage, datas, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        let ret = data.data.data;
        console.log(ret);
        var dataList = [];
        for (var i = 0; i < ret.length; i++) {
          dataList.push({
            "code1": ret[i].id,
            "codeText1": ret[i].busName
          });
        }
        this_.setData({
          array1: dataList
        })
      }
    });
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
      common.showModal('请选择服务站');
    } else {
      var datas = {
        tokenId: wx.getStorageSync('tokenId'),
        userId: wx.getStorageSync('userMess').userId,
        vno: app.globalData.vno,
        cno: app.globalData.cno,

        name: this.data.contactName,
        tel: this.data.contactTel,
        oId: busId,
        dis: this.data.applyTypeName,
        bId: this.data.applyType1
      };
      common.httpPost(app.globalData.baseUrl + '/webapi/qztServiceOrder/add', datas, "POST", true, true).then(res => {
        var data = res.data;
        if (data.code == '200') {
          common.promptJumpNavigateBack(data.message);
          // setTimeout(function () {
          //   //要延时执行的代码
          //   wx.navigateTo({            
          //     url: '../order/order'
          //   })
          // }, 1000) //延迟时间 这里是1秒
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
        } else {
          common.alert(data.message);
          setTimeout(function() {
            //要延时执行的代码
            wx.switchTab({
              url: '../order/order'
            })
          }, 1000) //延迟时间 这里是1秒
        }
      });
    }
  },
  contactName: function(e) { //申请人姓名
    this.setData({
      contactName: e.detail.value
    });
  },
  contactTel: function(e) { //联系电话
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