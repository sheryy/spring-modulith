package dev.sheraz.events.publisher;

import dev.sheraz.events.common.Random;
import dev.sheraz.events.post.domain.PostService;
import dev.sheraz.events.post.event.PostCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class PublisherEventListener {
    private final Random random;
    private final PostService postService;

    @ApplicationModuleListener
    public void on(PostCreated event) {
        random.exception(0.3);
        log.info("PostCreated event: {}", event.id());
        postService.publish(event.id());
    }

    @Deprecated(forRemoval = true)
    @ApplicationModuleListener(id = "dev.sheraz.events.publisher.PublisherListener.on(dev.sheraz.events.post.event.PostCreated)")
    public void onPostCreated(PostCreated event) {
        on(event);
    }
}
