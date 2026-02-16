package playground.modern.streams;

import playground.common.Demo;
import playground.modern.streams.models.Transaction;
import playground.modern.streams.models.TransactionFixtures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public final class StreamPitfallsDemo implements Demo
{

   void main()
   {
      List<Transaction> txs = TransactionFixtures.sampleTransactions();

      System.out.println("=== StreamPitfallsDemo ===");
      System.out.println();

      pitfallBoxingVsPrimitive(txs);
      System.out.println();

      pitfallIntermediateCollections(txs);
      System.out.println();

      pitfallSideEffects(txs);
      System.out.println();

      pitfallParallelMisuse(txs);
   }

   // -------------------------------------------------
   // Pitfall 1: Boxing overhead
   // -------------------------------------------------
   private static void pitfallBoxingVsPrimitive(List<Transaction> txs)
   {
      System.out.println("[Pitfall 1] Boxing vs primitive streams");

      // BAD: amountMinor() (long) gets boxed to Long due to map(...) returning Stream<Long>
      Long boxedTotal = txs.stream().map(Transaction::amountMinor) // boxing happens
            .reduce(0L, Long::sum);

      // GOOD: primitive stream, no boxing
      long primitiveTotal = txs.stream().mapToLong(Transaction::amountMinor).sum();

      System.out.println("boxedTotal     = " + boxedTotal);
      System.out.println("primitiveTotal = " + primitiveTotal);
      System.out.println("Note: prefer mapToLong/mapToInt/mapToDouble for numeric aggregation.");
   }

   // -------------------------------------------------
   // Pitfall 2: Accidental intermediate collections
   // -------------------------------------------------
   private static void pitfallIntermediateCollections(List<Transaction> txs)
   {
      System.out.println("[Pitfall 2] Intermediate collections");

      // BAD: creates a temporary list and re-streams it
      List<Transaction> groceries = txs.stream().filter(t -> t.category() == Transaction.Category.GROCERIES).toList();
      long badTotal = groceries.stream().mapToLong(Transaction::amountMinor).sum();

      // GOOD: fuse operations in one pipeline
      long goodTotal = txs.stream().filter(t -> t.category() == Transaction.Category.GROCERIES)
            .mapToLong(Transaction::amountMinor).sum();

      System.out.println("badTotal  (two pipelines) = " + badTotal);
      System.out.println("goodTotal (one pipeline)  = " + goodTotal);
      System.out.println("Note: intermediate lists add allocations and often reduce readability.");
   }

   // -------------------------------------------------
   // Pitfall 3: Side effects inside stream operations
   // -------------------------------------------------
   private static void pitfallSideEffects(List<Transaction> txs)
   {
      System.out.println("[Pitfall 3] Side effects (especially dangerous with parallel)");

      // BAD: mutating external state in a stream pipeline
      List<String> ids = new ArrayList<>();
      txs.stream().forEach(t -> ids.add(t.id())); // works here, but it's a bad habit

      // GOOD: collect results via stream terminal ops
      List<String> safeIds = txs.stream().map(Transaction::id).toList();

      System.out.println("ids.size()     = " + ids.size());
      System.out.println("safeIds.size() = " + safeIds.size());
      System.out.println("Note: side effects break referential transparency and become race-prone in parallel.");
   }

   // -------------------------------------------------
   // Pitfall 4: Parallel stream misuse / false assumptions
   // -------------------------------------------------
   private static void pitfallParallelMisuse(List<Transaction> txs)
   {
      System.out.println("[Pitfall 4] Parallel streams misuse");

      // BAD (performance): parallel + shared mutable state => contention.
      // AtomicLong is thread-safe (no lost updates), but frequent atomic updates can erase any parallel benefit.
      AtomicLong atomicTotal = new AtomicLong(0);
      txs.parallelStream().forEach(t -> atomicTotal.addAndGet(t.amountMinor()));

      // GOOD: use reduction (built-in is optimized and correct)
      long reducedTotal = txs.parallelStream().mapToLong(Transaction::amountMinor).sum();

      System.out.println("atomicTotal  (thread-safe, but contended) = " + atomicTotal.get());
      System.out.println("reducedTotal (parallel reduction)    = " + reducedTotal);
      System.out.println("Note: with parallel streams, prefer stateless operations/reductions and avoid shared mutable state (even if atomic).");
   }

   @Override
   public String name()
   {
      return "Stream Pitfalls";
   }

   @Override
   public String description()
   {
      return "Demonstrates common stream pitfalls and how to avoid them.";
   }

   @Override
   public void run()
   {
      this.main();
   }
}
