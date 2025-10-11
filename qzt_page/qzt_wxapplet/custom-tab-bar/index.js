// component/tabbar.js
Component({
  data: {
    selected: 0,
    color: "#7A7E83",
    selectedColor: "#22723C",
    list: [
      {
        "pagePath": "/pages/index/index",
        "iconPath": "/img/index.png",
        "selectedIconPath": "/img/index_active.png",
        "text": "首页"
      },
      {
        "pagePath": "/pages/classroom/classroom",
        "iconPath": "/img/classroom.png",
        "selectedIconPath": "/img/classroom_active.png",
        "text": "健康课堂"
      },
      {
        "pagePath": "/pages/servicestation/servicestation",
        "iconPath": "/img/servicestation.png",
        "selectedIconPath": "/img/servicestation_active.png",
        "text": "服务站"
      },
      {
        "pagePath": "/pages/my_shopping_cart/my_shopping_cart",
        "iconPath": "/img/shopping_cart.png",
        "selectedIconPath": "/img/shopping_cart_active.png",
        "text": "购物车"
      },
      {
        "pagePath": "/pages/my/my",
        "iconPath": "/img/my.png",
        "selectedIconPath": "/img/my_active.png",
        "text": "我的"
      }
    ]
  },
  attached() {
  },
  methods: {
    switchTab(e) {
      const data = e.currentTarget.dataset
      const url = data.path
      wx.switchTab({ url })
      this.setData({
        selected: data.index
      })
    }
  }
})
