package io.hexlet.blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @Value("${app.welcome-message}")
    private String welcomeMessage;

    @Value("${app.admin-email}")
    private String adminEmail;

    @Value("${app.page-size}")
    private int pageSize;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

    @GetMapping("/info")
    public String info() {
        return "Admin email: " + adminEmail + ", Page size: " + pageSize;
    }
}
