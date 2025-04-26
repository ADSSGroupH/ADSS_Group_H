package inventory.view;

import inventory.domain.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Class managing the list of items and displaying them,
 * including tracking defective items.
 */
public class ItemsView {
    private List<Item> items;
    private List<Item> defectiveitems = new ArrayList<>();

    /** יוצר View עם רשימת מוצרים קיימת */
    public ItemsView(List<Item> items) {
        this.items = items;
    }


    /** מוסיף מוצר לרשימת המוצרים */
    public void addItem(Item p) {
        if (p != null) {
            items.add(p);
        }
    }

    /**
     * מסיר מוצר מהרשימה לפי שם
     * @param name השם של המוצר
     * @return true אם מוצא והוסר, false אחרת
     */
    public boolean removeItemByName(String name) {
        Iterator<Item> it = items.iterator();
        boolean removed = false;
        while (it.hasNext()) {
            Item p = it.next();
            if (p.getName().equals(name)) {
                it.remove();
                removed = true;
                break;
            }
        }
        if (removed) {
            // הסרה גם מרשימת המוצרים הפגומים אם היה
            defectiveitems.removeIf(p -> p.getName().equals(name));
        }
        return removed;
    }

    /** מחזיר רשימה של כל המוצרים (עותק לשמירה על איטרטורים חיצוניים) */
    public List<Item> getAllitems() {
        return new ArrayList<>(items);
    }

    /** מחפש ומחזיר מוצר לפי שם, או null אם לא קיים */
    public Item getItemByName(String name) {
        for (Item p : items) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * מסמן מוצר כפגום ומוסיף לרשימת המוצרים הפגומים
     * @param name השם של המוצר
     * @return true אם הצליח לסמן, false אם לא נמצא
     */
    public boolean markItemDefective(String name) {
        Item p = getItemByName(name);
        if (p == null) return false;
        p.setDefect(true);
        if (!defectiveitems.contains(p)) {
            defectiveitems.add(p);
        }
        return true;
    }

    /** מחזירה רשימה של כל המוצרים שסומנו כפגומים */
    public List<Item> getDefectiveitems() {
        return new ArrayList<>(defectiveitems);
    }

    /** מציגה את דוח המוצרים הפגומים */
    public void displayDefectiveitems() {
        System.out.println("=== Defective items Report ===");
        if (defectiveitems.isEmpty()) {
            System.out.println("אין מוצרים פגומים");
            return;
        }
        for (Item p : defectiveitems) {
            System.out.printf("ID: %s | Name: %s",
                    p.getIid(), p.getName());
        }
    }
}
