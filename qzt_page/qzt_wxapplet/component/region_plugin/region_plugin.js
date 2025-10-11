// component/region_plugin/region_plugin.js
var address = require('../../utils/address.min.js');

Component({
  /**
   * 组件的属性列表
   */
  properties: {
    areaCode: String
  },
  /**
   * 组件的初始数据
   */
  data: {
    multiIndex: [0, 0, 0], //地区选择器0,1,2列默认索引
    multiArray: [], //地区选择器0,1,2列name
    multiArrayInfo: [], //地区选择器0,1,2列详细信息
    proCityArea: "", //地区选择结果name
    areaCode: ""
  },
  //组件生命周期函数-在组件实例刚刚被创建时执行，注意此时不能调用 setData )
  created: function(e) {

  },
  //组件生命周期函数-在组件布局完成后执行)
  ready: function(e) {
   
  },
  /**
   * 组件的方法列表
   */
  methods: {
    initializeArea: function(e) { //初始化默认索引值.
      let areaCode = this.data.areaCode;
      var multiIndex = address.initializeArea(areaCode);
      console.log(multiIndex);
      this.setData({
        multiIndex: multiIndex
      });
      this.areaDispose();
      if (areaCode != null && areaCode != "") {
        this.selectAreaChange();
      }
    },
    areaDispose: function(e) { //地区处理
      wx.showLoading({
        title: '加载中',
        mask: true
      });
      this.setData(address.areaDispose(this.data.multiIndex));
      wx.hideLoading();
    },
    selectAreaColumnChange: function(e) { //列改动监听 列：e.detail.column  值：e.detail.value
      var data = {
        multiIndex: this.data.multiIndex
      };
      if (e.detail.column == 0) {
        data.multiIndex[e.detail.column] = e.detail.value;
        data.multiIndex[1] = 0;
        data.multiIndex[2] = 0;
      } else {
        data.multiIndex[e.detail.column] = e.detail.value;
      }
      this.setData(data);
      this.areaDispose();
    },
    selectAreaChange: function(e) { //确认选择
      var multiIndex = this.data.multiIndex;
      if (e != undefined) {
        multiIndex = e.detail.value;
      }
      var pinfo = this.data.multiArrayInfo[0][multiIndex[0]];
      var cinfo = this.data.multiArrayInfo[1][multiIndex[1]];
      var ainfo = this.data.multiArrayInfo[2][multiIndex[2]];
      var data = {
        multiIndex: multiIndex,
        zipCode: ainfo.zipCode,
        areaCode: ainfo.areaCode,
        proCityArea: pinfo.areaName + cinfo.areaName + ainfo.areaName
      };
      this.setData(data);
      var myEventDetail = { // detail对象，提供给事件监听函数
        zipCode: ainfo.zipCode,
        areaCode: ainfo.areaCode,
        proCityArea: pinfo.areaName + cinfo.areaName + ainfo.areaName
      }
      var myEventOption = { //触发事件的选项 详见微信文档
      }
      this.triggerEvent('myevent', myEventDetail, myEventOption);
    },
    //接收使用组件页面的地址数据
    receivesTheValue: function(e) {
      this.setData({
        areaCode: e
      });
      this.initializeArea(); //初始化地址
    }

  }

})