package io.hexlet.blog.controller;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/simple-users")
public class SimpleUserController {
    
    private final List<Map<String, Object>> users = new ArrayList<>();
    private long nextId = 1;
    
    @PostMapping
    public Map<String, Object> create(@RequestBody Map<String, Object> user) {
        user.put("id", nextId++);
        users.add(user);
        return user;
    }
    
    @GetMapping
    public List<Map<String, Object>> list() {
        return users;
    }
}
