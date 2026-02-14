# Domain Modeling

This mini-project demonstrates modern Java domain modeling patterns using sealed classes, records, and pattern matching.

## What It Demonstrates

- **Sealed hierarchies** — Restrict which classes can extend a type, enabling exhaustive `switch` expressions
- **Records** — Immutable data carriers with automatic `equals`, `hashCode`, `toString`, and constructors
- **Pattern matching switch** — Type-safe dispatch without instanceof casts

## Why It Matters

In production systems, domain models represent core business concepts. Modern Java features help:

1. **Type safety** — Sealed classes ensure all variants are handled at compile time
2. **Immutability** — Records create immutable value objects with minimal boilerplate
3. **Expressiveness** — Pattern matching makes code read like business rules

## Key Concepts

### Sealed Hierarchy

```java
public sealed interface Payment permits CardPayment, BankTransfer, CryptoPayment {
    String id();
}
```

Benefits:
- Compiler enforces exhaustive pattern matching
- Clear domain model that shows all payment types
- Future-proof: new variants require updating `permits` clause

### Records as Value Objects

```java
public record CardPayment(String id, String cardNumber, BigDecimal amount) implements Payment {
    // Automatic: equals(), hashCode(), toString(), constructor
}
```

Benefits:
- Immutable by default
- Automatic implementation of `equals`/`hashCode` based on fields
- Removes boilerplate

### Pattern Matching Switch

```java
String process(Payment payment) {
    return switch (payment) {
        case CardPayment c -> "Card ending in " + c.cardNumber().substring(c.cardNumber().length() - 4);
        case BankTransfer b -> "IBAN: " + b.iban();
        case CryptoPayment x -> "Crypto: " + x.walletAddress();
    };
}
```

Benefits:
- Exhaustive: compiler warns if any case is missing
- Type-safe: no need for instanceof checks
- Readable: business rules directly in code

## Trade-offs / Gotchas

- **Sealed classes require module or same-package definition** — Can't be split across modules without opens
- **Records are final** — Cannot extend, but can implement interfaces
- **Pattern matching in switch requires Java 21+** — For full expressiveness

## How to Run

```bash
./gradlew runModern
# Select "Domain Modeling Demo" from the menu
```

Or run directly:

```bash
./gradlew run --args="domainmodel"
```

## Files

| File | Description |
|------|-------------|
| `Payment.java` | Sealed interface defining payment types |
| `CardPayment.java` | Record for card payments |
| `BankTransfer.java` | Record for bank transfers |
| `CryptoPayment.java` | Record for crypto payments |
| `PaymentProcessor.java` | Pattern matching switch examples |
| `DomainModelDemo.java` | Main demo runner |
| `README.md` | This file |
