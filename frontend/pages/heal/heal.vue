<template>
	<view class="heal-page">
		<!-- Tab 标签栏 -->
		<view class="tab-bar">
			<view
				v-for="(tab, index) in tabs"
				:key="index"
				class="tab-item"
				:class="{ active: currentTab === index }"
				@click="switchTab(index)"
			>
				<text class="tab-text">{{ tab }}</text>
				<view class="tab-indicator" v-if="currentTab === index"></view>
			</view>
		</view>

		<!-- 滑动内容区 -->
		<swiper
			class="tab-content"
			:current="currentTab"
			@change="onSwiperChange"
			duration="300"
			:disable-touch="false"
		>
			<!-- 聊天页 -->
			<swiper-item class="swiper-item">
				<view class="chat-wrapper">
					<!-- 顶部横幅 -->
					<view class="chat-banner">
						<image class="chat-banner-avatar" src="https://api.dicebear.com/7.x/lorelei/svg?seed=Mia&backgroundColor=ffd5dc" mode="aspectFill" />
						<view class="chat-banner-info">
							<view class="chat-banner-title-row">
								<text class="chat-banner-name">心晴</text>
								<view class="chat-banner-tag">AI 心理助手</view>
							</view>
							<text class="chat-banner-desc">专业倾听，温暖陪伴，随时为你解忧 🌸</text>
						</view>
						<view class="chat-banner-status">
							<view class="chat-banner-dot online"></view>
							<text class="chat-banner-online-text">在线</text>
						</view>
					</view>

					<!-- 聊天记录滚动区 -->
					<scroll-view scroll-y class="chat-messages" :scroll-top="scrollTop" scroll-with-animation>
						<view
							v-for="(msg, idx) in chatMessages"
							:key="idx"
							class="message-row"
							:class="msg.role === 'user' ? 'message-right' : 'message-left'"
						>
							<!-- AI 头像（左侧） -->
							<image
								v-if="msg.role === 'ai'"
								class="avatar ai-avatar"
								src="https://api.dicebear.com/7.x/lorelei/svg?seed=Mia&backgroundColor=ffd5dc"
								mode="aspectFill"
							/>
							<!-- 消息气泡 -->
							<view class="bubble" :class="msg.role === 'user' ? 'bubble-user' : 'bubble-ai'">
								<image v-if="msg.type === 'image'" class="msg-image" :src="msg.src" mode="widthFix" />
								<text v-else class="bubble-text">{{ msg.text }}</text>
							</view>
							<!-- 用户头像（右侧） -->
							<image
								v-if="msg.role === 'user'"
								class="avatar user-avatar"
								src="https://api.dicebear.com/7.x/lorelei/svg?seed=Alex&backgroundColor=c8f0d8"
								mode="aspectFill"
							/>
						</view>
						<view class="msg-bottom-anchor"></view>
					</scroll-view>

					<!-- 输入区（固定在底部） -->
					<view class="chat-input-area">
						<!-- 表情面板 -->
						<view class="emoji-panel" v-if="showEmojiPanel">
							<view class="emoji-grid">
								<text
									v-for="(e, i) in emojiList"
									:key="i"
									class="emoji-item"
									@click="insertEmoji(e)"
								>{{ e }}</text>
							</view>
						</view>
						<!-- 工具栏 -->
						<view class="chat-toolbar">
							<view class="toolbar-btn" @click="chooseImage">
								<text class="toolbar-icon">📷</text>
							</view>
							<view
								class="toolbar-btn"
								:class="{ 'toolbar-btn-active': isRecording }"
								@click="toggleVoice"
							>
								<text class="toolbar-icon">{{ isRecording ? '⏹️' : '🎤' }}</text>
							</view>
							<view
								class="toolbar-btn"
								:class="{ 'toolbar-btn-active': showEmojiPanel }"
								@click="toggleEmojiPanel"
							>
								<text class="toolbar-icon">😊</text>
							</view>
							<text v-if="isRecording" class="recording-tip">🔴 录音中...</text>
						</view>
						<!-- 输入行 -->
						<view class="chat-input-row">
							<textarea
								class="chat-input"
								v-model="inputText"
								placeholder="说说你今天的感受..."
								placeholder-style="color:#b0bec5;font-size:0.92em;"
								:auto-height="false"
								maxlength="500"
							/>
							<view class="send-btn" @click="sendMessage">
								<text class="send-icon">➤</text>
							</view>
						</view>
					</view>
				</view>
			</swiper-item>

			<!-- 冥想页 -->
			<swiper-item class="swiper-item">
				<scroll-view scroll-y class="meditation-page">
					<!-- 呼吸圆圈 -->
					<view class="breath-section">
						<view class="breath-outer" :class="{ breathing: isMeditating }">
							<view class="breath-middle">
								<view class="breath-inner">
									<text class="breath-tip">{{ breathTip }}</text>
									<text class="breath-timer" v-if="isMeditating">{{ formatTime(timeLeft) }}</text>
									<text class="breath-idle" v-else>点击开始</text>
								</view>
							</view>
						</view>
					</view>

					<!-- 时长选择 -->
					<view class="duration-section">
						<text class="section-title">选择时长</text>
						<view class="duration-list">
							<view
								v-for="d in durations"
								:key="d"
								class="duration-item"
								:class="{ 'duration-active': selectedDuration === d }"
								@click="selectDuration(d)"
							>
								<text>{{ d }}分钟</text>
							</view>
						</view>
					</view>

					<!-- 开始/暂停按钮 -->
					<view class="meditate-btn-wrap">
						<view class="meditate-btn" @click="toggleMeditate">
							<text class="meditate-btn-text">{{ isMeditating ? '暂停冥想' : '开始冥想' }}</text>
						</view>
					</view>

					<!-- 冥想项目 -->
					<view class="meditation-cards">
						<text class="section-title">精选冥想</text>
						<view
							v-for="(item, idx) in meditationItems"
							:key="idx"
							class="med-card"
							:style="{ background: item.bg }"
						>
							<view class="med-card-left">
								<text class="med-icon">{{ item.icon }}</text>
								<view>
									<text class="med-name">{{ item.name }}</text>
									<text class="med-duration">{{ item.duration }}</text>
								</view>
							</view>
							<view class="med-play-btn">▶</view>
						</view>
					</view>
				</scroll-view>
			</swiper-item>

			<!-- 日记页 -->
			<swiper-item class="swiper-item">
				<scroll-view scroll-y class="diary-page">
					<!-- 顶部日期 -->
					<view class="diary-header">
						<text class="diary-today-label">今天</text>
						<text class="diary-today-date">{{ todayStr }}</text>
					</view>

					<!-- 今日写日记入口 -->
					<view class="diary-write-card" @click="showDiaryModal = true">
						<view class="diary-mood-row">
							<text class="diary-mood-label">今日心情</text>
							<view class="mood-selector">
								<text
									v-for="(m, idx) in moods"
									:key="idx"
									class="mood-emoji"
									:class="{ 'mood-selected': selectedMood === idx }"
									@click.stop="selectedMood = idx"
								>{{ m.emoji }}</text>
							</view>
						</view>
						<view class="diary-placeholder">
							<text class="diary-placeholder-text">{{ selectedDiaryText || '记录此刻的心情...' }}</text>
						</view>
					</view>

					<!-- 日记列表 -->
					<view class="diary-list">
						<text class="section-title">往日记录</text>
						<view
							v-for="(entry, idx) in diaryEntries"
							:key="idx"
							class="diary-entry"
						>
							<view class="diary-entry-left">
								<text class="diary-entry-emoji">{{ moods[entry.moodIndex].emoji }}</text>
							</view>
							<view class="diary-entry-body">
								<view class="diary-entry-top">
									<text class="diary-entry-date">{{ entry.date }}</text>
									<text class="diary-entry-mood-name">{{ moods[entry.moodIndex].label }}</text>
								</view>
								<text class="diary-entry-content">{{ entry.content }}</text>
							</view>
						</view>
					</view>
				</scroll-view>

				<!-- 写日记浮动按钮 -->
				<view class="fab-btn" @click="showDiaryModal = true">
					<text class="fab-icon">✏️</text>
				</view>
			</swiper-item>
		</swiper>

		<!-- 日记编辑弹窗 -->
		<view class="modal-mask" v-if="showDiaryModal" @click.self="showDiaryModal = false">
			<view class="modal-box">
				<view class="modal-header">
					<text class="modal-title">写日记</text>
					<text class="modal-close" @click="showDiaryModal = false">✕</text>
				</view>
				<view class="modal-mood-row">
					<text
						v-for="(m, idx) in moods"
						:key="idx"
						class="mood-emoji modal-mood"
						:class="{ 'mood-selected': selectedMood === idx }"
						@click="selectedMood = idx"
					>{{ m.emoji }}</text>
				</view>
				<textarea
					class="modal-textarea"
					v-model="newDiaryText"
					placeholder="今天发生了什么？有什么感受想记录下来？"
					placeholder-style="color:#b0bec5"
					maxlength="500"
				/>
				<view class="modal-footer">
					<view class="modal-cancel-btn" @click="showDiaryModal = false">取消</view>
					<view class="modal-save-btn" @click="saveDiary">保存</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		const now = new Date();
		const pad = n => String(n).padStart(2, '0');
		const todayStr = `${now.getFullYear()}年${pad(now.getMonth() + 1)}月${pad(now.getDate())}日`;
		return {
			tabs: ['聊天', '冥想', '日记'],
			currentTab: 0,

			// --- 聊天 ---
			inputText: '',
			scrollTop: 0,
			showEmojiPanel: false,
			isRecording: false,
			recorderManager: null,
			emojiList: [
				'🌸','🌿','🌙','✨','🌟','💫','🌈','✨️',
				'🧘','💪','💙','💚','💜','🤍','🧠','🏠',
				'🌻','🍊','🍜','☕','🍃','🌊','🕊','🐢',
				'😊','😄','😍','🤩','🙂','😐'
			],
			chatMessages: [
				{ role: 'ai', text: '你好呀 🌸 我是心晴，你的专属心理助手。今天感觉怎么样？有什么想聊的吗？' },
				{ role: 'user', text: '最近压力有点大，睡不好。' },
				{ role: 'ai', text: '听起来你最近承受了不少，睡眠不好会让人更容易焦虑。能跟我说说是工作还是生活上的压力吗？🌙' }
			],
			aiReplies: [
				'我理解你的感受，这很正常。试着做几次深呼吸，让自己慢慢平静下来 🌿',
				'谢谢你愿意分享这些。你已经做得很好了，对自己温柔一点 💙',
				'放松一下，今天能来倾诉就是很勇敢的一步 🌟',
				'我在听，继续说吧，没有评判，只有陪伴 🍃',
				'也许可以试试冥想，切换到"冥想"标签页感受一下？🧘'
			],

			// --- 冥想 ---
			durations: [5, 10, 15, 20],
			selectedDuration: 5,
			isMeditating: false,
			timeLeft: 300,
			timer: null,
			breathPhase: 0,
			breathTimer: null,
			breathTips: ['吸气...', '屏息...', '呼气...', '放松...'],
			breathTip: '准备好了吗',
			meditationItems: [
				{ name: '专注冥想', duration: '10 分钟', icon: '🎯', bg: 'linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%)' },
				{ name: '睡眠冥想', duration: '20 分钟', icon: '🌙', bg: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)' },
				{ name: '减压冥想', duration: '15 分钟', icon: '🌊', bg: 'linear-gradient(135deg, #96e6a1 0%, #cef475 100%)' },
				{ name: '晨间冥想', duration: '5 分钟', icon: '🌅', bg: 'linear-gradient(135deg, #fda085 0%, #f6d365 100%)' }
			],

			// --- 日记 ---
			todayStr,
			moods: [
				{ emoji: '😄', label: '开心' },
				{ emoji: '😊', label: '平静' },
				{ emoji: '😐', label: '一般' },
				{ emoji: '😔', label: '难过' },
				{ emoji: '😰', label: '焦虑' }
			],
			selectedMood: 1,
			showDiaryModal: false,
			newDiaryText: '',
			selectedDiaryText: '',
			diaryEntries: [
				{ date: '2026年03月18日', moodIndex: 0, content: '今天和朋友出去爬山，天气很好，心情也特别轻松，感觉充了满满的电 ⛰️' },
				{ date: '2026年03月17日', moodIndex: 2, content: '工作上遇到了些小麻烦，不过慢慢解决了，晚上喝了杯热牛奶好多了。' },
				{ date: '2026年03月16日', moodIndex: 3, content: '有点想家，想念妈妈做的饭菜，下次回家一定要多陪陪家人。' }
			]
		};
	},

	methods: {
		switchTab(index) {
			this.currentTab = index;
		},
		onSwiperChange(e) {
			this.currentTab = e.detail.current;
		},

		// 聊天
		sendMessage() {
			const text = this.inputText.trim();
			if (!text) return;
			this.showEmojiPanel = false;
			this.chatMessages.push({ role: 'user', text });
			this.inputText = '';
			this.$nextTick(() => { this.scrollToBottom(); });
			setTimeout(() => {
				const reply = this.aiReplies[Math.floor(Math.random() * this.aiReplies.length)];
				this.chatMessages.push({ role: 'ai', text: reply });
				this.$nextTick(() => { this.scrollToBottom(); });
			}, 800);
		},
		scrollToBottom() {
			this.scrollTop = 99999;
		},
		chooseImage() {
			uni.chooseImage({
				count: 1,
				sizeType: ['compressed'],
				sourceType: ['album', 'camera'],
				success: (res) => {
					const src = res.tempFilePaths[0];
					this.chatMessages.push({ role: 'user', type: 'image', src });
					this.$nextTick(() => { this.scrollToBottom(); });
					setTimeout(() => {
						this.chatMessages.push({ role: 'ai', text: '我看到你分享的图片了 👀 有什么想说的吗？' });
						this.$nextTick(() => { this.scrollToBottom(); });
					}, 600);
				}
			});
		},
		toggleVoice() {
			if (this.isRecording) {
				this.stopVoice();
			} else {
				this.startVoice();
			}
		},
		startVoice() {
			const rm = uni.getRecorderManager();
			this.recorderManager = rm;
			rm.onStart(() => { this.isRecording = true; });
			rm.onStop((res) => {
				this.isRecording = false;
				// 模拟语音转文字（实际项目可对接语音识别 API）
				uni.showToast({ title: '语音识别中...', icon: 'loading', duration: 1000 });
				setTimeout(() => {
					const demoTexts = ['我最近心情不错', '感觉有点压力', '今天天气很好，心情轻松多了'];
					this.inputText = demoTexts[Math.floor(Math.random() * demoTexts.length)];
					uni.showToast({ title: '已转为文字', icon: 'success' });
				}, 1000);
			});
			rm.onError(() => {
				this.isRecording = false;
				uni.showToast({ title: '麦克风权限不足', icon: 'none' });
			});
			rm.start({ duration: 30000, sampleRate: 16000, numberOfChannels: 1, encodeBitRate: 48000, format: 'mp3' });
		},
		stopVoice() {
			if (this.recorderManager) { this.recorderManager.stop(); }
		},
		toggleEmojiPanel() {
			this.showEmojiPanel = !this.showEmojiPanel;
		},
		insertEmoji(emoji) {
			this.inputText += emoji;
		},

		// 冥想
		selectDuration(d) {
			if (this.isMeditating) return;
			this.selectedDuration = d;
			this.timeLeft = d * 60;
		},
		toggleMeditate() {
			if (this.isMeditating) {
				this.stopMeditate();
			} else {
				this.startMeditate();
			}
		},
		startMeditate() {
			this.timeLeft = this.selectedDuration * 60;
			this.isMeditating = true;
			this.timer = setInterval(() => {
				if (this.timeLeft <= 0) {
					this.stopMeditate();
					uni.showToast({ title: '冥想完成 🎉', icon: 'none' });
					return;
				}
				this.timeLeft--;
			}, 1000);
			this.breathPhase = 0;
			this.breathTip = this.breathTips[0];
			const breathDurations = [4000, 2000, 6000, 2000];
			const cycleBreath = () => {
				this.breathPhase = (this.breathPhase + 1) % 4;
				this.breathTip = this.breathTips[this.breathPhase];
				this.breathTimer = setTimeout(cycleBreath, breathDurations[this.breathPhase]);
			};
			this.breathTimer = setTimeout(cycleBreath, breathDurations[0]);
		},
		stopMeditate() {
			this.isMeditating = false;
			clearInterval(this.timer);
			clearTimeout(this.breathTimer);
			this.timer = null;
			this.breathTimer = null;
			this.breathTip = '准备好了吗';
		},
		formatTime(seconds) {
			const m = Math.floor(seconds / 60);
			const s = seconds % 60;
			return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`;
		},

		// 日记
		saveDiary() {
			if (!this.newDiaryText.trim()) {
				uni.showToast({ title: '请写点什么吧~', icon: 'none' });
				return;
			}
			const now = new Date();
			const pad = n => String(n).padStart(2, '0');
			const dateStr = `${now.getFullYear()}年${pad(now.getMonth() + 1)}月${pad(now.getDate())}日`;
			this.diaryEntries.unshift({
				date: dateStr,
				moodIndex: this.selectedMood,
				content: this.newDiaryText.trim()
			});
			this.selectedDiaryText = this.newDiaryText.trim();
			this.newDiaryText = '';
			this.showDiaryModal = false;
			uni.showToast({ title: '日记已保存 📖', icon: 'none' });
		}
	},

	beforeDestroy() {
		this.stopMeditate();
	}
};
</script>

<style lang="scss" scoped>
.heal-page {
	display: flex;
	flex-direction: column;
	height: 100vh;
	background: linear-gradient(160deg, #e8f4fd 0%, #f0faf5 50%, #f5f0fb 100%);
	overflow: hidden;
}

/* ===== Tab 栏 ===== */
.tab-bar {
	display: flex;
	background: #fff;
	box-shadow: 0 1px 6px rgba(0, 0, 0, 0.06);
	position: relative;
	z-index: 10;
}
.tab-item {
	flex: 1;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 12px 0 8px;
	position: relative;
	cursor: pointer;
}
.tab-text {
	font-size: 1em;
	color: #999;
	transition: color 0.25s;
	font-weight: 500;
}
.tab-item.active .tab-text {
	color: #5b9cf6;
	font-weight: bold;
}
.tab-indicator {
	position: absolute;
	bottom: 0;
	width: 28px;
	height: 3px;
	border-radius: 2px;
	background: linear-gradient(to right, #a1c4fd, #5b9cf6);
	animation: slideIn 0.25s ease;
}
@keyframes slideIn {
	from { width: 0; opacity: 0; }
	to { width: 28px; opacity: 1; }
}

/* ===== Swiper ===== */
.tab-content {
	flex: 1;
	height: 0;
}
.swiper-item {
	height: 100%;
}

/* ===== 公共 ===== */
.section-title {
	font-size: 1em;
	font-weight: bold;
	color: #4a4c50;
	margin: 14px 0 8px 16px;
	display: block;
}

/* ===== 聊天 ===== */
.chat-wrapper {
	height: 100%;
	display: flex;
	flex-direction: column;
	background: linear-gradient(160deg, #edf4ff 0%, #f5f9ff 100%);
	overflow: hidden;
	position: relative;
}
.chat-banner {
	display: flex;
	align-items: center;
	background: linear-gradient(135deg, #f9a8c9 0%, #f7c4d8 40%, #d4a8f0 100%);
	border-radius: 0 0 22px 22px;
	margin: 0 0 4px;
	padding: 12px 16px 16px;
	box-shadow: 0 6px 20px rgba(217, 130, 180, 0.3);
	flex-shrink: 0;
	position: relative;
	overflow: hidden;
}
.chat-banner::before {
	content: '';
	position: absolute;
	top: -20px;
	right: -20px;
	width: 80px;
	height: 80px;
	border-radius: 50%;
	background: rgba(255,255,255,0.12);
}
.chat-banner::after {
	content: '';
	position: absolute;
	bottom: -30px;
	left: 30%;
	width: 100px;
	height: 100px;
	border-radius: 50%;
	background: rgba(255,255,255,0.08);
}
.chat-banner-avatar {
	width: 52px;
	height: 52px;
	border-radius: 50%;
	border: 2.5px solid rgba(255,255,255,0.8);
	box-shadow: 0 3px 10px rgba(0,0,0,0.15);
	margin-right: 12px;
	flex-shrink: 0;
	background: #fff;
}
.chat-banner-info {
	flex: 1;
	display: flex;
	flex-direction: column;
	z-index: 1;
}
.chat-banner-title-row {
	display: flex;
	align-items: center;
	gap: 8px;
	margin-bottom: 3px;
}
.chat-banner-name {
	font-size: 1.1em;
	font-weight: bold;
	color: #fff;
	letter-spacing: 0.5px;
	text-shadow: 0 1px 3px rgba(80,130,200,0.3);
}
.chat-banner-tag {
	background: rgba(255,255,255,0.28);
	border: 1px solid rgba(255,255,255,0.5);
	border-radius: 10px;
	padding: 1px 8px;
	font-size: 0.7em;
	color: #fff;
	letter-spacing: 0.5px;
}
.chat-banner-desc {
	font-size: 0.78em;
	color: rgba(255,255,255,0.9);
}
.chat-banner-status {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 3px;
	z-index: 1;
}
.chat-banner-dot {
	width: 10px;
	height: 10px;
	border-radius: 50%;
	background: #52e76a;
	box-shadow: 0 0 8px rgba(82, 231, 106, 0.7);
}
.chat-banner-online-text {
	font-size: 0.68em;
	color: rgba(255,255,255,0.9);
}
/* 消息滚动区：flex:1 自动占满剩余空间 */
.chat-messages {
	flex: 1;
	overflow-y: auto;
	padding: 10px 12px 4px;
	min-height: 0;
}
.msg-bottom-anchor {
	height: 8px;
}
.message-row {
	display: flex;
	align-items: flex-end;
	margin-bottom: 14px;
}
.message-left {
	flex-direction: row;
	justify-content: flex-start;
}
.message-right {
	flex-direction: row;
	justify-content: flex-end;
}
.avatar {
	width: 38px;
	height: 38px;
	border-radius: 50%;
	flex-shrink: 0;
	box-shadow: 0 2px 8px rgba(0,0,0,0.14);
	border: 2px solid #fff;
	background: #f0f5ff;
}
.ai-avatar { margin-right: 8px; }
.user-avatar { margin-left: 8px; }
.bubble {
	max-width: 66%;
	padding: 10px 14px;
	border-radius: 18px;
	line-height: 1.5;
}
.bubble-ai {
	background: #fff;
	border-radius: 4px 18px 18px 18px;
	box-shadow: 0 3px 10px rgba(161,196,253,0.18);
}
.bubble-user {
	background: linear-gradient(135deg, #5b9cf6, #a1c4fd);
	border-radius: 18px 4px 18px 18px;
	box-shadow: 0 3px 10px rgba(91,156,246,0.35);
}
.bubble-text {
	font-size: 0.92em;
	color: #333;
	line-height: 1.6;
}
.bubble-user .bubble-text {
	color: #fff;
}
.msg-image {
	max-width: 160px;
	border-radius: 10px;
}

/* 表情面板 */
.emoji-panel {
	background: #fff;
	border-bottom: 1px solid #eef2f8;
	padding: 8px 10px;
	flex-shrink: 0;
}
.emoji-grid {
	display: flex;
	flex-wrap: wrap;
	gap: 4px;
}
.emoji-item {
	font-size: 1.55em;
	width: calc(100% / 10 - 4px);
	text-align: center;
	padding: 4px 0;
	border-radius: 6px;
	transition: background 0.15s;
}
.emoji-item:active {
	background: #eef4ff;
}

/* 输入区：收缩在底部，不拉伸 */
.chat-input-area {
	background: #fff;
	border-top: 1px solid #eef2f8;
	padding: 8px 14px 12px;
	flex-shrink: 0;
	box-shadow: 0 -4px 20px rgba(161,196,253,0.15);
	border-radius: 20px 20px 0 0;
}
.chat-toolbar {
	display: flex;
	align-items: center;
	gap: 8px;
	padding: 0 2px 8px;
}
.toolbar-btn {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 38px;
	height: 38px;
	border-radius: 12px;
	background: #f0f5ff;
	border: 1.5px solid #e0eaff;
	transition: all 0.2s;
	cursor: pointer;
}
.toolbar-btn-active {
	background: linear-gradient(135deg, #a1c4fd, #c2e9fb);
	border-color: #a1c4fd;
	box-shadow: 0 3px 10px rgba(161,196,253,0.45);
}
.toolbar-icon {
	font-size: 1.2em;
	line-height: 1;
}
.recording-tip {
	font-size: 0.78em;
	color: #ff4d4d;
	font-weight: bold;
	margin-left: 4px;
	animation: blink 1s step-start infinite;
}
@keyframes blink {
	50% { opacity: 0; }
}
.chat-input-row {
	display: flex;
	align-items: center;
	gap: 8px;
}
.chat-input {
	flex: 1;
	background: #f0f5ff;
	border-radius: 22px;
	padding: 10px 16px;
	font-size: 0.92em;
	color: #333;
	border: 1.5px solid #d0e2ff;
	box-shadow: 0 2px 8px rgba(161,196,253,0.1);
	min-height: 40px;
	max-height: 40px;
	resize: none;
	line-height: 1.4;
}
.send-btn {
	width: 44px;
	height: 44px;
	border-radius: 14px;
	background: linear-gradient(135deg, #5b9cf6, #a1c4fd);
	display: flex;
	align-items: center;
	justify-content: center;
	flex-shrink: 0;
	box-shadow: 0 4px 14px rgba(91,156,246,0.45);
	transition: transform 0.15s, box-shadow 0.15s;
	cursor: pointer;
}
.send-btn:active { transform: scale(0.90); box-shadow: 0 2px 6px rgba(91,156,246,0.3); }
.send-icon {
	color: #fff;
	font-size: 1em;
}

/* ===== 冥想 ===== */
.meditation-page {
	height: 100%;
}
.breath-section {
	display: flex;
	justify-content: center;
	align-items: center;
	padding: 30px 0 10px;
}
.breath-outer {
	width: 180px;
	height: 180px;
	border-radius: 50%;
	background: rgba(161, 196, 253, 0.2);
	display: flex;
	align-items: center;
	justify-content: center;
	transition: transform 4s ease-in-out;
}
.breath-outer.breathing {
	animation: breathe 14s ease-in-out infinite;
}
@keyframes breathe {
	0%, 100% { transform: scale(1); box-shadow: 0 0 20px rgba(161, 196, 253, 0.3); }
	28% { transform: scale(1.18); box-shadow: 0 0 40px rgba(161, 196, 253, 0.6); }
	42% { transform: scale(1.18); box-shadow: 0 0 40px rgba(161, 196, 253, 0.6); }
	85% { transform: scale(0.95); box-shadow: 0 0 10px rgba(161, 196, 253, 0.2); }
}
.breath-middle {
	width: 140px;
	height: 140px;
	border-radius: 50%;
	background: rgba(161, 196, 253, 0.35);
	display: flex;
	align-items: center;
	justify-content: center;
}
.breath-inner {
	width: 106px;
	height: 106px;
	border-radius: 50%;
	background: linear-gradient(135deg, #a1c4fd, #c2e9fb);
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	box-shadow: 0 4px 20px rgba(161, 196, 253, 0.5);
}
.breath-tip {
	font-size: 0.85em;
	color: #fff;
	font-weight: bold;
	text-align: center;
}
.breath-timer {
	font-size: 1.1em;
	color: #fff;
	font-weight: bold;
	margin-top: 4px;
}
.breath-idle {
	font-size: 0.75em;
	color: rgba(255,255,255,0.85);
	margin-top: 4px;
}
.duration-section {
	padding: 0 16px;
}
.duration-list {
	display: flex;
	flex-direction: row;
	gap: 10px;
	margin-top: 8px;
}
.duration-item {
	flex: 1;
	text-align: center;
	padding: 8px 4px;
	border-radius: 20px;
	background: #f0f4ff;
	font-size: 0.85em;
	color: #7a8aac;
	border: 1.5px solid transparent;
	transition: all 0.2s;
}
.duration-active {
	background: linear-gradient(135deg, #a1c4fd, #c2e9fb);
	color: #fff;
	border-color: #a1c4fd;
	font-weight: bold;
	box-shadow: 0 2px 8px rgba(161,196,253,0.4);
}
.meditate-btn-wrap {
	display: flex;
	justify-content: center;
	margin: 18px 16px 10px;
}
.meditate-btn {
	background: linear-gradient(135deg, #a18cd1, #fbc2eb);
	border-radius: 30px;
	padding: 13px 50px;
	box-shadow: 0 4px 16px rgba(161, 140, 209, 0.45);
}
.meditate-btn-text {
	color: #fff;
	font-size: 1em;
	font-weight: bold;
	letter-spacing: 1px;
}
.meditation-cards {
	padding: 0 12px 20px;
}
.med-card {
	display: flex;
	align-items: center;
	justify-content: space-between;
	border-radius: 14px;
	padding: 14px 16px;
	margin-bottom: 10px;
	box-shadow: 0 2px 10px rgba(0,0,0,0.07);
}
.med-card-left {
	display: flex;
	align-items: center;
}
.med-icon {
	font-size: 1.8em;
	margin-right: 12px;
}
.med-name {
	font-size: 0.95em;
	font-weight: bold;
	color: #fff;
	display: block;
}
.med-duration {
	font-size: 0.78em;
	color: rgba(255,255,255,0.85);
	margin-top: 2px;
	display: block;
}
.med-play-btn {
	width: 36px;
	height: 36px;
	border-radius: 50%;
	background: rgba(255,255,255,0.35);
	display: flex;
	align-items: center;
	justify-content: center;
	color: #fff;
	font-size: 0.9em;
}

/* ===== 日记 ===== */
.diary-page {
	height: 100%;
	position: relative;
}
.diary-header {
	display: flex;
	flex-direction: column;
	padding: 16px 16px 8px;
}
.diary-today-label {
	font-size: 0.8em;
	color: #96e6a1;
	font-weight: bold;
	letter-spacing: 1px;
}
.diary-today-date {
	font-size: 1.15em;
	font-weight: bold;
	color: #4a4c50;
	margin-top: 2px;
}
.diary-write-card {
	background: #fff;
	border-radius: 16px;
	margin: 0 12px 12px;
	padding: 14px 16px;
	box-shadow: 0 3px 12px rgba(150, 230, 161, 0.25);
	border: 1.5px solid rgba(150, 230, 161, 0.3);
}
.diary-mood-row {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 10px;
}
.diary-mood-label {
	font-size: 0.9em;
	font-weight: bold;
	color: #4a4c50;
}
.mood-selector {
	display: flex;
	gap: 6px;
}
.mood-emoji {
	font-size: 1.5em;
	opacity: 0.45;
	transition: all 0.2s;
}
.mood-selected {
	opacity: 1;
	transform: scale(1.2);
}
.diary-placeholder {
	min-height: 50px;
	border-radius: 10px;
	background: #f8fbf8;
	padding: 10px 12px;
}
.diary-placeholder-text {
	font-size: 0.88em;
	color: #b0bec5;
	line-height: 1.6;
}
.diary-list {
	padding: 0 12px 80px;
}
.diary-entry {
	display: flex;
	background: #fff;
	border-radius: 14px;
	padding: 12px 14px;
	margin-bottom: 10px;
	box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.diary-entry-left {
	margin-right: 12px;
	display: flex;
	align-items: flex-start;
	padding-top: 2px;
}
.diary-entry-emoji {
	font-size: 1.8em;
}
.diary-entry-body {
	flex: 1;
}
.diary-entry-top {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 4px;
}
.diary-entry-date {
	font-size: 0.78em;
	color: #aaa;
}
.diary-entry-mood-name {
	font-size: 0.78em;
	color: #96e6a1;
	font-weight: bold;
}
.diary-entry-content {
	font-size: 0.88em;
	color: #555;
	line-height: 1.6;
}
.fab-btn {
	position: fixed;
	right: 22px;
	bottom: 120px;
	width: 52px;
	height: 52px;
	border-radius: 50%;
	background: linear-gradient(135deg, #96e6a1, #cef475);
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 4px 16px rgba(150, 230, 161, 0.5);
	z-index: 100;
}
.fab-icon {
	font-size: 1.4em;
}

/* ===== 弹窗 ===== */
.modal-mask {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0,0,0,0.45);
	display: flex;
	align-items: flex-end;
	z-index: 999;
}
.modal-box {
	width: 100%;
	background: #fff;
	border-radius: 24px 24px 0 0;
	padding: 20px 16px 32px;
}
.modal-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 14px;
}
.modal-title {
	font-size: 1.1em;
	font-weight: bold;
	color: #333;
}
.modal-close {
	font-size: 1.1em;
	color: #aaa;
	padding: 4px 8px;
}
.modal-mood-row {
	display: flex;
	justify-content: center;
	gap: 14px;
	margin-bottom: 14px;
}
.modal-mood {
	font-size: 1.8em;
}
.modal-textarea {
	width: 100%;
	height: 130px;
	background: #f8fbf8;
	border-radius: 12px;
	padding: 12px;
	font-size: 0.9em;
	color: #333;
	border: 1px solid #e8efe8;
	line-height: 1.6;
}
.modal-footer {
	display: flex;
	gap: 12px;
	margin-top: 16px;
}
.modal-cancel-btn {
	flex: 1;
	text-align: center;
	padding: 12px;
	border-radius: 24px;
	background: #f0f4ff;
	color: #888;
	font-size: 0.95em;
}
.modal-save-btn {
	flex: 1;
	text-align: center;
	padding: 12px;
	border-radius: 24px;
	background: linear-gradient(135deg, #96e6a1, #5bc87a);
	color: #fff;
	font-size: 0.95em;
	font-weight: bold;
	box-shadow: 0 3px 10px rgba(91,200,122,0.4);
}
</style>
