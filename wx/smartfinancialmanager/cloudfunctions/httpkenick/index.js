// 云函数入口文件
const cloud = require('wx-server-sdk')
const rp = require('request-promise')

cloud.init()

// 云函数入口函数
exports.main = async () => {
  const url = 'http://www.kenick.top/fund/queryfundinfolist'

  return rp(url)
    .then(function (res) {
      return res
    })
    .catch(function (err) {
      return err.messages
    })
}
