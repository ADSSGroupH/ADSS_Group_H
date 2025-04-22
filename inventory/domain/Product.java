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
}
