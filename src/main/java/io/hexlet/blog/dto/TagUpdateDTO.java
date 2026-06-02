package io.hexlet.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagUpdateDTO {
    @NotBlank(message = "Tag name is required")
    private String name;
}
