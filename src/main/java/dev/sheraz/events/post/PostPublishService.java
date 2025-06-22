package dev.sheraz.events.post;

import org.springframework.stereotype.Service;

import dev.sheraz.events.post.domain.Post;
import dev.sheraz.events.post.domain.PostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostPublishService {
    private final PostService postService;

    public void publish(Integer id) {
        Post post = postService.get(id);
        post.isPublished(true);
        postService.save(post);
    }
}
