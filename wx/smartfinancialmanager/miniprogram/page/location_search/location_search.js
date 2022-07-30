// page/location_search/location_search.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    longitude: 113.3723,
    latitude: 22.9707
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    /*
    wx.navigateTo({
      url: '/page/swipe/swipe',
    })
    */
  },

  jdBlur: function(options){
    this.setData({
      longitude: options.detail.value
    })
  },

  wdBlur: function (options) {
    this.setData({
      latitude: options.detail.value
    })
  },

  operate: function (options) {
    let optype = options.target.dataset.optype;
    let longitudeNew = parseFloat(this.data.longitude);
    if (optype == 'jd-'){
      longitudeNew = parseFloat(longitudeNew) - 0.0001;
    }
    if (optype == 'jd+') {
      longitudeNew = parseFloat(longitudeNew) + 0.0001;
    }
    let latitudeNew = parseFloat(this.data.latitude);
    if (optype == 'wd-') {
      latitudeNew = parseFloat(latitudeNew) - 0.0001;
    }
    if (optype == 'wd+') {
      latitudeNew = parseFloat(latitudeNew) + 0.0001;
    }
    this.setData({
      longitude: longitudeNew.toFixed(4),
      latitude: latitudeNew.toFixed(4)
    });
  },

  // 用户点击右上角 分享给好友
  onShareAppMessage: function () {
    let path = `page/location_search/location_search`;
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
