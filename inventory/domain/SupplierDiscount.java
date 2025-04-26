package inventory.domain;

import java.util.Date;

public class SupplierDiscount {
    private String disID;
    private String supplierName;
    private double discountPercentage;
    private Date startDate;
    private Date endDate;

    public SupplierDiscount(String disID,
                            String supplierName,
                            double discountPercentage,
                            Date startDate,
                            Date endDate) {
        this.disID               = disID;
        this.supplierName        = supplierName;
        this.discountPercentage  = discountPercentage;
        this.startDate           = startDate;
        this.endDate             = endDate;
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

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
