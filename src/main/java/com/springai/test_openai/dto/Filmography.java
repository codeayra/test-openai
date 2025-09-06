package com.springai.test_openai.dto;

import java.util.List;

public record Filmography(String actor, List<String> films, List<String> televisions) {
}
