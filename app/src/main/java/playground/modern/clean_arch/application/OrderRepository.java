package playground.modern.clean_arch.application;

import playground.modern.clean_arch.domain.Order;
import playground.modern.clean_arch.domain.OrderId;

import java.util.Optional;


public interface OrderRepository
{
   void save(Order order);

   Optional<Order> findById(OrderId id);
}
