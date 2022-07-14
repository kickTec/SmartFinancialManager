// 获取应用实例
const app = getApp()
Page({
  data: {
    ets: [],
    fundList: [],
    baseUrl: app.globalData.baseUrl
  },

  onLoad() {
    const that = this;
    that.loadFund()
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

  onShareAppMessage() {
    return {
      title: '基金-无忧石金',
      path: 'page/my/pages/fund/fund'
    }
  },

  callKenickFunction() {
    wx.cloud.callFunction({
      name: 'httpkenick',
      data: {
        url: 'http://www.kenick.top/fund/queryfundinfolist',
        paramJson: {
          data: '123'
        }
      }
    }).then(res => {
      console.log('result:', res.result)
      return 1
    }).catch(error => { console.log('error', error) })
  }
})
