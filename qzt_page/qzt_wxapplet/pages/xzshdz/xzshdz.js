// pages/xzshdz/xzshdz.js
var common = require('../../utils/util.js');
var methods = "PUT";
var urls = common.addAddress;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    id: null,
    recipientName: "",
    phone: "",
    areaCode: "",
    detailAddress: "",
    zipCode: "",
    isDefault: "N",
    isOrder: "",
    proCityArea: "" //地区选择结果name
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var id = options.id;
    var titles = "新增收货地址";
    if (id != "" && id != null && id != undefined) {
      titles = "编辑收货地址";
      this.setData({
        id: id
      });
      this.selectAddressById();
      //初始化地址组件的默认显示区域
      methods = "POST";
      urls = common.updateAddress;
    } else {
      let region = this.selectComponent('#region'); // 页面获取自定义组件实例
      region.receivesTheValue(this.data.areaCode);
      this.setData({
        id: "",
        isOrder: options.isOrder
      });
    }
    //修改title名称
    wx.setNavigationBarTitle({
      title: titles
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
  //获取收货地址详情
  selectAddressById: function(e) {
    var _this = this;
    var datas = {
      id: _this.data.id
    };
    common.httpPost(common.selectAddressById, datas, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var ret = data.data;
        _this.setData({
          recipientName: ret.recipientName,
          phone: ret.phone,
          areaCode: ret.areaCode,
          proCityArea: ret.proCityArea,
          detailAddress: ret.detailAddress,
          zipCode: ret.zipCode,
          isDefault: ret.isDefault
        });
        let region = _this.selectComponent('#region'); // 页面获取自定义组件实例
        region.receivesTheValue(this.data.areaCode);
      }
    });
  },
  //添加/保存
  saveAddress: function(e) {
    var _this = this;
    if (_this.data.recipientName == null || _this.data.recipientName == "") {
      common.showModal('请输入姓名');
    } else if (!/^1(3|4|5|6|7|8|9)\d{9}$/.test(_this.data.phone)) {
      common.showModal('请输入正确的手机号码');
    } else if (_this.data.areaCode == null || _this.data.areaCode.length != 12) {
      common.showModal('请选择所在区域');
    } else if (_this.data.detailAddress == null || _this.data.detailAddress == "") {
      common.showModal('请输入详细地址');
    } else if (_this.data.zipCode == null || _this.data.zipCode == "") {
      common.showModal('请输入地区邮编');
    } else {
      var datas = {
        recipientName: this.data.recipientName,
        phone: this.data.phone,
        areaCode: this.data.areaCode,
        detailAddress: this.data.detailAddress,
        zipCode: this.data.zipCode,
        isDefault: this.data.isDefault,
        pkId: this.data.id
      };
      common.httpPost(urls, datas, methods, false, false).then(res => {
        var data = res.data;
        if (data.code == '200') {
          if (_this.data.isOrder == "Gorder") {
            let ret = data.data;
            var pages = getCurrentPages(); //获取当前页面实例
            var prevPage = pages[pages.length - 2]; //获取上一页面实例     减几对应几页的实例
            prevPage.setData({ //直接给上一个页面赋值   prevPage指定页实例 使用.操作
              addressId: ret.id,
              companyName: ret.recipientName,
              consigneeTel: ret.phone,
              consigneeAddress: ret.address,
              addressStatus: true,
              addressClick: ""
            });
          }
          common.promptJumpNavigateBack(data.message);
        }
      });
    }
  },
  recipientName: function(e) { //姓名
    this.setData({
      recipientName: e.detail.value
    });
  },
  phone: function(e) { //手机号码
    this.setData({
      phone: e.detail.value
    });
  },
  detailAddress: function(e) { //详细地址
    this.setData({
      detailAddress: e.detail.value
    });
  },
  zipCode: function(e) { //地区邮编
    this.setData({
      zipCode: e.detail.value
    });
  },
  isDefault: function(e) { //是否默认地址
    this.setData({
      isDefault: this.data.isDefault == "Y" ? "N" : "Y"
    });
  },
  //接收组件数据
  onMyevent: function(e) {
    var data = e.detail;
    this.setData(data);
  }

});