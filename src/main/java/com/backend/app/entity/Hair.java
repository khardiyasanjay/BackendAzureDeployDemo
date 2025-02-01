package com.backend.app.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Hair {
    private String color;   // Hair color
    private String type;    // Hair type (e.g., curly, straight)
}

