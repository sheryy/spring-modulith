package dev.sheraz.events.common.job;

import java.time.Duration;

import org.springframework.modulith.events.IncompleteEventPublications;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

/**
 * Background job that ensures reliable event processing by retrying failed events.
 * Demonstrates how to handle event processing failures in a production environment.
 * Uses ShedLock to prevent duplicate job execution in clustered environments.
 */
@Component
@RequiredArgsConstructor
@Slf4j
class RepublishEventJob {
    private final IncompleteEventPublications incompleteEventPublications;
    private final Duration duration = Duration.ofSeconds(5);

    /**
     * Retries failed event publications older than 5 seconds.
     * Runs every 5 seconds and uses ShedLock to ensure only one instance runs at a time.
     * This ensures that failed events (due to random exceptions) are eventually processed.
     */
    @Scheduled(initialDelay = 2000, fixedDelay = 5_000)
    @SchedulerLock(name = "republishEvents", lockAtLeastFor = "5s")
    public void runJob1() {
        LockAssert.assertLocked();
        log.info("Running runJob1: {}", Thread.currentThread().getName());
        incompleteEventPublications.resubmitIncompletePublicationsOlderThan(duration);
    }

    /**
     * Alternative job that also retries failed events.
     * Demonstrates how multiple jobs can share the same ShedLock name
     * to ensure only one runs at a time across the cluster.
     */
    @Scheduled(initialDelay = 2000, fixedDelay = 2_000)
    @SchedulerLock(name = "republishEvents", lockAtLeastFor = "5s")
    public void runJob2() {
        LockAssert.assertLocked();
        log.info("Running runJob2: {}", Thread.currentThread().getName());
        incompleteEventPublications.resubmitIncompletePublicationsOlderThan(duration);
    }
}
