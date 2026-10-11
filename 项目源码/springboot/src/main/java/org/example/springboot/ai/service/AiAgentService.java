package org.example.springboot.ai.service;

import org.example.springboot.ai.tool.FreshTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class AiAgentService {

    private final ChatClient chatClient;

    public AiAgentService(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = builder
                .defaultSystem("你是生鲜平台销售系统的智能客服助手，可以帮助用户查询生鲜商品、库存、订单信息。" +
                        "回答要简洁、友好，涉及查询时请主动调用工具获取真实数据。")
                .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks()) // 使用回调
                .build();
    }

    public String chat(String userMessage) {
        return chatClient.prompt().user(userMessage).call().content();
    }
}