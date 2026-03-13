<template>
	<view>
		<div class="head">
			<image class="img" :src="userInfo.avatar || '../../static/images/mine/avatar.png'">
			</image>
			<div class="user-info-container">
				<div class="greet">用户：{{ userInfo.userName || "未登录" }}</div>
				<div class="button-container">
					<el-button class="logoutClass" type="text" icon="el-icon-switch-button" @click="showBasicInfo"
						v-if="hasBasicInfo">个人信息</el-button>
					<el-button class="logoutClass" type="text" icon="el-icon-switch-button" @click="showBasicInfo"
						v-else>个人信息待完善<span class="red-dot" /></el-button>
				</div>
			</div>
		</div>

		<u-divider class="custom-divider" text-align="center" />

		<div class="title">服务</div>
		<div class="content">
			<div @click="Chart" class="box hover" id="chart">
				<div class="box-content">
					<div class="box-title">历史记录</div>
					<div class="box-data">
						<span>查询过去提交的记录</span>
					</div>
				</div>
			</div>
			<div @click="Modify" class="box hover" id="modify">
				<div class="box-content">
					<div class="box-title">修改密码</div>
					<div class="box-data">
						<span>修改您的登录密码</span>
					</div>
				</div>
			</div>
			<div @click="Settings" class="box hover" id="setting">
				<div class="box-content">
					<div class="box-title">设置</div>
				</div>
			</div>
			<div @click="Logout" class="box hover" id="logout">
				<div class="box-content">
					<div class="box-title">退出登录</div>
				</div>
			</div>
			<div @click="Logoff" class="box hover" id="logoff">
				<div class="box-content">
					<div class="box-title">注销账户</div>
				</div>
			</div>
		</div>
	</view>
</template>

<script>
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
					avatar: '',
					birthDate: ''
				},
				hasBasicInfo: true
			};
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
			jpushModule.setAlias({
				'alias': userInfo.userId,
				'sequence': 1
			});
			// #endif
			http.get("/user/pushConnected").then((res) => {})
		},

		onShow(parms) {
			this.getBasicInfoStatus();
		},

		methods: {
			getBasicInfoStatus() {
				http.get("/basicInfo/getRecord").then((res) => {
					if (res.result == 0) {
						this.hasBasicInfo = false;
					}
				})
			},

			async uploadImage() {
				uni.chooseImage({
					count: 1,
					sizeType: ['original', 'compressed'],
					sourceType: ['album', 'camera'],
					success: (res) => {
						if (res.tempFilePaths.length > 0) {
							const filePath = res.tempFilePaths[0];
							const cloudPath = `image_${Date.now()}.jpg`;

							this.uploadFiles(filePath, cloudPath).then(fileID => {
								this.userInfo.avatar = fileID; // 直接使用 fileID 而不是数组  
								console.log('File uploaded successfully with ID:', fileID);
								this.saveToDatabase(fileID);
							}).catch(error => {
								console.error('Error uploading file:', error);
								uni.showToast({
									title: '图片上传失败',
									icon: 'none'
								});
							});
						}
					}
				});
			},

			async uploadFiles(filePath, cloudPath) {
				try {
					const result = await uniCloud.uploadFile({
						filePath: filePath,
						cloudPath: cloudPath
					});
					return result.fileID; // 假设result对象包含fileID  
				} catch (error) {
					throw new Error('Failed to upload file: ' + error.message);
				}
			},

			// 假设这个方法用于将新闻数据保存到数据库（如果需要的话）  
			async saveToDatabase(fileID) {
				const userData = {
					avatar: fileID
				};
				// 发送POST请求到服务器  
				http.post(`${config.baseUrl}/user/updateAvatar`, userData).then(res => {
					if (res.code === 200) {
						uni.showToast({
							title: "提交成功"
						});
					} else {
						uni.showToast({
							title: "提交失败",
							icon: 'error'
						});
					}
				}).catch(error => {
					console.error('Error saving news to database:', error);
					uni.showToast({
						title: "网络请求失败",
						icon: 'none'
					});
				});
			},

			scanCode: function() {
				uni.scanCode({
					success: function(res) {
						console.log('条码类型：' + res.scanType);
						console.log('条码内容：' + res.result);
					}
				});
			},

			Chart() {
				const _this = this;
				uni.navigateTo({
					url: "/pages/Chart/ChartMain"
				});
			},

			Modify() {
				const _this = this;
				uni.navigateTo({
					url: '/pages/me/ChangePass'
				});
			},

			showBasicInfo() {
				const _this = this;
				uni.navigateTo({
					url: "/pages/health/BasicInfo"
				});
			},

			DepressionVideo() {
				uni.navigateTo({
					url: '/pages/video/Video'
				});
			},
			
			Settings() {
				uni.navigateTo({
					url: '/pages/me/setting'
				});
			},

			Logout() {
				let _this = this;
				uni.showModal({
					title: '提示',
					content: '您确定要退出登录吗？',
					success: function(res) {
						if (res.confirm) {
							
							_this.$store.dispatch('setUserData', null);
							uni.setStorageSync('token', null);
							setTimeout(() => {
								uni.reLaunch({
									url: "/pages/login/login"
								});
							}, 500)
						}
					}
				});
			},
			Logoff() {
				let _this = this;
				uni.showModal({
					title: '提示',
					content: '您确定要注销账户吗？',
					success: function(res) {
						let userInfo = uni.getStorageSync('userInfo');
						this.userInfo = userInfo;
						let userId = {};
						userId.userId = this.userInfo.userId;
						console.log(userId.userId)
						if (res.confirm) {
							console.log(this.userId)
							console.log(`Sending request to: ${config.baseUrl}/user/deleteUserById`);
							console.log('Request data:', userId);
							http.get(`${config.baseUrl}/user/deleteUserById`, userId).then((res) => {
								console.log(this.userId)
								if (res.code === 200) { //返回成功码200，弹窗提示，延迟转到用户主页
									uni.showToast({
										title: "注销成功"
									});
									setTimeout(() => {
										uni.navigateTo({
											url: "/pages/login/login"
										});
									}, 700);
								} else { //返回非200，弹窗提示失败，停留在此页
									uni.showToast({
										title: "注销失败",
										icon: 'error'
									});
								}
							})
							
							// _this.$store.dispatch('setUserData', null);
							// uni.setStorageSync('token', null);
							// setTimeout(() => {
							// 	uni.reLaunch({
							// 		url: "/pages/login/login"
							// 	});
							// }, 500)
						}
					}
				});
			},
		}
	}
</script>

<style lang="scss" scoped>
	.logoutClass{
		border-radius: 15px;
		// height: 60px;
		padding: 5px 10px;
		background: linear-gradient(to right, #fda085, #f6d365);
		color: #ffffff;
	}
	
	.custom-divider {
		height: 1px;
		background-color: #d8d8d8;
		width: 100%;
		margin: 0;
		// border: 2px solid #00ff00;
	}

	.head {
		display: flex;
		// border: 2px solid #aa0000;
		height: 100px;
		width: 100%;
		z-index: 1;
		border-radius: 10px;
		padding: 10px;
		align-items: center;

		.img {
			width: 100px;
			height: 100px;
			border-radius: 50%;
			margin-l: 10px;
			margin-right: 10px;
		}

		.user-info-container {
			display: flex;
			flex-direction: column;
			align-items: flex-start;
			// border: 1px solid #00ff00;
			margin-left: 10px;
			/* 或者 center，取决于你想要的对齐方式 */
		}

		.greet {
			font-size: 1.5em;
			font-weight: bold;
		}

		.button-container {
			color: #333399;
			margin-top: 20px;

		}

		.red-dot {
			display: inline-block;
			width: 5px;
			height: 5px;
			background-color: red;
			border-radius: 50%;
			margin-left: 8px;
			/* 小红点与文本之间的间隔 */
		}
	}

	/*分割线的样式*/
	.title {
		/*上右下左（顺时针）*/
		padding: 2px 0 2px 10px; // 文字
		margin: 8px auto;
		border-left: 8px solid #4554F5;
		font-size: 2em;
		font-weight: bold;
		color: #4c5158;
	}

	.content {
		width: 100%;
		height: 100%;
	}

	.box {
		border-radius: 10px;
		margin: 10px 10px 0 10px;
		padding: 3px 0 0 5px; // 文字
		color: white;
	}

	.box-content {
		width: 100%;
		height: 45px;
		padding: 10px;
	}

	.box-title {
		font-size: 1.6em;
		font-weight: bold;
		// margin-bottom: 3px;
	}

	.box-data {
		color: black;
		margin-top: 5px;
		// margin-bottom: 3px;
		span {
			font-size: 0.8em;
		}
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
	#chart {
		background: linear-gradient(to right, #a18cd1, #FFF1EB);
		box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);  
	}

	#modify {
		background: linear-gradient(to right, #96e6a1, #FFF1EB);
		box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);  
	}
	
	#setting {
		background: linear-gradient(to right, #a1c4fd, #FFF1EB);
		box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);  
	}
	#logout {
		background: linear-gradient(to right, #a7b2c1, #FFF1EB);
		box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);  
	}
	#logoff {
		background: linear-gradient(to right, #ffce52, #FFF1EB);
		box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);  
	}
	
</style>