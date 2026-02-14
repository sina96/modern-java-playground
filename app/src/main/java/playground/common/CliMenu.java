package playground.common;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Simple CLI menu for selecting and running demos.
 */
public class CliMenu {

    private final DemoRegistry registry;
    private final Scanner scanner;

    public CliMenu(DemoRegistry registry) {
        this.registry = registry;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the menu and handles user interaction.
     * Returns true if a demo was run, false if user chose to exit.
     */
    public boolean show() {
        printHeader();

        if (registry.isEmpty()) {
            System.out.println("  No demos available yet.");
            System.out.println();
            return false;
        }

        printDemos();
        System.out.println();
        printInstructions();

        int choice = readChoice();
        return handleChoice(choice);
    }

    private void printHeader() {
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("  " + registry.routeName());
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    private void printDemos() {
        for (int i = 0; i < registry.size(); i++) {
            Demo demo = registry.demos().get(i);
            System.out.printf("  [%d] %s%n", i + 1, demo.name());
            System.out.printf("      %s%n", demo.description());
        }
    }

    private void printInstructions() {
        System.out.println("───────────────────────────────────────────────────────────────");
        System.out.println("  Enter number to run demo, or 0 to exit");
    }

    private int readChoice() {
        System.out.print("  > ");
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NoSuchElementException | IllegalStateException | NumberFormatException e) {
            // No input available (non-interactive), default to exit
            return 0;
        }
    }

    private boolean handleChoice(int choice) {
        if (choice == 0) {
            System.out.println("  Goodbye!");
            return false;
        }

        var demoOpt = registry.get(choice - 1);
        if (demoOpt.isEmpty()) {
            System.out.println("  Invalid choice. Try again.");
            return true;
        }

        runDemo(demoOpt.get());
        return true;
    }

    private void runDemo(Demo demo) {
        System.out.println();
        System.out.println("───────────────────────────────────────────────────────────────");
        System.out.println("  Running: " + demo.name());
        System.out.println("───────────────────────────────────────────────────────────────");
        try {
            demo.run();
        } catch (Exception e) {
            System.err.println("  Error running demo: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }

    /**
     * Closes the underlying scanner.
     */
    public void close() {
        scanner.close();
    }
}
