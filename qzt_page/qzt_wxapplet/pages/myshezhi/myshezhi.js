// pages/myshezhi/myshezhi.js
var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    mobileShow:false,
    mobile:'添加手机号',
    wxName:'',
    shareShow:true,
    pwdShow:true
  },
  xgmm:function(){
    wx.navigateTo({
      url: '../xgmm/xgmm',
    })
  },
  szmm: function () {
    wx.navigateTo({
      url: '../szmm/szmm',
    })
  },
  wdtjr:function(){
    wx.navigateTo({
      url: '../mytjr/mytjr',
    })
  },
  szmobile:function(){
    wx.navigateTo({
      url: '../szmobile/szmobile?mobile=' + this.data.mobile,
    })    
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
   
  },

  doLoad: function () {
    let that = this;
    var url = app.globalData.baseUrl + '/webapi/qztUser/queryUserAccount';
    var data = {
    }
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {
        that.setData({
          wxName: data.userBaseData.wxNickName
        })       
        if (data.userBaseData.mobile!='') {       
          that.setData({
            mobileShow :true,
            mobile: data.userBaseData.mobile
          })
        }
        if (data.userBaseData.referrerFirst != '') {
          that.setData({
            shareShow: false,
            referrerFirst: data.userBaseData.referrerFirst
          })
        }
        that.setData({         
          userId: data.userBaseData.userId
        })
        if (data.userBaseData.wxHeadImage != '') {
          that.setData({
            userImg: data.userBaseData.wxHeadImage
          })
        }
        if (data.userBaseData.mobile != '') {
          that.setData({
            mobile: data.userBaseData.mobile
          })
        }
        if (data.userBaseData.payPwd==''){          
          that.setData({
            pwdShow: false
          })
        }else{
          that.setData({
            pwdShow: true
          })
        }
        console.log(this.data.pwdShow)
        wx.hideNavigationBarLoading();
      }
    })
  },
  changeImg:function(){
    let that = this;
    wx.chooseImage({
      count: 1, // 默认5
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: res => {
        wx.showLoading({
          title: '正在上传...',
        })
        // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
        let tempFilePaths = res.tempFilePaths;
        that.setData({
          tempFilePaths: tempFilePaths
        })
        /**
         * 上传完成后把文件上传到服务器
         */
        var count = 0;
        for (var i = 0, h = tempFilePaths.length; i < h; i++) {
          //上传文件
          wx.uploadFile({
            url: app.globalData.baseUrl + '/pubapi/qiniuUploading/uploadingPic',
            filePath: tempFilePaths[i],
            name: 'file',
            header: {
              "Content-Type": "multipart/form-data"
            },
            success: function (res) {
              count++;
              // //如果是最后一张,则隐藏等待中  
              // if (count == tempFilePaths.length) {
              //   //wx.hideToast();
              // }
              var data_ = JSON.parse(res.data);
              if (data_.code == '200') {
                
                that.setData({
                  userImg: data_.data.picUrl
                })
                //上传个人头像进行修改
                var url = app.globalData.baseUrl + '/webapi/qztUser/updateUserMess';
                var data = {
                  imgUrl: data_.data.picUrl,
                  conType:4
                }
                util.httpPost(url, data).then(res => {
                  var data = res.data;
                  if (data.code == '200') {
                    wx.hideLoading();
                  }
                })
              } else {
                wx.showToast({
                  title: data_.message,
                  icon: 'none'
                })
              }              
            },
            fail: function (res) {
              wx.hideToast();
              wx.showModal({
                title: '错误提示',
                content: '上传图片失败',
                showCancel: false,
                success: function (res) { }
              })
            }

          });

        }

      }
    })

  },

  getUserInfoFun: function () {
    var that = this;
    wx.getUserInfo({
      success: function (res) {
        console.log("userInfo:" + res.userInfo)
        //do anything
        that.checkSettingStatu(res.userInfo);
      }
    })
  },
  checkSettingStatu: function (cb) {
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
      }


    })

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.doLoad();
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
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  }
})