<template>
	<view>
		<view v-if="evaluateReplyDTO != undefined">
			<u-popup z-index="100090"
			:show="replyInputShow" mode="bottom" @close="close" round="10" @open="open" :safeAreaInsetBottom="true" :overlayOpacity="0.2">
				<view class="reply-input">
					<view style="width: 600rpx; display: inline-block;vertical-align: middle;">
						<u-input :adjust-position="false" :fixed="true" :focus="true"
						:placeholder="'回复 ' + evaluateReplyDTO.replyUserNickname + ':'" v-model="evaluateReplyDTO.evaluate" type="textarea" :height="400" :auto-height="true" border="true" border-color="#000" />
					</view>
					<view class="button" @click="commit()">发布</view>
				</view>
			</u-popup>
		</view>
	</view>
</template>

<script>
	export default {
		name:"replyPopUp",
		props: ['replyInputShow', 'replyObj', 'evaluateId'],
		data() {
			return {
				text:'',
				evaluateReplyDTO: {},
				localEvaluateId: this.evaluateId
			};
		},
		methods:{
			commit(){
				let that = this;
				if(this.evaluateReplyDTO.evaluate == ''){
					uni.showToast({
						icon:'none',
						title:'没有回复内容',
					}, 1500);
					return;
				}
				let token = uni.getStorageSync("user").token;
				if(token == undefined)return;
				//将回复入库
				uni.request({
					url: this.baseURL + "/evaluates/reply",
					header:{
						"authorization": token
					},
					method:'POST',
					data: this.evaluateReplyDTO,
					success: (res) => {
						if(res.data.code == 200){
							uni.showToast({
								icon:'none',
								title:'回复已提交',
							}, 1500);
						}
						this.evaluateReplyDTO.evaluate = ''
						this.localEvaluateId = this.evaluateReplyDTO.evaluateId;
						this.close();
					},
					
				})
				
			},
			open(){
				console.log(this.replyObj)
				this.evaluateReplyDTO = this.replyObj;
			},
			close(){
				let data = {
					replyInputShow: false,
					replyObj: this.replyObj,
					evaluateId: this.localEvaluateId 
				}
				this.$emit('setReply', data);
			}
		}
	}
</script>

<style>
	.button{
		width: 150rpx;
		display: inline-block;
		vertical-align: middle;
		border: none;
		text-align: center;
		color: #ccc;
		font-size: 30rpx;
	}
</style>