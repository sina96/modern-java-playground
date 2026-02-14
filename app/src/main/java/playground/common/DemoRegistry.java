package playground.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Registry for collecting and managing demos.
 * Maintains an ordered list of registered demos.
 */
public class DemoRegistry {

    private final String routeName;
    private final List<Demo> demos = new ArrayList<>();

    public DemoRegistry(String routeName) {
        this.routeName = routeName;
    }

    /**
     * Registers a demo to this registry.
     */
    public void register(Demo demo) {
        demos.add(demo);
    }

    /**
     * Returns the name of this route.
     */
    public String routeName() {
        return routeName;
    }

    /**
     * Returns an unmodifiable list of all registered demos.
     */
    public List<Demo> demos() {
        return Collections.unmodifiableList(demos);
    }

    /**
     * Returns the demo at the given index.
     */
    public Optional<Demo> get(int index) {
        if (index >= 0 && index < demos.size()) {
            return Optional.of(demos.get(index));
        }
        return Optional.empty();
    }

    /**
     * Returns the number of registered demos.
     */
    public int size() {
        return demos.size();
    }

    /**
     * Returns true if no demos are registered.
     */
    public boolean isEmpty() {
        return demos.isEmpty();
    }
}
