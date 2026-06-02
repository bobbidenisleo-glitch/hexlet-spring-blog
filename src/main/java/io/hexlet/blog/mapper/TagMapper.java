package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.TagDTO;
import io.hexlet.blog.dto.TagCreateDTO;
import io.hexlet.blog.dto.TagUpdateDTO;
import io.hexlet.blog.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TagMapper {
    TagDTO toDTO(Tag tag);
    Tag toEntity(TagCreateDTO createDTO);
    void update(TagUpdateDTO updateDTO, @MappingTarget Tag tag);
}
