package inventory.domain;

import java.util.Date;

public class Promotion {
    private String proID;
    private double discountPercentage;
    private Date startDate;
    private Date endDate;
    private Product promotedProduct;     // 0..1
    private Category promotedCategory;   // 0..1

    public Promotion(String proID,
                     double discountPercentage,
                     Date startDate,
                     Date endDate,
                     Product promotedProduct,
                     Category promotedCategory) {
        this.proID               = proID;
        this.discountPercentage  = discountPercentage;
        this.startDate           = startDate;
        this.endDate             = endDate;
        this.promotedProduct     = promotedProduct;
        this.promotedCategory    = promotedCategory;
    }
}
