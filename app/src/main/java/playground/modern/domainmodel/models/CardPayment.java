package playground.modern.domainmodel.models;

import java.time.Instant;


public record CardPayment(String id, Money amount, String customerId, Instant createdAt,
                          String maskedPan, String authCode) implements Payment
{

   public CardPayment
   {
      if(!maskedPan.contains("*") || authCode.isBlank())
         throw new IllegalArgumentException("Invalid card payment: maskedPan=" + maskedPan + ", authCode=" + authCode);
   }
}
