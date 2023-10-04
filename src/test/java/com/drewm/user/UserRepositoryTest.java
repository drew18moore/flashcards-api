package com.drewm.user;

import com.drewm.exception.ResourceNotFoundException;
import com.drewm.model.User;
import com.drewm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    void shouldFindUserByUsername() {
        final String username = "test-user";
        User user = User.builder()
                .username(username)
                .password("pass123")
                .build();
        userRepository.save(user);

        User foundUser = userRepository.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        assertThat(foundUser).isEqualTo(user);
    }
}
