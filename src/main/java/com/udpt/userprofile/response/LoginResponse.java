package com.udpt.userprofile.response;

import com.udpt.userprofile.entity.Account;
import lombok.Data;

@Data
public class LoginResponse {
    private Account account;
    private String accessToken;
}
