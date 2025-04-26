package Service;

import Domain.Agreement;
import Domain.AgreementItem;
import Domain.Item;
import Domain.Supplier;

import java.util.*;

public class ItemService {
    private final Map<String, Item> items = new HashMap<>();

    // Ensure an item exists in the system, or create it if missing
    public void ensureItemExists(String itemId, String itemName) {
        if (!items.containsKey(itemId)) {
            items.put(itemId, new Item(itemId, itemName));
        }
    }

    // Get an item by its ID
    public Item getItemById(String itemId) {
        return items.get(itemId);
    }

    // Get all items in the system
    public Collection<Item> getAllItems() {
        return items.values();
    }

    // Add a new item to the system
    public void addItem(String itemId, String itemName) {
        if (!items.containsKey(itemId)) {
            items.put(itemId, new Item(itemId, itemName));
        }
    }

    // Get suppliers that sell a specific item (by catalog number)
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

    // Get the internal map of all items (ID to Item object)
    public Map<String, Item> getItemsMap() {
        return items;
    }
}
