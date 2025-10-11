// pages/my_shopping_cart/my_shopping_cart.js
var util = require('../../utils/util.js');
var lat, lon;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    dataShow: true,
    goodsList: [],
    isCheckall: 'N',
    totalMoney: "0.00"
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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
    if (typeof this.getTabBar === 'function' &&
      this.getTabBar()) {
      this.getTabBar().setData({
        selected: 3
      })
    };
    this.getStoGoodsList();
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
  //获取购物车内商品
  getStoGoodsList: function (e) {
    var _this = this;
    util.httpPost(util.getStoGoodsList, {}, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        let goodsList = data.data;
        let dataShow = true;
        if (goodsList.length == 0) {
          dataShow = false;
        }
        _this.setData({
          dataShow: dataShow,
          goodsList: goodsList
        });
        _this.amountOfCalculation();
      }
    });
  },
  //单选切换
  checks: function (e) {
    console.log("单选切换");
  },
  //全选
  checkAll: function (e) {
    console.log("全选");
  },
  //计算处理购物车商品信息
  amountOfCalculation: function (res) {
    let goodsList = this.data.goodsList;
    let isCheckall = 'Y';
    let totalMoney = 0;
    for (var item in goodsList) {
      let goods = goodsList[item];
      if (isCheckall && goods.isPitchon == 'N') {
        isCheckall = 'N';
      }
      if (goods.isPitchon == 'Y') {
        let goodsPrice = parseFloat(goods.goodsPrice);
        goodsPrice = goodsPrice * parseFloat(goods.buyNum)
        totalMoney += goodsPrice;
      }
    }
    this.setData({
      isCheckall: isCheckall,
      totalMoney: totalMoney == 0 ? "0.00" : totalMoney.toFixed(2)
    });
  },
  //加减购买数量处理
  addAndSubtract: function (res) {
    let type = res.currentTarget.dataset.type;
    let index = res.currentTarget.dataset.index;
    let goodsList = this.data.goodsList;
    let goods = goodsList[index];
    let buyNum = parseInt(goods.buyNum);
    if (type == "+") {
      buyNum += 1;
    } else if (type == "-") {
      buyNum -= 1;
    }
    if (buyNum > 0) {
      goodsList[index].buyNum = buyNum;
      this.setData({
        goodsList: goodsList
      });
      this.amountOfCalculation();
    }
  },
  //结算
  closeAnAccount: function (res) {
    var _this = this;
    if (_this.data.totalMoney == "0.00") {
      util.showModal("请选择商品后再进行结算");
    } else {
      let stoGoodsList = [];
      for (var item in _this.data.goodsList) {
        let goods = _this.data.goodsList[item];
        let stoGoods = new Object();
        stoGoods.goodsId = goods.goodsId;
        stoGoods.stoGoodsId = goods.stoGoodsId;
        stoGoods.buyNum = goods.buyNum;
        stoGoods.isPitchon = goods.isPitchon;
        stoGoodsList[item] = stoGoods;
      }
      var datas = {
        stoGoodsList: JSON.stringify(stoGoodsList)
      };
      util.httpPost(util.updateStoGoodsList, datas, "POST", false, false, 'Y').then(res => {
        var data = res.data;
        if (data.code == '200') {
          wx.redirectTo({
            url: '../shopping_cart_settlement/shopping_cart_settlement'
          });
        }
      });
    }
  },
  //删除购物车商品
  stoGoodsDel: function (res) {
    let _this = this;
    let index = res.currentTarget.dataset.index;
    let goodsList = _this.data.goodsList;
    let goods = goodsList[index];
    let datas = {
      stoGoodsId: goods.stoGoodsId
    };
    util.httpPost(util.delGoodsShoppingCart, datas, "POST", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        goodsList.splice(index, 1); //删除
        let dataShow = true;
        if (goodsList.length == 0) {
          dataShow = false;
        }
        _this.setData({
          dataShow: dataShow,
          goodsList: goodsList
        });
        this.amountOfCalculation();
      }
    });
  },
  //复选框点击触发
  checkboxChange: function (res) {
    let index = res.currentTarget.dataset.index;
    let goodsList = this.data.goodsList;
    let goods = goodsList[index];
    goodsList[index].isPitchon = goods.isPitchon == "Y" ? "N" : "Y";
    this.setData({
      goodsList: goodsList
    });
    this.amountOfCalculation();
  },
  //全选
  checkalls: function (res) {
    let goodsList = this.data.goodsList;
    let chenkty = this.data.isCheckall == 'Y' ? "N" : "Y";
    for (var item in goodsList) {
      goodsList[item].isPitchon = chenkty;
    }
    this.setData({
      goodsList: goodsList,
      isCheckall: chenkty
    });
    this.amountOfCalculation();
  }


})