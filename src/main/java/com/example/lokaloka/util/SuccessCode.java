package com.example.lokaloka.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum SuccessCode {
    CREATED(201, "Success Created" , HttpStatus.CREATED),
    SUCCESS(200, "Success " , HttpStatus.OK),
    DELETE_SUCCESSFUL(1012, "Delete itineraries successfully", HttpStatus.OK),
    UPDATE_SUCCESSFUL(1013, "Update itineraries successfully", HttpStatus.OK),
    GET_SUCCESSFUL(1010, "Get successful", HttpStatus.OK),
    GET_ITINERARIES_SUCCESSFUL(1011, "Get itineraries successfully", HttpStatus.OK),
    CREATE_ITINERARIES_SUCCESSFUL(1012, "Save itineraries successfully", HttpStatus.OK),
    LOGIN(1011, "Login successful", HttpStatus.OK),

    DELETED(20401, "Delete success", HttpStatus.NO_CONTENT),
    UPDATED(20402, "Update success", HttpStatus.OK),
    GET_SUCCESS(20403, "Get success", HttpStatus.OK),
    ;
    int code;
    String message;
    HttpStatus httpStatus;
}
