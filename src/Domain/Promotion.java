package Domain;

import java.time.LocalDate;
import java.util.Date;

public class Promotion {
    private String proID;
    private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private Product promotedProduct;     // 0..1
    private Classification promotedCategory;   // 0..1

    public Promotion(String proID,
                     double discountPercentage,
                     LocalDate startDate,
                     LocalDate endDate,
                     Product promotedProduct,
                     Classification promotedCategory) {
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

    public Product getPromotedProduct() {
        return promotedProduct;
    }
    public void setPromotedProduct(Product promotedProduct) {
        this.promotedProduct = promotedProduct;
    }

    public Classification getPromotedCategory() {
        return promotedCategory;
    }
    public void setPromotedCategory(Classification promotedCategory) {
        this.promotedCategory = promotedCategory;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "proID='" + proID + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
