package io.hexlet.blog.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class PostCreateDTO {
    @NotBlank
    private String title;
    
    @NotBlank
    private String body;
    
    private boolean published;
    private Long userId;
}
