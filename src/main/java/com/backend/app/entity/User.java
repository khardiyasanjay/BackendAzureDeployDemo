package com.backend.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    @Column
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    @Column
    private String lastName;

    @Size(max = 100, message = "Maiden name cannot exceed 100 characters")
    @Column
    private String maidenName;

    @Min(value = 0, message = "Age must be a positive number")
    @Max(value = 130, message = "Age cannot exceed 130")
    private Integer age;             // Age of the user

    @Pattern(regexp = "^(male|female|other)$", message = "Gender must be one of: male, female, or other")
    private String gender;           // Gender of the user

    @Email(message = "Email should be valid")
    private String email;            // Email address
    private String phone;            // Phone number
    private String username;         // Username
    private String password;         // Password
    private String birthDate;        // Date of birth in String format
    private String image;            // Profile image URL
    private String bloodGroup;       // Blood group
    private Double height;           // Height in cm
    private Double weight;           // Weight in kg
    private String eyeColor;         // Eye color

    @Embedded
    private Hair hair;               // Nested object for hair details

    private String ip;               // IP address
    private String macAddress;       // MAC address
    private String university;       // University name

    @Embedded
    private Bank bank;               // Nested object for bank details

    @Embedded
    private Company company;         // Nested object for company details

    private String ein;              // Employer Identification Number
    private String ssn;              // Social Security Number
    private String userAgent;        // User-Agent string
    private String role;             // Role of the user (e.g., admin, user, etc.)

    @Embedded
    private Crypto crypto;           // Nested object for cryptocurrency details
}

