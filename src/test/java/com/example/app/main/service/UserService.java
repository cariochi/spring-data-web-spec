package com.example.app.main.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    public Set<String> getAllowedRegions() {
        return Set.of("US", "EU");
    }
}
