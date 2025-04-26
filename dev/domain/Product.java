package inventory.domain;

import java.util.List;

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

    public String getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public double getCostPrice() {
        return costPrice;
    }
    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSalePrice() {
        return salePrice;
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
}
