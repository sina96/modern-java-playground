package playground.modern.clean_arch.domain;

import java.util.Objects;
import java.util.UUID;


public record OrderId(String value)
{
   public OrderId
   {
      Objects.requireNonNull(value);
      if(value.isBlank()) throw new IllegalArgumentException("Order ID cannot be blank");
   }

   static OrderId newId()
   {
      return new OrderId(UUID.randomUUID().toString());
   }
}
