package io.hexlet.blog.controller;

import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.dto.PostCreateDTO;
import io.hexlet.blog.dto.PostUpdateDTO;
import io.hexlet.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    private PostService postService;

    @GetMapping("/published")
    public Page<PostDTO> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        return postService.getAllPublished(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public PostDTO show(@PathVariable Long id) {
        try {
            return postService.getById(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO create(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        return postService.create(postCreateDTO);
    }

    @PutMapping("/{id}")
    public PostDTO update(@PathVariable Long id, @Valid @RequestBody PostUpdateDTO postUpdateDTO) {
        return postService.update(id, postUpdateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }
}
