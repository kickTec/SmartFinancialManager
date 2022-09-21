// page/fund-detail/fund-detail.js
import * as echarts from '../../ec-canvas/echarts';
let util = require('../../util/util');
const app = getApp();

Page({
  data: {
    ec: {
      // 将 lazyLoad 设为 true 后，需要手动初始化图表
      lazyLoad: true
    },
    fundCode:'',
    fundName:'',
    item: null,
    avg1: null,
    avg2: null,
    avg3: null,
    xArray: [],
    yArray: [],
    backArray:[],
    backFinal: null,
    showDetailTip: '展开网格交易明细▼',
    intervalDefault: 0.7,
    labelSelectType: '30day',
    gridQuantity: 100,
    dataHeadArray: ['result1', 'result2', 'result3', 'result4', 'result5', 'result6', 'result7', 'result8', 'result9', 'result10', 'result11', 'result12', 'result13', 'result14', 'Summary', 'condition1', 'condition2', 'condition3', 'condition4', 'result15', 'result16', 'result17', 'result18', 'result19', 'result20']
  },

  onReady(){
    // 获取chart组件
    this.ecComponent = this.selectComponent('#mychart-dom-bar');
  },

  onLoad(options) {
    let openid = wx.getStorageSync("openid");
    if (openid == "onwab5X3Gyi_oH-xMPA0Qkux5PzA") {
      this.setData({
        fundCode: options.id,
        dataHeadArray: ['近2日涨幅', '昨日股价', '昨日最高', '昨日最低', '近30日平均值', '30日低位均值', '30日高位均值', '今日预估', '今日股价', '今日最高', '今日最低', '近60日平均值', '60日低位均值', '60日高位均值', '过往数据网格模拟', '初始价格', '网格间隔', '过往天数', '单次数量', '总收益', '最新基准价', '买入次数', '总手续费', '最后持仓量', '卖出次数']
      });
      this.queryDetail();
    } 
  },

  chooseLabelSelectType(e){
    let type = e.currentTarget.dataset.type;

    this.setData({
      "labelSelectType": type
    });

    if (type == '15day') {
      this.initEcharts("15日走势图", 15);
    }
    if (type == '30day'){
      this.initEcharts("30日走势图", 30);
    }
    if (type == '45day') {
      this.initEcharts("45日走势图", 45);
    }
  },

  clickCopy(){
    wx.setClipboardData({
      data: this.data.item.fundCode, //复制的数据
      success: function (res) {
        wx.showToast({
          title: '已复制到剪贴板',
        })
      }
    })
  },

  queryDetail() {
    // http://www.kenick.top/fund/queryDetail
  },

  initEcharts: function (name, dayNum) {
    this.ecComponent.init((canvas, width, height, dpr) => {
      // 获取组件的 canvas、width、height 后的回调函数
      // 在这里初始化图表
      const chart = echarts.init(canvas, null, {
        width: width,
        height: height,
        devicePixelRatio: dpr // new
      });
      let start = 0;
      if (dayNum < this.data.xArray.length){
        start = (1 - (dayNum / this.data.xArray.length)) * 100;
      }
      setChartOption(chart, name, this.data.xArray, this.data.yArray, start);

      // 将图表实例绑定到 this 上，可以在其他成员函数（如 dispose）中访问
      this.chart = chart;

      this.setData({
        isLoaded: true,
        isDisposed: false
      });

      // 注意这里一定要返回 chart 实例，否则会影响事件处理等
      return chart;
    });
  },

  formSubmit: function(option){
    let initPrice = option.detail.value.initPrice; // 初始价格
    let gridInterval = parseFloat(option.detail.value.gridInterval); // 间隔
    let gridQuantity = parseInt(option.detail.value.gridQuantity); // 数量
    let dayNum = parseInt(option.detail.value.dayNum); // 天数
    if (initPrice == '自动'){
      initPrice = 0;
    }
    const that = this;
    let param = this.data.fundCode + "," + dayNum + "," + initPrice + "," + gridInterval + "," + gridQuantity;
    console.log(param);
    // http://www.kenick.top/grid/backTest
  },

  showDetail:function(options){
    let showTip = options.currentTarget.dataset.showtip;
    let showDetailTip = '隐藏网格交易明细▲';
    if (showTip != '展开网格交易明细▼'){
      showDetailTip = '展开网格交易明细▼';
    }
    this.setData({
      showDetailTip: showDetailTip
    });
  },

  // 用户点击右上角 分享给好友
  onShareAppMessage: function () {
    let path = `/page/fund-detail/fund-detail?id=${this.data.fundCode}`;
    let title = '';
    return {
      title: title,
      path: path
    }
  }

});

// 设置chart选项
function setChartOption(chart, title, xArray, yArray, startX) {
  var option = {
    title: {
      text: title,
      textStyle: {
        align: 'center',
        color: 'black',
        fontSize: 13,
      },
      left: 'center'
    },

    // 折线图线条的颜色
    color: ["#37A2DA", "#67E0E3", "#9FE6B8"],

    // 刻度
    grid: {
      left: '1%',
      right: '3%',
      containLabel: true
    },

    // 悬浮图标
    tooltip: {
      show: true,
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },

    //x坐标轴
    xAxis: {
      type: 'category',
      //x坐标轴
      data: xArray,
      axisTick: {
        show: false // 不显示坐标轴刻度线
      },
      axisLine: {
        show: false, // 不显示坐标轴线
      },
      axisLabel: {
        show: true, // 不显示坐标轴上的文字
        interval: 'auto', // x轴间距
        textStyle: {
          color: '#000',
          fontSize: '8'
        },
        formatter: function (params,index) {
          // 根据展示数据量自定义间隔
          let showNum = xArray.length * (1 - (startX / 100));
          if (showNum > 40) { // 展示数量大于40，间隔3个显示下标
            if (index % 3 != 0) {
              return ''; // 空字符串不会显示
            } 
          } else if (showNum > 20) {// 展示数量21-40，间隔1个显示下标
            if (index % 2 == 0) {
              return '';
            }
          }

          // x轴换行显示
          params = params.replace(/-/g, '');
          params = params.substring(4, params.length);
          var newParamsName = "";
          var paramsNameNumber = params.length;
          var provideNumber = 2; //一行显示几个字
          var rowNumber = Math.ceil(paramsNameNumber / provideNumber);
          if (paramsNameNumber > provideNumber) {
            for (var p = 0; p < rowNumber; p++) {
              var tempStr = "";
              var start = p * provideNumber;
              var end = start + provideNumber;
              if (p == rowNumber - 1) {
                tempStr = params.substring(start, paramsNameNumber);
              } else {
                tempStr = params.substring(start, end) + "\n";
              }
              newParamsName += tempStr;
            }
          } else {
            newParamsName = params;
          }

          return newParamsName;
        }
      },
      splitLine: {
        show: false // 不显示网格线
      }
    },

    //y坐标轴
    yAxis: {
      show: true,
      splitLine: {
        lineStyle: {
          type: 'dotted',//分割线变成虚线
        }
      },
      axisLine: {
        show: false, // 不显示坐标轴线
        lineStyle: {
          opacity: 0,
        }
      },
      axisTick: {
        show: false, // 不显示坐标轴刻度线
      },
      axisLabel: {
        show: true // 不显示坐标轴上的文字
      },
      min: (value) => {
        return Math.floor(value.min)
      },
      max: (value) => {
        return Math.ceil(value.max)
      }
    },

    dataZoom: [
      {
        show: true,
        type: 'inside',
        start: startX,
        end: 100
      }
    ],

    series: [{
      name: '股价',
      type: 'line',
      // 设置折线是否平滑
      smooth: false,
      showAllSymbol: true,
      symbolSize: 1,
      lineStyle: {
        normal: {
          color: "#2B68D4", // 线条颜色
        },
      },
      //对应x轴的y轴数据
      data: yArray,
      markPoint: {
        data: [
          { type: 'max', name: '最大值' },
          { type: 'min', name: '最小值' }
        ]
      },
      markLine: {
        label: {
          position: "middle", //将警示值放在哪个位置，三个值“start”,"middle","end"  开始  中点 结束
        },
        data: [
          { type: 'average', name: '平均值' }
        ]
      }
    }]
  };
  chart.clear();
  chart.setOption(option);
}
