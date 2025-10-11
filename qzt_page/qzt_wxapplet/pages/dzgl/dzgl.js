//pages/dzgl/dzgl.js
var common = require('../../utils/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    addressList: [],
    dataShow: true
  },
  //编辑/新增跳转
  xzshdz: function(e) {
    let id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '../xzshdz/xzshdz?id=' + (id == null || id == "" || id == undefined ? "" : id),
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
    this.getAddressList();
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
  //获取收货地址列表
  getAddressList: function(e) {
    var _this = this;
    var datas = {};
    common.httpPost(common.getAddressList, datas, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        let dataShow = true;
        if (data.data.length == 0) {
          dataShow = false;
        }
        _this.setData({
          dataShow: dataShow,
          addressList: data.data
        });
      }
    });
  },
  //删除地址
  addressDel: function(e) {
    var _this = this;
    wx.showModal({
      title: '提示',
      content: '确定要删除吗？',
      success: function(sm) {
        if (sm.confirm) {
          var datas = {
            id: e.currentTarget.dataset.id
          };
          common.httpPost(common.delectAddress, datas, "POST", false, false).then(res => {
            var data = res.data;
            if (data.code == '200') {
              common.alert("已删除");
              let index = e.currentTarget.dataset.index //数组下标
              let addressList = _this.data.addressList;
              addressList.splice(index, 1); //删除
              _this.setData({
                addressList: addressList,
                dataShow: addressList.length > 0 ? true : false
              });
            }
          });
        } else if (sm.cancel) {

        }
      }
    });
  },
  //设置默认地址
  defaultArea: function(e) {
    var _this = this;
    wx.showModal({
      title: '提示',
      content: '确定要将该地址设为默认吗？',
      success: function(sm) {
        if (sm.confirm) {
          var datas = {
            id: e.currentTarget.dataset.id
          };
          common.httpPost(common.defaultArea, datas, "POST", false, false).then(res => {
            var data = res.data;
            if (data.code == '200') {
              _this.getAddressList();
              common.alert("设置成功");
            }
          });
        } else if (sm.cancel) {

        }
      }
    });
  }

})