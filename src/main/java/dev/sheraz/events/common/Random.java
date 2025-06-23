package dev.sheraz.events.common;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

/**
 * Utility class for simulating random failures in the demo.
 * Used to demonstrate how the system handles event processing failures
 * and how the retry mechanism works.
 */
@Component
public class Random {
    /**
     * Simulates a random failure with the given probability.
     * Used in event listeners to demonstrate reliable event processing.
     * 
     * @param probability Probability of failure (0.0 to 1.0)
     * @throws RuntimeException if the random check triggers a failure
     */
    public void exception(double probability) {
        // Simulate a random failure with a given probability
        if (ThreadLocalRandom.current().nextDouble() < probability) {
            throw new RuntimeException("Random failure with probability: " + probability);
        }
    }
}
