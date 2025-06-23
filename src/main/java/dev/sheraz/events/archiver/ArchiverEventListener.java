package dev.sheraz.events.archiver;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import dev.sheraz.events.common.Random;
import dev.sheraz.events.post.domain.PostService;
import dev.sheraz.events.post.event.PostPublished;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Archiver module that listens to PostPublished events.
 * Demonstrates the final step in the event flow: archiving published posts.
 * Shows how multiple modules can react to the same event chain independently.
 */
@Component
@RequiredArgsConstructor
@Slf4j
class ArchiverEventListener {
    private final Random random;
    private final PostService postService;

    /**
     * Handles PostPublished events by automatically archiving the post.
     * Simulates a 30% failure rate to demonstrate event retry mechanisms.
     * This completes the event flow: Create → Publish → Archive
     */
    @ApplicationModuleListener
    public void on(PostPublished event) {
        random.exception(0.3); // 30% chance of failure to demonstrate reliability
        log.info("PostPublished event: {}", event.id());
        postService.archive(event.id());
    }
}
