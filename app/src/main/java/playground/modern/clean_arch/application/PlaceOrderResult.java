package playground.modern.clean_arch.application;

import playground.modern.clean_arch.domain.OrderId;
import playground.modern.clean_arch.domain.OrderStatus;

import java.util.Objects;

public record PlaceOrderResult(
      OrderId orderId,
      OrderStatus status,
      String message
) {
   public PlaceOrderResult {
      Objects.requireNonNull(orderId);
      Objects.requireNonNull(status);
      Objects.requireNonNull(message);
   }
}
