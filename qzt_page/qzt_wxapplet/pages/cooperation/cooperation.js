// pages/cooperation/cooperation.js
var common = require('../../utils/util.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    disabled: true,
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
    let shareCode = wx.getStorageSync("shareCode");
    this.setData({
      referrerUserId: shareCode
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
    //初始化地址组件的默认显示区域
    if (typeof this.getTabBar === 'function' &&
      this.getTabBar()) {
      this.getTabBar().setData({
        selected: 3
      })
    }
    this.getApplay0rderInfo();
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
  //获取申请服务站订单信息
  getApplay0rderInfo: function(e) {
    var _this = this;
    var datas = {};
    common.httpPost(common.getApplay0rderInfo, datas, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var ret = data.data;
        let applyMess = ret.applyMess;
        let borderMess = ret.borderMess;
        let disabled = true;
        let datas = {};
        if (borderMess.orderState == "01" || borderMess.orderState == "") {
          disabled = false;
        }
        if (borderMess.orderState != "") {
          datas = {
            disabled: disabled,
            contactName: applyMess.contactName,
            contactTel: applyMess.contactTel,
            context: applyMess.context,
            proCityArea: applyMess.proCityArea,
            stateMark: applyMess.stateMark,
            referrerUserId: applyMess.referrerUserId
          };
        }else{
          datas = {
            disabled: disabled
          };
        }
        _this.setData(datas);
        if (borderMess.orderState == "01" || borderMess.orderState == "") {
          let region = this.selectComponent('#region'); // 页面获取自定义组件实例
          region.receivesTheValue(applyMess.context);
        }
      }
    });
  },
  //提交申请
  subApplyfor: function(e) {
    var _this = this;
    if (_this.data.contactName == null || _this.data.contactName == "") {
      common.showModal('请输入申请人姓名');
    } else if (!/^1(3|4|5|6|7|8|9)\d{9}$/.test(_this.data.contactTel)) {
      common.showModal('请输入正确的联系电话');
    } else if (_this.data.context == null || _this.data.context.length != 12) {
      common.showModal('请选择所在区域');
    } else if (_this.data.stateMark == null || _this.data.stateMark == "") {
      common.showModal('请输入详细地址');
    } else {
      var datas = {
        contactName: this.data.contactName,
        contactTel: this.data.contactTel,
        context: this.data.context,
        stateMark: this.data.stateMark,
        referrerUserId: this.data.referrerUserId
      };
      common.httpPost(common.applySub0rder, datas, "POST", false, true).then(res => {
        var data = res.data;
        if (data.code == '200') {
          common.alert(data.message);
          wx.navigateTo({
            url: '../txdd/txdd?orderNo=' + data.data,
          })
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
  stateMark: function(e) { //详细地址
    this.setData({
      stateMark: e.detail.value
    });
  },
  referrerUserId: function(e) { //推荐人
    this.setData({
      referrerUserId: e.detail.value
    });
  },
  //接收组件数据
  onMyevent: function(e) {
    var data = e.detail;
    var data = {
      context: data.areaCode,
      proCityArea: data.proCityArea
    };
    this.setData(data);
  }

});