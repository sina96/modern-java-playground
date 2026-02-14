package playground.jdk25;

import playground.common.CliMenu;
import playground.common.Demo;
import playground.common.DemoRegistry;
import playground.common.Util;

/**
 * Main entry point for Route B: Java 25 Specific Features.
 * Demonstrates Java 25-specific features and platform upgrades.
 */
public class Jdk25Main implements Demo {

    private final DemoRegistry registry;

    public Jdk25Main() {
        this.registry = new DemoRegistry("Route B: Java 25 Features");
        registerDemos();
    }

    private void registerDemos() {
        // Demos will be registered here as they are implemented
        // Placeholder demo to show the route exists
        registry.register(new Jdk25PlaceholderDemo());
    }

    @Override
    public String name() {
        return "Route B: Java 25 Features";
    }

    @Override
    public String description() {
        return "Java 25-specific features (may require preview/experimental flags)";
    }

    @Override
    public void run() {
        CliMenu menu = new CliMenu(registry);
        while (menu.show()) {
            // Continue showing menu until user exits
        }
        menu.close();
    }

    /**
     * Entry point for running this route directly.
     */
    public static void main(String[] args) {
        new Jdk25Main().run();
    }

    /**
     * Placeholder demo indicating this route is under construction.
     */
    private static class Jdk25PlaceholderDemo implements Demo {

        @Override
        public String name() {
            return "Coming Soon";
        }

        @Override
        public String description() {
            return "Java 25 demos will be added in Week 2";
        }

        @Override
        public void run() {
            Util.section("Route B: Java 25 Features");
            System.out.println("  This route is under construction.");
            System.out.println("  Java 25 demos will be added in Week 2 of the plan.");
            System.out.println();
            System.out.println("  Planned demos include:");
            System.out.println("  - Module Import Declarations (JEP 511)");
            System.out.println("  - Compact Source Files (JEP 512)");
            System.out.println("  - Flexible Constructor Bodies (JEP 513)");
            System.out.println("  - Scoped Values");
            System.out.println("  - Structured Concurrency");
            System.out.println("  - Virtual Threads");
            System.out.println("  - Cryptography (KDF)");
            System.out.println("  - JFR Observability");
            Util.rule();
        }
    }
}
