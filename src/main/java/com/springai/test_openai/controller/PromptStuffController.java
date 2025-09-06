package com.springai.test_openai.controller;

import com.springai.test_openai.dto.Filmography;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.List;

@RestController
public class PromptStuffController {

    private final ChatClient chatClient;

    @Value("classpath:prompts/temprature.st")
    private Resource tempratureTemplate;

    @Value("classpath:prompts/temprature-example.txt")
    private Resource tempratureExample;


    public PromptStuffController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/{city}/temprature")
    String getTemprature(@PathVariable String city,
                         @RequestParam(value = "stuff", defaultValue = "false") boolean isStuff)
            throws Exception {
        String sample = tempratureExample.getContentAsString(Charset.defaultCharset());
        return chatClient.prompt().user(u -> {
            u.text(tempratureTemplate);
            u.param("city", city);
            u.param("context", isStuff ? sample : "");
        }).call().content();
    }


}
