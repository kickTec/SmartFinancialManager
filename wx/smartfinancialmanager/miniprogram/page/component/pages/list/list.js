// 获取应用实例
var app = getApp()
Page({
  data: {
    ets: [],
    baseUrl: app.globalData.baseUrl
  },
  onLoad: function() {
    // this.loading();
    var that = this;
    that.loadPer();
  },
  // 加载艺人列表
  loadPer: function() {
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/list/', // 接口地址
      method: 'GET',
      header: {
        'content-type': 'application/json' //默认值
      },
      // 成功
      success: function(res) {
        console.log("加载艺人列表 成功", res.data.data);
        that.setData({
          ets: res.data.data
        });
      },
      // 失败
      fail: function(err) {
        console.log("加载艺人列表 失败", err);
      }
    })
  },

  // 下拉刷新
  onPullDownRefresh: function() {
    console.log("下拉刷新")
    var that = this;
    wx.showNavigationBarLoading()

    setTimeout(() => {
      that.loadPer();
      wx.hideNavigationBarLoading()
      wx.stopPullDownRefresh()
    }, 2000)
  },

  seePerson: function(e) {
    // if (!e.currentTarget.id == "") {
    //   wx.navigateTo({
    //     url: "../person/person?id=" + e.currentTarget.id
    //   })
    //   console.log(e)
    // } else {
    //   console.log("无内容")
    // }

    if (!e.currentTarget.id == "") {
      wx.navigateTo({
        url: "../detailjiemu/detailjiemu?id=" + e.currentTarget.id
      })
      console.log(e)
    } else {
      console.log("无内容")
    }
  }
})
