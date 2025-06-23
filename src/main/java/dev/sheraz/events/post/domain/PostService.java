package dev.sheraz.events.post.domain;

import dev.sheraz.events.post.event.PostCreated;
import dev.sheraz.events.post.event.PostPublished;
import dev.sheraz.events.post.exception.PostNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@NamedInterface
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final ApplicationEventPublisher events;

    Post get(int id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    void create(Post post) {
        Post createdPost = postRepository.save(post);
        events.publishEvent(new PostCreated(createdPost.id()));
    }

    public void publish(Integer id) {
        Post post = get(id).status(Post.Status.PUBLISHED);
        postRepository.save(post);
        events.publishEvent(new PostPublished(post.id()));
    }

    public void archive(Integer id) {
        Post post = get(id).status(Post.Status.ARCHIVED);
        postRepository.save(post);
    }

}
