import java.util.*;

public class ItemService {
    private Map<String, Item> items = new HashMap<>();

    public void ensureItemExists(String itemId, String itemName) {
        if (!items.containsKey(itemId)) {
            items.put(itemId, new Item(itemId, itemName));
        }
    }

    public Item getItemById(String itemId) {
        return items.get(itemId);
    }

    public void printAllItems() {
        if (items.isEmpty()) {
            System.out.println("No items in the system.");
            return;
        }
        for (Item item : items.values()) {
            System.out.println(item);
        }
    }

    public Collection<Item> getAllItems() {
        return items.values();
    }

    // New method to add item explicitly
    public void addItem(String itemId, String itemName) {
        if (!items.containsKey(itemId)) {
            items.put(itemId, new Item(itemId, itemName));
            System.out.println("Item added: " + itemId + " - " + itemName);
        } else {
            System.out.println("Item with ID " + itemId + " already exists.");
        }
    }

    public Map<Supplier, String> getSuppliersForItem(String itemId, List<Supplier> suppliers) {
        Map<Supplier, String> result = new HashMap<>();
        for (Supplier supplier : suppliers) {
            for (Agreement agreement : supplier.getAgreements()) {
                for (AgreementItem ai : agreement.getItems().keySet()) {
                    if (ai.getCatalogNumber().equals(itemId)) {
                        result.put(supplier, ai.getCatalogNumber());
                    }
                }
            }
        }
        return result;
    }
}