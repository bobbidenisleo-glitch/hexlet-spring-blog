package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.dto.PostCreateDTO;
import io.hexlet.blog.dto.PostUpdateDTO;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.model.User;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    
    public PostDTO toDTO(Post post) {
        if (post == null) {
            return null;
        }
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setPublished(post.isPublished());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        
        if (post.getUser() != null) {
            dto.setUserId(post.getUser().getId());
        }
        
        return dto;
    }
    
    public Post toEntity(PostCreateDTO postCreateDTO, User user) {
        if (postCreateDTO == null) {
            return null;
        }
        Post post = new Post();
        post.setTitle(postCreateDTO.getTitle());
        post.setBody(postCreateDTO.getBody());
        post.setPublished(postCreateDTO.isPublished());
        post.setUser(user);
        return post;
    }
    
    // Частичное обновление — обновляем только не-null поля
    public void updateEntity(Post post, PostUpdateDTO postUpdateDTO) {
        if (postUpdateDTO.getTitle() != null) {
            post.setTitle(postUpdateDTO.getTitle());
        }
        if (postUpdateDTO.getBody() != null) {
            post.setBody(postUpdateDTO.getBody());
        }
        if (postUpdateDTO.getPublished() != null) {
            post.setPublished(postUpdateDTO.getPublished());
        }
    }
}
