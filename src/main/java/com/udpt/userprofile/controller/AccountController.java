package com.udpt.userprofile.controller;

import com.udpt.userprofile.dto.CreateAndUpdateUserDto;
import com.udpt.userprofile.dto.LoginDto;
import com.udpt.userprofile.dto.RegisterDto;
import com.udpt.userprofile.entity.Account;
import com.udpt.userprofile.entity.UserProfile;
import com.udpt.userprofile.response.LoginResponse;
import com.udpt.userprofile.service.AccountService;
import com.udpt.userprofile.service.UserProfileService;
import com.udpt.userprofile.vo.ResponseVO;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final UserProfileService userProfileService;
    private final AccountService accountService;

    @Autowired
    public AccountController(UserProfileService userProfileService, AccountService accountService) {
        this.userProfileService = userProfileService;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseVO<Account> register(@Valid @RequestBody RegisterDto registerDto){
        UserProfile newUserProfile = userProfileService.createUserProfile(registerDto);
        Account newAccount = accountService.register(registerDto.getUsername(),  registerDto.getPassword(), newUserProfile.getId());
        return new ResponseVO<>("CREATED", "Account created successfully", newAccount);
    }


    @PostMapping("/login")
    public ResponseVO<LoginResponse> login(@Valid @RequestBody LoginDto loginDto){
        LoginResponse loginResponse = accountService.login(loginDto);
        return new ResponseVO<>("LOGIN", "Login successfully", loginResponse);
    }

    @PostMapping("/change-password")
    public ResponseVO<Account> changePassword(@Valid @RequestBody LoginDto loginDto){
        Account account = accountService.changePassword(loginDto);
        return new ResponseVO<>("CHANGE_PASSWORD", "Change password successfully", account);
    }

}
