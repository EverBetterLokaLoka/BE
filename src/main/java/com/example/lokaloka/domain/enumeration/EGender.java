package com.example.lokaloka.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum EGender {
    Male("Male"),
    Female("Female"),
    Other("Other");
    String name;
}