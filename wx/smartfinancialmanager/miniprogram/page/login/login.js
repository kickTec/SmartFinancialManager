// 获取应用实例
const app = getApp()

Page({

  data: {
    loading: false
  },

  onLoad() {
  },

  onGetOpenid() {
    this.setData({
      loading: true
    });
    app.getUserOpenIdViaCloud()
      .then(openid => {
        this.setData({
          loading: false
        });
        wx.setStorageSync('openid', openid);

        setTimeout(() => {
          wx.navigateBack({
            complete: (res) => { },
          })
        }, 500);
        return openid
      })
      .catch(err => {
        console.error(err)
      })
  }
})
