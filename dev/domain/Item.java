package inventory.domain;

import java.util.Date;
import java.util.List;

public class Item {

    private String iid;
    private String name;
    private Location location;
    private Date expirationDate;
    private Classification classification;
    private Product product;
    private boolean isDefect;  // שדה חדש לסימון פריט כפגום


    public Item (String iid, String name, Location location,
                   Date expirationDate,
                   Classification classification, Product product) {
        this.iid                 = iid;
        this.name                = name;
        this.location            = location;
        this.expirationDate      = expirationDate;
        this.classification            = classification;
        this.product               = product;
        this.isDefect = false;  // ברירת מחדל: לא פגום


        product.setStockQuantity(product.getStockQuantity() + 1);
        if (location == Location.WareHouse) {
            product.setWarehouseQuantity(
                    product.getWarehouseQuantity() + 1
            );
        } else {
            product.setShelfQuantity(
                    product.getShelfQuantity() + 1
            );
            }
    }
    public String getIid() {
        return iid;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Classification getClassification() {
        return classification;
    }
    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Product getProduct() {
        return product;
    }

    // Getter ו-Setter עבור isDefect
    public boolean isDefect() {
        return isDefect;
    }

    public void setDefect(boolean isDefect) {
        this.isDefect = isDefect;
    }

    public void display() {
        System.out.println("=== Product Details ===");
        System.out.println("ID: " + this.getIid());
        System.out.println("Name: " + this.getName());
        System.out.println("Location: " + this.getLocation());
        System.out.println("Cost Price: " + this.getProduct().getCostPrice());
        System.out.println("Sale Price: " + this.getProduct().getSalePrice());
        System.out.println("Manufacturer: " + this.getProduct().getManufacturer());
        System.out.println("Stock Quantity: " + this.getProduct().getStockQuantity());
        System.out.println("Min Quantity: " + this.getProduct().getMinQuantity());
        System.out.println("Warehouse Quantity: " + this.getProduct().getWarehouseQuantity());
        System.out.println("Shelf Quantity: " + this.getProduct().getShelfQuantity());
        System.out.println("Expiration Date: " + this.getExpirationDate());
        System.out.println("Category: " + this.getClassification().getCategory() + " SubCategory: " + this.getClassification().getSubcategory() + " Size: " + this.getClassification().getsize());
        System.out.println("Promotions: " + this.getProduct().getPromotions());
        System.out.println("Supplier Discounts: " + this.getProduct().getSupplierDiscounts());
    }
};
