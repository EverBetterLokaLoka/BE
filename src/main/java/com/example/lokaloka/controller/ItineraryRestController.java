package com.example.lokaloka.controller;

import com.example.lokaloka.domain.dto.resdto.ItineraryResDTO;
import com.example.lokaloka.domain.entity.Itinerary;
import com.example.lokaloka.service.impl.GeminiService;
import com.example.lokaloka.service.impl.ItineraryService;
import com.example.lokaloka.util.ApiResponse;
import com.example.lokaloka.util.CustomException;
import com.example.lokaloka.util.ResponseData;
import com.example.lokaloka.util.SuccessCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateSchedule(
            @RequestBody(required = false) Map<String, String> request) {

        // Nếu request body null, tạo request rỗng để tránh lỗi
        if (request == null) {
            request = new HashMap<>();
        }

        String prompt = request.get("prompt");

        if (prompt == null || prompt.isEmpty()) {
            return ResponseEntity.status(400).body(ApiResponse.<Map<String, Object>>builder()
                    .success(false)
                    .status(HttpStatus.SC_BAD_REQUEST)
                    .message("Missing prompt")
                    .data(null)
                    .build());
        }

        try {
            Map<String, Object> response = geminiService.generateSchedule(prompt);

            if (response.containsKey("error")) {
                return ResponseEntity.status(500).body(ApiResponse.<Map<String, Object>>builder()
                        .success(false)
                        .code(500)
                        .message(response.get("error").toString())
                        .data(null)
                        .build());
            }

            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .success(true)
                    .status(HttpStatus.SC_OK)
                    .message("Itinerary generated successfully")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.<Map<String, Object>>builder()
                    .success(false)
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .code(500)
                    .message("Error when creating itinerary: " + e.getMessage())
                    .data(null)
                    .build());
        }
    }


    @GetMapping
    public ResponseEntity<?> getItineraries() {
        return ResponseEntity.ok(ResponseData.builder()
                .success(true)
                .status(SuccessCode.GET_ITINERARIES_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_ITINERARIES_SUCCESSFUL.getMessage())
                .data(itineraryService.getAllItineraries())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItineraryById(@PathVariable Long id) {
        ItineraryResDTO itinerary = itineraryService.getItineraryById(id);
        return ResponseEntity.ok(ResponseData.builder()
                .success(true)
                .status(SuccessCode.GET_ITINERARIES_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_ITINERARIES_SUCCESSFUL.getMessage())
                .data(itinerary)
                .build());
    }

    @PostMapping
    public ResponseEntity<?> createItinerary(@RequestBody ItineraryResDTO itineraryResDTO) {
        ItineraryResDTO createdItinerary = itineraryService.createItinerary(itineraryResDTO);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(
                ApiResponse.builder()
                        .success(true)
                        .status(HttpStatus.SC_CREATED)
                        .message("Save itineraries successfully")
                        .data(createdItinerary)
                        .build()
        );
    }

    // Bắt lỗi chung cho toàn bộ controller
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
                ApiResponse.builder()
                        .success(false)
                        .status(ex.getStatus().value())
                        .message(ex.getMessage())
                        .data(null)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItinerary(@PathVariable Long id, @RequestBody ItineraryResDTO itineraryResDTO) {
        itineraryResDTO.setId(id);
        ItineraryResDTO updatedItinerary = itineraryService.updateItinerary(itineraryResDTO);
        return ResponseEntity.ok(ResponseData.builder()
                .success(true)
                .status(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                .data(updatedItinerary)
                .build());
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateItineraryPatch(@PathVariable Long id, @RequestBody ItineraryResDTO itineraryResDTO) {
        itineraryResDTO.setId(id);
        ItineraryResDTO updatedItinerary = itineraryService.updateItinerary(itineraryResDTO);
        return ResponseEntity.ok(ResponseData.builder()
                .success(true)
                .status(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                .data(updatedItinerary)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItinerary(@PathVariable Long id) {

        return itineraryService.deleteItineraryById(id);
    }

    private ItineraryResDTO convertMapToItineraryDTO(Map<String, Object> itineraryData) {
        // Implement the conversion logic from Map to ItineraryResDTO
        // This should handle the nested structure of locations and activities
        // You'll need to implement this based on your specific needs
        return null; // Replace with actual implementation
    }
}
