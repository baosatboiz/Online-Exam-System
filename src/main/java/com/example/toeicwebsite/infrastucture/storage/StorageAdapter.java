package com.example.toeicwebsite.infrastucture.storage;

import com.example.toeicwebsite.application.port.StoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageAdapter implements StoragePort {
    @Value("${supabase.url}")
    private String storageUrl;
    @Override
    public String getFileUrl(String path) {
        return storageUrl+path;
    }
}
