package playground.modern.concurrency;

import playground.common.Demo;
import playground.modern.concurrency.util.Harness;

import java.util.concurrent.CountDownLatch;

/**
 * Same scenario as VisibilityBrokenDemo, but the flag is volatile.
 * volatile write/read establishes happens-before and ensures visibility.
 */

public class VisibilityFixedVolatileDemo implements Demo
{

   private static final int ATTEMPTS = 10;
   private static final long SPIN_TIMEOUT_MS = 150;

   private volatile boolean running;

   @Override
   public String name()
   {
      return "Visibility Fixed Volatile Demo";
   }

   @Override
   public String description()
   {
      return "Demonstrates how volatile flag can be used to ensure visibility and ordering guarantees";
   }

   @Override
   public void run()
   {
      Harness.header("VisibilityFixedVolatileDemo (volatile flag)");

      Harness.bullet("Expected: worker reliably observes running=false and stops.");
      Harness.bullet("volatile provides visibility + ordering guarantees (but not atomicity for compound ops).");

      int stopped = 0;

      for (int i = 1; i <= ATTEMPTS; i++) {
         if (runOnce(i)) stopped++;
      }

      System.out.println();
      System.out.println("Summary:");
      System.out.println("  stoppedWithinTimeout = " + stopped + " / " + ATTEMPTS);
   }

   private boolean runOnce(int attempt) {
      running = true;

      CountDownLatch started = new CountDownLatch(1);

      Thread worker = new Thread(() -> {
         started.countDown();

         long x = 0;
         while (running) {
            x++;
            if ((x & 0xFF) == 0) {
               Thread.onSpinWait();
            }
         }
      }, "jmm-worker-volatile");

      worker.start();
      Harness.await(started, 50);

      Harness.sleep(5);
      running = false;

      boolean stopped = Harness.join(worker, SPIN_TIMEOUT_MS);
      System.out.println("Attempt " + attempt + ": " + (stopped ? "STOPPED" : "UNEXPECTEDLY STUCK"));
      return stopped;
   }
}
