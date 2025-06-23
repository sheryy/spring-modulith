package dev.sheraz.events.post.domain;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduled job that automatically creates demo posts to trigger the event flow.
 * Runs every 15 seconds and creates 2-10 posts per batch.
 * This simulates real-world scenarios where posts are created continuously.
 */
@Component
@RequiredArgsConstructor
@Slf4j
class PostCreateJob {
    private static final int MAX_POSTS = 10;
    private final PostService postService;

    /**
     * Creates a batch of demo posts every 15 seconds.
     * Each post creation triggers a PostCreated event, starting the event flow.
     */
    @Transactional
    @Scheduled(initialDelay = 2000, fixedDelay = 15_000)
    public void run() {
        AtomicLong sequence = new AtomicLong(Instant.now().toEpochMilli());
        List<Post> posts = IntStream.range(0, getMaxPosts())
                .mapToObj(_ -> createPost(sequence.getAndIncrement()))
                .toList();
        posts.forEach(postService::create);
        log.info("Posts created ({}): {}", posts.size(), Thread.currentThread().getName());
    }

    /**
     * Returns a random number of posts to create (2-10).
     */
    int getMaxPosts() {
        return ThreadLocalRandom.current().nextInt(2, MAX_POSTS + 1);
    }

    /**
     * Creates a demo post with generated title and content.
     */
    Post createPost(long sequence) {
        return new Post().title("Post-" + sequence).content("Content-" + sequence);
    }
}
