package inventory.view;

import inventory.domain.Product;
import java.util.List;

public class ProductsView {
    private List<Product> products;
    public ProductsView(List<Product> products) {
        this.products = products;
    }
    public void display() {
        System.out.println("=== Products List ===");
        for (Product p : products) {
            System.out.printf("ID: %s | Name: %s | Stock: %d\n",
                    p.getPid(), p.getName(), p.getStockQuantity());
        }
    }
}
