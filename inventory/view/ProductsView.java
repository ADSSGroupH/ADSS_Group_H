package inventory.view;

import inventory.domain.Product;
import java.util.List;

public class ProductsView {
    private List<Product> products;
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public List<Product> getProducts() { return products; }
}
