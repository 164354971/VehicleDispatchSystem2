<template>
	<view style="display: inline-block;">
		<span>{{salary}}</span>
	</view>
</template>

<script>
	export default {
		name:"price",
		props: ['price'],
		data() {
			return {
				salary:''
			};
		},
		mounted(){
			//console.log(this.price)
			let price = this.price.toString().split('.');
			//1234567.89
			let i = price[0].length - 1;
			//console.log(i)
			let price0 = "";
			let price1 = "";
			for(i; i >= 0; i -= 3){
				let start = i - 2 >= 0 ? i - 2 : 0;
				price0 = price[0].substr(start, i + 1) + price0;
				if(i > 2){
					price0 = ',' + price0;
				}
				//console.log(price0)
			}
			if(price.length == 2){
				if(price[1].length == 1)
					price1 = '.' + price[1] + '0';
				else
					price1 = '.' + price[1]
			}else{
				price1 = '.00';
			}
			this.salary = price0 + price1;
			//console.log(this.salary)
		}
	}
</script>

<style>

</style>