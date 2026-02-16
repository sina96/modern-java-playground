package playground.modern.streams;

import playground.common.Demo;
import playground.modern.streams.models.Transaction;
import playground.modern.streams.models.TransactionFixtures;
import playground.modern.streams.models.TransactionSummary;
import playground.modern.streams.models.TransactionSummaryCollector;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public final class StreamsDemo implements Demo
{

   void main()
   {
      List<Transaction> txs = TransactionFixtures.sampleTransactions();

      System.out.println("=== StreamsDemo: Custom Collector Summary ===");
      TransactionSummary summary = txs.stream().collect(new TransactionSummaryCollector());
      printSummary(summary);

      System.out.println();
      System.out.println("=== StreamsDemo: A few good stream patterns ===");

      // 1) Total spend using primitive stream (avoid boxing)
      long total = txs.stream().mapToLong(Transaction::amountMinor).sum();
      System.out.println("Total spend (minor units): " + total);

      // 2) Top 3 transactions by amount (descending)
      List<Transaction> top3 = txs.stream().sorted(Comparator.comparingLong(Transaction::amountMinor).reversed()).limit(3)
            .toList();
      System.out.println("Top 3 transactions:");
      top3.forEach(t -> System.out.println("  " + formatTx(t)));

      // 3) Spend per merchant using groupingBy + summingLong
      Map<String, Long> spendByMerchant = txs.stream()
            .collect(Collectors.groupingBy(Transaction::merchant, Collectors.summingLong(Transaction::amountMinor)));
      System.out.println("Spend by merchant:");
      spendByMerchant.forEach((m, amt) -> System.out.println("  " + m + " -> " + amt));

      // 4) Count per category using groupingBy (Enum keys)
      Map<Transaction.Category, Long> countByCategory = txs.stream()
            .collect(Collectors.groupingBy(Transaction::category, Collectors.counting()));
      System.out.println("Count by category:");
      countByCategory.forEach((c, cnt) -> System.out.println("  " + c + " -> " + cnt));
   }

   private static void printSummary(TransactionSummary s)
   {
      System.out.println("count = " + s.count());
      System.out.println("totalAmountMinor = " + s.totalAmountMinor());
      System.out.println("minAmountMinor = " + s.minAmountMinor());
      System.out.println("maxAmountMinor = " + s.maxAmountMinor());

      System.out.println("countByCategory:");
      s.countByCategory().forEach((cat, cnt) -> System.out.println("  " + cat + " -> " + cnt));

      System.out.println("totalByMerchant:");
      s.totalByMerchant().forEach((merchant, amt) -> System.out.println("  " + merchant + " -> " + amt));
   }

   private static String formatTx(Transaction t)
   {
      return t.id() + " | " + t.merchant() + " | " + t.category() + " | " + t.amountMinor();
   }

   @Override
   public String name()
   {
      return "Streams Demo";
   }

   @Override
   public String description()
   {
      return "Demonstrates Java 17+ stream features like groupingBy, summingLong, and collectors";
   }

   @Override
   public void run()
   {
      this.main();
   }
}
