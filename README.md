# modern-java-playground - Work in Progress

A collection of modern Java demos and patterns, organized into two routes:

- **Route A: Modern Java Approaches** — Generic patterns portable across Java 17-25
- **Route B: Java 25 Features** — Java 25-specific features (may require preview flags)

## Quick Start

```bash
# Run Route A (Modern Java Approaches)
./gradlew runModern

# Run Route B (Java 25 Features)
./gradlew runJdk25

# Or use the default run task with property
./gradlew run -Pmain=playground.jdk25.Jdk25Main
```

## Project Structure

```
src/main/java/playground/
├── common/           # Shared infrastructure (Demo, CliMenu, Util)
├── modern/           # Route A: Modern Java Approaches
│   └── ModernMain.java
└── jdk25/            # Route B: Java 25 Features
    └── Jdk25Main.java
```

## Route A: Modern Java Approaches

| Mini Project | Description |
|--------------|-------------|
| Domain Modeling | Sealed hierarchies, records, pattern matching |
| Result/Either API | Explicit error handling without exceptions |
| Streams + Collectors | Custom collectors, avoiding pitfalls |
| Concurrency Fundamentals | JMM, volatiles, atomics |
| Clean Architecture | Domain-driven design, framework-free |

## Route B: Java 25 Features

| Mini Project | Description |
|--------------|-------------|
| Language Ergonomics | JEP 511-513 (Module Imports, Compact Source, Flexible Constructors) |
| Scoped Values | Request context propagation |
| Structured Concurrency | Preview feature, task cancellation |
| Virtual Threads | Platform vs virtual thread comparison |
| Security/Crypto | KDF, PEM support |
| JFR Observability | Method timing and tracing |

## Requirements

- Java 25 (JDK toolchain configured via Gradle)
- Gradle 9.x (wrapper included)

## Running Demos

Each route provides an interactive CLI menu. Select a demo by number, or 0 to exit.

```bash
$ ./gradlew runModern

═══════════════════════════════════════════════════════════════
  Route A: Modern Java Approaches
═══════════════════════════════════════════════════════════════
  [1] Domain Modeling Demo
      ...

  Enter number to run demo, or 0 to exit
  > 1
```

## Adding New Demos

1. Create your demo class implementing `playground.common.Demo`
2. Register it in the appropriate `*Main.java` file:
   ```java
   registry.register(new MyDemo());
   ```

## License

MIT
