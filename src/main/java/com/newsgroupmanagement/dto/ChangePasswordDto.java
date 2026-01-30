package com.newsgroupmanagement.dto;

import com.newsgroupmanagement.validation.group.PasswordValidationGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GroupSequence({PasswordValidationGroup.NotBlank.class,PasswordValidationGroup.Pattern.class,
        ChangePasswordDto.class
})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordDto {

    @NotBlank(message = "Old password is required.",groups = PasswordValidationGroup.NotBlank.class)
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d+)(?=.*[@$!%*?&]+)[A-Za-z\\d@$!%*?&]{8,16}",message = "Invalid old password.",groups = PasswordValidationGroup.Pattern.class)
    private String oldPassword;

    @NotBlank(message = "New password is required.",groups = PasswordValidationGroup.NotBlank.class)
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d+)(?=.*[@$!%*?&]+)[A-Za-z\\d@$!%*?&]{8,16}",message = "Include letters, numbers, and symbols 8-16.",groups = PasswordValidationGroup.Pattern.class)
    private String newPassword;

    private String confirmNewPassword;
}
