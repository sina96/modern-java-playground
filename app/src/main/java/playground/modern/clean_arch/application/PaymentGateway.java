package playground.modern.clean_arch.application;

import playground.modern.clean_arch.domain.Money;



public interface PaymentGateway
{
   PaymentResult charge(String customerId, Money amount, String paymentToken);
}
