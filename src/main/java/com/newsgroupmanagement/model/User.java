package com.newsgroupmanagement.model;

import com.newsgroupmanagement.enums.AuthProvider;
import com.newsgroupmanagement.enums.Gender;
import com.newsgroupmanagement.enums.LanguagePreference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@DynamicUpdate
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Info
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true, updatable = false)
    private String email;
    private String phone;
    private String city;
    private String state;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider = AuthProvider.LOCAL;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguagePreference languagePreference = LanguagePreference.EN;
    private String profilePhotoUrl;
    @Column(nullable = false)
    private boolean subscribed = false;

    // Security / Account
    private String password;
    @Column(nullable = false)
    private boolean isPasswordSet = true;
    @Column(nullable = false)
    private boolean enabled = true;
    @Column(nullable = false)
    private boolean twoFactorEnabled = false;
    @Column(nullable = false)
    private boolean accountNonLocked = true;
    @Column(nullable = false)
    private int failedAttempt = 0;
    private LocalDateTime lockTime;
    @Column(nullable = false)
    private boolean isDeleted = false;
    @Column(nullable = false)
    private LocalDateTime termsAcceptedAt;

    // Timestamps
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // News
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<News> newsList = new HashSet<>();
}
