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
                "1. The response **must** be in **pure JSON format** with **no extra text**.",
                "2. **Do NOT include any explanation**, just return the JSON object.",
                "3. The JSON must be **complete and valid** (no missing brackets).",
                "4. If the response is too long, **split into multiple JSON arrays** but keep valid format.",
                "5. Ensure all keys have correct closing quotes and no missing commas.",
                "6. Use **standard JSON syntax** with proper nesting and spacing.",
                "7. Double-check before sending the response to make sure JSON is valid.",
                "",
                userPrompt,
                "",
                "Return the result in the **exact** following JSON structure:",
                "{",
                "  \"itinerary\": {",
                "    \"title\": \"string\",",
                "    \"description\": \"string\",",
                "    \"price\": \"double\",",
                "    \"locations\": [",
                "      {",
                "        \"name\": \"string\",",
                "        \"day\": \"number\",",
                "        \"description\": \"string\",",
                "        \"flag\": \"boolean\",",
                "        \"time_start\": \"datetime\",",
                "        \"time_finish\": \"datetime\",",
                "        \"time_reminder\": \"string\",",
                "        \"latitude\": \"double\",",
                "        \"longitude\": \"double\",",
                "        \"image_url\": \"string\",",
                "        \"culture\": \"string\",",
                "        \"recommended_time\": \"string\",",
                "        \"activities\": [",
                "          {",
                "            \"name\": \"string\",",
                "            \"description\": \"string\",",
                "            \"activities_possible\": \"string\",",
                "            \"price\": \"double\",",
                "            \"rule\": \"string\",",
                "            \"recommend\": \"json\"",
                "          }",
                "        ]",
                "      }",
                "    ]",
                "  }",
                "}"
        );
    }

    private Map<String, Object> createGenerationConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("temperature", 1.0);
        config.put("topP", 0.95);
        config.put("topK", 40);
        config.put("maxOutputTokens", 20000); // Tăng giới hạn tokens
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

                                // 🛠 In JSON để kiểm tra
                                System.out.println("🚀 RAW JSON từ Gemini:\n" + jsonString);

                                // Kiểm tra JSON hợp lệ
                                jsonString = fixJson(jsonString);
                                return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
                            }
                        }
                    }
                }
            }
            return Map.of("error", "Không có kết quả hợp lệ.");
        } catch (Exception e) {
            return Map.of("error", "Lỗi khi xử lý response: " + e.getMessage());
        }
    }
    private String fixJson(String json) {
        try {
            json = json.trim();

            // 🛠 Nếu thiếu dấu đóng, ta thêm vào
            if (!json.endsWith("}") && !json.endsWith("]")) {
                json += "}";
            }

            // 🛠 Kiểm tra JSON hợp lệ
            objectMapper.readTree(json);
            return json;
        } catch (Exception e) {
            return json; // Nếu lỗi, vẫn trả về JSON gốc
        }
    }


}