// 获取应用实例
const app = getApp()
Page({
  data: {
    ets: [],
    fundList: [],
    baseUrl: app.globalData.baseUrl
  },

  onLoad() {
    const that = this
    that.loadFund()
  },

  // 加载列表
  loadPer() {
    const that = this
    wx.request({
      url: app.globalData.baseUrl + '/list/', // 接口地址
      method: 'GET',
      header: {
        'content-type': 'application/json'
      },
      success(res) {
        console.log('加载列表成功', res.data.data)
        that.setData({
          ets: res.data.data
        })
      },
      fail(err) {
        console.log('加载列表失败', err)
      }
    })
  },

  loadFund() {
    const that = this
    wx.cloud.callFunction({
      name: 'httpkenick',
      data: {}
    }).then(res => {
      console.log('请求成功，返回:', res.result)
      const response = JSON.parse(res.result)
      console.log('response:', response)
      if (response.flag === true) {
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
    // if (!e.currentTarget.id == "") {
    //   wx.navigateTo({
    //     url: "../person/person?id=" + e.currentTarget.id
    //   })
    //   console.log(e)
    // } else {
    //   console.log("无内容")
    // }

    if (!e.currentTarget.id === '') {
      wx.navigateTo({
        url: '../detailjiemu/detailjiemu?id=' + e.currentTarget.id
      })
      console.log(e)
    } else {
      console.log('无内容')
    }
  }
})
