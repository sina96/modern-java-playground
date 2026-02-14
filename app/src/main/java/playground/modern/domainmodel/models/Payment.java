package playground.modern.domainmodel.models;

import java.time.Instant;


public sealed interface Payment permits CardPayment, CryptoPayment, BankTransfer
{
   String id();

   Money amount();

   String customerId();

   Instant createdAt();
}
