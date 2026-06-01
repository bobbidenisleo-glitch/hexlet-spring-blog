package io.hexlet.blog.controller;

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

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Post> index() {
        return postRepository.findAll();
    }

    @GetMapping("/published")
    public Page<Post> getPublishedPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Sort sort = Sort.by(sortDirection, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        
        return postRepository.findByPublishedTrue(pageRequest);
    }

    @GetMapping("/{id}")
    public Post show(@PathVariable Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        
        if (!post.isPublished()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        
        return post;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@Valid @RequestBody Post post) {
        if (post.getUser() != null && post.getUser().getId() != null) {
            User user = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            post.setUser(user);
        }
        // Сохраняем значение published из запроса, если оно передано
        // Если не передано, по умолчанию false (уже установлено в модели)
        return postRepository.save(post);
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable Long id, @Valid @RequestBody Post postData) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        
        if (postData.getTitle() != null) {
            post.setTitle(postData.getTitle());
        }
        if (postData.getBody() != null) {
            post.setBody(postData.getBody());
        }
        // Обновляем published, если оно передано в запросе
        // Используем isPublished() для примитива boolean
        if (postData.isPublished() != post.isPublished()) {
            post.setPublished(postData.isPublished());
        }
        
        return postRepository.save(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
}
