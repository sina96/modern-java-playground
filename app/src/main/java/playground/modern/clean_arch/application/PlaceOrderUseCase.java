package playground.modern.clean_arch.application;

import playground.modern.clean_arch.domain.Order;
import playground.modern.clean_arch.domain.OrderId;
import playground.modern.clean_arch.domain.OrderPolicy;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public final class PlaceOrderUseCase {

   private final OrderRepository orderRepository;
   private final PaymentGateway paymentGateway;
   private final OrderPolicy policy;

   public PlaceOrderUseCase(
         OrderRepository orderRepository,
         PaymentGateway paymentGateway,
         OrderPolicy policy
   ) {
      this.orderRepository = Objects.requireNonNull(orderRepository);
      this.paymentGateway = Objects.requireNonNull(paymentGateway);
      this.policy = Objects.requireNonNull(policy);
   }

   public PlaceOrderResult handle(PlaceOrderCommand cmd) {
      Objects.requireNonNull(cmd);

      // 1) Create order (domain factory)
      Order created = Order.create(cmd.customerId(), cmd.items());

      // 2) Domain policy validation
      boolean violation = policy.validate(created);
      if (!violation) {
         Order rejected = created.MarkAsRejected(created);
         orderRepository.save(rejected);

         return new PlaceOrderResult(
               rejected.id(),
               rejected.status(),
               "Order rejected"
         );
      }

      // 3) Charge payment (port)
      PaymentResult payment = paymentGateway.charge(created.customerId(), created.total(), cmd.paymentToken());

      // 4) Transition state based on payment outcome
      Order finalOrder = payment.approved()
            ? created.MarkAsPaid(created)
            : created.MarkAsRejected(created);

      // 5) Persist (port)
      orderRepository.save(finalOrder);

      // 6) Return response DTO
      String msg = payment.approved()
            ? "Order paid. providerRef=" + payment.providerRef()
            : "Order rejected. reason=" + payment.message();

      return new PlaceOrderResult(finalOrder.id(), finalOrder.status(), msg);
   }
}
