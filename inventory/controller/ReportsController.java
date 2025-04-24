package inventory.controller;

import inventory.domain.InventoryManager;
import inventory.domain.*;
import java.util.*;

public class ReportsController {
    private InventoryManager manager;

    public ReportsController(InventoryManager manager) {
        this.manager = manager;
    }

    public void generateStockReport(List<Category> cats) {
        StockReport report = manager.generateOrderReport(cats);
        // display via ReportsView
    }

    public void generateDefectReport() {
        DefectReport report = manager.generateDefectReport();
        // display via ReportsView
    }
}