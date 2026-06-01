package io.hexlet.blog.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateDTO {
    
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @Size(min = 10, message = "Body must be at least 10 characters")
    private String body;
    
    private Boolean published;  // Используем Boolean для частичного обновления
}
