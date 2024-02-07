<template>
	<view class="content">
		<u-datetime-picker
		        :show="start"
		        v-model="st"
		        mode="date"
				:minDate="Number(timestampNow)"
				closeOnClickOverlay 
				@close="start = false"
				@cancel="start = false"
				@confirm="setStartTime"
		></u-datetime-picker>
		<u-datetime-picker
		        :show="end"
		        v-model="et"
		        mode="date"
				:minDate="Number(startTime.timestamp) + 1000*60*60*24"
				closeOnClickOverlay 
				@close="end = false"
				@cancel="end = false"
				@confirm="setEndTime"
		></u-datetime-picker>
		<view class="bg">
			<u-swiper
			        :list="bgImgList"
			        :loading="loading"
					height="500"
					interval="5000"
					effect3d="true"
			></u-swiper>
		</view>
		<view class="bg-shadow"></view>
		<view class="card-info">
			<view class="card-info-nav">
				<span class="title">{{campus[campusIdx]}}校区 </span> <span class="tl"> | </span>
				<span class="title"> 选择时间</span>
				<view class="choose fr">
					<span :style="{'background-color':campusIdx == 0?'#91da00':'#dedede'}" @click="changeCampus(0)">永川</span>
					<span :style="{'background-color':campusIdx == 1?'#91da00':'#dedede'}" @click="changeCampus(1)">巴南</span>
				</view>
			</view>
			<view class="card-info-time">
				<view class="start-time" @click="start = true">
					<view class="fl">
						<view class="specific-time fl">
							<span>{{startTime.weekday}}</span>
							<span>{{startTime.hours}}:{{startTime.minutes}}</span>
						</view>
						<view class="about-time">
							<span>{{startTime.month}}</span>月
							<span>{{startTime.day}}</span>日
						</view>
					</view>
				</view>
				<view class="day-time">
					<image src="../../static/index/line.png"></image>
					<span>{{day}}天</span>
				</view>
				<view class="end-time" @click="end = true">
					<view>
						<view class="specific-time fr">
							<span>{{endTime.weekday}}</span>
							<span>{{endTime.hours}}:{{endTime.minutes}}</span>
						</view>
						<view class="about-time">
							<span>{{endTime.month}}</span>月
							<span>{{endTime.day}}</span>日
						</view>
					</view>
				</view>
			</view>
			<view class="card-info-btn">
				<view class="button" @click="toSelect()">去选车</view>
			</view>
			<view class="card-info-link">
				<view class="fl" @click="">
					<image src="../../static/index/phone.png"></image>
					<span>联系管理</span>
				</view>
				<view class="fr" @click="">
					<image src="../../static/index/share1.png"></image>
					<span>分享好友</span>
				</view>
			</view>
		</view>
		<view class="coupon" @click="">
			<view class="fl" style="display: inline-block;width: 450rpx;">
				<h3><span>先领券</span>,再出发</h3> 	
				<view style="font-size: 26rpx;">会员每周免费领满100减50等优惠好券</view>
				<view style="display: inline-block;font-size: 30rpx;background-color: #232323;padding: 0 10rpx;border-radius: 10rpx;color: antiquewhite;margin-top: 6rpx;">点击前往 >></view>
			</view>
			<view class="fr" style="display: inline-block;">
				<image src='../../static/index/gift.png' style="width: 110rpx;height: 110rpx;margin: 20rpx 10rpx;"></image>
			</view>
		</view>
		<view class="car-list">
			<view class="car-guide" @click="toLogin()">
				<view class="drag">
					<span>新人选车指南</span>
					<span>在线选车 高效服务 点击 >></span>
				</view>
			</view>
			<view class="car-item" v-for="(item, key) in modelList" @click="toCar(item)">
				<image :src='staticURL + item.img'></image>
				<view style="padding: 2rpx 10rpx 18rpx;">
					<span class="model">{{item.model}}</span>
					<view class="type">
						<span>{{item.type}}</span>/<span>{{item.energy}}</span>/<span>{{item.seatNumber}}座</span>
					</view>
					<view class="bottom">
						<view>
							<span class="rCount">推荐</span> 
						</view>
						<view> 
							<span class="price">日租￥{{item.minDayPrice}}起</span>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="on-reach-bottom">{{reachTip}}</view>
		<loginPopUp :login="login" @setLogin="setLogin"></loginPopUp>
	</view>
</template>

<script>
	import loginPopUp from '../../components/loginPopUp.vue'
	export default {
		data() {
			return {
				rCount1: 1,
				rCount2: 1,
				area:'',
				timestampNow:'',
				st:'',
				start:false,
				et:'',
				end:false, 
				bgImgList: ['../../static/bg/bg1.png','../../static/bg/bg4.png', '../../static/bg/bg2.png', '../../static/bg/bg1.png'],
				loading: false,
				campus: ['永川', '巴南'],
				campusIdx : 0,
				startTime: {},
				endTime: {},
				day: 2,
				weekdayList:['周日','周一','周二','周三','周四','周五','周六'],
				modelList:[],
				recommentList1:[],
				recommentList2:[],
				reachTip:'上拉加载更多',
				login:false, 
			}
		},
		components:{
			"loginPopUp":loginPopUp
		},
		onShow() {
			this.getData();
		},
		methods: {
			gePreValue(e){
				this.setLogin(e.login);
			},
			toCar(item) {
				uni.navigateTo({
					url: '../car/car?model=' + JSON.stringify(item)
				})
			},
			getData(){
				this.bgImg = this.baseURL + '/image/background/bg2.png'
				let day = 0;
				let date = new Date();
				if(date.getHours() >= 21) day++;
				this.startTime = this.getDate(day);
				this.endTime = this.getDate(this.day + day); 
				this.day = Math.floor(((this.endTime.timestamp - this.startTime.timestamp) + 100)/1000/60/60/24);
				this.timestampNow = this.startTime.timestamp;
				this.user = uni.getStorageSync("user");
				this.rCount1 = 0;
				this.rCount2 = 0; 
				this.recommentList1 = [];
				this.recommentList2 = [];
				this.modelList = [];
				console.log("this.user", this.user)
				if(!this.isValue(this.user)){
					setTimeout(() => {
						this.login = true; 
					}, 3000);
				}else{
					this.login = false;
				}
				//this.modelPage('巴南');
				this.modelPage(this.campus[this.campusIdx]);
				
			},
			setLogin(login){
				this.login = login;
			},
			modelPage(area){
				var that = this;
				let rCount;
				//console.log(this.rCount2);
				if(area !== ''){
					this.area = area;
				}
				console.log(this.area)
				if(this.area == '永川'){
					rCount = this.rCount1;
				}else if(this.area == '巴南'){
					rCount = this.rCount2;
				}else{
					return;
				}
				
				uni.request({
					url: that.baseURL + "/models/page/" + ++rCount + "/" + that.area,
					method:'GET',//请求方式，必须为大写
					success: (res) => {
						console.log('/models/getModelPage/------',res);
						if(res.data.code == 200){
							if(res.data.data.records.length == 0){
								that.reachTip = '暂无新数据';
							}else{
								that.modelList = []
								if(that.area == '永川'){
									that.rCount1 = rCount;
									that.recommentList1 = that.recommentList1.concat(res.data.data.records);
									that.modelList = that.recommentList1;
								}else if(that.area == '巴南'){
									that.rCount2 = rCount;
									that.recommentList2 = that.recommentList2.concat(res.data.data.records);
									that.modelList = that.recommentList2;
								}
								
								console.log(that.modelList);
							}
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
						console.log(err)
					}
				})
			},
			setStartTime(e){
				this.startTime = this.getDate(0, e.value)
				if(Math.floor((this.endTime.timestamp - this.startTime.timestamp)/1000/60/60/24) <= 0){
					this.endTime = this.getDate(1, e.value)
				}
				let time = (this.endTime.timestamp - this.startTime.timestamp)/1000/60/60/24
				this.day = Math.ceil(time);
				console.log(time);
				this.start = false
			},
			setEndTime(e){
				console.log(e.value);
				this.endTime = this.getDate(0, e.value)
				let time = (this.endTime.timestamp - this.startTime.timestamp)/1000/60/60/24
				this.day = Math.ceil(time);
				console.log(time);
				this.end = false
			},
			changeCampus(idx){
				this.campusIdx = idx;
				this.area = this.campus[idx];
				if(this.area == '永川')
					this.modelList = this.recommentList1;
				else
					this.modelList = this.recommentList2;
				if(this.modelList.length == 0)
					this.modelPage(this.area)
			},
			getDate(dayNum, dateTime = null) {
				//获取当天8点的时间
				let mytime = new Date(new Date(new Date().toLocaleDateString()).getTime()).getTime()
				console.log(mytime)
				// 如果为null,则格式化当前时间为时间戳 
				/* if (!dateTime) 
					dateTime = +new Date(); */
				if (!dateTime)
					dateTime = new Date(new Date(new Date().toLocaleDateString()).getTime()).getTime();
				console.log(dateTime)
				// 如果dateTime长度为10或者13，则为秒和毫秒的时间戳，如果超过13位，则为其他的时间格式
				if (dateTime.toString().length == 10) dateTime *= 1000;
				const timestamp = +new Date(Number(dateTime));
					
				const one_day = 86400000; // 24 * 60 * 60 * 1000;
				const addVal = dayNum * one_day + timestamp;
				
				//x天后的日期
				const date = new Date(addVal);
				//timestamp = +new Date(Number(date));
				const year = date.getFullYear();
				//格式化日期
				const filters = n => {
					return n < 10 ? (n = '0' + n) : n;
				};
				const month = filters(date.getMonth() + 1);
				const day = filters(date.getDate());
				const weekday = date.getDay();
				
				const hours = filters(date.getHours() + 9);//当前时间的9小时后
				const minutes = filters(date.getMinutes());
				const seconds = filters(date.getSeconds());
				var time = {
					date:date,
					year:year,
					month:month,
					day:day,
					weekday:this.weekdayList[weekday],
					hours:hours,
					minutes:minutes,
					timestamp:date,
					formatDateTime: `${date.getFullYear()}-${month}-${day} ${hours}:${minutes}:${seconds}`  
				}
				//const lastTime = `${date.getFullYear()}/${month}/${day} ${hours}:${minutes}:${seconds}`;
				return time;
			},
			toLogin(){
				uni.showToast({
					title: '即将跳转Demo页面',
					icon:'none',
					duration: 2000
				})
				setTimeout(() => {
					uni.navigateTo({
					     //url: '../login/login'
					     url: '../demo/demo'
					     //url: '../navigation/navigation'
					});
				}, 2000)
				
			},
			toSelect(){
				uni.setStorageSync("startTime",this.startTime)
				uni.setStorageSync("endTime",this.endTime)
				uni.setStorageSync("day",this.day)
				uni.setStorageSync("area",this.area)
				uni.navigateTo({
				     url: '../select/select?startTime=' + JSON.stringify(this.startTime) + '&endTime=' + JSON.stringify(this.endTime) 
											+ '&day=' + this.day + '&area=' + this.area
				});
			}
		},
		//监听上拉加载刷新
		onReachBottom() {	
			this.reachTip = '正在加载...请稍后'
			setTimeout(() =>{
				this.reachTip = '上拉加载更多'
				this.modelPage(this.area);	
				uni.stopPullDownRefresh();//关闭刷新状态
			}, 600)
		},
		onPullDownRefresh () {
		    //调用获取数据方法
		    this.getData();
		    setTimeout(() => {
		        //结束下拉刷新
		      uni.stopPullDownRefresh ();
		    }, 1000);
		},
	}
</script>

<style lang="scss">
	.on-reach-bottom{
		margin-top: 50rpx;
		padding: 10rpx;
		width: 100%;
		font-size: 24rpx;
		background-color: #e4ecff;
		color: #b2b2b2;
		text-align: center;
	}
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		background-color: #f1f1f1; 
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 32rpx;
		color: #030303;
	}
	
	.bg{
		width: 750rpx;
		height: 540rpx;
	}
	
	.bg-shadow{
		width: 100%;
		z-index: 1;
		
		margin-top: 20rpx;
	}
	.card-info{
		position: relative;
		width: 620rpx;
		height: 470rpx;
		margin-top: -150rpx;
		margin-bottom: 30rpx;
		padding: 30rpx;
		background-color: #ffffff;
		border-radius: 40rpx;
		z-index: 2;
	}
	.card-info .card-info-nav{
		height: 60rpx;
		margin-bottom: 40rpx;
	}
	.card-info .card-info-nav .title{
		vertical-align: top;
	}
	.card-info .card-info-nav .choose{
		vertical-align: top;
		font-size: 24rpx;
		color: #FFFFFF;
		border-radius: 30rpx;
	}
	.card-info .card-info-nav .choose span{
		background-color: #dedede;
		padding: 10rpx 20rpx;
	}
	.card-info .card-info-nav .choose span:first-child{
		border-radius: 30rpx 0 0 30rpx;
	}
	.card-info .card-info-nav .choose span:last-child{
		border-radius: 0 30rpx 30rpx 0;
	}

	.card-info .card-info-time{
		margin-bottom: 40rpx;
	}
	.card-info .card-info-time .start-time{
		position: absolute;
	}
	.specific-time{
		font-size: 26rpx;
		margin-top: 6rpx;
		margin-bottom: 10rpx;
		color: #787878;
	}
	.specific-time span:first-child{
		margin-right: 6rpx;
	}
	.day-time span{
		font-size: 26rpx;
		color: #787878;
	}
	.about-time{
		font-weight: 530;
	}
	.about-time span{
		font-weight: 550;
	}
	.card-info .card-info-time .day-time{
		position: absolute;
		width: 200rpx;
		height: 26rpx;
		left: 250rpx;
		text-align: center;
	}
	.card-info .card-info-time .day-time image{
		width: 100%;
		height: 100%; 
		object-fit: cover;
	}
	.card-info .card-info-time .end-time{
		position: absolute;
		right: 30rpx;
	}
	.card-info .card-info-btn .button {
		height: 100rpx;
		line-height: 100rpx;
		margin-top: 190rpx;
		margin-left: 62rpx;
		margin-right: 62rpx;
		margin-bottom: 20rpx;
		border-radius: 50rpx;
		font-size: 36rpx;
		font-weight: 300;
		background-color: #191de9;
		color: #FFFFFF;
		text-align: center;
		box-shadow: 0 20rpx 10rpx 0rpx #dfe6fa;
	}
	.card-info .card-info-link{
		margin: 60rpx 40rpx;
		height: 40rpx;
		line-height: 40rpx;
	}
	.card-info .card-info-link span{
		vertical-align:middle;
		font-size: 24rpx;
		height: 40rpx;
		line-height: 40rpx;
	}
	.card-info .card-info-link image{
		vertical-align:middle;
		margin-right: 6rpx;
		width: 40rpx;
		height: 40rpx; 
		object-fit: cover;
	}
	.coupon{
		position: relative;
		width: 620rpx;
		height: 150rpx;
		margin-bottom: 30rpx;
		padding: 20rpx 30rpx;
		background-color: #ffffff;
		border-radius: 40rpx;
		z-index: 2;
		background-image: linear-gradient(to right, #262639 , #565658);
		color: #ffdf7d;
	}
	.car-list{
		width: 680rpx;
		column-count: 2;
		column-gap: 20rpx;
	}
	.car-list .car-item,
	.car-list .car-guide{
		width: 330rpx;
		border-radius: 26rpx;
		z-index:1;
		overflow: hidden;
		break-inside: avoid;
		/*避免在元素内部插入分页符*/
		box-sizing: border-box;
		margin-bottom: 30rpx;
	}
	.car-list .car-item{
		background-color: #FFFFFF;
		border-radius: 0 80rpx 0 40rpx;
	}
	.car-list .car-item image{
		width: 100%; 
		max-height: 180rpx;
		object-fit: cover;
	}
	.car-list .car-item .bottom>view:first-child{
		width: 80rpx;
		display: inline-block;
		text-align: left;
	}
	.car-list .car-item .bottom>view:last-child{
		width: 220rpx;
		display: inline-block;
		text-align: right;
	}
	.car-list .car-item .rCount{
		font-size: 22rpx;
		color: #ffffff;
		background-color: #91da00;
		padding: 4rpx 6rpx;
		margin: 10rpx 0 10rpx 10rpx;
		border-radius: 6rpx;
	}
	.car-list .car-item .type{
		font-size: 24rpx;
		padding: 4rpx 0;
		margin: 2rpx 10rpx 10rpx;
		border-radius: 6rpx;
		width: 100%;
	}
	.car-list .car-item .model{
		font-size: 34rpx;
		font-weight: 600;
		margin: 0rpx 10rpx;
	}
	.car-list .car-item .price{
		font-size: 28rpx;
		font-weight: 650;
		margin: 10rpx 0 10rpx -20rpx ;
		color: #ff6769;
		padding: 4rpx;
		border-radius: 4rpx;
	}
	.car-list .car-guide{
		height: 240rpx;
		position: relative;
		background: url('../../static/index/newbg.png') no-repeat;
		background-size:cover;
		box-sizing:border-box;
	}
	.car-list .car-guide:after{
	    content: "";
	    width:320rpx;
	    height:240rpx;
		border-radius: 20rpx;
	    position: absolute;
	    left:-2rpx;
	    top:0;
	    background: inherit;
	    filter: blur(5rpx);
	    z-index: 2;
	}
	.drag{
	    position: absolute;
	    z-index:11;
		padding:30rpx 40rpx ;
	}
	.car-list .car-guide .drag span{
		display: block;
		font-style: 12rpx;
		color: #ffffff;
		
	}
	.car-list .car-guide .drag span:first-child{
		font-size: 41rpx;
		font-weight: 700;
	}
</style>
