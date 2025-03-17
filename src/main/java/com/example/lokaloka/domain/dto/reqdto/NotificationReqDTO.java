package com.example.lokaloka.domain.dto.reqdto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReqDTO {
    private Long userId;
    private String description;
    private Long foreignId;
}
