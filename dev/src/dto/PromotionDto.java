package dto;

import java.time.LocalDate;

public class PromotionDto {
    private String proID;
    private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String promotedProductId;     // מזהה המוצר הממוקרם
    private String promotedCategoryId;    // מזהה ה־Classification

    public PromotionDto(String proID,
                        double discountPercentage,
                        LocalDate startDate,
                        LocalDate endDate,
                        String promotedProductId,
                        String promotedCategoryId) {
        this.proID = proID;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotedProductId = promotedProductId;
        this.promotedCategoryId = promotedCategoryId;
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

    public String getPromotedProductId() {
        return promotedProductId;
    }
    public void setPromotedProductId(String promotedProductId) {
        this.promotedProductId = promotedProductId;
    }

    public String getPromotedCategoryId() {
        return promotedCategoryId;
    }
    public void setPromotedCategoryId(String promotedCategoryId) {
        this.promotedCategoryId = promotedCategoryId;
    }}
