package io.hexlet.blog.component;

import io.hexlet.blog.model.User;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.repository.UserRepository;
import io.hexlet.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @PostConstruct
    public void init() {
        System.out.println("=== DataInitializer запущен ===");
        
        if (userRepository.count() == 0) {
            System.out.println("=== Начинаем заполнение базы данных ===");
            
            // Создаём пользователей
            User user1 = new User();
            user1.setUsername("alice");
            user1.setEmail("alice@example.com");
            userRepository.save(user1);
            
            User user2 = new User();
            user2.setUsername("bob");
            user2.setEmail("bob@example.com");
            userRepository.save(user2);
            
            User user3 = new User();
            user3.setUsername("charlie");
            user3.setEmail("charlie@example.com");
            userRepository.save(user3);
            
            System.out.println("Создано пользователей: " + userRepository.count());
            
            // Создаём посты
            Post post1 = new Post();
            post1.setTitle("Hello Spring Boot");
            post1.setBody("Learning Spring Boot is fun!");
            post1.setUser(user1);
            post1.setPublished(true);
            postRepository.save(post1);
            
            Post post2 = new Post();
            post2.setTitle("JPA Auditing");
            post2.setBody("Auditing helps track creation and update times");
            post2.setUser(user1);
            post2.setPublished(true);
            postRepository.save(post2);
            
            Post post3 = new Post();
            post3.setTitle("REST API Design");
            post3.setBody("Best practices for REST API design");
            post3.setUser(user2);
            post3.setPublished(true);
            postRepository.save(post3);
            
            Post post4 = new Post();
            post4.setTitle("Draft Post");
            post4.setBody("This is an unpublished draft");
            post4.setUser(user2);
            post4.setPublished(false);
            postRepository.save(post4);
            
            Post post5 = new Post();
            post5.setTitle("Spring Data JPA");
            post5.setBody("Working with repositories");
            post5.setUser(user3);
            post5.setPublished(true);
            postRepository.save(post5);
            
            System.out.println("Создано постов: " + postRepository.count());
            System.out.println("Опубликованных постов: " + postRepository.countByPublishedTrue());
            System.out.println("=== База данных успешно заполнена ===");
        } else {
            System.out.println("Данные уже есть: " + userRepository.count() + " пользователей, " + postRepository.count() + " постов");
        }
    }
}
