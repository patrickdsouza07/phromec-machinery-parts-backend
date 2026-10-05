package com.phromec.management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
    private String message;
    private int status;

    public ApiError(String message, int status) {
        this.message = message;
        this.status = status;
    }

}
