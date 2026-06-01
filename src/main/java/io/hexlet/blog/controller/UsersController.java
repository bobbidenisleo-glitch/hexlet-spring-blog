package io.hexlet.blog.controller;

import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserRepository userRepository;
    
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> index() {
        return userRepository.findAll();
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User show(@PathVariable Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User update(@PathVariable Long id, @RequestBody User userData) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (userData.getUsername() != null) {
            user.setUsername(userData.getUsername());
        }
        if (userData.getEmail() != null) {
            user.setEmail(userData.getEmail());
        }
        
        return userRepository.save(user);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
