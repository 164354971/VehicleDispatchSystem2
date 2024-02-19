<template>
	<view>
		<view class="title">
			<span>车辆品牌</span>
		</view>
		<u-search :showAction="false" placeholder="请输入品牌名称或品牌首字母" v-model="select" bgColor="#fff" borderColor="#eeeeee"></u-search>
		<view v-for="(item, idx) in brandSum" :key="idx">
			<view class="initial"> 
				<span>{{item.initial}}</span>
			</view>
			<u-cell-group>
				<view v-for="(brand, i) in item.brandList" :key="brand.id" @click="toModel(brand)" v-show="select == '' || brand.brand.indexOf(select) >= 0 || select.toUpperCase() == item.initial">
					<u-cell arrow-direction="down" :title="brand.brand">
						<u-icon slot="icon" size="80" :name="staticURL + brand.img"></u-icon> 
						<!-- <u-badge count="99" :absolute="false" slot="right-icon"></u-badge> -->
					</u-cell>
				</view>
			</u-cell-group>
		</view>
			
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				select:'',
				brandSum:[],
			}
		},
		onLoad(){
			this.listBrand()
		},
		onShow(){
			this.listBrand()
		},
		methods: {
			listBrand(name = ''){
				let that = this;
				uni.showNavigationBarLoading();
				uni.request({
						url: this.baseURL + "/brands/list?str=" + name,
						method:'GET',//请求方式，必须为大写
						success: (res) => {
							console.log('/brands/list', res);
							if(res.data.code == 200){ 
								console.log(res.data.data)
								that.brandSum = res.data.data;
							}
							else{
								uni.showToast({
									title: res.data.msg,
									icon:'none',
									duration: 2000
								});
							}
							uni.hideNavigationBarLoading();
						},
						fail: (err) => {
							uni.showToast({
								title: res.data.msg,
								icon:'none',
								duration: 2000
							});
							uni.hideNavigationBarLoading();
						}
					})
				
			},
			toModel(item){
				uni.navigateTo({
				     url: '../model/model?brand=' + JSON.stringify(item)
				});
			}
		}
	}
</script>

<style>
	.title{
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
	.initial{
		width: 690rpx;
		padding: 2rpx 30rpx;
		background-color: #f1f4fd;
		color: #ababab;
		font-size: 28rpx;
		font-weight: 700;
	}
</style>
