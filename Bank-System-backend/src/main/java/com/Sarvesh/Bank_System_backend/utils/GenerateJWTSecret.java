package com.Sarvesh.Bank_System_backend.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerateJWTSecret {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32]; // 256-bit key for HS256
        secureRandom.nextBytes(key);
        String secretKey = Base64.getEncoder().encodeToString(key);
        System.out.println("JWT Secret Key: " + secretKey);
    }
}
