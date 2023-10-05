package com.drewm.service;

import com.drewm.dto.UserDTO;
import com.drewm.model.User;
import com.drewm.repository.UserRepository;
import com.drewm.service.UserService;
import com.drewm.utils.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDTOMapper userDTOMapper;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userDTOMapper);
    }

    @Test
    void getAllUsers() {
        // given
        List<User> users = Arrays.asList(
                new User(1, "User 1", "user1", "pass123"),
                new User(2, "User 2", "user2", "password123")
        );
        when(userRepository.findAll()).thenReturn(users);

        // when
        List<UserDTO> userDTOs = Arrays.asList(new UserDTO(1, "user1"), new UserDTO(2, "user2"));
        when(userDTOMapper.apply(any(User.class))).thenReturn(userDTOs.get(0), userDTOs.get(1));
        List<UserDTO> result = userService.getAllUsers();

        // then
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
        verify(userDTOMapper, times(2)).apply(any(User.class));
    }

    @Test
    void getUserByUsername() {
        // given
        String username = "testuser";
        User user = User.builder()
                .username(username)
                .password("pass123")
                .build();

        // when
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUserByUsername(username);

        // then
        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(userRepository, times(1)).findUserByUsername(username);
    }
}