package playground.modern.concurrency;

import playground.common.Demo;
import playground.modern.concurrency.util.Harness;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;


/**
 * Demonstrates atomicity and contention trade-offs:
 * - naive ++ (broken under concurrency)
 * - synchronized
 * - AtomicLong
 * - LongAdder
 */

public class CounterComparisonDemo implements Demo
{
   @Override
   public String name()
   {
      return "Counter Comparison";
   }

   @Override
   public String description()
   {
      return "Demonstrates atomicity and contention trade-offs";
   }

   @Override
   public void run()
   {
      Harness.header("CounterComparisonDemo (atomicity + contention)");

      int threads = Math.max(2, Runtime.getRuntime().availableProcessors() * 2);
      int incrementsPerThread = 200_000;

      long expected = (long) threads * incrementsPerThread;

      Harness.bullet("threads=" + threads + ", incrementsPerThread=" + incrementsPerThread + ", expected=" + expected);
      System.out.println();

      runCase("NaiveCounter (broken: value++)", new NaiveCounter(), threads, incrementsPerThread, expected);
      runCase("SynchronizedCounter", new SynchronizedCounter(), threads, incrementsPerThread, expected);
      runCase("AtomicLongCounter", new AtomicLongCounter(), threads, incrementsPerThread, expected);
      runCase("LongAdderCounter", new LongAdderCounter(), threads, incrementsPerThread, expected);

      System.out.println();
      Harness.bullet("Interpretation:");
      Harness.bullet("NaiveCounter loses updates because ++ is read-modify-write, not atomic.");
      Harness.bullet("synchronized is correct but may contend.");
      Harness.bullet("AtomicLong is correct; can contend under heavy updates.");
      Harness.bullet("LongAdder scales better for hot counters (more memory, eventual sum).");
   }

   private static void runCase(
         String name,
         Counter counter,
         int threads,
         int incrementsPerThread,
         long expected
   ) {
      long elapsedMs = Harness.timeMilis(() -> runThreads(counter, threads, incrementsPerThread));
      long actual = counter.get();
      long delta = expected - actual;

      System.out.printf("%-30s | actual=%d | delta=%d | time=%dms%n",
            name, actual, delta, elapsedMs);
   }

   private static void runThreads(Counter counter, int threads, int incrementsPerThread) {
      List<Thread> list = new ArrayList<>(threads);

      for (int i = 0; i < threads; i++) {
         Thread t = new Thread(() -> {
            for (int j = 0; j < incrementsPerThread; j++) {
               counter.inc();
            }
         }, "counter-worker-" + i);
         list.add(t);
      }

      list.forEach(Thread::start);

      for (Thread t : list) {
         try {
            t.join();
         } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while joining threads", ie);
         }
      }
   }

   // -------------------------------------------------
   // Counter abstraction
   // -------------------------------------------------
   interface Counter {
      void inc();
      long get();
   }

   // -------------------------------------------------
   // Implementations
   // -------------------------------------------------

   static final class NaiveCounter implements Counter {
      private long value;

      @Override public void inc() { value++; } // NOT ATOMIC
      @Override public long get() { return value; }
   }

   static final class SynchronizedCounter implements Counter {
      private long value;

      @Override public synchronized void inc() { value++; }
      @Override public synchronized long get() { return value; }
   }

   static final class AtomicLongCounter implements Counter {
      private final AtomicLong value = new AtomicLong();

      @Override public void inc() { value.incrementAndGet(); }
      @Override public long get() { return value.get(); }
   }

   static final class LongAdderCounter implements Counter {
      private final LongAdder adder = new LongAdder();

      @Override public void inc() { adder.increment(); }
      @Override public long get() { return adder.sum(); }
   }
}
