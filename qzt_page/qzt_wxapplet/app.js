//app.js
App({
  onShow: function(e) {
    const updateManager = wx.getUpdateManager()
    updateManager.onCheckForUpdate(function(res) {
      // 请求完新版本信息的回调
      console.log(res.hasUpdate)
    })
    updateManager.onUpdateReady(function() {
      wx.showModal({
        title: '更新提示',
        content: '新版本已经准备好，是否重启应用？',
        success: function(res) {
          if (res.confirm) {
            // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
            updateManager.applyUpdate()
          }
        }
      })
    })
    updateManager.onUpdateFailed(function() {
      // 新版本下载失败
    })
  },
  onLaunch: function(options) {
    // 展示本地存储能力
    // 登录
    var that = this;
    let wxOpenId = wx.getStorageSync('userMess').wxOpenId;
    if (wx.getStorageSync('tokenId') == '' || wxOpenId == undefined || wxOpenId == null || wxOpenId == "" || wxOpenId == "undefined") {
      let that = this;
      wx.showLoading({
        title: '登录中',
      })
      setTimeout(function() {
        wx.login({
          success: res => {
            // 发送 res.code 到后台换取 openId, sessionKey, unionId
            wx.request({
              //请求地址
              url: that.globalData.baseUrl + '/pubapi/weChat/weJsChatLogin',
              data: {
                code: res.code,
                shareCode: wx.getStorageSync('shareCode')
              }, //发送给后台的数据         
              method: 'POST',
              header: {
                //设置参数内容
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              success: function(res) {
                var data = res.data;
                if (data.code == '200') {
                  data = data.data;
                  wx.setStorageSync('tokenId', data.tokenId);
                  wx.setStorageSync('userMess', data.userData);
                }
                wx.hideLoading();
                let pages = getCurrentPages();
                let atPresent = pages[pages.length - 1]; //当前页实例      
                if (atPresent.route == "pages/lingquyhq/lingquyhq" || atPresent.route == "pages/fillin_goods_order/fillin_goods_order") {
                  atPresent.reLoad();
                } else {
                  atPresent.onShow();
                }

              },
              fail: function(err) {}, //请求失败
              complete: function() {} //请求完成后执行的函数
            })

          }
        })
      }, 1000)


    }
  },
  checkSettingStatu: function(cb, tokenId, userId) {
    var nickname = cb.nickName;
    var that = this;
    //修改用户的头像和名称信息
    wx.request({
      //请求地址
      url: that.globalData.baseUrl + '/webapi/qztUser/updateUserMess',
      data: {
        tokenId: tokenId,
        userId: userId,
        vno: that.globalData.vno,
        cno: that.globalData.cno,
        conType: 1,
        nickName: nickname,
        imgUrl: cb.avatarUrl

      }, //发送给后台的数据         
      method: 'POST',
      header: {
        //设置参数内容
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      success: function(res) {

      }
    })
  },
  reLogin: function() {


  },
  globalData: {
    userInfo: null,
    baseUrl: 'http://127.0.0.1:8089/',
    // baseUrl: 'http://localhost:8089/',
    // baseUrl:'https://wuwu8023.51vip.biz',
    // baseUrl: 'https://testklyg.rhzt.net/qzt/',
    // baseUrl: 'https://testapi.jlqizutang.com/qzt',
    //baseUrl:'https://wuwu8023.51vip.biz',
    //baseUrl: 'https://api.jlqizutang.com/qzt',
    // baseUrl: 'https://api.chinaqzt.cn/qzt',
    vno: '1.0.0',
    cno: '5',
    shareTitle: '我推荐七足堂。免费试用，见效付款',
    shareUrl: '/pages/index/index',
    sharePicUrl: 'https://img.jlqizutang.com/shareindex2.jpg'
  }

})