package com.drewm.user;

import com.drewm.exception.ResourceNotFoundException;
import com.drewm.model.User;
import com.drewm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RequiredArgsConstructor
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findUserByUsername_exists() {
        final String username = "test-user";
        User user = User.builder()
                .username(username)
                .password("pass123")
                .build();
        userRepository.save(user);

        User foundUser = userRepository.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void findUserByUsername_notExists() {
        Optional<User> user = userRepository.findUserByUsername("username");
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    void existsByUsername_exists() {
        final String username = "test-user";
        User user = User.builder()
                .username(username)
                .password("pass123")
                .build();
        userRepository.save(user);

        boolean exists = userRepository.existsByUsername(username);
        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_notExists() {
        boolean exists = userRepository.existsByUsername("username");
        assertThat(exists).isFalse();
    }
}
