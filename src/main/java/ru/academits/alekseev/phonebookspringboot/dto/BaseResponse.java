package ru.academits.alekseev.phonebookspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse {
    private boolean success;
    private String message;

    public static BaseResponse success() {
        return new BaseResponse(true, null);
    }

    public static BaseResponse error(String message) {
        return new BaseResponse(false, message);
    }
}
