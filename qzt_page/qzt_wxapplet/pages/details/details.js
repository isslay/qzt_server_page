// pages/details/details.js
const app = getApp();
var common = require('../../utils/util.js');
let wxparse = require("../../utils/wxParse/wxParse.js");
var id;
var shareTitle, sharePic, shareContent, thePurchaseNum;
var back;

Page({

  /**
   * 页面的初始数据
   */
  data: {
    goodMoney: "", //金额
    goodName: "", //商品名称
    goodRemark: "", //商品说明
    goodType: '', //规格
    goodNum: "", //库存
    goodContent: "", //详情
    number: 1, //数量
    statusBarHeight: "",
    background: [], //轮播
    indicatorDots: true, //是否显示小点
    vertical: false, //横竖播放
    autoplay: true, //是否自动播放
    circular: true, //循环播放
    interval: 3000, //轮播时间
    duration: 500, //轮播速度
    previousMargin: 0, //前距离
    nextMargin: 0, //后距离
    isbg: false, //自定义导航栏是否显示透明显示
    imageIndex: 0,
    title: ""
  },

  jian: function(e) {
    var number = this.data.number;
    if (thePurchaseNum > 0) {
      if (number > 0) {
        number--
      }
    }
    this.setData({
      number: number
    })
  },

  jia: function(e) {
    var number = this.data.number;
    var goodNum = this.data.goodNum //库存
    number++
    if (number > goodNum) {
      wx.showToast({
        title: '数量不能大于库存',
        icon: 'none',
        duration: 2000 //持续的时间
      })
      return
    }
    this.setData({
      number: number
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    back = options.back;
    //获得参数  shareCode
    var shareCode = options.shareCode;
    if (shareCode != null) {
      wx.setStorageSync('shareCode', shareCode)
    }
    id = options.goodsId;
    let this_ = this;
    wx.getSystemInfo({
      success(res) {
        this_.setData({
          statusBarHeight: res.statusBarHeight + 4
        })
      }
    });
  },
  scroll: function(e) {
    // if (e.detail.scrollTop >= 200) {
    //   this.setData({
    //     isbg: true
    //   })
    //   this.setData({
    //     title: "商品详情"
    //   })
    // } else {
    //   this.setData({
    //     isbg: false
    //   })
    //   this.setData({
    //     title: ""
    //   })

    // }
  },
  back1: function() {
    console.log(back);
    if (back == "T") {
      wx.switchTab({
        url: '../index/index'
      })
    } else {
      //返回
      wx.navigateBack({
        delta: 1
      })
    }
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
    this.getGoodsInfo();
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

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function(res) {
    let paths = '/pages/details/details?goodsId=' + id + "&shareCode=" + wx.getStorageSync('userMess').userId + "&back=T";
    return {
      title: shareTitle,
      path: paths,
      imageUrl: sharePic, //用户分享出去的自定义图片大小为5:4,
      success: function(res) {
        // 转发成功
        console.log(path)
        wx.showToast({
          title: "分享成功",
          icon: 'success',
          duration: 2000
        })
      },
      fail: function(res) {
        // 分享失败
      },
    }
  },
  //立即购买
  buyGoods: function(res) {
    if (this.data.number < this.data.goodNum) {
      wx.navigateTo({
        url: '../fillin_goods_order/fillin_goods_order?id=' + id + "&buyNum=" + this.data.number,
      });
    } else {
      common.showModal("库存不足");
    }
  },
  //获取商品详情
  getGoodsInfo(res) {
    let this_ = this;
    wx.request({
      url: app.globalData.baseUrl + '/pubapi/qztGoods/selectById/' + id, //url
      method: 'GET', //请求方式
      header: {
        'Content-Type': 'application/json',
      },
      success: function(res) {
        // console.log(res);
        // console.log(res.data.good);
        if (res.data.data.good == "") {
          wx.showToast({
            title: "查无此商品信息",
            icon: 'none'
          })
          setTimeout(function() {
            //要延时执行的代码
            wx.switchTab({
              url: '../index/index'
            })
          }, 1500) //延迟时间 这里是1秒

        }
        if (res.data.code === '200') {
          var ret = res.data.data;
          var bannerList = [];
          var datesBanner = ret.goodBanner;
          for (var i = 0; i < datesBanner.length; i++) {
            bannerList.push(datesBanner[i].bannerUrl);
          }

          var datesGood = ret.good;
          shareTitle = datesGood.shareTitle;
          sharePic = datesGood.sharePic;
          shareContent = datesGood.shareContent;
          thePurchaseNum = datesGood.goodsNum;
          var nums = 1;
          if (thePurchaseNum == 0) {
            nums = thePurchaseNum
          }
          this_.setData({
            goodMoney: datesGood.goodsPrice / 100, //金额
            goodName: datesGood.goodsName, //商品名称
            goodRemark: datesGood.goodsRemark, //商品说明
            goodType: datesGood.goodsSpec, //规格
            goodNum: datesGood.goodsNum, //库存,
            goodContent: datesGood.content,
            background: bannerList,
            imageIndex: 0,
            number: nums
          })
          wxparse.wxParse('goodContent', 'html', datesGood.content, this_, 5);
        }
      },
      fail: function() {
        app.consoleLog("请求数据失败");
      },
      complete: function() {
        // complete 
      }
    })
  },
  //添加到购物车
  addGoodsShoppingCart(res) {
    if (this.data.number > this.data.goodNum) {
      common.showModal("库存不足");
    } else {
      let _this = this;
      let datas = {
        goodsId: id,
        buyNum: this.data.number,
      };
      common.httpPost(common.addGoodsShoppingCart, datas, "PUT", false, false).then(res => {
        var data = res.data;
        if (data.code == '200') {
          let ret = data.data;
          common.alert("已成功添加到购物车");
        }
      });
    }
  },
  //购物车按钮
  shoppingCat: function(res) {
    wx.switchTab({
      url: "../my_shopping_cart/my_shopping_cart"
    })
  },
})