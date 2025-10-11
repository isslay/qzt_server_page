// pages/fwddxq/fwddxq.js
var app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    status: "",
    service: "",
    name: "",
    tel: "",
    station: "",
    beginTime: "",
    endTime: ""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var id = options.id;
    let this_ = this;
    wx.request({
      //请求地址
      url: app.globalData.baseUrl + '/pubapi/qztServiceOrder/selectById/' + id,
      data: {
        vno: app.globalData.vno,
        cno: app.globalData.cno
      }, //发送给后台的数据
      method: 'GET', //请求方式
      header: {
        'Content-Type': 'application/json',
      },
      // method: 'POST',
      // header: {
      //   //设置参数内容
      //   'Content-Type': 'application/x-www-form-urlencoded'
      // },
      success: function(res) {
        var data = res.data;
        // console.log(data);
        if (data.code == '200') {
          var s = "";
          if (data.status == "01") {
            s = "待确认";
          } else if (data.status == "03") {
            s = "待服务";
          } else if (data.status == "05") {
            s = "已到店";
          } else if (data.status == "07") {
            s = "已完成";
          } else if (data.status == "90") {
            s = "已驳回";
          }

          this_.setData({
            status: s,
            service: data.service,
            name: data.name,
            tel: data.tel,
            station: data.station,
            beginTime: data.beginTime,
            endTime: data.endTime,
            disease: data.disease
          })

        } else {
          wx.showToast({
            title: "查询失败",
            icon: 'none'
          })
          setTimeout(function() {
            //要延时执行的代码
            wx.switchTab({
              url: '../my/my'
            })
          }, 1500) //延迟时间 这里是1秒
        }
      },

    })

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

  /**
   * 用户点击右上角分享
   */
  // onShareAppMessage: function () {

  // }
})