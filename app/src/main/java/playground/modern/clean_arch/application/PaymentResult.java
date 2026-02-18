package playground.modern.clean_arch.application;

import java.util.Objects;

public record PaymentResult(Status status, String providerRef, String message) {

   public enum Status { APPROVED, DECLINED }

   public PaymentResult {
      Objects.requireNonNull(status);
      Objects.requireNonNull(providerRef);
      Objects.requireNonNull(message);
   }

   public boolean approved() {
      return status == Status.APPROVED;
   }
}
