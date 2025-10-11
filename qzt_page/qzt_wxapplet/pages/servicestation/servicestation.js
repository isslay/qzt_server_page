// pages/servicestation/servicestation.js
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
    areaCode: "",
    sorts: "01"
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

    wx.getLocation({
      type: 'wgs84',
      success: (res) => {
        lat = res.latitude;
        lon = res.longitude;
        //console.log(lat + "===" + lon);
        // var speed = res.speed
        // var accuracy = res.accuracy      
        if (typeof this.getTabBar === 'function' &&
          this.getTabBar()) {
          this.getTabBar().setData({
            selected: 2
          })
        };
        this.setData({
          current: 1
        });
        this.getBusinessListPage();
      },
      fail: (res) => {
        if (typeof this.getTabBar === 'function' &&
          this.getTabBar()) {
          this.getTabBar().setData({
            selected: 2
          })
        };
        this.setData({
          current: 1
        });
        this.getBusinessListPage();
      }
    })

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
      busLong: lon,
      busLat: lat,
      busName: _this.data.busName,
      areaCode: _this.data.areaCode,
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
  //详情
  businessParticulars: function(e) {
    wx.navigateTo({
      url: '../fwzxq/fwzxq?id=' + e.currentTarget.dataset.id
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