package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.UserDTO;
import io.hexlet.blog.dto.UserCreateDTO;
import io.hexlet.blog.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
    
    public User toEntity(UserCreateDTO userCreateDTO) {
        if (userCreateDTO == null) {
            return null;
        }
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        return user;
    }
    
    public void updateEntity(User user, UserCreateDTO userCreateDTO) {
        if (userCreateDTO.getUsername() != null) {
            user.setUsername(userCreateDTO.getUsername());
        }
        if (userCreateDTO.getEmail() != null) {
            user.setEmail(userCreateDTO.getEmail());
        }
    }
}
