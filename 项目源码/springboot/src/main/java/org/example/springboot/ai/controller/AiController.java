package org.example.springboot.ai.controller;

import org.example.springboot.ai.service.AiAgentService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private AiAgentService aiAgentService;

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return aiAgentService.chat(message);
    }
}