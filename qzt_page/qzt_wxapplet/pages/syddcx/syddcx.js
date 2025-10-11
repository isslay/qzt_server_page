var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据pageNum: 1,
        pageSize: 10,
   */
  data: {
    currentIndex: 0,
    orderStateAr: ['01', '03', '05', '07', '90'],
    dataShow: [true, true, true, true, true],
    pageData: [{ 'pageNum': 0, 'pageSize': 10, 'isLoad': true, 'orderNo': '' }, { 'pageNum': 0, 'pageSize': 10, 'isLoad': true, 'orderNo': '' }, { 'pageNum': 0, 'pageSize': 10, 'isLoad': true, 'orderNo': '' }, { 'pageNum': 0, 'pageSize': 10, 'isLoad': true, 'orderNo': '' }, { 'pageNum': 0, 'pageSize': 10, 'isLoad': true, 'orderNo': '' }],
    orderData0: [],
    orderData1: [],
    orderData2: [],
    orderData3: [],
    orderData4: [],
    orderNoAr: ['', '', '', ''],
    isfresh: false

  },
  //swiper切换时会调用
  pagechange: function (e) {
    if ("touch" === e.detail.source) {
      let currentPageIndex = this.data.currentIndex
      currentPageIndex = e.detail.current
      this.setData({
        currentIndex: currentPageIndex
      })
      this.startPullFash();
    }
  },
  //用户点击tab时调用
  titleClick: function (e) {
    let currentPageIndex =
      this.setData({
        //拿到当前索引并动态改变
        currentIndex: e.currentTarget.dataset.idx
      });
    this.startPullFash();

  },
  qrdd: function (e) {
    this.setData({
      isfresh: true
    })
    wx.navigateTo({
      url: '../qrdd/qrdd?orderId=' + e.target.dataset.orderid
    })
  },
  wc: function (e) {
    this.setData({
      isfresh: true
    })
    wx.navigateTo({
      url: '../syywc/syywc?orderId=' + e.target.dataset.orderid,
    })
  },
  go: function (e) {
    console.log(e);
    wx.navigateTo({
      url: '../syddxq/syddxq?orderId=' + e.currentTarget.dataset.orderid,
    })

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.onloadData();
  },
  onloadData: function () {
    var that = this;
    if (!that.data.pageData[that.data.currentIndex].isLoad) {
      return false;
    }

    var url = app.globalData.baseUrl + '/webapi/qztApplyTryorder/getServiceListPage';
    var data = {
      tokenId: wx.getStorageSync('tokenId'),
      userId: wx.getStorageSync('userMess').userId,
      pageNum: that.data.pageData[that.data.currentIndex].pageNum + 1,
      pageSize: that.data.pageData[that.data.currentIndex].pageSize,
      vno: app.globalData.vno,
      cno: app.globalData.cno,
      state: that.data.orderStateAr[that.data.currentIndex],
      orderNo: that.data.pageData[that.data.currentIndex].orderNo
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {

        if (that.data.currentIndex == 0) {
          if (data.data.pageData.current == 1) {
            that.setData({
              orderData0: []
            })
          }
          that.setData({
            orderData0: that.data.orderData0.concat(data.data.pageData.data)
          })

        }
        if (that.data.currentIndex == 1) {
          if (data.data.pageData.current == 1) {
            that.setData({
              orderData1: []
            })
          }
          that.setData({
            orderData1: that.data.orderData1.concat(data.data.pageData.data)
          })
        }
        if (that.data.currentIndex == 2) {
          if (data.data.pageData.current == 1) {
            that.setData({
              orderData2: []
            })
          }
          that.setData({
            orderData2: that.data.orderData2.concat(data.data.pageData.data)
          })
        }
        if (that.data.currentIndex == 3) {
          if (data.data.pageData.current == 1) {
            that.setData({
              orderData3: []
            })
          }
          that.setData({
            orderData3: that.data.orderData3.concat(data.data.pageData.data)
          })
        }
        if (that.data.currentIndex == 4) {
          if (data.data.pageData.current == 1) {
            that.setData({
              orderData4: []
            })
          }
          that.setData({
            orderData4: that.data.orderData4.concat(data.data.pageData.data)
          })
        }
        if (data.data.pageData.current * data.data.pageData.size >= data.data.pageData.total) {
          that.data.pageData[that.data.currentIndex] = { 'pageNum': that.data.pageData[that.data.currentIndex].pageNum, 'pageSize': that.data.pageData[that.data.currentIndex].pageSize, 'isLoad': false, 'orderNo': that.data.pageData[that.data.currentIndex].orderNo }
        } else {
          that.data.pageData[that.data.currentIndex] = { 'pageNum': that.data.pageData[that.data.currentIndex].pageNum + 1, 'pageSize': that.data.pageData[that.data.currentIndex].pageSize, 'isLoad': true, 'orderNo': that.data.pageData[that.data.currentIndex].orderNo }
        }

        var dataShowNew = that.data.dataShow;

        if (that.data.orderData0.length == 0) {
          dataShowNew[0] = false;
        }else{
          dataShowNew[0] = true;
        }
        if (that.data.orderData1.length == 0) {
          dataShowNew[1] = false;
        } else {
          dataShowNew[1] = true;
        }
        if (that.data.orderData2.length == 0) {
          dataShowNew[2] = false;
        } else {
          dataShowNew[2] = true;
        }
        if (that.data.orderData3.length == 0) {
          dataShowNew[3] = false;
        } else {
          dataShowNew[3] = true;
        }
        if (that.data.orderData4.length == 0) {
          dataShowNew[4] = false;
        } else {
          dataShowNew[4] = true;
        }
        console.log(dataShowNew);
        that.setData({
          dataShow: dataShowNew,
        })
        wx.hideLoading();
        wx.stopPullDownRefresh();
        wx.hideNavigationBarLoading();
      }
    })

  },
  doQuery: function () {

    this.data.pageData[this.data.currentIndex] = { 'pageNum': 0, 'pageSize': this.data.pageData[this.data.currentIndex].pageSize, 'isLoad': true, 'orderNo': this.data.orderNoAr[this.data.currentIndex] };
    console.log(this.data.pageData[this.data.currentIndex])
    this.onLoad();

  },
  orderInput: function (e) {
    this.data.orderNoAr[this.data.currentIndex] = e.detail.value;
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  qrorder: function (e) {
    let that = this;
    wx.showModal({
      title: '提示',
      content: '您确定接收' + e.target.dataset.tel + '用户试用订单么？',
      confirmText: '确认',
      cancelText: '取消',
      success(res) {
        if (res.confirm) {
          var id = e.target.dataset.orderid;
          wx.request({
            //请求地址
            url: app.globalData.baseUrl + '/webapi/qztApplyTryorder/aggreeOrder',
            data: {
              tokenId: wx.getStorageSync('tokenId'),
              userId: wx.getStorageSync('userMess').userId,
              vno: app.globalData.vno,
              cno: app.globalData.cno,
              orderId: id
            },//发送给后台的数据         
            method: 'POST',
            header: {
              //设置参数内容
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            success: function (res) {
              var data = res.data;
              if (data.code == '200') {
                var orderData = that.data.orderData0;
                orderData.splice(e.target.dataset.orderno, 1);
                that.setData({
                  orderData0: orderData
                })
              }
            }
          })
        }
      }
    })

  },
  jjorder: function (e) {
    let that = this;
    wx.showModal({
      title: '提示',
      content: '您确定拒绝' + e.target.dataset.tel + '用户试用订单么？',
      confirmText: '确认',
      cancelText: '取消',
      success(res) {
        if (res.confirm) {
          var id = e.target.dataset.orderid;
          wx.request({
            //请求地址
            url: app.globalData.baseUrl + '/webapi/qztApplyTryorder/refuseOrder',
            data: {
              tokenId: wx.getStorageSync('tokenId'),
              userId: wx.getStorageSync('userMess').userId,
              vno: app.globalData.vno,
              cno: app.globalData.cno,
              orderId: id
            },//发送给后台的数据         
            method: 'POST',
            header: {
              //设置参数内容
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            success: function (res) {
              var data = res.data;
              if (data.code == '200') {
                var orderData = that.data.orderData0;
                orderData.splice(e.target.dataset.orderno, 1);
                that.setData({
                  orderData0: orderData
                })
              }
            }
          })

        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    if (this.data.isfresh) {
      wx.startPullDownRefresh();
      wx.showNavigationBarLoading();
      this.data.orderNoAr[this.data.currentIndex] = '';
      this.setData({
        orderNoAr: this.data.orderNoAr,
        isfresh: false
      })
      this.data.pageData[this.data.currentIndex] = { 'pageNum': 0, 'pageSize': this.data.pageData[this.data.currentIndex].pageSize, 'isLoad': true, 'orderNo': '' };
      this.onLoad();

    }

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
  startPull: function () {

    wx.startPullDownRefresh();
    wx.showNavigationBarLoading();
    this.data.orderNoAr[this.data.currentIndex] = '';
    this.setData({
      orderNoAr: this.data.orderNoAr
    })
    this.data.pageData[this.data.currentIndex] = { 'pageNum': 0, 'pageSize': this.data.pageData[this.data.currentIndex].pageSize, 'isLoad': true, 'orderNo': '' };
    this.onLoad();

  },
  startPullFash: function () {

    wx.showNavigationBarLoading();
    this.data.orderNoAr[this.data.currentIndex] = '';
    this.setData({
      orderNoAr: this.data.orderNoAr
    })
    this.data.pageData[this.data.currentIndex] = { 'pageNum': 0, 'pageSize': this.data.pageData[this.data.currentIndex].pageSize, 'isLoad': true, 'orderNo': '' };
    this.onLoad();

  },
  onPullDownRefresh: function () {

  },
  scrollLower: function () {
    this.onLoad()
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log("xxxxxxxxx")
  }
})