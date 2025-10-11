// pages/skzh/skzh.js
var util = require('../../utils/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    dataShow: [true, true],
    currentIndex: 0,
    currentIndex1: 0,
    showModalStatus: false,
    animationData: '',
    bankList: [],
    aliPayList: []
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

  //swiper切换时会调用
  pagechange1: function(e) {
    if ("touch" === e.detail.source) {
      let currentPageIndex = this.data.currentIndex1
      currentPageIndex = e.detail.current
      this.setData({
        currentIndex1: currentPageIndex
      })
    }
  },
  //用户点击tab时调用
  titleClick1: function(e) {
    let currentPageIndex =
      this.setData({
        //拿到当前索引并动态改变
        currentIndex1: e.currentTarget.dataset.idx
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
  //添加/编辑收款账号点击
  edittoAddaddShroffAccount: function(e) {
    let id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '../add_shroff_account/add_shroff_account?id=' + (id == null || id == undefined ? "" : id) + "&currentIndex=" + this.data.currentIndex,
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
  //设置默认收款账号
  defaultShroffAccount: function(e) {
    var _this = this;
    wx.showModal({
      title: '提示',
      content: '确定要将该地址设为默认吗？',
      success: function(sm) {
        if (sm.confirm) {
          var datas = {
            id: e.currentTarget.dataset.id
          };
          util.httpPost(util.defaultShroffAccount, datas, "POST", false, false).then(res => {
            var data = res.data;
            if (data.code == '200') {
              _this.getShroffAccountList();
              util.alert("设置成功");
            }
          });
        } else if (sm.cancel) {

        }
      }
    });
  },
  //删除收款账号
  delShroffAccount: function(e) {
    var _this = this;
    wx.showModal({
      title: '提示',
      content: '确定要删除吗？',
      success: function(sm) {
        if (sm.confirm) {
          var datas = {
            id: e.currentTarget.dataset.id
          };
          util.httpPost(util.delShroffAccount, datas, "POST", false, false).then(res => {
            var data = res.data;
            if (data.code == '200') {
              util.alert("已删除");
              let index = e.currentTarget.dataset.index //数组下标
              let bankList = _this.data.bankList;
              let aliPayList = _this.data.bankList;
              if (_this.data.currentIndex == 0) { //银行卡
                bankList.splice(index, 1); //删除
              } else {
                aliPayList.splice(index, 1); //删除
              }
              _this.setData({
                bankList: bankList,
                aliPayList: aliPayList,
                dataShow: [bankList.length > 0 ? true : false, aliPayList.length > 0 ? true : false]
              });
            }
          });
        } else if (sm.cancel) {

        }
      }
    });
  },


});