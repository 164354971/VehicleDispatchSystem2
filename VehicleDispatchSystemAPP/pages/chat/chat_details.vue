<template>
  <view>
    <view class="chat-name" @click="toChat()">
    	<image style="display: inline-block;vertical-align: middle;width: 60rpx;height: 60rpx;" src="../../static/return1.png"></image><span>{{getter.nickname}}</span>
    </view>
    <view >
		<scroll-view class="notice-scroll scroll-Y" 
		:scroll-into-view="bottomId" :scroll-y="true" :scroll-with-animation="true">
			<view>
				<view class="message" v-for="(item,index) in records" :key="index" >
					<view class="message-time" v-if="item.timeDisplay">{{compareTime(item.sendTimestamp)}}</view>
					<view style="text-align: right;" v-if="item.senderId == senderId" >
						<view class="message-content-send" v-html="item.content"></view>
						<view style="width: 24rpx;display: inline-block;"></view>
						<image class="message-head" :src="staticURL + senderImg"></image>
					</view>
					<view style="text-align: left;" v-else>
						<image class="message-head" :src="staticURL + getter.userImg"></image>
						<view style="width: 24rpx;display: inline-block;"></view>
						<view class="message-content-receive" v-html="item.content"></view>
					</view>
					
					<!-- <view class="message-time">{{item.sendTime}}</view> -->
				</view>
			</view>
			<view :id="'p' + records.length"></view>
		</scroll-view>
	</view>
	<view class="record" :style="{'bottom': `${KeyHeight}px`}">
		<view class="record-input">
			<u-input :adjust-position="false" :fixed="true" :focus="false" class="input"
			 v-model="recordInput" type="textarea" :height="400" :auto-height="true" border="true" border-color="#000" />
			 <image @click="send" class="button" :src="'../../static/chat/' + (recordInput == '' ? 'no-send.png' : 'send.png')"></image>
		</view>
		
	</view>
  </view>
</template>

<script>
export default {
	data() {
		return {
			getter: {},
			senderImg: '',
			records: [],
			senderId: 0,
			getterId: 0,
			recordInput: "",
			KeyHeight: 0,
			timeIdx: 0,
			sendTimestamp: 0,
			bottomId:''
		};
	},
	onLoad(options) {
		this.getter = JSON.parse(options.chat);
		this.senderImg = uni.getStorageSync("user").userImg;
		this.senderId = uni.getStorageSync("user").userId;
		this.getterId = this.getter.userId;
		this.getList();
		this.socket();
		uni.onKeyboardHeightChange((res) => {
		    //转化为rpx
		    this.KeyHeight = res.height;
			//console.log(this.KeyHeight)
		});
	},
	methods: {
		getList() {
			let that = this;
			uni.request({
				url: this.baseURL + '/messages/' + this.senderId + '/' + this.getterId,
				header:{
					"authorization": this.getToken()
				},
				method:'GET',
				success: (res) => {
					console.log(that.baseURL + '/messages/' + this.senderId + '/' + this.getterId, res)
					if(res.data.code == 200){
						this.records = res.data.data;
						this.sendTimeDisplay();
						this.autoScroll()
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
		sendTimeDisplay(){
			for(let i = 0; i < this.records.length; i++){
				if(this.records[i].sendTimestamp - this.sendTimestamp > 300000){
					this.records[i].timeDisplay = true;
					this.sendTimestamp = this.records[i].sendTimestamp;
				}else{
					this.records[i].timeDisplay = false;
				}
			}
		},
		autoScroll() {
			setTimeout(()=>{
				this.bottomId = 'p'+ this.records.length
			},500)
		},
		socket(){
			//创建webSocket
			this.webSocketTask = uni.connectSocket({
				url: this.wsURL + this.getToken(),
				header: {
					'content-type': 'application/json'
				},
				success(res) {
					console.log('ws连接成功', res);
				},
			})
		 
			// 监听WebSocket连接打开事件
			this.webSocketTask.onOpen((res) => {
				console.info("监听WebSocket连接打开事件", res)
			});
		 
			// 监听WebSocket错误
			uni.onSocketError((res) => {
				console.info("监听WebSocket错误" + res)
			});
		},
		send(){
			if(this.recordInput == '')return;
			this.message = {
				senderId: this.senderId,
				getterId: this.getterId,
				replyId: 0,
				content: this.recordInput,
				sendTimestamp: Date.parse(new Date()),
				messageType: '3'
			}
			this.webSocketTask.send({
				//data: JSON.stringify({type: 'pong'})
				data: JSON.stringify(this.message)
			});
			this.message.content = this.message.content.replace(/\n/g, "<br/>"),
			this.message.timeDisplay = this.message.sendTimestamp - this.sendTimestamp > 300000 ? true : false;
			this.sendTimestamp = this.message.sendTimestamp - this.sendTimestamp > 300000 ? this.message.sendTimestamp : this.sendTimestamp;
			this.records.push(this.message);
			this.autoScroll();
			this.recordInput = '';
			console.log(this.recordInput)
			
		},
		toChat() {
			uni.switchTab({
				url: './chat'
			})
		},
	},
	onShow(){
		let that = this;
	    // 接收websocket消息及处理
	    this.webSocketTask.onMessage((res) => {
			console.log(res)
			let data = JSON.parse(res.data);
			this.records.push(data);
	 
		});
	},
	onUnload() {
		uni.closeSocket({
			success: () => {
				console.info("退出成功")
			},
		})
	}
};
</script>

<style lang="less">
@left-right-margin: 40rpx;
.chat-name{
	width: 710rpx;
	height: 90rpx;
	text-align: left;
	padding: 82rpx 20rpx 0;
	color: #515151;
	span{
		display: inline-block;
		vertical-align: middle;
		font-size: 36rpx;
	}
}

.message {
	margin: 0px @left-right-margin 32rpx;
	display: flex;
	flex-direction: column;
	.message-time{
		text-align: center;
		color: #515151;
		font-size: 26rpx;
		margin: 20rpx 0;
	}
	.message-content-send {
		margin-top: 20rpx;
		width: auto;
		max-width: calc(100vw - 220rpx);
		word-wrap: break-word;
		box-sizing: border-box;
		padding: 20rpx 26rpx;
		line-height: 46rpx;
		background: pink;
		border-radius: 16rpx 0px 16rpx 16rpx;
		display: inline-block;
		vertical-align: top;
	}
	.message-content-receive {
		margin-top: 20rpx;
		width: auto;
		max-width: calc(100vw - 220rpx);
		word-wrap: break-word;
		box-sizing: border-box;
		padding: 20rpx 26rpx;
		line-height: 46rpx;
		background: #f0f0f0;
		border-radius: 0rpx 16px 16rpx 16rpx;
		display: inline-block;
		vertical-align: top;
	}
	.message-head{
		display: inline-block;
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		vertical-align: text-top;
	}
	.message-time {
		line-height: 32rpx;
		margin-top: 16rpx;
	}
}
.scroll-Y{
	height: calc(100vh - 310rpx);
}

.record{
	position: absolute;
	padding: 20rpx 40rpx 50rpx;
	background-color: #fff;
	.record-input{
		width: 670rpx; 
		.input{
			width: 530rpx;
			display: inline-block;
			vertical-align: middle;
			background-color: #f0f0f0;
			margin-right: 20rpx;
			margin-left: 10rpx;
		}
		.button{
			width: 70rpx;
			height: 70rpx;
			display: inline-block;
			vertical-align: middle;
			border: none;
			
		}
	}
}



</style>