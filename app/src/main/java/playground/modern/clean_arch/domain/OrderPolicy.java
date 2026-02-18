package playground.modern.clean_arch.domain;

public class OrderPolicy
{

   public boolean validate(Order order)
   {
      if(order.totalAmount().minorUnits() <= 0 || order.totalAmount().minorUnits() > 10000){
         return false;
      }


      if(order.items().size() > 100){
         return false;
      }

      return true;
   }
}
