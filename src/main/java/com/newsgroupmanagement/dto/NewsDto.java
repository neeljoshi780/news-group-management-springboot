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
public class NewsDto {
    private Long newsId;
    private Long categoryId;
    private Long userId;
    private String title;
    private String description;
    private String imageUrl;
    private Timestamp createdAt;
}
