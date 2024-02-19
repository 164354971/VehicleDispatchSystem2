<template>
	<view class="centent">
		<u-empty
				v-if="obligationList.length == 0"
		        mode="order"
		        icon="../../static/order/order_empty.png"
				style="height: 60vh;"
		>
		</u-empty>
		<view v-for="(item, index) in obligationList" @click="">
			<view class="card">
				<view class="card-body">
					<u--image v-if="isValue(item.vehicleOrderCarVo.img)" :src="baseURL + item.vehicleOrderCarVo.img" radius="10rpx" width="240rpx" height="180rpx"></u--image>
					<u--image v-else :src="baseURL + item.vehicleOrderCarVo.modelImg" radius="10rpx" width="240rpx" height="180rpx" mode="widthFix"></u--image>
					<span class="card-method">到店取车</span>
					<view class="card-info">
						<view class="card-title">
							<span>{{item.vehicleOrderCarVo.model}}</span>
						</view>
						<view>
							<u--text size="28rpx" class="text" :lines="3" :text="item.vehicleOrderCarVo.name"></u--text>
						</view>
					</view>
					<view class="card-time">
						取车时间：<span style="padding-left: 0;">{{item.startTime.split(':')[0]}}时</span> 至 <span>{{item.endTime.split(':')[0]}}时</span>
					</view>
				</view>
				
				<view class="card-footer">
					<view>
						<price class="fr" style="color: #666;font-size: 26rpx;vertical-align: bottom;display: inline-block;line-height: 40rpx;height: 40rpx;" :price="item.deposit"></price>
						<span class="fr" style="color: #666;font-size: 26rpx;vertical-align: bottom;display: inline-block;line-height: 40rpx;height: 40rpx;">押金:￥</span>
					</view>
					<view class="clear"></view>
					<view class="fr" style="margin: 10rpx 0;">
						<span>订单价格:</span>
						￥<price :price="item.totalAmount + item.commission"></price>
					</view>
					<view class="clear"></view>
					<view>
						<span class="button fl" style="width: 400rpx;">付款倒计时: {{item.countdown}}秒</span>
						<u-button type="primary" :plain="true" text="去付款" class="button fr" @click="toDetails(item)" ></u-button>
						<span class="button fr" style="color: #666;font-size: 26rpx;width: 60rpx;">更多</span>
					</view>
				</view>
			</view>
		</view>
		
	</view>
</template>

<script>
	import price from '../../components/price.vue'
	import orderNav from '../../components/order_nav.vue'
	export default {
		data() {
			return {
				orderNavIdx: 0,
				noPay: false,
				order:{},
				coupon:{},
				weekdayList:['周日','周一','周二','周三','周四','周五','周六'],
			}
		},
		props:['obligationList', 'user'],
		components:{
			"price":price,
			"orderNav":orderNav
		},
		methods: {
			async toDetails(item){
				let startTime = await this.getDate(item.startTime)
				let endTime = await this.getDate(item.endTime)
				uni.navigateTo({
				     url: '../details/details?startTime=' + JSON.stringify(startTime) + '&endTime=' + JSON.stringify(endTime) 
											+ '&day=' + item.day + '&area=' + item.vehicleOrderCarVo.area + '&car=' + JSON.stringify(item.vehicleOrderCarVo)
											+ '&orderNo=' + item.orderNo
				});
			},
			getDate(value) {
				const date = new Date(value);
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
				var time = {
					date:date,
					year:year,
					month:month,
					day:day,
					weekday:this.weekdayList[weekday],
					hours:hours,
					minutes:minutes,
					timestamp:date,
					formatDateTime: `${date.getFullYear()}-${month}-${day} ${hours}:${minutes}:${seconds}`  
				}
				return time;
			},
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
		font-size: 24rpx;
		color: #464646;
	}
	.card .card-time span{
		padding: 0 10rpx;
		font-size: 26rpx;
		color: #000;
		font-weight: 650;
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
	.button{
		display: inline-block;
		margin-top: 20rpx;
		width: 140rpx;
		height: 60rpx;
		line-height: 60rpx;
		vertical-align: bottom;
	}
</style>
