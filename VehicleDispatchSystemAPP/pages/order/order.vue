<template>
	<view class="centent">
		<view class="title">
			<span @click="toHome()">< 返回</span>
			<span>{{name[orderNavIdx]}}</span>
			<span></span>
		</view>
		<orderNav :orderNavIdx="orderNavIdx" :userId="user.id" @getOrderNavIdx="getOrderNavIdx"></orderNav>
		<view class="search">
			<u-search :showAction="true" actionText="搜索" height="60rpx" bgColor="#fff"></u-search>
		</view>
		<obligation :obligationList="orderStatusList" :user="user" v-show="orderNavIdx == 0"></obligation>
		<inProgress :inProgressList="orderStatusList" :user="user" v-show="orderNavIdx == 1"></inProgress>
		<complete :completeList="orderStatusList" :user="user" v-show="orderNavIdx == 2"></complete>
		<cancel :cancelList="orderStatusList" :user="user" v-show="orderNavIdx == 3"></cancel>
	</view>
</template>

<script>
	import price from '../../components/price.vue'
	import orderNav from '../../components/order_nav.vue'
	import obligation from './obligation_list.vue'
	import inProgress from './in_progress_list.vue'
	import complete from './complete_list.vue'
	import cancel from './cancel_list.vue'
	export default {
		data() {
			return {
				user:{
					
				},
				orderStatusList:[],//订单{订单车辆}，总价
				orderNavIdx: 0,
				name:['待付款','已付款','已完成','已取消']
			}
		},
		components:{
			"price":price,
			"orderNav":orderNav,
			"obligation":obligation,
			"inProgress":inProgress,
			"complete":complete,
			"cancel":cancel
		},
		onShow(){
			
		},
		onLoad(option) {
			this.user = JSON.parse(option.user)
			this.orderNavIdx = option.idx;
			console.log(this.user.id)
			this.getOrderStatusList()
		},
		methods: {
			getOrderStatusList(){
				let that = this;
				let status;
				console.log(this.orderNavIdx)
				if(this.orderNavIdx == 0)status = 0;
				else if(this.orderNavIdx == 1)status = 1;
				else if(this.orderNavIdx == 2)status = 2;
				else if(this.orderNavIdx == 3)status = 3;
				
				console.log(status)
				uni.request({
					url: this.baseURL + "/order/getOrderStatusList?id=" + this.user.id + "&status=" + status,
					header: {
						// 'Content-Type': 'application/x-www-form-urlencoded'
						 'Content-Type': 'application/json' //自定义请求头信息
					},
					method:'GET',//请求方式，必须为大写
					success: (res) => {
						console.log('/order/getOrderStatusList',res);
						if(res.data.code == 1){
							console.log(res.data.data)
							that.orderStatusList = res.data.data
							
							console.log("that.orderStatusList",that.orderStatusList)
						}
						else{
							
						}
					}
				})
			},
			toHome() {
				uni.switchTab({
					url: '../home/home'
				})
			},
			getOrderNavIdx(e){
				console.log(e)
				this.orderNavIdx = e;
			},
		},
		watch:{
			orderNavIdx(newIdx, oldIdx){
				this.getOrderStatusList()
			}
		}
	}
</script>

<style>
	.centent{
		min-height: 100vh;
		display: flex;
		flex-direction: column;
		align-items: center;
		background-color: #f1f1f1;
	}
	.card{
		width: 680rpx;
		margin-bottom: 16rpx;
		padding: 30rpx 20rpx;
		background-color: #ffffff;
		border-radius: 20rpx;
		z-index: 2;
	}
	.card .card-body{
		position: relative;
	}
	.card .card-info{
		position: absolute;
		top: 0rpx;
		left: 266rpx;
		padding: 8rpx 0;
	}
	.card .card-method{
		position: absolute;
		top: 0rpx;
		right: 0;
		padding: 8rpx 6rpx;
		font-size: 30rpx;
		color: #ff5500;
	}
	.card .card-info .card-title{
		font-weight: 650;
		font-size: 30rpx
	}
	.card .card-time{
		padding: 14rpx 20rpx;
		background-color: #fafafa;
		margin: 16rpx 0;
		font-weight: 600;
		font-size: 22rpx;
		color: #464646;
	}
	.card .card-time span{
		padding: 0 10rpx;
		font-size: 24rpx;
		color: #000;
	}
	
	.card:after{
		content:'.';
		display: block;
		clear: both;
		overflow: hidden;
		height: 0;
	}
	.title {
		width: 750rpx;
		height: 100rpx;
		line-height: 100rpx;
		padding-top: 60rpx;
		color: #464646;
		background-color: #fff;
	}

	.title view {
		display: inline-block;
		width: 220rpx;
		height: 120rpx;
	}

	.title view image {
		display: inline-block;
		width: 20rpx;
		height: 20rpx;
		vertical-align: middle;
	} 

	.title span {
		font-size: 36rpx;
		font-weight: 650;
		width: 220rpx;
		display: inline-block;
		text-align: center;
		padding: 0 15rpx;
		vertical-align: middle;
	}

	.title span:first-child {
		text-align: left;
		font-weight: 450;
		font-size: 36rpx;
	}

	.title span:last-child {
		text-align: right;
		font-weight: 450;
		font-size: 32rpx;
	}
	
	.search{
		width: 750rpx;
		margin: 0 0 14rpx 0;
		padding: 6rpx 0;
		background-color: #fff;
	}
</style>
