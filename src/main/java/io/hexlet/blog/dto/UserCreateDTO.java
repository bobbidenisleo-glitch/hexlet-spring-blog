package io.hexlet.blog.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class UserCreateDTO {
    @NotBlank
    private String username;
    
    @NotBlank
    @Email
    private String email;
}
