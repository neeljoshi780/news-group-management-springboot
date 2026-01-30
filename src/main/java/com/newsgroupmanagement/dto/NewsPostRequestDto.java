package com.newsgroupmanagement.dto;

import com.newsgroupmanagement.validation.group.*;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@GroupSequence({CategoryValidationGroup.NotBlank.class,
        TitleValidationGroup.NotBlank.class,TitleValidationGroup.Pattern.class,
        DescriptionValidationGroup.NotBlank.class,DescriptionValidationGroup.Pattern.class,
        NewsPostRequestDto.class
})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsPostRequestDto {

    @NotBlank(message = "Category is required.", groups = {CategoryValidationGroup.NotBlank.class})
    private String categoryId;

    @NotBlank(message = "Title is required.", groups = {TitleValidationGroup.NotBlank.class})
    @Pattern(regexp = "(?=.{2,500}$)[a-zA-Z0-9][a-zA-Z0-9.,:;'\"$%&?!()\\-–—]*(?: (?! )[a-zA-Z0-9.,:;'\"$%&?!()\\-–—]+){0,100}", message = "Title must be 2-500 characters.", groups = {TitleValidationGroup.Pattern.class})
    private String title;

    @NotBlank(message = "Description is required.", groups = {DescriptionValidationGroup.NotBlank.class})
    @Pattern(regexp = "(?=.{2,1000000}$)[a-zA-Z0-9][a-zA-Z0-9.,:;'\"$%&?!()\\-–—]*(?: (?! )[a-zA-Z0-9.,:;'\"$%&?!()\\-–—]+){0,1000}", message = "Description must be 2-1000000 characters.", groups = {DescriptionValidationGroup.Pattern.class})
    private String description;

    private MultipartFile file;
}
