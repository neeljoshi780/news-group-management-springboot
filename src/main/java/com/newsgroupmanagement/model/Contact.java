package com.newsgroupmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String fullName;
    @Column(nullable = false, length = 320)
    private String email;
    @Column(nullable = false, length = 10)
    private String phone;
    @Column(nullable = false, length = 500)
    private String message;
}
