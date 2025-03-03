package com.example.lokaloka.domain.dto.resdto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FollowerResDTO {
    private Long id;
    private Long followerId;
    private Long followedId;
    private Long relationshipType;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
