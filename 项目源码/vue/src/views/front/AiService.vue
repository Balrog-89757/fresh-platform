<template>
  <div class="ai-service-page">
    <front-header></front-header>

    <div class="page-main">
      <!-- 顶部横幅 -->
      <div class="ai-hero">
        <div class="hero-left">
          <div class="hero-icon">
            <i class="el-icon-service"></i>
          </div>
          <div class="hero-text">
            <h1 class="hero-title">
              AI 智能客服
              <span class="hero-tag">智能体 · 实时查询</span>
            </h1>
            <p class="hero-desc">
              基于大模型的智能客服助手，可直接查询平台商品的<b>价格、库存、产地</b>等信息，帮您快速选到新鲜好物
            </p>
            <div class="hero-points">
              <span class="point"><i class="el-icon-check"></i> 商品库存实时查询</span>
              <span class="point"><i class="el-icon-check"></i> 分类商品一键浏览</span>
              <span class="point"><i class="el-icon-check"></i> 下单配送问题解答</span>
            </div>
          </div>
        </div>
        <div class="hero-right">
          <div class="hero-bubble bubble-1">苹果还有多少库存？</div>
          <div class="hero-bubble bubble-2">水果分类下有哪些商品？</div>
          <div class="hero-bubble bubble-3"><i class="el-icon-service"></i> 正在为您查询…</div>
        </div>
      </div>

      <!-- 主体：聊天区 + 侧边说明 -->
      <div class="ai-body">
        <div class="chat-card">
          <div class="chat-card-header">
            <div class="cc-left">
              <span class="online-dot"></span>
              <span class="cc-title">在线客服</span>
              <span class="cc-sub">AI 助手 · 全天候在线</span>
            </div>
            <div class="cc-right">
              <i class="el-icon-chat-dot-round"></i>
              <span>多轮对话，可连续追问</span>
            </div>
          </div>
          <div class="chat-card-body">
            <chat-window mode="page"></chat-window>
          </div>
        </div>

        <div class="side-col">
          <div class="side-card">
            <div class="side-title">
              <i class="el-icon-magic-stick"></i>
              <span>我能做什么</span>
            </div>
            <ul class="cap-list">
              <li v-for="(cap, i) in capabilities" :key="i" class="cap-item">
                <div class="cap-icon"><i :class="cap.icon"></i></div>
                <div class="cap-content">
                  <div class="cap-name">{{ cap.name }}</div>
                  <div class="cap-desc">{{ cap.desc }}</div>
                </div>
              </li>
            </ul>
          </div>

          <div class="side-card">
            <div class="side-title">
              <i class="el-icon-guide"></i>
              <span>使用指南</span>
            </div>
            <ol class="guide-list">
              <li>在下方输入框描述您想了解的商品或问题</li>
              <li>也可以点击「试试这样问」中的示例快速体验</li>
              <li>AI 会自动查询平台数据并给出答案，可继续追问</li>
              <li>如需人工服务，请拨打客服专线 400-123-4567</li>
            </ol>
          </div>

          <div class="side-card tip-card">
            <div class="side-title">
              <i class="el-icon-warning-outline"></i>
              <span>温馨提示</span>
            </div>
            <p class="tip-text">
              AI 的回答基于平台实时数据生成，库存与价格请以商品详情页和最终下单结果为准。
            </p>
          </div>
        </div>
      </div>
    </div>

    <front-footer></front-footer>
  </div>
</template>

<script>
import FrontHeader from '@/components/front/FrontHeader.vue'
import FrontFooter from '@/components/front/FrontFooter.vue'
import ChatWindow from '@/components/ai/ChatWindow.vue'

export default {
  name: 'AiService',
  components: {
    FrontHeader,
    FrontFooter,
    ChatWindow
  },
  data() {
    return {
      capabilities: [
        {
          icon: 'el-icon-search',
          name: '商品查询',
          desc: '按名称查询商品价格、库存等信息'
        },
        {
          icon: 'el-icon-s-goods',
          name: '分类导购',
          desc: '蔬菜 / 水果 / 肉禽蛋 / 海鲜 / 乳品'
        },
        {
          icon: 'el-icon-s-data',
          name: '库存预警',
          desc: '查询库存偏低的商品，随时掌握货源'
        },
        {
          icon: 'el-icon-headset',
          name: '常见问题',
          desc: '下单、支付、配送、售后问题解答'
        }
      ]
    }
  }
}
</script>

<style scoped>
.ai-service-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(to bottom, #ffffff, #f6faf3);
}

.page-main {
  flex: 1;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px 24px 48px;
  box-sizing: border-box;
}

/* ---------- 顶部横幅 ---------- */
.ai-hero {
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  padding: 28px 32px;
  border-radius: 20px;
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 55%, #a8dd8a 100%);
  box-shadow: 0 12px 32px rgba(103, 194, 58, 0.25);
  margin-bottom: 24px;
}

.ai-hero::after {
  content: '';
  position: absolute;
  inset: 0;
  background-image:
    radial-gradient(circle at 12% 20%, rgba(255, 255, 255, 0.28) 0%, transparent 42%),
    radial-gradient(circle at 88% 80%, rgba(255, 255, 255, 0.22) 0%, transparent 45%);
  pointer-events: none;
}

.hero-left {
  display: flex;
  align-items: center;
  gap: 20px;
  position: relative;
  z-index: 1;
}

.hero-icon {
  flex: 0 0 70px;
  width: 70px;
  height: 70px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.24);
  border: 2px solid rgba(255, 255, 255, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 34px;
  backdrop-filter: blur(6px);
}

.hero-title {
  margin: 0;
  color: #fff;
  font-size: 27px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.hero-tag {
  font-size: 12px;
  font-weight: 500;
  padding: 3px 11px;
  border-radius: 11px;
  background: rgba(255, 255, 255, 0.26);
  border: 1px solid rgba(255, 255, 255, 0.45);
}

.hero-desc {
  margin: 9px 0 0;
  color: rgba(255, 255, 255, 0.94);
  font-size: 14px;
  line-height: 1.7;
  max-width: 640px;
}

.hero-desc b {
  font-weight: 600;
  border-bottom: 1px dashed rgba(255, 255, 255, 0.7);
}

.hero-points {
  display: flex;
  gap: 18px;
  margin-top: 12px;
  flex-wrap: wrap;
}

.hero-points .point {
  color: #fff;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.hero-right {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: flex-end;
  min-width: 240px;
}

.hero-bubble {
  padding: 9px 15px;
  border-radius: 14px;
  font-size: 13px;
  color: #4a7a25;
  background: #fff;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  animation: floatY 3.2s ease-in-out infinite;
}

.bubble-1 {
  border-bottom-right-radius: 3px;
}

.bubble-2 {
  background: rgba(255, 255, 255, 0.86);
  animation-delay: 0.5s;
  border-bottom-right-radius: 3px;
}

.bubble-3 {
  background: #f0f9eb;
  color: #67c23a;
  display: flex;
  align-items: center;
  gap: 6px;
  animation-delay: 1s;
  border-bottom-right-radius: 3px;
}

@keyframes floatY {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

/* ---------- 主体布局 ---------- */
.ai-body {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 320px;
  gap: 22px;
  align-items: start;
}

.chat-card {
  background: #fff;
  border-radius: 18px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06);
  border: 1px solid #eef2e8;
  display: flex;
  flex-direction: column;
  height: 720px;
}

.chat-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  border-bottom: 1px solid #eef2e8;
  background: linear-gradient(to right, #f7fbf4, #ffffff);
}

.cc-left {
  display: flex;
  align-items: center;
  gap: 9px;
}

.online-dot {
  width: 9px;
  height: 9px;
  border-radius: 50%;
  background: #67c23a;
  box-shadow: 0 0 0 4px rgba(103, 194, 58, 0.16);
  animation: online 1.8s infinite;
}

@keyframes online {
  0%, 100% {
    box-shadow: 0 0 0 4px rgba(103, 194, 58, 0.16);
  }
  50% {
    box-shadow: 0 0 0 7px rgba(103, 194, 58, 0.08);
  }
}

.cc-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.cc-sub {
  font-size: 12px;
  color: #909399;
}

.cc-right {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #909399;
}

.chat-card-body {
  flex: 1;
  min-height: 0;
}

/* ---------- 侧边栏 ---------- */
.side-col {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.side-card {
  background: #fff;
  border-radius: 16px;
  padding: 18px;
  border: 1px solid #eef2e8;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.04);
}

.side-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 14px;
}

.side-title i {
  color: #67c23a;
  font-size: 17px;
}

.cap-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.cap-item {
  display: flex;
  align-items: flex-start;
  gap: 11px;
  padding: 10px;
  border-radius: 11px;
  transition: all 0.25s ease;
}

.cap-item:hover {
  background: rgba(103, 194, 58, 0.07);
  transform: translateX(3px);
}

.cap-icon {
  flex: 0 0 34px;
  width: 34px;
  height: 34px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #67c23a;
  font-size: 17px;
  background: rgba(103, 194, 58, 0.12);
}

.cap-name {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  line-height: 1.5;
}

.cap-desc {
  font-size: 12px;
  color: #909399;
  line-height: 1.6;
  margin-top: 2px;
}

.guide-list {
  margin: 0;
  padding-left: 18px;
  color: #606266;
  font-size: 13px;
  line-height: 1.9;
}

.guide-list li {
  margin-bottom: 6px;
}

.guide-list li::marker {
  color: #67c23a;
  font-weight: 600;
}

.tip-card {
  background: linear-gradient(135deg, #f0f9eb, #ffffff);
}

.tip-text {
  margin: 0;
  font-size: 13px;
  color: #7d8a72;
  line-height: 1.75;
}

/* ---------- 响应式 ---------- */
@media (max-width: 1200px) {
  .ai-body {
    grid-template-columns: 1fr;
  }

  .chat-card {
    height: 640px;
  }

  .side-col {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .tip-card {
    grid-column: 1 / -1;
  }
}

@media (max-width: 992px) {
  .hero-right {
    display: none;
  }

  .ai-hero {
    padding: 22px;
  }

  .hero-title {
    font-size: 22px;
  }
}

@media (max-width: 768px) {
  .page-main {
    padding: 16px 14px 32px;
  }

  .chat-card {
    height: 560px;
  }

  .side-col {
    grid-template-columns: 1fr;
  }

  .hero-icon {
    flex: 0 0 52px;
    width: 52px;
    height: 52px;
    font-size: 26px;
    border-radius: 15px;
  }

  .hero-left {
    gap: 14px;
  }
}
</style>
