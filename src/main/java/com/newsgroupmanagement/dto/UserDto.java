package com.newsgroupmanagement.dto;

import com.newsgroupmanagement.enums.AuthProvider;
import com.newsgroupmanagement.enums.Gender;
import com.newsgroupmanagement.enums.LanguagePreference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private Long id;

    // Basic Info
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String state;
    private LocalDate dateOfBirth;
    private Gender gender;
    private AuthProvider authProvider;
    private LanguagePreference languagePreference;
    private String profilePhotoUrl;
    private boolean subscribed;

    // Security / Account
    private String password;
    private boolean isPasswordSet;
    private boolean enabled;
    private boolean twoFactorEnabled;
    private boolean accountNonLocked;
    private int failedAttempt;
    private LocalDateTime lockTime;
    private boolean isDeleted;
    private LocalDateTime termsAcceptedAt;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
