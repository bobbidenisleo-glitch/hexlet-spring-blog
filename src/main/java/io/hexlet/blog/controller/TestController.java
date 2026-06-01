package io.hexlet.blog.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @PostMapping("/json")
    public Map<String, String> testJson(@RequestBody Map<String, String> data) {
        return Map.of("received", data.toString());
    }
}
