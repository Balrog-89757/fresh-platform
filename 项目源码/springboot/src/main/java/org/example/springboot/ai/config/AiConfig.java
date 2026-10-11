package org.example.springboot.ai.config;

import org.example.springboot.ai.tool.FreshTool;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(FreshTool freshTool) {
        // 1. 获取代理背后的目标实例（原始 FreshTool 对象）
        Object targetInstance = AopProxyUtils.getSingletonTarget(freshTool);
        // 2. 如果目标实例存在就用它，否则用原始 Bean
        Object toolObject = targetInstance != null ? targetInstance : freshTool;
        // 3. 传入实例对象，而非 Class 对象
        return MethodToolCallbackProvider.builder()
                .toolObjects(toolObject)
                .build();
    }
}