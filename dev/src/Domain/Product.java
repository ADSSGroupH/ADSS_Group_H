package Domain;

import Domain.Supplier;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    private String pid;
    private String name;
    private double costPrice;
    private double salePrice;
    private String manufacturer;
    private List<Promotion> promotions;
    private List<SupplierDiscount> supplierDiscounts;
    private int minQuantity;
    private int stockQuantity;
    private int warehouseQuantity;
    private int shelfQuantity;
    private Map<Supplier, String> suppliers = new HashMap<>(); // supplier -> catalog number


    public Product(String pid, String name,
                   double costPrice, double salePrice,
                   String manufacturer,
                   List<Promotion> promotions,
                   List<SupplierDiscount> supplierDiscounts,
                   int minQuantity) {
        this.pid = pid;
        this.name = name;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.manufacturer = manufacturer;
        this.promotions = promotions;
        this.supplierDiscounts = supplierDiscounts;
        this.minQuantity = minQuantity;
        this.stockQuantity = 0;
        this.warehouseQuantity = 0;
        this.shelfQuantity = 0;
    }

    // Getters ו-Setters קיימים עבור שדות אחרים...

    public Map<Supplier, String> getSuppliers() {
        return suppliers;
    }

    public String getPid() {
        return pid;
    }
    public void setPId(String id) {
        this.pid = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getCostPrice() {
        return costPrice;
    }
    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSalePrice() {
        double discountedPrice = this.salePrice;
        LocalDate today = LocalDate.now();

        // מיון לפי תאריך התחלה אופציונלי (אם אתה רוצה סדר עקבי)
        for (Promotion promo : promotions) {
            boolean isForThisProduct = promo.getPromotedProduct() == this;
            boolean isActive = !today.isBefore(promo.getStartDate()) && !today.isAfter(promo.getEndDate());

            if (isForThisProduct && isActive) {
                double discount = promo.getDiscountPercentage();
                discountedPrice *= (1 - discount / 100.0);
            }
        }

        // עיגול לשתי ספרות אחרי הנקודה
        return Math.round(discountedPrice * 100.0) / 100.0;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }
    public void addPromotion(Promotion pr) {
        promotions.add(pr);
    }
    public boolean removePromotion(Promotion pr) {
        return promotions.remove(pr);
    }

    public List<SupplierDiscount> getSupplierDiscounts() {
        return supplierDiscounts;
    }
    public void addSupplierDiscount(SupplierDiscount sd) {
        supplierDiscounts.add(sd);
    }
    public boolean removeSupplierDiscount(SupplierDiscount sd) {
        return supplierDiscounts.remove(sd);
    }

    public int getMinQuantity() {
        return minQuantity;
    }
    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getWarehouseQuantity() {
        return warehouseQuantity;
    }
    public void setWarehouseQuantity(int warehouseQuantity) {
        this.warehouseQuantity = warehouseQuantity;
    }

    public int getShelfQuantity() {
        return shelfQuantity;
    }
    public void setShelfQuantity(int shelfQuantity) {
        this.shelfQuantity = shelfQuantity;
    }

    @Override
    public String toString() {
        return
                "name='" + name + '\'' +
                        ", productId='" + pid + '\'' ;
    }


}
