package dev.sheraz.events.publisher;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import dev.sheraz.events.common.Random;
import dev.sheraz.events.post.domain.PostService;
import dev.sheraz.events.post.event.PostCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Publisher module that listens to PostCreated events.
 * Demonstrates how modules can react to events from other modules without direct dependencies.
 * Includes failure simulation to demonstrate reliable event processing.
 */
@Component
@RequiredArgsConstructor
@Slf4j
class PublisherEventListener {
    private final Random random;
    private final PostService postService;

    /**
     * Handles PostCreated events by automatically publishing the post.
     * Simulates a 30% failure rate to demonstrate event retry mechanisms.
     * This method shows loose coupling - the Publisher module doesn't directly depend on the Post module.
     */
    @ApplicationModuleListener
    public void on(PostCreated event) {
        random.exception(0.3); // 30% chance of failure to demonstrate reliability
        log.info("PostCreated event: {}", event.id());
        postService.publish(event.id()); // This will trigger a PostPublished event
    }

    /**
     * @deprecated Legacy method kept for demonstration purposes.
     * Shows how event listener IDs can be explicitly specified.
     */
    @Deprecated(forRemoval = true)
    @ApplicationModuleListener(id = "dev.sheraz.events.publisher.PublisherListener.on(dev.sheraz.events.post.event.PostCreated)")
    public void onPostCreated(PostCreated event) {
        on(event);
    }
}
