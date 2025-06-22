package dev.sheraz.events;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ModularityTest {
    static ApplicationModules modules = ApplicationModules.of(EventsApplication.class);

    @Test
    void verifyModularStructure() {
        modules.verify().forEach(System.out::println);
    }

    @Test
    void generateDocumentation() {
        new Documenter(modules).writeDocumentation();
    }
}
