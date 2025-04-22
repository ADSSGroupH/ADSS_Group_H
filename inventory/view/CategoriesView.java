package inventory.view;

import inventory.domain.Category;
import java.util.List;

public class CategoriesView {
    private List<Category> categories;
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    public List<Category> getCategories() { return categories; }
}
