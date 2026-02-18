package playground.modern.clean_arch.application;

import playground.modern.clean_arch.domain.Money;

import java.util.Objects;
import java.util.UUID;

public final class FakePaymentGateway implements PaymentGateway {

   private final long approveUpToMinor; // deterministic rule

   public FakePaymentGateway(long approveUpToMinor) {
      if (approveUpToMinor < 0) throw new IllegalArgumentException("approveUpToMinor must be >= 0");
      this.approveUpToMinor = approveUpToMinor;
   }

   @Override
   public PaymentResult charge(String customerId, Money amount, String paymentToken) {
      Objects.requireNonNull(customerId);
      Objects.requireNonNull(amount);
      Objects.requireNonNull(paymentToken);

      if (paymentToken.isBlank()) {
         return new PaymentResult(PaymentResult.Status.DECLINED, "N/A", "Blank payment token");
      }

      if (amount.minorUnits() <= approveUpToMinor) {
         return new PaymentResult(
               PaymentResult.Status.APPROVED,
               "PAY-" + UUID.randomUUID(),
               "Approved by fake gateway"
         );
      }

      return new PaymentResult(
            PaymentResult.Status.DECLINED,
            "PAY-" + UUID.randomUUID(),
            "Amount exceeds limit (" + approveUpToMinor + " minor units)"
      );
   }
}