var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    hiddenName: true,
    show1: true,
    showModalStatus: false,
    userImg: 'https://img.jlqizutang.com/user.png',
    invitationOne: '',
    hiddenPic: true,
    showCode: true
  },
  go: function() {
    wx.navigateTo({
      url: '../order/order',
    })

  },

  sydd: function() {
    wx.navigateTo({
      url: '../sydd/sydd',
    })
  },
  fwdd: function() {
    wx.navigateTo({
      url: '../fworder/fworder',
    })
  },
  fwzwh: function() {
    wx.navigateTo({
      url: '../fwzwh/fwzwh',
    })
  },
  fwddcx: function() {
    wx.navigateTo({
      url: '../fwddcx/fwddcx',
    })
  },
  syddcx: function() {
    wx.navigateTo({
      url: '../syddcx/syddcx',
    })
  },
  puias: function() {
    wx.navigateTo({
      url: '../puias_order/puias_order',
    })
  },
  myqb: function() {
    wx.navigateTo({
      url: '../myqb/myqb',
    })
  },
  yxck: function() {
    wx.navigateTo({
      url: '../yxck/yxck',
    })
  },
  myyhq: function() {
    wx.navigateTo({
      url: '../myyhq/myyhq',
    })
  },
  wdtg: function() {
    wx.navigateTo({
      url: '../mytg/mytg',
    })
  },
  skzh: function() {
    wx.navigateTo({
      url: '../skzh/skzh',
    })
  },
  dzgl: function() {
    wx.navigateTo({
      url: '../dzgl/dzgl',
    })
  },
  wdsz: function() {
    wx.navigateTo({
      url: '../myshezhi/myshezhi',
    })
  },
  mytx: function() {
    wx.navigateTo({
      url: '../withdrawal_record/withdrawal_record',
    })
  },
  //合作申请
  applicationFor: function() {
    wx.navigateTo({
      url: '../cooperation/cooperation',
    })
  },
  click: function() {
    this.setData({
      hiddenName: !this.data.hiddenName
    })
  },

  sharecode: function() {
    this.setData({
      hiddenPic: !this.data.hiddenPic
    })
  },

  getUserInfoFun: function() {

    var that = this;
    wx.getUserInfo({
      success: function(res) {
        console.log("userInfo:" + res.userInfo)　　　　　　　 //do anything
        that.checkSettingStatu(res.userInfo);
      }
    })
  },
  checkSettingStatu: function(cb) {
    var that = this;
    //修改用户的头像和名称信息  
    var url = app.globalData.baseUrl + '/webapi/qztUser/updateUserMess';
    var data = {
      conType: 1,
      nickName: cb.nickName,
      imgUrl: cb.avatarUrl
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        this.doLoad();
        this.hideModal();
      }


    })

  },
  /**
   * 生命周期函数--监听页面加载
   */

  onLoad: function(options) {

  },
  doLoad: function() {
    let that = this;
    var url = app.globalData.baseUrl + '/webapi/qztUser/queryUserAccount';
    var data = {}
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        var showTitle = true;
        wx.setStorageSync('userMess', data.userBaseData);
        if (data.userBaseData.userType > 0) {
          showTitle = false;
          that.setData({
            userTypeMc: data.userTypeMc,
            show1: false
          })
        }
        that.setData({
          show1: showTitle,
          accountMoney: data.accountData.accountMoney,
          sMoney: data.accountData.shareMoney.sMoney,
          tMoney: data.accountData.shareMoney.tMoney,
          userName: data.userName,
          invitationOne: data.userBaseData.invitationOne,
          showCode: data.showCode
        })
        if (data.userBaseData.wxHeadImage != '') {
          that.setData({
            userImg: data.userBaseData.wxHeadImage
          })
        }
        if (data.userBaseData.wxNickName == '') {
          this.showModal();
        }
      }
      wx.hideNavigationBarLoading();
      wx.stopPullDownRefresh();
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
    if (typeof this.getTabBar === 'function' &&
      this.getTabBar()) {
      this.getTabBar().setData({
        selected: 4
      })
    }

    this.doLoad();
  },
  //点击保存图片
  save() {
    let that = this
    //若二维码未加载完毕，加个动画提高用户体验
    wx.showToast({
      icon: 'loading',
      title: '正在保存图片',
      duration: 1000
    })
    //判断用户是否授权"保存到相册"
    wx.getSetting({
      success(res) {
        //没有权限，发起授权
        if (!res.authSetting['scope.writePhotosAlbum']) {
          wx.authorize({
            scope: 'scope.writePhotosAlbum',
            success() { //用户允许授权，保存图片到相册
              that.savePhoto();
            },
            fail() { //用户点击拒绝授权，跳转到设置页，引导用户授权
              wx.openSetting({
                success() {
                  wx.authorize({
                    scope: 'scope.writePhotosAlbum',
                    success() {
                      that.savePhoto();
                    }
                  })
                }
              })
            }
          })
        } else { //用户已授权，保存到相册
          that.savePhoto()
        }
      }
    })
  },
  //保存图片到相册，提示保存成功
  savePhoto() {
    let that = this
    wx.downloadFile({
      url: that.data.invitationOne,
      success: function(res) {
        wx.saveImageToPhotosAlbum({
          filePath: res.tempFilePath,
          success(res) {
            wx.showToast({
              title: '保存成功',
              icon: "success",
              duration: 1000
            })
          }
        })
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
    this.doLoad();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {
    return {
      title: app.globalData.shareTitle,
      path: app.globalData.shareUrl + "?shareCode=" + wx.getStorageSync('userMess').userId,
      imageUrl: app.globalData.sharePicUrl //不设置则默认为当前页面的截图
    }
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
  tel: function() {
    wx.makePhoneCall({
      phoneNumber: '0431-82705666',
    })
  }
})