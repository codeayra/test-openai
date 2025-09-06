package com.springai.test_openai.controller;

import com.springai.test_openai.dto.Filmography;
import com.springai.test_openai.dto.Movie;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OutputFluentController {

    private final ChatClient chatClient;


    public OutputFluentController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/movies-string")
    String getMoviesByActorString() {
        String message = """
                Generate a filmography for actor Tom Hanks.
                If you don't know any movie for this actor, just say "I don't know any movie for this actor."
                """;
        return chatClient.prompt().user(message).call().content();
    }

    @GetMapping("/ai/movies")
    Filmography getMovies() {
        String message = """
                Generate a filmography for actor Tom Hanks.
                If you don't know any movie for this actor, just say "I don't know any movie for this actor."
                """;
        return chatClient.prompt().user(message).call().entity(Filmography.class);
    }

    @GetMapping("/ai/movies-list")
    List<Filmography> getMoviesByActors() {
        String message = """
                Generate a filmography for actor Tom Hanks and actor Leonardo DiCaprio and actor Robert De Niro.
                If you don't know any movie for this actor, just say "I don't know any movie for this actor."
                """;
        return chatClient.prompt().user(message).call().entity(new ParameterizedTypeReference<List<Filmography>>() {
        });
    }

    @GetMapping("/ai/movies-by-actor")
    Filmography getMoviesByActors(@RequestParam String actor) {
        String message = """
                Generate a filmography for actor {actor}.
                If you don't know any movie for this actor, just say "I don't know any movie for this actor."
                """;
        return chatClient.prompt().user(u -> u.text(message).param("actor", actor))
                .call().entity(Filmography.class);
    }

}
