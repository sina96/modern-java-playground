package playground.modern.domainmodel.models;

import java.time.Instant;
import java.util.Objects;


public record CardPayment(String id, Money amount, String customerId, Instant createdAt,
                          String maskedPan, String authCode) implements Payment
{

   public CardPayment
   {
      Objects.requireNonNull(amount);
      Objects.requireNonNull(customerId);
      Objects.requireNonNull(maskedPan);
      Objects.requireNonNull(authCode);
      if(!maskedPan.contains("*") || authCode.isBlank())
         throw new IllegalArgumentException("Invalid card payment: maskedPan=" + maskedPan + ", authCode=" + authCode);
   }
}
