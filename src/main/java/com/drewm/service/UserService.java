package com.drewm.service;

import com.drewm.dto.EditUserRequest;
import com.drewm.dto.UserDTO;
import com.drewm.exception.ResourceNotFoundException;
import com.drewm.exception.UnauthorizedException;
import com.drewm.model.User;
import com.drewm.repository.UserRepository;
import com.drewm.utils.UserDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    public Optional<User> getUserByUserId(Integer userId) {
        return userRepository.findById(userId);
    }

    public UserDTO editUser(Integer userId, EditUserRequest request, Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        String displayName = request.displayName();
        String username = request.username();

        if (!authUser.getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to edit this user");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (displayName != null && !displayName.isEmpty()) {
            user.setDisplayName(displayName);
        }

        if (username != null && !username.isEmpty()) {
            user.setUsername(username);
        }

        userRepository.save(user);
        return userDTOMapper.apply(user);
    }
}
