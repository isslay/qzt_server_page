// pages/qrdd/qrdd.js
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orderId:null,
    cardId:'',
    showhidden:false,
    imgAr: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let that = this;
    var id = options.orderId;
    that.setData({
      orderId: id
    });
    wx.request({
      //请求地址
      url: app.globalData.baseUrl + '/webapi/qztApplyTryorder/getTryOrderMess',
      data: {
        tokenId: wx.getStorageSync('tokenId'),
        userId: wx.getStorageSync('userMess').userId,
        vno: app.globalData.vno,
        cno: app.globalData.cno,
        orderId: id
      },//发送给后台的数据         
      method: 'POST',
      header: {
        //设置参数内容
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      success: function (res) {
        var data = res.data;
        if (data.code == '200') {
          var orderData = data.orderData;
          that.setData({
            orderData: orderData
          })
        }
      }
    })    
 
  },

  upload: function () {
    let that = this;
    var nowSize = that.data.imgAr.length;

    wx.chooseImage({
      count: 5 - nowSize, // 默认5
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
                if (data_.code=='200'){
                  var picAr = that.data.imgAr;
                  var newpicAr = picAr.concat(data_.data.picUrl);
                  console.log(newpicAr);
                  that.setData({
                    imgAr: newpicAr
                  })
                }else{
                  wx.showToast({
                    title: data_.message,
                    icon: 'none'
                  })
                }
                if (count == tempFilePaths.length){
                  wx.hideLoading();
                }   
                if (that.data.imgAr.length > 4) {
                  that.setData({
                    showhidden: true
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
  cardIdInput: function(e){

      this.setData({
        cardId: e.detail.value
      })

  },
  jyCardId: function(e){
    var that = this;
    if (that.data.cardId=='' ||  that.data.cardId.length!=18){
      wx.showToast({
        title: '请输入有效身份证',
        icon: 'none'
      })
      return false;
    }
    wx.showLoading({
      title: '校验中..',
    })
   

    wx.request({
      //请求地址
      url: app.globalData.baseUrl + '/webapi/qztApplyTryorder/checkCardId',
      data: {
        tokenId: wx.getStorageSync('tokenId'),
        userId: wx.getStorageSync('userMess').userId,
        vno: app.globalData.vno,
        cno: app.globalData.cno,
        cardId: that.data.cardId
      },//发送给后台的数据         
      method: 'POST',
      header: {
        //设置参数内容
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      success: function (res) {
        var data = res.data;
        wx.hideLoading();
        console.log(data.applyType)
        if (data.code == '200') {          
          wx.showToast({
            title: '该身份证号未使用',
            icon: 'none'
          })
        } else {
          wx.showToast({
            title: data.message,
            icon: 'none'
          })
        }
      },
      fail: function (err) { },//请求失败
      complete: function () { }//请求完成后执行的函数
    })   
  },
  delPic: function(e){
    var picAr = this.data.imgAr;
    picAr.splice(e.target.dataset.picno, 1);
    
    this.setData({
      imgAr:picAr,
      showhidden: false
    })
    
  },
  doSubmit:function(){

    var imgSize = this.data.imgAr.length;
    var message ;
    if (this.data.cardId==null  || this.data.cardId==''){
      message="请填写有效身份证";
    }else if (imgSize == 0) {
      message = "请上传1-5张试用前照片";
    }

    if(message!=null){
      wx.showToast({
        title: message,
        icon: 'none'
      })
      return false;
    }
    wx.showLoading({
      title: '提交中..',
    })
    var that =this;
   
    wx.request({
      //请求地址
      url: app.globalData.baseUrl + '/webapi/qztApplyTryorder/qrddOrder',
      data: {
        tokenId: wx.getStorageSync('tokenId'),
        userId: wx.getStorageSync('userMess').userId,    
        vno: app.globalData.vno,
        cno: app.globalData.cno,
        cardId: that.data.cardId,
        imgAr: that.data.imgAr,
        orderId: that.data.orderId        
      },//发送给后台的数据         
      method: 'POST',
      header: {
        //设置参数内容
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      success: function (res) {
        var data = res.data;
        console.log(data.applyType)
        if (data.code == '200') {
          wx.hideLoading();  
          wx.showModal({
            title: "操作成功",    
            showCancel:false,
            success: function (res) {
              if(res.confirm) {
                wx.navigateBack({
                  delta: 1
                })
              }              
            }
          })       
        }else{
          wx.showToast({
            title: data.message,
            icon: 'none'            
          })
        }
      },
      fail: function (err) { },//请求失败
      complete: function () { }//请求完成后执行的函数
    }) 
    

  },
  /**
   * 
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

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