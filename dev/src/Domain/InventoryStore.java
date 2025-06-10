package Domain;

import java.util.*;

// InventoryStore’s lists are the “source of truth” for the entire application. The lists
// live in the Domain layer and hold every Product, Category, Report, or Alert ever created, in memory.
// Whenever you need to add, remove, or fetch the full dataset, you go via InventoryStore.

// in the View layer each View’s list holds exactly what the user sees right now.
// You typically call something like showProducts(), pull the master list out of InventoryStore, and then assign it to ProductsView.products.
// From there you can sort it, filter it, highlight selections, or even discard it entirely without touching the global data.

public class InventoryStore {
    private final List<Product> products           = new ArrayList<>();
    private final List<Classification> categories        = new ArrayList<>();
    private final List<Report> reports             = new ArrayList<>();
    private final List<Alert> alerts               = new ArrayList<>();

    // singleton instance
    private static final InventoryStore INSTANCE = new InventoryStore();
    private InventoryStore() {}
    public static InventoryStore getInstance() { return INSTANCE; }

    // methods to add/retrieve
    public void addProduct(Product p)      { products.add(p); }
    public List<Product> getProducts()     { return Collections.unmodifiableList(products); }

    public void addCategory(Classification c)    { categories.add(c); }
    public List<Classification> getCategories()  { return Collections.unmodifiableList(categories); }

    public void addReport(Report r)        { reports.add(r); }
    public List<Report> getReports()       { return Collections.unmodifiableList(reports); }

    public void addAlert(Alert a)          { alerts.add(a); }
    public List<Alert> getAlerts()         { return Collections.unmodifiableList(alerts); }
}