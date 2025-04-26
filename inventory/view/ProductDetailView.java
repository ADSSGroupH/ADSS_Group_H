package inventory.view;

import inventory.domain.*;
import java.util.*;

public class ProductDetailView {
    private Product product;
    public ProductDetailView(Product product) { this.product = product; }
    public void display() {
        System.out.println("=== Product Details ===");
        System.out.println("ID: " + product.getPid());
        System.out.println("Name: " + product.getName());
        System.out.println("Location: " + product.getLocation());
        System.out.println("Cost Price: " + product.getCostPrice());
        System.out.println("Sale Price: " + product.getSalePrice());
        System.out.println("Manufacturer: " + product.getManufacturer());
        System.out.println("Stock Quantity: " + product.getStockQuantity());
        System.out.println("Min Quantity: " + product.getMinQuantity());
        System.out.println("Warehouse Quantity: " + product.getWarehouseQuantity());
        System.out.println("Shelf Quantity: " + product.getShelfQuantity());
        System.out.println("Expiration Date: " + product.getExpirationDate());
        System.out.println("Category: " + (product.getCategory() != null ? product.getCategory().getName() : "None"));
        System.out.println("Promotions: " + product.getPromotions());
        System.out.println("Supplier Discounts: " + product.getSupplierDiscounts());
    }
}