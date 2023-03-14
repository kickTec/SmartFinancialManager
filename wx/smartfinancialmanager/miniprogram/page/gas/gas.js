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
    blurtype:"",
    cost: 0,
    costUnit: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (options && options.shareFlag){ // 分享进入
      if (options.gas100){
        this.setData({
          "gas100": parseFloat(options.gas100)
        });
      }
      if (options.gasPrice){
        this.setData({
          "gasPrice": parseFloat(options.gasPrice)
        });
      }
      if (options.distance) {
        this.setData({
          "distance": parseFloat(options.distance)
        });
      }
    }else{ // 非分享进入
      let gas100 = wx.getStorageSync("gas100");
      if (gas100) {
        this.setData({
          "gas100": parseFloat(gas100)
        });
      }
      let gasPrice = wx.getStorageSync("gasPrice");
      if (gasPrice) {
        this.setData({
          "gasPrice": parseFloat(gasPrice)
        });
      }
    }
    
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
    if (option){
      if(option.currentTarget.dataset.blurtype=="gas100"){
        this.setData({
          "gas100": parseFloat(option.detail.value)
        });
        wx.setStorageSync("gas100", parseFloat(option.detail.value));
      }
      if (option.currentTarget.dataset.blurtype == "gasPrice") {
        this.setData({
          "gasPrice": parseFloat(option.detail.value)
        });
        wx.setStorageSync("gasPrice", parseFloat(option.detail.value));
      }
      if (option.currentTarget.dataset.blurtype == "distance") {
        this.setData({
          "distance": parseFloat(option.detail.value)
        });
      }
    }

    let cost = (this.data.gas100 / 100) * this.data.distance * this.data.gasPrice;
    let costUnit = cost / this.data.distance;
    cost = util.setScale(cost, 2);
    costUnit = util.setScale(costUnit, 2);
    this.setData({
      "cost": cost,
      "costUnit": costUnit
    });
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    let path = "/page/gas/gas?shareFlag=true&gas100=" + this.data.gas100 + "&gasPrice=" + this.data.gasPrice 
      +"&distance="+this.data.distance;
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
