package domain.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String id;
    private String name;
    private Category parent;                  
    private List<Category> subCategories = new ArrayList<>();

    public Category(String id, String name, Category parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        if (parent != null) {
            parent.subCategories.add(this);
        }
    }

    // Getters / Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public Category getParent() { return parent; }
    public List<Category> getsubCategories() { return subCategories; }
}
