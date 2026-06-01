package io.hexlet.blog.controller;

import io.hexlet.blog.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // GET /api/users - список всех пользователей
    @GetMapping
    public ResponseEntity<List<User>> index() {
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }

    // GET /api/users/{id} - просмотр пользователя
    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable Long id) {
        Optional<User> user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        
        return ResponseEntity.of(user);
    }

    // POST /api/users - создание пользователя
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        Long newId = idCounter.getAndIncrement();
        user.setId(newId);
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // PUT /api/users/{id} - обновление пользователя
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = existingUser.get();
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());

        return ResponseEntity.ok(user);
    }

    // DELETE /api/users/{id} - удаление пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        
        if (!removed) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.noContent().build();
    }
}
