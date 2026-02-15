package playground.modern.result;

import playground.common.Demo;
import playground.modern.result.models.DomainError;
import playground.modern.result.models.Result;

import java.util.Objects;


public class ResultDemo implements Demo
{
   @Override
   public String name()
   {
      return "ResultDemo";
   }

   @Override
   public String description()
   {
      return "Demonstrates functional error handling using Result type with parsing, validation, and computation scenarios";
   }

   @Override
   public void run()
   {
      runScenario("29", "45000");     // happy path
      runScenario("17", "45000");     // validation fail (age)
      runScenario("29", "-10");       // validation fail (salary)
      runScenario("abc", "45000");    // parse fail (age)
      runScenario("29", "999999999"); // computation fail (risk rule)


   }

   // scenario runner
   private void runScenario(String ageText, String salaryText)
   {
      System.out.printf("Scenario: age=%s salary=%s%n", ageText, salaryText);

      Result<CustomerProfile, DomainError> profileResult = parseInt("age", ageText).flatMap(
            age -> validateRange("age", age, 18, 120)).flatMap(
            validAge -> parseInt("salary", salaryText).flatMap(salary -> validateMin("salary", salary, 0))
                  .map(validSalary -> new CustomerProfile(validAge, validSalary)));
      Result<String, DomainError> bucketResult = profileResult.flatMap(profile -> computeRiskBucket(profile.age(), profile.salary()));

      String message = bucketResult.fold(
            error -> "Error: " + formatError(error),
            ok -> "OK. Risk bucket: " + ok
      );

      System.out.println(message);
   }



   // helpers
   static Result<Integer, DomainError> parseInt(String field, String input)
   {
      Objects.requireNonNull(field);
      Objects.requireNonNull(input);

      try
      {
         return Result.ok(Integer.parseInt(input.trim()));
      }
      catch (NumberFormatException ex)
      {
         return Result.err(new DomainError.ParseError(field, input, "Not a valid integer"));
      }
   }

   static Result<Integer, DomainError> validateRange(String field, int value, int minInclusive, int maxInclusive)
   {
      Objects.requireNonNull(field);

      if (value < minInclusive || value > maxInclusive)
      {
         return Result.err(new DomainError.ValidationError(field,
               "Must be in range [" + minInclusive + ", " + maxInclusive + "], got " + value));
      }
      return Result.ok(value);
   }

   static Result<Integer, DomainError> validateMin(String field, int value, int minInclusive)
   {
      Objects.requireNonNull(field);

      if (value < minInclusive)
      {
         return Result.err(new DomainError.ValidationError(field, "Must be >= " + minInclusive + ", got " + value));
      }
      return Result.ok(value);
   }

   /**
    * Computes a risk bucket classification based on customer age and salary.
    * <p>
    * The method applies simple deterministic rules to categorize customers:
    * <ul>
    *   <li>YOUNG: age < 25</li>
    *   <li>LOW_INCOME: age >= 25 and salary < 30,000</li>
    *   <li>STANDARD: age >= 25 and salary between 30,000 and 79,999</li>
    *   <li>HIGH_INCOME: age >= 25 and salary between 80,000 and 200,000</li>
    * </ul>
    * <p>
    * Salaries above 200,000 are rejected as they exceed the demo rule limits.
    *
    * @param age    the customer's age (expected to be validated in range [18, 120])
    * @param salary the customer's salary (expected to be validated as non-negative)
    * @return Result.ok with the risk bucket string if computation succeeds,
    * or Result.err with ComputationError if salary exceeds 200,000
    */
   static Result<String, DomainError> computeRiskBucket(int age, int salary)
   {
      if (salary > 200_000)
      {
         return Result.err(new DomainError.ComputationError("Risk engine rejected salary: " + salary + " (too large for demo rules)"));
      }

      // Simple deterministic mapping to buckets
      String bucket = (age < 25) ? "YOUNG" : (salary < 30_000) ? "LOW_INCOME" : (salary < 80_000) ? "STANDARD" : "HIGH_INCOME";

      return Result.ok(bucket);
   }

   static String formatError(DomainError error)
   {
      return switch (error)
      {
         case DomainError.ParseError e -> "ParseError(field=" + e.field() + ", input=" + e.input() + ", msg=" + e.message() + ")";
         case DomainError.ValidationError e -> "ValidationError(field=" + e.field() + ", msg=" + e.message() + ")";
         case DomainError.ComputationError e -> "ComputationError(msg=" + e.message() + ")";
      };
   }


   // records
   record CustomerProfile(int age, int salary)
   {
   }
}
