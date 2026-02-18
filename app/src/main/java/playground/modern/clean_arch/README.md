# Clean Architecture

This mini-project demonstrates a framework-free, educational Clean Architecture flow in modern Java using records, explicit ports, and simple in-memory adapters.

## What This Mini-Project Demonstrates

- **Layered boundaries** with clear package separation: `domain`, `application`, and `api`
- **Use-case orchestration** in `PlaceOrderUseCase` instead of pushing business flow into controllers or repositories
- **Ports and adapters style** via `OrderRepository` and `PaymentGateway` interfaces
- **Rich domain modeling** with value objects (`Money`, `OrderId`) and immutable record types
- **Deterministic infrastructure fakes** (`InMemoryOrderRepository`, `FakePaymentGateway`) to keep behavior observable and testable

## Why It Matters

Clean Architecture keeps the business core independent of frameworks and external systems.

1. **Domain-first design** - business rules live in domain/application code, not in transport or persistence code.
2. **Replaceable dependencies** - swapping in a real DB or payment provider does not require changing core use-case logic.
3. **Testability by construction** - use cases accept interfaces, so tests can inject fakes without heavy setup.
4. **Teaching-friendly structure** - each layer has a focused responsibility that is easy to explain and evolve.

## Architecture Walkthrough

```text
clean_arch/
|- api/
|  `- CleanArchDemo.java                 # Composition root + runnable scenarios
|- application/
|  |- PlaceOrderUseCase.java             # Application service (workflow orchestration)
|  |- PlaceOrderCommand.java             # Input DTO for use case
|  |- PlaceOrderResult.java              # Output DTO for use case
|  |- OrderRepository.java               # Outbound port: persistence
|  |- PaymentGateway.java                # Outbound port: payments
|  |- InMemoryOrderRepository.java       # Adapter: in-memory persistence
|  |- FakePaymentGateway.java            # Adapter: deterministic payment simulator
|  `- PaymentResult.java                 # Payment gateway response model
|- domain/
|  |- Order.java                         # Aggregate root and state transitions
|  |- OrderItem.java                     # Line item value object
|  |- Money.java                         # Money value object (currency + minor units)
|  |- OrderId.java                       # Strongly typed order id
|  |- OrderStatus.java                   # Lifecycle status enum
|  `- OrderPolicy.java                   # Domain policy validation
`- README.md
```

## Dependency Direction

The dependency direction follows the Clean Architecture rule: outer layers depend on inner layers, never the opposite.

```text
api -> application -> domain
         |
         +-> ports (interfaces)
              ^
              |
          adapters (in-memory / fake implementations)
```

- `domain` has no dependency on `application` or `api`
- `application` depends on `domain` and abstractions (ports)
- `api` wires concrete implementations and triggers scenarios

## Use Case Flow: Place Order

`PlaceOrderUseCase.handle(...)` is the central educational flow:

1. Create a domain `Order` from incoming command data.
2. Validate domain policy with `OrderPolicy`.
3. If policy fails, mark order as rejected and persist.
4. Otherwise call `PaymentGateway` (port) to attempt charge.
5. Transition order state to `PAID` or `REJECTED`.
6. Persist through `OrderRepository` (port).
7. Return an application-level result DTO.

This keeps orchestration in one place while domain types stay focused on business meaning.

## Key Educational Concepts

### 1) Records as Domain and DTO Types

The project uses Java records for immutable state containers:

- Domain: `Order`, `OrderItem`, `Money`, `OrderId`
- Application DTOs: `PlaceOrderCommand`, `PlaceOrderResult`, `PaymentResult`

Benefits:
- Less boilerplate
- Built-in value semantics (`equals`, `hashCode`, `toString`)
- Easier reasoning about state transitions

### 2) Ports Keep Application Layer Stable

`OrderRepository` and `PaymentGateway` are application-level interfaces.

Benefits:
- Core logic does not care if implementation is in-memory, SQL, HTTP, or messaging
- Unit tests can inject fake adapters quickly
- Infrastructure can evolve independently

### 3) Domain Policy as Explicit Rule Object

`OrderPolicy` centralizes validation constraints (for example order amount/item count limits).

Benefits:
- Rules are discoverable and reusable
- Use case reads like business language
- Changes to policy stay localized

### 4) Manual Composition Root

`CleanArchDemo` wires dependencies explicitly (no DI framework required).

Benefits:
- Beginners can see dependency graph directly
- Easy to migrate later to Spring/Guice/CDI if needed
- Encourages constructor injection and clear ownership

## How To Run

From project root:

```bash
./gradlew runModern
```

Then select `Clean Architecture Demo` from the interactive menu.

## Suggested Study Path

1. Start at `api/CleanArchDemo.java` to see wiring and scenarios.
2. Read `application/PlaceOrderUseCase.java` top-to-bottom.
3. Inspect `domain/Order.java` and `domain/OrderPolicy.java` for business rules.
4. Compare port interfaces to adapter implementations.
5. Add a new adapter (for example file-based repository) without changing use-case logic.

## Trade-offs and Gotchas

- **More files and types** - the architecture is intentionally explicit, which can feel heavy for very small apps.
- **Extra mapping effort** - DTO/domain mapping cost grows with complexity.
- **Discipline required** - layers are useful only if boundaries are respected.
- **State transition naming consistency** - keep transition APIs consistent and intention-revealing when evolving this code.

## Practice Extensions

- Add an inbound port and separate command handler from demo runner.
- Introduce a `PAYMENT_PENDING` workflow and asynchronous payment confirmation.
- Add domain events (`OrderPlaced`, `PaymentFailed`) and publish via an adapter.
- Replace the fake gateway with a real HTTP client adapter behind `PaymentGateway`.
- Add focused tests for `OrderPolicy` and `PlaceOrderUseCase` with stubbed ports.

## Requirements

- Java 17+ (records and modern language features)
