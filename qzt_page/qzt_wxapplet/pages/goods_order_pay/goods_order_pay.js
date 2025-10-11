// pages/goods_order_pay/goods_order_pay.js
var util = require('../../utils/util.js');

var region;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    stoGoodsList: [],

    goodsName: "",
    goodsExplain: "",
    goodsPrice: 0,
    goodsPic: "",
    goodsId: null,

    orderNo: "",
    actuaPayment: 0,
    totalPriceMc: "",
    balanceMoney: 0,
    cashCouponMoney: 0,
    threeSidesMoney: 0,
    payType: "w",
    usedMoney: 0,
    isContinuePay: "",
    usedMoneyChecked: true,
    payPwd: ""
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      orderNo: options.orderNo
    });

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
    this.getPay0rderInfo();
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
  //获取订单支付页信息
  getPay0rderInfo: function (e) {
    let _this = this;
    let datas = {
      orderNo: _this.data.orderNo
    };
    util.httpPost(util.getPay0rderInfo, datas, "GET", false, true, null, "1.0.1").then(res => {
      let data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        let userAssets = ret.userAssets;
        if (userAssets.isPayPwd == "N") { //设置支付密码
          util.promptJumpAssign("你还未设置支付密码,请设置", "../szmm/szmm", "05")
        }
        let goodsMess = ret.goodsMess;
        let orderMess = ret.orderMess;
        let payMess = ret.payMess;
        let isContinuePay = ret.isContinuePay;

        _this.setData({
          stoGoodsList: ret.stoGoodsList,
          actuaPayment: orderMess.actuaPayment,
          totalPriceMc: orderMess.totalPriceMc,
          balanceMoney: payMess.balanceMoney,
          cashCouponMoney: payMess.cashCouponMoney,
          threeSidesMoney: payMess.threeSidesMoney,
          payType: payMess.payType,
          usedMoney: isContinuePay == "Y" ? 0 : userAssets.usedMoney,
          isContinuePay: isContinuePay
        });
        if (isContinuePay == "N") { //第一次支付才计算
          this.amountOfCalculation();
        }
      } else if (data.code == '6017') {
        util.promptJumpNavigateBack(data.message);
      } else if (data.code == '6018') {
        util.promptJumpAssign(data.message, "../order/order?currentIndex=1", "03")
      }
    });
  },
  //计算价格
  amountOfCalculation: function (res) {
    let threeSidesMoney = parseFloat(this.data.threeSidesMoney);
    let usedMoney = parseFloat(this.data.usedMoney);
    let balanceMoney = parseFloat(this.data.balanceMoney);
    let payType = "w";
    if (this.data.usedMoneyChecked) { //开启余额使用
      if (threeSidesMoney >= usedMoney) {
        balanceMoney = usedMoney;
      } else if (threeSidesMoney < usedMoney) {
        balanceMoney = threeSidesMoney;
        payType = "";
      }
      threeSidesMoney -= balanceMoney;
    } else {
      threeSidesMoney += balanceMoney;
      balanceMoney = 0;
    }
    this.setData({
      threeSidesMoney: threeSidesMoney.toFixed(2),
      balanceMoney: balanceMoney.toFixed(2),
      payType: payType
    });
  },
  //余额开关
  usedMoneySwitch: function (res) {
    this.setData({
      usedMoneyChecked: res.detail.value
    });
    this.amountOfCalculation();
  },
  payType: function (res) {
    this.setData({
      payType: this.data.payType == 'w' ? "" : "w"
    });
  },
  //立即支付
  pay0rder: function (res) {
    var _this = this;
    if (_this.data.threeSidesMoney > 0 && _this.data.payType != "w") {
      util.showModal('请选择支付方式');
    } else if (_this.data.balanceMoney > 0 && (_this.data.payPwd == "" || _this.data.payPwd == null)) {
      let paypwd = this.selectComponent('#paypwd'); // 页面获取自定义组件实例
      paypwd.showInputLayer(); // 通过实例调用组件事件
    } else {
      var datas = {
        orderNo: this.data.orderNo,
        payType: this.data.payType,
        balanceMoney: this.data.balanceMoney,
        payPwd: this.data.payPwd
      };
      util.httpPost(util.pay0rder, datas, "POST", false, true).then(res => {
        var data = res.data;
        if (data.code == '200') {
          var ret = data.data;
          if (ret.payType == 'w') {
            wx.redirectTo({
              url: "../pay_page/pay_page?orderType=" + ret.orderType + "&orderNo=" + ret.orderNo + "&payMoney=" + ret.threeSidesMoney
            });
          } else if (ret.payType == 'z') {
            console.log("暂不支持支付宝");
          } else {
            wx.redirectTo({
              url: "../ddxq/ddxq?orderNo=" + ret.orderNo
              // url: "../order/order?currentIndex=1"
            });
          }
        } else if (data.code == '6009') { //请设置支付密码
          util.promptJumpAssign("请设置支付密码", "../szmm/szmm", "05")
        } else if (data.code == '6012') { //支付密码错误
          util.showModal(data.message);
          this.setData({
            payPwd: ""
          });
        }
      });
    }
  }, //选择优惠券
  selectCoupon: function (res) {
    let paypwd = this.selectComponent('#paypwd'); // 页面获取自定义组件实例
    paypwd.showInputLayer(); // 通过实例调用组件事件
  },
  //接组件数据
  onMyeventPaypwd: function (e) {
    var data = e.detail;
    this.setData(data);
    this.pay0rder();
  }

});