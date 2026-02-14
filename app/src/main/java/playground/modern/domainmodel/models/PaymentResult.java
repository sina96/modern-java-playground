package playground.modern.domainmodel.models;

public record PaymentResult(String id, PaymentStatus status, String message)
{
   //String providerRef(String authCode, String bankRef, String txHash)
}
