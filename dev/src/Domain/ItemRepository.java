package Domain;

import java.util.*;

public class ItemRepository {
    private static ItemRepository instance;

    private Map<String, Item> items; // key: item name
    private Set<String> defectiveItemNames;

    // Singleton constructor
    private ItemRepository() {
        items = new HashMap<>();
        defectiveItemNames = new HashSet<>();
    }

    public static ItemRepository getInstance() {
        if (instance == null) {
            synchronized (ItemRepository.class) {
                if (instance == null) {
                    instance = new ItemRepository();
                }
            }
        }
        return instance;
    }

    /** Adds an item to the repository */
    public void addItem(Item item) {
        if (item != null && !items.containsKey(item.getName())) {
            items.put(item.getName(), item);
        }
    }

    /** Removes an item by name */
    public boolean removeItemByName(String name) {
        if (items.containsKey(name)) {
            items.remove(name);
            defectiveItemNames.remove(name);
            return true;
        }
        return false;
    }

    /** Returns an item by name */
    public Item getItemByName(String name) {
        return items.get(name);
    }

    /** Returns all items */
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    /** Marks an item as defective */
    public boolean markItemDefective(String name) {
        Item item = getItemByName(name);
        if (item != null) {
            item.setDefect(true);
            defectiveItemNames.add(name);
            return true;
        }
        return false;
    }

    /** Returns a list of all defective items */
    public List<Item> getDefectiveItems() {
        List<Item> result = new ArrayList<>();
        for (String name : defectiveItemNames) {
            Item item = getItemByName(name);
            if (item != null) result.add(item);
        }
        return result;
    }

    /** Displays all defective items */
    public void displayDefectiveItems() {
        System.out.println("=== Defective Items Report ===");
        if (defectiveItemNames.isEmpty()) {
            System.out.println("No Defective Items");
            return;
        }

        for (String name : defectiveItemNames) {
            Item item = items.get(name);
            if (item != null) {
                System.out.printf("ID: %s | Name: %s%n", item.getIid(), item.getName());
            }
        }
    }

    /** Displays detailed information for a specific item */
    public void displayItemDetails(String name) {
        Item item = getItemByName(name);
        if (item == null) {
            System.out.println("Item not found");
            return;
        }

        System.out.println("=== Item Details ===");
        System.out.println("ID: " + item.getIid());
        System.out.println("Name: " + item.getName());
        System.out.println("Location: " + item.getLocation());
        System.out.println("Cost Price: " + item.getProduct().getCostPrice());
        System.out.println("Sale Price: " + item.getProduct().getSalePrice());
        System.out.println("Manufacturer: " + item.getProduct().getManufacturer());
        System.out.println("Stock Quantity: " + item.getProduct().getStockQuantity());
        System.out.println("Min Quantity: " + item.getProduct().getMinQuantity());
        System.out.println("Warehouse Quantity: " + item.getProduct().getWarehouseQuantity());
        System.out.println("Shelf Quantity: " + item.getProduct().getShelfQuantity());
        System.out.println("Expiration Date: " + item.getExpirationDate());
        System.out.println("Category: " + item.getClassification().getCategory()
                + " SubCategory: " + item.getClassification().getSubcategory()
                + " Size: " + item.getClassification().getsize());
        System.out.println("Promotions: " + item.getProduct().getPromotions());
        System.out.println("Supplier Discounts: " + item.getProduct().getSupplierDiscounts());
    }
}