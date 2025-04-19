//
//import java.util.*;
//
//public class SystemManager {
//    private Map<String, Supplier> suppliers = new HashMap<>();
//    private List<Order> orders = new ArrayList<>();
//    private Map<String, Item> items = new HashMap<>();
//
//
//
//    public void registerSupplier(Supplier s) {
//        suppliers.put(s.getSupplierId(), s); // שונה מ־getSupplierId()
//    }
//
//    public void placeOrder(Order o) {
//        orders.add(o);
//    }
//
//    public void printSuppliers() {
//        if (suppliers.isEmpty()) {
//            System.out.println("No suppliers registered.");
//            return;
//        }
//
//        int count = 1;
//        for (Supplier s : suppliers.values()) {
//            System.out.println("Supplier " + count + ": " + s);
//            count++;
//        }
//    }
//
//    public void printOrders() {
//        orders.forEach(System.out::println);
//    }
//
//    public Supplier getSupplierById(String companyId) {
//        return suppliers.get(companyId);
//    }
//
//    public Map<String, Supplier> getSuppliers() {
//        return suppliers;
//    }
//
//    public List<Order> getOrders() {
//        return orders;
//    }
//
//    // הוספת פריט חדש
//    public void addItem(Item item) {
//        items.put(item.getItemId(), item);
//    }
//
//    // קבלת פריט לפי מזהה
//    public Item getItemById(String itemId) {
//        return items.get(itemId);
//    }
//
//    // (אופציונלי) הדפסת כל הפריטים
//    public void printItems() {
//        for (Item item : items.values()) {
//            System.out.println(item);
//        }
//    }
//
//    // (אופציונלי) קבלת כל המוצרים
//    public Collection<Item> getAllItems() {
//        return items.values();
//    }
//
//}
