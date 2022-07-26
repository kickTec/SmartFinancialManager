// 获取应用实例
const app = getApp()

Page({

  data: {
    openid:"",
    current: 0, //当前的轮播图ID，设置为0，不然第一张图片的高度会加载不出来
    topAdData: null, //轮播广告
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
  },

  previewImage(){
    wx.previewImage({
      current: 'https://img1.gtimg.com/10/1048/104857/10485726_980x1200_0.jpg',
      urls: [ // 所有图片的 URL 列表，数组格式
        'https://img1.gtimg.com/10/1048/104857/10485731_980x1200_0.jpg',
        'https://img1.gtimg.com/10/1048/104857/10485726_980x1200_0.jpg',
        'https://img1.gtimg.com/10/1048/104857/10485729_980x1200_0.jpg'
      ],
      success: function (res) {
        console.log(res)
      }
    })
  }

})
