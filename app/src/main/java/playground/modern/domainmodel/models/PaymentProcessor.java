package playground.modern.domainmodel.models;

public class PaymentProcessor
{

   public PaymentResult process(Payment payment){
      switch (payment){
         case CardPayment cardPayment-> {
            return new PaymentResult(cardPayment.id(), PaymentStatus.APPROVED, "Approved");
         }
         case CryptoPayment cryptoPayment-> {
            return new PaymentResult(cryptoPayment.id(), PaymentStatus.PENDING, "Pending");
         }
         case BankTransfer bankTransfer-> {
            return new PaymentResult(bankTransfer.id(), PaymentStatus.PENDING, "Pending");
         }
      }
   }
}
