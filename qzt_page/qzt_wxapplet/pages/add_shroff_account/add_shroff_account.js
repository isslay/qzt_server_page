// pages/xzshdz/xzshdz.js
var util = require('../../utils/util.js');

var urls = util.addShroffAccount;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    currentIndex: 0,
    showModalStatus: false,
    animationData: '',
    isNewModified: true,
    bankIdList: [],
    bankNameList: [],
    id: null,
    shroffAccount: [{
        realName: "",
        bankId: "",
        bankName: "",
        bankBranchName: "",
        cardNum: "",
        bindingTel: "",
        isDefault: "N"
      },
      {
        realName: "",
        cardNum: "",
        bindingTel: "",
        isDefault: "N"
      }
    ],
    index: 0
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.getBankList();
    var id = options.id;
    var titles = "新增收款账号";
    if (id != "") {
      titles = "编辑收款账号";
      this.setData({
        id: id,
        isNewModified: false,
        currentIndex: options.currentIndex
      });
      this.getShroffAccountById();
      urls = util.modifyShroffAccount;
    } else {
      this.setData({
        id: "",
        isOrder: options.isOrder,
        currentIndex: options.currentIndex
      });
    }
    //修改title名称
    wx.setNavigationBarTitle({
      title: titles
    });
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

  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },
  //获取收款账号详情
  getShroffAccountById: function(e) {
    var _this = this;
    var datas = {
      id: _this.data.id
    };
    util.httpPost(util.getShroffAccountById, datas, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        let datas = {};
        if (ret.bankId != null && ret.bankId != "") {
          datas = {
            id: ret.id,
            realName: ret.realName,
            bankId: ret.bankId,
            bankName: ret.bankName,
            bankBranchName: ret.bankBranchName,
            cardNum: ret.cardNum,
            bindingTel: ret.bindingTel,
            isDefault: ret.isDefault
          };
          //初始化银行选则默认
          if (ret.bankId != null && ret.bankId != "") {
            for (var item in _this.data.bankIdList) {
              if (_this.data.bankIdList[item] == ret.bankId) {
                _this.setData({
                  index: item
                });
                break;
              }
            }
          }
        } else {
          datas = {
            id: ret.id,
            realName: ret.realName,
            cardNum: ret.cardNum,
            bindingTel: ret.bindingTel,
            isDefault: ret.isDefault
          };
        }
        let shroffAccount = _this.data.shroffAccount;
        shroffAccount[_this.data.currentIndex] = datas;
        _this.setData({
          shroffAccount: shroffAccount
        });
      }
    });
  },
  //添加/保存
  saveShroffAccount: function(e) {
    var _this = this;

    let shroffAccount = _this.data.shroffAccount;
    let currentIndex = _this.data.currentIndex;
    let sa = shroffAccount[currentIndex];
    if (sa.realName == null || sa.realName == "") {
      util.showModal('请输入您的姓名');
    } else if (currentIndex == 0 && (sa.bankId == null || sa.bankId == "")) {
      util.showModal('请选择银行');
    } else if (currentIndex == 0 && (sa.bankBranchName == null || sa.bankBranchName == "")) {
      util.showModal('请输入开户行信息');
    } else if (sa.cardNum == null || sa.cardNum == "") {
      util.showModal(currentIndex == 1 ? '请输入支付宝账号' : '请输入银行卡号');
    } else if (!/^1(3|4|5|6|7|8|9)\d{9}$/.test(sa.bindingTel)) {
      util.showModal('请输入正确的手机号码');
    } else {
      var datas = sa;
      console.log(datas);
      util.httpPost(urls, datas, "POST", false, false).then(res => {
        var data = res.data;
        if (data.code == '200') {
          var pages = getCurrentPages();   //当前页面
          var prevPage = pages[pages.length - 2];   //上一页面
          prevPage.setData({ //直接给上一个页面赋值
            currentIndex: _this.data.currentIndex
          });
          util.promptJumpNavigateBack(data.message);
        }
      });
    }
  },
  realName: function(e) { //姓名
    let shroffAccount = this.data.shroffAccount;
    shroffAccount[this.data.currentIndex].realName = e.detail.value;
    this.setData({
      shroffAccount: shroffAccount
    });
  },
  bankBranchName: function(e) { //开户行
    let shroffAccount = this.data.shroffAccount;
    shroffAccount[this.data.currentIndex].bankBranchName = e.detail.value;
    this.setData({
      shroffAccount: shroffAccount
    });
  },
  cardNum: function(e) { //号
    let shroffAccount = this.data.shroffAccount;
    shroffAccount[this.data.currentIndex].cardNum = e.detail.value;
    this.setData({
      shroffAccount: shroffAccount
    });
  },
  bindingTel: function(e) { //手机号码
    let shroffAccount = this.data.shroffAccount;
    shroffAccount[this.data.currentIndex].bindingTel = e.detail.value;
    this.setData({
      shroffAccount: shroffAccount
    });
  },
  isDefault: function(e) { //是否默认
    let shroffAccount = this.data.shroffAccount;
    shroffAccount[this.data.currentIndex].isDefault = shroffAccount[this.data.currentIndex].isDefault == "Y" ? "N" : "Y";
    this.setData({
      shroffAccount: shroffAccount
    });
  },
  //接收组件数据
  onMyevent: function(e) {
    var data = e.detail;
    this.setData(data);
  },
  //swiper切换时会调用
  pagechange: function(e) {
    if ("touch" === e.detail.source) {
      let currentPageIndex = this.data.currentIndex
      currentPageIndex = e.detail.current
      this.setData({
        currentIndex: currentPageIndex
      })
    }
  },
  //用户点击tab时调用
  titleClick: function(e) {
    let currentPageIndex =
      this.setData({
        //拿到当前索引并动态改变
        currentIndex: e.currentTarget.dataset.idx
      })
  },
  //获取银行列表
  getBankList: function() {
    util.httpPost(util.getBankList, {}, "GET", false, false).then(res => {
      var data = res.data;
      if (data.code == '200') {
        let ret = data.data;
        this.setData({
          bankIdList: ret.bankIdList,
          bankNameList: ret.bankNameList
        });
      }
    });
  },
  //选择银行触发
  bindPickerChange: function(e) {
    let shroffAccount = this.data.shroffAccount;
    shroffAccount[this.data.currentIndex].bankId = this.data.bankIdList[e.detail.value];
    shroffAccount[this.data.currentIndex].bankName = this.data.bankNameList[e.detail.value];
    this.setData({
      index: e.detail.value,
      shroffAccount: shroffAccount
    });
  }


});