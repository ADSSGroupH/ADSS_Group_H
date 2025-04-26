package inventory.domain;

import java.util.Date;
import java.util.List;

public class Product {
    private String pid;
    private String name;
    private String location;
    private double costPrice;
    private double salePrice;
    private String manufacturer;
    private List<Promotion> promotions;
    private List<SupplierDiscount> supplierDiscounts;
    private Date expirationDate;
    private int minQuantity;
    private int stockQuantity;
    private int warehouseQuantity;
    private int shelfQuantity;
    private Category category;

    public Product(String pid, String name, String location,
                   double costPrice, double salePrice,
                   String manufacturer,
                   List<Promotion> promotions,
                   List<SupplierDiscount> supplierDiscounts,
                   Date expirationDate,
                   int minQuantity,
                   int stockQuantity,
                   int warehouseQuantity,
                   int shelfQuantity,
                   Category category) {
        this.pid                 = pid;
        this.name                = name;
        this.location            = location;
        this.costPrice           = costPrice;
        this.salePrice           = salePrice;
        this.manufacturer        = manufacturer;
        this.promotions          = promotions;
        this.supplierDiscounts   = supplierDiscounts;
        this.expirationDate      = expirationDate;
        this.minQuantity         = minQuantity;
        this.stockQuantity       = stockQuantity;
        this.warehouseQuantity   = warehouseQuantity;
        this.shelfQuantity       = shelfQuantity;
        this.category            = category;
    }
    // … getters & setters …
    
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
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
    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<SupplierDiscount> getSupplierDiscounts() {
        return supplierDiscounts;
    }
    public void setSupplierDiscounts(List<SupplierDiscount> supplierDiscounts) {
        this.supplierDiscounts = supplierDiscounts;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
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

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
}
