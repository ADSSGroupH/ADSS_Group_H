package Domain;

import java.util.*;

public class ProductRepository {
    private static ProductRepository instance;
    private Map<String, Product> products;

    // Private constructor (Singleton)
    private ProductRepository() {
        this.products = new HashMap<>();
    }

    // Singleton accessor
    public static ProductRepository getInstance() {
        if (instance == null) {
            synchronized (ProductRepository.class) {
                if (instance == null) {
                    instance = new ProductRepository();
                }
            }
        }
        return instance;
    }




    /**
     * Add a product to the repository
     */
    public void addProduct(Product p) {
        if (p != null && !products.containsKey(p.getPid())) {
            products.put(p.getPid(), p);
        }
    }

    public boolean removeProductById(String pid) {
        return products.remove(pid) != null;
    }

    /**
     * Remove a product by name
     */
    public boolean removeProductByName(String name) {
        String keyToRemove = null;
        for (Map.Entry<String, Product> entry : products.entrySet()) {
            if (entry.getValue().getName().equals(name)) {
                keyToRemove = entry.getKey();
                break;
            }
        }
        if (keyToRemove != null) {
            products.remove(keyToRemove);
            return true;
        }
        return false;
    }


    /**
     * Get a product by name
     */
    public Product getProductByName(String name) {
        for (Product p : products.values()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Get all products
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public void display() {
        System.out.println("=== Products List ===");
        for (Product p : products.values()) {
            System.out.printf("ID: %s | Name: %s | Stock: %d%n",
                    p.getPid(), p.getName(), p.getStockQuantity());
        }
    }

    public Product createProduct(String pid, String name, double costPrice, double salePrice, String manufacturer, int minQuantity) {
        // promotions and supplierDiscounts lists initialized empty
        return new Product(pid, name, costPrice, salePrice, manufacturer, new ArrayList<>(), new ArrayList<>(), minQuantity);
    }

        public boolean addSupplierDiscountToProduct(String productName, SupplierDiscount discount) {
        Product p = getProductByName(productName);
        if (p != null) {
            p.addSupplierDiscount(discount);
            return true;
        }
        return false;
    }



    public boolean removeSupplierDiscountFromProduct(String productName, String discountId) {
        Product p = getProductByName(productName);
        if (p == null) return false;

        return p.getSupplierDiscounts().removeIf(d -> d.getDisID().equals(discountId));
    }

    public Product getProductById(String itemId) {
        return products.get(itemId);
    }


}