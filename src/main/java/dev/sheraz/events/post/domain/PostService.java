package dev.sheraz.events.post.domain;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dev.sheraz.events.post.event.PostCreated;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final ApplicationEventPublisher events;

    public Post get(int id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post (%d) not found".formatted(id)));
    }

    public Post create(Post post) {
        if (post.id() != null) {
            throw new IllegalArgumentException("Post ID must be null for creation");
        }
        Post createdPost = save(post);
        events.publishEvent(new PostCreated(createdPost.id()));
        return createdPost;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }
}
