package com.springai.test_openai.controller;

import com.springai.test_openai.dto.Movie;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OutputConverterController {

    private final ChatClient chatClient;


    public OutputConverterController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/movies/artist/{actor}")
    String getMoviesByActor(@PathVariable String actor) {
        String message = """
                Provide a list of 5 popular movies for actor {actor}.
                If you don't know any movie for this actor, just say "I don't know any movie for this actor."
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("actor", actor));
        return this.chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/ai/movies/artist/{actor}/list")
    List<String> getMoviesByActorList(@PathVariable String actor) {
        String message = """
                Provide a list of 5 popular movies for actor {actor}.
                If you don't know any movie for this actor, just say "I don't know any movie for this actor.
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        ListOutputConverter listOutputConverter = new ListOutputConverter();
        Prompt prompt = promptTemplate.create(Map.of("actor", actor, "format", listOutputConverter.getFormat()));
        String response = this.chatClient.prompt(prompt).call().content();
        return listOutputConverter.convert(response);
    }

    @GetMapping("/ai/movies/artist/{actor}/map")
    Map<String, Object> getMoviesByActorMap(@PathVariable String actor) {
        String message = """
                Provide a list of 5 popular movies for actor {actor} with their year of release and co actor.Include the actor name as key and rest information as object.
                If you don't know any movie for this actor, just say "I don't know any movie for this actor.
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        MapOutputConverter mapOutputConverter = new MapOutputConverter();
        Prompt prompt = promptTemplate.create(Map.of("actor", actor, "format", mapOutputConverter.getFormat()));
        String response = this.chatClient.prompt(prompt).call().content();
        return mapOutputConverter.convert(response);
    }

    @GetMapping("/ai/movies/artist/{actor}/object")
    Movie getMoviesByActorObject(@PathVariable String actor) {
        String message = """
                Provide a list of 5 popular movies for actor {actor} .
                If you don't know any movie for this actor, just say "I don't know any movie for this actor.
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        BeanOutputConverter<Movie> movieBeanOutputConverter = new BeanOutputConverter<>(Movie.class);
        Prompt prompt = promptTemplate.create(Map.of("actor", actor, "format", movieBeanOutputConverter.getFormat()));
        String response = this.chatClient.prompt(prompt).call().content();
        return movieBeanOutputConverter.convert(response);
    }

}
