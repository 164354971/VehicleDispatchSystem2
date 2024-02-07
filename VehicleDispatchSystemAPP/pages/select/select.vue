<template>
	<view class="content" style="min-height: 100vh;">
		<view class="card-info">
			<view class="card-info-nav">
				<span class="title">{{area}}校区</span>
			</view>
		
			<view class="card-info-time">
				<view class="start-time" @click="">
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
				<view class="end-time" @click="">
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
			<view style="margin-top: 170rpx;"></view>
			<view class="select-parkade" @click="mapIdx = 1">
				<image :src="staticURL + parkade.img"></image>
				<view class="text">
					<p style="font-weight: 700;">{{parkade.name}}</p>
					<span>距离该停车场：{{parkade.distance}}km</span>
				</view>
				
			</view>
			<view class="select-parkade-click">
				<image :src="arrows1[parkadeIdx]" @click="optionParkadeList"></image>
			</view>
		</view>
		<view class="card-list" >
			<view class="options">
				<view class="option" @click="optionSort">
					<span>排序</span>
					<image :src="arrows[sortIdx]"></image>
				</view>
				<view class="option" @click="optionSelect">
					<span>筛选</span>
					<image :src="arrows[selectIdx]"></image>
				</view>
			</view>
			<view class="card-item" v-for="(item, key) in vehicleCarList" @click="toDetails(item)">
				<image class="show" :src='staticURL + item.modelImg'></image>
				<view class="text">
					<p class="title">{{item.name}}</p>
					<span class="type">{{item.type}}/{{item.driverType}}/{{item.displacement}}/{{item.energy}}</span>
					<span class="type">{{item.color}}/{{item.year}}/<span>
							{{item.carNumber[0]}}{{item.carNumber[1]}}*****</span>
						<span v-if="item.carNumber.length == 8">*</span></span>
				</view>
				<view class="bottom">
					<view class="bottom-label fl">
						<view class="label vip" v-if="item.noDeposit == true">
							<image src="../../static/select/vip.png"></image>
							<span>会员免押</span>
						</view>
						<view class="label car-luxury" v-if="item.luxury == true">
							<image src="../../static/select/luxury.png"></image>
							<span>豪华品牌</span>
						</view>
						<view class="label car-green" v-if="item.newEnergy == true">
							<image src="../../static/select/carNumber.png"></image>
							<span>新能源</span>
						</view>
					</view>
					<view class="bottom-label fr">
						<view class="label price fr">
							<image src="../../static/select/price.png"></image>
							<span>预计费用:</span>
							<span>￥{{item.dayPrice * day}}</span>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="on-reach-bottom">{{reachTip}}</view>
		
		<view class="option-parkade" v-show="parkadeIdx == 1" >
			<scroll-view
			      class="notice-scroll"
			      :scroll-y="true"
			      :scroll-with-animation="true"
			    >
				<view style="max-height: 500rpx; border-radius: 10rpx;background-color: #ffffff;">
					<view class="select-parkade" style="border-bottom: 1rpx solid #d5d5d5;" v-for="(item, idx) in parkadeList" @click="listParkade(item)">
						<image :src="staticURL + item.img"></image>
						<view class="text">
							<p style="font-weight: 700;color: #1d1d1d;">{{item.name}}</p>
							<span>距离该停车场：{{item.distance}}km</span>
						</view>
					</view>
				</view>
			      
			</scroll-view>
			
		</view>
		
		<view class="option-sort" v-show="sortIdx == 1" @tap.stop>
			<span :class="price == 1 ? 'check' : ''" @click="sortPrice(1)" >优先低租金</span>
			<span :class="price == 2 ? 'check' : ''" @click="sortPrice(2)" >优先高租金</span>
		</view>
		
		<view class="option-select" v-show="selectIdx == 1">
			<view>
				<view>
					<span>类型</span>
				</view>
				<span :class="typeAll == true ? 'check' : ''" @click="selectType(0)">全部</span>
				<span :class="typeBools[idx] == true ? 'check' : ''" v-for="(item, idx) in types" @click="selectType(idx + 1)">{{item}}</span>
			</view>
			<view>
				<view>
					<span>价格 (按日租)</span>
				</view>
				<span :class="priceAll == true ? 'check' : ''" @click="selectPrice(0)">全部</span>
				<span :class="priceBools[idx] == true ? 'check' : ''" v-for="(item, idx) in prices" @click="selectPrice(idx + 1)">{{item}}</span>
			</view>
			<view>
				<view>
					<span>标签</span>
				</view>
				<span :class="labelAll == true ? 'check' : ''" @click="selectLabel(0)">全部</span>
				<span :class="labelBools[idx] == true ? 'check' : ''" v-for="(item, idx) in labels" @click="selectLabel(idx + 1)">{{item}}</span>
			</view>
			<view></view>
		</view>
		<u-overlay :show="mapIdx == 1" @click="mapIdx = 0" opacity="0.3">
			<map style="width: 90%;height: 50vh;margin: 370rpx 5%;" :layer-style='5'
				:circles="[{ latitude, longitude, color: '#E51860', strokeWidth: 1, radius: 200, fillColor: '#E5186020' }]"
				:latitude="latitude" :longitude="longitude" :markers="marker" :scale="scale" @markertap="markertap"
				@callouttap='callouttap' @tap="tap">
			</map>
		</u-overlay>
		
		<u-loading-page :loading="loading" bg-color="#ffffff" style="z-index: 40;" loading-text="数据正在加载..." color="#55557f"  font-size="42" icon-size="50"></u-loading-page>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				reachTip: '上拉加载更多',
				area:'',
				timestampNow: '',
				st: '',
				start: false,
				et: '',
				end: false,
				price: 0,
				weekdayList: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
				arrows: ['../../static/select/down.png', '../../static/select/up.png'],
				arrows1: ['../../static/select/u.png', '../../static/select/d.png'],
				sortIdx: 0,
				selectIdx: 0,
				parkadeIdx: 0,
				mapIdx: 0,
				prices:['200及以下','200 - 500','500及以上'],
				priceBools:[false, false, false],
				labels:['会员免押','豪华品牌','新能源'],
				labelBools:[false, false, false],
				types:['轿车','SUV','MPV','其他'],
				typeBools:[false, false, false, false],
				findCarPage:{
					parkadeId: 0,
					page: 1,
					pageSize: 10,
					sort: 0,//0不排序， 1升序2降序
					typeList:['全部'],
					startPrice: -1,
					endPrice: -1,
					nln: true,
					noDeposit: false,
					luxury: false,
					newEnergy: false
				},
				vehicleCarList: [],
				typeAll: true,
				priceAll: true,
				labelAll: true,
				parkade:{},
				parkadeList:[],
				loading: true,
				
				//以下是地图属性
				longitude:0,
				latitude:0,
				scale: 15, //缩放级别
				compass: true,
				marker: [{
						id: 0,
						latitude: 0, //纬度
						longitude: 0, //经度
						iconPath: '../../static/navigation/position.png', //显示的图标        
						rotate: 0, // 旋转度数
						width: 20, //宽
						height: 30, //高
						//   title:'我在这里',//标注点名
						alpha: 0.5, //透明度
						callout: { //自定义标记点上方的气泡窗口 点击有效  
							content: '当前位置', //文本
							color: '#ffffff', //文字颜色
							fontSize: 14, //文本大小
							borderRadius: 15, //边框圆角
							borderWidth: '10',
							bgColor: '#e51860', //背景颜色
							display: 'ALWAYS', //常显
						},
					},
					{
						id: 1,
						latitude: 0, //纬度
						longitude: 0, //经度
						iconPath: '../../static/navigation/location.png', //显示的图标        
						rotate: 0, // 旋转度数
						width: 10, //宽
						height: 20, //高
						//   title:'我在这里',//标注点名
						alpha: 0.5, //透明度
						callout: { //自定义标记点上方的气泡窗口 点击有效  
							content: '重庆城市科技学院永川校区综合楼停车场', //文本
							color: '#ffffff', //文字颜色
							fontSize: 14, //文本大小
							borderRadius: 15, //边框圆角
							borderWidth: '10',
							bgColor: '#e51860', //背景颜色
							display: 'ALWAYS', //常显
						},
					},
				],
			}
		},
		onShow(){
			this.listParkade();  
		},
		onLoad(options) {
			var that = this;
			console.log(options)
			this.startTime = JSON.parse(options.startTime);
			this.endTime = JSON.parse(options.endTime);
			
			this.findCarPage.startTime = this.startTime.formatDateTime;
			this.findCarPage.endTime = this.endTime.formatDateTime;
			this.day = options.day;
			this.area = options.area;
			this.timestampNow = this.startTime.timestamp;
			
			/* setInterval(()=>{
				this.location(); 
			}, 1000) */
			this.location();
			setTimeout(()=>{
				this.listParkade();  
			}, 1000);
		},
		methods: {
			location() {
				let that = this;
				uni.getLocation({
					type: 'gcj02',
					geocode: true,
					success: function(res) {
						//console.log('当前位置的经度：' + res.longitude);
						//console.log('当前位置的纬度：' + res.latitude);
						that.longitude = res.longitude;
						that.latitude = res.latitude;
						that.marker[0].longitude = res.longitude
						that.marker[0].latitude = res.latitude
					}
			
				});
			},
			distance(lat, lng) {
				var that = this;
				console.log(this.latitude, this.longitude)
				//纬度差
				let Lat1Radom = (lat* Math.PI) / 180.0;
				let Lat2Radom = (this.latitude * Math.PI) / 180.0;
				let lati = Lat1Radom - Lat2Radom;
				//经度差
				let Long1Radom = (lng * Math.PI) / 180.0;
				let Long2Radom = (this.longitude * Math.PI) / 180.0;
				let long = Long1Radom - Long2Radom;
				//计算两点之间的实际距离（经过我的查询这个公式叫做:Haversine公式）
				let s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(lati / 2), 2) + Math.cos(Lat1Radom) * Math.cos(Lat2Radom) * Math.pow(Math.sin(long / 2), 2)));
				s = s * 6378.137;
				s = Math.round(s * 10000) / 10000;
				//保留最后两位小数 
				s = s.toString();
				s = s.substring(0, s.indexOf('.') + 2);
				//提醒一下，这里的获取的距离，也就是s的单位是Km
				return s;
			},
			setScale(){
				if(this.parkade.distance <= 0.1){
					this.scale = 17
				}
				if(this.parkade.distance <= 0.2){
					this.scale = 16
				}
				else if(this.parkade.distance <= 0.5){
					this.scale = 15
				}
				else if(this.parkade.distance <= 1.0){
					this.scale = 14
				}
				else if(this.parkade.distance <= 2.0){
					this.scale = 13
				}
				else if(this.parkade.distance <= 5.0){
					this.scale = 12
				}
				else if(this.parkade.distance <= 10.0){
					this.scale = 11
				}
				else if(this.parkade.distance <= 20.0){
					this.scale = 10
				}
				else if(this.parkade.distance <= 50.0){
					this.scale = 9
				}
				else if(this.parkade.distance <= 100.0){
					this.scale = 8
				}
				else if(this.parkade.distance <= 200.0){
					this.scale = 7
				}
				else if(this.parkade.distance <= 500.0){
					this.scale = 6
				}
				else if(this.parkade.distance <= 1000.0){
					this.scale = 5
				}
				else if(this.parkade.distance <= 2000.0){
					this.scale = 4
				}
				else {
					this.scale = 3
				}
			},
			listParkade(item = null){
				var that = this;
				console.log(this.area)
				uni.request({
					url: this.baseURL + '/parkades/list/' + this.area,
					withCredentials:true,
					xhrFields: {
						withCredentials: true
					},
					method:'GET',
					success(res) {
						if(res.data.code == 200){
							that.location();
							console.log("res.data", res.data)
							that.parkadeList = res.data.data;
							console.log("that.parkadeList", that.parkadeList);
							let len = that.parkadeList.length;
							if(len == 0){
								that.loading = false;
								uni.showToast({
									title: '当前页面数据缺失，即将回到首页，请联系管理员',
									icon:'none',
									duration: 2000
								});
								setTimeout(()=>{
									uni.switchTab({
										url:'/pages/index/index'
									})
								},2000);
								
							}else{
								for(let i = 0; i < len; i++){
									console.log(i, that.parkadeList[i])
									let latitude = parseFloat(that.parkadeList[i].latitude);
									let longitude = parseFloat(that.parkadeList[i].longitude);
									that.parkadeList[i].distance = that.distance(latitude, longitude);
									//that.parkadeList[i].distance = 0
								}
								that.parkadeList.sort((a, b) => {return a.distance - b.distance})
								if(item == null)
									that.parkade = that.parkadeList[0];
								else{
									that.parkade = item;
								}
								that.marker[1].longitude = that.parkade.longitude
								that.marker[1].latitude = that.parkade.latitude
								that.setScale();
								that.findCarPage.parkadeId = that.parkade.id;
								that.parkadeIdx = 0
								that.carPage();
							}
						}
						
					}
				})
			},
			toDetails(car){
				this.sortIdx = this.selectIdx = 0;
				uni.setStorageSync("car",car)
				console.log(car)
				uni.navigateTo({
				     url: '../details/details?startTime=' + JSON.stringify(this.startTime) + '&endTime=' + JSON.stringify(this.endTime) 
											+ '&day=' + this.day + '&area=' + this.area + '&car=' + JSON.stringify(car)
				});
				
			},
			sortPrice(idx){
				this.price = this.price == idx ? 0 : idx;
				this.findCarPage.sort = this.price;
				this.carPage()
			},
			selectType(idx){
				this.findCarPage.typeList = [];
				if(idx == 0){
					this.typeAll = true;
					this.findCarPage.typeList = ['全部'];
					let len = this.typeBools.length;
					for(let i = 0; i < len; i++){
						this.typeBools[i] = false;
					}
				}else{
					this.typeAll = false;
					this.typeBools[idx - 1] = this.typeBools[idx - 1] == true ? false : true;
					let len = this.typeBools.length;
					let sum = 0
					for(let i = 0; i < len; i++){
						if(this.typeBools[i] == true){
							this.findCarPage.typeList.push(this.types[i]);
							sum++;
						}
					}
					if(sum == 0){
						this.typeAll = true;
						this.findCarPage.typeList = ['全部'];
					}
				}
				this.carPage()
			},
			selectPrice(idx){
				if(idx == 0){
					this.priceAll = true;
					this.findCarPage.startPrice = -1;
					this.findCarPage.endPrice = -1;
					let len = this.priceBools.length;
					for(let i = 0; i < len; i++){
						this.priceBools[i] = false;
					}
				}else{
					this.priceAll = false;
					this.priceBools[idx - 1] = this.priceBools[idx - 1] == true ? false : true;
					let flag = false
					if(this.priceBools[0] == true){
						this.findCarPage.startPrice = 0;
						this.findCarPage.endPrice = 200;
						flag = true;
						if(this.priceBools[1] == true){
							this.findCarPage.endPrice = 500;
							if(this.priceBools[2] == true){
								this.findCarPage.endPrice = -1;
							}
						}
					}
					else if(this.priceBools[1] == true){
						this.findCarPage.startPrice = 200;
						this.findCarPage.endPrice = 500;
						flag = true;
						if(this.priceBools[2] == true){
							this.findCarPage.endPrice = -1;
						}
					}
					else if(this.priceBools[2] == true){
						this.findCarPage.startPrice = 500;
						this.findCarPage.endPrice = -1;
						flag = true;
					}
					if(!flag){
						this.priceAll = true;
						this.findCarPage.startPrice = -1;
						this.findCarPage.endPrice = -1;
					}
				}
				this.carPage()
			},
			selectLabel(idx){
				if(idx == 0){
					this.labelAll = true;
					for(let i = 0; i < 3; i++){
						this.labelBools[i] = false
					}
					this.findCarPage.nln = true;
					this.findCarPage.noDeposit = false;
					this.findCarPage.luxury = false;
					this.findCarPage.newEnergy = false;
				}
				else{
					this.labelAll = false;
					this.labelBools[idx - 1] = this.labelBools[idx - 1] == true ? false : true;
					this.findCarPage.nln = false;
					this.findCarPage.noDeposit = this.labelBools[0];
					this.findCarPage.luxury = this.labelBools[1];
					this.findCarPage.newEnergy = this.labelBools[2];
					if(!this.labelBools[0] && !this.labelBools[1] && !this.labelBools[2]){
						this.labelAll = true;
						this.findCarPage.nln = true;
						this.findCarPage.noDeposit = false;
						this.findCarPage.luxury = false;
						this.findCarPage.newEnergy = false;
					}
				}
				this.carPage()
			},
			carPage() {
				var that = this;
				uni.request({
					url: this.baseURL + '/cars/page',
					data: this.findCarPage,
					withCredentials:true,
					xhrFields: {
						withCredentials: true
					},
					method:'POST',
					success(res) {
						if(res.data.code == 200){
							that.vehicleCarList = res.data.data.records;
							that.loading = false;
						}
					}
				})
			},
			optionSort() {
				this.sortIdx = this.sortIdx == 1 ? 0 : 1;
				this.selectIdx = 0;
			},
			optionSelect() {
				this.selectIdx = this.selectIdx == 1 ? 0 : 1;
				this.sortIdx = 0;
			},
			optionParkadeList(){
				this.parkadeIdx = this.parkadeIdx == 1 ? 0 : 1;
			},
			setStartTime(e) {
				console.log(e.value);
				this.startTime = this.getDate(0, e.value)
				if (Math.floor((this.endTime.timestamp - this.startTime.timestamp) / 1000 / 60 / 60 / 24) == 0) {
					this.endTime = this.getDate(1, e.value)
				}
				this.day = Math.floor((this.endTime.timestamp - this.startTime.timestamp) / 1000 / 60 / 60 / 24);
				this.start = false
			},
			setEndTime(e) {
				console.log(e.value);
				this.endTime = this.getDate(0, e.value)
				this.day = Math.floor((this.endTime.timestamp - this.startTime.timestamp) / 1000 / 60 / 60 / 24);
				this.end = false
			},
			changeCampus(idx) {
				this.campusIdx = idx;
			},
			getDate(dayNum, dateTime = null) {
				// 如果为null,则格式化当前时间为时间戳
				if (!dateTime) dateTime = +new Date();
				//console.log(dateTime)
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

				//const hours = filters(date.getHours() + 1);//当前时间的下一个小时
				//const minutes = filters(date.getMinutes());
				//const seconds = filters(date.getSeconds());
				var time = {
					date: date,
					year: year,
					month: month,
					day: day,
					weekday: this.weekdayList[weekday],
					hours: '08',
					minutes: '00',
					timestamp: date
				}
				//const lastTime = `${date.getFullYear()}/${month}/${day} ${hours}:${minutes}:${seconds}`;
				return time;
			},
			// 点击标记点时触发
			markertap(e) {
				console.log('点击标记点时触发', e)
			},
			// 点击标记点对应的气泡时触发，
			callouttap(e) {
				console.log('点击标记点对应的气泡时触发', e)
			},
			// 点击地图时触发
			tap(e) {
				console.log('点击地图时触发', e)
			}
		},
		//监听上拉加载刷新
		onReachBottom() {
			this.reachTip = '正在加载...请稍后'
			setTimeout(() => {
				this.reachTip = '上拉加载更多'
				this.carPage();
				uni.stopPullDownRefresh(); //关闭刷新状态
			}, 1000)
		}
	}
</script>

<style>
	.select-parkade{
		padding: 20rpx 30rpx;
		background-color: #f9f9f9;
	}
	.select-parkade>image{
		width: 200rpx;
		height: 150rpx;
		margin: 0 40rpx 0 0;
		display: inline-block;
		object-fit: cover;
		vertical-align: top;
	}
	.select-parkade>view{
		display: inline-block;
		vertical-align: top;
		font-size: 26rpx;
	}
	.select-parkade-click{
		height: 40rpx;
		text-align: center;
	}
	.select-parkade-click>image{
		display: inline-block;
		margin-top: 10rpx;
		width: 40rpx;
		height: 40rpx;
	}
	.option-parkade{
		position: absolute;
		margin: 0 30rpx;
		width: 660rpx;
		top: 560rpx;
		z-index: 20;
	}
	.on-reach-bottom {
		margin-top: 50rpx;
		padding: 10rpx;
		width: 100%;
		font-size: 24rpx;
		background-color: #e4ecff;
		color: #b2b2b2;
		text-align: center;
	}

	.content {
		
		background-color: #e0e6f6;
		padding: 40rpx 15rpx;
	}

	.card-info {
		position: relative;
		width: 660rpx;
		margin-bottom: 20rpx;
		padding: 30rpx;
		background-color: #ffffff;
		border-radius: 40rpx;
		z-index: 2;
	}

	.card-info .card-info-nav {
		height: 60rpx;
		margin-bottom: 40rpx;
		text-align: center;
	}

	.card-info .card-info-nav .title {
		vertical-align: top;
	}

	.card-info .card-info-nav .choose {
		vertical-align: top;
		font-size: 24rpx;
		color: #FFFFFF;
		border-radius: 30rpx;
	}

	.card-info .card-info-nav .choose span {
		background-color: #dedede;
		padding: 10rpx 20rpx;
	}

	.card-info .card-info-nav .choose span:first-child {
		border-radius: 30rpx 0 0 30rpx;
	}

	.card-info .card-info-nav .choose span:last-child {
		border-radius: 0 30rpx 30rpx 0;
	}

	.card-info .card-info-time {
		margin-bottom: 40rpx;
	}

	.card-info .card-info-time .start-time {
		position: absolute;
	}

	.specific-time {
		font-size: 26rpx;
		margin-top: 6rpx;
		margin-bottom: 10rpx;
		color: #787878;
	}

	.specific-time span:first-child {
		margin-right: 6rpx;
	}

	.day-time span {
		font-size: 26rpx;
		color: #787878;
	}

	.about-time {
		font-weight: 530;
	}

	.about-time span {
		font-weight: 550;
	}

	.card-info .card-info-time .day-time {
		position: absolute;
		width: 200rpx;
		height: 26rpx;
		left: 260rpx;
		text-align: center;
	}

	.card-info .card-info-time .day-time image {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}

	.card-info .card-info-time .end-time {
		position: absolute;
		right: 30rpx;
	}

	.card-list {
		background-color: #fff;
		border-radius: 30rpx;
		padding: 20rpx 0rpx;
		width: 720rpx;
	}

	.card-list .options {
		width: 100%;
		height: 70rpx;
		border-bottom: #e8e8e8 2rpx solid;
	}

	.card-list .options .option {
		width: 345rpx;
		display: inline-block;
		text-align: center;
	}

	.card-list .options .option span {
		font-size: 30rpx;
		margin-right: 16rpx;
	}

	.card-list .options .option image {
		width: 30rpx;
		height: 30rpx;
		display: inline-block;
	}

	.card-list .card-item {
		width: 686rpx;
		padding: 16rpx 0;
		margin: 20rpx;
		background-image: linear-gradient(#eaeef6, #fafafa);
		border-radius: 10rpx 60rpx 0 30rpx;
	}

	.card-list .card-item .show {
		width: 240rpx;
		height: 135rpx;
		display: inline-block;
		object-fit: cover;
		vertical-align: middle;
		padding: 0 20rpx;
	}

	.text {
		width: 340rpx;
		display: inline-block;
		vertical-align: middle;
	}

	.text .title {
		white-space: nowrap;
		width: 340rpx;
		text-overflow: ellipsis;
		overflow: hidden;
		font-weight: 600;
		font-size: 32rpx;
		margin-bottom: 10rpx;
	}

	.text .type {
		white-space: nowrap;
		width: 340rpx;
		text-overflow: ellipsis;
		overflow: hidden;
		font-weight: 500;
		font-size: 22rpx;
		display: inline-block;
	}

	.card-list .card-item .bottom {
		width: 686rpx;
		overflow: hidden;
	}

	.card-list .card-item .bottom .bottom-label {
		font-size: 16rpx;
	}

	.card-list .card-item .bottom .bottom-label .label {
		border-radius: 10rpx;
		display: inline-block;

		margin: 16rpx 6rpx;
		padding: 3rpx;
	}

	.card-list .card-item .bottom .bottom-label .label image {
		width: 34rpx;
		height: 34rpx;
		display: inline-block;
		margin-right: 2rpx;

		vertical-align: middle;
	}

	.card-list .card-item .bottom .bottom-label .label span {
		vertical-align: middle;
	}

	.vip {
		background-color: #60a2f9;
		color: #ffffff;
		font-size: 16rpx;
		font-weight: 400;
	}

	.car-blue {
		background-color: #6bb8ff;
		color: #ffffff;
		font-size: 16rpx;
		font-weight: 400;
	}

	.car-green {
		background-color: #7ce23d;
		color: #ffffff;
		font-size: 16rpx;
		font-weight: 400;
	}

	.car-luxury {
		background-color: #ff4727;
		color: #ffffd4;
		font-size: 16rpx;
		font-weight: 400;
	}

	.price {
		background-color: #fff6dd;
		color: #ff6769;
		font-size: 24rpx;
		font-weight: 700;
	}

	.option-sort,
	.option-select {
		position: absolute;
		top: 664rpx;
		width: 720rpx;
		padding: 10rpx 0;
		z-index: 10;
		background-color: #fff;
		border: #ececec 1px solid;
		border-radius: 0 0 10rpx 10rpx;
		opacity: 0.96;
	}

	.option-sort span,
	.option-select span {
		display: inline-block;
		height: 60rpx;
		line-height: 60rpx;
		text-align: center;
		
	}
	
	.option-sort span{
		width: 360rpx;
		font-size: 26rpx;
	}
	
	.option-select>view>span{
		width: 240rpx;
		font-size: 26rpx;
	}
	
	.option-select>view>view{
		text-align: center;
		font-size: 22rpx;
		color: #9dc4ff;
	}

	.check {
		background-color: #fafafa;
		color: #60a2f9;
	}
</style>