<style lang="scss">
    /* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
    @import "uview-ui/index.scss";
</style>
<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/*每个页面公共css */
	.fl{
		float: left;
	}
	.fr{
		float: right;
	}
	.tl{
		color: #b8b8b8;
		margin: 0 8rpx;
	}
	.clear{
		clear: both;
	}
	.strike{
		text-decoration:line-through;
		color: #6a6a6a;
		font-size: 26rpx;
		font-weight: 500;
	}
	
</style>
