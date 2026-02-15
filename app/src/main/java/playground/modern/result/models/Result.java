package playground.modern.result.models;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;


public sealed interface Result<T, E> permits Success, Failure
{

   // core
   boolean isSuccess();

   boolean isFailure();

   // value accessors

   T getOrThrow();

   default Optional<T> toOptional()
   {
      return switch (this)
      {
         case Success<T, E> success -> Optional.of(success.value());
         case Failure<T, E> _ -> Optional.empty();
      };
   }

   default T orElse(T fallback){
      return switch (this){
         case Success<T, E> success -> success.value();
         case Failure<T, E> _ -> fallback;
      };
   }

   default T orElseGet(Supplier<? extends T> supplier)
   {
      Objects.requireNonNull(supplier);
      return switch (this) {
         case Success<T, E> success -> success.value();
         case Failure<T, E> _ -> supplier.get();
      };
   }

   // Transformation

   default <U> Result<U, E> map(Function<? super T, ? extends U> mapper){
      Objects.requireNonNull(mapper);

      return switch (this) {
         case Success<T, E> s-> {
            U mapped = Objects.requireNonNull(mapper.apply(s.value()), "map() returns null");
            yield Result.ok(mapped);
         }
         case Failure<T, E> f-> Result.err(f.error());
      };
   }

   default <U> Result<U, E> flatMap(Function<? super T, ? extends Result<U, E>> mapper)
   {
      Objects.requireNonNull(mapper);

      return switch (this) {
         case Success<T, E> s -> Objects.requireNonNull(mapper.apply(s.value()), "flatMap() returns null");

         case Failure<T, E> f -> Result.err(f.error());
      };
   }

   default <F> Result<T, F> mapError(Function<? super E, ? extends F> mapper) {
      Objects.requireNonNull(mapper);

      return switch (this){
         case Success<T, E> s -> Result.ok(s.value());
         case Failure<T, E> f -> {
            F mapped = Objects.requireNonNull(mapper.apply(f.error()), "mapError() returns null");
            yield Result.err(mapped);
         }
      };
   }

   default Result<T, E> recover(Function<? super E, ? extends T> recovery){
      Objects.requireNonNull(recovery);
      return switch (this){
         case Success<T, E> _ -> this;
         case Failure<T, E> f -> {
            T recovered = Objects.requireNonNull(recovery.apply(f.error()), "recover() returns null");
            yield Result.ok(recovered);
         }
      };
   }

   default <R> R fold(Function<? super E, ? extends R> failureMapper, Function<? super T, ? extends R> successMapper) {
      Objects.requireNonNull(failureMapper);
      Objects.requireNonNull(successMapper);

      return switch (this) {
         case Success<T, E> s -> successMapper.apply(s.value());
         case Failure<T, E> f -> failureMapper.apply(f.error());
      };
   }

   // factories

   static <T, E> Result<T, E> ok(T value){
      return new Success<>(value);
   }

   static <T, E> Result<T, E> err(E error)
   {
      return new Failure<>(error);
   }

   // Boundary helper

   static <T, E> Result<T, E> fromOptional(Optional<T> optional, Supplier<? extends E> errorSupplier) {
      Objects.requireNonNull(optional);
      Objects.requireNonNull(errorSupplier);

      return optional.<Result<T, E>>map(Result::ok).orElseGet(() -> Result.err(errorSupplier.get()));
   }

}
