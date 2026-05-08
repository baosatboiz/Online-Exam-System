package com.example.toeicwebsite.infrastucture.security.encryption;

public interface EncryptionService {
    String encrypt(String plainText);
    String decrypt(String encryptedText);
}
