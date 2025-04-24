package inventory.controller;

import inventory.domain.InventoryManager;
import inventory.domain.*;
import inventory.view.*;
import java.util.*;

public class ProductsController {
    private InventoryManager manager;

    public ProductsController(InventoryManager manager) {
        this.manager = manager;
    }

    public void showProducts() {
        List<Product> products = manager.getAllProducts();
        ProductsView view = new ProductsView(products);
        view.display();
    }

    public void showProductDetails(String pid) {
        // now delegates printing to the service
        manager.getProductDetails(pid);
    }
}
