package io.hexlet.blog.controller;

import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.dto.PostCreateDTO;
import io.hexlet.blog.dto.PostUpdateDTO;
import io.hexlet.blog.dto.PostPatchDTO;
import io.hexlet.blog.mapper.PostMapper;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.PostRepository;
import io.hexlet.blog.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PostMapper postMapper;

    @GetMapping("/published")
    public Page<PostDTO> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        
        return postRepository.findByPublishedTrue(pageRequest)
                .map(postMapper::toDTO);
    }

    @GetMapping("/{id}")
    public PostDTO show(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        
        if (!post.isPublished()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        
        return postMapper.toDTO(post);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO create(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        // Находим пользователя по ID из DTO
        User user = userRepository.findById(postCreateDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Создаём пост и устанавливаем связь с пользователем
        Post post = postMapper.toEntity(postCreateDTO);
        post.setUser(user);
        
        // Также можно добавить пост в коллекцию пользователя
        user.getPosts().add(post);
        
        Post savedPost = postRepository.save(post);
        return postMapper.toDTO(savedPost);
    }

    @PutMapping("/{id}")
    public PostDTO update(@PathVariable Long id, @Valid @RequestBody PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        
        postMapper.update(postUpdateDTO, post);
        Post updatedPost = postRepository.save(post);
        return postMapper.toDTO(updatedPost);
    }

    @PatchMapping("/{id}")
    public PostDTO patch(@PathVariable Long id, @Valid @RequestBody PostPatchDTO postPatchDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        
        postMapper.patch(postPatchDTO, post);
        Post patchedPost = postRepository.save(post);
        return postMapper.toDTO(patchedPost);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        
        // Удаляем пост из коллекции пользователя
        if (post.getUser() != null) {
            post.getUser().getPosts().remove(post);
        }
        
        postRepository.deleteById(id);
    }
}
