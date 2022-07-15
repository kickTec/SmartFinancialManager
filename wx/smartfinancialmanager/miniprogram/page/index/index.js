// 获取应用实例
const app = getApp()
Page({
  data: {
    fundList: []
  },

  onLoad() {
    let openid = wx.getStorageSync("openid");

    if (!openid){
      console.log("未登录,openid is null");
      wx.navigateTo({
        url: '/page/login/login',
      })
    }else{
      console.log("已登录,openid:", openid);
      this.loadFund()
    }
  },

  loadFund() {
    const that = this
    wx.cloud.callFunction({
      name: 'httpkenick',
      data: {
        url: 'http://www.kenick.top/fund/queryfundinfolist',
        paramJson: {
          data: '{"orderBy":"fund_code"}'
        }
      }
    }).then(res => {
      const response = JSON.parse(res.result)
      if (response !== null && response.flag === true) {
        that.setData({
          fundList: response.data
        })
      } else {
        wx.showToast({
          title: '网络繁忙',
          icon: 'success',
          duration: 1000
        })
        console.log('服务器连接异常')
      }
      return 1
    }).catch(error => { console.log('error', error) })
  },

  // 下拉刷新
  onPullDownRefresh() {
    const that = this
    wx.showNavigationBarLoading()

    setTimeout(() => {
      that.loadFund()
      wx.hideNavigationBarLoading()
      wx.stopPullDownRefresh()
    }, 500)
  },

  onShareAppMessage() {
    return {
      title: '无忧石金',
      path: 'page/index/index'
    }
  }

})
