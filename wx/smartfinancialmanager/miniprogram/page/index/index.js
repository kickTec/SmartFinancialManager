// 获取应用实例
var app = getApp();

Page({

  data: {
    dataArray: ['日复利', '年复利'],
    cycleArray: ['周期-天', '周期-年'],
    type: 1,
    finalMoney: '',
    initMoney: 10000,
    rate: 3,
    cycle: 3
  },

  onLoad: function (options) { 
    this.calcCycleMoney();
  },

  inputBlur(option) {
    if (option){
      let blurType = option.currentTarget.dataset.blurtype;
      if (blurType == "type") {
        this.setData({
          "type": parseInt(option.detail.value)
        });
      }
      if (blurType == "initMoney") {
        this.setData({
          "initMoney": parseFloat(option.detail.value)
        });
      }
      if (blurType == "rate") {
        this.setData({
          "rate": parseFloat(option.detail.value)
        });
      }
      if (blurType == "cycle") {
        this.setData({
          "cycle": parseFloat(option.detail.value)
        });
      }
      
      this.calcCycleMoney();
    }
  },

  formSubmit(e) {
    let type = parseInt(e.detail.value.type);
    let initMoney = parseFloat(e.detail.value.initMoney);
    let rate = parseFloat(e.detail.value.rate);
    let cycle = parseInt(e.detail.value.cycle);
    this.setData({
      "type":type,
      "initMoney": initMoney,
      "rate": rate,
      "cycle": cycle
    });

    this.calcCycleMoney();
  },

  calcCycleMoney() {
    let initMoney = this.data.initMoney;
    let finalMoney = initMoney;
    if (this.data.type == 0) {
      for (let i = 0; i < this.data.cycle; i++) {
        let gain = (initMoney * this.data.rate * 0.01) / 365;
        finalMoney = initMoney + gain;
        initMoney = finalMoney;
      }
    } else if (this.data.type == 1) {
      for (let i = 0; i < this.data.cycle; i++) {
        let gain = initMoney * this.data.rate * 0.01;
        finalMoney = initMoney + gain;
        initMoney = finalMoney;
      }
    }

    this.setData({
      "finalMoney": finalMoney.toFixed(2)
    });
  },

  // 用户点击右上角 分享给好友
  onShareAppMessage: function () {
    let path = `/page/index/index`;
    let title = '复利计算';
    return {
      title: title,
      path: path
    }
  },

  // 分享到朋友圈
  onShareTimeline: function () {
    return {
      title: '复利计算'
    }
  }

})
