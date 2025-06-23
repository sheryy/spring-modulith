package dev.sheraz.events.common;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class Random {
    public void exception(double probability) {
        // Simulate a random failure with a given probability
        if (ThreadLocalRandom.current().nextDouble() < probability) {
            throw new RuntimeException("Random failure with probability: " + probability);
        }
    }
}
