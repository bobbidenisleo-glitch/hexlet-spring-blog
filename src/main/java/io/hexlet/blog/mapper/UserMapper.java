package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.UserDTO;
import io.hexlet.blog.dto.UserCreateDTO;
import io.hexlet.blog.dto.UserUpdateDTO;
import io.hexlet.blog.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
    uses = { JsonNullableMapper.class },
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UserMapper {
    
    @Autowired
    protected PasswordEncoder passwordEncoder;
    
    public abstract UserDTO toDTO(User user);
    
    @Mapping(target = "passwordDigest", expression = "java(passwordEncoder.encode(createDTO.getPassword()))")
    public abstract User toEntity(UserCreateDTO createDTO);
    
    public abstract void update(UserUpdateDTO updateDTO, @MappingTarget User user);
}
