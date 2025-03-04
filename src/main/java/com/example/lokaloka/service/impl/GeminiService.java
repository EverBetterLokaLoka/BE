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

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-thinking-exp-01-21:generateContent";

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
        // Cập nhật base prompt sao cho đúng định dạng mà không chứa dấu backtick
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
                "Return the result in this exact JSON format:",
                "{",
                "  \"itinerary\": {",
                "    \"title\": \"Sample Title\",",
                "    \"description\": \"Sample Description\",",
                "    \"price\": 100.0,",
                "    \"locations\": [",
                "      {",
                "        \"name\": \"Sample Location\",",
                "        \"day\": 1,",
                "        \"description\": \"A great place to visit.\",",
                "        \"flag\": false,",
                "        \"time_start\": \"2025-02-13T08:00:00Z\",",
                "        \"time_finish\": \"2025-02-13T10:00:00Z\",",
                "        \"time_reminder\": \"30 minutes before\",",
                "        \"culture\": \"French\",",
                "        \"recommended_time\": \"Morning\",",
                "        \"price\": \"30.0\",",
                "        \"activities\": [",
                "          {",
                "            \"name\": \"Visit Eiffel Tower\",",
                "            \"description\": \"Enjoy a panoramic view of Paris.\",",
                "            \"activities_possible\": \"Photography, Sightseeing\",",
                "            \"price\": 25.0,",
                "            \"rule\": \"No drones allowed\",",
                "            \"recommend\": \"Highly recommended for Instagram enthusiasts and photography buffs.\"",
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
        config.put("temperature", 0.8); // Giảm temperature để trả về kết quả chính xác hơn
        config.put("topP", 0.85);       // Giảm topP để trả về kết quả ổn định hơn
        config.put("topK", 20);         // Giảm topK để mô hình ít sáng tạo hơn
        config.put("maxOutputTokens", 15000); // Tăng giới hạn tokens (nếu cần thiết)
        return config;
    }

    private HttpEntity<Map<String, Object>> createHttpEntity(Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestBody, headers);
    }

    private Map<String, Object> processResponse(ResponseEntity<Map> response) {
        try {
            System.out.println("🚀 RAW RESPONSE: " + response.getBody());

            if (response.getBody() != null) {
                // Kiểm tra các thông tin về token sử dụng (nếu có)
                if (response.getBody().containsKey("usageMetadata")) {
                    Map<String, Object> usageMetadata = (Map<String, Object>) response.getBody().get("usageMetadata");
                    System.out.println("🔥 Tokens Used: " + usageMetadata);
                }

                if (response.getBody().containsKey("candidates")) {
                    List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");

                    if (!candidates.isEmpty()) {
                        Map<String, Object> firstCandidate = candidates.get(0);
                        if (firstCandidate.containsKey("content")) {
                            Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
                            if (content.containsKey("parts")) {
                                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                                if (!parts.isEmpty() && parts.get(0).containsKey("text")) {
                                    String jsonString = (String) parts.get(0).get("text");

                                    System.out.println("📌 JSON nhận từ API: " + jsonString);

                                    // Loại bỏ tất cả dấu backtick và từ "json" trong chuỗi phản hồi
                                    jsonString = jsonString.replace("`", "").replace("json", "").trim();

                                    // Kiểm tra JSON hợp lệ
                                    if (!isValidJson(jsonString)) {
                                        return Map.of("error", "JSON không hợp lệ từ API", "raw_json", jsonString);
                                    }

                                    // Parse JSON thành object
                                    try {
                                        return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
                                    } catch (Exception jsonException) {
                                        System.out.println("❌ Lỗi khi parse JSON: " + jsonException.getMessage());
                                        return Map.of("error", "Lỗi parse JSON: " + jsonException.getMessage(), "raw_json", jsonString);
                                    }
                                } else {
                                    return Map.of("error", "Không có dữ liệu text trong response.");
                                }
                            } else {
                                return Map.of("error", "Không có 'parts' trong response.");
                            }
                        } else {
                            return Map.of("error", "Không có 'content' trong candidate.");
                        }
                    } else {
                        return Map.of("error", "Không có candidate trong response.");
                    }
                } else {
                    return Map.of("error", "Không có candidates trong response.");
                }
            } else {
                return Map.of("error", "Response từ API trống.");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi xử lý response: " + e.getMessage());
            return Map.of("error", "Lỗi khi xử lý response: " + e.getMessage());
        }
    }

    private boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception e) {
            System.out.println("❌ JSON không hợp lệ: " + e.getMessage());
            return false;
        }
    }
}
