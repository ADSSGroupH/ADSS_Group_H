package inventory.presentation;

import inventory.domain.*;
import inventory.view.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class MenuwithData {
    private static final Scanner scan = new Scanner(System.in);
    private static final ProductsView productsView = new ProductsView(new ArrayList<>());
    private static final ItemsView itemsView = new ItemsView(new ArrayList<>());
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public static void main(String[] args) throws ParseException {

        // Adding data

        // Products
        Product product1 = new Product(
                "A1", "Cottage Tnuva", 3, 5, "Tnuva",
                new ArrayList<>(), new ArrayList<>(), 2);
        productsView.addProduct(product1);

        Product product2 = new Product(
                "A2", "Cottage Tara", 2, 5, "Tara",
                new ArrayList<>(), new ArrayList<>(), 3);
        productsView.addProduct(product2);

        Product product3 = new Product(
                "SG1", "Sunglasses", 100, 500, "RayBan",
                new ArrayList<>(), new ArrayList<>(), 2);
        productsView.addProduct(product3);

        Product product4 = new Product(
                "S1", "Shirt", 60, 100, "Castro",
                new ArrayList<>(), new ArrayList<>(), 3);
        productsView.addProduct(product4);

        // Classifications
        Classification class1 = new Classification("cl1", "Fashion", "Summer", 1);
        Classification class2 = new Classification("cl2", "Dairy", "Cottage", 250);
        Classification class3 = new Classification("cl3", "Dairy", "Milk", 1000);
        Classification class4 = new Classification("cl4", "Dairy", "Cottage", 150);

        // Items
        Item item1 = new Item("I1", "Cottage1", Location.Store, sdf.parse("2026-01-01"), class2, product1);
        itemsView.addItem(item1);
        Item item2 = new Item("I2", "Cottage2", Location.Store, sdf.parse("2026-01-05"), class2, product1);
        itemsView.addItem(item2);
        Item item3 = new Item("I3", "Cottage3", Location.Store, sdf.parse("2026-01-01"), class4, product2);
        itemsView.addItem(item3);
        Item item4 = new Item("I4", "Sunglasses1", Location.WareHouse, sdf.parse("2025-01-01"), class1, product3);
        itemsView.addItem(item4);
        Item item5 = new Item("I5", "Sh1", Location.Store, sdf.parse("2026-08-01"), class1, product4);
        itemsView.addItem(item5);
        Item item6 = new Item("I6", "Milk1", Location.WareHouse, sdf.parse("2026-01-01"), class3, product1);
        itemsView.addItem(item6);

        for (Product p : productsView.getAllProducts()){
            if (p.getStockQuantity() < p.getMinQuantity()){
                Alert alert = new Alert(p, new Date());
                alert.printShortageMessage();
            }
        }


// Menu
        int choice;
        do {
            System.out.println("Menu:");
            System.out.println("1.  Add product");
            System.out.println("2.  Remove product");
            System.out.println("3.  Add item");
            System.out.println("4.  Remove item");
            System.out.println("5.  Show all products in stock");
            System.out.println("6.  Show details of a specific item");
            System.out.println("7.  Update Min Quantity");
            System.out.println("8.  Add Supplier Discount");
            System.out.println("9.  Remove Supplier Discount");
            System.out.println("10. Add Promotion");
            System.out.println("11. Remove Promotion");
            System.out.println("12. Inventory report by category");
            System.out.println("13. Mark item as defective");
            System.out.println("14. Defective items report");
            System.out.println("15. Show product price by ID");
            System.out.println("16. Update product price by ID");
            System.out.println("17. Exit");
            System.out.print("Choose an option (1-17): ");
            choice = parseIntSafe(scan.nextLine());
            switch (choice) {
                case 1:  addProduct();               break;
                case 2:  removeProduct();            break;
                case 3:  addItem();                  break;
                case 4:  removeItem();               break;
                case 5:  showProductsInStock();      break;
                case 6:  showProductDetails();       break;
                case 7:  updateMinQuantity();        break;
                case 8:  addSupplierDiscount();      break;
                case 9:  removeSupplierDiscount();   break;
                case 10: addPromotion();             break;
                case 11: removePromotion();          break;
                case 12: generateCategoryReport();   break;
                case 13: markDefective();            break;
                case 14: generateDefectiveReport();  break;
                case 15: showProductPrice();         break;
                case 16: updateProductPrice();       break;
                case 17: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice, please try again");
            }
        } while (choice != 17);
        scan.close();
    }

    private static int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return -1; }
    }

    private static void addProduct() {
        System.out.println("=== Add product ===");
        System.out.print("Product ID: "); String pid = scan.nextLine().trim();
        System.out.print("Name: "); String name = scan.nextLine().trim();

        Product product = productsView.getProductByName(name);
        if (product == null){
            System.out.print("Cost Price: "); double costPrice = Double.parseDouble(scan.nextLine().trim());
            System.out.print("Sale Price: "); double salePrice = Double.parseDouble(scan.nextLine().trim());
            System.out.print("Manufacturer: "); String manufacturer = scan.nextLine().trim();
            System.out.print("Min Quantity: "); int minQuantity = Integer.parseInt(scan.nextLine().trim());

            product = new Product(
                    pid, name, costPrice, salePrice, manufacturer,
                    new ArrayList<>(), new ArrayList<>(), minQuantity);
            productsView.addProduct(product);
            System.out.println("Product is successfully added");}
        else System.out.println("Product is already exist");}


    private static void addItem() throws ParseException {
        System.out.print("Item ID: "); String iid = scan.nextLine().trim();
        System.out.print("Name: "); String iname = scan.nextLine().trim();

        System.out.print("Product Name: "); String name = scan.nextLine().trim();

        Product product = productsView.getProductByName(name);
        System.out.print("Classification ID: "); String classId = scan.nextLine().trim();
        System.out.print("Category: "); String category = scan.nextLine().trim();
        System.out.print("Subcategory: "); String subcategory = scan.nextLine().trim();
        System.out.print("Size: "); double size = Double.parseDouble(scan.nextLine().trim());
        Classification cls = new Classification(classId, category, subcategory, size);
        System.out.print("Location (WareHouse=0, Store=1): ");
        Location loc = parseIntSafe(scan.nextLine()) == 0 ? Location.WareHouse : Location.Store;
        System.out.print("Expiration Date (yyyy-MM-dd): "); Date exp = sdf.parse(scan.nextLine().trim());


        Item item = new Item(iid, iname, loc, exp, cls, product);
        itemsView.addItem(item);

        if (item.getProduct().getStockQuantity() < item.getProduct().getMinQuantity()){
            Alert alert = new Alert(item.getProduct(), new Date());
            alert.printShortageMessage();
        }
    }

    private static void removeProduct() {
        System.out.println("=== Remove product ===");
        System.out.print("Name: "); String name = scan.nextLine().trim();
        boolean removed = productsView.removeProductByName(name);
        System.out.println(removed ? "Product is successfully removed" : "Product is not exist");
    }

    private static void removeItem() {
        System.out.println("=== Remove item ===");
        System.out.print("Name: "); String name = scan.nextLine().trim();

        Item item = itemsView.getItemByName(name);
        item.getProduct().setStockQuantity(item.getProduct().getStockQuantity() - 1);
        if (item.getLocation() == Location.WareHouse) {
            item.getProduct().setWarehouseQuantity(
                    item.getProduct().getWarehouseQuantity() - 1
            );
        } else {
            item.getProduct().setShelfQuantity(
                    item.getProduct().getShelfQuantity() - 1
            );
        }

        boolean removed = itemsView.removeItemByName(name);
        System.out.println(removed ? "Item is successfully removed" : "Item is not exist");
    }

    private static void showProductsInStock() {
        System.out.println("=== Products in stock ===");
        productsView.display();
    }

    private static void showProductDetails() {
        System.out.println("=== Item details ===");
        try {
            System.out.print("Item Name: "); String pname = scan.nextLine().trim();
            Item item = itemsView.getItemByName(pname);
            if (item == null) {
                System.out.println("Item is not exist"); return;
            }
            item.display();
        } catch (Exception e) {
            System.out.println("Error in item details: " + e.getMessage());
        }
    }

    private static void updateMinQuantity() {
        System.out.println("=== Update Min Quantity ===");
        System.out.print("Enter Product ID: ");
        String pid = scan.nextLine().trim();
        Product p = productsView.getAllProducts().stream()
                .filter(x -> x.getPid().equals(pid))
                .findFirst().orElse(null);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        System.out.print("Current Min Quantity: " + p.getMinQuantity() + ". Enter new Min Quantity: ");
        try {
            int newMin = Integer.parseInt(scan.nextLine().trim());
            p.setMinQuantity(newMin);
            System.out.println("Min Quantity updated to " + p.getMinQuantity());
        } catch (Exception e) {
            System.out.println("Invalid number format");
        }
    }

    private static void addSupplierDiscount() {
        System.out.println("=== Add Supplier Discount ===");
        try {
            System.out.print("Product Name: "); String name = scan.nextLine().trim();
            Product p = productsView.getProductByName(name);
            if (p == null) {
                System.out.println("Product is not exist"); return;
            }
            System.out.print("Discount ID: "); String disID = scan.nextLine().trim();
            System.out.print("Supplier Name: "); String supName = scan.nextLine().trim();
            System.out.print("Discount Percentage: "); double dp = Double.parseDouble(scan.nextLine().trim());
            System.out.print("Start Date (yyyy-MM-dd): "); LocalDate sd = LocalDate.parse(scan.nextLine().trim());
            System.out.print("End Date   (yyyy-MM-dd): "); LocalDate ed = LocalDate.parse(scan.nextLine().trim());
            p.addSupplierDiscount(new SupplierDiscount(disID, supName, dp, sd, ed));
            System.out.println("Supplier Discount is successfully added");
        } catch (Exception e) {
            System.out.println("Error in Update Supplier Discount: " + e.getMessage());
        }
    }

    private static void removeSupplierDiscount() {
        System.out.println("=== Remove Supplier Discount ===");
        System.out.print("Product Name: ");
        String name = scan.nextLine().trim();
        Product p = productsView.getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        System.out.print("Discount ID to remove: ");
        String disID = scan.nextLine().trim();

        boolean removed = p.getSupplierDiscounts()
                .removeIf(d -> d.getDisID().equals(disID));
        System.out.println(removed
                ? "Supplier Discount removed successfully"
                : "Discount ID not found");
    }

    private static void addPromotion() {
        System.out.println("=== Add Promotion ===");
        try {
            System.out.print("Product Name: "); String name = scan.nextLine().trim();
            Product p = productsView.getProductByName(name);
            if (p == null) {
                System.out.println("Product is not exist"); return;
            }
            System.out.print("Discount ID: "); String disID = scan.nextLine().trim();
            System.out.print("Discount Percentage: "); double dp = Double.parseDouble(scan.nextLine().trim());
            System.out.print("Start Date (yyyy-MM-dd): "); LocalDate sd = LocalDate.parse(scan.nextLine().trim());
            System.out.print("End Date   (yyyy-MM-dd): "); LocalDate ed = LocalDate.parse(scan.nextLine().trim());
            p.addPromotion(new Promotion(disID, dp, sd, ed, p, null));
            System.out.println("Promotion successfully added");
        } catch (Exception e) {
            System.out.println("Error adding promotion: " + e.getMessage());
        }
    }

    /**
     * Remove a promotion by its ID.
     */
    private static void removePromotion() {
        System.out.println("=== Remove Promotion ===");
        System.out.print("Product Name: ");
        String name = scan.nextLine().trim();
        Product p = productsView.getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        System.out.print("Promotion ID to remove: ");
        String proID = scan.nextLine().trim();

        boolean removed = p.getPromotions()
                .removeIf(pr -> pr.getProID().equals(proID));
        System.out.println(removed
                ? "Promotion removed successfully"
                : "Promotion ID not found");
    }

    private static void generateCategoryReport() {
        System.out.println("Which categories?");
        String stringCategories = scan.nextLine();
        List<String> categories = Arrays.asList(stringCategories.split(" "));

// 3. סינון ובנייה של רשימת מוצרים
        List<Product> filteredProducts = new ArrayList<>();
        for (Item item : itemsView.getAllitems()) {
            // נקבל את שם הקטגוריה והסאב־קטגוריה (בהנחה שיש getName())
            String cat = item.getClassification().getCategory();
            String subcat = item.getClassification().getSubcategory();

            if (categories.contains(cat) || categories.contains(subcat)) {
                filteredProducts.add(item.getProduct());
            }
        }
        Report rep = new StockReport(
                UUID.randomUUID().toString(), new Date(),
                filteredProducts);
        new ReportsView(rep).display();
    }

    private static void markDefective() {
        System.out.print("Item Name: "); String name = scan.nextLine().trim();
        boolean ok = itemsView.markItemDefective(name);
        System.out.println(ok ? "Item is successfully marked as Defective" : "Item is not exist");
    }

    private static void generateDefectiveReport() {
        Report rep = new DefectReport(
                UUID.randomUUID().toString(), new Date(),
                itemsView.getAllitems());
        new ReportsView(rep).display();
    }

    private static void showProductPrice() {
        System.out.print("Enter Product ID: "); String pid = scan.nextLine().trim();
        Product found = productsView.getAllProducts().stream()
                .filter(x -> x.getPid().equals(pid))
                .findFirst().orElse(null);
        if (found == null) {
            System.out.println("Product is not exist"); return;
        }
        System.out.println("Name: " + found.getName() + " | Price: " + found.getSalePrice());
    }

    private static void updateProductPrice() {
        System.out.print("Enter Product ID: "); String pid = scan.nextLine().trim();
        Product found = productsView.getAllProducts().stream()
                .filter(x -> x.getPid().equals(pid))
                .findFirst().orElse(null);
        if (found == null) {
            System.out.println("Product is not exist"); return;
        }
        System.out.print("Current: " + found.getSalePrice() + ". Enter new Price: ");
        try {
            double np = Double.parseDouble(scan.nextLine().trim());
            found.setSalePrice(np);
            System.out.println("Updated to " + found.getSalePrice());
        } catch (Exception e) {
            System.out.println("Invalid format");
        }
    }
}
