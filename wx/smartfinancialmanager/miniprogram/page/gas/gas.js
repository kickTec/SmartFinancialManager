// page/gas/gas.js
let util = require('../../util/util');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    gas100:5.86,
    gasPrice:8.66,
    distance:6.66
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  formSubmit(e) {
    let gas100 = parseFloat(e.detail.value.gas100);
    let gasPrice = parseFloat(e.detail.value.gasPrice);
    let distance = parseFloat(e.detail.value.distance);
    let cost = (gas100/100)*distance*gasPrice;
    cost = util.setScale(cost, 2);
    this.setData({
      "cost": cost
    });
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  // 分享到朋友圈
  onShareTimeline: function () {

  }  

})
