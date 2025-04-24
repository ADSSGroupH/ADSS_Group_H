package inventory.view;

import inventory.domain.Category;
import java.util.List;

public class CategoriesView {
    private List<Category> categories;
    public CategoriesView(List<Category> categories) {
        this.categories = categories;
    }
    public void display() {
        System.out.println("=== Categories List ===");
        for (Category c : categories) {
            System.out.printf("ID: %s | Name: %s\n",
                    c.getId(), c.getName());
        }
    }
}
