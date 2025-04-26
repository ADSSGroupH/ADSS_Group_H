package Domain;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String itemId;
    private String name;
    private Map<Supplier, String> suppliers = new HashMap<>(); // supplier -> catalog number

    public Item(String itemId, String name) {
        this.itemId = itemId;
        this.name = name;
    }

    // Getters
    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public Map<Supplier, String> getSuppliers() {
        return suppliers;
    }

    // Setters
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return
                "name='" + name + '\'' +
                ", itemId='" + itemId + '\'' ;
    }
}
