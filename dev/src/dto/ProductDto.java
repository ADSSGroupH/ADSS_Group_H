package dto;

import java.util.Map;

/**
 * Data Transfer Object for Product entity
 */
public class ProductDto {
    private String pid;
    private String name;
    private double costPrice;
    private double salePrice;
    private String manufacturer;
    private String promotionIds;           // comma-separated promotion IDs
    private String supplierDiscountIds;    // comma-separated supplier discount IDs
    private int minQuantity;
    private int stockQuantity;
    private int warehouseQuantity;
    private int shelfQuantity;
    private String supplierCatalogNumbers; // supplierId -> catalog number

    public ProductDto(String pid,
                      String name,
                      double costPrice,
                      double salePrice,
                      String manufacturer,
                      String promotionIds,
                      String supplierDiscountIds,
                      int minQuantity,
                      int stockQuantity,
                      int warehouseQuantity,
                      int shelfQuantity,
                      String supplierCatalogNumbers) {
        this.pid = pid;
        this.name = name;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.manufacturer = manufacturer;
        this.promotionIds = promotionIds;
        this.supplierDiscountIds = supplierDiscountIds;
        this.minQuantity = minQuantity;
        this.stockQuantity = stockQuantity;
        this.warehouseQuantity = warehouseQuantity;
        this.shelfQuantity = shelfQuantity;
        this.supplierCatalogNumbers = supplierCatalogNumbers;
    }

    // Getters and Setters
    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = pid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getCostPrice() { return costPrice; }
    public void setCostPrice(double costPrice) { this.costPrice = costPrice; }

    public double getSalePrice() { return salePrice; }
    public void setSalePrice(double salePrice) { this.salePrice = salePrice; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getPromotionIds() { return promotionIds; }
    public void setPromotionIds(String promotionIds) { this.promotionIds = promotionIds; }

    public String getSupplierDiscountIds() { return supplierDiscountIds; }
    public void setSupplierDiscountIds(String supplierDiscountIds) { this.supplierDiscountIds = supplierDiscountIds; }

    public int getMinQuantity() { return minQuantity; }
    public void setMinQuantity(int minQuantity) { this.minQuantity = minQuantity; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getWarehouseQuantity() { return warehouseQuantity; }
    public void setWarehouseQuantity(int warehouseQuantity) { this.warehouseQuantity = warehouseQuantity; }

    public int getShelfQuantity() { return shelfQuantity; }
    public void setShelfQuantity(int shelfQuantity) { this.shelfQuantity = shelfQuantity; }

    public String getSupplierCatalogNumbers() { return supplierCatalogNumbers; }
  }
