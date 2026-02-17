package playground.modern.concurrency;

import playground.common.Demo;
import playground.modern.concurrency.util.Harness;


public class ConcurrencyFundamentals implements Demo
{
   @Override
   public String name()
   {
      return "Concurrency Fundamentals";
   }

   @Override
   public String description()
   {
      return "Core Java concurrency concepts: visibility, happens-before, atomicity, and contention";
   }

   @Override
   public void run()
   {
      Harness.header( "Concurrency Fundamentals");

      Harness.bullet("1- Visibility Broken demo (no happens-before)");
      new VisibilityBrokenDemo().run();
      System.out.println();

      Harness.bullet("2- Visibility Fixed Volatile Demo — volatile establishes visibility");
      new VisibilityFixedVolatileDemo().run();
      System.out.println();

      Harness.bullet("3- Visibility Fixed Synchronized Demo — synchronized establishes happens-before");
      new VisibilityFixedSynchronizedDemo().run();
      System.out.println();

      Harness.bullet("4- Counter Comparison Demo — ++ vs synchronized vs AtomicLong vs LongAdder");
      new CounterComparisonDemo().run();

      System.out.println();
      Harness.bullet("Done.");

   }
}
