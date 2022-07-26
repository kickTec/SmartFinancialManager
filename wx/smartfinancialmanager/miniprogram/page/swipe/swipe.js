// page/swipe/swipe.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    statusBar: app.globalData.statusBar,
    imgheights: [], //轮播图高度列表
    current: 0, //当前的轮播图ID，设置为0，不然第一张图片的高度会加载不出来
    topAdData: null, //轮播广告
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 设置轮播图片
    let array = [];
    for(let i=0; i<4; i++){
      let tmp={};
      tmp.picturePath = '/image/ad'+ (i+1) +'.jpg';
      array.push(tmp);
    }
    this.setData({
      topAdData: array
    });
  },

  //图片加载函数，获取每张图片的真实高度，按比例兑换成显示出来的高度，存储下来，以自适应轮播图高度
  imageLoad: function (e) { //获取图片真实宽度  
    var imgwidth = e.detail.width,
    imgheight = e.detail.height,
    //宽高比  
    ratio = imgwidth / imgheight;
    //计算的高度值
    var viewHeight = app.globalData.systemInfo.windowWidth / ratio;

    //把每一张图片的对应的高度记录到数组里  
    let imgheights = this.data.imgheights
    imgheights.push(viewHeight);
    this.setData({
      imgheights: imgheights
    })
  },

  onSlideClick: function (event){
    let item = event.currentTarget.dataset.item;
    let index = event.currentTarget.dataset.index;
    console.log("item:", item);
    console.log("index:", index);
  }

})
