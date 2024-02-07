<template>
	<view class="contant">
		<map style="width: 100%;height: 1000rpx;" :layer-style='5'
			:circles="[{ latitude, longitude, color: '#E51860', strokeWidth: 1, radius: 200, fillColor: '#E5186020' }]"
			:latitude="latitude" :longitude="longitude" :markers="marker" :scale="scale" @markertap="markertap"
			@callouttap='callouttap' @tap="tap">
		</map>
		<view class="button" @click="location()">获取当前位置</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				latitude: 0, //纬度
				longitude: 0, //经度
				scale: 16, //缩放级别
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
						latitude: 29.39586, //纬度
						longitude: 105.89046, //经度
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
		onLoad() {
			this.location()
		},
		methods: {
			location() {
				let that = this;
				uni.getLocation({
					type: 'gcj02',
					geocode: true,
					success: function(res) {
						console.log('当前位置的经度：' + res.longitude);
						console.log('当前位置的纬度：' + res.latitude);
						console.log(res.address);
						that.marker[0].longitude = res.longitude
						that.longitude = res.longitude
						that.marker[0].latitude = res.latitude
						that.latitude = res.latitude
					}
		
				});
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
		}
	}
</script>

<style>

</style>
