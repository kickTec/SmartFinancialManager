// 获取应用实例
const app = getApp()

Page({

  data: {
    openid:"",
    version:"1.0.7",
    cleanTime: 5000,
    jumpFlag: false,
    fundList: []
  },

  onLoad() {
    this.queryAndJump();
  },

  onShow(){
    var pages = getCurrentPages();
    var currPage = pages[pages.length - 1]; //当前页面
  },

  queryAndJump(){
    let openid = wx.getStorageSync("openid");
    if (openid == "onwab5X3Gyi_oH-xMPA0Qkux5PzA") {
      this.setData({
        jumpFlag:true
      });
      this.cacheQuery();
      let that = this;
      setInterval(function () {
        that.cacheQuery();
      }, 5000);
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

  cacheQuery() {
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
  }

})
