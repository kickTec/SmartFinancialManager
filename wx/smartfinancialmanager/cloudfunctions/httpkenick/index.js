// 云函数入口文件
const cloud = require('wx-server-sdk')
var rp = require('request-promise');

cloud.init()

// 云函数入口函数
exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()

  let url = 'http://www.kenick.top/fund/queryfundinfolist';

  return await rp(url)
    .then(function (res) {
      return res
    })
    .catch(function (err) {
      return '失败'
    });
}