package playground.modern.concurrency;

import playground.common.Demo;
import playground.modern.concurrency.util.Harness;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * Demonstrates visibility using synchronized:
 * monitor enter/exit creates happens-before between threads.
 */

public class VisibilityFixedSynchronizedDemo implements Demo
{

   private static final int ATTEMPTS = 10;
   private static final long SPIN_TIMEOUT_MS = 150;

   private final StopFlag flag = new StopFlag();
   @Override
   public String name()
   {
      return "Visibility Fixed Synchronized Demo";
   }

   @Override
   public String description()
   {
      return "Demonstrates how synchronized flag can be used to ensure visibility and ordering guarantees";
   }

   public void run() {
      Harness.header("VisibilityFixedSynchronizedDemo (synchronized accessors)");

      Harness.bullet("Expected: worker reliably observes stop via synchronized reads/writes.");
      Harness.bullet("synchronized provides both mutual exclusion and visibility (happens-before).");

      int stopped = 0;

      for (int i = 1; i <= ATTEMPTS; i++) {
         if (runOnce(i)) stopped++;
      }

      System.out.println();
      System.out.println("Summary:");
      System.out.println("  stoppedWithinTimeout = " + stopped + " / " + ATTEMPTS);
   }

   private boolean runOnce(int attempt) {
      flag.start();

      CountDownLatch started = new CountDownLatch(1);

      Thread worker = new Thread(() -> {
         started.countDown();

         long x = 0;
         while (flag.isRunning()) {
            x++;
            if ((x & 0xFF) == 0) {
               Thread.onSpinWait();
            }
         }
      }, "jmm-worker-sync");

      worker.start();
      await(started, 50);

      Harness.sleep(5);
      flag.stop();

      boolean stopped = join(worker, SPIN_TIMEOUT_MS);
      System.out.println("Attempt " + attempt + ": " + (stopped ? "STOPPED" : "UNEXPECTEDLY STUCK"));
      return stopped;
   }

   private static final class StopFlag {
      private boolean running;

      synchronized void start() { running = true; }
      synchronized void stop()  { running = false; }
      synchronized boolean isRunning() { return running; }
   }

   private static boolean join(Thread t, long timeoutMs) {
      try {
         t.join(timeoutMs);
         return !t.isAlive();
      } catch (InterruptedException ie) {
         Thread.currentThread().interrupt();
         return false;
      }
   }

   private static void await(CountDownLatch latch, long timeoutMs) {
      try {
         latch.await(timeoutMs, TimeUnit.MILLISECONDS);
      } catch (InterruptedException ie) {
         Thread.currentThread().interrupt();
      }
   }
}
