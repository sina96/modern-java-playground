# Result Type Demo

A demonstration of functional error handling using the **Result type pattern** in modern Java.

## Overview

This module implements a `Result<T, E>` type inspired by functional programming languages (like Rust's `Result<T, E>` or Haskell's `Either`), providing an alternative to exception-based error handling. It leverages Java's **sealed interfaces** and **record classes** (introduced in Java 17+) to create a type-safe, composable error handling mechanism.

## Concepts

### What is a Result Type?

The Result type is a container that represents either a **success** value (`T`) or an **error** value (`E`). Unlike exceptions, which can bubble up unexpectedly, Result types make error handling explicit in the type system.

### Why Use Result Types?

| Aspect | Exceptions | Result Type |
|--------|------------|-------------|
| **Type Safety** | Runtime only | Compile-time |
| **Flow Control** | Implicit (throws) | Explicit (return value) |
| **Composability** | Requires try-catch | Chainable with `map`, `flatMap` |
| **Checked Errors** | Declared in signature | Part of return type |
| **Performance** | Stack trace overhead | Lightweight |

## File Structure

```
result/
├── models/
│   ├── Result.java       # Core sealed interface with transformation methods
│   ├── Success.java      # Record representing successful result
│   ├── Failure.java      # Record representing failed result
│   └── DomainError.java # Sealed hierarchy of domain-specific errors
├── ResultDemo.java       # Demo showcasing parsing, validation, and computation
└── README.md             # This file
```

## Key Features

### Core Operations

- **`isSuccess()` / `isFailure()`** - Check result state
- **`getOrThrow()`** - Get value or throw (not recommended for typical use)
- **`toOptional()`** - Convert to Java's `Optional`
- **`orElse()` / `orElseGet()`** - Provide fallback values

### Transformation Methods

| Method | Purpose |
|--------|---------|
| `map(fn)` | Transform success value, pass error through |
| `flatMap(fn)` | Chain operations that return Result |
| `mapError(fn)` | Transform error value, pass success through |
| `recover(fn)` | Attempt to recover from error |
| `fold(fn1, fn2)` | Handle both success and error cases |

### Factory Methods

- **`Result.ok(value)`** - Create a success result
- **`Result.err(error)`** - Create a failure result
- **`Result.fromOptional(opt, errSupplier)`** - Convert Optional to Result

## Usage Example

```java
// Parse and validate in a chain
Result<CustomerProfile, DomainError> profile = 
    parseInt("age", input)
        .flatMap(age -> validateRange("age", age, 18, 120))
        .flatMap(validAge -> parseInt("salary", input))
        .flatMap(salary -> validateMin("salary", salary, 0))
        .map(validated -> new CustomerProfile(validated.age, validated.salary));

// Handle result
String message = profile.fold(
    error -> "Error: " + error.message(),
    profile -> "Success: " + profile
);
```

---

## Focus Points

This section highlights key learning points from this implementation.

### 1. Sealed Interfaces for Type Safety

The `Result<T, E>` is a **sealed interface** that permits only `Success` and `Failure`. This enables exhaustive pattern matching with the `switch` expression, ensuring all cases are handled.

```java
return switch (this) {
    case Success<T, E> s -> s.value();
    case Failure<T, E> f -> f.error();
};
```

**Lesson**: Sealed interfaces let you define finite type hierarchies and control which classes can implement them.

### 2. Records for Immutable Data

`Success` and `Failure` are **record classes** (data carriers) with:
- Automatic `equals()`, `hashCode()`, `toString()`
- Immutable fields
- Concise constructor

**Lesson**: Use records for simple data holders to reduce boilerplate.

### 3. Default Methods for Composition

Default methods on the sealed interface allow chaining operations directly on the Result without external utilities.

**Lesson**: Default methods in interfaces enable adding behavior to existing types without breaking implementations.

### 4. Null Safety

All methods use `Objects.requireNonNull()` to fail fast on null inputs, and the implementation explicitly rejects null success/error values.

**Lesson**: Be explicit about nullability. The Result type models "absence of value" through the `Failure` variant, not through null.

### 5. Chaining with flatMap

`flatMap` is essential for chaining operations that can fail:

```java
Result<A, E> result = initial.flatMap(a -> operation(a));
```

If `initial` is a Failure, the error propagates without executing `operation`. If it's a Success, the function runs.

**Lesson**: Use `map` when transforming the value, `flatMap` when the transformation can also fail.

### 6. Error Type Hierarchies

`DomainError` is itself a sealed interface with specific error types (`ParseError`, `ValidationError`, `ComputationError`). This enables exhaustive handling of all error cases.

**Lesson**: Model your domain errors as a sealed hierarchy for compile-time completeness checks.

### 7. Functional Error Handling vs Exceptions

| Scenario | Exception Approach | Result Approach |
|----------|-------------------|-----------------|
| Missing value | `null` or `Optional` | `Failure` |
| Expected failure | `throws` declared | Return `Result.err()` |
| Unexpected failure | `RuntimeException` | Still return `Result.err()` |

**Lesson**: Result types make error handling explicit and composable, while exceptions are better suited for truly exceptional, unrecoverable situations.

---

## Further Exploration

- Try adding a `tap()` method that executes side-effects without changing the Result
- Implement `andThen()` as an alias for `flatMap()`
- Add railway-oriented programming concepts (happy/sad path visualization)
- Explore integrating with Java's `Either` from libraries like Vavr

## Requirements

- Java 17+ (for sealed interfaces, records, and switch expressions)
