<template>
	<view>
		<view>
			<u-modal :show="show" @confirm="show = false" title="用户查询">
				<scroll-view class="notice-scroll" :scroll-y="true" :scroll-with-animation="true">
					<u-search @search="findUserList()" @custom="findUserList()"
						:showAction="true" placeholder="搜索" v-model="selectUser" bgColor="#fff" borderColor="#eeeeee"></u-search>
					<u-cell-group>
						<view v-for="(item, idx) in userList">
							<u-cell @click="toDetails(item)" arrow-direction="left" :title="item.nickname" :label="item.username" :clickable="true">
								<u-icon slot="icon" size="90" :name="staticURL + item.userImg"></u-icon> 
							</u-cell>
						</view>
					</u-cell-group>
				</scroll-view>
			</u-modal>
		</view> 
		<view class="title">
			<span>消息管理</span>
			<image @click="show = true" :class="{'title-right' : true, 'rotate1' : show, 'rotate2' : !show}" src="../../static/chat/add.png"></image>
		</view>
		<u-search placeholder="搜索" @change="findUser()" :showAction="false"
		 v-model="select" bgColor="#fff" borderColor="#eeeeee"></u-search>
		
		<u-cell-group>
			<view v-for="(item, idx) in contactsList">
				<view>
					<u-cell @click="toDetails(item)" arrow-direction="left" :label="item.lastContent" :clickable="true">
						<view slot="title" class="u-slot-title">
							<text class="chat-title">{{item.nickname}}<span class="chat-title-username">（{{item.username}}）</span></text>
						</view>
						
						<u-icon slot="icon" size="90" :name="staticURL + item.userImg"></u-icon> 
						<!-- <u-badge count="99" :absolute="false" slot="right-icon"></u-badge> -->
						<span class="chat-time" slot="value">{{compareTime(item.lastContentTimestamp)}}</span>
						<u-badge class="chat-notice" :value="item.noticeContentNum" slot="right-icon"></u-badge>
					</u-cell>
				</view>
			</view>
		</u-cell-group>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				show: false,
				select: '',
				contactsList: [],
				contactsSumList: [],
				selectUser: '',
				userId: 0,
				userList: []
			}
		},
		onLoad(){
			this.userId = uni.getStorageSync("user").userId;
			this.listSimpleMessage();
			setInterval(() => {
				this.listSimpleMessage();
			}, 3000)
		},
		methods: {
			listSimpleMessage(name = ''){
				let that = this;
				uni.request({
					url: this.baseURL + "/messages",
					header:{
						"authorization": this.getToken()
					},
					method:'GET',//请求方式，必须为大写
					success: (res) => {
						//console.log(that.baseURL + "/messages", res);
						if(res.data.code == 200){ 
							//console.log(res.data.data)
							that.contactsSumList = res.data.data;
							this.findUser();
						}
						else{
							uni.showToast({
								title: res.data.msg,
								icon:'none',
								duration: 2000
							});
						}
					},
					fail: (err) => {
						uni.showToast({
							title: res.data.msg,
							icon:'none',
							duration: 2000
						});
					}
				})
			},
			findUser(){
				let that = this;
				let contactsSet = new Set();
				let search = this.select.toLowerCase();
				if(search){
					this.contactsSumList.filter(data => {
						Object.keys(data).some(key => {
							for(let i = 0; i < search.length; i++){
								if(key == 'nickname' || key == 'username'){
									console.log(data[key])
									if(String(data[key]).toLowerCase().indexOf(search[i]) > -1){
										contactsSet.add(data)
									}
								}
							}
						})
					})
					this.contactsList = Array.from(contactsSet);
				}else{
					this.contactsList = this.contactsSumList;
				}
			},
			findUserList(){
				let that = this;
				uni.request({
					url: this.baseURL + '/users/list/other?str=' + this.selectUser,
					header:{
						"authorization": this.getToken()
					},
					withCredentials:true,
					xhrFields: {
						withCredentials: true
					},
					method:'GET',
					success: (res) => {
						//console.log(res)
						if(res.data.code == 200){
							that.userList = res.data.data;
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
			toDetails(item){
				console.log(item)
				this.show = false;
				uni.request({
					url: this.baseURL + '/messages/update/' + item.userId + '/' + this.userId,
					header:{
						"authorization": this.getToken()
					},
					method:'GET',
					success: (res) => {
						console.log(this.baseURL + '/messages/update/' + item.userId + '/' + this.userId, res);
					}
				})
				uni.navigateTo({
				     url: './chat_details?chat=' + JSON.stringify(item),
					 fail (error) {
						console.log(error)
					}
				});
			}
		}
	}
</script>

<style>
	.title{
		position: relative;
		width: 750rpx;
		height: 100rpx;
		text-align: center;
		line-height: 100rpx;
		margin-top: 60rpx;
		color: #464646;
	}
	.title span{
		font-size: 36rpx;
		font-weight: 650;
	}
	.title .title-right{
		position: absolute;
		display: inline-block;
		width: 50rpx;
		height: 50rpx;
		padding: 25rpx;
		right: 0;
	}
	.rotate1{
		transform: rotate(90deg);
		transition-duration: 1s;
	}
	.rotate2{
		transform: rotate(-90deg);
		transition-duration: 1s;
	}
	.chat-time{
		display: inline-block;
		color: #919191;
		font-size: 24rpx;
		margin-top: -50rpx;
	}
	.chat-title{
		display: inline-block;
		color: #2c2c2c;
		font-size: 30rpx;
		max-width: 60vw;
		overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
	}
	.chat-title span{
		vertical-align: top;
	}
	.chat-title .chat-title-username{
		display: inline-block;
		font-size: 26rpx;
		color: #a9a9a9;
		margin-left: 10rpx;
	}
	.chat-notice{
		display: inline-block;
		margin-top: 46rpx;
		margin-left: -50rpx;
	}
</style>
