package com.newsgroupmanagement.dto;

import com.newsgroupmanagement.validation.group.EmailValidationGroup;
import com.newsgroupmanagement.validation.group.NameValidationGroup;
import com.newsgroupmanagement.validation.group.OtpValidationGroup;
import com.newsgroupmanagement.validation.group.PasswordValidationGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GroupSequence({EmailValidationGroup.NotBlank.class,EmailValidationGroup.Size.class,EmailValidationGroup.Pattern.class,
        OtpValidationGroup.NotBlank.class,OtpValidationGroup.Pattern.class,
        NameValidationGroup.NotBlank.class,NameValidationGroup.Pattern.class,
        PasswordValidationGroup.NotBlank.class,PasswordValidationGroup.Pattern.class,
        AuthRequestDto.class
})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRequestDto {

    @NotBlank(message = "Email is required.",groups = EmailValidationGroup.NotBlank.class)
    @Size(max =320, message = "Email must not exceed 320 characters.",groups = EmailValidationGroup.Size.class)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",message = "Please enter a valid email address.",groups = EmailValidationGroup.Pattern.class)
    private String email;

    private boolean termsAccepted;

    @NotBlank(message = "OTP is required.",groups = OtpValidationGroup.NotBlank.class)
    @Pattern(regexp = "[0-9]{6}", message = "OTP must be exactly 6 digits.",groups = OtpValidationGroup.Pattern.class)
    private String otp;

    @NotBlank(message = "First name is required.",groups = NameValidationGroup.NotBlank.class)
    @Pattern(regexp = "[A-Za-z]{1,30}", message = "First name must be 1-30 letters.",groups = NameValidationGroup.Pattern.class)
    private String firstName;

    @NotBlank(message = "Last name is required.",groups = NameValidationGroup.NotBlank.class)
    @Pattern(regexp = "[A-Za-z]{1,30}", message = "Last name must be 1-30 letters.",groups = NameValidationGroup.Pattern.class)
    private String lastName;

    @NotBlank(message = "Password is required.",groups = PasswordValidationGroup.NotBlank.class)
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d+)(?=.*[@$!%*?&]+)[A-Za-z\\d@$!%*?&]{8,16}",message = "Include letters, numbers, and symbols 8-16.",groups = PasswordValidationGroup.Pattern.class)
    private String password;

    private String confirmPassword;
}
