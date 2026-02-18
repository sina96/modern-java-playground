package playground.modern.clean_arch.application;

import playground.modern.clean_arch.domain.OrderItem;

import java.util.List;
import java.util.Objects;

public record PlaceOrderCommand(
      String customerId,
      List<OrderItem> items,
      String paymentToken
) {
   public PlaceOrderCommand {
      Objects.requireNonNull(customerId);
      Objects.requireNonNull(items);
      Objects.requireNonNull(paymentToken);

      if (customerId.isBlank()) throw new IllegalArgumentException("customerId must not be blank");
      if (paymentToken.isBlank()) throw new IllegalArgumentException("paymentToken must not be blank");
      if (items.isEmpty()) throw new IllegalArgumentException("items must not be empty");
   }
}
