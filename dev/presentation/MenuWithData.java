package inventory.presentation;

import inventory.domain.*;
import inventory.view.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class MenuWithData {
    private static final Scanner scan = new Scanner(System.in);
    private static final ProductsView productsView = new ProductsView(new ArrayList<>());
    private static final ItemsView itemsView = new ItemsView(new ArrayList<>());
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws ParseException {
        System.out.print("Initialize with sample data? (Y/N): ");
        String ans = scan.nextLine().trim();
        if (ans.equalsIgnoreCase("Y")) {
            initializeSampleData();
        }

        int mainChoice;
        do {
            printMainMenu();
            mainChoice = promptInt("Choose an option (1-5): ");
            switch (mainChoice) {
                case 1 -> handleProductsMenu();
                case 2 -> handleItemsMenu();
                case 3 -> handleDiscountsMenu();
                case 4 -> handleReportsMenu();
                case 5 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (mainChoice != 5);

        scan.close();
    }

    private static void printMainMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Manage Products");
        System.out.println("2. Manage Items");
        System.out.println("3. Manage Discounts & Promotions");
        System.out.println("4. Reports");
        System.out.println("5. Exit");
    }

    private static void handleProductsMenu() {
        int choice;
        do {
            System.out.println("Manage Products:");
            System.out.println("1. Add product");
            System.out.println("2. Remove product");
            System.out.println("3. Update Min Quantity");
            System.out.println("4. Show product price by ID");
            System.out.println("5. Update product price by ID");
            System.out.println("6. Back to Main Menu");
            choice = promptInt("Choose an option (1-6): ");
            switch (choice) {
                case 1 -> addProduct();
                case 2 -> removeProduct();
                case 3 -> updateMinQuantity();
                case 4 -> showProductPrice();
                case 5 -> updateProductPrice();
                case 6 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 6);
    }

    private static void handleItemsMenu() {
        int choice;
        do {
            System.out.println("Manage Items:");
            System.out.println("1. Add item");
            System.out.println("2. Remove item");
            System.out.println("3. Show item details");
            System.out.println("4. Mark item as defective");
            System.out.println("5. Mark item as expired");
            System.out.println("6. Back to Main Menu");
            choice = promptInt("Choose an option (1-6): ");
            switch (choice) {
                case 1 -> addItem();
                case 2 -> removeItem();
                case 3 -> showProductDetails();
                case 4 -> markDefective();
                case 5 -> markExpired();
                case 6 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 6);
    }

    private static void handleDiscountsMenu() {
        int choice;
        do {
            System.out.println("Manage Discounts & Promotions:");
            System.out.println("1. Add Supplier Discount");
            System.out.println("2. Remove Supplier Discount");
            System.out.println("3. Add Promotion");
            System.out.println("4. Remove Promotion");
            System.out.println("5. Back to Main Menu");
            choice = promptInt("Choose an option (1-5): ");
            switch (choice) {
                case 1 -> addSupplierDiscount();
                case 2 -> removeSupplierDiscount();
                case 3 -> addPromotion();
                case 4 -> removePromotion();
                case 5 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 5);
    }

    private static void handleReportsMenu() {
        int choice;
        do {
            System.out.println("Reports:");
            System.out.println("1. List all products in stock");
            System.out.println("2. Inventory report by category");
            System.out.println("3. Defective items report");
            System.out.println("4. Expired items report");
            System.out.println("5. Back to Main Menu");
            choice = promptInt("Choose an option (1-5): ");
            switch (choice) {
                case 1 -> showProductsInStock();
                case 2 -> generateCategoryReport();
                case 3 -> generateDefectiveReport();
                case 4 -> generateExpiredReport();
                case 5 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 5);
    }

    private static void initializeSampleData() {
        // Create example products
        Product p1 = new Product("P1", "Milk", 2.0, 3.5, "Tnuva", new ArrayList<>(), new ArrayList<>(), 5);
        Product p2 = new Product("P2", "Bread", 1.0, 2.5, "Angel", new ArrayList<>(), new ArrayList<>(), 3);
        Product p3 = new Product("P3", "Shampoo", 10.0, 15.0, "Head&Shoulders", new ArrayList<>(), new ArrayList<>(), 2);

        productsView.addProduct(p1);
        productsView.addProduct(p2);
        productsView.addProduct(p3);

        // Create example classifications
        Classification c1 = new Classification("C1", "Dairy", "Milk", 1.0);
        Classification c2 = new Classification("C2", "Bakery", "Bread", 0.5);
        Classification c3 = new Classification("C3", "Toiletries", "Hair", 0.3);

        // Create example items
        itemsView.addItem(new Item("I1", "Milk1", Location.Store, new Date(System.currentTimeMillis() + 86400000), c1, p1));
        itemsView.addItem(new Item("I2", "Bread1", Location.WareHouse, new Date(System.currentTimeMillis() + 172800000), c2, p2));
        itemsView.addItem(new Item("I3", "Shampoo1", Location.Store, new Date(System.currentTimeMillis() + 259200000), c3, p3));

        p1.setStockQuantity(1); p1.setShelfQuantity(1);
        p2.setStockQuantity(1); p2.setWarehouseQuantity(1);
        p3.setStockQuantity(1); p3.setShelfQuantity(1);

        // Mark an item as defective
        itemsView.markItemDefective("I2");

        // Print initial alerts if any
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
        if (iname.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }
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
        if (sz <= 0) {
            System.out.println("Size must be a positive number.");
            return;
        }
        Classification cls = new Classification(cid, cat, sub, sz);

        int locInt = promptInt("Location (0=WareHouse, 1=Store): ");
        if (locInt != 0 && locInt != 1) {
            System.out.println("Invalid location. Please enter 0 or 1.");
            return;
        }
        Location loc = (locInt == 0) ? Location.WareHouse : Location.Store;

        Date expDate = promptDate("Expiration Date (yyyy-MM-dd): ");

        if (expDate.before(new Date())) {
            System.out.println("Cannot add item: expiration date is in the past.");
            return;
        }

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
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
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
        if (newMin < 0) {
            System.out.println("Min quantity cannot be negative.");
            return;
        }
        p.setMinQuantity(newMin);
        System.out.println("Min Quantity updated to " + p.getMinQuantity());
    }

    private static boolean isValidPercentage(double perc) {
        if (perc < 0 || perc > 100) {
            System.out.println("Percentage must be between 0 and 100.");
            return false;
        }
        return true;
    }

    private static boolean isValidId(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }
        return true;
    }

    private static void addSupplierDiscount() {
        System.out.println("=== Add Supplier Discount ===");
        String name = promptString("Product Name: ");
        Product p = productsView.getProductByName(name);
        if (p == null) {
            System.out.println("Product does not exist");
            return;
        }
        String did  = promptString("Discount ID: ");
        if (!isValidId(did)) {
            return;
        }
        String sup  = promptString("Supplier Name: ");
        double perc = promptDouble("Discount Percentage: ");

        if (!isValidPercentage(perc)) {
            return;
        }

        LocalDate sd, ed;
        try {
            sd = LocalDate.parse(promptString("Start Date (yyyy-MM-dd): "));
            ed = LocalDate.parse(promptString("End Date   (yyyy-MM-dd): "));
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return;
        }
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
        String pid = promptString("Promotion ID: ");
        if (!isValidId(pid)) {
            return;
        }
        double perc = promptDouble("Discount Percentage: ");
        if (!isValidPercentage(perc)) {
            return;
        }
        LocalDate sd, ed;
        try {
            sd = LocalDate.parse(promptString("Start Date (yyyy-MM-dd): "));
            ed = LocalDate.parse(promptString("End Date   (yyyy-MM-dd): "));
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return;
        }
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

    private static void markExpired() {
        System.out.println("=== Mark item expired ===");
        String name = promptString("Item Name: ");
        Item item = itemsView.getItemByName(name);
        if (item == null) {
            System.out.println("Item not found");
            return;
        }
        item.setExpirationDate(new Date(System.currentTimeMillis() - 1000));
        System.out.println("Item marked as expired.");
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
