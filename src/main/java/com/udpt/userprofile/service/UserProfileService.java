package com.udpt.userprofile.service;

import com.udpt.userprofile.dto.CreateAndUpdateUserDto;
import com.udpt.userprofile.dto.LoginDto;
import com.udpt.userprofile.dto.RegisterDto;
import com.udpt.userprofile.entity.Account;
import com.udpt.userprofile.entity.UserProfile;
import com.udpt.userprofile.exception.API_STATUSES;
import com.udpt.userprofile.exception.DefaultException;
import com.udpt.userprofile.repository.AccountRepository;
import com.udpt.userprofile.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository, AccountRepository accountRepository) {
        this.userProfileRepository = userProfileRepository;
        this.accountRepository = accountRepository;
    }

    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public UserProfile getUserProfileById(UUID id) {
        return userProfileRepository.findById(id).orElseThrow(()->new DefaultException(API_STATUSES.USER_PROFILE_NOT_FOUND));
    }

    public UserProfile createUserProfile(RegisterDto registerDto) {
        // Check for duplicate user
        Optional<UserProfile> existingUser = userProfileRepository.findByEmail(registerDto.getEmail());
        if (existingUser.isPresent()) {
            throw new DefaultException(API_STATUSES.USER_PROFILE_EXISTS);
        }

        Optional<Account> account = accountRepository.findByUsername(registerDto.getUsername());
        if(account.isPresent()){
            throw new DefaultException(API_STATUSES.ACCOUNT_EXISTS);
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(registerDto.getFirstName());
        userProfile.setLastName(registerDto.getLastName());
        userProfile.setEmail(registerDto.getEmail());
        userProfile.setAddress(registerDto.getAddress());
        userProfile.setDateOfBirth(registerDto.getDateOfBirth());

        return userProfileRepository.save(userProfile);
    }

    public UserProfile updateUserProfile(CreateAndUpdateUserDto createAndUpdateUserDto, UUID id) {

        Optional<UserProfile> existingUser = userProfileRepository.findByEmail(createAndUpdateUserDto.getEmail());
        if (existingUser.isPresent()) {
            throw new DefaultException(API_STATUSES.USER_PROFILE_EXISTS);
        }

        return userProfileRepository.findById(id)
                .map(userProfile -> {
                    userProfile.setFirstName(createAndUpdateUserDto.getFirstName());
                    userProfile.setLastName(createAndUpdateUserDto.getLastName());
                    userProfile.setEmail(createAndUpdateUserDto.getEmail());
                    userProfile.setAddress(createAndUpdateUserDto.getAddress());
                    userProfile.setDateOfBirth(createAndUpdateUserDto.getDateOfBirth());
                    return userProfileRepository.save(userProfile);
                })
                .orElseThrow(()->new DefaultException(API_STATUSES.USER_PROFILE_NOT_FOUND));
    }

    public void deleteUserProfile(UUID id) {
        userProfileRepository.deleteById(id);
    }
}
