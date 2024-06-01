package com.udpt.userprofile.service;

import com.udpt.userprofile.dto.LoginDto;
import com.udpt.userprofile.entity.Account;
import com.udpt.userprofile.exception.API_STATUSES;
import com.udpt.userprofile.exception.DefaultException;
import com.udpt.userprofile.repository.AccountRepository;
import com.udpt.userprofile.response.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(String username, String password, UUID userProfileId) {
        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withParallelism(1)
                .withIterations(2)
                .build();

        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(params);

        byte[]  result = new byte[32];
        gen.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);


        Account newAccount = new Account();
        newAccount.setUsername(username);
        newAccount.setPassword(Base64.getEncoder().encodeToString(result));
        newAccount.setUserProfileId(userProfileId);


        return accountRepository.save(newAccount);
    }

    public LoginResponse login(LoginDto loginDto){
        Optional<Account> account = accountRepository.findByUsername(loginDto.getUsername());
        if (account.isEmpty()) {
            throw new DefaultException(API_STATUSES.ACCOUNT_NOT_FOUND);
        }

        byte[] storedPassword = Base64.getDecoder().decode(account.get().getPassword());


        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withParallelism(1)
                .withIterations(2)
                .build();

        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(params);
        byte[] providedPassword = new byte[32];
        gen.generateBytes(loginDto.getPassword().getBytes(StandardCharsets.UTF_8), providedPassword, 0, providedPassword.length);

        if (!Arrays.equals(storedPassword, providedPassword)) {
            throw new DefaultException(API_STATUSES.WRONG_PASSWORD);
        }

        String jwtToken = Jwts.builder()
                .claim("accountId", account.get().getId().toString())
                .claim("userProfileId", account.get().getUserProfileId().toString())
                .signWith(Keys.hmacShaKeyFor(this.secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccount(account.get());
        loginResponse.setAccessToken(jwtToken);

        return loginResponse;
    }

    public Account changePassword(LoginDto loginDto){
        Optional<Account> account = accountRepository.findByUsername(loginDto.getUsername());
        if (account.isEmpty()) {
            throw new DefaultException(API_STATUSES.ACCOUNT_NOT_FOUND);
        }

        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withParallelism(1)
                .withIterations(2)
                .build();

        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(params);

        byte[] result = new byte[32];
        gen.generateBytes(loginDto.getPassword().getBytes(StandardCharsets.UTF_8), result, 0, result.length);

        Account existingAccount = account.get();
        existingAccount.setPassword(Base64.getEncoder().encodeToString(result));

        return accountRepository.save(existingAccount);
    }
}
