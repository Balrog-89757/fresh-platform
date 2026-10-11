<template>
  <div v-if="visible" class="floating-chat">
    <!-- 聊天面板 -->
    <transition name="panel-pop">
      <div v-show="open" class="fc-panel">
        <div class="fc-panel-header">
          <div class="fc-ph-left">
            <div class="fc-ph-avatar">
              <i class="el-icon-service"></i>
            </div>
            <div class="fc-ph-text">
              <div class="fc-ph-title">AI 智能客服</div>
              <div class="fc-ph-sub">
                <span class="fc-dot"></span>
                在线为您服务
              </div>
            </div>
          </div>
          <div class="fc-ph-actions">
            <i class="el-icon-full-screen" title="全屏页面" @click="goToPage"></i>
            <i class="el-icon-close" title="收起" @click="open = false"></i>
          </div>
        </div>

        <div class="fc-panel-body">
          <chat-window
            mode="panel"
            :quick-questions="quickQuestions"
            :greeting="panelGreeting"
          ></chat-window>
        </div>
      </div>
    </transition>

    <!-- 悬浮按钮 -->
    <div class="fc-button" :class="{ 'is-open': open }" @click="toggle">
      <i :class="open ? 'el-icon-arrow-down' : 'el-icon-service'"></i>
      <span class="fc-btn-text">{{ open ? '收起' : '智能客服' }}</span>
      <span v-if="!open && hasNew" class="fc-badge"></span>
    </div>
  </div>
</template>

<script>
import ChatWindow from '@/components/ai/ChatWindow.vue'

// 仅在商城前台展示浮窗，后台管理页面不展示
const FRONT_ONLY_ROUTES = [
  'Home',
  'Products',
  'ProductDetail',
  'Cart',
  'Favorite',
  'Order',
  'UserCenter',
  'category',
  'Articles',
  'ArticleDetail',
  'Search',
  'NoticeList'
]

export default {
  name: 'FloatingChat',
  components: {
    ChatWindow
  },
  data() {
    return {
      open: false,
      hasNew: true,
      path: '',
      quickQuestions: ['苹果还有多少库存？', '水果分类下有哪些商品？', '库存低于50的商品有哪些？'],
      panelGreeting: '您好！我是 AI 智能客服 🌿\n' +
        '可以帮您查询商品的**价格、库存**，也可以解答下单、配送等问题。\n\n' +
        '请直接说出您的问题，或点击下方示例提问～'
    }
  },
  computed: {
    visible() {
      const name = this.$route.name
      if (!name) return false
      return FRONT_ONLY_ROUTES.indexOf(name) !== -1
    }
  },
  watch: {
    // 切换路由时自动收起面板，避免遮挡新页面
    $route() {
      this.open = false
    }
  },
  mounted() {
    if (localStorage.getItem('aiChatVisited') === '1') {
      this.hasNew = false
    }
  },
  methods: {
    toggle() {
      this.open = !this.open
      if (this.open) {
        this.hasNew = false
        localStorage.setItem('aiChatVisited', '1')
      }
    },
    goToPage() {
      this.open = false
      if (this.$route.path !== '/ai-service') {
        this.$router.push('/ai-service')
      }
    }
  }
}
</script>

<style scoped>
.floating-chat {
  position: fixed;
  right: 26px;
  bottom: 26px;
  z-index: 2200;
  font-family: inherit;
}

/* ---------- 悬浮按钮 ---------- */
.fc-button {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 54px;
  padding: 0 22px;
  border-radius: 27px;
  cursor: pointer;
  color: #fff;
  font-size: 15px;
  font-weight: 500;
  background: linear-gradient(135deg, #67c23a, #85ce61);
  box-shadow: 0 8px 22px rgba(103, 194, 58, 0.4);
  transition: all 0.3s ease;
  user-select: none;
  overflow: hidden;
}

.fc-button::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.28);
  transform: translate(-50%, -50%);
  transition: width 0.5s ease, height 0.5s ease;
}

.fc-button:hover::before {
  width: 200px;
  height: 200px;
}

.fc-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 28px rgba(103, 194, 58, 0.5);
}

.fc-button i,
.fc-btn-text {
  position: relative;
  z-index: 1;
}

.fc-button i {
  font-size: 21px;
}

.fc-button.is-open {
  background: linear-gradient(135deg, #909399, #b1b3b8);
  box-shadow: 0 8px 22px rgba(144, 147, 153, 0.35);
}

.fc-badge {
  position: absolute;
  top: 8px;
  right: 12px;
  width: 9px;
  height: 9px;
  border-radius: 50%;
  background: #f56c6c;
  box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.85);
  animation: badgePulse 1.6s infinite;
}

@keyframes badgePulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.25);
  }
}

/* ---------- 面板 ---------- */
.fc-panel {
  position: absolute;
  right: 0;
  bottom: 68px;
  width: 400px;
  height: 580px;
  max-height: calc(100vh - 140px);
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-shadow: 0 18px 50px rgba(0, 0, 0, 0.18);
  border: 1px solid #eef2e8;
}

.fc-panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 13px 16px;
  background: linear-gradient(135deg, #67c23a, #85ce61);
  color: #fff;
  flex: 0 0 auto;
}

.fc-ph-left {
  display: flex;
  align-items: center;
  gap: 11px;
}

.fc-ph-avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.24);
  border: 1.5px solid rgba(255, 255, 255, 0.55);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.fc-ph-title {
  font-size: 15px;
  font-weight: 600;
  line-height: 1.3;
}

.fc-ph-sub {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.88);
  margin-top: 2px;
}

.fc-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #fff;
  animation: badgePulse 1.6s infinite;
}

.fc-ph-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 17px;
}

.fc-ph-actions i {
  cursor: pointer;
  opacity: 0.9;
  transition: all 0.22s ease;
}

.fc-ph-actions i:hover {
  opacity: 1;
  transform: scale(1.14);
}

.fc-panel-body {
  flex: 1;
  min-height: 0;
}

/* ---------- 面板动画 ---------- */
.panel-pop-enter-active,
.panel-pop-leave-active {
  transition: opacity 0.26s ease, transform 0.26s ease;
  transform-origin: bottom right;
}

.panel-pop-enter,
.panel-pop-leave-to {
  opacity: 0;
  transform: translateY(14px) scale(0.96);
}

@media (max-width: 480px) {
  .floating-chat {
    right: 14px;
    bottom: 14px;
  }

  .fc-panel {
    width: calc(100vw - 28px);
    height: calc(100vh - 150px);
  }

  .fc-btn-text {
    display: none;
  }

  .fc-button {
    width: 54px;
    padding: 0;
  }
}
</style>
