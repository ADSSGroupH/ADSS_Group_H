package inventory.controller;

import inventory.domain.InventoryManager;

public class InventoryController {
    private InventoryManager manager;
    private ProductsController productsController;
    private CategoriesController categoriesController;
    private ReportsController reportsController;
    private AlertsController alertsController;

    public InventoryController() {
        manager = new InventoryManager();
        productsController = new ProductsController(manager);
        categoriesController = new CategoriesController(manager);
        reportsController = new ReportsController(manager);
        alertsController = new AlertsController(manager);
    }

    public void start() {
        // entry point: display main menu (not detailed here)
    }
}