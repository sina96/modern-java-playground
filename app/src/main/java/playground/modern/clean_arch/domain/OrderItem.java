package playground.modern.clean_arch.domain;

import java.util.Objects;


public record OrderItem(String sku, String name, Money unitPrice, int quantity)
{

   public OrderItem
   {
      Objects.requireNonNull(sku);
      Objects.requireNonNull(name);

      if(quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");
      if(sku.isBlank()) throw new IllegalArgumentException("SKU cannot be blank");
      if(name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
   }

   public Money lineTotal()
   {
      return unitPrice.multiply(quantity);
   }
}
