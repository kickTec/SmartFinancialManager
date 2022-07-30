// 获取应用实例
const app = getApp()

Page({

  data: {
    openid:"",
    version:"1.0.7",
    cleanTime: 5000
  },

  onLoad() {
    this.queryAndJump();
  },

  queryAndJump(){
    let openid = wx.getStorageSync("openid");
    if (openid == "onwab5X3Gyi_oH-xMPA0Qkux5PzA") {
      wx.navigateTo({
        url: '/page/rank/rank',
      });
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
        if (openid == 'onwab5X3Gyi_oH-xMPA0Qkux5PzA'){
          wx.navigateTo({
            url: '/page/rank/rank',
          })
        }
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
  }

})
