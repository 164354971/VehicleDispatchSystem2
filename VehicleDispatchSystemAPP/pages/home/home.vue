<template>
	<view class="content">
		<view class="head">
			<image class="head-img" :src="staticURL + user.userImg" v-if="text == '退出登录'" @click="isLoading()"></image>
			<image class="head-img" src="../../static/login/login_default.png" @click="login = true" style="background-color: #fff;;" v-else></image>
			<image class="driver-card" src="../../static/home/card_hidden.png" v-if="!isValue(user.driverType)"></image>
			<image class="driver-card" src="../../static/home/card_display.png" v-else></image>
			<image class="driver-car" src="../../static/home/car_hidden.png"></image>
			<br>
			<view>
				<span class="vip" v-if="user.vipLevel > 0">vip{{user.vipLevel}}</span>
				<span class="nickname">{{user.nickname}}</span>
			</view>
		</view>
		<view class="box"></view>
		
		<view>
			<view class="card">
				<view class="title">
					<span class="fl">我的订单 | </span>
					<span class="fl" style="padding: 0 10rpx;margin-left: 10rpx;background-color: #f5f5f5;border-radius: 6rpx;" v-if="redDot == true" @click="redDot = false"> 弱化消息通知</span>
					<span class="fl" style="padding: 0 10rpx;margin-left: 10rpx;background-color: #f5f5f5;border-radius: 6rpx;" v-else @click="redDot = true"> 强化消息通知</span>
					<span class="fr">全部 > </span>
				</view>
				<view class="body">
					<view class="body-item" v-for="(item, idx) in orderTypes" @click="changeOrderType(idx)">
						<image :src="item.img"></image>
						<view :class="{'red-dot' : redDot, 'grey-dot' : !redDot}" v-show="idx == 0 && orderObligationNum > 0">{{orderObligationNum}}</view>
						<view :class="{'red-dot' : redDot, 'grey-dot' : !redDot}" v-show="idx == 1 && orderInProgressNum > 0">{{orderInProgressNum}}</view>
						<!-- <view :class="{'red-dot' : redDot, 'grey-dot' : !redDot}" v-show="idx == 2 && orderComplete.num > 0">{{orderComplete.num}}</view>
						<view :class="{'red-dot' : redDot, 'grey-dot' : !redDot}" v-show="idx == 3 && orderCancel.num > 0">{{orderCancel.num}}</view> -->
						<br>
						<span>{{item.text}}</span>
					</view>
				</view>
				<view class="real-name">
					<image src="../../static/home/card_hidden.png"></image>
					<view class="body-item">
						<span>请完善实名信息</span>
						<br>
						<span>认证身份信息后可提车</span>
					</view>
					<span class="button">去完善</span>
				</view>
			</view>
			<view class="card">
				<view class="title">
					<span class="fl">更多</span>
				</view>
				<view class="body">
					<view class="body-item" v-for="(item, idx) in otherTypes">
						<image :src="item.img"></image>
						<br>
						<span>{{item.text}}</span>
					</view>
				</view>
				
			</view>
			<view class="click" @click="backLogin = true" v-if="text == '退出登录'">
				<span>{{text}}</span>
			</view>
			<view class="click" @click="login = true" v-else>
				<span>{{text}}</span>
			</view>
			
		</view>
		<u-modal showCancelButton :show="backLogin" title="退出登录" :content='content' @cancel="backLogin = false" @confirm="goBack()"></u-modal>	
		<loginPopUp :login="login" @setLogin="setLogin" @setUser="setUser"></loginPopUp>
	</view>
    
</template>

<script>
	import loginPopUp from '../../components/loginPopUp.vue'
    export default {
        data() {
            return {
                user:{},
				userInfo:[],
				redDot: true,
				orderTypes:[
					{img:'../../static/home/pay.png', text:'待付款'},
					{img:'../../static/home/ing.png', text:'进行中'},
					{img:'../../static/home/finish.png', text:'已完成'},
					{img:'../../static/home/cancel.png', text:'已取消'},
				],
				otherTypes:[
					{img:'../../static/home/collect.png', text:'收藏车辆'},
					{img:'../../static/home/coupon.png', text:'优惠券'},
					{img:'../../static/home/manage.png', text:'联系用户'},
					{img:'../../static/home/feedback.png', text:'问题反馈'},
					{img:'../../static/home/about.png', text:'开发人员'},
				],
				text:'登录',
				content:'是否退出登录？',
				backLogin: false,
				login:false,
				orderObligationNum:0,
				orderInProgressNum:0,
				orderCompleteNum:0,
				orderCancelNum:0,
				loading:false,
			};
        },
		components:{
			"loginPopUp":loginPopUp
		},
        onShow() {
			this.isLogin()
			this.getData()
		},
        methods: {
			isLoading(){
				console.log(this.loading)
				if(!this.loading){
					this.userInfo = [
						{text: '账号：' + this.username}
					]
				}
				this.loading = !this.loading;
			},
			userInfoClick(index){
				console.log(index)
			},
			open() {
				let that = this;
			}, 
			close() {
				this.loading = false;
			},
			getData(){
				for(let i = 0; i <= 3; i++)
					this.getOrderStatusNum(i)
			},
			gePreValue(e){
				this.setLogin(e.login);
				this.setUser(e.user);
			},
			setLogin(login){
				this.login = login;
			},
			setUser(user){
				this.user = user;
				if(JSON.stringify(this.user)=="{}" || this.user.username == ''|| this.user.username == undefined){
					this.text = '登录'
				}else{
					for(let i = 0; i <= 3; i++)
						this.getOrderStatusNum(i)
					this.text = '退出登录'
				}
			},
			isLogin(){
				var that = this;
				let token = this.getToken();
				this.user = uni.getStorageSync("user");
				console.log(token)
				uni.request({
					url:this.baseURL + '/users/userLogin',
					header: {
						"authorization" : token
					},
					withCredentials:true,
					xhrFields: {
						withCredentials: true
					},
					method:'GET',//请求方式，必须为大写
					success: (res) => {
						if(res.data.code != 200){
							uni.removeStorageSync("user");
							that.user = uni.getStorageSync("user");
							that.orderObligationNum = 0;
							that.orderInProgressNum = 0;
							that.orderCompleteNum = 0;
							that.orderCancelNum = 0;
							that.text = '登录'
							that.backLogin = false
						}else{
							that.text = '退出登录'
						}
					} 
				})
				
			},
			goBack(){
				var that = this;
				let token = uni.getStorageSync("user").token;
				uni.request({
					url:this.baseURL + '/users/logout/' + this.user.userId,
					header: {
						"authorization" : token
					},
					withCredentials:true,
					xhrFields: {
						withCredentials: true
					},
					method:'GET',//请求方式，必须为大写
					success: (res) => {
						console.log('接口返回------',res);
						uni.showToast({
							title: res.data.msg,
							icon:'none',
							duration: 2000
						});
						if(res.data.code == 200){
							uni.removeStorageSync("user");
							that.user = uni.getStorageSync("user");
							that.orderObligationNum = 0;
							that.orderInProgressNum = 0;
							that.orderCompleteNum = 0;
							that.orderCancelNum = 0;
							that.text = '登录'
							that.backLogin = false
						}else{
							
						}
					}
				})
			},
			toLogin(){
				uni.showToast({
					title: '即将跳转登录页面',
					icon:'none',
					duration: 1000
				})
				setTimeout(() => {
					uni.navigateTo({
					     url: '../login/login'
					});
				}, 1000)
				
			},
			getOrderStatusNum(status){
				let that = this;
				uni.request({
					url:this.baseURL + "/order/getOrderStatusNum?id=" + this.user.id + "&status=" + status,
					withCredentials: true,
					xhrFields: {
						withCredentials: true
					},
					method: 'GET', //请求方式，必须为大写
					success(res) {
						if(res.data.code == 1){
							if(status == 0)
								that.orderObligationNum = res.data.data;
							else if(status == 1)
								that.orderInProgressNum = res.data.data;
							else if(status == 2)
								that.orderCompleteNum = res.data.data;
							else if(status == 3)
								that.orderCancelNum = res.data.data;
						}
					}
				})
			},
			changeOrderType(idx){
				let url = '../order/order';
				uni.navigateTo({
					url: url + '?user=' + JSON.stringify(this.user) + '&idx=' + idx,
				})
			},
			
		},

    };
</script>

<style scoped="scoped" lang="scss">
	.content {
		background-color: #ebeef6;
		padding-bottom: 60rpx;
		text-align: center;
		min-height: 100vh;
	}
    .head{
		width: 750rpx;
		text-align: center;
		padding: 200rpx 0;
		position: relative;
	}
	.head .head-img{
		width: 260rpx;
		height: 260rpx;
		border-radius: 130rpx;
		margin: 20rpx 0;
		background-color: #cccccc;
	}
	.head .driver-card{
		position: absolute;
		width: 48rpx;
		height: 48rpx;
		top: 416rpx;
		left: 462rpx;
	}
	.head .driver-car{
		position: absolute;
		width: 48rpx;
		height: 48rpx;
		top: 416rpx;
		left: 518rpx;
	}
	.nickname{
		text-align: center;
		font-weight: 600;
		color: #868686;
	}
	.vip{
		font-size: 28rpx;
		background-image: linear-gradient(to right, #262639 , #565658);
		color: #ffdf7d;
		border-radius: 14rpx;
		padding: 0 14rpx 0 8rpx;
		margin-right: 10rpx;
		font-weight: bold;
		font-style: italic;
	}
	.card{
		display: inline-block;
		background-color: #fff;
		border-radius: 40rpx;
		width: 660rpx;
		padding: 20rpx;
		box-shadow: 0 80rpx 60rpx #e8ebf6;
		margin-bottom: 40rpx; 
	}
	.card .title{
		font-size: 26rpx;
		color: #666;
		overflow: hidden;
	}
	.card .body{
		width: 100%;
		display: flex;
		flex: 1;
	}
	.card .body .body-item{
		margin: 50rpx 0 0;
		width: 160rpx;
		height: 120rpx;
		font-size: 24rpx;
		position: relative;
	}
	.card .body .body-item image{
		width: 60rpx;
		height: 60rpx;
	}
	.card .body .body-item view{
		position: absolute;
		display: inline-block;
		min-width: 36rpx;
		border-radius: 30rpx;
		margin-left: -20rpx;
		top: -10rpx;
	}
	.red-dot{
		color: #fffff9;
		background-color: #fc0922;
		transition-duration: 1s;
	}
	.grey-dot{
		color: #fffff9;
		background-color: #cccccc;
		transition-duration: 1s;
	}
	.card .real-name{
		display: inline-block;
		width: 640rpx;
		background-color: #f5f5f6;
		border-radius: 20rpx;

		height: 100rpx;
	}
	.card .real-name image{
		width: 60rpx;
		height: 60rpx;
		margin: 20rpx 0 20rpx 0;
		vertical-align: middle;
	}
	.card .real-name view{
		display: inline-block;
		width: 300rpx;
		height: 72rpx;
		font-size: 26rpx;
		text-align: left;
		margin: 0 40rpx;
		vertical-align: middle;
	}
	.card .real-name view>span:first-child{
		font-weight: 700;
	}
	.card .button{
		background-color: #5555ff;
		padding: 8rpx 16rpx;
		border-radius: 30rpx;
		color: #fff;
		font-size: 28rpx;
		vertical-align: middle;
	}
	.click{
		margin-top: 100rpx;
		height: 80rpx;
		line-height: 80rpx;
		color: #ef4236;
		background-color: #fff
	}
	.information{
		margin-top: -100rpx;
		background-color: #fff;
		width: 710rpx;
		text-align: left;
		padding: 20rpx;
		border-radius: 20rpx 20rpx 0 0;
		height: 70vh;
	}
	

</style>