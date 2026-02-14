package playground.common;

/**
 * Interface for all demo programs in the playground.
 * Each demo must provide a name, description, and run implementation.
 */
public interface Demo {

    /**
     * Returns the name of this demo.
     */
    String name();

    /**
     * Returns a brief description of what this demo demonstrates.
     */
    String description();

    /**
     * Executes the demo.
     */
    void run();
}
