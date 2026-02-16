package playground.modern.streams.models;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


public class TransactionSummaryCollector implements Collector<Transaction, TransactionSummaryCollector.Accumulator, TransactionSummary> {

   static final class Accumulator
   {
      long count = 0;
      long total = 0;
      long max = Long.MAX_VALUE;
      long min = Long.MIN_VALUE;

      final EnumMap<Transaction.Category, Long> countByCategory = new EnumMap<>(Transaction.Category.class);

      final HashMap<String, Long> totalByMerchant = new HashMap<>();

      void add(Transaction t) {
         count++;
         long amount = t.amountMinor();
         total += amount;

         min = Math.min(min, amount);
         max = Math.max(max, amount);

         //Category count
         countByCategory.merge(t.category(), 1L, Long::sum);

         //Merchant total
         totalByMerchant.merge(t.merchant(), amount, Long::sum);
      }

      Accumulator combine(Accumulator other)
      {
         this.count += other.count;
         this.total += other.total;
         min = Math.min(this.min, other.min);
         max = Math.max(this.max, other.max);
         other.countByCategory.forEach((k, v) -> this.countByCategory.merge(k, v, Long::sum));
         other.totalByMerchant.forEach((k, v) -> this.totalByMerchant.merge(k, v, Long::sum));
         return this;
      }

      TransactionSummary finish() {
         OptionalLong minOpt = (count == 0) ? OptionalLong.empty() : OptionalLong.of(min);

         OptionalLong maxOpt = (count == 0) ? OptionalLong.empty() : OptionalLong.of(max);

         return new TransactionSummary(
               count,
               total,
               minOpt,
               maxOpt, Map.copyOf(countByCategory), Map.copyOf(totalByMerchant)
         );
      }
   }


   @Override
   public Supplier<Accumulator> supplier()
   {
      return Accumulator::new;
   }

   @Override
   public BiConsumer<Accumulator, Transaction> accumulator()
   {
      return Accumulator::add;
   }

   @Override
   public BinaryOperator<Accumulator> combiner()
   {
      return Accumulator::combine;
   }

   @Override
   public Function<Accumulator, TransactionSummary> finisher()
   {
      return Accumulator::finish;
   }

   @Override
   public Set<Characteristics> characteristics()
   {
      return Collections.emptySet();
   }

}
