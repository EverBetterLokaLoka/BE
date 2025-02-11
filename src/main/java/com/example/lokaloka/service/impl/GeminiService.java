package com.example.lokaloka.service.impl;

import com.example.lokaloka.model.GeminiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";

    public Map<String, Object> generateSchedule(String prompt) {
        try {
            Map<String, Object> requestBody = buildRequestBody(prompt);
            HttpEntity<Map<String, Object>> requestEntity = createHttpEntity(requestBody);

            ResponseEntity<Map> response = restTemplate.exchange(
                    GEMINI_API_URL + "?key=" + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            return processResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gọi Gemini API: " + e.getMessage());
        }
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        String basePrompt = createBasePrompt(prompt);

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("role", "user");
        content.put("parts", Collections.singletonList(Map.of("text", basePrompt)));

        requestBody.put("contents", Collections.singletonList(content));
        requestBody.put("generationConfig", createGenerationConfig());

        return requestBody;
    }

    private String createBasePrompt(String userPrompt) {
        return String.join("\n",
                "1. Dữ liệu phải hoàn toàn bằng Tiếng Việt.",
                "2. Cung cấp thông tin chính xác, không dư thừa.",
                "3. Liệt kê địa điểm với tọa độ chính xác 100%.",
                "4. Tôi đã đến địa điểm này, mang theo xe máy và tự chi tiền xăng.",
                "",
                userPrompt,
                "",
                "Hãy trả về kết quả dưới dạng JSON với cấu trúc sau:",
                "{",
                "  \"overview\": \"string mô tả tổng quan\",",
                "  \"response\": [",
                "    {",
                "      \"recipeName\": \"string\",",
                "      \"title\": \"string\",",
                "      \"description\": \"string\",",
                "      \"day\": \"string\",",
                "      \"price\": \"string\",",
                "      \"activities\": [",
                "        {",
                "          \"name\": \"string\",",
                "          \"start_time\": \"string\",",
                "          \"end_time\": \"string\",",
                "          \"location\": \"string\",",
                "          \"price\": \"string\",",
                "          \"description\": \"string\",",
                "          \"image\": \"string\",",
                "          \"latitude\": \"string\",",
                "          \"longitude\": \"string\"",
                "        }",
                "      ]",
                "    }",
                "  ]",
                "}"
        );
    }

    private Map<String, Object> createGenerationConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("temperature", 1.0);
        config.put("topP", 0.95);
        config.put("topK", 40);
        config.put("maxOutputTokens", 3000);
        return config;
    }

    private HttpEntity<Map<String, Object>> createHttpEntity(Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestBody, headers);
    }

    private Map<String, Object> processResponse(ResponseEntity<Map> response) {
        try {
            if (response.getBody() != null && response.getBody().containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> firstCandidate = candidates.get(0);
                    if (firstCandidate.containsKey("content")) {
                        Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
                        if (content.containsKey("parts")) {
                            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                            if (!parts.isEmpty() && parts.get(0).containsKey("text")) {
                                String jsonString = (String) parts.get(0).get("text");
                                // Chuyển đổi JSON string thành Map
                                return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
                            }
                        }
                    }
                }
            }
            return Map.of("error", "Không có kết quả");
        } catch (Exception e) {
            return Map.of("error", "Lỗi xử lý response: " + e.getMessage());
        }
    }
}