package com.newsgroupmanagement.dto;

import com.newsgroupmanagement.enums.Gender;
import com.newsgroupmanagement.enums.LanguagePreference;
import com.newsgroupmanagement.validation.group.NameValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String state;
    private LocalDate dob;
    private Gender gender;
    private LanguagePreference languagePreference;
    private String profilePhotoUrl;
    private boolean subscribed;
    private boolean isPasswordSet;
    private boolean twoFactorEnabled;
}
