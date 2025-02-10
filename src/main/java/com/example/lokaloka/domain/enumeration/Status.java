package com.example.lokaloka.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum Status {
    Draft("Draft"),
    Published("Published"),
    Archived("Archived");
    String name;
}
