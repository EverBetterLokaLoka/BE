package com.example.lokaloka.domain.dto.resdto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResDTO {
    private Long id;
    private String content;
    private String type;
}