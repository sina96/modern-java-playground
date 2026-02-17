package playground.modern.concurrency.util;

public class Harness
{

   private Harness()
   {
      // utility class
   }

   public static void sleep(long millis)
   {
      try {
         Thread.sleep(millis);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }
   }

   public static long timeMilis(Runnable r)
   {
      long start = System.currentTimeMillis();
      r.run();
      return System.currentTimeMillis() - start;
   }

   public static void runNTimes(Runnable r, int n)
   {
      for (int i = 0; i < n; i++)
      {
         r.run();
      }
   }

   public static void header(String title) {
      System.out.println("=================================================");
      System.out.println(title);
      System.out.println("=================================================");
   }

   public static void bullet(String s) {
      System.out.println("• " + s);
   }
}
