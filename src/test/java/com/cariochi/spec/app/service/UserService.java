package com.cariochi.spec.app.service;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public List<String> getAllowedRegions() {
        return List.of("US", "EU");
    }
}
