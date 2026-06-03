package io.hexlet.blog.service;

import io.hexlet.blog.dto.TagDTO;
import io.hexlet.blog.dto.TagCreateDTO;
import io.hexlet.blog.dto.TagUpdateDTO;
import io.hexlet.blog.mapper.TagMapper;
import io.hexlet.blog.model.Tag;
import io.hexlet.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    public List<TagDTO> getAll() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TagDTO getById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        return tagMapper.toDTO(tag);
    }

    public TagDTO create(TagCreateDTO createDTO) {
        if (tagRepository.existsByName(createDTO.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag already exists");
        }
        Tag tag = tagMapper.toEntity(createDTO);
        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toDTO(savedTag);
    }

    public TagDTO update(Long id, TagUpdateDTO updateDTO) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        
        if (!updateDTO.getName().equals(tag.getName())) {
            if (tagRepository.existsByName(updateDTO.getName())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag name already exists");
            }
        }
        
        tagMapper.update(updateDTO, tag);
        Tag updatedTag = tagRepository.save(tag);
        return tagMapper.toDTO(updatedTag);
    }

    public void delete(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        tagRepository.delete(tag);
    }
}
