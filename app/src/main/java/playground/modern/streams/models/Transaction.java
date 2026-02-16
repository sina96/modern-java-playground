package playground.modern.streams.models;

import java.time.Instant;
import java.util.Currency;
import java.util.Objects;



public record Transaction (String id, String customerId, String merchant,
                           Category category, long amountMinor, Currency currency, Instant timestamp) {

   public Transaction {
      Objects.requireNonNull(id);
      Objects.requireNonNull(customerId);
      Objects.requireNonNull(merchant);
      Objects.requireNonNull(category);
      Objects.requireNonNull(currency);
      Objects.requireNonNull(timestamp);

      if(amountMinor < 0) throw new IllegalArgumentException("Amount cannot be negative");
    }

   public enum Category { GROCERIES, HOME, ELECTRONICS, PHARMACY, OTHER }
}
