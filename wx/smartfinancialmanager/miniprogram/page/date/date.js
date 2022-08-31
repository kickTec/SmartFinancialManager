// 获取应用实例
let app = getApp();
let dateUtil = require('../../util/dateUtil');

Page({
  data: {
    dataArray: ['日复利', '年复利'],
    cycleArray: ['周期-天', '周期-年'],
    index: 0,
    finalDate: '',
    initDate: '',
    dayDiff: -14,
    cycle: 365
  },

  onLoad: function (options) {
    let curDate = dateUtil.getShortDate(dateUtil.getCurrentDatetime());

    let initDate = curDate;
    let dayDiff = parseInt(this.data.dayDiff);
    let finalDate = dateUtil.addDate(initDate, dayDiff);
    this.setData({
      initDate: curDate,
      finalDate: finalDate
    });
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
    let initDate = e.detail.value.initDate;
    let dayDiff = parseInt(e.detail.value.dayDiff);
    let finalDate = dateUtil.addDate(initDate, dayDiff);
    this.setData({
      "finalDate": finalDate
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
