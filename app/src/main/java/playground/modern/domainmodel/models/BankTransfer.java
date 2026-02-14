package playground.modern.domainmodel.models;

import java.time.Instant;
import java.util.Objects;


public record BankTransfer(String id, Money amount, String customerId, Instant createdAt,
                    String iban, String reference) implements Payment
{

   public BankTransfer
   {
      Objects.requireNonNull(amount);
      Objects.requireNonNull(customerId);
      Objects.requireNonNull(iban);
      if(iban.isBlank())
         throw new IllegalArgumentException("IBAN is blank");
   }
}
