<template>
	<view>
		<div class="content-all2">
			<IndexSwiper></IndexSwiper>

			<div class="title">每日填报</div>
			<div class="content">
				<div @click="DailyReport" class="box hover" id="dailyReport">
					<div class="box-content">
						<div class="box-title">日常状态</div>
						<div class="box-en">Daily State</div>
						<div class="custom-divider" text-align="center" />
						<div class="box-data">
							<span>每日晚八点后提交1次</span>
						</div>
						<div class="image-container">
							<image class="box-img" src="../../static/images/index/set/in2.png"></image>
						</div>
					</div>
				</div>
				<!-- <div @click="EMA" class="box hover" id="ema">
					<div class="box-content">
						<div class="box-title">情绪</div>
						<div class="box-en">Bracelet Data</div>
						<div class="custom-divider" text-align="center" />
						<div class="box-data">
							<span>无需填写<br>点击进入即可查看</span>
						</div>
						<div class="image-container">
							<image class="box-img" src="../../static/images/index/set/in2.png"></image>
						</div>
					</div>
				</div>
			</div> -->
				<div @click="BraceletData" class="box hover" id="braceletData">
					<div class="box-content">
						<div class="box-title">手环数据</div>
						<div class="box-en">Bracelet Data</div>
						<div class="custom-divider" text-align="center" />
						<div class="box-data">
							<span>无需填写<br>点击进入即可查看</span>
						</div>
						<div class="image-container">
							<image class="box-img" src="../../static/images/index/set/in2.png"></image>
						</div>
					</div>
				</div>
			</div>
			<!-- <u-divider class="custom-divider" text-align="center" /> -->

			<div class="title">精神状态评估
				<span style="font-size: 0.7em;">（与医生线下访谈时填写）</span>
			</div>

			<div class="content-1">
				<div @click="PHQ" class="box hover" id="phq">
					<div class="box-content">
						<div class="box-title">SDS-抑郁自测量表</div>
						<div class="box-en">Self-rating Depression Scale</div>
						<div class="custom-divider" text-align="center" />
						<div class="box-data">
							<span>20个问题<br>每两周填写一次</span>
						</div>
						<div class="image-container">
							<image class="box-img" src="../../static/images/index/set/in2.png"></image>
						</div>
					</div>
				</div>
				<div @click="DepressionVideo" class="box hover" id="depressionvideo">

					<div class="box-content">
						<div class="box-title">迷你访谈</div>
						<div class="box-en">Mini-interviews Assessment</div>
						<div class="custom-divider" text-align="center" />
						<div class="box-data">
							<span>说说最近的事和心情吧<br></span>
						</div>
						<div class="image-container">
							<image class="box-img" src="../../static/images/index/set/in2.png"></image>
						</div>
					</div>
				</div>
			</div>
			<div class="content-2">
				<div @click="PSS" class="box hover" id="pss">
					<div class="box-content">
						<div class="box-title">PSS-压力自测量表</div>
						<div class="box-en">Perceived Stress Scale</div>
						<div class="custom-divider" text-align="center" />
						<div class="box-data">
							<span>14个问题<br>每两周填写一次</span>
						</div>
						<div class="image-container">
							<image class="box-img" src="../../static/images/index/set/in2.png"></image>
						</div>
					</div>
				</div>
				<div @click="SAS" class="box hover" id="sas">
					<div class="box-content">
						<div class="box-title">SAS-焦虑自测量表</div>
						<div class="box-en">Self-rating Anxiety Scale</div>
						<div class="custom-divider" text-align="center" />
						<div class="box-data">
							<span>20个问题<br>每两周填写一次</span>
						</div>
						<div class="image-container">
							<image class="box-img" src="../../static/images/index/set/in2.png"></image>
						</div>
					</div>
				</div>
			</div>
			<!-- <u-divider class="custom-divider" text-align="center" /> -->
			<div class="title-2">保持好心情，让生活处处充满色彩。</div>
			<!-- <div class="content">
				<div @click="" class="box hover" id="dailyReport">
					<div class="box-content">
						<div class="box-title">量表1</div>
						<div class="box-en"> Something</div>
						<div class="custom-divider" text-align="center" />
						<div class="box-data">
							<span>信息</span>
						</div>
					</div>
				</div>
				<div @click="" class="box hover" id="dailyReport">
					<div class="box-content">
						<div class="box-title">量表2</div>
						<div class="box-en">Something</div>
						<div class="custom-divider" text-align="center" />
						<div class="box-data">
							<span>信息</span>
						</div>
					</div>
				</div>
				<div @click="" class="box hover" id="dailyReport">
					<div class="box-content">
						<div class="box-title">量表3</div>
						<div class="box-en">Something</div>
						<div class="custom-divider" text-align="center" />
						<div class="box-data">
							<span>信息</span>
						</div>
					</div>
				</div>
			</div> -->
		</div>
	</view>
</template>

<script>
	import IndexSwiper from '@/components/swiper/IndexSwiper.vue';

	// #ifdef APP-PLUS
	var jpushModule = uni.requireNativePlugin("JG-JPush");
	// #endif

	import http from '@/nxTemp/config/requestConfig'
	import config from "@/nxTemp/config/index.config.js";

	import {
		testApi
	} from "@/nxTemp/apis/index.js";

	export default {
		data() {
			return {
				userInfo: {
					userName: '',
					password: '',
					userPhone: '',
					userGender: '',
					birthDate: ''
				},
				hasBasicInfo: true
			};
		},

		components: {
			IndexSwiper
		},

		onLoad(parms) {
			try {
				// #ifdef APP-PLUS
				var Color = plus.android.importClass("android.graphics.Color");
				plus.android.importClass("android.view.Window");
				var mainActivity = plus.android.runtimeMainActivity();
				var window_android = mainActivity.getWindow();
				window_android.setNavigationBarColor(Color.parseColor("#ffffff"));
				// #endif
			} catch (err) {}

			// 获取
			let userInfo = uni.getStorageSync('userInfo');

			this.userInfo = userInfo;
			// #ifdef APP-PLUS
			// 设置别名
			uni.$emit('userLoginEvent', {});
			// #endif
			http.get("/user/pushConnected").then((res) => {})
		},

		onShow(parms) {
			this.getBasicInfoStatus();
		},

		onUnload() {},
		methods: {
			getBasicInfoStatus() {
				http.get("/basicInfo/getRecord").then((res) => {
					if (res.result == 0) {
						this.hasBasicInfo = false;
					}
				})
			},

			// EMA() {
			// 	const _this = this;
			// 	uni.navigateTo({
			// 		url: 'pages/Emotion/EMA'
			// 	});
			// },
			// 添加手环数据跳转方法
			BraceletData() {
				const _this = this;
				uni.navigateTo({
					url: '/pages/braceletData/braceletData'
				});
			},

			// DailyReport() {
			// 	const _this = this;
			// 	uni.navigateTo({
			// 		url: 'pages/health/DailyReport'
			// 	});
			// },
			DailyReport() {
				const now = new Date();
				const hours = now.getHours();

				// 允许时间段：20:00–23:59 或 0:00–4:59
				if (!(hours >= 20 || hours < 5)) {
					uni.showToast({
						title: '未到填报时间，请在晚上8点到次日凌晨5点之间填写',
						icon: 'none'
					});
					return;
				}

				// 满足条件才跳转
				uni.navigateTo({
					url: 'pages/health/DailyReport'
				});
			},


			DepressionVideo() {
				uni.navigateTo({
					url: '/pages/video/Video'
				});
			},

			// 量表
			PSS() {
				const _this = this;
				uni.navigateTo({
					url: '/pages/Scale/PSS'
				});
			},

			PHQ() {
				const _this = this;
				uni.navigateTo({
					url: '/pages/Scale/SDS'
				});
			},

			SAS() {
				const _this = this;
				uni.navigateTo({
					url: '/pages/Scale/SAS'
				});
			}
		}
	}
</script>

<style lang="scss" scoped>
	.content-all {
		// background-color: #fff2dc;
		// background: linear-gradient(#fda085, #f6d365);
		background-image: linear-gradient(180deg, #e0f7fd 0%, #ebfbec 100%);

		// background-image: url('../../static/images/background/blur3.jpg');
		// background-size: cover;
		// background-position: center;

		// position: relative;  
		// z-index: 1;
		// &::before {  
		//     content: "";  
		//     position: absolute;  
		//     top: 0;  
		//     left: 0;  
		//     right: 0;  
		//     bottom: 0;  
		//     background-image: url('../../static/images/background/blur3.jpg');  
		//     background-size: cover;  
		//     background-position: center;  
		//     filter: blur(1px); // 调整模糊度  
		//     z-index: -1; // 确保这个伪元素位于原元素之下  
		//   } 
	}

	.content-all2 {
		background-color: #cffdff;
		background-image:
			radial-gradient(closest-side, rgb(194, 243, 164), rgba(235, 105, 78, 0)),
			radial-gradient(closest-side, rgb(255, 211, 211), rgba(243, 11, 164, 0)),
			radial-gradient(closest-side, rgb(202, 221, 250), rgba(254, 234, 131, 0)),
			radial-gradient(closest-side, rgb(250, 220, 96), rgba(170, 142, 245, 0));

		background-size: 130vw 130vh, 120vw 120vh, 100vw 150vh, 120vw 130vh;
		background-position: -80vw -80vh, 30vw -20vh, -20vw 20vh, 30vw 20vh;
		background-repeat: no-repeat;
		animation: 15s moiveAnimation infinite;
	}

	@keyframes moiveAnimation {

		0%,
		100% {
			background-size: 130vw 130vh, 120vw 120vh, 100vw 150vh, 120vw 130vh;
			background-position: -80vw -80vh, 30vw -20vh, -20vw 20vh, 30vw 20vh;
		}

		25% {
			background-size: 120vw 120vh, 130vw 150vh, 130vw 120vh, 100vw 110vh;
			background-position: -30vw -30vh, 40vw -10vh, 0vw 10vh, -10vw 20vh;
		}

		50% {
			background-size: 130vw 130vh, 140vw 100vh, 100vw 150vh, 90vw 110vh;
			background-position: 10vw -60vh, 20vw 10vh, 10vw 30vh, 10vw -20vh;
		}

		75% {
			background-size: 140vw 140vh, 100vw 130vh, 100vw 150vh, 130vw 110vh;
			background-position: -70vw -70vh, 0vw -10vh, 30vw -20vh, 20vw 30vh;
		}
	}

	/*分割线的样式*/
	.title {
		/*上右下左（顺时针）*/
		padding: 2px 0 2px 15px; // 文字
		// margin: 8px auto;
		font-size: 1em;
		font-weight: bold;
		color: #4a4c50;
		// background-color: #333399;
		// background-color: #EF8374;
		border-radius: 10px 10px 0 0;
	}

	.title-2 {
		/*上右下左（顺时针）*/
		// padding: 2px; // 文字
		text-align: center;
		font-size: 0.9em;
		color: #75787e;
		padding-bottom: 10px;
	}

	.custom-divider {
		height: 1px;
		/* 分割线的高度 */
		background-color: white;
		/* 分割线的颜色 */
		width: 90%;
		/* 分割线的宽度，这里设置为父容器宽度的80% */
		margin-top: 5px;
		/* 上下外边距和水平居中 */
	}

	.content {
		width: 95%;
		height: 100%;
		display: flex;
		padding: 10px;
		// background-color: #afdcef;
		align-content: flex-start;
		/* 换行紧贴 */
		flex-wrap: wrap;
		/* 换行 */
	}

	.content-1 {
		width: 95%;
		height: 100%;
		display: flex;
		padding: 10px 10px 0 10px;
		// background-color: #afdcef;
		align-content: flex-start;
		/* 换行紧贴 */
		flex-wrap: wrap;
		/* 换行 */
	}

	.content-2 {
		width: 95%;
		height: 100%;
		display: flex;
		padding: 0 10px 10px 10px;
		// background-color: #afdcef;
		align-content: flex-start;
		/* 换行紧贴 */
		flex-wrap: wrap;
		/* 换行 */
	}

	.box {
		// box-shadow: 1px 1px 4px gray;
		width: 160px;
		height: 110px;
		position: relative;
		flex: 1 0 29%;
		border-radius: 10px;
		// margin: 8px 10px 0 10px;
		padding: 3px 0 0 5px; // 文字

		margin: 3px
	}

	.box-content {
		width: 100%;
		height: 100%;
		padding: 4px;
		position: relative;
		//box-shadow: 1px 1px 4px gray;
	}

	.content-wrapper .content {
		margin-top: 8px;
		margin-left: 16px;
		font-size: 2.2em;
	}

	.box-title {
		font-size: 1.3em;
		font-weight: bold;
		color: white;
		// letter-spacing: 1.5px;
	}

	.box-en {
		margin-top: 3px;
		font-size: 0.8em;
		color: white;
	}

	.box-data {
		margin-top: 5px;
		color: #626364;

		span {
			font-size: 0.8em;
			// font-weight: bolder;
		}
	}

	.image-container {
		position: absolute;
		bottom: 0;
		right: 0;
		// text-align: right;
		padding: 0 25px 15px 0;
	}

	.box-img {
		width: 20px;
		height: 25px;
	}


	/*
十六进制颜色码转换成RGB颜色值：
https://www.sioe.cn/yingyong/yanse-rgb-16/
*/
	/*
每一行两个按钮，但最后一行会出现只有1个按钮，
这时候为了保持一行两个按钮的布局，
添加一个空元素占位
*/
	#empty {
		background: rgb(255, 255, 255);
	}

	#dailyReport {
		background: linear-gradient(#a1c4fd, #c2e9fb);
	}

	#braceletData {
		background: linear-gradient(#96e6a1, #cef475);
	}

	#phq {
		background: linear-gradient(#fda085, #f6d365);
	}

	#depressionvideo {
		// background: rgb(237, 180, 88);
		// background: #FEA443
		background: linear-gradient(#ff9a9c, #fad0c4);
	}

	#pss {
		background: linear-gradient(#a18cd1, #fbc2eb);
	}

	#sas {
		// background: rgb(190, 243, 248);
		background: linear-gradient(#a7b2c1, #dfe1e3);
		// background: #C8E9EE
	}
</style>