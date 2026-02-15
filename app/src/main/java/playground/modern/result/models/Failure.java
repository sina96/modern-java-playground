package playground.modern.result.models;

import java.util.Objects;


public record Failure<T, E> (E error) implements Result<T, E>
{
   public Failure
   {
      Objects.requireNonNull(error);
   }

   @Override
   public boolean isSuccess()
   {
      return false;
   }

   @Override
   public boolean isFailure()
   {
      return true;
   }

   @Override
   public T getOrThrow()
   {
      throw new IllegalArgumentException("Error: " + error);
   }
}
