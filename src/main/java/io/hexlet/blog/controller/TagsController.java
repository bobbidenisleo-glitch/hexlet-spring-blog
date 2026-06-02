package io.hexlet.blog.controller;

import io.hexlet.blog.dto.TagDTO;
import io.hexlet.blog.dto.TagCreateDTO;
import io.hexlet.blog.dto.TagUpdateDTO;
import io.hexlet.blog.mapper.TagMapper;
import io.hexlet.blog.model.Tag;
import io.hexlet.blog.repository.TagRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagsController {
    
    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    private TagMapper tagMapper;
    
    @GetMapping
    public List<TagDTO> index() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toDTO)
                .toList();
    }
    
    @GetMapping("/{id}")
    public TagDTO show(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        return tagMapper.toDTO(tag);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO create(@Valid @RequestBody TagCreateDTO createDTO) {
        if (tagRepository.existsByName(createDTO.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag already exists");
        }
        Tag tag = tagMapper.toEntity(createDTO);
        tagRepository.save(tag);
        return tagMapper.toDTO(tag);
    }
    
    @PutMapping("/{id}")
    public TagDTO update(@PathVariable Long id, @Valid @RequestBody TagUpdateDTO updateDTO) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        
        // Если имя меняется, проверяем, не занято ли новое имя
        if (!updateDTO.getName().equals(tag.getName())) {
            if (tagRepository.existsByName(updateDTO.getName())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag name already exists");
            }
        }
        
        tagMapper.update(updateDTO, tag);
        tagRepository.save(tag);
        return tagMapper.toDTO(tag);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        tagRepository.delete(tag);
    }
}
