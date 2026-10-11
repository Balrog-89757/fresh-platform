import request from '@/utils/request'


export function aiChat(message, config = {}) {
  return request({
    url: '/ai/chat',
    method: 'get',
    params: { message },
    timeout: 120000,
    ...config
  })
}

/**
 * 统一解析 AI 回复，返回 { text, ok }
 */
export function parseAiReply(res) {
  // 纯文本（当前后端实现）
  if (typeof res === 'string') {
    return { ok: true, text: res }
  }

  // 兼容 Result 包装
  if (res && typeof res === 'object') {
    if (res.code !== undefined && res.code !== null && res.code !== '0' && res.code !== 0) {
      return { ok: false, text: res.msg || 'AI 服务返回异常' }
    }
    const data = res.data !== undefined ? res.data : res
    if (typeof data === 'string') {
      return { ok: true, text: data }
    }
    if (data && typeof data === 'object' && typeof data.content === 'string') {
      return { ok: true, text: data.content }
    }
  }

  return { ok: false, text: '抱歉，AI 客服暂时无法回答，请稍后再试。' }
}

/**
 * 提取错误信息，友好展示给用户
 */
export function extractErrorMessage(error) {
  if (!error) {
    return '网络异常，请稍后重试'
  }
  if (error.response) {
    const { status, data } = error.response
    if (typeof data === 'string' && data.trim() && !data.trim().startsWith('<')) {
      return data
    }
    if (status === 401) return '登录已失效，请重新登录后再试'
    if (status === 404) return 'AI 客服接口不存在，请确认后端服务已启动'
    if (status >= 500) return 'AI 服务繁忙，请稍后再试'
    return `请求失败（${status}）`
  }
  if (error.code === 'ECONNABORTED') {
    return 'AI 思考时间过长，请稍后重试或换个问题'
  }
  return error.message || '网络异常，请稍后重试'
}

export default { aiChat, parseAiReply, extractErrorMessage }
