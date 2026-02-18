package playground.modern.clean_arch.application;

import playground.modern.clean_arch.domain.Order;
import playground.modern.clean_arch.domain.OrderPolicy;

import java.util.Objects;

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
      boolean isValid = policy.validate(created);
      if (!isValid) {
         Order rejected = created.markAsRejected();
         orderRepository.save(rejected);

         return new PlaceOrderResult(
               rejected.id(),
               rejected.status(),
               "Order rejected"
         );
      }

      // 3) Move to pending before charging
      Order pending = created.markAsPending();

      // 4) Charge payment (port)
      PaymentResult payment = paymentGateway.charge(pending.customerId(), pending.totalAmount(), cmd.paymentToken());

      // 5) Transition state based on payment outcome
      Order finalOrder = payment.approved()
            ? pending.markAsPaid()
            : pending.markAsRejected();

      // 6) Persist (port)
      orderRepository.save(finalOrder);

      // 7) Return response DTO
      String msg = payment.approved()
            ? "Order paid. providerRef=" + payment.providerRef()
            : "Order rejected. reason=" + payment.message();

      return new PlaceOrderResult(finalOrder.id(), finalOrder.status(), msg);
   }
}
