// pages/select_business/select_business.js
var common = require('../../utils/util.js');
var lat, lon;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    dataShow: true,
    businessList: [],
    current: 1,
    size: 10,
    total: 0,
    busLong: "",
    busLat: "",
    busName: "",
    kilometer: "",
    sorts: "01"
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.setData({
      busLong: options.longitude,
      busLat: options.latitude,
      kilometer: options.kilometer
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
    if (typeof this.getTabBar === 'function' &&
      this.getTabBar()) {
      this.getTabBar().setData({
        selected: 2
      })
    };
    this.setData({
      current: 1,
    });
    this.getBusinessListPage();
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
    this.setData({
      current: 1
    });
    this.getBusinessListPage();
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
    var current = this.data.current;
    var total = this.data.total;
    var size = this.data.size;
    if (current * size < total) {
      this.setData({
        current: current + 1
      });
      this.getBusinessListPage();
    }
  },
  //获取服务站列表
  getBusinessListPage: function(e) {
    var _this = this;
    var datas = {
      pageNum: _this.data.current,
      pageSize: _this.data.size,
      busLong: _this.data.busLong,
      busLat: _this.data.busLat,
      busName: _this.data.busName,
      kilometer: _this.data.kilometer,
      sorts: _this.data.sorts
    };
    common.httpPost(common.getBusinessListPage, datas, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        let businessList = [];
        let dataShow = true;
        if (ret.total == 0) {
          dataShow = false;
        } else if (ret.current == 1) {
          businessList = ret.data;
        } else {
          businessList = _this.data.businessList.concat(ret.data);
        }
        _this.setData({
          dataShow: dataShow,
          businessList: businessList,
          current: ret.current,
          size: ret.size,
          total: ret.total
        });
      }
      wx.stopPullDownRefresh();
    });
  },
  //选中跳回上一页
  businessParticulars: function(res) {
    let dataset = res.currentTarget.dataset;
    var pages = getCurrentPages();   //当前页面
    var prevPage = pages[pages.length - 2];   //上一页面
    prevPage.setData({ //直接给上一个页面赋值
      addressId: dataset.id,
      consigneeTel: dataset.bustel,
      companyName: dataset.busname,
      consigneeAddress: dataset.busaddress,
      addressStatus: true
    });
    wx.navigateBack({
      delta: 1
    });
  },
  //站点查询
  busName: function(e) {
    this.setData({
      busName: e.detail.value
    });
  },
  //搜索
  selectBusName: function(e) {
    this.setData({
      current: 1,
      busName: e.detail.value
    });
    this.getBusinessListPage();
  },

})