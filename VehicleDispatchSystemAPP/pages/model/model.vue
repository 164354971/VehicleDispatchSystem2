<template>
	<view class="content">
		<view>
			<u-modal :show="show" @confirm="show = false" :title="brand.brand" :content='brand.info'>
				<scroll-view class="notice-scroll" :scroll-y="true" :scroll-with-animation="true">
					<view class="model">
						<image :src="staticURL + brand.img"></image>
						<rich-text :nodes="brand.info" style="font-size: 26rpx;text-align: left;"></rich-text>
					</view>
				</scroll-view>
			</u-modal>
		</view>
		<view>
			<u-modal :zIndex="10000" :show="evaluateListShow" @confirm="evaluateListShow = false" title="全部评价">
				<scroll-view class="notice-scroll" :scroll-y="true" :scroll-with-animation="true">
					<view class="eva-model">
						<view v-for="(item, key) in evaluateList">
							<view>
								<image :src="staticURL + item.userImg"
									style="width: 66rpx;height: 66rpx;vertical-align: top;
									border-radius: 33rpx;margin: 6rpx 0%;">
								</image><!-- 头像 -->
								<view class="user-evaluate">
									<view>
										<span class="eva-name">{{item.userNickname}}</span><span class="vip" v-if="item.vipLevel > 0">vip{{item.vipLevel}}</span>
										<u-rate style="display: inline-block;" count="5" v-model="item.score" :disabled="false"></u-rate>
									</view>
									<view style="margin-top: -12rpx;width: 100%;">
										<span class="eva-label" >{{item.evaluateImgVO.carName}}.租用{{item.rentDays}}天</span><span class="eva-price">日租:￥{{item.dayPrice}}</span>
									</view>
									<rich-text :nodes="item.evaluate" style="font-size: 28rpx;text-align: left;color: #222;"></rich-text>
									<view>
										<span class="eva-time">{{item.createTime}}</span><view style="display: inline-block;width: 12rpx;"></view>
										<span class="eva-reply" @click="openReplyInput(item)">回复</span>
									</view>
									<view v-if="item.replyCount > 0">
										<span class="eva-reply-list" @click="lookReplyList(item)"> ——— 展开{{item.replyCount}}条回复</span>
									</view> 
									<view class="user-evaluate-img" v-for="(img, imgKey) in item.evaluateImgVO.evaluateImgList">
										<image class="eva-image"  :src="staticURL + img"></image>
									</view> 
								</view>
							</view> 
						</view>
					</view>
				</scroll-view>
			</u-modal>
		</view>
		<view class="title">
			<span @click="toBrand()">< 返回</span>
			<span>{{brand.brand}}</span>
			<span @click="show = true">品牌介绍</span>
		</view>
		<view class="list" style="padding: 0;">
			<view class="card-list">
				<span>最新热评</span>
			</view>
		</view>
		
		<view class="swiper-frame" v-if="idx >= 0" @click="getEvaluateListShow">
			<u-swiper class="swiper" :list="newEvaluateList" keyName="coverImg" indicator indicatorMode="line" height="210"
				interval="5000" circular @change="changeSwiper"></u-swiper>
			<view class="swiper-text">
				<image :src="newEvaluateList[idx].userImg" 
				style="width: 36rpx;height: 36rpx;vertical-align: top;
				border-radius: 18rpx;margin: 6rpx 0%;"></image>
				<u--text size="20" class="text" :lines="3" :text="':' + newEvaluateList[idx].evaluate"></u--text>
				<u-rate count="5" v-model="newEvaluateList[idx].score" :readonly="true"></u-rate>
				<span class="price">日租:￥{{newEvaluateList[idx].dayPrice}}</span>
			</view>
		</view>
		<view v-else>
			<u-empty
					:text="evaluate"
			        icon="../../static/empty/no_evaluate.png"
			>
			</u-empty>
		</view>
		<view class="list">
			<view class="type-list">
				<span :class="key == type ? 'type' : ''" v-for="(item, key) in typeList"
					@click="switchTab(key)">{{item}}</span>
			</view>
			<view class="card-list" v-show="type == 0 || type == 1">
				<span>轿车</span>
				<view class="card-item" v-for="(item, key) in modelList" v-if="item.type == '轿车'" @click="toCar(item)">
					<image :src="staticURL + item.img"></image>
					<view class="bottom">
						<span class="name">{{item.model}}</span>
						<br>
						<p class="market-price">市售价：{{item.marketPrice}} </p>
						<view class="bottom-label">
							<!-- <view class="label vip">
								<image src="../../static/details/vip.png"></image>
								<span>会员免押</span>
							</view> -->
							<view class="label car-green"
								v-if="item.energy == '插混' || item.energy == '增程' || item.energy == '纯电' ">
								<image src="../../static/green_car.png"></image>
								<span>新能源</span>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="card-list" v-show="type == 0 || type == 2">
				<span>SUV</span>
				<view class="card-item" v-for="(item, key) in modelList" v-if="item.type == 'SUV'" @click="toCar(item)">
					<image :src="staticURL + item.img"></image>
					<view class="bottom">
						<span class="name">{{item.model}}</span>
						<br>
						<p class="market-price">市售价：{{item.marketPrice}} </p>
						<view class="bottom-label">
							<!-- <view class="label vip">
								<image src="../../static/details/vip.png"></image>
								<span>会员免押</span>
							</view> -->
							<view class="label car-green"
								v-if="item.energy == '插混' || item.energy == '增程' || item.energy == '纯电' ">
								<image src="../../static/green_car.png"></image>
								<span>新能源</span>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="card-list" v-show="type == 0 || type == 3">
				<span>MPV</span>
				<view class="card-item" v-for="(item, key) in modelList" v-if="item.type == 'MPV'" @click="toCar(item)">
					<image :src="staticURL + item.img"></image>
					<view class="bottom">
						<span class="name">{{item.model}}</span>
						<br>
						<p class="market-price">市售价：{{item.marketPrice}}</p>
						<view class="bottom-label">
							<!-- <view class="label vip">
								<image src="../../static/details/vip.png"></image>
								<span>会员免押</span>
							</view> -->
							<view class="label car-green"
								v-if="item.energy == '插混' || item.energy == '增程' || item.energy == '纯电' ">
								<image src="../../static/green_car.png"></image>
								<span>新能源</span>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="card-list" v-show="type == 0 || type == 4">
				<span>新能源</span>
				<view class="card-item" v-for="(item, key) in modelList"
					v-if="item.energy == '插混' || item.energy == '增程' || item.energy == '纯电' " @click="toCar(item)">
					<image :src="staticURL + item.img"></image>
					<view class="bottom">
						<span class="name">{{item.model}}</span>
						<br>
						<p class="market-price">市售价：{{item.marketPrice}} </p>
						<view class="bottom-label">
							<!-- <view class="label vip">
								<image src="../../static/details/vip.png"></image>
								<span>会员免押</span>
							</view> -->
							<view class="label car-green"
								v-if="item.energy == '插混' || item.energy == '增程' || item.energy == '纯电' ">
								<image src="../../static/green_car.png"></image>
								<span>新能源</span>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="card-list" v-show="type == 0 || type == 5">
				<span>其他</span>
				<view class="card-item" v-for="(item, key) in modelList" v-if="item.type == '其他'" @click="toCar(item)">
					<image :src="staticURL + item.img"></image>
					<view class="bottom">
						<span class="name">{{item.model}}</span>
						<br>
						<p class="market-price">市售价：{{item.marketPrice}}</p>
						<view class="bottom-label">
							<view class="label vip">
								<image src="../../static/details/vip.png"></image>
								<span>会员免押</span>
							</view>
							<view class="label car-green"
								v-if="item.energy == '插混' || item.energy == '增程' || item.energy == '纯电' ">
								<image src="../../static/green_car.png"></image>
								<span>新能源</span>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<brandReplyPopUp :reply="reply" :evaluateObj="evaluateObj" :evaluateId="evaluateId" @setBrandReply="setBrandReply"></brandReplyPopUp>
		<replyPopUp :replyInputShow="replyInputShow" :replyObj="replyObj" :evaluateId="evaluateId" @setReply="setReply"></replyPopUp>
	</view>
</template>

<script>
	import brandReplyPopUp from '../../components/brandReplyPopUp.vue'
	import replyPopUp from '../../components/replyPopUp.vue'
	export default {
		components:{
			"brandReplyPopUp":brandReplyPopUp,
			"replyPopUp":replyPopUp
		},
		data() {
			return {
				idx: -1,
				duration: 5030,
				show: false,
				timer: null,
				typeList: ['全部', '轿车', 'SUV', 'MPV', '新能源', '其他'],
				type: 0,
				brand: {},
				modelList: [],
				newEvaluateList: [],
				evaluate:'暂无当前品牌最新评价',
				evaluateListShow: false,
				evaluateList: [],
				queryEvaluatePage:{},
				reply: false,
				evaluateObj: {},
				replyInputShow: false,
				replyObj: {},
				evaluateId: 0
			}
		},
		onLoad: function(options) {
			var that = this;
			that.brand = JSON.parse(options.brand);
			console.log("brand:", that.brand)
			this.setQueryEvaluatePage();
			this.getModelList();
			this.getNewEvaluateList();
		},
		methods: {
			setBrandReply(reply){
				this.reply = reply;
			},
			setReply(data){
				console.log(data)
				this.replyInputShow = data.replyInputShow;
				this.replyObj = data.replyObj;
				this.evaluateId = data.evaluateId;
				if(this.evaluateId > 0){
					this.queryEvaluatePage.page = 0;
					for(let i = 0; i < this.evaluateList.length; i++){
						if(this.evaluateList[i].id == this.evaluateId){
							this.evaluateList[i].replyCount++;
							break;
						}
					}
					this.evaluateId = 0;
				}
			},
			lookReplyList(item){
				this.evaluateObj = item;
				this.reply = true;
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
			changeSwiper(e){
				//console.log(e)
				this.idx = e.current;
			},
			setQueryEvaluatePage(){
				this.queryEvaluatePage = {
					type:1,
					queryId:this.brand.id,
					queryType:1,
					page:0,
					size:10
				};
			},
			getEvaluateListShow(){
				let that = this;
				console.log(this.baseURL + '/evaluates/page');
				this.queryEvaluatePage.page++;
				uni.request({
					url: this.baseURL + '/evaluates/page',
					method: 'POST',
					data: this.queryEvaluatePage,
					success: (res) => {
						console.log(res.data.data);
						if(res.data.code == 200){
							let current = res.data.data.current;
							let total = res.data.data.total;
							if(current == that.queryEvaluatePage.page && total != 0){
								that.evaluateList = that.evaluateList.concat(res.data.data.records)
							}
							that.evaluateListShow = true;
						}else {
							uni.showToast({
								title: res.data.msg,
								icon: 'none',
								duration: 2000
							});
						}
						
					}
				})
			},
			getModelList() {
				let that = this;
				console.log(this.baseURL + '/models/list/' + this.brand.id)
				uni.request({
					url: this.baseURL + '/models/list/' + this.brand.id,
					method: 'GET', //请求方式，必须为大写
					success: (res) => {
						console.log('/models/list', res);
						if (res.data.code == 200) {
							console.log(res.data.data)
							that.modelList = res.data.data;
						} else {
							uni.showToast({
								title: res.data.msg,
								icon: 'none',
								duration: 2000
							});
						}
					}
				})
			},
			getNewEvaluateList() {
				let that = this;
				uni.request({
					url: this.baseURL + '/evaluates/getNewEvaluateList/' + this.brand.id,
					method: 'GET', //请求方式，必须为大写
					success: (res) => {
						console.log('/evaluates/getNewEvaluateList/', res);
						if (res.data.code == 200) {
							console.log(res.data.data)
							that.newEvaluateList = res.data.data;
							let len = that.newEvaluateList.length;
							if (len == 0) 
								that.idx = -1;
							else 
								that.idx = 0;
							for (let i = 0; i < len; i++) {
								that.newEvaluateList[i].coverImg = that.staticURL + that.newEvaluateList[i].coverImg
								that.newEvaluateList[i].userImg = that.staticURL + that.newEvaluateList[i].userImg
							}
						} else {
							// uni.showToast({
							// 	title: res.data.msg,
							// 	icon: 'none',
							// 	duration: 2000
							// });
						}
					}
				})
			},
			toBrand() {
				uni.switchTab({
					url: '../brand/brand'
				})
			},
			toCar(item) {
				uni.navigateTo({
					url: '../car/car?model=' + JSON.stringify(item)
				})
			},
			switchTab(i) {
				this.type = i;
			}
		}
	}
</script>

<style lang="scss">
	.content {
		width: 750rpx;
		min-height: 100vh;
		background-color: #f8f7fe
	}

	.title {
		width: 750rpx;
		height: 100rpx;
		line-height: 100rpx;
		padding-top: 60rpx;
		color: #464646;
		background-color: #fff;
	}

	.title view {
		display: inline-block;
		width: 220rpx;
		height: 120rpx;
	}

	.title view image {
		display: inline-block;
		width: 20rpx;
		height: 20rpx;
		vertical-align: middle;
	} 

	.title span {
		font-size: 36rpx;
		font-weight: 650;
		width: 220rpx;
		display: inline-block;
		text-align: center;
		padding: 0 15rpx;
		vertical-align: middle;
	}

	.title span:first-child {
		text-align: left;
		font-weight: 450;
		font-size: 36rpx;
	}

	.title span:last-child {
		text-align: right;
		font-weight: 450;
		font-size: 32rpx;
	}

	.swiper-frame {
		padding: 20rpx 36rpx;
		width: 678rpx;
		background-color: #f5f5f6;
		position: relative;
	}

	.swiper-frame .swiper {
		width: 280rpx;
	}

	.swiper-frame .swiper-text {
		position: absolute;
		width: 400rpx;
		top: 18rpx;
		right: 0rpx;
		padding: 10rpx;
	}

	.swiper-frame .swiper-text .text {
		display: inline-block;
		width: 360rpx;
		height: 160rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		vertical-align: top;
	}

	.swiper-frame .swiper-text span {

		font-size: 30rpx;
		margin-top: 4rpx;
		padding: 2rpx 4rpx;
		border-radius: 2rpx;
	}

	.swiper-frame .swiper-text .count {
		float: left;
		background-color: #55aaff;
		color: #f9f8e2;
		font-size: 24rpx;
	}

	.swiper-frame .swiper-text .price {
		float: right;
		margin-top: -26rpx;
		background-color: #fff6dd;
		color: #ff6769;
		font-weight: 550;
		font-size: 22rpx;
	}

	.list {
		padding: 20rpx;

	}

	.type-list {
		text-align: center;
		margin: 12rpx 2rpx;
	}

	.type-list>span {
		font-size: 30rpx;
		padding: 6rpx 16rpx;
		margin: 0 12rpx;
		border-radius: 4rpx;
		border: #f0f0f3 1rpx solid;
		background-color: #f0f0f3;
		color: #565656;
		font-size: 24rpx;
	}

	.type-list .type {
		border: #a1a1a1 1rpx solid;
		background-color: #fff;
		color: #000;
	}

	.list .card-list {
		text-align: center;
	}

	.list .card-list>span {
		font-size: 23rpx;
		color: #757575;
	}

	.list .card-list .card-item {
		text-align: left;
		width: 650rpx;

		padding: 20rpx;
		margin: 16rpx 10rpx;
		background-color: #ffffff;
		border-radius: 6rpx;
		box-shadow: 1rpx 1rpx 10rpx #f6f6f6;
	}

	.list .card-list .card-item image {
		display: inline-block;
		width: 240rpx;
		height: 135rpx;
		vertical-align: middle;
	}

	.list .card-list .card-item .bottom {
		display: inline-block;
		width: 340rpx;
		margin-left: 50rpx;
		vertical-align: middle;
	}

	.list .card-list .card-item .bottom .name {
		font-size: 28rpx;
		font-weight: 550;
		margin-bottom: 20rpx;
	}

	.list .card-list .card-item .bottom .market-price {
		font-size: 20rpx;
		overflow: hidden;
	}

	.list .card-list .card-item .bottom .bottom-label {
		display: inline-block;
		width: 340rpx;
		font-size: 16rpx;
	}

	.list .card-list .card-item .bottom .bottom-label .label {
		border-radius: 10rpx;
		display: inline-block;

		margin: 16rpx 6rpx;
		padding: 3rpx;
	}

	.list .card-list .card-item .bottom .bottom-label .label image {
		width: 34rpx;
		height: 34rpx;
		display: inline-block;
		margin-right: 6rpx;
		vertical-align: bottom;
	}

	.list .card-list .card-item .bottom .bottom-label .label span {
		vertical-align: bottom;
		font-size: 22rpx
	}

	.model {
		text-align: center;
		height: 500rpx;
	}

	.model>image {
		width: 250rpx;
		height: 250rpx;
	}
	
	.eva-model {
		text-align: center;
		max-height: 800rpx;
	}
	
	.user-evaluate {
		display: inline-block;
		width: 466rpx;
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