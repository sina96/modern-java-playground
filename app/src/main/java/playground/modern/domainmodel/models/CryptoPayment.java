package playground.modern.domainmodel.models;

import java.time.Instant;
import java.util.Objects;


public record CryptoPayment(String id, Money amount, String customerId, Instant createdAt,
                                 String network, String txHash) implements Payment
{

   public CryptoPayment
   {
      Objects.requireNonNull(amount);
      Objects.requireNonNull(customerId);
      Objects.requireNonNull(network);
      Objects.requireNonNull(txHash);
      if(txHash.isBlank())
         throw new IllegalArgumentException("Tx hash is blank");
   }
}
