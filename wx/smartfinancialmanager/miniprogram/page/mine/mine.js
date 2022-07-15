// 获取应用实例
const app = getApp()

Page({

  data: {
    openid:""
  },

  onLoad() {
    let openid = wx.getStorageSync("openid");

    if (!openid) {
      wx.navigateTo({
        url: '/page/login/login',
      })
    }else{
      console.log("已登录,openid:", openid)
      this.setData({
        openid: openid
      })
    }
  }

})
