package com.newsgroupmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsListDto {
    private Long newsId;
    private String title;
    private String description;
    private String imageUrl;
    private String categoryName;
    private String userFirstName;
    private String userLastName;
    private String userProfileUrl;
    private Timestamp createdAt;
}
