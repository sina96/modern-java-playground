package playground.modern.streams.models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;

public final class TransactionFixtures {

   private TransactionFixtures() {}

   public static List<Transaction> sampleTransactions() {
      Currency sek = Currency.getInstance("SEK");
      Instant base = Instant.parse("2026-01-01T12:00:00Z");

      return List.of(
            tx("t-001", "c-100", "ICA",   Transaction.Category.GROCERIES,  4590, sek, base.plus(1, ChronoUnit.MINUTES)),
            tx("t-002", "c-101", "Rusta", Transaction.Category.HOME,      12900, sek, base.plus(2, ChronoUnit.MINUTES)),
            tx("t-003", "c-100", "IKEA",  Transaction.Category.HOME,      49900, sek, base.plus(3, ChronoUnit.MINUTES)),
            tx("t-004", "c-102", "Elgiganten", Transaction.Category.ELECTRONICS,  79900, sek, base.plus(4, ChronoUnit.MINUTES)),
            tx("t-005", "c-103", "Apoteket",   Transaction.Category.PHARMACY,     890, sek, base.plus(5, ChronoUnit.MINUTES)),

            tx("t-006", "c-100", "ICA",   Transaction.Category.GROCERIES,  2390, sek, base.plus(6, ChronoUnit.MINUTES)),
            tx("t-007", "c-101", "ICA",   Transaction.Category.GROCERIES,  1590, sek, base.plus(7, ChronoUnit.MINUTES)),
            tx("t-008", "c-102", "IKEA",  Transaction.Category.HOME,      19900, sek, base.plus(8, ChronoUnit.MINUTES)),
            tx("t-009", "c-103", "Rusta", Transaction.Category.HOME,       7900, sek, base.plus(9, ChronoUnit.MINUTES)),
            tx("t-010", "c-100", "Apoteket", Transaction.Category.PHARMACY,  1290, sek, base.plus(10, ChronoUnit.MINUTES)),

            tx("t-011", "c-104", "ICA",   Transaction.Category.GROCERIES,  990, sek, base.plus(11, ChronoUnit.MINUTES)),
            tx("t-012", "c-104", "IKEA",  Transaction.Category.HOME,      29900, sek, base.plus(12, ChronoUnit.MINUTES)),
            tx("t-013", "c-101", "Elgiganten", Transaction.Category.ELECTRONICS, 149900, sek, base.plus(13, ChronoUnit.MINUTES)),
            tx("t-014", "c-102", "Apoteket", Transaction.Category.PHARMACY,  590, sek, base.plus(14, ChronoUnit.MINUTES)),
            tx("t-015", "c-103", "ICA",   Transaction.Category.GROCERIES,  3290, sek, base.plus(15, ChronoUnit.MINUTES)),

            tx("t-016", "c-100", "Rusta", Transaction.Category.HOME,       3900, sek, base.plus(16, ChronoUnit.MINUTES)),
            tx("t-017", "c-101", "ICA",   Transaction.Category.GROCERIES,  2190, sek, base.plus(17, ChronoUnit.MINUTES)),
            tx("t-018", "c-102", "IKEA",  Transaction.Category.HOME,      15900, sek, base.plus(18, ChronoUnit.MINUTES)),
            tx("t-019", "c-103", "Apoteket", Transaction.Category.PHARMACY,  1890, sek, base.plus(19, ChronoUnit.MINUTES)),
            tx("t-020", "c-104", "ICA",   Transaction.Category.GROCERIES,  1890, sek, base.plus(20, ChronoUnit.MINUTES))
      );
   }

   private static Transaction tx(
         String id,
         String customerId,
         String merchant,
         Transaction.Category category,
         long amountMinor,
         Currency currency,
         Instant timestamp
   ) {
      return new Transaction(id, customerId, merchant, category, amountMinor, currency, timestamp);
   }
}
