package dto;

import java.time.LocalDate;

public class SupplierDiscountDto {
    private String disID;
    private String supplierName;
    private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;

    public SupplierDiscountDto(String disID,
                               String supplierName,
                               double discountPercentage,
                               LocalDate startDate,
                               LocalDate endDate) {
        this.disID = disID;
        this.supplierName = supplierName;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDisID() {
        return disID;
    }
    public void setDisID(String disID) {
        this.disID = disID;
    }

    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
