// 获取应用实例
var app = getApp();

Page({

  data: {
    dataArray: ['日复利', '年复利'],
    cycleArray: ['周期-天', '周期-年'],
    index: 0,
    finalMoney: '',
    initMoney: 10000,
    rate: 2,
    cycle: 365
  },

  bindViewEvent(data) {
    let indexValue = parseInt(data.detail.value);
    if (indexValue == 1) {
      this.setData({
        cycle: 1
      });
    }
    this.setData({
      index: indexValue
    });
  },

  formSubmit(e) {
    let type = parseInt(e.detail.value.type);
    let initMoney = parseFloat(e.detail.value.initMoney);
    let rate = parseFloat(e.detail.value.rate);
    let cycle = parseInt(e.detail.value.cycle);
    let finalMoney = this.calcCycleMoney(type, initMoney, rate, cycle);
    this.setData({
      "finalMoney": finalMoney
    });
  },

  calcCycleMoney(type, initMoney, rate, cycle) {
    let finalMoney = initMoney;
    if (type == 0) {
      for (let i = 0; i < cycle; i++) {
        let gain = (initMoney * rate * 0.01) / 365;
        finalMoney = initMoney + gain;
        initMoney = finalMoney;
      }
    } else if (type == 1) {
      for (let i = 0; i < cycle; i++) {
        let gain = initMoney * rate * 0.01;
        finalMoney = initMoney + gain;
        initMoney = finalMoney;
      }
    }

    return finalMoney.toFixed(2);
  },

  // 用户点击右上角 分享给好友
  onShareAppMessage: function () {
    let path = `/page/index/index`;
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
