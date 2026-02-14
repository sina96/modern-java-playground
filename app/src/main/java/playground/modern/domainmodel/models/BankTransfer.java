package playground.modern.domainmodel.models;

import java.time.Instant;


public record BankTransfer(String id, Money amount, String customerId, Instant createdAt,
                    String iban, String reference) implements Payment
{

   public BankTransfer
   {
      if(iban.isBlank())
         throw new IllegalArgumentException("IBAN is blank");
   }
}
