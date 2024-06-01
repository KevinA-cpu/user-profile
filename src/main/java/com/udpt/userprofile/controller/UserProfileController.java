package com.udpt.userprofile.controller;

import com.udpt.userprofile.dto.CreateAndUpdateUserDto;
import com.udpt.userprofile.dto.RegisterDto;
import com.udpt.userprofile.entity.UserProfile;
import com.udpt.userprofile.service.UserProfileService;
import com.udpt.userprofile.vo.ResponseVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public ResponseVO<List<UserProfile>> getAllUserProfiles() {
        List<UserProfile> userProfiles = userProfileService.getAllUserProfiles();
        return new ResponseVO<>("SUCCESS", "Get all users successfully", userProfiles);
    }

    @GetMapping("/{id}")
    public ResponseVO<UserProfile> getUserProfile(@PathVariable UUID id) {
       UserProfile userProfile = userProfileService.getUserProfileById(id);
        return new ResponseVO<>("SUCCESS", "Get user with ID " + id + " successfully", userProfile);
    }

    @PostMapping
    public ResponseVO<UserProfile> createUserProfile(@Valid @RequestBody RegisterDto registerDto){
        UserProfile newUserProfile = userProfileService.createUserProfile(registerDto);
        return new ResponseVO<>("CREATED", "User created successfully", newUserProfile);
    }

    @PutMapping("/{id}")
    public ResponseVO<UserProfile> updateUserProfile(@Valid @RequestBody CreateAndUpdateUserDto createAndUpdateUserDto, @PathVariable UUID id){
        UserProfile newUserProfile = userProfileService.updateUserProfile(createAndUpdateUserDto, id);
        return new ResponseVO<>("UPDATED", "User updated successfully", newUserProfile);
    }

    @DeleteMapping("/{id}")
    public ResponseVO<UUID> deleteUserProfile(@PathVariable UUID id){
        userProfileService.deleteUserProfile(id);
        return new ResponseVO<>("DELETED", "User deleted successfully", id);
    }

}
