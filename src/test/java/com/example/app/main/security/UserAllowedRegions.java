package com.example.app.main.security;

import com.example.app.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserAllowedRegions implements Function<String, Set<String>> {

    private final UserService userService;

    @Override
    public Set<String> apply(String name) {
        return userService.getAllowedRegions();
    }
}
