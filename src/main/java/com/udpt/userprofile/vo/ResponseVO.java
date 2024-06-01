package com.udpt.userprofile.vo;

import lombok.Data;

@Data
public class ResponseVO<T> {
    private String status;
    private String message;
    private T data;

    public ResponseVO(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
