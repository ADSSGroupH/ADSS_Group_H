package inventory.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// InventoryStore’s lists are the “source of truth” for the entire application. The lists
// live in the Domain layer and hold every Product, Category, Report, or Alert ever created, in memory.
// Whenever you need to add, remove, or fetch the full dataset, you go via InventoryStore.

// in the View layer each View’s list holds exactly what the user sees right now.
// You typically call something like showProducts(), pull the master list out of InventoryStore, and then assign it to ProductsView.products.
// From there you can sort it, filter it, highlight selections, or even discard it entirely without touching the global data.
public class InventoryStore {
    private final List<Product> products    = new ArrayList<>();
    private final List<Category> categories = new ArrayList<>();
    private final List<Report> reports      = new ArrayList<>();
    private final List<Alert> alerts        = new ArrayList<>();

    // singleton instance
    private static final InventoryStore INSTANCE = new InventoryStore();
    private InventoryStore() {}
    public static InventoryStore getInstance() { return INSTANCE; }

    // --- getters (already read-only) ---
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public List<Report> getReports() {
        return Collections.unmodifiableList(reports);
    }

    public List<Alert> getAlerts() {
        return Collections.unmodifiableList(alerts);
    }

    // --- setters (overwrite the contents of each list) ---
    public void setProducts(List<Product> products) {
        this.products.clear();
        if (products != null) {
            this.products.addAll(products);
        }
    }

    public void setCategories(List<Category> categories) {
        this.categories.clear();
        if (categories != null) {
            this.categories.addAll(categories);
        }
    }

    public void setReports(List<Report> reports) {
        this.reports.clear();
        if (reports != null) {
            this.reports.addAll(reports);
        }
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts.clear();
        if (alerts != null) {
            this.alerts.addAll(alerts);
        }
    }

    // --- convenience add/remove methods ---
    public void addProduct(Product p)      { products.add(p); }
    public void removeProduct(Product p)   { products.remove(p); }

    public void addCategory(Category c)    { categories.add(c); }
    public void removeCategory(Category c) { categories.remove(c); }

    public void addReport(Report r)        { reports.add(r); }
    public void removeReport(Report r)     { reports.remove(r); }

    public void addAlert(Alert a)          { alerts.add(a); }
    public void removeAlert(Alert a)       { alerts.remove(a); }
}
