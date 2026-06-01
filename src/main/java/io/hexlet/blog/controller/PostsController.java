package io.hexlet.blog.controller;

import io.hexlet.blog.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Post>> index() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(posts);
    }

    // GET /posts/{id} - получение одного поста
    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id) {
        Optional<Post> post = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        
        return ResponseEntity.of(post); // 200 если есть, 404 если нет
    }

    // POST /posts - создание поста
    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        Long newId = idCounter.getAndIncrement();
        post.setId(newId);
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post); // 201 Created
    }

    // PUT /posts/{id} - обновление поста
    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post updatedPost) {
        Optional<Post> existingPost = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (existingPost.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        Post post = existingPost.get();
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setAuthor(updatedPost.getAuthor());

        return ResponseEntity.ok(post); // 200 OK
    }

    // DELETE /posts/{id} - удаление поста
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        boolean removed = posts.removeIf(p -> p.getId().equals(id));
        
        if (!removed) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
