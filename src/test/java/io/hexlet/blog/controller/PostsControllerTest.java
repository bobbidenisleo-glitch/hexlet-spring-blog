package io.hexlet.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import io.hexlet.blog.model.Post;
import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.PostRepository;
import io.hexlet.blog.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
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
    void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    void testShow() throws Exception {
        var result = mockMvc.perform(get("/api/posts/" + testPost.getId()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isObject()
                .containsEntry("id", testPost.getId())
                .containsEntry("title", testPost.getTitle())
                .containsEntry("body", testPost.getBody())
                .containsEntry("published", testPost.isPublished());
    }

    @Test
    void testGetPublishedPosts() throws Exception {
        var result = mockMvc.perform(get("/api/posts/published")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "createdAt")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isObject()
                .containsKey("content")
                .containsKey("totalElements")
                .containsKey("totalPages");
    }

    @Test
    void testCreate() throws Exception {
        Map<String, Object> newPost = new HashMap<>();
        newPost.put("title", "New Post Title");
        newPost.put("body", "New post body content");
        newPost.put("user", Map.of("id", testUser.getId()));
        newPost.put("published", true);

        var request = post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPost));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isObject()
                .containsEntry("title", "New Post Title")
                .containsEntry("body", "New post body content");
    }

    @Test
    void testUpdate() throws Exception {
        Map<String, String> updateData = new HashMap<>();
        updateData.put("title", "Updated Post Title");

        var request = put("/api/posts/" + testPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedPost = postRepository.findById(testPost.getId()).get();
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Post Title");
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/posts/" + testPost.getId()))
                .andExpect(status().isNoContent());

        var deletedPost = postRepository.findById(testPost.getId());
        assertThat(deletedPost).isEmpty();
    }

    @Test
    void testShowNotFound() throws Exception {
        mockMvc.perform(get("/api/posts/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testShowUnpublishedPost() throws Exception {
        // Создаём неопубликованный пост
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
