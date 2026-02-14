package playground.modern.domainmodel.models;

import java.time.Instant;


public record CryptoPayment(String id, Money amount, String customerId, Instant createdAt,
                                 String network, String txHash) implements Payment
{

   public CryptoPayment
   {
      if(txHash.isBlank())
         throw new IllegalArgumentException("Tx hash is blank");
   }
}
