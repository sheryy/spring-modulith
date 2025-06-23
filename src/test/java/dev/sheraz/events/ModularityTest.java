package dev.sheraz.events;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * Tests for verifying the modular structure of the application.
 * Demonstrates Spring Modulith's capabilities for ensuring proper module boundaries
 * and generating documentation.
 */
public class ModularityTest {
    static ApplicationModules modules = ApplicationModules.of(EventsApplication.class);

    /**
     * Verifies that the application follows proper modular structure.
     * Checks for circular dependencies and ensures module boundaries are respected.
     */
    @Test
    void verifyModularStructure() {
        modules.verify().forEach(System.out::println);
    }

    /**
     * Generates documentation for the modular structure.
     * Creates HTML documentation showing module relationships and dependencies.
     * Output is saved to target/spring-modulith-docs/
     */
    @Test
    void generateDocumentation() {
        new Documenter(modules).writeDocumentation();
    }
}
