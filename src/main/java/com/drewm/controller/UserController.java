package com.drewm.controller;

import com.drewm.dto.EditUserRequest;
import com.drewm.dto.UserDTO;
import com.drewm.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDTO> fetchAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> editUser(@PathVariable Integer userId, @RequestBody EditUserRequest request, Authentication authentication) {
        return ResponseEntity.ok(userService.editUser(userId, request, authentication));
    }
}
