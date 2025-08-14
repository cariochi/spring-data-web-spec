package com.cariochi.spec.app.security;

import com.cariochi.spec.app.service.UserService;
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
