package com.backend.app.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Embeddable
@Data
public class Address {
    private String address;   // Street address
    private String city;      // City name
    private String state;     // State name
    private String stateCode; // State code
    private String postalCode;// Postal code
    private String country;   // Country name

    @Embedded
    private Coordinates coordinates; // Nested coordinates object
}

