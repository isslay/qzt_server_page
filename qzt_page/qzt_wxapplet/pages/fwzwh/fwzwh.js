// pages/fwzwh/fwzwh.js
var app = getApp();
const util = require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    detail_info:'请选择详细地址',
    busTimeStart:'开始时间',
    busTimeEnd:'结束时间',
    array: [{ 'code': '0', 'codeText': '正常' }, { 'code': '1', 'codeText': '歇业' }],
    busState:'',
    busStateMc:'',
    busTel:'',
    busLong:'',
    busLat:'',
    busAddress:'',
    busName:'',
    picUrl:'',
    busDetail:'',    
    imgAr1:[],
    addImgAr0:true,
    addImgAr1:true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    var url = app.globalData.baseUrl + '/pubapi/qztBusiness/selectById?id=' + wx.getStorageSync('userMess').bussId;
   // var url = app.globalData.baseUrl + '/pubapi/qztBusiness/selectById?id=30';
    var data={};
    var that = this;
    util.httpPost(url, data,'get').then(res => {
      var data = res.data;
     
      if (data.code == '200') {   
        data = data.data;     
        var busState = data.busState;
        var busStateMc = data.busState == 0 ? '正常' : '歇业';
        var busAddress = data.busAddress;
        if (data.busPic==''){
          busState=0;
          busStateMc ="正常";
          busAddress="";
        }   
        that.setData({
          busName: data.busName,
          busTel: data.busTel,
          detail_info: busAddress == ''?'请选择详细地址' : busAddress,
          busAddress: busAddress,
          busTimeStart: data.busTimeStart == '' ? '开始时间' : data.busTimeStart,
          busTimeEnd: data.busTimeEnd == '' ? '歇业时间' : data.busTimeEnd,
          busState: busState+"",
          busStateMc: busStateMc,
          busDetail: data.busDetail,           
          busLong: data.busLong,
          busLat: data.busLat
        })
        var imgAr0 = '';
        let addImgAr0 = true;
        if (data.busPic != '') {
          imgAr0 = data.busPic;
          addImgAr0 = false;
        } 
        var imgAr1=[]; 
        let addImgAr1 = true;
        if (data.busPicList.length>0){
          imgAr1 = data.busPicList;
          if(data.busPicList.length==5){
            addImgAr1 = false;
          }          
        }
        console.log(imgAr0);
        that.setData({
          picUrl: imgAr0,
          imgAr1: imgAr1,
          addImgAr0: addImgAr0,
          addImgAr1: addImgAr1
        });
      }
    })
  },
  delPic: function (e) {
    var picno = e.target.dataset.picno;
    if(picno=='a'){      
      this.setData({
        picUrl: '',
        addImgAr0: true
      })
    }else{
      var imgAr1 = this.data.imgAr1;      
      imgAr1.splice(e.target.dataset.picno, 1);
      this.setData({
        imgAr1: imgAr1,
        addImgAr1: true
      })

    }

   

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  busNameInput: function (e) {
    this.setData({
      busName: e.detail.value
    })
  },
  busTelInput: function (e) {
    this.setData({
      busTel: e.detail.value
    })
  },
 
  busDetailInput: function (e) {
    this.setData({
      busDetail: e.detail.value
    })
  },  

  bindPickerChange: function (e) {
    this.setData({
      busState: this.data.array[e.detail.value].code,
      busStateMc: this.data.array[e.detail.value].codeText
    })
  },
  bindTimeChange1:function(e){
    this.setData({
      busTimeStart: e.detail.value
    })
  },
  bindTimeChange2: function (e) {
    this.setData({
      busTimeEnd: e.detail.value
    })
  },
  upload0: function () {
    let that = this;    
    wx.chooseImage({
      count:1, // 默认5
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
                  picUrl: data_.data.picUrl,                   
                  addImgAr0: false
                })
              } else {
                wx.showToast({
                  title: data_.message,
                  icon: 'none'
                })
              }              
              wx.hideLoading();              
              
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
  upload1: function () {
    let that = this;
    var nowSize = that.data.imgAr1.length;
    console.log(nowSize);
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
              if (data_.code == '200') {
                var picAr = that.data.imgAr1;
                var newpicAr = picAr.concat(data_.data.picUrl);
                console.log(newpicAr);
                that.setData({
                  imgAr1: newpicAr
                })
              } else {
                wx.showToast({
                  title: data_.message,
                  icon: 'none'
                })
              }
              if (count == tempFilePaths.length) {
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

  },
  mapView: function () {
    var that = this
    wx.chooseLocation({
      success: function (res) {
        // success
        console.log(res, "location")
        that.setData({
          hasLocation: true,
          location: {
            longitude: res.longitude,
            latitude: res.latitude
          },
          detail_info: res.address,
          busAddress: res.address,
          busLat: res.latitude,
          busLong: res.longitude
        })
      },
      fail: function () {
        // fail
      },
      complete: function () {
        // complete
      }
    })
  },
  submit:function(){
    var that =this;
     if (that.data.busName == '') {
      util.alert("请填写服务站名称");
      return false;
    }
    if (that.data.busTel == '') {
      util.alert("请填写联系电话");
      return false;
    }
    if (that.data.busAddress == '') {
      util.alert("请选择详细地址");
      return false;
    }
    if (that.data.busTimeStart == '' || that.data.busTimeStart == '开始时间'){
      util.alert("请选择营业开始时间");
      return false;
    }
    if (that.data.busTimeEnd == '' || that.data.busTimeEnd == '结束时间') {
      util.alert("请选择营业结束时间");
      return false;
    }
    if (that.data.busState == '') {
      util.alert("请选择营业状态");
      return false;
    }
    if (that.data.busDetail == '') {
      util.alert("请填写服务站描述");
      return false;
    }
    if (that.data.picUrl == '') {
      util.alert("请上传服务站图片");
      return false;
    }
    if (that.data.imgAr1.length == 0) {
      util.alert("请上传服务站环境照片");
      return false;
    }
    var data={
      busName: that.data.busName,
      busTel: that.data.busTel,
      busAddress: that.data.busAddress, 
      busTimeStart: that.data.busTimeStart,
      busTimeEnd: that.data.busTimeEnd,
      busState: that.data.busState,
      busDetail: that.data.busDetail,
      picUrl: that.data.picUrl,
      bussId: wx.getStorageSync('userMess').bussId,
      //bussId:30,
      busLong: that.data.busLong,
      busLat: that.data.busLat,
      busPicUrl: that.data.imgAr1
    }
    //console.log(data);
    //return null;
    var url = app.globalData.baseUrl + '/webapi/qztBusiness/updateBussinessMess';    
    util.httpPost(url, data).then(res => {
      var data = res.data;
      if (data.code == '200') {       
        wx.showToast({
          title: "提交成功",
          icon: 'none'
        })
        setTimeout(function () {
          //要延时执行的代码
          wx.switchTab({
            url: '../my/my'
          })
        }, 1500) //延迟时间 这里是1秒
      }

    })

  }
})