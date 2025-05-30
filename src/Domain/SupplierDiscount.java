package Domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable class representing a supplier discount.
 */
public final class SupplierDiscount {
    private final String disID;
    private final String supplierName;
    private final double discountPercentage;
    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * @param disID               unique identifier for this discount (non-null)
     * @param supplierName        name of the supplier (non-null)
     * @param discountPercentage  percentage between 0.0 and 100.0 inclusive
     * @param startDate           date when discount becomes active (non-null)
     * @param endDate             date when discount expires (non-null, not before startDate)
     * @throws NullPointerException     if disID, supplierName, startDate or endDate is null
     * @throws IllegalArgumentException if discountPercentage is out of range or endDate is before startDate
     */
    public SupplierDiscount(String disID,
                            String supplierName,
                            double discountPercentage,
                            LocalDate startDate,
                            LocalDate endDate) {
        this.disID = Objects.requireNonNull(disID, "disID cannot be null");
        this.supplierName = Objects.requireNonNull(supplierName, "supplierName cannot be null");

        if (discountPercentage < 0.0 || discountPercentage > 100.0) {
            throw new IllegalArgumentException("discountPercentage must be between 0 and 100");
        }
        this.discountPercentage = discountPercentage;

        this.startDate = Objects.requireNonNull(startDate, "startDate cannot be null");
        this.endDate   = Objects.requireNonNull(endDate,   "endDate cannot be null");
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate cannot be before startDate");
        }
    }

    public String getDisID() {
        return disID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupplierDiscount)) return false;
        SupplierDiscount that = (SupplierDiscount) o;
        return Double.compare(that.discountPercentage, discountPercentage) == 0
                && disID.equals(that.disID)
                && supplierName.equals(that.supplierName)
                && startDate.equals(that.startDate)
                && endDate.equals(that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(disID, supplierName, discountPercentage, startDate, endDate);
    }

    @Override
    public String toString() {
        return "SupplierDiscount{" +
                "disID='" + disID + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
