// 获取应用实例
const app = getApp()

Page({

  data: {
    openid:"",
    version:"1.0.7",
    cleanTime: 5000,
    jumpFlag: false,
    fundList: [],
    dataHeadArray: ['result1', 'result2', 'result3', 'result4', 'result5', 'result6', 'result7', 'result8', 'result9', 'result10', 'result11', 'result12', 'result13', 'result14', 'Summary', 'condition1', 'condition2', 'condition3', 'condition4', 'result15', 'result16', 'result17', 'result18', 'result19', 'result20']
  },

  onLoad() {
    this.queryAndJump();
  },

  onShow(){
    var pages = getCurrentPages();
    var currPage = pages[pages.length - 1]; //当前页面
  },

  queryAndJump(){
    let openid = wx.getStorageSync("openid");
    if (openid == "onwab5X3Gyi_oH-xMPA0Qkux5PzA") {
      this.setData({
        jumpFlag:true,
        dataHeadArray: ['近2日涨幅', '昨日股价', '今日最低', '今日预估', '今日股价','今日最高']
      });
      this.cacheQuery();
      let that = this;
      setInterval(function () {
        that.cacheQuery();
      }, 5000);
    } else {
      this.queryOpenId();
    }
  },

  queryOpenId(){
    let that = this;
    app.getUserOpenIdViaCloud()
      .then(openid => {
        that.setData({
          openid: openid
        });
        wx.setStorage({
          key: 'openid',
          data: openid
        });
      }).catch(err => {
        console.error(err)
      })
    setTimeout(()=>{
      that.setData({
        openid: ""
      });
    }, this.data.cleanTime);  
  },

  // 点击复制
  copyText: function (event) {
    // 下方为微信开发文档中的复制 API
    wx.setClipboardData({
      data: this.data.openid, //复制的数据
      success: function (res) {
        wx.showToast({
          title: '已复制到剪贴板',
        })
      }
    })
  },

  // 用户点击右上角 分享给好友
  onShareAppMessage: function () {
    let path = `page/mine/mine`;
    let title = "无忧石金"
    return {
      title: title,
      path: path
    }
  },

  // 分享到朋友圈
  onShareTimeline: function () {

  },

  onTabItemTap(item) {
    if (item.pagePath == "page/mine/mine"){
      this.queryAndJump();
    }
  },

  // 跳转到详情
  gotoDetail(e) {
    if (e.currentTarget.id !== '') {
      wx.navigateTo({
        url: '../fund-detail/fund-detail?id=' + e.currentTarget.id
      })
    } else {
      console.log('无内容')
    }
  },

  loadFund() {
    // http://www.kenick.top/fund/queryfundinfolist
  },

  cacheQuery() {
    let needQuery = false;
    let fundResponse = wx.getStorageSync("fundResponse");
    if (fundResponse && fundResponse.data && fundResponse.data.length > 0) {
      if (Date.now() - fundResponse.queryTimeStamp < 1000 * 20) {
        this.setData({
          fundList: fundResponse.data
        })
      } else {
        needQuery = true;
      }
    } else {
      needQuery = true;
    }

    if (needQuery) {
      this.loadFund();
    }
  },

  loadFund() {
    const that = this;

    // http://www.kenick.top/fund/queryfundinfolist
  }

})
