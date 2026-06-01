package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.dto.PostCreateDTO;
import io.hexlet.blog.dto.PostUpdateDTO;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostMapper {
    
    @Mapping(source = "user.id", target = "userId")
    PostDTO toDTO(Post post);
    
    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUserIdToUser")
    Post toEntity(PostCreateDTO createDTO);
    
    void updateEntity(PostUpdateDTO updateDTO, @MappingTarget Post post);
    
    @Named("mapUserIdToUser")
    default User mapUserIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
