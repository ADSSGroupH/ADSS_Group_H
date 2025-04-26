package inventory.view;

import inventory.domain.Classification;
import java.util.List;

/**
 * View for displaying the list of classifications (categories).
 */
public class CategoriesView {
    private final List<Classification> categories;

    /**
     * @param categories list of classifications to display (must not be null)
     */
    public CategoriesView(List<Classification> categories) {
        this.categories = categories;
    }

    /**
     * Prints each classification's ID, category, subcategory, and size.
     */
    public void display() {
        System.out.println("=== Categories List ===");
        for (Classification c : categories) {
            System.out.printf(
                    "ID: %s | Category: %s | Subcategory: %s | Size: %.2f%n",
                    c.getId(),
                    c.getCategory(),
                    c.getSubcategory(),
                    c.getsize()
            );
        }
    }
}
