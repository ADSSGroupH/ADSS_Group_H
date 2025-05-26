package inventory.presentation;

import inventory.domain.*;
import inventory.view.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Interactive console menu for inventory management.
 * Supports products, items, discounts, promotions, and various reports.
 */
public class Menu {
    // Scanner for reading user input
    private static final Scanner scan = new Scanner(System.in);
    // View for displaying products
    private static final ProductsView productsView = new ProductsView(new ArrayList<>());
    // View for displaying items
    private static final ItemsView itemsView = new ItemsView(new ArrayList<>());
    // Date format for expiration dates
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = promptInt("Choose an option (1-18): ");
            handleChoice(choice);
        } while (choice != 18);
        scan.close();
    }

    /**
     * Display the main menu options.
     */
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

    /**
     * Route the user's menu choice to the corresponding method.
     */
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

    /**
     * Retrieve a product by its ID.
     * @param pid Product ID
     * @return matching Product or null if not found
     */
    private static Product getProductById(String pid) {
        return productsView.getAllProducts()
                .stream()
                .filter(p -> p.getPid().equals(pid))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add a new product to the system.
     * Ensures unique ID and name.
     */
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
        double costPrice    = promptDouble("Cost Price: ");
        double salePrice    = promptDouble("Sale Price: ");
        String manufacturer = promptString("Manufacturer: ");
        int    minQuantity  = promptInt("Min Quantity: ");

        Product product = new Product(
                pid, name, costPrice, salePrice,
                manufacturer,
                new ArrayList<>(), new ArrayList<>(),
                minQuantity
        );
        productsView.addProduct(product);
        System.out.println("Product successfully added");
    }

    /**
     * Remove an existing product by ID.
     */
    private static void removeProduct() {
        System.out.println("=== Remove product ===");
        String pid = promptString("Product ID: ");
        Product p = getProductById(pid);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        boolean removed = productsView.removeProductByName(p.getName());
        System.out.println(removed
                ? "Product successfully removed"
                : "Failed to remove product"
        );
    }

    /**
     * Add a new item (unit) under an existing product.
     * Updates stock and warehouse/shelf counts.
     */
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
        String cid        = promptString("Classification ID: ");
        String category   = promptString("Category: ");
        String subcat     = promptString("Subcategory: ");
        double size       = promptDouble("Size: ");
        Classification cls = new Classification(cid, category, subcat, size);

        int locInt = promptInt("Location (0=WareHouse, 1=Store): ");
        Location loc = (locInt == 0) ? Location.WareHouse : Location.Store;

        Date expDate = promptDate("Expiration Date (yyyy-MM-dd): ");

        Item it = new Item(iid, iname, loc, expDate, cls, prod);
        itemsView.addItem(it);

        // update stock counts
        prod.setStockQuantity(prod.getStockQuantity() + 1);
        if (loc == Location.WareHouse) {
            prod.setWarehouseQuantity(prod.getWarehouseQuantity() + 1);
        } else {
            prod.setShelfQuantity(prod.getShelfQuantity() + 1);
        }

        // check for shortage
        if (prod.getStockQuantity() < prod.getMinQuantity()) {
            new Alert(prod, new Date()).printShortageMessage();
        }

        System.out.println("Item successfully added");
    }

    /**
     * Remove an existing item by its name.
     * Updates stock counts accordingly.
     */
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

    /**
     * Display all products currently in stock.
     */
    private static void showProductsInStock() {
        System.out.println("=== Products in stock ===");
        productsView.display();
    }

    /**
     * Show details for a specific item by its name.
     */
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

    /**
     * Update the minimum quantity threshold for a product.
     */
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

    /**
     * Add a supplier discount to a product.
     */
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
        LocalDate sd  = promptLocalDate("Start Date (yyyy-MM-dd): ");
        LocalDate ed  = promptLocalDate("End Date   (yyyy-MM-dd): ");
        if (sd.isAfter(ed)) {
            System.out.println("Start Date must be on or before End Date");
            return;
        }
        p.addSupplierDiscount(new SupplierDiscount(did, sup, perc, sd, ed));
        System.out.println("Supplier Discount added");
    }

    /**
     * Remove a supplier discount by its ID.
     */
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

    /**
     * Add a promotion to a product.
     */
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
        LocalDate sd = promptLocalDate("Start Date (yyyy-MM-dd): ");
        LocalDate ed = promptLocalDate("End Date   (yyyy-MM-dd): ");
        if (sd.isAfter(ed)) {
            System.out.println("Start Date must be on or before End Date");
            return;
        }
        p.addPromotion(new Promotion(pid, perc, sd, ed, p, null));
        System.out.println("Promotion added");
    }

    /**
     * Remove a promotion by its ID.
     */
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

    /**
     * Inventory report filtered by category, subcategory, or size.
     */
    private static void generateCategoryReport() {
        System.out.println("=== Inventory report by category/subcategory/size ===");

        // Prompt for categories
        String lineCats = promptString("Enter categories (space-separated, or blank for all): ");
        Set<String> cats = new HashSet<>();
        if (!lineCats.isBlank()) {
            cats.addAll(Arrays.asList(lineCats.split("\\s+")));
        }

        // Prompt for subcategories
        String lineSubcats = promptString("Enter subcategories (space-separated, or blank for all): ");
        Set<String> subCats = new HashSet<>();
        if (!lineSubcats.isBlank()) {
            subCats.addAll(Arrays.asList(lineSubcats.split("\\s+")));
        }

        // Prompt for sizes
        Set<Double> sizes = promptDoubleSet("Enter sizes (space-separated, or blank for all): ");

        // Filter products
        Set<Product> filtered = new LinkedHashSet<>();
        for (Item it : itemsView.getAllitems()) {
            Classification cls = it.getClassification();
            String cat = cls.getCategory();
            String sub = cls.getSubcategory();
            double sz = cls.getSize();

            if ((!cats.isEmpty() && cats.contains(cat)) ||
                    (!subCats.isEmpty() && subCats.contains(sub)) ||
                    (!sizes.isEmpty() && sizes.contains(sz))) {
                filtered.add(it.getProduct());
            }
        }

        new ReportsView(new StockReport(UUID.randomUUID().toString(), new Date(), new ArrayList<>(filtered))).display();
    }

    /**
     * Mark an item as defective.
     */
    private static void markDefective() {
        System.out.println("=== Mark item defective ===");
        String name = promptString("Item Name: ");
        boolean ok = itemsView.markItemDefective(name);
        System.out.println(ok ? "Item marked defective" : "Item not found");
    }

    /**
     * Display a report of all defective items.
     */
    private static void generateDefectiveReport() {
        System.out.println("=== Defective items report ===");
        new ReportsView(
                new DefectReport(UUID.randomUUID().toString(), new Date(), itemsView.getAllitems())
        ).display();
    }

    /**
     * Display a report of all expired items.
     */
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

    /**
     * Show the sale price of a product by ID.
     */
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

    /**
     * Update the sale price of a product by ID.
     */
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

    // ------------------ Helper Methods ------------------

    /**
     * Prompt the user for an integer, retrying until valid.
     */
    private static int promptInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please try again.");
            }
        }
    }

    /**
     * Prompt the user for a double, retrying until valid.
     */
    private static double promptDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal, please try again.");
            }
        }
    }

    /**
     * Prompt the user for a date (yyyy-MM-dd), retrying until valid.
     */
    private static Date promptDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return sdf.parse(scan.nextLine().trim());
            } catch (ParseException e) {
                System.out.println("Invalid date (yyyy-MM-dd), please try again.");
            }
        }
    }

    /**
     * Prompt the user for a LocalDate (yyyy-MM-dd), retrying until valid.
     * Used for discounts and promotions.
     */
    private static LocalDate promptLocalDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scan.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date (yyyy-MM-dd), please try again.");
            }
        }
    }

    /**
     * Prompt the user for a line of text.
     */
    private static String promptString(String prompt) {
        System.out.print(prompt);
        return scan.nextLine().trim();
    }

    /**
     * Prompt the user for a set of doubles (space-separated), retrying until all valid.
     * An empty input line returns an empty set (matches all sizes).
     */
    private static Set<Double> promptDoubleSet(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scan.nextLine().trim();
            if (line.isBlank()) {
                return Collections.emptySet();
            }
            String[] toks = line.split("\\s+");
            Set<Double> out = new HashSet<>();
            boolean allValid = true;
            for (String t : toks) {
                try {
                    out.add(Double.parseDouble(t));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid size '" + t + "'. Please enter decimal numbers.");
                    allValid = false;
                    break;
                }
            }
            if (allValid) {
                return out;
            }
        }
    }
}
