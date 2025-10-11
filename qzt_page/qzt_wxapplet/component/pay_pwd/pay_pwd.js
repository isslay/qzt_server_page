// component/pay_pwd/pay_pwd.js
const md5 = require('../../utils/md5.js');
Component({
  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {
    showPayPwdInput: false, //是否展示密码输入层
    pwdVal: '', //输入的密码
    payFocus: true, //文本框焦点
  },

  /**
   * 组件的方法列表
   */
  methods: {
    /**
     * 显示支付密码输入层
     */
    showInputLayer: function() {
      this.setData({
        showPayPwdInput: true,
        payFocus: true
      });
    },
    /**
     * 隐藏支付密码输入层
     */
    hidePayLayer: function() {
      var val = this.data.pwdVal;
      this.setData({
        showPayPwdInput: false,
        payFocus: false,
        pwdVal: ''
      });
    },
    /**
     * 获取焦点
     */
    getFocus: function() {
      this.setData({
        payFocus: true
      });
    },
    /**
     * 输入密码监听
     */
    inputPwd: function(e) {
      this.setData({
        pwdVal: e.detail.value
      });
      console.log();
      if (e.detail.value.length >= 6) {
        var myEventDetail = { // detail对象，提供给事件监听函数
          payPwd: md5.hexMD5(e.detail.value)
        }
        var myEventOption = { //触发事件的选项 详见微信文档
        }
        this.triggerEvent('myevent', myEventDetail, myEventOption);
        this.hidePayLayer();
      }
    },
    //忘记密码
    forgetPwd: function(res) {
      wx.navigateTo({
        url: ""
      });
    }
  }
});