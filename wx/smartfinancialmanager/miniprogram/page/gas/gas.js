// page/gas/gas.js
let util = require('../../util/util');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    gas100:4.7,
    gasPrice:7.39,
    distance:14,
    blurtype:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.calcGasFee();
  },

  formSubmit(e) {
    this.setData({
      "gas100": parseFloat(e.detail.value.gas100),
      "gasPrice": parseFloat(e.detail.value.gasPrice),
      "distance": parseFloat(e.detail.value.distance)
    });
    this.calcGasFee();
  },

  calcGasFee(option){
    console.log(option);
    if (option){
      if(option.currentTarget.dataset.blurtype=="gas100"){
        this.setData({
          "gas100": parseFloat(option.detail.value)
        });
      }
      if (option.currentTarget.dataset.blurtype == "gasPrice") {
        this.setData({
          "gasPrice": parseFloat(option.detail.value)
        });
      }
      if (option.currentTarget.dataset.blurtype == "distance") {
        this.setData({
          "distance": parseFloat(option.detail.value)
        });
      }
    }

    let cost = (this.data.gas100 / 100) * this.data.distance * this.data.gasPrice;
    cost = util.setScale(cost, 2);
    this.setData({
      "cost": cost
    });
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    let path = `/page/gas/gas`;
    let title = '自驾油费计算';
    return {
      title: title,
      path: path
    }
  },

  // 分享到朋友圈
  onShareTimeline: function () {
    return {
      title: '自驾油费计算'
    }
  }  

})
