package com.springai.test_openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PromptController {

    private final ChatClient chatClient;

    @Value("classpath:prompts/books.st")
    private Resource bookListResource;

    public PromptController(ChatClient.Builder chatClientBuilder) {
        String sysMessage = """
                 You are a helpful assistant that provides book recommendations based on genre." +
                "If anyone asks any other questions, respond with 'I can only provide book recommendations based on genre." +
                """;
        chatClientBuilder.defaultSystem(sysMessage);
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/prompt")
    String generation(@RequestParam("message") String userInput) {
        Prompt prompt = new Prompt(userInput);
        return this.chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/ai/books/popular")
    String getPopularBooks(@RequestParam(value = "genre", defaultValue = "science fiction") String genre) {
        String message = """
                Provide a list of 5 popular books in the {genre} genre, formatted as a numbered list with each book title on a new line.
                If you don't know any books in that genre, just say "I don't know any books in that genre."
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre));
        return this.chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/ai/test/system-prompt")
    String useSystemPrompts(@RequestParam String message) {
        String sysMessage = """
                 You are a helpful assistant that provides book recommendations based on genre." +
                "If anyone asks any other questions, respond with 'I can only provide book recommendations based on genre." +
                """;
        var systemMessage = new SystemMessage(sysMessage);
        var userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        return this.chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/ai/books/popular/prompt-template")
    String getPopularBooksFromTemplate(@RequestParam(value = "genre", defaultValue = "science fiction") String genre) {
        PromptTemplate promptTemplate = new PromptTemplate(bookListResource);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre));
        return this.chatClient.prompt(prompt).call().content();
    }
}
