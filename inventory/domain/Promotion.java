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

    public String getProID() {
        return proID;
    }
    public void setProID(String proID) {
        this.proID = proID;
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

    public Product getPromotedProduct() {
        return promotedProduct;
    }
    public void setPromotedProduct(Product promotedProduct) {
        this.promotedProduct = promotedProduct;
    }

    public Category getPromotedCategory() {
        return promotedCategory;
    }
    public void setPromotedCategory(Category promotedCategory) {
        this.promotedCategory = promotedCategory;
    }
}
