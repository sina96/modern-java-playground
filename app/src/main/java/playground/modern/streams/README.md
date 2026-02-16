# Streams + Collectors

This mini-project demonstrates practical Java Streams patterns, a small custom `Collector`, and common stream pitfalls (with safer alternatives).

## What It Demonstrates

- **Aggregation without boxing** using primitive streams (`mapToLong().sum()`)
- **Top-N queries** with `sorted().limit()` and a clear comparator
- **Grouping with downstream collectors** (`groupingBy + summingLong/counting`)
- **A custom Collector** (`Collector<T, A, R>`) that builds an immutable summary object
- **Pitfalls**: side effects, accidental intermediate collections, and parallel stream misuse

## Why It Matters

Streams are great for *expressing transformations and aggregations*, but they are easy to misuse:

- Small mistakes (boxing, side effects, extra allocations) can quietly degrade performance or correctness
- Parallel streams are not a default; they require associative reductions and a careful data/CPU profile
- Custom collectors are powerful, but you must follow the `Collector` contract for correctness

## How To Run

```bash
./gradlew runModern
# Select:
# - "Streams Demo"
# - "Stream Pitfalls"
```

## File Structure

```text
streams/
├── StreamsDemo.java                 # Good stream patterns + custom collector usage
├── StreamPitfallsDemo.java          # Common mistakes + safer alternatives
└── models/
    ├── Transaction.java             # Simple transaction model
    ├── TransactionFixtures.java     # Sample data used by the demos
    ├── TransactionSummary.java      # Immutable summary output
    └── TransactionSummaryCollector.java # Custom Collector implementation
```

## Focus Points

### 1) Prefer Primitive Streams for Numeric Work

When the source data is numeric, avoid `Stream<Long>` unless you truly need boxed values.

```java
long total = txs.stream()
    .mapToLong(Transaction::amountMinor)
    .sum();
```

Practical tip: `mapToInt/mapToLong/mapToDouble` + `sum/average/summaryStatistics` covers most cases.

### 2) Grouping With Downstream Collectors

Grouping becomes expressive when you attach a downstream collector:

```java
Map<String, Long> spendByMerchant = txs.stream()
    .collect(Collectors.groupingBy(
        Transaction::merchant,
        Collectors.summingLong(Transaction::amountMinor)
    ));
```

This avoids manual map mutation and keeps the pipeline declarative.

### 3) Custom Collector: Build Once, Return an Immutable Result

`TransactionSummaryCollector` shows the core pieces of the collector API:

- `supplier()` creates the mutable accumulator (`A`)
- `accumulator()` folds one element into `A`
- `combiner()` merges two accumulators (required for parallel)
- `finisher()` converts `A` into the final result (`R`)
- `characteristics()` declares optimizations (kept empty here for clarity)

The finisher returns a `TransactionSummary` with defensive copies (`Map.copyOf(...)`) so callers cannot mutate the summary.

### 4) Keep Pipelines Single-Pass When It Improves Clarity

Avoid materializing an intermediate list just to immediately stream it again:

```java
long groceriesTotal = txs.stream()
    .filter(t -> t.category() == Transaction.Category.GROCERIES)
    .mapToLong(Transaction::amountMinor)
    .sum();
```

This is often more readable and reduces allocations.

### 5) Side Effects and Parallel Streams

Streams are easiest to reason about when operations are *pure* (no external mutation). This becomes critical with `parallelStream()`, where side effects can introduce races or contention.

If you do go parallel, prefer built-in reductions and collectors over shared mutable state.

## Lessons Learned

- **Boxing is easy to trigger accidentally**; primitive streams make numeric intent explicit.
- **Downstream collectors are the “power tool”** of `groupingBy` (counting, summing, mapping, collectingAndThen).
- **Custom collectors must have a correct combiner**; even if you run sequentially today, someone will try parallel tomorrow.
- **Choose identity values carefully** when implementing min/max/reductions; wrong identities silently produce wrong answers.
- **Parallel streams are a design choice**; measure first, and ensure operations are associative and stateless.

## Further Exploration

- Replace parts of the custom collector with built-ins like `LongSummaryStatistics` (`summaryStatistics()`).
- Try `Collectors.teeing(...)` to compute two aggregates in one pass.
- Add a variant that groups by `(merchant, category)` using a composite key (record) and a downstream collector.
- Compare `stream().sorted().limit(n)` with `Collectors.toCollection(PriorityQueue::new)` for large datasets.

## Requirements

- Java 17+ (records + `Stream.toList()`)
