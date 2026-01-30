package com.newsgroupmanagement.dto;

import com.newsgroupmanagement.validation.group.*;
import jakarta.persistence.Column;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GroupSequence({NameValidationGroup.NotBlank.class, NameValidationGroup.Pattern.class,
        EmailValidationGroup.NotBlank.class,EmailValidationGroup.Size.class,EmailValidationGroup.Pattern.class,
        PhoneValidation.NotBlank.class, PhoneValidation.Pattern.class,
        MessageValidation.NotBlank.class, MessageValidation.Pattern.class,
        ContactDto.class
})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactDto {

    private Long id;

    @NotBlank(message = "Full name is required.",groups = NameValidationGroup.NotBlank.class)
    @Pattern(regexp = "(?=.{2,40}$)[a-zA-Z][a-zA-Z]*(?: [a-zA-Z]+){0,2}", message = "Full name must be 2-40 letters.",groups = NameValidationGroup.Pattern.class)
    private String fullName;

    @NotBlank(message = "Email is required.",groups = EmailValidationGroup.NotBlank.class)
    @Size(max =320, message = "Email must not exceed 320 characters.",groups = EmailValidationGroup.Size.class)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",message = "Please enter a valid email address.",groups = EmailValidationGroup.Pattern.class)
    private String email;

    @NotBlank(message = "Phone number is required.", groups = PhoneValidation.NotBlank.class)
    @Pattern(regexp = "[6-9][0-9]{9}", message = "Enter a valid 10-digit phone number.", groups = PhoneValidation.Pattern.class)
    private String phone;

    @NotBlank(message = "Message field cannot be empty.", groups = MessageValidation.NotBlank.class)
    @Pattern(regexp = "(?=.{2,500}$)[a-zA-Z0-9][a-zA-Z0-9.,:;'\"$%&?!()\\-–—]*(?: (?! )[a-zA-Z0-9.,:;'\"$%&?!()\\-–—]+){0,100}", message = "Message must be 2-500 characters long.", groups = MessageValidation.Pattern.class)
    private String message;
}
