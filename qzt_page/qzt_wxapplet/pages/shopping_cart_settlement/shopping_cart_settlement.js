// pages/shopping_cart_settlement/shopping_cart_settlement.js
var util = require('../../utils/util.js');
let latitude = "",
  longitude = "",
  shareCode = "";

Page({
  /**
   * 页面的初始数据
   */
  data: {
    stoGoodsList: [],
    couponList: [],
    addressList: [],
    addressId: "",
    consigneeTel: "",
    companyName: "",
    consigneeAddress: "",
    addressId: "",
    cashCouponId: "",
    couponStatus: "N",
    remarks: "",
    grossFreight: 0, //运费总额
    totalAmountGoods: 0, //商品总额
    amountInTotal: 0, //应付金额-合计
    cashCouponMoney: 50, //券金额
    fullAmountReduction: 10000, //满减金额
    cashCouponText: "请选择",
    addressStatus: true,
    addressClick: "",
    isPickupWay: true,
    pickupWay: ['门店自提', '快递发货'],
    pickupWayIndex: 0,
    kilometer: "",
    userTel: "",
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    shareCode = wx.getStorageSync("shareCode");
    shareCode = shareCode == null || shareCode == undefined || shareCode == "undefined" ? "" : shareCode;
    this.initialFunc();
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
  //页面初始化方法
  initialFunc: function (res) {
    let _this = this;
    wx.getLocation({
      type: 'wgs84',
      success(res) {
        latitude = res.latitude
        longitude = res.longitude
        _this.getSettlementPageInfo();
      },
      fail(res) {
        wx.getSetting({
          success: function (res) {
            var statu = res.authSetting;
            if (!statu['scope.userLocation']) {
              wx.showModal({
                title: '是否授权当前位置',
                content: '需要获取您的地理位置，请确认授权，否则将无法购买商品',
                success: function (tip) {
                  if (tip.confirm) {
                    wx.openSetting({
                      success: function (data) {
                        if (data.authSetting["scope.userLocation"] === true) {
                          wx.showToast({
                            title: '授权成功',
                            icon: 'success',
                            duration: 1000
                          })
                          //授权成功之后，再调用chooseLocation选择地方
                          wx.getLocation({
                            type: 'wgs84',
                            success(res) {
                              latitude = res.latitude
                              longitude = res.longitude
                              _this.getSettlementPageInfo();
                            }
                          });
                        } else {
                          wx.showToast({
                            title: '授权失败',
                            icon: 'success',
                            duration: 1000,
                            success(res) {
                              wx.switchTab({
                                url: "../my_shopping_cart/my_shopping_cart"
                              })
                            }
                          })
                        }
                      }
                    })
                  } else { //取消授权
                    wx.switchTab({
                      url: "../my_shopping_cart/my_shopping_cart"
                    })
                  }
                }
              })
            }
          },
          fail: function (res) {
            wx.showToast({
              title: '调用授权窗口失败',
              icon: 'success',
              duration: 1000,
              success(res) {
                wx.switchTab({
                  url: "../my_shopping_cart/my_shopping_cart"
                })
              }
            })
          }
        })
      }
    })
  },
  //获取商品填写订单页-结算信息
  getSettlementPageInfo: function (e) {
    let _this = this;
    let datas = {
      longitudeAndLatitude: longitude + "," + latitude
    };
    util.httpPost(util.getSettlementPageInfo, datas, "GET", false, false).then(res => {
      let data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        let couponList = ret.couponList; //用户可用抵扣券集合
        let addressMess = ret.addressMess;
        let addressList = addressMess.addressList; //用户收货地址集合
        let pickupWayIndex = 0;
        if (ret.isPickupWay) {
          wx.showModal({
            title: '提示',
            content: '请选择取货方式',
            cancelText: "门店自提",
            confirmText: "快递发货",
            success(res) {
              if (res.confirm) {
                pickupWayIndex = 1;
              } else if (res.cancel) {
                pickupWayIndex = 0;
              }
            },
            complete: function (res) {
              _this.setData({
                stoGoodsList: ret.stoGoodsList,
                totalAmountGoods: ret.totalAmountGoods,
                grossFreight: ret.grossFreight,
                amountInTotal: ret.amountInTotal,
                pickupWayIndex: pickupWayIndex,
                isPickupWay: ret.isPickupWay,
                couponStatus: ret.couponStatus,
                couponList: couponList,
                addressList: addressList,
                kilometer: ret.kilometer,
                userTel: ret.userTel
              });
              _this.handleHeaderSelectionType();
              let saddress = _this.selectComponent('#saddress'); // 页面获取自定义组件实例
              saddress.receivesTheValue(addressList); //向组件传递数据
              let cashcoupon = _this.selectComponent('#cashcoupon'); // 页面获取自定义组件实例
              cashcoupon.receivesTheValue(couponList); //向组件传递数据
              _this.amountOfCalculation();
            }
          });
        } else {
          _this.setData({
            stoGoodsList: ret.stoGoodsList,
            totalAmountGoods: ret.totalAmountGoods,
            grossFreight: ret.grossFreight,
            amountInTotal: ret.amountInTotal,
            pickupWayIndex: pickupWayIndex,
            isPickupWay: ret.isPickupWay,
            couponStatus: ret.couponStatus,
            couponList: couponList,
            addressList: addressList,
            kilometer: ret.kilometer,
            userTel: ret.userTel
          });
          _this.handleHeaderSelectionType();
          let saddress = _this.selectComponent('#saddress'); // 页面获取自定义组件实例
          saddress.receivesTheValue(addressList); //向组件传递数据
          let cashcoupon = _this.selectComponent('#cashcoupon'); // 页面获取自定义组件实例
          cashcoupon.receivesTheValue(couponList); //向组件传递数据
          _this.amountOfCalculation();
        }
      } else if (data.code == '6005') {
        util.promptJumpNavigateBack(data.message);
      }
    });
  },
  //处理头部选择类型
  handleHeaderSelectionType: function (res) {
    let pickupWayIndex = this.data.pickupWayIndex;
    let addressList = this.data.addressList;
    let consigneeAddress = this.data.consigneeAddress;
    let addressClick = "";
    if (pickupWayIndex == 0) { //是否可切换取货方式 默认自提 
      addressClick = "selectBusiness";
      consigneeAddress = "请选择服务站自提";
    } else if (pickupWayIndex == 1) {
      if (addressList != null && addressList.length > 0) {
        consigneeAddress = "请选择收货地址";
        addressClick = "selectAddress";
      } else {
        consigneeAddress = "您还未添加过收货地址，请添加";
        addressClick = "addAddress";
      }
    }
    this.setData({
      consigneeAddress: consigneeAddress,
      addressStatus: false,
      addressClick: addressClick,
      addressId: "",
      consigneeTel: "",
      companyName: ""
    });
  },
  //计算价格
  amountOfCalculation: function (res) {
    let totalAmountGoods = parseFloat(this.data.totalAmountGoods); //商品总额
    let amountInTotal = parseFloat(this.data.amountInTotal); //应付金额-合计
    //使用券支付总额大于满减金额进行计算
    if (this.data.cashCouponId != null && this.data.cashCouponId != "" && totalAmountGoods > this.data.fullAmountReduction) {
      amountInTotal = amountInTotal - parseFloat(this.data.cashCouponMoney);
    }
    amountInTotal = amountInTotal.toFixed(2);
    this.setData({
      amountInTotal: amountInTotal
    });
  },
  //提交订单
  subGoodsOrder: function (res) {
    var _this = this;
    if (_this.data.addressId == null || _this.data.addressId == "") {
      util.showModal(_this.data.pickupWayIndex == 0 ? "请选择服务站" : '请选择收货地址');
    } else if (_this.data.userTel == null || _this.data.userTel == "") {
      util.showModal("请输入联系电话");
    } else {
      var datas = {
        addressId: this.data.addressId,
        pickupWay: this.data.pickupWayIndex,
        cashCouponId: this.data.cashCouponId,
        shareCode: shareCode,
        userTel: this.data.userTel,
        remarks: this.data.remarks
      };
      util.httpPost(util.subGorder, datas, "PUT", false, false).then(res => {
        var data = res.data;
        if (data.code == '200') {
          wx.redirectTo({
            url: '../goods_order_pay/goods_order_pay?orderNo=' + data.data
          });
        }
      });
    }
  },
  //添加收货地址
  addAddress: function (res) {
    console.log("添加收货地址");
    wx.navigateTo({
      url: '../xzshdz/xzshdz?isOrder=Gorder',
    })
  },
  //选择地址
  selectAddress: function (res) {
    let saddress = this.selectComponent('#saddress'); // 页面获取自定义组件实例
    saddress.showModal(); // 通过实例调用组件事件
  },
  //接收地址组件数据
  onMyeventAddress: function (e) {
    var data = e.detail;
    this.setData(data);
  },
  //选择优惠券
  selectCoupon: function (res) {
    let cashcoupon = this.selectComponent('#cashcoupon'); // 页面获取自定义组件实例
    cashcoupon.showModal(); // 通过实例调用组件事件
  },
  //接收券组件数据
  onMyeventCoupon: function (e) {
    var data = e.detail;
    this.setData(data);
    this.amountOfCalculation();
  },
  //监听取货方式方法
  pickupWayBindPickerChange: function (e) {
    this.setData({
      pickupWayIndex: e.detail.value
    });
    this.handleHeaderSelectionType("1");
  },
  //选择服务站
  selectBusiness: function (e) {
    wx.navigateTo({
      url: '../select_business/select_business?kilometer=' + (this.data.isPickupWay ? "" : this.data.kilometer) + "&latitude=" + latitude + "&longitude=" + longitude,
    });
  },
  //联系电话输入监听
  luserTel: function (e) {
    this.setData({
      userTel: e.detail.value
    });
  }


});