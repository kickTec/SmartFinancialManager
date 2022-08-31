
/**
 * 获取短日期字符串 2016-01-05
 * dateStr 2018/1/18/ 23:50:20
 */
const getShortDate = (dateStr) => {
  if (!dateStr || dateStr == '' || dateStr == null) {
    return '';
  }else if (dateStr.indexOf(' ') > 0) {
    let index = dateStr.indexOf(' ');
    let resultStr = dateStr.substring(0, index)
    return resultStr;
  }
  return '';
}

/**
 * 获取当前的完整日期(如:2018/1/18/ 23:50:20)
 */
const getCurrentDatetime = () => {
  let date = new Date();
  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let day = date.getDate();
  let hour = date.getHours();
  let minute = date.getMinutes();
  let second = date.getSeconds();
  return year + '/' + month + '/' + day + ' ' + hour + ':' + minute + ':' + second;
}

/**
 * 两个日期相差数，返回负天数表示已超过指定日期，正天数表示还没到指定时间
 * @param dateBegin 开始日期
 * @param dateEnd 结束日期
 * @returns 相差的天数，小时数，分钟数，秒数
 */
const dateDiff = (dateBegin, dateEnd) => {
  dateBegin = dateBegin.replace(/-/g, '\/');
  dateEnd = dateEnd.replace(/-/g, '\/');
  let date1 = new Date(dateBegin);
  let date2 = new Date(dateEnd);

  let d1 = date1.getTime();
  let d2 = date2.getTime();
  let total = 0;
  if (d1 > d2) {
    total = (d1 - d2) / 1000;
  } else {
    total = (d2 - d1) / 1000;
  }

  let day = Math.floor(Number(total / (24 * 60 * 60))); //计算整数天数
  let afterDay = Math.floor(Number(total - day * 24 * 60 * 60)); //取得算出天数后剩余的秒数
  let hour = Math.floor(Number(afterDay / (60 * 60))); //计算整数小时数
  let afterHour = Math.floor(Number(total - day * 24 * 60 * 60 - hour * 60 * 60)); //取得算出小时数后剩余的秒数
  let minute = Math.floor(Number(afterHour / 60)); //计算整数分
  let second = Math.floor(Number(total - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60)); //取得算出分后剩余的秒数

  day = d1 > d2 ? -day : day;
  return {
    total: d1 - d2,
    day: day,
    hour: hour,
    minute: minute,
    second: second
  }
}

/**
 * 增加日期 
 * date 2022/8/4
 * diffNum 日期差值
 */
function addDate(date, diffNum) {
  let d = new Date(date.replace(/-/g, '\/'));
  let day = d.setDate(d.getDate() + diffNum);
  day = d.getDate();
  let month = d.getMonth() + 1;

  if (day < 10) {
    day = "0" + day;
  }

  if (month < 10) {
    month = "0" + month;
  }

  return d.getFullYear() + '-' + month + '-' + day;
}

/**
 * 获取过去第dayDiff天
 */
function getPastDateZero(dayDiff) {
  return addDate(this.getShortDate(this.getCurrentDatetime()), -dayDiff) + " 00:00:00";
}

module.exports = {
  getShortDate,
  getCurrentDatetime,
  dateDiff,
  addDate,
  getPastDateZero
}
