// pages/fillin_goods_order/fillin_goods_order.js
var util = require('../../utils/util.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    couponList: [],
    addressList: [],
    addressId: "",
    consigneeTel: "",
    companyName: "",
    consigneeAddress: "",
    goodsName: "",
    goodsExplain: "",
    goodsPrice: 0,
    goodsPic: "",
    goodsId: null,
    goodsNum: 0,
    addressId: "",
    buyNum: 1,
    cashCouponId: "",
    couponStatus: "N",
    remarks: "",
    goodsFreight: 0, //运费
    totalFreight: 0, //运费总额
    orderTotalAmount: 0, //订单总额
    amountPayable: 0, //应付金额-合计
    cashCouponMoney: 50, //券金额
    fullAmountReduction: 10000, //满减金额
    cashCouponText: "请选择",
    addressStatus: true,
    addressClick: "",
    shareCode: "",
    latitude: "",
    longitude: "",
    isPickupWay: true,
    pickupWay: ['门店自提', '快递发货'],
    pickupWayIndex: 0,
    kilometer: "",
    userTel: "",
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let buyNum = options.buyNum == null || options.buyNum == "" ? 1 : options.buyNum;
    let shareCode = wx.getStorageSync("shareCode");
    shareCode = shareCode == null || shareCode == undefined || shareCode == "" ? options.shareCode : shareCode;
    // options.id = "1198532173636866050";
    this.setData({
      goodsId: options.id,
      shareCode: shareCode,
      buyNum: !buyNum || buyNum < 1 ? 1 : buyNum
    });
    this.initialFunc();
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
  //页面初始化方法
  initialFunc: function(res) {
    let _this = this;
    wx.getLocation({
      type: 'wgs84',
      success(res) {
        const latitude = res.latitude
        const longitude = res.longitude
        _this.setData({
          latitude: latitude,
          longitude: longitude
        });
        _this.queryFillinGorderInfo();
      },
      fail(res) {
        wx.getSetting({
          success: function(res) {
            var statu = res.authSetting;
            if (!statu['scope.userLocation']) {
              wx.showModal({
                title: '是否授权当前位置',
                content: '需要获取您的地理位置，请确认授权，否则将无法购买商品',
                success: function(tip) {
                  if (tip.confirm) {
                    wx.openSetting({
                      success: function(data) {
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
                              const latitude = res.latitude
                              const longitude = res.longitude
                              _this.setData({
                                latitude: latitude,
                                longitude: longitude
                              });
                              _this.queryFillinGorderInfo();
                            }
                          });
                        } else {
                          wx.showToast({
                            title: '授权失败',
                            icon: 'success',
                            duration: 1000,
                            success(res) {
                              wx.redirectTo({
                                url: "../details/details?goodsId=" + _this.data.goodsId
                              });
                            }
                          })
                        }
                      }
                    })
                  } else { //取消授权
                    wx.redirectTo({
                      url: "../details/details?goodsId=" + _this.data.goodsId
                    });
                  }
                }
              })
            }
          },
          fail: function(res) {
            wx.showToast({
              title: '调用授权窗口失败',
              icon: 'success',
              duration: 1000,
              success(res) {
                wx.redirectTo({
                  url: "../details/details?goodsId=" + _this.data.goodsId
                });
              }
            })
          }
        })
      }
    })
  },
  //获取商品填写订单页信息
  queryFillinGorderInfo: function(e) {
    let _this = this;
    const latitude = _this.data.latitude
    const longitude = _this.data.longitude
    let datas = {
      goodsId: _this.data.goodsId,
      longitudeAndLatitude: longitude + "," + latitude
    };
    util.httpPost(util.queryFillinGorderInfo, datas, "GET", false, false).then(res => {
      let data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        let goodsMess = ret.goodsMess;
        let couponList = ret.couponList; //用户可用抵扣券集合
        let addressMess = ret.addressMess;
        let addressList = addressMess.addressList; //用户收货地址集合
        let buyNum = _this.data.buyNum;
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
            complete: function(res) {
              _this.setData({
                pickupWayIndex: pickupWayIndex,
                isPickupWay: ret.isPickupWay,
                couponStatus: goodsMess.couponStatus,
                couponList: couponList,
                addressList: addressList,
                goodsName: goodsMess.goodsName,
                goodsExplain: goodsMess.goodsExplain,
                goodsPrice: goodsMess.goodsPrice,
                goodsFreight: goodsMess.goodsFreight,
                goodsPic: goodsMess.goodsPic,
                goodsNum: goodsMess.goodsNum,
                buyNum: buyNum > goodsMess.goodsNum ? goodsMess.goodsNum : buyNum,
                latitude: latitude,
                longitude: longitude,
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
            pickupWayIndex: pickupWayIndex,
            isPickupWay: ret.isPickupWay,
            couponStatus: goodsMess.couponStatus,
            couponList: couponList,
            addressList: addressList,
            goodsName: goodsMess.goodsName,
            goodsExplain: goodsMess.goodsExplain,
            goodsPrice: goodsMess.goodsPrice,
            goodsFreight: goodsMess.goodsFreight,
            goodsPic: goodsMess.goodsPic,
            goodsNum: goodsMess.goodsNum,
            buyNum: buyNum > goodsMess.goodsNum ? goodsMess.goodsNum : buyNum,
            latitude: latitude,
            longitude: longitude,
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
  handleHeaderSelectionType: function(res) {
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
  amountOfCalculation: function(res) {
    let data = {
      totalFreight: 0, //运费总额
      orderTotalAmount: 0, //订单总额
      amountPayable: 0 //应付金额-合计
    };
    let goodsPrice = parseFloat(this.data.goodsPrice); //单价
    let goodsFreight = parseFloat(this.data.goodsFreight); //运费单价
    let buyNum = parseInt(this.data.buyNum); //数量
    var totalFreightmoney = 0;
    if (goodsFreight != null && goodsFreight != "" && goodsFreight > 0) {
      totalFreightmoney = (goodsFreight * buyNum).toFixed(2);
    }
    let orderTotalAmount = (goodsPrice * buyNum).toFixed(2);
    data.orderTotalAmount = orderTotalAmount;
    data.amountPayable = parseFloat(orderTotalAmount) + parseFloat(totalFreightmoney);
    //使用券支付总额大于满减金额进行计算
    if (this.data.cashCouponId != null && this.data.cashCouponId != "" && data.amountPayable > this.data.fullAmountReduction) {
      data.amountPayable = data.amountPayable - parseFloat(this.data.cashCouponMoney);
    }
    data.amountPayable = data.amountPayable.toFixed(2);
    data.totalFreight = totalFreightmoney == 0 ? "包邮" : "¥" + totalFreightmoney;
    this.setData(data);
  },
  //加减购买数量处理
  addAndSubtract: function(res) {
    let type = res.currentTarget.dataset.type;
    let buyNum = parseInt(this.data.buyNum);
    if (type == "+") {
      buyNum += 1;
    } else if (type == "-") {
      buyNum -= 1;
    }
    if (buyNum > 0 && buyNum <= this.data.goodsNum) {
      this.setData({
        buyNum: buyNum
      });
      this.amountOfCalculation();
    }
  },
  //提交订单
  subGoodsOrder: function(res) {
    var _this = this;
    if (_this.data.addressId == null || _this.data.addressId == "") {
      util.showModal(_this.data.pickupWayIndex == 0 ? "请选择服务站" : '请选择收货地址');
    } else if (_this.data.userTel == null || _this.data.userTel == "") {
      util.showModal("请输入联系电话");
    } else {
      var datas = {
        addressId: this.data.addressId,
        pickupWay: this.data.pickupWayIndex,
        goodsId: this.data.goodsId,
        buyNum: this.data.buyNum,
        cashCouponId: this.data.cashCouponId,
        shareCode: this.data.shareCode,
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
  //选择优惠券
  selectCoupon: function(res) {
    let cashcoupon = this.selectComponent('#cashcoupon'); // 页面获取自定义组件实例
    cashcoupon.showModal(); // 通过实例调用组件事件
  },
  //接收券组件数据
  onMyeventCoupon: function(e) {
    var data = e.detail;
    this.setData(data);
    this.amountOfCalculation();
  },
  //监听取货方式方法
  pickupWayBindPickerChange: function(e) {
    this.setData({
      pickupWayIndex: e.detail.value
    });
    this.handleHeaderSelectionType("1");
  },
  //选择服务站
  selectBusiness: function(e) {
    wx.navigateTo({
      url: '../select_business/select_business?kilometer=' + (this.data.isPickupWay ? "" : this.data.kilometer) + "&latitude=" + this.data.latitude + "&longitude=" + this.data.longitude,
    });
  },
  //联系电话输入监听
  luserTel: function(e) {
    this.setData({
      userTel: e.detail.value
    });
  }


});