<template>
	<view class="warp">
		<view v-for="(item,idx) in list">{{item.senderId}}:{{item.content}} => {{item.getterId}}</view>
	    <u-input v-model="text"></u-input>
		<button @click="send">发送</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list:[],
				text:'',
				message:{
					
				}
			}
		},
		onLoad(){
		    this.socket()
		},
		methods:{
		    socket(){
				//创建webSocket
				this.webSocketTask = uni.connectSocket({
					url: 'ws://192.168.10.10:9999/globalWs/message?token=' + this.getToken(),
					header: {
						'content-type': 'application/json'
					},
					success(res) {
						console.log('成功', res);
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
				this.message = {
					senderId: 0,
					getterId: 2,
					content: this.text,
					sendTime: '2024-02-05 11:22:33',
					messageType: '3'
				}
				this.webSocketTask.send({
					//data: JSON.stringify({type: 'pong'})
					data: JSON.stringify(this.message)
				});
				this.list.push(this.message);
			}
		},
		onShow(){
			let that = this;
		    // 接收websocket消息及处理
		    this.webSocketTask.onMessage((res) => {
				console.log(res)
				let data = JSON.parse(res.data);
				this.list.push(data);
				console.log(data);
				
				switch (data.type) {
					// 服务端ping客户端
					case 'ping':
						this.webSocketTask.send({
							data: JSON.stringify({
								type: 'pong'
							})
						});
						break;
					// 登录 更新用户列表
					case 'init':
						uni.setStorageSync('client_id', data.client_id);
						this.$u.post('接口', {
							client_id: data.client_id,
							user_id: this.userId
						}).then(res => {
							this.webSocketTask.send({
								data: JSON.stringify({
									type: 'bindUid',
									user_id: this.userId
								})
							});
						});
						break;
					case 'simple':
						this.userList.push(data.data)
						this.scrollToBottom()
						break;
					case 'group':
						// this.groupMenuList()
						this.userList.push(data.data)
						this.scrollToBottom()
						break
				}
		 
			});
		},
		 
		onUnload() {
			uni.closeSocket({
				success: () => {
					console.info("退出成功")
				},
			})
		}
		
	}
</script>

<style>
	

</style>