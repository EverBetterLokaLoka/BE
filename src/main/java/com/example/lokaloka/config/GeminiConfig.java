package com.example.lokaloka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GeminiConfig {

    @Value("${gemini.model.name:gemini-2.0-flash-exp}")
    private String modelName;

    @Value("${gemini.temperature:1.0}")
    private double temperature;

    @Value("${gemini.top-p:0.95}")
    private double topP;

    @Value("${gemini.top-k:40}")
    private int topK;

    @Value("${gemini.max-output-tokens:3000}")
    private int maxOutputTokens;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}