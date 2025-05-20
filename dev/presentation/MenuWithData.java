package inventory.presentation;

import inventory.domain.*;
import inventory.view.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class MenuWithData {
    private static final Scanner scan = new Scanner(System.in);
    private static final ProductsView productsView = new ProductsView(new ArrayList<>());
    private static final ItemsView itemsView = new ItemsView(new ArrayList<>());
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws ParseException {
        initializeSampleData();

        int choice;
        do {
            printMenu();
            choice = promptInt("Choose an option (1-18): ");
            handleChoice(choice);
        } while (choice != 18);

        scan.close();
    }

    private static void initializeSampleData() throws ParseException {
        // --- Products ---
        Product p1 = new Product("A1", "Cottage Tnuva", 3.0,   5.0,   "Tnuva", new ArrayList<>(), new ArrayList<>(), 2);
        Product p2 = new Product("A2", "Cottage Tara",  2.0,   5.0,   "Tara",  new ArrayList<>(), new ArrayList<>(), 3);
        Product p3 = new Product("SG1","Sunglasses",   100.0, 500.0, "RayBan",new ArrayList<>(), new ArrayList<>(), 2);
        Product p4 = new Product("S1", "Shirt",         60.0, 100.0, "Castro",new ArrayList<>(), new ArrayList<>(), 3);
        productsView.addProduct(p1);
        productsView.addProduct(p2);
        productsView.addProduct(p3);
        productsView.addProduct(p4);

        // --- Classifications ---
        Classification c1 = new Classification("cl1", "Dairy",   "Cottage", 250.0);
        Classification c2 = new Classification("cl2", "Dairy",   "Milk",    1000.0);
        Classification c3 = new Classification("cl3", "Fashion", "Summer",  1.0);
        Classification c4 = new Classification("cl4", "Dairy",   "Cottage", 150.0);

        // --- Initial Items ---
        addInitItem("I1", "Cottage1",    Location.Store,     "2026-01-01", c1, p1);
        addInitItem("I2", "Cottage2",    Location.Store,     "2026-01-05", c1, p1);
        addInitItem("I3", "Cottage3",    Location.Store,     "2026-01-01", c4, p2);
        addInitItem("I4", "Sunglasses1", Location.WareHouse, "2025-01-01", c3, p3); // expired
        addInitItem("I5", "Shirt1",      Location.Store,     "2026-08-01", c3, p4);
        addInitItem("I6", "Milk1",       Location.WareHouse, "2026-01-01", c2, p2);

        // Mark some as defective
        itemsView.markItemDefective("I2");
        itemsView.markItemDefective("I5");

        // Initial shortage alerts
        for (Product p : productsView.getAllProducts()) {
            if (p.getStockQuantity() < p.getMinQuantity()) {
                new Alert(p, new Date()).printShortageMessage();
            }
        }
    }

    private static void addInitItem(String iid, String name, Location loc,
                                    String expDateStr, Classification cls,
                                    Product prod) throws ParseException {
        Date exp = sdf.parse(expDateStr);
        Item it = new Item(iid, name, loc, exp, cls, prod);
        itemsView.addItem(it);
        prod.setStockQuantity(prod.getStockQuantity() + 1);
        if (loc == Location.WareHouse) {
            prod.setWarehouseQuantity(prod.getWarehouseQuantity() + 1);
        } else {
            prod.setShelfQuantity(prod.getShelfQuantity() + 1);
        }
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
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
        System.out.println("15. Expired items report");
        System.out.println("16. Show product price by ID");
        System.out.println("17. Update product price by ID");
        System.out.println("18. Exit");
    }

    private static void handleChoice(int choice) {
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
            case 15: generateExpiredReport();    break;
            case 16: showProductPrice();         break;
            case 17: updateProductPrice();       break;
            case 18: System.out.println("Goodbye!"); break;
            default: System.out.println("Invalid choice, please try again");
        }
    }

    // --- Prompt Helpers ---

    private static int promptInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format, please try again.");
            }
        }
    }

    private static double promptDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal number, please try again.");
            }
        }
    }

    private static Date promptDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            try {
                return sdf.parse(line);
            } catch (ParseException e) {
                System.out.println("Invalid date (use yyyy-MM-dd), please try again.");
            }
        }
    }

    private static String promptString(String prompt) {
        System.out.print(prompt);
        return scan.nextLine().trim();
    }

    // --- Core Operations ---

    private static Product getProductById(String pid) {
        return productsView.getAllProducts()
                .stream()
                .filter(p -> p.getPid().equals(pid))
                .findFirst()
                .orElse(null);
    }

    private static void addProduct() {
        System.out.println("=== Add product ===");
        String pid = promptString("Product ID: ");
        if (getProductById(pid) != null) {
            System.out.println("Product with ID '" + pid + "' already exists");
            return;
        }
        String name = promptString("Name: ");
        if (productsView.getProductByName(name) != null) {
            System.out.println("Product with name '" + name + "' already exists");
            return;
        }
        double costPrice     = promptDouble("Cost Price: ");
        double salePrice     = promptDouble("Sale Price: ");
        String manufacturer  = promptString("Manufacturer: ");
        int minQuantity      = promptInt("Min Quantity: ");

        Product product = new Product(
                pid, name, costPrice, salePrice,
                manufacturer, new ArrayList<>(), new ArrayList<>(), minQuantity
        );
        productsView.addProduct(product);
        System.out.println("Product successfully added");
    }

    private static void removeProduct() {
        System.out.println("=== Remove product ===");
        String pid = promptString("Product ID: ");
        Product p = getProductById(pid);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        boolean removed = productsView.removeProductByName(p.getName());
        System.out.println(removed ? "Product successfully removed" : "Failed to remove product");
    }

    private static void addItem() {
        System.out.println("=== Add item ===");
        String iid   = promptString("Item ID: ");
        String iname = promptString("Name: ");
        String pname = promptString("Product Name: ");
        Product prod = productsView.getProductByName(pname);
        if (prod == null) {
            System.out.println("Product '" + pname + "' does not exist, please add it first.");
            return;
        }
        String cid  = promptString("Classification ID: ");
        String cat  = promptString("Category: ");
        String sub  = promptString("Subcategory: ");
        double sz   = promptDouble("Size: ");
        Classification cls = new Classification(cid, cat, sub, sz);

        int locInt = promptInt("Location (0=WareHouse, 1=Store): ");
        Location loc = (locInt == 0) ? Location.WareHouse : Location.Store;

        Date expDate = promptDate("Expiration Date (yyyy-MM-dd): ");

        Item it = new Item(iid, iname, loc, expDate, cls, prod);
        itemsView.addItem(it);

        prod.setStockQuantity(prod.getStockQuantity() + 1);
        if (loc == Location.WareHouse) {
            prod.setWarehouseQuantity(prod.getWarehouseQuantity() + 1);
        } else {
            prod.setShelfQuantity(prod.getShelfQuantity() + 1);
        }

        if (prod.getStockQuantity() < prod.getMinQuantity()) {
            new Alert(prod, new Date()).printShortageMessage();
        }

        System.out.println("Item successfully added");
    }

    private static void removeItem() {
        System.out.println("=== Remove item ===");
        String name = promptString("Item Name: ");
        Item it = itemsView.getItemByName(name);
        if (it == null) {
            System.out.println("Item does not exist");
            return;
        }
        boolean removed = itemsView.removeItemByName(name);
        if (removed) {
            Product pr = it.getProduct();
            pr.setStockQuantity(pr.getStockQuantity() - 1);
            if (it.getLocation() == Location.WareHouse) {
                pr.setWarehouseQuantity(pr.getWarehouseQuantity() - 1);
            } else {
                pr.setShelfQuantity(pr.getShelfQuantity() - 1);
            }
            System.out.println("Item successfully removed");
        } else {
            System.out.println("Failed to remove item");
        }
    }

    private static void showProductsInStock() {
        System.out.println("=== Products in stock ===");
        productsView.display();
    }

    private static void showProductDetails() {
        System.out.println("=== Item details ===");
        String name = promptString("Item Name: ");
        Item it = itemsView.getItemByName(name);
        if (it == null) {
            System.out.println("Item does not exist");
        } else {
            it.display();
        }
    }

    private static void updateMinQuantity() {
        System.out.println("=== Update Min Quantity ===");
        String pid = promptString("Product ID: ");
        Product p = getProductById(pid);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        int newMin = promptInt("New Min Quantity (current " + p.getMinQuantity() + "): ");
        p.setMinQuantity(newMin);
        System.out.println("Min Quantity updated to " + p.getMinQuantity());
    }

    private static void addSupplierDiscount() {
        System.out.println("=== Add Supplier Discount ===");
        String name = promptString("Product Name: ");
        Product p = productsView.getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        String did    = promptString("Discount ID: ");
        String sup    = promptString("Supplier Name: ");
        double perc   = promptDouble("Discount Percentage: ");
        LocalDate sd  = LocalDate.parse(promptString("Start Date (yyyy-MM-dd): "));
        LocalDate ed  = LocalDate.parse(promptString("End Date   (yyyy-MM-dd): "));
        if (sd.isAfter(ed)) {
            System.out.println("Start Date must be on or before End Date");
            return;
        }
        p.addSupplierDiscount(new SupplierDiscount(did, sup, perc, sd, ed));
        System.out.println("Supplier Discount added");
    }

    private static void removeSupplierDiscount() {
        System.out.println("=== Remove Supplier Discount ===");
        String name = promptString("Product Name: ");
        Product p = productsView.getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        String did = promptString("Discount ID to remove: ");
        boolean removed = p.getSupplierDiscounts().removeIf(d -> d.getDisID().equals(did));
        System.out.println(removed ? "Supplier Discount removed" : "Discount ID not found");
    }

    private static void addPromotion() {
        System.out.println("=== Add Promotion ===");
        String name = promptString("Product Name: ");
        Product p = productsView.getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        String pid   = promptString("Promotion ID: ");
        double perc  = promptDouble("Discount Percentage: ");
        LocalDate sd = LocalDate.parse(promptString("Start Date (yyyy-MM-dd): "));
        LocalDate ed = LocalDate.parse(promptString("End Date   (yyyy-MM-dd): "));
        if (sd.isAfter(ed)) {
            System.out.println("Start Date must be on or before End Date");
            return;
        }
        p.addPromotion(new Promotion(pid, perc, sd, ed, p, null));
        System.out.println("Promotion added");
    }

    private static void removePromotion() {
        System.out.println("=== Remove Promotion ===");
        String name = promptString("Product Name: ");
        Product p = productsView.getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        String pid = promptString("Promotion ID to remove: ");
        boolean removed = p.getPromotions().removeIf(pr -> pr.getProID().equals(pid));
        System.out.println(removed ? "Promotion removed" : "Promotion ID not found");
    }

    private static void generateCategoryReport() {
        System.out.println("=== Inventory report by category ===");
        Set<String> cats = new HashSet<>(Arrays.asList(
                promptString("Enter categories (space-separated): ").split("\\s+")
        ));
        Set<Product> out = new HashSet<>();
        for (Item it : itemsView.getAllitems()) {
            String cat = it.getClassification().getCategory();
            String sub = it.getClassification().getSubcategory();
            if (cats.contains(cat) || cats.contains(sub)) {
                out.add(it.getProduct());
            }
        }
        new ReportsView(
                new StockReport(UUID.randomUUID().toString(), new Date(), new ArrayList<>(out))
        ).display();
    }

    private static void markDefective() {
        System.out.println("=== Mark item defective ===");
        String name = promptString("Item Name: ");
        boolean ok = itemsView.markItemDefective(name);
        System.out.println(ok ? "Item marked defective" : "Item not found");
    }

    private static void generateDefectiveReport() {
        System.out.println("=== Defective items report ===");
        new ReportsView(
                new DefectReport(UUID.randomUUID().toString(), new Date(), itemsView.getAllitems())
        ).display();
    }

    private static void generateExpiredReport() {
        System.out.println("=== Expired items report ===");
        Date now = new Date();
        List<Item> expired = new ArrayList<>();
        for (Item it : itemsView.getAllitems()) {
            if (it.getExpirationDate().before(now)) {
                expired.add(it);
            }
        }
        new ReportsView(
                new ExpiredReport(UUID.randomUUID().toString(), now, expired)
        ).display();
    }

    private static void showProductPrice() {
        System.out.println("=== Show product price ===");
        String pid = promptString("Product ID: ");
        Product p = getProductById(pid);
        if (p == null) {
            System.out.println("Product not found");
        } else {
            System.out.println("Name: " + p.getName() + " | Price: " + p.getSalePrice());
        }
    }

    private static void updateProductPrice() {
        System.out.println("=== Update product price ===");
        String pid = promptString("Product ID: ");
        Product p = getProductById(pid);
        if (p == null) {
            System.out.println("Product not found");
            return;
        }
        double np = promptDouble("New Price (current " + p.getSalePrice() + "): ");
        p.setSalePrice(np);
        System.out.println("Price updated to " + p.getSalePrice());
    }
}
