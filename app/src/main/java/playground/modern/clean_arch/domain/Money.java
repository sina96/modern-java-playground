package playground.modern.clean_arch.domain;

import java.util.Objects;


public record Money(String currency, long minorUnits)
{

   public Money{
      Objects.requireNonNull(currency);

      if(currency.length() != 3) throw new IllegalArgumentException("Invalid currency code: " + currency);
      if(minorUnits < 0) throw new IllegalArgumentException("Negative amount: " + minorUnits);
   }

   public static Money of(String sek, int i)
   {
      return new Money(sek, i /100);
   }

   public Money add(Money other)
   {
      if(!currency.equals(other.currency)) throw new IllegalArgumentException("Different currencies: " + currency + " vs " + other.currency);
      return new Money(currency, minorUnits + other.minorUnits);
   }

   public Money multiply(int quantity){
      if(quantity <= 0) throw new IllegalArgumentException("Invalid quantity: " + quantity);
      return new Money(currency, minorUnits * quantity);
   }


}
