public enum PaymentMethod {
    CREDIT_CARD,
    CHECK,
    CASH,
    BANK_TRANSFER;

    @Override
    public String toString() {
        return switch (this) {
            case CREDIT_CARD -> "Credit Card";
            case CHECK -> "Check";
            case CASH -> "Cash";
            case BANK_TRANSFER -> "Bank Transfer";
        };
    }

    public static PaymentMethod fromString(String input) {
        return switch (input.trim().toLowerCase()) {
            case "credit", "credit card" -> CREDIT_CARD;
            case "check", "cheque" -> CHECK;
            case "cash" -> CASH;
            case "bank", "bank transfer" -> BANK_TRANSFER;
            default -> throw new IllegalArgumentException("Invalid payment method: " + input);
        };
    }
}
