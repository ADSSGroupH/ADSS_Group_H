package inventory.view;

import inventory.domain.Product;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class managing the list of products and displaying them,
 * including tracking defective items.
 */
public class ProductsView {
    private List<Product> products;

    /** יוצר View עם רשימת מוצרים קיימת */
    public ProductsView(List<Product> products) {
        this.products = products;
    }

    /** מציג את כל המוצרים ברשימה */
    public void display() {
        System.out.println("=== Products List ===");
        for (Product p : products) {
            System.out.printf("ID: %s | Name: %s | Stock: %d%n",
                    p.getPid(), p.getName(), p.getStockQuantity());
        }
    }

    /** מוסיף מוצר לרשימת המוצרים */
    public void addProduct(Product p) {
        if (p != null) {
            products.add(p);
        }
    }

    /**
     * מסיר מוצר מהרשימה לפי שם
     * @param name השם של המוצר
     * @return true אם מוצא והוסר, false אחרת
     */
    public boolean removeProductByName(String name) {
        Iterator<Product> it = products.iterator();
        boolean removed = false;
        while (it.hasNext()) {
            Product p = it.next();
            if (p.getName().equals(name)) {
                it.remove();
                removed = true;
                break;
            }
        }
        return removed;
    }

    /** מחזיר רשימה של כל המוצרים (עותק לשמירה על איטרטורים חיצוניים) */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /** מחפש ומחזיר מוצר לפי שם, או null אם לא קיים */
    public Product getProductByName(String name) {
        for (Product p : products) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
}
