<template>
	<view class="order-nav">
		<view v-for="(item, idx) in name">
			<span v-if="orderNavIdx == idx" class="change">{{item}}</span>
			<span v-else @click="toNav(idx)">{{item}}</span>
		</view>
	</view>
</template>

<script>
	export default {
		name:"order_nav",
		props:['orderNavIdx', 'userId'],
		data() {
			return {
				name:['待付款','进行中','已完成','已取消']
			};
		},
		methods:{
			toNav(idx){
				this.$emit('getOrderNavIdx', idx);
			}
		}
	}
</script>

<style>
	.order-nav{
		width: 720rpx;
		height: 80rpx;
		line-height: 80rpx;
		padding: 0 15rpx;
		background-color: #f4f4f4
	}
	.order-nav view{
		display: inline-block;
		width: 180rpx;
		text-align: center;
		color: #424242;
	}
	.change{
		font-weight: 650;
		color: #faa40d;
	}
</style>