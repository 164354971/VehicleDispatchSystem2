<template>
	<view class="content">
		<view class="nav">取车二维码</view>
		<view class="canvas">
			<canvas canvas-id="qrcode" style="width: 180px;height: 180px;"></canvas>
		</view>
		<view class="body">
			<view>
				<span>位置：{{area}}</span>
			</view>
			<view>
				<span>{{parkade}} </span>
			</view>
			<view class="button" click="">
				<span>导航</span>
			</view>
		</view>
		
	</view>
</template>

<script>
	import UQrcode from '../../utils/uqrcode.js'
	export default {
		data() {
			return {
				text: '',
				area: '',
				parkade: '',
			}
		},
		onLoad(options) {
			let that = this;
			console.log(options)
			this.area = options.area;
			this.parkade = options.parkade;
			if(this.isValue(options.username) && this.isValue(options.orderNo))
				uni.request({
					url: this.baseURL + "/order/createQRCode?username=" + options.username + "&orderNo=" + options.orderNo,
					header: {
						// 'Content-Type': 'application/x-www-form-urlencoded'
						 'Content-Type': 'application/json' //自定义请求头信息
					},
					withCredentials: true,
					xhrFields: {
						withCredentials: true
					},
					method:'GET',//请求方式，必须为大写
					success: (res) => {
						console.log('/order/createQRCode',res);
						if(res.data.code == 1){
							that.text = res.data.data
							that.qrFun(that.text)
						}
						else{
							uni.showToast({
								title: res.data.msg,
								icon:'none',
								duration: 2000
							});
							uni.navigateBack({
								delta:1,//返回层数，2则上上页
							})
						}
					}
				})
			else
				uni.navigateBack({
					delta:1,//返回层数，2则上上页
				})
		},
		methods: {
			qrFun(text) {
				
				UQrcode.make({
					canvasId: 'qrcode', //切记canvasId 里边的内容需要跟canvas里边canvas-id="qrcode"的名字一样
					componentInstance: this,
					text: text, //需要转成二维码的内容是后端传过来的，我这里是onLoad传过来的，根                              据自己的需要
					size: 180,
					margin: 0,
					backgroundColor: '#ffffff',
					foregroundColor: '#000000',
					fileType: 'jpg',
					errorCorrectLevel: UQrcode.errorCorrectLevel.H,
					success: res => {}
				})
			},
		}

	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center; 
		padding: 100rpx 0;
	}
	.nav{
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center; 
		margin-bottom: 40rpx;
	}
	.canvas{
		margin-bottom: 40rpx;
	}
	.button{
		margin-top: 60rpx;
		width: 300rpx;
		height: 100rpx;
		background-color: #ff0000;
		color: #ffffff;
		font-size: 40rpx;
		font-weight: 650;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}
	.body{
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center; 
		margin-bottom: 40rpx;
	}
</style>