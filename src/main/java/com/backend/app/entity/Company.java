package com.backend.app.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Embeddable
@Data
public class Company {
    private String department;   // Department within the company
    private String name;         // Company name
    private String title;        // Job title

    @Embedded
    private Address address;     // Nested address object for company location
}

