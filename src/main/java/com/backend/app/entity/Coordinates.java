package com.backend.app.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Coordinates {
    private Double lat;   // Latitude
    private Double lng;   // Longitude
}

