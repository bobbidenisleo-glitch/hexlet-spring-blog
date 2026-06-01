package io.hexlet.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.blog.dto.PostCreateDTO;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.PostRepository;
import io.hexlet.blog.repository.UserRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Faker faker;

    private User testUser;
    private Post testPost;

    @BeforeEach
    void setUp() {
        testUser = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .ignore(Select.field(User::getPosts))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getUsername), () -> faker.name().username())
                .create();
        userRepository.save(testUser);

        testPost = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getCreatedAt))
                .ignore(Select.field(Post::getUpdatedAt))
                .supply(Select.field(Post::getTitle), () -> faker.book().title())
                .supply(Select.field(Post::getBody), () -> faker.lorem().paragraph())
                .set(Select.field(Post::getUser), testUser)
                .set(Select.field(Post::isPublished), true)
                .create();
        postRepository.save(testPost);
    }

    @Test
    void testGetPublishedPosts() throws Exception {
        mockMvc.perform(get("/api/posts/published")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "createdAt")
                        .param("direction", "desc"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        PostCreateDTO newPost = new PostCreateDTO();
        newPost.setTitle("New Post Title");
        newPost.setBody("New post body content");
        newPost.setUserId(testUser.getId());
        newPost.setPublished(true);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPost)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdate() throws Exception {
        PostCreateDTO updateData = new PostCreateDTO();
        updateData.setTitle("Updated Post Title");
        updateData.setBody("Updated body");
        updateData.setUserId(testUser.getId());
        updateData.setPublished(true);

        mockMvc.perform(put("/api/posts/" + testPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/posts/" + testPost.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testShowNotFound() throws Exception {
        mockMvc.perform(get("/api/posts/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testShowUnpublishedPost() throws Exception {
        Post unpublishedPost = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getCreatedAt))
                .ignore(Select.field(Post::getUpdatedAt))
                .set(Select.field(Post::getUser), testUser)
                .set(Select.field(Post::isPublished), false)
                .create();
        postRepository.save(unpublishedPost);

        mockMvc.perform(get("/api/posts/" + unpublishedPost.getId()))
                .andExpect(status().isNotFound());
    }
}
