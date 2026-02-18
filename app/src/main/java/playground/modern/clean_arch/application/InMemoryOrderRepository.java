package playground.modern.clean_arch.application;

import playground.modern.clean_arch.domain.Order;
import playground.modern.clean_arch.domain.OrderId;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryOrderRepository implements OrderRepository {

   private final Map<OrderId, Order> store = new ConcurrentHashMap<>();

   @Override
   public void save(Order order) {
      Objects.requireNonNull(order);
      store.put(order.id(), order);
   }

   @Override
   public Optional<Order> findById(OrderId id) {
      Objects.requireNonNull(id);
      return Optional.ofNullable(store.get(id));
   }
}
