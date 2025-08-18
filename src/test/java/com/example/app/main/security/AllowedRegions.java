package com.example.app.main.security;

import com.example.app.main.service.UserService;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AllowedRegions implements Supplier<List<String>> {

    private final UserService userService;

    @Override
    public List<String> get() {
        return userService.getAllowedRegions();
    }
}
