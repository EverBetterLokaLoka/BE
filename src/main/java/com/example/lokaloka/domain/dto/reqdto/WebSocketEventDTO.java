package com.example.lokaloka.domain.dto.reqdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketEventDTO {
    private String type;
    private Object payload;
}