<template>
	<view class="centent">
		<u-empty
				v-if="cancelList.length == 0"
		        mode="order"
		        icon="../../static/order/order_empty.png"
				style="height: 60vh;"
		>
		</u-empty>
		<view v-for="(item, index) in cancelList">
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
					<view class="fr" style="margin: 10rpx 0;">
						<span>押金:</span>
						￥<price :price="item.deposit"></price>
					</view>
					<view class="clear"></view>
					<view class="fr">
						<price class="fr" style="line-height: 40rpx;height: 40rpx;" :price="item.totalAmount"></price>
						<span class="fr" style="line-height: 40rpx;height: 40rpx;">已退款:￥</span>
						<price class="fr" style="text-decoration: line-through;color: #666;font-size: 26rpx;vertical-align: bottom;display: inline-block;line-height: 40rpx;height: 40rpx;margin-right: 10rpx" :price="item.paymentAmount"></price>
						<span class="fr" style="color: #666;font-size: 26rpx;vertical-align: bottom;display: inline-block;line-height: 40rpx;height: 40rpx;margin-right: 10rpx;">￥</span>
					</view>
					<view class="clear"></view>
					
					
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
				user:{
					
				},
				orderNavIdx: 3
			}
		},
		components:{
			"price":price,
			"orderNav":orderNav
		},
		props:['cancelList', 'user'],
		methods: {
			
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
</style>
