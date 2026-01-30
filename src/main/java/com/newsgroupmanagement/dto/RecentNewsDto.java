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
public class RecentNewsDto {
    private String firstName;
    private String lastName;
    private String title;
    private Timestamp createdAt;
    private String timeAgo;

    public RecentNewsDto(String firstName, String lastName, String title, Timestamp createdAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.createdAt = createdAt;
    }
}
