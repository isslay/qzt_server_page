// pages/xzskzh/xzskzh.js
var util = require('../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    dataShow: [true, true],
    currentIndex: 0,
    showModalStatus: false,
    animationData: '',
    bankList: [],
    aliPayList: [],
    backBankId: "",
    backBankName: ""
  },
  showModal: function() {
    // 显示遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "ease-in-out",
      delay: 0
    })
    this.animation = animation
    animation.translateY(500).step()
    this.setData({
      animationData: animation.export(),
      showModalStatus: true
    })
    setTimeout(function() {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export()
      })
    }.bind(this), 10)
  },
  hideModal: function() {
    this.setData({
      showModalStatus: false,
    })
  },
  //swiper切换时会调用
  pagechange: function(e) {
    if ("touch" === e.detail.source) {
      let currentPageIndex = this.data.currentIndex
      currentPageIndex = e.detail.current
      this.setData({
        currentIndex: currentPageIndex
      })
    }
  },
  //用户点击tab时调用
  titleClick: function(e) {
    let currentPageIndex =
      this.setData({
        //拿到当前索引并动态改变
        currentIndex: e.currentTarget.dataset.idx
      })
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
    this.getShroffAccountList();
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
    this.getShroffAccountList();
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },
  //选择触发
  selectBank: function(e) {
    let backBankName = "";
    if (this.data.currentIndex == 0) {
      backBankName = e.currentTarget.dataset.bankname;
    } else {
      backBankName = e.currentTarget.dataset.bankname + e.currentTarget.dataset.cardnum
    }
    console.log(backBankName);
    this.setData({
      backBankId: e.currentTarget.dataset.id,
      backBankName: backBankName
    });
  },
  //获取收款账号列表
  getShroffAccountList: function(e) {
    var _this = this;
    util.httpPost(util.getShroffAccountList, {}, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        _this.setData({
          bankList: ret.bankList,
          aliPayList: ret.aliPayList,
          dataShow: [ret.bankList.length > 0 ? true : false, ret.aliPayList.length > 0 ? true : false]
        });
      }
    });
  },
  //确认
  affirm: function(e) {
    if (this.data.backBankId == null || this.data.backBankId == "") {
      util.showModal("请选择收款账号！");
    } else {
      var pages = getCurrentPages();   //当前页面
      var prevPage = pages[pages.length - 2];   //上一页面
      prevPage.setData({ //直接给上一个页面赋值
        cardId: this.data.backBankId,
        bankName: this.data.backBankName,
      });
      wx.navigateBack({
        delta: 1
      });
    }
  },
  //添加收款账号
  addaddShroffAccount: function(e) {
    let id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '../add_shroff_account/add_shroff_account?id=&currentIndex=' + this.data.currentIndex,
    });
  },



})