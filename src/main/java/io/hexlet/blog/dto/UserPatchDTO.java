package io.hexlet.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserPatchDTO {
    
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private JsonNullable<String> username = JsonNullable.undefined();
    
    @Email(message = "Email should be valid")
    private JsonNullable<String> email = JsonNullable.undefined();
}
