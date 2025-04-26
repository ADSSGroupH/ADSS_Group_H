package inventory.domain;

import inventory.view.ProductDetailView;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryManager {
    // Map products by their ID
    private final Map<String, Product> products = new HashMap<>();
    // Map items by their item ID
    private final Map<String, Item> items = new HashMap<>();
    private final Map<String, Classification> categories = new HashMap<>();
    private final List<Alert> alerts = new ArrayList<>();

    /**

    public void addItem(Item item) {
        items.put(item.getIid(), item);
        products.put(item.getProduct().getPid(), item.getProduct());
    }

    // MH: record item shortages manually
    public void recordShortage(String iid, int shortageQty) {
        Item it = items.get(iid);
        if (it != null) {
            alerts.add(new Alert(it.getProduct(), new Date()));
        }
    }

    // MH: generate order report based on current stock and optional category filters
    public StockReport generateOrderReport(List<Classification> filterCategories) {
        List<Product> toOrder = products.values().stream()
                .filter(p -> p.getStockQuantity() < p.getMinQuantity())
                .filter(p -> filterCategories == null || filterCategories.contains(p.getClassification()))
                .collect(Collectors.toList());
        return new StockReport(UUID.randomUUID().toString(), new Date(), toOrder, filterCategories);
    }

    // MH: proactive notifications for low stock
    public void checkAndNotify() {
        for (Product p : products.values()) {
            if (p.getStockQuantity() <= p.getMinQuantity()) {
                alerts.add(new Alert(p, new Date()));
            }
        }
    }

    // MH: set/update minimal alert quantities
    public void setMinAlertQuantity(String pid, int minQty) {
        Product p = products.get(pid);
        if (p != null) p.setMinQuantity(minQty);
    }

    // MH: detailed product info
    public void getProductDetails(String pid) {
        Product p = products.get(pid);
        if (p == null) {
            System.out.println("Product not found: " + pid);
        } else {
            new ProductDetailView(new Item("-", p.getName(), Location.Store, new Date(), p.getClassification(), p)).display();
        }
    }

    // MH: supplier discounts
    public void addSupplierDiscount(String pid, SupplierDiscount sd) {
        Product p = products.get(pid);
        if (p != null) p.addSupplierDiscount(sd);
    }

    // MH: hierarchical categories
    public void addCategory(Classification c) {
        categories.put(c.getId(), c);
    }

    // MH: weekly report alias
    public StockReport weeklyReport(List<Classification> cats) {
        return generateOrderReport(cats);
    }
/*
    // MH: defective/expired reporting uses Items for expirationDate
    public DefectiveProductsReport generateDefectReport() {
        Date now = new Date();
        List<Product> bad = items.values().stream()
                .filter(it -> it.getExpirationDate() != null && it.getExpirationDate().before(now))
                .map(Item::getProduct)
                .collect(Collectors.toList());
        return new DefectiveProductsReport(
                UUID.randomUUID().toString(), new Date(), bad,
                new ArrayList<>(categories.values())
        );
    }

 */

    // Added for controllers:
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }
    public List<Classification> getAllCategories() {
        return new ArrayList<>(categories.values());
    }
    public List<Alert> getAllAlerts() {
        return new ArrayList<>(alerts);
    }
}
