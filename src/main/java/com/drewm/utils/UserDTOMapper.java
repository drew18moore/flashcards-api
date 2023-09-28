package com.drewm.utils;

import com.drewm.dto.UserDTO;
import com.drewm.model.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername()
        );
    }
}
