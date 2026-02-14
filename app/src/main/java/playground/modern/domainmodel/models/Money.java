package playground.modern.domainmodel.models;

public record Money(String currency, long minorUnits)
{

   public Money
   {
      if (currency.length() != 3 && currency.equals(currency.toUpperCase()))
         throw new IllegalArgumentException("Invalid currency code: " + currency);
      if (minorUnits < 0)
         throw new IllegalArgumentException("Negative amount: " + minorUnits);
   }

   public static Money of(String currency, long minorUnits)
   {
      return new Money(currency, minorUnits);
   }

   @Override
   public String toString()
   {
      return "Money: %s %s".formatted(currency, minorUnits);
   }
}
