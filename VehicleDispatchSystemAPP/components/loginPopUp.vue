<template>
	<view>
		<u-popup :show="login" mode="bottom" @close="close" @open="open">
			<view class="login">
				<view class="title">
					<span>城科车辆 随时出动</span>
				</view>
				<view class="button" @click="toLogin">
					用户登录/注册
				</view>
				<br>
				<span style="font-size: 24rpx;">其他登录方式</span>
				<view class="others">
					<image src="../static/common/weixin.png" style="background-color: #fff; border-radius: 33rpx;" @click="wxLogin"></image>
					<image src="../static/common/qq.png"></image>
					<image src="../static/common/phone.png"></image>
				</view>
				<view class="footer">
					<image :src="img" class="radio" @click="change"></image>
					<span>
					我已阅读并同意
					<span>《用户服务条款》</span>和<span>《隐私政策》</span>
					</span>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		name: "loginPopUp",
		props: ['login'],
		data() {
			return {
				agreement: [],
				radio: true,
				img: '/static/login/select.png',
				user:{}
			}
		},
		methods: {
			wxLogin(){
				if(!this.radio){
					uni.showToast({
						icon:'none',
						title: '请阅读并勾选下方协议',
						duration: 2000
					})
					return;
				}
				this.handleThirdLoginApp()
			},
			handleThirdLoginApp() {
				var that = this;
				uni.getProvider({
					service: 'oauth',
					success: function(res) {
						if (~res.provider.indexOf('weixin')) {
							uni.login({
								provider: 'weixin',
								success: function(loginRes) {
									// loginRes 包含access_token，expires_in，openid，unionid等信息
									//这里只需要把这些数据传递给后台，让后台去请求微信的接口拿到用户信息就可以了，
									//然后返回登录状态
									that.getApploginData(loginRes); //请求登录接口方法
								},
			
								fail: function(res) {
									that.$refs.uToast.show({
										title: '微信登录失败',
										type: 'warning'
									});
								}
							});
						}
					}
				});
			},
			//请求登录接口方法
			getApploginData(data) {
				var that = this;
				//这边是前端自己去调微信用户信息的接口，根据接口需要请求，如果不需要前端去获取的话就交给后端，可省去次操作
				uni.request({
					url: 'https://api.weixin.qq.com/sns/userinfo?access_token=' + data.authResult.access_token + '&openid=' + data.authResult.openid,
					method: 'GET',
					dataType: 'json',
					header: {
						'content-type': 'application/x-www-form-urlencoded' // 默认值
					},
			
					success(res) {
						console.log('【登录回调】', res);
						if(res.statusCode == 200){
							let data = {
								thirdUniqueAccount: res.data.openid,
								type: 1,
								nickname: res.data.nickname,
								sex: res.data.sex,
								img: res.data.headimgurl
							}
							uni.request({
								url: that.baseURL + "/login/thirdAccountLogin",
								data: data,
								header: {
									 'Content-Type': 'application/json' //自定义请求头信息
								},
								withCredentials:true,
								xhrFields: {
									withCredentials: true
								},
								method:'POST',//请求方式，必须为大写
								success: (res) => {
									console.log('接口返回------',res.data);
									if(res.data.code == 200){
										uni.showToast({
											title: res.data.msg,
											icon:'none',
											duration: 1000
										}); // 这里写要延时执行的代码
										this.user = res.data.data;
										//uni.removeStorageSync("user");
										uni.setStorageSync("user",this.user)
										uni.setStorageSync("activeRadio","用户")
										
										
										that.$emit('setUser', this.user);
										that.$emit('setLogin', false);//关闭弹出窗口
										uni.showToast({
											icon:'none',
											title: '微信登录成功！',
											duration: 2000
										})
										that.close();
										
									}else{
										uni.showToast({
											title: res.data.msg,
											icon:'none',
											duration: 2000
										});
									}
								}
							})
						}
					},
					fail() {
						that.$refs.uToast.show({
							title: '微信登录失败',
							type: 'warning'
						});
					}
				});
			},
			open() {

			}, 
			close() {
				this.$emit('setLogin', false);
				
			},
			change(){
				console.log(this.radio)
				this.radio = this.radio == true ? false : true;
				if(this.radio){
					this.img = '/static/login/select.png';
				}else{
					this.img = '/static/login/no-select.png';
				}
			},
			toLogin(){
				uni.showToast({
					title: '即将跳转至登录页面',
					icon:'none',
					duration: 1500
				})
				setTimeout(() => {
					uni.navigateTo({
					     url: '../login/login'
					});
				}, 1500)
				
			},
		}
	}
</script>

<style>
	.login{
		text-align: center;
		padding: 20rpx 0 80rpx;
	}
	.login .title{
		margin-bottom: 80rpx;
	}
	.login .title>span{
		font-size: 32rpx;
		font-weight: 650;
	}
	.login .button{
		display: inline-block;
		color: aliceblue;
		width: 540rpx;
		height: 80rpx;
		line-height: 80rpx;
		background-color: rgba(0, 0, 255, 0.7);
		border-radius: 80rpx;
		margin-bottom: 30rpx;
		box-shadow: 0 20rpx 40rpx #dce7ff;
	}
	.login .others{
		display: flex;
		margin: 15rpx 235rpx;
		width: 280rpx;
		margin-bottom: 60rpx;
		justify-content: space-around;
	}
	.login .others>image{
		display: inline-block;
		width: 66rpx;
		height: 66rpx;
	}
	.login .footer{
		display: inline-block;
		width: 580rpx;
	}
	.login .footer .radio{
		display: inline-block;
		width: 26rpx;
		height: 26rpx;
		border-radius: 26rpx;
		border: 2rpx solid #020202;
		margin-right: 10rpx;
	}
	.login .footer>span{
		display: inline-block;
		font-size: 22rpx;
	}
	.login .footer>span>span{
		color: #00aaff;
	}
</style>