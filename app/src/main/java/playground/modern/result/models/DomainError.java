package playground.modern.result.models;

public sealed interface DomainError permits DomainError.ComputationError, DomainError.ParseError, DomainError.ValidationError
{
    record ParseError(String field, String input, String message) implements DomainError
   {}

   record ValidationError(String field, String message) implements DomainError
   { }

   record ComputationError(String message) implements DomainError
   { }
}



