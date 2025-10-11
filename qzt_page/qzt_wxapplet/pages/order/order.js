// pages/order/order.js
const util = require('../../utils/util.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    currentIndex: 0,
    dataShow: [true, true, true, true, true],
    pageData: [{
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'orderState': '01'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'orderState': '03'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'orderState': '05'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'orderState': '09'
    }, {
      'pageNum': 0,
      'pageSize': 10,
      'isLoad': true,
      'orderState': '11'
    }],
    orderData: [
      [],
      [],
      [],
      [],
      []
    ],
    setInter: null //定时器实例
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let currentIndex = options.currentIndex;
    if (currentIndex != "" && currentIndex != undefined) {
      this.setData({
        currentIndex: currentIndex
      });
    }
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
    let allPageData = this.data.pageData;
    let nowPageDate = allPageData[this.data.currentIndex];
    nowPageDate.pageNum = 0;
    nowPageDate.isLoad = true;
    let orderData = this.data.orderData;
    orderData[this.data.currentIndex] = [];
    this.setData({
      orderData: orderData,
      pageData: allPageData
    });
    this.loadData();
  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    clearInterval(this.data.setInter);
  },
  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    clearInterval(this.data.setInter);
  },
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  // onPullDownRefresh: function() {
  //   let allPageData = this.data.pageData;
  //   let nowPageDate = allPageData[this.data.currentIndex];
  //   nowPageDate.pageNum = 0;
  //   nowPageDate.isLoad = true;
  //   let orderData = this.data.orderData;
  //   orderData[this.data.currentIndex] = [];
  //   this.setData({
  //     orderData: orderData,
  //     pageData: allPageData
  //   });
  //   this.loadData();
  // },
  /**
   * 页面上拉触底事件的处理函数 翻页使用
   */
  scrollLower: function () {
    this.loadData();
  },
  //swiper切换时会调用
  pagechange: function (e) {
    if ("touch" === e.detail.source) {
      let currentPageIndex = this.data.currentIndex
      currentPageIndex = e.detail.current
      this.setData({
        currentIndex: currentPageIndex
      });
      this.refresh1();
    }
  },
  //用户点击tab时调用
  titleClick: function (e) {
    let currentPageIndex = e.currentTarget.dataset.idx;
    this.setData({
      //拿到当前索引并动态改变
      currentIndex: e.currentTarget.dataset.idx
    });
    this.refresh1();
  },
  //获取订单数据
  loadData: function (e) {
    let _this = this;
    if (!_this.data.pageData[_this.data.currentIndex].isLoad) {
      return false;
    }
    var data = _this.data.pageData[_this.data.currentIndex];
    data.pageNum = data.pageNum + 1;
    util.httpPost(util.getGorderListPage, data, "GET", false, false, null, "1.0.1").then(res => {
      var data = res.data;
      if (data.code == '200') {
        var ret = data.data;
        var dataShowAr = [true, true, true, true, true];
        if (ret.total == 0) {
          dataShowAr[_this.data.currentIndex] = false;
          _this.setData({
            dataShow: dataShowAr
          })
        } else {
          var newOrderData = _this.data.orderData;
          if (ret.current == 1) {
            newOrderData[_this.data.currentIndex] = ret.data;
          } else {
            newOrderData[_this.data.currentIndex] = newOrderData[_this.data.currentIndex].concat(ret.data);
          }

          if (_this.data.currentIndex == 0) { //待付款订单初始化计时
            for (var item in newOrderData[_this.data.currentIndex]) {
              let order = newOrderData[_this.data.currentIndex][item];
              order.countdownTimeText = util.secondTransformation(order.timingTime);
              newOrderData[_this.data.currentIndex][item] = order;
            }
          }
          var allPageData = _this.data.pageData;
          var pageJson = allPageData[_this.data.currentIndex];
          pageJson.isLoad = true;
          if (ret.current * ret.size >= ret.total) {
            pageJson.isLoad = false;
          }
          pageJson.pageNum = ret.current;
          allPageData[_this.data.currentIndex] = pageJson;
          _this.setData({
            orderData: newOrderData,
            pageData: allPageData,
            dataShow: dataShowAr
          })
          wx.hideNavigationBarLoading();
          wx.stopPullDownRefresh();
          //订单状态为待付款触发定时器
          if (_this.data.currentIndex == 0) {
            //清除计时器  即清除setInter
            clearInterval(_this.data.setInter);
            _this.countDown();
          }
        }
      }
      wx.hideNavigationBarLoading();
      wx.stopPullDownRefresh();
    });
  },
  refresh: function (e) {
    let _this = this;
    wx.startPullDownRefresh();
    wx.showNavigationBarLoading();
    var allPageData = _this.data.pageData;
    allPageData[_this.data.currentIndex].pageNum = 0;
    allPageData[_this.data.currentIndex].isLoad = true;
    var orderData = _this.data.orderData;
    orderData[_this.data.currentIndex] = [];
    var dataShow = _this.data.dataShow;
    dataShow[_this.data.currentIndex] = true;
    _this.setData({
      orderData: orderData,
      pageData: allPageData,
      dataShow: dataShow
    });
    this.loadData();
  },
  refresh1: function (e) {
    let _this = this;
    wx.showNavigationBarLoading();
    var allPageData = _this.data.pageData;
    allPageData[_this.data.currentIndex].pageNum = 0;
    allPageData[_this.data.currentIndex].isLoad = true;
    var orderData = _this.data.orderData;
    orderData[_this.data.currentIndex] = [];
    _this.setData({
      orderData: orderData,
      pageData: allPageData
    });
    this.loadData();
  },
  //订单详情
  orderDetails: function (e) {
    var orderNo = e.currentTarget.dataset.orderno;
    wx.navigateTo({
      url: '../ddxq/ddxq?orderNo=' + orderNo
    });
  },
  //取消订单
  cancellationOrder: function (e) {
    var orderNo = e.currentTarget.dataset.orderno;
    var _this = this;
    wx.showModal({
      title: '提示',
      content: '确定要取消该订单吗？',
      success: function (sm) {
        if (sm.confirm) {
          var datas = {
            orderNo: orderNo
          };
          util.httpPost(util.cancellationOfOrder, datas, "POST", false, false).then(res => {
            var data = res.data;
            if (data.code == '200') {
              util.alert("已取消");
              _this.refresh();
              let index = e.currentTarget.dataset.index //数组下标
              let orderData = _this.data.orderData;
              let ddata = orderData[_this.data.currentIndex];
              if (ddata.length > 0) {
                ddata.splice(index, 1); //删除
                _this.setData({
                  orderData: orderData
                });

              }
            }
          });
        }
      }
    });
  },
  //去支付
  toPayFor: function (e) {
    var orderNo = e.currentTarget.dataset.orderno;
    wx.navigateTo({
      url: '../goods_order_pay/goods_order_pay?orderNo=' + orderNo,
    });
  },
  //确认收货
  confirmReceipt: function (e) {
    var orderNo = e.currentTarget.dataset.orderno;
    var datas = {
      orderNo: orderNo
    };
    util.httpPost(util.confirmReceipt, datas, "POST", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        util.alert("收货成功");
        this.refresh();
      }
    });
  },
  //查看物流
  checkTheLogistics: function (e) {
    var orderNo = e.currentTarget.dataset.orderno;
    wx.navigateTo({
      url: '../logistics_details/logistics_details?orderNo=' + orderNo
    });
  },
  //再来一单
  recurOrder: function (e) {
    var orderNo = e.currentTarget.dataset.orderno;
    var datas = {
      orderNo: orderNo
    };
    util.httpPost(util.recurOrder, datas, "POST", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        wx.switchTab({
          url: "../my_shopping_cart/my_shopping_cart"
        })
      }
    });
  },
  //申请服务
  applyForTheService: function (e) {
    var orderNo = e.currentTarget.dataset.orderno;
    wx.navigateTo({
      url: '../sqfwtwo/sqfwtwo?orderNo=' + orderNo,
    });
  },
  //计时器
  countDown: function (e) {
    var _this = this;
    let setInter = setInterval(function () {
      console.log("定时器执行");
      var orderData = _this.data.orderData;
      var orderList = orderData[0];
      if (orderList.length > 0) {
        for (var item in orderList) {
          let order = orderList[item];
          if (order.timingTime != "0") {
            let timingTime = parseInt(order.timingTime); //剩余时间S
            if (timingTime > 0) {
              order.timingTime = timingTime - 1;
              order.countdownTimeText = util.secondTransformation(timingTime);
            } else {
              //移除超时未支付的订单
              orderList.splice(item, 1);
            }
          }
        }
        orderData[0] = orderList;
        _this.setData({
          orderData: orderData
        });
      } else {
        var dataShowAr = _this.data.dataShow;
        dataShowAr[_this.data.currentIndex] = false;
        _this.setData({
          dataShowAr: dataShowAr
        });
      }
    }, 1000);
    console.log("定时器执行", setInter);
    _this.setData({
      setInter: setInter
    });
  }

})