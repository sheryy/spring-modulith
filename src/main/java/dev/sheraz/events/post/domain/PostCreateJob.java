package dev.sheraz.events.post.domain;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
class PostCreateJob {
    private static final int MAX_POSTS = 10;
    private final PostService postService;

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

    int getMaxPosts() {
        return ThreadLocalRandom.current().nextInt(2, MAX_POSTS + 1);
    }

    Post createPost(long sequence) {
        return new Post().title("Post-" + sequence).content("Content-" + sequence);
    }
}
