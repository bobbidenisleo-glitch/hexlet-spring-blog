package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.dto.PostCreateDTO;
import io.hexlet.blog.dto.PostUpdateDTO;
import io.hexlet.blog.dto.PostPatchDTO;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    uses = { JsonNullableMapper.class },
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostMapper {
    
    @Mapping(source = "user.id", target = "userId")
    PostDTO toDTO(Post post);
    
    @Mapping(target = "user", source = "userId")
    Post toEntity(PostCreateDTO createDTO);
    
    void update(PostUpdateDTO updateDTO, @MappingTarget Post post);
    
    void patch(PostPatchDTO patchDTO, @MappingTarget Post post);
    
    default User map(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }
    
    default Long map(User user) {
        return user != null ? user.getId() : null;
    }
}
