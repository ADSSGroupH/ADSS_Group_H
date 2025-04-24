package inventory.controller;

import inventory.domain.InventoryManager;
import inventory.domain.*;
import inventory.view.*;
import java.util.*;

public class CategoriesController {
    private InventoryManager manager;

    public CategoriesController(InventoryManager manager) {
        this.manager = manager;
    }

    public void showCategories() {
        List<Category> categories = manager.getAllCategories();
        CategoriesView view = new CategoriesView(categories);
        view.display();
    }

    public void chooseCategory(String cid) {
        // filter products by category...
    }
}
