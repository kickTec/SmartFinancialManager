const app = getApp()

Page({

  data: {
    openid: '',
    hasLogin: false,
    loading: false
  },

  onShareAppMessage() {
    return {
      title: '自选-无忧石金',
      path: 'page/my/pages/myselect/myselect'
    }
  },

  onLoad() {
    this.setData({
      openid: app.globalData.openid
    })
    if (app.globalData.hasLogin) {
      this.loadFund()
    }
  },

  onGetOpenid() {
    const that = this
    this.setData({
      loading: true
    })
    app.getUserOpenIdViaCloud()
      .then(openid => {
        this.setData({
          openid,
          loading: false,
          hasLogin: true
        })
        app.globalData.openid = openid
        app.globalData.hasLogin = true
        that.loadFund()
        return openid
      })
      .catch(err => {
        console.error(err)
      })
  },

  onPullDownRefresh() {
    const that = this
    wx.showNavigationBarLoading()

    setTimeout(() => {
      that.loadFund()
      wx.hideNavigationBarLoading()
      wx.stopPullDownRefresh()
    }, 500)
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
      console.log('response:', response)
      if (response !== null && response.flag === true) {
        that.setData({
          fundList: response.data
        })
      } else {
        wx.showToast({
          title: '服务器连接异常',
          icon: 'success',
          duration: 500
        })
        console.log('服务器连接异常')
      }
      return 1
    }).catch(error => { console.log('error', error) })
  }
})
