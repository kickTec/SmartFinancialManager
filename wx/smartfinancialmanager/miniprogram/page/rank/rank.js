// 获取应用实例
Page({
  data: {
    ets: [],
    fundList: []
  },

  onLoad() {
    this.cacheQuery();
    let that = this;
    setInterval(function(){
      that.cacheQuery();
    }, 5000);
  },

  cacheQuery(){
    let needQuery = false;
    let fundResponse = wx.getStorageSync("fundResponse");
    if (fundResponse) {
      if (Date.now() - fundResponse.queryTimeStamp < 1000 * 20) {
        this.setData({
          fundList: fundResponse.data
        })
      } else {
        needQuery = true;
      }
    } else {
      needQuery = true;
    }

    if (needQuery) {
      this.loadFund();
    }
  },

  loadFund() {
    const that = this;
    wx.cloud.callFunction({
      name: 'httpkenick',
      data: {
        url: 'http://www.kenick.top/fund/queryfundinfolist',
        paramJson: {
          data: '{"orderBy":"gain_total desc,cur_gain desc"}'
        }
      }
    }).then(res => {
      const response = JSON.parse(res.result);
      response.queryTimeStamp = Date.now();
      wx.setStorage({
        key: 'fundResponse',
        data: response
      });
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
      }
      return 1
    }).catch(error => { 
      wx.showToast({
        title: '服务器连接异常',
        icon: 'success',
        duration: 500
      });
      console.log('error', error);
    })
  },

  // 下拉刷新
  onPullDownRefresh() {

  },

  // 跳转到详情
  gotoDetail(e) {
    if (e.currentTarget.id !== '') {
      wx.navigateTo({
        url: '../fund-detail/fund-detail?id=' + e.currentTarget.id
      })
    } else {
      console.log('无内容')
    }
  },

  // 用户点击右上角 分享给好友
  onShareAppMessage: function () {
    let path = `/page/rank/rank`;
    let title = '';
    return {
      title: title,
      path: path
    }
  },

  // 分享到朋友圈
  onShareTimeline: function () {

  }

})
