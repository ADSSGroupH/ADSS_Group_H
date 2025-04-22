package inventory.domain;

import java.util.List;

public class Category {
    private String cid;
    private String name;
    private Category parent;
    private List<Category> subCategories;

    public Category(String cid, String name,
                    Category parent,
                    List<Category> subCategories) {
        this.cid           = cid;
        this.name          = name;
        this.parent        = parent;
        this.subCategories = subCategories;
    }
}

