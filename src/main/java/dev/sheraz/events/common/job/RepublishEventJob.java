package dev.sheraz.events.common.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
class RepublishEventJob {
    private final IncompleteEventPublications incompleteEventPublications;
    private final Duration duration = Duration.ofSeconds(5);

    @Scheduled(initialDelay = 2000, fixedDelay = 5_000)
    @SchedulerLock(name = "republishEvents", lockAtLeastFor = "5s")
    public void runJob1() {
        LockAssert.assertLocked();
        log.info("Running runJob1: {}", Thread.currentThread().getName());
        incompleteEventPublications.resubmitIncompletePublicationsOlderThan(duration);
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 2_000)
    @SchedulerLock(name = "republishEvents", lockAtLeastFor = "5s")
    public void runJob2() {
        LockAssert.assertLocked();
        log.info("Running runJob2: {}", Thread.currentThread().getName());
        incompleteEventPublications.resubmitIncompletePublicationsOlderThan(duration);
    }
}
