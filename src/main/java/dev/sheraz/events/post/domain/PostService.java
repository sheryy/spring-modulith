package dev.sheraz.events.post.domain;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Service;

import dev.sheraz.events.post.event.PostCreated;
import dev.sheraz.events.post.event.PostPublished;
import dev.sheraz.events.post.exception.PostNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Domain service for managing posts.
 * Demonstrates how domain services publish events after state changes,
 * enabling loose coupling between modules.
 */
@Service
@RequiredArgsConstructor
@NamedInterface
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final ApplicationEventPublisher events;

    /**
     * Retrieves a post by ID.
     */
    Post get(int id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    /**
     * Creates a new post and publishes a PostCreated event.
     * This event will be handled by the Publisher module.
     */
    void create(Post post) {
        Post createdPost = postRepository.save(post);
        events.publishEvent(new PostCreated(createdPost.id()));
    }

    /**
     * Publishes a post and publishes a PostPublished event.
     * This event will be handled by the Archiver module.
     */
    public void publish(Integer id) {
        Post post = get(id).status(Post.Status.PUBLISHED);
        postRepository.save(post);
        events.publishEvent(new PostPublished(post.id()));
    }

    /**
     * Archives a post (no event published for this demo).
     */
    public void archive(Integer id) {
        Post post = get(id).status(Post.Status.ARCHIVED);
        postRepository.save(post);
    }

}
