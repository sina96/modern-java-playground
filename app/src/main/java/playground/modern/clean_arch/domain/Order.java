package playground.modern.clean_arch.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;


public record Order(OrderId id, OrderStatus status, String customerId, List<OrderItem> items, Instant createdAt, Money totalAmount)
{
   public Order
   {
      Objects.requireNonNull(id);
      Objects.requireNonNull(items);
      Objects.requireNonNull(createdAt);
   }

   public static Order create(String customerId, List<OrderItem> items)
   {
      Money totalAmount = items.stream().map(OrderItem::lineTotal).reduce(Money::add).orElseThrow();
      return new Order(OrderId.newId(), OrderStatus.CREATED, customerId, items, Instant.now(), totalAmount);
   }

   public Money total()
   {
      return items.stream().map(OrderItem::lineTotal).reduce(Money::add).orElseThrow();
   }

   public Order MarkAsPaid(Order order)
   {
      return new Order(order.id, OrderStatus.PAID, order.customerId, order.items, order.createdAt, order.totalAmount);
   }

   public Order MarkAsRejected(Order order)
   {
      return new Order(order.id, OrderStatus.REJECTED, order.customerId, order.items, order.createdAt, order.totalAmount);
   }

   public Order MarkAsPending(Order order)
   {
      return new Order(order.id, OrderStatus.PAYMENT_PENDING, order.customerId, order.items, order.createdAt, order.totalAmount);
   }
}
