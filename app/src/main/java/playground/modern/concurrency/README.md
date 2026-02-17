# Concurrency Fundamentals

This mini-project demonstrates core Java concurrency concepts through small, runnable experiments focused on visibility, happens-before, atomicity, and contention.

## What This Mini-Project Demonstrates

- **Visibility failure without coordination** (`VisibilityBrokenDemo`) - a non-`volatile` flag can cause a worker thread to spin longer than expected because updates are not guaranteed to be observed.
- **Visibility fix with `volatile`** (`VisibilityFixedVolatileDemo`) - `volatile` establishes a happens-before relationship for reads/writes of the flag and makes stop signaling reliable.
- **Visibility fix with `synchronized`** (`VisibilityFixedSynchronizedDemo`) - monitor enter/exit provides both mutual exclusion and visibility guarantees.
- **Atomicity and throughput trade-offs** (`CounterComparisonDemo`) - compares `value++`, `synchronized`, `AtomicLong`, and `LongAdder` under multithreaded increments.

## Lessons Learned

- **`volatile` solves visibility, not compound atomicity** - it makes writes visible but does not make `value++` safe.
- **`synchronized` provides correctness first** - it guarantees visibility and atomicity, but may introduce lock contention.
- **`AtomicLong` is lock-free and correct for single-value counters** - good default for many concurrent counter cases.
- **`LongAdder` is often better for hot counters** - higher write scalability under contention, with a different internal cost model.
- **Concurrency bugs can be probabilistic** - the broken visibility demo may pass on one run and fail on another; this is expected with JIT/CPU timing differences.

## Setup Walkthrough

1. Make sure you are on Java 17+ and have the Gradle wrapper available (already included in this repo).
2. From the project root, run:

```bash
./gradlew runModern
```

3. In the CLI menu, select:
   - `Concurrency Fundamentals`

4. Read output section-by-section:
   - broken visibility example,
   - volatile fix,
   - synchronized fix,
   - counter comparison table.

## Files

```text
concurrency/
|- ConcurrencyFundamentals.java            # Orchestrates all concurrency demos
|- VisibilityBrokenDemo.java               # No happens-before; probabilistic visibility issue
|- VisibilityFixedVolatileDemo.java        # Visibility fix via volatile flag
|- VisibilityFixedSynchronizedDemo.java    # Visibility fix via synchronized accessors
|- CounterComparisonDemo.java              # Counter correctness/performance comparison
`- util/
   `- Harness.java                         # Printing/sleep/timing helpers
```

## Practical Tips While Running

- If `VisibilityBrokenDemo` shows few or zero stuck attempts, rerun it; outcomes depend on runtime conditions.
- Focus on the `delta` column in the counter comparison output:
  - `delta != 0` means lost updates (incorrect counter),
  - `delta == 0` means logically correct counting.
