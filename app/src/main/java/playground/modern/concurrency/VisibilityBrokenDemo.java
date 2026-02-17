package playground.modern.concurrency;

import playground.common.Demo;
import playground.modern.concurrency.util.Harness;

import java.util.concurrent.CountDownLatch;

/**
 * Demonstrates a classic visibility issue:
 * One thread spins on a non-volatile flag. Another thread updates it.
 * Without a happens-before relationship, the spinning thread may never observe the update.
 * <p>
 * IMPORTANT:
 * - This is probabilistic by nature. We run multiple attempts and count "stuck" outcomes.
 * - We always enforce a timeout so the demo never hangs.
 */

public class VisibilityBrokenDemo implements Demo
{

   private static final int ATTEMPTS = 20;
   private static final long SPIN_TIMEOUT_MS = 150;

   private boolean running;  // deliberately NOT volatile

   @Override
   public String name()
   {
      return "Visibility Broken Demo";
   }

   @Override
   public String description()
   {
      return "Demonstrates a common Java memory model violation: visibility broken due to lack of volatile.";
   }

   @Override
   public void run()
   {
      Harness.header("VisibilityBrokenDemo (no volatile, no synchronized)");

      Harness.bullet("Expected: worker stops quickly after main sets running=false.");
      Harness.bullet("Reality: worker may not observe the write (no happens-before) and keeps spinning.");
      Harness.bullet("This demo is probabilistic. We'll run " + ATTEMPTS + " attempts with a timeout");

      int stuck = 0;
      int stopped = 0;

      for (int i = 1; i <= ATTEMPTS; i++) {
         boolean ok = runOnce(i);
         if (ok) stopped++;
         else stuck++;
      }
      System.out.println();
      System.out.println("Summary:");
      System.out.println("  stoppedWithinTimeout = " + stopped);
      System.out.println("  stuckWithinTimeout   = " + stuck);
      System.out.println("Note: If stuck==0 on your machine, re-run. JIT/CPU changes can affect the outcome.");
   }

   private boolean runOnce(int attempt) {
      running = true;

      CountDownLatch started = new CountDownLatch(1);

      Thread worker = new Thread(() -> {
         started.countDown();

         // Busy-spin: encourages JIT to hoist reads and makes the visibility issue more likely.
         long x = 0;
         while (running) {
            x++;
            if ((x & 0xFF) == 0) {
               Thread.onSpinWait();
            }
         }
      }, "jmm-worker-broken");

      worker.start();

      // Ensure worker started
      Harness.await(started, 50);

      // Give it a brief moment to get into the loop
      Harness.sleep(5);

      // Write from main thread
      running = false;

      // Join with timeout: if it doesn't stop, we consider it "stuck" for this attempt.
      boolean stopped = Harness.join(worker, SPIN_TIMEOUT_MS);

      System.out.println("Attempt " + attempt + ": " + (stopped ? "STOPPED" : "STUCK (visibility issue)"));

      // Safety: if stuck, try to unblock (doesn't guarantee, but we already time-boxed)
      if (!stopped) {
         // Interrupt doesn't fix visibility, but helps if the worker ever checks interrupted status (it doesn't).
         worker.interrupt();
      }

      return stopped;
   }

}
