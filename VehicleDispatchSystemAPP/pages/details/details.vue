<template>
	<view class="contant">
		<view class="card-car">
			<view class="car-number" :class="car.newEnergy == true ? 'car-green' : 'car-blue'">
				<span>
					{{car.carNumber[0]}}{{car.carNumber[1]}}*****
				</span>
				<span v-if="car.carNumber.length == 8">*</span>
			</view>
			<image class="show" :src='staticURL + car.modelImg'></image>
			<view class="text">
				<p class="title">{{car.name}}</p>
				<span class="type">{{car.type}}/{{car.driverType}}/{{car.displacement}}</span>
				<span class="type">
					{{car.energy}}/{{car.color}}/{{car.year}}
				</span>
			</view> 
			<view class="bottom">
				<view class="bottom-label fl">
					<view class="label vip" v-if="car.noDeposit == true">
						<image src="../../static/details/vip.png"></image>
						<span>会员免押</span>
					</view>
					<view class="label car-luxury" v-if="car.luxury == true">
						<image src="../../static/details/luxury.png"></image>
						<span>豪华品牌</span>
					</view>
					<view class="label energy" v-if="car.newEnergy == true">
						<image src="../../static/details/carNumber.png"></image>
						<span>新能源</span>
					</view>
				</view>

			</view>
		</view>
		<view class="swiper-frame" v-if="idx >= 0">
			<u-swiper class="swiper" :list="carImgList" keyName="img" indicator indicatorMode="line" height="330"
				interval="5000" circular @change="changeSwiper" previousMargin="155" nextMargin="155"></u-swiper>

		</view>
		<view v-else>
			<u-empty text="未获取到该车辆实图" icon="../../static/empty/no_data.png">
			</u-empty>
		</view>
		<view class="card-info">
			<view class="card-info-time">
				<view class="start-time" @click="">
					<view class="fl">
						<view class="specific-time fl">
							<span>{{startTime.weekday}}</span>
							<span>{{startTime.hours}}:{{startTime.minutes}}</span>
						</view>
						<view class="about-time">
							<span>{{startTime.month}}</span>月
							<span>{{startTime.day}}</span>日
						</view>
					</view>
				</view>
				<view class="day-time">
					<image src="../../static/index/line.png"></image>
					<span>{{day}}天</span>
				</view>
				<view class="end-time" @click="">
					<view>
						<view class="specific-time fr">
							<span>{{endTime.weekday}}</span>
							<span>{{endTime.hours}}:{{endTime.minutes}}</span>
						</view>
						<view class="about-time">
							<span>{{endTime.month}}</span>月
							<span>{{endTime.day}}</span>日
						</view>
					</view>
				</view>
			</view>
			<view style="margin-top: 100rpx;"></view>
		</view>
		<view class="card">
			<view class="title">
				<span class="fl">取车方式</span>
			</view>
			<view class="body">
				<view class="body-item">
					<span class="fl">送车上门（暂未开发）</span>
					<span class="fr"><u-switch size="30" style="margin-bottom: -8rpx;" disabled v-model="way"></u-switch></span>
				</view>
			</view>
		</view>
		<view class="card">
			<view class="title">
				<span class="fl">优惠券</span>
			</view>
			<view class="body">
				<view class="body-item" @click="couponShow = couponShow == true ? false : true" v-show="coupon.id == 0">
					<span class="fl">未选择优惠券</span>
					<span class="fr">选择优惠券></span>
				</view>
				<view class="body-item" @click="couponShow = couponShow == true ? false : true" v-show="coupon.id != 0">
					<span class="fl name">{{coupon.name}}</span>
					<span class="fr cancel" @click.stop="cancelCoupon">取消选择</span>
					<span class="fr" v-if="coupon.type == 1 || coupon.type == 2">-{{coupon.discount}}</span>
					<span class="fr" v-else-if="coupon.type == 3 || coupon.type == 4">*{{coupon.discount}}</span>
				</view>
				<view class="body-item" v-for="(item, idx) in couponUserList" v-show="couponShow == true" @click="changeCoupon(item)">
					<span class="fl name"  v-if="(item.type == 1 || item.type == 3) && item.reach <= car.dayPrice * day">{{item.name}}</span>
					<span class="fl name"  v-else-if="(item.type == 2 || item.type == 4) && item.reach <= day">{{item.name}}</span>
					<span class="fl no_name" v-else>{{item.name}}</span>
					<span class="fr" v-if="item.type == 1 || item.type == 2">-{{item.discount}}</span>
					<span class="fr" v-else-if="item.type == 3 || item.type == 4">*{{item.discount}}</span>
				</view>
			</view>

		</view>
		<view class="card">
			<view class="title">
				<span class="fl">订单明细</span>
			</view>
			<view class="body">
				<view class="body-item">
					<span class="fl">基本租金</span>
					<span class="fr">{{car.dayPrice}}元 * {{day}}天 = {{car.dayPrice * day}}元</span>
				</view>
				<view class="body-item">
					<span class="fl">手续费</span>
					<span class="fr">{{car.commission}}元</span>
				</view>
				<view class="body-item">
					<view v-if="coupon.type == 0">
						<span class="fl">优惠金额</span>
						<span class="fr">0元</span>
					</view>
					<view v-else-if="coupon.type == 1 || coupon.type == 2">
						<span class="fl">抵扣金额</span>
						<span class="fr">-{{coupon.discount}}元</span>
					</view>
					<view v-else-if="coupon.type == 3 || coupon.type == 4">
						<span class="fl">折扣</span>
						<span class="fr" >*{{coupon.discount}}</span>
					</view>
					
				</view>
				
				<view class="body-item">
					<span class="fl">合计</span>
					<span class="fr" v-if="coupon.type == 0">{{car.dayPrice * day + car.commission}}元</span>
					<span class="fr" v-else-if="coupon.type == 1 || coupon.type == 2">{{car.dayPrice * day + car.commission - coupon.discount}}元</span>
					<span class="fr" v-else-if="coupon.type == 3 || coupon.type == 4">{{(car.dayPrice * day + car.commission) * coupon.discount}}元</span>
				</view>
			</view>
		</view>
		<view style="height: 300rpx;width: 100%;">

		</view>
		<view class="card-bottom">
			<view style="position: relative;height: 100rpx;width: 650rpx;">
				<span style="height: 100rpx;line-height:76rpx;">￥</span>
				<span class="price" v-if="coupon.type == 0">{{car.dayPrice * day + car.commission}}元</span>
				<span class="price" v-else-if="coupon.type == 1 || coupon.type == 2">{{car.dayPrice * day + car.commission - coupon.discount}}元</span>
				<span class="price" v-else-if="coupon.type == 3 || coupon.type == 4">{{(car.dayPrice * day + car.commission) * coupon.discount}}元</span>
				<span class="deposit" v-if="car.noDeposit == 1 && user.vipLevel > 0">押金:<span style="text-decoration: line-through;">￥{{car.deposit}}</span></span>
				<span class="deposit" v-else>押金:￥{{car.deposit}}</span>
				<span class="detail">加购</span>
				<span class="button-buy " @click="newOrder()">立即下单</span>
			</view>
		</view>
		<payPopup :noPay="noPay" :user="user" :order="order" :car="car" :coupon="coupon" :paymentAmount="paymentAmount" @getNoPay="getNoPay"></payPopup>
	</view>
</template>

<script>
	import payPopup from '../../components/payPopUp.vue'
	export default {
		data() {
			return {
				user: {}, //用户数据
				couponUserList: [], //用户的所有优惠券
				area: '', //地区
				day: 0, //租几天
				startTime: {}, //开始租车时间
				endTime: {}, //结束租车时间
				car: {}, //车辆
				carImgObj: {
					carId: 0,
					imgList: []
				},
				carImgList: [], //车辆实图列表
				idx: -1, ///车辆实图列表索引
				coupon: {
					id: 0,
					type: 0,
					discount:0
				},
				couponShow: false,
				discountShow: false,
				way:false,
				noPay: false,		//true弹出支付框
				order:{
					
				},
				orderNo: 0,
				paymentAmount: 0,
			}
		},
		components:{
			"payPopup":payPopup
		},
		onLoad(options) {
			this.getData(options)
		},
		onShow(options) {
			this.getData(options)
		},
		methods: {
			async getData(options){
				let that = this;
				this.car = JSON.parse(options.car);
				console.log("car",this.car)
				this.car.dayPrice = Number.parseFloat(this.car.dayPrice);
				this.car.deposit = Number.parseFloat(this.car.deposit);
				this.car.commission = Number.parseFloat(this.car.commission);
				this.area = options.area;
				this.day = options.day;
				if(this.isValue(options.orderNo))
					this.orderNo = options.orderNo;
				this.startTime = JSON.parse(options.startTime);
				this.endTime = JSON.parse(options.endTime);
				console.log(this.startTime)
				//获取用户数据
				let user = uni.getStorageSync("user");
				console.log(this.getToken());
				await uni.request({
					url: this.baseURL + "/users/userLogin",
					header:{
						"authorization": this.getToken()
					},
					withCredentials: true,
					xhrFields: {
						withCredentials: true
					},
					method: 'GET', //请求方式，必须为大写
					success: (res) => {
						console.log('/users/userLogin', res.data);
						if (res.data.code == 200) {
							uni.setStorageSync("user", user)
							uni.setStorageSync("activeRadio", "用户");
							that.user = uni.getStorageSync("user");
							that.getCouponUserList()
							that.getCarImg();
						} else {
							uni.showToast({
								title: '用户未登录，即将跳转登录页',
								icon: 'none',
								duration: 2000
							});
							uni.removeStorageSync("user")
							setTimeout(() => {
								uni.navigateTo({
									url:'/pages/login/login_index'
								})
							}, 2000)
						}
					},
					complete() {
					}
				})
				
			},
			
			getDate() {
				const date = new Date();
				//timestamp = +new Date(Number(date));
				const year = date.getFullYear();
				//格式化日期
				const filters = n => {
					return n < 10 ? (n = '0' + n) : n;
				};
				const month = filters(date.getMonth() + 1);
				const day = filters(date.getDate());
				const weekday = date.getDay();
				const hours = filters(date.getHours());//当前时间的9小时后
				const minutes = filters(date.getMinutes());
				const seconds = filters(date.getSeconds());
				var formatDateTime = `${date.getFullYear()}-${month}-${day} ${hours}:${minutes}:${seconds}`  
				return formatDateTime;
			},
			getCouponUserList() {
				var that = this;
				let formatDateTime = this.getDate();
				console.log(formatDateTime)
				let token = this.getToken();
				uni.request({
					url: this.baseURL + "/coupons/list/user/" + formatDateTime,
					header:{
						"authorization": token
					},
					withCredentials: true,
					xhrFields: {
						withCredentials: true
					},
					method: 'GET', //请求方式，必须为大写
					success: (res) => {
						console.log('接口返回------', res.data);
						if (res.data.code == 200) {
							that.couponUserList = res.data.data;
						} else {
							uni.showToast({
								title: res.data.msg,
								icon: 'none',
								duration: 2000
							});
						}
					}
				})
			},
			getCarImg() {
				var that = this;
				uni.request({
					url: this.baseURL + '/cars/img/' + this.car.id + '/0', //0表示查所有图片
					withCredentials: true,
					xhrFields: {
						withCredentials: true
					},
					success(res) {
						console.log(res.data)
						if (res.data.code != 200) {
							uni.showToast({
								icon: 'none',
								title: res.data.msg,
								duration: 2000
							})
						} else {
							console.log(res.data.data)
							that.carImgObj = res.data.data;
							console.log(that.carImgObj)
							let len = that.carImgObj.imgList.length;
							console.log(len);
							if (len == 0)
								that.idx = -1;
							else {
								that.carImgList = []
								for (let i = 0; i < len; i++) {
									let obj = {
										img: that.staticURL + that.carImgObj.imgList[i]
									}
									that.carImgList.push(obj)
								}
								that.idx = 0;
							}


						}

					}
				})
			},
			changeSwiper(e) {
				//console.log(e)
				this.idx = e.current;
			},
			changeCoupon(item){
				let flag = false;
				switch(item.type){
					case 1: 
					case 3: if(item.reach <= this.car.dayPrice *this.day)flag = true;break;
					case 2:
					case 4: if(item.reach <= this.day)flag = true;break;
				}
				if(!flag){
					uni.showToast({
						duration:2000,
						icon: 'none',
						title:'未满足使用条件'
					})
					return;
				}
				this.coupon = item;
				this.couponShow = false;
			},
			cancelCoupon(){
				this.coupon = {}
				this.coupon.id = 0
				this.coupon.type = 0
				this.coupon.discount = 0
			},
			newOrder(){
				let that = this;
				this.order = {
					userId: this.user.id,
					vehicleCarId: this.car.id,
					orderNo: this.orderNo,
					totalAmount: this.car.dayPrice * this.day,
					deposit: this.car.deposit,
					commission: this.car.commission,
					startTime: this.startTime.formatDateTime,
					endTime: this.endTime.formatDateTime,
				}
				let paymentAmount = this.car.dayPrice * this.day + this.car.commission;
				if(this.coupon.type == 1 || this.coupon.type == 2){
					paymentAmount -= this.coupon.discount;
				}else if(this.coupon.type == 3 || this.coupon.type == 4){
					paymentAmount *= this.coupon.discount;
				}
				let deposit;
				if(this.user.vipLevel >= 0 && this.car.noDeposit == 1){
					deposit = 0;
				}else{
					deposit = this.car.deposit;
					paymentAmount += deposit;
				}
				this.paymentAmount = paymentAmount;
				this.noPay = true;
			},
			getNoPay(e){
				this.noPay = e.noPay;
			}
		}
	}
</script>

<style>
	.warp {
		width: 100vw;
		height: 100vh;
		text-align: center;
	}

	.warp .ticket {
		display: inline-block;
	}

	.warp .ticket>view .border {
		width: 650rpx;
		height: 900rpx;
	}

	.contant {
		background-color: #ffffff
	}

	.card-info {
		position: relative;
		width: 640rpx;
		margin: 20rpx 15rpx;
		padding: 30rpx 40rpx;
		box-shadow: 0 20rpx 30rpx #e8ebf6;
		background-color: #ffffff;
		border-radius: 40rpx;
		z-index: 2;
	}

	.card-info .card-info-nav {
		height: 60rpx;
		margin-bottom: 40rpx;
		text-align: center;
	}

	.card-info .card-info-nav .title {
		vertical-align: top;
	}

	.card-info .card-info-nav .choose {
		vertical-align: top;
		font-size: 24rpx;
		color: #FFFFFF;
		border-radius: 30rpx;
	}

	.card-info .card-info-nav .choose span {
		background-color: #dedede;
		padding: 10rpx 20rpx;
	}

	.card-info .card-info-nav .choose span:first-child {
		border-radius: 30rpx 0 0 30rpx;
	}

	.card-info .card-info-nav .choose span:last-child {
		border-radius: 0 30rpx 30rpx 0;
	}

	.card-info .card-info-time {
		margin-bottom: 40rpx;
	}

	.card-info .card-info-time .start-time {
		position: absolute;
	}

	.specific-time {
		font-size: 26rpx;
		margin-top: 6rpx;
		margin-bottom: 10rpx;
		color: #787878;
	}

	.specific-time span:first-child {
		margin-right: 6rpx;
	}

	.day-time span {
		font-size: 26rpx;
		color: #787878;
	}

	.about-time {
		font-weight: 530;
	}

	.about-time span {
		font-weight: 550;
	}

	.card-info .card-info-time .day-time {
		position: absolute;
		width: 200rpx;
		height: 26rpx;
		left: 275rpx;
		text-align: center;
	}

	.card-info .card-info-time .day-time image {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}

	.card-info .card-info-time .end-time {
		position: absolute;
		right: 30rpx;
	}

	.car-number {
		position: absolute;
		display: inline-block;
		border-radius: 10rpx 0 0 10rpx;
		width: 134rpx;
		padding: 12rpx 8rpx;
		bottom: 22rpx;
		right: 0;

		text-align: center;
	}

	.car-number span {
		font-size: 22rpx;
	}

	.card-car {
		width: 710rpx;
		padding: 16rpx 0;
		margin: 20rpx;
		background-image: linear-gradient(#eaeef6, #fafafa);
		border-radius: 10rpx;
		position: relative;
	}

	.card-car .show {
		width: 240rpx;
		height: 135rpx;
		display: inline-block;
		object-fit: cover;
		vertical-align: middle;
		padding: 0 20rpx;
	}

	.card-car .text {
		width: 340rpx;
		height: 135rpx;
		display: inline-block;
		vertical-align: middle;
	}

	.card-car .text .title {
		white-space: nowrap;
		width: 340rpx;
		text-overflow: ellipsis;
		overflow: hidden;
		font-weight: 600;
		font-size: 32rpx;
		margin-bottom: 10rpx;
	}

	.card-car .text .type {
		white-space: nowrap;
		width: 340rpx;
		text-overflow: ellipsis;
		overflow: hidden;
		font-weight: 500;
		font-size: 22rpx;
		display: inline-block;
	}

	.card-car .bottom {
		width: 686rpx;
		overflow: hidden;
	}

	.card-car .bottom .bottom-label {
		font-size: 16rpx;
	}

	.card-car .bottom .bottom-label .label {
		border-radius: 10rpx;
		display: inline-block;

		margin: 16rpx 6rpx;
		padding: 3rpx;
	}

	.card-car .bottom .bottom-label .label image {
		width: 34rpx;
		height: 34rpx;
		display: inline-block;
		margin-right: 2rpx;

		vertical-align: middle;
	}

	.card-car .bottom .bottom-label .label span {
		vertical-align: middle;
	}

	.vip {
		background-color: #60a2f9;
		color: #ffffff;
		font-size: 16rpx;
		font-weight: 400;
	}

	.energy {
		background-color: #7ce23d;
		color: #ffffff;
		font-size: 16rpx;
		font-weight: 400;
	}

	.car-blue {
		background-image: linear-gradient(#9dcdff, #d8e7fa);
		color: #ffffff;
		font-size: 16rpx;
		font-weight: 400;
	}

	.car-green {
		background-image: linear-gradient(#6eff7f, #cdfacc);
		color: #ffffff;
		font-size: 16rpx;
		font-weight: 400;
	}

	.car-luxury {
		background-color: #ff4727;
		color: #ffffd4;
		font-size: 16rpx;
		font-weight: 400;
	}

	.card-bottom {
		position: fixed;
		bottom: 0;
		width: 650rpx;
		padding: 60rpx 50rpx 60rpx;
		border-top: #ededed 1rpx solid;
		border-radius: 30rpx 30rpx 0 0;
		color: #0055ff;
		box-shadow: 0 -20rpx 60rpx #f2f4ff;
		z-index: 10;
		background-color: #fff;
	}

	.card-bottom .price {
		font-size: 34rpx;
		font-weight: 700;
		margin-right: 30rpx;
		height: 100rpx;
		line-height: 76rpx;
	}

	.card-bottom .deposit {
		font-size: 26rpx;
		color: #000;
		border: #989898 1rpx solid;
		padding: 2rpx 6rpx;
	}

	.card-bottom .detail {
		position: absolute;
		top: 20%;
		right: 26%;
		font-size: 24rpx;
		color: #868686;
		margin-right: 20rpx;
	}

	.card-bottom .button-buy {
		position: absolute;
		top: 0%;
		right: 0%;
		font-size: 30rpx;
		color: ivory;
		background-color: #0055ff;
		padding: 20rpx 26rpx;
		border-radius: 60rpx;
		height: 38rpx;
		line-height: 38rpx;
	}

	.card {
		display: inline-block;
		width: 660rpx;
		margin: 20rpx 25rpx;
		background-color: #fff;
		border-radius: 40rpx;
		padding: 20rpx;
		box-shadow: 0 20rpx 30rpx #e8ebf6;
		margin-bottom: 40rpx;
	}

	.card .title {
		font-size: 26rpx;
		margin-bottom: 30rpx;
		color: #666;
		overflow: hidden;
	}

	.card .body {
		width: 100%;

	}

	.card .body .body-item {
		width: 660rpx;
		font-size: 24rpx;
		margin-bottom: 20rpx;
	}

	.card .body .body-item::after {
		content: '.';
		display: block;
		clear: both;
		overflow: hidden;
		height: 0;

	}
	.card .body .body-item .name{
		background-color: #f00000;
		color: #fff7c5;
		padding:0 6rpx;
		border-radius: 6rpx;
	}
	.card .body .body-item .no_name{
		background-color: #959595;
		color: #f0f0f0;
		padding:0 6rpx;
		border-radius: 6rpx;
	}
	.card .body .body-item .cancel{
		background-color: #ff99a0; 
		color: #f8fffb;
		padding:0 6rpx; 
		border-radius: 6rpx;
		margin-left: 10rpx;
	}

	.card .body .body-item image {
		width: 60rpx;
		height: 60rpx;
	}
</style>