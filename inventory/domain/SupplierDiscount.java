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
}
