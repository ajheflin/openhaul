package io.openhaul.openhaul.controllers;

import io.openhaul.openhaul.entities.User;
import io.openhaul.openhaul.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody User aUser) {
        return userService.createUser(aUser);
    }

    @GetMapping("/active-user")
    public User getActiveUser() {
        return userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }
}
