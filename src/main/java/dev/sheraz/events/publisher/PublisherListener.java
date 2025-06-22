package dev.sheraz.events.publisher;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import dev.sheraz.events.common.Random;
import dev.sheraz.events.post.PostPublishService;
import dev.sheraz.events.post.event.PostCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
class PublisherListener {
    private final Random random;
    private final PostPublishService postPublishService;

    @ApplicationModuleListener
    public void on(PostCreated event) {
        random.exception(0.3);
        log.info("Post created event received: {}", event.id());
        postPublishService.publish(event.id());
    }
}
