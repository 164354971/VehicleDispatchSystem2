import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
// main.js，注意要在use方法之后执行
import uView from 'uview-ui'

Vue.use(uView)

// 如此配置即可
uni.$u.config.unit = 'rpx'
Vue.config.productionTip = false
//Vue.prototype.baseURL = 'https://lingbao-crisps.cn:9000'
Vue.prototype.staticURL = 'https://lingbao-crisps.cn/static'
Vue.prototype.baseURL = 'http://192.168.10.10:9000' 
Vue.prototype.wsURL = 'ws://192.168.10.10:9999/globalWs/message?token='
Vue.prototype.getToken = function (){
  //
	let user = uni.getStorageSync("user");
	let token;
	if(this.isValue(user)){
		token = user.token
	}else{
		token = null;
	}
	//console.log(token);
	return token;
}
Vue.prototype.isValue = function (obj){
	if(obj == undefined || obj == null || obj == '' || obj == JSON.stringify(obj))
		return false;
	return true;
}
Vue.prototype.timestampToTime = function(timestamp) {
    let date = new Date();//时间戳为10位需*1000，时间戳为13位的话不需乘1000
	date.setTime(timestamp)
    let Y = date.getFullYear() + '-';
    let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    let D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
    let h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    let m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
	//console.log(Y + M + D + h + m + s)
    return Y + M + D + h + m + s;
}
Vue.prototype.timeToTimestamp = function(time){
    let timestamp = Date.parse(new Date(time).toString());
    //timestamp = timestamp / 1000; //时间戳为13位需除1000，时间戳为13位的话不需除1000
    console.log(time + "的时间戳为：" + timestamp);
    return timestamp;
    //2021-11-18 22:14:24的时间戳为：1637244864707
}

Vue.prototype.compareTime = function(timestamp) {
	let time = this.timestampToTime(timestamp);
    let nowTime = this.timestampToTime(Date.parse(new Date()));
	let Y = time.substring(0, 4);
	let nY = nowTime.substring(0, 4);
	let M = time.substring(5, 7);
	let nM = nowTime.substring(5, 7);
	let D = time.substring(8, 10);
	let nD = nowTime.substring(8, 10);
	let h = time.substring(11, 13);
	let nh = nowTime.substring(11, 13);
	let m = time.substring(14, 16);
	let nm = nowTime.substring(14, 16);
	if(Y == nY){
		if(M == nM){
			if(D == nD){
				return h + ':' + m;
			}
			return M + '-' + D + ' ' + h + ':' + m;
		}
		return M + '-' + D;
	}
	return Y + '-' + M + '-' + D;
    
}


//日期比较的方法
Vue.prototype.compare = function (date1) {
	let dates1 = new Date(date1);
	let dates2 = new Date();
	console.log(dates1)
	console.log(dates2)
	if (dates1 > dates2) {
		return true
	} else {
		return false
	}
}

 Vue.prototype._strMapToObj = function(strMap){
    let obj= Object.create(null);
    for (let[k,v] of strMap) {
      obj[k] = v;
    }
    return obj;
}
  /**
  *map转换为json
  */
Vue.prototype._mapToJson = function(map) {
  return JSON.stringify(this._strMapToObj(map));
}

Vue.prototype._objToStrMap = function(obj){
  let strMap = new Map();
  for (let k of Object.keys(obj)) {
    strMap.set(k,obj[k]);
  }
  return strMap;
}
 /**
  *json转换为map
  */
Vue.prototype._jsonToMap = function(jsonStr){
    return this._objToStrMap(JSON.parse(jsonStr));
}

Vue.prototype.isUserLogin = async function (){
	let that = this;
	let user = uni.getStorageSync("user");
	console.log(user);
	if(!this.isValue(user)){
		user = {};
		user.id = 0;
	}
	await uni.request({
		url: this.baseURL + "/login/isUserLogin?id=" + user.id,
		withCredentials: true,
		xhrFields: {
			withCredentials: true
		},
		method: 'GET', //请求方式，必须为大写
		success: (res) => {
			console.log('/login/isUserLogin', res.data);
			
			if (res.data.code == 1) {
				user = res.data.data;
				console.log("user.salary", user.salary)
				uni.setStorageSync("user", user)
				uni.setStorageSync("activeRadio", "用户");
			} else {
				uni.showToast({
					title: '用户未登录，即将跳转登录页',
					icon: 'none',
					duration: 2000
				});
				uni.removeStorageSync("user")
			}
		},
		complete() {
		}
	})

	
	
}
App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif