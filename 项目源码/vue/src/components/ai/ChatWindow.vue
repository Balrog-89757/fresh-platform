<template>
  <div class="chat-window" :class="[`mode-${mode}`]">
    <!-- 消息区 -->
    <div ref="scrollBody" class="chat-body">
      <div class="chat-body-inner">
        <div
          v-for="(item, index) in messages"
          :key="index"
          class="msg-row"
          :class="item.role === 'user' ? 'is-user' : 'is-ai'"
        >
          <div class="avatar" :class="item.role === 'user' ? 'avatar-user' : 'avatar-ai'">
            <i :class="item.role === 'user' ? 'el-icon-user-solid' : 'el-icon-service'"></i>
          </div>

          <div class="msg-main">
            <div class="msg-bubble" :class="{ 'is-error': item.error }">
              <!-- 用户消息：纯文本 -->
              <div v-if="item.role === 'user'" class="msg-plain">{{ item.content }}</div>
              <!-- AI 消息：轻量 Markdown 渲染 -->
              <div v-else class="msg-rich" v-html="renderRich(item.content)"></div>
            </div>
            <div class="msg-time">{{ item.time }}</div>
          </div>
        </div>

        <!-- 思考中 -->
        <div v-if="loading" class="msg-row is-ai">
          <div class="avatar avatar-ai"><i class="el-icon-service"></i></div>
          <div class="msg-main">
            <div class="msg-bubble thinking">
              <span class="dot"></span>
              <span class="dot"></span>
              <span class="dot"></span>
              <span class="thinking-text">AI 正在为您查询…</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 快捷提问 -->
    <div v-if="quickQuestions.length" class="chat-quick">
      <span class="quick-label">试试这样问：</span>
      <div class="quick-list">
        <span
          v-for="(q, i) in quickQuestions"
          :key="i"
          class="quick-item"
          :class="{ disabled: loading }"
          @click="handleQuickAsk(q)"
        >{{ q }}</span>
      </div>
    </div>

    <!-- 输入区 -->
    <div class="chat-footer">
      <div class="input-wrap">
        <textarea
          ref="input"
          v-model="draft"
          class="chat-input"
          :placeholder="placeholder"
          :disabled="loading"
          rows="1"
          @keydown.enter.exact.prevent="handleSend"
          @input="autoGrow"
        ></textarea>
        <el-button
          class="send-btn"
          type="primary"
          :loading="loading"
          :disabled="!canSend"
          @click="handleSend"
        >
          <span v-if="!loading">发送</span>
        </el-button>
      </div>
      <div class="input-tip">
        <span>Enter 发送，Shift + Enter 换行</span>
        <span
          v-if="messages.length > 1 && !loading"
          class="clear-btn"
          @click="handleClear"
        >清空对话</span>
      </div>
    </div>
  </div>
</template>

<script>
import { aiChat, parseAiReply, extractErrorMessage } from '@/api/ai'

const DEFAULT_GREETING = '您好！我是农产品销售平台的 AI 智能客服 🌿\n' +
  '我可以帮您：\n' +
  '- 查询商品的**价格、库存、产地、保质期**，例如「苹果还有多少库存」\n' +
  '- 按分类浏览商品：1-蔬菜 2-水果 3-肉禽蛋 4-海鲜 5-乳品\n' +
  '- 查看库存预警（低于指定数量的商品）\n' +
  '- 解答下单、配送、售后等常见问题\n\n' +
  '请问有什么可以帮您？'

const DEFAULT_QUICK = [
  '苹果还有多少库存？',
  '水果分类下有哪些商品？',
  '蔬菜类有哪些商品？',
  '库存低于50的商品有哪些？'
]

export default {
  name: 'ChatWindow',
  props: {
    // page：独立页面（消息区自适应高度）；panel：浮窗内嵌（占满父容器）
    mode: {
      type: String,
      default: 'panel'
    },
    placeholder: {
      type: String,
      default: '请输入您的问题，例如：苹果还有多少库存？'
    },
    quickQuestions: {
      type: Array,
      default: () => DEFAULT_QUICK
    },
    greeting: {
      type: String,
      default: DEFAULT_GREETING
    }
  },
  data() {
    return {
      draft: '',
      loading: false,
      messages: []
    }
  },
  computed: {
    canSend() {
      return !this.loading && this.draft.trim().length > 0
    }
  },
  created() {
    this.resetMessages()
  },
  methods: {
    resetMessages() {
      this.messages = [
        {
          role: 'assistant',
          content: this.greeting,
          time: this.formatTime()
        }
      ]
    },
    formatTime(date = new Date()) {
      const pad = n => (n < 10 ? `0${n}` : `${n}`)
      return `${pad(date.getHours())}:${pad(date.getMinutes())}`
    },
    // 把 AI 返回的纯文本做安全转义 + 轻量 Markdown 渲染
    renderRich(text) {
      if (!text) return ''
      let safe = String(text)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;')
      // 加粗
      safe = safe.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
      // 按行处理无序列表与段落
      const lines = safe.split(/\n/)
      const out = []
      let inList = false
      lines.forEach(line => {
        const trimmed = line.trim()
        if (/^[-*]\s+/.test(trimmed)) {
          if (!inList) {
            out.push('<ul class="md-list">')
            inList = true
          }
          out.push(`<li>${trimmed.replace(/^[-*]\s+/, '')}</li>`)
        } else {
          if (inList) {
            out.push('</ul>')
            inList = false
          }
          out.push(trimmed ? `<p class="md-p">${trimmed}</p>` : '<div class="md-gap"></div>')
        }
      })
      if (inList) out.push('</ul>')
      return out.join('')
    },
    autoGrow() {
      const el = this.$refs.input
      if (!el) return
      el.style.height = 'auto'
      el.style.height = `${Math.min(el.scrollHeight, 120)}px`
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const el = this.$refs.scrollBody
        if (el) {
          el.scrollTop = el.scrollHeight
        }
      })
    },
    handleQuickAsk(question) {
      if (this.loading) return
      this.draft = question
      this.handleSend()
    },
    handleClear() {
      this.resetMessages()
      this.$message.success('已清空对话')
    },
    handleSend() {
      if (!this.canSend) return
      const text = this.draft.trim()
      this.draft = ''
      this.autoGrow()
      this.messages.push({
        role: 'user',
        content: text,
        time: this.formatTime()
      })
      this.scrollToBottom()
      this.ask(text)
    },
    async ask(text) {
      this.loading = true
      this.scrollToBottom()
      try {
        const res = await aiChat(text)
        const { ok, text: reply } = parseAiReply(res)
        this.messages.push({
          role: 'assistant',
          content: reply,
          time: this.formatTime(),
          error: !ok
        })
      } catch (error) {
        console.error('AI 客服请求失败:', error)
        this.messages.push({
          role: 'assistant',
          content: extractErrorMessage(error),
          time: this.formatTime(),
          error: true
        })
      } finally {
        this.loading = false
        this.scrollToBottom()
        this.$emit('replied')
      }
    }
  }
}
</script>

<style scoped>
.chat-window {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  background: #fff;
}

/* ---------- 消息区 ---------- */
.chat-body {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: 20px 22px;
  background:
    radial-gradient(circle at 0% 0%, rgba(103, 194, 58, 0.06) 0%, transparent 45%),
    radial-gradient(circle at 100% 100%, rgba(103, 194, 58, 0.06) 0%, transparent 45%),
    #f8faf5;
}

.chat-body-inner {
  max-width: 900px;
  margin: 0 auto;
}

.msg-row {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  animation: msgIn 0.28s ease;
}

.msg-row.is-user {
  flex-direction: row-reverse;
}

@keyframes msgIn {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.avatar {
  flex: 0 0 38px;
  width: 38px;
  height: 38px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 19px;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.08);
}

.avatar-ai {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.avatar-user {
  background: linear-gradient(135deg, #409eff, #79bbff);
}

.msg-main {
  max-width: 78%;
  display: flex;
  flex-direction: column;
}

.is-user .msg-main {
  align-items: flex-end;
}

.msg-bubble {
  padding: 11px 15px;
  border-radius: 12px;
  font-size: 14px;
  line-height: 1.7;
  word-break: break-word;
  white-space: pre-wrap;
}

.is-ai .msg-bubble {
  background: #fff;
  color: #303133;
  border: 1px solid #ebeef5;
  border-top-left-radius: 3px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.03);
}

.is-user .msg-bubble {
  background: linear-gradient(135deg, #67c23a, #85ce61);
  color: #fff;
  border-top-right-radius: 3px;
  box-shadow: 0 3px 10px rgba(103, 194, 58, 0.22);
}

.msg-bubble.is-error {
  background: #fef0f0;
  border-color: #fde2e2;
  color: #f56c6c;
}

.msg-plain {
  white-space: pre-wrap;
}

.msg-rich ::v-deep .md-p {
  margin: 0 0 4px;
}

.msg-rich ::v-deep .md-p:last-child {
  margin-bottom: 0;
}

.msg-rich ::v-deep .md-list {
  margin: 4px 0;
  padding-left: 18px;
}

.msg-rich ::v-deep .md-list li {
  margin: 2px 0;
}

.msg-rich ::v-deep .md-gap {
  height: 6px;
}

.msg-time {
  margin-top: 5px;
  font-size: 12px;
  color: #c0c4cc;
}

/* ---------- 思考中 ---------- */
.thinking {
  display: flex;
  align-items: center;
  gap: 5px;
}

.thinking .dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #67c23a;
  animation: blink 1.2s infinite ease-in-out;
}

.thinking .dot:nth-child(2) {
  animation-delay: 0.2s;
}

.thinking .dot:nth-child(3) {
  animation-delay: 0.4s;
}

.thinking-text {
  margin-left: 6px;
  font-size: 13px;
  color: #909399;
}

@keyframes blink {
  0%, 80%, 100% {
    opacity: 0.25;
    transform: scale(0.8);
  }
  40% {
    opacity: 1;
    transform: scale(1);
  }
}

/* ---------- 快捷提问 ---------- */
.chat-quick {
  padding: 10px 22px 4px;
  background: #fff;
  border-top: 1px solid #f2f6fc;
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.quick-label {
  font-size: 13px;
  color: #909399;
  flex: 0 0 auto;
}

.quick-list {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.quick-item {
  padding: 5px 13px;
  border-radius: 15px;
  font-size: 13px;
  color: #67c23a;
  background: rgba(103, 194, 58, 0.08);
  border: 1px solid rgba(103, 194, 58, 0.2);
  cursor: pointer;
  transition: all 0.25s ease;
}

.quick-item:hover {
  background: rgba(103, 194, 58, 0.16);
  transform: translateY(-1px);
}

.quick-item.disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

/* ---------- 输入区 ---------- */
.chat-footer {
  padding: 12px 22px 14px;
  background: #fff;
  border-top: 1px solid #ebeef5;
}

.input-wrap {
  display: flex;
  align-items: flex-end;
  gap: 10px;
}

.chat-input {
  flex: 1;
  resize: none;
  border: 1px solid #dcdfe6;
  border-radius: 10px;
  padding: 10px 14px;
  font-size: 14px;
  line-height: 1.6;
  font-family: inherit;
  color: #303133;
  outline: none;
  max-height: 120px;
  min-height: 42px;
  transition: all 0.25s ease;
  box-sizing: border-box;
}

.chat-input:focus {
  border-color: #67c23a;
  box-shadow: 0 0 0 3px rgba(103, 194, 58, 0.12);
}

.chat-input:disabled {
  background: #f5f7fa;
  cursor: not-allowed;
}

.send-btn {
  height: 42px;
  min-width: 82px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, #67c23a, #85ce61);
  box-shadow: 0 4px 12px rgba(103, 194, 58, 0.24);
}

.send-btn:hover,
.send-btn:focus {
  background: linear-gradient(135deg, #5ab82f, #79c152);
}

.send-btn.is-disabled,
.send-btn.is-disabled:hover {
  background: #c8e6a8;
  box-shadow: none;
}

.input-tip {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 6px;
  font-size: 12px;
  color: #c0c4cc;
  min-height: 16px;
}

.clear-btn {
  cursor: pointer;
  transition: color 0.2s ease;
}

.clear-btn:hover {
  color: #67c23a;
}

/* ---------- 浮窗模式适配 ---------- */
.mode-panel .chat-body {
  padding: 14px 14px 10px;
}

.mode-panel .chat-quick {
  padding: 8px 14px 2px;
}

.mode-panel .chat-footer {
  padding: 10px 14px 12px;
}

.mode-panel .msg-main {
  max-width: 84%;
}

.mode-panel .msg-bubble {
  font-size: 13px;
  padding: 9px 12px;
}

.mode-panel .input-tip {
  font-size: 11px;
}

/* ---------- 页面模式适配 ---------- */
.mode-page .chat-body {
  padding: 26px 30px;
}

.mode-page .msg-bubble {
  font-size: 15px;
}

.mode-page .avatar {
  flex: 0 0 42px;
  width: 42px;
  height: 42px;
  font-size: 21px;
}

@media (max-width: 768px) {
  .chat-body {
    padding: 14px;
  }

  .chat-quick {
    padding: 8px 14px 2px;
  }

  .chat-footer {
    padding: 10px 14px 12px;
  }

  .msg-main {
    max-width: 85%;
  }

  .quick-label {
    display: none;
  }
}
</style>
