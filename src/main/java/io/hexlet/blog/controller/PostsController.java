package io.hexlet.blog.controller;

import io.hexlet.blog.model.Post;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/posts")
public class PostsController {

    private final List<Post> posts = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // GET /posts - список всех постов
    @GetMapping
    public List<Post> index() {
        return posts;
    }

    // GET /posts/{id} - получение одного поста
    @GetMapping("/{id}")
    public Optional<Post> show(@PathVariable Long id) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
    }

    // POST /posts - создание поста
    @PostMapping
    public Post create(@RequestBody Post post) {
        Long newId = idCounter.getAndIncrement();
        post.setId(newId);
        posts.add(post);
        return post;
    }

    // PUT /posts/{id} - обновление поста
    @PutMapping("/{id}")
    public Post update(@PathVariable Long id, @RequestBody Post updatedPost) {
        Optional<Post> existingPost = posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();

        existingPost.ifPresent(post -> {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setAuthor(updatedPost.getAuthor());
        });

        return updatedPost;
    }

    // DELETE /posts/{id} - удаление поста
    @DeleteMapping("/{id}")
    public void destroy(@PathVariable Long id) {
        posts.removeIf(post -> post.getId().equals(id));
    }
}
