package io.hexlet.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateDTO {
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @NotBlank(message = "Body is required")
    @Size(min = 10, message = "Body must be at least 10 characters")
    private String body;
    
    private boolean published;
    
    @NotNull(message = "User ID is required")
    private Long userId;
}
