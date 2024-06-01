package com.udpt.userprofile.exception;

import lombok.Getter;

@Getter
public enum API_STATUSES {
    // USER PROFILE API ERROR
    USER_PROFILE_NOT_FOUND("USER_PROFILE_NOT_FOUND", "User profile not found"),
    USER_PROFILE_EXISTS("USER_PROFILE_EXISTS", "User profile exists"),

    // ACCOUNT API ERROR
    ACCOUNT_EXISTS("ACCOUNT_EXISTS", "Account exists"),
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", "Account not found"),
    WRONG_PASSWORD("WRONG_PASSWORD", "Wrong password"),;

    private final String status;
    private final String message;

    API_STATUSES(String status, String msg) {
        this.status = status;
        this.message = msg;
    }

}