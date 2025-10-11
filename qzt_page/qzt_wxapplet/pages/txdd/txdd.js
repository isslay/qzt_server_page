// pages/txdd/txdd.js
var common = require('../../utils/util.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    orderNo: null,
    goodsName: "",
    goodsExplain: "",
    goodsPrice: "",
    goodsPic: "",
    balanceMoney: "",
    orderMoney: "",
    threeSidesMoney: "",
    consigneeTel: "",
    companyName: "",
    addressId: "",
    consigneeAddress: "",
    usedMoney: "",
    payType: "w",
    balanceMoney: 0,
    payPwd: "",
    addressStatus: true,
    addressClick: "selectAddress",
    addressList: []
  },
  onLoad: function(options) {
    this.setData({
      orderNo: options.orderNo
    });
    this.getPayApplay0rderInfo();
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
  //获取订单支付信息
  getPayApplay0rderInfo: function(e) {
    var _this = this;
    var datas = {
      orderNo: _this.data.orderNo
    };
    common.httpPost(common.getPayApplay0rderInfo, datas, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var ret = data.data;
        var goodsMess = ret.goodsMess;
        var orderMess = ret.orderMess;
        var addressMess = ret.addressMess;
        var userMess = ret.userMess;
        let addressList = addressMess.addressList; //用户收货地址集合
        let consigneeAddress = addressMess.consigneeAddress;

        let addressStatus = true; //是否显示地址
        let addressClick = "selectAddress";
        if (addressMess.id == null || addressMess.id == "") {
          addressStatus = false;
          if (addressList != null && addressList.length > 0) {
            consigneeAddress = "请选择收货地址";
          } else {
            consigneeAddress = "您还未添加过收货地址，请添加";
            addressClick = "addAddress";
          }
        }

        var datas = {
          consigneeAddress: consigneeAddress,
          addressStatus: addressStatus,
          addressClick: addressClick,
          addressList: addressList,
          addressStatus: addressStatus,
          goodsName: goodsMess.goodsName,
          goodsExplain: goodsMess.goodsExplain,
          goodsPrice: goodsMess.goodsPrice,
          goodsPic: goodsMess.goodsPic,
          balanceMoney: orderMess.balanceMoney,
          orderMoney: orderMess.orderMoney,
          threeSidesMoney: orderMess.threeSidesMoney,
          consigneeTel: addressMess.consigneeTel,
          companyName: addressMess.companyName,
          addressId: addressMess.addressId,
         
          usedMoney: userMess.usedMoney
        }
        _this.setData(datas);
        let saddress = this.selectComponent('#saddress'); // 页面获取自定义组件实例
        saddress.receivesTheValue(addressList); //向组件传递数据
      }
    });
  },
  //立即支付
  immediatePayment: function(e) {
    var _this = this;
    if (_this.data.addressId == null || _this.data.addressId == "") {
      common.showModal('请选择收货地址');
    } else if (_this.data.balanceMoney > 0 && (_this.data.payPwd == "" || _this.data.payPwd == null)) {
      common.showModal('请输入支付密码');
    } else {
      var datas = {
        orderNo: this.data.orderNo,
        addressId: this.data.addressId,
        payType: this.data.payType,
        balanceMoney: this.data.balanceMoney,
        payPwd: this.data.payPwd
      };
      common.httpPost(common.payApplay0rder, datas, "POST", false, false).then(res => {
        var data = res.data;
        if (data.code == '200') {
          var ret = data.data;
          wx.redirectTo({
            url: "../pay_page/pay_page?orderType=" + ret.orderType + "&orderNo=" + ret.orderNo + "&payMoney=" + ret.threeSidesMoney
          });
        }
      });
    }
  },
  //添加收货地址
  addAddress: function(res) {
    console.log("添加收货地址");
    wx.navigateTo({
      url: '../xzshdz/xzshdz?isOrder=Gorder',
    })
  },
  //选择地址
  selectAddress: function(res) {
    let saddress = this.selectComponent('#saddress'); // 页面获取自定义组件实例
    saddress.showModal(); // 通过实例调用组件事件
  },
  //接收地址组件数据
  onMyeventAddress: function(e) {
    var data = e.detail;
    this.setData(data);
  },


});