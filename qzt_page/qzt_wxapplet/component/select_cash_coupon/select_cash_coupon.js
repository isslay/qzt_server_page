// component/select_cash_coupon/select_cash_coupon.js

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
    showModalStatus: false,
    cashCouponId: "",
    coupon: {},
    cashCouponMoney: 0, //券金额
    fullAmountReduction: 0, //满减金额
    couponList: []
  },
  //组件生命周期函数-在组件布局完成后执行)
  ready: function(e) {

  },
  /**
   * 组件的方法列表
   */
  methods: {
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
    //radio监听
    radioChange: function(e) {
      let coupon = this.data.couponList[e.detail.value];
      this.setData({
        coupon: coupon
      });
    },
    //确认
    affirm: function(e) {
      let coupon = {};
      console.log(e.currentTarget.dataset.index);
      if (e.currentTarget.dataset.index != undefined) {
        coupon = this.data.couponList[e.currentTarget.dataset.index];
      }
      this.setData({
        showModalStatus: false,
        coupon: coupon
      });
      var myEventDetail = { // detail对象，提供给事件监听函数
        cashCouponId: coupon.couponId == undefined ? "" : coupon.couponId,
        cashCouponMoney: coupon.couponMoney == 0 ? "" : coupon.couponMoney, //券金额
        fullAmountReduction: coupon.fullAmountReduction == undefined ? "" : coupon.fullAmountReduction //满减金额
      }
      var myEventOption = { //触发事件的选项 详见微信文档
      }
      this.triggerEvent('myevent', myEventDetail, myEventOption);
    },
    //接收使用组件页面的地址数据
    receivesTheValue: function(e) {
      this.setData({
        couponList: e
      });
    },
    //关闭
    closes: function(e) {
      this.setData({
        showModalStatus: false,
      });
    }

  }
})