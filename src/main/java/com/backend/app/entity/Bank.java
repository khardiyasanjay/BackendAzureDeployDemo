package com.backend.app.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Bank {
    private String cardExpire;   // Card expiration date
    private String cardNumber;   // Card number
    private String cardType;     // Card type (e.g., Visa, Elo, etc.)
    private String currency;     // Currency of the bank account
    private String iban;         // IBAN (International Bank Account Number)
}

