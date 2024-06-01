package com.udpt.userprofile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udpt.userprofile.entity.UserProfile;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name="accounts")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private UUID id;

    @Column(name="user_profile_id")
    private UUID userProfileId;

    @Column(name="username")
    private String username;

    @Column(name="password")
    @JsonIgnore
    private String password;

    @ManyToOne
    @JoinColumn(name="user_profile_id", insertable = false, updatable = false)
    private UserProfile userProfile;
}
