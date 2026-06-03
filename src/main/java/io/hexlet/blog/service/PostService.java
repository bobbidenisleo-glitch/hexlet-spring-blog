package io.hexlet.blog.service;

import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.dto.PostCreateDTO;
import io.hexlet.blog.dto.PostUpdateDTO;
import io.hexlet.blog.exception.ResourceNotFoundException;
import io.hexlet.blog.mapper.PostMapper;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.PostRepository;
import io.hexlet.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMapper postMapper;

    public Page<PostDTO> getAllPublished(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        
        return postRepository.findByPublishedTrue(pageRequest)
                .map(postMapper::toDTO);
    }

    public PostDTO getById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));
        
        if (!post.isPublished()) {
            throw new ResourceNotFoundException("Post not found: " + id);
        }
        
        return postMapper.toDTO(post);
    }

    public PostDTO create(PostCreateDTO createDTO) {
        User user = userRepository.findById(createDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + createDTO.getUserId()));
        
        Post post = postMapper.toEntity(createDTO);
        post.setUser(user);
        
        Post savedPost = postRepository.save(post);
        return postMapper.toDTO(savedPost);
    }

    public PostDTO update(Long id, PostUpdateDTO updateDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));
        
        postMapper.update(updateDTO, post);
        Post updatedPost = postRepository.save(post);
        return postMapper.toDTO(updatedPost);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));
        
        if (post.getUser() != null) {
            post.getUser().getPosts().remove(post);
        }
        
        postRepository.deleteById(id);
    }
}
