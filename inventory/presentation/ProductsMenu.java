package inventory.presentation;

import inventory.view.ProductsView;

// **ProductsMenu** holds a reference to ProductsView
class ProductsMenu {
    private ProductsView productsList;
    public ProductsMenu() { this.productsList = new ProductsView(); }
    public ProductsView getProductsList() { return productsList; }
}
