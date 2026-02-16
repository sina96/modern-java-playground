package playground.modern.streams.models;

import java.util.Map;
import java.util.Objects;
import java.util.OptionalLong;


public record TransactionSummary(long count, long totalAmountMinor, OptionalLong minAmountMinor, OptionalLong maxAmountMinor, Map<Transaction.Category, Long> countByCategory, Map<String, Long> totalByMerchant)
{

   public TransactionSummary
   {
      Objects.requireNonNull(count);
      Objects.requireNonNull(totalAmountMinor);
      Objects.requireNonNull(maxAmountMinor);
      Objects.requireNonNull(minAmountMinor);
      Objects.requireNonNull(countByCategory);
      Objects.requireNonNull(totalByMerchant);

      if (countByCategory.isEmpty())
         throw new IllegalArgumentException("No transactions");

      if (totalByMerchant.isEmpty())
         throw new IllegalArgumentException("No transactions");
   }
}
