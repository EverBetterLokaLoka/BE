package com.example.lokaloka.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class GeminiRequest {
    private List<Content> contents;
    private GenerationConfig generationConfig;

    @Data
    public static class Content {
        private String role;
        private List<Part> parts;
    }

    @Data
    public static class Part {
        private String text;
    }

    @Data
    public static class GenerationConfig {
        private double temperature;
        private double topP;
        private int topK;
        private int maxOutputTokens;
        private String responseMimeType;
        private Map<String, Object> responseSchema;
    }
}