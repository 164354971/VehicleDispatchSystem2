<template>
	<view class="login" :style="{'backgroundImage':'url(' + background +')'}">
		<view class="top_item">
			<text style="color: #000000;font-size: 22px;font-weight: bold;">密码登录</text>
			<text style="color: #000000;font-size: 20px;margin-right: 10px">手机号></text>
		</view>
		<view style="width: 100%; height: 450rpx;"></view>
		
		<view class="input-item">
			<view class="title-content">
				<text class="title">账号</text>
			</view>
			<input class="input" placeholder="请输入用户名" style="margin-left: 80rpx;" v-model="userInfo.username"/>
		</view>
		<view class="input-item">
			<view class="title-content">
				<text class="title">密码</text>
			</view>
			<input class="input" placeholder="请输入密码" style="margin-left: 80rpx;" v-model="userInfo.password" :password="openPassword"/>
			<image class="eyes" :src="eyes==true?'../../static/eye_display.png':'../../static/eye_hidden.png'" alt="" @click="passwordType()">
		</view>
		<view class="input-item">
			<view class="title-content"> 
				<text class="title">身份</text>
			</view>
			<radio-group @change="chang">
				<label v-for="item in radioGroup" :key="item">
					<radio :value="item" :checked="item==activeRadio" style="margin-left: 80rpx;"/>{{item}}
				</label> 
			</radio-group>
		</view>
		<view class="input-item">
			<view class="title-content"> 
				<text class="title">验证码</text>
			</view>
			<input class="input" placeholder="请输入验证码" style="margin-left: 50rpx;width: 284rpx;" v-model="userInfo.code"/>
			<image class="code"  :src="'data:image/png;base64,' + code" @click="updateCode()"></image>
			<span class="amplify"  @click="show = true">放大</span>

		</view>
		<view class="button" @click="login">登录</view>
		<u-modal :show="show" @confirm="show = false">
			<view style="width: 400rpx;height: 300rpx;text-align: center;">
				<image style="width: 400rpx;height: 150rpx;margin-bottom: 50rpx;" :src="'data:image/png;base64,' + code" @click="updateCode()">
				<input style="width: 290rpx;height: 48rpx;border: #000000 1rpx solid;border-radius: 10rpx;padding: 0 4rpx;margin: 0 50rpx;color: #000;" placeholder="请输入验证码" v-model="userInfo.code"/>
			</view>
		</u-modal>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				code: '',
				show: false,
				openPassword: true,
				eyes: 0,
				// 单选框数据
				activeRadio: '', //存的是单选按钮选中的value值
				radioGroup: ['用户', '管理员'],
				background: '../../static/login/loginbg1.png',
				user:{},
				userInfo:{}
			}
		},
		onLoad() {
			this.updateCode();
			this.setUser();
		},
		methods: {
			filterBase64(codeImages){
				return codeImages.replace(/[\r\n]/g, "");
			},
			updateCode(){
				var that = this;
				uni.request({
					url: this.baseURL + "/login",
					/* header: {
						// 'Content-Type': 'application/x-www-form-urlencoded'
						 'Content-Type': 'application/json' //自定义请求头信息
					},
					withCredentials:true,
					xhrFields: {
						withCredentials: true
					}, */
					method:'GET',//请求方式，必须为大写
					success: (res) => {
						console.log('接口返回------',res);
						if(res.data.code == 200){
							this.code = that.filterBase64(res.data.data);
						}else{
							uni.showToast({
								title: res.data.msg,
								icon:'none',
								duration: 2000
							});
						}
					}
				})
			},
			setUser(){
				var that = this;
				uni.getStorage({
				    key:"user",
				    success: function(res){
				        console.log(res.data); //user
						that.user = res.data;
				    }
				})
				uni.getStorage({
				    key:"userInfo",
				    success: function(res){
				        console.log(res.data); //userInfo
						that.userInfo = res.data;
				    }
				})
				uni.getStorage({
				    key:"activeRadio",
				    success: function(res){
				        console.log(res.data); 
						that.activeRadio = res.data;
				    }
				})
			},
			passwordType(){
				this.eyes = this.eyes == 0 ? 1 : 0;
				if(this.eyes == 0){
					this.openPassword = true;
				}else{
					this.openPassword = false;
				}
			},
			chang(e) {
				this.activeRadio = e.detail.value;  //选中按钮的value值
				console.log(this.activeRadio);
			},
			login() {
				var that = this;
				this.user = this.userInfo
				if(this.activeRadio == '用户'){
					uni.request({
						url: this.baseURL + "/login",
						data: this.user,
						header: {
							// 'Content-Type': 'application/x-www-form-urlencoded'
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
								that.user = res.data.data;
								//uni.removeStorageSync("user");
								uni.setStorageSync("user",that.user)
								uni.setStorageSync("userInfo",that.userInfo)
								uni.setStorageSync("activeRadio",that.activeRadio)
								// 1. 获取当前页面栈实例（此时最后一个元素为当前页）
								let pages = getCurrentPages() 
								 
								// 2. 上一页面实例
									// 注意是length长度，所以要想得到上一页面的实例需要 -2
									// 若要返回上上页面的实例就 -3，以此类推
								let prevPage = pages[pages.length -2] 
								 
								// 3. 给上一页面实例绑定getValue()方法和参数（注意是$vm）
								prevPage.$vm.gePreValue({login: false, user: that.user});
								uni.showToast({
									title: '登录成功！页面即将跳转...',
									icon:'none',
									duration: 2000
								});
								setTimeout(() => {
									
									uni.navigateBack({
										delta: 1,
									});
									
								}, 2000)
								
								
							}else{
								that.updateCode();
								uni.showToast({
									title: res.data.msg,
									icon:'none',
									duration: 2000
								});
							}
						}
					})
				}else if(this.activeRadio == '管理员'){
					uni.request({
						url: this.baseURL + "/login/adminLogin",
						data: this.user,
						header: {
							// 'Content-Type': 'application/x-www-form-urlencoded'
							 'Content-Type': 'application/json' //自定义请求头信息
						},
						withCredentials:true,
						xhrFields: {
							withCredentials: true
						},
						method:'POST',//请求方式，必须为大写
						success: (res) => {
							console.log('接口返回------',res);
							if(res.data.code == 200){
								setTimeout(() => {
									uni.showToast({
										title: res.data.msg,
										icon:'none',
										duration: 2000
									}); 
								}, 2000)
							}else{
								that.updateCode();
								uni.showToast({
									title: res.data.msg,
									icon:'none',
									duration: 2000
								});
								
							}
						}
					})
				}else{
					uni.showToast({
						title: '请选择用户身份',
						icon:'none',
						duration: 2000
					});
				}
				
			},
		}
	}
</script>

<style>
	page{
		background-color: #eee;
		height: 100%;
		margin: 0;
	}
	.login{
		width: 100%;
		height: 100%;
		margin: 0;
		padding: 32rpx 0;
		background-size: cover;
		position: relative;
		box-sizing:border-box;
		color: #d9e1f4;
	}
	.login:after{
	    content: "";
		width: 100%;
		height: auto;
	    position: absolute;
	    background: inherit;
	    filter: blur(5rpx);
	    z-index: 2;
	}
	.logo {
		display: block;
		width: 320rpx;
		height: 320rpx;
		margin: 100rpx auto 0;
	}
	.top_item {
		display: flex;
		margin-top: 50rpx;
		margin-left: 32rpx;
		margin-right: 32rpx;
		justify-content: space-between;
	}

	.input-item {
		display: flex;
		margin-left: 32rpx;
		margin-right: 32rpx;
		height: 50px;
		align-items: center;
		border-bottom: 1px solid #efeff4;
		margin-bottom: 20rpx;
		
		.title-content {
			  display: flex;
			  justify-content: flex-start;
			  align-items: center;
			  
			  .title {
				width: 2000rpx;
				font-size: 18px;
			  }
		}
		
		.input {
		  flex: 1;
		}
	}


  
	
	.button {
		height: 50px;
		line-height: 50px;
		margin-top: 20rpx;
		margin-left: 32rpx;
		margin-right: 32rpx;
		margin-bottom: 20rpx;
		border-radius: 50rpx;
		font-size: 20px;
		background-color: rgba(255, 255, 255, 0.5);
		text-align: center;
	}
	.code{
		width: 138rpx;
		height: 80rpx;
		margin-right: 10rpx;
		background-color: #ccc;
	}
	.eyes{
		width: 60rpx;
		height: 60rpx;
		margin-left: 66rpx;
	}
	.amplify{
		width: 100rpx;
		height: 60rpx;
		line-height: 60rpx;
		text-align: center;
		background-color: rgba(0, 0, 0, 0.4);
		border-radius: 30rpx;
		color: #fefff2;
	}
</style>