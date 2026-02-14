package playground.modern.domainmodel;

import playground.common.Demo;
import playground.modern.domainmodel.models.BankTransfer;
import playground.modern.domainmodel.models.CardPayment;
import playground.modern.domainmodel.models.CryptoPayment;
import playground.modern.domainmodel.models.Money;
import playground.modern.domainmodel.models.Payment;
import playground.modern.domainmodel.models.PaymentProcessor;

import java.time.Instant;
import java.util.List;


public class DomainModelDemo implements Demo
{
   @Override
   public String name()
   {
      return "Domain Model Demo";
   }

   @Override
   public String description()
   {
      return "Shows how to use records, sealed interfaces, and pattern matching to build a domain model";
   }

   @Override
   public void run()
   {
      PaymentProcessor paymentProcessor = new PaymentProcessor();
      CardPayment cardPayment = new CardPayment("cardId", Money.of("SEK", 200), "customer01",
            Instant.now(), "4189****7171", "authCode");
      CryptoPayment cryptoPayment = new CryptoPayment("cryptoId", Money.of("SEK", 200), "customer02",
         Instant.now(), "network", "txHash");
      BankTransfer bankTransfer = new BankTransfer("bankTransferId", Money.of("SEK", 200), "customer03",
            Instant.now(), "iban", "reference");
      List<Payment> payments = List.of(cardPayment, cryptoPayment, bankTransfer);

      System.out.printf("Processing payments for %s payments...%n", payments.size());
      payments.forEach(payment -> System.out.println(paymentProcessor.process(payment)));
   }
}
