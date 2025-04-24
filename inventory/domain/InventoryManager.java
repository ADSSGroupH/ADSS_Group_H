package inventory.domain;

import java.util.*;
import java.util.stream.Collectors;

public class InventoryManager {
    private Map<String, Product> products = new HashMap<>();
    private Map<String, Category> categories = new HashMap<>();
    private List<Alert> alerts = new ArrayList<>();

    // MH: record item shortages manually
    public void recordShortage(Product product, String pid, int shortageQty) {
        Product p = products.get(pid);
        if (p != null) {
            Alert alert = new Alert(product, new Date());
            alerts.add(alert);
        }
    }
    // MH: generate order report based on current stock
    public StockReport generateOrderReport(List<Category> filterCategories) {
        List<Product> toOrder = products.values().stream()
                .filter(p -> p.getStockQuantity() < p.getMinQuantity())
                .filter(p -> filterCategories == null || filterCategories.contains(p.getCategory()))
                .collect(Collectors.toList());
        return new StockReport(UUID.randomUUID().toString(), new Date(), toOrder, filterCategories);
    }

    // MH: proactive notifications
    public void checkAndNotify() {
        for (Product p : products.values()) {
            if (p.getStockQuantity() <= p.getMinQuantity()) {
                Alert alert = new Alert(p, new Date());
                alerts.add(alert);
            }
        }
    }

    // MH: set/update minimal alert quantities
    public void setMinAlertQuantity(String pid, int minQty) {
        Product p = products.get(pid);
        if (p != null) p.setMinQuantity(minQty);
    }

    // MH: detailed product info
    public Product getProductDetails(String pid) {
        return products.get(pid);
    }

    // MH: supplier discounts
    public void addSupplierDiscount(String pid, SupplierDiscount sd) {
        Product p = products.get(pid);
        if (p != null) p.getSupplierDiscounts().add(sd);
    }

    // MH: hierarchical categories
    public void addCategory(Category c) {
        categories.put(c.getId(), c);
    }

    // MH: weekly report alias
    public StockReport weeklyReport(List<Category> cats) {
        return generateOrderReport(cats);
    }

    // MH: defective/expired reporting
    public DefectReport generateDefectReport() {
        Date now = new Date();
        List<Product> bad = products.values().stream()
                .filter(p -> p.getExpirationDate() != null && p.getExpirationDate().before(now))
                .collect(Collectors.toList());
        return new DefectReport(UUID.randomUUID().toString(), new Date(), bad, new ArrayList<>(categories.values()));
    }

    // Added for controllers:
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }
    public List<Alert> getAllAlerts() {
        return new ArrayList<>(alerts);
    }
}
