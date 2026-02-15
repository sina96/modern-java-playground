package playground.modern.result.models;

import java.util.Objects;


public record Success<T, E>(T value) implements Result<T, E>
{
   public Success
   {
      Objects.requireNonNull(value);
   }

   @Override
   public boolean isSuccess()
   {
      return true;
   }

   @Override
   public boolean isFailure()
   {
      return false;
   }

   @Override
   public T getOrThrow()
   {
      return value;
   }
}
