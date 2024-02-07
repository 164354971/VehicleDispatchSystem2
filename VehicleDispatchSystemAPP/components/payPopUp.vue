<template>
	<view>
		<u-popup :show="noPay" mode="bottom" @close="close" @open="open" closeable round="20"  :zIndex="payConfirm == true ? 1000 : 10075">
			<view class="pay">
				<view class="price">
					￥<span v-if="car.noDeposit == 1 && user.vipLevel > 0"><price :price="paymentAmount + car.deposit"></price></span>
					<span v-else><price :price="paymentAmount + car.deposit"></price></span>
				</view>
				<view class="username">
					<span class="fl">账号</span>
					<span class="fr">{{user.username}}</span>
				</view>
				<view class="payment">
					<span class="fl">支付方式</span>
					<span class="fr">{{payment.name}}</span>
					<image class="fr" :src="payment.img"></image>
				</view> 
				<view class="payment-list">
					<span class="title">选择支付方式</span>
					<view class="payment-item" v-for="(item, idx) in paymentList" @click="changePayment(item)">
						<image class="fl" :src="item.img"></image>
						<span class="fl">{{item.name}}</span>
						<image class="fr" src="../static/common/dui_3.png" v-show="item.id == payment.id"></image>
					</view>
				</view>
				<view class="button" @click="confirm">
					立即支付
				</view>
			</view>
		</u-popup>
		<u-modal showCancelButton :show="payConfirm" @confirm="payTrue" @cancel="payFalse" 
				confirmText="确认支付" cancelText="我再想想"
				ref="uModal" :title="title" :content='content' ></u-modal>
		<payPasswordPopUp :payPasswordShow="payPasswordShow" :payOrder="payOrder" @getPayPasswordShow="getPayPasswordShow"></payPasswordPopUp>
	</view>
</template>

<script>
	import price from './price.vue'
	import payPasswordPopUp from './payPasswordPopUp.vue'
	export default {
		name: "payPopUp",
		props: ['noPay', 'user', 'order', 'coupon', 'car', 'paymentAmount'],
		data() {
			return {
				radio: true,
				payment:{
					id: 1,
					name:'余额支付',
					type:1,
					img:'../../static/common/yue.png'
				},
				paymentList:[
					{
						id: 1,
						name:'余额支付',
						type:1,
						img:'../../static/common/yue.png'
					},
					{
						id: 2,
						name:'微信支付',
						type:2,
						img:'../../static/common/weixin_pay.png'
					},
					{
						id: 3,
						name:'支付宝',
						type:3,
						img:'../../static/common/zhifubao.png'
					},
					{
						id: 4,
						name:'银行卡',
						type:4,
						img:'../../static/common/bank_card.png'
					},
				],
				payConfirm: false,
				title:"支付确认",
				content:'',
				payPasswordShow: false,
				payOrder:{
					userId: 0,
					orderNo: 0,
					payChannel: 0,
					paymentPassword: ''
				}
			}
		},
		components:{
			"price":price,
			"payPasswordPopUp":payPasswordPopUp,
		},
		methods: {
			confirm(){
				console.log(this.order);
				let paymentAmount;
				if(this.car.noDeposit == 1 && this.user.vipLevel > 0){
					paymentAmount = this.paymentAmount;
				}else{
					paymentAmount = this.paymentAmount + this.car.deposit;
				}
				this.content = "确认支付金额：" + paymentAmount + " 元吗？";
				this.payConfirm = true;
			},
			payTrue(){
				let that = this;
				console.log(this.user.id)
				uni.request({
					url: that.baseURL + '/user/isPaymentPassword?id=' +  that.user.id,
					withCredentials:true,
					xhrFields: {
						withCredentials: true
					},
					method: 'GET',
					success(res) {
						console.log(res);
						if(res.data.code == 1){
							uni.request({
								url: that.baseURL + '/order/createOrder',
								data: that.order,
								withCredentials:true,
								xhrFields: {
									withCredentials: true
								},
								method: 'POST',
								success(res1) {
									console.log(res1);
									if(res1.data.code == 1){
										that.payOrder.orderNo = res1.data.data
										that.payOrder.payChannel = that.payment.id;
										that.payOrder.userId = that.user.id;
										that.payOrder.couponUserId = that.coupon.id;
										that.payOrder.paymentAmount = that.paymentAmount;
										let deposit;
										if(that.user.vipLevel >= 0 && that.car.noDeposit == 1){
											deposit = 0;
										}else{
											deposit = that.car.deposit;
										}
										that.payOrder.deposit = deposit;
										that.payPasswordShow = true;
										that.payConfirm = false;
									}
									else if(res1.data.code == 0){
										uni.showToast({
											icon:'none',
											title: res1.data.msg,
											duration:3000
										})
										that.payConfirm = false;
									}
									else{
										uni.showToast({
											icon:'none',
											title: res1.data.message,
											duration:3000
										})
										that.payConfirm = false;
									}
									//that.close();
								}
							})
						}
						else if(res.data.code == 0){
							uni.showToast({
								icon:'none',
								title: res.data.msg,
								duration:3000
							})
							that.payConfirm = false;
						}
						else{
							uni.showToast({
								icon:'none',
								title: res.data.message,
								duration:3000
							})
							that.payConfirm = false;
						}
						
					}
				})
				
			},
			payFalse(){
				this.payConfirm = false;	
			},
			changePayment(item){
				this.payment = item; 
			},
			getPayPasswordShow(e){
				console.log(e)
				this.payPasswordShow = e;
			},
			open() {

			}, 
			close() {
				this.$emit('getNoPay', false);
			},
			
		}
	}
</script>

<style>
	.pay{
		text-align: center;
		margin: 20rpx 0 80rpx;
	}
	.pay image{
		width: 46rpx;
		height: 46rpx;
		line-height: 46rpx;
		margin-right: 2rpx;
	}
	.pay .price{
		margin-top: 60rpx;
		margin-bottom: 120rpx;
		font-size: 52rpx;
	}
	.pay .price > span{
		font-size: 76rpx;
		font-weight: 650;
	}
	.pay .username,
	.pay .payment{
		padding: 20rpx 0;
		margin: 0 36rpx;
		overflow: hidden;
		color: #5e5e5e;
		height: 46rpx;
		line-height: 46rpx;
	}
	.pay .username{
		border-bottom: #e2e2e2 1rpx solid;
	}
	.pay .payment-list{
		margin: 10rpx 36rpx 50rpx;
		padding: 20rpx;
		background-color: #f9f9f9;
		border-radius: 20rpx;
		text-align: left;
		overflow: hidden;
		color: #5e5e5e;
	}

	.pay .payment-list .title{
		font-size: 26rpx;
		font-weight: 650;
		margin: 0 6rpx;
	}
	.pay .payment-list .payment-item{
		height: 46rpx;
		line-height: 46rpx;
		font-size: 30rpx;
		padding: 26rpx 10rpx;
		border-bottom: #e2e2e2 1rpx solid;
	}
	.pay .payment-list .payment-item span{
		padding: 0 3rpx;
	}
	.pay .button{
		display: inline-block;
		font-size: 36rpx;
		color: aliceblue;
		width: 540rpx;
		height: 80rpx;
		line-height: 80rpx;
		background-color: #0055ff;
		border-radius: 20rpx;
		margin-bottom: 30rpx;
		padding: 4rpx 0;
		box-shadow: 0 20rpx 40rpx #dce7ff;
	}
	.pay .others{
		display: flex;
		margin: 15rpx 235rpx;
		width: 280rpx;
		margin-bottom: 60rpx;
		justify-content: space-around;
	}
	.pay .others>image{
		display: inline-block;
		width: 66rpx;
		height: 66rpx;
	}
	.pay .footer{
		display: inline-block;
		width: 580rpx;
	}
	.pay .footer .radio{
		display: inline-block;
		width: 26rpx;
		height: 26rpx;
		border-radius: 26rpx;
		border: 2rpx solid #020202;
		margin-right: 10rpx;
	}
	.pay .footer>span{
		display: inline-block;
		font-size: 22rpx;
	}
	.pay .footer>span>span{
		color: #00aaff;
	}
</style>