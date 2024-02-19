<template>
	<view class="content">
		<u-popup :show="payPasswordShow" @close="close" @open="open">
			<view class="pay-view">
				<view class="password">
					<view class="title">{{title}}</view>
					<view class="tip">{{tip}}</view>
					<view>
						<span class="password-item" :class="{'bold': index == idx, 'no-bold': index != idx }" v-for="(item, idx) in password">
							{{item}}
						</span>
					</view>
					
				</view>
				<view class="numbers">
					<span class="number" v-for="(item, idx) in numbers" @click="changeNum(item)">{{item}}</span>
					<span class="number" @click="clear()">清空</span>
					<span class="number" @click="changeNum(0)">0</span>
					<span class="number" @click="backspace()">×</span>
				</view>
				<view class="button" @click="pay()">
					付款
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				password:['','','','','',''],
				payPassword:'',
				title:'输入支付密码',
				tip:'',
				index:0,
				numbers:'123456789',
				payCount:3
			}
		},
		props: ['payPasswordShow', 'payOrder'],
		methods: {
			changeNum(item){
				if(this.index < 6){
					this.password[this.index] = '·';
					this.payPassword += item;
					this.index++;
					console.log(this.payPassword)
				}
				
			},
			backspace(){
				if(this.index > 0){
					this.index--;
					
					this.password[this.index] = '';
					this.payPassword = this.payPassword.substr(0, this.index)
				}
				
				console.log(this.payPassword)
			},
			clear(){
				this.index = 0;
				for(let i = 0; i < this.password.length; i++)
					this.password[i] = '';
				this.payPassword = ''
				console.log(this.payPassword)
			},
			pay(){
				let that = this;
				if(this.payPassword.length < 6){
					uni.showToast({
						icon:'none',
						title:'支付密码输入不完整，请重新输入'
					})
					return;
				}

				this.payOrder.paymentPassword = this.payPassword
				uni.request({
					url: that.baseURL + '/order/payOrder',
					data: this.payOrder,
					withCredentials:true,
					xhrFields: {
						withCredentials: true
					},
					method: 'POST',
					success(res) {
						console.log(res);
						if(res.data.code == 1){
							that.clear();
							that.close();
						}else{
							if(that.payCount > 0){
								that.tip = '支付密码输入错误，您还有'+ (that.payCount --) + '次机会';
								uni.showToast({
									icon:'none',
									title:that.tip
								})
							}else{
								uni.showToast({
									icon:'none',
									title:'输入次数已上限，15分钟后可再重试'
								})
								that.close() 
							}
							
							
						}
						
					}
				})
			},
			open() { 
			
			}, 
			close() { 
				//this.payPasswordShow = false;
				this.$emit('getPayPasswordShow', false);
			},
		}
	}
</script>

<style>
	.content{
		text-align: center;
		border-radius: 20rpx 20rpx 0 0;
	}
	.pay-view{
		margin-bottom: 130rpx;
	}
	.password{
		padding: 50rpx 20rpx;
	}
	.password view{
		margin: 30rpx 10rpx;
	}
	.password .title{
		font-size: 46rpx;
		font-weight: 650;
	}
	.password .tip{
		font-size: 28rpx;
		color: #ff3b00;
	}
	.password .password-item{
		display: inline-block;
		width: 90rpx;
		height: 90rpx;
		line-height: 90rpx;
		margin: 20rpx 10rpx;
	}
	.numbers{
		padding: 20rpx;
		
	}
	.numbers .number{
		display: inline-block;
		width: 210rpx;
		height: 140rpx;
		line-height: 140rpx;
		border-bottom: 2rpx solid #bcd7ff;
		margin: 0 10rpx;
		font-size: 34rpx;
		font-weight: 650;
		color: #444444;
	}
	.button{
		display: inline-block;
		width: 600rpx;
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 30rpx;
		background-image: linear-gradient(to top, #ffbd83 , #ffcca7);
		color: #fff;
		margin: 30rpx 0;
		font-size: 36rpx;
		font-weight: 650;
	}
	.no-bold{
		border: 2rpx solid #1f1f1f;
	}
	.bold{
		border: 2rpx solid #00aaff;
	}
</style>
