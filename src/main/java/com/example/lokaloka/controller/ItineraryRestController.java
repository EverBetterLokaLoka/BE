package com.example.lokaloka.controller;

import com.example.lokaloka.domain.dto.resdto.ItineraryResDTO;
import com.example.lokaloka.domain.entity.Itinerary;
import com.example.lokaloka.service.impl.GeminiService;
import com.example.lokaloka.service.impl.ItineraryService;
import com.example.lokaloka.util.ApiResponse;
import com.example.lokaloka.util.ResponseData;
import com.example.lokaloka.util.SuccessCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/itineraries")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ItineraryRestController {
    ItineraryService itineraryService;
    GeminiService geminiService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateSchedule(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");

        try {
            Map<String, Object> response = geminiService.generateSchedule(prompt);

            // Kiểm tra nếu có lỗi
            if (response.containsKey("error")) {
                return ResponseEntity.status(500).body(ApiResponse.<Map<String, Object>>builder()
                        .code(500)
                        .message(response.get("error").toString())
                        .data(null)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .code(200)
                    .message("Itinerary generated successfully")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<Map<String, Object>>builder()
                    .code(500)
                    .message("Error when creating itinerary: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getItineraries() {
        return ResponseEntity.ok(ResponseData.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(itineraryService.getAllItineraries())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItineraryById(@PathVariable Long id) {
        ItineraryResDTO itinerary = itineraryService.getItineraryById(id);
        return ResponseEntity.ok(ResponseData.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(itinerary)
                .build());
    }

    @PostMapping
    public ResponseEntity<?> createItinerary(@RequestBody ItineraryResDTO itineraryResDTO) {
        ItineraryResDTO createdItinerary = itineraryService.createItinerary(itineraryResDTO);
        return ResponseEntity.ok(ResponseData.builder()
                .code(SuccessCode.GET_SUCCESS.getCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .data(createdItinerary)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItinerary(@PathVariable Long id, @RequestBody ItineraryResDTO itineraryResDTO) {
        itineraryResDTO.setId(id);
        ItineraryResDTO updatedItinerary = itineraryService.updateItinerary(itineraryResDTO);
        return ResponseEntity.ok(ResponseData.builder()
                .code(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                .data(updatedItinerary)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItinerary(@PathVariable Long id) {
        itineraryService.deleteItineraryById(id);
        return ResponseEntity.ok(ResponseData.builder()
                .code(SuccessCode.DELETE_SUCCESSFUL.getCode())
                .message(SuccessCode.DELETE_SUCCESSFUL.getMessage())
                .data(null)
                .build());
    }

    private ItineraryResDTO convertMapToItineraryDTO(Map<String, Object> itineraryData) {
        // Implement the conversion logic from Map to ItineraryResDTO
        // This should handle the nested structure of locations and activities
        // You'll need to implement this based on your specific needs
        return null; // Replace with actual implementation
    }
}
