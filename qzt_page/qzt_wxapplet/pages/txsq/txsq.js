// pages/txsq/txsq.js

var util = require('../../utils/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    withdrawMoney: null,
    arrivalAmount: 0,
    serviceCharge: 0,
    cardId: "",
    payPwd: "",
    userId: "",
    usedMoney: "",
    commissionRatio: 0,
    commissionLeast: 0,
    commissionMax: 0,
    bankName: "请选择收款账号",
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

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
    // var pages = getCurrentPages();
    // var currPage = pages[pages.length - 1]; //当前页面
    // let data = currPage.data;
    this.getWithdrawPageInfo();
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
    this.getWithdrawPageInfo();
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },
  //获取提现页信息
  getWithdrawPageInfo: function(e) {
    var _this = this;
    util.httpPost(util.getWithdrawPageInfo, {}, "GET", false, true).then(res => {
      var data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        _this.setData(ret);
      } else if (data.code == '6009') { //请设置支付密码
        util.promptJumpAssign("请设置支付密码", "../szmm/szmm", "05")
      }
    });
  },
  //申请提交
  withdrawApplyfor: function(e) {
    var _this = this;
    if (_this.data.withdrawMoney == null || _this.data.withdrawMoney == "" || _this.data.withdrawMoney < 1) {
      util.showModal("您的账户余额不满10元无法提现");
    } else if (_this.data.withdrawMoney > _this.data.usedMoney) {
      util.showModal("您的账户余额不足");
    } else if (_this.data.cardId == null || _this.data.cardId == "") {
      util.showModal('请选择收款账号');
    } else if (_this.data.payPwd == null || _this.data.payPwd == "") {
      let paypwd = this.selectComponent('#paypwd'); // 页面获取自定义组件实例
      paypwd.showInputLayer(); // 通过实例调用组件事件
    } else {
      var datas = {
        withdrawMoney: _this.data.withdrawMoney,
        serviceCharge: _this.data.serviceCharge,
        cardId: _this.data.cardId,
        payPwd: _this.data.payPwd
      };
      util.httpPost(util.withdrawApplyfor, datas, "PUT", false, true).then(res => {
        var data = res.data;
        _this.setData({
          payPwd: ""
        });
        if (data.code == '200') {
          //跳转到提现记录页
          util.promptJumpAssign(data.message, "../withdrawal_record/withdrawal_record", "05");
        } else if (data.code == '6012') {
          util.showModal(data.message);
        }
      });
    }
  },
  //提现金额监听
  withdrawMoney: function(e) {
    let withdrawMoney = e.detail.value == null || e.detail.value == "" ? 0 : parseFloat(e.detail.value);
    let commissionLeast = parseFloat(this.data.commissionLeast);
    let commissionMax = parseFloat(this.data.commissionMax);
    let commissionRatio = parseFloat(this.data.commissionRatio);
    // withdrawMoney = withdrawMoney < 10 ? 10 : withdrawMoney;
    if (this.data.usedMoney > 10) {
      let serviceCharge = withdrawMoney * commissionRatio; //手续费
      serviceCharge = serviceCharge < commissionLeast ? commissionLeast : serviceCharge > commissionMax ? commissionMax : serviceCharge;
      serviceCharge = serviceCharge.toFixed(2);
      let arrivalAmount = withdrawMoney - parseFloat(serviceCharge);
      arrivalAmount = arrivalAmount.toFixed(2);
      this.setData({
        withdrawMoney: withdrawMoney,
        serviceCharge: serviceCharge,
        arrivalAmount: arrivalAmount
      });
    }
    //  else {
    //   util.showModal("您的账户余额不满10元无法提现");
    // }

  },
  //提现记录
  checkWithdrawalRecords: function() {
    wx.navigateTo({
      url: '../withdrawal_record/withdrawal_record',
    })
  },
  //选择收款账号
  selectReceivingAccount: function() {
    wx.navigateTo({
      url: '../xzskzh/xzskzh',
    })
  },
  //接组件数据
  onMyeventPaypwd: function(e) {
    var data = e.detail;
    this.setData(data);
    this.withdrawApplyfor();
  }
})