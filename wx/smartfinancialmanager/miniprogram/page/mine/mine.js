// 获取应用实例
const app = getApp()
Page({
  data: {
    ets: [],
    fundList: [],
    baseUrl: app.globalData.baseUrl,
    openid: app.globalData.openid,
    hasLogin: app.globalData.hasLogin,
    loading: false
  },

  onLoad() {
    console.log("openid:", app.globalData.openid)
    this.setData({
      openid: app.globalData.openid
    })
    if (app.globalData.hasLogin) {
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
  },

  // 下拉刷新
  onPullDownRefresh() {
    const that = this
    wx.showNavigationBarLoading()

    setTimeout(() => {
      that.loadFund()
      wx.hideNavigationBarLoading()
      wx.stopPullDownRefresh()
    }, 2000)
  },

  seePerson(e) {
    if (e.currentTarget.id !== '') {
      wx.navigateTo({
        url: '../detailjiemu/detailjiemu?id=' + e.currentTarget.id
      })
      console.log(e)
    } else {
      console.log('无内容')
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
        });
        console.log("openid:", openid);
        app.globalData.openid = openid
        app.globalData.hasLogin = true
        that.loadFund()
        return openid
      })
      .catch(err => {
        console.error(err)
      })
  }
})
