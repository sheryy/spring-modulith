package dev.sheraz.events.archiver;

import dev.sheraz.events.common.Random;
import dev.sheraz.events.post.domain.PostService;
import dev.sheraz.events.post.event.PostPublished;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class ArchiverEventListener {
    private final Random random;
    private final PostService postService;

    @ApplicationModuleListener
    public void on(PostPublished event) {
        random.exception(0.3);
        log.info("PostPublished event: {}", event.id());
        postService.archive(event.id());
    }
}
