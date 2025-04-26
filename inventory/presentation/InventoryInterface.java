package inventory.presentation;

import inventory.domain.InventoryStore;
import inventory.view.AlertsView;
import inventory.view.CategoriesView;
import inventory.view.ProductsView;
import inventory.view.ReportsView;

public class InventoryInterface {
    private final ProductsMenu productsMenu = new ProductsMenu();
    private final ProductsView productsView = new ProductsView();
    private final CategoriesMenu categoriesMenu = new CategoriesMenu();
    private final CategoriesView categoriesView = new CategoriesView();
    private final ReportsMenu reportsMenu = new ReportsMenu();
    private final ReportsView reportsView = new ReportsView();
    private final AlertsMenu alertsMenu = new AlertsMenu();
    private final AlertsView alertsView = new AlertsView();

    // factory methods
    public ProductsMenu   chooseProducts()   { return productsMenu;   }
    public ProductsView   showProducts()     {
        productsView.setProducts(InventoryStore.getInstance().getProducts());
        return productsView;
    }

    public CategoriesMenu chooseCategories() { return categoriesMenu; }
    public CategoriesView showCategories()   {
        categoriesView.setCategories(InventoryStore.getInstance().getCategories());
        return categoriesView;
    }

    public ReportsMenu    chooseReports()    { return reportsMenu;    }
    public ReportsView    showReports()      {
        reportsView.setReports(InventoryStore.getInstance().getReports());
        return reportsView;
    }

    public AlertsMenu     chooseAlerts()     { return alertsMenu;     }
    public AlertsView     showAlerts()       {
        alertsView.setAlerts(InventoryStore.getInstance().getAlerts());
        return alertsView;
    }
}