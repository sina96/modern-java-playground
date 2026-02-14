package playground.common;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Utility helpers for demos: timing, printing, and random data.
 */
public final class Util {

    private static final Random RANDOM = new Random();

    private Util() {
        // Utility class - no instantiation
    }

    // ─────────────────────────────────────────────────────────────────
    // Timing helpers
    // ─────────────────────────────────────────────────────────────────

    /**
     * Measures and prints the execution time of a Runnable.
     */
    public static void timedRun(String label, Runnable runnable) {
        long start = System.nanoTime();
        try {
            runnable.run();
        } finally {
            long duration = System.nanoTime() - start;
            System.out.printf("  [%s] completed in %.3f ms%n", label, duration / 1_000_000.0);
        }
    }

    /**
     * Measures and returns the execution time of a Runnable in milliseconds.
     */
    public static double measureMs(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        return (System.nanoTime() - start) / 1_000_000.0;
    }

    /**
     * Times a Supplier and returns its result.
     */
    public static <T> T timed(String label, Supplier<T> supplier) {
        long start = System.nanoTime();
        try {
            return supplier.get();
        } finally {
            long duration = System.nanoTime() - start;
            System.out.printf("  [%s] completed in %.3f ms%n", label, duration / 1_000_000.0);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // Printing helpers
    // ─────────────────────────────────────────────────────────────────

    /**
     * Prints a section header.
     */
    public static void section(String title) {
        System.out.println();
        System.out.println("── " + title + " ──");
    }

    /**
     * Prints a subsection header.
     */
    public static void subsection(String title) {
        System.out.println();
        System.out.println("  " + title + ":");
    }

    /**
     * Prints a key-value pair.
     */
    public static void print(String key, Object value) {
        System.out.printf("    %-30s : %s%n", key, value);
    }

    /**
     * Prints a horizontal rule.
     */
    public static void rule() {
        System.out.println("───────────────────────────────────────────────────────────────");
    }

    // ─────────────────────────────────────────────────────────────────
    // Random data helpers
    // ─────────────────────────────────────────────────────────────────

    /**
     * Returns a random int between min (inclusive) and max (inclusive).
     */
    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /**
     * Returns a random long between min and max.
     */
    public static long randomLong(long min, long max) {
        return min + RANDOM.nextLong() * (max - min);
    }

    /**
     * Returns a random element from an array.
     */
    @SafeVarargs
    public static <T> T randomOf(T... elements) {
        return elements[RANDOM.nextInt(elements.length)];
    }

    /**
     * Generates a random string of given length.
     */
    public static String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
