<template>
	<view>
		<view class="title">
			<span @click="toBack()">&lt; 返回</span>
			<span style="width: 490rpx;text-align: center;">{{model.model}}</span>
			<span></span>
		</view>
		<view class="list">
			<view class="card-list">
				<view class="card-item" v-for="(item, key) in carList" @click="toDetails(item)">
					<image style="border-radius: 6rpx;" :src="staticURL + item.coverImg"></image>
					<view class="bottom">
						<span class="name">{{item.name}}</span>
						<br>
						<p class="market-price">市售价：{{item.marketPrice}} </p>
						<view class="bottom-label">
							<view class="label vip">
								<image src="../../static/details/vip.png"></image>
								<span>会员免押</span>
							</view>
							<view class="label car-green" v-if="item.energy == '插混' || item.energy == '增程' || item.energy == '纯电' ">
								<image src="../../static/green_car.png"></image>
								<span>新能源</span>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				model:{},
				carList:[]
			}
		},
		onLoad(options) {
			this.model = JSON.parse(options.model)
			this.getCarList()
		},
		methods: {
			toBack(){
				uni.navigateBack()
			},
			getCarList() {
				let that = this;
				console.log(this.model.id)
				uni.request({
					url: this.baseURL + '/cars/list/' + this.model.id,
					method: 'GET', //请求方式，必须为大写
					success: (res) => {
						console.log(that.baseURL + '/cars/list', res);
						if (res.data.code == 200) {
							console.log(res.data.data)
							that.carList = res.data.data;
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
			toDetails(item){
				uni.navigateTo({
					url:'../../pages/details/details'
				})
			}
		}
	}
</script>

<style>
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
		display: inline-block;
		text-align: center;
		padding: 0 15rpx;
		vertical-align: middle;
	}
	
	.title span:first-child {
		text-align: left;
		font-weight: 450;
		font-size: 36rpx;
		width: 100rpx;
	}
	
	.title span:last-child {
		text-align: right;
		font-weight: 450;
		font-size: 32rpx;
		width: 100rpx;
	}
	
	.list {
		padding: 20rpx;

	}

	.type-list {
		text-align: center;
		margin: 12rpx 2rpx;
	}

	.type-list>span {
		font-size: 30rpx;
		padding: 6rpx 16rpx;
		margin: 0 12rpx;
		border-radius: 4rpx;
		border: #f0f0f3 1rpx solid;
		background-color: #f0f0f3;
		color: #565656;
		font-size: 24rpx;
	}

	.type-list .type {
		border: #a1a1a1 1rpx solid;
		background-color: #fff;
		color: #000;
	}

	.list .card-list {
		text-align: center;
	}

	.list .card-list>span {
		font-size: 23rpx;
		color: #757575;
	}

	.list .card-list .card-item {
		text-align: left;
		width: 650rpx;

		padding: 20rpx;
		margin: 16rpx 10rpx;
		background-color: #ffffff;
		border-radius: 6rpx;
		box-shadow: 1rpx 1rpx 10rpx #f6f6f6;
	}

	.list .card-list .card-item image {
		display: inline-block;
		width: 240rpx;
		height: 180rpx;
		vertical-align: middle;
	}

	.list .card-list .card-item .bottom {
		display: inline-block;
		width: 340rpx;
		margin-left: 50rpx;
		vertical-align: middle;
	}

	.list .card-list .card-item .bottom .name {
		font-size: 28rpx;
		font-weight: 550;
		margin-bottom: 20rpx;
	}

	.list .card-list .card-item .bottom .market-price {
		font-size: 20rpx;
		overflow: hidden;
	}

	.list .card-list .card-item .bottom .bottom-label {
		display: inline-block;
		width: 340rpx;
		font-size: 16rpx;
	}

	.list .card-list .card-item .bottom .bottom-label .label {
		border-radius: 10rpx;
		display: inline-block;

		margin: 16rpx 6rpx;
		padding: 3rpx;
	}

	.list .card-list .card-item .bottom .bottom-label .label image {
		width: 34rpx;
		height: 34rpx;
		display: inline-block;
		margin-right: 6rpx;
		vertical-align: bottom;
	}

	.list .card-list .card-item .bottom .bottom-label .label span {
		vertical-align: bottom;
		font-size: 22rpx
	}

</style>
