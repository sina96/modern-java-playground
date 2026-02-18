package playground.modern.clean_arch.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;


public record Order(OrderId id, OrderStatus status, String customerId, List<OrderItem> items, Instant createdAt, Money totalAmount)
{
   public Order
   {
      Objects.requireNonNull(id);
      Objects.requireNonNull(status);
      Objects.requireNonNull(customerId);
      Objects.requireNonNull(items);
      Objects.requireNonNull(createdAt);
      Objects.requireNonNull(totalAmount);

      items = List.copyOf(items);
   }

   public static Order create(String customerId, List<OrderItem> items)
   {
      Objects.requireNonNull(customerId);
      Objects.requireNonNull(items);
      if (items.isEmpty()) {
         throw new IllegalArgumentException("order must contain at least one item");
      }

      Money totalAmount = items.stream().map(OrderItem::lineTotal).reduce(Money::add).orElseThrow();
      return new Order(OrderId.newId(), OrderStatus.CREATED, customerId, items, Instant.now(), totalAmount);
   }

   public Money total()
   {
      return totalAmount;
   }

   public Order markAsPaid()
   {
      return new Order(this.id, OrderStatus.PAID, this.customerId, this.items, this.createdAt, this.totalAmount);
   }

   public Order markAsRejected()
   {
      return new Order(this.id, OrderStatus.REJECTED, this.customerId, this.items, this.createdAt, this.totalAmount);
   }

   public Order markAsPending()
   {
      return new Order(this.id, OrderStatus.PAYMENT_PENDING, this.customerId, this.items, this.createdAt, this.totalAmount);
   }
}
