package com.drewm.service;

import com.drewm.dto.UserDTO;
import com.drewm.model.User;
import com.drewm.repository.UserRepository;
import com.drewm.utils.UserDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userDTOMapper).collect(Collectors.toList());
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
