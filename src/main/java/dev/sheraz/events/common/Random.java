package dev.sheraz.events.common;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class Random {
    public void exception(double probability) {
        // Simulate a random failure with a given probability
        if (ThreadLocalRandom.current().nextDouble() < probability) {
            throw new RuntimeException("Simulated random failure");
        }
    }
}
