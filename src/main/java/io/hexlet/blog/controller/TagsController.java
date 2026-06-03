package io.hexlet.blog.controller;

import io.hexlet.blog.dto.TagDTO;
import io.hexlet.blog.dto.TagCreateDTO;
import io.hexlet.blog.dto.TagUpdateDTO;
import io.hexlet.blog.service.TagService;
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
    private TagService tagService;

    @GetMapping
    public List<TagDTO> index() {
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    public TagDTO show(@PathVariable Long id) {
        return tagService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO create(@Valid @RequestBody TagCreateDTO createDTO) {
        return tagService.create(createDTO);
    }

    @PutMapping("/{id}")
    public TagDTO update(@PathVariable Long id, @Valid @RequestBody TagUpdateDTO updateDTO) {
        return tagService.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}
