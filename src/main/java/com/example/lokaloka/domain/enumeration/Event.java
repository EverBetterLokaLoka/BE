package com.example.lokaloka.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum Event {
    EVENT("EVENT"),
    LIKE("LIKE"),
    COMMENT("COMMENT");
    String name;
}
