package playground.modern.clean_arch.api;

import playground.common.Demo;
import playground.modern.clean_arch.application.FakePaymentGateway;
import playground.modern.clean_arch.application.InMemoryOrderRepository;
import playground.modern.clean_arch.application.PlaceOrderCommand;
import playground.modern.clean_arch.application.PlaceOrderResult;
import playground.modern.clean_arch.application.PlaceOrderUseCase;
import playground.modern.clean_arch.domain.OrderItem;
import playground.modern.clean_arch.domain.OrderPolicy;
import playground.modern.clean_arch.domain.Money;

import java.util.List;


public class CleanArchDemo implements Demo
{
   @Override
   public String name()
   {
      return "Clean Architecture Demo";
   }

   @Override
   public String description()
   {
      return "A minimal “Order Processing” system with Domain, Infrastructure, and Application layers + Fake API runner";
   }

   @Override
   public void run()
   {
      // --- Manual wiring (composition root) ---
      OrderPolicy policy = new OrderPolicy(); // assumes default constructor
      InMemoryOrderRepository repo = new InMemoryOrderRepository();
      FakePaymentGateway payments = new FakePaymentGateway(100_000); // approve up to SEK 1000.00 if 100 minor = 1 SEK

      PlaceOrderUseCase useCase = new PlaceOrderUseCase(repo, payments, policy);

      // --- Scenario 1: small order -> approved ---
      PlaceOrderCommand okCmd = new PlaceOrderCommand(
            "cust-100",
            List.of(
                  new OrderItem("SKU-1", "Toothbrush", Money.of("SEK", 2990), 1),
                  new OrderItem("SKU-2", "Toothpaste", Money.of("SEK", 1990), 2)
            ),
            "tok_ok_123"
      );

      PlaceOrderResult okRes = useCase.handle(okCmd);
      System.out.println("Scenario 1 result: " + okRes);

      // Fetch and print stored order
      repo.findById(okRes.orderId()).ifPresent(order ->
            System.out.println("Stored order: " + order)
      );

      System.out.println("-------------------------------------------------");

      // --- Scenario 2: large order -> declined by fake gateway ---
      PlaceOrderCommand bigCmd = new PlaceOrderCommand(
            "cust-200",
            List.of(
                  new OrderItem("SKU-9", "Dental Chair", Money.of("SEK", 250_000), 1) // huge
            ),
            "tok_ok_999"
      );

      PlaceOrderResult bigRes = useCase.handle(bigCmd);
      System.out.println("Scenario 2 result: " + bigRes);

      repo.findById(bigRes.orderId()).ifPresent(order ->
            System.out.println("Stored order: " + order)
      );
   }
}
