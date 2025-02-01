package com.backend.app.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Crypto {
    private String coin;      // Cryptocurrency (e.g., Bitcoin)
    private String wallet;    // Wallet address
    private String network;   // Cryptocurrency network (e.g., Ethereum)
}

