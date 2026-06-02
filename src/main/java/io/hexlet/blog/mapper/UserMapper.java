package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.UserDTO;
import io.hexlet.blog.dto.UserCreateDTO;
import io.hexlet.blog.dto.UserUpdateDTO;
import io.hexlet.blog.dto.UserPatchDTO;
import io.hexlet.blog.model.User;
import org.mapstruct.Mapper;
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
public interface UserMapper {
    
    UserDTO toDTO(User user);
    
    User toEntity(UserCreateDTO createDTO);
    
    void update(UserUpdateDTO updateDTO, @MappingTarget User user);
    
    void patch(UserPatchDTO patchDTO, @MappingTarget User user);
}
