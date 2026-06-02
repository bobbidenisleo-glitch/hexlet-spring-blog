package io.hexlet.blog.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class PostPatchDTO {
    
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private JsonNullable<String> title = JsonNullable.undefined();
    
    @Size(min = 10, message = "Body must be at least 10 characters")
    private JsonNullable<String> body = JsonNullable.undefined();
    
    private JsonNullable<Boolean> published = JsonNullable.undefined();
}
