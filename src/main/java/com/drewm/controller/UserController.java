package com.drewm.controller;

import com.drewm.dto.UserDTO;
import com.drewm.model.User;
import com.drewm.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
