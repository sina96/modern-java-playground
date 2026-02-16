package playground.modern;

import playground.common.CliMenu;
import playground.common.Demo;
import playground.common.DemoRegistry;
import playground.common.Util;
import playground.modern.domainmodel.DomainModelDemo;
import playground.modern.result.ResultDemo;
import playground.modern.streams.StreamPitfallsDemo;
import playground.modern.streams.StreamsDemo;


/**
 * Main entry point for Route A: Modern Java Approaches.
 * Demonstrates patterns and features that work across Java 17-25.
 */
public class ModernMain implements Demo {

    private final DemoRegistry registry;

    public ModernMain() {
        this.registry = new DemoRegistry("Route A: Modern Java Approaches");
        registerDemos();
    }

    private void registerDemos() {
        // Demos will be registered here as they are implemented
        registry.register(new SetupVerificationDemo());
        registry.register(new DomainModelDemo());
        registry.register(new ResultDemo());
        registry.register(new StreamsDemo());
        registry.register(new StreamPitfallsDemo());
        // Add more demos here as they are implemented
    }

    @Override
    public String name() {
        return "Route A: Modern Java Approaches";
    }

    @Override
    public String description() {
        return "Generic modern Java engineering patterns (portable across Java 17-25)";
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
    static void main() {
        new ModernMain().run();
    }

    /**
     * Placeholder demo to verify the setup works.
     * Remove this once real demos are added.
     */
    private static class SetupVerificationDemo implements Demo {

        @Override
        public String name() {
            return "Setup Verification";
        }

        @Override
        public String description() {
            return "Verifies the demo framework is working correctly";
        }

        @Override
        public void run() {
            Util.section("Setup Verification");
            System.out.println("  The demo framework is successfully configured!");
            System.out.println("  Java version: " + System.getProperty("java.version"));
            System.out.println("  This is a placeholder demo.");
            System.out.println("  Add real demos to the ModernMain class to get started.");
            Util.rule();
        }
    }
}
