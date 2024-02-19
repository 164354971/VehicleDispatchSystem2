<template>
	<view v-if="evaluateObj.id > 0" style="position: relative;">
		<u-popup :show="reply" close-icon-size="50" mode="bottom" @close="close" @open="open" :closeable="true" round="10" :safeAreaInsetBottom="true" :safeAreaInsetTop="false">
			<scroll-view class="notice-scroll" :scroll-y="true" :scroll-with-animation="true">
				<view class="reply">
					<h4 style="background-color: #fff;padding: 20rpx 0 10rpx;">该评论的全部回复</h4>
					<view>
						<view style="pargin: 30rpx 0;background-color: #fff;">
							<image :src="staticURL + evaluateObj.userImg"
								style="width: 66rpx;height: 66rpx;vertical-align: top;
								border-radius: 33rpx;margin: 12rpx 0%;">
							</image><!-- 头像 -->
							<view class="user-evaluate">
								<view>
									<span class="eva-name">{{evaluateObj.userNickname}}</span><span class="vip" v-if="evaluateObj.vipLevel > 0">vip{{evaluateObj.vipLevel}}</span>
									<u-rate style="display: inline-block;" count="5" v-model="evaluateObj.score" :readonly="true"></u-rate>
								</view>
								<view style="margin-top: -12rpx;width: 100%;">
									<span class="eva-label" >{{evaluateObj.evaluateImgVO.carName}}.租用{{evaluateObj.rentDays}}天</span><span class="eva-price">日租:￥{{evaluateObj.dayPrice}}</span>
								</view>
								<rich-text :nodes="evaluateObj.evaluate" style="font-size: 28rpx;text-align: left;color: #222;"></rich-text>
								<view>
									<span class="eva-time">{{evaluateObj.createTime}}</span>
								</view>
								<view class="user-evaluate-img" v-for="(img, imgKey) in evaluateObj.evaluateImgVO.evaluateImgList">
									<image class="eva-image"  :src="baseURL + img"></image>
								</view> 
							</view>
						</view>
						<view v-for="(item, key) in evaluateReplyList" style="padding: 10rpx 0;">
							<image :src="staticURL + item.userImg"
								style="width: 66rpx;height: 66rpx;vertical-align: top;
								border-radius: 33rpx;margin: 12rpx 0%;">
							</image><!-- 头像 -->
							<view class="user-evaluate">
								<view>
									<span class="eva-name">{{item.userNickname}}</span>
								</view>
								<view v-if="item.evaluateReplyId != 0" style="background-color: #ccc; border-radius: 6rpx;padding: 2rpx 0;">
									<rich-text :nodes="'@' + replyMap.get(item.evaluateReplyId).userNickname + ':' + replyMap.get(item.evaluateReplyId).evaluate" 
									style="font-size: 28rpx;text-align: left;color: #f6f6f6;"></rich-text>
								</view>
								<rich-text :nodes="item.evaluate" style="font-size: 28rpx;text-align: left;color: #222;"></rich-text>
								<view>
									<span class="eva-time">{{item.createTime}}</span><view style="display: inline-block;width: 12rpx;"></view>
									<span class="eva-reply" @click="replySomeOne(item)">回复</span>
								</view>
								
							</view>
						</view>
					</view> 
				</view>
			</scroll-view>
			<view class="reply-input" @click="openReplyInput(evaluateObj)">
				回复 {{replyNickname}}: <span v-if="replyObj.evaluate !=undefined"></span></view>
			<replyPopUp :replyInputShow="replyInputShow" :replyObj="replyObj" :evaluateId="evaluateId" @setReply="setReply"></replyPopUp>
		</u-popup>
	</view>
</template>

<script>
	import replyPopUp from './replyPopUp.vue'
	export default {
		name:"brandReplyPopUp",
		props:['reply', 'evaluateObj', 'evaluateId'],
		components:{
			'replyPopUp': replyPopUp
		},
		data() {
			return {
				evaluateReplyList:[],
				value:'',
				replyNickname:'',
				replyMap: new Map(),
				replyInputShow: false,
				replyObj: {
					evaluate: '',
					evaluateReplyId: 0
				},
				localEvaluateId: this.evaluateId
			};
		},
		methods:{
			setReply(data){
				console.log(data)
				this.replyInputShow = data.replyInputShow;
				this.replyObj = data.replyObj;
				this.localEvaluateId = data.evaluateId;
				if(this.localEvaluateId > 0){
					this.open();
					this.localEvaluateId = 0;
				}
			},
			replySomeOne(e){
				let user = uni.getStorageSync("user");
				this.replyObj = {
					userId: user.id,
					userNickname: user.nickname,
					userImg: user.userImg,
					replyUserId: e.userId,
					replyUserNickname: e.userNickname,
					evaluateId: e.evaluateId,
					evaluate: this.replyObj.evaluate,
					evaluateReplyId: e.id
				}
				this.replyInputShow = true
			},
			openReplyInput(e){
				let user = uni.getStorageSync("user");
				this.replyObj = {
					userId: user.id,
					userNickname: user.nickname,
					userImg: user.userImg,
					replyUserId: e.userId,
					replyUserNickname: e.userNickname,
					evaluateId: e.id,
					evaluate: this.replyObj.evaluate,
					evaluateReplyId: 0 
				}
				console.log(this.replyObj)
				this.replyInputShow = true
			},
			open() { 
				let that = this;
				console.log(this.evaluateObj.id)
				this.replyNickname = this.evaluateObj.userNickname
				uni.request({
					url: this.baseURL + "/evaluates/reply/" + this.evaluateObj.id,
					method:'GET',
					success: (res) => {
						console.log(res.data)
						if(res.data.code == 200){
							that.evaluateReplyList = res.data.data;
							that.replyMap = new Map();
							for(let i = 0; i < that.evaluateReplyList.length; i++){
								let obj = that.evaluateReplyList[i];
								if(obj.evaluateReplyId != 0){
									for(let j = 0; j < that.evaluateReplyList.length; j++){
										if(that.evaluateReplyList[j].id == obj.evaluateReplyId){
											that.replyMap.set(obj.evaluateReplyId, that.evaluateReplyList[j]);
											break;
										}
									}
									
								}
							}
						}
					}
				})
			}, 
			close() {
				this.$emit('setBrandReply', false);
			},
		}
	}
</script>

<style>
	.reply{
		text-align: center;
		height: 80vh;
		padding: 0 0 80rpx;
		background-color: #fafafa;
	}
	.reply-input{
		padding: 0 20rpx;
		height: 60rpx;
		line-height: 60rpx;
		background-color: #f2f2f9;
		color: #91949e;
		display: flex;
	}
	.user-evaluate {
		display: inline-block;
		width: 566rpx;
		margin-left: 16rpx;
		text-align: left;
		vertical-align: top;
	}
	.user-evaluate span{
		font-size: 26rpx;
	}
	.user-evaluate .eva-name{
		color: #7d8191;
		font-size: 26rpx;
		font-weight: 700;
	}
	.user-evaluate .eva-label{
		color: #91949e;
		font-size: 22rpx;
		font-weight: 500;
	}
	.user-evaluate .eva-time{
		color: #a3a6b1;
		font-size: 22rpx;
		font-weight: 500;
	}
	.user-evaluate .eva-reply{
		color: #2a2b2e;
		font-size: 24rpx;
		font-weight: 500;
	}
	.user-evaluate .eva-price { 
		float: right;
		margin-top: 10rpx;
		background-color: #fff6dd;
		color: #ff6769;
		font-weight: 550;
		font-size: 22rpx;
	}
	.user-evaluate .eva-reply-list{
		color: #0600c4;
		font-weight: 700;
	}
	.user-evaluate-img{
		display: inline-block;
		text-align: left;
	}
	.user-evaluate-img .eva-image{
		width: 232rpx;
		height: 174rpx; 
		object-fit: cover;
	}
	.vip{
		font-size: 22rpx;
		background-image: linear-gradient(to right, #262639 , #565658);
		color: #ffdf7d;
		border-radius: 14rpx;
		padding: 0 12rpx 0 6rpx;
		margin-left: 6rpx;
		font-weight: bold;
		font-style: italic;
	}
</style>