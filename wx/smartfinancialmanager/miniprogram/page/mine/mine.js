// 获取应用实例
const app = getApp()

Page({

  data: {
    openid:"查询成功5秒后将自动清除",
    version:"1.0.5"
  },

  onLoad() {

  },

  queryOpenId(){
    let that = this;
    app.getUserOpenIdViaCloud()
      .then(openid => {
        that.setData({
          openid: openid
        });
      }).catch(err => {
        console.error(err)
      })
    setTimeout(()=>{
      that.setData({
        openid: "查询成功5秒后将自动清除!"
      });
    },5000);  
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

  }

})
