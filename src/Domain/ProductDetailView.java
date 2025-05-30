package Domain;

public class ProductDetailView {
    private Item item;
    public ProductDetailView(Item item) { this.item = item; }
    public void display() {
        System.out.println("=== Product Details ===");
        System.out.println("ID: " + item.getIid());
        System.out.println("Name: " + item.getName());
        System.out.println("Location: " + item.getLocation());
        System.out.println("Cost Price: " + item.getProduct().getCostPrice());
        System.out.println("Sale Price: " + item.getProduct().getSalePrice());
        System.out.println("Manufacturer: " + item.getProduct().getManufacturer());
        System.out.println("Stock Quantity: " + item.getProduct().getStockQuantity());
        System.out.println("Min Quantity: " + item.getProduct().getMinQuantity());
        System.out.println("Warehouse Quantity: " + item.getProduct().getWarehouseQuantity());
        System.out.println("Shelf Quantity: " + item.getProduct().getShelfQuantity());
        System.out.println("Expiration Date: " + item.getExpirationDate());
        System.out.println("Category: " + item.getClassification().getCategory() + " SubCategory: " + item.getClassification().getSubcategory() + " Size: " + item.getClassification().getsize());
        System.out.println("Promotions: " + item.getProduct().getPromotions());
        System.out.println("Supplier Discounts: " + item.getProduct().getSupplierDiscounts());
    }
}